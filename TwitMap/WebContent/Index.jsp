<!DOCTYPE html>
<html>
    <head>
        <title>JEE7 WebSocket Example</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="/websocket/favicon.ico">
        <style>
            
        </style>
        <script>
            var chatClient = new WebSocket("ws://localhost:8080/TwitMap/hello");
            
            chatClient.onmessage = function(evt) {
                var p = document.createElement("p");
                p.innerHTML = "Server: " + evt.data;
                var container = document.getElementById("container");
                container.appendChild(p);
            }
            
            function send() {
                
                chatClient.send("");
            	
            }
        </script>
    </head>
    <body>
        <h1>JEE7 WebSocket Example</h1>
        <div id="container">
            
        </div>
        <button type="button" id="send" onclick="send()">Start receiving message from server</button>
    </body>
</html>