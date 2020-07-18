lyfdefine(['scm-order-package.style','scm-order-package-request-table','scm-order-package-request-add','scm-order-package-request-import'],function(style,RequestTable,AddDialog,ImportDialog){
    return ({
        name: "scm-order-package-request-list",
        components: {
            RequestTable,
            AddDialog,
            ImportDialog,
        },
        watch: {
            'filterForm.startTime': 'watchStartTime',
            'filterForm.endTime': 'watchEndTime',
            'filterForm.requiredStartTime': 'watchRequiredStartTime',
            'filterForm.requiredEndTime': 'watchRequiredEndTime'
        },
        data: function () {
            return {
                POOL_NAME: "/scm-order-admin",
                timer: null,
                exportLoading: false,
                loading: false,
                activeTab: '1',
                addDialogVisible: false,
                importDialogVisible: false,
                titileConfigId: window.location.href + '/1',
                cols: [{
                    title: "包装需求单号",
                    fieldName: "recordCode",
                    show: 1,
                    formatter(row) {
                        return row.recordCode
                    }
                },
                    {
                        title: "销售单号",
                        fieldName: "saleCode",
                        show: 1,
                        formatter(row) {
                            return row.saleCode
                        }
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
                        formatter(row) {
                            return row.channelCode
                        }
                    },
                    {
                        title: "渠道名称",
                        fieldName: "channelName",
                        show: 1,
                        formatter(row) {
                            return row.channelName
                        }
                    },
                    {
                        title: "需求日期",
                        fieldName: "demandDate",
                        show: 1,
                        formatter(row) {
                            return row.demandDate
                        }
                    },
                    {
                        title: "包装车间",
                        fieldName: "packWorkshopCode",
                        show: 1,
                        formatter(row) {
                            return row.packWorkshopCode
                        }
                    },
                    {
                        title: "领料仓库",
                        fieldName: "pickWorkshopCode",
                        show: 1,
                        formatter(row) {
                            return row.pickWorkshopCode
                        }
                    },
                    {
                        title: "备注",
                        fieldName: "remark",
                        show: 1,
                        formatter(row) {
                            return row.remark
                        }
                    },
                    {
                        title: "单据状态",
                        fieldName: "recordStatus",
                        show: 1,
                        formatter: (row) => {
                            const item = this.filterFormOption.packageStates.find(n => n.value == row.recordStatus)
                            if (item) {
                                return item.label
                            }
                            return row.recordStatus
                        },
                    },
                    {
                        title: "领料状态",
                        fieldName: "pickStatus",
                        show: 1,
                        formatter: (row) => {
                            const item = this.filterFormOption.pickingStates.find(n => n.value == row.pickStatus)
                            if (item) {
                                return item.label
                            }
                            return row.pickStatus
                        },
                    },
                    {
                        title: "创建方式",
                        fieldName: "createType",
                        show: 1,
                        formatter: (row) => {
                            // 创建方式 1：接口创建2：页面创建3：导入创建
                            const item = this.filterFormOption.createTypes.find(n => n.value == row.createType)
                            if (item) {
                                return item.label
                            }
                            return row.createType
                        },
                    },
                    {
                        title: "创建人",
                        fieldName: "creator",
                        show: 1,
                        formatter(row) {
                            return row.creator
                        }
                    },
                    {
                        title: "创建时间",
                        fieldName: "createTime",
                        show: 1,
                        formatter(row) {
                            return row.createTime
                        }
                    },
                    {
                        title: "更新人",
                        fieldName: "modifier",
                        show: 1,
                        formatter(row) {
                            return row.modifier
                        }
                    },
                    {
                        title: "更新时间",
                        fieldName: "updateTime",
                        show: 1,
                        formatter(row) {
                            return row.updateTime
                        }
                    },
                ],
                filterForm: {
                    recordCodes: "",
                    channelNames: "",
                    outFactoryCode: '',
                    outRealWarehouseCode: '',
                    recordStatus: "",
                    packType: "",
                    skuCodes: "",
                    saleCodes: "",
                    creator: "",
                    startTime: this.getDate(-7),
                    endTime: this.getDate(23),
                    requiredStartTime: '',
                    requiredEndTime: '',
                    pickStatus: "",
                    channelCodes: [],
                },
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
                requiredStartTimePickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        return !this.filterForm.requiredEndTime ? false : time.getTime() > this.filterForm.requiredEndTime.getTime()
                    }
                },
                requiredEndTimePickerOptions: {
                    selectableRange: [],
                    disabledDate: (time) => {
                        let hms = 0
                        if (this.filterForm.requiredStartTime) {
                            hms = this.filterForm.requiredStartTime.getMinutes() + this.filterForm.requiredStartTime.getSeconds() + this.filterForm.requiredStartTime.getHours()
                        }
                        return !this.filterForm.requiredStartTime ? false : time.getTime() + (hms === 0 ? 0 : 8.64e7) < this.filterForm.requiredStartTime.getTime()
                    }
                },
                filterFormOption: {
                    warehouses: [],
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
                    pickingStates: [{
                        label: '未领料',
                        value: 1
                    },
                        {
                            label: '已领料',
                            value: 2
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
            onClickSearchBtn() {
                clearTimeout(this.timer)
                this.timer = setTimeout(() => {
                    if (this.$refs.resquestTable) {
                        this.$refs.resquestTable.handleSearchData()
                    }
                }, 300)
            },
            // 重置按钮触发
            onClickResetBtn() {
                this.filterForm.outFactoryCode = ''
                this.filterForm.outRealWarehouseCode = ''
                this.filterForm.channelCodes = []
                this.filterForm.channelNames = ''
                if (this.$refs.filterForm) {
                    this.$refs.filterForm.resetFields();
                }
            },
            onClickConfirmBtn() {
                const requestTable = this.$refs.resquestTable
                if (requestTable) {
                    if (requestTable.selectedTableData.length === 0) {
                        this.$message({
                            type: 'warning',
                            message: '请选择需求单',
                            showClose: true
                        })
                    } else {
                        // const hasConfirmData = requestTable.selectedTableData.findIndex(n => n.recordStatus === 1 )>-1
                        // if(hasConfirmData){

                        // }
                        this.$confirm(`确定要确认${requestTable.selectedTableData.length}条需求单?`, '警告', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                        })
                            .then(() => {
                                this.$http({
                                    method: 'post',
                                    url: this.POOL_NAME + '/order/v1/demand/updatePackDemandStatus',
                                    data: requestTable.selectedTableData.map(n => n.recordCode)
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
                                        requestTable.handleSearchData()
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
                }
            },
            onClickCancelBtn() {
                const requestTable = this.$refs.resquestTable
                if (requestTable) {
                    if (requestTable.selectedTableData.length === 0) {
                        this.$message({
                            type: 'warning',
                            message: '请选择需求单',
                            showClose: true
                        })
                    } else {
                        // const hasConfirmData = requestTable.selectedTableData.findIndex(n => n.recordStatus === 1 )>-1
                        // if(hasConfirmData){

                        // }
                        this.$confirm(`确定要取消${requestTable.selectedTableData.length}条需求单?`, '警告', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                        })
                            .then(() => {
                                this.$http({
                                    method: 'post',
                                    url: this.POOL_NAME + '/order/v1/demand/batchCancelDemand',
                                    data: requestTable.selectedTableData.map(n => n.recordCode)
                                }).then(res => {
                                    if (res.data.code === '0') {
                                        if (Array.isArray(res.data.data)) {
                                            const info = res.data.data.map(n => n.requireCode + ':' + n.message).join('</br>')
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
                                        requestTable.handleSearchData()
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
                }
            },
            // 添加按钮触发
            onClickAddBtn() {
                this.addDialogVisible = true;
            },
            // 批量导入按钮触发
            onClickBactchImportBtn() {
                this.importDialogVisible = true
            },
            // 原材料调拨按钮触发
            onClickMaterialBtn() {
                this.materialDialogVisible = true
            },
            // 导出按钮触发
            onClickExportBtn() {
                clearTimeout(this.timer);
                this.timer = setTimeout(() => {
                    const params = {
                        recordCodes: this.filterForm.recordCodes ? this.filterForm.recordCodes.split(/[(\r\n)\r\n]+/) : [],
                        outRealWarehouseCode: this.filterForm.outRealWarehouseCode,
                        recordStatus: this.filterForm.recordStatus,
                        packType: this.filterForm.packType,
                        skuCodes: this.filterForm.skuCodes ? this.filterForm.skuCodes.split(/[(\r\n)\r\n]+/) : [],
                        saleCodes: this.filterForm.saleCodes ? this.filterForm.saleCodes.split(/[(\r\n)\r\n]+/) : [],
                        creator: this.filterForm.creator,
                        startTime: this.filterForm.startTime,
                        endTime: this.filterForm.endTime,
                        requiredStartTime: this.filterForm.requiredStartTime,
                        requiredEndTime: this.filterForm.requiredEndTime,
                        pickStatus: this.filterForm.pickStatus,
                        channelCodes: this.filterForm.channelCodes,
                    }
                    this.exportLoading = true
                    this.$http({
                        method: "post",
                        url: this.POOL_NAME + "/order/v1/demand/exportPackDemand",
                        data: params,
                        responseType: 'blob'
                    })
                        .then(
                            res => {

                                if (res.headers && (res.headers['content-type'] === 'application/vnd.ms-excel;charset=utf8' || res.headers['content-type'] === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                                    const elementA = document.createElement('a');
                                    const disposition = res.headers['content-disposition'];
                                    const contentMsg = res.headers['content-msg'];
                                    const contentOtherMsg = res.headers['content-other-msg'];
                                    const fileName = decodeURIComponent(disposition.substring(disposition.indexOf('filename=') + 9, disposition.length));
                                    elementA.download = `${fileName}`;
                                    const blob = new Blob([res.data])
                                    elementA.href = window.URL.createObjectURL(blob);
                                    elementA.dispatchEvent(new MouseEvent('click'));
                                }
                                this.exportLoading = false
                            })
                        .catch(result => {
                            this.exportLoading = false
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因：" + _data.msg,
                                type: 'error',
                                duration: 1500,
                            });
                        })
                }, 300)
            },
            handleChangeWarehouse(value) {
                const warehouse = this.filterFormOption.warehouses.find(n => n.realWarehouseCode === value)
                if (warehouse) {
                    this.filterForm.outFactoryCode = warehouse.factoryCode
                    this.filterForm.outRealWarehouseCode = warehouse.realWarehouseOutCode
                } else {
                    this.filterForm.outFactoryCode = ''
                    this.filterForm.outRealWarehouseCode = ''
                }
            },
            handleChangeChannel(channels) {
                if (Array.isArray(channels)) {
                    this.filterForm.channelNames = channels.map(n => n.channelName).join('\n')
                    this.filterForm.channelCodes = channels.map(n => n.channelCode)
                }
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
            },
            watchStartTime(n) {
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
            watchEndTime(n) {
                const st = this.filterForm.startTime
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
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
            watchRequiredStartTime(n) {
                const et = this.filterForm.requiredEndTime
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = s < 10 ? '0' + s : s
                    const hstr = h < 10 ? '0' + h : h
                    this.requiredEndTimePickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
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
                        this.requiredStartTimePickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
                    } else {
                        this.requiredStartTimePickerOptions.selectableRange = []
                    }
                } else {
                    this.requiredStartTimePickerOptions.selectableRange = []
                }
            },
            watchRequiredEndTime(n) {
                const st = this.filterForm.requiredStartTime
                if (n) {
                    const m = n.getMinutes()
                    const s = n.getSeconds()
                    const h = n.getHours()
                    const mstr = m < 10 ? '0' + m : m
                    const sstr = m < 10 ? '0' + s : s
                    const hstr = m < 10 ? '0' + h : h
                    this.requiredStartTimePickerOptions.selectableRange = ['00:00:00' + " - " + hstr + ':' + mstr + ':' + sstr]
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
                        this.requiredEndTimePickerOptions.selectableRange = [hstr + ':' + mstr + ':' + sstr + " - " + '23:59:59']
                    } else {
                        this.requiredEndTimePickerOptions.selectableRange = []
                    }
                } else {
                    this.requiredEndTimePickerOptions.selectableRange = []
                }
            },
        },
        template: `
<div data-scm-order-package class="scm-order-package-request-list">
    <el-card>
        <el-form label-width="125px" size="mini" :model="filterForm" ref="filterForm">
            <el-row>
                <el-col :span="6">
                    <el-form-item label="包装需求单号" prop="recordCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.recordCodes"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="领料状态" prop="pickStatus">
                        <el-select style="width:100%" v-model="filterForm.pickStatus" filterable clearable placeholder="请选择领料状态">
                            <el-option v-for="item in filterFormOption.pickingStates" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="包装仓库" prop="realWarehouseCode">
                        <el-select style="width:100%" v-model="filterForm.realWarehouseCode" @change="handleChangeWarehouse" clearable placeholder="请选择包装仓库">
                            <el-option v-for="item in filterFormOption.warehouses" :key="item.realWarehouseCode" :label="item.realWarehouseName+ '('+item.realWarehouseCode+')'" :value="item.realWarehouseCode">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="包装状态" prop="recordStatus">
                        <el-select style="width:100%" v-model="filterForm.recordStatus" filterable clearable placeholder="请选择">
                            <el-option v-for="item in filterFormOption.packageStates" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
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
                <el-col :span="6">
                    <el-form-item label="包含商品" prop="skuCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.skuCodes"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="销售订单号" prop="saleCodes">
                        <el-input type="textarea" style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.saleCodes" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="创建人" prop="creator">
                        <el-input style="width:100%" v-model="filterForm.creator" clearable></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="创建时间">
                        <el-col :span="11">
                            <el-form-item prop="startTime" style="marginBottom:0px;">
                                <el-date-picker :clearable="false" type="datetime" :picker-options="startTimePickerOptions" placeholder="选择日期" v-model="filterForm.startTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col class="line" :span="2" style="textAlign:center;">~</el-col>
                        <el-col :span="11">
                            <el-form-item prop="endTime" style="marginBottom:0px;">
                                <el-date-picker :clearable="false" type="datetime" :picker-options="endTimePickerOptions" placeholder="选择日期" v-model="filterForm.endTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>

                        </el-col>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="需求完成日期">
                        <el-col :span="11">
                            <el-form-item prop="requiredStartTime" style="marginBottom:0px;">
                                <el-date-picker :clearable="false" type="date" :picker-options="requiredStartTimePickerOptions" placeholder="选择日期" v-model="filterForm.requiredStartTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>

                        </el-col>
                        <el-col class="line" :span="2" style="textAlign:center;">~</el-col>
                        <el-col :span="11">
                            <el-form-item prop="requiredEndTime" style="marginBottom:0px;">
                                <el-date-picker :clearable="false" type="date" :picker-options="requiredEndTimePickerOptions" placeholder="选择日期" v-model="filterForm.requiredEndTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="13">
                    <el-row :gutter="5">
                        <el-col :span="18">
                            <el-form-item label="渠道" prop="channelNames">
                                <el-input type="textarea" disabled style="width:100%" :autosize="{ minRows: 1, maxRows: 4}" v-model="filterForm.channelNames"></el-input>
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
        <div slot="header" class="clearfix">
            <el-button type="primary" size="mini" @click="onClickAddBtn">新建</el-button>
            <el-button type="primary" size="mini" @click="onClickConfirmBtn">确认</el-button>
            <el-button type="danger" size="mini" @click="onClickCancelBtn">取消</el-button>
            <el-button type="primary" size="mini" @click="onClickBactchImportBtn">批量导入</el-button>
            <el-button type="primary" size="mini" @click="onClickExportBtn" :disabled="exportLoading" :loading="exportLoading">导出</el-button>
        </div>
        <div class="request-list-card2__body">
            <request-table :columns="cols" :baseFilterForm="filterForm" ref="resquestTable" />
        </div>
    </el-card>
    <add-dialog :visible="addDialogVisible" @dialog-close="addDialogVisible=false" :callback="onClickSearchBtn" />
    <import-dialog :visible="importDialogVisible" @dialog-close="importDialogVisible=false" :callback="onClickSearchBtn" />
</div>
        `
    });
});
