lyfdefine(['warehouse_allocation_record_add', 'warehouse_allocation_record_edit',
    'warehouse_allocation_record_view', 'warehouse_allocation_record_import','vw_allocation'], function (add, edit, viewPage, importPage ,recordsAllocation) {
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: {
                    recordCode: '',
                    sapOrderCode: '',
                    sapPoNo: '',
                    tmsRecordCode: '',
                    inWarehouseId: '',
                    inWarehouseName: '',
                    outWarehouseId: '',
                    outWarehouseName: '',
                    businessType: '',
                    recordStatus: '',
                    isDiffIn: '',
                    startDate: new Date(new Date(new Date().getTime() - 15 * 24 * 60 * 60 * 1000).toLocaleDateString()),
                    endDate: new Date(),
                    isDisparity: '',
                    isQualityAllotcate: '',
                    addType: '',
                    startDate: '',
                    endDate: '',
                    empNum: '',
                    skuCode: '',
                    pageIndex: 1,
                    pageSize: 10,
                    totalPage: 0
                },
                employeeInfos: [],
                dataListLoading: false,
                dialogFormVisible: false,
                addVisible: true,
                editVisible: true,
                viewPageVisible: true,
                importVisible: true,
                recordStatuses: {},
                dataListSelections: [],
                allocInfoList: [],
                allocationTip :'',
                orderTypeList: [
                    {value: 1, label: '内部调拨'},
                    {value: 2, label: 'RDC调拨'}
                ],
                addTypeList: [
                    {value: 1, label: '页面新增'},
                    {value: 2, label: 'excel导入'},
                    {value: 3, label: '差异创建'},
                    {value: 4, label: '销售预约单创建'},
                    {value: 5, label: '包装需求单创建'}
                ],
                dialogVisible: false,
                tableList: [],//标题自定义信息集合
                checkedTitles: [],
                originTableTitles: [],
                tableTitles: [],
                lastCheckedTitle: [],
                oldCheckedTitles: [],
                oldTitleOrder: {},
                isRequest: false,
                loading: false,
                tableCode: 'warehouse_allocation_record',
                dateTimeRange: [new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000), new Date()],
                datePayTimeRange: [],
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
            }
        },
        components: {
            'add': add,
            'edit': edit,
            'viewPage': viewPage,
            'importPage': importPage,
            'recordsAllocation':recordsAllocation
        },
        methods: {
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                if(_this.datePayTimeRange && _this.datePayTimeRange.length >1){
                    _this.dataForm.startDate = _this.datePayTimeRange[0];
                    _this.dataForm.endDate = _this.datePayTimeRange[1];
                }else {
                    _this.dataForm.startDate = null;
                    _this.dataForm.endDate=null;
                }
                let star = _this.dataForm.startDate;
                this.dataListLoading = true;
                this.$http.post('/scm-order-admin/order/v1/whAllocation/queryList',
                    this.dataForm
                ).then(function (res) {
                    if (res.status == '200') {
                        _this.dataList = res.data.data.list;
                        _this.dataForm.totalPage = res.data.data.total;
                        let creatorList = [];
                        res.data.data.list.forEach(function (e) {
                            creatorList.push(e.creator);
                            //只有创建方式为导入的并且有差异的并且状态为1已确认 4已派车 10已出库的允许差异创建
                            e.showDisparityButton = false;
                            if ((e.addType == 1 || e.addType == 2 )&& e.isDisparity ==1 && e.isQualityAllotcate == 0) {
                                if (e.recordStatus == 1 || e.recordStatus == 4 || e.recordStatus == 10) {
                                    e.showDisparityButton = true;
                                }
                            }
                        });
                        _this.getEmployeeNumberByUserId(creatorList);
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                        _this.$message('查询失败！');
                    }
                    _this.dataListLoading = false;
                }).catch(function (error) {
                    console.log(error);
                    _this.dataListLoading = false;
                });
            },
            updateTmsOrder: function (row) {
                var _this = this;
                if (!row.recordCode) {
                    this.$message({
                        message: '请至少选择一个单据进行修改',
                        type: 'warn'
                    });
                    return;
                }
                _this.dataListLoading = true;
                this.$prompt("派车单号", "派车单修改", {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^\d{10}$/,
                    inputErrorMessage: '派车单号必须是10位数字'
                }).then(({value}) => {
                    this.$http({
                        url: '/scm-order-admin/order/v1/whAllocation/updateAllotTmsOrder',
                        method: 'get',
                        params: _this.$http.adornParams({
                            "codes": row.recordCode,
                            "tmsCode": value
                        })
                    }).then(function (_ref2) {
                        var data = _ref2.data;
                        if (data && data.code == 0) {
                            var map = data.data;
                            _this.$message({
                                type: 'success',
                                message: map.msg
                            });
                            row.tmsRecordCode = value;
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

            //根据用户id查找员工工号
            getEmployeeNumberByUserId: function (userIds) {
                let _this = this;
                this.employeeInfos = [];
                //从后台查询recordType
                this.$http.post('/scm-order-admin/order/v1/user/getEmployeeInfoByUserIds', userIds).then(function (_ref) {
                    let data = _ref.data.data;
                    _this.employeeInfos = data;
                }).catch(function (error) {
                    console.log(error);
                });
            },
            formatCreator: function (row, column, cellValue) {
                let _this = this;
                let columnValue = row.creator;
                Object.keys(_this.employeeInfos).forEach(function (key) {
                    if (_this.employeeInfos[key].id == row.creator) {
                        let employeeNumber = _this.employeeInfos[key].employeeNumber;
                        if (null != employeeNumber) {
                            columnValue = employeeNumber;
                        }
                    }
                });
                return columnValue;
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
            formatOrderType(row, column, cellValue) {
                let _this = this;
                let columnValue = "";
                Object.keys(_this.orderTypeList).forEach(function (key) {
                    if (_this.orderTypeList[key].value == row.businessType) {
                        columnValue = _this.orderTypeList[key].label
                    }
                });
                return columnValue
            },
            formatAddType(row, column, cellValue) {
                let _this = this;
                let columnValue = "";
                Object.keys(_this.addTypeList).forEach(function (key) {
                    if (_this.addTypeList[key].value == row.addType) {
                        columnValue = _this.addTypeList[key].label
                    }
                });
                return columnValue
            },
            confirmPost: function (id) {
                let _this = this;
                this.$http.post('/scm-order-admin/order/v1/whAllocation/confimWhAllocation?id=' + id
                ).then(function (res) {
                    debugger
                    let data = res.data;
                    let tip =  "确认成功</br>" +  data.data;
                    if (data && data.code === '0') {
                        _this.$message({
                            message:tip,
                            type: 'success',
                            dangerouslyUseHTMLString: true,
                            duration: 4000
                        });

                        // _this.$message({message: '确认成功！', type: 'success'});
                        _this.getDataList();
                    } else {
                        _this.$message({message: data.msg, type: 'error', duration: 1500});
                    }
                    _this.dataListLoading = false;
                }).catch(function (error) {
                    _this.$message({message: '部分单据确认失败！', type: 'error', duration: 1500});
                    _this.getDataList();
                });
            },
            //确认
            confimRecords: function () {
                let _this = this;
                if (this.dataListSelections.length != 1) {
                    _this.$message({message: '请选择一条行记录！', type: 'error', duration: 1500});
                    return;
                }
                let id = this.dataListSelections[0].id;
                let status = this.dataListSelections[0].recordStatus;
                if (status != 0) {
                    _this.$message({message: '该订单已经确认, 无需再次确认！', type: 'success'});
                    return;
                }
                _this.$http.post('/scm-order-admin/order/v1/whAllocation/queryMarketDownSku?id=' + id
                ).then(function (res) {
                    let queryData = res.data;
                    if (queryData && queryData.code === '0') {
                        let tip = queryData.data;

                        console.log(JSON.stringify(tip));
                        if (tip.length > 0) {
                            _this.$confirm('有(预)下市商品,如下:' + JSON.stringify(tip) + ', 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                _this.confirmPost(id);
                            }).catch(() => {
                            });
                        } else {
                            _this.confirmPost(id);
                        }
                    } else {
                        _this.$message({message: queryData.msg, type: 'error', duration: 1500});
                    }
                    _this.dataListLoading = false;
                }).catch(function (error) {
                    _this.$message({message: '部分单据确认失败！', type: 'error', duration: 1500});
                    _this.getDataList();
                });

            },

            //批量取消
            cancleRecords: function (isForceCancle) {
                let _this = this;
                if (this.dataListSelections.length != 1) {
                    _this.$message({message: '请选择一条行记录！', type: 'error', duration: 1500});
                    return;
                }
                let id = this.dataListSelections[0].id;
                _this.$confirm('此操作将取消调拨单, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('/scm-order-admin/order/v1/whAllocation/cancleWhAllocation?id=' + id + "&isForceCancle=" + isForceCancle
                    ).then(function (res) {
                        let data = res.data;
                        if (data && data.code === '0') {
                            _this.$message({message: '取消成功！', type: 'success'});
                            _this.getDataList();
                        } else {
                            _this.$message({message: data.msg, type: 'error', duration: 1500});
                        }
                    }).catch(function (error) {
                        _this.$message('部分单据取消失败！');
                        _this.getDataList();
                    });
                }).catch(() => {

                });
            },
            //模糊查询
            querySearch: function (queryString, cb) {
                let _this = this;
                let list = [];
                this.$http.post('/scm-order-admin/order/v1/whAllocation/queryForAdmin?name=' + queryString).then(function (res) {
                    if (res.status == '200') {
                        debugger;
                        for (let i of res.data.data) {
                            i.value = i.realWarehouseName +'('+ i.realWarehouseCode+')';   //将想要展示的数据作为value
                        }
                    }
                    list = res.data.data;
                    cb(list);
                }).catch(function (error) {
                    _this.$message('查询失败！');
                    cb();
                });
            },
            allocRateConfig: function allocRateConfig(id, status) {
                let canEdit = true;
                if (status == 2) {
                    //已入库
                    canEdit = false;
                }
                this.dialogFormVisible = true;
                this.queryAllocConfigInfo(id, canEdit ,2);

            },
            allocOutRateConfig : function (id, status) {
                let canEdit = false;
                if (status === 0) {
                    //初始状态，只有审核之前的单据才能设置出库比例
                    canEdit = true;
                }
                this.dialogFormVisible = true;
                this.queryAllocConfigInfo(id, canEdit ,1);
            },
            // type =1 出库 type=2 入库
            queryAllocConfigInfo: function queryAllocConfigInfo(id, canEdit ,type) {
                let _this = this;
                let url = '/scm-order-admin/order/v1/whAllocation/queryAllocConfigInfo/';
                _this.allocationTip = "入库SKU虚拟仓分配比例";
                if(type === 1 ){
                    url = '/scm-order-admin/order/v1/whAllocation/queryOutAllocConfigInfo/';
                    _this.allocationTip = "出库SKU虚拟仓分配比例";
                }

                this.$http.get(url + id
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data) {
                        let res = [];
                        if (data.frontRecordDetails) {
                        	var allotType = null;
                            for (let i = 0; i < data.frontRecordDetails.length; i++) {
                                let skuDetail = data.frontRecordDetails[i];
                                if (skuDetail.vmSyncRate) {
                                    let vmSyncRate = skuDetail.vmSyncRate;
                                    for (var m in vmSyncRate) {
                                    	if(vmSyncRate[m].allotType != null) {
                                    		allotType = vmSyncRate[m].allotType;
                                    		break;
                                    	}
                                    }
                                    for (let j = 0; j < vmSyncRate.length; j++) {
                                        let vmSyncRateItem = vmSyncRate[j];
                                        let realId = data.inWarehouseId;
                                        let realName = data.inWarehouseName;
                                        let realCode = data.inWarehouseCode;
                                        let realWarehouseOutCode = data.inRealWarehouseCode;
                                        let factoryCode = data.inFactoryCode;
                                        if(type === 1){
                                            realId = data.outWarehouseId;
                                            realName = data.outWarehouseName;
                                            realCode = data.outWarehouseCode;
                                            realWarehouseOutCode = data.outRealWarehouseCode;
                                            factoryCode = data.outFactoryCode;
                                        }
                                        res.push({
                                            "recordId": data.id,
                                            "recordCode": data.recordCode,
                                            "realWarehouseId": realId,
                                            "realWarehouseName": realName,
                                            "realWarehouseCode": realCode,
                                            "skuId": skuDetail.skuId,
                                            "skuCode": skuDetail.skuCode,
                                            "skuName": skuDetail.skuName,
                                            "allotQty": skuDetail.allotQty,
                                            "virtualWarehouseId": vmSyncRateItem.id,
                                            "virtualWarehouseCode": vmSyncRateItem.virtualWarehouseCode,
                                            "virtualWarehouseName": vmSyncRateItem.virtualWarehouseName,
                                            "syncRate": vmSyncRateItem.syncRate,
                                            "configSyncRate": vmSyncRateItem.allotType == 1? vmSyncRateItem.configSyncRate : null,
                                            "configAbsolute": vmSyncRateItem.allotType == 2? vmSyncRateItem.configSyncRate : null,		
                                            "isEditSyncRate": canEdit,
                                            "isEditAbsolute": canEdit,
                                            "isEditSyncRateInit": canEdit,
                                            "isEditAbsoluteInit": canEdit,
                                            "realWarehouseOutCode": realWarehouseOutCode,
                                            "factoryCode": factoryCode
                                        });
                                    }
                                }
                            }
                            if(allotType != null && canEdit) {
                            	for(var n in res) {
                            		if(allotType == 1) {
                            			res[n].isEditAbsolute = false;
                                   }
                            		if(allotType == 2) {
                            			res[n].isEditSyncRate = false;
                            		}
                            	}
                            }
                        }
                        _this.allocInfoList = res;
                        if (!canEdit) {
                            _this.allocationTip = '出库SKU虚拟仓分配比例【确认审核之后不允许设置分配关系,仅查看】';
                            if(type === 2){
                                _this.allocationTip = '入库SKU虚拟仓分配比例【已取消的单据不允许配置分配关系,仅查看】';
                            }
                        }
                    } else {
                        _this.allocInfoList = [];
                    }
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
            resetForm: function (form) {
                this.dataForm.recordCode = '';
                this.dataForm.inWarehouseId = '';
                this.dataForm.inWarehouseName = '';
                this.dataForm.outWarehouseId = '';
                this.dataForm.outWarehouseName = '';
                this.dataForm.businessType = '';
                this.dataForm.recordStatus = '';
                this.dataForm.isDiffIn = '';
                this.dataForm.startDate = new Date(new Date(new Date().getTime() - 15 * 24 * 60 * 60 * 1000).toLocaleDateString());
                this.dataForm.endDate = new Date();
                this.dataForm.isDisparity = '';
                this.dataForm.isQualityAllotcate = '';
                this.dataForm.addType = '';
                this.dataForm.startDate = '';
                this.dataForm.endDate = '';
                this.dataForm.sapOrderCode = '';
                this.dataForm.sapPoNo = '';
                this.dataForm.tmsRecordCode = '';
                this.dataForm.empNum = '';
                this.dataForm.skuCode = '';
                this.datePayTimeRange = [];
            },
            checkboxT(row, rowIndex) {
                return true;//都不禁用
            },
            selectionChangeHandle: function selectionChangeHandle(val) {
                this.dataListSelections = val;
            },

            batchConfigIn : function (type) {
                var _this = this;
                if (0 == this.dataListSelections.length) {
                    this.$message({
                        message: '请至少选择一行记录',
                        type: 'warn'
                    });
                    return;
                }

                this.$refs.recordsAllocation.init( _this.dataListSelections , type );
            },

            //出库选中
            handleSelectOut(item) {
                this.dataForm.outWarehouseId = item.id;
            },
            //入库选中
            handleSelectIn(item) {
                this.dataForm.inWarehouseId = item.id;
            },
            onSubmit() {
                this.getDataList();
            },
            addHandle: function (id, name, code) {
                let _this2 = this;
                this.addVisible = true;
                _this2.$refs.add.init();
            },
            toEdit: function (id) {
                let _this2 = this;
                this.editVisible = true;
                _this2.$refs.edit.init(id);

            },
            toviewPage: function (id) {
                let _this2 = this;
                this.viewPageVisible = true;
                _this2.$refs.viewPage.init(id);

            },
            toImportPage: function () {
                let _this2 = this;
                this.importVisible = true;
                _this2.$refs.importPage.init();

            },
            submitAllocConfig: function submitAllocConfig() {
                let list = this.allocInfoList;
                let needInsert = [];
                let skuId = -1;
                let skuName = "";
                let defaultSync = 0;
                let configSync = 0;
                let config = false;
                let _this = this;
                //判断用户填的比例是否正整数，以及是否全配置或全不配置 以及是否=默认比例之和的校验
                const re = /^[1-9]*[0-9]$/;
                for (let i = 0; i < list.length; i++) {
                    let item = list[i];
                    item.realWarehouseCode = item.realWarehouseOutCode;
                    if (item.isEditSyncRate) {
                        if (!(!item.configSyncRate || Number(item.configSyncRate) && item.configSyncRate >= 0 && item.configSyncRate <= 100)) {
                            //用户有输入且输入不合法
                            if (item.configSyncRate !== '0') {
                            	_this.$message({
                                    message: "请输入[0-100]之间的正整数"
                                });
                                return;
                            }
                        }
                        if (item.configSyncRate && item.configSyncRate < 100) {
                            const rsCheck = re.test(item.configSyncRate);
                            if (!rsCheck) {
                            	_this.$message({
                                    message: "请输入[1-100]之间的整数"
                                });
                                return;
                            }
                        }
                        if (skuId === item.skuId) {
                            defaultSync = defaultSync + item.syncRate;
                            if ((item.configSyncRate || item.configSyncRate === 0) && config) {
                                //该sku第一行配了，改行也配了正常，需要校验总和
                                configSync = configSync + item.configSyncRate * 1;
                            } else if (!(item.configSyncRate || item.configSyncRate === 0) && !config) {
                                //该sku第一行没配，该行也没配，正常，不需要校验总和
                            } else {
                                //不正常
                            	_this.$message({
                                    message: "sku:" + skuName + "要么全配，要么全不配"
                                });
                                return;
                            }
                        } else {
                            if (config && defaultSync !== configSync) {
                                //默认的跟配置的之和不相等，则提示用户，不进入到后台
                            	_this.$message({
                                    message: "sku:" + skuName + "配置的分配比例跟默认比例之和不相等!"
                                });
                                return;
                            }
                            skuId = item.skuId;
                            skuName = item.skuName;
                            defaultSync = item.syncRate;
                            if ((item.configSyncRate || item.configSyncRate === 0)) {
                                //某个sku的第一行用户配置了，则后面的sku都需要配，否则检验不通过
                                config = true;
                                configSync = item.configSyncRate * 1;
                            } else {
                                config = false;
                            }
                        }
                        if (config && i === list.length - 1) {
                            //最后一个sku需要额外处理
                            if (defaultSync !== configSync) {
                                //默认的跟配置的之和不相等，则提示用户，不进入到后台
                            	_this.$message({
                                    message: "sku:" + skuName + "配置的分配比例跟默认比例之和不相等!"
                                });
                                return;
                            }
                        }
                        if (config) {
                        	item.allotType = 1;
                            //用户配置了，才需要入库
                            needInsert.push(item);
                        }
                    }
                    
                    if (item.isEditAbsolute) {
                        if (!(!item.configAbsolute || Number(item.configAbsolute) && item.configAbsolute >= 0)) {
                            //用户有输入且输入不合法
                            if (item.configAbsolute !== '0') {
                                _this.$message({
                                    message: "请输入大于或等于0的数"
                                });
                                return;
                }
                        }
                        if (item.configAbsolute) {
                            if (item.configAbsolute < 0) {
                            	_this.$message({
                                    message: "请输入大于或等于0的数"
                                });
                                return;
                            }
                        }
                        if (skuId === item.skuId) {
                            defaultSync = item.allotQty;
                            if ((item.configAbsolute || item.configAbsolute===0) && config) {
                                //该sku第一行配了，改行也配了正常，需要校验总和
                                configSync = configSync + Number(item.configAbsolute);
                            } else if (!(item.configAbsolute || item.configAbsolute===0) && !config) {
                                //该sku第一行没配，该行也没配，正常，不需要校验总和
                            } else {
                                //不正常
                                _this.$message({
                                    message: "sku:" + skuName + "要么全配，要么全不配"
                                });
                                return;
                            }
                        } else {
                            if (config && defaultSync !== configSync) {
                            	_this.$message({
                                    message: "sku:" + skuName + "配置的分配数量之和跟可分配数量不相等!"
                                });
                                return;
                            }
                            skuId = item.skuId;
                            skuName = item.skuName;
                            defaultSync = item.allotQty;
                            if ((item.configAbsolute || item.configAbsolute===0)) {
                                //某个sku的第一行用户配置了，则后面的sku都需要配，否则检验不通过
                                config = true;
                                configSync = Number(item.configAbsolute);
                            } else {
                                config = false;
                            }
                        }
                        if (config && i === list.length - 1) {
                            //最后一个sku需要额外处理
                            if (defaultSync !== configSync) {
                            	_this.$message({
                                    message: "sku:" + skuName + "配置的分配数量之和跟可分配数量不相等!"
                                });
                                return;
                            }
                        }
                        if (config) {
                        	item.allotType = 2;
                        	item.configAbsolute = item.configAbsolute;
                            //用户配置了，才需要入库
                            needInsert.push(item);
                        }
                    }
                }
                if (needInsert.length > 0) {
                    _this.$http.post('/scm-order-admin/order/v1/whAllocation/allotConfig', needInsert
                    ).then(function (_ref2) {
                        let data = _ref2.data;
                        _this.$message({
                            message: 0 == data.code ? '操作成功' : '操作失败',
                            type: 0 == data.code ? 'success' : 'error',
                            duration: 1500,
                            onClose: function onClose() {
                                _this.dialogFormVisible = false;
                            }
                        });
                    });
                } else {
                    _this.dialogFormVisible = false;
                }
                console.log(needInsert);
            }
            ,
            colspanMethod: function colspanMethod({row, column, rowIndex, columnIndex}) {
                var _this = this;
                var totalRow = _this.allocInfoList.length;
                var virtualWarehouseIds = [];
                for (var i = 0; i < totalRow; i++) {
                    if (virtualWarehouseIds.indexOf(_this.allocInfoList[i].virtualWarehouseId) < 0) {
                        virtualWarehouseIds.push(_this.allocInfoList[i].virtualWarehouseId)
                    }
                }
                var virtualWarehouseNum = virtualWarehouseIds.length;
                if ((columnIndex === 0 || columnIndex === 1) && rowIndex === 0) {
                    return [totalRow, 1];
                } else if (rowIndex > 0 && (columnIndex === 0 || columnIndex === 1)) {
                    return [0, 0]
                } else if ((columnIndex === 2 || columnIndex === 3  || columnIndex === 4) && rowIndex % virtualWarehouseNum === 0) {
                    return [virtualWarehouseNum, 1]
                } else if ((columnIndex === 2 || columnIndex === 3  || columnIndex === 4) && rowIndex % virtualWarehouseNum > 0) {
                    return [0, 0]
                } else {
                    return [1, 1]
                }
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
            // 以下为导出excel
            exportExcel: function exportExcel() {
                let _this = this;
                if(_this.datePayTimeRange && _this.datePayTimeRange.length >1){
                    _this.dataForm.startDate = _this.datePayTimeRange[0];
                    _this.dataForm.endDate = _this.datePayTimeRange[1];
                }else {
                    _this.dataForm.startDate = null;
                    _this.dataForm.endDate=null;
                }
                let star = _this.dataForm.startDate;
                if (star == null) {
                    _this.$message({
                        message: '请选择开始时间'
                    });
                    return;
                }
                let day = 1000 * 60 * 60 * 24;
                let diff = Math.ceil((new Date().getTime() - new Date(star).getTime() ) / (day));
                if (diff > 15) {
                    _this.$message({
                        message: '导出的开始时间需在15天内'
                    });
                    return;
                }
                _this.loading = true;
                this.$http({
                    method: "post",
                    url: "/scm-order-admin/order/v1/whAllocation/exportWhallot",
                    data:  _this.dataForm,
                    responseType: 'blob'
                }).then(function (res) {
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
                    _this.loading = false
                }).catch(function (error) {
                    _this.loading = false
                    console.log(error);
                });
            },
            downloadFile(fileName, content) {
                let aLink = document.createElement('a');
                let blob = this.base64ToBlob(content); //new Blob([content]);
                let evt = document.createEvent("HTMLEvents");
                evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
                aLink.download = fileName;
                aLink.href = URL.createObjectURL(blob);
                aLink.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));//兼容火狐
            }, base64ToBlob(code) {
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
            //初始化前端标题数据
            initOriginTableTitles() {
                this.originTableTitles = [
                    {title: "调拨单号", rowCode: "recordCode", prop: "recordCode", isShow: 1, orderNum: 1},
                    {title: "调拨单状态", rowCode: "recordStatus", prop: "recordStatus", isShow: 1, orderNum: 2},
                    //{title: "调拨交货单号", rowCode: "sapOrderCode", prop: "sapOrderCode", isShow: 1, orderNum: 3},
                    {title: "派车单号", rowCode: "tmsRecordCode", prop: "tmsRecordCode", isShow: 1, orderNum: 4},
                    {title: "调拨采购单号", rowCode: "sapPoNo", prop: "sapPoNo", isShow: 1, orderNum: 5},
                    {
                        title: "调入工厂",
                        rowCode: "inRealWarehouse.factoryCode",
                        prop: "inRealWarehouse",
                        subProp: "factoryCode",
                        isShow: 1,
                        orderNum: 6
                    },
                    {
                        title: "调入仓库",
                        rowCode: "inRealWarehouse.realWarehouseCode",
                        prop: "inRealWarehouse",
                        subProp: "realWarehouseCode",
                        isShow: 1,
                        orderNum: 7
                    },
                    {
                        title: "调入仓库名称",
                        rowCode: "inRealWarehouse.realWarehouseName",
                        prop: "inRealWarehouse",
                        subProp: "realWarehouseName",
                        isShow: 1,
                        orderNum: 8
                    },
                    {
                        title: "调入地点",
                        rowCode: "inRealWarehouse.realWarehouseAddress",
                        prop: "inRealWarehouse",
                        subProp: "realWarehouseAddress",
                        isShow: 1,
                        orderNum: 9
                    },

                    {
                        title: "调出工厂",
                        rowCode: "outRealWarehouse.factoryCode",
                        prop: "outRealWarehouse",
                        subProp: "factoryCode",
                        isShow: 1,
                        orderNum: 10
                    },
                    {
                        title: "调出仓库",
                        rowCode: "outRealWarehouse.realWarehouseCode",
                        prop: "outRealWarehouse",
                        subProp: "realWarehouseCode",
                        isShow: 1,
                        orderNum: 11
                    },
                    {
                        title: "调出仓库名称",
                        rowCode: "outRealWarehouse.realWarehouseName",
                        prop: "outRealWarehouse",
                        subProp: "realWarehouseName",
                        isShow: 1,
                        orderNum: 12
                    },
                    {
                        title: "调出地点",
                        rowCode: "outRealWarehouse.realWarehouseAddress",
                        prop: "outRealWarehouse",
                        subProp: "realWarehouseAddress",
                        isShow: 1,
                        orderNum: 13
                    },
                    {title: "调拨单类型", rowCode: "businessType", prop: "businessType", isShow: 1, orderNum: 14},
                    {title: "调拨日期", rowCode: "allotTime", prop: "allotTime", isShow: 1, orderNum: 15},
                    {title: "确认人", rowCode: "auditor", prop: "auditor", isShow: 1, orderNum: 17},
                    {title: "确认时间", rowCode: "auditTime", prop: "auditTime", isShow: 1, orderNum: 18},
                    {title: "创建人", rowCode: "creator", prop: "creator", isShow: 1, orderNum: 19},
                    {title: "创建时间", rowCode: "createTime", prop: "createTime", isShow: 1, orderNum: 20},
                    {title: "更新人", rowCode: "modifyEmpNum", prop: "modifyEmpNum", isShow: 1, orderNum: 21},
                    {title: "更新时间", rowCode: "updateTime", prop: "updateTime", isShow: 1, orderNum: 22},
                    {title: "创建类型", rowCode: "addType", prop: "addType", isShow: 1, orderNum: 23},
                    {title: "调拨数量差异", rowCode: "isDisparity", prop: "isDisparity", isShow: 1, orderNum: 24},
                    {title: "是否质量调拨", rowCode: "isQualityAllotcate", prop: "isQualityAllotcate", isShow: 1, orderNum: 25}
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
            displayBlock() {
                this.$refs.messageDrop.show();
            },
            displayNone() {
                this.$refs.messageDrop.hide();
            },
            toCreateDisparity(id){
                let _this = this;
                _this.$confirm('确认是否按调拨差异创建单据', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('/scm-order-admin/order/v1/whAllocation/createDisparityAllot?id=' + id
                    ).then(function (res) {
                        let data = res.data;
                        let duration = 1500;
                        if (data && data.code === '0') {
                            let tip = "创建成功";
                            let obj = JSON.parse(data.data);
                            if (obj.inSufficeStockCodes.length > 0 || obj.undercarriageCodes.length > 0) {
                                tip += "以下物料没有创建,原因如下：";
                                duration = 10000;
                                if (obj.inSufficeStockCodes.length > 0) {
                                    tip += "</br>  库存不足物料: " + JSON.stringify(obj.inSufficeStockCodes);
                                }
                                if (obj.undercarriageCodes.length > 0) {
                                    tip += "</br>  预下市物料： " + JSON.stringify(obj.undercarriageCodes);
                                }
                            }
                            _this.$message({
                                message: tip,
                                type: 'success',
                                dangerouslyUseHTMLString: true,
                                duration: duration
                            });
                            _this.getDataList();
                        } else {
                            _this.$message({message: data.msg, type: 'error', duration: duration});
                        }
                    }).catch(function (error) {
                        _this.$message('创建成功！');
                        _this.getDataList();
                    });
                }).catch(() => {

                });
            },
            //按比例分配获取点击事件
            syncRateClick : function (v) {
            	for(var i in v) {
            		if(v[i].isEditSyncRateInit) {
            			v[i].isEditSyncRate = v[i].isEditSyncRateInit;
            			v[i].configAbsolute = null;
            			v[i].isEditAbsolute = false; 
            		}
            	 }
            	
        },
            //按绝对值分配获取点击事件
            absoluteClick : function (v) {
            	for(var i in v) {
            		if(v[i].isEditAbsoluteInit) {
            			v[i].isEditAbsolute = v[i].isEditAbsoluteInit;
            			v[i].configSyncRate = null;
            			v[i].isEditSyncRate = false; 
            		}
            	 }
            },

        },
        created() {
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
        mounted() {
            this.getDataList();
            this.getRecordStatuses();
        },
        template: /*html*/`
            <el-container>
                <el-main>
                    <el-row type="flex" justify="start" style="line-height:30px;height: 30px;font-size: 14px">
                        <el-col :span="3"><div style="font-weight: bold">仓库调拨单</div></el-col>
                    </el-row>
                    <el-form :inline="true" :model="dataForm" class="demo-form-inline">
                        <el-form-item label="调拨单号">
                            <el-input v-model="dataForm.recordCode" placeholder="请输入调拨单号"></el-input>
                        </el-form-item>
                        <!-- <el-form-item label="调拨交货单号：" prop="sapOrderCode">
        					<el-input v-model="dataForm.sapOrderCode" placeholder="调拨交货单号" type="textarea" :autosize="{ minRows: 1, maxRows: 4}"></el-input>
        				</el-form-item> -->
        				<el-form-item label="调拨采购单号：" prop="sapPoNo">
        					<el-input v-model="dataForm.sapPoNo" placeholder="调拨采购单号" clearable></el-input>
        				</el-form-item>
        				<el-form-item label="派车单号：" prop="tmsRecordCode">
        					<el-input v-model="dataForm.tmsRecordCode" placeholder="派车单号" clearable></el-input>
        				</el-form-item>
                        <el-form-item label="调出仓库" >
                            <el-autocomplete v-model="dataForm.outWarehouseName" :fetch-suggestions="querySearch" 
                            placeholder="请输入名称" :trigger-on-focus="false"   @select="handleSelectOut"/>
                       </el-form-item>
                    
                       <el-form-item label="调入仓库" >
                            <el-autocomplete v-model="dataForm.inWarehouseName" :fetch-suggestions="querySearch" 
                            placeholder="请输入名称" :trigger-on-focus="false"   @select="handleSelectIn"/>
                       </el-form-item>
                       
                           <!--<el-form-item label="创建日期">-->
                            <!--<el-col :span="11">-->
                                <!--<el-form-item prop="startDate">-->
                                    <!--<el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.startDate" style="width: 100%;"></el-date-picker>-->
                                <!--</el-form-item>-->
                            <!--</el-col>-->
                            <!--<el-col class="line" :span="1">-</el-col>-->
                            <!--<el-col :span="11">-->
                                 <!--<el-form-item prop="endDate">-->
                                     <!--<el-date-picker type="datetime" default-time="23:59:59" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.endDate" style="width: 100%;"></el-date-picker>-->
                                 <!--</el-form-item>-->
                            <!--</el-col>-->
                        <!--</el-form-item>-->
                         <el-form-item label="创建时间：" prop="datePayTimeRange">
                            <el-date-picker
                              value-format='yyyy-MM-dd HH:mm:ss'
                              :default-time="['00:00:00', '23:59:59']"
                              v-model="datePayTimeRange"
                              type="datetimerange"
                              :picker-options="pickerOptions"
                              range-separator="至"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
                              align="right">
                            </el-date-picker>
                         </el-form-item>
                         <el-form-item label="调拨类型" prop="businessType">
                            <el-select clearable v-model="dataForm.businessType" placeholder="类型">
                                <el-option v-for="item in orderTypeList" :key="item.value" :label="item.label" :value="item.value" />
                            </el-select>
                        </el-form-item>
                        
                         <el-form-item label="创建类型" prop="addType">
                            <el-select clearable v-model="dataForm.addType" placeholder="类型">
                                <el-option v-for="item in addTypeList" :key="item.value" :label="item.label" :value="item.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="单状态" prop="recordStatus">
                            <el-select clearable v-model="dataForm.recordStatus" placeholder="">
                            <el-option key="0" label="初始" value="0"/>
                                <el-option key="1" label="审核通过" value="1"/>
                                <el-option key="2" label="取消订单" value="2"/>
                                <el-option key="4" label="已派车" value="4"/>
                                <el-option key="10" label="已出库" value="10"/>
                                <el-option key="11" label="已入库" value="11"/>
                            </el-select>
                        </el-form-item>
                         <el-form-item label="调拨数量差异" prop="isDisparity">
                            <el-select clearable v-model="dataForm.isDisparity" placeholder="调拨数量差异">
                                <el-option key="0" label="无" value="0"/>
                                <el-option key="1" label="有" value="1"/>
                            </el-select>
                        </el-form-item>
                        
                        <el-form-item label="创建人" >
                            <el-input v-model="dataForm.empNum" placeholder="请输入工号"/>
                        </el-form-item>
                        <el-form-item label="商品编号" >
                            <el-input v-model="dataForm.skuCode" placeholder="请输入商品编号"/>
                        </el-form-item>
                         <el-form-item label="是否质量调拨" prop="isQualityAllotcate">
                            <el-select clearable v-model="dataForm.isQualityAllotcate" placeholder="是否质量调拨">
                                <el-option key="0" label="否" value="0"/>
                                <el-option key="1" label="是" value="1"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">查询</el-button>
                            <el-button  @click="resetForm('dataForm')">重置</el-button>
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
                   <div style="margin-bottom: 15px">
                        <el-button size="small" type="primary" @click="addHandle()">+ 新建</el-button>
                        <el-button size="small" @click="confimRecords">确认</el-button>     
                        <el-button size="small" @click="cancleRecords(0)">取消</el-button> 
                        <el-button size="small" v-if="isAuth('stock.whallocate.forceCancle')"  @click="cancleRecords(1)">强制取消</el-button>
                        <el-button size="small" @click="toImportPage" type="primary">单条导入</el-button>  
                        <el-button size="small" @click="exportExcel()" type="primary">导出</el-button><i v-show="loading" class="el-icon-loading"></i>
                        <el-button  @click="batchConfigIn(1)">批量指定虚仓出库</el-button>
                        <el-button  @click="batchConfigIn(2)">批量指定虚仓入库</el-button>

                    </div>
                    <el-table  size="small" border :data="dataList" v-loading="dataListLoading" 
                    element-loading-text="拼命加载中..." @selection-change="selectionChangeHandle">
                      <el-table-column
                      type="selection"
                      width="55"  :selectable='checkboxT' >
                    </el-table-column>
                      <el-table-column v-for="t in tableTitles"    :prop="t.prop" header-align="center" align="center" :label="t.title"  width="140">
                 
          <template slot-scope="scope">    
               <span  v-if="t.prop && (t.prop == 'inRealWarehouse' || t.prop == 'outRealWarehouse') && scope.row[t.prop]">{{ eval("scope.row[t.prop]."+t.subProp)}}</span>
               <span  v-else-if="t.prop && t.prop == 'recordStatus'">{{recordStatuses[scope.row.recordStatus]}}</span>
               <span  v-else-if="t.prop && t.prop == 'businessType'">{{formatOrderType(scope.row)}}</span>
               <span  v-else-if="t.prop && t.prop == 'isDiffIn'">{{scope.row.isDiffIn == 0?'无':(scope.row.isDiffIn == 1?'有':'')}}</span>
               <span  v-else-if="t.prop && t.prop == 'isDisparity'">{{scope.row.isDisparity == 0?'无':(scope.row.isDisparity == 1?'有':'')}}</span>
               <span  v-else-if="t.prop && t.prop == 'isQualityAllotcate'">{{scope.row.isQualityAllotcate == 0?'否':(scope.row.isQualityAllotcate == 1?'是':'')}}</span>
               <span  v-else-if="t.prop && t.prop == 'addType'">{{formatAddType(scope.row)}}</span>
               <span  v-else-if="t.prop && t.prop == 'creator'">{{formatCreator(scope.row)}}</span>
               <span  v-else-if="t.prop">{{ scope.row[t.prop]}}</span>
               <span v-else></span>       
          </template>
      </el-table-column>
       <el-table-column fixed="right"  prop="操作" header-align="center"
                          align="center" width="150" label="操作">
                            <template slot-scope="scope">
                                <el-button type="text"  size="small" @click="allocOutRateConfig(scope.row.id,scope.row.recordStatus)">出库比例分配</el-button>
                                <el-button type="text"  size="small" @click="allocRateConfig(scope.row.id,scope.row.recordStatus)">入库比例分配</el-button>
                                <!-- <el-button type="text" v-if="scope.row.tmsRecordCode"  size="small" @click="updateTmsOrder(scope.row)">派车单修改</el-button> -->
                                <el-button type="text" v-if="scope.row.recordStatus == 0"  size="small" @click="toEdit(scope.row.id, scope.row.recordStatus)">编辑</el-button>
                                <el-button type="text" v-if="scope.row.showDisparityButton"  size="small" @click="toCreateDisparity(scope.row.id)">差异创建</el-button>
                                <el-button type="text"   size="small" @click="toviewPage(scope.row.id, scope.row.recordStatus)">查看</el-button>
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
                    <el-dialog :title="allocationTip"   :visible.sync="dialogFormVisible"  width="80%">
          <el-table  
          :data="allocInfoList"
          :span-method="colspanMethod"
          border    max-height="450"
          style="width: 100%;">
         
             <el-table-column
                prop="realWarehouseCode"
                header-align="center"
                align="center"
                label="仓库编号">
              </el-table-column>
         
              <el-table-column
                prop="realWarehouseName"
                header-align="center"
                align="center"
                label="仓库名称">
              </el-table-column>
              
               <el-table-column
                prop="skuCode"
                header-align="center"
                align="center"
                label="商品编号">
              </el-table-column>
              
              <el-table-column
                prop="skuName"
                header-align="center"
                align="center"
                label="商品名称">
              </el-table-column>
              
                <el-table-column
                prop="allotQty"
                header-align="center"
                align="center"
                label="调拨总数量">
                </el-table-column>
                
                 <el-table-column
                prop="virtualWarehouseCode"
                header-align="center"
                align="center"
                label="虚仓编号">
                </el-table-column>
                
                <el-table-column
                prop="virtualWarehouseName"
                header-align="center"
                align="center"
                label="分配虚仓">
                </el-table-column>
                
                 <el-table-column
                prop="syncRate"
                header-align="center"
                align="center"
                label="默认比例">
                </el-table-column>
                
                <el-table-column
                    prop="configAbsolute"
                    header-align="center"
                    align="center"
                    label="按数量分配">
                    
                      <template scope="scope">
                      <div @click="absoluteClick(allocInfoList)">
                        <el-input size="small" :disabled="scope.row.isEditAbsolute?false:true" v-model="scope.row.configAbsolute" placeholder="请输入分配数量">
                        </el-input>
                      </div>
                    </template>
                    
                </el-table-column>
                
                 <el-table-column
                    prop="configSyncRate"
                    header-align="center"
                    align="center"
                    label="按比例分配">
                    
                      <template scope="scope">
                      <div @click="syncRateClick(allocInfoList)">
                        <el-input size="small" :disabled="scope.row.isEditSyncRate?false:true" v-model="scope.row.configSyncRate" placeholder="请输入分配比例">
                        </el-input>
                      </div>
                    </template>
                    
                </el-table-column>
          </el-table>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button  type="primary" @click="submitAllocConfig()">确 定</el-button>
      </div>
    </el-dialog>
    </el-main>
    <add v-if="addVisible" ref="add" @refreshDataList="getDataList"></add>
    <edit v-if="editVisible" ref="edit" @refreshDataList="getDataList"></edit>
    <viewPage v-if="viewPageVisible" ref="viewPage" @refreshDataList="getDataList"></viewPage>
     <importPage v-if="importVisible" ref="importPage" @refreshDataList="getDataList"></importPage>
     <recordsAllocation v-if="true" ref="recordsAllocation" @refreshDataList="getDataList"></recordsAllocation>

   </el-container>
        `
    });
});
