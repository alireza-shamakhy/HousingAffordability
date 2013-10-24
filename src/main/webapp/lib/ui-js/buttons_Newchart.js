var analyseBtn_Newchart = Ext.create('Ext.Button', {
	text : 'Show in Chart',
	margin : '0 5 8 5',
	scale   : 'large',
	handler : function() {		

//	// generate a unique taskId for this request, so that it can
//// be used as a link while checking the progress
//var taskId=Ext.Date.format(new Date,'Hisu-');
// 
//// a boolean flag to decide when to stop checking progress
//var getProgressBoo=true;
	
	
		
		if (leftForm_New.getForm().isValid()
				&& rightForm_New.getForm().isValid())
				 {
					
			var houseprice_income_ratio_val = JSON.parse(sla.getForm().findField('houseprice_income_ratio_Id').getValue());
			var rental_income_ratio_val = JSON.parse(sla.getForm().findField('rental_income_ratio_Id').getValue());
			var interest_mortgage_val = JSON.parse(sla.getForm().findField('interest_mortgage_Id').getValue());
			var social_private_rented_val = JSON.parse(sla.getForm().findField('social_private_rented_Id').getValue());
			var homeownership_products_val = JSON.parse(sla.getForm().findField('homeownership_products_Id').getValue());
			var crime_sla_sd_val = JSON.parse(sla.getForm().findField('crime_sla_sd_Id').getValue());
			var count_employment_val = JSON.parse(sla.getForm().findField('count_employment_Id').getValue());
			var public_transport_val = JSON.parse(sla.getForm().findField('public_transport_Id').getValue());
			var count_educationcentre_val = JSON.parse(sla.getForm().findField('count_educationcentre_Id').getValue());			
			var commercial_count_val = JSON.parse(sla.getForm().findField('commercial_count_Id').getValue());
			var count_healthservice_val = JSON.parse(sla.getForm().findField('count_healthservice_Id').getValue());
			var childcare_count_val = JSON.parse(sla.getForm().findField('childcare_count_Id').getValue());
			var leisure_count_val = JSON.parse(sla.getForm().findField('leisure_count_Id').getValue());
			var greenpublicspace_area_val = JSON.parse(sla.getForm().findField('greenpublicspace_area_Id').getValue());
			var housing_quality_median_val = JSON.parse(sla.getForm().findField('housing_quality_median_Id').getValue());
			var energy_efficiency_val = JSON.parse(sla.getForm().findField('energy_efficiency_Id').getValue());
			var landfill_count_val = JSON.parse(sla.getForm().findField('landfill_count_Id').getValue());
			var desirability_val = JSON.parse(sla.getForm().findField('desirability_Id').getValue());
			var sum_prop_decile123_val = JSON.parse(sla.getForm().findField('sum_prop_decile123_Id').getValue());
			var environmental_problem_val = JSON.parse(sla.getForm().findField('environmental_problem_Id').getValue());

			
			

			var waitingMsg3 = Ext.MessageBox.wait('Processing...',
					'Performing Analysis');

			Ext.Ajax.request({
						url : '/housing/housing-controller/developmentNew',
						method : 'post',
						waitMsg : 'Saving changes...',
						jsonData : {
							
//							taskIdentity: taskId , // pass the taskId to the server
							
							//selectedLGAs3 : selectedLGAs3,

						  houseprice_income_ratio_val : houseprice_income_ratio_val,
						  rental_income_ratio_val : rental_income_ratio_val,							
						  interest_mortgage_val : interest_mortgage_val,
						  social_private_rented_val :social_private_rented_val, 
						  homeownership_products_val : homeownership_products_val,
						  crime_sla_sd_val : crime_sla_sd_val,
						  count_employment_val : count_employment_val, 
						  public_transport_val : public_transport_val, 
						  count_educationcentre_val : count_educationcentre_val,			
						  commercial_count_val : commercial_count_val, 
						  count_healthservice_val : count_healthservice_val,
						  childcare_count_val : childcare_count_val, 
						  leisure_count_val : leisure_count_val, 
						  greenpublicspace_area_val : greenpublicspace_area_val,
						  housing_quality_median_val : housing_quality_median_val, 
						  energy_efficiency_val : energy_efficiency_val, 
						  landfill_count_val : landfill_count_val, 
						  desirability_val : desirability_val, 
						  sum_prop_decile123_val : sum_prop_decile123_val, 
						  environmental_problem_val : environmental_problem_val 
							
							//floodwayVal1 : floodwayVal1
							
						},
						success : function(response) {
//							Ext.MessageBox.hide();   // hide the progress bar
//            				getProgressBoo=false;  // no need to check progress now
							console.log("33333333333333333");
                         // alert(response.responseText);
            
                          //waitingMsg3.hide();
							var jresp = Ext.JSON.decode(response.responseText);
							console.log(jresp.message);
							//alert (jresp.message);
							
							console.log('NewResponse' + jresp.message);
							
							waitingMsg3.hide();
							if (jresp.successStatus == "success") {
								var showMap = new Ext.Window({
											title : 'Analysis Status',
											height : 100,
											padding : 1,
											width : 300,
											style : {
												"text-align" : "center"
											},
											items : [{
														xtype : 'label',
														text : jresp.message
													}],
											buttons : [{
												text : 'Show Chart',
												handler : function() {
													window
															.open(
																	'ui-jsp/map_new_chart.jsp',
																	"_blank");
													showMap.close();
												}
											}]
										}).show();
							} else {
								Ext.Msg.show({
											title : 'Analysis Status',
											msg : jresp.message,
											width : 400,
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.WARNING
										});
							}
						},
						failure : function(response, options) {
							
//							Ext.MessageBox.hide();   // hide the progress bar
//            getProgressBoo=false;  // no need to check progress now
//            
            
            
							waitingMsg3.hide();
							var jresp = Ext.JSON.decode(response.responseText);
							Ext.MessageBox.alert(jresp.message);
						}

					});
		} else {
			alert("Please assign values to attributes");
		}
	}
});

