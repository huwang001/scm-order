lyfdefine(function () {
    return ({
        data: function data() {
            return {
                visible: false,
                type:3,
                multipleSelection: [],
                dataListLoading:false,
                recordsAllocInfoList: [],
                dataList:[],//请求的原始数据，包含有sku信息
                recordsAllocationTip:''
            };

        },

        methods: {
            init: function (multipleSelection  , type) {
                this.type = type;
                this.multipleSelection=multipleSelection;
                this.visible = true;
                let ids=[];
                this.multipleSelection.forEach(function (value) {
                    ids.push(value.id);
                });
                this.queryAllocConfigInfoByRecords(ids ,type);
            },



            queryAllocConfigInfoByRecords: function queryAllocConfigInfoByRecords(ids ,type ) {
                var _this = this;
                _this.recordsAllocInfoList = [];
                this.dataListLoading = true;
                var url ;
                if (type === 1) {
                    //调拨出库
                    url = '/scm-order-admin/order/v1/whAllocation/queryOutAllocConfigInfoByRecords';
                    _this.recordsAllocationTip = "单据级虚拟仓出库批量指定" ;
                } else if (type === 2) {
                    //调拨入库
                    url = '/scm-order-admin/order/v1/whAllocation/queryAllocConfigInfoByRecords';
                    _this.recordsAllocationTip = "单据级虚拟仓入库批量指定" ;
                } else {
                    //外采入库
                	//（需修改）
                    url = '/scm-order-admin/order/v1/purchaseOrder/queryAllocConfigInfoByRecords';
                    _this.recordsAllocationTip = "单据级虚拟仓入库批量指定" ;
                }
                this.$http.post( url , ids
                ).then(function (_ref) {
                    var dataList = _ref.data.data;
                    var res = [];
                    
                    if (dataList) {
                        _this.dataList = dataList;
                        for (let k = 0; k < dataList.length; k++) {
                            let data = dataList[k];

                            let realId = data.realWarehouseId;
                            let realName = data.realWarehouseName;
                            let realCode = data.realWarehouseCode;
                            if (type === 1) {
                                realId = data.outWarehouseId;
                                realName = data.outWarehouseName;
                                realCode = data.outWarehouseCode;
                            } else if (type === 2) {
                                realId = data.inWarehouseId;
                                realName = data.inWarehouseName;
                                realCode = data.inWarehouseCode;
                            }
                            var skuDetail;
                            if (data.skuDetails) {
                                skuDetail = data.skuDetails[0];
                            } else {
                                skuDetail = data.frontRecordDetails[0];
                            }
                            if (skuDetail.vmSyncRate) {
                                var vmSyncRate = skuDetail.vmSyncRate;
                                for (var j = 0; j < vmSyncRate.length; j++) {
                                    var vmSyncRateItem = vmSyncRate[j];
                                    res.push({
                                        "recordId": data.recordId,
                                        "recordCode": data.recordCode,
                                        "realWarehouseId": realId,
                                        "realWarehouseName": realName,
                                        "realWarehouseCode": realCode,
                                        "virtualWarehouseId": vmSyncRateItem.id,
                                        "virtualWarehouseName": vmSyncRateItem.virtualWarehouseName,
                                        "virtualWarehouseCode": vmSyncRateItem.virtualWarehouseCode,
                                        "syncRate": vmSyncRateItem.syncRate,
                                        "configSyncRate": "",
                                        "isEdit": (type !== 1 && data.recordStatus !== 2) || (type === 1 && data.recordStatus === 0)
                                    });
                                }
                            }

                        }
                        _this.recordsAllocInfoList = res;
                        console.log(_this.recordsAllocInfoList)

                    } else {
                        _this.recordsAllocInfoList = [];
                        _this.dataList = [];
                    }
                    _this.dataListLoading = false;
                });
            },

            groupBy: function groupBy(array, f) {
                const groups = {};
                array.forEach(function (o) { //注意这里必须是forEach 大写
                    const group = JSON.stringify(f(o));
                    groups[group] = groups[group] || [];
                    groups[group].push(o);
                });
                return Object.keys(groups).map(function (group) {
                    return groups[group];
                });
            },
            submitAllocConfig: function submitAllocConfig() {
                var list = this.recordsAllocInfoList;
                var needInsert = [];
                var recordCode = '';
                var defaultSync = 0;
                var configSync = 0;
                var config = false;
                var _this = this;
                //判断用户填的比例是否正整数，以及是否全配置或全不配置 以及是否=默认比例之和的校验
                const re = /^[1-9]*[0-9]$/;
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    if (item.isEdit) {
                        if (!(!item.configSyncRate || Number(item.configSyncRate) && item.configSyncRate >= 0 && item.configSyncRate <= 100)) {
                            //用户有输入且输入不合法
                            if (item.configSyncRate !== '0') {
                                _this.$message({
                                    message: "请输入[0-100]之间的正整数"
                                });
                                return;
                            }
                        }
                        if (item.configSyncRate && item.configSyncRate < 100) {
                            const rsCheck = re.test(item.configSyncRate);
                            if (!rsCheck) {
                                _this.$message({
                                    message: "请输入[1-100]之间的整数"
                                });
                                return;
                            }
                        }
                        if (recordCode === item.recordCode) {
                            defaultSync = defaultSync + item.syncRate;
                            if ((item.configSyncRate || item.configSyncRate === 0) && config) {
                                //该单据第一行配了，改行也配了正常，需要校验总和
                                configSync = configSync + item.configSyncRate * 1;
                            } else if (!(item.configSyncRate || item.configSyncRate === 0) && !config) {
                                //该sku第一行没配，该行也没配，正常，不需要校验总和
                            } else {
                                //不正常
                                _this.$message({
                                    message: "单据:" + recordCode + "要么全配，要么全不配"
                                });
                                return;
                            }
                        } else {
                            if (config && defaultSync !== configSync) {
                                //默认的跟配置的之和不相等，则提示用户，不进入到后台
                                _this.$message({
                                    message: "单据:" + recordCode + "配置的分配比例跟默认比例之和不相等!"
                                });
                                return;
                            }
                            recordCode = item.recordCode;
                            defaultSync = item.syncRate;
                            if ((item.configSyncRate || item.configSyncRate === 0)) {
                                //某个sku的第一行用户配置了，则后面的sku都需要配，否则检验不通过
                                config = true;
                                configSync = item.configSyncRate * 1;
                            } else {
                                config = false;
                            }
                        }
                        if (config && i === list.length - 1) {
                            //最后一个sku需要额外处理
                            if (defaultSync !== configSync) {
                                //默认的跟配置的之和不相等，则提示用户，不进入到后台
                                _this.$message({
                                    message: "单据:" + recordCode + "配置的分配比例跟默认比例之和不相等!"
                                });
                                return;
                            }
                        }
                        if (config) {
                            item.allotType = 1;
                            //用户配置了，才需要入库
                            needInsert.push(item);
                        }
                    }
                }

                if (needInsert.length > 0) {
                    var list = [];
                    //sku填充
                    //1、将array按recordCode分组
                    let map = this.groupBy(needInsert , function (item) {
                        return [item.recordCode];
                    });
                    //遍历所有单据
                    for (var i in map) {
                        //2、遍历虚仓
                        map[i].forEach(function (value) {
                            _this.dataList.forEach(function (item) {
                                if (item.recordCode === value.recordCode) {
                                    //3、遍历原始数据的所有的sku,某个单据只要配了虚仓 就适用所有的sku
                                    var skuDetail;
                                    if (item.skuDetails) {
                                        skuDetail = item.skuDetails;
                                    } else {
                                        skuDetail = item.frontRecordDetails;
                                    }
                                    skuDetail.forEach(function (sku) {
                                        let temp = {};
                                        temp.skuId = sku.skuId;
                                        temp.recordId = value.recordId;
                                        temp.recordCode = value.recordCode;
                                        temp.realWarehouseId = value.realWarehouseId;
                                        temp.virtualWarehouseId = value.virtualWarehouseId;
                                        temp.configSyncRate = value.configSyncRate;
                                        temp.allotType = value.allotType;
                                        list.push(temp);
                                    });
                                }
                            });
                        });
                    }

                    //发送请求保存
                    _this.$http.post('/scm-order-admin/order/v1/whAllocation/allotConfig', list
                    ).then(function (_ref2) {
                        var data = _ref2.data;
                        _this.$message({
                            message: 0 == data.code ? '操作成功' : '操作失败',
                            type: 0 == data.code ? 'success' : 'error',
                            duration: 1500,
                            onClose: function onClose() {
                                _this.visible = false;
                            }
                        });
                        _this.visible = false;

                    });
                } else {
                    _this.visible = false;
                }
                console.log(needInsert);
            }
        },

        template: /*html*/`
  <el-dialog

          :title="recordsAllocationTip"
          width="80%"
         :visible.sync="visible">
             
             
           <el-table   border    max-height="450"
           v-loading="dataListLoading" element-loading-text="拼命加载中..."
            :data="recordsAllocInfoList">
            
            <el-table-column
                prop="recordCode"
                header-align="center"
                align="center"
                label="单据号">
            </el-table-column>  
            <el-table-column
                prop="realWarehouseCode"
                header-align="center"
                align="center"
                :label="type == 1 ? '出库仓库编号' : '入库仓库编号'">
              </el-table-column>
         
              <el-table-column
                prop="realWarehouseName"
                header-align="center"
                align="center"
                :label="type == 1 ? '出库仓库名称' : '入库仓库名称'">
              </el-table-column>
              
             
                
                 <el-table-column
                prop="virtualWarehouseCode"
                header-align="center"
                align="center"
                 :label="type == 1 ? '出库虚仓编号' : '入库虚仓编号'">
                </el-table-column>
                
                <el-table-column
                prop="virtualWarehouseName"
                header-align="center"
                align="center"
                 :label="type == 1 ? '出库虚仓名称' : '入库虚仓名称'">
                </el-table-column>
                
                 <el-table-column
                prop="syncRate"
                header-align="center"
                align="center"
                label="默认比例">
                </el-table-column>
                
                <el-table-column
                    prop="configAbsolute"
                    header-align="center"
                    align="center"
                    :label="type == 1 ? '单据比例出库分配' : '单据比例入库分配'">
                    
                      <template scope="scope">
                      <div >
                        <el-input size="small" :disabled="scope.row.isEdit?false:true" v-model="scope.row.configSyncRate" placeholder="请输入分配比例">
                        </el-input>
                      </div>
                    </template>
                    
                </el-table-column>
                
              
          </el-table>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button  type="primary" @click="submitAllocConfig()">确 定</el-button>
      </div>
   </el-dialog>
    `
    });
});
