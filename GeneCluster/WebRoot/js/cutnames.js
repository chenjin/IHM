//group gene names,return grouped gene names
var cutnames = function(data_cut,names,groups){
   var grouped_names = new Array(buckets)
   for(var i =0;i<buckets;i++){
       grouped_names[i] = new Array(groups[i].length)
   }
   var row =0,inx = 0,ing_inx=0
   while(row < data.length){
	     for(row=data_cut[inx];row<data_cut[inx+1];row++){
		     grouped_names[inx][ing_inx] =names[row]
		     ing_inx++;
		 }
		 if(inx+1 < buckets -1){
		     inx++
		     ing_inx=0;
		 }else{
		     for(row = data_cut[buckets-1];row < data.length;row++){
		         grouped_names[buckets-1][ing_inx] = names[row]
		         ing_inx++;
		     } 
		 }
    }
    return grouped_names
}