package com.iwhalecloud.lottery.controller;

import com.iwhalecloud.lottery.config.LotteryParam;
import com.iwhalecloud.lottery.pojo.R;
import com.iwhalecloud.lottery.service.LotteryService;
import com.iwhalecloud.lottery.service.ParamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>Title: LotteryController</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-28 00:17
 */
@CrossOrigin
@RestController
@RequestMapping("/api/lottery/")
public class LotteryController {

    @Resource
    LotteryService lotteryService;

    @Resource
    ParamService paramService;

    @Value("user.id")
    private String userId;


    @GetMapping("test")
    public Object test() {
        return userId;
    }


    /**
     * 抽奖.
     *
     * @return 抽奖结果
     */
    @GetMapping("play")
    public Object play() {
        int state = lotteryService.luckyDraw();
        return R.ok(state, state == LotteryParam.WIN_FALSE ? "未中奖" : "中奖");
    }

    @GetMapping("incr")
    public Object incr() {
        return paramService.incr(LotteryParam.REDIS_KEY_PEOPLE_NUM);
    }
}
