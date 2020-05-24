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

function connect(){
    username = document.querySelector('#userName').innerHTML.trim();

    //if(username) {
    //    usernamePage.classList.add('hidden');
        chatPage.classList.remove('close');	// Hacer visible 

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    //}
    //event.preventDefault();
}


function onConnected() {

    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');	// Ocultar
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
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
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('list-group-item','list-group-item-success');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('list-group-item','list-group-item-info');
    
        var usernameElement = document.createElement('span');
        //usernameElement.style.color='blue';
        usernameElement.classList.add('d-flex','justify-content-between','bg-info','mb-3');
        
        var userName = document.createElement('div');
        var userTimestamp = document.createElement('div');
        
        
        var usernameText = document.createTextNode(message.sender);
        userName.appendChild(usernameText);
        var userTimestampText = document.createTextNode(message.timestamp);
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
    // messageArea.scrollTop = messageArea.scrollHeight;
}

/*
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}
*/
//function initChat() {
//	if (username == null)
//		username = document.querySelector('#userName').innerHTML.trim();
//	
//	if (usernameForm != null)
//		usernameForm.addEventListener('submit', connect, true);
	
//	if (messageForm != null)
		messageForm.addEventListener('submit', sendMessage, true);
//}

