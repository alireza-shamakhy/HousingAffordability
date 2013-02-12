package au.org.housing.service.impl;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.LayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopAssessment;
import au.org.housing.service.DevelpmentAssessment;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.PostGISService;
import au.org.housing.utilities.Zip;


import com.vividsolutions.jts.geom.Geometry;

@Service
public class DevelpmentAssessmentImpl implements DevelpmentAssessment {

	private static final Logger LOGGER = LoggerFactory.getLogger(DevelpmentAssessmentImpl.class);

	@Autowired
	private ParameterDevelopAssessment parameter;

	@Autowired
	private PostGISService postGISService;

	@Autowired
	private GeoServerConfig geoServerConfig;

	@Autowired
	private LayersConfig layerMapping;

	@Autowired
	private ExportService exportService;

	SimpleFeatureSource propertyFc;

	GetMapResponse response;
	BufferedImage image;

	SimpleFeatureTypeBuilder stb;
	SimpleFeatureBuilder sfb;
	SimpleFeatureType newFeatureType;
	@Autowired 
	private FeatureBuilder featureBuilder;

	public boolean analyse() throws IOException, FileNotFoundException,
	ServiceException, NoSuchAuthorityCodeException, FactoryException, PSQLException {

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools
				.getDefaultHints());

		List<Filter> pparsFilters = new ArrayList<Filter>();
		List<Filter> lgaFilters = new ArrayList<Filter>();
		List<Filter> propertyFilters = new ArrayList<Filter>();

		SimpleFeatureSource pparsFc = postGISService.getFeatureSource(layerMapping.getPpars());
		if (pparsFc==null){
			return false;
		}
		SimpleFeatureSource propertyFc = postGISService.getFeatureSource(layerMapping.getProperty());
		if (propertyFc==null){
			return false;
		}
		
