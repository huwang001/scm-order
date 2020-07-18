//门店零售详情
lyfdefine(function () {
    return ({
        data: function () {
            return {
                data: [],
                visible: false
            }
        },
        methods: {
            init: function init(recordId, outRecordCode) {
                var _this = this;
                this.visible = true;
                this.$http.get('/scm-order-admin/order/v1/salesReturn/queryDetail/' + recordId
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
                        <el-col :span="3"><div style="font-weight: bold">门店退货单详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="6">
                            <el-form-item label="退入仓库：">
                                {{data.recordCode}}
                            </el-form-item>
                        </el-col>
                         <el-col  :span="6">
                            <el-form-item label="退入仓库地址：" >
                                 {{data.realWarehouseAddress}}
                            </el-form-item>
                         </el-col>
                        <el-col  :span="6">
                        <el-form-item label="订单编号："  >
                            {{data.outRecordCode}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="6">
                        <el-form-item label="买家账户："  >
                            {{data.userCode}}
                        </el-form-item>
                        </el-col>
                        <el-col  :span="6">
                        <el-form-item label="客户联系电话：" >
                            {{data.mobile}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="6">
                         <el-form-item label="申请日期：">
                            {{data.createTime}}
                        </el-form-item>
                         </el-col>
                          <el-col  :span="6">
                            <el-form-item label="订单状态：" >
                                 {{data.recordStatusName}}
                            </el-form-item>
                         </el-col>
                         <el-col  :span="6">
                        <el-form-item label="单据类型：" >
                            {{data.recordTypeName}}
                        </el-form-item>
                         </el-col>
                           <el-col  :span="12">
                        <el-form-item label="退货原因：" >
                            {{data.reason}}
                        </el-form-item>
                         </el-col>
                        
                        
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        
                    </div>
                 
                    <el-table size="mini" border :data="data.details" max-height="400">
                      
                      <el-table-column  label="商品ID"  prop="skuId">
                            
                        </el-table-column>
                        <el-table-column prop="skuCode" label="商品编码" 
                          header-align="center"
                          align="center">
                       
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称"  
                          header-align="center"
                          align="center">
                        
                        </el-table-column>
                        
                          <el-table-column prop="categoryCode" label="商品类别编码"  
                          header-align="center"
                          align="center">
                        
                        </el-table-column>
                        
                          <el-table-column prop="categoryName" label="商品类别名称"  
                          header-align="center"
                          align="center">
                        
                        </el-table-column>
                    
                        <el-table-column prop="unit" label="主单位"   
                        header-align="center"
                          align="center">
                        </el-table-column>
                        
                            <el-table-column prop="planQty" label="订单数量"  
                         header-align="center"
                          align="center">
                        
                        </el-table-column>
                        
                        <el-table-column prop="actualQty" label="入库数量"    
                        header-align="center"
                          align="center">
                        </el-table-column>
                        
                        <el-table-column  label="到仓时间"  
                         header-align="center"
                          align="center"
                          prop="createTime">
                        </el-table-column>
                       
                    </el-table>
            </el-dialog>
       </div>
        `
    });
});