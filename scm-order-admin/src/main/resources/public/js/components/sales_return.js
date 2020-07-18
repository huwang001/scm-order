lyfdefine(['sales_return_detail','validate'], function (addOrUpdate,validate) {
    return ({
        data: function data() {
            return {
                dataForm: {
                    pageIndex: 1,
                    totalPage: 0,
                    channelCodes: null,
                    pageSize:10
                },
                channelList: null,
                channelListString: '',
                menuKey: 1,
                dataList: [],
                dataListLoading: false,
                recordStatusList: [{
                    "status": 12,
                    "desc": "已入库"
                },
                 {
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
        created: function(){
            this.init();
            this.getDataList();
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
                this.dataListLoading = true;
                if(this.dataForm.createTimeStr instanceof Array && this.dataForm.createTimeStr.length == 2){
                    this.dataForm.startCrateTime = this.dataForm.createTimeStr[0];
                    this.dataForm.endCreateTime = this.dataForm.createTimeStr[1];
                }else{
                    this.dataForm.startCrateTime = null;
                    this.dataForm.endCreateTime = null;
                }
                this.$http.post('/scm-order-admin/order/v1/salesReturn/queryCondition', this.dataForm
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

                });
            },

            init: function init(){
            },

            seeDetail: function seeDetail(id,outRecordCode) {

                this.$refs.addOrUpdate.init(id,outRecordCode);
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

            resetForm: function (dataForm) {
                var _this = this;
                Object.keys(_this.dataForm).forEach(function (key) {
                    if (key !== 'totalPage' && key !== 'pageIndex' && key !== "pageSize") {
                        _this.dataForm[key] = null;
                    }

                });
                _this.dataForm.createTimeStr = [];
                _this.dataForm.channelCodes = '';
                _this.channelListString =[];
                _this.channelList = [];
                _this.menuTree();
            },

            formatOrderCode:function formatOrderCode(row, column, value){
                if(value && value.length>1){
                    return value.join(",");
                }
                return value;
            },

            getIndex: function getIndex(index) {

                return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
            },
            chosetype(event){
                this.channelList = event
                console.log(this.channelList)
                var namelist = [],typelist=[];

                for(let i=0;i<this.channelList.length;i++){
                    namelist.push(this.channelList[i].channelName)
                    typelist.push(this.channelList[i].channelCode)
                }
                this.dataForm.channelCodes = typelist.join(',')
                this.channelListString = namelist.join(',')
                console.log(this.dataForm.channelCodes)
            }
        },
        template: `
 <div class="mod-config">
   <el-form :inline="true" :model="dataForm" ref="dataForm" :key="menuKey" > <!--@keyup.enter.native="getDataList()">-->
      <el-form-item label="退货单号：">
        <el-input v-model="dataForm.recordCode" placeholder="退货单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="订单编号：">
        <el-input v-model="dataForm.outRecordCode" placeholder="订单编号" clearable></el-input>
      </el-form-item>

        <el-form-item label="渠道：" prop="channelCodes">
           <el-input v-model="dataForm.channelCodes" placeholder="选择渠道" disabled style="width:auto;"></el-input>
           <basedata-el-channel style="float: right" @change="chosetype" clear="true"></basedata-el-channel>
        </el-form-item>

       <el-form-item label="入库仓编码：" prop="realWarehouseCode">
           <el-input v-model="dataForm.realWarehouseCode" placeholder="入库仓编码" clearable ></el-input>
        </el-form-item>
      <br/>
      <el-form-item label="入库单状态：">
        <el-select v-model="dataForm.recordStatus"  placeholder="请选择" clearable >
                <el-option
                  v-for="item in recordStatusList"
                  :key="item.status"
                  :label="item.desc"
                  :value="item.status">
                </el-option>
           </el-select>
      </el-form-item>
      
       <el-form-item label="创建时间：" prop="">
            <el-date-picker
              value-format='yyyy-MM-dd HH:mm:ss'
              :default-time="['00:00:00', '23:59:59']"
             v-model="dataForm.createTimeStr"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right">
            </el-date-picker>
         </el-form-item>
      
      <el-form-item label="退货原因：">
      <el-input v-model="dataForm.reason" placeholder="退货原因" clearable></el-input>
      </el-form-item>
      <br/>
      <el-form-item>
        <el-button @click="getDataList()"  type="primary">查询</el-button>
        <el-button  @click="resetForm('dataForm')">重置</el-button>
      </el-form-item>
    </el-form>
    
    <el-table :data="dataList" border v-loading="dataListLoading" element-loading-text="拼命加载中..." style="width: 100%;">
    <el-table-column type = "index" header-align="center" align="center" label="序号">
         <template scope="scope">
            <span>{{getIndex(scope.$index)}}</span>
        </template>
    </el-table-column>
    <el-table-column prop="recordCode" header-align="center" align="center" label="退货单号"/>
     <el-table-column prop="recordStatusName" header-align="center" align="center" label="单据状态" />
    <el-table-column prop="outRecordCodeList" header-align="center" align="center" label="订单编号"  :formatter="formatOrderCode"/>
    <el-table-column prop="userCode" header-align="center" align="center" label="买家账户"/>
   
    <el-table-column prop="realWarehouseName" header-align="center" align="center" label="接收方"/>
    <el-table-column prop="channelCode" header-align="center" align="center" label="渠道"/>
    <el-table-column prop="createTime" header-align="center" align="center" label="创建时间" width="180"/>
    <!--<el-table-column prop="receiverTime" header-align="center" align="center" label="到仓时间"/>-->
    <el-table-column fixed="right"  prop="操作" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text"  size="small" @click="seeDetail(scope.row.id,scope.row.outRecordCodeList)">查看详情</el-button>
        </template>
    </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="dataForm.pageIndex"
      :page-sizes="[10, 20, 50]" :page-size="dataForm.pageSize" :total="dataForm.totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
     <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="true" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
    `
    });
});
