var analyseBtn_NewtableRight = Ext.create('Ext.Button', {
	text : 'Show in Table',
	margin : '0 5 8 5',
	scale   : 'large',
	handler : function() {		

//	// generate a unique taskId for this request, so that it can
//// be used as a link while checking the progress
//var taskId=Ext.Date.format(new Date,'Hisu-');
// 
//// a boolean flag to decide when to stop checking progress
//var getProgressBoo=true;
	
	
		

   if (rightForm_New.getForm().isValid())
				 {
				
			var houseprice_income_ratio_val = JSON.parse(geo.getForm().findField('chk_houseprice_income_ratio').getValue());			
			var rental_income_ratio_val = JSON.parse(geo.getForm().findField('chk_rental_income_ratio').getValue());
			var interest_mortgage_val = JSON.parse(geo.getForm().findField('chk_interest_mortgage').getValue());
			var social_private_rented_val = JSON.parse(geo.getForm().findField('chk_social_private_rented').getValue());
			var homeownership_products_val = JSON.parse(geo.getForm().findField('chk_homeownership_products').getValue());
			var crime_sla_sd_val = JSON.parse(geo.getForm().findField('chk_crime_sla_sd').getValue());
			var count_employment_val = JSON.parse(geo.getForm().findField('chk_count_employment').getValue());
			var public_transport_val = JSON.parse(geo.getForm().findField('chk_public_transport').getValue());
			var count_educationcentre_val = JSON.parse(geo.getForm().findField('chk_count_educationcentre').getValue());			
			var commercial_count_val = JSON.parse(geo.getForm().findField('chk_commercial_count').getValue());
			var count_healthservice_val = JSON.parse(geo.getForm().findField('chk_count_healthservice').getValue());
			var childcare_count_val = JSON.parse(geo.getForm().findField('chk_childcare_count').getValue());
			var leisure_count_val = JSON.parse(geo.getForm().findField('chk_leisure_count').getValue());
			var greenpublicspace_area_val = JSON.parse(geo.getForm().findField('chk_greenpublicspace_area').getValue());
			var housing_quality_median_val = JSON.parse(geo.getForm().findField('chk_housing_quality_median').getValue());
			var energy_efficiency_val = JSON.parse(geo.getForm().findField('chk_energy_efficiency').getValue());
			var landfill_count_val = JSON.parse(geo.getForm().findField('chk_landfill_count').getValue());
			var desirability_val = JSON.parse(geo.getForm().findField('chk_desirability').getValue());
			var sum_prop_decile123_val = JSON.parse(geo.getForm().findField('chk_sum_prop_decile123').getValue());
			var environmental_problem_val = JSON.parse(geo.getForm().findField('chk_environmental_problem').getValue());

			cnt=0;
			
			 if(houseprice_income_ratio_val  == true)
	            	cnt = cnt + 1;
	            if(rental_income_ratio_val  == true)
	            	cnt = cnt + 1;                                      
	            if(interest_mortgage_val  == true)
	            	cnt = cnt + 1;
	            if(social_private_rented_val  == true)
	            	cnt = cnt + 1;
	            if(homeownership_products_val  == true)
	            	cnt = cnt + 1;
	            if(crime_sla_sd_val  == true)
	            	cnt = cnt + 1;
	            if(count_employment_val  == true)
	            	cnt = cnt + 1;	
	            if(public_transport_val  == true)
	            	cnt = cnt + 1;
	            if(count_educationcentre_val  == true)
	            	cnt = cnt + 1;
	            if(commercial_count_val  == true)
	            	cnt = cnt + 1;
	            if(count_healthservice_val  == true)
	            	cnt = cnt + 1;
	            if(childcare_count_val  == true)
	            	cnt = cnt + 1;
	            if(leisure_count_val  == true)
	            	cnt = cnt + 1; 
	            if(greenpublicspace_area_val  == true)
	            	cnt = cnt + 1;
	            if(housing_quality_median_val  == true)
	            	cnt = cnt + 1;	
	            if(energy_efficiency_val  == true)
	            	cnt = cnt + 1; 
	            if(landfill_count_val  == true)
	            	cnt = cnt + 1;
	            if(desirability_val  == true)
	            	cnt = cnt + 1;
	            if(sum_prop_decile123_val  == true)
	            	cnt = cnt + 1;
	            if(environmental_problem_val  == true)
	            	cnt = cnt + 1;   
			
			
	            if (cnt > 0)
	            	{
			
							
				
							var waitingMsg4 = Ext.MessageBox.wait('Processing...',
									'Performing Analysis');
				
							Ext.Ajax.request({
										url : '/housing/housing-controller/developmentNewTable',
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
											
											waitingMsg4.hide();
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
																text : 'Show Table',
																handler : function() {
																	window
																			.open(
																					'ui-jsp/map_new_tableright.jsp',
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
											        
											waitingMsg4.hide();
											var jresp = Ext.JSON.decode(response.responseText);
											Ext.MessageBox.alert(jresp.message);
										}
				
									});
			
	            	}
	            else
	            	{
	            	alert("Please select attributes");
	            	}
		} else {
			alert("Please select attributes");
		}
	}
});

var clearBtn_NewtableRight = Ext.create('Ext.Button', {
			text : 'Clear All',
			margin : '0 5 8 5',
			scale   : 'large',
			handler : function() {
					
				Ext.getCmp("chk_houseprice_income_ratio").setValue(false);				
				Ext.getCmp("chk_rental_income_ratio").setValue(false);								
				Ext.getCmp("chk_interest_mortgage").setValue(false);								
				Ext.getCmp("chk_social_private_rented").setValue(false);				
				Ext.getCmp("chk_homeownership_products").setValue(false);								
				Ext.getCmp("chk_crime_sla_sd").setValue(false);								
				Ext.getCmp("chk_count_employment").setValue(false);								
				Ext.getCmp("chk_public_transport").setValue(false);								
				Ext.getCmp("chk_count_educationcentre").setValue(false);								
				Ext.getCmp("chk_commercial_count").setValue(false);								
				Ext.getCmp("chk_count_healthservice").setValue(false);								
				Ext.getCmp("chk_childcare_count").setValue(false);								
				Ext.getCmp("chk_leisure_count").setValue(false);								
				Ext.getCmp("chk_greenpublicspace_area").setValue(false);								
				Ext.getCmp("chk_housing_quality_median").setValue(false);								
				Ext.getCmp("chk_energy_efficiency").setValue(false);								
				Ext.getCmp("chk_landfill_count").setValue(false);								
				Ext.getCmp("chk_desirability").setValue(false);							
				Ext.getCmp("chk_sum_prop_decile123").setValue(false);				
				Ext.getCmp("chk_environmental_problem").setValue(false);
				
					

				//lgaCombo3.clearValue();
				//lgaCombo3.applyEmptyText();
				//lgaCombo3.getPicker().getSelectionModel().doMultiSelect([],false);

			}
		});

var SelectBtn_NewtableRight = Ext.create('Ext.Button', {
	text : 'Select All',
	margin : '0 5 8 5',
	scale   : 'large',
	handler : function() {
			
		Ext.getCmp("chk_houseprice_income_ratio").setValue(true);				
		Ext.getCmp("chk_rental_income_ratio").setValue(true);								
		Ext.getCmp("chk_interest_mortgage").setValue(true);								
		Ext.getCmp("chk_social_private_rented").setValue(true);				
		Ext.getCmp("chk_homeownership_products").setValue(true);								
		Ext.getCmp("chk_crime_sla_sd").setValue(true);								
		Ext.getCmp("chk_count_employment").setValue(true);								
		Ext.getCmp("chk_public_transport").setValue(true);								
		Ext.getCmp("chk_count_educationcentre").setValue(true);								
		Ext.getCmp("chk_commercial_count").setValue(true);								
		Ext.getCmp("chk_count_healthservice").setValue(true);								
		Ext.getCmp("chk_childcare_count").setValue(true);								
		Ext.getCmp("chk_leisure_count").setValue(true);								
		Ext.getCmp("chk_greenpublicspace_area").setValue(true);								
		Ext.getCmp("chk_housing_quality_median").setValue(true);								
		Ext.getCmp("chk_energy_efficiency").setValue(true);								
		Ext.getCmp("chk_landfill_count").setValue(true);								
		Ext.getCmp("chk_desirability").setValue(true);							
		Ext.getCmp("chk_sum_prop_decile123").setValue(true);				
		Ext.getCmp("chk_environmental_problem").setValue(true);
		
			

		//lgaCombo3.clearValue();
		//lgaCombo3.applyEmptyText();
		//lgaCombo3.getPicker().getSelectionModel().doMultiSelect([],false);

	}
});

