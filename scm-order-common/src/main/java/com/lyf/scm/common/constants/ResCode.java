package com.lyf.scm.common.constants;

public interface ResCode {

    String ORDER_ERROR_1000 = "0";
    String ORDER_ERROR_1000_DESC = "操作成功";

    String ORDER_ERROR_1001 = "SC_1001";
    String ORDER_ERROR_1001_DESC = "操作失败";

    String ORDER_ERROR_1002 = "SC_1002";
    String ORDER_ERROR_1002_DESC = "参数有误";

    String ORDER_ERROR_1003 = "SC_1003";
    String ORDER_ERROR_1003_DESC = "系统异常";

    String ORDER_ERROR_1004 = "SC_1004";
    String ORDER_ERROR_1004_DESC = "预约单不存在";

    String ORDER_ERROR_1005 = "SC_1005";
    String ORDER_ERROR_1005_DESC = "预约单状态不是部分锁定或全部锁定";

    String ORDER_ERROR_1006 = "SC_1006";
    String ORDER_ERROR_1006_DESC = "预约单已创建调拨";

    String ORDER_ERROR_1007 = "SC_1007";
    String ORDER_ERROR_1007_DESC = "预约单明细不存在";

    String ORDER_ERROR_1008 = "SC_1008";
    String ORDER_ERROR_1008_DESC = "预约单对应仓库信息列表不包含调入实仓CODE";

    String ORDER_ERROR_1009 = "SC_1009";
    String ORDER_ERROR_1009_DESC = "实仓调拨单号返回异常";

    String ORDER_ERROR_1010 = "SC_1010";
    String ORDER_ERROR_1010_DESC = "实仓调拨状态更新失败";

    String ORDER_ERROR_1011 = "SC_1011";
    String ORDER_ERROR_1011_DESC = "调拨单号不能为空";

    String ORDER_ERROR_1012 = "SC_1012";
    String ORDER_ERROR_1012_DESC = "调拨出库回调状态更新失败";

    String ORDER_ERROR_1013 = "SC_1013";
    String ORDER_ERROR_1013_DESC = "当前预约锁库单库存已经全部锁定，请勿操作虚仓移库！";

    String ORDER_ERROR_1014 = "SC_1014";
    String ORDER_ERROR_1014_DESC = "预约单状态已加工更新失败";

    String ORDER_ERROR_1015 = "SC_1015";
    String ORDER_ERROR_1015_DESC = "预约单已存在";

    String ORDER_ERROR_1016 = "SC_1016";
    String ORDER_ERROR_1016_DESC = "该预约单不需要加工";

    String ORDER_ERROR_1017 = "SC_1017";
    String ORDER_ERROR_1017_DESC = "该预约单不为调拨入库状态";

    String ORDER_ERROR_1018 = "SC_1018";
    String ORDER_ERROR_1018_DESC = "该预约单中的商品不存在";

    String ORDER_ERROR_1019 = "SC_1019";
    String ORDER_ERROR_1019_DESC = "该预约单中的商品非基本单位";

    String ORDER_ERROR_1020 = "SC_1020";
    String ORDER_ERROR_1020_DESC = "预约单状态待发货更新失败";

    String ORDER_ERROR_1021 = "SC_1021";
    String ORDER_ERROR_1021_DESC = "预约单交易审核状态通过更新失败";

    String ORDER_ERROR_1022 = "SC_1022";
    String ORDER_ERROR_1022_DESC = "预约单状态已发货更新失败";

    String ORDER_ERROR_1023 = "SC_1023";
    String ORDER_ERROR_1023_DESC = "预约单状态同步交易状态待同步DO更新失败";

    String ORDER_ERROR_1025 = "SC_1025";
    String ORDER_ERROR_1025_DESC = "预约单状态不是已发货";

    String ORDER_ERROR_1026 = "SC_1026";
    String ORDER_ERROR_1026_DESC = "预约单发货状态更新失败";

    String ORDER_ERROR_1024 = "SC_1024";
    String ORDER_ERROR_1024_DESC = "预约单状态同步交易状态已同步DO更新失败";

    String ORDER_ERROR_1027 = "SC_1027";
    String ORDER_ERROR_1027_DESC = "预约单状态同步交易状态待同步LOCK更新失败";

    String ORDER_ERROR_1028 = "SC_1028";
    String ORDER_ERROR_1028_DESC = "预约单状态同步交易状态已同步LOCK更新失败";

    String ORDER_ERROR_1029 = "SC_1029";
    String ORDER_ERROR_1029_DESC = "预约单明细存在重复行";

