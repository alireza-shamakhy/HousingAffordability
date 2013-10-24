Ext.require(['Ext.data.*', 'Ext.chart.*']);

var st="[{ name: 'rank',  type: 'float' },  { name: 'sla_name',   type: 'string' }]";
//var myfield = Ext.getCmp(st);



//Ext.ns('globalVariable');
//globalVariable = st;
//var sb = new Ext.ux.StringBuilder();
//sb.append(st);
//alert(sb.toString());

var tempUserAOIStore = new Ext.data.ArrayStore({
    fields: st
   
   });

 /*
Ext.define('chartmodel', {
    extend: 'Ext.data.Model',
    fields: tempUserAOIStore

});
*/

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


var model = {
	    extend: 'Ext.data.Model',
	    fields: st
	};

//alert(model.fields.toString());

//Ext.define('chartmodel', model);

/*
Ext.define('chartmodel', {
    extend: 'Ext.data.Model',
    fields: (function(){
        var globalVariable = st;    

        Ext.application({

            launch: function() { globalVariable; }

        });

    })
});

alert(model.fields);
Ext.define('chartmodel', {
    extend: 'Ext.data.Model',
    fields: st
        });
  */  


Ext.onReady(function() {

        var store1 =	Ext.create('Ext.data.Store', {
        	 autoLoad: true,
             //autoSync: true,
             //autoLoad: true,
             required: chartmodel,
             sorters: [{
                 property: 'rank',
                 direction: 'ASC'
             }],
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
   
/*        
        for (var i = 0; i < store1.getCount(); i++)
        {
          //if (ds.getAt(i).data.isselected) // isselected is a field in my store
            alert(store1.data.items[i].name); 
        }
        //Ext.MessageBox.alert('Results', s);
        //store1.reload(store1.lastOptions);
        store1.each(function(rec){
            
            console.write(rec.get('name'));
     

       });
  */      
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
            columns: [{ text: 'sla_name',  dataIndex: 'sla_name', width :250 },                
                      { text: 'rank', dataIndex: 'rank' }],      //,hidden: true, 	hideable: true      // One header just for show. There's no data,
            store: store1, // A dummy empty data store
            sorters: ['rank', 'sla_name'],
            height: 300,
            width: 370,
            flex: 1      // Use 1/3 of Container's height (hint to Box layout)
        }]
        
        });
        
 

});