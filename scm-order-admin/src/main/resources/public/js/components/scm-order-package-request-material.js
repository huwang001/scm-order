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
        data: function () {
            return {
                loading: false,
                POOL_NAME: '/scm-order-admin',
                timer: null,
                validateYksl(row) {
                    const validate = (rule, value, callback) => {
                        // 包含0的正整数
                        const reg = /^(([0-9]+)|([0-9]+\.[0-9]{0,3}))$/
                        if (!reg.test(value)) {
                            callback(new Error('只能输入正整数且至多保留三位小数'));
                        }
                        if (value > 9999999999) {
                            callback(new Error('移库数量超过阈值'));
                        }
                        callback()
                    }
                    return validate
                },
                validateTbck(row) {
                    const validate = (rule, value, callback) => {
                        callback()
                    }
                    return validate
                },
                submitFormOption: {
                    warehouses: []
                },
                submitForm: {
                    tableData: []
                }
            }
        },
        methods: {
            init() {
                if (this.row && this.row.recordCode) {
                    this.loading = true
                    // 初始化数据
                    this.$http({
                        method: 'get',
                        url: this.POOL_NAME + '/order/v1/pack/demandComponent/queryDemandComponentByRecordCodeAllot',
                        params: {
                            recordCode: this.row.recordCode
                        }
                    }).then(res => {
                        this.loading = false
                        if (res.data.code === '0' && Array.isArray(res.data.data)) {
                            this.submitForm.tableData = res.data.data
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.data.msg,
                                showClose: true
                            })
                        }
                    }, () => {this.loading = false})
                }
            },
            onClickSubmitBtn() {
                clearTimeout(this.timer)
                this.timer = setTimeout(() => {
                    this.$refs.submitForm.validate((valid) => {
                        if (valid) {
                            this.loading = true
                            this.$http({
                                method: 'post',
                                url: this.POOL_NAME + '/order/v1/pack/demandComponent/createDemandAllot',
                                data: {
                                    requireCode: this.row.recordCode,
                                    demandAllotDetailList: this.submitForm.tableData.map(n => {
                                        return {
                                            parentSkuCode: n.parentSkuCode,
                                            skuCode: n.skuCode,
                                            allotQty: n.allotQty
                                        }
                                    })
                                }
                            }).then(
                                (res) => {
                                    this.loading = false
                                    if (res.data.code === '0') {
                                        this.$message({
                                            type: 'success',
                                            message: '保存成功',
                                            showClose: true
                                        })
                                        this.handleBeforeClose();
                                        this.callback(false);
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
                    this.submitForm.tableData = []
                }
                this.$emit('dialog-close');
            }

        },
        template: `
<el-dialog title="原料调拨表" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="960px">
    <div v-loading="loading">
        <el-form :model="submitForm" ref="submitForm" inline-message size="mini" label-width="80px">
            <el-table :data="submitForm.tableData" size="mini" border ref="table" style="width: 100%;">
                <el-table-column prop="recordCode" header-align="center" align="center" label="包装需求单号" min-width="120">
                </el-table-column>
                <el-table-column prop="channelCode" header-align="center" align="center" label="渠道" min-width="100">
                </el-table-column>
                <el-table-column prop="parentSkuCode" header-align="center" align="center" label="成品商品编码" min-width="100">
                </el-table-column>
                <el-table-column prop="skuCode" header-align="center" align="center" label="组件商品编码" min-width="100">
                </el-table-column>
                <el-table-column prop="requireQty" header-align="center" align="center" label="组件需求数" min-width="100">
                </el-table-column>
                <el-table-column prop="allotQty" header-align="center" align="center" label="待移库数量" min-width="100">
                    <template slot-scope="scope">
                        <el-form-item label-width="0px" style="margin:0px" :prop="'tableData.'+scope.$index + '.allotQty'" :rules="{
                        required: true, validator: validateYksl(scope.row), trigger: 'blur'}">
                            <el-input v-model="scope.row.allotQty"></el-input>
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column prop="pickWorkshopCode" header-align="center" align="center" label="调拨仓库" width="120">
                </el-table-column>
                <el-table-column prop="lockQty" header-align="center" align="center" label="已锁定数量" width="120">
                </el-table-column>
                <el-table-column prop="unit" header-align="center" align="center" label="单位" min-width="100">
                </el-table-column>
                <el-table-column prop="actualMoveQty" header-align="center" align="center" label="已移库数量" min-width="100">
                </el-table-column>
                <el-table-column prop="moveType" header-align="center" align="center" label="类型" min-width="80">
                    <template slot-scope="scope">{{scope.row.moveType === 1 ? '出库' : '入库'}}</template>
                </el-table-column>
            </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer" style="textAlign:center;padding:10px;">
            <el-button type="primary" @click.native="onClickSubmitBtn" size="mini">确定</el-button>
            <el-button @click.native="onClickCancelBtn" size="mini">取消</el-button>
        </div>
    </div>
</el-dialog>
        `
    });
});
