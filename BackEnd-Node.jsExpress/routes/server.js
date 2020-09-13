const express=require("express");
const app=express();
var mysql = require('mysql');
const port=3333;
var chackMap=false;


var router = express.Router();
const http=require("http").createServer();
const io=require("socket.io")(http);
const con = mysql.createConnection({
    host: 'localhost', user: 'root', database: 'minesweeper', port:3308

});

 function pokrenisql(sql){


    return  new Promise((resolve, reject) => {

        con.query(sql, function (err, result) {
            if (err) {
                console.log(err.message);
                reject(err.message);
            }
       resolve(result);

         });

     });

 }



 igraciWIN=[{
    username:"1",
    idSocket:"1",
    status:"slobodan",
}];

igraci=[{
    username:"1",
    idSocket:"1",
    status:"slobodan",
}];
io.on("connection",(socket)=>{

    socket.on("newUser",(data)=>{


        var checkP=winProvera(data);
        if(checkP==false){

       var x={
            username:data,
            idSocket:socket.id,
            status:"zauzet",

        };
        igraciWIN.push(x);
        }else{
            igraciWIN[checkP].idSocket=socket.id;
        }


        console.log("Igraci igraju"+igraciWIN);
    });
    function winProvera(member){
        var check=false;
        for(var i=0; i<igraciWIN.length; i++){
            if(igraciWIN[i].username==member){
                check= i;
            break;
            }
        }
        return check;
    }

    socket.on("igraci_spremni_zaigru",(data)=>{
      
        socket.emit("send-players",igraci);
        console.log(igraci.toString());
       
     
    });






    socket.on("win_game",(data)=>{
        data=data.split(",");
        var pobedio=[data[0]];
        var izgubio=[data[1]];
        var idgame=data[2];
        var vreme=data[3];


        for(var i =0; i<igraciWIN.length;i++){

            if(igraciWIN[i].username==pobedio){
                io.to(`${igraciWIN[i].idSocket}`).emit('EndGameWin', izgubio);
                ukloni(igraciWIN[i].username);
            }else if(igraciWIN[i].username==izgubio){
                console.log(izgubio);
                io.to(`${igraciWIN[i].idSocket}`).emit('EndGameLose', pobedio);
              ukloni(igraciWIN[i].username);
            }
        }
        var sql=`UPDATE onlinechallenge set winner='${pobedio}', vremeIgre='${vreme}' where id_game='${idgame}'`;
        con.query(sql, function (err, result) {
            if (err) {
                console.log(err.message);
            }
            console.log(200);

        });
    });



    socket.on("startGame",(data)=>{
         var res = data.split(",");
        var date=new Date();
        setbomba();

        var dd = String(date.getDate()).padStart(2, '0');
        var mm = String(date.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = date.getFullYear();

        var today =  yyyy+'-'+mm + '-' + dd+' ';
        today+=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();


        var sqlUPDATA=`UPDATE onlinechallenge SET player2='${res[1]}',bombe='${cekx+","+ceky}',datum_igre='${today}'  where player1='${res[0]}' and player2='/';`;
        var sqlD = `DELETE FROM onlinechallenge WHERE player1='${res[1]}' and player2='/';`;
        async function pokreni(){

        var x=cekx+","+ceky;
        var id_game= await pokrenisql(`select id_game from onlinechallenge where player1='${res[0]}' and player2='/'`);
            id_game=id_game[0].id_game;
        console.log("id"+id_game);
            var niz=x.split(",");
            await pokrenisql(sqlUPDATA);
            await pokrenisql(sqlD);

        posaljiBombe(res[0],res[1],niz,id_game);
        }
        pokreni();

    });

   socket.on("novi_igrac",(data)=>{


       var x={
            username:data.toString(),
            idSocket:socket.id,
           status:"slobodan",
       };
       var sql=`insert into onlinechallenge (player1,player2,winner,vremeIgre) values( '${x.username}', '/', '/','00:00:00');`;
       var check=provera(x.username);

       if(check===false){
       igraci.push(x);


           con.query(sql, function (err, result) {
               if (err) {
                   // res.status(500);
                   console.log(err.message);
                   // reject(err.message);
               }
               console.log(200);
               // res.status(200);

           });

    }else{con.query(sql, function (err, result) {
           if (err) {
               // res.status(500);
               console.log(err.message);
               // reject(err.message);
           }
           console.log(200);
           // res.status(200);

       });

           igraci[check].status="slobodan";

       }
       io.emit("send-players",igraci);
       console.log(igraci);

   });

   socket.on("igrac_napusta_igru",(data)=>{

    ukloni(data);
       io.emit("send-players",igraci);
    

});
function posaljiBombe(player1,player2,bombe,id){
    console.log("bome saljem");
    console.log(igraci);
    var brojac=0;
    for(var i =0; i<igraci.length;i++){
        if(igraci[i].username==player1||igraci[i].username==player2){
            bombe[20]=player1;
            bombe[21]=player2;
            id=String(id);
            bombe[22]=id;
            igraci[i].status="zauzet";

            console.log(bombe);
            io.to(`${igraci[i].idSocket}`).emit('StartBombe', bombe);
            ++brojac;

            if(brojac==2){


                io.emit("send-players",igraci);
                break;
            }
        }


    }

}


});

function ukloni(username){

        for (var i = 0; i < this.igraci.length; i++) {


            if (username == igraci[i].username) {
                igraci.splice(i, 1);

                io.emit("send-players", igraci);
                var sql = `DELETE FROM onlinechallenge WHERE player1='${username}' and player2='/';`;
                con.query(sql, function (err, result) {
                    if (err) {
                        // res.status(500);
                        console.log(err.message);
                        // reject(err.message);
                    }
                    console.log(200);
                    // res.status(200);

                });


            }

        }


}

function provera(username){
    var vrati=false;
   
    for(var i=0; i<this.igraci.length; i++){
     

        if(username==igraci[i].username){
            vrati=i;
        }
    }
   console.log("provera vraca"+vrati);
    return vrati;
}


http.listen(port,()=>{
});

















var cekx=[];
    cekx=new Array();
var ceky=[];
ceky=new Array();


console.log("");

function setbomba() {
    var x = 0;
    var y = 0;
     cekx = [-1];
     ceky = [-1];

    for (var i = 0; i < 10; i++) {

        x = randomic(9);
        y = randomic(9);



        var proveri = proveraI(cekx, ceky, x, y, i);

        if (proveri == true) {
            cekx[i] = x;

            ceky[i] = y;


        } else {

            --i;

        }


    }


}



function proveraI(cekxx,cekyy,x,y,n) {

    var ceking = false;

    for (var i = 0; i <= n; i++) {
        if (cekxx[i] == x && cekyy[i] == y) {
            ceking = false;

            break;

        } else {

            ceking = true;

        }

    }


    return ceking;


}











function randomic(broj) {

    var max = broj - 1;
    var min = 0;
    var range = max - min + 1;
    var rand = Math.floor((Math.random() * range) + min);


    return rand;

}




module.exports = router;