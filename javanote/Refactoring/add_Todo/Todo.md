
# Todo.md

## 详细描述增加 todo 的完整流程。

1.增加主页路由,privatestaticbyte[]responseForPath(Requestrequest)中增加
if(path.equals("/todo")){
response=RouteTodo.index(request);index路由浏览器请求时跳转todo页面;

2.增加html页面todo_index.html,保证todo页面正常展示http://localhost:9000/todo

3.todo index页面中输入todo项,点击[添加]项,发送post请求
请求:
POST /todo/add

4.服务器根据路径选择路由
if(path.equals("/todo/add")){
response=RouteTodo.add(request);
此函数调用TodoService.add(form);进入TodoService调用add函数,把数据加入txt文件中(其中调用load/save函数)

5.加完数据后调用redirect("/todo");函数跳转回来todeindex(302)页面,同时刷新页面todos结果为todoListShowString

6.最后通过responseWithHeader函数把返回返回码headers和body拼接,返回页面给浏览器

## 详细描述删除 todo 的完整流程。

1.增加主页路由,privatestaticbyte[]responseForPath(Requestrequest)中增加
if(path.equals("/todo/delete")){
response=RouteTodo.delete(request);

2.todoListHtml的html页面中添加<ahref=\"/todo/delete?id=%s\">删除</a>\n"+删除按钮;

3.先进入todo主页http://localhost:9000/todo,然后点击删除按钮,html发送GET的http请求,GET /todo/delete?id=8 HTTP/1.1

4.在Routetodo中进入/todo/delete路由,执行TodoService.delete(id);主要执行下述3个动作:
ArrayList<Todo>todos=load();
todos.remove(e);
save(todos);

5.删除完数据后调用redirect("/todo");函数跳转回来todeindex(302)页面,同时刷新页面todos结果为todoListShowString

6.最后通过responseWithHeader函数把返回返回码headers和body拼接,返回页面给浏览器

## 详细描述修改 todo 的完整流程。

1.增加主页路由,privatestaticbyte[]responseForPath(Requestrequest)中增加
if(path.equals("/todo/edit")){
response=RouteTodo.editView(request);

2.新增tode_edit.xml文件

3.todoListHtml的html页面中添加"<ahref=\"/todo/edit?id=%s\">编辑</a>\n"+编辑按钮;


4.先进入todo主页http://localhost:9000/todo,然后点击编辑按钮,html发送GET的http请求,GET /todo/edit?id=7 HTTP/1.1

5.在Routetodo中进入/todo/edit路由,
editView(Requestrequest)函数展示edit字段内容
编写内容后提交,html发送POST的html请求POST /todo/update HTTP/1.1

6.增加主页路由,privatestaticbyte[]responseForPath(Requestrequest)中增加
if(path.equals("/todo/update")){
response=RouteTodo.update(request);

7.进入RouteTodo.update(request);函数中
TodoService.updateContent(id,content);函数分别获取表单form提交的id和content然后更新;
其中updateContent(id,content)分别经过ArrayList<Todo>todos=load();e.content=content;save(todos);几个函数完成内容更新


8.更新完数据后调用redirect("/todo");函数跳转回来todeindex(302)页面,同时刷新页面todos结果为todoListShowString

9最后通过responseWithHeader函数把返回返回码headers和body拼接,返回页面给浏览器

## 增加一个 complete 路由, 接受发往 /todo/complete 的请求
接受请求, 根据 query 中的参数 id, 找到对应的 Todo, 把 completed 属性改成 true
完成修改后, 重定向到 /todo

```Java
//Complete:

1.Server.java中添加：
	else if(path.equals("/todo/complete")){
		response = RouteTodo.complete(request);
	}else{
		response=Route.route404();
	}

2.RouteTodo.java中添加
static byte[] complete(Requestrequest){
	//Utils.log("complete函数");
	HashMap<String,String>query =r equest.query;
	//Utils.log("query%s",query);
	Integerid = Integer.valueOf(query.get("id"));
	TodoService.complete(id);
	returnre direct("/todo");
}

3.TodoService.java中添加

public static void complete(Integerid){
	ArrayList<Todo>todos=load();
		for(inti=0;i<todos.size();i++){
		Todo e=todos.get(i);
			if(e.id.equals(id)){
			e.completed=true;
			}
		}
	save(todos);
}


4.

public static String todoListHtml () {
        ArrayList<Todo> all = load();
        // usopp: 21111

        StringBuilder sb = new StringBuilder();

        for (Todo m: all) {
            // <br> 在 html 里面代表换行符
            String todoHtml = String.format("<h3>\n" +
                    "    %s. %s \n" +
                    "    <a href=\"/todo/delete?id=%s\">删除</a>\n" +
                    "    <a href=\"/todo/edit?id=%s\">编辑</a>\n" +
                    "    <a href=\"/todo/complete?id=%s\">完成</a>\n" +
                    "</h3>",
                    m.id,
                    m.content,
                    m.id,
                    m.id,
                    m.id);
            if (m.completed.equals(true)){
                todoHtml = String.format("<h3 style='text-decoration:line-through'>\n" +
                                "    %s. %s \n" +
                                "    <a href=\"/todo/delete?id=%s\">删除</a>\n" +
                                "    <a href=\"/todo/edit?id=%s\">编辑</a>\n" +
                                "    <a href=\"/todo/complete?id=%s\">完成</a>\n" +
                                "</h3>",
                        m.id,
                        m.content,
                        m.id,
                        m.id,
                        m.id);
            }
            sb.append(todoHtml);
        }
        return sb.toString();
    }
```