package com.lyf.scm.core.service.stockFront.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.CustomizeTableRowDTO;
import com.lyf.scm.core.domain.convert.stockFront.CustomizeTableRowConvertor;
import com.lyf.scm.core.mapper.stockFront.CustomizeTableRowMapper;
import com.lyf.scm.core.service.stockFront.CustomizeTableRowService;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;


/**
 * 用户自定义标题处理类
 */
@Service
@Slf4j
public class CustomizeTableRowServiceImpl implements CustomizeTableRowService {
    
	@Resource
    private CustomizeTableRowMapper customizeTableRowMapper;
	
	@Resource
	private CustomizeTableRowConvertor customizeTableRowConvertor;

    /**
     * 根据table_code和用户获取自定义数据
     *
     * @param tableCode
     * @param userId
     * @return
     */
    @Override
    public List<CustomizeTableRowDTO> getDetailByTableCodeAndUserId(String tableCode, Long userId) {
        return customizeTableRowConvertor.convertEList2DTOList(customizeTableRowMapper.getDetailByTableCodeAndUserId(tableCode, userId));
    }

    /**
     * 根据CustomizeTableRowDTO对象集合更新某表的标题信息
     *
     * @param customizeTableRowDTOs
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetailByDates(List<CustomizeTableRowDTO> customizeTableRowDTOs) {
        if (customizeTableRowDTOs == null || customizeTableRowDTOs.size() == 0) {
            throw new RomeException(ResCode.ORDER_ERROR_1002,ResCode.ORDER_ERROR_1002_DESC);
        }
        //首先删除有关该标题的数据
        customizeTableRowMapper.deleteDetailByTableCode(customizeTableRowDTOs.get(0).getTableCode(),customizeTableRowDTOs.get(0).getUserId());
        //写入该标题数据
        customizeTableRowMapper.insertDetailByDates(customizeTableRowConvertor.convertDTOList2EList(customizeTableRowDTOs));
    }

}