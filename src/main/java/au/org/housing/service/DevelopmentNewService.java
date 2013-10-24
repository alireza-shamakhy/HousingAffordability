package au.org.housing.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface for handling New Development analysis.
 *
 * @author Ali
 * @version 1.0
 *
 */ 

public interface DevelopmentNewService {		

	public boolean analyse(String username, HttpSession session)throws Exception;
	
	public boolean analyseTable(String username, HttpSession session)throws Exception;

	public Geometry getBufferAllParams() ;

	public void setBufferAllParams(Geometry bufferAllParams);

	public Map<String, Object> getOutputLayer() ;
	
	public String getStep() ;

	public void setStep(String step) ;
	
	public String getJson();
	
	public String getJsonChart();

	public String getTable();

}
