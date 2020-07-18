lyfdefine(function(){
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: {
                    frontRecord:{
                        remark: ''
                    }
                },
                dataListLoading:false,
                visible:false
            }
        },
        methods: {
            init:function init(warehouseRecord,typeName){
                this.dataForm = warehouseRecord;
                if (!this.dataForm.frontRecord) {
                    this.dataForm.frontRecord = {};
                    this.dataForm.frontRecord.remark = "";
                } else {
                    if (!this.dataForm.frontRecord.remark) {
                        this.dataForm.frontRecord.remark = "";
                    }
                }
                this.dataForm.typeName = typeName;
                this.visible = true;
                this.getDataList();
            },
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                this.dataListLoading = true;
                this.$http.get('/scm-order-admin/order/v1/warehouse_record/warehouse_record_detail/list',
                    { params : {"warehouseRecordId" : this.dataForm.id } }
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.dataList = res.data.data;
                    }else{
                        _this.dataList = [];
                        _this.$message('查询失败！');
                    }
                    _this.dataListLoading = false;
                }).catch(function(error){
                    console.log(error);
                    _this.dataListLoading = false;
                });
            },
        },
        template: `
            <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="75%"
            :visible.sync="visible">
                <el-main>
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="4"><div style="font-weight: bold">入库单详情</div></el-col>
                    </el-row>
                    <el-form :inline="true" size="small" :model="dataForm" class="demo-form-inline">
                        <el-col :span="12">
                            <el-form-item label="入库单号：">
                                <div>{{dataForm.recordCode}}</div>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                        <el-form-item label="单据类型："  >
                            <div>{{dataForm.typeName}}</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="12">
                        <el-form-item label="工厂编号："  >
                            <div>{{dataForm.factoryCode}}</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="12">
                        <el-form-item label="仓库编号/名称：" >
                            <div>{{dataForm.realWarehouseCode}}/{{dataForm.realWarehouseName}}</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="12">
                        <el-form-item label="入库日期：" >
                           <div>{{dataForm.receiverTime}}</div>
                        </el-form-item>
                         </el-col>
                         <el-col :span="12">
                            <el-form-item label="备注：" >
                                 <div>{{dataForm.frontRecord.remark}}</div>
                            </el-form-item>
                         </el-col>
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        <el-col :span="4"><div style="font-weight: bold">商品明细</div></el-col>
                    </div>
                 
                    <el-table size="small" border :data="dataList" v-loading="dataListLoading" element-loading-text="拼命加载中...">
                    	
                    	<el-table-column prop="lineNo" header-align="center" align="center" label="po行号" >                         
                        </el-table-column>
                        <el-table-column prop="deliveryLineNo" header-align="center" align="center" label="交货行号" >                         
                        </el-table-column>
                    
                        <el-table-column prop="skuCode" header-align="center" align="center" label="商品编号">                         
                        </el-table-column>
                        <el-table-column prop="skuName" header-align="center" align="center" label="商品名称">                      
                        </el-table-column>
                        <el-table-column prop="planQty" header-align="center" align="center" label="计划入库数量">
                        </el-table-column>
                        <el-table-column prop="actualQty" header-align="center" align="center" label="实际入库数量">
                        </el-table-column>
                        <el-table-column prop="unit" header-align="center" align="center" label="单位">
                    </el-table>
                    
                </el-main>
            </el-dialog>
        `
    });
});