    String ORDER_ERROR_1030 = "SC_1030";
    String ORDER_ERROR_1030_DESC = "保存预约单数据失败";

    String ORDER_ERROR_1031 = "SC_1031";
    String ORDER_ERROR_1031_DESC = "保存预约单明细数据失败";

    String ORDER_ERROR_1032 = "SC_1032";
    String ORDER_ERROR_1032_DESC = "更新预约单数据失败";

    String ORDER_ERROR_1033 = "SC_1033";
    String ORDER_ERROR_1033_DESC = "更新预约单明细数据失败";

    String ORDER_ERROR_1034 = "SC_1034";
    String ORDER_ERROR_1034_DESC = "需要包装时预约单状态必须为加工完成";

    String ORDER_ERROR_1035 = "SC_1035";
    String ORDER_ERROR_1035_DESC = "不需要包装时预约单状态必须为调拨入库";

    String ORDER_ERROR_1036 = "SC_1036";
    String ORDER_ERROR_1036_DESC = "查询预约单工厂对应的团购发货仓列表失败";

    String ORDER_ERROR_1037 = "SC_1037";
    String ORDER_ERROR_1037_DESC = "不符合二次锁定要求";
    
    String ORDER_ERROR_1038 = "SC_1038";
    String ORDER_ERROR_1038_DESC = "预约单状态不是交易审核已通过";

    String ORDER_ERROR_1039 = "SC_1039";
    String ORDER_ERROR_1039_DESC = "预约单信息更新失败";

    String ORDER_ERROR_1040 = "SC_1040";
    String ORDER_ERROR_1040_DESC = "预约单状态不是初始状态";

    String ORDER_ERROR_2001 = "SC_2001";
    String ORDER_ERROR_2001_DESC = "调入虚仓不存在";

    String ORDER_ERROR_2002 = "SC_2002";
    String ORDER_ERROR_2002_DESC = "实仓下该虚仓不存在";

    String ORDER_ERROR_2003 = "SC_2003";
    String ORDER_ERROR_2003_DESC = "虚仓调拨商品实际调拨数量不允许全部为0";

    String ORDER_ERROR_2004 = "SC_2004";
    String ORDER_ERROR_2004_DESC = "移库失败，请返回重新操作移库！";

    String ORDER_ERROR_2005 = "SC_2005";
    String ORDER_ERROR_2005_DESC = "移库失败，预约单不包含此转移商品";

    String ORDER_ERROR_3001 = "SC_3001";
    String ORDER_ERROR_3001_DESC = "调用预约单接口失败";

    String ORDER_ERROR_3002 = "SC_3002";
    String ORDER_ERROR_3002_DESC = "调用商品中心接口失败";

    String ORDER_ERROR_3003 = "SC_3003";
    String ORDER_ERROR_3003_DESC = "调用库存中心接口失败";

    String ORDER_ERROR_4001 = "SC_4001";
    String ORDER_ERROR_4001_DESC = "预约单号状态不是已发货";

    String ORDER_ERROR_4002 = "SC_4002";
    String ORDER_ERROR_4002_DESC = "退货数量大于下单数量";

    String ORDER_ERROR_4003 = "SC_4003";
    String ORDER_ERROR_4003_DESC = "商品不在退货范围";

    String ORDER_ERROR_4004 = "SC_4004";
    String ORDER_ERROR_4004_DESC = "退货明细中有重复商品";

    String ORDER_ERROR_4005 = "SC_4005";
    String ORDER_ERROR_4005_DESC = "团购退货仓不存在";

    String ORDER_ERROR_4006 = "SC_4006";
    String ORDER_ERROR_4006_DESC = "退货售后单号已存在";

    String ORDER_ERROR_4007 = "SC_4007";
    String ORDER_ERROR_4007_DESC = "退货单不存在";

    String ORDER_ERROR_4008 = "SC_4008";
    String ORDER_ERROR_4008_DESC = "退货单明细不存在";

    String ORDER_ERROR_4009 = "SC_4009";
    String ORDER_ERROR_4009_DESC = "退货入库通知明细中有重复商品";

    String ORDER_ERROR_4010 = "SC_4010";
    String ORDER_ERROR_4010_DESC = "商品不存在";

    String ORDER_ERROR_4011 = "SC_4011";
    String ORDER_ERROR_4011_DESC = "商品不是基本单位";

    String ORDER_ERROR_4012 = "SC_4012";
    String ORDER_ERROR_4012_DESC = "实际收货数量大于退货数量";

