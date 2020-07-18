lyfdefine(['shop_allocation_record_detail'],function(addOrUpdate){
    return ({
        data: function () {
            return {
                dataList: [],
                dataForm: {
                    recordCode:'',
                    outShopCode:'',
                    inShopCode:'',
                    startDate:'',
                    endDate:'',
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
                dataListLoading:false,
                dateTimeRange: [],
                businessType: [{
                    value: '1',
                    label: '直营-直营(同组织)'
                }, {
                    value: '2',
                    label: '加盟-加盟(同组织)'
                }],
                recordStatuses: {},
                detailsVisible:true
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
                this.$http.post('/scm-order-admin/order/v1/allocation/list',
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
            //获取状态列表
            getRecordStatuses: function getRecordStatuses(){
                let _this = this;
                this.$http.post('/scm-order-admin/order/show/queryFrontRecordStatus/list'
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.recordStatuses =res.data.data;
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
            detailsHandle(frontRecord){
                this.$refs.addOrUpdate.init(frontRecord);
            },
            onSubmit() {
                this.getDataList();
            },
            resetForm: function resetForm() {
                this.dataForm.recordCode = '';
                this.dataForm.outShopCode = '';
                this.dataForm.inShopCode = '';
                this.dateTimeRange = new Array();
            }
        },
        components: {
            'addOrUpdate': addOrUpdate
        },
        mounted() {
            this.getDataList();
            this.getRecordStatuses();
        },
        template: `
            <el-container>
                <el-main>
                    <el-row type="flex" justify="start" style="line-height:30px;height: 30px;font-size: 14px">
                        <el-col :span="2"><div style="font-weight: bold">门店调拨</div></el-col>
                    </el-row>
                    <el-form :inline="true" :model="dataForm" size="small" class="demo-form-inline">
                        <el-form-item label="调拨单号">
                            <el-input v-model="dataForm.recordCode" placeholder="请输入单号"></el-input>
                        </el-form-item>
                        <el-form-item label="调出门店">
                            <el-input v-model="dataForm.outShopCode" placeholder="请输入门店编号"></el-input>
                        </el-form-item>
                        <el-form-item label="调入门店">
                            <el-input v-model="dataForm.inShopCode" placeholder="请输入门店编号"></el-input>
                        </el-form-item>
                        <el-form-item label="调拨类型：">
                            <el-select v-model="dataForm.businessType" placeholder="订单类型" clearable>
                                <el-option v-for="item in businessType" :key="item.value" :label="item.label" :value="item.value"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="调拨时间">
                            <el-date-picker value-format='yyyy-MM-dd HH:mm:ss' :default-time="['00:00:00', '23:59:59']" v-model="dateTimeRange"
                                type="datetimerange" :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期"
                                end-placeholder="结束日期" align="right">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">查询</el-button>
                            <el-button  @click="resetForm()">重置</el-button>
                        </el-form-item>
                    </el-form>               
                    <el-table max-height="550" size="small" border :data="dataList" v-loading="dataListLoading" element-loading-text="拼命加载中...">
                        <el-table-column prop="recordCode" label="调拨单号" width="140">
                        </el-table-column>
                          <el-table-column prop="recordStatuses[recordStatus]" label="调拨单状态" min-width="150">
                            <template slot-scope="scope">
                                <span>{{recordStatuses[scope.row.recordStatus]}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="outRecordCode" label="关联单号" width="140">
                        </el-table-column>
                        <el-table-column prop="outShopCode" label="调出门店编号" width="140">
                        </el-table-column>
                        <el-table-column prop="outShopName" label="调出门店名称" width="140">
                        </el-table-column>
                        <el-table-column prop="inShopCode" label="调入门店编号" width="140">
                        </el-table-column>
                        <el-table-column prop="inShopName" label="调入门店名称" width="140">
                        </el-table-column>
                        <el-table-column prop="businessType" label="调拨类型" min-width="150">
                            <template slot-scope="scope">
                                <span v-if="scope.row.businessType==1">直营-直营(同组织)</span>
                                <span v-if="scope.row.businessType==2">加盟-加盟(同组织)</span>
                                <span v-if="scope.row.businessType==3">直营-直营(跨组织)</span>
                                <span v-if="scope.row.businessType==4">加盟-直营</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="outCreateTime" label="调拨日期" min-width="150">
                        </el-table-column>
                      
                        <el-table-column prop="createTime" label="创建日期" min-width="150">
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
