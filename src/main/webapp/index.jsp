<html>
<body>
<h2>WebSocket Demo</h2>

<form>
	<input type="text" id="messages">
	<input type="button" onclick="sendMessage()" value="Send Message"> 
</form>
<textarea id="textAreaMessage" rows="10" cols="50"></textarea>

<script type="text/javascript">

var websocket = new WebSocket("ws://localhost:8080/webSocketsPractice/serverendpoint");

websocket.onopen = function(message){processOpen(message);}
websocket.onclose = function(message){processClose(message);}
websocket.onerror = function(message){processError(message);}
websocket.onmessage = function(message){processMessage(message);}

function sendMessage(){
		websocket.send(messages.value);
		messages.value="";	
}

function processOpen(message){
	textAreaMessage.value += "Connected to the Server"+"\n";
}

function processClose(message){
	websocket.send("client disconnected");
	textAreaMessage.value += "Disconnected from the Server"+"\n";
}

function processError(message){
	textAreaMessage.value += "Error occured... \n";
}

function processMessage(msg){
	var jsonData = JSON.parse(msg.data);
	if(jsonData.message!=null)
		
	textAreaMessage.value += jsonData.message +" \n"
}
</script>
</body>
</html>
