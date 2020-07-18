lyfdefine(['sale_tob_warehouseRecord_detail', 'validate'], function (addOrUpdate, validate) {
    return ({
        data: function data() {
            return {
                dataForm: {
                    recordCode: null,//发货单号
                    frontRecordCode: null,//前置单号
                    sapPoNo: null,
                    outRecordCode: null,
                    tmsRecordCode:null,
                    inRealWarehouseId: null,//入门店id
                    skuCode: null,//商品编码
                    recordType: null,//单据类型
                    outRealWarehouseCode: null,//发货仓库id
                    startTime: null,
                    endTime: null,
                    recordStatus: null,//出库单状态
                    pageSize: 10,
                    pageIndex: 1
                },
                channelList: [],
                menuKey: 1,
                channelListString: null,
                recordTypeList: [],
                recordTypeChoose:[],
                recordStatusList: [],
                recordStatusChoose:[],
                wmsSyncStatusList:[],
                dataList: [],
                dataListLoading: false,
                addOrUpdateVisible: true,
                tableList: [],//标题自定义信息集合
                checkedTitles: [],
                originTableTitles: [],
                tableTitles: [],
                lastCheckedTitle: [],
                oldCheckedTitles: [],
                oldTitleOrder: {},
                isRequest: false,
                tableCode: 'sale_tob_warehouseRecord',
                multipleSelection: [],
                visibleError: false,
                message: null,
                pickerOptions : {

                    disabledDate: time => {
                        let secondNum = 3600 * 24 * 29 * 1000;
                        let _secondNum = 3600 * 24  * 1000;
                        if (this.dataForm.startTime) {
                            let _time =this.dataForm.startTime;
                            if(!(this.dataForm.startTime instanceof  Date)){
                                _time = new Date(this.dataForm.startTime)
                            }
                            return (time.getTime() > _time.getTime() + secondNum || time.getTime() <
                                _time.getTime() );
                        }
                    }
                }
            };
        },

        components: {
            'addOrUpdate': addOrUpdate,
        },

        methods: {
            menuTree: function () {
                ++this.menuKey
            },
            dateFormat:function(date ,_time) {
                var date=new Date(date);
                var year=date.getFullYear();
                /* 在日期格式中，月份是从0开始的，因此要加0
                 * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
                 * */
                var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
                var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
                // 拼接
                return year+"-"+month+"-"+day+" "+_time;
            },
            // 获取数据列表
            getDataList: function getDataList() {
                var _this = this;
                if (!this.dataForm.startTime || !this.dataForm.endTime) {
                    this.$message({
                        message: '时间必填!',
                        type: 'warn'
                    });
                    return;
                }
                let start = new Date(this.dataForm.startTime);
                let end = new Date(this.dataForm.endTime);
                let max = 3600 * 24 * 30 *1000;
                if(end.getTime() - start.getTime() > max){
                    this.$message({
                        message: '时间跨度必须在30天以内!',
                        type: 'warn'
                    });
                    return;
                }
                _this.dataListLoading = true;

                _this.$http.post('/scm-order-admin/order/v1/warehouse_sale_tob/list',
                    _this.dataForm,
                ).then(function (_ref) {
                    var data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                    }
                    _this.dataListLoading = false;
                })

            },
            upTmsOrder: function() {
                var _this = this;
                if (0 == this.multipleSelection.length) {
                    this.$message({
                        message: '请至少选择一个单据进行修改',
                        type: 'warn'
                    });
                    return;
                }
                let errorCode = "";
                //判断状态
                this.multipleSelection.forEach(multiSelect => {
                    if(!multiSelect.tmsRecordCode) {
                        errorCode = errorCode + multiSelect.recordCode + ",";
                    }
                });
                if(errorCode != ""){
                    this.message = "如下订单: " + errorCode + "缺少派车单号";
                    _this.visibleError = true;
                    return;
                }
                _this.dataListLoading = true;
                this.$prompt("派车单号", "派车单修改", {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^\d{10}$/,
                    inputErrorMessage: '派车单号必须是10位数字'
                }).then(({ value }) => {
                    var idsStr = [];
                    let data={};
                    this.multipleSelection.forEach(multiSelect => {
                        idsStr.push(multiSelect.id);
                    });
                    data.recordIdList = idsStr;
                    data.tmsCode = value;
                    this.$http({
                        url: '/scm-order-admin/order/v1/warehouse_sale_tob/updateWarehouseSaleTobTmsOrder',
                        method: 'post',
                        data: data
                    }).then(function (_ref2) {
                        var data = _ref2.data;
                        if(data && data.code == 0) {
                            var map = data.data;
                            _this.$message({
                                type: 'success',
                                message: map.msg
                            });
                            _this.dataList.forEach(data => {
                                if(map.suCode.indexOf(data.recordCode) != -1) {
                                    data.tmsRecordCode = value;
                                }
                            });
                        } else {
                            _this.$message.error(data.msg);
                        }
                        _this.dataListLoading = false;
                    }).catch(function (error) {
                        _this.dataListLoading = false;
                        this.$message({
                            type: 'info',
                            message: '系统异常'
                        });
                        console.log(error);
                    });
                }).catch(() => {
                    _this.dataListLoading = false;
                    this.$message({
                        type: 'info',
                        message: '取消更新'
                    });
                });
            },
            closeDialogError(done) {
                this.visibleError = false;
            },
            batchCancle: function() {
                var _this = this;
                if (0 == this.multipleSelection.length) {
                    this.$message({
                        message: '请至少选择一个单据进行修改',
                        type: 'warn'
                    });
                    return;
                }
                var codes = "";
                this.multipleSelection.forEach(multiSelect => {
                    if (multiSelect.outRecordCode === null){
                        return;
                    }
                    codes = codes + multiSelect.outRecordCode + ",";
                });
                codes = codes.replace(/<br\/>/ig,',');
                if (codes === null || codes === ""){
                    this.$message({
                        message: '外部系统单据编号不能为空',
                        type: 'warn'
                    });
                    return;
                }
                _this.$confirm('此操作将取消单据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http({
                        url: '/scm-order-admin/order/v1/warehouse_sale_tob/cancelReplenishBatch',
                        method: 'post',
                        data: codes
                    }).then(function (_ref2) {
                        var data = _ref2.data;
                        if(data && data.code == 0) {
                            var map = data.data;
                            _this.$message({
                                type: 'success',
                                message: map
                            });
                        } else {
                            _this.message = "如下订单: " + data.msg;                            _this.visibleError = true;
                            _this.visibleError = true;
                        }
                        _this.getDataList();
                        _this.dataListLoading = false;
                    }).catch(function (error) {
                        _this.dataListLoading = false;
                        this.$message({
                            type: 'info',
                            message: '系统异常'
                        });
                        console.log(error);
                    });
                }).catch(() => {

                });
            },
            init: function init() {
                var _this = this;
                //非Z补货出库单列表
                _this.getReplenishOutWarehouseRecordList();
                //非Z补货出库单的状态
                _this.getReplenishOutWarehouseRecordStatusList();
                //wms同步状态
                this.dataForm.endTime = this.dateFormat(new Date,"23:59:59");
                this.dataForm.startTime = this.dateFormat(new Date,"00:00:00");


            },
            //非Z补货出库单列表
            getReplenishOutWarehouseRecordList: function getReplenishOutWarehouseRecordList() {
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/warehouse_record/getReplenishOutWarehouseRecordList'
                ).then(function (res) {
                    if (res.status == '200') {
                        _this.recordTypeList = res.data.data;
                        for (let v of Object.keys(_this.recordTypeList)) {
                            _this.recordTypeList[v]=_this.recordTypeList[v]+'('+v+')';
                        }
                        _this.recordTypeChoose=_this.recordTypeList;
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },
            //非Z补货出库单的状态
            getReplenishOutWarehouseRecordStatusList: function getReplenishOutWarehouseRecordStatusList() {
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/warehouse_record/getReplenishOutWarehouseRecordStatusList'
                ).then(function (res) {
                    if (res.status == '200') {
                        _this.recordStatusList = res.data.data;
                        for (let v of Object.keys(_this.recordStatusList)) {
                            _this.recordStatusList[v]=_this.recordStatusList[v]+'('+v+')';
                        }
                        let choose=_this.recordStatusList;
                        _this.recordStatusChoose=choose;
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },
            // 每页数
            sizeChangeHandle: function sizeChangeHandle(val) {
                this.dataForm.pageSize = val;
                this.dataForm.pageIndex = 1;
                this.getDataList();
            },

            // 当前页
            currentChangeHandle: function currentChangeHandle(val) {
                this.dataForm.pageIndex = val;
                this.getDataList();
            },
            seeDetail: function seeDetail(id) {
                this.addOrUpdateVisible = true;
                this.$refs.addOrUpdate.init(id);
            },
            toDisparityHandle:function toDisparityHandle(id){
                let _this = this;
                _this.$confirm('此操作是门店拒收落差异单操作,后续将无法再确认收货, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http({
                        url: '/stock-admin/stock/v1/shop_replenish/rejectConfirmDisparity?id=' + id,
                        method: 'post'
                    }).then(function (_ref2) {
                        var data = _ref2.data;
                        if(data && data.code == 0) {
                            _this.$message({
                                type: 'success',
                                message: '整单拒收落差异单处理成功'
                            });
                        } else {
                            _this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        _this.getDataList();
                        _this.dataListLoading = false;
                    }).catch(function (error) {
                        _this.dataListLoading = false;
                        _this.$message({
                            type: 'info',
                            message: '系统异常'
                        });
                        console.log(error);
                    });
                }).catch(() => {

                });
            }
            ,

            getIndex: function getIndex(index) {

                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },

            resetForm: function (form) {
                var _this = this;
                Object.keys(_this.dataForm).forEach(function (key) {
                    if (key !== 'totalPage' && key !== 'pageIndex' && key !== "pageSize" && key!=='startTime' && key !=='endTime') {
                        _this.dataForm[key] = null;
                    }
                });
                _this.dataForm.endTime = this.dateFormat( new Date,"23:59:59");
                _this.dataForm.startTime = this.dateFormat(new Date,"00:00:00");
                _this.channelListString = null;
                _this.recordTypeList = [];
                _this.recordStatusList = [];
                _this.channelList = [];
                _this.menuTree();
                _this.init();
            },
            chosetype(event) {
                this.channelList = event
                var namelist = [], typelist = [];
                for (let i = 0; i < this.channelList.length; i++) {
                    namelist.push(this.channelList[i].channelName)
                    typelist.push(this.channelList[i].channelCode)
                }
                this.dataForm.channelCodes = typelist.join(',')
                this.channelListString = namelist.join(',')
            }, updateChangedTitles: function (value) {
                var _this = this;
                if (value) {
                    //存储此时的标题已选状态
                    _this.oldCheckedTitles = _this.checkedTitles;
                    //存储标题顺序
                    _this.oldTitleOrder = new Map();
                    _this.originTableTitles.forEach(item => {
                        _this.oldTitleOrder.set(item.rowCode, item.orderNum)
                    })
                    return;
                }
                if (_this.isRequest) {
                    _this.isRequest = false;
                } else {
                    _this.dropChange()
                }
            },
            //初始定义标题信息
            getDefinedTitles() {
                //从后台获取数据
                var _this = this;
                _this.$http({
                    url: '/scm-order-admin/order/v1/customize_table_row/getDetailByTableCodeAndUserId',
                    method: 'get',
                    params: _this.$http.adornParams({
                        "tableCode": _this.tableCode,
                    })
                }).then(function (_ref) {
                    if (_ref.data.data && _ref.data.data.length > 0) {
                        _this.handleOriginTitlesAndRegularTitles(_ref.data.data);
                        _this.tableList = _this.originTableTitles
                    } else {
                        _this.tableList = _this.originTableTitles
                    }
                    //标题排序----决定多选框的排序
                    _this.tableList.sort(function (a, b) {
                        return a.orderNum - b.orderNum
                    })
                    //去除排序重复标题
                    var index = 1;
                    for (var i = 0; i < _this.tableList.length - 1; i++) {
                        if (_this.tableList[i].orderNum == _this.tableList[i + 1].orderNum) {
                            for (var j = i + 1; j < _this.tableList.length; j++) {
                                _this.tableList[j].orderNum = _this.tableList[i].orderNum + index;
                                index++;
                            }
                            break;
                        }
                    }
                    //待获取成功后，写进标题改变至table中
                    _this.$nextTick(function () {
                        _this.changeTitle();
                    })
                })
                return;
            },
            changeTitle() {
                this.tableTitles = [];
                this.checkedTitles.forEach(e => {
                    this.tableList.forEach(b => {
                        if (e == b.title) {
                            this.tableTitles.push(b);
                        }
                    })
                })
                //设置空白列
                for (var i = this.tableTitles.length; i < this.tableList.length; i++) {
                    var tmep = {prop: null, index: i};
                    this.tableTitles.push(tmep)
                }
            },
            //复选框被选中或去除选中状态时候触发的事件
            checkboxChanged: function () {
                var _this = this;
                if (!_this.checkedTitles || _this.checkedTitles.length == 0) {
                    _this.checkedTitles = _this.lastCheckedTitle;
                    _this.$message({
                        message: "至少保留一项标题",
                        type: 'error',
                        duration: 1500,
                    });
                }
            },
            //初始化前端标题数据
            initOriginTableTitles() {
                this.originTableTitles = [
                    {title: "后置单id", rowCode: "id", prop: "id", isShow: 0, orderNum: 0},
                    {title: "发货单号", rowCode: "recordCode", prop: "recordCode", isShow: 1, orderNum: 1},
                    {title: "订单状态", rowCode: "recordStatusName", prop: "recordStatusName", isShow: 1, orderNum: 2},
                    {title: "SAP采购单号", rowCode: "sapPoNo", prop: "sapPoNo", isShow: 1, orderNum: 4},
                    {title: "交易中心po单据号", rowCode: "outRecordCode", prop: "outRecordCode", isShow: 1, orderNum: 5},
                    {
                        title: "前置单号",
                        rowCode: "frontRecordCodes",
                        prop: "frontRecordCodes",
                        isShow: 1,
                        orderNum: 6
                    },
                    {
                        title: "派车单号",
                        rowCode: "tmsRecordCode",
                        prop: "tmsRecordCode",
                        isShow: 1,
                        orderNum: 7
                    },
                    {
                        title: "接收门店",
                        rowCode: "inRealWarehouseName",
                        prop: "inRealWarehouseName",
                        isShow: 1,
                        orderNum: 8
                    },
                    {title: "接收门店编号", rowCode: "shopCode", prop: "shopCode", isShow: 1, orderNum: 1},
                    {
                        title: "单据类型",
                        rowCode: "recordTypeName",
                        prop: "recordTypeName",
                        isShow: 1,
                        orderNum: 9
                    },
                    {
                        title: "渠道名称",
                        rowCode: "channelName",
                        prop: "channelName",
                        isShow: 1,
                        orderNum: 10
                    },
                    {
                        title: "供应工厂",
                        rowCode: "deliveryFactory",
                        prop: "deliveryFactory",
                        isShow: 1,
                        orderNum: 11
                    },
                    {
                        title: "发货仓",
                        rowCode: "outRealWarehouseName",
                        prop: "outRealWarehouseName",
                        isShow: 1,
                        orderNum: 12
                    },
                    {
                        title: "创建时间",
                        rowCode: "createTime",
                        prop: "createTime",
                        isShow: 1,
                        orderNum: 13
                    },
                    {
                        title: "更新人",
                        rowCode: "modifyEmpNum",
                        prop: "modifyEmpNum",
                        isShow: 1,
                        orderNum: 15
                    },
                    {
                        title: "更新时间",
                        rowCode: "updateTime",
                        prop: "updateTime",
                        isShow: 1,
                        orderNum: 16
                    }
                    
                    ]
            },
            handleOriginTitlesAndRegularTitles(tableList) {
                //以前端标题为准,控制排序规则，是否勾选
                var _this = this;
                for (var i = 0; i < _this.originTableTitles.length; i++) {
                    for (var j = 0; j < tableList.length; j++) {
                        if (_this.originTableTitles[i].rowCode == tableList[j].rowCode) {
                            _this.originTableTitles[i].isShow = tableList[j].isShow;
                            _this.originTableTitles[i].orderNum = tableList[j].orderNum;
                            break;
                        }
                    }

                }
            },
            //菜单上移
            sortUp: function (title) {
                let cu = 0;
                for (let i = 0; i < this.tableList.length; i++) {
                    cu++;
                    if (this.tableList[i].title == title) {
                        break;
                    }
                }
                var tempOrderNum = this.tableList[cu - 2].orderNum;
                this.tableList[cu - 2].orderNum = this.tableList[cu - 1].orderNum;
                this.tableList[cu - 1].orderNum = tempOrderNum;
                let e = this.tableList[cu - 2];//前一个
                let b = this.tableList[cu - 1];//后一个
                this.tableList.splice(cu - 2, 2, b, e);
            },
            sortDown: function (title) {
                let cu = 0;
                for (let i = 0; i < this.tableList.length; i++) {
                    cu++;
                    if (this.tableList[i].title == title) {
                        break;
                    }
                }
                var tempOrderNum = this.tableList[cu - 1].orderNum;
                this.tableList[cu - 1].orderNum = this.tableList[cu].orderNum;
                this.tableList[cu].orderNum = tempOrderNum;
                let e = this.tableList[cu - 1];//前一个
                let b = this.tableList[cu];//后一个
                this.tableList.splice(cu - 1, 2, b, e);
            },
            //取消改变
            dropChange: function () {
                var _this = this;
                _this.displayNone();
//首先去除勾选带来的影响
                _this.checkedTitles = _this.oldCheckedTitles
//去掉排序带来的影响
                for (var i = 0; i < _this.tableList.length; i++) {
                    _this.tableList[i].orderNum = _this.oldTitleOrder.get(_this.tableList[i].rowCode)
                }
                //改变顺序
                _this.tableList.sort(function (a, b) {
                    return a.orderNum - b.orderNum
                })
            },
            confirmChange: function () {
                var _this = this;
                _this.displayNone();
                //判断顺序是否变过
                for (var i = 0; i < _this.originTableTitles.length; i++) {
                    if (_this.originTableTitles[i].orderNum != _this.oldTitleOrder.get(_this.originTableTitles[i].rowCode)) {
                        _this.isRequest = true;
                        break;
                    }
                }
                //判断是否勾选改变过
                var isContained = false;
                if (!_this.isRequest) {
                    for (var i = 0; i < _this.oldCheckedTitles.length; i++) {
                        for (var j = 0; j < _this.checkedTitles.length; j++) {
                            if (_this.oldCheckedTitles[i] == _this.checkedTitles[j]) {
                                isContained = true;
                                break;
                            }
                        }
                        if (!isContained) {
                            _this.isRequest = true;
                            break;
                        } else {
                            isContained = false;
                        }
                    }
                }
                if (!_this.isRequest) {
                    isContained = false;
                    for (var i = 0; i < _this.checkedTitles.length; i++) {
                        for (var j = 0; j < _this.oldCheckedTitles.length; j++) {
                            if (_this.oldCheckedTitles[i] == _this.checkedTitles[j]) {
                                isContained = true;
                                break;
                            }
                        }
                        if (!isContained) {
                            _this.isRequest = true;
                            break;
                        } else {
                            isContained = false;
                        }
                    }
                }
                if (_this.isRequest) {
                    //生成所有标题信息对象
                    var flag = false;
                    for (var i = 0; i < _this.originTableTitles.length; i++) {
                        _this.originTableTitles[i].tableCode = _this.tableCode;
                        for (var j = 0; j < _this.checkedTitles.length; j++) {
                            if (_this.originTableTitles[i].title == _this.checkedTitles[j]) {
                                _this.originTableTitles[i].isShow = 1;
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            flag = false;
                        } else {
                            _this.originTableTitles[i].isShow = 0;
                        }
                    }
                    _this.$http.post('/scm-order-admin/order/v1/customize_table_row/updateDetailByDates', _this.originTableTitles
                    ).then(function (_ref2) {
                        if (_ref2.data.code && _ref2.data.code == "0") {
                            //如果请求并成功后，首先确定其正确顺序，然后修改表格展示
                            _this.checkedTitles.sort(function (a, b) {
                                var lena;
                                var lenb;
                                for (var i = 0; i < _this.tableList.length; i++) {
                                    if (_this.tableList[i].title == a) {
                                        lena = _this.tableList[i].orderNum;
                                    }
                                    if (_this.tableList[i].title == b) {
                                        lenb = _this.tableList[i].orderNum;
                                    }
                                }
                                return lena - lenb;
                            })
                            _this.changeTitle()
                            _this.getDataList()
                        } else {
                            _this.$message({
                                message: _ref2.data.data.msg,
                                type: 'error',
                                duration: 1500
                            });
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            },
            checkboxT(row, rowIndex) {
                if(row.recordStatus == 2 || row.recordStatus == 11 ||  row.recordStatus == 12){
                    return false;
                }else{
                    return true;
                }
            },
            handleSelectionChange: function handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            displayBlock() {
                this.$refs.messageDrop.show();
            },
            displayNone() {
                this.$refs.messageDrop.hide();
            },
            remoteMethodRecordType(query) {
                let _this=this;
                _this.recordTypeList;
                if (query !== '') {
                    let item=_this.recordTypeList[query];
                    if(item){
                        _this.dataForm.recordType=query;
                    }
                }else {
                    _this.recordTypeList=_this.recordTypeChoose;
                }
            },
            remoteMethodStatus(query) {
                let _this=this;
                if (query !== '') {
                    let item=_this.recordStatusList[query];
                    if(item){
                        _this.dataForm.recordStatus=query;
                    }
                }else {
                    _this.recordStatusList=_this.recordStatusChoose;
                }
            }
        },

        created() {
            this.init();
            this.getDataList();
            this.initOriginTableTitles();
            this.getDefinedTitles();//获取自定义标题
        },
        watch: {
            checkedTitles(newVal) {
                if (newVal && newVal.length == 1) {
                    this.lastCheckedTitle = newVal;
                }
            }
        },
        template: `
 <div class="mod-config">
   <el-form :inline="true" :model="dataForm" ref="dataForm" :key="menuKey"> 
      <el-form-item label="发货单号：" prop="recordCode">
        <el-input v-model="dataForm.recordCode" placeholder="发货单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="前置单号：" prop="frontRecordCode">
        <el-input v-model="dataForm.frontRecordCode" placeholder="前置单号" clearable></el-input>
      </el-form-item>
       <el-form-item label="SAP采购单号：" prop="sapPoNo">
        <el-input v-model="dataForm.sapPoNo" placeholder="SAP采购单号" type="textarea" :autosize="{ minRows: 1, maxRows: 4}"></el-input>
      </el-form-item>
       <el-form-item label="交易中心po单据号：" prop="outRecordCode">
        <el-input v-model="dataForm.outRecordCode" placeholder="交易中心po单据号" clearable></el-input>
      </el-form-item>
       <el-form-item label="派车单号：" prop="tmsRecordCode">
        <el-input v-model="dataForm.tmsRecordCode" placeholder="派车单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="接收门店或加盟商编码：" prop="inRealWarehouseId">       
   <el-input v-model="dataForm.inShopCode" placeholder="接收门店或加盟商" clearable></el-input>
      </el-form-item>
      <el-form-item label="订单包含商品：" prop="skuCode">
        <el-input v-model="dataForm.skuCode" placeholder="订单包含商品CODE" clearable></el-input>
      </el-form-item>
      <el-form-item label="渠道：" prop="channelCode">
           <el-input v-model="channelListString" placeholder="" disabled style="width:auto;"></el-input>
           <basedata-el-channel style="float: right" @change="chosetype" clear="true"></basedata-el-channel>
        </el-form-item>
        <el-form-item label="单据类型：" prop="recordType">
         <el-select v-model="dataForm.recordType" filterable remote :remote-method="remoteMethodRecordType" placeholder="请选择" clearable >
                <el-option
                  v-for="(value, key) in recordTypeList"
                  :key="key"
                  :label="value"
                  :value="key">
                </el-option>
           </el-select>
        </el-form-item>
        <el-form-item label="发货仓库编号：">
  <el-input v-model="dataForm.outRealWarehouseCode" placeholder="请填写发货仓库编号" clearable></el-input>
          </el-form-item>  
      <el-form-item label="创建日期">
                            <el-col :span="11">
                                <el-form-item prop="startDate">
                                    <el-date-picker type="datetime" :clearable="false" :editable="false" placeholder="选择日期"  value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00']" v-model="dataForm.startTime" style="width: 100%;"></el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col class="line" :span="1">-</el-col>
                            <el-col :span="11">
                                 <el-form-item prop="endDate">
                                     <el-date-picker type="datetime" :clearable="false" :editable="false" placeholder="选择日期" :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss" :default-time="['23:59:59']" v-model="dataForm.endTime" style="width: 100%;"></el-date-picker>
                                 </el-form-item>
                            </el-col>
                        </el-form-item>
        
        <el-form-item label="出库单状态：" prop="recordStatus">
         <el-select v-model="dataForm.recordStatus" filterable  remote :remote-method="remoteMethodStatus"   placeholder="请选择" clearable >
                <el-option
                  v-for="(value, key) in recordStatusList"
                  :key="key"
                  :label="value"
                  :value="key">
                </el-option>
           </el-select>
        </el-form-item>
      
      <el-form-item>
        <el-button @click="getDataList()"  type="primary">查询</el-button>
        <el-button  @click="resetForm('dataForm')">重置</el-button>
        <!--<el-button @click="upTmsOrder()"  type="primary">派车单修改</el-button>-->
        <el-button v-if="isAuth('stock.saleToB.forceCancle')"  @click="batchCancle()"  type="primary">批量取消</el-button>
      </el-form-item>
       <el-dropdown :hide-on-click="false" @visible-change="updateChangedTitles" ref="messageDrop" trigger="click">
          <el-button class="el-dropdown-link">
            标题配置<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          
          <el-dropdown-menu slot="dropdown">
           <div  style="height: 200px;overflow: auto;">
            <el-dropdown-item v-for="item in tableList">
           <el-checkbox v-model="checkedTitles" @change="checkboxChanged" :checked="item.isShow == 1":label="item.title"></el-checkbox>
           <div style="float: right">
            <i  v-if="tableList[0].title != item.title" class="el-icon-top"  @click="sortUp(item.title)"></i>
             <i v-if="tableList[tableList.length-1].title != item.title" class="el-icon-bottom"  @click="sortDown(item.title)"></i>
              <i v-if="tableList[tableList.length-1].title == item.title" class="el-icon-bottom" style="visibility: hidden"></i>
            </div>
        </el-dropdown-item>
        </div style="height: 20px;">
            <hr style="border-width:0.5px;margin-block-end:0em;">
           <div style="float: right;margin-top: 10px">
             <el-button size="mini" style="" type="primary" @click="confirmChange">
            确定
          </el-button>
           <el-button size="mini" style="margin-right:5px;" @click="dropChange">
            取消
          </el-button>
        </div>
          </el-dropdown-menu>
        </el-dropdown>
    </el-form>
    
    
    <el-table  size="small" 
      :data="dataList"
      border
      v-loading="dataListLoading"
      element-loading-text="拼命加载中..."
      @selection-change="handleSelectionChange"
      style="width: 100%;">
     <el-table-column
      type="selection"
      :selectable='checkboxT'
      width="55">
    </el-table-column>
     <el-table-column
        label="序号"
        type="index"
        width="70"
        align="center">
        <template scope="scope">
            <span>{{getIndex(scope.$index)}}</span>
        </template>
        </el-table-column>
           <el-table-column v-for="t in tableTitles"    :prop="t.prop" header-align="center" align="center" :label="t.title"  width="140">
             <template slot-scope="scope">
                <span  v-if="t.prop && t.prop == 'syncWmsStatus'">{{wmsSyncStatusList[scope.row.syncWmsStatus]}}</span>
                <span  v-else-if="t.prop" v-html="scope.row[t.prop]"></span>
                <span v-else></span> 
            </template>
        </el-table-column>
      <el-table-column
      width="140"
        fixed="right"  prop="操作"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text"  size="small" @click="seeDetail(scope.row.id)">查看详情</el-button>
          <el-button type="text" v-if="scope.row.disparityHandle"  size="small" @click="toDisparityHandle(scope.row.id)">整单拒收落差异</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="dataForm.pageIndex"
      :page-sizes="[2,10, 20, 50, 100]"
      :page-size="dataForm.pageSize"
      :total="dataForm.totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <el-dialog
                 title="错误提示"
                 :close-on-click-modal="false"
                 width="40%"
                 :visible.sync="visibleError"
                 :before-close="closeDialogError"
                 append-to-body>
        <div style="height:200px;overflow:scroll;color: red"  >
       <div v-html="message">
  </div>
`
    });
});
