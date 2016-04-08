package com.guanghe.cordova.plugin;

import android.text.TextUtils;
import android.widget.Toast;

//import com.yangcong345.android.phone.YCMathApplication;
//import com.yangcong345.android.phone.domain.legacy.DataRequestManager;
//import com.yangcong345.android.phone.datasource.ResponseBean;
//import com.yangcong345.android.phone.datasource.legacy.ResponseCallback;
//import com.yangcong345.android.phone.core.point.PointMeta;
//import com.yangcong345.android.phone.manager.LogManager;
//import com.yangcong345.android.phone.manager.TracksManager;
//import com.yangcong345.android.phone.manager.UserManager;
//import com.yangcong345.android.phone.manager.YCPreferenceManager;
//import com.yangcong345.android.phone.model.scheme.ProblemSet;
//import com.yangcong345.android.phone.request.AnswerRequest;
//import com.yangcong345.android.phone.ui.activity.ProblemDoingActivity;
//import com.yangcong345.android.phone.ui.activity.ProblemEndActivity;
//import com.yangcong345.android.phone.utils.CommonUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Copyright (c) 2014 Guanghe.tv All right reserved.
 * --------------------<-.->-----------------------
 * Author:      Nono(陈凯)
 * CreateDate:  15/10/20
 * Description: exp...
 * Version:     1.0.0
 */
public class YCProblemSetPlugin extends CordovaPlugin {

    /**
     * json key
     */
    private static final String TOPIC_ID = "topicId";
    private static final String TYPE = "type";
    private static final String TOPIC_TYPE = "topicType";
    private static final String TOPIC_STATE = "topicState";
    private static final String REWARD_RULES = "rewardRules";
    private static final String FINISH_STATE = "finishState";
    private static final String LEVELS = "levels";
    private static final String SHOW_IMG_SYMBOL = "showImgSymbol";


    private JSONObject mPracticeSetJO;
    private JSONArray mChallengeSetJA;

    private JSONArray mCacheAnswersJA;
    /**
     * 图片mock数据
     */
    /**
     * action name
     */
    private static final String ACTION_LEVEL_FIAL = "levelFail";//层失败
    private static final String ACTION_LEVEL_SUCCESS = "levelSuccess";// 层成功
    private static final String ACTION_LOAD_PROBLEM_CONTEXT = "loadProblemContext";// 加载做题的一些元素数据
    private static final String ACTION_CLOSE_PROBLEM_SET = "closeProblemSet";//退出做题
    private static final String ACTION_FINISH_PROBLEM_SET = "finishProblemSet";//完成专题测试
    private static final String ACTION_LOAD_PROBLEM_SET = "loadProblemSet";
    private static final String ACTION_RECORD_TRACK_INFO = "recordTrackInfo";
    private static final String ACTION_RECORD_WRONG_PROBLEMS = "recordWrongProblems";

    /**
     * 题目类型
     */
    private boolean isChallenge = false;
    private int mSetIndex;


    private String mTopic;
    private String mType;
    private String mTopicType;
    private JSONObject mTopicState;
    private JSONObject mRules;
    private boolean mIsShowImgSymbol;