    String ORDER_ERROR_4013 = "SC_4013";
    String ORDER_ERROR_4013_DESC = "预约单状态为已发货，不能取消";

    String ORDER_ERROR_4014 = "SC_4014";
    String ORDER_ERROR_4014_DESC = "预约单状态已取消更新失败";

    String ORDER_ERROR_4015 = "SC_4015";
    String ORDER_ERROR_4015_DESC = "退货单状态已入库更新失败";

    String ORDER_ERROR_4016 = "SC_4016";
    String ORDER_ERROR_4016_DESC = "退货单明细实际收货数量更新失败";

    String ORDER_ERROR_4017 = "SC_4017";
    String ORDER_ERROR_4017_DESC = "保存邮件发送信息失败";

    String ORDER_ERROR_4018 = "SC_4018";
    String ORDER_ERROR_4018_DESC = "通知库存中心状态不是待通知";

    String ORDER_ERROR_4019 = "SC_4019";
    String ORDER_ERROR_4019_DESC = "通知交易中心状态不是待通知";

    String ORDER_ERROR_4020 = "SC_4020";
    String ORDER_ERROR_4020_DESC = "预约单更新后置单状态为取消失败";

    String ORDER_ERROR_5000 = "SC_5000";
    String ORDER_ERROR_5000_DESC = "调用外部接口失败";

    String ORDER_ERROR_5001 = "SC_5001";
    String ORDER_ERROR_5001_DESC = "调用商品中心接口失败";

    String ORDER_ERROR_5002 = "SC_5002";
    String ORDER_ERROR_5002_DESC = "调用库存中心接口失败";

    String ORDER_ERROR_5003 = "SC_5003";
    String ORDER_ERROR_5003_DESC = "调用交易中心接口失败";

    String ORDER_ERROR_5004 = "SC_5004";
    String ORDER_ERROR_5004_DESC = "调用基础数据中心接口失败";

    String ORDER_ERROR_5005 = "SC_5005";
    String ORDER_ERROR_5005_DESC = "调用用户中心接口失败";

    String ORDER_ERROR_5017 = "SC_5017";
    String ORDER_ERROR_5017_DESC = "该订单已取消，不能重复取消";

    // 门店补货
    String ORDER_ERROR_5100 = "SC_5100";
    String ORDER_ERROR_5100_DESC = "渠道编码有误";

    String ORDER_ERROR_5101 = "SC_5101";
    String ORDER_ERROR_5101_DESC = "行号不能为空";

    String ORDER_ERROR_5102 = "SC_5102";
    String ORDER_ERROR_5102_DESC = "入库仓库不存在";

    String ORDER_ERROR_5103 = "SC_5103";
    String ORDER_ERROR_5103_DESC = "出库仓库不存在";

    String ORDER_ERROR_5104 = "SC_5104";
    String ORDER_ERROR_5104_DESC = "可用库存不满足需求";

    String ORDER_ERROR_5105 = "SC_5105";
    String ORDER_ERROR_5105_DESC = "当前门店未匹配到对应的退货仓";

    String ORDER_ERROR_5106 = "SC_5106";
    String ORDER_ERROR_5106_DESC = "门店补货单不存在";

    String ORDER_ERROR_5107 = "SC_5107";
    String ORDER_ERROR_5107_DESC = "明细上是否删除状态不能为空";

    String ORDER_ERROR_5108 = "SC_5108";
    String ORDER_ERROR_5108_DESC = "该订单已经执行寻源分配无法进行修改";

    String ORDER_ERROR_5109 = "SC_5109";
    String ORDER_ERROR_5109_DESC = "该类型订单暂不支持修改";

    String ORDER_ERROR_5110 = "SC_5110";
    String ORDER_ERROR_5110_DESC = "只允许确认加盟的PO单";

    String ORDER_ERROR_5111 = "SC_5111";
    String ORDER_ERROR_5111_DESC = "确认单据失败,单据状态已经变化";

    String ORDER_ERROR_5112 = "SC_5112";
    String ORDER_ERROR_5112_DESC = "单据明细不存在";

    String ORDER_ERROR_5113 = "SC_5113";
    String ORDER_ERROR_5113_DESC = "取消单据失败,单据状态已经变化";

    String ORDER_ERROR_5114 = "SC_5114";
    String ORDER_ERROR_5114_DESC = "取消订单失败,当前订单已经推送至仓库";

    String ORDER_ERROR_5115 = "SC_5115";
    String ORDER_ERROR_5115_DESC = "库存中心取消订单失败";

