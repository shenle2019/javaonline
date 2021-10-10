console.log("this is  1.js")

// log 套路
var log = console.log.bind(console);

log("this is log");

var n = 1;
var s = "test";
log("n: ", n)
log("s: ", s)


// 定义一个函数
function f1() {
    log("this is f1")
}

f1()

// 定义函数方法 2
var f2 = function () {
    log("this is f2")
}

f2()


var f3 = function (a) {
    log("this is f3", a)
    // js 里面比较两个变量, 用 ===, 会加上类型比较, 不是同一类型的, 就为 false
    // == 会把等式两边转成同一个类型
    if (a === 666) {
        console.log("a is 666");
    }
}

f3(666)
f3("string")
f3("666")


// js 里面的数组
// 定义一个空数组
var a1 = [];

a1 = [1,2,3,4]
log("a1: ", a1)

a1.push(6);
log("a1: ", a1)

a1.push("string");
log("a1: ", a1)

// 数组越界, 会取到一个 undefined
log("a1[9] : ", a1[9])

var a2 = null;

// 字典
var d = {
    // 这里的 id, 会自动转成字符串, 相当于 "id": 1
    id: 1,
    content: "string",

    hello: function (username) {
        log("d.hello ", username)
    }
}

log("d:", d)

d["message"] = "this is message"
log("d:", d)

log("d id", d["id"])
log("d id", d.id)
d.hello("usopp")


// js 里面新抄的 java 的玩意
var map = new Map();
map.set("content", "this is content")
log("map: ", map)

