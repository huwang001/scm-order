lyfdefine(['out_warehouse_record_detail'],function(addOrUpdate){
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: {
                    recordCode:'',
                    startDate:'',
                    endDate:'',
                    frontRecord:{
                        recordCode: ''
                    },
                    recordType:'',
                    recordStatus:'',
                    realWarehouseCode:'',
                    pageIndex: 1,
                    pageSize: 10,
                    totalPage: 0
                },
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
                dateTimeRange: [],
                dataListLoading:false,
                warehouseRecordTypes: {},
                warehouseRecordStatuses: {},
                frontRecordTypes:{},
                detailsVisible:true,
                tableList: [],//标题自定义信息集合
                checkedTitles: [],
                originTableTitles: [],
                tableTitles: [],
                lastCheckedTitle: [],
                oldCheckedTitles: [],
                oldTitleOrder: {},
                isRequest: false,
                tableCode: 'out_warehouse_record'
            }
        },
        methods: {
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                if (this.dateTimeRange && this.dateTimeRange.length > 1) {
                    //构建时间查询参数
                    this.dataForm.startDate = this.dateTimeRange[0];
                    this.dataForm.endDate = this.dateTimeRange[1];
                }else{
                    this.dataForm.startDate = null;
                    this.dataForm.endDate = null;
                }
                this.dataListLoading = true;
                this.$http.post('/scm-order-admin/order/v1/warehouse_record/out_warehouse_record/list',
                    this.dataForm
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.dataList = res.data.data.list;
                        _this.dataForm.totalPage = res.data.data.total;
                    }else{
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                        _this.$message('查询失败！');
                    }
                    _this.dataListLoading = false;
                }).catch(function(error){
                    console.log(error);
                    _this.dataListLoading = false;
                });
            },
            //获取出库单类型集合
            getWarehouseRecordTypes: function getWarehouseRecordTypes() {
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/queryOutWarehouseRecordType/list'
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.warehouseRecordTypes = res.data.data;
                    }
                }).catch(function(error){
                    console.log(error);
                });
            },
            //获取前置单类型集合
            getFrontRecordTypes: function getFrontRecordTypes() {
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/queryFrontRecordType/list'
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.frontRecordTypes =res.data.data;
                    }
                }).catch(function(error){
                    console.log(error);
                });
            },
            //获取出库单状态集合
            getWarehouseRecordStatuses: function getWarehouseRecordStatuses(){
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/queryOutWarehouseRecordStatus/list'
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.warehouseRecordStatuses =res.data.data;
                    }
                }).catch(function(error){
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
            //详情页
            detailsHandle(warehouseRecord){
                this.$refs.addOrUpdate.init(warehouseRecord,this.warehouseRecordTypes[warehouseRecord.recordType]);
            },
            onSubmit() {
                this.getDataList();
            },
            resetForm: function resetForm() {
                this.dataForm.recordCode = '';
                this.dataForm.frontRecord.recordCode = '';
                this.dataForm.recordType = '';
                this.dataForm.recordStatus = '';
                this.dataForm.realWarehouseCode = '';
                this.dateTimeRange = new Array();
            } , updateChangedTitles: function (value) {
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
                    {title: "出库单号", rowCode: "recordCode", prop: "recordCode", isShow: 1, orderNum: 1},
                    {title: "出库单状态", rowCode: "recordStatus", prop: "recordStatus", isShow: 1, orderNum: 2},
                    {title: "外部单号", rowCode: "frontRecord.outRecordCode", prop: "frontRecord",subProp:"outRecordCode", isShow: 1, orderNum: 3},
                    {title: "前置单号", rowCode: "frontRecord.recordCode", prop: "frontRecord",subProp:"recordCode", isShow: 1, orderNum: 4},
                    {title: "前置单据类型", rowCode: "frontRecord.recordType", prop: "frontRecord",subProp:"recordType", isShow: 1, orderNum: 5},
                    {
                        title: "工厂代码",
                        rowCode: "factoryCode",
                        prop: "factoryCode",
                        isShow: 1,
                        orderNum: 6
                    },
                    {
                        title: "仓库编号",
                        rowCode: "realWarehouseCode",
                        prop: "realWarehouseCode",
                        isShow: 1,
                        orderNum: 7
                    },
                    {
                        title: "仓库名称",
                        rowCode: "realWarehouseName",
                        prop: "realWarehouseName",
                        isShow: 1,
                        orderNum: 8
                    },
                    {
                        title: "出库单类型",
                        rowCode: "recordType",
                        prop: "recordType",
                        isShow: 1,
                        orderNum: 9
                    },
                    {
                        title: "备注",
                        rowCode: "frontRecord.remark",
                        prop: "frontRecord",
                        subProp:"remark",
                        isShow: 1,
                        orderNum: 10
                    },
                    {
                        title: "供应商",
                        rowCode: "supplier",
                        prop: "supplier",
                        isShow: 1,
                        orderNum: 11
                    },
                    {
                        title: "手机号码",
                        rowCode: "phoneNum",
                        prop: "phoneNum",
                        isShow: 1,
                        orderNum: 12
                    },
                    {
                        title: "创建时间",
                        rowCode: "createTime",
                        prop: "createTime",
                        isShow: 1,
                        orderNum: 13
                    }]
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
            }
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
        components: {
            'addOrUpdate': addOrUpdate
        },
        mounted() {
            this.getDataList();
            this.getWarehouseRecordTypes();
            this.getFrontRecordTypes();
            this.getWarehouseRecordStatuses();
        },
        template: `
            <el-container>
                <el-main>
                    <el-row type="flex" justify="start" style="line-height:30px;height: 30px;font-size: 14px">
                         <el-col :span="2"><div style="font-weight: bold">出库单查询</div></el-col>
                    </el-row>
                    <el-form :inline="true" :model="dataForm" class="demo-form-inline">
                        <el-form-item label="前置单号">
                            <el-input v-model="dataForm.frontRecord.recordCode" placeholder="请输入单号"></el-input>
                        </el-form-item>
                        <el-form-item label="出库单号">
                            <el-input v-model="dataForm.recordCode" placeholder="请输入单号"></el-input>
                        </el-form-item>
                        <el-form-item label="出库单类型" prop="recordType">
                            <el-select v-model="dataForm.recordType" placeholder="单类型">
                                <el-option v-for="(value, key) in warehouseRecordTypes" :key="key" :label="value" :value="key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="出库单状态" prop="recordStatus">
                            <el-select v-model="dataForm.recordStatus" placeholder="状态">
                                <el-option v-for="(value, key) in warehouseRecordStatuses" :key="key" :label="value" :value="key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="创建时间">
                            <el-date-picker value-format='yyyy-MM-dd HH:mm:ss' :default-time="['00:00:00', '23:59:59']" v-model="dateTimeRange"
                                type="datetimerange" :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期"
                                end-placeholder="结束日期" align="right">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="仓库编号">
                            <el-input v-model="dataForm.realWarehouseCode" placeholder="请输入仓库编号"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">查询</el-button>
                            <el-button  @click="resetForm()">重置</el-button>
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
                    <el-table  size="small" border :data="dataList" v-loading="dataListLoading" element-loading-text="拼命加载中...">
                       <el-table-column v-for="t in tableTitles"    :prop="t.prop" header-align="center" align="center" :label="t.title"  width="140">
                 <template slot-scope="scope">
                    <span  v-if="t.prop && t.prop=='recordStatus'">{{warehouseRecordStatuses[scope.row.recordStatus]}}</span>
                     <span  v-else-if="t.prop && t.prop == 'frontRecord' && scope.row[t.prop] && t.subProp == 'recordType'">{{frontRecordTypes[eval("scope.row[t.prop]."+t.subProp)]}}</span>
                     <span  v-else-if="t.prop && t.prop == 'frontRecord' && scope.row[t.prop]">{{eval("scope.row[t.prop]."+t.subProp)}}</span>
                 <span  v-else-if="t.prop && t.prop == 'recordType'">{{warehouseRecordTypes[scope.row.recordType]}}</span>
                  <span  v-else-if="t.prop">{{scope.row[t.prop]}}</span>
               <span v-else></span> 
                 </template>
      </el-table-column> 
                       <el-table-column fixed="right" prop="操作" header-align="center" align="center" min-width="150" label="操作">
                            <template slot-scope="scope">
                                <el-button type="text" size="small" @click="detailsHandle(scope.row)">查看详情</el-button>
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
                    <add-or-update v-if="detailsVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
                </el-main>
            </el-container>
        `
    });
});
