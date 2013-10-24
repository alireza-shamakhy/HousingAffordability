
		//alert("lga3");
var lgaStore3 = Ext.create('Ext.data.Store', {
			autoLoad : true,
			model : "LGA",
			storeId : 'lgaStore3',
			fields : ['lgaCode', 'lgaName'],
			proxy : {
				type : 'ajax',
				url : 'getLGAs.json',
				reader : {
					type : 'json',
					root : 'lgas'
				}
			}
		});

var selectedLGAs3 = [];

var lgaCombo3 = new Ext.form.ComboBox({
			id : 'lgaCombo3',
			forceSelection : true,
			allowBlank : false,
			editable: false,            
			labelWidth : 50,
			store : lgaStore3,
			displayField : 'lgaName',
			valueField : 'lgaCode',
			mode : 'local',
			fieldLabel : 'LGA',
			anchor : '50%',
			multiSelect : true,
			triggerAction : 'all',
			emptyText : 'Select LGAs...',
			selectOnFocus : true,
			margin : '6 0 10 10',
			listeners : {
				select : function(obj, records) {
					selectedLGAs3 = [];
					Ext.each(records, function(rec) {
								selectedLGAs3.push(rec.get('lgaCode'));
							});
					console.log(selectedLGAs3);
				},
				render : function(p) {
						Ext.QuickTips.register({
							target : p.getEl(),
							text : 'Select from LGA list and the analysis is performed only in the selected LGAs3.'							
						});
					}
			}
		});

var LGA3 = Ext.create('Ext.form.Panel', {
			items : [lgaCombo3, selectedLGAs3],
			width : '100%',
			border : false
		});