    String ORDER_ERROR_5116 = "SC_5116";
    String ORDER_ERROR_5116_DESC = "苏南指定寻源,需要传入工厂";

    String ORDER_ERROR_5117 = "SC_5117";
    String ORDER_ERROR_5117_DESC = "当前有寻源分配任务正在执行,请稍后再试";

    String ORDER_ERROR_5118 = "SC_5118";
    String ORDER_ERROR_5118_DESC = "门店补货寻源失败,请稍后再试";

    String ORDER_ERROR_5119 = "SC_5119";
    String ORDER_ERROR_5119_DESC = "更新前置单据状态失败";

    String ORDER_ERROR_5120 = "SC_5120";
    String ORDER_ERROR_5120_DESC = "前置单与出入库单绑定关系不存在";

    String ORDER_ERROR_5121 = "SC_5121";
    String ORDER_ERROR_5121_DESC = "门店补货单推送交易中心状态失败";

    String ORDER_ERROR_5122 = "SC_5122";
    String ORDER_ERROR_5122_DESC = "前置单不存在";

    String ORDER_ERROR_5123 = "SC_5123";
    String ORDER_ERROR_5123_DESC = "后置单不存在";

    String ORDER_ERROR_5124 = "SC_5124";
    String ORDER_ERROR_5124_DESC = "单据类型不在大福配置的支持类型中";

    /**
     * 库存前置单迁移
     */
    String ORDER_ERROR_6001 = "SC_6001";
    String ORDER_ERROR_6001_DESC = "仓库调拨单已存在";

    String ORDER_ERROR_6002 = "SC_6002";
    String ORDER_ERROR_6002_DESC = "调拨类型不存在";

    String ORDER_ERROR_6003 = "SC_6003";
    String ORDER_ERROR_6003_DESC = "仓库调拨的出入库仓库不能相同";

    String ORDER_ERROR_6004 = "SC_6004";
    String ORDER_ERROR_6004_DESC = "入库仓库不存在";

    String ORDER_ERROR_6005 = "SC_6005";
    String ORDER_ERROR_6005_DESC = "出库仓库不存在";

    String ORDER_ERROR_6006 = "SC_6006";
    String ORDER_ERROR_6006_DESC = "内部调拨的工厂必须相同";

    String ORDER_ERROR_6007 = "SC_6007";
    String ORDER_ERROR_6007_DESC = "RDC调拨的工厂不能相同";

    String ORDER_ERROR_6008 = "SC_6008";
    String ORDER_ERROR_6008_DESC = "修改单据失败,单据状态已经变化";

    String ORDER_ERROR_6009 = "SC_6009";
    String ORDER_ERROR_6009_DESC = "没有查询到skuID";

    String ORDER_ERROR_6010 = "SC_6010";
    String ORDER_ERROR_6010_DESC = "没有查询到sku编号";

    String ORDER_ERROR_6011 = "SC_6011";
    String ORDER_ERROR_6011_DESC = "不允许两个非中台仓库间发生调拨";

    String ORDER_ERROR_6012 = "SC_6012";
    String ORDER_ERROR_6012_DESC = "确认单据失败,单据状态已经变化";

    String ORDER_ERROR_6013 = "SC_6013";
    String ORDER_ERROR_6013_DESC = "没有相关sku单位";

    String ORDER_ERROR_6014 = "SC_6014";
    String ORDER_ERROR_6014_DESC = "可用库存不满足需求";

    String ORDER_ERROR_6015 = "SC_6015";
    String ORDER_ERROR_6015_DESC = "取消单据失败,单据状态已经变化";

    String ORDER_ERROR_6016 = "SC_6016";
    String ORDER_ERROR_6016_DESC = "取消订单失败,当前订单已经推送至仓库";

    String ORDER_ERROR_6017 = "SC_6017";
    String ORDER_ERROR_6017_DESC = "下发订单给sap，sap接口调用失败";

    String ORDER_ERROR_6018 = "SC_6018";
    String ORDER_ERROR_6018_DESC = "仓库调拨单单据状态更新失败";

    String ORDER_ERROR_6019 = "SC_6019";
    String ORDER_ERROR_6019_DESC = "每条行记录的调拨类型必填且必须保持一致";

    String ORDER_ERROR_6020 = "SC_6020";
    String ORDER_ERROR_6020_DESC = "是否退货类型不正确";

    String ORDER_ERROR_6021 = "SC_6021";
    String ORDER_ERROR_6021_DESC = "是否质量调拨类型不正确";

