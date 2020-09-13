

var express = require('express');
var https = require('https');
var getJSON = require('get-json')
var passwordHash = require('password-hash');
var router = express.Router();
var mysql = require('mysql');
const parser=require('body-parser');



/* GET users listing. */


const con = mysql.createConnection({
    host: 'localhost', user: 'root', database: 'minesweeper', port:3308

});



router.post('/', function(req, res){




    if(!con){
        con.connect(function(err){
            if(err){
                res.status(500);
                return res.end(err.message);
            }
        });
    }
    console.log("korisnicko ime "+req.body.korisnicko_ime);

    let sql = "INSERT INTO users (username, ime_prezime, lozinka, email) VALUES (";
    sql += "'" + req.body.korisnicko_ime + "', ";
    sql += "'" + req.body.imePrezime + "', ";
    sql += "'" + passwordHash.generate(req.body.lozinka )+ "', ";
    sql += "'" + req.body.email + "' ";
    sql += ");";
     con.query(sql, function(err, result){
         if(err){
             res.status(500);
             return res.end(err.message);
         }
         res.status(200);
         let x={
             rez:
                 [{status:'Uspesna registracija'}],
         };
         console.log(x);
         return res.json(x);
     });
});





module.exports = router;