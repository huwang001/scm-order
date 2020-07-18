package com.lyf.scm.core.service.stockFront;

import java.util.List;

import com.lyf.scm.core.api.dto.stockFront.CustomizeTableRowDTO;

public interface CustomizeTableRowService {
	
    List<CustomizeTableRowDTO> getDetailByTableCodeAndUserId(String tableCode, Long userId);

    void updateDetailByDates(List<CustomizeTableRowDTO> customizeTableRowDTOs);
    
}