    String ORDER_ERROR_6022 = "SC_6022";
    String ORDER_ERROR_6022_DESC = "格式不正确";

    String ORDER_ERROR_6023 = "SC_6023";
    String ORDER_ERROR_6023_DESC = "仓库调拨单不存在";

    String ORDER_ERROR_6024 = "SC_6024";
    String ORDER_ERROR_6024_DESC = "导出的开始时间不能为空";

    String ORDER_ERROR_6025 = "SC_6025";
    String ORDER_ERROR_6025_DESC = "导出的开始时间必须在15天内";

    String ORDER_ERROR_6026 = "SC_6026";
    String ORDER_ERROR_6026_DESC = "出入库单不存在";

    String ORDER_ERROR_6027 = "SC_6027";
    String ORDER_ERROR_6027_DESC = "同步库存中心状态不是待同步";

    String ORDER_ERROR_6028 = "SC_6028";
    String ORDER_ERROR_6028_DESC = "调入门店不存在";

    String ORDER_ERROR_6029 = "SC_6029";
    String ORDER_ERROR_6029_DESC = "调出门店不存在";

    String ORDER_ERROR_6030 = "SC_6030";
    String ORDER_ERROR_6030_DESC = "TMS派车单号不能为空";

    String ORDER_ERROR_6031 = "SC_6031";
    String ORDER_ERROR_6031_DESC = "出入库单派车状态更新失败";

    String ORDER_ERROR_6032 = "SC_6032";
    String ORDER_ERROR_6032_DESC = "出入库单取消失败";

    String ORDER_ERROR_6033 = "SC_6033";
    String ORDER_ERROR_6033_DESC = "仓库调拨出库库存中心回调异常";

    String ORDER_ERROR_6034 = "SC_6034";
    String ORDER_ERROR_6034_DESC = "仓库调拨入库库存中心回调异常";

    String ORDER_ERROR_6035 = "SC_6035";
    String ORDER_ERROR_6035_DESC = "批次信息不能为空";

    String ORDER_ERROR_6036 = "SC_6036";
    String ORDER_ERROR_6036_DESC = "同步到派车系统失败";

    String ORDER_ERROR_6037 = "SC_6037";
    String ORDER_ERROR_6037_DESC = "同步到派车系统-前置单类型错误";

    String ORDER_ERROR_6038 = "SC_6038";
    String ORDER_ERROR_6038_DESC = "当前订单中有预下市物料";

    String ORDER_ERROR_6039 = "SC_6039";
    String ORDER_ERROR_6039_DESC = "出库仓不受中台管理, 不支持移库";

    String ORDER_ERROR_6040 = "SC_6040";
    String ORDER_ERROR_6040_DESC = "入库单不存在";
    
    String ORDER_ERROR_6041 = "SC_6041";
    String ORDER_ERROR_6041_DESC = "出库单状态为已出库，不能取消";
    
    String ORDER_ERROR_6042 = "SC_6042";
    String ORDER_ERROR_6042_DESC = "保存预约单状态变更日志失败";
    
    String ORDER_ERROR_6043 = "SC_6043";
    String ORDER_ERROR_6043_DESC = "仓库不存在";
    
    String ORDER_ERROR_6044 = "SC_6044";
    String ORDER_ERROR_6044_DESC = "出/入库单虚仓编码必须为空";
    
    //门店试吃单
    String ORDER_ERROR_7001 = "SC_7001";
    String ORDER_ERROR_7001_DESC = "试吃单无明细";
    String ORDER_ERROR_7002 = "SC_7002";
    String ORDER_ERROR_7002_DESC = "门店试吃出库单-推库存失败";

    //门店盘点
    String ORDER_ERROR_7201 = "SC_7201";
    String ORDER_ERROR_7201_DESC = "门店盘点系统处理异常";
    String ORDER_ERROR_7202 = "SC_7202";
    String ORDER_ERROR_7202_DESC = "没有相关门店仓信息";
    String STOCK_ERROR_7203 = "SC_7203";
    String STOCK_ERROR_7203_DESC = "盘点下发sap失败！";
    String STOCK_ERROR_7204 = "SC_7204";
    String STOCK_ERROR_7204_DESC = "盘点单不存在！";

    //门店零售
    String ORDER_ERROR_7402 = "SC_7402";
    String ORDER_ERROR_7402_DESC = "门店零售单保存失败";
    String ORDER_ERROR_7403 = "SC_7403";
    String ORDER_ERROR_7403_DESC = "库存中心-取消门店零售出库单-异常";
    String ORDER_ERROR_7404 = "SC_7404";
    String ORDER_ERROR_7404_DESC = "门店零售单取消失败";

