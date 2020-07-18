package com.lyf.scm.core.mapper.stockFront;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.lyf.scm.core.domain.entity.stockFront.CustomizeTableRowE;

public interface CustomizeTableRowMapper {
	
	/**
	 * 根据table_code和用户获取自定义数据
	 * 
	 * @param tableCode
	 * @param userId
	 * @return
	 */
	List<CustomizeTableRowE> getDetailByTableCodeAndUserId(@Param("tableCode") String tableCode, @Param("userId") Long userId);

	/**
	 *删除有关该标题的数据
	 *
	 * @param tableCode
	 * @param userId
	 */
    void deleteDetailByTableCode(@Param("tableCode") String tableCode, @Param("userId") Long userId);

	/**
	 * 写入该标题数据
	 * 
	 * @param customizeTableRowEs
	 */
	void insertDetailByDates(@Param("customizeTableRowEs") List<CustomizeTableRowE> customizeTableRowEs);
	
}
