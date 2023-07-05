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

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}

function subscribe() {
    var memberId = $("#memberId").val()
    var headers = { 'memberId' : memberId };

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

    stompClient.send("/app/fetch", {}, JSON.stringify({ 'memberId': memberId, 'timeStamp': new Date(2006, 0, 2, 15, 4, 5) }));
}

function sendMsg() {
    var memberId = $("#memberId").val();

    stompClient.send("/app/send", {}, JSON.stringify({'sendMemberId': memberId, 'recvMemberId': $("#recv-memberId").val(), 'timeStamp': giveCurrentTime(), 'message': $("#msg").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnecte    d");
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

const giveCurrentTime = () => {
    const timeElapsed = Date.now();
    const date = new Date(timeElapsed);

    const dateISO = date.toISOString().slice(0, 19) + "Z";
    return dateISO;
};