package com.dempe.ocean.rpc.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/16
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class JsonResult extends JSONObject {

    public static JsonResult getJsonResult() {
        return new JsonResult().putCode(CodeType.SUCCESS.value());
    }

    public static JsonResult getJsonResult(Object data) {
        return getJsonResult().putData(data);
    }

    public JsonResult putCode(int code) {
        put("code", code);
        return this;
    }

    public JsonResult putData(Object data) {
        put("data", data);
        return this;
    }

    public static enum CodeType {
        SUCCESS(0),
        SRV_ERR(1);
        private int code;

        private CodeType(int code) {
            this.code = code;
        }

        public int value() {
            return code;
        }


    }
}
