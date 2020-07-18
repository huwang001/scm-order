lyfdefine(['scm-order-add-or-update','scm-order-import','scm-order-detail-list','scm-order-allot-suggest','scm-order-create-allot','real_warehouse_allocation','scm-order-create-pack-demand'],
    function (addOrUpdate,secretImport,scmOrderDetailList,scmOrderAllotSuggest,scmOrderCreateAllot,realWarehouseAllocation,scmOrderCreatePackDemand) {
        return ({
                data: function () {
                    return {
                        show: false,
                        POOL_NAME: '/scm-order-admin',
                        orderStatusList: {},
                        dataForm: {
                            id: '', //id
                            orderCode:"",   // 预约单号
                            saleCode:"", // 销售单号
                            allotCode:"", // 调拨单号
                            doCode: '', //团购发货单号
                            customName: '', //所属客户
                            orderType: '', //单据类型 1:普通订单 2:卡卷订单
                            orderStatus: '', //单据状态
                            realWarehouseName: '', //do发货仓库名称
                            expectDate: '', //预计交货日期
                            createTime:"", // 创建时间
                            createTimeSt:"",
                            createTimeEnd:"",
                            startTime: '', //开始时间
                            endTime: '', //结束时间
                            pageSize: 10,
                            pageIndex: 1

                        },
                        dataList: [
                        ],
                        datePayTimeRange: [],
                        pageIndex: 1,
                        pageSize: 10,
                        totalPage: 0,
                        dataListLoading: false,
                        dataListSelections: [],
                        addOrUpdateVisible:true,
                        secretImportVisible: true,
                        scmOrderDetailListVisible: true,
                        scmOrderAllotSuggestVisible: true,
                        scmOrderCreateAllotVisible: true,
                        realWarehouseAllocationVisible:false,
                        scmOrderCreatePackDemandVisible:true,
                        needPackage:0,
                        dateTimeRange: [this.dateConversion(new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)), this.dateConversion(new Date())],
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
                    'addOrUpdate' : addOrUpdate,
                    'secretImport' : secretImport,
                    'scmOrderDetailList' : scmOrderDetailList,
                    'scmOrderAllotSuggest' : scmOrderAllotSuggest,
                    'scmOrderCreateAllot' : scmOrderCreateAllot,
                    'scmOrderCreatePackDemand' : scmOrderCreatePackDemand,
                    'realWarehouseAllocation': realWarehouseAllocation

                },

                activated: function () {
                    this.getDataList()
                    this.getSignType()
                },
                mounted: function () {
                    this.getDataList()
                    this.getSignType()
                },
                methods: {
                    // 获取当前时间，day为number，getDay(-1):昨天的日期;getDay(0):今天的日期;getDay(1):明天的日期;【以此类推】
                    getDayDate(day) {
                        var today = new Date();
                        var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
                        today.setTime(targetday_milliseconds); //注意，这行是关键代码

                        var tYear = today.getFullYear();
                        var tMonth = today.getMonth();
                        var tDate = today.getDate();
                        tMonth = this.doHandleMonth(tMonth + 1);
                        tDate = this.doHandleMonth(tDate);
                        var hour = today.getHours();
                        var minutes = today.getMinutes();
                        var second = today.getSeconds();
                        return tYear + "-" + tMonth + "-" + tDate +" "+hour+":"+minutes+":"+second;
                    },
                    doHandleMonth(month) {
                        var m = month;
                        if (month.toString().length == 1) {
                            m = "0" + month;
                        }
                        return m;
                    },
                    // 获取数据列表
                    getDataList() {
                        this.dataListLoading = true;
                        var _this = this;
                        if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                            //构建时间查询参数
                            _this.dataForm.startTime = _this.dateTimeRange[0];
                            _this.dataForm.endTime = _this.dateTimeRange[1];
                        }else{
                            _this.dataForm.startTime = null;
                            _this.dataForm.endTime = null;
                        }
                        if(_this.datePayTimeRange && _this.datePayTimeRange.length >1){
                            _this.dataForm.startPayTime = _this.datePayTimeRange[0];
                            _this.dataForm.endPayTime = _this.datePayTimeRange[1];
                        }else {
                            _this.dataForm.startPayTime = null;
                            _this.dataForm.endPayTime=null;
                        }
                        // if(this.dataForm.startTime==''&&this.dataForm.endTime==''){
                        //     this.dataForm.startTime=  new Date(this.getDayDate(-30));
                        //     this.dataForm.endTime= new Date();
                        // }

                        // if((this.dataForm.createTimeSt==null||this.dataForm.createTimeSt=='')&&(this.dataForm.createTimeEnd==null||this.dataForm.createTimeEnd=='')){
                        //     this.dataForm.createTimeSt=  new Date(this.getDayDate(-30));
                        //     this.dataForm.createTimeEnd= new Date();
                        //
                        // }
                        // if(!(this.datePayTimeRange && this.datePayTimeRange.length >1)) {
                        //
                        // }
                        // this.datePayTimeRange[0] = new Date(this.getDayDate(-30));
                        // this.datePayTimeRange[1] = new Date();
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/order/pageOrder',
                            method: 'post',
                            data: {
                                'pageNum': this.pageIndex,
                                'pageSize': this.pageSize,
                                'orderCode': this.dataForm.orderCode,
                                'saleCode': this.dataForm.saleCode,
                                'orderType': this.dataForm.orderType,
                                'orderStatus': this.dataForm.orderStatus,
                                'custom': this.dataForm.customName,
                                'startTime': this.dataForm.startTime,
                                'endTime': this.dataForm.endTime
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                            if (data && data.code === '0') {
                                this.dataList = data.data.list;
                                this.totalPage = data.data.total;
                                this.pageIndex = data.data.pageNum;
                                this.pageSize = data.data.pageSize;
                                if (data.data.list == null || data.data.list.length == 0){
                                    this.pageIndex = 1;
                                    this.pageSize = 10;
                                }
                            } else {
                                this.dataList = [];
                                this.totalPage = 0;
                                this.pageIndex = 1;
                                this.pageSize = 10;
                            }
                            this.dataListLoading = false
                        }).catch(result=> {
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                            this.dataListLoading = false
                        }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                            this.dataListLoading = false
                        });
                    },
                    refreshDataList() {
                        this.getDataList()
                    },

                    // 每页数
                    sizeChangeHandle(val) {
                        this.pageSize = val
                        this.pageIndex = 1
                        this.getDataList()
                    },
                    //重置
                    reset(dataForm) {
                        this.dataForm = {
                            id: '', //id
                            orderCode:"",   // 预约单号
                            saleCode:"", // 销售单号
                            allotCode:"", // 调拨单号
                            doCode: '', //团购发货单号
                            customName: '', //所属客户
                            orderType: '', //单据类型 1:普通订单 2:卡券订单
                            orderStatus: '', //单据状态
                            realWarehouseName: '', //do发货仓库名称
                            expectDate: '', //预计交货日期
                            createTime:"", // 创建时间
                            createTimeSt: new Date(this.getDayDate(-30)),
                            createTimeEnd: new Date(),
                            pageSize: 10,
                            pageIndex: 1,
                        };
                        this.dateTimeRange = [this.dateConversion(new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)), this.dateConversion(new Date())];
                    },
                    // 当前页
                    currentChangeHandle(val) {
                        this.pageIndex = val
                        this.getDataList()
                    },
                    // 多选
                    selectionChangeHandle(val) {
                        this.dataListSelections = val
                    },

                    addOrUpdate(id, type,signType) {
                        this.addOrUpdateVisible = true
                        this.$nextTick(() => {
                            this.$refs.addOrUpdate.init(id, type,signType)
                        })
                    },
                    scmOrderDetailList(orderCode) {
                        this.scmOrderDetailListVisible = true
                        this.$nextTick(() => {
                            this.$refs.scmOrderDetailList.init(orderCode)
                        })
                    },
                    scmOrderAllotSuggest(orderCode,vwWarehouseCode) {
                        this.scmOrderAllotSuggestVisible = true
                        this.$nextTick(() => {
                            this.$refs.scmOrderAllotSuggest.init(orderCode,vwWarehouseCode)
                        })
                    },

                    scmOrderCreateAllot(orderCode, orderStatus) {
                    	//部分锁定，弹窗提醒
                    	if(orderStatus == 1){
                    		this.scmOrderCreateAllotVisible = false;
                    		this.$confirm('此预约单状态为部分锁定, 是否继续?', '提示', {
                    	          confirmButtonText: '确定',
                    	          cancelButtonText: '取消',
                    	          type: 'warning'
                    	        }).then(() => {
                    	          this.scmOrderCreateAllotVisible = true;
                    	          this.$nextTick(() => {
                                      this.$refs.scmOrderCreateAllot.init(orderCode)
                                  })
                    	        });
                    	} else {
                    		this.scmOrderCreateAllotVisible = true;
                    		this.$nextTick(() => {
                                this.$refs.scmOrderCreateAllot.init(orderCode)
                            })
                    	}
                    },
                    scmOrderCreatePackDemand(orderCode, orderStatus) {
                        this.scmOrderCreatePackDemandVisible = true;
                        this.$nextTick(() => {
                            this.$refs.scmOrderCreatePackDemand.init(orderCode)
                        })
                    },
                    // 加工完成
                    updateStatus (id,status) {
                        if (status == "00"){
                            status = "01";
                            this.open(id,status);
                        }else if (status == "01"){
                            status = "00";
                            this.submit(id,status)
                        }
                    },

                    // 加工完成
                    processSubmit (orderCode) {
                        this.$http({
                            url: this.POOL_NAME + `/order/v1/order/processCompleted`,
                            method: 'post',
                            data :{
                                'orderCode': orderCode
                            },
                            headers:{
                                'Content-Type':'application/json'
                            }
                        }).then(data =>  {
                            let _data = data.data;
                        if (_data && _data.code == 0) {
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 2000,
                        });
                            this.refreshDataList();
                        } else {
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            })
                        }
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                    });
                    },

                    open(id,status) {
                        this.$confirm('停用会影响支付渠道使用，确定修改?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            // 调用提交方法
                            this.submit(id,status);
                    }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消停用'
                            });
                    });
                    },


                    // 生成出库单
                    createStockOut (scmorder) {
                        this.$http({
                            url: this.POOL_NAME + `/order/v1/order/createStockOut`,
                            method: 'post',
                            data :{
                                'orderCode': scmorder.orderCode
                            },
                            headers:{
                                'Content-Type':'application/json'
                            }
                        }).then(data =>  {
                            let _data = data.data;
                        if (_data && _data.code == 0) {
                            this.$message({
                                message: '出库单已生成！',
                                type: 'success',
                                duration: 2000,
                            });
                            this.refreshDataList();
                        } else {
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            })
                        }
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                    });
                    },



                    deleteConfig(id) {
                        this.$confirm('删除会影响该门店pos支付使用，确认是否删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            // 调用提交方法
                            this.submit(id);
                    }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消停用'
                            });
                    });
                    },
                    // 获取状态
                    getDic(dicName) {
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/order/queryOrderStatusList',
                            method: 'post',
                            data: {
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let _data = result.data;
                            if (_data && _data.code == '0') {
                                this.orderStatusList = _data.data;
                            } else {
                                this.orderStatusList = []
                            }
                        }).catch(result=> {
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                        }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                        });
                    },

                    // 查询商户属性
                    getSignType(){
                        this.getDic('signType');
                    },

                    //导出预约单列表
                    exportData() {
                        let _this = this;
                        if(_this.dateTimeRange && _this.dateTimeRange.length >1){
                            _this.dataForm.startTime = _this.dateTimeRange[0];
                            _this.dataForm.endTime = _this.dateTimeRange[1];
                        }else {
                            _this.dataForm.startTime = null;
                            _this.dataForm.endTime=null;
                        }
                        let star = _this.dataForm.startTime;
                        if (star == null || null==_this.dataForm.endTime) {
                            _this.$message({
                                message: '请选择开始时间'
                            });
                            return;
                        }
                        let exportUrl = '';
                        if (window.sessionStorage.getItem("menuList") === null) {
                            exportUrl = "/order/v1/order/export";
                        } else {
                            exportUrl = this.POOL_NAME + "/order/v1/order/export";
                        }
                        exportUrl = exportUrl + '?custom=' + this.dataForm.customName
                            + '&endTime=' + _this.dataForm.endTime + '&orderCode=' + this.dataForm.orderCode+ '&orderStatus=' + this.dataForm.orderStatus
                            + '&saleCode=' + this.dataForm.saleCode+ '&startTime=' + _this.dataForm.startTime

                        window.open(exportUrl)
                    },

                    //导出预约单详细列表
                    exportDetailData() {
                        let _this = this;
                        if(_this.dateTimeRange && _this.dateTimeRange.length >1){
                            _this.dataForm.startTime = _this.dateTimeRange[0];
                            _this.dataForm.endTime = _this.dateTimeRange[1];
                        }else {
                            _this.dataForm.startTime = null;
                            _this.dataForm.endTime=null;
                        }
                        let star = _this.dataForm.startTime;
                        if (star == null || null==_this.dataForm.endTime) {
                            _this.$message({
                                message: '请选择开始时间'
                            });
                            return;
                        }
                        let exportUrl = '';
                        if (window.sessionStorage.getItem("menuList") === null) {
                            exportUrl = "/order/v1/order/exportOrderDetail";
                        } else {
                            exportUrl = this.POOL_NAME + "/order/v1/order/exportOrderDetail";
                        }
                        exportUrl = exportUrl + '?custom=' + this.dataForm.customName
                            + '&endTime=' + _this.dataForm.endTime+ '&orderCode=' + this.dataForm.orderCode+ '&orderStatus=' + this.dataForm.orderStatus
                            + '&saleCode=' + this.dataForm.saleCode+ '&startTime=' + _this.dataForm.startTime

                        window.open(exportUrl)
                    },

                    dateConversion(value){
                        var d = value;
                        var date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
                        return date;
                    },



                    importData(){
                        this.secretImportVisible = true;
                        this.$nextTick(function () {
                            this.$refs.secretImport.init();
                        });
                    },


                    orderStatusFormat (row, column, cellValue){
                        let obj = {};
                        obj = this.orderStatusList.find((item)=>{
                            return item.orderStatusCode === cellValue;
                        });
                        if (obj == undefined){
                            return cellValue;
                        }
                        var needPackeage = row.needPackage;
                        if(needPackeage ==0 && cellValue==12){
                            return "调拨入库";
                        }else if(needPackeage ==1 && cellValue==12){
                            return "调拨入库待加工";
                        }
                        return obj.orderStatusName;
                    },

                    //时间转换
                    formatDate(row, column, cellValue) {
                        let realDate;
                        if (cellValue) {
                            let date = new Date(cellValue);
                            let y = date.getFullYear();
                            let MM = date.getMonth() + 1;
                            MM = MM < 10 ? ('0' + MM) : MM;
                            let d = date.getDate();
                            d = d < 10 ? ('0' + d) : d;
                            let h = date.getHours();
                            h = h < 10 ? ('0' + h) : h;
                            let m = date.getMinutes();
                            m = m < 10 ? ('0' + m) : m;
                            let s = date.getSeconds();
                            s = s < 10 ? ('0' + s) : s;
                            return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
                        } else {
                            realDate = "";
                        }
                        return realDate;
                    },

                    //时间转换
                    formatTimeDay(row, column, cellValue) {
                        let realDate;
                        if (cellValue) {
                            let date = new Date(cellValue);
                            let y = date.getFullYear();
                            let MM = date.getMonth() + 1;
                            MM = MM < 10 ? ('0' + MM) : MM;
                            let d = date.getDate();
                            d = d < 10 ? ('0' + d) : d;
                            let h = date.getHours();
                            h = h < 10 ? ('0' + h) : h;
                            let m = date.getMinutes();
                            m = m < 10 ? ('0' + m) : m;
                            let s = date.getSeconds();
                            s = s < 10 ? ('0' + s) : s;
                            return y + '-' + MM + '-' + d ;
                        } else {
                            realDate = "";
                        }
                        return realDate;
                    },
                    //实仓调拨
                     realWarehouseAllocation(orderCode) {
                        var _this2 = this;
                        if(orderCode!='' && orderCode!=null){
                             this.dataListSelections.push(orderCode);
                         }
                        if(this.dataListSelections.length==0 || orderCode==''){
                            this.$message({
                                message: '预约单号不能为空'
                            });
                            return;
                        }

                        this.dataListSelections.forEach(function (e) {
                        });
                        // if(check==true){
                        //     this.$message({
                        //         message: '预约单包装类型不一致,预约单号:'+orderCode
                        //     });
                        // }
                        //去重
                        this.dataListSelections= Array.from(new Set(this.dataListSelections));
                        this.realWarehouseAllocationVisible = true;
                        this.$nextTick(function () {
                            _this2.$refs.realWarehouseAllocation.init(_this2.dataListSelections);
                        });
                    },
                    //创建包装需求单
                    createPackDemand(orderCode){
                        var _this = this;
                        this.$http({
                            url: this.POOL_NAME + "/order/v1/order/createPackDemand",
                            method: 'post',
                            params: _this.$http.adornParams({
                                "orderCode": orderCode,
                            })
                        }).then(data =>  {
                            let _data = data.data;
                            if (_data && _data.code == 0) {
                                this.$message({
                                    message: '操作成功！',
                                    type: 'success',
                                    duration: 2000,
                                });
                                this.refreshDataList();
                            } else {
                                this.$message({
                                    message: "操作失败，原因："+_data.msg,
                                    type: 'fail',
                                    duration: 1500,
                                })
                            }
                        }).catch(result=> {
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                        }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                        });
                    },
                    // 强制创建DO
                    forceCreateDo(orderCode){
                        var _this = this;
                        this.$http({
                            url: this.POOL_NAME + "/order/v1/order/forceCreateDo",
                            method: 'post',
                            params: _this.$http.adornParams({
                                "orderCode": orderCode,
                            })
                        }).then(data =>  {
                            let _data = data.data;
                            if (_data && _data.code == 0) {
                                this.$message({
                                    message: '操作成功！',
                                    type: 'success',
                                    duration: 2000,
                                });
                                this.refreshDataList();
                            } else {
                                this.$message({
                                    message: "操作失败，原因："+_data.msg,
                                    type: 'fail',
                                    duration: 1500,
                                })
                            }
                        }).catch(result=> {
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                        }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                        });
                    },

                },
                template: `

    <div class="mod-config">
        <el-card class="box-card search-input" shadow="never">
            <div slot="header">
                <span >预约单</span>
            </div>
            <el-form  label-width="120px" size="mini" :model="dataForm" @keyup.enter.native="getDataList()">

                <el-col :span="6" >
                    <el-form-item label="预约单号：">
                        <el-input v-model="dataForm.orderCode" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="销售单号：">
                        <el-input v-model="dataForm.saleCode" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                <el-col :span="6">
                    <el-form-item label="预约单状态：">
                          <el-select v-model="dataForm.orderStatus" clearable>
                          <el-option label="全部" value=""></el-option>
                            <el-option
                              v-for="item in orderStatusList"
                              :key="item.orderStatusCode"
                              :label="item.orderStatusName"
                              :value="item.orderStatusCode">
                            </el-option>
                          </el-select>
                    </el-form-item>
               </el-col>
               
                <el-col :span="6">
                    <el-form-item label="销售订单类型：">
                          <el-select v-model="dataForm.orderType" clearable>
                          <el-option label="全部" value=""></el-option>
                          <el-option label="普通订单" value="1"></el-option>
                           <el-option label="卡卷订单" value="2"></el-option>
                          </el-select>
                    </el-form-item>
               </el-col>
                
                
             
                <el-col :span="6">
                    <el-form-item label="所属客户：">
                        <el-input v-model="dataForm.customName" clearable></el-input>
                    </el-form-item>
                </el-col>

                         
                         
                <el-col :span="5">
                
                <el-form-item label="创建时间：" prop="dateTimeRange">
            <el-date-picker
              value-format='yyyy-MM-dd HH:mm:ss'
              :default-time="['00:00:00', '23:59:59']"
              v-model="dateTimeRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right">
            </el-date-picker>
         </el-form-item>
<!--                       <el-form-item label="创建时间：">-->
<!--                           <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"  v-model="dataForm.createTimeSt" type="date" placeholder="选择日期" clearable></el-date-picker>-->
<!--                       </el-form-item>-->
<!--                   </el-col>-->
<!--                  <el-col :span="6">-->
<!--                       <el-form-item label="至：">-->
<!--                            <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.createTimeEnd" type="date" placeholder="选择日期" clearable></el-date-picker>-->
<!--                       </el-form-item>-->
                  </el-col>
               
               <el-col :span="10" :offset="10">
                   <el-form-item>
                        <el-button type="primary" @click="getDataList()">查询</el-button>
                        <el-button @click="reset('dataForm')">重置</el-button>
                        <el-button @click="exportData()">导出列表</el-button>
                        <el-button @click="exportDetailData()">导出明细</el-button>
                    </el-form-item>
               </el-col>
            </el-form>
        </el-card>
        <el-card >
            <el-table
                    :data="dataList"
                    size="mini"
                    border
                    v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle"
                    style="width: 100%;">
                      <el-table-column
                            prop="id"
                            header-align="center"
                            align="center"
                            v-if="show" 
                            label="id">
                    </el-table-column>
                    <el-table-column
                            prop="orderCode"
                            header-align="center"
                            align="center"
                            label="预约单号">
                    </el-table-column>
                    <el-table-column
                            prop="saleCode"
                            header-align="center"
                            align="center"
                            label="销售单号">
                    </el-table-column>
                    <el-table-column
                            prop="allotCode"
                            header-align="center"
                            align="center"
                            label="调拨单号">
                    </el-table-column>
                     
                     <el-table-column
                            prop="doCode"
                            header-align="center"
                            align="center"
                            label="团购发货单号">
                    </el-table-column>
                     <el-table-column
                            prop="customName"
                            header-align="center"
                            align="center"
                            label="所属客户">
                    </el-table-column>
                    
                    <el-table-column
                            prop="orderType"
                            header-align="center"
                            width="105"
                            align="center"
                            label="单据类型">
                             <template slot-scope="scope">
                            <span v-if ="scope.row.orderType == 1">普通订单</span>
                            <span v-if ="scope.row.orderType == 2">卡卷订单</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="needPackage" header-align="center" align="center" label="是否需要包装">
                        <template slot-scope="scope">
                            <span v-if ="scope.row.needPackage == 0">不需要</span>
                            <span v-if ="scope.row.needPackage == 1">需要</span>
                        </template>
                    </el-table-column>
                     <el-table-column
                            prop="orderStatus"
                            header-align="center"
                            width="105"
                            align="center"
                            :formatter="orderStatusFormat"
                            label="单据状态">
                    </el-table-column>
                     <el-table-column
                            prop="factoryCode,realWarehouseCode"
                            header-align="center"
                            align="center"
                            label="仓库编码">
                            <template slot-scope="scope">
                            {{scope.row.factoryCode}}-{{scope.row.realWarehouseCode}}
                            </template>
                    </el-table-column>
                     <el-table-column
                            prop="realWarehouseName"
                            header-align="center"
                            align="center"
                            label="仓库名称">
                    </el-table-column>
                     <el-table-column
                            prop="expectDateStr"
                            header-align="center"
                            width="140"
                            align="center"
                            label="交货日期">
                    </el-table-column>
                    
                    <el-table-column
                            prop="createTimeStr"
                            header-align="center"
                            width="140"
                            align="center"
                            label="创建时间">
                    </el-table-column>
                   
                  
                    <el-table-column
                        fixed="right"
                        header-align="center"
                        align="center"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="forceCreateDo(scope.row.orderCode)">
                            <span v-if="scope.row.needPackage==0 && scope.row.hasTradeAudit==1 && scope.row.orderStatus==0">强制创建DO</span>
                        </el-button>
                        <el-button type="text" size="small" @click="scmOrderDetailList(scope.row.orderCode)">查看详情</el-button>
                        
                        <el-button type="text" size="small" @click="scmOrderAllotSuggest(scope.row.orderCode,scope.row.vmWarehouseCode)">
                            <span v-if ="scope.row.orderStatus == 1 && scope.row.vmWarehouseCode !=null && scope.row.vmWarehouseCode !=''">调拨虚仓建议</span>
                         </el-button>
                        <el-button type="text" size="small" v-if="scope.row.needPackage==1 && scope.row.orderStatus==0 && scope.row.hasTradeAudit==1" @click="scmOrderCreatePackDemand(scope.row.orderCode, scope.row.orderStatus)">强制包装</el-button>
                        <el-button type="text" size="small" v-if="(scope.row.orderStatus==1 || scope.row.orderStatus==2) && scope.row.hasTradeAudit==1" @click="scmOrderCreateAllot(scope.row.orderCode, scope.row.orderStatus)">生成调拨单</el-button>
                        <!--<el-button type="text" size="small" @click="realWarehouseAllocation(scope.row.orderCode)">实仓调拨</el-button>-->
                        <el-button type="text" size="small" @click="processSubmit(scope.row.orderCode)">
                            <span v-if ="scope.row.orderStatus == 12 && scope.row.needPackage == 1">加工完成</span>
                         </el-button>
                        
                        <el-button type="text" size="small" @click="createStockOut(scope.row)">
                            <span v-if ="scope.row.orderStatus == 20 || (scope.row.orderStatus == 12 && scope.row.needPackage == 0)">生成出库单</span>
                         </el-button>
                        
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <el-pagination
                @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle"
                :current-page="pageIndex"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                :total="totalPage"
                layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

        <!-- 弹窗, 新增 / 修改 -->
             <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="refreshDataList"></add-or-update>
             <secretImport v-if="secretImportVisible" ref="secretImport" @refreshDataList="refreshDataList"></secretImport>
              <scmOrderDetailList v-if="scmOrderDetailListVisible" ref="scmOrderDetailList" @refreshDataList="refreshDataList"></scmOrderDetailList>
               <scmOrderAllotSuggest v-if="scmOrderAllotSuggestVisible" ref="scmOrderAllotSuggest" @refreshDataList="refreshDataList"></scmOrderAllotSuggest>
               <scmOrderCreateAllot v-if="scmOrderCreateAllotVisible" ref="scmOrderCreateAllot" @refreshDataList="refreshDataList"></scmOrderCreateAllot>
              <scmOrderCreatePackDemand v-if="scmOrderCreatePackDemandVisible" ref="scmOrderCreatePackDemand" @refreshDataList="refreshDataList"></scmOrderCreatePackDemand>
               <!--实仓调拨-->
               <realWarehouseAllocation v-if="realWarehouseAllocationVisible" ref="realWarehouseAllocation" @refreshDataList="getDataList"></realWarehouseAllocation>
    </div>
    `
            }
        );
    });