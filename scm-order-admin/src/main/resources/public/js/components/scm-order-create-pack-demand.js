lyfdefine(
    function () {
        return ({
                data: function () {
                    return {
                        loading: false,
                        visible: false,
                        POOL_NAME: '/scm-order-admin',
                        submitForm: {
                            orderCode: "", // 订单编码
                            inFactoryCode: '', //入向工厂编码
                            inRealWarehouseCode: '', // 入向实仓编码
                            inRealWarehouseId: '', // 包装仓ID
                            outFactoryCode: '', // 出向工厂编码
                            outRealWarehouseCode: '', // 出向实仓编码
                            outRealWarehouseId: '', // 领料仓ID
                        },
                        submitFormOption:{
                            pickingWarehouses: [],
                            packageWarehouses: [],
                        },
                        validateRules: {
                            inRealWarehouseId: [{
                                required: true,
                                message: '请选择包装仓库',
                                trigger: 'blur'
                            }],
                            outRealWarehouseId: [{
                                required: true,
                                message: '请选择领料仓库',
                                trigger: 'blur'
                            }],
                        }
                    }
                },
                methods: {
                    // 获取包装仓列表
                    init(orderCode) {
                        this.loading = false
                        this.visible = true
                        this.submitForm = {
                            orderCode: "", // 订单编码
                            inFactoryCode: '', //入向工厂编码
                            inRealWarehouseCode: '', // 入向实仓编码
                            inRealWarehouseId: '', // 包装仓ID
                            outFactoryCode: '', // 出向工厂编码
                            outRealWarehouseCode: '', // 出向实仓编码
                            outRealWarehouseId: '', // 领料仓ID
                        }
                        this.submitForm.orderCode = orderCode;
                        // 请求领料仓库
                        this.$http({
                            method: 'get',
                            url: this.POOL_NAME + '/order/v1/real_warehouse/queryNotShopWarehouse',
                        }).then(res => {
                            if (res.data.code === '0' && Array.isArray(res.data.data)) {
                                this.submitFormOption.pickingWarehouses = res.data.data
                            }
                        }, () => {
                            this.submitFormOption.pickingWarehouses = []
                        })
                    },
                    handleChangeOutRealWarehouseId(value) {
                        this.submitForm.outFactoryCode = ''
                        this.submitForm.outRealWarehouseCode = ''
                        this.submitForm.inFactoryCode = ''
                        this.submitForm.inRealWarehouseId = ''
                        this.submitForm.inRealWarehouseCode = ''
                        this.submitFormOption.packageWarehouses = []
                        const warehouse = this.submitFormOption.pickingWarehouses.find(n => n.id === value)
                        if (warehouse) {
                            // 请求包装仓库
                            this.$http({
                                method: 'get',
                                url: this.POOL_NAME + '/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeAndRWType',
                                params: {
                                    factoryCode: warehouse.factoryCode,
                                    realWarehouseType: 2
                                }
                            }).then(res => {
                                if (res.data.code === '0' && Array.isArray(res.data.data)) {
                                    this.submitFormOption.packageWarehouses = res.data.data
                                }
                            }, () => {
                                this.submitFormOption.packageWarehouses = []
                            })
                            this.submitForm.outFactoryCode = warehouse.factoryCode
                            this.submitForm.outRealWarehouseCode = warehouse.realWarehouseOutCode
                        }
                    },
                    handleChangeInRealWarehouseId(value) {
                        const warehouse = this.submitFormOption.packageWarehouses.find(n => n.id === value)
                        if (warehouse) {
                            this.submitForm.inFactoryCode = warehouse.factoryCode
                            this.submitForm.inRealWarehouseCode = warehouse.realWarehouseOutCode
                        }
                    },
                    //确认
                    dataFormSubmit : function () {
                        if(this.loading){
                            return
                        }
                        this.$refs.submitForm.validate((valid) => {
                            if (valid) {
                                const _submitForm = {
                                    orderCode: this.submitForm.orderCode, // 订单编码
                                    inFactoryCode:this.submitForm.inFactoryCode, //入向工厂编码
                                    inRealWarehouseCode:this.submitForm.inRealWarehouseCode, // 入向实仓编码
                                    outFactoryCode:this.submitForm.outFactoryCode, // 出向工厂编码
                                    outRealWarehouseCode: this.submitForm.outRealWarehouseCode, // 出向实仓编码
                                }
                                this.loading = true
                                this.$http({
                                    url: this.POOL_NAME + "/order/v1/order/createPackDemand",
                                    method: 'post',
                                    data:_submitForm
                                }).then(
                                    (res) => {
                                        this.loading = false
                                        if (res.data.code === '0') {
                                            this.$message({
                                                type: 'success',
                                                message: '创建成功',
                                                showClose: true
                                            })
                                            this.visible = false
                                        } else {
                                            this.$message({
                                                type: 'error',
                                                message: res.data.msg,
                                                showClose: true
                                            })
                                        }
                                    },
                                    (error) => {
                                        this.loading = false
                                        this.$message({
                                            type: 'error',
                                            message: error.toString(),
                                            showClose: true
                                        })
                                    },
                                );
                            }
                        });
                    },
                },
                template: `
<el-dialog
    width="450px"
    height="90%"
    title="生成需求单"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
         <el-form  label-width="100px"  inline :model="submitForm" ref="submitForm" :rules="validateRules" size="mini">
                            <el-form-item label="领料仓库:" prop="outRealWarehouseId">
                                <el-select style="width:100%" @change="handleChangeOutRealWarehouseId" v-model="submitForm.outRealWarehouseId" filterable clearable placeholder="请选择领料仓库">
                                    <el-option v-for="item in submitFormOption.pickingWarehouses" :key="item.id" :label="item.realWarehouseName+ '('+item.realWarehouseCode+')'" :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
    
                            <el-form-item label="包装仓库:" prop="inRealWarehouseId">
                                <el-select style="width:100%" @change="handleChangeInRealWarehouseId" v-model="submitForm.inRealWarehouseId" filterable clearable placeholder="请选择包装仓库">
                                    <el-option v-for="item in submitFormOption.packageWarehouses" :key="item.id" :label="item.realWarehouseName+ '('+item.realWarehouseCode+')'" :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
             </el-form>
    </div>
     <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"    @click="dataFormSubmit()">确认</el-button>
    </span>
    </el-dialog>
    `
            }
        );
    });