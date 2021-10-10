// ajax (Asynchronous JavaScript + XML)

var ajax = function (method, path, data, callback) {
    var r = new XMLHttpRequest();
    r.open(method, path, true)

    r.setRequestHeader("Content-Type", "application/json")

    r.onreadystatechange = function () {
    if (r.readyState === 4) {
        callback(r.response)
    }
    }

    data = JSON.stringify(data);
    r.send(data)
}

var url = "https://www.kuaibiancheng.com/sandbox/todo/3587405093/all"
var method = "GET";
var data = ""

ajax(method, url, data, function (response) {
    console.log("response: ", response)
})
