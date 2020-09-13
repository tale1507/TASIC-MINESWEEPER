var express = require('express');

var https = require('https');
var getJSON = require('get-json')
var passwordHash = require('password-hash');
var router = express.Router();
var mysql = require('mysql');
const parser=require('body-parser');

let x={};

/* GET users listing. */


const con = mysql.createConnection({
  host: 'localhost', user: 'root', database: 'minesweeper', port:3308

});

 function pokrenisql(sql,res){
   return  new Promise((resolve, reject) => {
      if (!con) {
        con.connect(function (err) {
          if (err) {
            res.status(500);
            reject(err.message);
          }
        });
      }
      con.query(sql, function (err, result) {
        if (err) {
          res.status(500);
          console.log(err.message);
          reject(err.message);
        }
        res.status(200);


        resolve(result);

      });

    });

}

router.post('/prijava',function (req,res,nex) {


    let sql = `Select *  from users where username='${req.body.username}' `;


    con.query(sql, function(err, result){
      if(err){
        res.status(500);
        console.log(err.message);
        return res.end(err.message);
      }
      res.status(200);

      if(result.length==0){
          let sql = `Select *  from users;`;

          con.query(sql, function(err, result1) {
              if (err) {
                  res.status(500);
                  console.log(err.message);
                  return res.end(err.message);
              }
              res.status(200);

              x = {
                  odgovor: {status: "username", odg: result1}
              };

          });
      }else{


      if(passwordHash.verify(req.body.lozinka, result[0].lozinka)){

        x= {
          odgovor:{status:"OK",odg:result}

        };


      }else{
         x= {
          odgovor:{status:"lozinka",odg:result}
        };
      }
      }

let p= {
      broj:[x]
    };


        return res.json(p);
    });

  });
router.post('/singlegame',(req,res,next)=>{

  let sql = `Select *  from singlegame where username='${req.body.username}'`;
    var d = new Date();
    var time=req.body.sat+":"+req.body.minut+":"+req.body.sekund;

  async function display() {
      var result=  await pokrenisql(sql,res);
      if(result.length==0){


          let sqlUpisiRez=`INSERT INTO singlegame (username,najbolje_vreme)VALUES ('${req.body.username}','${time}');`;
          pokrenisql(sqlUpisiRez,res);
          console.log(sqlUpisiRez);
      }else {
        let sqlUp=`UPDATE singlegame SET najbolje_vreme = '${time}' WHERE username='${req.body.username}'and najbolje_vreme >'${time}';`;
          pokrenisql(sqlUp,res);
      }
      res.end();

  }

    display();



  });




router.post('/singlegame/rank',(req,res)=>{

  let sql = `Select *  from singlegame order by najbolje_vreme ASC`;

  async function display() {
    var result = await pokrenisql(sql,res);
        console.log(req.body.username);
     let rank= result.findIndex(ranki=>ranki.username===`${req.body.username}`);


      let vrati= {
          no:++rank,
          lista:result
      };
        console.log(vrati.lista);
        return res.json({objekat:vrati});

  };
    display();





});


module.exports = router;
