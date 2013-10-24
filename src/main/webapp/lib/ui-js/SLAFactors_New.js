
var priority_houseprice_income_ratio = 8.66; //HousePrice_Income_Ratio
var priority_rental_income_ratio = 8.69;     //Rental_Income_Ratio
var priority_interest_mortgage = 7.18;       //Average loan-to-value ratio
var priority_social_private_rented = 7.71;   //social rented
var priority_homeownership_products = 7.09;  //Housing Stock
var priority_crime_sla_sd = 6.62;            //Crime_SLA_SD
var priority_count_employment = 7.49;		 //Count_Employment	
var priority_public_transport = 6.8;         //Public_transport
var priority_count_educationcentre = 6.75;   //Count_EducationCentre
var priority_commercial_count = 6.7;         //Commercial_Count
var priority_count_healthservice = 6.78;     //Count_HealthService
var priority_childcare_count = 5.42;         //Childcare_Count
var priority_leisure_count = 4.94;           //Leisure_Count
var priority_greenpublicspace_area = 5.66;   //GreenPublicSpace_Area
var priority_housing_quality_median = 8.36;  //Housing_Quality_Median	
var priority_energy_efficiency = 7.42;       //Energy Efficiency 
var priority_landfill_count = 4.52;          //LandFill_Count
var priority_desirability = 5.98;            //desirability
var priority_sum_prop_decile123 = 6.89;      //Sum_Prop_Decile123 (Deprivation)
var priority_environmental_problem = 6.71;   //Environmental_problem

//houseprice_income_ratio 
var houseprice_income_ratio = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'houseprice_income_ratio_Id_value',
				text : 'House price in relation to incomes :',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'houseprice_income_ratio_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_houseprice_income_ratio,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("houseprice_income_ratio_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("houseprice_income_ratio_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'houseprice_income_ratio_Id',
				width : '58%',
				increment : 0.01,
				value : priority_houseprice_income_ratio,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("houseprice_income_ratio_Id_value").setValue(thumb);
					}
				}
			}]
});

//rental_income_ratio 
var rental_income_ratio = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'rental_income_ratio_Id_value',
				text : 'Rental Costs in relation to incomes:',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'rental_income_ratio_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_rental_income_ratio,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("rental_income_ratio_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("rental_income_ratio_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'rental_income_ratio_Id',
				width : '58%',
				increment : 0.01,
				value : priority_rental_income_ratio,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("rental_income_ratio_Id_value").setValue(thumb);
					}
				}
			}]
});


//interest_mortgage 
var interest_mortgage = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'interest_mortgage_Id_value',
				text : 'Interest rates and mortgage availability :',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'interest_mortgage_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_interest_mortgage,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("interest_mortgage_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("interest_mortgage_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'interest_mortgage_Id',
				width : '58%',
				increment : 0.01,
				value : priority_interest_mortgage,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("interest_mortgage_Id_value").setValue(thumb);
					}
				}
			}]
});


//social_private_rented
var social_private_rented= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'social_private_rented_Id_value',
				text : 'Availability of private and social rented accomodation:',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'social_private_rented_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_social_private_rented,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("social_private_rented_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("social_private_rented_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'social_private_rented_Id',
				width : '58%',
				increment : 0.01,
				value : priority_social_private_rented,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("social_private_rented_Id_value").setValue(thumb);
					}
				}
			}]
});


//homeownership_products
var homeownership_products= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'homeownership_products_Id_value',
				text : 'Availability of affordable homeownership products:',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'homeownership_products_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_homeownership_products,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("homeownership_products_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("homeownership_products_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'homeownership_products_Id',
				width : '58%',
				increment : 0.01,
				value : priority_homeownership_products,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("homeownership_products_Id_value").setValue(thumb);
					}
				}
			}]
});


