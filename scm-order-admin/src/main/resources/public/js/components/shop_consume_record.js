lyfdefine(['shop_consume_record_addOrUpdate','shop_consume_record_detail'],function(addOrUpdate,detail){
    return({
        data:function data() {
            return{
                dataForm: {
                    startTime: null,//开始日期
                    endTime: null,//结束日期
                    pageIndex: 1,
                    pageSize: 10,
                    totalPage: 0,
                    recordType:39,//单据类型-门店报废
                    recordCode:null,
                    recordStatus:null,
                    realWarehouseCode:null,
                    reasonCode:null,
                    factoryCode:null
                },
                factoryForm:{
                    isRequest:true,
                    codeOrName:null,
                    factoryLoading:false,
                    pageIndex:1,
                    pageSize:15,
                },
                employeeInfos:[],
                timer:-1,
                addOrUpdateVisible:true,
                detailVisible:true,
                dataList: [],
                dataListLoading: false,
                statusList: [
                    {value: 0, label: '已新建'},
                    {value: 1, label: '确认中'},
                    {value: 2, label: '已取消'},
                    {value: 10, label: '已出库'},
                    {value: 21, label: '确认失败'}
                ],
                reasonCodeList:[],
                realWareCode:[],
                factoryCode1:[],
                organizationCode:[],
                multipleSelection: [],
                dateTimeRange: [],
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
        created:function() {
            this.getRecordType();
            this.getDataList();
            this.getReasonList();
            this.remoteMethod('');
        },
        components: {
            'addOrUpdate': addOrUpdate,
            'detail':detail,
        },
        methods: {
            getDataList1:function(){
                this.dataForm.pageIndex=1;
                this.dataForm.pageSize=10;
                this.dataForm.totalPage=0;
                this.getDataList();
            },
            //页面加载数据
            getDataList: function () {
                let _this = this;
                if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                    //构建时间查询参数
                    _this.dataForm.startTime = _this.dateTimeRange[0];
                    _this.dataForm.endTime = _this.dateTimeRange[1];
                } else {
                    _this.dataForm.startTime = null;
                    _this.dataForm.endTime = null;
                }
                this.dataListLoading = true;
                //调用接口查询调整单信息
                this.$http.post('/stock-admin/admin/v1/shop_consume/selectShopConsumeAdjustRecordByCondition',
                    this.dataForm
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                        let creatorList = [];
                        data.list.forEach(function (e){
                            creatorList.push(e.creator);
                        });
                        _this.getEmployeeNumberByUserId(creatorList);
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                    }
                    _this.dataListLoading = false;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            //根据用户id查找员工工号
            getEmployeeNumberByUserId : function(userIds){
                let _this = this;
                this.employeeInfos = [];
                //从后台查询recordType
                this.$http.post('/stock-admin/stock/v1/user-core/getEmployeeInfoByUserIds', userIds).then(function (_ref) {
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
                        if(null != employeeNumber){
                            columnValue = employeeNumber;
                        }
                    }
                });
                return columnValue;
            },
            getRecordType:function(){
                let _this = this;
                //从后台查询recordType
                this.$http.post('/stock-admin/admin/v1/getFrontRecordTypeCodeList').then(function (_ref) {
                    let data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataForm.recordType = data["SCAR"];
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },

            //根据工厂编号获取实仓编号和名字
            getRealWareByFCode: function(row){
                if('' == row || undefined == row){
                    this.dataForm.factoryCode = null;
                    this.dataForm.realWarehouseCode = null;
                    this.realWareCode = [];
                    this.remoteMethod('');
                    return;
                }
                let _this = this;
                this.$http.get('/stock-admin/admin/v1/queryRealWarehouseByFactoryCode/' + row + '/' + _this.dataForm.recordType).then(function (_ref2) {
                    let data = _ref2.data.data;
                    _this.realWareCode = [];
                    _this.dataForm.realWarehouseCode  = null;
                    data.forEach(function (e) {
                        let arr1 = {};
                        arr1.value = e.realWarehouseCode;
                        arr1.label = e.realWarehouseCode +"/" + e.realWarehouseName;
                        _this.realWareCode.push(arr1);
                    });

                }).catch(function (error) {
                    console.log(error);
                });
            },

            choiceFactory:function () {
                let fac = this.dataForm.factoryCode;
                if(null == fac || '' == fac || undefined == fac){
                    this.$message({
                        message: '请先选择工厂'
                    });
                }
            },
            //每页数量改变
            sizeChangeHandle: function sizeChangeHandle(val) {
                this.dataForm.pageSize = val;
                this.dataForm.pageIndex = 1;
                this.getDataList();
            },

            //当前页设置
            currentChangeHandle: function currentChangeHandle(val) {
                this.dataForm.pageIndex = val;
                this.getDataList();
            },

            //重置查询条件
            resetForm:function (formName) {
                this.$refs[formName].resetFields();
                this.dateTimeRange = [];
            },
            formatOrderStatus:function(row, column, cellValue){
                let _this = this;
                let columnValue = "";
                Object.keys(_this.statusList).forEach(function(key){
                    if(_this.statusList[key].value == row.recordStatus){
                        columnValue =  _this.statusList[key].label
                    }
                });
                return columnValue;
            },

            formatOrderReason: function(row, column, cellValue){

                let _this = this;
                let columnValue = "";
                Object.keys(_this.reasonCodeList).forEach(function(key){
                    if(_this.reasonCodeList[key].reasonCode == row.reasonCode){
                        columnValue =  _this.reasonCodeList[key].reasonName;
                    }
                });
                return columnValue;
            },

            getReasonList:function () {
                //调用接口查询损益单的业务原因字典
                let _this = this;
                this.$http.post('/stock-admin/admin/v1/queryBusinessReason',
                    this.dataForm.recordType
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data && data) {
                        _this.reasonCodeList = data;
                    } else {
                        _this.reasonCodeList = [];
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },

            addOrUpdateHandle: function (id, name, code) {
                let _this2 = this;

                this.addOrUpdateVisible = true;
                _this2.$refs.addOrUpdate.init(id, _this2.dataForm.recordType);
            },
            detailHandle:function(id,name,code){
                let _this2 = this;
                this.detailVisible = true;
                _this2.$refs.detail.init(id, _this2.dataForm.recordType);
            },
            handleSelectionChange:function(val) {
                this.multipleSelection = val;
            },

            //单据确认后定时刷新状态
            refreshData:function(ids){
                let _this = this;
                this.timer = setInterval(function () {
                    //根据id批量查询单子信息
                    _this.$http.post('/stock-admin/admin/v1/shop_consume/selectShopConsumeRecordByIds',ids).then(function (_ref2) {
                        let data = _ref2.data.data;
                        if (data) {
                            let isClose = true;//是否关闭定时器
                            data.forEach(function (val) {
                                if(val.recordStatus == 10 || val.recordStatus == 21){
                                    _this.dataList.forEach(function (vv) {
                                        if(vv.id == val.id){
                                            vv.recordStatus = val.recordStatus;
                                            vv.sapRecordCode = val.sapRecordCode;
                                        }
                                    })
                                }else {
                                    isClose = false;
                                }
                            });

                            if(isClose == true){
                                clearInterval(_this.timer);
                            }
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                },2000);
            },
            //调整单确认
            confrimRecord:function(){
                let _this2 = this;
                if(0 == this.multipleSelection.length){
                    this.$message({
                        message: '请选择调整单'
                    });
                    return;
                }else {
                    _this2.$confirm('确定执行操作吗, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let ids = [];
                        _this2.multipleSelection.forEach(function (row) {
                            ids.push(row.id);
                        });
                        //调用确认单接口，根据单据id修改单子状态为确认
                        _this2.$http.post('/stock-admin/admin/v1/shop_consume/confirmShopConsumeRecord',ids).then(function (_ref2) {
                            let data = _ref2.data;
                            if (data && data.code === '0') {
                                _this2.$message({
                                    message: '操作成功',
                                    type: 'success',
                                    duration: 1500,
                                    onClose: function onClose() {
                                        _this2.getDataList();
                                    }
                                });
                                _this2.refreshData(ids);
                            } else if (data && data.code != 0) {
                                _this2.$message({
                                    message: data.msg,
                                    type: 'error',
                                    duration: 1500,
                                    onClose: function onClose() {
                                        _this2.getDataList();
                                    }
                                });
                                _this2.refreshData(ids);
                            }
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }).catch(()=>{
                        _this2.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    })

                }
            },
            //调整单取消
            cancleRecord:function () {
                let _this2 = this;
                if(0 == this.multipleSelection.length){
                    this.$message({
                        message: '请选择调整单'
                    });
                    return;
                }else {
                    _this2.$confirm('确定要取消单据吗, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        _this2.multipleSelection.forEach(function (row) {
                            //调用取消接口，根据单据id修改单子状态为取消
                            _this2.$http.get('/stock-admin/admin/v1/shop_consume/cancelShopConsumeRecord?id='+row.id).then(function (_ref2) {
                                let data = _ref2.data;

                                if (data && data.code === '0') {
                                    _this2.$message({
                                        message: '操作成功',
                                        type: 'success',
                                        duration: 1500,
                                        onClose: function onClose() {
                                            _this2.getDataList();
                                        }
                                    });

                                } else if (data && data.code != 0) {
                                    _this2.$message({
                                        message: data.msg,
                                        type: 'error',
                                        duration: 1500,
                                        onClose: function onClose() {
                                            _this2.getDataList();
                                        }
                                    });
                                }
                            }).catch(function (error) {
                                console.log(error);
                            });
                        })
                    }).catch(()=>{
                        _this2.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    })
                }
            },
            remoteMethod(query) {
                let _this = this;
                _this.factoryForm.factoryLoading = true;

                if(query != _this.factoryForm.codeOrName){
                    _this.factoryForm.isRequest = true;
                    _this.factoryForm.codeOrName = query;
                    _this.factoryForm.pageIndex = 1;
                    _this.factoryCode1 = [];
                }
                if(_this.factoryForm.isRequest == true){
                    _this.$http.post('/stock-admin/admin/v1/shop_consume/queryShopFactory',_this.factoryForm).then(function (_ref) {
                        let data = _ref.data.data;
                        if (data) {
                            if(_this.factoryCode1.length == data.total){
                                _this.factoryForm.isRequest = false;
                            }else {
                                data.list.forEach(function (e) {
                                    let arr = {};
                                    arr.value = e.code;
                                    arr.label = e.code + '/' + e.name;
                                    _this.factoryCode1.push(arr);
                                });
                            }
                            _this.factoryForm.factoryLoading = false;
                        }
                    }).catch(function () {
                        _this.factoryForm.factoryLoading = false;
                        console.log(error);
                    });
                }
            },
            //点击下拉框时触发的事件
            factoryFocus:function () {
                let _this = this;
                let remoteMethodDom = document.getElementsByName('remoteMethod2');
                if(remoteMethodDom && remoteMethodDom.length){
                    remoteMethodDom[0].parentNode.parentNode.addEventListener('scroll', _this.scrollEvent,true);
                }
            },
            scrollEvent(e){
                //是否请求为true时
                if(this.factoryForm.isRequest){
                    /**
                       * 判断滚动条是否到达底部 如果到最底部调用 loadMore方法
                       * scrollTop 滚动条距顶部的长度
                       * clientHeight 当前显示的高度
                       * scrollHeight 滚动条总高度
                       */
                    if(e.currentTarget.scrollTop != 0){
                        if(e.currentTarget.scrollTop + e.currentTarget.clientHeight >= e.currentTarget.scrollHeight){
                            this.loadMore();
                        }
                    }
                }
            },
            loadMore(){
                this.factoryForm.pageIndex = this.factoryForm.pageIndex + 1;
                this.remoteMethod(this.factoryForm.codeOrName);
            }
        },
        watch:{
            newFactoryCode(val){
                if(val == ''){
                    this.dataForm.factoryCode = null;
                }
            },
        },
        computed:{
            newFactoryCode(){
                return this.dataForm.factoryCode;
            }
        },

        template:/*html*/`
        <div class="mod-config">
            <el-form label-position="left" label-width="120px" :inline="true" :model="dataForm" ref="dataForm" @keyup.enter.native="getDataList()">
                <el-row>
                    <el-col span="5">
                        <el-form-item label="门店报废单号" prop="recordCode">
                            <el-input v-model="dataForm.recordCode" placeholder="门店报废单号" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col span="5">
                        <el-form-item label="单状态" prop="recordStatus">
                            <el-select clearable v-model="dataForm.recordStatus" placeholder="单状态">
                                <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col span="8">     
                        <el-form-item label="创建时间：" prop="">
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
                    </el-col>
                </el-row>
                <el-row>
                    <el-col span="5">     
                        <el-form-item label="工厂编号/名称" prop="factoryCode">
                           <el-select 
                           v-model="dataForm.factoryCode" 
                           placeholder="工厂编号/名称" 
                           @change="getRealWareByFCode"
                           @focus="factoryFocus"
                           filterable
                           remote
                           :remote-method="remoteMethod"
                           clearable>
                               <el-option name="remoteMethod2" v-for="item in factoryCode1" :key="item.value" :label="item.label" :value="item.value">
                           </el-option>
                           </el-select>
                           <span v-if="factoryForm.factoryLoading" class="el-icon-loading"></span>
                        </el-form-item>
                    </el-col>
                    <el-col span="5">     
                        <el-form-item label="门店仓编号/名称" prop="realWarehouseCode">
                            <el-select v-model="dataForm.realWarehouseCode" placeholder="门店仓编号/名称" @focus="choiceFactory">
                               <el-option v-for="item in realWareCode" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col span="5">     
                        <el-form-item label="业务原因" prop="reasonCode">
                            <el-select v-model="dataForm.reasonCode" placeholder="业务原因" clearable>
                                <el-option v-for="item in reasonCodeList" :key="item.reasonCode" :label="item.reasonName" :value="item.reasonCode">
                            </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col span="5">    
                        <el-form-item>
                            <el-button @click="getDataList1()"  type="primary">查询</el-button>
                            <el-button  @click="resetForm('dataForm')">重置</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        
            <el-form>
                <el-form-item>
                    <el-button @click="addOrUpdateHandle()"  type="primary">新建</el-button>
                    <el-button @click="confrimRecord()" type="primary">确认</el-button>
                    <el-button @click="cancleRecord()" type="primary">取消</el-button>
                </el-form-item>
                <el-table
                  :data="dataList"
                  border
                  v-loading="dataListLoading"
                  element-loading-text="拼命加载中..."
                  @selection-change="handleSelectionChange"
                  style="width: 100%"
                  ref="multipleTable">
                     <el-table-column
                      type="selection"
                      width="55">
                     </el-table-column>
                     <el-table-column
                        prop="recordCode"
                        header-align="center"
                        align="center"
                        label="报废单号">
                     </el-table-column>
                     <el-table-column
                        prop="recordStatus"
                        header-align="center"
                        align="center"
                        label="单据状态"
                        :formatter="formatOrderStatus">
                     </el-table-column>
                     <el-table-column
                        prop="sapRecordCode"
                        header-align="center"
                        align="center"
                        label="SAP过账单号">
                     </el-table-column>
                     <el-table-column
                        prop="factoryCode"
                        header-align="center"
                        align="center"
                        label="工厂编号">
                     </el-table-column>
                     <el-table-column
                        prop="realWarehouseCode"
                        header-align="center"
                        align="center"
                        label="门店仓编号">
                     </el-table-column>
                     <el-table-column
                        prop="realWarehouseName"
                        header-align="center"
                        align="center"
                        label="门店仓名称">
                     </el-table-column>
                     <el-table-column
                        prop="organizationName"
                        header-align="center"
                        align="center"
                        label="组织归属"
                        width="250px">
                     </el-table-column>
                     <el-table-column
                        prop="reasonCode"
                        header-align="center"
                        align="center"
                        label="业务原因"
                        :formatter="formatOrderReason">
                     </el-table-column>
                     
                     <el-table-column
                        prop="createTime"
                        header-align="center"
                        align="center"
                        label="创建时间">
                     </el-table-column>
                     <el-table-column
                        prop="approveOACode"
                        header-align="center"
                        align="center"
                        label="OA审批号">
                     </el-table-column>
                     <el-table-column
                        prop="remark"
                        header-align="center"
                        align="center"
                        label="备注">
                     </el-table-column>
                     <el-table-column
                        prop="creator"
                        header-align="center"
                        align="center"
                        label="创建人"
                        :formatter="formatCreator">
                     </el-table-column>
                      <el-table-column fixed="right" prop="operate" header-align="center" align="center" label="操作">
                        <template slot-scope="scope">
                          <el-button type="text" v-if="scope.row.recordStatus==0" size="small" @click="addOrUpdateHandle(scope.row.id,scope.row.recordCode)">编辑</el-button>
                          <el-button type="text" size="small" @click="detailHandle(scope.row.id,scope.row.recordCode)">查看详情</el-button>
                        </template>
                     </el-table-column>
                </el-table>
                
                <el-pagination
                  @size-change="sizeChangeHandle"
                  @current-change="currentChangeHandle"
                  :current-page="dataForm.pageIndex"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="dataForm.pageSize"
                  :total="dataForm.totalPage"
                  layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
                
                <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
                <detail v-if="detailVisible" ref="detail"></detail>
            </el-form>
   
        </div>
        `
    });
});
