/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dempe.ocean.rpc.utils;


public class ErrorCodes {

    /**
     * success status
     */
    public static final int ST_SUCCESS = 0;

    /**
     * 未知异常
     */
    public static final int ST_ERROR = 2001;

    /**
     * 方法未找到异常
     */
    public static final int ST_SERVICE_NOTFOUND = 1001;


    /**
     * 压缩与解压异常
     */
    public static final int ST_ERROR_COMPRESS = 3000;


    /**
     * read time out
     */
    public static final int ST_READ_TIMEOUT = 62;

    /**
     * onceTalkTimeout timeout message
     */
    public static final String MSG_READ_TIMEOUT =
            "method request time out, please check 'onceTalkTimeout' property. current value is:";

    /**
     * check is error code is equals to ST_SUCCESS
     *
     * @param errorCode
     * @return
     */
    public static boolean isSuccess(int errorCode) {
        return ST_SUCCESS == errorCode;
    }


}
