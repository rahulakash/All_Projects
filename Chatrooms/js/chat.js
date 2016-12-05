
$(document).ready(function(){
    var socket = io.connect("http://localhost:2222/");
    var userName;
    var password;
    $(".divLoginWidget").show();
    $('.divChatWindow').hide();
    $('.dropdown').hide();

    $('.divSignUpForm').hide();
    $('#divErrorStatus').hide();
    
    /* Toggle between login and signup */
    $('.divLogin').click(function(){
        $('.divLoginForm').show();
        $('.divSignUpForm').hide();
        $('.divSignUp').addClass("transparent");
        $(this).removeClass("transparent");
    });
    
    /* Toggle between login and signup */
    $('.divSignUp').click(function(){
        $('#divErrorStatus').hide();
        $('.divLoginForm').hide();
        $('.divSignUpForm').show();
        $('.divLogin').addClass("transparent");
        $(this).removeClass("transparent");
    });
    
    /* Login Button Click */ 
    $('#btnLogin').click(function(){
        $('#modalLoading').modal('show');
        $('#divErrorStatus').hide();
        var errorMsg;
        userName = $('#txtUsername').val();
        password = $('#txtPassword').val();
        if(userName.trim()!=""){
            if(password.trim()!=""){
                setTimeout(function(){
                    // Emit login info to server for authentication.
                    socket.emit("login", userName, password);
                }, 2000);  
            }
            else{
                $('#modalLoading').modal('hide');
                displayErrorMsg(" Please enter Password");
            }
        }
        else{
            $('#modalLoading').modal('hide');
            displayErrorMsg(" Please enter Username");
        }
    });
    
    /* Sign up button click */
    $('#btnSignUp').click(function(){
        $('#modalLoading').modal('show');
        $('#divErrorStatus').hide();
        var errorMsg;
        var name = $('#txtName').val();
        var userNameSign = $('#txtSignUsername').val();
        var passwordSign = $('#txtSignPassword').val();
        var email = $('#txtEmail').val();
        if(name.trim()!=""){
            if(userNameSign.trim()!=""){
                if(passwordSign.trim()!=""){
                    setTimeout(function(){
                        // Emit user info to server for authentication.
                        socket.emit("signUp", {"name":name,"userName":userNameSign,"password":passwordSign,"email":email});
                    }, 2000);
                }
                else{
                    $('#modalLoading').modal('hide');
                    displayErrorMsg(" Please enter Password");
                }
            }
            else{
                $('#modalLoading').modal('hide');
                displayErrorMsg(" Please enter Username");
            }
        }
        else{
            $('#modalLoading').modal('hide');
            displayErrorMsg(" Please enter your Name");
        }
    });
    
    /* Logout button click */
    $('#aLogout').click(function(){
        location.reload();
        /* Emit disconnect message to server */
        socket.emit("disconnect");
    });
    
    /* Check room availiblity as user types */
    $('#txtRoomName').keyup(function(){
        var roomName = $(this).val().replace(/ /g,'');
        socket.emit("roomAvailablity", roomName);
    });
    
    /* Create room button click */
    $('#btnCreateRoom').click(function(){
        var roomName = $('#txtRoomName').val().trim();
        $('#txtRoomName').val("");
        $(".divRoomName").removeClass("has-error");
        $(".divRoomName").removeClass("has-success");
        $(".divRoomNameStatus").html("");
        socket.emit("createRoom", roomName);
    });
    
    /* Click on send message */
    $("#btnSendMessage").click(function(){
        var roomName = $(this).val();
        var msg = $("#txtTypedMsg").val();
        $("#txtTypedMsg").val("");
        socket.emit("message", roomName, msg);
        $("#Msgs"+roomName+"").append("<span class='msgRight'><b>Me: </b>"+msg+"</span><br><br><br>");
    });
    
    $("#txtTypedMsg").keypress(function(e){
        if(e.which == 13){
            var roomName = $("#btnSendMessage").val();
            var msg = $("#txtTypedMsg").val();
            $("#txtTypedMsg").val("");
            socket.emit("message", roomName, msg);
            $("#Msgs"+roomName+"").append("<span class='msgRight'><b>Me: </b>"+msg+"</span><br><br><br>");
        }
    });
    
    
    /* Event handler when server forwards message to a group */
    socket.on("message", function(data){
        // If the current chat room active is the room the message is intended for then append the meesage. 
        if($('.divChatBox .divisionTitle').html()==data.RoomName){
            $("#Msgs"+data.RoomName+"").append("<span class='msgLeft'><b>"+data.Sender+": </b>"+data.Message+"</span><br><br><br>");
        }
        else{
            // If the current chat room active is not the room the message is intended for then append the meesage and increment the count of no of msgs unread.
            var msgsCount = Number($("#"+data.RoomName+" .badge").html());
            msgsCount += 1;
            $("#"+data.RoomName+" .badge").html(msgsCount);
            $("#Msgs"+data.RoomName+"").append("<span class='msgLeft'><b>"+data.Sender+": </b>"+data.Message+"</span><br><br><br>"); 
        }
    });
    
    /* Event handler when new user joins a group */
    socket.on("newChatUser", function(user, room){
        if($('.divChatBox .divisionTitle').html()==room){
            $("#Msgs"+room+"").append("<span class='msgMiddle'><b>"+user+" has joined the room </b></span><br><br><br>");
        }
        else{
            var msgsCount = Number($("#"+room+" .badge").html());
            
            msgsCount += 1;
            $("#"+room+" .badge").html(msgsCount);
            $("#Msgs"+room+"").append("<span class='msgMiddle'><b>"+user+" has joined the room </b></span><br><br><br>"); 
        }
        $(".online"+room+"").append("<p id='online"+room+""+user+"'><b>"+user+"</b></p><br>");
    });
    
    /* Event handler when a user leaves a group */
    socket.on("userLeft", function(user, room){
        if($('.divChatBox .divisionTitle').html()==room){
            $("#Msgs"+room+"").append("<span class='msgMiddle'><b>"+user+" has left the room </b></span><br><br><br>");
        }
        else{
            var msgsCount = Number($("#"+room+" .badge").html());
            msgsCount += 1;
            $("#"+room+" .badge").html(msgsCount);
            $("#Msgs"+room+"").append("<span class='msgMiddle'><b>"+user+" has left the room </b></span><br><br><br>"); 
        }
        $("#online"+room+""+user+"").remove();
    });
    
    /* Event handler when a new room is created by someone */
    socket.on("newRoom", function(roomName){
        var divChatRoom = "<div id="+roomName+" class='chatRoomTile row'>";
        divChatRoom+= "<div class='col-md-5'><b>"+roomName+"</b></div>";
        divChatRoom+= "<div id='divBtn"+roomName+"' class='col-md-3'><button value="+roomName+" type='button' class='btn btn-sm btn-default btnChatRoomJoin'>Join</button></div>&nbsp;<span class='badge'></span></div>";
        $('.divChatRooms').append(divChatRoom);
        
        $(".btnChatRoomJoin").unbind( "click" );
        $(".chatRoomTile").unbind("click");
        $('.btnChatRoomJoin').click(function(){
            //alert("ok");
            var chatRoom = $(this).val();
            //alert(chatRoom);
            socket.emit("join", chatRoom);
        });
        
        $(".chatRoomTile").click(function(){
            if($(this).attr("value")=="joined"){
                $(".divBtnSend").show();
                $(".divChatMsgs div").hide();
                
                $(".divChatRooms .chatRoomTile").removeClass("select");
                var roomName = $(this).attr("id");
                $("#btnLeave").attr("value",roomName);
                $(".secOnlineUsers div").hide();
                $(".online"+roomName+"").show();
                $("#btnLeave").attr("value",roomName);
                $("#Msgs"+roomName+"").show();
                $("#"+roomName+"").addClass("select");
                $('#'+roomName+' .badge').html("0");
                $(".divChatBox .divisionTitle").html(roomName);
                $("#btnSendMessage").val(roomName);
            }
            else{
                return;
            }
        });
    });
    
    $("#btnLeave").click(function(){
        socket.emit("leaveGroup", $(this).val());
    });
    
    /* Event handler when you leave a group is successful */
    socket.on("leaveSuccess", function(room){
        $("#divBtn"+room+"").html("<button value="+room+" type='button' class='btn btn-sm btn-default btnChatRoomJoin'>Join</button>");
        $("#"+room+"").attr("value","");
        $('#'+room+' .badge').html("");
        $("#Msgs"+room+"").remove();
        $(".divBtnSend").hide();
        $(".online"+room+"").remove();
        $(".divChatBox .divisionTitle").html("");
        $(".divChatRooms .chatRoomTile").removeClass("select");
        $('.btnChatRoomJoin').click(function(){
            var chatRoom = $(this).val();
            socket.emit("join", chatRoom);
        });
    });
    
    
    /* Event handler when joining a group is successful */
    socket.on("joinSuccess", function(roomName, chatRoomUsers){
        $(".divBtnSend").show();
        $("#btnLeave").show();
        $("#btnLeave").attr("value",roomName);
        $(".divChatRooms .chatRoomTile").removeClass("select");
        $("#"+roomName+"").addClass("select");
        $("#"+roomName+"").attr("value","joined");
        $(".divChatBox .divisionTitle").html(roomName);
        $("#btnSendMessage").val(roomName);
        $("#divBtn"+roomName+"").html("Joined");
        $(".divChatMsgs div").hide();
        $(".divChatMsgs").append("<div id='Msgs"+roomName+"'></div>");
        $("#Msgs"+roomName+"").show();
        $(".secOnlineUsers div").hide();
        $(".secOnlineUsers").append("<div class='online"+roomName+"'></div>");
        console.log(chatRoomUsers);
        for(var i=0;i<chatRoomUsers.length;i++){
            $(".online"+roomName+"").append("<p id='online"+roomName+""+chatRoomUsers[i]+"'><b>"+chatRoomUsers[i]+"</b></p><br>");
        }
        $(".online"+roomName+"").show();
    });
    
    
    
    socket.on("roomAvailable", function(){
        $(".divRoomName").removeClass("has-error");
        $(".divRoomName").addClass("has-success");
        $(".divRoomNameStatus").html("<span class='glyphicon glyphicon-ok form-control-feedback'>Available</span>");
        $("#btnCreateRoom").removeAttr("disabled");
    });
    
    socket.on("roomUnavailable", function(){
        $(".divRoomName").addClass("has-error");
        $(".divRoomName").removeClass("has-success");
        $(".divRoomNameStatus").html("<span class='glyphicon glyphicon-remove form-control-feedback'>Not available</span>");
        $("#btnCreateRoom").attr("disabled","true");
    });
    
    socket.on("errorLogin", function(msg){
        $('#modalLoading').modal('hide');
        displayErrorMsg(msg);
    });
    
    /* Event handler on login success */
    socket.on("loginSuccess", function(chatRooms){
        $('#modalLoading').modal('hide');
        $("#btnLeave").hide();
        $(".divBtnSend").hide();
        //setCookie("user",userName+","+password, 3);
        $('#dropdownUser').prepend(userName);
        $('#txtUsername').val("");
        $('#txtPassword').val("");
        $(".divLoginWidget").hide();
        $('.divChatWindow').show();
        $('.dropdown').show();
        var divChatRooms = $(".divChatRooms");
        //divChatRooms
        console.log(chatRooms);
        for(chatRoom in chatRooms){
            var divChatRoom = "<div id="+chatRoom+" class='chatRoomTile row'>";
            divChatRoom+= "<div class='col-md-5'><b>"+chatRoom+"</b></div>";
            divChatRoom+= "<div id='divBtn"+chatRoom+"' class='col-md-3'><button value="+chatRoom+" type='button' class='btn btn-sm btn-default btnChatRoomJoin'>Join</button></div>&nbsp;<span class='badge'></span></div>";
            divChatRooms.append(divChatRoom);
        }
        
        $(".btnChatRoomJoin").unbind( "click" );
        $(".chatRoomTile").unbind("click");
        $('.btnChatRoomJoin').click(function(){
            var chatRoom = $(this).val();
            socket.emit("join", chatRoom);
        }); 
        $(".chatRoomTile").click(function(){
            if($(this).attr("value")=="joined"){
                $(".divBtnSend").show();
                $(".divChatMsgs div").hide();
                
                $(".divChatRooms .chatRoomTile").removeClass("select");
                var roomName = $(this).attr("id");
                $("#btnLeave").attr("value",roomName);
                $(".secOnlineUsers div").hide();
                $(".online"+roomName+"").show();
                $("#Msgs"+roomName+"").show();
                $('#'+roomName+' .badge').html("0");
                $("#"+roomName+"").addClass("select");
                $(".divChatBox .divisionTitle").html(roomName);
                $("#btnSendMessage").val(roomName);
            }
            else{
                return;
            }
        });
    });
    
    /* Event handler on sign up success */
    socket.on("signupSuccess", function(){
        $('#modalLoading').modal('hide');
        $('#modalAlert').modal('show');
        document.getElementById("formSignup").reset();
    });
});
    

function displayErrorMsg(errorMsg){
    $('#divErrorMsg').html(errorMsg);
    $('#divErrorStatus').show();
}

function setCookie(name, val, expiryDays){
    var date = new Date();
    date.setTime(date.getTime() + (expiryDays*24*60*60*1000));
    document.cookie = name+"="+val+";"+"expires="+date.toUTCString();
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
    }
    return "";
}

function removeCookie(name, val){
    document.cookie = name+"="+val+";"+"expires=Thu, 01 Jan 1970 00:00:00 UTC";
}