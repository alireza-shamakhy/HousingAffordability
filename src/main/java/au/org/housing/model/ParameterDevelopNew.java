/*
 * Copyright 2002-2012 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package au.org.housing.model;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Contains all the parameters on Assessment New form.
 *
 * @author Ali
 * @version 1.0
 *
 */ 


@Component
public class ParameterDevelopNew {

	//private List<String> selectedLGAs;	

		
	double houseprice_income_ratio = 0;  	
	double rental_income_ratio = 0; 
	double interest_mortgage = 0;
	double social_private_rented = 0;
	double homeownership_products = 0;
	double crime_sla_sd = 0;
	double count_employment = 0;	
	double public_transport = 0;
	double count_educationcentre = 0;
	double commercial_count = 0;
	double count_healthservice = 0;
	double childcare_count = 0;
	double leisure_count = 0;
	double greenpublicspace_area = 0;
	double housing_quality_median = 0;	
	double energy_efficiency = 0; 
	double landfill_count = 0;
	double desirability = 0;
	double sum_prop_decile123 = 0;
	double environmental_problem = 0;
	
	
	boolean chk_houseprice_income_ratio = false;  	
	boolean chk_rental_income_ratio = false; 
	boolean chk_interest_mortgage = false;
	boolean chk_social_private_rented = false;
	boolean chk_homeownership_products = false;
	boolean chk_crime_sla_sd = false;
	boolean chk_count_employment = false;	
	boolean chk_public_transport = false;
	boolean chk_count_educationcentre = false;
	boolean chk_commercial_count = false;
	boolean chk_count_healthservice = false;
	boolean chk_childcare_count = false;
	boolean chk_leisure_count = false;
	boolean chk_greenpublicspace_area = false;
	boolean chk_housing_quality_median = false;	
	boolean chk_energy_efficiency = false; 
	boolean chk_landfill_count = false;
	boolean chk_desirability = false;
	boolean chk_sum_prop_decile123 = false;
	boolean chk_environmental_problem = false;

	
	public double getHouseprice_income_ratio() {
		return houseprice_income_ratio;
	}
	public void setHouseprice_income_ratio(double houseprice_income_ratio) {
		this.houseprice_income_ratio = houseprice_income_ratio;
	}
	
