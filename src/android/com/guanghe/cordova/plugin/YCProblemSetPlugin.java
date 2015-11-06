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


    private JSONObject mPracticeSetJO;
    private JSONArray mChallengeSetJA;

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
     * 题目类型
     */
    private String type = "p";


    /**
     * 维护做题状态
     */

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
//        String argSet = cordova.getActivity().getIntent().getStringExtra(ProblemDoingActivity.ARG_PROBLEM_SET);
//        boolean isChallenge = cordova.getActivity().getIntent().getBooleanExtra(ProblemDoingActivity.ARG_IS_CHALLENGE, false);
//        mProgress = cordova.getActivity().getIntent().getIntExtra(ProblemDoingActivity.ARG_CURRENT_PROGRESS, 0);
//        mTotal = cordova.getActivity().getIntent().getIntExtra(ProblemDoingActivity.ARG_TOTAL_PROGRESS, 0);
//        if (isChallenge) {
//            type = "c";
//        } else {
//            type = "p";
//        }
//
//        if (argSet != null) {
//            try {
//                if (isChallenge) {
//                    mChallengeSetJA = new JSONArray(argSet);
//                } else {
//                    mPracticeSetJO = new JSONObject(argSet);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
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
                    Toast.makeText(cordova.getActivity().getBaseContext(), "测试通过了!!!!", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//                    cordova.getActivity().startActivity(intent);
//                    cordova.getActivity().finish();
                } else {
                    String setId = args.getString(1);
                    if (TextUtils.isEmpty(setId) || TextUtils.equals("null", setId)) {
                        Toast.makeText(cordova.getActivity().getBaseContext(), "专辑失败", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(cordova.getActivity().getBaseContext(), "挑战失败:失败专辑=" + setId, Toast.LENGTH_LONG).show();

                    }
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
            if (mPracticeSetJO == null) {
                mPracticeSetJO = new JSONObject(practiceSetMock);
            }
            callbackContext.success(mPracticeSetJO);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());

        }
    }

    private void loadChallengeSet(CallbackContext callbackContext) {
        try {
            if (mChallengeSetJA == null) {
                mChallengeSetJA = new JSONArray(challengeSetMock);
            }
            callbackContext.success(mChallengeSetJA);
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
            jsonObject.put("type", type);
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
    @Deprecated
    private void loadProblem(JSONArray args, CallbackContext callbackContext) {
        callbackContext.error("Deprecated");
    }


    /**
     * 加载图片
     *
     * @param args
     * @param callbackContext
     */
    @Deprecated
    private void loadImage(JSONArray args, final CallbackContext callbackContext) {

        callbackContext.error(" Deprecated");
    }

    //    "  \"hyperVideo\": {\n" +
//            "    \"_id\": \"hvideo_1\",\n" +
//            "    \"video\": \"videoId213131\",\n" +
//            "    \"titleTime\": \"10000\",\n" +
//            "    \"finishTime\": \"10000\",\n" +
//            "    \"duration\": \"10000\",\n" +
//            "    \"name\": \"测试的讲解视频\",\n" +
//            "    \"url\": {\n" +
//            "      \"mobile\": \"http://119.90.16.196:9999/7xaw4c.com2.z0.glb.qiniucdn.com/%E6%95%B4%E5%BC%8F%E7%9A%84%E4%B9%98%E6%B3%95_1a_%E5%9B%9B%E5%88%99%E8%BF%90%E7%AE%97%E8%A1%A5%E5%AE%8C%E8%AE%A1%E5%88%92.mp4\",\n" +
//            "      \"pc\": \"http://119.90.16.196:9999/7xaw4c.com2.z0.glb.qiniucdn.com/%E6%95%B4%E5%BC%8F%E7%9A%84%E4%B9%98%E6%B3%95_1a_%E5%9B%9B%E5%88%99%E8%BF%90%E7%AE%97%E8%A1%A5%E5%AE%8C%E8%AE%A1%E5%88%92.mp4\"\n" +
//            "    }\n" +
//            "  }\n," +
    private String practiceSetMock = "{\n" +
            "  \"hyperVideo\": {\n" +
            "    \"_id\": \"hvideo_1\",\n" +
            "    \"video\": \"videoId213131\",\n" +
            "    \"titleTime\": \"10000\",\n" +
            "    \"finishTime\": \"10000\",\n" +
            "    \"duration\": \"10000\",\n" +
            "    \"name\": \"测试的讲解视频\",\n" +
            "    \"url\": {\n" +
            "      \"mobile\": \"http://119.90.16.196:9999/7xaw4c.com2.z0.glb.qiniucdn.com/%E6%95%B4%E5%BC%8F%E7%9A%84%E4%B9%98%E6%B3%95_1a_%E5%9B%9B%E5%88%99%E8%BF%90%E7%AE%97%E8%A1%A5%E5%AE%8C%E8%AE%A1%E5%88%92.mp4\",\n" +
            "      \"pc\": \"http://119.90.16.196:9999/7xaw4c.com2.z0.glb.qiniucdn.com/%E6%95%B4%E5%BC%8F%E7%9A%84%E4%B9%98%E6%B3%95_1a_%E5%9B%9B%E5%88%99%E8%BF%90%E7%AE%97%E8%A1%A5%E5%AE%8C%E8%AE%A1%E5%88%92.mp4\"\n" +
            "    }\n" +
            "  }\n," +
            "  \"_id\": \"563866a538e9d0bf55bc9e93\",\n" +
            "  \"deleted\": false,\n" +
            "  \"createdAt\": \"2015-11-03T07:47:49.098Z\",\n" +
            "  \"updatedAt\": \"2015-11-03T07:47:49.098Z\",\n" +
            "  \"name\": \"新专题123123313\",\n" +
            "  \"challengeAmount\": 2,\n" +
            "  \"gongLueImage\": \"http://7sbkoa.com2.z0.glb.qiniucdn.com/lALOAnmF_c0DbM0Dkw_915_876.png\",\n" +
            "  \"desc\": [],\n" +
            "  \"problems\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"blank\": \"1\",\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"_id\": \"563866ac38e9d0bf55bc9e95\",\n" +
            "        \"body\": \"答案是1\",\n" +
            "        \"type\": \"blank\",\n" +
            "        \"level\": 1,\n" +
            "        \"choices\": []\n" +
            "      },\n" +
            "      {\n" +
            "        \"_id\": \"563866ad38e9d0bf55bc9e96\",\n" +
            "        \"body\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 1,\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ],\n" +
            "    [\n" +
            "      {\n" +
            "        \"_id\": \"563866ab38e9d0bf55bc9e94\",\n" +
            "        \"body\": \"阿斯蒂芬的萨芬\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 2,\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"_id\": \"563866ab38e9d0bf55bc9e94\",\n" +
            "        \"body\": \"阿斯蒂芬的萨芬\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 2,\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"_id\": \"563866ab38e9d0bf55bc9e94\",\n" +
            "        \"body\": \"阿斯蒂芬的萨芬\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 2,\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"_id\": \"563866ab38e9d0bf55bc9e94\",\n" +
            "        \"body\": \"阿斯蒂芬的萨芬\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 2,\n" +
            "        \"explain\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"prompt\": \"负数可以表示一个相反意义的量。比如说，桑尼姐增肥$5kg$用$+5$表示，那么$-2$表示\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ],\n" +
            "    [\n" +
            "      {\n" +
            "        \"prompt\": \"哈哈哈哈\",\n" +
            "        \"blank\": \"2012\",\n" +
            "        \"explain\": \"填空题2111\",\n" +
            "        \"_id\": \"563866ac38e9d1bf55bc9e95\",\n" +
            "        \"body\": \"答案是2012\",\n" +
            "        \"type\": \"blank\",\n" +
            "        \"level\": 3,\n" +
            "        \"choices\": []\n" +
            "      },\n" +
            "      {\n" +
            "        \"_id\": \"563866ad38e9d0bf55bc9e963213123\",\n" +
            "        \"body\": \"床前明月光～～\",\n" +
            "        \"type\": \"single\",\n" +
            "        \"level\": 3,\n" +
            "        \"explain\": \"大大叔大叔大叔大叔大叔｀\",\n" +
            "        \"prompt\": \"dasdasdasdasdasdasdas\",\n" +
            "        \"choices\": [\n" +
            "          {\n" +
            "            \"body\": \"$A$\",\n" +
            "            \"correct\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$B$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$C$\",\n" +
            "            \"correct\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"body\": \"$D$\",\n" +
            "            \"correct\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
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
