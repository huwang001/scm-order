lyfdefine([],function(addOrUpdate){
    return ({
        name: "scm-order-package-finish-list",
        watch: {
            'filterForm.packTimeStart': 'watchPackTimeStart',
            'filterForm.packTimeEnd': 'watchPackTimeEnd',
            'filterForm.createTimeStart': 'watchCreateTimeStart',
            'filterForm.createTimeEnd': 'watchCreateTimeEnd'
        },
        data: function () {
            return {
                titileConfigId: window.location.href + '/1',
                cols: [{
                    title: "包装任务单号",
                    fieldName: "taskCode",
                    show: 1,
                },
                    {
                        title: "包装需求单号",
                        fieldName: "requireCode",
                        show: 1
                    },
                    {
                        title: "前置单号",
                        fieldName: "recordCode",
                        show: 1
                    },
                    {
                        title: "包装组线",
                        fieldName: "packLine",
                        show: 1
                    },
                    {
                        title: "包装类型",
                        fieldName: "packType",
                        show: 1,
                        formatter: (row) => {
                            const item = this.filterFormOption.packageTypes.find(n => n.value == row.packType)
                            if (item) {
                                return item.label
                            }
                            return row.packType
                        }
                    },
                    {
                        title: "渠道",
                        fieldName: "channelCode",
                        show: 1,
                    },
                    {
                        title: "渠道名称",
                        fieldName: "channelName",
                        show: 1
                    },
                    {
                        title: "成品商品编码",
                        fieldName: "skuCode",
                        show: 1
                    },
                    {
                        title: "商品名称",
                        fieldName: "skuName",
                        show: 1
                    },
                    {
                        title: "实际包装日期",
                        fieldName: "packTime",
                        show: 1,
                    },
                    {
                        title: "计划包装数量",
                        fieldName: "planNum",
                        show: 1
                    },
                    {
                        title: "已包装数量",
                        fieldName: "packNum",
                        show: 1
                    },
                    {
                        title: "批次号",
                        fieldName: "batchNo",
                        show: 1
                    },
                    {
                        title: "备注",
                        fieldName: "remark",
                        show: 1,
                    },
                    {
                        title: "创建时间",
                        fieldName: "createTime",
                        show: 1
                    },
                    {
                        title: "更新人",
                        fieldName: "modifier",
                        show: 1
                    },
                    {
                        title: "更新时间",
                        fieldName: "updateTime",
                        show: 1
                    },
                ],
                POOL_NAME: "/scm-order-admin",
                timer: null,
                detailDialogVisible: false,
                selectedTableData: [],
                tableData: [],
                formatterPackType: (row) => {
                    const item = this.filterFormOption.packageTypes.find(n => n.value == row.packType)
                    if (item) {
                        return item.label
                    }
                    return row.packType
                },
                packTimeStartPickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        return !this.filterForm.packTimeEnd ? false : time.getTime() > this.filterForm.packTimeEnd.getTime()
                    }
                },
                packTimeEndPickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        let hms = 0
                        if (this.filterForm.packTimeStart) {
                            hms = this.filterForm.packTimeStart.getMinutes() + this.filterForm.packTimeStart.getSeconds() + this.filterForm.packTimeStart.getHours()
                        }
                        return !this.filterForm.packTimeStart ? false : time.getTime() + (hms === 0 ? 0 : 8.64e7) < this.filterForm.packTimeStart.getTime()
                    }
                },
                createTimeStartPickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        return !this.filterForm.createTimeEnd ? false : time.getTime() > this.filterForm.createTimeEnd.getTime()
                    }
                },
                createTimeEndPickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        let hms = 0
                        if (this.filterForm.createTimeStart) {
                            hms = this.filterForm.createTimeStart.getMinutes() + this.filterForm.createTimeStart.getSeconds() + this.filterForm.createTimeStart.getHours()
                        }
                        return !this.filterForm.createTimeStart ? false : time.getTime() + (hms === 0 ? 0 : 8.64e7) < this.filterForm.createTimeStart.getTime()
                    }
                },
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                },
                filterForm: {
                    channelNames: '',
                    requireCodeList: "", // 包装需求单号
                    channelCodeList: [], // 渠道列表
                    warehouseCode: "", // 包装车间
                    taskCodeList: "", // 包装任务号
                    packType: "", // 包装类型
                    skuCodeList: "", // 包含商品
                    packTimeStart: "", // 实际包装开始日期
                    packTimeEnd: "", // 实际包装结束日期
                    createTimeStart: this.getDate(-7), // 创建开始日期
                    createTimeEnd: this.getDate(23), // 创建结束日期
                },
                filterFormOption: {
                    warehouses: [],
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
                    ]
                }
            };
        },
        mounted: function () {
            this.init();
        },
        methods: {
            getDate(day) {
                const now = new Date().getTime()
                return new Date(now + 24 * 60 * 60 * 1000 * day)
            },
            watchPackTimeStart(n) {
                const et = this.filterForm.packTimeEnd
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
                    this.packTimeEndPickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
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
                        this.packTimeStartPickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
                    } else {
                        this.packTimeStartPickerOptions.selectableRange = []
                    }
                } else {
                    this.packTimeStartPickerOptions.selectableRange = []
                }
            },
            watchPackTimeEnd(n) {
                const st = this.filterForm.packTimeStart
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
                    this.packTimeStartPickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
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
                        this.packTimeEndPickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
                    } else {
                        this.packTimeEndPickerOptions.selectableRange = []
                    }
                } else {
                    this.packTimeEndPickerOptions.selectableRange = []
                }
            },
            watchCreateTimeStart(n) {
                const et = this.filterForm.createTimeEnd
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
                    this.createTimeEndPickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
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
                        this.createTimeStartPickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
                    } else {
                        this.createTimeStartPickerOptions.selectableRange = []
                    }
                } else {
                    this.createTimeStartPickerOptions.selectableRange = []
                }
            },
            watchCreateTimeEnd(n) {
                const st = this.filterForm.createTimeStart
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = m < 10 ? '0' + s : s
                    const hstr = m < 10 ? '0' + h : h
                    this.createTimeStartPickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
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
                        this.createTimeEndPickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
                    } else {
                        this.createTimeEndPickerOptions.selectableRange = []
                    }
                } else {
                    this.createTimeEndPickerOptions.selectableRange = []
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
                if (this.$refs.filterForm) {
                    this.$refs.filterForm.resetFields();
                }
            },
            onClickFinshAmountBtn() {
                this.detailDialogVisible = true
            },
            handleSizeChange(pageSize) {
                this.paginationOption.pageSize = pageSize
                this.paginationOption.page = 1
                this.fetchTableData();
            },
            handleCurrentPageChange(page) {
                this.paginationOption.page = page
                this.fetchTableData();
            },
            handleSelectionChange(rows) {
                this.selectedTableData = rows;
            },
            handleChangeChannel(channels) {
                if (Array.isArray(channels)) {
                    this.filterForm.channelNames = channels.map(n => n.channelName).join('\n')
                    this.filterForm.channelCodeList = channels.map(n => n.channelCode)
                }
            },
            fetchTableData(showMessage) {
                this.selectReceiverCode = []
                const params = {
                    requireCodeList: this.filterForm.requireCodeList ? this.filterForm.requireCodeList.split(/[(\r\n)\r\n]+/) : [],
                    channelCodeList: this.filterForm.channelCodeList,
                    warehouseCode: this.filterForm.warehouseCode,
                    taskCodeList: this.filterForm.taskCodeList ? this.filterForm.taskCodeList.split(/[(\r\n)\r\n]+/) : [],
                    packType: this.filterForm.packType,
                    skuCodeList: this.filterForm.skuCodeList ? this.filterForm.skuCodeList.split(/[(\r\n)\r\n]+/) : [],
                    packTimeStart: this.filterForm.packTimeStart,
                    packTimeEnd: this.filterForm.packTimeEnd,
                    createTimeStart: this.filterForm.createTimeStart,
                    createTimeEnd: this.filterForm.createTimeEnd,
                    pageSize: this.paginationOption.pageSize,
                    pageNum: this.paginationOption.page
                }
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/taskFinish/queryTaskFinishList',
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
                }, () => {
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            },
            init() {
                this.onClickSearchBtn()
                // 请求包装仓库
                this.$http({
                    method: 'get',
                    url: this.POOL_NAME + '/order/v1/real_warehouse/queryNotShopWarehouseByType',
                    params: {
                        realWarehouseType: 2
                    }
                }).then(res => {
                    if (res.data.code === '0' && Array.isArray(res.data.data)) {
                        this.filterFormOption.warehouses = res.data.data
                    }
                }, () => {
                    this.filterFormOption.warehouses = [];
                })
            }
        },
        template: `
<div data-scm-order-package class="scm-order-package-finish-list">
    <el-card>
        <el-form label-width="125px" size="mini" :model="filterForm" ref="filterForm">
            <el-row>
                <el-col :span="6">
                    <el-form-item label="包装需求单号" prop="requireCodeList">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.requireCodeList" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="包含商品" prop="skuCodeList">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.skuCodeList" clearable></el-input>
                    </el-form-item>
                </el-col>

                <el-col :span="6">
                    <el-form-item label="包装车间" prop="warehouseCode">
                        <el-select style="width:100%" v-model="filterForm.warehouseCode" filterable clearable placeholder="请选择包装车间">
                            <el-option v-for="item in filterFormOption.warehouses" :key="item.realWarehouseCode" :label="item.realWarehouseName + '('+item.realWarehouseCode+')'" :value="item.realWarehouseCode">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="包装任务单号" prop="taskCodeList">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.taskCodeList" clearable></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>

                <el-col :span="12">
                    <el-form-item label="创建时间">
                        <el-col :span="11">
                            <el-form-item prop="createTimeStart" style="marginBottom:0px;">
                                <el-date-picker type="datetime" :picker-options="createTimeStartPickerOptions" placeholder="选择日期" v-model="filterForm.createTimeStart" style="width: 100%;"></el-date-picker>
                            </el-form-item>

                        </el-col>
                        <el-col class="line" :span="2" style="textAlign:center;">~</el-col>
                        <el-col :span="11">
                            <el-form-item prop="createTimeEnd" style="marginBottom:0px;">
                                <el-date-picker type="datetime" :picker-options="createTimeEndPickerOptions" placeholder="选择日期" v-model="filterForm.createTimeEnd" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="实际包装日期">
                        <el-col :span="11">
                            <el-form-item prop="packTimeStart" style="marginBottom:0px;">
                                <el-date-picker type="datetime" :picker-options="packTimeStartPickerOptions" placeholder="选择日期" v-model="filterForm.packTimeStart" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col class="line" :span="2" style="textAlign:center;">~</el-col>
                        <el-col :span="11">
                            <el-form-item prop="packTimeEnd" style="marginBottom:0px;">
                                <el-date-picker type="datetime" :picker-options="packTimeEndPickerOptions" placeholder="选择日期" v-model="filterForm.packTimeEnd" style="width: 100%;"></el-date-picker>
                            </el-form-item>

                        </el-col>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="6">
                    <el-form-item label="包装类型" prop="packType">
                        <el-select style="width:100%" v-model="filterForm.packType" filterable clearable placeholder="请选择包装类型">
                            <el-option v-for="item in filterFormOption.packageTypes" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-row :gutter="5">
                        <el-col :span="18">
                            <el-form-item label="渠道" prop="channelNames">
                                <el-input type="textarea" disabled style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.channelNames" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <basedata-el-channel style="width:100%;" @change="handleChangeChannel" :clear="true"></basedata-el-channel>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
            <el-row>
                <el-col :push="10" :span="14">
                    <el-button size="mini" type="primary" @click="onClickSearchBtn()">查询</el-button>
                    <el-button size="mini" @click="onClickResetBtn">重置</el-button>
                    <column-select :cols0="cols" :formId="titileConfigId" showName="标题配置" style="display:inline; marginLeft:10px;"></column-select>
                </el-col>
            </el-row>
        </el-form>
    </el-card>
    <el-card class="request-list-card2">
        <div class="request-list-card2__body">
            <el-table border :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
                <template v-for="(item,index) in cols">
                    <el-table-column :key="index" :formatter="item.formatter ? item.formatter : null" :prop="item.fieldName" v-if="item.show == 1" header-align="center" align="center" :label="item.title" min-width="120">
                    </el-table-column>
                </template>
            </el-table>
        </div>
        <div style="padding:10px;textAlign:right;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper">
            </el-pagination>
        </div>
    </el-card>
</div>
        `
    });
});
