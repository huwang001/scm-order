package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.AdjustForetasteDO;
import lombok.Data;

import java.util.List;

@Data
public class AdjustForetasteE extends AdjustForetasteDO {

    private String channelCode;

    private List<AdjustForetasteDetailE> frontRecordDetails;
}
