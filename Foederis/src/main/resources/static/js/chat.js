'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
//var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');
var uploadForm = document.querySelector('#uploadForm');

var stompClient = null;
var username = null;
var eventId = null;
var connected = false;

function connect(){
    username = document.querySelector('#userName').innerHTML.trim();

    //if(username) {
    //    usernamePage.classList.add('hidden');
    //    chatPage.classList.remove('close');	// Hacer visible 

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    //}
    //event.preventDefault();
        setTimeout(tryConnection,5000);
}

function tryConnection(){
	if (!connected)
		connect();
}

function disconnect(){
	stompClient.disconnect();
}

function endEvent(){
    // Notificar fin de evento
    stompClient.send("/app/chat.addUser/" + eventId,
        {},
        JSON.stringify({sender: username, type: 'END'})
    )

}

function onConnected() {
	connected = true;
    eventId = $("#eventId").text();
    
    // Suscribirse a topic/public/eventId para recibir los mensajes del chat
    stompClient.subscribe('/topic/public/' + eventId, onMessageReceived);
    // Suscribirse a topic/publi para mensaje de desconexión
    // stompClient.subscribe('/topic/public', onMessageReceived);

    // Notificar al servidor la conexión
    stompClient.send("/app/chat.addUser/" + eventId,
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('d-none');	// Ocultar
}


function onError(error) {
    connectingElement.textContent = 'No se pudo conectar al websocket. Por favor, espere o refresque la página para reintentarlo!';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage/" + eventId, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('list-group-item','list-group-item-success');
        messageElement.style.textAlign='center';
        message.content = message.sender + ' se unió al evento!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('list-group-item','list-group-item-success');
        messageElement.style.textAlign='center';
        message.content = message.sender + ' abandonó el evento!';
    } else if (message.type == 'END') {
    	messageElement.classList.add('list-group-item','list-group-item-success','text-danger');
        messageElement.style.textAlign='center';
        message.content = message.sender + ' finalizó el evento!';
        disconnect();
    } else {
        messageElement.classList.add('list-group-item','list-group-item-info');
    
        var usernameElement = document.createElement('span');
        usernameElement.classList.add('d-flex','justify-content-between','bg-info','mb-3');
        
        var userName = document.createElement('div');
        var userTimestamp = document.createElement('div');
        
        
        var usernameText = document.createTextNode(message.sender);
        userName.appendChild(usernameText);
        var userTimestampText = document.createTextNode(moment.tz(message.timestamp,"America/New_York").local().format("DD-MM-YYYY HH:mm:ss"));
        userTimestamp.appendChild(userTimestampText);
        
        
        usernameElement.appendChild(userName);
        usernameElement.appendChild(userTimestamp);
        
        messageElement.appendChild(usernameElement);
        $('#messageArea').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 500);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit', sendMessage, true);


