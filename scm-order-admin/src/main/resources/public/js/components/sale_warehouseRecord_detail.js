//门店零售详情
lyfdefine(function () {
    return ({
        data: function () {
            return {
                data: [],
                visible: false,
                batchVisible: false,
                batchInfo: []
            }
        },

        methods: {
            init: function init(recordId, outRecordCode) {
                var _this = this;
                this.visible = true;
                this.$http.get('/scm-order-admin/order/v1/warehouse_sale/querySaleWarehouseRecordInfo/' + recordId
                ).then(function (_ref) {
                    var data = _ref.data.data;
                    if (data) {
                        console.log(data);
                        _this.data = data;
                        _this.data.outRecordCode = _this.formatOrderCode(outRecordCode);
                    } else {
                        _this.data = {};
                    }

                });
            },
            formatOrderCode: function formatOrderCode(value) {
                if (value) {
                    return value.join(",");
                }
                return "";
            },
            seeBatchInfo: function seeBatchInfo(skuId) {
                this.batchVisible = true;
                var _this = this;
                this.data.details.forEach((element, index) => {
                    if (element.skuId === skuId) {
                        _this.batchInfo = element.batchStockChangeFlowList;
                    }
                });
            }
        },
        template: `
       <div>
          <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="70%"
           style="font-size: 14px"
            :visible.sync="visible">
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="5"><div style="font-weight: bold">门店销售单详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="8">
                            <el-form-item label="发货单号：">
                                {{data.recordCode}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                        <el-form-item label="订单编号："  >
                            {{data.outRecordCode}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="8">
                        <el-form-item label="买家账户："  >
                            {{data.userCode}}
                        </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                        <el-form-item label="手机号：" >
                            {{data.mobile}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="8">
                         <el-form-item label="来源渠道：" >
                            {{data.channelCodeName}}
                        </el-form-item>
                         </el-col>
                         <el-col  :span="8">
                        <el-form-item label="单据类型：" >
                          {{data.recordTypeName}}
                        </el-form-item>
                         </el-col>
                         <el-col  :span="8">
                            <el-form-item label="地址信息：" >
                                 {{data.realWarehouseAddress}}
                            </el-form-item>
                         </el-col>
                        
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        
                    </div>  
                 
                    <el-table size="mini" border :data="data.details" max-height="400">
                      
                      <el-table-column  label="商品ID"  prop="skuId" 
                       header-align="center"
                          align="center">
                            
                        </el-table-column>
                        <el-table-column prop="skuCode" label="商品编码" 
                          header-align="center"
                          align="center">
                       
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称"  
                          header-align="center"
                          align="center">
                        
                        </el-table-column>
                        <el-table-column prop="planQty" label="数量"  
                         header-align="center"
                          align="center">
                        
                        </el-table-column>
                        <el-table-column prop="unit" label="单位"   
                        header-align="center"
                          align="center">
                        </el-table-column>
                        <el-table-column prop="realWarehouseName" label="转出仓库"    
                        header-align="center"
                          align="center">
                        </el-table-column>
                         <el-table-column  label="批次信息"   
                          header-align="center"
                          align="center">
                             <template slot-scope="scope">
                              <el-button type="text"  size="small" @click="seeBatchInfo(scope.row.skuId)">批次信息</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column  label="状态"  
                         header-align="center"
                          align="center">
                          <template scope="scope">
                            <span>{{data.recordStatusName}}</span>
                        </template>
                        </el-table-column>
                       
                    </el-table>
            </el-dialog>
          <el-dialog
            :title="'批次信息'"
            :close-on-click-modal="false"
            width="20%"
           style="font-size: 14px"
            :visible.sync="batchVisible">
                 
                <el-table size="mini" border :data="batchInfo" >
                        <el-table-column   header-align="center"
                        align="center" label="批次号" prop="batchCode">
                            
                        </el-table-column>
                        <el-table-column   header-align="center"
                           align="center" label="批次数量" prop="skuQty">
                       
                        </el-table-column>
                       
                    </el-table>
                </el-dialog>
       </div>
        `
    });
});