//crime_sla_sd
var crime_sla_sd= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'crime_sla_sd_Id_value',
				text : 'Safety (Crime):',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'crime_sla_sd_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_crime_sla_sd,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("crime_sla_sd_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("crime_sla_sd_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'crime_sla_sd_Id',
				width : '58%',
				increment : 0.01,
				value : priority_crime_sla_sd,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("crime_sla_sd_Id_value").setValue(thumb);
					}
				}
			}]
});


//count_employment
var count_employment= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'count_employment_Id_value',
				text : 'Access to employment',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'count_employment_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_count_employment,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("count_employment_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("count_employment_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'count_employment_Id',
				width : '58%',
				increment : 0.01,
				value : priority_count_employment,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("count_employment_Id_value").setValue(thumb);
					}
				}
			}]
});

//public_transport
var public_transport= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'public_transport_Id_value',
				text : 'Access to public transport services',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'public_transport_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_public_transport,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("public_transport_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("public_transport_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'public_transport_Id',
				width : '58%',
				increment : 0.01,
				value : priority_public_transport,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("public_transport_Id_value").setValue(thumb);
					}
				}
			}]
});

//count_educationcentre
var count_educationcentre= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'count_educationcentre_Id_value',
				text : 'Access to good quality schools',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'count_educationcentre_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_count_educationcentre,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("count_educationcentre_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("count_educationcentre_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'count_educationcentre_Id',
				width : '58%',
				increment : 0.01,
				value : priority_count_educationcentre,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("count_educationcentre_Id_value").setValue(thumb);
					}
				}
			}]
});

//commercial_count
var commercial_count= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'commercial_count_Id_value',
				text : 'Access to shops',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'commercial_count_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_commercial_count,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("commercial_count_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("commercial_count_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'commercial_count_Id',
				width : '58%',
				increment : 0.01,
				value : priority_commercial_count,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("commercial_count_Id_value").setValue(thumb);
					}
				}
			}]
});

//count_healthservice
var count_healthservice= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'count_healthservice_Id_value',
				text : 'Access to health services',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'count_healthservice_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_count_healthservice,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("count_healthservice_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("count_healthservice_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'count_healthservice_Id',
				width : '58%',
				increment : 0.01,
				value : priority_count_healthservice,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("count_healthservice_Id_value").setValue(thumb);
					}
				}
			}]
});

//childcare_count
var childcare_count= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'childcare_count_Id_value',
				text : 'Access to child care',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'childcare_count_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_childcare_count,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("childcare_count_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("childcare_count_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'childcare_count_Id',
				width : '58%',
				increment : 0.01,
				value : priority_childcare_count,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("childcare_count_Id_value").setValue(thumb);
					}
				}
			}]
});

//leisure_count
var leisure_count= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'leisure_count_Id_value',
				text : 'Access to leisure',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'leisure_count_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_leisure_count,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("leisure_count_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("leisure_count_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'leisure_count_Id',
				width : '58%',
				increment : 0.01,
				value : priority_leisure_count,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("leisure_count_Id_value").setValue(thumb);
					}
				}
			}]
});

//greenpublicspace_area
var greenpublicspace_area= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'greenpublicspace_area_Id_value',
				text : 'Access to open green public space',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'greenpublicspace_area_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_greenpublicspace_area,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("greenpublicspace_area_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("greenpublicspace_area_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'greenpublicspace_area_Id',
				width : '58%',
				increment : 0.01,
				value : priority_greenpublicspace_area,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("greenpublicspace_area_Id_value").setValue(thumb);
					}
				}
			}]
});

//housing_quality_median
var housing_quality_median= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'housing_quality_median_Id_value',
				text : 'Quality of housing in area',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'housing_quality_median_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_housing_quality_median,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("housing_quality_median_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("housing_quality_median_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'housing_quality_median_Id',
				width : '58%',
				increment : 0.01,
				value : priority_housing_quality_median,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("housing_quality_median_Id_value").setValue(thumb);
					}
				}
			}]
});


