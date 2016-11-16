var app = require('express')();
var server = require('http').Server(app);

app.get('/', function(req, res){                  
    res.sendFile(__dirname + '/random_index_jc.html');
});

server.listen(3333);