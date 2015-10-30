package com.guanghe.cordova.plugin;

import android.text.TextUtils;
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
//        int x = (int) (Math.random() * 100);
//        if (x % 2 == 1) {
//            type = "p";
//        } else {
//            type = "c";
//        }
    }

    private String type = "p";

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
            if (TextUtils.equals("p", type)) {
                loadPracticeSet(callbackContext);
            } else {
                loadChallengeSet(callbackContext);
            }
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
    private void loadPracticeSet(CallbackContext callbackContext) {
        try {
            problemSetJO = new JSONObject(practiceSetMock);
            callbackContext.success(problemSetJO);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());

        }
    }

    private void loadChallengeSet(CallbackContext callbackContext) {
        try {
            JSONArray array = new JSONArray(challengeSetMock);
            callbackContext.success(array);
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
            jsonObject.put("type", type);
//            jsonObject.put("bloods", cBloods);
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
            JSONObject problemSetJO = new JSONObject(practiceSetMock);
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
        }
    }


    private String practiceSetMock = "{\n" +
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

    private String challengeSetMock = "[{\n" +
            "\t\"problemSet\": \"9d8440cf2f411804\",\n" +
            "\t\"type\": \"single\",\n" +
            "\t\"body\": \"帮小锤在横线上填上正确的数： $x^2-6x+$____$=(x-$____$)^2$\",\n" +
            "\t\"choices\": [{\n" +
            "\t\t\"body\": \"$9$&nbsp;;&nbsp;$3$\",\n" +
            "\t\t\"correct\": true\n" +
            "\t}, {\n" +
            "\t\t\"body\": \"$36$&nbsp;;&nbsp;$6$\",\n" +
            "\t\t\"correct\": false\n" +
            "\t}],\n" +
            "\t\"explain\": \"由$a^2-2ab+b^2=(a-b)^2$ <br />且$6x=2 \\\\\\\\cdot x \\\\\\\\cdot 3$，<br />所以$ x^2-6x+9= (x-3) ^2$。 \",\n" +
            "\t\"id\": \"251717a8a2619840\"\n" +
            "}, {\n" +
            "\t\"problemSet\": \"19b20098859caadb\",\n" +
            "\t\"type\": \"single\",\n" +
            "\t\"choices\": [{\n" +
            "\t\t\"body\": \"$36$&nbsp;;&nbsp;$6$\",\n" +
            "\t\t\"correct\": true\n" +
            "\t}, {\n" +
            "\t\t\"body\": \"$144$&nbsp;;&nbsp;$12$\",\n" +
            "\t\t\"correct\": false\n" +
            "\t}],\n" +
            "\t\"body\": \"帮小锤在横线上填上正确的数： $x^2-12x+$____$= (x-$____$)  ^2$\",\n" +
            "\t\"prompt\": null,\n" +
            "\t\"explain\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$12x=2 \\\\\\\\cdot x \\\\\\\\cdot 6$，<br />所以$ x^2-12x+36= (x-6) ^2$\",\n" +
            "\t\"blank\": null,\n" +
            "\t\"id\": \"cd650a55bb633826\"\n" +
            "}, {\n" +
            "\t\"problemSet\": \"c57f43c6c2a1b844\",\n" +
            "\t\"type\": \"single\",\n" +
            "\t\"choices\": [{\n" +
            "\t\t\"body\": \"$4$&nbsp;;&nbsp;$2$\",\n" +
            "\t\t\"correct\": true\n" +
            "\t}, {\n" +
            "\t\t\"body\": \"$16$&nbsp;;&nbsp;$4$\",\n" +
            "\t\t\"correct\": false\n" +
            "\t}],\n" +
            "\t\"body\": \"帮小锤在横线上填上正确的数： $x^2+4x+$____$= (x+$____$)^2$\",\n" +
            "\t\"prompt\": null,\n" +
            "\t\"explain\": \"由$a^2+2ab+b^2= (a+b) ^2$ <br />且$4x=2 \\\\\\\\cdot x \\\\\\\\cdot 2$，<br />所以$ x^2+4x+4= (x+2) ^2$。\",\n" +
            "\t\"blank\": null,\n" +
            "\t\"id\": \"ee5bb2fc9fff3896\"\n" +
            "}, {\n" +
            "\t\"problemSet\": \"7636d00c9160089a\",\n" +
            "\t\"type\": \"single\",\n" +
            "\t\"choices\": [{\n" +
            "\t\t\"body\": \"$25 \\\\over 4$&nbsp;;&nbsp;$5 \\\\over 2$\",\n" +
            "\t\t\"correct\": true\n" +
            "\t}, {\n" +
            "\t\t\"body\": \"$25$&nbsp;;&nbsp;$5$\",\n" +
            "\t\t\"correct\": false\n" +
            "\t}],\n" +
            "\t\"body\": \"帮小锤在横线上填上正确的数： $x^2-5x+$____$= (x-$____$)^2$\",\n" +
            "\t\"prompt\": null,\n" +
            "\t\"explain\": \"由$a^2-2ab+b^2= (a-b) ^2$ <br />且$5x=2 \\\\\\\\cdot x \\\\\\\\cdot  {5 \\\\\\\\over 2}$，<br />所以$ x^2-5x+{25 \\\\\\\\over 4}= (x-{5 \\\\\\\\over 2}) ^2$\",\n" +
            "\t\"blank\": null,\n" +
            "\t\"id\": \"c6f7c2685bbf1854\"\n" +
            "}]";
}
