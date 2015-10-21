package com.yangcong345.android.phone.support.cordova.plugin;

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

    private String imageBase64 = "data:image/png;base64,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";

    private static final String ACTION_LOAD_PROBLEM = "loadProblem";//加载题目
    private static final String ACTION_LOAD_IMAGE = "loadImage";// 加载图片


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
//        this.
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (ACTION_LOAD_PROBLEM.equals(action)) {
            loadProblem(args, callbackContext);
            return true;
        } else if (ACTION_LOAD_IMAGE.equals(action)) {
            loadImage(args, callbackContext);
            return true;
        } else {
            callbackContext.error(action + ":action not define!");
            return false;
        }
    }

    /**
     * 加载题目
     *
     * @param args
     * @param callbackContext
     */
    private void loadProblem(JSONArray args, CallbackContext callbackContext) {
        JSONObject reslutJO = new JSONObject();
        try {
            reslutJO.put("title", "title测试");
            reslutJO.put("content", "content测试");
            callbackContext.success(reslutJO);
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }

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
}
