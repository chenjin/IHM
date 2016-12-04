function createXHR(){  
    if(window.XMLHttpRequest){  
        return new XMLHttpRequest();  
    }  
    else{  
        return new ActiveXObject("Microsoft.XMLHTTP");  
    }  
}  

function getAJAX(fn,url){  
	var xhr = createXHR();  
	xhr.open("GET",url,true);  
	xhr.onreadystatechange =  function(){  
		if(xhr.readyState == 4){//异步请求时的状态码4代表数据接收完毕  
			if(xhr.status == 200){//HTTP的状态 成功  
				//var data = eval("(" + xhr.responseText + ")");  
				fn(xhr.responseText);//实现函数的回调,将结果返回  
			}  
		}  
	}  
	xhr.send(null);  
}  