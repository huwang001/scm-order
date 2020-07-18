lyfdefine(['sale_warehouseRecord_detail', 'validate'], function (addOrUpdate, validate) {
    //virtual_warehouse_addupdate 为以后的查看详情留坑位
    return ({
        data: function data() {
            return {
                dataForm: {
                    recordCode: null,
                    orderCode: null,
                    userCode: null,
                    skuCode: null,
                    // _skuCode:null,
                    channelCodes: null,
                    recordStatus: null,
                    realWarehouseId: null,
                    realWarehouseCode: null,

                    startTime: null,
                    endTime: null,
                    recordType: 1,
                    pageSize: 10,
                    pageIndex: 1
                },
                channelList: null,
                channelListString: '',
                dateTimeRange: [new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000), new Date()],
                channelTypeList: [],
                // realWarehouseList: [],
                menuKey: 1,
                // recordStatusList: [],
                dataList: [],
                recordTypeList: [
                    {type: 1, name: '门店零售出库单'},
                    {type: 99, name: '电商零售出库单'}
                ],
                dataListLoading: false,
                addOrUpdateVisible: true,
                rule: {
                    // _skuId: [
                    //     {validator: isInteger, trigger: 'blur'}
                    // ],
                },
                recordStatusList: [{
                    "status": 11,
                    "desc": "已出库"
                }, {
                    "status": 2,
                    "desc": "已取消"
                }],
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

        components: {
            'addOrUpdate': addOrUpdate
        },

        methods: {
            menuTree: function () {
                ++this.menuKey
            },
            // 获取数据列表
            getDataList: function getDataList() {
                var _this = this;
                // if (_this.dataForm._skuCode && !Number(_this.dataForm._skuCode))
                //     _this.dataForm.skuCode = -1;
                // else _this.dataForm.skuCode = _this.dataForm._skuCode;
                if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                    //构建时间查询参数
                    _this.dataForm.startTime = _this.dateTimeRange[0];
                    _this.dataForm.endTime = _this.dateTimeRange[1];
                } else {
                    _this.dataForm.startTime = null;
                    _this.dataForm.endTime = null;
                }
                _this.dataListLoading = true;
                _this.$http.post('/scm-order-admin/order/v1/warehouse_sale/list',
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
            seeDetail: function seeDetail(id, outRecordCode) {

                this.addOrUpdateVisible = true;
                this.$refs.addOrUpdate.init(id, outRecordCode);
            },

            getIndex: function getIndex(index) {

                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },

            resetForm: function (form) {
                var _this = this;
                _this.menuTree();
                Object.keys(_this.dataForm).forEach(function (key) {
                    if (key !== 'totalPage' && key !== 'pageIndex' && key !== "pageSize" && key !== "recordType") {
                        _this.dataForm[key] = null;
                    }
                });
                _this.dataForm.channelCodes = '';
                _this.channelListString = [];
                _this.dateTimeRange = [];
                _this.channelList = [];
                var start = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
                var end = new Date();
                _this.dateTimeRange.push(start);
                _this.dateTimeRange.push(end);

            },
            formatOrderCode: function formatOrderCode(row, column, value) {
                if (value && value.length > 1) {
                    return value.join(",");
                }
                return value;
            },
            chosetype: function (event) {
                this.channelList = event
                var namelist = [], typelist = [];
                for (let i = 0; i < this.channelList.length; i++) {
                    namelist.push(this.channelList[i].channelName)
                    typelist.push(this.channelList[i].channelCode)
                }
                this.dataForm.channelCodes = typelist.join(',')
                this.channelListString = namelist.join(',')
            },
            // 提交查询
            submitQueryForms: function submitForms() {
                this.dataForm.pageIndex = 1;
                this.getDataList();
            }
        },
        created() {
            this.init();
            //this.getDataList();
        },
        template: /*html*/`
 <div class="mod-config">
   <el-form :inline="true" :model="dataForm" ref="dataForm"   :rules="rule" :key="menuKey" > 
      <el-form-item label="发货单号：" prop="recordCode">
        <el-input v-model="dataForm.recordCode" placeholder="发货单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="订单编号：" prop="orderCode">
        <el-input v-model="dataForm.orderCode" placeholder="订单编号" clearable></el-input>
      </el-form-item>
      <el-form-item label="买家账户：" prop="userCode">
        <el-input v-model="dataForm.userCode" placeholder="买家账户" clearable></el-input>
      </el-form-item>
      <el-form-item label="订单包含商品：" prop="skuCode">
        <el-input v-model="dataForm.skuCode" placeholder="订单包含商品CODE" clearable></el-input>
      </el-form-item>
      
      <el-form-item label="渠道：" prop="channelCodes">
           <el-input v-model="dataForm.channelCodes" placeholder="选择渠道名称" disabled style="width:auto;"></el-input>
           <basedata-el-channel style="float: right" @change="chosetype" clear="true"></basedata-el-channel>
        </el-form-item>

        <el-form-item label="出库仓编码：" prop="realWarehouseCode">
           <el-input v-model="dataForm.realWarehouseCode" placeholder="出库仓编码" clearable ></el-input>
        </el-form-item>
        
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
        
        <el-form-item label="出库单状态：" prop="recordStatus">
         <el-select v-model="dataForm.recordStatus"  placeholder="请选择" clearable >
                <el-option
                  v-for="item in recordStatusList"
                  :key="item.status"
                  :label="item.desc"
                  :value="item.status">
                </el-option>
           </el-select>
        </el-form-item>
      <el-form-item>
        <el-button @click="submitQueryForms()"  type="primary">查询</el-button>
        <el-button  @click="resetForm('dataForm')">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      element-loading-text="拼命加载中..."
      style="width: 100%;">
     
     <el-table-column
        label="序号"
        type="index"
        width="70"
        align="center">
        <template scope="scope">
            <span>{{getIndex(scope.$index)}}</span>
        </template>
    </el-table-column>
      <el-table-column
        prop="recordCode"
        header-align="center"
        align="center"
        label="发货单号">
      </el-table-column>
      <el-table-column
        prop="recordStatusName"
        header-align="center"
        align="center"
        label="订单状态">
      </el-table-column>
      <el-table-column
        prop="outRecordCode"
        header-align="center"
        :formatter="formatOrderCode"
        align="center"
        label="订单编号">
      </el-table-column>
      <el-table-column
        prop="userCode"
        header-align="center"
        align="center"
        label="买家账户">
      </el-table-column>
       <el-table-column
        prop="recordTypeName"
        header-align="center"
        align="center"
        label="订单类型">
      </el-table-column>
       <el-table-column
        prop="channelCodeName"
        header-align="center"
        align="center"
        label="渠道">
      </el-table-column>
       <el-table-column
        prop="realWarehouseName"
        header-align="center"
        align="center"
        label="发货仓">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        fixed="right"  prop="操作"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text"  size="small" @click="seeDetail(scope.row.id,scope.row.outRecordCode)">查看详情</el-button>
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
   
  </div>
`
    });
});
