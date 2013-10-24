var leftForm_New = Ext.create('Ext.form.Panel', {
			title : 'Affordability Assesment Criteria',
			frame : true,
			border : false,
			width : '99%',
			items : [{
						title : 'Adjust attributes',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [sla]
					}]
		});

var rightForm_New = Ext.create('Ext.form.Panel', {
			title : 'Criteria selector for display of values',
			id : 'yynew',
			frame : true,
			border : false,
			width : '99%',
			items : [{
						title : 'Select attributes',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [geo, footerPanel_Newright]
					}]
		});

/*
var footerPanel_New = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : true,
	bodyPadding : 5,
	bodyStyle : {
		"background-color" : "#DFE8F6"
	},
	
	
	items : [analyseBtn_New,analyseBtn_Newchart,analyseBtn_Newtable,clearBtn_New]
			
});




var footerPanel_Newright = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : true,
	bodyPadding : 5,
	bodyStyle : {
		"background-color" : "#DFE8F6"
	},
	items: [{
	    title: 'Width = 25%',
	    columnWidth: .25,
	    html: 'Content'
	},{
		  title: 'Width = 75%',
		    columnWidth: .75,
		    html: 'Content'
	}]
});
	*/
	
var footerPanel_Newright = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : true,
	bodyPadding : 5,
	bodyStyle : {
		"background-color" : "#DFE8F6"
	},
	items: [{
	    title: '',
	    columnWidth: .53,	
		bodyStyle : {
			"background-color" : "#DFE8F6"
		},
		items: [ analyseBtn_New,analyseBtn_Newchart,analyseBtn_Newtable,clearBtn_New ]
	},{
		  title: '',
		    columnWidth: .47,
			bodyStyle : {
				"background-color" : "#DFE8F6"
			},
		    items: [ analyseBtn_NewtableRight,SelectBtn_NewtableRight,clearBtn_NewtableRight ]
	}]
});

/*
	var panel_bottom1 = new Ext.Panel({
	    layout: 'column',
	    defaults: {
	        columnWidth: 0.5
	    },
		border : true,
		bodyPadding : 5,
		bodyStyle : {
			"background-color" : "#DFE8F6"
		},
	    items: [ analyseBtn_New,analyseBtn_Newchart,analyseBtn_Newtable,clearBtn_New ]
	});
	
	var panel_bottom2 = new Ext.Panel({
	    layout: 'column',
	    defaults: {
	        columnWidth: 0.5
	    },border : true,
		bodyPadding : 5,
		bodyStyle : {
			"background-color" : "#DFE8F6"
		},
	    items: [ analyseBtn_NewtableRight,SelectBtn_NewtableRight,clearBtn_NewtableRight ]
	});
	*/