    //门店零售退货
    String ORDER_ERROR_7502 = "SC_7502";
    String ORDER_ERROR_7502_DESC = "门店零售退货单保存失败";
    String ORDER_ERROR_7503 = "SC_7503";
    String ORDER_ERROR_7503_DESC = "库存中心-取消门店零售退货入库单-异常";

    //门店报废
    String ORDER_ERROR_7101 = "SC_7101";
    String ORDER_ERROR_7101_DESC = "前置单据不存在";

    //门店调拨
    String ORDER_ERROR_7301 = "SC_7301";
    String ORDER_ERROR_7301_DESC = "同一门店不能调拨";

    String ORDER_ERROR_7302 = "SC_7302";
    String ORDER_ERROR_7302_DESC = "当前门店不存在";

    String ORDER_ERROR_7303 = "SC_7303";
    String ORDER_ERROR_7303_DESC = "当前门店不能跨组织调拨";

    String ORDER_ERROR_7304 = "SC_7304";
    String ORDER_ERROR_7304_DESC = "后置单状态更新失败";

    String ORDER_ERROR_7305 = "SC_7305";
    String ORDER_ERROR_7305_DESC = "门店调拨单保存失败";

    String ORDER_ERROR_7306 = "SC_7306";
    String ORDER_ERROR_7306_DESC = "退货单保存失败";

    String ORDER_ERROR_7307 = "SC_7307";
    String ORDER_ERROR_7307_DESC = "门店调拨单不存在";

