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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.PolygonIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.config.OutPutLayerConfig;
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopPotential;
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
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;

import org.geotools.data.DataUtilities;

/**
 * Implementation for handling Potential Development analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
@Scope("session")	
public class DevelopmentPotentialServiceImpl implements DevelopmentPotentialService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DevelopmentPotentialServiceImpl.class);

	@Autowired	ParameterDevelopPotential parameter;
	@Autowired	 PostGISService postGISService;
	@Autowired	 GeoServerConfig geoServerConfig;
	@Autowired	GeoServerService geoServerService;
	@Autowired	private ValidationService validationService;
	@Autowired	private ExportService exportService;
	@Autowired	UnionService unionService;
	@Autowired 	FeatureBuilder featureBuilder;
	@Autowired	InputLayersConfig inputLayersConfig;
	@Autowired	OutPutLayerConfig outPutLayerConfig;

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
//	ReferencedEnvelope envelope;
	//	String layerName;
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
		layersValidation();
		propertyOverlaysNum = -1;
		oldRules = "";
		dropCreateSchema = true;
		propertyFilters = new ArrayList<Filter>();
		propertyFilter = null;	
		ownershipFilter = null;
		landUseFilter = null;
		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());		

		outputLayer = new HashMap<String, Object>(); 
		String workspace = geoServerConfig.getGsWorkspace() + "_" + username;
//		String dataStore = geoServerConfig.getGsPotentialDatastore();
		String dataStore = geoServerConfig.getGsDataStore();
		String layer = geoServerConfig.getGsPotentialLayer();
//		String style = geoServerConfig.getGsPotentialStyle();
		String styleName = username+"_"+geoServerConfig.getGsPotentialStyle();
		File newDirectory =  TemporaryFileManager.getNew(session, username,  layer , "",true);
		newFile = new File(newDirectory.getAbsolutePath()+"/"+ layer + ".shp");
		System.out.println(newFile.toURI());

		this.lgaFilter();
		this.dpiFilter();
		this.landUseFilter();
		this.bufferAllParamsFilter();
		this.ownershipFilter();		
		this.propertyFilter();
		if (!this.generateQuery(session)){
			return false;
		}		
		this.overlayCollection();
		if (!this.overlayIntersection(username, layer, session)){
			return false;
		}
		
		String sldBody = GeoServerServiceImpl.potentialStyleStart + oldRules + GeoServerServiceImpl.potentialStyleEnd;
		geoServerService.getGeoServer(workspace);
		geoServerService.publishPotentialStyle(sldBody, styleName);
		geoServerService.publishToGeoServer( workspace , dataStore , layer, styleName, newFile );	

		outputLayer.put("workspace", workspace);
		outputLayer.put("layerName", layer);		

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

	private void lgaFilter() throws Exception{	
		step = "lgaFilter";
		try{
			List<Filter> lgaFilters = new ArrayList<Filter>();
			for (String lga_code : parameter.getSelectedLGAs()) {
				Filter filter = ff.equals(ff.property(inputLayersConfig.getPropertyLgaCode()),ff.literal(lga_code));
				lgaFilters.add(filter);
			}
			Filter lgaFilter = ff.or(lgaFilters);
			propertyFilters.add(lgaFilter);			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_LGA_FILTER);			
		}
	}	

	private void dpiFilter() throws Exception{
		step = "dpiFilter";
		try{
			if (parameter.getDpi() != 0) {
				Divide divide = ff.divide(ff.property(inputLayersConfig.getProperty_svCurrentYear()),
						ff.property(inputLayersConfig.getProperty_civCurrentYear()));
				String operator = parameter.getDpiOperateorVal();
				Filter filterDPI = null;
				if (operator.equals(">")){
					filterDPI =  ff.greater( divide, ff.literal(parameter.getDpi()) );
				}else if (operator.equals(">=")){
					filterDPI =  ff.greaterOrEqual( divide, ff.literal(parameter.getDpi()) );
				}else if (operator.equals("=")){
					Filter filterGreater =  ff.greater( divide, ff.literal(parameter.getDpi()) );
					Filter filterEquals =  ff.equals( divide, ff.literal(parameter.getDpi()) );					
					Filter filterGreaterOrEqual = ff.or( filterGreater, filterEquals );
					Filter filterLess =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
					filterDPI = ff.and( filterGreaterOrEqual, filterLess );
				}else if (operator.equals("<")){
					filterDPI =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
				}else if (operator.equals("<=")){
					filterDPI =  ff.lessOrEqual( divide, ff.literal(parameter.getDpi()+0.1) );
				}
				propertyFilters.add(filterDPI);		

			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_DPI_FILTER);			
		}
	}

	private void landUseFilter() throws Exception{
		step = "landUseFilter";
		try{
			landUseFilters = new ArrayList<Filter>();
			Filter filter = null;
			if (parameter.getResidential()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("RESIDENTIAL"));
				landUseFilters(filter);
			}
			if (parameter.getBusiness()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("BUSINESS"));
				landUseFilters(filter);
			}
			if (parameter.getRural()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("RURAL"));
				landUseFilters(filter);
			}
			if (parameter.getMixedUse()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("MIXED USE"));
				landUseFilters(filter);
			}
			if (parameter.getSpecialPurpose()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()),ff.literal("SPECIAL PURPOSE"));
				landUseFilters(filter);
			}
			if (parameter.getUrbanGrowthBoundry()) {
				filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()),ff.literal("URBAN GROWTH BOUNDARY"));
				landUseFilters(filter);
			}		
			if (!landUseFilters.isEmpty()) {
				landUseFilter = ff.or(landUseFilters);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_LANDUSE_FILTER);			
		}
	}

	private void bufferAllParamsFilter() throws Exception{
		try{
			if (bufferAllParams != null) {
				Filter filter = null;
				LOGGER.info("bufferAllParams is not null");
//				if (bufferAllParams.isValid()){
//					LOGGER.info("bufferAllParams is Valid");
//				}
				GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
				String gemoName = gd.getName().toString();
				filter = ff.within(ff.property(gemoName),ff.literal(bufferAllParams));
				propertyFilters.add(filter);				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_TRANSPORT_FACILITY_FILTER);			
		}
	}

	private void ownershipFilter() throws Exception{
		try{
			List<Filter> ownershipFilters= new ArrayList<Filter>();
			Filter filter = null;
			Geometry publicAcquisitionsUnion;
			SimpleFeatureCollection publicAcquisitions = null;
			if (parameter.getPublicAcquision()) {
				filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("PUBLIC ACQUISION OVERLAY"));
				publicAcquisitions = overlayCollection(filter);			
				System.out.println("publicAcquisitions size==="+publicAcquisitions.size());				
				publicAcquisitionsUnion = (Geometry) unionService.createUnion(publicAcquisitions);			
				GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
				String gemoName = gd.getName().toString();
				filter = ff.not(ff.intersects(ff.property(gemoName),ff.literal(publicAcquisitionsUnion)));
				ownershipFilters.add(filter);	
			}
			//************ Ownership  --  CommonWealth ************
			Geometry commonWealthsUnion;
			SimpleFeatureCollection commonWealths = null;
			if ( parameter.getCommonwealth() ) {
				filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("COMMONWEALTH LAND"));
				commonWealths = overlayCollection(filter);			
				System.out.println("commonWealths size==="+commonWealths.size());
				commonWealthsUnion = (Geometry) unionService.createUnion(commonWealths);
				GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
				String gemoName = gd.getName().toString();
				filter = ff.not(ff.intersects(ff.property(gemoName),ff.literal(commonWealthsUnion)));	
			}		
			if (!ownershipFilters.isEmpty()) {
				ownershipFilter = ff.and(ownershipFilters);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_OWNERSHIP_FILTER);			
		}
	}

	private void overlayCollection() throws IOException, HousingException{
		Filter filter = null;
		overlayMap = new HashMap<String, Object>();
		anyOverlayChecked = false;

		//************ FLOODWAY OVERLAY ************	
		Geometry floodwaysUnion = null;
		if (parameter.getFloodway()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("FLOODWAY OVERLAY"));
			SimpleFeatureCollection floodways = overlayCollection(filter);
			if ( floodways != null ){
				floodwaysUnion = (Geometry) unionService.createUnion(floodways);				
//				floodwaysUnion = floodwaysUnion.buffer(0.001);
				overlayMap.put("floodways", floodwaysUnion);
				System.out.println("floodways size==="+floodways.size());
			}else{
				overlayMap.put("floodways", null);
			}
			stb.add("OL_Floodway", Boolean.class); 
			anyOverlayChecked = true;
		}
		//************ LAND SUBJECT TO INUNDATION OVERLAY ************
		
		inundationsUnion = null; //???
		if (parameter.getInundation()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
			SimpleFeatureCollection inundations = overlayCollection(filter);
			if ( inundations != null ){
				inundationsUnion = unionService.createUnion(inundations) ;
//				inundationsUnion = inundationsUnion.buffer(0.001);
				overlayMap.put("inundations", inundationsUnion);
				System.out.println("inundations size==="+inundations.size());
			}else{
				overlayMap.put("inundations", null);
			}
			stb.add("OL_Inundation", Boolean.class); 
			anyOverlayChecked = true;
		}
		//************ NEIGHBOURHOOD CHARACTER OVERLAY ************
		Geometry neighborhoodsUnion = null;		
		if (parameter.getNeighborhood()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("NEIGHBOURHOOD CHARACTER OVERLAY"));
			SimpleFeatureCollection neighborhoods = overlayCollection(filter);
			if ( neighborhoods != null && !neighborhoods.isEmpty()){
				neighborhoodsUnion = (Geometry) unionService.createUnion(neighborhoods);
//				neighborhoodsUnion = neighborhoodsUnion.buffer(0.001);
				overlayMap.put("neighborhoods", neighborhoodsUnion);
				System.out.println("neighborhoods size==="+neighborhoods.size());
			}else{
				overlayMap.put("neighborhoods", null);
			}
			stb.add("OL_Neighborhood", Boolean.class);
			anyOverlayChecked = true;
		}

		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry designDevelopmentsUnion = null;		
		if (parameter.getDesignDevelopment()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DESIGN AND DEVELOPMENT OVERLAY"));
			SimpleFeatureCollection designDevelopments = overlayCollection(filter);
			if ( designDevelopments != null && !designDevelopments.isEmpty()){
				designDevelopmentsUnion = (Geometry) unionService.createUnion(designDevelopments);
//				designDevelopmentsUnion = designDevelopmentsUnion.buffer(0.001);
				overlayMap.put("designDevelopments", designDevelopmentsUnion);
				System.out.println("designDevelopments size==="+designDevelopments.size());	
			}else{
				overlayMap.put("designDevelopments", null);
			}
			stb.add("OL_DesignDevelopment", Boolean.class);
			anyOverlayChecked = true;
		}
		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry developPlansUnion = null;		
		if (parameter.getDevelopPlan()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DEVELOPMENT PLAN OVERLAY"));
			SimpleFeatureCollection developPlans = overlayCollection(filter);
			if ( developPlans != null && !developPlans.isEmpty()){
				developPlansUnion = (Geometry) unionService.createUnion(developPlans);
//				developPlansUnion = developPlansUnion.buffer(0.001);
				overlayMap.put("developPlans", developPlansUnion);
				System.out.println("developPlans size==="+developPlans.size());	
			}else{
				overlayMap.put("developPlans", null);
			}
			stb.add("OL_DevelopmentPlan", Boolean.class); 
			anyOverlayChecked = true;
		}
		// ************ PARKING OVERLAY ************
		Geometry parkingsUnion = null;		
		if (parameter.getParking() ) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("PARKING OVERLAY"));
			SimpleFeatureCollection parkings = overlayCollection(filter);
			if ( parkings != null && !parkings.isEmpty() ){
				parkingsUnion = (Geometry) unionService.createUnion(parkings);
//				parkingsUnion = parkingsUnion.buffer(0.001);
				overlayMap.put("parkings", parkingsUnion);
				System.out.println("parkings size==="+parkings.size());	
			}else{
				overlayMap.put("parkings", null);
			}
			stb.add("OL_Parking", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ BUSHFIRE MANAGEMENT OVERLAY ************
		Geometry bushfiresUnion = null;	
		if (parameter.getBushfire() ) {	 
			Filter bushfilter1 = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("BUSHFIRE MANAGEMENT OVERLAY"));
			Filter bushfilter2 = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("WILDFIRE MANAGEMENT OVERLAY"));
			filter = ff.or(bushfilter1, bushfilter2);
			SimpleFeatureCollection bushfires = overlayCollection(filter);
			if ( bushfires != null && !bushfires.isEmpty() ){
				bushfiresUnion = (Geometry) unionService.createUnion(bushfires);
//				bushfiresUnion = bushfiresUnion.buffer(0.001);
				overlayMap.put("bushfires", bushfiresUnion);
				System.out.println("bushfires size==="+bushfires.size());
			}else{
				overlayMap.put("bushfires", null);
			}
			stb.add("OL_Bushfire", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ EROSION MANAGEMENT OVERLAY ************
		Geometry erosionsUnion = null;	
		if (parameter.getErosion() ) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("EROSION MANAGEMENT OVERLAY"));
			SimpleFeatureCollection erosions = overlayCollection(filter);
			if ( erosions != null && !erosions.isEmpty()){
				erosionsUnion = (Geometry) unionService.createUnion(erosions);
//				erosionsUnion = erosionsUnion.buffer(0.001);
				overlayMap.put("erosions", erosionsUnion);
				System.out.println("erosions size==="+erosions.size());	
			}else{
				overlayMap.put("erosions", null);
			}
			stb.add("OL_Erosion", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ VEGETATION PROTECTION OVERLAY ************
		Geometry vegprotectionsUnion = null;	
		if (parameter.getVegprotection()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("VEGETATION PROTECTION OVERLAY"));
			SimpleFeatureCollection vegprotections = overlayCollection(filter);
			if ( vegprotections != null && !vegprotections.isEmpty()){
				vegprotectionsUnion = (Geometry) unionService.createUnion(vegprotections);
//				vegprotectionsUnion = vegprotectionsUnion.buffer(0.001);
				overlayMap.put("vegprotections", vegprotectionsUnion);
				System.out.println("vegprotections size==="+vegprotections.size());	
			}else{
				overlayMap.put("vegprotections", null);
			}
			stb.add("OL_VegProtection", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ SALINITY MANAGEMENT OVERLAY ************
		Geometry salinitysUnion = null;
		if (parameter.getSalinity()) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("SALINITY MANAGEMENT OVERLAY"));
			SimpleFeatureCollection salinitys = overlayCollection(filter);
			if ( salinitys != null && !salinitys.isEmpty()){
				salinitysUnion = (Geometry) unionService.createUnion(salinitys);
//				salinitysUnion = salinitysUnion.buffer(0.001);
				overlayMap.put("salinitys", salinitysUnion);
				System.out.println("salinitys size==="+salinitys.size());	
			}else{
				overlayMap.put("salinitys", null);
			}
			stb.add("OL_Salinity", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ CONTAMINATION MANAGEMENT OVERLAY ************
		Geometry contaminationsUnion = null;
		if (parameter.getContamination()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("POTENTIALLY CONTAMINATED LAND OVERLAY"));
			SimpleFeatureCollection contaminations = overlayCollection(filter);
			if ( contaminations != null && !contaminations.isEmpty()){
				contaminationsUnion = (Geometry) unionService.createUnion(contaminations);
//				contaminationsUnion = contaminationsUnion.buffer(0.001);
				overlayMap.put("contaminations", contaminationsUnion);
				System.out.println("contaminations size==="+contaminations.size());	
			}else{
				overlayMap.put("contaminations", null);
			}
			stb.add("OL_Contamination", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ ENVIRONMENTAL SIGNIFICANCE OVERLAY ************
		Geometry envSignificancesUnion = null;
		if (parameter.getEnvSignificance()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL SIGNIFICANCE OVERLAY"));
			SimpleFeatureCollection envSignificances = overlayCollection(filter);
			if ( envSignificances != null && !envSignificances.isEmpty()){
				envSignificancesUnion = (Geometry) unionService.createUnion(envSignificances);
//				envSignificancesUnion = envSignificancesUnion.buffer(0.001);
				overlayMap.put("envSignificances", envSignificancesUnion);
				System.out.println("envSignificances size==="+envSignificances.size());	
			}else{
				overlayMap.put("envSignificances", null);
			}
			stb.add("OL_EnvSignificance", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ ENVIRONMENTAL AUDIT OVERLAY ************
		Geometry envAuditsUnion = null;
		if (parameter.getEnvAudit()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL AUDIT OVERLAY"));//???
			SimpleFeatureCollection envAudits = overlayCollection(filter);
			if ( envAudits != null && !envAudits.isEmpty()){
				envAuditsUnion = (Geometry) unionService.createUnion(envAudits);
//				envAuditsUnion = envAuditsUnion.buffer(0.001);
				overlayMap.put("envAudits", envAuditsUnion);
				System.out.println("envAudits size==="+envAudits.size());	
			}else{
				overlayMap.put("envAudits", null);
			}
			stb.add("OL_EnvAudit", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ HERITAGE OVERLAY ************		
		Geometry heritageUnion = null;
		if (parameter.getHeritage() ) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("HERITAGE OVERLAY"));// ???
			SimpleFeatureCollection heritages = overlayCollection(filter);
			if (heritages != null && !heritages.isEmpty()){
				heritageUnion = (Geometry) unionService.createUnion(heritages);
//				heritageUnion = heritageUnion.buffer(0.001);
				overlayMap.put("heritages", heritageUnion);
				System.out.println("heritages size==="+heritages.size());
			}else{
				overlayMap.put("heritages", null);
			}
			stb.add("OL_Heritage", Boolean.class);
			anyOverlayChecked = true;
		}
	}

	private void propertyFilter() throws IOException, HousingException{
		try{
			if (propertyFilters == null || propertyFilters.isEmpty()){
				throw new HousingException(Messages._SELECT_PARAM_TO_CONTINUE);
			}
			if (!propertyFilters.isEmpty() ) {
				if (landUseFilter != null){
					propertyFilters.add(landUseFilter);				
				}
				if (ownershipFilter != null){
					propertyFilters.add(ownershipFilter);				
				}
				propertyFilter = ff.and(propertyFilters);
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_PROPERTY_FILTER);
		}
	}
	

	private boolean generateQuery(HttpSession session) throws SQLException, Exception{
		propertyQuery = new Query();
		String geom_name = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
		//String[] attributes = {geom_name, outPutLayerConfig.getObjectid(), outPutLayerConfig.getPfi(), outPutLayerConfig.getLgaName()
		//		, outPutLayerConfig.getStreet_name(), outPutLayerConfig.getStreet_type(), outPutLayerConfig.getSuburb()
		//		, outPutLayerConfig.getPostcode(), outPutLayerConfig.getLand_area(), outPutLayerConfig.getAreameasure() };
		
		
		//ali
		String[] attributes = {geom_name, outPutLayerConfig.getObjectid(),outPutLayerConfig.getLand_area(), outPutLayerConfig.getSuburb()
				, outPutLayerConfig.getPostcode()				
				,outPutLayerConfig.getPfi(), outPutLayerConfig.getLgaName()
				, outPutLayerConfig.getStreet_name(), outPutLayerConfig.getStreet_type()
				,  outPutLayerConfig.getAreameasure() };
		
		
		propertyQuery.setPropertyNames(attributes);
		propertyQuery.setFilter(propertyFilter);			
		properties= propertyFc.getFeatures(propertyQuery);
		ReferencedEnvelope envelope = properties.getBounds();
		outputLayer.put("maxX", envelope.getMaxX());
		outputLayer.put("minX", envelope.getMinX());
		outputLayer.put("maxY", envelope.getMaxY());
		outputLayer.put("minY", envelope.getMinY());
		
		if (properties == null || properties.isEmpty()){
			LOGGER.info("No Properties Found!");
			throw new HousingException(Messages._NO_FEATURE);
		}
		System.out.println("size= "+properties.size());			
		stb = featureBuilder.createFeatureTypeBuilder(properties.getSchema(), "OutPut");	
		stb.add("OverlaysNum", Integer.class);
		return true;
	}

	private boolean overlayIntersection(String username, String layer, HttpSession session) throws NoSuchAuthorityCodeException, IOException, FactoryException, SQLException, Exception{
		List<Integer> oldRulesList = new ArrayList<Integer>(); 
		newFeatureType = stb.buildFeatureType();
		sfb = new SimpleFeatureBuilder(newFeatureType);
		List<SimpleFeature> newList = new ArrayList<SimpleFeature>();
		SimpleFeatureIterator propertyIt = null;
		propertyIt = properties.features();
		LOGGER.info("properties.size()=="+String.valueOf(properties.size()));
		
		
		
		System.out.println("properties.size()=="+String.valueOf(properties.size()));
		
		
		try {
			 
			
/////////////////////////////////////////////////////////////ali //ali		
			
			  if (anyOverlayChecked)
			  {
			  
				 SimpleFeatureCollection propertiesnew = FeatureCollections.newCollection();
				 SimpleFeatureTypeBuilder TypeBuilder = new SimpleFeatureTypeBuilder();
				 SimpleFeatureType featureTypeNew = null;
				 
				 while (propertyIt.hasNext()) {	
					  SimpleFeature featureIn = propertyIt.next();
					 
					  
				      TypeBuilder.setName("Location");     
				      SimpleFeatureType featureType = featureIn.getFeatureType();
				      TypeBuilder.addAll(featureType.getAttributeDescriptors());
				      TypeBuilder.add("OL_Floodway", Boolean.class); 
				      TypeBuilder.add("OL_Inundation", Boolean.class); 
				      TypeBuilder.add("OL_Neighborhood", Boolean.class);
				      TypeBuilder.add("OL_DesignDevelopment", Boolean.class);
				      TypeBuilder.add("OL_DevelopmentPlan", Boolean.class);
				      TypeBuilder.add("OL_Parking", Boolean.class);
				      TypeBuilder.add("OL_Bushfire", Boolean.class);
				      TypeBuilder.add("OL_Erosion", Boolean.class);
				      TypeBuilder.add("OL_VegProtection", Boolean.class);
				      TypeBuilder.add("OL_Salinity", Boolean.class);
				      TypeBuilder.add("OL_Contamination", Boolean.class);
				      TypeBuilder.add("OL_EnvSignificance", Boolean.class);
				      TypeBuilder.add("OL_EnvAudit", Boolean.class);
				      TypeBuilder.add("OL_Heritage", Boolean.class);
				      TypeBuilder.add("OverlaysNum", Integer.class);
				      
					  SimpleFeature featureOut;
				      featureTypeNew = TypeBuilder.buildFeatureType();
				      SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureTypeNew);
				      builder.init(featureIn);
				      featureOut = builder.buildFeature(featureIn.getID());
				      propertiesnew.add(featureOut);
				     
					}			 
				  propertyIt.close();
				 
				 
			      SimpleFeatureSource properies_source=DataUtilities.source(propertiesnew);
				  SimpleFeatureType properies_schema = properies_source.getSchema();
			      String properties_geometryAttributeName =  properies_schema.getGeometryDescriptor().getLocalName();
			      Filter filter_properties = ff.bbox(ff.property(properties_geometryAttributeName), propertiesnew.getBounds());
			      int y23=0;
			      
			      if (overlayMap.get("floodways") != null)
			      {
			          Geometry polygon_floodway1 =(Geometry)(overlayMap.get("floodways"));
				      Object polygon_floodway;
				      if (polygon_floodway1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_floodway =(MultiPolygon)overlayMap.get("floodways");		      
		
				      }
				      else
				      {
					       polygon_floodway =(Polygon)overlayMap.get("floodways");		      
		
				      }
				      
				      Filter filter_floodway = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_floodway));		      
				      Filter filter_properties_floodway = ff.and(filter_properties, filter_floodway);	      
				      SimpleFeatureIterator features_floodway_iterator = properies_source.getFeatures(filter_properties_floodway).features();
				      while (features_floodway_iterator.hasNext()) {
				    	  SimpleFeature sf_floodway = features_floodway_iterator.next();
				    	  sf_floodway.setAttribute("OL_Floodway", Boolean.TRUE);
				    	  sf_floodway.setAttribute("OverlaysNum", Integer.parseInt(sf_floodway.getAttribute("OverlaysNum").toString())+1);
				      }
				      features_floodway_iterator.close();
			      
			      }
			      
			      
				  if (overlayMap.get("inundations") != null)
				  {
				      Geometry polygon_inundations1 =(Geometry)(overlayMap.get("inundations"));
				      Object polygon_inundations;
				      if (polygon_inundations1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_inundations =(MultiPolygon)overlayMap.get("inundations");		      
		
				      }
				      else
				      {
					       polygon_inundations =(Polygon)overlayMap.get("inundations");		      
		
				      }
				      
				      Filter filter_inundations = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_inundations));		      
				      Filter filter_properties_inundations = ff.and(filter_properties, filter_inundations);		      
				      SimpleFeatureIterator features_inundations_iterator = properies_source.getFeatures(filter_properties_inundations).features();
				      while (features_inundations_iterator.hasNext()) {
				    	  SimpleFeature sf_inundations = features_inundations_iterator.next();
				    	  sf_inundations.setAttribute("OL_Inundation", Boolean.TRUE);
				    	  if (sf_inundations.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_inundations.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {
				    	      sf_inundations.setAttribute("OverlaysNum", Integer.parseInt(sf_inundations.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_inundations_iterator.close();
				  }
			      
			      if (overlayMap.get("neighborhoods") != null)		    	  
			      {		    	  
				      
					      Geometry polygon_neighborhoods1 =(Geometry)(overlayMap.get("neighborhoods"));
					      Object polygon_neighborhoods;
					      if (polygon_neighborhoods1.getGeometryType().equals("MultiPolygon"))
					      {
						       polygon_neighborhoods =(MultiPolygon)overlayMap.get("neighborhoods");		      
			
					      }
					      else
					      {
						       polygon_neighborhoods =(Polygon)overlayMap.get("neighborhoods");		      
			
					      }
					      
					      
					     	      
					      Filter filter_neighborhoods = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_neighborhoods));		      
					      Filter filter_properties_neighborhoods = ff.and(filter_properties, filter_neighborhoods);		      
					      SimpleFeatureIterator features_neighborhoods_iterator = properies_source.getFeatures(filter_properties_neighborhoods).features();
					      while (features_neighborhoods_iterator.hasNext()) {
					    	  SimpleFeature sf_neighborhoods = features_neighborhoods_iterator.next();
					    	  sf_neighborhoods.setAttribute("OL_Neighborhood", Boolean.TRUE);
					    	  if (sf_neighborhoods.getAttribute("OverlaysNum")==null)
					    	  {
					    		  sf_neighborhoods.setAttribute("OverlaysNum", 1);
					    	  }
					    	  else
					    	  {
					    	      sf_neighborhoods.setAttribute("OverlaysNum", Integer.parseInt(sf_neighborhoods.getAttribute("OverlaysNum").toString())+1);
					    	  }
					      }
					      features_neighborhoods_iterator.close();
				      }
				      
				      
				      if (overlayMap.get("designDevelopments") != null)
				      {
				    	  
				     
					      Geometry polygon_designDevelopments1 =(Geometry)(overlayMap.get("designDevelopments"));
					      Object polygon_designDevelopments;
					      if (polygon_designDevelopments1.getGeometryType().equals("MultiPolygon"))
					      {
						       polygon_designDevelopments =(MultiPolygon)overlayMap.get("designDevelopments");		      
			
					      }
					      else
					      {
						       polygon_designDevelopments =(Polygon)overlayMap.get("designDevelopments");		      
			
					      }
			      
					      Filter filter_designDevelopments = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_designDevelopments));		      
					      Filter filter_properties_designDevelopments = ff.and(filter_properties, filter_designDevelopments);		      
					      SimpleFeatureIterator features_designDevelopments_iterator = properies_source.getFeatures(filter_properties_designDevelopments).features();
					      while (features_designDevelopments_iterator.hasNext()) {
					    	  SimpleFeature sf_designDevelopments = features_designDevelopments_iterator.next();
					    	  sf_designDevelopments.setAttribute("OL_DesignDevelopment", Boolean.TRUE);
					    	  if (sf_designDevelopments.getAttribute("OverlaysNum")==null)
					    	  {
					    		  sf_designDevelopments.setAttribute("OverlaysNum", 1);
					    	  }
					    	  else
					    	  {
					    	  
					    		  sf_designDevelopments.setAttribute("OverlaysNum", Integer.parseInt(sf_designDevelopments.getAttribute("OverlaysNum").toString())+1);
					    	  }
					      }
					      features_designDevelopments_iterator.close();		
				      }
			      
			      
				      
				  if (overlayMap.get("developmentPlans") != null)
				  {
					  
				  
				      Geometry polygon_developmentPlans1 =(Geometry)(overlayMap.get("developmentPlans"));
				      Object polygon_developmentPlans;
				      if (polygon_developmentPlans1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_developmentPlans =(MultiPolygon)overlayMap.get("developmentPlans");		      
		
				      }
				      else
				      {
					       polygon_developmentPlans =(Polygon)overlayMap.get("developmentPlans");		      
		
				      }
				      
			      
				      Filter filter_developmentPlans = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_developmentPlans));		      
				      Filter filter_properties_developmentPlans = ff.and(filter_properties, filter_developmentPlans);		      
				      SimpleFeatureIterator features_developmentPlans_iterator = properies_source.getFeatures(filter_properties_developmentPlans).features();
				      while (features_developmentPlans_iterator.hasNext()) {
				    	  SimpleFeature sf_developmentPlans = features_developmentPlans_iterator.next();
				    	  sf_developmentPlans.setAttribute("OL_DevelopmentPlan", Boolean.TRUE);
				    	  if (sf_developmentPlans.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_developmentPlans.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {
				    	  
				    	      sf_developmentPlans.setAttribute("OverlaysNum", Integer.parseInt(sf_developmentPlans.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_developmentPlans_iterator.close();	
			      
				  }
			      
		 
			      
				  if (overlayMap.get("parkings") != null)
				  {
				      Geometry polygon_parkings1 =(Geometry)(overlayMap.get("parkings"));
				      Object polygon_parkings;
				      if (polygon_parkings1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_parkings =(MultiPolygon)overlayMap.get("parkings");		      
		
				      }
				      else
				      {
					       polygon_parkings =(Polygon)overlayMap.get("parkings");		      
		
				      }
				      	      
				      Filter filter_parkings = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_parkings));		      
				      Filter filter_properties_parkings = ff.and(filter_properties, filter_parkings);		      
				      SimpleFeatureIterator features_parkings_iterator = properies_source.getFeatures(filter_properties_parkings).features();
				      while (features_parkings_iterator.hasNext()) {
				    	  SimpleFeature sf_parkings = features_parkings_iterator.next();
				    	  sf_parkings.setAttribute("OL_Parking", Boolean.TRUE);
				    	  if (sf_parkings.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_parkings.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {
				    	      sf_parkings.setAttribute("OverlaysNum", Integer.parseInt(sf_parkings.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_parkings_iterator.close();		
			      
				  }
			      
				  
				  if (overlayMap.get("bushfires") != null)
				  {
					  
				  
				      Geometry polygon_bushfires1 =(Geometry)(overlayMap.get("bushfires"));
				      Object polygon_bushfires;
				      if (polygon_bushfires1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_bushfires =(MultiPolygon)overlayMap.get("bushfires");		      
		
				      }
				      else
				      {
					       polygon_bushfires =(Polygon)overlayMap.get("bushfires");		      
		
				      }	
				      
				      Filter filter_bushfires = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_bushfires));		      
				      Filter filter_properties_bushfires = ff.and(filter_properties, filter_bushfires);		      
				      SimpleFeatureIterator features_bushfires_iterator = properies_source.getFeatures(filter_properties_bushfires).features();
				      while (features_bushfires_iterator.hasNext()) {
				    	  SimpleFeature sf_bushfires = features_bushfires_iterator.next();
				    	  sf_bushfires.setAttribute("OL_Bushfire", Boolean.TRUE);
				    	  if (sf_bushfires.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_bushfires.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {
				    	       sf_bushfires.setAttribute("OverlaysNum", Integer.parseInt(sf_bushfires.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_bushfires_iterator.close();	
				  }
				  
				  
			      
				  if (overlayMap.get("erosions") != null)
				  {
					  
					  
				      Geometry polygon_erosions1 =(Geometry)(overlayMap.get("erosions"));
				      Object polygon_erosions;
				      if (polygon_erosions1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_erosions =(MultiPolygon)overlayMap.get("erosions");		      
		
				      }
				      else
				      {
					       polygon_erosions =(Polygon)overlayMap.get("erosions");		      
		
				      }	
				      
				   	      
				      Filter filter_erosions = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_erosions));		      
				      Filter filter_properties_erosions = ff.and(filter_properties, filter_erosions);		      
				      SimpleFeatureIterator features_erosions_iterator = properies_source.getFeatures(filter_properties_erosions).features();
				      while (features_erosions_iterator.hasNext()) {
				    	  SimpleFeature sf_erosions = features_erosions_iterator.next();
				    	  sf_erosions.setAttribute("OL_Erosion", Boolean.TRUE);
				    	  if (sf_erosions.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_erosions.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {
				    	      sf_erosions.setAttribute("OverlaysNum", Integer.parseInt(sf_erosions.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_erosions_iterator.close();	
			      
				  }
			      
			      
				  
				  if (overlayMap.get("vegprotections") != null)
				  {
					  
				      Geometry polygon_vegprotections1 =(Geometry)(overlayMap.get("vegprotections"));
				      Object polygon_vegprotections;
				      if (polygon_vegprotections1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_vegprotections =(MultiPolygon)overlayMap.get("vegprotections");		      
		
				      }
				      else
				      {
					       polygon_vegprotections =(Polygon)overlayMap.get("vegprotections");		      
		
				      }	
				      
			      
				      Filter filter_vegprotections = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_vegprotections));		      
				      Filter filter_properties_vegprotections = ff.and(filter_properties, filter_vegprotections);		      
				      SimpleFeatureIterator features_vegprotections_iterator = properies_source.getFeatures(filter_properties_vegprotections).features();
				      while (features_vegprotections_iterator.hasNext()) {
				    	  SimpleFeature sf_vegprotections = features_vegprotections_iterator.next();
				    	  sf_vegprotections.setAttribute("OL_VegProtection", Boolean.TRUE);
				    	  if (sf_vegprotections.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_vegprotections.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {		    	  
				    	      sf_vegprotections.setAttribute("OverlaysNum", Integer.parseInt(sf_vegprotections.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_vegprotections_iterator.close();	
				      
				  }
			      
				  
				  if (overlayMap.get("salinitys") != null)
				  {
				      Geometry polygon_salinitys1 =(Geometry)(overlayMap.get("salinitys"));
				      Object polygon_salinitys;
				      if (polygon_salinitys1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_salinitys =(MultiPolygon)overlayMap.get("salinitys");		      
		
				      }
				      else
				      {
					       polygon_salinitys =(Polygon)overlayMap.get("salinitys");		      
		
				      }	
				      
				    	      
				      Filter filter_salinitys = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_salinitys));		      
				      Filter filter_properties_salinitys = ff.and(filter_properties, filter_salinitys);		      
				      SimpleFeatureIterator features_salinitys_iterator = properies_source.getFeatures(filter_properties_salinitys).features();
				      while (features_salinitys_iterator.hasNext()) {
				    	  SimpleFeature sf_salinitys = features_salinitys_iterator.next();
				    	  sf_salinitys.setAttribute("OL_Salinity", Boolean.TRUE);
				    	  if (sf_salinitys.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_salinitys.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {		 		    	  
				    	       sf_salinitys.setAttribute("OverlaysNum", Integer.parseInt(sf_salinitys.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_salinitys_iterator.close();		
				  }
			      
			      
				  if (overlayMap.get("contaminations") != null)
				  {
				      Geometry polygon_contaminations1 =(Geometry)(overlayMap.get("contaminations"));
				      Object polygon_contaminations;
				      if (polygon_contaminations1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_contaminations =(MultiPolygon)overlayMap.get("contaminations");		      
		
				      }
				      else
				      {
					       polygon_contaminations =(Polygon)overlayMap.get("contaminations");		      
		
				      }	
			      
				      Filter filter_contaminations = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_contaminations));		      
				      Filter filter_properties_contaminations = ff.and(filter_properties, filter_contaminations);		      
				      SimpleFeatureIterator features_contaminations_iterator = properies_source.getFeatures(filter_properties_contaminations).features();
				      while (features_contaminations_iterator.hasNext()) {
				    	  SimpleFeature sf_contaminations = features_contaminations_iterator.next();
				    	  sf_contaminations.setAttribute("OL_Contamination", Boolean.TRUE);
				    	  if (sf_contaminations.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_contaminations.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {	
				    	      sf_contaminations.setAttribute("OverlaysNum", Integer.parseInt(sf_contaminations.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_contaminations_iterator.close();	
				  }
			      
				  
				  if (overlayMap.get("envSignificances") != null)
				  {
				      Geometry polygon_envSignificances1 =(Geometry)(overlayMap.get("envSignificances"));
				      Object polygon_envSignificances;
				      if (polygon_envSignificances1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_envSignificances =(MultiPolygon)overlayMap.get("envSignificances");		      
		
				      }
				      else
				      {
					       polygon_envSignificances =(Polygon)overlayMap.get("envSignificances");		      
		
				      }	
			      
				      Filter filter_envSignificances = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_envSignificances));		      
				      Filter filter_properties_envSignificances = ff.and(filter_properties, filter_envSignificances);		      
				      SimpleFeatureIterator features_envSignificances_iterator = properies_source.getFeatures(filter_properties_envSignificances).features();
				      while (features_envSignificances_iterator.hasNext()) {
				    	  SimpleFeature sf_envSignificances = features_envSignificances_iterator.next();
				    	  sf_envSignificances.setAttribute("OL_EnvSignificance", Boolean.TRUE);
				    	  if (sf_envSignificances.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_envSignificances.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {	
				    	      sf_envSignificances.setAttribute("OverlaysNum", Integer.parseInt(sf_envSignificances.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_envSignificances_iterator.close();
				  }
			      
			      
				  if (overlayMap.get("envAudits") != null)
				  {
				      Geometry polygon_envAudits1 =(Geometry)(overlayMap.get("envAudits"));
				      Object polygon_envAudits;
				      if (polygon_envAudits1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_envAudits =(MultiPolygon)overlayMap.get("envAudits");		      
		
				      }
				      else
				      {
					       polygon_envAudits =(Polygon)overlayMap.get("envAudits");		      
		
				      }	
				      
				          
				      Filter filter_envAudits = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_envAudits));		      
				      Filter filter_properties_envAudits = ff.and(filter_properties, filter_envAudits);		      
				      SimpleFeatureIterator features_envAudits_iterator = properies_source.getFeatures(filter_properties_envAudits).features();
				      while (features_envAudits_iterator.hasNext()) {
				    	  SimpleFeature sf_envAudits = features_envAudits_iterator.next();
				    	  sf_envAudits.setAttribute("OL_EnvAudit", Boolean.TRUE);
				    	  if ( sf_envAudits.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_envAudits.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {	
				    	   
				    	      sf_envAudits.setAttribute("OverlaysNum", Integer.parseInt(sf_envAudits.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_envAudits_iterator.close();	
				  }
			      
			      
				  if (overlayMap.get("heritages") != null)
				  {
				  
				      Geometry polygon_heritages1 =(Geometry)(overlayMap.get("heritages"));
				      Object polygon_heritages;
				      if (polygon_heritages1.getGeometryType().equals("MultiPolygon"))
				      {
					       polygon_heritages =(MultiPolygon)overlayMap.get("heritages");		      
		
				      }
				      else
				      {
					       polygon_heritages =(Polygon)overlayMap.get("heritages");		      
		
				      }	
				      
				      
				      Filter filter_heritages = ff.intersects(ff.property(properties_geometryAttributeName), ff.literal(polygon_heritages));		      
				      Filter filter_properties_heritages = ff.and(filter_properties, filter_heritages);		      
				      SimpleFeatureIterator features_heritages_iterator = properies_source.getFeatures(filter_properties_heritages).features();
				      while (features_heritages_iterator.hasNext()) {
				    	  SimpleFeature sf_heritages = features_heritages_iterator.next();
				    	  sf_heritages.setAttribute("OL_Heritage", Boolean.TRUE);
				    	  if ( sf_heritages.getAttribute("OverlaysNum")==null)
				    	  {
				    		  sf_heritages.setAttribute("OverlaysNum", 1);
				    	  }
				    	  else
				    	  {	
				    	      sf_heritages.setAttribute("OverlaysNum", Integer.parseInt(sf_heritages.getAttribute("OverlaysNum").toString())+1);
				    	  }
				      }
				      features_heritages_iterator.close();	
			      
				  }
			      
			      //////
			      
			        SimpleFeatureIterator propertyItnew = null;
					propertyItnew = propertiesnew.features();
					int ii=0;
					
					if (!oldRulesList.contains(0)){					
	 					oldRules = geoServerService.createPotentialRule(oldRules, 0);
	 					oldRulesList.add(0);
					}
					
					while (propertyItnew.hasNext()) 
					{
						
						SimpleFeature newFeature = propertyItnew.next();
						
						if (newFeature.getAttribute("OverlaysNum") != null)
						{						
							
						    propertyOverlaysNum =  Integer.parseInt(newFeature.getAttribute("OverlaysNum").toString());
							
							// **************** Create Styles ****************
							if (!oldRulesList.contains(Integer.valueOf(propertyOverlaysNum))){					
			 					oldRules = geoServerService.createPotentialRule(oldRules, propertyOverlaysNum);
			 					oldRulesList.add(Integer.valueOf(propertyOverlaysNum));
							}							
						}
						else
						{
							newFeature.setAttribute("OverlaysNum",0);
						}
						newList.add(newFeature);
						ii++;
						System.out.println(ii);
						if (ii == 1000) {
							System.out.println("properties.size() == 1000");
							SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(featureTypeNew, newList);						 
							exportService.featuresExportToShapeFile(featureTypeNew, featureCollectionNew,newFile, dropCreateSchema);
							newList.clear();
							ii = 0;
							dropCreateSchema = false;
						}				
								
					}//end while					
					propertyItnew.close();
					
					if (!newList.isEmpty()){
						System.out.println("properties.size() < 1000");
						SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(featureTypeNew, newList);
						exportService.featuresExportToShapeFile(featureTypeNew, featureCollectionNew,newFile, dropCreateSchema);
					}	
				
				}
				
		       ////end ali
		     
/////////////////////////////////////////////////////////////////////////////////////	
				//Ghazal
		
			/*
			int i = 0;
	
			
			propertyOverlaysNum = -1;
			while (propertyIt.hasNext()) {
				
				SimpleFeature sf = propertyIt.next();
				sfb.addAll(sf.getAttributes());
				Geometry propertyGeom = (Geometry) sf.getDefaultGeometry();
				propertyGeom = propertyGeom.buffer(0.001);
				if (anyOverlayChecked){
					propertyOverlaysNum = 0;
					// **************** Intersections ****************
				    Geometry floodwaysUnion = (Geometry) overlayMap.get("floodways");					
					if (floodwaysUnion != null) {			
	
						if (floodwaysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Floodway", Boolean.TRUE);
							propertyOverlaysNum++;					
						}					
													
					}	
					
					//Geometry inundationsUnion = (Geometry) overlayMap.get("inundations");					
					if (inundationsUnion != null) {	
					
						if (inundationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Inundation", Boolean.TRUE);	
							propertyOverlaysNum++;
							System.out.println(sf.getAttribute("pfi")+"  have intersection  with OL_Inundation  == ");							
						}
									
					}
					
					Geometry neighborhoodsUnion = (Geometry) overlayMap.get("neighborhoods");
					if (neighborhoodsUnion != null) {
						if (neighborhoodsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Neighborhood", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Neighborhood  == ");						
						}									
					}				
					Geometry designDevelopmentsUnion = (Geometry) overlayMap.get("designDevelopments");
					if (designDevelopmentsUnion != null) {
						if (designDevelopmentsUnion.intersects(propertyGeom)) {
							sfb.set("OL_DesignDevelopment", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_DesignDevelopment  == ");						
						}									
					}				
					Geometry developPlansUnion = (Geometry) overlayMap.get("developPlans");
					if (developPlansUnion != null) {
						if (developPlansUnion.intersects(propertyGeom)) {
							sfb.set("OL_DevelopmentPlan", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_DevelopmentPlan  == ");						
						}									
					}				
					Geometry parkingsUnion = (Geometry) overlayMap.get("parkings");
					if (parkingsUnion != null) {
						if (parkingsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Parking", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Parking  == ");						
						}									
					}					
					Geometry bushfiresUnion = (Geometry) overlayMap.get("bushfires");
					if (bushfiresUnion != null) {
						if (bushfiresUnion.intersects(propertyGeom)) {
							sfb.set("OL_Bushfire", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Bushfire  == ");						
						}									
					}				
					Geometry erosionsUnion = (Geometry) overlayMap.get("erosions");
					if (erosionsUnion != null) {
						if (erosionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Erosion", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Erosion  == ");						
						}									
					}				
					Geometry vegprotectionsUnion = (Geometry) overlayMap.get("vegprotections");
					if (vegprotectionsUnion != null) {
						if (vegprotectionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_VegProtection", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_VegProtection  == ");						
						}									
					}				
					Geometry salinitysUnion = (Geometry) overlayMap.get("salinitys");
					if (salinitysUnion != null) {
						if (salinitysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Salinity", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Salinity  == ");						
						}									
					}				
					Geometry contaminationsUnion = (Geometry) overlayMap.get("contaminations");
					if (contaminationsUnion != null) {
						if (contaminationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Contamination", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Contamination  == ");						
						}									
					}				
					Geometry envSignificancesUnion = (Geometry) overlayMap.get("envSignificances");
					if (envSignificancesUnion != null) {
						if (envSignificancesUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvSignificance", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_EnvSignificance  == ");						
						}									
					}				
					Geometry envAuditsUnion = (Geometry) overlayMap.get("envAudits");
					if (envAuditsUnion != null) {
						if (envAuditsUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvAudit", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_EnvAudit  == ");						
						}									
					}
					Geometry heritageUnion = (Geometry) overlayMap.get("heritages");
					if (heritageUnion != null) {
						if (heritageUnion.intersects(propertyGeom)) {
							sfb.set("OL_Heritage", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Heritage  == ");

						}									
					}
				}
				
				// **************** Create Styles ****************
				if (!oldRulesList.contains(Integer.valueOf(propertyOverlaysNum))){					
 					oldRules = geoServerService.createPotentialRule(oldRules, propertyOverlaysNum);
 					oldRulesList.add(Integer.valueOf(propertyOverlaysNum));
				}
				
				// **************** Intersections End ****************
				sfb.set("OverlaysNum", propertyOverlaysNum);
				SimpleFeature newFeature = sfb.buildFeature(null);
				newList.add(newFeature);

				i++;
				System.out.println(i);
				if (i == 1000) {
					System.out.println("properties.size() == 10000");
					SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(newFeatureType, newList);						 
					exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,newFile, dropCreateSchema);
					//						newList = new ArrayList<SimpleFeature>();
					newList.clear();
					i = 0;
					dropCreateSchema = false;
				}				
			}//end while
			
			if (!newList.isEmpty()){
				System.out.println("properties.size() < 1000");
				SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(newFeatureType, newList);
				exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,newFile, dropCreateSchema);
			}
			//end Ghazal
		*/
				
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_OVERLAY_INTERSECTION);
		} finally {
			propertyIt.close();
		}
		return true;
	}


	//*************************  landUseFilters ***************************

	private void landUseFilters(Filter filter) throws IOException, HousingException {
		SimpleFeatureIterator it = null;
		try{
			Query zoneCodeQuery = new Query();
			zoneCodeQuery.setPropertyNames(new String[] { inputLayersConfig.getZonecodes_zoneCode(), inputLayersConfig.getZonecodes_group1()});
			zoneCodeQuery.setFilter(filter);
			tblZoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
			it = tblZoneCodes.features();
			List<Filter> match = new ArrayList<Filter>();
			while (it.hasNext()) {
				SimpleFeature zoneCode = it.next();
				Object value = zoneCode.getAttribute(inputLayersConfig.getZonecodes_zoneCode());
				filter = ff.equals(ff.property(inputLayersConfig.getProperty_zoning()), ff.literal(value));
				match.add(filter);
			}
			System.out.println(match.size());			
			Filter filterRES = ff.or(match);
			landUseFilters.add(filterRES);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_LANDUSE_FILTER);
		}finally{
			it.close();
		}
	}

	//************************* overlayCollection  ***************************
	private SimpleFeatureCollection overlayCollection(Filter filter) throws IOException, HousingException {
		SimpleFeatureCollection overlays = null;
		SimpleFeatureIterator it = null;
		try{
			Query codeListQuery = new Query();
			codeListQuery.setPropertyNames(new String[] { inputLayersConfig.getPlanCodes_zoneCode(), inputLayersConfig.getPlanCodes_group1() });
			codeListQuery.setFilter(filter);
			planCodeList = planCodeListFc.getFeatures(codeListQuery);
			System.out.println("planCodeList.size()===  "+planCodeList.size());
			it = planCodeList.features();
			List<Filter> match = new ArrayList<Filter>();
			while (it.hasNext()) {
				SimpleFeature zoneCode = it.next();
				Object value = zoneCode.getAttribute(inputLayersConfig.getPlanOverlay_zoneCode());
				filter = ff.equals(ff.property(inputLayersConfig.getPlanOverlay_zoneCode()), ff.literal(value));
				match.add(filter);
			}
			Filter filterOverlay = ff.or(match);
			Query overlayQuery = new Query();
			overlayQuery.setFilter(filterOverlay);
			overlays = planOverlayFc.getFeatures(overlayQuery);	
			System.out.println("codeoverlaysList.size()===  "+ overlays.size());
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_OVERLAY_FEATURELIST);
		}finally{
			it.close();
		}
		return overlays;
	}

	//************************* Getter Setter  ***************************
	public ParameterDevelopPotential getParameter() {
		return parameter;
	}

	public void setParameter(ParameterDevelopPotential parameter) {
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

	//	if (propertyFilter != null) {
	//			propertyQuery.setFilter(propertyFilter);
	//		} else {
	//			List<Filter> fs = new ArrayList<Filter>();
	//			Filter filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));// no inundation 
	//			fs.add(filter);
	//			filter = ff.equals(ff.property("pfi"),ff.literal("4787005"));//  inundation 
	//			fs.add(filter);
	//			filter = ff.equals(ff.property("pfi"),ff.literal("1317010"));//  inundation 
	//			fs.add(filter);
	//			filter = ff.equals(ff.property("lga_code"),ff.literal("1317010"));//  inundation 
	//			fs.add(filter);
	//			propertyFilter = ff.or(fs);	
	//			propertyQuery.setFilter(propertyFilter);
	//			System.out.println("if (propertyFilter ===== null) {");
	//		}
	
	
//	filter = ff.equals(ff.property("pfi"),ff.literal("5026012"));//
		//			filter = ff.equals(ff.property("pfi"),ff.literal("3196232"));//public acuisition
		//			filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));//inundation
//		propertyFilters.add(filter);
}

