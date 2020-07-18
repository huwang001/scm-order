lyfdefine([],function(){
    return ({
        props: {
            originRecordCode: {
                type: [String, Number],
                default: ''
            },
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
                loading: false,
                tableData: [],
                activeRadio: '',
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                }
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
                this.tableData = []
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
            fetchTableData() {
                // const data = {
                //     originRecordCode: this.originRecordCode,
                //     pageSize: this.paginationOption.pageSize,
                //     pageNum: this.paginationOption.page,
                // }
                this.loading = true
                this.$http({
                    get: 'get',
                    url: this.POOL_NAME + '/order/v1/reverse/queryReceiverRecordByRecordCode/' + this.originRecordCode,
                }).then(res => {
                    this.loading = false
                    if (res.data.code === '0' && res.data.data && Array.isArray(res.data.data.list)) {
                        this.tableData = res.data.data.list;
                        this.paginationOption.total = res.data.data.total;
                    } else {
                        this.tableData = [];
                        this.paginationOption.total = 0;
                        this.$message({
                            type: 'error',
                            message: res.data.msg,
                            showClose: true
                        })
                    }
                }, () => {
                    this.loading = false
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            }
        },
        watch: {
            visible(n) {
                if (n) {
                    this.loading = false
                    this.onClickSearchBtn()
                }
            }
        },
        template: `
<el-dialog title="选择单号" append-to-body :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1100px">
    <el-form size="mini" inline label-width="90px">
        <el-form-item>
            <el-button size="mini" type="primary" @click="onClickConfirm" :disabled="activeRadio=== ''">确定</el-button>
        </el-form-item>
    </el-form>
    <div v-loading="loading" element-loading-text="正在加载中" element-loading-spinner="el-icon-loading">
        <el-table border class="el-table__select--nolabel" height="300px" :data="tableData" style="width: 100%">
            <el-table-column header-align="center" width="50" align="center" label="选择">
                <template slot-scope="scope">
                    <input type="radio" :value="scope.row.recordCode" v-model="activeRadio" name="order" />
                </template>
            </el-table-column>
            <el-table-column header-align="center" width="50" align="center" type="index" label="序号">
            </el-table-column>
            <el-table-column prop="recordType" align="center" label="单据类型" min-width="70">
            </el-table-column>
            <el-table-column prop="realWarehouseName" align="center" label="入库仓库" min-width="100"></el-table-column>
            <el-table-column prop="outRecordCode" align="center" label="外部单号" min-width="100"></el-table-column>
            <el-table-column prop="receiverDate" align="center" label="收货时间" min-width="60"></el-table-column>
        </el-table>
        <el-pagination style="padding:10px 0; textAlign:right;" @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper"></el-pagination>
    </div>
</el-dialog>
        `
    });
});
