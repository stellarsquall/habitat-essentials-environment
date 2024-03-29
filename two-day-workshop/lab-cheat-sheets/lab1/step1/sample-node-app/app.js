var express = require('express');
var path = require('path');
var index = require('./routes');

var app = express();
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(express.static(path.join(__dirname, 'public')));
app.use('/', index);

module.exports = app;
