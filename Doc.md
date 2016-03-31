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
topicType: String,（[enum] 'A', 'B', 'C', 'D', 'E', 'I', 'S'（知识点的类型：A类和非A类））即服务器返回数据中知识点结构type
topicId: String,（知识点的id）
topicState:*JsonObj（知识点状态)（默认值：空对象）
          {
          *finishState: [enum] 'unfinished' / 'perfect' /  'imperfect'（未完成／完美完成／不完美完成）     （默认值：unfinished）
          *levels: {  （已有的层状态）  （默认值：空对象）
                  （在完全完成一轮测试题后，levels字段会被清空，再次进来时通过finishState字段来确定进入的层数和加不加分）
                      levelId: 0/1/2, // 0- failed; 1- 一次性做对;2- 第二次做对
                  }
          }
showImgSymbol: Boolean（是否将大于等于号或小于等于号等特殊符号，使用图片显示）
              （仅Android App传这个参数，iOS App无需传递这个字段）

 ````

####2. closeProblemSet (退出做题)
parameter:
  无  
return: 无 

####3. loadProblemSet (加载专辑所有题目)
parameter:无  
return: 
练习题:problemSet [problemSet scheme](https://github.com/guanghetv/onions/blob/master/src/models/problemSet.js)  
挑战题:  [{
        problemSet: id,
        type: 'single/multi/blank',
        choices: [{body: String, correct: Boolean}],
        body: String,
        prompt: String,
        explain: String,
        blank: String,
    }]

####4. finishProblemSet (完成专辑测试)
parameter: 
boolean（测试通过 or 测试失败 )  
String (挑战失败时，失败的setId,没有时传空字符"")  
return: 无  

####5. loadProblem (加载题目)
parameter: String（"succeed" or "failed" or "" )  
return: problemObj  

####6. loadImage (加载图片)
parameter: String（图片的url)  
return: String (图片的base64编码)  

####7. recordTrackInfo（记录做题埋点）
parameter: Array（埋点数组）  
return: 无 

####8. recordWrongProblems(记录做题错误)
parameter: Array（埋点数组）  
return: 无 

####9. evelFail（某一层题答题失败）
parameter: String（Level ID）  
return: 无 

####10. evelSuccess（某一层答题成功)
parameter: 
````
String（Level ID）
int  (tryTimes: [enum] 1/2 （第几次做对,用于积分计算）)
仅最后一层请求时会发另一个字段——
levels: {（在习题端纪录的层纪录，一般情况下后端不会用这个数据，在数据丢失无法计算完成状态时，会以前端的层纪录为计算依据）
          levelId: 0/1/2, // 0- failed; 1- 一次性做对;2- 第二次做对
        }
 ````               
return: 无 
