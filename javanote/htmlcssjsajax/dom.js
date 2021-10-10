// DOM(Document Object Mode), 就是库函数. DOM 节点, 就是页面里面的元素
// 前端用的语言叫做 JavaScript
var log = console.log.bind(console);

console.log("hello world")
var divs = document.querySelectorAll("div")
console.log(divs)

// 用 id 来取元素, id 名字之前加上一个 #
var div1 = document.querySelector("#id-div-1")
console.log("div1:", div1)


// 用 class 来取元素, class 名字之前加上一个 .
var div1Class = document.querySelector(".id-div-1")
console.log("div1Class: ", div1Class)

// 绑定 click 事件
// 事件就是一些操作, 做一个操作之后, 执行一个自定义的函数
// 这个机制, 就叫做事件
var buttonClick = document.querySelector("#id-button-click")
buttonClick.addEventListener("click", function () {
    console.log("button click")
})

// json 是现在流行的前后端数据交互格式
var j = {
    id: 1,
    content: "message"
}
// 把对象(字典)转成字符串, 这个字符串, 就是 json, 这就叫序列化
var jsonString = JSON.stringify(j);
console.log("json: ", jsonString, typeof jsonString)
// 把字符串转成对象(字典), 这就叫反序列化
var jsonObejct = JSON.parse(jsonString)
console.log("json object: ", jsonObejct, typeof jsonObejct)



var buttonDelete = document.querySelector("#id-button-delete")
buttonDelete.addEventListener("click", function () {
    console.log("button click")
    // 删除节点
    var div2 =  document.querySelector("#id-div-2")
    div2.remove()
    
    
    // 创建一个 html div 元素, 并且插入到 body
    var div3 = document.createElement("div")
    // div3.innerHTML = "this is div";
    // innerHtml 会渲染 html 标签, innerText 不会
    div3.innerHTML = "<button> innerButton </button>";
    // div3.innerText = "<button> innerButton </button>";
    var body = document.querySelector("body");
    body.appendChild(div3);


    // insertAdjacentHTML 可以直接插入 html 代码, 并且渲染
    var div4 = "<div>div4</div>"
    body.insertAdjacentHTML("beforeEnd", div4)
})


var input = document.querySelector("#id-input-1")
input.addEventListener("input", function () {
    log("input something")
    var i = document.querySelector("#id-input-1")
    var text = i.value
    log("input value", text)

    // 校验输入
    if (text.length > 3) {
        log("input length > 3")
        // 弹窗警告
        alert("这是一个不合法的输入")
    }

})