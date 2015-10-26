package com.guanghe.cordova.plugin;

import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (c) 2014 Guanghe.tv All right reserved.
 * --------------------<-.->-----------------------
 * Author:      Nono(陈凯)
 * CreateDate:  15/10/20
 * Description: exp...
 * Version:     1.0.0
 */
public class YCProblemSetPlugin extends CordovaPlugin {


    private JSONObject problemSetJO;

    /**
     * 图片mock数据
     */

    /**
     * action name
     */
    private static final String ACTION_LOAD_PROBLEM = "loadProblem";//加载题目
    private static final String ACTION_LOAD_IMAGE = "loadImage";// 加载图片
    private static final String ACTION_LOAD_PROBLEM_CONTEXT = "loadProblemContext";// 加载做题的一些元素数据
    private static final String ACTION_CLOSE_PROBLEM_SET = "closeProblemSet";//退出做题
    private static final String ACTION_FINISH_PROBLEM_SET = "finishProblemSet";//完成专辑测试
    private static final String ACTION_LOAD_PROBLEM_SET = "loadProblemSet";


    /**
     * 维护做题状态
     */
    private int cLevel;//当前做题级别
    private int cNumber;//当前做题序号
    private int cBloods;//当前血量
    private int maxLevel;//总levele


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
//        String arg = cordova.getActivity().getIntent().getStringExtra(ProblemDoingActivity.ARG_PROBLEM_SET);
//        if (arg != null) {
//            try {
//                problemSetJO = new JSONObject(arg);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        /**
         * 初始化做题状态
         */
        cLevel = 0;
        cNumber = 0;
        cBloods = 4;//mock
        maxLevel = 3;//mock
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (ACTION_LOAD_PROBLEM.equals(action)) {
            loadProblem(args, callbackContext);
            return true;
        } else if (ACTION_LOAD_IMAGE.equals(action)) {
            loadImage(args, callbackContext);
            return true;
        } else if (ACTION_LOAD_PROBLEM_CONTEXT.equals(action)) {
            loadProblemContext(args, callbackContext);
            return true;
        } else if (ACTION_CLOSE_PROBLEM_SET.equals(action)) {
            closeProblemSet();
            return true;
        } else if (ACTION_FINISH_PROBLEM_SET.equals(action)) {
            finishProblemSet(args, callbackContext);
            return true;
        } else if (ACTION_LOAD_PROBLEM_SET.equals(action)) {
            loadProblemSet(callbackContext);
            return true;
        } else {
            callbackContext.error(action + ":action not define!");
            return false;
        }
    }

    /**
     * 退出做题
     */
    private void closeProblemSet() {
        cordova.getActivity().finish();
    }

    /**
     * @param args            arg
     * @param callbackContext 回调
     */
    private void finishProblemSet(JSONArray args, CallbackContext callbackContext) {
        if (args == null) {//参数为null 返回
            callbackContext.error(" args is null ");
        } else {
            try {
                boolean isPass = args.getBoolean(0);
                if (isPass) {
                    Toast.makeText(cordova.getActivity().getBaseContext(), "专辑测试通过", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//                    cordova.getActivity().startActivity(intent);
//                    cordova.getActivity().finish();
                } else {
                    Toast.makeText(cordova.getActivity().getBaseContext(), "专辑测试失败", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//                    cordova.getActivity().startActivity(intent);
//                    cordova.getActivity().finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param callbackContext
     */
    private void loadProblemSet(CallbackContext callbackContext) {
        try {
            problemSetJO = new JSONObject(setMock);
            callbackContext.success(problemSetJO);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());

        }
    }

    /**
     * 初始化做题全局数据
     *
     * @param args
     * @param callbackContext
     */
    private void loadProblemContext(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("maxLevel", maxLevel);
            jsonObject.put("type", "p");
            jsonObject.put("bloods", cBloods);
            jsonObject.put("currentProgress", 3);
            jsonObject.put("totalProgress", 5);
            callbackContext.success(jsonObject);
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }
    }

    /**
     * 加载题目
     *
     * @param args
     * @param callbackContext
     */
    private void loadProblem(JSONArray args, CallbackContext callbackContext) {
        try {

            if (args == null) {//参数为null 返回
                callbackContext.error(" args is null ");
            } else {
                JSONObject resultJO = null;
                int preLevel = cLevel;
                int preNumber = cNumber;

                if (cLevel == 0 && cNumber == 0) {//判断下是不是第一题
                    preLevel++;
                    preNumber++;
                    resultJO = pickProblem(preLevel, preNumber);

                } else {
                    String arg = args.getString(0);//获取请求参数
                    if ("succeed".equals(arg)) {
                        //答对，level++，number重置为1.
                        preLevel++;
                        preNumber = 1;
                        if (preLevel > maxLevel) {//判断是有已经做完所有level
                            Toast.makeText(cordova.getActivity().getBaseContext(), "专辑完成", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//                            cordova.getActivity().startActivity(intent);
//                            cordova.getActivity().finish();
                            return;
                        } else {//否则继续给题目
                            resultJO = pickProblem(preLevel, preNumber);
                        }
                    } else if ("failed".equals(arg)) {
                        preNumber++;//题号++
                        cBloods--;// 血--；
                        if (cBloods < 1) {//血掉完，退出做题
                            Toast.makeText(cordova.getActivity().getBaseContext(), "做题失败，下次再来", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//                            cordova.getActivity().startActivity(intent);
//                            cordova.getActivity().finish();
                            return;
                        } else {
                            resultJO = pickProblem(preLevel, preNumber);
                        }
                    } else {//默认其他参数,返回
                        callbackContext.error("不是加载第一题 ,参数非法");
                    }
                }

                if (resultJO != null) {
                    callbackContext.success(resultJO);
                    cLevel = preLevel;
                    cNumber = preNumber;
                } else {
                    callbackContext.error("load error");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }

    }

    /**
     * @param level
     * @param number
     * @return
     */
    private JSONObject pickProblem(int level, int number) {
        if (level > maxLevel)
            return null;
        try {
            JSONObject problemSetJO = new JSONObject(setMock);
            JSONArray problemSet = problemSetJO.getJSONArray("problems");
            int count = problemSet.length();
            int tmpNumber = 0;
            for (int i = 0; i < count; i++) {
                JSONObject jo = problemSet.getJSONObject(i);
                if (jo != null) {
                    int lv = jo.getInt("level");
                    if (level == lv) {
                        tmpNumber++;
                        if (number == tmpNumber) {
                            return jo;
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    /**
     * 加载图片
     *
     * @param args
     * @param callbackContext
     */
    private void loadImage(JSONArray args, final CallbackContext callbackContext) {


        if (args == null || args.length() < 1) {
            callbackContext.error(ACTION_LOAD_IMAGE + " args is null");
        } else {
//            String s= args.getString(0);
            callbackContext.success("");

//            try {
//                JSONObject jo = args.getJSONObject(1);
//                String url = jo.getString("url");
//                VolleyManager.getInstance(YCMathApplication.getInstance().getApplicationContext()).getImageLoader().get(url, new ImageLoader.ImageListener() {
//                    @Override
//                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                        Bitmap bitmap = imageContainer.getBitmap();
//                        if (bitmap != null) {
//                            String result = CommonUtils.bitmapToBase64(bitmap);
//                            if (result != null){
//                                callbackContext.success(result);
//                            }else {
//                                callbackContext.error("bitmap2base64 failed");
//                            }
//                        }else {
//                            callbackContext.error("bitmap is null ");
//
//                        }
//                    }
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        callbackContext.error(volleyError.getMessage());
//
//                    }
//                });
//            } catch (JSONException e) {
//                callbackContext.error(e.getMessage());
//            }

        }
    }

    private String problemMockData = "[{\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 0,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他记不清了，怎样才是正确的移项呢？<hr /> <img src='http://7sbko6.com2.z0.glb.qiniucdn.com/QD-LX-AAS-J1.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x^2+11=-12x$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2+12x=-11$ \",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"将常数项移到方程右边后变号，得$x^2+12x=-11$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 1,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他记不清了，怎样才是正确的移项呢？<hr /> <img src='/images/demo112.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ x^2+15=-8x$\",\n" +
            "        \"isRight\": \" false \"\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2+8x=-15$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"将常数项移到方程右边后变号，得$x^2+8x=-15$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 2,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他记不清了，怎样才是正确的移项呢？<hr /> <img src='/images/demo113.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ x^2+24=10x$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2-10x=-24$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"将常数项移到方程右边后变号，得$x^2-10x=-24$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 3,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他忘了，接下来该怎么写才对呢？<hr /> <img src='/images/demo121.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x^2+12x+6^2=-11+6^2 $\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2+12x+12^2=-11+12^2 $\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2=(a+b)^2$ <br />且$12x=2 \\\\cdot x \\\\cdot 6$，<br /> 所以方程$x^2+12x=-11$两边同加上$6^2$得$x^2+12x+6^2=-11+6^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 4,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他忘了，接下来该怎么写才对呢？<hr /> <img src='/images/demo122.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x^2+8x+4^2=-15+4^2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2+8x+8^2=-15+8^2$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2=(a+b)^2$ <br />且$8x=2 \\\\cdot x \\\\cdot 4$，<br /> 所以方程$x^2+8x=-15$两边同加上$4^2$得$x^2+8x+4^2=-15+4^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 5,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，有些步骤他忘了，接下来该怎么写才对呢？<hr /> <img src='/images/demo123.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ x^2-10x+5^2=-24+5^2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$x^2-10x+10^2=-24+10^2$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2=(a-b)^2$ <br />且$10x=2 \\\\cdot x \\\\cdot 5$，<br /> ∴ 方程$x^2-10x=-24$两边同加上$5^2$得$x^2-10x+5^2=-24+5^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 6,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，配方的第二步该怎么写呢? <hr /> <img src='/images/demo131.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$(x+3)^2=25$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$(x+6) ^2=25$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2=(a+b)^2$ <br />得$x^2+12x+6^2=(x+6)^2$ <br />而$-11+6^2=25$ <br /> 所以 应选$(x+6)^2=25$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 7,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，配方的第二步该怎么写呢? <hr /> <img src='/images/demo132.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ (x+2)^2=1$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x+4)^2=1$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2=(a+b)^2$ <br />得$x^2+8x+4^2=(x+4)^2$ <br />而$-15+4^2=1$ <br /> 所以 应选$ (x+4)^2=1$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 8,\n" +
            "    \"body\": \"狗蛋今天刚学完用配方法解一元二次方程，配方的第二步该怎么写呢? <hr /> <img src='/images/demo133.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ (x-{5 \\\\over 2})^2=1$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x-5)^2=1$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2=(a+b)^2$ <br />得$x^2-10x+5^2=(x-5)^2$ <br />而$-24+5^2=1$ <br />所以应选$ (x-5)^2=1$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 9,\n" +
            "    \"body\": \"狗蛋终于要把这道题做完了，来帮狗蛋选出正确的答案吧！<hr /> <img src='/images/demo141.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x_1=11$，$x_2=1$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$x_1=-11$，$x_2=-1$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$x+6= \\\\pm 5$得$x= \\\\pm 5-6$ <br />所以$x_1=-11$，$x_2=-1$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 10,\n" +
            "    \"body\": \"狗蛋终于要把这道题做完了，来帮狗蛋选出正确的答案吧！<hr /> <img src='/images/demo142.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x_1=3$，$x_2=5$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ x_1=-3$，$x_2=-5$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$x+4= \\\\pm 1$得$x= \\\\pm 1-4$ <br />所以$x_1=-3，x_2=-5$\"\n" +
            "}, {\n" +
            "    \"topicId\": 0,\n" +
            "    \"id\": 11,\n" +
            "    \"body\": \"狗蛋终于要把这道题做完了，来帮狗蛋选出正确的答案吧！<hr /> <img src='/images/demo143.png'>\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$x_1=-4$，$x_2=-6$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ x_1=4$，$x_2=6$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$x-5= \\\\pm 1$得$x= \\\\pm 1+5$ <br />所以$x_1=4$，$x_2=6$\"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 12,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-6x+$____$=(x-$____$)^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$9$&nbsp;;&nbsp;$3$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$36$&nbsp;;&nbsp;$6$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2=(a-b)^2$ <br />且$6x=2 \\\\cdot x \\\\cdot 3$，<br />所以$ x^2-6x+9= (x-3) ^2$。 \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 13,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-12x+$____$= (x-$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$36$&nbsp;;&nbsp;$6$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$144$&nbsp;;&nbsp;$12$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$12x=2 \\\\cdot x \\\\cdot 6$，<br />所以$ x^2-12x+36= (x-6) ^2$ \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 14,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2+4x+$____$= (x+$____$)^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$4$&nbsp;;&nbsp;$2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$16$&nbsp;;&nbsp;$4$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2= (a+b) ^2$ <br />且$4x=2 \\\\cdot x \\\\cdot 2$，<br />所以$ x^2+4x+4= (x+2) ^2$。 \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 15,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-5x+$____$= (x-$____$)^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$25 \\\\over 4$&nbsp;;&nbsp;$5 \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$25$&nbsp;;&nbsp;$5$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$5x=2 \\\\cdot x \\\\cdot  {5 \\\\over 2}$，<br />所以$ x^2-5x+{25 \\\\over 4}= (x-{5 \\\\over 2}) ^2$ \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 16,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-7x+$____$= (x-$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$49 \\\\over 4$&nbsp;;&nbsp;$7 \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$49$&nbsp;;&nbsp;$7$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$7x=2 \\\\cdot x \\\\cdot  {7 \\\\over 2}$，<br />所以$ x^2-7x+{49 \\\\over 4}= (x-{7 \\\\over 2})^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 17,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2+3x+$____$= (x+$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$9 \\\\over 4$&nbsp;;&nbsp;$3 \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$9$&nbsp;;&nbsp;$3$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2= (a+b) ^2$ <br />且$3x=2 \\\\cdot x \\\\cdot  {3 \\\\over 2}$，<br />所以$ x^2+3x+{9 \\\\over 4}= (x+{3 \\\\over 2})^2$ \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 18,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-{3 \\\\over 2} x+$____$= (x-$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$9 \\\\over 4$&nbsp;;&nbsp;$3 \\\\over 2$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$9 \\\\over 16$&nbsp;;&nbsp;$3 \\\\over 4$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且 ${3 \\\\over 2 }x=2 \\\\cdot x \\\\cdot  {3 \\\\over 4}$，<br /> 所以$x^2-{3 \\\\over 2 }x+{9 \\\\over 16}= (x-{3 \\\\over 4}) ^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 19,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-{5 \\\\over 4} x+$____$= (x-$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$25 \\\\over 64$&nbsp;;&nbsp;$5 \\\\over 8$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$25 \\\\over 16$&nbsp;;&nbsp;$5 \\\\over 4$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且 ${5 \\\\over 4 }x=2 \\\\cdot x \\\\cdot  {5 \\\\over 8}$，<br />所以$ x^2-{5 \\\\over 4 }x+{25 \\\\over 64}= (x-{5 \\\\over 8})^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 20,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-{1 \\\\over 5 }x+$____$= (x-$____$)^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$1 \\\\over 100$&nbsp;;&nbsp;$1 \\\\over 10$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$1 \\\\over 25$&nbsp;;&nbsp;$1 \\\\over 5$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且 ${1 \\\\over 5} x=2 \\\\cdot x \\\\cdot  {1 \\\\over 10}$，<br />所以$ x^2-{1 \\\\over 5 }x+{1 \\\\over 100}= (x-{1 \\\\over 10}) ^2$ \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 21,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-px+$____$= (x-$____$)^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$p^2 \\\\over 4$&nbsp;;&nbsp;$p \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$p^2 \\\\over 2$&nbsp;;&nbsp;$p \\\\over 2$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$px=2 \\\\cdot x \\\\cdot  {p \\\\over 2}$，<br />所以$ x^2-px+{p^2 \\\\over 4}= (x-{p \\\\over 2}) ^2$ \"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 22,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2-ax+$____$= (x-$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$a^2 \\\\over 4$&nbsp;;&nbsp;$a \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$a^2 \\\\over 2$&nbsp;;&nbsp;$a \\\\over 2$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$ax=2 \\\\cdot x \\\\cdot  {a \\\\over 2}$，<br /> 所以$ x^2-ax+{a^2 \\\\over 4}= (x-{a \\\\over 2}) ^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 1,\n" +
            "    \"id\": 23,\n" +
            "    \"body\": \"帮小锤在横线上填上正确的数： $x^2+mx+$____$= (x+$____$)  ^2$\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$m^2 \\\\over 4$&nbsp;;&nbsp;$m \\\\over 2$\",\n" +
            "        \"isRight\": true\n" +
            "    }, {\n" +
            "        \"body\": \"$m^2 \\\\over 2$&nbsp;;&nbsp;$m \\\\over 2$\",\n" +
            "        \"isRight\": false\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 4,\n" +
            "    \"expl\": \"由$a^2+2ab+b^2= (a+b) ^2$ <br />且$mx=2 \\\\cdot x \\\\cdot  {m \\\\over 2}$，<br />所以$ x^2+mx+{m^2 \\\\over 4}= (x+{m \\\\over 2}) ^2$\"\n" +
            "}, {\n" +
            "    \"topicId\": 2,\n" +
            "    \"id\": 24,\n" +
            "    \"body\": \"对方程$x^2+10x+16=0$配方结果正确的是（  ）\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ (x+5) ^2=11$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x+10) ^2=84$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x+5) ^2=9$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 1,\n" +
            "    \"expl\": \"\"\n" +
            "}, {\n" +
            "    \"topicId\": 2,\n" +
            "    \"id\": 25,\n" +
            "    \"body\": \"对方程$x^2-x-{3 \\\\over 4}=0$配方结果正确的是（ ）\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ (x-{1 \\\\over 2}) ^2={1 \\\\over 2}$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x-1) ^2={1 \\\\over 4}$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x-{1 \\\\over 2}) ^2=1$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 2,\n" +
            "    \"expl\": \"\"\n" +
            "}, {\n" +
            "    \"topicId\": 2,\n" +
            "    \"id\": 26,\n" +
            "    \"body\": \"对方程 $4x^2-x-9=0$配方结果正确的是（  ）\",\n" +
            "    \"type\": \"single\",\n" +
            "    \"choices\": [{\n" +
            "        \"body\": \"$ (x-{1 \\\\over 4}) ^2={37 \\\\over 16}$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x-{1 \\\\over 8}) ^2={143 \\\\over 64}$\",\n" +
            "        \"isRight\": false\n" +
            "    }, {\n" +
            "        \"body\": \"$ (x-{1 \\\\over 8}) ^2={145 \\\\over 64}$\",\n" +
            "        \"isRight\": true\n" +
            "    }],\n" +
            "    \"answer\": \"\",\n" +
            "    \"level\": 3,\n" +
            "    \"expl\": \"\"\n" +
            "}]";


    private String setMock = "{\n" +
            "  \"_id\": \"5627355e14db95ec4d49cf76\",\n" +
            "  \"deleted\": false,\n" +
            "  \"createdAt\": \"2015-10-21T06:49:02.275Z\",\n" +
            "  \"updatedAt\": \"2015-10-21T06:49:02.275Z\",\n" +
            "  \"bloods\": 3,\n" +
            "  \"name\": \"myBaby123\",\n" +
            "  \"desc\": \"first create\",\n" +
            "  \"challengeAmount\": 5,\n" +
            "  \"__v\": 0,\n" +
            "  \"problems\": [\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 1,\n" +
            "      \"body\": \"已知：如图所示，$D$是$AC$上一点，$BC=AE$，$DE//AB$，$ angle B= angle DAE$，则$ \\triangle ABC stackrel{\\backsim}{=}  \\triangle DAE$的判定依据是（   ）<div><probimg src='http://7sbko6.com2.z0.glb.qiniucdn.com/QD-LX-AAS-J1.png'></probimg></div>\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627357e14db95ec4d49cf78\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"3\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"6\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 1,\n" +
            "      \"body\": \"单项式−a2b3c的系数和次数的和是（ ）\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627359414db95ec4d49cf79\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"3\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"6\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 2,\n" +
            "      \"body\": \"已知：如图所示，$E$、$B$、$F$、$C$在同一条直线上，若$ angle D= angle A=90^circ$，$EB=FC$，$AB=DF$，则$ \\triangle ABC stackrel{\\backsim}{=}  \\triangle DFE$，判定全等的根据是（   ）<div><probimg src='http://7sbko6.com2.z0.glb.qiniucdn.com/QD-HL-J3.png'></probimg></div>\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627357e14db95ec4d49cf78\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"13\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"61\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 2,\n" +
            "      \"body\": \"已知：a=2，b=3，则(−2ab+3a)−2(2a−b)+2ab的值为（ ）\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627357e14db95ec4d49cf78\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"13\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"61\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 3,\n" +
            "      \"body\": \"下面是根据规律排列的一列数:3、5、7、9⋯⋯，那么第n个数是____（ ）\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627357e14db95ec4d49cf78\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"13\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"61\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"explain\": \"−a2b3c的系数是−1，次数是2+3+1=6−1+6=5\",\n" +
            "      \"level\": 3,\n" +
            "      \"body\": \"下面是根据规律排列的一列数:3、5、7、9⋯⋯，那么第n个数是____（ ）\",\n" +
            "      \"type\": \"single\",\n" +
            "      \"flag\": \"practice\",\n" +
            "      \"_id\": \"5627357e14db95ec4d49cf78\",\n" +
            "      \"choices\": [\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"13\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": false,\n" +
            "          \"body\": \"5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"correct\": true,\n" +
            "          \"body\": \"61\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
