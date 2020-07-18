lyfdefine([], function () {
    return ({
        data: function data() {
            return {
                dataForm: {
                    allotType: null,
                    allotArea: null,
                    startTime: null,
                    endTime: null,
                    pageSize: 10,
                    pageIndex: 1
                },
                employeeInfos:[],
                menuKey: 1,
                dataList: [],
                dataListLoading: false,
                allotTypeList: [
                    {key:1, value:"指定单据寻源"},
                    {key:2, value:"当日寻源"},
                    {key:3, value:"按地区寻源"}
                    ],
                allotAreaList: [//寻源地区
                    {key:"G21200000_119-X001", value:"苏南"},
                    {key:"X001", value:"上海"},
                    {key:"X007", value:"南京"},
                    {key:"X005", value:"山东"},
                    {key:"X003", value:"浙江"},
                    {key:"X008", value:"北京"}
                ],
                allotAreaVisible:false
            };
        },
        methods: {
            menuTree: function () {
                ++this.menuKey
            },
            // 获取数据列表
            getDataList: function getDataList() {
                var _this = this;
                _this.dataListLoading = true;
                _this.$http.post('/scm-order-admin/order/v1/shopReplenish/queryReplenishAllotLogCondition',
                    _this.dataForm,
                ).then(function (_ref) {
                    var data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                        let creatorList = [];
                        _ref.data.data.list.forEach(function (e){
                            creatorList.push(e.creator);
                        });
                        _this.getEmployeeNumberByUserId(creatorList);
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                    }
                    _this.dataListLoading = false;
                })
            },
            //根据用户id查找员工工号
            getEmployeeNumberByUserId : function(userIds){
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
                        if(null != employeeNumber){
                            columnValue = employeeNumber;
                        }
                    }
                });
                return columnValue;
            },
            init: function init() {
                var _this = this;
                //查询日志列表
                _this.getDataList();
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

            getIndex: function getIndex(index) {
                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },

            resetForm: function (form) {
                this.dataForm.allotType = null;
                this.dataForm.allotArea = null;
                this.dataForm.startTime = null;
                this.dataForm.endTime = null;
            },
            allotTypeChange:function (row) {
                if(row!=null && row==3){
                    this.allotAreaVisible=true;
                }else{
                    this.allotAreaVisible=false;
                    this.dataForm.allotArea=null;
                }
            },
            formatAllotTypeArea(row, column, cellValue) {
                var _this = this;
                var columnValue = "";
                if(row.channelCode=='G21200000_119'){
                    columnValue='苏南';
                }else if(row.factoryCode=='X001'){
                    columnValue='上海';
                }else if(row.factoryCode=='X007'){
                    columnValue='南京';
                }else if(row.factoryCode=='X005'){
                    columnValue='山东';
                }else if(row.factoryCode=='X003'){
                    columnValue='浙江';
                }else if(row.factoryCode=='X008'){
                    columnValue='北京';
                }else{
                    columnValue='-';
                }
                return columnValue;
            }
        },
        created() {
            this.init();
        },
        template: `
 <div class="mod-config">
    <el-form :inline="true" :model="dataForm" ref="dataForm" :key="menuKey"> 
        <el-form-item label="开始执行时间">
            <el-col :span="11">
                <el-form-item prop="startDate">
                    <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" v-model="dataForm.startTime" style="width: 100%;">
                    </el-date-picker>
                </el-form-item>
            </el-col>
            <el-col class="line" :span="1">-</el-col>
        
            <el-col :span="11">
                 <el-form-item prop="endDate">
                     <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" v-model="dataForm.endTime" style="width: 100%;">
                     </el-date-picker>
                 </el-form-item>
            </el-col>
        </el-form-item>
     
        <el-form-item label="寻源类型：" prop="allotType">
            <el-select v-model="dataForm.allotType"  placeholder="请选择" clearable @change="allotTypeChange">
                <el-option v-for="allotSelect in allotTypeList" :key="allotSelect.key" :label="allotSelect.value" :value="allotSelect.key">
                </el-option>
            </el-select>
        </el-form-item>
        
        <el-form-item label="寻源地区：" prop="allotArea" v-if="allotAreaVisible">
            <el-select v-model="dataForm.allotArea"  placeholder="请选择" clearable >
                <el-option v-for="allotSelect in allotAreaList" :key="allotSelect.key" :label="allotSelect.value" :value="allotSelect.key">
                </el-option>
            </el-select>
        </el-form-item>
   
        <el-form-item>
            <el-button @click="getDataList()"  type="primary">查询</el-button>
            <el-button  @click="resetForm('dataForm')">重置</el-button>
        </el-form-item>
    </el-form>
    
    <el-table :data="dataList" border v-loading="dataListLoading" element-loading-text="拼命加载中..." style="width: 100%;">
        <el-table-column label="序号" type="index" width="70" align="center">
            <template scope="scope">
                <span>{{getIndex(scope.$index)}}</span>
            </template>
        </el-table-column>
    
        <el-table-column prop="allotTypeName" header-align="center" align="center" label="寻源类型">
        </el-table-column>
        
        <el-table-column prop="allotTypeArea" header-align="center" align="center" label="寻源地区" :formatter="formatAllotTypeArea">
        </el-table-column>
        
        <el-table-column prop="startTime" header-align="center" align="center" label="寻源开始时间">
        </el-table-column>
        
        <el-table-column prop="endTime" header-align="center" align="center" label="寻源结束时间">
        </el-table-column>
        
        <el-table-column prop="totalRecords" header-align="center" align="center" label="寻源订单数">
        </el-table-column>
        
        <el-table-column prop="successRecords" header-align="center" align="center" label="寻源成功订单数">
        </el-table-column>
        
        <el-table-column label="操作人" type="index" width="160" align="center">
            <template slot-scope="scope">
                <span>{{formatCreator(scope.row)}}</span>
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
  </div>
`
    });
});
