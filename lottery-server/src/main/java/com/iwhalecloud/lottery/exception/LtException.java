package com.iwhalecloud.lottery.exception;

import java.io.Serializable;

/**
 * <p>Title: LtException</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-28 00:10
 */
public class LtException extends RuntimeException implements Serializable {
    private final int code;
    private final String msg;

    public LtException(String msg) {
        super(msg);
        this.code = -1;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
