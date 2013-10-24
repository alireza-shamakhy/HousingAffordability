Ext.require(['Ext.data.*', 'Ext.chart.*']);

Ext.define('chartmodel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'rank',
        type: 'float'        
     },
     {
        name: 'sla_name',
        type: 'string'    	
     }
    ]
});
Ext.onReady(function() {

        var store1 =	Ext.create('Ext.data.Store', {
        	 autoLoad: true,
             autoSync: true,
             autoLoad: true,
             model: 'chartmodel',
             sorters: [{
                 property: 'rank',
                 direction: 'ASC'
             }],
             proxy: {
                 type: 'rest',
                 url: '/housing/housing-controller/map_new_chart',
                 reader: {
                     type: 'json',
                     root: 'chartdata'
                       }
                   }  
				});
/*
	    	    var chart = new Ext.chart.Chart({
	    	        width: 1200,
	    	        height: 600,
	    	        animate: true,
	    	        store: store1,
	    	        renderTo: Ext.getBody(),
	    	        shadow: true,
	    	        axes: [{
	    	            type: 'Numeric',
	    	            position: 'left',
	    	            fields: ['rank'],
	    	            label: {
	    	                renderer: Ext.util.Format.numberRenderer('0,0')
	    	            },
	    	            title: 'Rank',
	    	            grid: true,
	    	            minimum: 0
	    	        }, {
	    	            type: 'Category',
	    	            position: 'bottom',
	    	            fields: ['sla_name'],
	    	            title: 'SLA_Name',
	    	            
	    	        }],
	    	        series: [{
	    	            type: 'column',
	    	            axis: 'bottom',
	    	            highlight: true,
	                    tips: {
	                        trackMouse: true,
	                        width: 140,
	                        height: 28,
	                        renderer: function(storeItem, item) {
	                          this.setTitle(storeItem.get('sla_name') + ': ' + (81 - storeItem.get('rank')) + ' rank');
	                          }
	                       },	    	            
	    	            xField: 'sla_name',
	    	            yField: 'rank'
	    	        }]
	    	    });
	    	    */
	    	    var chart = new Ext.chart.Chart({
	    	        width: 1200,
	    	        height: 600,
	    	        animate: true,
	    	        store: store1,
	    	        renderTo: Ext.getBody(),
	    	        shadow: true,
	    	        legend: {
	                    position: 'right'
	                },
	    	        axes: [{
	    	            //type: 'string',
	    	        	type: 'Category',
	    	            position: 'left',
	    	            fields: ['sla_name'],	    	           
	    	            title: 'SLA_name',
	    	           // grid: true
	    	           // minimum: 0
	    	        }, {	    	            
	    	            type: 'Numeric',
	    	            position: 'bottom',
	    	            fields: ['rank'],
	    	            title: 'Rank',
	    	            label: {
	    	                renderer: Ext.util.Format.numberRenderer('0,0')
	    	            },
	    	            grid: true,
    	                minimum: 0,
    	                roundToDecimal: false
	    	            
	    	        }],
	    	        series: [{
	    	            type: 'bar',
	    	            axis: 'bottom',	    	            
	    	            highlight: true,
	                    tips: {
	                        trackMouse: true,
	                        width: 140,
	                        height: 28,
	                        renderer: function(storeItem, item) {
	                          this.setTitle(storeItem.get('sla_name') + ': ' + (81 - storeItem.get('rank')) + ' rank');
	                          }
	                       },	    	            
	    	            xField: 'sla_name',
	    	            yField: 'rank'
	    	        }]
	    	    });	    
	    	    
	    	 

});