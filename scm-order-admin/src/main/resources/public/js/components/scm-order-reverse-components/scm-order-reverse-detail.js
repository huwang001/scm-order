lyfdefine([],function(){
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
        filters: {
            filterRecordType: (value) => {
                const orderTypes = [{
                    label: '出库单冲销',
                    value: 1
                },
                    {
                        label: '入库单冲销',
                        value: 2
                    }
                ]
                const type = orderTypes.find(n => n.value === value)
                if (type) {
                    return type.label
                }
                return value
            }
        },
        data: function () {
            return {
                orderDialogVisible: false,
                POOL_NAME: '/scm-order-admin',
                timer: null,
                loading: false,
                loadingText: '正在加载中',
                submitForm: {
                    recordCode: '',
                    recordType: '', // 冲销类型
                    recordStatus: '', // 单据状态
                    originRecordCode: '', // 冲销原单号
                    realWarehouseName: '', // 仓库名称
                    realWarehouseCode: '', //  实仓编码
                    reverseDate: '', // 冲销日期
                    outRecordCode: '', // 外部单号
                    remark: '',
                    reverseDetailDTOList: [],
                },
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
                            this.submitForm.recordCode = form.recordCode
                            this.submitForm.recordType = form.recordType
                            this.submitForm.recordStatus = form.recordStatus
                            this.submitForm.originRecordCode = form.originRecordCode
                            this.submitForm.realWarehouseName = form.realWarehouseName
                            this.submitForm.realWarehouseCode = form.realWarehouseCode
                            this.submitForm.reverseDate = form.reverseDate
                            this.submitForm.outRecordCode = form.outRecordCode
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
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                if (this.$refs.submitForm) {
                    this.submitForm.realWarehouseName = ''
                    this.$refs.submitForm.resetFields();
                }
                this.submitForm.reverseDetailDTOList = []
                this.$emit('dialog-close');
            }

        },
        template: `
<el-dialog title="冲销单详情" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1200px">
    <div v-loading="loading" :element-loading-text="loadingText" element-loading-spinner="el-icon-loading">
        <el-form :model="submitForm" inline-message ref="submitForm" size="mini" label-width="120px">
            <el-card shadow="never">
                <div slot="header">
                    基础信息
                </div>
                <div>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="冲销类型:" prop="recordType">
                                {{submitForm.recordType | filterRecordType}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="冲销原单号" prop="originRecordCode">
                                {{submitForm.originRecordCode}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="仓库编号/名称:" prop="realWarehouseCode">
                                {{submitForm.realWarehouseCode? submitForm.realWarehouseCode + '/' : ''}} {{submitForm.realWarehouseName}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="冲销日期:" prop="reverseDate">
                                {{submitForm.reverseDate}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="外部单号:" prop="outRecordCode">
                                {{submitForm.outRecordCode}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注:" prop="remark">
                                {{submitForm.remark}}
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
                        <el-table-column prop="batchRemark" header-align="center" align="center" width="150px" label="明细备注">
                        </el-table-column>
                        <el-table-column prop="actualQty" header-align="center" align="center" label="实际入库数量" min-width="100">
                        </el-table-column>
                    </el-table>
                </div>
            </el-card>
        </el-form>
        <div slot="footer" class="dialog-footer" style="textAlign: center;padding:10px 0px">
            <el-button @click.native="onClickCancelBtn" size="mini">返回</el-button>
        </div>
    </div>
</el-dialog>
        `
    });
});
