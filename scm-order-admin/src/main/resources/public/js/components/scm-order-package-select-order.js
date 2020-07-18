lyfdefine([],function(){
    return ({
        props: {
            visible: {
                type: Boolean,
                default: false
            },
            callback: {
                type: Function,
                default: () => {}
            }
        },
        data() {
            return {
                POOL_NAME: "/scm-order-admin",
                timer: null,
                tableData: [],
                activeRadio: '',
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                },
                filterForm: {
                    channelName: '',
                    recordCode: '', // 需求单号
                    channelCode: '', // 销售渠道
                },
            };
        },
        methods: {
            onClickSearchBtn() {
                clearTimeout(this.timer);
                this.paginationOption.page = 1;
                this.timer = setTimeout(() => {
                    this.fetchTableData();
                }, 300)
            },
            onClickResetBtn() {
                this.activeRadio = ''
                this.filterForm.channelCode = ''
                this.$refs.filterForm.resetFields()
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            onClickConfirm() {
                const order = this.tableData.find(n => n.recordCode === this.activeRadio)
                this.$emit('select-order', order)
                this.handleBeforeClose()
            },
            handleBeforeClose() {
                this.onClickResetBtn()
                this.$emit("dialog-close");
            },
            handleSizeChange(pageSize) {
                this.paginationOption.pageSize = pageSize
                this.paginationOption.page = 1
                this.fetchTableData();
            },
            handleCurrentPageChange(page) {
                this.paginationOption.page = page
                this.fetchTableData();
            },
            handleChangeCheckbox() {
                if (this.selectReceiverCode.length > 1) {
                    this.selectReceiverCode.splice(0, 1)
                }
            },
            handleChangeChannel(channel) {
                if(channel.length > 0){
                    this.filterForm.channelName = channel[0].channelName
                    this.filterForm.channelCode = channel[0].channelCode
                }
            },
            fetchTableData() {
                const data = {
                    packType: 3,
                    channelCodes: this.filterForm.channelCode ? [this.filterForm.channelCode ] : [],
                    recordCodes: this.filterForm.recordCode ? [this.filterForm.recordCode ] : [],
                    pageSize: this.paginationOption.pageSize,
                    pageNum: this.paginationOption.page,
                }
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/demand/queryPackDemandList',
                    data,
                }).then(res => {
                    if (res.data.code === '0' && res.data.data && Array.isArray(res.data.data.list)) {
                        this.tableData = res.data.data.list;
                        this.paginationOption.total = res.data.data.total;
                    } else {
                        this.tableData = [];
                        this.paginationOption.total = 0;
                    }
                }, () => {
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            }
        },
        watch: {
            visible(n) {
                if (n) {
                    this.onClickSearchBtn()
                }
            }
        },
        template: `
<el-dialog title="选择需求单号" append-to-body :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1100px">
    <el-form size="mini" :model="filterForm" inline ref="filterForm" label-width="90px">
        <el-form-item label="需求单号" prop="recordCode">
            <el-input clearable v-model="filterForm.recordCode" style="width:150px" />
        </el-form-item>
        <el-form-item label="渠道" prop="channelName" label-width="65px">
            <el-row :gutter="5">
                <el-col :span="11">
                    <el-input style="width:100%;"  v-model="filterForm.channelName" disabled placeholder="请选择渠道" />
                </el-col>
                <el-col :span="11">
                    <basedata-el-channel style="width:100%;" @change="handleChangeChannel" :clear="true"></basedata-el-channel>
                </el-col>
            </el-row>
        </el-form-item>
        <el-form-item>
            <el-button size="mini" type="primary" @click="onClickSearchBtn">查询</el-button>
            <el-button size="mini" type="primary" @click="onClickResetBtn">重置</el-button>
            <el-button size="mini" type="primary" @click="onClickConfirm" :disabled="activeRadio=== ''">确定</el-button>
        </el-form-item>
    </el-form>
    <el-table border class="el-table__select--nolabel" height="300px" :data="tableData" style="width: 100%">
        <el-table-column header-align="center" width="50" align="center" label="选择">
            <template slot-scope="scope">
                <input type="radio" :value="scope.row.recordCode" v-model="activeRadio" name="order"/>
            </template>
        </el-table-column>
        <el-table-column header-align="center" width="50" align="center" type="index" label="序号">
        </el-table-column>
        <el-table-column prop="recordCode" align="center" label="需求单号" min-width="70">
        </el-table-column>
        <el-table-column prop="introducer" align="center" label="客户" min-width="100"></el-table-column>
        <el-table-column prop="channelCode" align="center" label="渠道CODE" min-width="100"></el-table-column>
        <el-table-column prop="channelName" align="center" label="渠道名称" min-width="60"></el-table-column>
    </el-table>
    <el-pagination style="padding:10px 0; textAlign:right;" @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper"></el-pagination>
</el-dialog>
        `
    });
});
