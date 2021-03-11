package com.iwhalecloud.lottery.config;

/**
 * @author shijinpu
 * @version v1.0.0
 * @date 2020/10/26 19:46
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ------------------------------------------------------------
 */
public final class LotteryParam {

    private LotteryParam() {
    }

    /**
     * 中奖
     */
    public static final int WIN_TRUE = 1;
    /**
     * 未中
     */
    public static final int WIN_FALSE = 0;


    /**
     * 获奖基数
     */
    public static final String REDIS_KEY_BASE_WIN_NUM = "baseWinNum";
    /**
     * 最大获奖人数
     */
    public static final String REDIS_KEY_MAX_WIN_NUM = "maxWinNum";
    /**
     * 预测抽奖人数
     */
    public static final String REDIS_KEY_PREDICT_NUM= "predictNum";
    /**
     * 中奖人数
     */
    public static final String REDIS_KEY_WIN_PEOPLE_NUM = "winPeopleNum";

    /**
     * 抽奖人数
     */
    public static final String REDIS_KEY_PEOPLE_NUM = "peopleNum";
    /**
     * 轮数
     */
    public static final String REDIS_ROUND = "round";


}
