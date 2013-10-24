Ext.require(['Ext.data.*', 'Ext.chart.*']);


/*
var strField="fields: [{name: 'sla_name', type: 'string'},";
var strColumn="columns: [{ text: 'sla_name',  dataIndex: 'sla_name', width :250 },";
//columns: [{ text: 'sla_name',  dataIndex: 'sla_name', width :250 },                
//          { text: 'Priority', dataIndex: 'Priority' }],            // One header just for show. There's no data,

 //var cc= Ext.getCmp("chk_houseprice_income_ratio").getValue(); 
// alert(cc);
//alert(Ext.getCmp("chk_houseprice_income_ratio").getValue());

//if (Ext.getCmp("chk_houseprice_income_ratio").getValue() == true)
//{
	strField = strField + "{name: 'houseprice_income_ratio', type: 'float'},";
	strColumn = strColumn + "{ text: 'houseprice_income_ratio', dataIndex: 'houseprice_income_ratio' },";
//}
//if (Ext.getCmp("chk_rental_income_ratio").getValue() == true)
//	{
		strField = strField + "{name: 'rental_income_ratio', type: 'float'},";
		strColumn = strColumn + "{ text: 'rental_income_ratio', dataIndex: 'rental_income_ratio' },";
//	}
//if (Ext.getCmp("chk_interest_mortgage").getValue() == true)
//	{
		strField = strField + "{name: 'interest_mortgage_rate', type: 'float'},";
		strColumn = strColumn + "{ text: 'interest_mortgage_rate', dataIndex: 'interest_mortgage_rate' },";
//	}
//if (Ext.getCmp("chk_social_private_rented").getValue() == true)
//	{
		strField = strField + "{name: 'social_private_rented', type: 'float'},";
		strColumn = strColumn + "{ text: 'social_private_rented', dataIndex: 'social_private_rented' },";
//	}
//if (Ext.getCmp("chk_homeownership_products").getValue() == true)
//	{
		strField = strField + "{name: 'homeownership_product', type: 'float'},";
		strColumn = strColumn + "{ text: 'homeownership_product', dataIndex: 'homeownership_product' },";
//	}
//if (Ext.getCmp("chk_crime_sla_sd").getValue() == true)
//	{
		strField = strField + "{name: 'crime_cluster', type: 'float'},";
		strColumn = strColumn + "{ text: 'crime_cluster', dataIndex: 'crime_cluster' },";
//	}
//if (Ext.getCmp("chk_count_employment").getValue() == true)
//	{
		strField = strField + "{name: 'employment_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'employment_count', dataIndex: 'employment_count'},";
//	}
//if (Ext.getCmp("chk_public_transport").getValue() == true)
//	{
		strField = strField + "{name: 'public_transport_station_route', type: 'float'},";
		strColumn = strColumn + "{ text: 'public_transport_station_route', dataIndex: 'public_transport_station_route' },";
//	}
//if (Ext.getCmp("chk_count_educationcentre").getValue() == true)
//	{
		strField = strField + "{name: 'educationcentre_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'educationcentre_count', dataIndex: 'educationcentre_count' },";
//	}
//if (Ext.getCmp("chk_commercial_count").getValue() == true)
//	{
		strField = strField + "{name: 'shop_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'shop_count', dataIndex: 'shop_count' },";
//	}
//if (Ext.getCmp("chk_count_healthservice").getValue() == true)
//	{
		strField = strField + "{name: 'healthservice_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'healthservice_count', dataIndex: 'healthservice_count' },";
//	}
//if (Ext.getCmp("chk_childcare_count").getValue() == true)
//	{
		strField = strField + "{name: 'childcare_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'childcare_count', dataIndex: 'childcare_count' },";
//	}
//if (Ext.getCmp("chk_leisure_count").getValue() == true)
//	{
		strField = strField + "{name: 'leisure_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'leisure_count', dataIndex: 'leisure_count' },";
//	}
//if (Ext.getCmp("chk_greenpublicspace_area").getValue() == true)
//	{
		strField = strField + "{name: 'greenpublicspace_area', type: 'float'},";
		strColumn = strColumn + "{ text: 'greenpublicspace_area', dataIndex: 'greenpublicspace_area' },";
//	}
//if (Ext.getCmp("chk_housing_quality_median").getValue() == true)
//	{
		strField = strField + "{name: 'quality_housing_cluster', type: 'float'},";
		strColumn = strColumn + "{ text: 'quality_housing_cluster', dataIndex: 'quality_housing_cluster' },";
//	}
//if (Ext.getCmp("chk_energy_efficiency").getValue() == true)
//	{
		strField = strField + "{name: 'floorarea_median', type: 'float'},";
		strColumn = strColumn + "{ text: 'floorarea_median', dataIndex: 'floorarea_median' },";
//	}
//if (Ext.getCmp("chk_landfill_count").getValue() == true)
//	{
		strField = strField + "{name: 'landfill_count', type: 'float'},";
		strColumn = strColumn + "{ text: 'landfill_count', dataIndex: 'landfill_count' },";
//	}
//if (Ext.getCmp("chk_desirability").getValue() == true)
//	{
		strField = strField + "{name: 'desirability_neighbourhood_area', type: 'float'},";
		strColumn = strColumn + "{ text: 'desirability_neighbourhood_area', dataIndex: 'desirability_neighbourhood_area' },";
//	}
//if (Ext.getCmp("chk_sum_prop_decile123").getValue() == true)
//	{
		strField = strField + "{name: 'deprivation_cluster', type: 'float'},";
		strColumn = strColumn + "{ text: 'deprivation_cluster', dataIndex: 'deprivation_cluster' },";
//	}
//if (Ext.getCmp("chk_environmental_problem").getValue() == true)
//	{
		strField = strField + "{name: 'environmental_problem', type: 'float'},";
		strColumn = strColumn + "{ text: 'environmental_problem', dataIndex: 'environmental_problem' },";
///	}

strField = strField.substring(0,strField.length-1)+ "]";
strColumn = strColumn.substring(0,strColumn.length-1)+ "]";
		
	*/	

