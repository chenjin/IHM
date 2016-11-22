 var data = new Array()
		var array_data =[]
		//var a = d3.rgb(255,255,217)
		//var b = d3.rgb(8,29,88)
		var a= d3.rgb(255,0,0)
		var b= d3.rgb(0,255,0)
		var compute = d3.interpolate(a,b)
		var matrixrow = 200
		var matrixcol = 15
		for(var i=0;i<matrixrow;i++){
		    data[i]= new Array();
			for(var j=0;j<matrixcol;j++){
			    data[i][j] = Math.floor(Math.random()*100)
				//temp = Math.floor(Math.random()*100)
				array_data[i*matrixrow+j] =data[i][j]
			}
		}
		
		var margin = { top: 0, right: 0, bottom: 0, left: 0 },
		  heatmapwidth = 600,
		  heatmapheight = 800,
		  width = heatmapwidth + margin.left + margin.right,        // ?????????��?Heatmap???
		  height = heatmapheight + margin.top + margin.bottom,
		  
		  gridSize = Math.floor(width / matrixcol),    // ???��???��???��width??24?
		  gridSize_h = Math.floor(height / matrixrow),    // ???��???��???��width??24?
		  legendElementWidth = gridSize_h * 2,    // ???????����?????}?
		  buckets = 9,        // ??9?????
		  colors = ["#ffffd9","#edf8b1","#c7e9b4","#7fcdbb","#41b6c4","#1d91c0","#225ea8","#253494","#081d58"]; 
		 
		  var linear = d3.scale.linear()
		    .domain([d3.min(array_data),d3.max(array_data)])
			.range([0,1])
		  var colorScale =d3.scale.quantile()
			.domain([d3.min(array_data),buckets-1,d3.max(array_data)])//???[0,n,?????]
			.range(colors)//??
		  //??chart��svg
		  
		  var svg =d3.select("#chart").append("svg")
			 .attr("width",width+margin.left + margin.right)
			 .attr("height",height +margin.top + margin.bottom)
			 .append("g")//?svg?????g��?????g��?��?
			 .attr("transform","translate("+margin.left+","+margin.top+")")
		  
		  var heatMap =svg.selectAll(".score")
		    .data(array_data)
			.enter()//?data???��????".score"
			.append("rect")
			.attr("x",function(d,i){return (i % matrixrow)*gridSize;})
			.attr("y",function(d,i){return parseInt(i / matrixrow)*gridSize_h;})
			.attr("rx",0)
			.attr("ry",0)
			.attr("class","hour bordered")
			.style("text-anchor", "end")
			.attr("width",gridSize)
			.attr("height",gridSize_h)
			.style("fill","#FFFFFF")
			;
		 var tooltip = d3.select("body")
		 .append("div")
		 .attr("class","tooltip")
		 .style("opacity",0.0)
		 // duration(1000) ?1000ns??��1s???????
		  heatMap.transition().duration(1)
		    //.style("fill",function(d){return colorScale(d);});
			.style("fill",function(d){return compute(linear(d));})
	      heatMap
		  .on("mouseover",function(d){
			 tooltip.html(d)
			 .style("left",(d3.event.pageX) +"px")
			 .style("top",(d3.event.pageY)+"px")
			 .style("opacity",1.0)
		  })
		  
		  
		  var svg_block =d3.select("#chart").append("svg")
			 .attr("width", 300)
			 .attr("height",height +margin.top + margin.bottom)
			 .append("g")//?svg?????g��?????g��?��?
			 .attr("transform","translate("+(margin.left+20)+","+0+")")
			  
          //cluster_block,get k non-duplicate random numbers
          var origin = new Array;
		  for(var i=0;i<height;i++){
		      origin[i] = i+1
		  }
		  origin.sort(function(){
			return 0.5 - Math.random() 
		  })
		  var cut = origin.slice(0,buckets)
	      var data_cut = new Array(buckets)
		  cut = cut.sort(function(a,b){
		    return a-b
		  })
		  
		  for(var i=cut.length-1;i>0;i--){
		    cut[i] = cut[i-1]
			data_cut[i] = Math.floor(cut[i] * matrixrow / heatmapheight)
		  }
          data_cut[0] =0 		  
 		  cut[0] =0
		  /*for(var i=0;i<cut.length;i++){
		     console.log(Math.floor(cut[i]*matrixrow/heatmapheight))
		  }*/
		 
		  var cluster =new Array(data.length)
		  var row =0,inx = 0
		  while(row < data.length){
		     for(row=data_cut[inx];row<data_cut[inx+1];row++){
			     cluster[row] =inx
			 }
			 if(inx+1 < buckets -1){
			     inx++
			 }else{
			     for(row = data_cut[buckets-1];row < data.length;row++){
			         cluster[row] =buckets-1
			     } 
			 }
		  }
		  /*for(var i =0;i < data.length;i++){
		     console.log(cluster[i])
		  }*/
		  
		
		  var estimate = function(cluster,data){
		     var a =new Array(data.length),
			 b =new Array(data.length),
			 s =new Array(data.length),
			 avg_s =0

		     for(var row=0; row< data.length; row++){
			    a[row]=0
				b[row]=0
				s[row]=0
			    var inside =0,outside=0
			    for(var other=0;other< data.length;other++){
				    if(row == other){
					    continue;
					}
					var distance =0;
					math.square(math.add(data[row],math.multiply(data[other],-1)))
					.map(function(value,index,matrix){
						distance += value;
					})
					
					if(cluster[row] == cluster[other]){//distance within cluster
   						a[row] += Math.sqrt(distance)
						inside++
					}else{//distance among cluster
					    b[row] += Math.sqrt(distance)
						outside++
					}
					
				}
				//console.log(inside)
				//console.log(outside)
				if(inside > 0){
					a[row] = a[row] /inside
			    }
				if(outside > 0){
					b[row] = b[row] /outside
			    }
				//console.log(a[row])
				//console.log(b[row])
				s[row] = (b[row] -a[row])/Math.max(a[row],b[row])
				//console.log(Math.max(a[row],b[row]))
				avg_s += s[row]
				//console.log(avg_s)
			 }
			 avg_s /= data.length 
			 console.log(avg_s)
			 return avg_s
		  }
		  var avg_s = estimate(cluster,data)
		  //block on the right
		  var block = svg_block.selectAll(".block")
             // .data([0].concat(colorScale.quantiles()), function(d) { return d; });
             .data(cut)
          block.enter().append("g")
             .attr("class", "block");
          
          block.append("rect")
		    .attr("x", 0)
			//.attr("y", function(d, i) { return height /colors.length * i;})
			.attr("y",function(d,i){
				return d;
			})
            .attr("width", 60)
            //.attr("height", height/colors.length)
			.attr("height",function(d, i){
			     if(i<cut.length-1) return cut[i+1]-cut[i]
				 return height-cut[i]
			})
            .style("fill", function(d, i){return colors[i];})
            .on("mouseover",function(d,i){
			 //tooltip.html("cluster"+i)
			 tooltip.html(avg_s)
			 .style("left",(d3.event.pageX) +"px")
			 .style("top",(d3.event.pageY)+"px")
			 .style("opacity",1.0)
		    })
          block.append("text")
            .attr("class", "mono")
            .text(function(d,i) { return "label:" + i; })
            .attr("x", 80)
            //.attr("y", function(d, i) { return height /colors.length * i ;})
			.attr("y",function(d,i){
			    return d
			})
            .style("writing-mode","tb-rl")
			
          block.exit().remove();
		  
		  
		 
      //???legend
		/* var svg_legend =d3.select("#legend").append("svg")
			 .attr("width", 300)
			 .attr("height",80)
			 .attr("x",50)
			 .attr("y", heatmapheight)
		  var defs =svg_block.append("defs")
		  var linearGradient = defs.append("linearGradient")  
                        .attr("id","linearColor")  
                        .attr("x1","0%")  
                        .attr("y1","0%")  
                        .attr("x2","100%")  
                        .attr("y2","0%");  
  
          var stop1 = linearGradient.append("stop")  
                .attr("offset","0%")  
                .style("stop-color",a.toString());  
  
		  var stop2 = linearGradient.append("stop")  
                .attr("offset","100%")  
                .style("stop-color",b.toString());
		  var colorRect = svg_legend.append("rect")
                .attr("x", 100)  
                .attr("y", 0)  
                .attr("width", 200)  
                .attr("height", 30)  
                .style("fill","url(#" + linearGradient.attr("id") + ")"); 
          var text_small =svg_legend.append("text")
              	.attr("class","small")
                .text("small")
                .attr("x",100)
				.attr("y",60)
		  var text_large =svg_legend.append("text")
              	.attr("class","large")
                .text("large")
                .attr("x",250)
				.attr("y",60)*/