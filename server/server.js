/**
 * Created by KHJ on 2017-06-03.
 */
var express = require('express');
var app = express();

var bodyParser = require('body-parser')
// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({extended: false}))
// parse application/json
app.use(bodyParser.json())

var sqlite3 = require('sqlite3').verbose();


//server start/////////////////////////////////////////////////////////////////////////////////////////////////////
app.listen(8008, function () {
    console.log('Server listening on port 8008!');
});
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//DB////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
createDb();
function createDb() {
    db = new sqlite3.Database('./food.db', function (err) {
        if (err) {
            console.log("createDB or openDB get err :" + err);
        } else {
            console.log("createDB or openDB succeed");
        }
    });
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
app.get('/search', function (req,res) {
    console.log('dd')
    res.end('hi~')
})

app.post('/search', function (req, res) {
    console.log(req.body)
    console.log(JSON.parse(Object.keys(req.body)).COUNTRY)
    console.log(JSON.parse(Object.keys(req.body)).FOODNAME)
    console.log(JSON.parse(Object.keys(req.body)).COUNTRY2)

    let data = [];
    let data2 = [];
    let data3 = [];
    let data4 = [];
    let data5 = [];
    let data6 = [];
    let data7 = [];
    let data8 = [];
    let weight = [];
    db.all("SELECT * FROM " + JSON.parse(Object.keys(req.body)).COUNTRY + " WHERE FOODNAME = ?", [JSON.parse(Object.keys(req.body)).FOODNAME.toString()],
        function (err, rows) {
            if (err) {
                console.log(err)
            } else {
                rows.forEach(function (row) {
                    data.push(row);
                });
            }

            db.all("SELECT * FROM " + JSON.parse(Object.keys(req.body)).COUNTRY2 + " WHERE KIND = ?", [data[0].KIND.toString()],
                function (err, rows) {
                    if (err) {
                        console.log(err)
                    } else {
                        rows.forEach(function (row) {
                            data2.push(row);
                        });
                    }

                    for (i = 0; i < data2.length; i++) {
                        if (data2[i].CUISINE == data[0].CUISINE) {
                            data3.push(data2[i])
                        }
                    }

                    for (i = 0; i < data3.length; i++) {
                        weight[i] = 0
                    }


                    for (i = 0; i < data3.length; i++) {
                        let temp = 0;
                        for (j = 0; j < data[0].TASTE.split(",").length; j++) {
                            for (k = 0; k < data3[i].TASTE.split(",").length; k++) {
                                if (data[0].TASTE.split(",")[j] == data3[i].TASTE.split(",")[k]) {
                                    if (data[0].TASTE.split(",").length == 1 && data3[i].TASTE.split(",").length == 1) {
                                        weight[i] += 40;
                                        temp += 40
                                        data3[i]["TASTEweight"] = temp
                                    } else {
                                        weight[i] += 20;
                                        temp += 20
                                        data3[i]["TASTEweight"] = temp
                                    }

                                }

                            }
                        }
                    }

                    //console.log(Object.keys(JSON.parse(data[0].INGREDIENT))[0])
                    for (i = 0; i < data3.length; i++) {
                        let temp = 0;
                        for (j = 0; j < Object.keys(JSON.parse(data[0].INGREDIENT)).length; j++) {
                            for (k = 0; k < Object.keys(JSON.parse(data3[i].INGREDIENT)).length; k++) {
                                //console.log(data3[i].FOODNAME)
                                if (Object.keys(JSON.parse(data[0].INGREDIENT))[j] == Object.keys(JSON.parse(data3[i].INGREDIENT))[k]) {
                                    weight[i] += (25 * (JSON.parse(data[0].INGREDIENT)[Object.keys(JSON.parse(data[0].INGREDIENT))[j]] / 100))
                                    temp += (25 * (JSON.parse(data[0].INGREDIENT)[Object.keys(JSON.parse(data[0].INGREDIENT))[j]] / 100))
                                    data3[i]["INGREDIENTweight"] = temp
                                    //console.log(25*(JSON.parse(data[0].INGREDIENT)[Object.keys(JSON.parse(data[0].INGREDIENT))[j]]/100))
                                }

                            }
                        }
                    }


                    for (i = 0; i < data3.length; i++) {
                        let temp = 0;
                        if (data3[i].HOTNESS == data[0].HOTNESS) {
                            weight[i] += 25
                            temp += 25
                            data3[i]["HOTNESSweight"] = temp
                        }
                    }


                    for (i = 0; i < data3.length; i++) {
                        let temp = 0;
                        if (data3[i].DEGREE == data[0].DEGREE) {
                            weight[i] += 5
                            temp += 5
                            data3[i]["DEGREEweight"] = temp
                        }
                    }


                    for (i = 0; i < data3.length; i++) {
                        let temp = 0;
                        if (data3[i].MOUTHFEEL == data[0].MOUTHFEEL) {
                            weight[i] += 5
                            temp += 5
                            data3[i]["MOUTHFEELweight"] = temp
                        }
                    }




                    for (i = 0; i < data3.length; i++) {
                        data3[i]["weight"] = weight[i]
                    }

                    console.log(data3)


                    res.end(JSON.stringify(data3))
                })

        });

    // res.sendFile(filepath);
});