package com.iwhalecloud.lottery.service;

/**
 * @author shijinpu
 * @version v1.0.0
 * @date 2020/10/26 20:18
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ------------------------------------------------------------
 */
public interface LotteryService {

    /**
     * 抽奖.
     *
     * @return 1:中奖, 0 未中奖
     */
    int luckyDraw();
}
