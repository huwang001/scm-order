lyfdefine(['scm-order-reverse-components/scm-order-reverse-edit','scm-order-reverse-components/scm-order-reverse-add','scm-order-reverse-components/scm-order-reverse-detail'],function(EditDialog,AddDialog,DetailDialog){
    return ({
        components: {
            EditDialog,
            AddDialog,
            DetailDialog
        },
        watch: {
            'filterForm.startTime': 'watchCreateTimeStart',
            'filterForm.endTime': 'watchCreateTimeEnd'
        },
        data: function () {
            return {
                titleConfigId: window.location.href + '/1',
                POOL_NAME: "/scm-order-admin",
                loading: false,
                timer: null,
                addDialogVisible: false,
                editDialogVisible: false,
                detailDialogVisible: false,
                selectedTableData: [],
                tableData: [],
                currentRow: null,
                cols: [{
                    title: "冲销单号",
                    fieldName: "recordCode",
                    show: 1,
                },
                    {
                        title: "仓库编号",
                        fieldName: "warehouseCode",
                        show: 1
                    },
                    {
                        title: "仓库名称",
                        fieldName: "realWarehouseName",
                        show: 1
                    },
                    {
                        title: "冲销单原号",
                        fieldName: "originRecordCode",
                        show: 1
                    },
                    {
                        title: "冲销类型",
                        fieldName: "recordType",
                        show: 1,
                        formatter: (row) => {
                            const item = this.filterFormOption.chargeGainstTypes.find(n => n.value == row.recordType)
                            if (item) {
                                return item.label
                            }
                            return row.recordType
                        }
                    },
                    {
                        title: "冲销单状态",
                        fieldName: "recordStatus",
                        show: 1,
                        formatter: (row) => {
                            const item = this.filterFormOption.orderStates.find(n => n.value == row.recordStatus)
                            if (item) {
                                return item.label
                            }
                            return row.recordStatus
                        }
                    },
                    {
                        title: "冲销日期",
                        fieldName: "reverseDate",
                        show: 1
                    },
                    {
                        title: "外部单号",
                        fieldName: "outRecordCode",
                        show: 1
                    },
                    {
                        title: "冲销凭证号",
                        fieldName: "voucherCode",
                        show: 1
                    },
                    {
                        title: "备注",
                        fieldName: "remark",
                        show: 1,
                    },
                    {
                        title: "创建人",
                        fieldName: "creator",
                        show: 1
                    },
                    {
                        title: "创建时间",
                        fieldName: "createTime",
                        show: 1
                    }
                ],
                startTimePickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        return !this.filterForm.endTime ? false : time.getTime() > this.filterForm.endTime.getTime()
                    }
                },
                endTimePickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        let hms = 0
                        if (this.filterForm.startTime) {
                            hms = this.filterForm.startTime.getMinutes() + this.filterForm.startTime.getSeconds() + this.filterForm.startTime.getHours()
                        }
                        return !this.filterForm.startTime ? false : time.getTime() + (hms === 0 ? 0 : 8.64e7) < this.filterForm.startTime.getTime()
                    }
                },
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                },
                filterForm: {
                    warehouseCodes: '',
                    recordCodes: '', // 冲销单号
                    recordStatus: "", // 单状态
                    recordType: "", // 冲销类型
                    originRecordCodes: "", // 冲销原单号
                    outRecordCodes: "", // 外部单号
                    creator: "", // 创建人
                    startTime: "", // 创建开始日期
                    endTime: "", // 创建结束日期
                },
                filterFormOption: {
                    chargeGainstTypes: [{
                        label: '出库单冲销',
                        value: 1
                    },
                        {
                            label: '入库单冲销',
                            value: 2
                        }
                    ],
                    // 单据状态 1已新建 2已取消 3已确认 11已出库 12已入库 13已过账
                    orderStates: [{
                        label: '已新建',
                        value: 1
                    },
                        {
                            label: '已取消',
                            value: 2
                        },
                        {
                            label: '已确认',
                            value: 3
                        },
                        {
                            label: '已出库',
                            value: 11
                        },
                        {
                            label: '已入库',
                            value: 12
                        },
                        {
                            label: '已过账',
                            value: 13
                        }
                    ]
                }
            };
        },
        mounted: function () {
            this.init();
        },
        methods: {
            watchCreateTimeStart(n) {
                const et = this.filterForm.endTime
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
                    this.endTimePickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
                }
                if (n && et) {
                    const ymds = n.getFullYear() + n.getMonth() + n.getDate()
                    const ymde = et.getFullYear() + et.getMonth() + et.getDate()
                    const m = et.getMinutes()
                    const s = et.getSeconds()
                    const h = et.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h

                    if (ymds === ymde) {
                        this.startTimePickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
                    } else {
                        this.startTimePickerOptions.selectableRange = []
                    }
                } else {
                    this.startTimePickerOptions.selectableRange = []
                }
            },
            watchCreateTimeEnd(n) {
                const st = this.filterForm.startTime
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = m < 10 ? '0' + s : s
                    const hstr = m < 10 ? '0' + h : h
                    this.startTimePickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
                }
                if (n && st) {
                    const ymds = n.getFullYear() + n.getMonth() + n.getDate()
                    const ymde = st.getFullYear() + st.getMonth() + st.getDate()
                    const m = st.getMinutes()
                    const s = st.getSeconds()
                    const h = st.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h

                    if (ymds === ymde) {
                        this.endTimePickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
                    } else {
                        this.endTimePickerOptions.selectableRange = []
                    }
                } else {
                    this.endTimePickerOptions.selectableRange = []
                }
            },
            onClickSearchBtn() {
                clearTimeout(this.timer);
                this.paginationOption.page = 1;
                this.timer = setTimeout(() => {
                    this.fetchTableData();
                }, 300)
            },
            // 重置按钮触发
            onClickResetBtn() {
                this.filterForm.realWarehouseOutCode = ''
                this.filterForm.factoryCode = ''
                if (this.$refs.filterForm) {
                    this.$refs.filterForm.resetFields();
                }
            },
            onClickConfirmBtn() {
                if (this.selectedTableData.length === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择冲销单',
                        showClose: true
                    })
                } else {
                    this.$confirm(`确定要确认${this.selectedTableData.length}条冲销单?`, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    })
                        .then(() => {
                            this.$http({
                                method: 'post',
                                url: this.POOL_NAME + '/order/v1/reverse/confirmReverse',
                                data: this.selectedTableData.map(n => n.recordCode)
                            }).then(res => {
                                if (res.data.code === '0') {
                                    if (Array.isArray(res.data.data)) {
                                        const info = res.data.data.map(n => n.recordCode + ':' + n.message).join('</br>')
                                        this.$alert(info, '提示', {
                                            confirmButtonText: '确定',
                                            dangerouslyUseHTMLString: true,
                                            callback: action => {}
                                        });
                                    } else {
                                        this.$message({
                                            type: 'success',
                                            message: res.data.msg,
                                            showClose: true
                                        })
                                    }
                                    this.onClickSearchBtn()
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: res.data.msg,
                                        showClose: true
                                    })
                                }
                            }, (err) => {
                                this.$message({
                                    type: 'error',
                                    message: err.toString(),
                                    showClose: true
                                })
                            })
                        })
                        .catch(() => {
                            // console.log('取消') for debug
                        });
                }
            },
            onClickCancelBtn() {
                if (this.selectedTableData.length === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择冲销单号',
                        showClose: true
                    })
                } else {
                    this.$confirm(`确定要取消${this.selectedTableData.length}条冲销单?`, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    })
                        .then(() => {
                            this.$http({
                                method: 'post',
                                url: this.POOL_NAME + '/order/v1/reverse/cancelReverse',
                                data: this.selectedTableData.map(n => n.recordCode)
                            }).then(res => {
                                if (res.data.code === '0') {
                                    if (Array.isArray(res.data.data)) {
                                        const info = res.data.data.map(n => n.recordCode + ':' + n.message).join('</br>')
                                        this.$alert(info, '提示', {
                                            confirmButtonText: '确定',
                                            dangerouslyUseHTMLString: true,
                                            callback: action => {}
                                        });
                                    } else {
                                        this.$message({
                                            type: 'success',
                                            message: res.data.msg,
                                            showClose: true
                                        })
                                    }
                                    this.onClickSearchBtn()
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: res.data.msg,
                                        showClose: true
                                    })
                                }
                            }, (err) => {
                                this.$message({
                                    type: 'error',
                                    message: err.toString(),
                                    showClose: true
                                })
                            })
                        })
                        .catch(() => {
                            // console.log('取消') for debug
                        });
                }
            },
            // 添加按钮触发
            onClickAddBtn() {
                this.addDialogVisible = true;
            },
            // 编辑按钮触发
            onClickEditBtn(row) {
                this.editDialogVisible = true
                this.currentRow = row
            },
            // 查询详情按钮触发
            onClickViewDetailBtn(row) {
                this.detailDialogVisible = true
                this.currentRow = row
            },
            handleSizeChange(pageSize) {
                this.paginationOption.pageSize = pageSize
                this.paginationOption.page = 1
                this.fetchTableData();
            },
            handleChangeWarehouse(value) {
                const warehouse = this.filterFormOption.warehouses.find(n => n.realWarehouseCode === value)
                if (warehouse) {
                    this.filterForm.realWarehouseOutCode = warehouse.realWarehouseOutCode
                    this.filterForm.factoryCode = warehouse.factoryCode
                } else {
                    this.filterForm.realWarehouseOutCode = ''
                    this.filterForm.factoryCode = ''
                }
            },
            handleCurrentPageChange(page) {
                this.paginationOption.page = page
                this.fetchTableData();
            },
            handleSelectionChange(rows) {
                this.selectedTableData = rows;
            },
            fetchTableData(showMessage) {
                const params = {
                    warehouseCodes: this.filterForm.warehouseCodes ? this.filterForm.warehouseCodes.split(/[(\r\n)\r\n]+/) : [],
                    recordCodes: this.filterForm.recordCodes ? this.filterForm.recordCodes.split(/[(\r\n)\r\n]+/) : [],
                    recordStatus: this.filterForm.recordStatus,
                    recordType: this.filterForm.recordType,
                    originRecordCodes: this.filterForm.originRecordCodes ? this.filterForm.originRecordCodes.split(/[(\r\n)\r\n]+/) : [],
                    outRecordCodes: this.filterForm.outRecordCodes ? this.filterForm.outRecordCodes.split(/[(\r\n)\r\n]+/) : [],
                    creator: this.filterForm.creator,
                    startTime: this.filterForm.startTime,
                    endTime: this.filterForm.endTime,
                    pageSize: this.paginationOption.pageSize,
                    pageNum: this.paginationOption.page
                }
                this.loading = true
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/reverse/queryReversePage',
                    data: params,
                }).then(res => {
                    if (res.data.code === '0' && res.data.data && Array.isArray(res.data.data.list)) {
                        this.tableData = res.data.data.list;
                        this.paginationOption.total = res.data.data.total;
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.data.msg,
                            showClose: true
                        })
                        this.tableData = [];
                        this.paginationOption.total = 0;
                    }
                    this.loading = false
                }, () => {
                    this.loading = false
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            },
            init() {
                this.onClickSearchBtn()
            }
        },
        template: `
<div>
    <el-card shadow="never">
        <el-form label-width="135px" size="mini" :model="filterForm" ref="filterForm">
            <el-row>
                <el-col :span="6">
                    <el-form-item label="冲销单号：" prop="recordCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.recordCodes" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="单状态：" prop="recordStatus">
                        <el-select style="width:100%" v-model="filterForm.recordStatus" filterable clearable placeholder="请选择单状态">
                            <el-option v-for="item in filterFormOption.orderStates" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="创建时间：">
                        <el-col :span="11">
                            <el-form-item prop="startTime" style="marginBottom:0px">
                                <el-date-picker type="datetime" :picker-options="startTimePickerOptions" placeholder="选择日期" v-model="filterForm.startTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2" style="textAlign:center;">~</el-col>
                        <el-col :span="11">
                            <el-form-item prop="endTime" style="marginBottom:0px">
                                <el-date-picker type="datetime" :picker-options="endTimePickerOptions" placeholder="选择日期" v-model="filterForm.endTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="6">
                    <el-form-item label="仓库编号/名称：" prop="warehouseCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.warehouseCodes" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="冲销类型：" prop="recordType">
                        <el-select style="width:100%" v-model="filterForm.recordType" filterable clearable placeholder="请选择冲销类型">
                            <el-option v-for="item in filterFormOption.chargeGainstTypes" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="冲销原单号：" prop="originRecordCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.originRecordCodes" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="外部单号：" prop="outRecordCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.outRecordCodes" clearable></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="6">
                    <el-form-item label="创建人：" prop="creator">
                        <el-input style="width:100%" v-model="filterForm.creator" clearable></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :push="10" :span="14">
                    <el-button size="mini" type="primary" @click="onClickSearchBtn()">查询</el-button>
                    <el-button size="mini" @click="onClickResetBtn">重置</el-button>
                    <column-select :cols0="cols" :formId="titleConfigId" showName="标题配置" style="display:inline; marginLeft:10px;"></column-select>
                </el-col>
            </el-row>
        </el-form>
    </el-card>
    <el-card shadow="never">
        <div slot="header" class="clearfix">
            <el-button type="primary" size="mini" @click="onClickAddBtn">新建</el-button>
            <el-button type="primary" size="mini" @click="onClickConfirmBtn">确认</el-button>
            <el-button type="danger" size="mini" @click="onClickCancelBtn">取消</el-button>
        </div>
        <div v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading">
            <el-table border :data="tableData" style="width: 100%;minHeight:300px" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <template v-for="(item,index) in cols">
                    <el-table-column :key="index" :formatter="item.formatter ? item.formatter : null" :prop="item.fieldName" v-if="item.show == 1" header-align="center" align="center" :label="item.title" min-width="120">
                    </el-table-column>
                </template>
                <el-table-column header-align="center" fixed="right" align="center" label="操作" width="120">
                    <template slot-scope="scope">
                        <el-button type="text" @click="onClickEditBtn(scope.row)">编辑</el-button>
                        <el-button type="text" @click="onClickViewDetailBtn(scope.row)">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="padding:10px;textAlign:right;">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </div>
    </el-card>
    <detail-dialog :row="currentRow" :visible="detailDialogVisible" @dialog-close="detailDialogVisible=false" />
    <add-dialog :visible="addDialogVisible" @dialog-close="addDialogVisible=false" :callback="onClickSearchBtn" />
    <edit-dialog :row="currentRow" :visible="editDialogVisible" @dialog-close="editDialogVisible=false" :callback="onClickSearchBtn" />
</div>
        `
    });
});
