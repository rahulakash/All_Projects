/*
Rahul Akash Nethaji - 1000996022
*/
var mongoose = require('mongoose');
var http = require('http'); 
var fs = require('fs');  
var io = require('socket.io'); 
var path = require('path');
var mime = require('mime');
var userModel;
var chatModel;
var dbconnected = false;
var Schema = mongoose.Schema;
var index;  
var chatRoom = {};
fs.readFile('./chat.html', function (err, data) {  
 if (err) {
    throw err;
 }
 index = data;
});

function sendFile(response, filepath, fileContents){
    response.writeHead(200, {"content-type": mime.lookup(path.basename(filepath))});
    response.end(fileContents);
}

function serverStatic(response, absPath){
    fs.readFile(absPath, function(err, data){
        if(err){
            console.log(err);
        }
        else{
            sendFile(response,absPath, data);
        }
    });
}

/* Create a server that listns on TCP port 2222 */
var server = http.createServer(function(request, response) {
    var filePath = false;
    if(request.url == '/'){
        filePath = './chat.html';
    }
    else{
        filePath = request.url;
    }
    var absPath = './' + filePath;
    serverStatic(response, absPath);
}).listen(2222);

/* Connect to MongoDB using mongoose */
mongoose.connect("mongodb://localhost:27017/Chat");
var db = mongoose.connection;

db.on('error', console.error.bind(console, 'connection error:'));
db.once("open",function callBack(){
    var userSchema = mongoose.Schema({
        UserName: String,
        UserEmail: String,
        Name: String,
        Password: String
    });
    var chatRoomSchema = mongoose.Schema({
    });
    userModel = mongoose.model("UserDetails", userSchema);
    chatModel = mongoose.model("ChatDetails", chatRoomSchema);
    dbconnected = true; 
});

var socket = io.listen(server);
var people = {};
var userRooms = [];
var chatRoomUserInfo = [];

/* Socket listens for a new client joining the server */
socket.on("connection", function(client){
    
    /* Each client is assinged with the following event handlers. 
       Each client is a new thread executing parallely. 
       Along with a socket connection between the clients and server.
       All events are handled asynchronously. */
    
    /* When client joins */
    client.on("join", function(roomName){
        try{
            client.join(roomName);
            chatRoomUserInfo[roomName].push(people[client.id]);
            client.emit("joinSuccess", roomName, chatRoomUserInfo[roomName]);
            if(userRooms[people[client.id]] == undefined){
                userRooms[people[client.id]] = [];
            }
            userRooms[people[client.id]].push(roomName);
            client.broadcast.to(roomName).emit("newChatUser", people[client.id], roomName);
        }
        catch(err){
        }
    });
    
    /* When client sends a message to a room */
    client.on("message", function(roomName, msg){
        console.log(roomName);
        client.broadcast.to(roomName).emit("message",{"RoomName":roomName, "Message":msg, "Sender": people[client.id]});
    });
    
    /* Authenticate user */
    client.on("login", function(username, password){
        if(dbconnected){
            userModel.findOne({'UserName': username, 'Password':password}, function(err, user){
                if(err){
                    client.emit("errorLogin", "Server unavailable"); 
                }
                else{
                    if(user==null){
                       client.emit("errorLogin", "Username or password invalid");  
                    }
                    else{
                        people[client.id] = username;
                        console.log(chatRoom);
                        client.emit("loginSuccess", chatRoom);
                    } 
                }
            });
        }
        else{
            client.emit("errorLogin", "Server unavailable");
        }  
    });
    
    client.on("logout", function(){
        try{
            delete people[client.id];
        }
        catch(err){
            console.log(err);
        }
    });
    
    client.on("signUp", function(data){
        console.log(data);
        userModel.findOne({'UserName': data.userName}, function(err, user){
            if(!err){
                if(user!=null){
                    console.log(user);
                    client.emit("errorLogin", "Username already exists");
                }
                else{
                    var newUser = new userModel({"UserName": data.userName, "Password": data.password, "UserEmail": data.email, "Name": data.name});
                    newUser.save(function(err, no){
                        if(err){
                            client.emit("errorLogin", "Server Unavailable");
                        }
                        else{
                            console.log(no);
                            if(no!=null){
                                client.emit("signupSuccess");
                            }
                            else{
                                client.emit("errorLogin", "Try again");
                            }
                        }
                    });
                }
            }
        });     
    });
    
    client.on("roomAvailablity", function(roomName){
        try{
            if(roomName==""){
                client.emit("roomUnavailable");
            }
            else if(chatRoom[roomName]==null){
                client.emit("roomAvailable");
            }
            else{
                client.emit("roomUnavailable");
            }
        }
        catch(err){
            console.log(err);
        }
    });
    
    client.on("createRoom", function(roomName){
        try{
            chatRoom[roomName] = roomName;
            console.log(roomName);
            socket.sockets.emit("newRoom", roomName);
            if(chatRoomUserInfo[roomName]==undefined){
                chatRoomUserInfo[roomName] = [];
            }
        }
        catch(err){
            console.log(err);
        }
        
    });
    
//    client.on("send", function(msg){
//        console.log(msg);
//        socket.sockets.emit("chat", people[client.id], msg);
//    });
    
    /* When client leaves a group */
    client.on("leaveGroup", function(room){
        try{
            client.broadcast.to(room).emit("userLeft", people[client.id], room);
            client.leave(room);
            var clientRooms = userRooms[people[client.id]];
            var index = clientRooms.indexOf(room);
            var indexUser = chatRoomUserInfo[room].indexOf(people[client.id]);
            if(indexUser>-1){
                chatRoomUserInfo[room].splice(indexUser,1);
            }
            if(index>-1){
                userRooms[people[client.id]].splice(index, 1);
                client.emit("leaveSuccess", room);
            }
        }
        catch(err){
            console.log(err);
        }
    });
    
    /* When client logsout or disconnects. remove user from all groups */
    client.on("disconnect", function(){
        try{
            var totalRooms = userRooms[people[client.id]].length;
            for(var i=0; i<totalRooms;i++ ){
                var room = userRooms[people[client.id]][i];
                client.leave(room);
                client.broadcast.to(room).emit("userLeft", people[client.id], room);
                var indexUser = chatRoomUserInfo[room].indexOf(people[client.id]);
                
                if(indexUser>-1){
                    chatRoomUserInfo[room].splice(indexUser,1);
                }
            }
            delete userRooms[people[client.id]];
            
            delete people[client.id];
        }
        catch(err){
        }

    }); 
});