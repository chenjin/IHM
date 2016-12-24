<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<style>#hide {display: none;}</style>
<link type="text/css" href="css/main.css" rel="stylesheet" />
<link type="text/css" href="css/other.css" rel="stylesheet" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="js/jit.js"></script>
<script language="javascript" type="text/javascript" src="js/example.js"></script>
<script language="javascript" type="text/javascript" src="js/scatter.js"></script>
<script language="javascript" type="text/javascript" src="js/bar.js"></script>
<script language="javascript" type="text/javascript" src="js/highcharts.js"></script>
<script language="javascript" type="text/javascript" src="js/exporting.js"></script>
<link rel="stylesheet" href="css/pace-theme-center-circle.css"/>
<script data-pace-options='{"ajax":true,"document":false,"eventLag":false}' type="text/javascript" src="js/pace.js"> </script>
<script type="text/javascript">

	function draw(){
		//alert(filename);
		//window.location.href="loadaction?filename="+filename;
		//清空person_id输入框避免再次保存
		var div=document.getElementById('hide');
		div.style.display='none';
		var data = [[2,1],[2,2],[2,3],[1,4],[1,5]];
		initpage();
		scatter(data);
		var x = [2,1,2];
		var y = [1,2,3];
		bar(x,y);
	}
	function alertfile(filename) {
	var div=document.getElementById('hide');
		div.style.display='block';
		var name = this.value;
		//alert(filename);
		//window.location.href = "loadaction?filename=" + filename;
		//清空person_id输入框避免再次保存
	}
</script>

<head>
<base href="<%=basePath%>">

<title>File Upload Page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body onload="draw();">
<br>
	<br>
	<h1>Upload your gene expression File!</h1>
	<br>
	<!--  这里做一个文件选择的按钮  之后交给action调用数据处理和计算的函数  剩下的交割js-->
	<div id="container">

		<div id="left-container">
			<br>
				
		   <div id="id-list"></div>
		</div>

		<div id="center-container">
<!-- 		<div style="text-align:left;"> -->
<!-- 		<p>We have done an investigation on the connections in our class, and three corresponding social -->
<!-- networks under different conditions (i.e., knowing someone’s name, knowing someone’s hometown, and -->
<!-- knowing someone’s dialect) are formulated. Please download the data from our assigned FTP.<br> -->
<!-- (1) Perform some computer simulations (by using whatever computer software or programming language) on -->
<!-- the three graphs to analyze the network properties (such as node-degree distribution, average shortest pathlength, -->
<!-- clustering coefficient, etc.) and dynamical behaviors (such as robustness against intentional attack and -->
<!-- random attack, etc.).<br> -->
<!-- (2) Calculate the node coreness in the three graphs;<br> -->
<!-- (3) Draw necessary figures and/or tables to demonstrate your simulation results and to support your -->
<!-- observations;<br> -->
<!-- (4) To have extra bonus, you can develop a small system (including friendly interfaces, or graphic -->
<!-- demonstrations) to show the layout of the networks of our class. -->
<!-- 		</p></div> -->
                　                                   <form action="uploadFile.action" method="post" enctype="multipart/form-data">
			    　　
			        Gene Type:
			        <select name="type">
			            <option>AFFYMETRIX_3PRIME_IVT_ID</option>
			            <option>AFFYMETRIX_EXON_GENE_ID</option>AFFYMETRIX_SNP_ID
						<option>AGILENT_CHIP_ID</option>
						<option>AGILENT_ID</option>
						<option>AGILENT_OLIGO_ID</option>
						<option>ENSEMBL_GENE_ID</option>
						<option>ENSEMBL_TRANSCRIPT_ID</option>
						<option>ENTREZ_GENE_ID</option>
						<option>FLYBASE_GENE_ID</option>
						<option>FLYBASE_TRANSCRIPT_ID</option>
						<option>GENBANK_ACCESSION</option>
						<option>GENPEPT_ACCESSION</option>
						<option>GENOMIC_GI_ACCESSION</option>
						<option>PROTEIN_GI_ACCESSION</option>
						<option>ILLUMINA_ID</option>
						<option>IPI_ID</option>
						<option>MGI_ID</option>
						<option>GENE_SYMBOL</option>
						<option>PFAM_ID</option>
					    <option>PIR_ACCESSION</option>
						<option>PIR_ID</option>
						<option>PIR_NREF_ID</option>
						<option>REFSEQ_GENOMIC</option>
						<option>REFSEQ_MRNA</option>
						<option>REFSEQ_PROTEIN</option>
						<option>REFSEQ_RNA</option>
						<option>RGD_ID</option>
						<option>SGD_ID</option>
						<option>TAIR_ID</option>
						<option>UCSC_GENE_ID</option>
						<option>UNIGENE</option>
						<option>UNIPROT_ACCESSION</option>
						<option>UNIPROT_ID</option>
						<option>UNIREF100_ID</option>
						<option>WORMBASE_GENE_ID</option>
						<option>WORMPEP_ID</option>
						<option>ZFIN_ID</option>
			        </select><br/>
			        Gene Expresstion data: <input type="file" name="upload"><br>
			        
			        <input type="submit" value="upload">
			    </form>
<!-- 		    <div style="text-align:center;"><a href="loadaction">Acquaintance</a></div>     -->
<!--             <div style="text-align:center;"><a href="loadhome">Hometown</a></div>     -->
<!--             <div style="text-align:center;"><a href="loaddialect">Dialect</a></div>     -->
			
		</div>

		<div id="right-container">

			<div id="inner-details"></div>

		</div>

		<div id="log"></div>
		<br>
	</div>
	<br>
	<br>
</body>


</html>
