package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description: 推送退货入库通知给交易中心传输对象
 * <p>
 * @Author: wwh 2020/4/20
 */
@Data
public class PushReturnNoticeDTO implements Serializable {

	/**
	 * 售后单号
	 */
    private String reverseOrderNo;

    /**
     * 明细集合
     */
    private List<PushReturnDetailNoticeDTO> itemList;
    
}