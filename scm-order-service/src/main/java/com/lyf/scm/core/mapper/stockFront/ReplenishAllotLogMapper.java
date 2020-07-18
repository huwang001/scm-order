package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ReplenishAllotLogDTO;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishAllotLogE;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReplenishAllotLogMapper {

    /**
     * @Description: 保存寻源日志 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param replenishAllotLogE
     * @return 
     */
    void saveReplenishAllotLog(ReplenishAllotLogE replenishAllotLogE);

    void updateSuccessRecords(@Param("id") Long id, @Param("successRecords") Integer successRecords, @Param("endTime") Date endTime);

    /**
     * 查询寻源执行日志
     */
    List<ReplenishAllotLogE> queryAllotLogCondition(@Param("condition") ReplenishAllotLogDTO condition);
}
