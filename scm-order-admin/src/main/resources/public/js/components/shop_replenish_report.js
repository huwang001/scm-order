lyfdefine(['shop_replenish_stat'], function (shopReplenishStat) {
    return ({
        data: function data() {
            return {
                dataForm: {
                    sapPoNo: null,
                    /* sapOrderCode: null,*/
                    factoryCode: null,
                    realWarehouseCode: null,
                    skuCode: null,
                    shopCode: null,
                    startTime: new Date(new Date(new Date().toLocaleDateString()).getTime()),
                    endTime: null,
                    pageSize: 10,
                    pageIndex: 1,
                    frontRecordType: null
                },
                realWarehouseType: 1,
                realWareCode: [],
                factoryCode: [],
                recordStatuses: {},
                shopList: [],
                menuKey: 1,
                dataList: [],
                dataListLoading: false,
                loading: false,
                shopReplenishStatVisible: true,
                tableList: [],//标题自定义信息集合
                checkedTitles: [],
                originTableTitles: [],
                tableTitles: [],
                lastCheckedTitle: [],
                oldCheckedTitles: [],
                oldTitleOrder: {},
                isRequest: false,
                tableCode: 'shop_replenish_report',
                exportButtonDisabled: 0,
                warehouseRecordTypes:
                    [
                        {value: 15, label: '直营门店补货单'},
                        {value: 19, label: '门店冷链交货单（内采）'},
                        {value: 21, label: '门店供应商直送交货单（内采）'},
                        {value: 30, label: '加盟门店补货单'},
                        {value: 44, label: '加盟商补货'},
                        {value: 45, label: '加盟托管补货单'},
                        {value: 53, label: '门店确认收货补偿单'}
                    ]
            };
        },
        components: {
            'shopReplenishStat': shopReplenishStat
        },
        methods: {
            menuTree: function () {
                ++this.menuKey
            },
            // 获取数据列表
            getDataList: function getDataList() {
                var _this = this;
                _this.dataListLoading = true;
                _this.$http.post('/scm-order-admin/order/v1/shopReplenish/queryReplenishReportCondition',
                    _this.dataForm
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
            getFactoryList: function () {
                let _this = this;
                //后台查询所有工厂信息
                this.$http.get('/scm-order-admin/order/v1/shop/queryNotShopFactory').then(function (_ref) {
                    let data = _ref.data.data;
                    if (data) {
                        data.forEach(function (value) {
                            let arr = {};
                            arr.value = value.code;
                            arr.label = value.name + "（" + value.code + "）";
                            _this.factoryCode.push(arr);
                        })
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },
            selectFacotry: function (row) {
                let _this = this;
                _this.realWareCode = [];
                _this.dataForm.realWarehouseCode = null;
                _this.shopList = [];
                _this.dataForm.shopCode = null;
                if (_this.dataForm.factoryCode != null && _this.dataForm.factoryCode != "") {
                    _this.getRealWareByFCode(row);
                    _this.getShopWareByCode(row);
                }
            },
            //根据工厂编号获取实仓编号和名字
            getRealWareByFCode: function (row) {
                let _this = this;
                this.$http.post('/scm-order-admin/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeNoShop',
                    row
                ).then(function (_ref2) {
                    _this.realWareCode = [];
                    _this.dataForm.realWarehouseCode = null;
                    let data = _ref2.data.data;
                    data.forEach(function (e) {
                        let arr1 = {};
                        arr1.value = e.realWarehouseCode;
                        arr1.label = e.realWarehouseCode + "/" + e.realWarehouseName;
                        _this.realWareCode.push(arr1);
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            },
            //根据工厂编号和仓库类型获取门店类别
            getShopWareByCode: function (row) {
                let _this = this;
                this.$http.get('/scm-order-admin/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeAndRWType?factoryCode=' +
                    row + "&realWarehouseType=" + this.realWarehouseType)
                    .then(function (_ref2) {
                        let data = _ref2.data.data;
                        _this.shopList = [];
                        _this.dataForm.shopCode = null;
                        data.forEach(function (e) {
                            let arr1 = {};
                            arr1.value = e.shopCode;
                            arr1.label = e.shopCode + "/" + e.realWarehouseName;
                            _this.shopList.push(arr1);
                        });

                    }).catch(function (error) {
                    console.log(error);
                });
            },
            choiceFactory: function () {
                let fac = this.dataForm.factoryCode;
                if (null == fac || '' == fac || undefined == fac) {
                    this.$message({
                        message: '请先选择工厂'
                    });
                }
            },
            exportReport: function () {
                var _this = this;
                _this.exportButtonDisabled = 1;
                _this.loading = true;
                this.$http({
                    method: "post",
                    url: "/scm-order-admin/order/v1/shopReplenish/exportReplenishReport",
                    data: _this.dataForm,
                    responseType: 'blob'
                })
                    .then(function (res) {
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
                            _this.loading = false;
                        }
                    }).catch(function (error) {
                    _this.loading = false;
                    _this.exportButtonDisabled = 0;
                    console.log(error);
                });
            },
            downloadFile: function (fileName, content) {
                let aLink = document.createElement('a');
                let blob = this.base64ToBlob(content); //new Blob([content]);
                let evt = document.createEvent("HTMLEvents");
                evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
                aLink.download = fileName;
                aLink.href = URL.createObjectURL(blob);
                aLink.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));//兼容火狐
            },
            base64ToBlob: function (code) {
                let parts = code.split(';base64,');
                let contentType = parts[0].split(':')[1];
                let raw = window.atob(parts[1]);
                let rawLength = raw.length;
                let uInt8Array = new Uint8Array(rawLength);
                for (let i = 0; i < rawLength; ++i) {
                    uInt8Array[i] = raw.charCodeAt(i);
                }
                return new Blob([uInt8Array], {type: contentType});
            },
            statReportHandle: function () {
                this.shopReplenishStatVisible = true;
                this.$refs.shopReplenishStat.init(this.dataForm);
            },
            init: function init() {
                var _this = this;
                //查询日志列表
                _this.getDataList();
                _this.getFactoryList();
                _this.getRecordStatuses();
            },
            // 每页数
            sizeChangeHandle: function sizeChangeHandle(val) {
                this.dataForm.pageSize = val;
                this.dataForm.pageIndex = 1;
                this.getDataList();
            },
            //获取状态列表
            getRecordStatuses: function getRecordStatuses() {
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/queryFrontRecordStatus/list'
                ).then(function (res) {
                    if (res.status == '200') {
                        _this.recordStatuses = res.data.data;
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },

            // 当前页
            currentChangeHandle: function currentChangeHandle(val) {
                this.dataForm.pageIndex = val;
                this.getDataList();
            },

            getIndex: function getIndex(index) {
                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },

            resetForm: function (form) {
                this.dataForm.sapPoNo = null;
                /*this.dataForm.sapOrderCode = null;*/
                this.dataForm.factoryCode = null;
                this.dataForm.realWarehouseCode = null;
                this.dataForm.skuCode = null;
                this.dataForm.shopCode = null;
                this.dataForm.startTime = new Date(new Date(new Date().toLocaleDateString()).getTime());
                this.dataForm.endTime = null;
                this.dataForm.recordStatus = null;
            },
            //初始化前端标题数据
            initOriginTableTitles() {
                this.originTableTitles = [
                    {title: "po行号", rowCode: "lineNo", prop: "lineNo", isShow: 1, orderNum: 1},
                    {title: "商品编号", rowCode: "skuCode", prop: "skuCode", isShow: 1, orderNum: 2},
                    {title: "商品名称", rowCode: "skuName", prop: "skuName", isShow: 1, orderNum: 3},
                    {title: "单据状态", rowCode: "warehouseName", prop: "warehouseName", isShow: 1, orderNum: 4},
                    {title: "工厂编号", rowCode: "factoryCode", prop: "factoryCode", isShow: 1, orderNum: 5},
                    {title: "工厂名称", rowCode: "factoryName", prop: "factoryName", isShow: 1, orderNum: 6},

                    {
                        title: "仓库编号",
                        rowCode: "outRealWarehoseCode",
                        prop: "outRealWarehoseCode",
                        isShow: 1,
                        orderNum: 7
                    },
                    {
                        title: "仓库名称",
                        rowCode: "outRealWarehouseName",
                        prop: "outRealWarehouseName",
                        isShow: 1,
                        orderNum: 8
                    },
                    {title: "门店编号", rowCode: "inShopCode", prop: "inShopCode", isShow: 1, orderNum: 9},
                    {
                        title: "门店名称",
                        rowCode: "inRealWarehouseName",
                        prop: "inRealWarehouseName",
                        isShow: 1,
                        orderNum: 10
                    },
                    {
                        title: "前置单类型",
                        rowCode: "frontRecordTypeName",
                        prop: "frontRecordTypeName",
                        isShow: 1,
                        orderNum: 10
                    },
                    {title: "前置单号", rowCode: "recordCode", prop: "recordCode", isShow: 1, orderNum: 11},

                    {title: "SAP采购单号", rowCode: "sapPoNo", prop: "sapPoNo", isShow: 1, orderNum: 12},
                    /*{title: "SAP交货单号", rowCode: "sapOrderCode", prop: "sapOrderCode", isShow: 1, orderNum: 13},*/
                    {title: "需求数量", rowCode: "skuQty", prop: "skuQty", isShow: 1, orderNum: 14},
                    {title: "寻源数量", rowCode: "allotQty", prop: "allotQty", isShow: 1, orderNum: 15},
                    {title: "出库单数量", rowCode: "planQty", prop: "planQty", isShow: 1, orderNum: 15},
                    {title: "实际出库数量", rowCode: "realOutQty", prop: "realOutQty", isShow: 1, orderNum: 16},

                    {title: "门店入库数量", rowCode: "realInQty", prop: "realInQty", isShow: 1, orderNum: 17},
                    {title: "单位", rowCode: "unit", prop: "unit", isShow: 1, orderNum: 18},
                    {title: "出库单创建时间", rowCode: "createTime", prop: "createTime", isShow: 1, orderNum: 19},
                    {title: "实际出库时间", rowCode: "deliveryTime", prop: "deliveryTime", isShow: 1, orderNum: 20},
                    {title: "实际收货时间", rowCode: "receiverTime", prop: "receiverTime", isShow: 1, orderNum: 21}

                ]
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
            updateChangedTitles: function (value) {
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
                            //如果请求并成功后，首先确定其正确顺序，然后修改表格展示queryNotShopFactory
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
            handleSelectionChange: function handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            displayBlock() {
                this.$refs.messageDrop.show();
            },
            displayNone() {
                this.$refs.messageDrop.hide();
            },
        },
        created() {
            this.init();
            this.initOriginTableTitles();
            this.getDefinedTitles();//获取自定义标题
        },
        template: `
 <div class="mod-config">
    <el-form label-position="left" label-width="120px" :inline="true" :model="dataForm" ref="dataForm":key="menuKey">
                <el-form-item label="SAP采购单号" prop="sapPoNo">
                    <el-input v-model="dataForm.sapPoNo" placeholder="SAP采购单号" clearable></el-input>
                </el-form-item>
   
                <!--<el-form-item label="SAP交货单号" prop="sapOrderCode">
                    <el-input v-model="dataForm.sapOrderCode" placeholder="SAP交货单号" clearable></el-input>
                </el-form-item>-->
          
                <el-form-item label="工厂编号/名称" prop="factoryCode">
                    <el-select v-model="dataForm.factoryCode" filterable placeholder="工厂编号/名称" clearable @change="selectFacotry">
                        <el-option v-for="item in factoryCode" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
        
                <el-form-item label="仓库编号/名称" prop="realWarehouseCode">
                    <el-select v-model="dataForm.realWarehouseCode" filterable placeholder="仓库编号/名称" clearable @focus="choiceFactory">
                        <el-option v-for="item in realWareCode" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
  
                <el-form-item label="商品编号" prop="skuCode">
                    <el-input v-model="dataForm.skuCode" placeholder="商品编号" clearable></el-input>
                </el-form-item>

                <el-form-item label="门店编号" prop="realWarehouseCode">
                    <el-input v-model="dataForm.shopCode" placeholder="门店编号" clearable></el-input>
                </el-form-item>
      
                <el-form-item label="创建日期" prop="startDate">
                    <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" v-model="dataForm.startTime" style="width: 100%;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="-" style="width: 10px" />
                <el-form-item  prop="endDate">
                    <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" v-model="dataForm.endTime" style="width: 100%;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="单状态" prop="recordStatus">
                   <el-select v-model="dataForm.recordStatus" placeholder="">
                      <el-option key="0" label="初始" value="0"/>
                          <el-option key="1" label="审核通过" value="1"/>
                          <el-option key="2" label="取消订单" value="2"/>
                          <el-option key="4" label="已派车" value="4"/>
                          <el-option key="10" label="已出库" value="10"/>
                          <el-option key="11" label="已入库" value="11"/>
                      </el-select>
                   </el-form-item>
                 <el-form-item label="单据类型" prop="frontRecordType">
                    <el-select v-model="dataForm.frontRecordType" placeholder="单据类型">
                        <el-option v-for="item in warehouseRecordTypes" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
               </el-form-item>      
                <el-form-item>
                    <el-button @click="getDataList()" type="primary">查询</el-button>
                    <el-button @click="resetForm('dataForm')">重置</el-button>
                </el-form-item>
                
        	<el-dropdown :hide-on-click="false" @visible-change="updateChangedTitles" ref="messageDrop" trigger="click">
	        	<el-button class="el-dropdown-link">标题配置<i class="el-icon-arrow-down el-icon--right"></i></el-button>
	          	<el-dropdown-menu slot="dropdown">
		           	<div style="height: 200px;overflow: auto;">
		            	<el-dropdown-item v-for="item in tableList">
		           			<el-checkbox v-model="checkedTitles" @change="checkboxChanged" :checked="item.isShow == 1":label="item.title"></el-checkbox>
		           			<div style="float: right">
		            			<i v-if="tableList[0].title != item.title" class="el-icon-top"  @click="sortUp(item.title)"></i>
		             			<i v-if="tableList[tableList.length-1].title != item.title" class="el-icon-bottom"  @click="sortDown(item.title)"></i>
		              			<i v-if="tableList[tableList.length-1].title == item.title" class="el-icon-bottom" style="visibility: hidden"></i>
		            		</div>
		        		</el-dropdown-item>
		        	</div style="height: 20px;">
		            <hr style="border-width:0.5px;margin-block-end:0em;">
		           	<div style="float: right;margin-top: 10px">
			        	<el-button size="mini" style="" type="primary" @click="confirmChange">确定</el-button>
			           	<el-button size="mini" style="margin-right:5px;" @click="dropChange">取消</el-button>
		        	</div>
	        	</el-dropdown-menu>
	        </el-dropdown>
 
    </el-form>
    
    <el-form>
        <el-form-item>
            <el-button @click="statReportHandle()">汇总交单情况</el-button>
            <el-button @click="exportReport()" :disabled="exportButtonDisabled==1?true:false">导出</el-button>
            <i v-show="loading" class="el-icon-loading"></i>
        </el-form-item>
    
        <el-table max-height="550" :data="dataList" border v-loading="dataListLoading" ref="multipleTable" element-loading-text="拼命加载中..." style="width: 100%;">
            <el-table-column label="序号" type="index" width="70" align="center">
                <template scope="scope">
                    <span>{{getIndex(scope.$index)}}</span>
                </template>
            </el-table-column>
            
            <el-table-column v-for="t in tableTitles" :prop="t.prop" header-align="center" align="center" :label="t.title" width="140">
            	<template slot-scope="scope">
                	<span  v-if="t.prop && t.prop == 'warehouseName'">{{recordStatuses[scope.row.recordStatus]}}</span>
                	<span  v-else-if="t.prop">{{scope.row[t.prop]}}</span>
                	<span v-else></span> 
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
        <shop-replenish-stat v-if="shopReplenishStatVisible" ref="shopReplenishStat" @refreshDataList="getDataList"></shop-replenish-stat>
    </el-form>
  </div>
`
    });
});