Ext.define('chartmodel', {
    extend: 'Ext.data.Model',
    fields: [{name: 'sla_name', type: 'string'},
             {name: 'houseprice_income_ratio', type: 'float'},
             {name: 'rental_income_ratio', type: 'float'},  
         	 {name: 'interest_mortgage_rate', type: 'float'},
         	 {name: 'social_private_rented', type: 'float'},
         	 {name: 'homeownership_product', type: 'float'},
         	 {name: 'crime_cluster', type: 'float'},
         	 {name: 'employment_count', type: 'float'},
         	 {name: 'public_transport_station_route', type: 'float'},
         	 {name: 'educationcentre_count', type: 'float'},
         	 {name: 'shop_count', type: 'float'},
         	 {name: 'healthservice_count', type: 'float'},
         	 {name: 'childcare_count', type: 'float'},
         	 {name: 'leisure_count', type: 'float'},
         	 {name: 'greenpublicspace_area', type: 'float'},
         	 {name: 'quality_housing_cluster', type: 'float'},
         	 {name: 'floorarea_median', type: 'float'},
         	 {name: 'landfill_count', type: 'float'},
         	 {name: 'desirability_neighbourhood_area', type: 'float'},
         	 {name: 'deprivation_cluster', type: 'float'},
         	 {name: 'environmental_problem', type: 'float'}
            ]
});


