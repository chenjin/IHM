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
		if(xhr.readyState == 4){//�첽����ʱ��״̬��4�������ݽ������  
			if(xhr.status == 200){//HTTP��״̬ �ɹ�  
				//var data = eval("(" + xhr.responseText + ")");  
				fn(xhr.responseText);//ʵ�ֺ����Ļص�,���������  
			}  
		}  
	}  
	xhr.send(null);  
}  