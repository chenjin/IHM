//Sihoutte coefficient
var estimate = function(cluster,data){
 var a =new Array(data.length),
 b =new Array(data.length),
 s =new Array(data.length),//sihoute for each row
 g =new Array(buckets),//sihoute for each group
 centers = new Array(buckets)//center for  each group
 for(var i=0;i<buckets;i++){
	 centers[i] = math.zeros(data[0].length)
 }
 avg_s =0
 for(var row=0; row< data.length; row++){
	a[row]=0
	b[row]=0
	s[row]=0
	var inside =0,outside=0
	centers[cluster[i]] = math.add(centers[cluster[i]],data[row])
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
	if(inside > 0){
		a[row] = a[row] /inside
	}
	if(outside > 0){
		b[row] = b[row] /outside
	}
	s[row] = (b[row] -a[row])/Math.max(a[row],b[row])
	avg_s += s[row]
 }
 for(var i=1;i<buckets;i++){
	centers[i-1] = math.multiply(centers[i-1],1/(cut[i]-cut[i-1]))
 }
 centers[buckets-1] = math.multiply(centers[buckets-1],1/(matrixrow-cut[cut.length-1]))

 var sorted_points =new Array(buckets)
 for(var i=0;i<g.length;i++){
	g[i] =0
	sorted_points[i] = new Array
 }
 
 for(var row=0; row< data.length; row++){
	var distance =0;
	math.square(math.add(data[row],math.multiply(centers[cluster[row]],-1)))
		.map(function(value,index,matrix){
			distance += value;
		})
	sorted_points[cluster[row]].push([row,distance])
	g[cluster[row]] += s[row]
 }
 
 for(var i =0;i<buckets;i++){
	sorted_points[i] =sorted_points[i].sort(function(x,y){return a[1]-b[1]})
	.map(function(item,index){
	return [item[0],s[item[0]]]
	//return item[0]
	})
	for(var j=0;j< sorted_points[i].length;j++){
		console.log(sorted_points[i][j][0]+' '+sorted_points[i][j][1])
		//console.log(sorted_points[i][j])
	}
	console.log("")
 }
 
 for(var i=1;i <cut.length;i++){
	g[i-1] /= (cut[i]-cut[i-1])
	if(g[i-1]<0){
		g[i-1] = -g[i-1]//to make positive,should be removed
	}
 }
 g[cut.length-1] /= (matrixrow - cut[cut.length-1])			 
 if(g[cut.length-1]<0){
	 g[cut.length-1] = -g[cut.length-1]
 }
 avg_s /= data.length 
 console.log(avg_s)
 return {avg:avg_s,group:g,sorted_points:sorted_points}
}
