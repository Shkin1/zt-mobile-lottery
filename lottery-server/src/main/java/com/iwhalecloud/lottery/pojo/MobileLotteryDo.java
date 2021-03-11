package com.iwhalecloud.lottery.pojo;

/**
 * <p>Title: MobileLotteryDo</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-12 09:31
 */
public class MobileLotteryDo {
    private Integer winPeopleNum;
    private Integer peopleNum;
    private Integer baseWinNum;
    private Integer maxWinNum;
    private Integer predictNum;
    private Integer round;

    public Integer getWinPeopleNum() {
        return winPeopleNum;
    }

    public void setWinPeopleNum(Integer winPeopleNum) {
        this.winPeopleNum = winPeopleNum;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getBaseWinNum() {
        return baseWinNum;
    }

    public void setBaseWinNum(Integer baseWinNum) {
        this.baseWinNum = baseWinNum;
    }

    public Integer getMaxWinNum() {
        return maxWinNum;
    }

    public void setMaxWinNum(Integer maxWinNum) {
        this.maxWinNum = maxWinNum;
    }

    public Integer getPredictNum() {
        return predictNum;
    }

    public void setPredictNum(Integer predictNum) {
        this.predictNum = predictNum;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
}