		if (parameter.getDurationAssessment() != 0) {
			String operator = parameter.getDurationAssessmentOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("duration"),
						ff.literal(parameter.getDurationAssessment()));
				break;
			case '=':
				filter = ff.equals(ff.property("duration"),
						ff.literal(parameter.getDurationAssessment()));
				break;
			case '<':
				filter = ff.less(ff.property("duration"),
						ff.literal(parameter.getDurationAssessment()));
				break;
			}
			pparsFilters.add(filter);
		}

		if (parameter.getNumOfObjection() != 0) {
			String operator = parameter.getNumOfObjectionOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("objections"),
						ff.literal(parameter.getNumOfObjection()));
				break;
			case '=':
				filter = ff.equals(ff.property("objections"),
						ff.literal(parameter.getNumOfObjection()));
				break;
			case '<':
				filter = ff.less(ff.property("objections"),
						ff.literal(parameter.getNumOfObjection()));
				break;
			}
			pparsFilters.add(filter);
		}

		if (parameter.getFurtherInfo() == 2) {
			Filter filter = ff.equals(ff.property("furtherinf"),
					ff.literal(parameter.getFurtherInfo()));
			pparsFilters.add(filter);
		}

		if (parameter.getPublicNotice() == 2) {
			Filter filter = ff.equals(ff.property("publicnoti"),
					ff.literal(parameter.getPublicNotice()));
			pparsFilters.add(filter);
		}

		if (parameter.getReferralIssues() == 2) {
			Filter filter = ff.equals(ff.property("referralis"),
					ff.literal(parameter.getReferralIssues()));
			pparsFilters.add(filter);
		}

		if (!parameter.getSelectedCategories().isEmpty()) {
			Filter filter = null;
			StringBuilder sb = new StringBuilder();
			int index = 0;
			for (Integer categoryCode : parameter.getSelectedCategories()) {
				sb.append(categoryCode);
				if (++index != parameter.getSelectedCategories().size()) {
					sb.append(",");
				}
				filter = ff.equals(ff.property("category"),
						ff.literal(sb.toString()));
			}
			pparsFilters.add(filter);
		}

		if (parameter.getNumOfDwelling() != 0) {
			String operator = parameter.getNumOfDwellingOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("numberofne"),
						ff.literal(parameter.getNumOfDwelling()));
				break;
			case '=':
				filter = ff.equals(ff.property("numberofne"),
						ff.literal(parameter.getNumOfDwelling()));
				break;
			case '<':
				filter = ff.less(ff.property("numberofne"),
						ff.literal(parameter.getNumOfDwelling()));
				break;
			}
			pparsFilters.add(filter);
		}

		if (parameter.getCurrentUse() == 7) {
			Filter filter = ff.equals(ff.property("currentuse"),
					ff.literal(parameter.getCurrentUse()));
			pparsFilters.add(filter);
		} else if (parameter.getReferralIssues() == -7) {
			Filter filter = ff.notEqual(ff.property("currentuse"),
					ff.literal(parameter.getCurrentUse()));
			pparsFilters.add(filter);
		}

		if (parameter.getProposedUse() == 7) {
			Filter filter = ff.equals(ff.property("proposedus"),
					ff.literal(parameter.getProposedUse()));
			pparsFilters.add(filter);
		} else if (parameter.getProposedUse() == -7) {
			Filter filter = ff.notEqual(ff.property("proposedus"),
					ff.literal(parameter.getProposedUse()));
			pparsFilters.add(filter);
		}

		if (parameter.getEstimatedCostOfWork() != 0) {
			String operator = parameter.getEstimatedCostOfWorkOperateor();
			Filter filter = null;
			switch (operator.charAt(0)) {
			case '>':
				filter = ff.greater(ff.property("estimatedc"),
						ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			case '=':
				filter = ff.equals(ff.property("estimatedc"),
						ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			case '<':
				filter = ff.less(ff.property("estimatedc"),
						ff.literal(parameter.getEstimatedCostOfWork()));
				break;
			}
			pparsFilters.add(filter);
		}

		if (parameter.getPreMeeting() == 2) {
			Filter filter = ff.equals(ff.property("premeeting"),
					ff.literal(parameter.getPreMeeting()));
			pparsFilters.add(filter);
		}

		if (parameter.getSelectedOutcome() != -1) {
			Filter filter = ff.less(ff.property("outcome"),
					ff.literal(parameter.getSelectedOutcome()));
			pparsFilters.add(filter);
		}

		Filter lgaFilter = null;
		if (!parameter.getSelectedLGAs2().isEmpty()){
			for (String lgaCode : parameter.getSelectedLGAs2()) {
				Filter filter = ff.equals(ff.property("lga_code"),ff.literal(lgaCode));
				lgaFilters.add(filter);
			}
			lgaFilter = ff.or(lgaFilters);
			System.out.println("lgaFilter=" + lgaFilter);
		}
		
		String geomName = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
		Filter pparsFilter = ff.and(pparsFilters);
		SimpleFeatureCollection pparsCollection = pparsFc.getFeatures(pparsFilter);
		System.out.println("collection.size()" + pparsCollection.size());
		SimpleFeatureIterator pparsIterator = pparsCollection.features();
		Filter filter;
		while (pparsIterator.hasNext()) {
			SimpleFeature ppars = pparsIterator.next();
			Geometry pparsGeom = (Geometry) ppars.getDefaultGeometry();
			filter = ff.intersects(ff.property(geomName),ff.literal(pparsGeom)); 
			if (lgaFilter != null){
				filter = ff.and(lgaFilter, filter);
			}
			propertyFilters.add(filter);
		}
		filter = null;
		Filter propertyFilter = ff.or(propertyFilters);
		SimpleFeatureCollection propertyCollection = propertyFc.getFeatures(propertyFilter);
		System.out.println("propertyCollection.size= "+ propertyCollection.size());

		if (propertyCollection.isEmpty()){
			LOGGER.info("No Properties Found!");
			Messages.setMessage(Messages._NO_FEATURE);
			return false;
		}
		// Export to ShapeFile
		File newFile = new File("C:/Programming/"+geoServerConfig.getGsAssessmentLayer()+"/"+geoServerConfig.getGsAssessmentLayer()+".shp");
		System.out.println(newFile.toURI());
		SimpleFeatureType sft = propertyFc.getSchema();
		stb = featureBuilder.createFeatureTypeBuilder(sft, "_LAYER");	
		newFeatureType = stb.buildFeatureType();		 
		exportService.featuresExportToShapeFile(newFeatureType, propertyCollection,newFile, true);
		ReferencedEnvelope envelope = propertyCollection.getBounds();

		if (!publishToGeoserver()){
			return false;	
		}
		return true;
	}


	private boolean publishToGeoserver() throws FileNotFoundException, IllegalArgumentException, MalformedURLException{
//		try{
			GeoServerRESTReader reader = 
					new GeoServerRESTReader(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
			GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
			if (!reader.existGeoserver()){
				Messages.setMessage(Messages._GEOSERVER_NOT_EXIST);
				return false;
			}

			//remove layer and datastore
			if (reader.getLayer(geoServerConfig.getGsAssessmentLayer())!=null){			
				boolean ftRemoved = publisher.unpublishFeatureType(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore(),geoServerConfig.getGsAssessmentLayer());
				publisher.reload();
				publisher.removeLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentLayer());
				publisher.reload();
				boolean dsRemoved = publisher.removeDatastore(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentDatastore(), true);
				publisher.reload();			
			}

			// Create Zip File 	
			Zip zip = new Zip("C:\\Programming\\"+ geoServerConfig.getGsAssessmentLayer() +".zip", "C:\\Programming\\"+ geoServerConfig.getGsAssessmentLayer());
			zip.createZip();

			//	 boolean created = publisher.createWorkspace(geoServerConfig.getGsWorkspace());
			File zipFile = new File("C:/Programming/"+ geoServerConfig.getGsAssessmentLayer() +".zip");
			boolean published = publisher.publishShp(geoServerConfig.getGsWorkspace(),geoServerConfig.getGsAssessmentDatastore(),geoServerConfig.getGsAssessmentLayer(), zipFile, "EPSG:28355",geoServerConfig.getGsAssessmentStyle());

			GSLayerEncoder le = new GSLayerEncoder();
			le.setDefaultStyle(geoServerConfig.getGsAssessmentStyle());
			publisher.configureLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsAssessmentLayer(), le);

			System.out.println("Publishsed Success");
//		}catch(Exception e){
//			Messages.setMessage(Messages._ERROR);
//			return false;
//		}
		return true;
	}

}










