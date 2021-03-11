package com.iwhalecloud.lottery.controller;

import com.iwhalecloud.lottery.pojo.R;
import com.iwhalecloud.lottery.service.ParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>Title: ParamController</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-26 12:38
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/param/")
public class ParamController {

    @Resource
    private ParamService paramService;

    /**
     * 首轮初始化.
     * 抽奖人数,中奖人数默认设置为0
     *
     *
     * @param baseWinNum 获奖基数
     * @param maxWinNum  最大获奖数
     * @param predictNum 预测获奖数
     * @return result
     */
    @GetMapping("initFirstDayParam")
    public Object initFirstDayParam(@RequestParam("baseWinNum") int baseWinNum,
                                    @RequestParam("maxWinNum") int maxWinNum,
                                    @RequestParam("predictNum") int predictNum) {
        try {
            paramService.initParam(baseWinNum, maxWinNum, predictNum, true);
            return R.ok();
        }
        catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }


    @GetMapping("nextLottery")
    public Object nextLottery() {
        try{
            // 保存当前轮结果, 并初始下一轮参数
            paramService.nextRound();
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("queryParam")
    public Object queryParam() {
        return R.ok(paramService.queryParam());
    }


    @GetMapping("getValueByKey")
    public Object getValueByKey(@RequestParam("key") String key) {
        return paramService.getValueByKey(key);
    }


    @GetMapping("queryResult")
    public Object queryResult() {
        return R.ok(paramService.queryResult());
    }

}