    /**
     * 维护做题状态
     */

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
//        initData();
        initMockData();
    }

    /**
     * todo mock 数据
     */
    private void initMockData() {
        mType = "P";
        mTopic = mockTopicId;
        mIsShowImgSymbol = true;
        mTopicType = mockTopicType;
        mTopicState = new JSONObject();
        try {
            mRules = new JSONObject(mockRules);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initData(){
//         mIsShowImgSymbol =   YCPreferenceManager.getBooleanValue(cordova.getActivity(), YCPreferenceManager.KEY_SHOW_IMG_SYMBOL)
//        String argSet = cordova.getActivity().getIntent().getStringExtra(ProblemDoingActivity.ARG_PROBLEM_SET);
//        isChallenge = cordova.getActivity().getIntent().getBooleanExtra(ProblemDoingActivity.ARG_IS_CHALLENGE, false);
//        mSetIndex = cordova.getActivity().getIntent().getIntExtra(ProblemDoingActivity.ARG_PROBLEM_SET_INDEX, 0);
//        mTopic = cordova.getActivity().getIntent().getStringExtra(ProblemDoingActivity.ARG_TOPIC_ID);
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
        if (ACTION_LOAD_PROBLEM_CONTEXT.equals(action)) {
            loadProblemContext(args, callbackContext);
            return true;
        } else if (ACTION_CLOSE_PROBLEM_SET.equals(action)) {
            closeProblemSet();
            return true;
        } else if (ACTION_FINISH_PROBLEM_SET.equals(action)) {
            finishProblemSet(args, callbackContext);
            return true;
        } else if (ACTION_LOAD_PROBLEM_SET.equals(action)) {
            loadPracticeSet(callbackContext);
//            } else {
//                loadChallengeSet(callbackContext);
//            }
            return true;
        } else if (ACTION_RECORD_TRACK_INFO.equals(action)) {
            recordTrackInfo(args, callbackContext);
            return true;
        } else if (ACTION_RECORD_WRONG_PROBLEMS.equals(action)) {
            recordWrongProblems(args, callbackContext);
            return true;
        } else if (ACTION_LEVEL_FIAL.equals(action)) {
            levelFail(args, callbackContext);
            return true;
        } else if (ACTION_LEVEL_SUCCESS.equals(action)) {
            levelSucceed(args, callbackContext);
            return true;
        } else {
            callbackContext.error(action + ":action not define!");
            return false;
        }
    }

    /**
     * 埋点做题
     *
     * @param args
     * @param callbackContext
     */
    private void recordTrackInfo(JSONArray args, CallbackContext callbackContext) {
        if (args == null) {//参数为null 返回
            callbackContext.error(" args is null ");
        } else {
            try {
                JSONArray trackInfo = args.getJSONArray(0);
//                TracksManager.postPointArray(cordova.getActivity().getApplicationContext(), trackInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送做题目纪录
     *
     * @param args
     * @param callbackContext
     */
    private void recordWrongProblems(JSONArray args, CallbackContext callbackContext) {
        if (args == null) {//参数为null 返回
            callbackContext.error(" args is null ");
        } else {
            try {
                JSONArray answers = args.getJSONArray(0);
                if (answers != null && answers.length() > 0) {
//                    LogManager.log("recordWrongProblems", answers.toString());
//                    if (UserManager.isLogined(cordova.getActivity().getApplicationContext())) {
//                        DataRequestManager.getInstance().addRequest(new AnswerRequest(cordova.getActivity().getApplicationContext(), answers, new ResponseCallback<JSONArray>() {
//                            @Override
//                            public void onResponse(int dataGetWay, ResponseBean rb, JSONArray jsonObj) {
//                            }
//                        }));
//                    } else {
//                        mCacheAnswersJA = answers;
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 退出做题
     */
    private void closeProblemSet() {
        cordova.getActivity().finish();
        HashMap<String, Object> values = new HashMap<>();
//        String topicId = ((YCMathApplication) (YCMathApplication.getInstance())).getStudyingTopicId();
//        if (isChallenge) {
//            values.put("topicId", topicId);
//            TracksManager.postPoint(cordova.getActivity().getApplicationContext(), PointMeta.EVENT_TERMINATE_CHALLENGE, TracksManager.CATEGORY_COURSE, values);
//        } else {
//            values.put("topicId", topicId);
//            values.put("problemSetId", CommonUtils.getStringFromJsonObj(ProblemSet._id, mPracticeSetJO));
//            TracksManager.postPoint(cordova.getActivity().getApplicationContext(), PointMeta.EVENT_TERMINATE_PROBLEMSET, TracksManager.CATEGORY_COURSE, values);
//        }
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
                String setId = args.getString(1);

                if (isPass) {
                    Toast.makeText(cordova.getActivity().getBaseContext(), "测试通过了!!!!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(setId) || TextUtils.equals("null", setId)) {
                        Toast.makeText(cordova.getActivity().getBaseContext(), "专题失败", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(cordova.getActivity().getBaseContext(), "挑战失败:失败专题=" + setId, Toast.LENGTH_LONG).show();

                    }
                }
                sumbmitPractice(isPass, setId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sumbmitPractice(boolean pass, String failedSet) {
//        Intent intent = new Intent(cordova.getActivity(), ProblemEndActivity.class);
//        intent.putExtra(ProblemEndActivity.ARG_IS_CHALLENGE, isChallenge);
//        intent.putExtra(ProblemEndActivity.ARG_IS_PASS, pass);
//        if (isChallenge) {
//            intent.putExtra(ProblemEndActivity.ARG_PROBLEM_SET_INDEX, Integer.MAX_VALUE);
//
//            intent.putExtra(ProblemEndActivity.ARG_PROBLEM_SET, mChallengeSetJA.toString());
//            if (!TextUtils.isEmpty(failedSet) || TextUtils.equals("null", failedSet)) {
//                intent.putExtra(ProblemEndActivity.ARG_FAILED_SET_ID, failedSet);
//            }
//        } else {
//            sendFinishProblemSetEvent(pass);
//            intent.putExtra(ProblemEndActivity.ARG_PROBLEM_SET_INDEX, mSetIndex);
//            intent.putExtra(ProblemEndActivity.ARG_PROBLEM_SET, mPracticeSetJO.toString());
//        }
//        if (mCacheAnswersJA != null) {
//            intent.putExtra(ProblemEndActivity.ARG_ANSWERS, mCacheAnswersJA.toString());
//        }
//        cordova.getActivity().startActivity(intent);
//        cordova.getActivity().finish();
    }

    private void sendFinishProblemSetEvent(boolean pass) {
//        HashMap<String, Object> values = new HashMap<>();
//        values.put("topicId", mTopic);
//        values.put("problemSetId", CommonUtils.getStringFromJsonObj(ProblemSet._id, mPracticeSetJO));
//        if (pass) {
//            TracksManager.postPoint(cordova.getActivity().getApplicationContext(), PointMeta.FINISH_PROBLEM_SET, TracksManager.CATEGORY_COURSE, values);
//        } else {
//            TracksManager.postPoint(cordova.getActivity().getApplicationContext(), PointMeta.EVENT_PROBLEMSET_FAILURE, TracksManager.CATEGORY_COURSE, values);
//
//        }
    }

    /**
     * 加载专题题目
     *
     * @param callbackContext
     */
    private void loadPracticeSet(CallbackContext callbackContext) {
        Toast.makeText(cordova.getActivity(), "loadPracticeSet", Toast.LENGTH_LONG).show();

        try {
            if (mPracticeSetJO == null) {
                mPracticeSetJO = new JSONObject(mockPracticeMock);
            }
            callbackContext.success(mPracticeSetJO);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());

        }
    }

    /**
     * 加载挑战题目
     *
     * @param callbackContext
     */
    private void loadChallengeSet(CallbackContext callbackContext) {
//        try {
//            if (mChallengeSetJA == null) {
//                mChallengeSetJA = new JSONArray(challengeSetMock);
//            }
//            callbackContext.success(mChallengeSetJA);
//        } catch (Exception e) {
//            e.printStackTrace();
//            callbackContext.error(e.getMessage());
//        }
    }


    /**
     * 初始化做题全局数据
     *
     * @param args
     * @param callbackContext
     */
    private void loadProblemContext(JSONArray args, CallbackContext callbackContext) {
        Toast.makeText(cordova.getActivity(), "loadProblemContext", Toast.LENGTH_LONG).show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(TYPE, mType);
            jsonObject.put(TOPIC_ID, mTopic);
            jsonObject.put(TOPIC_TYPE, mTopicType);
            jsonObject.put(TOPIC_STATE, null);
            jsonObject.put(REWARD_RULES, mRules);
            jsonObject.put(SHOW_IMG_SYMBOL, mIsShowImgSymbol);
            callbackContext.success(jsonObject);
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }
    }

    /**
     * 层失败
     *
     * @param args
     * @param callbackContext
     */
    private void levelFail(JSONArray args, CallbackContext callbackContext) {
        if (args == null) {//参数为null 返回
            callbackContext.error(" args is null ");
        } else {
            try {
                String levelId = args.getString(0);
                Boolean isHanger = args.getBoolean(1);
                Toast.makeText(cordova.getActivity(), "层失败:" + levelId+" is hanger?"+isHanger, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
        }
    }

    /**
     * 层成功
     *
     * @param args
     * @param callbackContext
     */
    private void levelSucceed(JSONArray args, CallbackContext callbackContext) {
        if (args == null) {//参数为null 返回
            callbackContext.error(" args is null ");
        } else {
            try {
                String levelId = args.getString(0);
                int tryTimes = args.getInt(1);
                JSONObject levelObj = args.getJSONObject(2);
                Toast.makeText(cordova.getActivity(), "层成功:" + levelId + "第几次做对" + tryTimes, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
        }
    }

    private static final String mockRules = "{\"step\":[5,2],\"target\":[10,5],\"extend\":[10,5],\"hanger\":[10,5],\"hyperVideo\":10}";
    private static final String mockTopicId = "xxxxx";
    private static final String mockTopicType = "A";
    private static final String mockTopicState = "xxxxx";
    public static final String mockPracticeMock = "{\n" +
            "  \"_id\": \"56f7ae99217847fde27a8f03\",\n" +
            "  \"ref\": \"54c708798bac81fccbd4bb12\",\n" +
            "  \"name\": \"补充练习\",\n" +
            "  \"levels\": [\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"56696477397a1fd6350f024e\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$3m$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$m3$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$3 \\\\times m$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$3$ 与$m$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"数字与字母相乘，数字必须在前，且通常将乘号写作$ \\\\cdot $或省略不写.表示$3$ 与$m$的乘积，可以写成$3 \\\\cdot m$或$3m$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696478397a1fd6350f024f\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$10x$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$x10$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$10 \\\\times x$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$10$ 与$x$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"数字与字母相乘，数字必须在前，且通常将乘号写作$ \\\\cdot $或省略不写.表示$10$ 与$x$的乘积，可以写成$10 \\\\cdot x$或$10x$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696478397a1fd6350f0250\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$2a$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$a2$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$2 \\\\times a$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$2$与$a$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"数字与字母相乘，数字必须在前，且通常将乘号写作$ \\\\cdot $或省略不写.表示$2$ 与$a$的乘积，可以写成$2 \\\\cdot a$或$2a$.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 1,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8ef4\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"566965b8397a1fd6350f0251\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$ab$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$a \\\\times b$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$a$与$b$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"字母与字母相乘，通常将乘号写作 $\\\\cdot $或省略不写.表示$a$与$b$的乘积，可以写成$a \\\\cdot b$或$ab$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566965b9397a1fd6350f0252\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$mn$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$m \\\\times n$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$m$与$n$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"字母与字母相乘，通常将乘号写作$ \\\\cdot $或省略不写.表示$m$与$n$的乘积，可以写成$m \\\\cdot n$或$mn$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566965ba397a1fd6350f0253\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$xy$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$x \\\\times y$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$x$与$y$的乘积，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"字母与字母相乘，通常将乘号写作$ \\\\cdot$ 或省略不写.表示$x$与$y$的乘积，可以写成$x \\\\cdot y$或$xy$.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 1,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8ef5\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"56696604397a1fd6350f0254\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$\\\\dfrac{7}{5}x$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$1\\\\dfrac{2}{5}x$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$1+\\\\dfrac{2}{5}x$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$ \\\\dfrac{5}{7}x$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$x$的$1\\\\dfrac{2}{5}$倍，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"带分数与字母相乘，带分数要写成假分数的形式. <br>\\n$x$的$1\\\\dfrac{2}{5}$倍，写成$\\\\dfrac{7}{5}x$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696604397a1fd6350f0255\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$\\\\dfrac{8}{3}a$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$2\\\\dfrac{2}{3}a$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$2+\\\\dfrac{2}{3}a$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$\\\\dfrac{3}{8}a$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$a$的$2\\\\dfrac{2}{3}$倍，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"带分数与字母相乘，带分数要写成假分数的形式. <br>\\n$a$的$2\\\\dfrac{2}{3}$倍，写成$\\\\dfrac{8}{3}a$.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696604397a1fd6350f0256\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$\\\\dfrac{2}{9}m$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$4\\\\dfrac{1}{2}m$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$4+\\\\dfrac{1}{2}m$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$\\\\dfrac{9}{2}m$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"表示$m$的$4\\\\dfrac{1}{2}$倍，下列符合代数式书写规范的是（  ）\",\n" +
            "          \"explain\": \"带分数与字母相乘，带分数要写成假分数的形式. <br>\\n$m$的$4\\\\dfrac{1}{2}$倍，写成$\\\\dfrac{9}{2}m$.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 1,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8ef7\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"5669696f397a1fd6350f0268\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2n$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$n+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$n^2 $\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2n+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$n$是整数，下列表示偶数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用 $2n$（$n$为整数）表示偶数, <br>\\n        用$2n-1$或$2n+1$（$n$为整数）表示奇数.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"5669696f397a1fd6350f0269\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2k$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$k+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$k^2 $\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2k+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$k$是整数，下列表示偶数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用$2k$（$k$为整数）表示偶数, <br>\\n        用$2k-1$或$2k+1$（$k$为整数）表示奇数.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696970397a1fd6350f026a\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2t$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$t+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$ t^2 $\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2t+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$t$是整数，下列表示偶数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用$2t$（$t$为整数）表示偶数, <br>\\n        用$2t+1$或$2t-1$（$t$为整数）表示奇数.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 3,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8efe\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"566969da397a1fd6350f026b\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2n-1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2n-2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2n$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$n+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$n$是整数，下列表示奇数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用$2n$（$n$为整数）表示偶数, <br>\\n        用$2n-1$或$2n+1$（$n$为整数）表示奇数.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566969da397a1fd6350f026c\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2k-1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2k-2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2k$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$k+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$k$是整数，下列表示奇数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用$2k$（$k$为整数）表示偶数, <br>\\n        用$2k-1$或$2k+1$（$k$为整数）表示奇数.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566969db397a1fd6350f026d\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$2t-1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2t-2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2t$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$t+1$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$t$是整数，下列表示奇数的是（ ）\",\n" +
            "          \"explain\": \"一般地，用$2t$（$t$为整数）表示偶数, <br>\\n        用$2t-1$或$2t+1$（$t$为整数）表示奇数.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 3,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8efc\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"56696a1b397a1fd6350f026e\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$k-1$，$k$，$k+1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2k-1$，$2k+1$，$2k+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$k-1$，$k+1$，$k+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2k-2$，$2k$，$2k+2$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$k$是整数，下列表示三个连续整数的是（ ）\",\n" +
            "          \"explain\": \"$\\\\because$相邻两个整数之间相差$1$，<br>\\n        $\\\\therefore$只有 $k-1$，$k$，$k+1$表示三个连续整数\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696a1c397a1fd6350f026f\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$x-1$，$x$，$x+1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2x-1$，$2x+1$，$2x+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$x-1$，$x+1$，$x+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2x-2$，$2x$，$2x+2$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$x$是整数，下列表示三个连续整数的是（ ）\",\n" +
            "          \"explain\": \"$\\\\because$相邻两个整数之间相差$1$，<br>\\n        $\\\\therefore$只有 $x-1$，$x$，$x+1$表示三个连续整数\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696a1d397a1fd6350f0270\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"body\": \"$m-1$，$m$，$m+1$\",\n" +
            "              \"correct\": true\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2m-1$，$2m+1$，$2m+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$m-1$，$m+1$，$m+2$\",\n" +
            "              \"correct\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"body\": \"$2m-2$，$2m$，$2m+2$\",\n" +
            "              \"correct\": false\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"设$m$是整数，下列表示三个连续整数的是（ ）\",\n" +
            "          \"explain\": \"$\\\\because$相邻两个整数之间相差$1$，<br>\\n        $\\\\therefore$只有 $m-1$，$m$，$m+1$表示三个连续整数\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 3,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8efd\",\n" +
            "      \"pool\": \"target\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"56696a7e397a1fd6350f0272\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$10b+a$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$ba$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$10a+b$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$a+b$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"一个两位数，个位数字是$a$，十位数字是$b$，则这个两位数表示为（ ）\",\n" +
            "          \"explain\": \"个位数字是$a$，十位数字是$b$的两位数可表示为$10b+a$.<br>\\n注意一定不要写成$ba$，$ba$表示两数相乘.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696a7e397a1fd6350f0273\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$10x+y$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$xy$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$10y+x$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$x+y$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"一个两位数，个位数字是$y$，十位数字是$x$，则这个两位数表示为（ ）\",\n" +
            "          \"explain\": \"个位数字是$y$，十位数字是$x$的两位数可表示为$10x+y$.<br>\\n注意一定不要写成$xy$，$xy$表示两数相乘.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"56696a7f397a1fd6350f0274\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$10m+n$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$mn$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$10n+m$\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$m+n$\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"一个两位数,个位数字是$n$，十位数字是$m$，则这个两位数表示为（ ）\",\n" +
            "          \"explain\": \"个位数字是$n$，十位数字是$m$的两位数可表示为$10m+n$.<br>\\n注意一定不要写成$mn$，$mn$表示两数相乘.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 4,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8eff\",\n" +
            "      \"pool\": \"step\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"problems\": [\n" +
            "        {\n" +
            "          \"_id\": \"566971e9397a1fd6350f0278\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$ (1+25\\\\%)a$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$25\\\\%a$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(1-25\\\\%)a$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(a+25\\\\%)$元\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"一件商品的成本为$a$元，按照成本增加$25\\\\%$定价销售，则销售价格列式表示为（  ）\",\n" +
            "          \"explain\": \"销售价格<br>$=(1+25\\\\%)   \\\\times  $成本<br>                \\n $=(1+25\\\\%)a$ 元.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566971eb397a1fd6350f0279\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$(1-15\\\\%)x$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$15\\\\%x$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(1+15\\\\%)x$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(x-15\\\\%)$元\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"一件商品标价$x$元，商场活动全场减价$15\\\\%$销售，则现在售价列式表示为（   ）\",\n" +
            "          \"explain\": \"现在售价<br>$=(1-15\\\\%)   \\\\times  $标价<br>\\n                $=(1-15\\\\%)x$ 元.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"_id\": \"566971eb397a1fd6350f027a\",\n" +
            "          \"type\": \"single\",\n" +
            "          \"choices\": [\n" +
            "            {\n" +
            "              \"correct\": true,\n" +
            "              \"body\": \"$(1+50\\\\%)p$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$50\\\\%p$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(1-50\\\\%)p$元\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"correct\": false,\n" +
            "              \"body\": \"$(p+50\\\\%)$元\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"body\": \"大米的收购价是$p$元/公斤，现经加工后价格提高$50\\\\%$出售，则现销售价格列式表示为（   ）\",\n" +
            "          \"explain\": \"加工后售价<br>$=(1+50\\\\%)   \\\\times  $收购价<br>\\n                  $=(1+50\\\\%)p$ 元.\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"special\": {\n" +
            "        \"order\": 4,\n" +
            "        \"flag\": \"normal\"\n" +
            "      },\n" +
            "      \"_id\": \"56f7ae99217847fde27a8f00\",\n" +
            "      \"pool\": \"target\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

}
