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

        var socket = new SockJS('/foederis/ws');
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
            content: messageContent,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage/" + eventId, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function deleteMessage(ev){
    // Notificar borrado de mensaje chat
    var chatMessage = {
            sender: username,
            idChat: ev.srcElement.id,
            type: 'REMOVE'
        };

    stompClient.send("/app/chat.remove/" + eventId, {}, JSON.stringify(chatMessage));
}

function onMessageReceived(payload) {
    var butDeleteMessage = null;
    var textMessageElement = null;

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
    }else if (message.type == 'REMOVE'){
    	var chatMessage = document.querySelector('#Message_' + message.idChat);
    	if (chatMessage != null){
    		chatMessage.classList.add ('d-none');
    		return;
    	}
    } else {
        messageElement.classList.add('list-group-item','list-group-item-info');
    
        var usernameElement = document.createElement('span');
        textMessageElement = document.createElement('span');
        usernameElement.classList.add('d-flex','justify-content-between','bg-info','mb-3');
        textMessageElement.classList.add('d-flex','justify-content-between','mb-3');
        
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
	textElement.classList.add('justify-content-between');

    if (textMessageElement != null){
    	var messageZone = document.createElement('div');
    	messageZone.appendChild(textElement);
    	textMessageElement.appendChild(messageZone);
    	
    	// Solo el jefe de proyecto tendrá visible el boton de eliminar un mensaje
    	if (message.rol == 2){
	        var butDeleteMessage = document.createElement('button')
	    	var textDeleteMessage=document.createTextNode('Eliminar');
	        butDeleteMessage.classList.add('btn','btn-outline-danger');
	        butDeleteMessage.setAttribute("id", message.idChat);
	        butDeleteMessage.addEventListener("click", deleteMessage);
	    	butDeleteMessage.appendChild(textDeleteMessage);
	        textMessageElement.appendChild(butDeleteMessage);
    	}

        // Necesario para eliminar el mensaje
        messageElement.setAttribute("id","Message_" + message.idChat);
    	messageElement.appendChild(textMessageElement);
    	messageArea.appendChild(messageElement);
    }
    else {
    	messageElement.appendChild(textElement);
    	messageArea.appendChild(messageElement);
    }
    messageArea.scrollTop = messageArea.scrollHeight;
}


messageForm.addEventListener('submit', sendMessage, true);


