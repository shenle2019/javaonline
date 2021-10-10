// DOM(Document Object Mode), 就是库函数. DOM 节点, 就是页面里面的元素
// 前端用的语言叫做 JavaScript
var log = console.log.bind(console);

var ftest = function (a) {
    var lowercase = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    result = false;
    for (let i = 0; i < lowercase.length ; i++) {
        if( a == lowercase.substr(i,1)){
            result = true;
            break;
        }else{
            result = false;
        }
    }
    return result;
}

var ftest2 = function (b) {
    var lowercase = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    result = false;
    for (let i = 0; i < lowercase.length ; i++) {
        if( b == lowercase.substr(i,1)){
            result = true;
            break;
        }else{
            result = false;
        }
    }
    return result;
}

var ftest3 = function (c) {
    var lowercase = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
    result = false;
    for (let i = 0; i < lowercase.length ; i++) {
        if( c == lowercase.substr(i,1)){
            result = true;
            break;
        }else{
            result = false;
        }
    }
    return result;
}

var ftest4 = function (d) {
    result = true;
    for (let i = 0; i < d.length ; i++) {
        if( ftest3(d[i]) == false){
            result = false;
            break;
        }else{
            result = true;
        }
    }
    return result;
}

var buttonlogin = document.querySelector("#id-button-click")
buttonlogin.addEventListener("click", function () {

    var i = document.querySelector("#id-input-username")
    var text = i.value
    log("input value", text)

    var divn = document.querySelector("#id-output-result")
    // var body = document.querySelector("body")

    // 创建一个 html div 元素, 并且插入到 body
    // var div3 = document.createElement("div")
    // div3.innerHTML = "this is div";
    // innerHtml 会渲染 html 标签, innerText 不会
    // div3.innerHTML = "<button> innerButton </button>";

    // 校验输入
    if (text.length <2 || text.length > 10) {
        log("1")
        log("用户名错误")
        divn.innerText = "用户名错误";

    }else if(ftest(text[0]) == false) {
        log("2")
        log("用户名错误")
        divn.innerText = "用户名错误";

    }else if(ftest2(text[text.length - 1]) == false){
        log("3")
        log("用户名错误")
        divn.innerText = "用户名错误";

    }else if(ftest4(text) == false){
        log("4")
        log("用户名错误")
        divn.innerText = "用户名错误";

    }else{
        log("5")
        log("检查合格")
        divn.innerText = "检查合格";
        // body.appendChild(divn);
    }
})