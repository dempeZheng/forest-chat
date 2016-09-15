##Ocean
> 基于mqtt协议，netty通讯组件实现的IM

###ocean-chat
>基于netty&mqtt的实现，维护管理connection，将业务逻辑通过motan rpc丢到ocean-logic处理

###Frame
![](/chat.png)

###ocean-logic
>处理IM具体业务逻辑，db暂用mongo

###ocean-model
>基础的model

###ocean-sdk
>java sdk demo版



###打开方式
    1.启动com.dempe.logic.server.LogicServer(依赖mongodb，注意修改application-mongo.xml配置)
    2.启动com.dempe.chat.ChatServer
    3.启动com.dempe.ocean.sdk.example.ChatSdkDemo

看到一下日志，ok了

```
[INFO]2016-09-09 11:50:34,370, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:50:44,182, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:50:54,183, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:51:04,184, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:51:14,184, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:51:24,182, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:51:34,186, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
[INFO]2016-09-09 11:51:44,185, [MessageThread]ChatSdkDemo.java:31, onFriendMsg topci:f|222,payload:[104, 101, 108, 108, 111]
```


###说明
项目还在探索中，逻辑正在实现中，处于不可用阶段。

###TODO list
    1.connector抽象出来，仅仅维护connection，转发消息，保持轻薄，
    2.重构ocean-logic,分离具体的实现，提供可插播的机制，增强扩展性
    2.mqtt实现im还有若干问题

