var webSocket = new WebSocket(SOCKET_URL);
var echoText = document.getElementById("echoText");
echoText.value = "";
var message = document.getElementById("message");
var inProgressSpan = $("#opInprogressNotice");
var overlayDiv = $("#overlay");
var resultButton = $("#resultButton");
webSocket.onopen = function(message){ wsOpen(message);};
webSocket.onmessage = function(message){ wsGetMessage(message);};
webSocket.onclose = function(message){ wsClose(message);};
webSocket.onerror = function(message){ wsError(message);};
function wsOpen(message){
	
}
function wsSendMessage(){
	webSocket.send(message.value);
	message.value = "";
}

function wsGetMessage(response){
	overlayDiv.show();
	var jsonObj = JSON.parse(response.data)
	var inProgress=""
	switch(jsonObj.eventType) {
	  case "BEGIN_UNIZIP":
		inProgress= "Unzipping files..."
	    break;
	  case "END_UNZIP":
		  inProgress= "Unzip completed!"
		  break;
	  case "BEGIN_ANALISYS":
		  inProgress= "Begin analisys..."
		  break;
	  case "END_ANALISYS":
		  inProgress= "Analisys completed!"
		  break;
	  case "GET_RESULTS":
		  inProgress= "Analisys completed!"
	      resultButton.attr("href","/ProgettoIGES/Results?analysisId="+jsonObj.payload)
		  resultButton.show();
		  break; 
	  case "STREAM_TEXT":
		  inProgress= "Analizing..."
		  break;
	  default:
	    // code block
	}
	if(inProgressSpan.text() != inProgress){
		setTimeout(function(){inProgressSpan.text(inProgress); }, 2000);
	}
	
	var newText = jsonObj.textResponse;
	if(newText!=""){
		$(echoText).val(newText);
		$(echoText).scrollTop(99999999999);
	}
	
	
}
function wsClose(message){
	setTimeout(function(){ overlayDiv.hide(); }, 3000);
	if(message.reason=="FINISH"){
		
	}
}

function wserror(message){
	echoText.value += "Error ... \n";
}
