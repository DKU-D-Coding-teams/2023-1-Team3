var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    // if (connected) {
    //     $("#conversation").show();
    // }
    // else {
    //     $("#conversation").hide();
    // }
}

function connect() {
    var socket = new SockJS('/chat');
    var memberId = 1;
    var headers = { 'memberId' : memberId };

    console.log("checking method connect()");
    console.log("connect() memberId: " + memberId);

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}

function subscribe() {
    var memberId = $("#memberId").val()
    var headers = { 'memberId' : memberId };

    console.log("subscribe() memberId: " + memberId);

    stompClient.subscribe(`/topic/${memberId}`, function (response) {
        onMessageReceived(response);
    }, headers);
    fetch();
}

function onMessageReceived(response) {
    var chatMessageResponse = JSON.parse(response.body)

    if(chatMessageResponse.status === "SEND") {
        printMessage(chatMessageResponse);
    }
    else if (chatMessageResponse.status === "FETCH") {
        if (chatMessageResponse.count !== 0) {
            printMessage(chatMessageResponse);
        }
    }
    else { // "RE-FETCH"
        printMessage(chatMessageResponse);
        fetch();
    }
}

function printMessage(chatMessageResponse) {
    console.log(chatMessageResponse.chatMessages)
    for (let i = 0; i < chatMessageResponse.count; i++) {
        var chatMessage = chatMessageResponse.chatMessages[i];
        $("#chat").append("<tr><td>"
            + chatMessage.timeStamp + " "
            + "sender: " + chatMessage.sendMemberId + " "
            + "recver: " + chatMessage.recvMemberId + " "
            + "message: " + chatMessage.message + "</td></tr>");
    }
}

function fetch() {
    var memberId = $("#memberId").val();
    var accessToken = $("#access-token").val();
    var headers = { 'memberId' : memberId, 'accessToken' : accessToken };

    console.log("fetch() memberId: " + memberId);
    console.log("fetch() accessToken: " + accessToken);

    stompClient.send("/app/fetch", headers, JSON.stringify({ 'memberId': memberId, 'timeStamp': new Date(2006, 0, 2, 15, 4, 5) }));
}

function sendMsg() {
    var memberId = $("#memberId").val();
    var accessToken = $("#access-token").val();
    var headers = { 'memberId' : memberId, 'accessToken' : accessToken };

    stompClient.send("/app/send", headers, JSON.stringify({'sendMemberId': memberId, 'recvMemberId': $("#recv-memberId").val(), 'timeStamp': new Date(2006, 0, 2, 15, 4, 5), 'message': $("#msg").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#fetch" ).click(function() { subscribe(); });
    $( "#send" ).click(function() { sendMsg(); });
});