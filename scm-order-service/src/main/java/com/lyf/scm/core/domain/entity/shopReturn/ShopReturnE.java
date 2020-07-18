package com.lyf.scm.core.domain.entity.shopReturn;

import com.lyf.scm.core.domain.model.shopReturn.ShopReturnDO;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/14
 */
@Data
public class ShopReturnE extends ShopReturnDO {
    private List<ShopReturnDetailE> shopReturnDetailEList;
}
