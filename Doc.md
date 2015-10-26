
###一. YCProblemSetPlugin
-----
+ parameter:请求参数  
+ return:默认都会返回true||false，需要有回调数据时会返回回调的数据

1. 
**loadProblemContext (加载做题需要的全局数据)**  
parameter:
  (无)  
return:  jsonObjct
 ````
maxLevel: String,
type: String,
bloods: Number,
currentProgress: Number,
totalProgress: Number

 ````

2. 
**closeProblemSet (退出做题)**  
parameter:
  无  
return: 无
