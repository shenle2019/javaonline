# Java-web学习笔记010-html+css+js



#### 描述事件添加流程

```js
1.在html中定义一个标签,如button 或者input

<button id="id-button-delete">
删除
</button>

<input id="id-input-1">

2.DOM.js中通过querySelector查找到对应的元素;

varbuttonDelete=document.querySelector("#id-button-delete")

3.监听对应的操作,如点击/输入等(即绑定对应事件)

buttonDelete.addEventListener("click",function(){
console.log("buttonclick")
input.addEventListener("input",function(){
log("inputsomething")


4.有对应事件触发后,执行一个自定义的函数

如删除节点/增加节点/判断输入等;

//删除节点
var div2=document.querySelector("#id-div-2")
div2.remove()

//校验输入
if(text.length>3){
log("inputlength>3")
//弹窗警告
alert("这是一个不合法的输入")
}
```



#### 文字描述 js 回调机制。


 js 回调机制实际是把函数作为对象,相当于变量,能作为函数参数被传递，能在函数中被创建，能从函数中返回;

因为函数在Javascript中是第一类对象，我们像对待对象一样对待函数，因此我们能像传递变量一样传递函数，在函数中返回函数，在其他函数中使用函数。

当我们将一个回调函数作为参数传递给另一个函数是，我们仅仅传递了函数定义。我们并没有在参数中执行函数。我们并不传递像我们平时执行函数一样带有一对

执行小括号()的函数,即回调函数并不会马上被执行。它会在包含它的函数内的某个特定时间点被“回调”（就像它的名字一样）



#### js 为什么一定需要回调？（选做）

JavaScript是事件驱动的语言。这意味着，JavaScript不会因为要等待一个响应而停止当前运行，而是在监听其他事件时继续执行。

同时回调能确保一段代码执行完毕之后按照要求再执行另一段代码。



#### 文字描述 ajax todo 中显示所有 todo 的流程。

浏览器访问/ajax/todo/index
即RouteAjaxTodo路由中添加/ajax/todo/index的路由:
map.put("/ajax/todo/index",RouteAjaxTodo::indexView);

-服务器返回HTML
HTML的body路径指向
Stringbody=Utils.html("ajax_todo_index.html");

-浏览器根据HTML执行JS
ajax_todo_index.html页面中执行两条主函数中的函数
bindEvents()
loadTodos();
其中
bindEvents()绑定添加按钮,监听添加事件,暂时不用;

