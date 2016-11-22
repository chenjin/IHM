var express = require('express')
var app = express()
var path = require('path');
var server = require('http').Server(app);
app.use(express.static(path.join(__dirname, 'js')));
app.use(express.static(path.join(__dirname, 'css')));
app.get('/', function(req, res){
    res.sendFile(__dirname + '/random_index_jc.html');
});

server.listen(3333);