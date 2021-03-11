package com.iwhalecloud.lottery.util;


import com.iwhalecloud.lottery.config.LotteryParam;

/**
 * <p>Title: LotteryUtil</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-30 15:33
 */
public class LotteryUtil {

    /**
     * @param rate 中奖概率
     * @return 1 中奖, 0不中奖
     */
    public static int lottery(double rate) {
        double winFlag = 1 - rate;
        double random = Math.random();
        if (random <= winFlag) {
            return LotteryParam.WIN_FALSE;
        }
        return LotteryParam.WIN_TRUE;
    }

    public static void main(String[] args) {
        int winNum = 0;
        int unWinNum = 0;
        double rate = 0.05d;

        for (int i = 0; i < 10000; i++) {
            int result = lottery(rate);
            if (result == LotteryParam.WIN_FALSE) {
                unWinNum++;
            }
            else {
                winNum++;
            }
        }
        int allPeople = winNum + unWinNum;
        System.out.println("中奖人数: " + winNum);
        System.out.println("未中奖人数: " + unWinNum);
        System.out.println("总人数: "+allPeople);
        double result = (double) winNum / (double)allPeople;
        System.out.println("实际概率: " + result);
        System.out.println("预测概率: " + rate);
        System.out.println("概率偏差: " + (rate - result));
    }

}
