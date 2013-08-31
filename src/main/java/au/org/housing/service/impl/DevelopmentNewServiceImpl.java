package au.org.housing.service.impl;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.PropertyImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.SortBy;
import org.geotools.filter.SortBy2;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.SortOrder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.helpers.NamespaceSupport;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.config.OutPutLayerConfig;
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopNew;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.DevelopmentNewService;
import au.org.housing.service.DevelopmentPotentialService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.TemporaryFileManager;
import au.org.housing.utilities.Zip;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.util.CollectionUtil.Function;


/**
 * Implementation for handling New Development analysis.
 *
 * @author Ali
 * @version 1.0
 *
 */

@Service
@Scope("session")   
public class DevelopmentNewServiceImpl implements DevelopmentNewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopmentPotentialServiceImpl.class);

    @Autowired    ParameterDevelopNew parameter;
    @Autowired     PostGISService postGISService;
    @Autowired     GeoServerConfig geoServerConfig;
    @Autowired    GeoServerService geoServerService;
    @Autowired    private ValidationService validationService;
    @Autowired    private ExportService exportService;
    @Autowired    UnionService unionService;
    @Autowired     FeatureBuilder featureBuilder;
    @Autowired    InputLayersConfig inputLayersConfig;
    @Autowired    OutPutLayerConfig outPutLayerConfig;

    SimpleFeatureSource propertyFc = null;
    SimpleFeatureSource planCodeListFc = null;
    SimpleFeatureSource planOverlayFc = null;
    SimpleFeatureSource zonecodesFc = null;

    FilterFactory2 ff;
    Filter propertyFilter;
    Filter lgaFilter;
    Query propertyQuery ;
    SimpleFeatureTypeBuilder stb;
    SimpleFeatureBuilder sfb;
    SimpleFeatureType newFeatureType;
