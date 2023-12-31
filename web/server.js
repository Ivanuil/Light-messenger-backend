const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;

app.get('/login', function(req, res) {
    res.sendFile(path.join(__dirname, '/login.html'));
});

app.get('/chat', function (req, res) {
    res.sendFile(path.join(__dirname, '/chat.html'))
})

app.listen(port);
console.log('Server started at http://127.0.0.1:' + port + "/login");
