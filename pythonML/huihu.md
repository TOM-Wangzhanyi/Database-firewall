如果采用传统的三层架构,SQL注入检测项目的后端可以设计为:
# 三层架构
## Controller层 
* 学生成绩管理Controller
    * 学生信息管理
    * 课程管理
    * 成绩管理
    * 成绩查询
* 规则管理Controller
    * 白名单规则
    * 黑名单规则
    * 模型管理
* 日志与告警Controller
    * SQL日志
    * 检测日志
    * 用户日志
    * 告警信息
    * 告警设置 
* 用户Controller
    * 用户管理
    * 角色管理
    * 权限管理
## Service层
* 学生成绩管理Service 
* 规则管理Service
* 日志与告警Service
* 权限管理Service 
* 用户管理Service
* ......
## DAO层
* 学生信息DAO
* 课程信息DAO
* 成绩信息DAO
* 规则信息DAO
* 日志信息DAO
* 告警信息DAO
* 用户信息DAO
* 角色信息DAO
* 权限信息DAO
* ......
![三层架构](三层架构.png)