//    ReferencedEnvelope envelope;
    //    String layerName;
    Filter ownershipFilter;
    Filter landUseFilter;
    Map<String, Object> overlayMap;
    List<Filter> propertyFilters;
    List<Filter> landUseFilters;
    Geometry bufferAllParams = null;
    SimpleFeatureCollection properties;
    boolean dropCreateSchema ;   
    Geometry inundationsUnion = null;
    Integer propertyOverlaysNum;   
    boolean anyOverlayChecked ;
    SimpleFeatureCollection planCodeList = null;
    SimpleFeatureCollection tblZoneCodes = null;
    File newFile;
    Map<String, Object> outputLayer;    
    
    String jsonOut="";
    String jsonOutChart="";
    String tableOut="";
   
    String step;
    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    String oldRules ;
     

    public boolean analyse(String username, HttpSession session) throws SQLException, Exception{
        step = "start";
        ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        outputLayer = new HashMap<String, Object>();

        layersValidation();
        //SimpleFeatureCollection pparsCollection = pparseFilter();
        //SimpleFeatureCollection propertyCollection = propertyFilter(pparsCollection);               
        SimpleFeatureCollection NewCollection = SLAFilter();
       
        String workspace = geoServerConfig.getGsWorkspace() + "_" + username;
        //        String dataStore = geoServerConfig.getGsAssessmentDatastore();
        String dataStore = geoServerConfig.getGsDataStore();
        String layer = geoServerConfig.getGsNewLayer();
        String style = geoServerConfig.getGsNewStyle();
        File newDirectory =  TemporaryFileManager.getNew(session, username,  layer , "",true);
        newFile = new File(newDirectory.getAbsolutePath() +"/" + layer + ".shp");

        exportToShapeFile(NewCollection, username, layer, session);
        geoServerService.getGeoServer(workspace);
        geoServerService.publishNewStyle(style);
        geoServerService.publishToGeoServer( workspace , dataStore , layer, style, newFile );       

        outputLayer.put("layerName", layer);
        outputLayer.put("workspace", workspace);
        return true;
    }
    
    public boolean analyseTable(String username, HttpSession session) throws SQLException, Exception{
    	
        layersValidation();
        SLAFilterTable();
        return true;
    }

    private SimpleFeatureCollection SLAFilter() throws PSQLException, IOException, HousingException{
        SimpleFeatureCollection newCollection;

        SimpleFeatureSource slaFC = postGISService.getFeatureSource(inputLayersConfig.getsla_housing());
       
        List<Filter> slaFilters = new ArrayList<Filter>();
        Filter slaFilter = null;
        Filter filter;
        /*
        if (parameter.getSelectedLGAs3()!= null && !parameter.getSelectedLGAs3().isEmpty()){
            for (String slaCode : parameter.getSelectedLGAs3()) {
                filter = ff.equals(ff.property(inputLayersConfig.getPropertySlaCode()),ff.literal(slaCode));
                filter = ff.equals(ff.property(inputLayersConfig.getPropertySlaCode()),ff.literal("205204654"));
                slaFilters.add(filter);               
           
            }
            slaFilter = ff.or(slaFilters);
        }
        //newCollection = slaFC.getFeatures(slaFilter);
        */       

       
        newCollection = slaFC.getFeatures();
       
        double priority_houseprice_income_ratio = parameter.getHouseprice_income_ratio();
        double priority_rental_income_ratio = parameter.getRental_income_ratio();
        double priority_interest_mortgage = parameter.getInterest_mortgage();
        double priority_social_private_rented = parameter.getSocial_private_rented();
        double priority_homeownership_products = parameter.getHomeownership_products();
        double priority_crime_sla_sd = parameter.getCrime_sla_sd();
        double priority_count_employment = parameter.getCount_employment();	
        double priority_public_transport = parameter.getPublic_transport();
        double priority_count_educationcentre = parameter.getCount_educationcentre();
        double priority_commercial_count = parameter.getCommercial_count();
        double priority_count_healthservice = parameter.getCount_healthservice();
        double priority_childcare_count = parameter.getChildcare_count();
        double priority_leisure_count = parameter.getLeisure_count();
        double priority_greenpublicspace_area = parameter.getGreenpublicspace_area();
        double priority_housing_quality_median = parameter.getHousing_quality_median();	
        double priority_energy_efficiency = parameter.getEnergy_efficiency(); 
        double priority_landfill_count = parameter.getLandfill_count();
        double priority_desirability = parameter.getDesirability();
        double priority_sum_prop_decile123 = parameter.getSum_prop_decile123();
        double priority_environmental_problem = parameter.getEnvironmental_problem();

        
        
        
        double priority_sum = priority_houseprice_income_ratio + priority_rental_income_ratio 
                              + priority_interest_mortgage + priority_social_private_rented
                              + priority_homeownership_products + priority_crime_sla_sd 
                              + priority_count_employment + priority_public_transport
                              + priority_count_educationcentre + priority_commercial_count
                              + priority_count_healthservice + priority_childcare_count
                              + priority_leisure_count + priority_greenpublicspace_area
                              + priority_housing_quality_median + priority_energy_efficiency
                              + priority_landfill_count + priority_desirability 
                              + priority_sum_prop_decile123+ priority_environmental_problem;
                              
                              
        //System.out.println("priority_sum:" + priority_sum);
        
        double q_houseprice_income_ratio = (priority_houseprice_income_ratio/priority_sum)* 100;
        double q_rental_income_ratio = (priority_rental_income_ratio/priority_sum)* 100;
        double q_interest_mortgage = (priority_interest_mortgage/priority_sum)* 100;
        double q_social_private_rented = (priority_social_private_rented/priority_sum)* 100;
        double q_homeownership_products = (priority_homeownership_products/priority_sum)* 100;
        double q_crime_sla_sd = (priority_crime_sla_sd/priority_sum)* 100;
        double q_count_employment = (priority_count_employment/priority_sum)* 100;	
        double q_public_transport = (priority_public_transport/priority_sum)* 100;
        double q_count_educationcentre = (priority_count_educationcentre/priority_sum)* 100;
        double q_commercial_count = (priority_commercial_count/priority_sum)* 100;
        double q_count_healthservice = (priority_count_healthservice/priority_sum)* 100;
        double q_childcare_count = (priority_childcare_count/priority_sum)* 100;
        double q_leisure_count =(priority_leisure_count/priority_sum)* 100; 
        double q_greenpublicspace_area = (priority_greenpublicspace_area/priority_sum)* 100;
        double q_housing_quality_median =(priority_housing_quality_median/priority_sum)* 100;	
        double q_energy_efficiency = (priority_energy_efficiency/priority_sum)* 100; 
        double q_landfill_count = (priority_landfill_count/priority_sum)* 100;
        double q_desirability = (priority_desirability/priority_sum)* 100;
        double q_sum_prop_decile123 = (priority_sum_prop_decile123/priority_sum)* 100;
        double q_environmental_problem = (priority_environmental_problem/priority_sum)* 100;

        
        //System.out.println("q_houseprice_income_ratio:" + q_houseprice_income_ratio);
        //System.out.println("priority_houseprice_income_ratio:" + priority_houseprice_income_ratio);
       
        double Sumof_sumPlus=0;
        double Sumof_sumMinus=0;
        double Sumof_sumProductMinus=0;
        
        double Max_Of_Qj=0;
       
        
       
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        org.opengis.filter.expression.Function houseprice_income_ratio =  ff.function("Collection_Sum", ff.property("houseprice_income_ratio"));
        org.opengis.filter.expression.Function rental_income_ratio =  ff.function("Collection_Sum", ff.property("rental_income_ratio"));
        org.opengis.filter.expression.Function interest_mortgage = ff.function("Collection_Sum", ff.property("interest_mortgage_rate"));
        org.opengis.filter.expression.Function social_private_rented = ff.function("Collection_Sum", ff.property("social_private_rented"));
        org.opengis.filter.expression.Function homeownership_products = ff.function("Collection_Sum", ff.property("homeownership_product"));
        org.opengis.filter.expression.Function crime_sla_sd = ff.function("Collection_Sum", ff.property("crime_cluster"));
        org.opengis.filter.expression.Function count_employment = ff.function("Collection_Sum", ff.property("employment_count"));	
        org.opengis.filter.expression.Function public_transport = ff.function("Collection_Sum", ff.property("public_transport_station_route"));
        org.opengis.filter.expression.Function count_educationcentre = ff.function("Collection_Sum", ff.property("educationcentre_count"));
        org.opengis.filter.expression.Function commercial_count = ff.function("Collection_Sum", ff.property("shop_count"));
        org.opengis.filter.expression.Function count_healthservice = ff.function("Collection_Sum", ff.property("healthservice_count"));
        org.opengis.filter.expression.Function childcare_count = ff.function("Collection_Sum", ff.property("childcare_count"));
        org.opengis.filter.expression.Function leisure_count =ff.function("Collection_Sum", ff.property("leisure_count")); 
        org.opengis.filter.expression.Function greenpublicspace_area = ff.function("Collection_Sum", ff.property("greenpublicspace_area"));
        org.opengis.filter.expression.Function housing_quality_median =ff.function("Collection_Sum", ff.property("quality_housing_cluster"));	
        org.opengis.filter.expression.Function energy_efficiency = ff.function("Collection_Sum", ff.property("floorarea_median")); 
        org.opengis.filter.expression.Function landfill_count = ff.function("Collection_Sum", ff.property("landfill_count"));
        org.opengis.filter.expression.Function desirability = ff.function("Collection_Sum", ff.property("desirability_neighbourhood_area"));
        org.opengis.filter.expression.Function sum_prop_decile123 = ff.function("Collection_Sum", ff.property("deprivation_cluster"));
        org.opengis.filter.expression.Function environmental_problem = ff.function("Collection_Sum", ff.property("environmental_problem"));        
        
       

        double sumindb_houseprice_income_ratio = Double.parseDouble(houseprice_income_ratio.evaluate( newCollection ).toString());
        double sumindb_rental_income_ratio = Double.parseDouble(rental_income_ratio.evaluate( newCollection ).toString());       
        double sumindb_interest_mortgage = Double.parseDouble(interest_mortgage.evaluate( newCollection ).toString());
        double sumindb_social_private_rented = Double.parseDouble(social_private_rented.evaluate( newCollection ).toString());
        double sumindb_homeownership_products = Double.parseDouble(homeownership_products.evaluate( newCollection ).toString());
        double sumindb_crime_sla_sd = Double.parseDouble(crime_sla_sd.evaluate( newCollection ).toString());
        double sumindb_count_employment = Double.parseDouble(count_employment.evaluate( newCollection ).toString());	
        double sumindb_public_transport = Double.parseDouble(public_transport.evaluate( newCollection ).toString());
        double sumindb_count_educationcentre = Double.parseDouble(count_educationcentre.evaluate( newCollection ).toString());
        double sumindb_commercial_count = Double.parseDouble(commercial_count.evaluate( newCollection ).toString());
        double sumindb_count_healthservice = Double.parseDouble(count_healthservice.evaluate( newCollection ).toString());
        double sumindb_childcare_count = Double.parseDouble(childcare_count.evaluate( newCollection ).toString());
        double sumindb_leisure_count =Double.parseDouble(leisure_count.evaluate( newCollection ).toString()); 
        double sumindb_greenpublicspace_area = Double.parseDouble(greenpublicspace_area.evaluate( newCollection ).toString());
        double sumindb_housing_quality_median =Double.parseDouble(housing_quality_median.evaluate( newCollection ).toString());	
        double sumindb_energy_efficiency = Double.parseDouble(energy_efficiency.evaluate( newCollection ).toString()); 
        double sumindb_landfill_count = Double.parseDouble(landfill_count.evaluate( newCollection ).toString());
        double sumindb_desirability = Double.parseDouble(desirability.evaluate( newCollection ).toString());
        double sumindb_sum_prop_decile123 = Double.parseDouble(sum_prop_decile123.evaluate( newCollection ).toString());
        double sumindb_environmental_problem = Double.parseDouble(environmental_problem.evaluate( newCollection ).toString()); 
        
        
        //System.out.println("sumindb_houseprice_income_ratio:" + sumindb_houseprice_income_ratio);
        //System.out.println(" sumindb_rental_income_ratio" +sumindb_rental_income_ratio);       
        //System.out.println(" sumindb_interest_mortgage " +sumindb_interest_mortgage);
        //System.out.println(" sumindb_social_private_rented " + sumindb_social_private_rented);
        //System.out.println(" sumindb_homeownership_products " +sumindb_homeownership_products);
        //System.out.println(" sumindb_crime_sla_sd " +sumindb_crime_sla_sd);
        //System.out.println(" sumindb_count_employment " + sumindb_count_employment);	
        //System.out.println(" sumindb_public_transport " +sumindb_public_transport);
        //System.out.println(" sumindb_count_educationcentre " + sumindb_count_educationcentre );
        //System.out.println(" sumindb_commercial_count " + sumindb_commercial_count );
        //System.out.println(" sumindb_count_healthservice " + sumindb_count_healthservice);
        //System.out.println(" sumindb_childcare_count " + sumindb_childcare_count);
        //System.out.println(" sumindb_leisure_count " + sumindb_leisure_count ); 
        //System.out.println(" sumindb_greenpublicspace_area " + sumindb_greenpublicspace_area );
        //System.out.println(" sumindb_housing_quality_median " + sumindb_housing_quality_median );	
        //System.out.println(" sumindb_energy_efficiency " + sumindb_energy_efficiency ); 
        //System.out.println(" sumindb_landfill_count " + sumindb_landfill_count );
        //System.out.println(" sumindb_desirability " + sumindb_desirability);
        //System.out.println(" sumindb_sum_prop_decile123 " + sumindb_sum_prop_decile123 );
        //System.out.println(" sumindb_environmental_problem " + sumindb_environmental_problem);         
        

         SimpleFeatureCollection tempCollectionOut = FeatureCollections.newCollection(); 
           
 
              
            //System.out.println(newCollection.size());
            int cnt =0;
            SimpleFeatureIterator iterator1 = newCollection.features();
            try
            {
                while (iterator1.hasNext())
                {
   
                    cnt+=1;
                    ////System.out.println(cnt);
                    SimpleFeature featureIn = iterator1.next();   
               
                 
                  
                   SimpleFeature featureOut = null;             
               
                   SimpleFeatureTypeBuilder TypeBuilder = new SimpleFeatureTypeBuilder();
                   TypeBuilder.setName("Location");         
                   SimpleFeatureType featureType = slaFC.getFeatures().features().next().getFeatureType();              
                   TypeBuilder.addAll(featureType.getAttributeDescriptors());   
                  
                    TypeBuilder.add("D_houseprice_income_ratio", Double.class);    
                    TypeBuilder.add("D_rental_income_ratio", Double.class);
                    TypeBuilder.add("D_interest_mortgage", Double.class);
                    TypeBuilder.add("D_social_private_rented", Double.class);
                    TypeBuilder.add("D_homeownership_products", Double.class);
                    TypeBuilder.add("D_crime_sla_sd", Double.class);
                    TypeBuilder.add("D_count_employment", Double.class);	
                    TypeBuilder.add("D_public_transport", Double.class);
                    TypeBuilder.add("D_count_educationcentre", Double.class);
                    TypeBuilder.add("D_commercial_count", Double.class);
                    TypeBuilder.add("D_count_healthservice", Double.class);
                    TypeBuilder.add("D_childcare_count", Double.class);
                    TypeBuilder.add("D_leisure_count", Double.class); 
                    TypeBuilder.add("D_greenpublicspace_area", Double.class);
                    TypeBuilder.add("D_housing_quality_median", Double.class);	
                    TypeBuilder.add("D_energy_efficiency", Double.class); 
                    TypeBuilder.add("D_landfill_count", Double.class);
                    TypeBuilder.add("D_desirability", Double.class);
                    TypeBuilder.add("D_sum_prop_decile123", Double.class);
                    TypeBuilder.add("D_environmental_problem", Double.class);                     
                    
                    
                   
                    TypeBuilder.add("SumPlus", Double.class);
                    TypeBuilder.add("SumMinus", Double.class);
                   
                    TypeBuilder.add("Qj", Double.class);
                    TypeBuilder.add("Priority", Double.class);
                    TypeBuilder.add("Percentage", Double.class);
                   
                   CoordinateReferenceSystem sourceCRS = featureType.getCoordinateReferenceSystem();
                   TypeBuilder.setCRS(sourceCRS);
                  
   
                   SimpleFeatureType featureTypeNew= null;
                   featureTypeNew = TypeBuilder.buildFeatureType();
                   SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureTypeNew);
                   builder.init(featureIn);
                 
                   featureOut = builder.buildFeature(featureIn.getID());
                             
                   featureOut.setAttribute("D_houseprice_income_ratio",(q_houseprice_income_ratio/sumindb_houseprice_income_ratio) * Double.parseDouble(featureIn.getAttribute("houseprice_income_ratio").toString()));
                   featureOut.setAttribute("D_rental_income_ratio",(q_rental_income_ratio/sumindb_rental_income_ratio) * Double.parseDouble(featureIn.getAttribute("rental_income_ratio").toString()));                                      
                   featureOut.setAttribute("D_interest_mortgage",(q_interest_mortgage/sumindb_interest_mortgage) * Double.parseDouble(featureIn.getAttribute("interest_mortgage_rate").toString()));
                   featureOut.setAttribute("D_social_private_rented",(q_social_private_rented/sumindb_social_private_rented) * Double.parseDouble(featureIn.getAttribute("social_private_rented").toString()));
                   featureOut.setAttribute("D_homeownership_products",(q_homeownership_products/sumindb_homeownership_products) * Double.parseDouble(featureIn.getAttribute("homeownership_product").toString()));
                   featureOut.setAttribute("D_crime_sla_sd",(q_crime_sla_sd/sumindb_crime_sla_sd) * Double.parseDouble(featureIn.getAttribute("crime_cluster").toString()));
                   featureOut.setAttribute("D_count_employment",(q_count_employment/sumindb_count_employment) * Double.parseDouble(featureIn.getAttribute("employment_count").toString()));	
                   featureOut.setAttribute("D_public_transport",(q_public_transport/sumindb_public_transport) * Double.parseDouble(featureIn.getAttribute("public_transport_station_route").toString()));
                   featureOut.setAttribute("D_count_educationcentre",(q_count_educationcentre/sumindb_count_educationcentre) * Double.parseDouble(featureIn.getAttribute("educationcentre_count").toString()));
                   featureOut.setAttribute("D_commercial_count",(q_commercial_count/sumindb_commercial_count) * Double.parseDouble(featureIn.getAttribute("shop_count").toString()));
                   featureOut.setAttribute("D_count_healthservice",(q_count_healthservice/sumindb_count_healthservice) * Double.parseDouble(featureIn.getAttribute("healthservice_count").toString()));
                   featureOut.setAttribute("D_childcare_count",(q_childcare_count/sumindb_childcare_count) * Double.parseDouble(featureIn.getAttribute("childcare_count").toString()));
                   featureOut.setAttribute("D_leisure_count",(q_leisure_count/sumindb_leisure_count) * Double.parseDouble(featureIn.getAttribute("leisure_count").toString())); 
                   featureOut.setAttribute("D_greenpublicspace_area",(q_greenpublicspace_area/sumindb_greenpublicspace_area) * Double.parseDouble(featureIn.getAttribute("greenpublicspace_area").toString()));
                   featureOut.setAttribute("D_housing_quality_median",(q_housing_quality_median/sumindb_housing_quality_median) * Double.parseDouble(featureIn.getAttribute("quality_housing_cluster").toString()));	
                   featureOut.setAttribute("D_energy_efficiency",(q_energy_efficiency/sumindb_energy_efficiency) * Double.parseDouble(featureIn.getAttribute("floorarea_median").toString())); 
                   featureOut.setAttribute("D_landfill_count",(q_landfill_count/sumindb_landfill_count) * Double.parseDouble(featureIn.getAttribute("landfill_count").toString()));
                   featureOut.setAttribute("D_desirability",(q_desirability/sumindb_desirability) * Double.parseDouble(featureIn.getAttribute("desirability_neighbourhood_area").toString()));
                   featureOut.setAttribute("D_sum_prop_decile123",(q_sum_prop_decile123/sumindb_sum_prop_decile123) * Double.parseDouble(featureIn.getAttribute("deprivation_cluster").toString()));
                   featureOut.setAttribute("D_environmental_problem",(q_environmental_problem/sumindb_environmental_problem) * Double.parseDouble(featureIn.getAttribute("environmental_problem").toString())); 
                   
                   
                  
                   double SumPlus= Double.parseDouble(featureOut.getAttribute("D_interest_mortgage").toString())+ Double.parseDouble(featureOut.getAttribute("D_social_private_rented").toString())
               		             + Double.parseDouble(featureOut.getAttribute("D_homeownership_products").toString())+ Double.parseDouble(featureOut.getAttribute("D_count_employment").toString())+
               		             + Double.parseDouble(featureOut.getAttribute("D_public_transport").toString())+ Double.parseDouble(featureOut.getAttribute("D_count_educationcentre").toString())+
               		             + Double.parseDouble(featureOut.getAttribute("D_commercial_count").toString())+ Double.parseDouble(featureOut.getAttribute("D_count_healthservice").toString())+
               		             + Double.parseDouble(featureOut.getAttribute("D_childcare_count").toString())+ Double.parseDouble(featureOut.getAttribute("D_leisure_count").toString())+
               		             + Double.parseDouble(featureOut.getAttribute("D_greenpublicspace_area").toString())+ Double.parseDouble(featureOut.getAttribute("D_housing_quality_median").toString())+
               		             + Double.parseDouble(featureOut.getAttribute("D_energy_efficiency").toString())+ Double.parseDouble(featureOut.getAttribute("D_desirability").toString());
                   
                   double SumMinus= Double.parseDouble(featureOut.getAttribute("D_houseprice_income_ratio").toString())+ Double.parseDouble(featureOut.getAttribute("D_rental_income_ratio").toString())
                		          + Double.parseDouble(featureOut.getAttribute("D_crime_sla_sd").toString())+ Double.parseDouble(featureOut.getAttribute("D_landfill_count").toString())
                		          + Double.parseDouble(featureOut.getAttribute("D_sum_prop_decile123").toString())+ Double.parseDouble(featureOut.getAttribute("D_environmental_problem").toString());
                  
                   featureOut.setAttribute("SumPlus", SumPlus);
                   featureOut.setAttribute("SumMinus", SumMinus);
                  
                   Sumof_sumPlus = Sumof_sumPlus + SumPlus;
                   Sumof_sumMinus = Sumof_sumMinus + SumMinus;
                   Sumof_sumProductMinus = Sumof_sumProductMinus + (1/SumMinus);
                  
                   ////System.out.println(q_houseprice_income_ratio + "*" +sumindb_houseprice_income_ratio+ "/"  +featureIn.getAttribute("houseprice_income_ratio")+ "=" +featureOut.getAttribute("D_houseprice_income_ratio"));
                   //System.out.println(featureIn.getAttribute("sla_name") + " : SumPlus" + ":" + SumPlus);
                   //System.out.println(featureIn.getAttribute("sla_name") + " : SumMinus" + ":" + SumMinus);
                   
                   
                    tempCollectionOut.add(featureOut);
   
               }
           }
            finally {
                   iterator1.close();
            }
           
            
            //System.out.println("Sumof_sumPlus" + ":" + Sumof_sumPlus);
            //System.out.println("Sumof_sumMinus" + ":" + Sumof_sumMinus);
            //System.out.println("Sumof_sumProductMinus" + ":" + Sumof_sumProductMinus);
           
            Map<String, String> unsortMap = new HashMap<String, String>();
           
            cnt = 0;
            SimpleFeatureIterator iteratorTemp = tempCollectionOut.features();
            try
            {
                while (iteratorTemp.hasNext())
                {
   
                   cnt+=1;
                   ////System.out.println(cnt);
                   SimpleFeature featureOut = iteratorTemp.next();
              
                   double Qj = Double.parseDouble(featureOut.getAttribute("SumPlus").toString()) + ((Sumof_sumMinus)/(Double.parseDouble(featureOut.getAttribute("SumMinus").toString()) * Sumof_sumProductMinus));
                   featureOut.setAttribute("Qj", Qj);              
                   //System.out.println(featureOut.getAttribute("sla_name") + "  Qj: "+ Qj);
                  
                 
                   unsortMap.put(featureOut.getAttribute("sla_main").toString(), featureOut.getAttribute("Qj").toString());
                 
               }
           }
            finally {
                iteratorTemp.close();
            }
           
            Map<String, String> sortedMap = sortByComparator(unsortMap);
            ArrayList<String> keys = new ArrayList<String>(sortedMap.keySet());
           
            cnt = 0;
            SimpleFeatureIterator iteratorTempnew = tempCollectionOut.features();
            Map<String, String> newMap = new HashMap<String, String>();

                Map<String, String> mapnew = new HashMap<String, String>();
                cnt = tempCollectionOut.size();
                for (Map.Entry entry : sortedMap.entrySet() ) {
                   
                    mapnew.put(entry.getKey().toString() , String.valueOf(cnt));
                    cnt = cnt -1;
                    ////System.out.println("Key : " + entry.getKey()+ " Value : " + entry.getValue());
                }
       
                  cnt =0;
                  
                 SimpleFeatureIterator iteratorTempsort = tempCollectionOut.features();
                    try
                    {
                        while (iteratorTempsort.hasNext())
                        {
           
                           cnt+=1;
                           ////System.out.println(cnt);
                           SimpleFeature featureOut = iteratorTempsort.next();
                           Object value = mapnew.get(featureOut.getAttribute("sla_main").toString());
                           featureOut.setAttribute("Priority", value);
                           if (Integer.parseInt((String) value) ==1)
                           {
                        	   Max_Of_Qj = Double.parseDouble(featureOut.getAttribute("Qj").toString());    
                           }
                           
                           ////System.out.println(featureOut.getAttribute("sla_name").toString() + " Qj: " + featureOut.getAttribute("Qj").toString() + " Priority:" + featureOut.getAttribute("Priority").toString());
                       }
                   }
                    finally {
                        iteratorTempsort.close();
                    }   
                    
                    cnt =0;
                    SimpleFeatureIterator iteratorTempsortnew = tempCollectionOut.features();
                    jsonOut ="{ \"success\": true, \"chartdata\": [";
                    jsonOutChart ="{ \"success\": true, \"chartdata\": [";
                    tableOut ="sla_main, rank";
                    
                     
                    	       
                    	
                       try
                       {
                           while (iteratorTempsortnew.hasNext())
                           {
              
                              cnt+=1;
                              ////System.out.println(cnt);
                              SimpleFeature featureOut = iteratorTempsortnew.next();
                              Object value = mapnew.get(featureOut.getAttribute("sla_main").toString());
                              featureOut.setAttribute("Priority", value);
                              if (Integer.parseInt((String) value) ==1)
                              {
                           	       Max_Of_Qj = Double.parseDouble(featureOut.getAttribute("Qj").toString());    
                              }
                              
                              double rankchart = 81 - Double.parseDouble(featureOut.getAttribute("Priority").toString());
                              double rank = Double.parseDouble(featureOut.getAttribute("Priority").toString());
                              
                              jsonOut = jsonOut+"{ \"rank\": " +   rank + ", \"sla_name\": \"" + featureOut.getAttribute("sla_name").toString() +"\" },";                              
                              jsonOutChart = jsonOutChart+"{ \"rank\": " +   rankchart + ", \"sla_name\": \"" + featureOut.getAttribute("sla_name").toString() +"\" },";
                              
                              //tableOut = tableOut + "\r\n" + featureOut.getAttribute("sla_name").toString() + "," + featureOut.getAttribute("Priority").toString();
                              tableOut = tableOut + "\r\n" + featureOut.getAttribute("sla_name").toString() + "," + rank;
                             
                              
                              featureOut.setAttribute("Percentage",(Double.parseDouble(featureOut.getAttribute("Qj").toString())/ Max_Of_Qj) * 100);
                              ////System.out.println("Qj:"+featureOut.getAttribute("Qj").toString() + " Priortity: " + featureOut.getAttribute("Priority").toString() + " Percentage: " + featureOut.getAttribute("Percentage").toString());
                              //System.out.println(featureOut.getAttribute("sla_name").toString() + " Qj: " + featureOut.getAttribute("Qj").toString() + " Priority:" + featureOut.getAttribute("Priority").toString()  + " Percentage: " + featureOut.getAttribute("Percentage").toString());
                          }
                           jsonOut = jsonOut.substring(0,jsonOut.length()-1)+ "]}";
                           jsonOutChart = jsonOutChart.substring(0,jsonOutChart.length()-1)+ "]}";
                           
                      }
                       finally {
                           iteratorTempsortnew.close();
                       }                       


        if (tempCollectionOut == null || tempCollectionOut.isEmpty()){
            throw new HousingException(Messages._NO_FEATURE);           
        }
        //System.out.println("tempCollectionOut.size()" + tempCollectionOut.size());
        ReferencedEnvelope envelope = tempCollectionOut.getBounds();
        outputLayer.put("maxX", envelope.getMaxX());
        outputLayer.put("minX", envelope.getMinX());
        outputLayer.put("maxY", envelope.getMaxY());
        outputLayer.put("minY", envelope.getMinY());
       
          
        return tempCollectionOut;
    }
   
    
    private void SLAFilterTable() throws PSQLException, IOException, HousingException{
        SimpleFeatureCollection newCollection;

        SimpleFeatureSource slaFC = postGISService.getFeatureSource(inputLayersConfig.getsla_housing());
       
  
        newCollection = slaFC.getFeatures();
        
        boolean houseprice_income_ratio = parameter.isChk_houseprice_income_ratio(); 
        boolean rental_income_ratio = parameter.isChk_rental_income_ratio();
        boolean interest_mortgage = parameter.isChk_interest_mortgage();
        boolean social_private_rented = parameter.isChk_social_private_rented();
        boolean homeownership_products = parameter.isChk_homeownership_products();
        boolean crime_sla_sd = parameter.isChk_crime_sla_sd();
        boolean count_employment = parameter.isChk_count_employment();	
        boolean public_transport = parameter.isChk_public_transport();
        boolean count_educationcentre = parameter.isChk_count_educationcentre();
        boolean commercial_count = parameter.isChk_commercial_count();
        boolean count_healthservice = parameter.isChk_count_healthservice();
        boolean childcare_count = parameter.isChk_childcare_count();
        boolean leisure_count = parameter.isChk_leisure_count();
        boolean greenpublicspace_area = parameter.isChk_greenpublicspace_area();
        boolean housing_quality_median = parameter.isChk_housing_quality_median();	
        boolean energy_efficiency = parameter.isChk_energy_efficiency(); 
        boolean landfill_count = parameter.isChk_landfill_count();
        boolean desirability = parameter.isChk_desirability();
        boolean sum_prop_decile123 = parameter.isChk_sum_prop_decile123();
        boolean environmental_problem = parameter.isChk_environmental_problem();
     
              
            //System.out.println(newCollection.size());
            int cnt =0;
            tableOut ="sla_name,";
            jsonOut ="{ \"success\": true, \"chartdata\": [";
            
            if(houseprice_income_ratio == true)
            	tableOut += "houseprice_income_ratio" + ",";
            if(rental_income_ratio == true)
            	tableOut += "rental_income_ratio" + ",";                                      
            if(interest_mortgage == true)
            	tableOut += "interest_mortgage_rate" + ",";
            if(social_private_rented == true)
            	tableOut += "social_private_rented" + ",";
            if(homeownership_products == true)
            	tableOut += "homeownership_product" + ",";
            if(crime_sla_sd == true)
            	tableOut += "crime_cluster" + ",";
            if(count_employment == true)
            	tableOut += "employment_count" + ",";	
            if(public_transport == true)
            	tableOut += "public_transport_station_route" + ",";
            if(count_educationcentre == true)
            	tableOut += "educationcentre_count" + ",";
            if(commercial_count == true)
            	tableOut += "shop_count" + ",";
            if(count_healthservice == true)
            	tableOut += "healthservice_count" + ",";
            if(childcare_count == true)
            	tableOut += "childcare_count" + ",";
            if(leisure_count == true)
            	tableOut += "leisure_count" + ","; 
            if(greenpublicspace_area == true)
            	tableOut += "greenpublicspace_area" + ",";
            if(housing_quality_median == true)
            	tableOut += "quality_housing_cluster" + ",";	
            if(energy_efficiency == true)
            	tableOut += "floorarea_median" + ","; 
            if(landfill_count == true)
            	tableOut += "landfill_count" + ",";
            if(desirability == true)
            	tableOut += "desirability_neighbourhood_area" + ",";
            if(sum_prop_decile123 == true)
            	tableOut += "deprivation_cluster" + ",";
            if(environmental_problem == true)
            	tableOut += "environmental_problem" + "\r\n";   
            
            SimpleFeatureIterator iterator1 = newCollection.features();
            try
            {
                while (iterator1.hasNext())
                {
   
                    cnt+=1;
                    ////System.out.println(cnt);
                    SimpleFeature featureIn = iterator1.next();
                    
                    jsonOut = jsonOut+"{\"sla_name\": \"" + featureIn.getAttribute("sla_name").toString() +"\","; 
                    tableOut += featureIn.getAttribute("sla_name").toString()  + ",";
                    if(houseprice_income_ratio == true)
                    {
                    	  tableOut += featureIn.getAttribute("houseprice_income_ratio").toString() + ",";
                          jsonOut = jsonOut+"\"houseprice_income_ratio\": " + featureIn.getAttribute("houseprice_income_ratio").toString() + ",";   
                    }
                    if(rental_income_ratio == true)
                    {
                    	tableOut += featureIn.getAttribute("rental_income_ratio").toString() + ",";
                        jsonOut = jsonOut+"\"rental_income_ratio\": " + featureIn.getAttribute("rental_income_ratio").toString() + ",";
                    }
                    if(interest_mortgage == true)
                    {
                    	tableOut += featureIn.getAttribute("interest_mortgage_rate").toString() + ",";
                    	jsonOut = jsonOut+"\"interest_mortgage_rate\": " + featureIn.getAttribute("interest_mortgage_rate").toString() + ",";
                    }
                    if(social_private_rented == true)
                    {
                    	tableOut += featureIn.getAttribute("social_private_rented").toString() + ",";
                    	jsonOut = jsonOut+"\"social_private_rented\": " + featureIn.getAttribute("social_private_rented").toString() + ",";
                    }
                    if(homeownership_products == true)
                    {
                    	tableOut += featureIn.getAttribute("homeownership_product").toString() + ",";
                    	jsonOut = jsonOut+"\"homeownership_product\": " + featureIn.getAttribute("homeownership_product").toString() + ",";
                    }
                    if(crime_sla_sd == true)
                    {
                    	tableOut += featureIn.getAttribute("crime_cluster").toString() + ",";
                    	jsonOut = jsonOut+"\"crime_cluster\": " + featureIn.getAttribute("crime_cluster").toString() + ",";
                    }
                    if(count_employment == true)
                    {
                    	tableOut += featureIn.getAttribute("employment_count").toString() + ",";
                    	jsonOut = jsonOut+"\"employment_count\": " + featureIn.getAttribute("employment_count").toString() + ",";
                    }
                    if(public_transport == true)
                    {
                    	tableOut += featureIn.getAttribute("public_transport_station_route").toString() + ",";
                    	jsonOut = jsonOut+"\"public_transport_station_route\": " + featureIn.getAttribute("public_transport_station_route").toString() + ",";
                    }
                    if(count_educationcentre == true)
                    {
                    	tableOut += featureIn.getAttribute("educationcentre_count").toString() + ",";
                    	jsonOut = jsonOut+"\"educationcentre_count\": " + featureIn.getAttribute("educationcentre_count").toString() + ",";
                    }
                    if(commercial_count == true)
                    {
                    	tableOut += featureIn.getAttribute("shop_count").toString() + ",";
                    	jsonOut = jsonOut+"\"shop_count\": " + featureIn.getAttribute("shop_count").toString() + ",";
                    }
                    if(count_healthservice == true)
                    {
                    	tableOut += featureIn.getAttribute("healthservice_count").toString() + ",";
                    	jsonOut = jsonOut+"\"healthservice_count\": " + featureIn.getAttribute("healthservice_count").toString() + ",";
                    }
                    if(childcare_count == true)
                    {
                    	tableOut += featureIn.getAttribute("childcare_count").toString() + ",";
                    	jsonOut = jsonOut+"\"childcare_count\": " + featureIn.getAttribute("childcare_count").toString() + ",";
                    }
                    if(leisure_count == true)
                    {
                    	tableOut += featureIn.getAttribute("leisure_count").toString() + ",";
                    	jsonOut = jsonOut+"\"leisure_count\": " + featureIn.getAttribute("leisure_count").toString() + ",";
                    }
                    if(greenpublicspace_area == true)
                    {
                    	tableOut += featureIn.getAttribute("greenpublicspace_area").toString() + ",";
                    	jsonOut = jsonOut+"\"greenpublicspace_area\": " + featureIn.getAttribute("greenpublicspace_area").toString() + ",";
                    }
                    if(housing_quality_median == true)
                    {
                    	tableOut += featureIn.getAttribute("quality_housing_cluster").toString() + ",";
                    	jsonOut = jsonOut+"\"quality_housing_cluster\": " + featureIn.getAttribute("quality_housing_cluster").toString() + ",";
                    }
                    if(energy_efficiency == true)
                    {
                    	tableOut += featureIn.getAttribute("floorarea_median").toString() + ",";
                    	jsonOut = jsonOut+"\"floorarea_median\": " + featureIn.getAttribute("floorarea_median").toString() + ",";
                    }
                    if(landfill_count == true)
                    {
                    	tableOut += featureIn.getAttribute("landfill_count").toString() + ",";
                    	jsonOut = jsonOut+"\"landfill_count\": " + featureIn.getAttribute("landfill_count").toString() + ",";
                    }
                    if(desirability == true)
                    {
                    	tableOut += featureIn.getAttribute("desirability_neighbourhood_area").toString() + ",";
                    	jsonOut = jsonOut+"\"desirability_neighbourhood_area\": " + featureIn.getAttribute("desirability_neighbourhood_area").toString() + ",";
                    }
                    if(sum_prop_decile123 == true)
                    {
                    	tableOut += featureIn.getAttribute("deprivation_cluster").toString() + ",";
                    	jsonOut = jsonOut+"\"deprivation_cluster\": " + featureIn.getAttribute("deprivation_cluster").toString() + ",";
                    }
                    if(environmental_problem == true)
                    {
                    	tableOut += featureIn.getAttribute("environmental_problem").toString() + ",";
                    	jsonOut = jsonOut+"\"environmental_problem\": " + featureIn.getAttribute("environmental_problem").toString() + ",";

                    }
                 
                    tableOut =  tableOut.substring(0,tableOut.length()-1)+ "\r\n";
               	    jsonOut =  jsonOut.substring(0,jsonOut.length()-1)+ "},";
               }
           }
            finally {
                   iterator1.close();
            }
           
            jsonOut = jsonOut.substring(0,jsonOut.length()-1)+ "]}";
            //System.out.println(jsonOut);
            
         
    }    
    
    
    private static Map sortByComparator(Map unsortMap) {
         
        List list = new LinkedList(unsortMap.entrySet());
 
        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
            }
        });
 
        // put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
   
   
 
   
    private boolean exportToShapeFile(SimpleFeatureCollection propertyCollection, String username, String layer, HttpSession session) throws NoSuchAuthorityCodeException, IOException, FactoryException, HousingException {
        //System.out.println(newFile.toURI());       
        SimpleFeatureTypeBuilder stb = featureBuilder.createFeatureTypeBuilder(propertyCollection.getSchema(), "OutPut");   
        SimpleFeatureType newFeatureType = stb.buildFeatureType();       
        //System.out.println("propertyCollection.size 222  = "+ propertyCollection.size());
        if (!exportService.featuresExportToShapeFile(newFeatureType, propertyCollection,newFile, true)){
            throw new HousingException(Messages._EXPORT_TO_SHP_UNSUCCESSFULL);           
        }
        return true;
    }
   
    private void layersValidation() throws IOException, PSQLException, HousingException {
        propertyFc = postGISService.getFeatureSource(inputLayersConfig.getProperty());
        planCodeListFc = postGISService.getFeatureSource(inputLayersConfig.getPlanCodes());
        planOverlayFc = postGISService.getFeatureSource(inputLayersConfig.getPlanOverlay());
        LOGGER.info(planOverlayFc.getSchema().getCoordinateReferenceSystem().toString());
        zonecodesFc =  postGISService.getFeatureSource(inputLayersConfig.getZonecodesTbl());
        validationService.propertyValidated(propertyFc, inputLayersConfig.getProperty()) ;
        validationService.isPolygon(propertyFc, inputLayersConfig.getProperty()) ;
        validationService.isMetric(propertyFc, inputLayersConfig.getProperty());
        validationService.planOverlayValidated(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
        validationService.isMetric(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
        validationService.isPolygon(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
        validationService.planCodeListValidated(planCodeListFc, inputLayersConfig.getPlanCodes()) ;
        validationService.zonecodesValidated(zonecodesFc, inputLayersConfig.getZonecodesTbl()) ;       
    }

 


    //************************* Getter Setter  ***************************
    public ParameterDevelopNew getParameter() {
        return parameter;
    }

    public void setParameter(ParameterDevelopNew parameter) {
        this.parameter = parameter;
    }

    public Geometry getBufferAllParams() {
        return bufferAllParams;
    }

    public void setBufferAllParams(Geometry bufferAllParams) {
        this.bufferAllParams = bufferAllParams;
    }

    public Map<String, Object> getOutputLayer() {
        return outputLayer;
    }
    
    public String getJson()
    {
    	////System.out.println(jsonOut);
    	return jsonOut;
    }
    
    public String getJsonChart()
    {
    	////System.out.println(jsonOutChart);
    	return jsonOutChart;
    }

    public String getTable()
    {
    	return tableOut;
    }
   
}