    //包装需求单
    String ORDER_ERROR_7601 = "SC_7601";
    String ORDER_ERROR_7601_DESC = "预约单的状态不等于拨审核通过";
    String ORDER_ERROR_7602 = "SC_7602";
    String ORDER_ERROR_7602_DESC = "预约单的创建调拨不为等于（已创建）";
    String ORDER_ERROR_7603 = "SC_7603";
    String ORDER_ERROR_7603_DESC = "预约单请先配置需要包装";
    String ORDER_ERROR_7604 = "SC_7604";
    String ORDER_ERROR_7604_DESC = "包装系统同步SO预约单状态失败";
    String ORDER_ERROR_7605 = "SC_7605";
    String ORDER_ERROR_7605_DESC = "包装系统同步需求单状态失败";
    String ORDER_ERROR_7606 = "SC_7606";
    String ORDER_ERROR_7606_DESC = "页面修改需求单状态为已确认失败";
    String ORDER_ERROR_7607 = "SC_7607";
    String ORDER_ERROR_7607_DESC = "需求单单据状态为其他状态,只有初始状态才能修改为已确认状态";
    String ORDER_ERROR_7608 = "SC_7608";
    String ORDER_ERROR_7608_DESC = "包装系统同步-需求单编号不存在";
    String ORDER_ERROR_7609 = "SC_7609";
    String ORDER_ERROR_7609_DESC = "该销售单号已绑定需求单";
    String ORDER_ERROR_7610 = "SC_7610";
    String ORDER_ERROR_7610_DESC = "预约单号不存在";
    String ORDER_ERROR_7611 = "SC_7611";
    String ORDER_ERROR_7611_DESC = "渠道编码不存在";
    String ORDER_ERROR_7612 = "SC_7612";
    String ORDER_ERROR_7612_DESC = "销售号不存在";
    String ORDER_ERROR_7613 = "SC_7613";
    String ORDER_ERROR_7613_DESC = "SO构建需求单-包装仓不存在";
    String ORDER_ERROR_7614 = "SC_7614";
    String ORDER_ERROR_7614_DESC = "SO构建需求单-领料仓不存在";
    String ORDER_ERROR_7615 = "SC_7615";
    String ORDER_ERROR_7615_DESC = "构建包装系统数据-包装仓不存在";
    String ORDER_ERROR_7616 = "SC_7616";
    String ORDER_ERROR_7616_DESC = "构建包装系统数据-领料仓不存在";
    String ORDER_ERROR_7617 = "SC_7617";
    String ORDER_ERROR_7617_DESC = "SO构建需求单-预约单明细不存在";
    String ORDER_ERROR_7618 = "SC_7618";
    String ORDER_ERROR_7618_DESC = "包装需求单不存在";
    String ORDER_ERROR_7619 = "SC_7619";
    String ORDER_ERROR_7619_DESC = "包装需求单状态不为“初始”“已确认”，不允许取消";
    String ORDER_ERROR_7620 = "SC_7620";
    String ORDER_ERROR_7620_DESC = "包装系统不允许取消包装需求单";
    String ORDER_ERROR_7621 = "SC_7621";
    String ORDER_ERROR_7621_DESC = "包装系统返回异常";
    String ORDER_ERROR_7622 = "SC_7622";
    String ORDER_ERROR_7622_DESC = "包装类型有误";
    String ORDER_ERROR_7623 = "SC_7623";
    String ORDER_ERROR_7623_DESC = "反拆类型不能进行导入";
    String ORDER_ERROR_7624 = "SC_7624";
    String ORDER_ERROR_7624_DESC = "包装仓库不存在";
    String ORDER_ERROR_7626 = "SC_7626";
    String ORDER_ERROR_7626_DESC = "领料仓库不存在";
    String ORDER_ERROR_7627 = "SC_7627";
    String ORDER_ERROR_7627_DESC = "渠道编码不能为空";
    String ORDER_ERROR_7628 = "SC_7628";
    String ORDER_ERROR_7628_DESC = "包装类型不能为空";
    String ORDER_ERROR_7629 = "SC_7629";
    String ORDER_ERROR_7629_DESC = "包装仓库不能为空";
    String ORDER_ERROR_7630 = "SC_7610";
    String ORDER_ERROR_7630_DESC = "领料仓库不能为空";
    String ORDER_ERROR_7631 = "SC_7631";
    String ORDER_ERROR_7631_DESC = "需求日期不能为空";
    String ORDER_ERROR_7632 = "SC_7632";
    String ORDER_ERROR_7632_DESC = "商品编号不能为空";
    String ORDER_ERROR_7633 = "SC_7633";
    String ORDER_ERROR_7633_DESC = "需求数量不能为空";
    String ORDER_ERROR_7634 = "SC_7634";
    String ORDER_ERROR_7634_DESC = "包装类型为自定义组合时自定义组合码不能为空";
    String ORDER_ERROR_7635 = "SC_7635";
    String ORDER_ERROR_7635_DESC = "包装类型为自定义组合时组合份数不能为空";
    String ORDER_ERROR_7636 = "SC_7636";
    String ORDER_ERROR_7636_DESC = "包装系统同步，需求单为其他状态,不能修改为包装完成状态";
    String ORDER_ERROR_7637 = "SC_7637";
    String ORDER_ERROR_7637_DESC = "构建包装系统数据，仓库类型不属于包装仓库";
    String ORDER_ERROR_7638 = "SC_7638";
    String ORDER_ERROR_7638_DESC = "的组件需求数量计算有误";
    String ORDER_ERROR_7639 = "SC_7638";
    String ORDER_ERROR_7639_DESC = "的组件BOM数量计算有误";
    String ORDER_ERROR_7640 = "SC_7640";
    String ORDER_ERROR_7640_DESC = "已确认不能再确认了";


    String ORDER_ERROR_7671 = "SC_7671";
    String ORDER_ERROR_7671_DESC = "部分商品不存在";

    String ORDER_ERROR_7672 = "SC_7672";
    String ORDER_ERROR_7672_DESC = "查询商品失败";

    String ORDER_ERROR_7661 = "SC_7661";
    String ORDER_ERROR_7661_DESC = "包装调拨明细为空";

    String ORDER_ERROR_7663 = "SC_7663";
    String ORDER_ERROR_7663_DESC = "需求单明细原料不包含需求调拨物料";

    String ORDER_ERROR_7664 = "SC_7664";
    String ORDER_ERROR_7664_DESC = "需求单领料状态更新失败";

    String ORDER_ERROR_7665 = "SC_7665";
    String ORDER_ERROR_7665_DESC = "需求单明细原料更新实际移库数量失败";

    String ORDER_ERROR_7666 = "SC_7666";
    String ORDER_ERROR_7666_DESC = "需求单状态不为已确认或部分包装，创建调拨单失败";

