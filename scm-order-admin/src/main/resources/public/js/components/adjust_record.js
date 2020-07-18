lyfdefine(['shop_adjustment_record_detail', 'validate'], function (addOrUpdate) {
    return ({
        data: function data() {
            return {
                dataForm: {
                    pageNum: 1,
                    pageIndex: 1,
                    totalPage: 0,
                    pageSize: 10,
                    startCrateTime: null,//开始日期
                    endCreateTime: null,//结束日期
                    recordCode: null,
                    shopCode: null,
                    reason: null,
                    recordType: 24,
                    recordStatus: null
                },
                detailsVisible: true,
                dataList: [],
                dataListLoading: false,
                recordStatusList: [{
                    "status": 10,
                    "desc": "已出库"
                }],
                recordTypeList: [{
                    value: 24,
                    label: '门店试尝调整单'
                }],
                reasonList: [],
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
            };
        },
        created: function () {
            this.getDataList();
            this.getReasonList();
        },
        components: {
            'addOrUpdate': addOrUpdate
        },
        methods: {
            getDataList1: function () {
                this.dataForm.pageIndex = 1;
                this.dataForm.pageSize = 10;
                this.dataForm.totalPage = 0;
                this.getDataList();
            },
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                this.dataListLoading = true;
                if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                    //构建时间查询参数
                    _this.dataForm.startCrateTime = _this.dateTimeRange[0];
                    _this.dataForm.endCreateTime = _this.dateTimeRange[1];
                } else {
                    _this.dataForm.startCrateTime = null;
                    _this.dataForm.endCreateTime = null;
                }
                _this.dataForm.pageNum = _this.dataForm.pageIndex;
                this.$http.post('/scm-order-admin/order/v1/shopAdjustRecord/queryCondition', this.dataForm
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                        _this.dataForm.pageIndex = data.pageNum;
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                    }
                    _this.dataListLoading = false;

                });
            },

            formatOrderReason: function (row, column, cellValue) {
                let _this = this;
                let columnValue = "";
                Object.keys(_this.reasonList).forEach(function (key) {
                    if (_this.reasonList[key].value == row.reason) {
                        columnValue = _this.reasonList[key].label;
                    }
                });
                return columnValue;
            },
            getReasonList: function () {
                //调用接口查询损益单的业务原因字典
                let _this = this;
                this.$http.post('/scm-order-admin/order/v1/queryBusinessReason',
                    this.dataForm.recordType
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data) {
                        data.forEach(function (val) {
                            let el = {};
                            el.value = val.reasonCode;
                            el.label = val.reasonName;
                            _this.reasonList.push(el);
                        });
                    } else {
                        _this.reasonList = [];
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            },

            detailsHandle(recordCode) {
                this.detailsVisible = true;
                this.$refs.addOrUpdate.init(recordCode);
            },

            init: function init() {
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

            resetForm: function (formName) {
                this.$refs[formName].resetFields();
                this.dateTimeRange = [];
            },

            getIndex: function getIndex(index) {

                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },

            formatStatus: function formatStatus(row, column, value) {
                if (value == 10) {
                    return "已出库";
                }
                return "";
            },
        },
        template: /*html*/`
 <div class="mod-config">
   <el-form :inline="true" :model="dataForm" ref="dataForm" > <!--@keyup.enter.native="getDataList()">-->
      <el-form-item label="试吃单号：" prop="recordCode">
        <el-input v-model="dataForm.recordCode" placeholder="试吃单号" clearable></el-input>
      </el-form-item>
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
      <el-form-item label="试吃门店：" prop="shopCode">
          <el-input v-model="dataForm.shopCode" placeholder="试吃门店" clearable></el-input>
      </el-form-item>
      <br/>
      <el-form-item label="单据类型：" prop="recordType">
        <el-select v-model="dataForm.recordType" placeholder="单据类型" clearable>
            <el-option
              v-for="item in recordTypeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="业务原因：" prop="reason">
        <el-select v-model="dataForm.reason" placeholder="业务原因" clearable>
            <el-option
              v-for="item in reasonList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单据状态：" prop="recordStatus">
        <el-select v-model="dataForm.recordStatus"  placeholder="请选择" clearable >
                <el-option
                  v-for="item in recordStatusList"
                  :key="item.status"
                  :label="item.desc"
                  :value="item.status">
                </el-option>
           </el-select>
      </el-form-item>
      <br/>
      <el-form-item>
        <el-button @click="getDataList1()"  type="primary">查询</el-button>
        <el-button  @click="resetForm('dataForm')">重置</el-button>
      </el-form-item>
    </el-form>
    
    <el-table :data="dataList" border v-loading="dataListLoading" element-loading-text="拼命加载中..." style="width: 100%;">
    <el-table-column type = "index" header-align="center" align="center" label="序号">
         <template scope="scope">
            <span>{{getIndex(scope.$index)}}</span>
        </template>
    </el-table-column>
    <el-table-column prop="recordCode" header-align="center" align="center" label="试吃单号"/>
     <el-table-column prop="recordStatus" header-align="center" align="center" label="试吃单状态" width="180" :formatter="formatStatus"/>
    <el-table-column prop="sapRecordCode" header-align="center" align="center" label="sap过账单号"/>
    <el-table-column prop="outRecordCode" header-align="center" align="center" label="cmp单号"/>
    <el-table-column prop="shopCode" header-align="center" align="center" label="门店编号"/>
    <el-table-column prop="realWarehouseName" header-align="center" align="center" label="试吃门店"/>
    <el-table-column prop="recordTypeDesc" header-align="center" align="center" label="单类型"/>
    <el-table-column prop="organizationName" header-align="center" align="center" label="组织归属"/>
    <el-table-column prop="reason" header-align="center" align="center" label="业务原因" :formatter="formatOrderReason"/>
    <!-- 目前试吃单是没有状态的，全部默认显示为已完成状态-->
   
    <el-table-column prop="createTime" header-align="center" align="center" label="创建时间" width="180"/>
    <el-table-column fixed="right"  prop="操作" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text"  size="small" @click="detailsHandle(scope.row.id)">查看详情</el-button>
        </template>
    </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="dataForm.pageIndex"
      :page-sizes="[10, 20, 50]" :page-size="dataForm.pageSize" :total="dataForm.totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    
    <add-or-update v-if="detailsVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
    `
    });
});
