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
                POOL_NAME: '/scm-order-admin',
                loading: true,
                submitForm: {
                    packType: '',
                    saleCode: '',
                    channelCode: '',
                    demandDate: '',
                    inRealWarehouseName: '',
                    outRealWarehouseName:'',
                    outRealWarehouseCode: '',
                    inRealWarehouseCode: '',
                    isOut: '',
                    introducer: '',
                    priority: '',
                    department: '',
                    remark: '',
                    packDemandDetailDTOList: [],
                    packDemandComponentDTOList: []
                },
                formatterMoveType: (row) => {
                    if (row.moveType === 1) {
                        return '出库'
                    }
                    if (row.moveType === 2) {
                        return '入库'
                    }
                    return row.moveType
                },
                submitFormOption: {
                    packageTypes: [{
                        value: 1,
                        label: '组装'
                    },
                        {
                            value: 2,
                            label: '反拆'
                        },
                        {
                            value: 3,
                            label: '自定义组合'
                        },
                        {
                            value: 4,
                            label: '自定义反拆'
                        },
                        {
                            value: 5,
                            label: '拆箱'
                        }
                    ],
                    pickingWarehouses: [],
                    packageWarehouses: [],
                    isGetMaterial: [{
                        value: 0,
                        label: '是'
                    },
                        {
                            value: 2,
                            label: '否'
                        }
                    ],
                    isDelegate: [{
                        value: 1,
                        label: '是'
                    },
                        {
                            value: 2,
                            label: '否'
                        }
                    ]
                },
            }
        },
        methods: {
            init() {
                this.loading = true
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/demand/queryPackDemandDetail',
                    params: {
                        recordCode: this.row.recordCode
                    }
                }).then(res => {
                    this.loading = false
                    if (res.data.code === '0') {
                        const data = res.data.data
                        this.submitForm.packType = data.packType;
                        this.submitForm.saleCode = data.saleCode;
                        this.submitForm.channelCode = data.channelCode;
                        this.submitForm.demandDate = data.demandDate;
                        this.submitForm.outRealWarehouseCode = data.outRealWarehouseCode;
                        this.submitForm.inRealWarehouseCode = data.inRealWarehouseCode;
                        this.submitForm.outRealWarehouseName = data.outRealWarehouseName;
                        this.submitForm.inRealWarehouseName = data.inRealWarehouseName;
                        this.submitForm.isOut = data.isOut;
                        this.submitForm.introducer = data.introducer;
                        this.submitForm.priority = data.priority;
                        this.submitForm.department = data.department;
                        this.submitForm.remark = data.remark;
                        if (Array.isArray(data.packDemandDetailDTOList)) {
                            this.submitForm.packDemandDetailDTOList = data.packDemandDetailDTOList
                        }
                        if (Array.isArray(data.packDemandComponentDTOList)) {
                            this.submitForm.packDemandComponentDTOList = data.packDemandComponentDTOList
                        }
                    } else {
                        this.$message({
                            type: 'error',
                            showClose: true,
                            message: res.data.msg
                        })
                    }
                }, (err) => {
                    this.loading = false
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: err.toString()
                    })
                })
            },
            formatPackType(packType) {
                const item = this.submitFormOption.packageTypes.find(n => n.value == packType)
                if (item) {
                    return item.label
                }
                return packType
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                this.submitForm.packDemandDetailDTOList = []
                this.submitForm.packDemandComponentDTOList = []
                if (this.$refs.submitForm) {
                    this.$refs.submitForm.resetFields();
                }
                this.$emit('dialog-close');
            }

        },
        template: `
<el-dialog title="包装需求单详情" data-scm-order-package class="scm-order-package-request-add" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1100px">
    <el-form :model="submitForm" v-loading="loading" ref="submitForm" size="mini" label-width="100px">
        <el-card shadow="never">
            <div slot="header">
                基础信息
            </div>
            <div class="request-add__base">
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="包装类型:" prop="packType" label-width="100px">
                            {{formatPackType(submitForm.packType)}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="销售订单号:" prop="saleCode" label-width="100px">
                            {{submitForm.saleCode}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="渠道:" prop="channelCode" label-width="100px">
                            {{submitForm.channelCode}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="需求日期:" prop="demandDate" label-width="100px">
                            {{submitForm.demandDate}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="领料仓库:" prop="outRealWarehouseName">
                            {{submitForm.outRealWarehouseName}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="包装仓库:" prop="inRealWarehouseName">
                            {{submitForm.inRealWarehouseName}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="是否委外:" prop="isOut" label-width="100px">
                            {{submitForm.isOut === 1 ? '是' : '否'}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="需求提出人:" prop="introducer" label-width="100px">
                            {{submitForm.introducer}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="需求优先级:" prop="priority" label-width="100px">
                            {{submitForm.priority}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="指令部门:" prop="department" label-width="100px">
                            {{submitForm.department }}
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="备注:" prop="remark" label-width="100px">
                            {{submitForm.remark}}
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
        </el-card>
        <el-card shadow="never">
            <div slot="header">
                成品商品明细
            </div>
            <div class="request-add__product">
                <div class="request-add__product__body">
                    <el-table :data="submitForm.packDemandDetailDTOList" size="mini" border ref="table" style="width: 100%;">
                        <el-table-column label="序号" align="center" type="index" width="55">
                        </el-table-column>
                        <el-table-column prop="skuCode" header-align="center" align="center" label="成品商品编码" min-width="120">
                        </el-table-column>
                        <el-table-column prop="skuName" header-align="center" align="center" label="商品名称" min-width="100">
                        </el-table-column>
                        <el-table-column prop="requireQty" header-align="center" align="center" label="需求数量">
                        </el-table-column>
                        <el-table-column prop="unit" header-align="center" align="center" label="单位">
                        </el-table-column>
                        <el-table-column prop="remark" header-align="center" align="center" label="明细备注">
                        </el-table-column>
                        <el-table-column prop="customGroupCode" header-align="center" align="center" label="自定义组合码">
                        </el-table-column>
                        <el-table-column prop="compositeQty" header-align="center" align="center" label="组合份数">
                        </el-table-column>
                        <el-table-column prop="actualPackedQty" header-align="center" align="center" label="实际包装数量">
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-card>
        <el-card shadow="never">
            <div slot="header">
                组件商品明细
            </div>
            <div class="request-add__component">
                <div class="request-add__component__body">
                    <el-table :data="submitForm.packDemandComponentDTOList" size="mini" border ref="table" style="width: 100%;">
                        <el-table-column label="序号" align="center" type="index" width="55">
                        </el-table-column>
                        <el-table-column prop="parentSkuCode" header-align="center" align="center" label="成品商品编码" min-width="120">
                        </el-table-column>
                        <el-table-column prop="skuCode" header-align="center" align="center" label="组件商品编码" min-width="100">
                        </el-table-column>
                        <el-table-column prop="skuName" header-align="center" align="center" label="商品名称">
                        </el-table-column>
                        <el-table-column prop="bomQty" header-align="center" align="center" label="BOM数量">
                        </el-table-column>
                        <el-table-column prop="requireQty" header-align="center" align="center" label="需求数量">
                        </el-table-column>
                        <el-table-column prop="unit" header-align="center" align="center" label="基本单位">
                        </el-table-column>
                        <el-table-column prop="requireBoxQty" header-align="center" align="center" label="运输单位数量">
                        </el-table-column>
                        <el-table-column prop="isPick" header-align="center" align="center" label="是否需领料" width="120px">
                            <template slot-scope="scope">
                                {{scope.row.isPick ? '是' : '否'}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="actualMoveQty" header-align="center" align="center" label="实际移库数量">
                        </el-table-column>
                        <el-table-column prop="moveType" :formatter="formatterMoveType" header-align="center" align="center" label="移库类型">
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-card>
    </el-form>
    <div slot="footer" class="dialog-footer" style="textAlign: center;">
        <el-button @click.native="onClickCancelBtn" size="mini">返回</el-button>
    </div>
</el-dialog>
        `
    });
});
