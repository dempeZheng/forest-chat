package com.dempe.ocean.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class JsonResult extends JSONObject {

    public JsonResult putCode(int code) {
        put("code", code);
        return this;
    }

    public JsonResult putData(Object data) {
        put("data", data);
        return this;
    }

    public static JsonResult getJsonResult(Object data) {
        return new JsonResult().putCode(StatCode.SUCCESS.code).putData(data);
    }


    public static enum StatCode {

        SUCCESS(0), SRV_ERR(1), NO_PERMISSION(2);

        private int code;

        private StatCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