//energy_efficiency
var energy_efficiency= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'energy_efficiency_Id_value',
				text : 'Energy efficency of housing in area',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'energy_efficiency_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_energy_efficiency,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("energy_efficiency_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("energy_efficiency_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'energy_efficiency_Id',
				width : '58%',
				increment : 0.01,
				value : priority_energy_efficiency,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("energy_efficiency_Id_value").setValue(thumb);
					}
				}
			}]
});


//landfill_count
var landfill_count= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'landfill_count_Id_value',
				text : 'Waste management in area',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'landfill_count_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_landfill_count,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("landfill_count_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("landfill_count_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'landfill_count_Id',
				width : '58%',
				increment : 0.01,
				value : priority_landfill_count,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("landfill_count_Id_value").setValue(thumb);
					}
				}
			}]
});

//desirability
var desirability= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'desirability_Id_value',
				text : 'Desirability of neighborhood area',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'desirability_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_desirability,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("desirability_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("desirability_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'desirability_Id',
				width : '58%',
				increment : 0.01,
				value : priority_desirability,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("desirability_Id_value").setValue(thumb);
					}
				}
			}]
});

//sum_prop_decile123 (Deprivation)
var sum_prop_decile123= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'sum_prop_decile123_Id_value',
				text : 'Deprivation in area',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'sum_prop_decile123_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_sum_prop_decile123,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("sum_prop_decile123_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("sum_prop_decile123_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'sum_prop_decile123_Id',
				width : '58%',
				increment : 0.01,
				value : priority_sum_prop_decile123,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("sum_prop_decile123_Id_value").setValue(thumb);
					}
				}
			}]
});


//environmental_problem
var environmental_problem= Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'environmental_problem_Id_value',
				text : 'Presence of environmental problems',
				margin : '2 4 5 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				decimalPrecision: 2,
				id : 'environmental_problem_Id_value',
				margin : '2 2 0 0',
				size : 3 ,
				value : priority_environmental_problem,
				forcePrecision: true,  
				step: 0.01,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("environmental_problem_Id").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("environmental_problem_Id").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				//text : 'm',
				margin : '2 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'environmental_problem_Id',
				width : '58%',
				increment : 0.01,
				value : priority_environmental_problem,
				decimalPrecision: 2,
				forcePrecision: true,  
				minValue : 0.00,
				maxValue : 10.00,
				margin : '2 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("environmental_problem_Id_value").setValue(thumb);
					}
				}
			}]
});
////////////////////////////

var sla = Ext.create('Ext.form.Panel', {
	border : false,
	autoHeight : true,	
	bodyPadding : 3,
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'Criteria weight '						
			});
		}
	},
	
	
	items : [houseprice_income_ratio, rental_income_ratio,interest_mortgage,social_private_rented,homeownership_products,crime_sla_sd,count_employment,public_transport,count_educationcentre,commercial_count,count_healthservice,childcare_count, leisure_count, greenpublicspace_area, housing_quality_median,energy_efficiency,landfill_count,desirability, sum_prop_decile123,environmental_problem]
	//items : [houseprice_income_ratio, rental_income_ratio,interest_mortgage,social_private_rented,homeownership_products,crime_sla_sd,count_employment,public_transport,count_educationcentre,commercial_count,count_healthservice,childcare_count, leisure_count, greenpublicspace_area, housing_quality_median,energy_efficiency,landfill_count,desirability, sum_prop_decile123,environmental_problem, chk_houseprice_income_ratio, chk_rental_income_ratio,chk_interest_mortgage,chk_social_private_rented,chk_homeownership_products,chk_crime_sla_sd,chk_count_employment,chk_public_transport,chk_count_educationcentre,chk_commercial_count,chk_count_healthservice,chk_childcare_count, chk_leisure_count, chk_greenpublicspace_area, chk_housing_quality_median,chk_energy_efficiency,chk_landfill_count,chk_desirability, chk_sum_prop_decile123,chk_environmental_problem]
	
});