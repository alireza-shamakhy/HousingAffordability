<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Housing Demonstrator Tool</title>
<link href="/housing/lib/ui-css/checkboxmodel.css" rel="stylesheet"
	type="text/css" />
<link
	href='https://apps.aurin.org.au/assets/js/extjs-4.1.0/resources/css/ext-all.css'
	rel='stylesheet' type='text/css' />
<link
	href="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ux/css/CheckHeader.css"
	rel="stylesheet" type="text/css" />

<script src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ext-all.js"></script>
<script
	src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ux/CheckColumn.js"></script>

<script type="text/javascript">
var successStatus = "<%=request.getAttribute("successStatus")%>"; 
var message = "<%=request.getAttribute("message")%>"; 
var username = "<%=request.getAttribute("username")%>";
</script>

<style type="text/css">
.ext-el-mask {
	color: blue;
	cursor: default;
	opacity: 0.6;
	background-color: red;
}

.myCSS .x-panel-body-default {
	background: white;
	/* border-color: white; */
	border-left: 0px solid red;
	border-right: 0px solid red;
	border-top: 0px solid red;
	border-bottom: 0px solid red;
	color: black;
	/* border-width: 2px;
	border-style: solid; */ . myCSS2 .x-panel-body-default { background :
	white;
	/* border-color: white; */
	border-left: 2px solid red;
	border-right: 2px solid red;
	border-top: 2px solid red;
	border-bottom: 2px solid red;
	color: black;
	/* border-width: 2px;
	border-style: solid; */
}
</style>



<!-- <script src="/housing/lib/ui-js/connectionSetup.js"></script> -->
<!-- Ali-->



<script src="/housing/lib/ui-js/buttons_New.js"></script>
<script src="/housing/lib/ui-js/buttons_Newchart.js"></script>
<script src="/housing/lib/ui-js/buttons_Newtable.js"></script>
<script src="/housing/lib/ui-js/buttons_NewtableRight.js"></script>

<script src="/housing/lib/ui-js/SLAFactors_New.js"></script> 
<script src="/housing/lib/ui-js/constraintsNew.js"></script>
<script src="/housing/lib/ui-js/Development_New.js"></script>

<script src="/housing/lib/ui-js/lga3.js"></script>
<script src="/housing/lib/ui-js/lga2.js"></script>


<script src="/housing/lib/ui-js/operators.js"></script>


<script src="/housing/lib/ui-js/app_categories.js"></script>
<script src="/housing/lib/ui-js/app_outcomes.js"></script>
<script src="/housing/lib/ui-js/processingDetails.js"></script>
<script src="/housing/lib/ui-js/categoriesOfApplication.js"></script>
<script src="/housing/lib/ui-js/prev_prop_use.js"></script>
<script src="/housing/lib/ui-js/applicationDetails.js"></script>
<script src="/housing/lib/ui-js/applicationOutcomes.js"></script>

<script src="/housing/lib/ui-js/lga1.js"></script>
<script src="/housing/lib/ui-js/dpi.js"></script>
<script src="/housing/lib/ui-js/transport.js"></script>
<script src="/housing/lib/ui-js/facility.js"></script>
<script src="/housing/lib/ui-js/landuse.js"></script>
<script src="/housing/lib/ui-js/constraints.js"></script>


<script src="/housing/lib/ui-js/buttons_potential.js"></script>
<script src="/housing/lib/ui-js/buttons_assessment.js"></script>
<script src="/housing/lib/ui-js/Development_Potential.js"></script>

<script src="/housing/lib/ui-js/Development_Assessment.js"></script>
<script src="/housing/lib/ui-js/mainPage.js"></script>


</head>
<body>
	<div id="tabs1" style="width: 800px; height: 400px;"></div>
	<!-- 
	<table width="100%" height="90px" style="margin-bottom: 1px"
		id="header">
		<tbody>
			<tr>
				<td colpan="3"><a target="_blank" href="http://aurin.org.au/"
					style="margin-top: 5px; margin-right: 10px; width: 440px; height: 100px; background-image: url('./resources/aurin_logo.gif'); z-index: 10000; display: block;"></a></td>
				<td align="right" rowspan="2" width="100%" id="ext-gen1277"><a
					target="_blank" href="http://www.ands.org.au/"
					style="margin-top: 0px;margin-right: 20px; width: 143px; height: 70px; background-image: url('./resources/ands-full-b.png'); z-index: 10000; display: block;"></a></td>
				<td align="right" rowspan="2" width="100%" id="ext-gen1277"><a
					target="_blank" href="http://www.csdila.unimelb.edu.au/"
					style="margin-top: 0px;margin-right: 20px; width: 65px; height: 106px; background-image: url('./resources/csdila_logo.png'); z-index: 10000; display: block;"></a></td>
				<td align="right" rowspan="2" width="100%"><a target="_blank"
					href="http://www.unimelb.edu.au/"
					style="margin-top: 13px; margin-right: 10px; width: 100px; height: 104px; background-image: url('./resources/melbourne_uni_logo.png'); z-index: 10000; display: block;"></a></td>
			</tr>

		</tbody>
	</table>	
	 -->
	 
	 <table width="100%" height="90px" style="margin-bottom: 1px"
		id="header">
		<tbody>
			<tr>
			  <td>
			  	<div style="text-align:center; width:910px; height:140px; margin-left:auto; margin-right:auto;">
					<img id="Image-Maps_52013080100443291" src= "./resources/Supporter_logos.gif" usemap="#Image-Maps_52013080100443291" border="0" width="910" height="120" alt="" />
					<map id="_Image-Maps_52013080100443291" name="Image-Maps_52013080100443291">
					<area shape="rect" coords="0,10,91,113" href="http://www.unimelb.edu.au/" target="_blank"/>
					<area shape="rect" coords="136,12,273,115" href="http://www.ands.org.au/" target="_blank"/>
					<area shape="rect" coords="317,12,469,115" href="http://www.aurin.org.au/" target="_blank"/>
					<area shape="rect" coords="508,10,660,113" href="http://www.innovation.gov.au/Pages/default.aspx" target="_blank"/>
					<area shape="rect" coords="676,11,792,114" href="http://www.csdila.unimelb.edu.au/" target="_blank"/>
					<area shape="rect" coords="789,12,905,115" href="http://www.vic.gov.au/" alt="vic" target="_blank"/>
					</map>
				</div>
			  </td>
			</tr>
		</tbody>
	</table>
	
</body>
</html>

