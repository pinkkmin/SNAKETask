~~测试~~

~~修改密码----authUser 不加 getdb~~

~~用户信息----authUser 不加 getdb~~

~~修复---图形叠层问题~~

---------------------------------------

~~修改数据库表 级联删除~~

生成用户信息

生成任务信息

生成标签信息

修改DAO层接口

主要集中在任务

/////////////////////////////////////////////////////////////////////////////////////

完成以上，对时间线设置任务和图标显示

更新用户名(退出一个InfoPage时更新)

添加修改功能

添加删除功能

添加状态修改功能

数据更新问题

进行中--->则完成  完成--->则进行中  结束--->进行中

判断结束时间与现在时间

/////////////////////////////////////////////////////////////////////////////////////

对表格进行处理

~~设置数据~~

~~表格类型~~

~~用户表 注销功能~~

~~用户表修改权限功能~~

任务表的编辑问题

添加分更新页功能

删除和修改

~~选择等问题~~

任务表

1.生成数据

为伪造数据写一个DAO：create task

faker--->生成date-----格式化-----task.create

管理员：

用户ID 用户名 标签 内容 状态 起始 结束

query：userName titleName titleType  content status  time



普通用户：标签 + 内容 + 状态 + 起始 + 结束

query:  task+title

select  title.titleName task.content task.status task.start task.end

from task,title

where task.tilteID = titlle.ID AND userID = ' ‘;

全部/年/月/周：只显示日期

今日:显示时分秒

update：titleName start end status 

titleName + userID --->确定--->titleID



tilte表

|-----------------------------------------------------

|titleID |userID| titleName| titleType |

| ------------------------------------------------------

titleID-->userID,titleID--->titleName, titleID--->titleType

query：titleName titleType  content status  time



//////////////////////////////////////////////////////////////////////////////////////

登陆和注册合并到一起

java Faker

http://dius.github.io/java-faker/apidocs/index.html

https://mvnrepository.com/artifact/com.github.javafaker/javafaker/1.0.2









用户表

<u>用户ID</u>  密码 昵称  邮箱  权限

标签表

<u>标签ID</u>     <u>用户ID</u>    标签

0~1000    default   default

任务记录表

<u>用户ID</u> <u>标签ID</u>  <u>开始时间</u> 结束时间 状态 内容





