	public double getRental_income_ratio() {
		return rental_income_ratio;
	}
	public void setRental_income_ratio(double Rental_income_ratio) {
		this.rental_income_ratio = Rental_income_ratio;
	}
		public double getInterest_mortgage() {
		return interest_mortgage;
	}
	public void setInterest_mortgage(double interest_mortgage) {
		this.interest_mortgage = interest_mortgage;
	}
	public double getSocial_private_rented() {
		return social_private_rented;
	}
	public void setSocial_private_rented(double social_private_rented) {
		this.social_private_rented = social_private_rented;
	}
	public double getHomeownership_products() {
		return homeownership_products;
	}
	public void setHomeownership_products(double homeownership_products) {
		this.homeownership_products = homeownership_products;
	}
	public double getCrime_sla_sd() {
		return crime_sla_sd;
	}
	public void setCrime_sla_sd(double crime_sla_sd) {
		this.crime_sla_sd = crime_sla_sd;
	}
	public double getCount_employment() {
		return count_employment;
	}
	public void setCount_employment(double count_employment) {
		this.count_employment = count_employment;
	}
	public double getPublic_transport() {
		return public_transport;
	}
	public void setPublic_transport(double public_transport) {
		this.public_transport = public_transport;
	}
	public double getCount_educationcentre() {
		return count_educationcentre;
	}
	public void setCount_educationcentre(double count_educationcentre) {
		this.count_educationcentre = count_educationcentre;
	}
	public double getCommercial_count() {
		return commercial_count;
	}
	public void setCommercial_count(double commercial_count) {
		this.commercial_count = commercial_count;
	}
	public double getCount_healthservice() {
		return count_healthservice;
	}
	public void setCount_healthservice(double count_healthservice) {
		this.count_healthservice = count_healthservice;
	}
	public double getChildcare_count() {
		return childcare_count;
	}
	public void setChildcare_count(double childcare_count) {
		this.childcare_count = childcare_count;
	}
	public double getLeisure_count() {
		return leisure_count;
	}
	public void setLeisure_count(double leisure_count) {
		this.leisure_count = leisure_count;
	}
	public double getGreenpublicspace_area() {
		return greenpublicspace_area;
	}
	public void setGreenpublicspace_area(double greenpublicspace_area) {
		this.greenpublicspace_area = greenpublicspace_area;
	}
	public double getEnergy_efficiency() {
		return energy_efficiency;
	}
	public void setEnergy_efficiency(double energy_efficiency) {
		this.energy_efficiency = energy_efficiency;
	}
	public double getLandfill_count() {
		return landfill_count;
	}
	public void setLandfill_count(double landfill_count) {
		this.landfill_count = landfill_count;
	}
	public double getDesirability() {
		return desirability;
	}
	public void setDesirability(double desirability) {
		this.desirability = desirability;
	}
	public double getSum_prop_decile123() {
		return sum_prop_decile123;
	}
	public void setSum_prop_decile123(double sum_prop_decile123) {
		this.sum_prop_decile123 = sum_prop_decile123;
	}
	public double getEnvironmental_problem() {
		return environmental_problem;
	}
	public void setEnvironmental_problem(double environmental_problem) {
		this.environmental_problem = environmental_problem;
	}
	public double getHousing_quality_median() {
		return housing_quality_median;
	}
	public void setHousing_quality_median(double housing_quality_median) {
		this.housing_quality_median = housing_quality_median;
	}
	public boolean isChk_houseprice_income_ratio() {
		return chk_houseprice_income_ratio;
	}
	public void setChk_houseprice_income_ratio(boolean chk_houseprice_income_ratio) {
		this.chk_houseprice_income_ratio = chk_houseprice_income_ratio;
	}
	public boolean isChk_rental_income_ratio() {
		return chk_rental_income_ratio;
	}
	public void setChk_rental_income_ratio(boolean chk_rental_income_ratio) {
		this.chk_rental_income_ratio = chk_rental_income_ratio;
	}
	public boolean isChk_interest_mortgage() {
		return chk_interest_mortgage;
	}
	public void setChk_interest_mortgage(boolean chk_interest_mortgage) {
		this.chk_interest_mortgage = chk_interest_mortgage;
	}
	public boolean isChk_social_private_rented() {
		return chk_social_private_rented;
	}
	public void setChk_social_private_rented(boolean chk_social_private_rented) {
		this.chk_social_private_rented = chk_social_private_rented;
	}
	public boolean isChk_homeownership_products() {
		return chk_homeownership_products;
	}
	public void setChk_homeownership_products(boolean chk_homeownership_products) {
		this.chk_homeownership_products = chk_homeownership_products;
	}
	public boolean isChk_crime_sla_sd() {
		return chk_crime_sla_sd;
	}
	public void setChk_crime_sla_sd(boolean chk_crime_sla_sd) {
		this.chk_crime_sla_sd = chk_crime_sla_sd;
	}
	public boolean isChk_count_employment() {
		return chk_count_employment;
	}
	public void setChk_count_employment(boolean chk_count_employment) {
		this.chk_count_employment = chk_count_employment;
	}
	public boolean isChk_public_transport() {
		return chk_public_transport;
	}
	public void setChk_public_transport(boolean chk_public_transport) {
		this.chk_public_transport = chk_public_transport;
	}
	public boolean isChk_count_educationcentre() {
		return chk_count_educationcentre;
	}
	public void setChk_count_educationcentre(boolean chk_count_educationcentre) {
		this.chk_count_educationcentre = chk_count_educationcentre;
	}
	public boolean isChk_commercial_count() {
		return chk_commercial_count;
	}
	public void setChk_commercial_count(boolean chk_commercial_count) {
		this.chk_commercial_count = chk_commercial_count;
	}
	public boolean isChk_count_healthservice() {
		return chk_count_healthservice;
	}
	public void setChk_count_healthservice(boolean chk_count_healthservice) {
		this.chk_count_healthservice = chk_count_healthservice;
	}
	public boolean isChk_childcare_count() {
		return chk_childcare_count;
	}
	public void setChk_childcare_count(boolean chk_childcare_count) {
		this.chk_childcare_count = chk_childcare_count;
	}
	public boolean isChk_leisure_count() {
		return chk_leisure_count;
	}
	public void setChk_leisure_count(boolean chk_leisure_count) {
		this.chk_leisure_count = chk_leisure_count;
	}
	public boolean isChk_greenpublicspace_area() {
		return chk_greenpublicspace_area;
	}
	public void setChk_greenpublicspace_area(boolean chk_greenpublicspace_area) {
		this.chk_greenpublicspace_area = chk_greenpublicspace_area;
	}
	public boolean isChk_housing_quality_median() {
		return chk_housing_quality_median;
	}
	public void setChk_housing_quality_median(boolean chk_housing_quality_median) {
		this.chk_housing_quality_median = chk_housing_quality_median;
	}
	public boolean isChk_energy_efficiency() {
		return chk_energy_efficiency;
	}
	public void setChk_energy_efficiency(boolean chk_energy_efficiency) {
		this.chk_energy_efficiency = chk_energy_efficiency;
	}
	public boolean isChk_landfill_count() {
		return chk_landfill_count;
	}
	public void setChk_landfill_count(boolean chk_landfill_count) {
		this.chk_landfill_count = chk_landfill_count;
	}
	public boolean isChk_desirability() {
		return chk_desirability;
	}
	public void setChk_desirability(boolean chk_desirability) {
		this.chk_desirability = chk_desirability;
	}
	public boolean isChk_sum_prop_decile123() {
		return chk_sum_prop_decile123;
	}
	public void setChk_sum_prop_decile123(boolean chk_sum_prop_decile123) {
		this.chk_sum_prop_decile123 = chk_sum_prop_decile123;
	}
	public boolean isChk_environmental_problem() {
		return chk_environmental_problem;
	}
	public void setChk_environmental_problem(boolean chk_environmental_problem) {
		this.chk_environmental_problem = chk_environmental_problem;
	}
	


	/*
	public List<String> getSelectedLGAs3() {
		return selectedLGAs;
	}
	public void setSelectedLGAs3(List<String> selectedLGAs) {
		this.selectedLGAs = selectedLGAs;
	}
*/
	
}
