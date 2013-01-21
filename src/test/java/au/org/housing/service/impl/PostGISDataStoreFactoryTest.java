package au.org.housing.service.impl;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import junit.framework.TestCase;

public class PostGISDataStoreFactoryTest extends TestCase {

	public PostGISDataStoreFactoryTest(String name) {
		super(name);
	}

	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {		
		super.tearDown();
	}

	public void testGetDataStore() throws IOException {
		PostGISDataStoreFactoryImpl postGISDataStoreFactoryImpl = new PostGISDataStoreFactoryImpl();
		DataStore dataStoreProperty = postGISDataStoreFactoryImpl.getDataStore(MapAttImpl.trainStation);
		assertNotNull(dataStoreProperty);
		DataStore postGISStore = postGISDataStoreFactoryImpl.getPOSTGISDataStore();
		assertEquals(dataStoreProperty, postGISStore);
	}

	public void testGetPOSTGISDataStore() {
		try{
			PostGISDataStoreFactoryImpl postGISDataStoreFactoryImpl = new PostGISDataStoreFactoryImpl();
			DataStore ds = postGISDataStoreFactoryImpl.getPOSTGISDataStore();
			SimpleFeatureSource featureSource = ds.getFeatureSource(MapAttImpl.trainStation);
			Query query = new Query();
			query.setMaxFeatures(1);
			SimpleFeatureCollection features = featureSource.getFeatures(query);
			SimpleFeatureIterator it = features.features();
			SimpleFeature simpleFeature = it.next(); 
			assertNotNull(simpleFeature);
		}catch(IOException e){
			fail(e.getMessage());
		}
	}

	
}