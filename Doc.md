
###一. YCProblemSetPlugin
-----
+ parameter:请求参数  
+ return:默认都会返回true||false，需要有回调数据时会返回回调的数据

####1. loadProblemContext (加载做题需要的全局数据)  
parameter:
  (无)  
return:  jsonObjct
 ````
type: String,（"c"挑战题 "p"专辑题）
topicId: String,（知识点的id）

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