//boolean ftRemoved = publisher.unpublishFeatureType("myws3",
// "PostGIS_DSN", "ppars");
// publisher.removeLayer("myws3", "ppars");
// boolean dsRemoved = publisher.removeDatastore("PostGIS_DSN",
// "ppars");
// publisher.reload();
// GSPostGISDatastoreEncoder datastoreEncoder = new
// GSPostGISDatastoreEncoder();
// datastoreEncoder.setName("PostGIS_DSN");
// datastoreEncoder.setHost("localhost");
// datastoreEncoder.setPort(5432);
// datastoreEncoder.setDatabase("housingmetric");
// datastoreEncoder.setSchema("public");
// datastoreEncoder.setUser("postgres");
// datastoreEncoder.setPassword("1q2w3e4r");
// datastoreEncoder.setExposePrimaryKeys(true);
// datastoreEncoder.setValidateConnections(false);
// datastoreEncoder.setPrimaryKeyMetadataTable("test");
//
// publisher.createPostGISDatastore("myws3", datastoreEncoder);
//
// boolean published = publisher.publishDBLayer("myws3", "PostGIS_DSN",
// "ppars", "EPSG:28355", "default_polygon");

/*
 * } catch (Exception e) {
 * LOGGER.debug("-------FileNotFoundException = "+ e.getMessage()); }
 * catch(IllegalArgumentException e){
 * LOGGER.debug("---------IllegalArgumentException = "+ e.getMessage());
 * }
 */