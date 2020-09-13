var express = require('express');
var https = require('https');
var getJSON = require('get-json')

var router = express.Router();
var mysql = require('mysql');
const parser=require('body-parser');


const con = mysql.createConnection({
    host: 'localhost', user: 'root', database: 'minesweeper', port:3308

});



router.get('/:id', function(req, res){




    if(!con){
        con.connect(function(err){
            if(err){
                res.status(500);
                console.log(err.message);
                return res.end(err.message);

            }
        });
    }

    let sql = `SELECT count(*)  AS provera FROM users WHERE username='${req.params.id}';`;
    console.log(sql);
    con.query(sql, function(err, result){
        if(err){
            res.status(500);
            console.log(err.message);
            return res.end(err.message);
        }
        res.status(200);

        let x= {
            odgovor:result
        };
        console.log(x);
   return  res.json(x);

    });
});




router.get('/email/:id', function(req, res){


    if(!con){
        con.connect(function(err){
            if(err){
                res.status(500);
                console.log(err.message);
                return res.end(err.message);

            }
        });
    }
    console.log(req.params.id);
    let sql = `SELECT count(*)  AS provera FROM users WHERE email='${req.params.id}';`;
    console.log(sql);
    con.query(sql, function(err, result){
        if(err){
            res.status(500);
            console.log(err.message);
            return res.end(err.message);
        }
        res.status(200);

        let x= {
            odgovor:result
        };
        console.log(result);
        return res.json(x);
    });
});

module.exports = router;
