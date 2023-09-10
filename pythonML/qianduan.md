markdown
```mermaid
graph LR  
A[sender1线程/进程]-->|消息队列|B[receiver线程/进程]   
A-->C{输入"exit"?}  
C-->"否"|A  
C-->"是"|D[发送"end1"消息]    
D-->E[等待"over1"应答]  
E-->F[显示应答信息]

G[sender2线程/进程]-->|消息队列|B
G-->H{输入"exit"?}  
H-->"否"|G 
H-->"是"|I[发送"end2"消息]  
I-->J[等待"over2"应答]
J-->K[显示应答信息]   

B-->L{收到"end1"?}
L-->"是"|M[发送"over1"应答]
L-->"否"|N   
N-->O{收到"end2"?}
O-->"是"|P[发送"over2"应答]
O-->"否"|Q[显示接收消息]
Q-->B