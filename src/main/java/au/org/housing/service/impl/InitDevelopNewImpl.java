package au.org.housing.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.ParameterDevelopNew;
import au.org.housing.service.InitDevelopNew;

/**
 * Implementation for initializing ParameterDevelopPotential
 * model based on the selected parameters by user and to be used
 * in other handler services.
 *
 * @author Ali
 * @version 1.0
 *
 */ 

@Service
public class InitDevelopNewImpl implements InitDevelopNew {

	@Autowired
	private ParameterDevelopNew parameter;		

	public void initParams(Map<String, Object> params) {
	
		
		parameter.setHouseprice_income_ratio(Double.parseDouble(params.get("houseprice_income_ratio_val").toString()));
		parameter.setRental_income_ratio(Double.parseDouble(params.get("rental_income_ratio_val").toString()));
		parameter.setInterest_mortgage(Double.parseDouble(params.get("interest_mortgage_val").toString()));
        parameter.setSocial_private_rented(Double.parseDouble(params.get("social_private_rented_val").toString()));
	    parameter.setHomeownership_products(Double.parseDouble(params.get("homeownership_products_val").toString()));	    
		parameter.setCrime_sla_sd(Double.parseDouble(params.get("crime_sla_sd_val").toString()));
		parameter.setCount_employment(Double.parseDouble(params.get("count_employment_val").toString()));
		parameter.setPublic_transport(Double.parseDouble(params.get("public_transport_val").toString()));
		parameter.setCount_educationcentre(Double.parseDouble(params.get("count_educationcentre_val").toString()));
		parameter.setCommercial_count(Double.parseDouble(params.get("commercial_count_val").toString()));
		parameter.setCount_healthservice(Double.parseDouble(params.get("count_healthservice_val").toString()));
		parameter.setChildcare_count(Double.parseDouble(params.get("childcare_count_val").toString()));
		parameter.setLeisure_count(Double.parseDouble(params.get("leisure_count_val").toString()));
		parameter.setGreenpublicspace_area(Double.parseDouble(params.get("greenpublicspace_area_val").toString()));
		parameter.setHousing_quality_median(Double.parseDouble(params.get("housing_quality_median_val").toString()));
		parameter.setEnergy_efficiency(Double.parseDouble(params.get("energy_efficiency_val").toString()));
		parameter.setLandfill_count(Double.parseDouble(params.get("landfill_count_val").toString()));
		parameter.setDesirability(Double.parseDouble(params.get("desirability_val").toString()));
		parameter.setSum_prop_decile123(Double.parseDouble(params.get("sum_prop_decile123_val").toString()));
		parameter.setEnvironmental_problem(Double.parseDouble(params.get("environmental_problem_val").toString()));

		//parameter.setSelectedLGAs3((List<String>) params.get("selectedLGAs3"));
		//System.out.println("selected LGAs=="+ parameter.getSelectedLGAs3());

	}
	
	public void initParamsCheks(Map<String, Object> params) {
	
		
		parameter.setChk_houseprice_income_ratio(Boolean.parseBoolean(params.get("houseprice_income_ratio_val").toString()));
		parameter.setChk_rental_income_ratio(Boolean.parseBoolean(params.get("rental_income_ratio_val").toString()));
		parameter.setChk_interest_mortgage(Boolean.parseBoolean(params.get("interest_mortgage_val").toString()));
        parameter.setChk_social_private_rented(Boolean.parseBoolean(params.get("social_private_rented_val").toString()));
	    parameter.setChk_homeownership_products(Boolean.parseBoolean(params.get("homeownership_products_val").toString()));	    
		parameter.setChk_crime_sla_sd(Boolean.parseBoolean(params.get("crime_sla_sd_val").toString()));
		parameter.setChk_count_employment(Boolean.parseBoolean(params.get("count_employment_val").toString()));
		parameter.setChk_public_transport(Boolean.parseBoolean(params.get("public_transport_val").toString()));
		parameter.setChk_count_educationcentre(Boolean.parseBoolean(params.get("count_educationcentre_val").toString()));
		parameter.setChk_commercial_count(Boolean.parseBoolean(params.get("commercial_count_val").toString()));
		parameter.setChk_count_healthservice(Boolean.parseBoolean(params.get("count_healthservice_val").toString()));
		parameter.setChk_childcare_count(Boolean.parseBoolean(params.get("childcare_count_val").toString()));
		parameter.setChk_leisure_count(Boolean.parseBoolean(params.get("leisure_count_val").toString()));
		parameter.setChk_greenpublicspace_area(Boolean.parseBoolean(params.get("greenpublicspace_area_val").toString()));
		parameter.setChk_housing_quality_median(Boolean.parseBoolean(params.get("housing_quality_median_val").toString()));
		parameter.setChk_energy_efficiency(Boolean.parseBoolean(params.get("energy_efficiency_val").toString()));
		parameter.setChk_landfill_count(Boolean.parseBoolean(params.get("landfill_count_val").toString()));
		parameter.setChk_desirability(Boolean.parseBoolean(params.get("desirability_val").toString()));
		parameter.setChk_sum_prop_decile123(Boolean.parseBoolean(params.get("sum_prop_decile123_val").toString()));
		parameter.setChk_environmental_problem(Boolean.parseBoolean(params.get("environmental_problem_val").toString()));

		//parameter.setSelectedLGAs3((List<String>) params.get("selectedLGAs3"));
		//System.out.println("selected LGAs=="+ parameter.getSelectedLGAs3());

	}	

}
