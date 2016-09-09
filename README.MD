##Ocean
> 基于mqtt协议，netty通讯组件实现的IM

###ocean-chat
>基于netty&mqtt的实现，维护管理connection，将业务逻辑通过motan rpc丢到ocean-logic处理

###ocean-logic
>处理IM具体业务逻辑，db暂用mongo

###ocean-model
>基础的model

###ocean-sdk
>java sdk demo版

###TODO list
1.connector抽象出来，仅仅维护connection，转发消息，保持轻薄，
2.重构ocean-logic,分离具体的实现，提供可插播的机制，增强扩展性
2.mqtt实现im还有若干问题