-浏览器发ajax请求到/ajax/todo/all
其中loadTodos()函数中执行ajax(method,path,data,function(response)函数
使用post方法,向/ajax/todo/all路径请求全部数据,获取response;



-服务器按json格式返回所有todo
返回来的response在loadTodos()函数中转成json格式:
vartodoList=JSON.parse(response)

-浏览器调用回调代码显示所有todo
在loadTodos()函数中,varhtml=todoTempalte(todo)将todo按照
<span>${todo.id}:${todo.content}</span>分行显示在todoHtml中
最后insertTodo(html)函数向
<divid="id-div-todoList"></div>中展示全部todolist结果



#### 文字描述 ajax todo 中添加新 todo 的流程。    

-浏览器访问/ajax/todo/index
即RouteAjaxTodo路由中添加/ajax/todo/index的路由:
map.put("/ajax/todo/index",RouteAjaxTodo::indexView);

-服务器返回HTML
HTML的body路径指向
Stringbody=Utils.html("ajax_todo_index.html");

-浏览器注册点击事件
ajax_todo_index.html页面中执行两条主函数中的函数
bindEvents()
loadTodos();
其中
bindEvents()绑定添加按钮,监听添加事件

-点击添加
,监听到#id-button-add被点击之后执行
varinput=document.querySelector("#id-input-add")
varcontent=input.value
获取到输入的content值

-浏览器发ajax请求到/ajax/todo/add
bindEvents()函数中赋值
varpath="/ajax/todo/add"
varmethod="POST"
vardata={
content:content}
ajax(method,path,data,function(resopnse)
向/ajax/todo/add路由发送post请求,获取response

路由RouteAjaxTodo中add函数返回增加todo之后的全部json格式数据给bindEvents()函数
Stringbody=JSON.toJSONString(todo);

-服务器返回json格式的message
bindEvents()函数接收到response之后同样用JSON格式解析一下
vartodo=JSON.parse(resopnse)

-浏览器调用回调代码显示message并插入最新todo

varhtml=todoTempalte(todo)将todo按照
<span>${todo.id}:${todo.content}</span>分行显示在todoHtml中
最后insertTodo(html)函数向
<divid="id-div-todoList"></div>中展示全部todolist结果



#### js ajax + css

```js
javascript
----------
- 前端用 ajax 发送 HTTP 请求到后端。
- 后端 API。
- 回调。
- ajax todo 列表页
    - 浏览器访问 /ajax/todo/index
    - 服务器返回 HTML
    - 浏览器根据 HTML 执行 JS
    - 浏览器发 ajax 请求到 /ajax/todo/all
    - 服务器按 json 格式返回所有 todo
    - 浏览器调用回调代码显示所有 todo 
- ajax todo 添加
    - 浏览器访问 /ajax/todo/index
    - 浏览器注册点击事件
    - 点击添加
    - 浏览器发 ajax 请求到 /ajax/todo/add
    - 服务器返回 json 格式的 message
    - 浏览器调用回调代码显示 message 并插入最新 todo


最小化可行产品 MVP Minimum Viable Product
-----------------------------------------

1. 准备 model
    - Todo model， todo
        - id
        - title
        - user_id
2. 写出操作场景的文档 （你要对这些数据做什么操作，这是最重要的一步）
    - 有一个主页，可看到所有todo
    - 主页有一个表单可以添加todo
3. 根据文档，写好 CRUD 操作和其他操作
4. 写路由函数
5. 画 HTML 页面
6. 用 JS 实现相关页面的逻辑
7. 美化页面


CSS 小全
--------
掌握本课可对付几乎所有页面布局需求
如果实在想看, 可以看看 《Head First HTML and CSS》 里面关于 css 的部分

HTML CSS JavaScript 解耦

CSS 基础
--------

- CSS 的使用
    - 内联 (inline style attribute)  完全不应该这样做
    - `<head>` 标签内的 `<style>` 标签   偶尔可以用
    - `<link>` 标签中的外联            推荐的方式



- 三种主要的选择器
    - 元素选择器
    - class 选择器
    - id 选择器



- 样式优先级(从高到低)
    1. !important
    2. 内联样式
    3. 按顺序执行


- 选择器优先级(从高到低)
    1. !important
    2. 内联样式
    3. id 选择器
    4. class 选择器
    5. 元素选择器


- display 属性
    - none
    - block
    - inline
    - inline-block

- none 不显示

- block 占一行
    - 默认 block 的标签有
    - div p ul ol li h1 h2 h3 h4 h5 h6


- inline 只占 content 的尺寸
    - 默认的标签有 button input span


- inline-block
    - inline-block 是 inline 布局 block 模式
    - inline-block 对外表现为 inline，所以可以和别的 inline 放在一行
    - 对内表现为 block，所以可以设置自身的宽高

- position 属性用于元素定位
    - static      默认定位
    - relative    相对定位, 相对于自己本来应该在的位置
    - absolute    绝对定位, 行为有点奇怪
    - fixed       固定定位, 相对位置是整个窗口

- 非 static 元素可以用 top left bottom right 属性来设置坐标
- 非 static 元素可以用 z-index 属性来设置显示层次
- relative 是相对定位
- absolute 完全绝对定位, 忽略其他所有东西
    - 往上浮动到 非 static 的元素
- fixed 基于 window 的绝对定位
    - 不随页面滚动改变

- overflow 属性
    - visible 默认
    - auto    需要的时候加滚动条
    - hidden  隐藏多余元素
    - scroll  就算用不着也会强制加滚动条

- 盒模型
    - 内容
    - padding
    - border
    - margin



盒模型相关的 CSS
----------------


- border
    - border-width
    - border-style    默认是 none, 表示不显示 border
    - border-color
    - 简写如下, 顺序不要紧：
    - `border: 10px blue solid;`


- border-top
    - border-top-width
    - border-top-style
    - border-top-color

- border-right
    - border-right-width
    - border-right-style
    - border-right-color

- border-bottom
    - border-bottom-width
    - border-bottom-style
    - border-bottom-color

- border-left
    - border-left-width
    - border-left-style
    - border-left-color



- margin
    - margin-top
    - margin-right
    - margin-bottom
    - margin-left

- padding
    - padding-top
    - padding-right
    - padding-bottom
    - padding-left

- 四种写法, 分别对应有 4 2 3 1 个值的时候的解释
    - `margin: top  right  bottom  left`
    - `margin: (top/bottom)  (right/left)`
    - `margin: top  (right/left)  bottom`
    - `margin: (top/right/bottom/left)`
    - padding 同理

- background 相关属性和缩写
    - `background-color: #233;`
    - `background-image: url(bg.png);`
    - `background-repeat: no-repeat;`
    - `background-attachment: fixed; /* 背景图片随滚动轴的移动方式 */`
- 缩写如下
    - `background: #233 url(bg.png) no-repeat;`



- float 属性(最初用于排版)
  - left
  - right
  - float 那一行相当于没有 后面的block元素会接上去

- 水平居中写法
    - block 元素居中, 两步走
        1. 设置自己的宽度
        2. 设置自己的 `margin: 0 auto`;

    - inline inline-block 元素居中
        1. 设置父元素的 text-align 属性
        2. text-align: center;

- 垂直居中
  - 记一下当套路 不需要理解机制
  - 需要父节点是 relative
```







