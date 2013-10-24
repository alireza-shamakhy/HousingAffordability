package au.org.housing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Output layer config from its name to its attributes 
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
public class OutPutLayerConfig{
	
	@Value("${outputName}") 
	private String outputName ;	
	
	@Value("${objectid}") 
	private String objectid ;	
	
	@Value("${pfi}") 
	private String pfi ;	
	
	@Value("${lgaName}") 
	private String lgaName ;	
	
	@Value("${street_name}") 
	private String street_name ;	
	
	@Value("${street_type}") 
	private String street_type ;	
	
	@Value("${suburb}") 
	private String suburb ;	
	
	@Value("${postcode}") 
	private String postcode ;
	
	@Value("${land_area}") 
	private String land_area ;
	
	@Value("${areameasure}") 
	private String areameasure ;
	
	//@Value("${zoning}") 
	//private String zoning;
	
	//@Value("${avpcc}") 
	//private String avpcc;	
	
	//@Value("${constructionmaterial}") 
	//private String constructionmaterial;
	
	//@Value("${constructionyear}") 
	//private String constructionyear;
	
	//@Value("${renovationyear}") 
	//private String renovationyear;	
	
	//@Value("${bcc}") 
	//private String bcc;	
	
	//@Value("${floorarea_m2}") 
	//private String floorarea_m2;
	
	//@Value("${numberofbedrooms}") 
	//private String numberofbedrooms;
	
	
	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getPfi() {
		return pfi;
	}

	public void setPfi(String pfi) {
		this.pfi = pfi;
	}

	public String getLgaName() {
		return lgaName;
	}

	public void setLgaName(String lgaName) {
		this.lgaName = lgaName;
	}

	public String getStreet_name() {
		return street_name;
	}

	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}

	public String getStreet_type() {
		return street_type;
	}

	public void setStreet_type(String street_type) {
		this.street_type = street_type;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLand_area() {
		return land_area;
	}

	public void setLand_area(String land_area) {
		this.land_area = land_area;
	}

	public String getAreameasure() {
		return areameasure;
	}

	public void setAreameasure(String areameasure) {
		this.areameasure = areameasure;
	}

	/*
	public String getZoning() {
		return zoning;
	}

	public void setZoning(String zoning) {
		this.zoning = zoning;
	}

	public String getAvpcc() {
		return avpcc;
	}

	public void setAvpcc(String avpcc) {
		this.avpcc = avpcc;
	}

	public String getConstructionmaterial() {
		return constructionmaterial;
	}

	public void setConstructionmaterial(String constructionmaterial) {
		this.constructionmaterial = constructionmaterial;
	}

	public String getConstructionyear() {
		return constructionyear;
	}

	public void setConstructionyear(String constructionyear) {
		this.constructionyear = constructionyear;
	}

	public String getRenovationyear() {
		return renovationyear;
	}

	public void setRenovationyear(String renovationyear) {
		this.renovationyear = renovationyear;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getFloorarea_m2() {
		return floorarea_m2;
	}

	public void setFloorarea_m2(String floorarea_m2) {
		this.floorarea_m2 = floorarea_m2;
	}

	public String getNumberofbedrooms() {
		return numberofbedrooms;
	}

	public void setNumberofbedrooms(String numberofbedrooms) {
		this.numberofbedrooms = numberofbedrooms;
	}
	*/
		
}