lyfdefine(['scm-order-reverse-components/scm-order-reverse-select-in'],function(SelectOrderDialog){
    return ({
        props: {
            row: {
                type: Object,
                default () {
                    return null
                }
            },
            visible: {
                type: Boolean,
                default: false
            },
            callback: {
                type: Function,
                default: () => {},
            }
        },
        watch: {
            visible(n) {
                if (n) {
                    this.init()
                }
            }
        },
        data: function () {
            const validateOutOrder = (rule, value, callback) => {
                callback();
            }
            const validateRemark = (rule, value, callback) => {
                callback();
            }
            return {
                orderDialogVisible: false,
                POOL_NAME: '/scm-order-admin',
                timer: null,
                loading: false,
                loadingText: '',
                validateNumberRemark() {
                    const validate = (rule, value, callback) => {
                        if (value && value.length > 512) {
                            callback(new Error('长度在 0 到 512 个字符'))
                        }
                        callback();
                    }
                    return validate
                },
                submitForm: {
                    recordCode: '', // 单据编号
                    warehouseCodeAndName: '', // 工厂+编号
                    recordType: '', // 冲销类型
                    factoryCode: '', // 工厂编号
                    originRecordCode: '', // 冲销原单号
                    realWarehouseName: '', // 仓库名称
                    realWarehouseCode: '', // 实仓编号
                    receiptRecordCode: '', // 收货单据编号
                    reverseDate: '', // 冲销日期
                    outRecordCodeList: '', // 外部单号
                    remark: '', // 备注
                    reverseDetailDTOList: [],
                },
                submitFormOption: {
                    orderTypes: [{
                        label: '出库单冲销',
                        value: 1
                    },
                        {
                            label: '入库单冲销',
                            value: 2
                        }
                    ]
                },
                validateRules: {
                    recordCode: [{
                        required: true,
                        message: '请选择冲销单号',
                        trigger: 'blur'
                    }, ],
                    warehouseCodeAndName: [{
                        required: true,
                        message: '仓库必须填入',
                        trigger: 'blur'
                    }],
                    originRecordCode: [{
                        required: true,
                        message: '请输入冲销原单号',
                        trigger: 'blur'
                    }],
                    reverseDate: [{
                        required: true,
                        message: '请选择冲销日期',
                        trigger: 'blur'
                    }],
                    outRecordCode: [{
                        required: false,
                        validator: validateOutOrder,
                        trigger: 'blur'
                    }],
                    remark: [{
                        required: false,
                        validator: validateRemark,
                        trigger: 'blur'
                    }, {
                        min: 0,
                        max: 32,
                        message: '长度在 0 到 512 个字符',
                        trigger: 'blur'
                    }],
                }
            }
        },
        methods: {
            init() {
                if (this.row && this.row.recordCode) {
                    this.loading = true
                    this.$http({
                        method: 'get',
                        url: this.POOL_NAME + '/order/v1/reverse/queryReverseDetail',
                        params: {
                            recordCode: this.row.recordCode
                        }
                    }).then(res => {
                        this.loading = false
                        if (res.data.code === '0' && res.data.data) {
                            const form = res.data.data
                            this.submitForm.factoryCode = form.factoryCode
                            this.submitForm.id = form.id
                            this.submitForm.receiptRecordCode = form.receiptRecordCode
                            this.submitForm.voucherCode = form.voucherCode
                            this.submitForm.warehouseCodeAndName = form.realWarehouseCode + '/' + form.realWarehouseName
                            this.submitForm.recordCode = form.recordCode
                            this.submitForm.recordType = form.recordType
                            this.submitForm.recordStatus = form.recordStatus
                            this.submitForm.originRecordCode = form.originRecordCode
                            this.submitForm.realWarehouseName = form.realWarehouseName
                            this.submitForm.realWarehouseCode = form.realWarehouseCode
                            this.submitForm.reverseDate = form.reverseDate
                            this.submitForm.outRecordCodeList = form.outRecordCode
                            this.submitForm.remark = form.remark
                            if (Array.isArray(form.reverseDetailDTOList)) {
                                this.submitForm.reverseDetailDTOList = form.reverseDetailDTOList
                            }
                        }
                    }, (error) => {
                        this.loading = false
                        this.$message({
                            message: error.toString(),
                            showClose: true,
                            type: 'error'
                        })
                    })
                }
            },
            initSubmitForm() {
                this.submitForm.warehouseCodeAndName = ''
                this.submitForm.factoryCode = ''
                this.submitForm.originRecordCode = ''
                this.submitForm.realWarehouseName = ''
                this.submitForm.realWarehouseCode = ''
                this.submitForm.receiptRecordCode = ''
                this.submitForm.outRecordCodeList = ''
                this.submitForm.reverseDetailDTOList = []
            },
            onClickSubmitBtn() {
                clearTimeout(this.timer)
                this.timer = setTimeout(() => {
                    this.$refs.submitForm.validate((valid) => {
                        if (valid) {
                            const _submitForm = {
                                id: this.submitForm.id,
                                factoryCode: this.submitForm.factoryCode,
                                originRecordCode: this.submitForm.originRecordCode,
                                outRecordCodeList: this.submitForm.outRecordCodeList.split(','),
                                realWarehouseCode: this.submitForm.realWarehouseCode,
                                receiptRecordCode: this.submitForm.receiptRecordCode,
                                recordCode: this.submitForm.recordCode,
                                recordType: this.submitForm.recordType,
                                remark: this.submitForm.remark,
                                reverseDate: this.submitForm.reverseDate,
                                reverseDetailDTOList: this.submitForm.reverseDetailDTOList,
                                userId: this.submitForm.userId
                            }
                            this.loading = true
                            this.$http({
                                method: 'post',
                                url: this.POOL_NAME + '/order/v1/reverse/createReverse',
                                data: _submitForm
                            }).then(
                                (res) => {
                                    this.loading = false
                                    if (res.data.code === '0') {
                                        this.$message({
                                            type: 'success',
                                            message: '编辑成功',
                                            showClose: true
                                        })
                                        this.handleBeforeClose();
                                        this.callback();
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

                }, 300)
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                if (this.$refs.submitForm) {
                    this.$refs.submitForm.resetFields();
                }
                this.initSubmitForm()
                this.$emit('dialog-close');
            }

        },
        template: `
<el-dialog title="冲销单编辑" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1200px">
    <div v-loading="loading" :element-loading-text="loadingText" element-loading-spinner="el-icon-loading">
        <el-form :model="submitForm" inline-message :rules="validateRules" ref="submitForm" size="mini" label-width="120px">
            <el-card shadow="never">
                <div slot="header">
                    基础信息
                </div>
                <div>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="冲销类型:" prop="recordType">
                                <el-select disabled v-model="submitForm.recordType" style="width:100%">
                                    <el-option v-for="item in submitFormOption.orderTypes" :key="item.value" :value="item.value" :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="冲销原单号" prop="originRecordCode">
                                <el-input style="width:100%" disabled v-model="submitForm.originRecordCode"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="仓库编号/名称:" prop="warehouseCodeAndName">
                                <el-input disabled v-model="submitForm.warehouseCodeAndName" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="冲销日期:" prop="reverseDate">
                                <el-date-picker style="width:100%;" type="date" v-model="submitForm.reverseDate" placeholder="冲销日期"></el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-tooltip class="item" effect="dark" :content="submitForm.outRecordCodeList" placement="top">
                                <el-form-item label="外部单号:" prop="outRecordCodeList">
                                    <el-input style="width:100%" disabled v-model="submitForm.outRecordCodeList"></el-input>
                                </el-form-item>
                            </el-tooltip>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注:" prop="remark">
                                <el-input style="width:100%" v-model="submitForm.remark"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
            </el-card>
            <el-card shadow="never" style="marginTop:10px;">
                <div slot="header">
                    商品明细
                </div>
                <div>
                    <el-table :data="submitForm.reverseDetailDTOList" size="mini" border ref="table" style="width: 100%;">
                        <el-table-column label="序号" align="center" type="index" width="55">
                        </el-table-column>
                        <el-table-column prop="skuCode" header-align="center" align="center" label="商品编码" min-width="120">
                        </el-table-column>
                        <el-table-column prop="skuName" header-align="center" align="center" label="商品名称" min-width="100">
                        </el-table-column>
                        <el-table-column prop="reverseQty" header-align="center" align="center" label="冲销数量" min-width="100">
                        </el-table-column>
                        <el-table-column prop="unit" header-align="center" align="center" label="单位" min-width="100">
                        </el-table-column>
                        <el-table-column prop="batchRemark" header-align="center" align="center" width="150px" label="批次备注">
                            <template slot-scope="scope">
                                <el-form-item label-width="0px" style="marginBottom:0px;" :prop="'reverseDetailDTOList.'+scope.$index + '.batchRemark'" :rules="{
                                        required: true, validator: validateNumberRemark(scope.row), trigger: 'blur'}">
                                    <el-input v-model="scope.row.batchRemark">
                                    </el-input>
                                </el-form-item>
                            </template>
                        </el-table-column>
                        <el-table-column prop="actualQty" header-align="center" align="center" label="实际入库数量" min-width="100">
                        </el-table-column>
                    </el-table>
                </div>
            </el-card>
        </el-form>
        <div slot="footer" class="dialog-footer" style="textAlign: center;padding:10px 0px">
            <el-button type="primary" :disabled="loading" @click.native="onClickSubmitBtn" size="mini">提交</el-button>
            <el-button @click.native="onClickCancelBtn" size="mini">返回</el-button>
        </div>
    </div>
</el-dialog>
        `
    });
});
