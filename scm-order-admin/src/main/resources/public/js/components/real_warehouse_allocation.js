lyfdefine(function(){
    return ({
        data: function () {
            return {
                dataForm: {
                    allotRealWarehouseCode:null,
                    dataListSelections:[],
                },
                warehouseInCode:[
                    {value: 'X001-C001', label: '测试调入仓库1'},
                    {value: 'X001-C002', label: '测试调入仓库2'}
                ],
                visible:false,
                allocationSelections:[],
                rules: {},
            }
        },
        methods: {
            init: function init(dataListSelections) {
                let _this = this;
                this.visible = true;
                _this.dataForm.dataListSelections=dataListSelections;
                _this.dataForm.dataListSelections.forEach(function (e) {
                    let data={};
                    data.factoryCode=e.factoryCode;
                    data.type=e.needPackage;
                    //根据预约单号查询对应仓库
                    _this.$http.post('/scm-order-admin/order/v1/realWarehouse/allocation/queryRealWarehouseByFactoryCodeAndType',data
                    ).then(function (res) {
                        if(res.status=='200' && res.data.data){
                            e.warehouseInCode=res.data.data;
                        }
                    }).catch(function(error){
                        console.log(error);
                    });
                    // _this.allocationSelections.push(e.orderCode);
                    //动态规则
                    // _this.$set(_this.rules,e.orderCode,[{ required: true, message: '调入仓库不能为空', trigger: ['blur','change'] }]);
                });
                console.log(_this.allocationSelections);

            },
            closeVisible: function () {
                this.visible = false;
                this.$refs['dataForm'].resetFields();
                this.allocationSelections=[];
            },
            itemFormSubmit:function () {
                let _this = this;
                this.$refs['dataForm'].validate((valid) => {
                    debugger;
                    if(!valid){
                        return false;
                    }
                    let reqData=[];
                    let item={};
                    // if(_this.dataForm.allotRealWarehouseCode =='' ||_this.dataForm.allotRealWarehouseCode ==null){
                    //     _this.$message({
                    //         message: '调入仓不能为空'
                    //     });
                    //     return;
                    // }
                    // if(_this.allocationSelections==null || _this.dataListSelections.length==0){
                    //     _this.$message({
                    //         message: '预约单号不能为空'
                    //     });
                    //     return;
                    // }
                    // _this.allocationSelections.forEach(function (e) {
                    //     item.allotRealWarehouseCode=_this.dataForm.allotRealWarehouseCode;
                    //     item.factoryCode=_this.factoryCode;
                    //     item.orderCode=e;
                    //     reqData.push(item);
                    // })
                    _this.dataForm.dataListSelections.forEach(function (e) {
                        if(e.allotRealWarehouseCode==''|| e.allotRealWarehouseCode ==null){
                            _this.$message({message: '预约单号:'+e.orderCode+'的调入仓不能为空'});
                            return false;
                        }
                    })
                    debugger;
                    console.log(_this.dataForm.dataListSelections);
                    this.$http.post('/admin-dome/order/v1/realWarehouse/allocation/realWarehouseBatchAllocation', _this.dataForm.dataListSelections
                    ).then(function (_ref) {
                        let data = _ref.data.data;
                        if (data && data.successList) {
                            _this.$message({message:'操作成功！'+data.successList+',操作失败：'+data.failList,type: 'success'});
                            _this.closeVisible();
                            _this.$emit('refreshDataList');
                        } else {
                            _this.dataList = [];
                            _this.dataForm.totalPage = 0;
                        }
                        _this.dataListLoading = false;
                    });
                })

            }
        },
        mounted() {
        },
        template: `
                <el-dialog
                 title="实仓调拨"
                 :close-on-click-modal="false"
                 :before-close="closeVisible"
                  v-if="visible"
                 width="80%"
                 :visible.sync="visible">
                    <el-form :inline="true"  :label-position="top"  :model="dataForm" class="demo-form-inline" ref="dataForm">
                        <!--<el-row>-->
                            <!--<el-col :span="8">-->
                                <!--<el-form-item label="调出仓库：" prop="businessType">-->
                                    <!--<div style="font-weight: bold">X001-C001-总仓原料仓</div>-->
                                <!--</el-form-item>-->
                            <!--</el-col>-->
                        <!--</el-row>-->
                        <!--<el-row>-->
                            <!--<el-form-item label="预约单号：" prop="allocationSelections">-->
                                <!--<el-select style="width: 800px;" v-model="allocationSelections" multiple placeholder="请选择">-->
                                <!--<el-option-->
                                  <!--v-for="item in dataListSelections"-->
                                  <!--:key="item.orderCode"-->
                                  <!--:label="item.orderCode"-->
                                  <!--:value="item.orderCode">-->
                                <!--</el-option>-->
                              <!--</el-select>-->
                            <!--</el-form-item>-->
                        <!--</el-row>-->
                        <!--<el-row>-->
                            <!--<el-col :span="8">-->
                                <!--<el-form-item label="调入工厂："  prop="outWarehouseCode">-->
                                    <!--{{factoryCode}}-->
                                <!--</el-form-item>-->
                            <!--</el-col>-->
                            <!--<el-col :span="8">-->
                                <!--<el-form-item label="选择调入仓："  prop="allotRealWarehouseCode">-->
                                    <!--<el-select filterable v-model="dataForm.allotRealWarehouseCode" placeholder="仓库编号/名称"">-->
                                        <!--<el-option v-for="item in warehouseOutCode" :key="item.value" :label="item.label" :value="item.value" >-->
                                        <!--</el-option>-->
                                    <!--</el-select>-->
                                <!--</el-form-item>-->
                            <!--</el-col>-->
                        <!--</el-row>-->
                        <el-row
                            v-for="(item, index) in dataForm.dataListSelections"
                            :label="'域名' + index"
                            :key="item.key">
                            <el-col :span="6">
                                <el-form-item label="调出仓库：">
                                    <div style="font-weight: bold">X001-C001-总仓原料仓</div>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="预约单号：">
                                    <div style="font-weight: bold">{{item.orderCode}}</div>
                                </el-form-item>
                            </el-col>
                            <el-col :span="4">
                                <el-form-item label="调入工厂：">
                                    <div style="font-weight: bold">{{item.factoryCode}}</div>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6"> 
                                <el-form-item label="调入仓库："  :prop="'dataListSelections.'+index+'.allotRealWarehouseCode'" :rules="[{required: true, message: '调入仓库不能为空', trigger: 'change'}]">
                                    <el-select clearable  filterable v-model="item.allotRealWarehouseCode" placeholder="仓库编号/名称"">
                                        <el-option v-for="e in item.warehouseInCode" :key="e.value" :label="e.label" :value="e.value" >
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                          </el-row>
                    </el-form>  
                    <span slot="footer" class="dialog-footer">
                        <el-button type="primary" @click="itemFormSubmit()">提交</el-button>
                        <el-button type="primary" @click="closeVisible">返回</el-button>
                    </span>
                    <addOrUpdate></addOrUpdate>
                   </el-dialog>
                 </el-dialog>
        `
    });
});
