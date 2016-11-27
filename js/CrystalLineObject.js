 //����������
function CrystalLineObject()
{
this.group=null;
this.path=null;
this.oldData=[];
 
this.init=function(id)
{
var arr=dataset[id];
this.group=svg.append("g");
 
var line = d3.svg.line()
.x(function(d,i){return xScale(i);})
.y(function(d){return yScale(d);});
 
//�������
this.path=this.group.append("path")
.attr("d",line(arr))
.style("fill","none")
.style("stroke-width",1)
.style("stroke",lineColor[id])
.style("stroke-opacity",0.9);
 
//���ϵ�е�СԲ��
this.group.selectAll("circle")
.data(arr)
.enter()
.append("circle")
.attr("cx", function(d,i) {
return xScale(i);
})
.attr("cy", function(d) {
return yScale(d);
})
.attr("r",5)
.attr("fill",lineColor[id]);
this.oldData=arr;
};
 
//������ʼ������
this.movieBegin=function(id)
{
var arr=dataset[i];
//����/ɾ��·��
var olddata=this.oldData;
var line= d3.svg.line()
.x(function(d,i){if(i>=olddata.length) return w-padding; else return xScale(i);})
.y(function(d,i){if(i>=olddata.length) return h-foot_height; else return yScale(olddata[i]);});
 
//·����ʼ��
this.path.attr("d",line(arr));
 
//�ضϾ�����
var tempData=olddata.slice(0,arr.length);
var circle=this.group.selectAll("circle").data(tempData);
 
//ɾ�������Բ��
circle.exit().remove();
 
//Բ���ʼ�������Բ��,������ĵ��Ҳ�ײ�
this.group.selectAll("circle")
.data(arr)
.enter()
.append("circle")
.attr("cx", function(d,i){
if(i>=olddata.length) return w-padding; else return xScale(i);
})
.attr("cy",function(d,i){
if(i>=olddata.length) return h-foot_height; else return yScale(d);
})
.attr("r",5)
.attr("fill",lineColor[id]);
 
this.oldData=arr;
};
 
//�ػ�Ӷ���Ч��
this.reDraw=function(id,_duration)
{
var arr=dataset[i];
var line = d3.svg.line()
.x(function(d,i){return xScale(i);})
.y(function(d){return yScale(d);});
 
//·������
this.path.transition().duration(_duration).attr("d",line(arr));
 
//Բ�㶯��
this.group.selectAll("circle")
.transition()
.duration(_duration)
.attr("cx", function(d,i) {
return xScale(i);
})
.attr("cy", function(d) {
return yScale(d);
})
};
 
//�ӻ���ɾ������
this.remove=function()
{
this.group.remove();
};
}

//���ͼ��
function addLegend()
{
var textGroup=legend.selectAll("text")
.data(lineNames);
 
textGroup.exit().remove();
 
legend.selectAll("text")
.data(lineNames)
.enter()
.append("text")
.text(function(d){return d;})
.attr("class","legend")
.attr("x", function(d,i) {return i*100;})
.attr("y",0)
.attr("fill",function(d,i){ return lineColor[i];});
 
var rectGroup=legend.selectAll("rect")
.data(lineNames);
 
rectGroup.exit().remove();
 
legend.selectAll("rect")
.data(lineNames)
.enter()
.append("rect")
.attr("x", function(d,i) {return i*100-20;})
.attr("y",-10)
.attr("width",12)
.attr("height",12)
.attr("fill",function(d,i){ return lineColor[i];});
 
legend.attr("transform","translate("+((w-lineNames.length*100)/2)+","+(h-10)+")");
}
 
//�����������
function getData()
{
var lineNum=Math.round(Math.random()*10)%3+1;
var dataNum=Math.round(Math.round(Math.random()*10))+5;
oldData=dataset;
dataset=[];
xMarks=[];
lineNames=[];
 
for(i=0;i<dataNum;i++)
{
xMarks.push("��ǩ"+i);
}
for(i=0;i<lineNum;i++)
{
var tempArr=[];
for(j=1;j<dataNum;j++)
{
tempArr.push(Math.round(Math.random()*h));
}
dataset.push(tempArr);
lineNames.push("ϵ��"+i);
}
}
 
//ȡ�ö�ά�������ֵ
function getMaxdata(arr)
{
maxdata=0;
for(i=0;i<arr.length;i++)
{
maxdata=d3.max([maxdata,d3.max(arr[i])]);
}
return maxdata;
}

