package com.iwhalecloud.lottery.mapper;

import com.iwhalecloud.lottery.pojo.MobileLotteryDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>Title: MobileLotteryMapper</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-12 09:31
 */
@Mapper
public interface MobileLotteryMapper {

    /**
     * 保存当前轮参数
     *
     * @param mobileLotteryDo mobileLotteryDo
     * @return insert num
     */
    int insert(MobileLotteryDo mobileLotteryDo);

    /**
     * query param
     *
     * @return MobileLotteryDos
     */
    List<MobileLotteryDo> query();

    /**
     * 重新(首轮)初始化后, 老数据的处理
     *
     * @return 更新的条数
     */
    int oldDataHandle();
}
