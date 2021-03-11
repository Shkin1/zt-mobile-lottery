package com.iwhalecloud.lottery.pojo;

import lombok.Data;

/**
 * <p>Title: R</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description: 响应类
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-26 19:36
 */
@Data
public final class R {
    public static final int SC_OK = 200;
    private final int code;
    private final Object data;
    private final String message;

    private R(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static R ok() {
        return new R(SC_OK, null, null);
    }

    public static R ok(Object o) {
        return new R(SC_OK, o, null);
    }

    public static R ok(Object data, String msg) {
        return new R(SC_OK, data, msg);
    }


    public static R fail(String msg) {
        return new R(-1, null, msg);
    }

}
