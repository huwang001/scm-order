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
            init: function init(recordId) {
                console.log(recordId)
                var _this = this;
                this.visible = true;
                this.$http({
                    url: '/scm-order-admin/order/v1/warehouse_sale_tob/getWarehouseSaleTobDetail/',
                    method: 'post',
                    params: _this.$http.adornParams({
                        "warehouseRecordId": recordId,
                    })
                }).then(function (_ref) {
                    var data = _ref.data.data;
                    if (data) {
                        console.log(data);
                        _this.data = data;
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
            getIndex: function getIndex(index) {
                return index + 1;
            },
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
                        <el-col :span="5"><div style="font-weight: bold">销售发货单tob详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="12">
                            <el-form-item label="发货单号：">
                                {{data.recordCode}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="12">
                        <el-form-item label="前置单号："  >
                            {{data.frontRecordCodes}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="12">
                        <el-form-item label="接收门店："  >
                            {{data.inRealWarehouseName}}
                        </el-form-item>
                        </el-col>
                        <el-col  :span="12">
                        <el-form-item label="发货仓：" >
                            {{data.outRealWarehouseName}}
                        </el-form-item>
                        </el-col>
                         <el-col  :span="12">
                         <el-form-item label="来源渠道：" >
                            {{data.channelName}}
                        </el-form-item>
                         </el-col>
                         <el-col  :span="12">
                        <el-form-item label="单据类型：" >
                          {{data.recordTypeName}}
                        </el-form-item>
                         </el-col>     
                          <el-col  :span="12">
                        <el-form-item label="发货单状态：" >
                          {{data.recordStatusName}}
                        </el-form-item>
                        </el-col>                                
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        
                    </div>  
                 
                    <el-table size="mini" border :data="data.details" max-height="400">
                      <el-table-column
                          label="序号"
                          type="index"
                          align="center">
                          <template scope="scope">
                              <span>{{getIndex(scope.$index)}}</span>
                          </template>
                      </el-table-column>
                      <el-table-column  label="SAP采购单号"  prop="sapPoNo" 
                       header-align="center"
                          align="center">
                      </el-table-column>
                      
                      <el-table-column  label="SAP采购单行号"  prop="lineNo" 
                       header-align="center"
                          align="center">
                      </el-table-column>
                      
                      <el-table-column  label="交货行号"  prop="deliveryLineNo" 
                       header-align="center"
                          align="center">
                      </el-table-column>
                      
                      <el-table-column  label="商品ID"  prop="skuId" 
                       header-align="center"
                          align="center">
                            
                        </el-table-column>
                        <el-table-column prop="skuCode" label="商品编码" 
                          header-align="center"
                          align="center" sortable>
                       
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称"  
                          header-align="center"
                          align="center">
                        
                        </el-table-column>
                        <el-table-column prop="planQty" label="计划商品数量"  
                         header-align="center"
                          align="center">
     
                        </el-table-column>
                         <el-table-column prop="actualQty" label="实际商品数量"  
                         header-align="center"
                          align="center">
     
                        </el-table-column>
                        <el-table-column prop="unit" label="单位"   
                        header-align="center"
                          align="center">
                        </el-table-column>
                    </el-table>
            </el-dialog>
       </div>
        `
    });
});
