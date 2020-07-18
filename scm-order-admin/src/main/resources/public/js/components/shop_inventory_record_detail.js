lyfdefine(function(){
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: [],
                dataListLoading:false,
                visible:false
            }
        },
        methods: {
            init:function init(frontRecord){
                this.dataForm = frontRecord;
                this.visible = true;
                this.getDataList();
            },
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                this.dataListLoading = true;
                this.$http.get('/scm-order-admin/order/v1/record/shopInventoryDetail/list',
                    { params : {"frontRecordId" : this.dataForm.id } }
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
            }
        },
        template: `
            <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="75%"
            :visible.sync="visible">
                <el-main>
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="3"><div style="font-weight: bold">盘点单详情</div></el-col>
                    </el-row>
                    <el-form :inline="true" size="small" :model="dataForm" class="demo-form-inline">
                        <el-col :span="8">
                            <el-form-item label="盘点单号：">
                                <div>{{dataForm.recordCode}}</div>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                        <el-form-item label="关联单号："  >
                            <div>{{dataForm.outRecordCode}}</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="8">
                        <el-form-item label="门店编号/名称："  >
                            <div>{{dataForm.shopCode}}/{{dataForm.shopName}}</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="8">
                        <el-form-item label="盘点类型：" >
                            <div v-if="dataForm.businessType==0">抽盘</div>
                            <div v-if="dataForm.businessType==1">全盘</div>
                            <div v-if="dataForm.businessType==9">账面盘</div>
                        </el-form-item>
                        </el-col>
                        <el-col :span="8">
                        <el-form-item label="盘点日期：" >
                           <div>{{dataForm.outCreateTime}}</div>
                        </el-form-item>
                         </el-col>
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        <el-col :span="3"><div style="font-weight: bold">商品明细</div></el-col>
                    </div>
                 
                    <el-table size="small" border :data="dataList" v-loading="dataListLoading" element-loading-text="拼命加载中...">
                        <el-table-column prop="skuCode" label="商品编号" width="140">
                          
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称" width="140">
                       
                        </el-table-column>
                        <el-table-column prop="stockQty" label="账面数量" width="140">
                       
                        </el-table-column>
                        <el-table-column prop="skuQty" label="实盘数量" width="140">
                       
                        </el-table-column>
                        <el-table-column prop="diffStockQty" label="差异数量" width="140">
                       
                        </el-table-column>
                        <el-table-column prop="unit" label="基本单位" width="140">
                        </el-table-column>
                        <el-table-column prop="accQty" label="源系统账面数量" width="140">
                        </el-table-column>
                        <el-table-column prop="diffQty" label="源系统差异数量" width="140">
                        </el-table-column>
                    </el-table>
                </el-main>
            </el-dialog>
        `
    });
});