var clearBtn_Newchart = Ext.create('Ext.Button', {
			text : 'Clear',
			margin : '0 5 8 5',
			scale   : 'large',
			handler : function() {
					
				Ext.getCmp("houseprice_income_ratio_Id").setValue(priority_houseprice_income_ratio);
				Ext.getCmp("houseprice_income_ratio_Id_value").setValue(priority_houseprice_income_ratio);
				Ext.getCmp("rental_income_ratio_Id").setValue(priority_rental_income_ratio);
				Ext.getCmp("rental_income_ratio_Id_value").setValue(priority_rental_income_ratio);				
				Ext.getCmp("interest_mortgage_Id").setValue(priority_interest_mortgage);
				Ext.getCmp("interest_mortgage_Id_value").setValue(priority_interest_mortgage);				
				Ext.getCmp("social_private_rented_Id").setValue(priority_social_private_rented);
				Ext.getCmp("social_private_rented_Id_value").setValue(priority_social_private_rented);
				Ext.getCmp("homeownership_products_Id").setValue(priority_homeownership_products);
				Ext.getCmp("homeownership_products_Id_value").setValue(priority_homeownership_products);				
				Ext.getCmp("crime_sla_sd_Id").setValue(priority_crime_sla_sd);
				Ext.getCmp("crime_sla_sd_Id_value").setValue(priority_crime_sla_sd);				
				Ext.getCmp("count_employment_Id").setValue(priority_count_employment);
				Ext.getCmp("count_employment_Id_value").setValue(priority_count_employment);				
				Ext.getCmp("public_transport_Id").setValue(priority_public_transport);
				Ext.getCmp("public_transport_Id_value").setValue(priority_public_transport);				
				Ext.getCmp("count_educationcentre_Id").setValue(priority_count_educationcentre);
				Ext.getCmp("count_educationcentre_Id_value").setValue(priority_count_educationcentre);				
				Ext.getCmp("commercial_count_Id").setValue(priority_commercial_count);
				Ext.getCmp("commercial_count_Id_value").setValue(priority_commercial_count);				
				Ext.getCmp("count_healthservice_Id").setValue(priority_count_healthservice);
				Ext.getCmp("count_healthservice_Id_value").setValue(priority_count_healthservice);				
				Ext.getCmp("childcare_count_Id").setValue(priority_childcare_count);
				Ext.getCmp("childcare_count_Id_value").setValue(priority_childcare_count);				
				Ext.getCmp("leisure_count_Id").setValue(priority_leisure_count);
				Ext.getCmp("leisure_count_Id_value").setValue(priority_leisure_count);				
				Ext.getCmp("greenpublicspace_area_Id").setValue(priority_greenpublicspace_area);
				Ext.getCmp("greenpublicspace_area_Id_value").setValue(priority_greenpublicspace_area);				
				Ext.getCmp("housing_quality_median_Id").setValue(priority_housing_quality_median);
				Ext.getCmp("housing_quality_median_Id_value").setValue(priority_housing_quality_median);				
				Ext.getCmp("energy_efficiency_Id").setValue(priority_energy_efficiency);
				Ext.getCmp("energy_efficiency_Id_value").setValue(priority_energy_efficiency);				
				Ext.getCmp("landfill_count_Id").setValue(priority_landfill_count);
				Ext.getCmp("landfill_count_Id_value").setValue(priority_landfill_count);				
				Ext.getCmp("desirability_Id").setValue(priority_desirability);
				Ext.getCmp("desirability_Id_value").setValue(priority_desirability);				
				Ext.getCmp("sum_prop_decile123_Id").setValue(priority_sum_prop_decile123);
				Ext.getCmp("sum_prop_decile123_Id_value").setValue(priority_sum_prop_decile123);
				Ext.getCmp("environmental_problem_Id").setValue(priority_environmental_problem);
				Ext.getCmp("environmental_problem_Id_value").setValue(priority_environmental_problem);
				
				


				Ext.getCmp("floodwayIdNew").setValue(false);
				

				//lgaCombo3.clearValue();
				//lgaCombo3.applyEmptyText();
				//lgaCombo3.getPicker().getSelectionModel().doMultiSelect([],false);

			}
		});