    //包装任务完成单
    String ORDER_ERROR_7701 = "SC_7701";
    String ORDER_ERROR_7701_DESC = "包装任务完成清单-包装需求单不存在";
    String ORDER_ERROR_7702 = "SC_7702";
    String ORDER_ERROR_7702_DESC = "包装需求单明细不存在";
    String ORDER_ERROR_7703 = "SC_7703";
    String ORDER_ERROR_7703_DESC = "任务完成操作单成品编码不存在当前需求单中";
    String ORDER_ERROR_7704 = "SC_7704";
    String ORDER_ERROR_7704_DESC = "需求单明细对应的组件为空";
    String ORDER_ERROR_7705 = "SC_7705";
    String ORDER_ERROR_7705_DESC = "任务完成清单-同步库存入库单异常";
    String ORDER_ERROR_7706 = "SC_7706";
    String ORDER_ERROR_7706_DESC = "包装需求单状态异常，不为“已确认”“部分包装”";
    String ORDER_ERROR_7707 = "SC_7707";
    String ORDER_ERROR_7707_DESC = "任务完成操作单-包装仓不存在";
    String ORDER_ERROR_7708 = "SC_7708";
    String ORDER_ERROR_7708_DESC = "包装任务完成清单-包装数量错误";
    String ORDER_ERROR_7709 = "SC_7709";
    String ORDER_ERROR_7709_DESC = "包装任务完成清单为空";

    //出入库冲销
    String ORDER_ERROR_8001 = "SC_8001";
    String ORDER_ERROR_8001_DESC = "冲销单据编号不存在";
    String ORDER_ERROR_8002 = "SC_8002";
    String ORDER_ERROR_8002_DESC = "冲销单修改失败";
    String ORDER_ERROR_8003 = "SC_8003";
    String ORDER_ERROR_8003_DESC = "入库单冲销类型时，收货单据编号不能为空";
    String ORDER_ERROR_8004 = "SC_8004";
    String ORDER_ERROR_8004_DESC = "入库单冲销类型时，收货单据编号已存在，请重新选择";
    String ORDER_ERROR_8005 = "SC_8005";
    String ORDER_ERROR_8005_DESC = "冲销单单据类型和选择的内容单据不符合，比如出库类型，选择了入库类型的单据";
    String ORDER_ERROR_8006 = "SC_8006";
    String ORDER_ERROR_8006_DESC = "该单据类型不在冲销单据类型范围内";
    String ORDER_ERROR_8007 = "SC_8007";
    String ORDER_ERROR_8007_DESC = "需求单单据状态为其他状态,只有已新建状态才能修改为已确认";
    String ORDER_ERROR_8008 = "SC_8008";
    String ORDER_ERROR_8008_DESC = "冲销单构建出库,仓库不存在";
    String ORDER_ERROR_8009 = "SC_8009";
    String ORDER_ERROR_8009_DESC = "冲销单据明细不存在";
    String ORDER_ERROR_8010 = "SC_8010";
    String ORDER_ERROR_8010_DESC = "该单据已经存在，不能重复创建";

    String ORDER_ERROR_8017 = "SC_8017";
    String ORDER_ERROR_8017_DESC = "冲销单据不存在";

    String ORDER_ERROR_8018 = "SC_8018";
    String ORDER_ERROR_8018_DESC = "冲销单状态不为“已新建”，不允许取消";

    String ORDER_ERROR_8019 = "SC_8019";
    String ORDER_ERROR_8019_DESC = "冲销单状态更新失败";

    //门店退货
    String ORDER_ERROR_7801 = "SC_7801";
    String ORDER_ERROR_7801_DESC = "门店退货单不存在";

    String ORDER_ERROR_7802 = "SC_7802";
    String ORDER_ERROR_7802_DESC = "线下退货仓不存在";

    String ORDER_ERROR_7803 = "SC_7803";
    String ORDER_ERROR_7803_DESC = "门店仓不存在";

    String ORDER_ERROR_7804 = "SC_7804";
    String ORDER_ERROR_7804_DESC = "指定退货仓不存在";
    String ORDER_ERROR_7805 = "SC_7805";
    String ORDER_ERROR_7805_DESC = "门店退货-更新推送交易中心状态";

    String ORDER_ERROR_9066 = "SC_9066";
    String ORDER_ERROR_9066_DESC = "退货单明细不存在";

    String ORDER_ERROR_9067 = "SC_9067";
    String ORDER_ERROR_9067_DESC = "退货单已入库";

    String ORDER_ERROR_9900 = "SC_9900";
    String ORDER_ERROR_9900_DESC = "MQ消息发送异常";
    String ORDER_ERROR_9901 = "SC_9901";
    String ORDER_ERROR_9901_DESC = "MQ消息发送状态错误";
    String ORDER_ERROR_9902 = "SC_9902";
    String ORDER_ERROR_9902_DESC = "库存取消出入库单异常";

    String STOCK_ERROR_1002 = "SC_1002";
    String STOCK_ERROR_1002_DESC = "参数有误";
}
