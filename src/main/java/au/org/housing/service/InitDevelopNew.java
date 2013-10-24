package au.org.housing.service;

import java.util.Map;


/**
 * Interface for initializing ParameterDevelopNew
 * model based on the selected parameters by user and to be used
 * in other handler services.
 *
 * @author Ali
 * @version 1.0
 *
 */ 

public interface InitDevelopNew {
	
	void initParams(Map<String, Object> housingParams);
	void initParamsCheks(Map<String, Object> housingParams);

}
