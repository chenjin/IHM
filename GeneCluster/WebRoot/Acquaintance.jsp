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
    
    <title>Simulation Network</title>
    
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
    <script language="javascript" type="text/javascript" src="JS/robut.js"></script>
    <script language="javascript" type="text/javascript" src="JS/robot2.js"></script>
    <script language="javascript" type="text/javascript" src="JS/highcharts.js"></script>
    <script language="javascript" type="text/javascript" src="JS/exporting.js"></script>

  <script type="text/javascript">
	function draw() {
		var name = document.getElementById("graph1").value;
		init(name);
		var core = document.getElementById("core1").value;
		core = eval(core);
		scatter(core);
		
		var x = document.getElementById("dx1").value;
		var y = document.getElementById("dy1").value;
	
		bar(x,y);
		
		sub();

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
				<h2>Acquaintance:</h2>

				A Graph Network simulating people knowing each other's name. <br /><br /> 
				APL: <b>
				<s:property value="all.name.avg_length" /></b> <br /> <br /> 
				CC: <b>
				<s:property value="all.name.coffient" /></b> <br /> 
				
				<input id="graph1" type="hidden" name="graph1" value="<s:property value='all.name.graphData'/>" /> 
				<input id="core1" type="hidden" name="core1" value="<s:property value='all.name.coreData'/>" /> 
				<input id="dx1" type="hidden" name="dx1" value="<s:property value='all.name.xdegree'/>" /> 
				<input id="dy1" type="hidden" name="dy1" value="<s:property value='all.name.ydegree'/>" />
				<input id="rs" type="hidden" name="rs" value="<s:property value='all.name.yrandoms'/>" />
				<input id="rl" type="hidden" name="rl" value="<s:property value='all.name.yrandoml'/>" />
				<input id="is" type="hidden" name="is" value="<s:property value='all.name.yintends'/>" />
				<input id="il" type="hidden" name="il" value="<s:property value='all.name.yintendl'/>" />
					
					
			</div>

			<div id="id-list"></div>

		</div>

		<div id="center-container">
			<div id="infovis"></div>
		</div>

		<div id="right-container">

			<div id="inner-details"></div>

		</div>
		
		<div id="right-container2">

			<img src="Images/acq.jpg"  alt="acquaintance" />

		</div>

		<div id="log"></div>


	</div>
    
		
	<br>
	<br>

</body>

</html>