Ext.onReady(function() {

        var store1 =	Ext.create('Ext.data.Store', {
        	 autoLoad: true,
             autoSync: true,
             model: 'chartmodel',
             proxy: {
                 type: 'rest',
                 url: '/housing/housing-controller/map_new_table',
                 reader: {
                     type: 'json',
                     root: 'chartdata'
                       }
                   }  
				});
       
   
        
        Ext.ux.Report = Ext.extend(Ext.Component, {
            autoEl: {tag: 'iframe', cls: 'x-hidden', src: Ext.SSL_SECURE_URL},
            load: function(config){
                this.getEl().dom.src = config.url + (config.params ? '?' + Ext.urlEncode(config.params) : '');
            }
        });
        
        var report = new Ext.ux.Report({
        	  renderTo: Ext.getBody()
        	});
        
        var resultsPanel = Ext.create('Ext.panel.Panel', {
            width : '99%',
            height: 800,
            renderTo: Ext.getBody(),
        layout: {
            type: 'vbox',
            align: 'center',
            pack: 'center'
        },
        items: [
                         
                {
                	xtype: 'button',
                	id : 'linkButton',
                    text: 'Download CSV',
                    renderTo: Ext.getBody(),
                    handler: function() {
     	
                    	report.load({
                    		url: '/housing/housing-controller/table.csv',
                    		reader: {
                            type: 'csv',
                            root: 'chartdata'
                              }                                              	
                    		});
                        }
                                     
                },
                
                {               // Results grid specified as a config object with an xtype of 'grid'
            xtype: 'grid',
            title: 'Table',
            //columns: [{ text: 'sla_name',  dataIndex: 'sla_name', width :250 },                
            //          { text: 'Priority', dataIndex: 'Priority' }],            // One header just for show. There's no data,
            columns: [{text: 'sla_name', dataIndex: 'sla_name', width :200},
                 	 {text: 'houseprice_income_ratio', dataIndex:'houseprice_income_ratio'},
                 	 {text: 'rental_income_ratio', dataIndex:'rental_income_ratio'},
                 	 {text: 'interest_mortgage_rate', dataIndex:'interest_mortgage_rate'},
                 	 {text: 'social_private_rented', dataIndex:'social_private_rented'},
                 	 {text: 'homeownership_product', dataIndex:'homeownership_product'},
                 	 {text: 'crime_cluster', dataIndex:'crime_cluster'},
                 	 {text: 'employment_count', dataIndex:'employment_count'},
                 	 {text: 'public_transport_station_route', dataIndex:'public_transport_station_route'},
                 	 {text: 'educationcentre_count', dataIndex:'educationcentre_count'},
                 	 {text: 'shop_count', dataIndex:'shop_count'},
                 	 {text: 'healthservice_count', dataIndex:'healthservice_count'},
                 	 {text: 'childcare_count', dataIndex:'childcare_count'},
                 	 {text: 'leisure_count', dataIndex:'leisure_count'},
                 	 {text: 'greenpublicspace_area', dataIndex:'greenpublicspace_area'},
                 	 {text: 'quality_housing_cluster', dataIndex:'quality_housing_cluster'},
                 	 {text: 'floorarea_median', dataIndex:'floorarea_median'},
                 	 {text: 'landfill_count', dataIndex:'landfill_count'},
                 	 {text: 'desirability_neighbourhood_area', dataIndex:'desirability_neighbourhood_area'},
                 	 {text: 'deprivation_cluster', dataIndex:'deprivation_cluster'},
                 	 {text: 'environmental_problem', dataIndex:'environmental_problem'}
                    ],            
            store: store1, // A dummy empty data store
            height: 300,
            width: 1200,
            flex: 1      // Use 1/3 of Container's height (hint to Box layout)
        }]
        
        });
        
        
        store1.load();
        store1.on('load', function(store1, records) {
            for (var i = 0; i < 1; i++) {
            	console.log(records[i].get('houseprice_income_ratio'));
	            if (records[i].get('houseprice_income_ratio') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=houseprice_income_ratio]')[0]
	                hideColumn.hide();
	            }
	            if (records[i].get('rental_income_ratio') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=rental_income_ratio]')[0]
	                hideColumn.hide();
	            }
	            if (records[i].get('interest_mortgage_rate') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=interest_mortgage_rate]')[0]
	                hideColumn.hide();
	            }
	            if (records[i].get('social_private_rented') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=social_private_rented]')[0]
	                hideColumn.hide();
	            }

	            if (records[i].get('homeownership_product') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=homeownership_product]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('crime_cluster') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=crime_cluster]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('employment_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=employment_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('public_transport_station_route') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=public_transport_station_route]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('educationcentre_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=educationcentre_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('shop_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=shop_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('healthservice_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=healthservice_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('childcare_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=childcare_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('leisure_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=leisure_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('greenpublicspace_area') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=greenpublicspace_area]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('quality_housing_cluster') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=quality_housing_cluster]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('floorarea_median') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=floorarea_median]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('landfill_count') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=landfill_count]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('desirability_neighbourhood_area') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=desirability_neighbourhood_area]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('deprivation_cluster') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=deprivation_cluster]')[0]
	                hideColumn.hide();
	            }	            
	            if (records[i].get('environmental_problem') == 0)
	            {
	            	
	                var hideColumn = Ext.ComponentQuery.query('.gridcolumn[text=environmental_problem]')[0]
	                hideColumn.hide();
	            }	            

            };
        });
        
        
        
        //var row = Table.getSelectionModel().getSelection()[0];
        //alert(row);
        //var inner = Ext.ComponentQuery.query('panel')[1];
        //alert(inner.getName());
        //var c=resultsPanel.getComponent( grid );
        
        //alert (resultsPanel.Items.count);
        //var rec = resultsPanel.Items[1].getStore().getAt(0);
       // alert(rec.sla_name);
        
        //var phoneColumn = Ext.ComponentQuery.query('.gridcolumn[text=environmental_problem]')[0]
        //phoneColumn.hide();
        
 

});