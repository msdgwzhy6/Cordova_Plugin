###做题App的埋点参数传递
----
+ eventTime:事件发生的时间[date]
+ category:事件分类[string]
+ eventKey:事件名称[string]
+ eventValue:事件的值[object]
 
做题App只能传递以上参数，额外参数应放入`eventValue`中。



###一. YCProblemSetPlugin
-----
+ parameter:请求参数  
+ return:默认都会返回true||false，需要有回调数据时会返回回调的数据

####1. loadProblemContext (加载做题需要的全局数据)  
parameter:
  (无)  
return:  jsonObjct
 ````
type :String ("E"阶段测验  "P" 练习题）
topicType: String,（[enum] 'A', 'B', 'C', 'D', 'E', 'I', 'S'（知识点的类型：A类和非A类））即服务器返回数据中知识点结构type
topicId: String（知识点的id）
topicState:Object (知识点进度，结构遵循后台，不存在是传递Null，非｛｝); 
showImgSymbol: Boolean（是否将大于等于号或小于等于号等特殊符号，使用图片显示）
              （仅Android App传这个参数，iOS App无需传递这个字段）
rewardRules: Object (/reward-rules 服务端返回结构中的 points对象，不存在则传递Null，非{});
 ````

####2. closeProblemSet (退出做题)
parameter: 无  
return: 无 

####3. loadProblemSet (加载专辑所有题目)
parameter:无  
return: Object (对于客户端来说对应的数据直接是topic－>module->practice object，不做过滤)

####4. recordTrackInfo（记录做题埋点）
parameter: Array（埋点数组）  
return: 无 

####5. recordWrongProblems(记录做题错误)
parameter: Array（埋点数组）  
return: 无 

####6. levelFail（某一层题答题失败）
parameter: String（Level ID）  
return: 无 

####7. levelSuccess（某一层答题成功)
parameter: args[]
````
args[0]:String（Level ID）
args[1]: boolean (是否是最后一层)
args[2]:Object (tryTimes & levels,不关心里面的具体是什么，直接传递给服务端)
               int  (tryTimes: [enum] 1/2 （第几次做对,用于积分计算）)
               仅最后一层请求时会发另一个字段——
               levels: {（在习题端纪录的层纪录，一般情况下后端不会用这个数据，在数据丢失无法计算完成状态时，会以前端的层纪录为计算依据）
                         levelId: 0/1/2, // 0- failed; 1- 一次性做对;2- 第二次做对
                       }

 ````               
return: 无 
