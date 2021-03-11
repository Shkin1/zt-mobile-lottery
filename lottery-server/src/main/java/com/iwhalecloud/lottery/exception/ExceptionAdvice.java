package com.iwhalecloud.lottery.exception;

import com.iwhalecloud.lottery.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>Title: ExceptionAdvice</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-09 09:24
 */
@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(LtException.class)
    public R exception(Exception e) {
        log.error("异常: {}", e.getMessage());
        return R.fail(e.getMessage());
    }
}
