package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author zys
 * @Description 前置单批次
 * @date 2020/6/12 10:18
 * @Version
 */

@Component
@Scope("prototype")
@Data
@EqualsAndHashCode(callSuper = false)
public class FrontBatchStockE extends SkuQtyUnitBase {

    /**
     * 批次
     */
    private String batchCode;
}
