lyfdefine([], function () {
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
                POOL_NAME: "/scm-transport-admin",
                timer: null,
                tableData: [],
            };
        },
        methods: {
            init() {},
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                this.$emit("dialog-close");
            },
        },
        watch: {
            visible(n) {
                if (n) {
                    this.init()
                }
            }
        },
        template: /*html*/`
 <el-dialog title="订单日志" data-scm-order-common class="scm-order-common-dialog scm-order-log" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="630px">
    <el-table :data="tableData" border height="300px" size="mini" ref="table" style="width: 100%;">
        <el-table-column prop="skuName" min-width="85" header-align="center" align="center" label="操作人">
        </el-table-column>
        <el-table-column prop="skuCode" header-align="center" align="center" label="操作类型" min-width="70">
        </el-table-column>
        <el-table-column prop="payAmount" header-align="center" align="center" label="操作时间" min-width="60">
        </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
