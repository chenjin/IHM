<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<script type="text/javascript">
	function draw() {
		var name = document.getElementById("graph3").value;
		init(name);
		var core = document.getElementById("core3").value;
		core = eval(core);
		scatter(core);
		
		var x = document.getElementById("dx3").value;
		var y = document.getElementById("dy3").value;
	
		bar(x,y);
		
		
	}
	function drawhome(){
		var home = document.getElementById("graph3").value;
		init2(home);
	}
	
</script>




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


  </head>
  
<!-- init 参数传递所有的结果值 -->
<body onload="draw();">
	<br>

	<br>
	<h1>ComplexNetwork Project</h1>
	<br>
	
	
	<br>
	<div id="container">

		<div id="left-container">


			<div class="text">
				<h2>Dialect:</h2>

				A Graph Network simulating people knowing each other's dialect. <br /><br /> 
				APL: <b>
				<s:property value="all.dialect.avg_length" /></b> <br /> <br /> 
				CC: <b>
				<s:property value="all.dialect.coffient" /></b> <br /> 
				
				<input id="graph3" type="hidden" name="graph3" value="<s:property value='all.dialect.graphData'/>" /> 
				<input id="core3" type="hidden" name="core3" value="<s:property value='all.dialect.coreData'/>" /> 
				<input id="dx3" type="hidden" name="dx3" value="<s:property value='all.dialect.xdegree'/>" /> 
				<input id="dy3" type="hidden" name="dy3" value="<s:property value='all.dialect.ydegree'/>" />
					
					
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

			<img src="Images/dialectDD.jpg"  alt="dialect" />

		</div>

		<div id="log"></div>

	</div>
	<br>
	<br>

</body>

</html>
