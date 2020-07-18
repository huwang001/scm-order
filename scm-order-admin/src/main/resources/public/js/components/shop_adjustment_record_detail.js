//门店调整单
lyfdefine(function(){
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: {
                    recordType:24
                },
                dataListLoading:false,
                visible:false,
                recordCode:'',
                reasonList:{}
            }
        },
        methods: {
            init:function init(id){
                this.dataForm.id = id;
                this.visible = true;
                this.getDataList();
                this.getReasonList();
            },

            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                this.dataListLoading = true;
                this.$http.get('/scm-order-admin/order/v1/shopAdjustRecord/getAdjustForetasteByRecordId?recordId=' + _this.dataForm.id).then(function (res) {
                    let data = res.data.data;
                    if (data) {
                        _this.dataForm = data;
                        _this.dataForm.warehouseCode = data.shopCode + '/' + data.realWarehouseName;
                        _this.dataList = data.frontRecordDetails;
                    }
                    _this.dataListLoading = false;
                }).catch(function(error){
                    console.log(error);
                    _this.dataListLoading = false;
                });
            },
            getReasonList:function () {
                //调用接口查询损益单的业务原因字典
                let _this = this;
                this.$http.post('/scm-order-admin/order/v1/queryBusinessReason',
                    this.dataForm.recordType
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data) {
                        data.forEach(function (val) {
                            _this.reasonList[val.reasonCode] = val.reasonName;
                        });
                    } else {
                        _this.reasonList = [];
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },
        },

        template: `
            <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="80%"
            :visible.sync="visible">
            
                <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                    <el-col :span="3"><div style="font-weight: bold">门店试吃单详情</div></el-col>
                </el-row>
                <el-form :inline="true" size="small" :model="dataForm" class="demo-form-inline">
                    <el-col :span="8">
                        <el-form-item label="试吃单号：" prop="recordCode">
                            {{dataForm.recordCode}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="SAP过账单号：" prop="recordCode">
                            {{dataForm.sapRecordCode}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    <el-form-item label="cmp单号："  prop="outRecordCode">
                        {{dataForm.outRecordCode}}
                    </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    <el-form-item label="业务原因：" prop="reason">
                        {{reasonList[dataForm.reason]}}
                    </el-form-item>
                    </el-col>
                     <el-col :span="8">
                     <el-form-item label="门店编号/名称：" prop="warehouseCode" >
                        {{dataForm.warehouseCode}}
                    </el-form-item>
                     </el-col>
                     <el-col :span="8">
                    <el-form-item label="试吃日期：" prop="createTime">
                        {{dataForm.createTime}}
                    </el-form-item>
                     </el-col>
                </el-form>  
                
               <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                    <el-col :span="3"><div style="font-weight: bold">商品明细</div></el-col>
                </div>
             
                <el-table size="small" border :data="dataList" v-loading="dataListLoading" element-loading-text="拼命加载中...">
                
                    <el-table-column prop="skuCode" label="商品编号">
                    </el-table-column>
                    
                    <el-table-column prop="skuName" label="商品名称">
                    </el-table-column>
                    
                    <el-table-column prop="skuQty" label="试吃数量">
                    </el-table-column>
                    
                    <el-table-column prop="actualQty" label="实际出库数量">
                    </el-table-column>
                                        
                    <el-table-column prop="unit" label="商品单位">
                    </el-table-column>
                                            
                </el-table>
            </el-dialog>
        `
    });
});
