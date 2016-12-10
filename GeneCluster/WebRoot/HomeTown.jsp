<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>ComplexNetwork Project</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<link type="text/css" href="Css/main.css" rel="stylesheet" />
    <link type="text/css" href="Css/other.css" rel="stylesheet" />

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="JS/jit.js"></script>
	<script language="javascript" type="text/javascript" src="JS/name.js"></script>
	<script language="javascript" type="text/javascript" src="JS/home.js"></script>
	<script language="javascript" type="text/javascript" src="JS/dialect.js"></script>
    <script language="javascript" type="text/javascript" src="JS/scatter.js"></script>
    <script language="javascript" type="text/javascript" src="JS/bar.js"></script>
    <script language="javascript" type="text/javascript" src="JS/highcharts.js"></script>
    <script language="javascript" type="text/javascript" src="JS/exporting.js"></script>
	
	<script type="text/javascript">
	function draw() {
		var name = document.getElementById("graph2").value;
		init(name);
		var core = document.getElementById("core2").value;
		core = eval(core)
		scatter(core);
		
		var x = document.getElementById("dx2").value;
		var y = document.getElementById("dy2").value;
	
		bar(x,y);
		
		
	}
	function drawhome(){
		var home = document.getElementById("graph2").value;
		init2(home);
	}
	</script>
	
  </head>
  
<body onload="draw();">
	<br>

	<br>
	<h1>ComplexNetwork Project</h1>
	<br>
	
	
	<br>
	<div id="container">

		<div id="left-container">



			<div class="text">
				<h2>Hometown:</h2>

				A Graph Network simulating students knowing each other's hometown. <br /><br /> 
				APL: <b>
				<s:property value="all.home.avg_length" /></b> <br /> <br /> 
				CC: <b>
				<s:property value="all.home.coffient" />
				</b> <br /> 
				
				<input id="graph2" type="hidden" name="graph2" value="<s:property value='all.home.graphData'/>" /> 
				<input id="core2" type="hidden" name="core2" value="<s:property value='all.home.coreData'/>" /> 
				<input id="dx2" type="hidden" name="dx2" value="<s:property value='all.home.xdegree'/>" /> 
				<input id="dy2" type="hidden" name="dy2" value="<s:property value='all.home.ydegree'/>" />
						
			</div>

			<div id="id-list"></div>

		</div>

		<div id="center-container">
			<div id="infovis"></div>
		</div>

		<div id="right-container">
			<div id="inner-details"></div>
		</div>

		<div id="log"></div>
		<div id="right-container2">

			<img src="Images/hometownDD.jpg"  alt="hometown" />

		</div>

	</div>
	<br>
	<br>

</body>

</html>
