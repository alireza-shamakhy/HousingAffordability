
var geo = Ext.create('Ext.form.Panel', {
			layout : {
				type : 'table',
				columns : 1
			},
			border : false,
			defaultType : 'checkboxfield',
			fieldDefaults : {
				margin : '3 5 6 5'
			},			
			bodyPadding : 3,
			items : [
			         {
						xtype : 'checkboxfield',
						id : 'chk_houseprice_income_ratio',
						boxLabel : 'House price in relation to incomes'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_rental_income_ratio',
							boxLabel : 'Rental Costs in relation to incomes'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_interest_mortgage',
							boxLabel : 'Interest rates and mortgage availability'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_social_private_rented',
							boxLabel : 'Availability of private and social rented accomodation'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_homeownership_products',
							boxLabel : 'Availability of affordable homeownership products'						
					 },	
			         {
							xtype : 'checkboxfield',
							id : 'chk_crime_sla_sd',
							boxLabel : 'Safety (Crime)'						
					 },					 
			         {
							xtype : 'checkboxfield',
							id : 'chk_count_employment',
							boxLabel : 'Access to employment'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_public_transport',
							boxLabel : 'Access to public transport services'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_count_educationcentre',
							boxLabel : 'Access to good quality schools'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_commercial_count',
							boxLabel : 'Access to shops'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_count_healthservice',
							boxLabel : 'Access to health services'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_childcare_count',
							boxLabel : 'Access to child care'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_leisure_count',
							boxLabel : 'Access to leisure'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_greenpublicspace_area',
							boxLabel : 'Access to open green public space'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_housing_quality_median',
							boxLabel : 'Quality of housing in area'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_energy_efficiency',
							boxLabel : 'Energy efficency of housing in area'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_landfill_count',
							boxLabel : 'Waste management in area'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_desirability',
							boxLabel : 'Desirability of neighborhood area'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_sum_prop_decile123',
							boxLabel : 'Deprivation in area'						
					 },
			         {
							xtype : 'checkboxfield',
							id : 'chk_environmental_problem',
							boxLabel : 'Presence of environmental problems'						
					 }		 
					 
			]
		});