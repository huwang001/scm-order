lyfdefine(['scm-order-package-request-detail','scm-order-package-request-edit','scm-order-package-request-material'],function(DetailDialog,EditDialog,MaterialDialog){
    return ({
        components: {
            DetailDialog,
            EditDialog,
            MaterialDialog
        },
        props: {
            columns: {
                type: Array,
                default () {
                    return []
                }
            },
            baseFilterForm: {
                type: Object,
                default () {
                    return null
                }
            },
            callback: {
                type: Function,
                default () {
                    return null
                }
            },
            baseFilterForm: {
                type: Object,
                default () {
                    return null
                }
            },
            state: {
                type: [String, Number],
                default: ''
            }
        },
        data() {
            return {
                POOL_NAME: '/scm-order-admin',
                selectedTableData: [],
                filterFormOption: {
                    packageStates: [{
                        label: '初始',
                        value: 0
                    },
                        {
                            label: '已确认',
                            value: 1
                        },
                        {
                            label: '已取消',
                            value: 2
                        },
                        {
                            label: '已部分包装',
                            value: 3
                        },
                        {
                            label: '已包装完成',
                            value: 4
                        }
                    ],
                    packageTypes: [{
                        label: "组装",
                        value: 1
                    },
                        {
                            label: "反拆",
                            value: 2
                        },
                        {
                            label: "自定义组合",
                            value: 3
                        },
                        {
                            label: "自定义反拆",
                            value: 4
                        },
                        {
                            label: "拆箱",
                            value: 5
                        }
                    ],
                    createTypes: [{
                        label: '接口创建',
                        value: 1
                    },
                        {
                            label: '页面创建',
                            value: 2
                        },
                        {
                            label: '导入创建',
                            value: 3
                        },
                        {
                            label: '预约单创建',
                            value: 4
                        }
                    ],
                    pickingStates: [{
                        label: '未领料',
                        value: 1
                    },
                        {
                            label: '已领料',
                            value: 2
                        }
                    ]
                },
                currentRow: null,
                editDialogVisible: false,
                detailDialogVisible: false,
                materialDialogVisible: false,
                tableData: [],
                paginationOption: {
                    total: 3,
                    page: 1,
                    pageSize: 10
                },
                formatterCreateType: (row) => {
                    // 创建方式 1：接口创建2：页面创建3：导入创建
                    const item = this.filterFormOption.createTypes.find(n => n.value == row.createType)
                    if (item) {
                        return item.label
                    }
                    return row.createType
                },
                formatterRecordStatus: (row) => {
                    const item = this.filterFormOption.packageStates.find(n => n.value == row.recordStatus)
                    if (item) {
                        return item.label
                    }
                    return row.recordStatus
                },
                formatterPickStatus: (row) => {
                    const item = this.filterFormOption.pickingStates.find(n => n.value == row.pickStatus)
                    if (item) {
                        return item.label
                    }
                    return row.pickStatus
                },
                formatterPackType: (row) => {
                    const item = this.filterFormOption.packageTypes.find(n => n.value == row.packType)
                    if (item) {
                        return item.label
                    }
                    return row.packType
                }
            }
        },
        methods: {
            handleSearchData() {
                this.paginationOption.page = 1;
                this.fetchTableData();
            },
            onClickEditBtn(row) {
                this.editDialogVisible = true
                this.currentRow = row
            },
            onClickViewDetailBtn(row) {
                this.detailDialogVisible = true
                this.currentRow = row
            },
            onClickViewMaterialBtn(row) {
                this.materialDialogVisible = true
                this.currentRow = row
            },
            // 处理分页数量
            handleSizeChange(pageSize) {
                this.paginationOption.page = 1
                this.paginationOption.pageSize = pageSize
                this.fetchTableData();
            },
            // 处理分页
            handleCurrentPageChange(page) {
                this.paginationOption.page = page
                this.fetchTableData();
            },
            // 处理表格数据多选
            handleSelectionChange(rows) {
                this.selectedTableData = rows;
            },
            fetchTableData() {
                const data = {
                    outFactoryCode: this.baseFilterForm.outFactoryCode,
                    outRealWarehouseCode: this.baseFilterForm.outRealWarehouseCode,
                    recordCodes: this.baseFilterForm.recordCodes ? this.baseFilterForm.recordCodes.split(/[(\r\n)\r\n]+/) : [],
                    recordStatus: this.baseFilterForm.recordStatus,
                    packType: this.baseFilterForm.packType,
                    skuCodes: this.baseFilterForm.skuCodes ? this.baseFilterForm.skuCodes.split(/[(\r\n)\r\n]+/) : [],
                    saleCodes: this.baseFilterForm.saleCodes ? this.baseFilterForm.saleCodes.split(/[(\r\n)\r\n]+/) : [],
                    creator: this.baseFilterForm.creator,
                    startTime: this.baseFilterForm.startTime,
                    endTime: this.baseFilterForm.endTime,
                    requiredStartTime: this.baseFilterForm.requiredStartTime,
                    requiredEndTime: this.baseFilterForm.requiredEndTime,
                    pickStatus: this.baseFilterForm.pickStatus,
                    channelCodes: this.baseFilterForm.channelCodes,
                    pageSize: this.paginationOption.pageSize,
                    pageNum: this.paginationOption.page,
                }
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/demand/queryPackDemandPage',
                    data,
                }).then(res => {
                    if (res.data.code === '0') {
                        if (res.data.data && Array.isArray(res.data.data.list)) {
                            this.tableData = res.data.data.list;
                            this.paginationOption.total = res.data.data.total;
                        } else {
                            this.tableData = []
                            this.paginationOption.total = 0;
                        }
                    } else {
                        this.$message({
                            type: 'error',
                            showClose: true,
                            message: res.data.msg
                        })
                        this.tableData = [];
                        this.paginationOption.total = 0;
                    }
                }, () => {
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            }
        },
        template: `
<div class="request-list-card2-table">
    <div class="request-list-card2-table__body">
        <el-table :data="tableData" size="mini" border ref="table" style="width: 100%;" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <template v-for="(item,index) in columns">
                <el-table-column :key="index" :formatter="item.formatter" :prop="item.fieldName" v-if="item.show == 1"  header-align="center" align="center" :label="item.title" min-width="120">
                </el-table-column>
            </template>
            <el-table-column header-align="center" fixed="right" align="center" label="操作" width="120">
                <template slot-scope="scope">
                    <el-button type="text" @click="onClickEditBtn(scope.row)" v-if="scope.row.recordStatus==0">编辑</el-button>
                    <el-button type="text" @click="onClickViewDetailBtn(scope.row)">查看详情</el-button>
                    <el-button type="text" @click="onClickViewMaterialBtn(scope.row)" v-if="scope.row.recordStatus ===1">原料调拨单</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="request-list-card2-table__footer" style="padding:10px;textAlign:right;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
    </div>
    <edit-dialog :row="currentRow" :visible="editDialogVisible" @dialog-close="editDialogVisible=false" :callback="handleSearchData" />
    <detail-dialog :row="currentRow" :visible="detailDialogVisible" @dialog-close="detailDialogVisible=false" />
    <material-dialog :row="currentRow" :visible="materialDialogVisible" @dialog-close="materialDialogVisible=false" :callback="handleSearchData" />
</div>
        `
    });
});
