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
                POOL_NAME: "/scm-order-admin",
                timer: null,
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
 <el-dialog title="协助售后" data-scm-order-common class="scm-order-common-dialog scm-order-common-service" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="300px">
    <div class="service-common__body">
        <p class="service-item">
            <span class="service-item__title">已补偿金额</span>&nbsp;
            <span class="service-item__value">¥0.00</span>
        </p>
        <el-row :gutter="20">
            <el-col :span="12"><el-button style="width:100%;">发货单</el-button></el-col>
            <el-col :span="12"><el-button style="width:100%;">补偿单</el-button></el-col>
        </el-row>
    </div>
    <div slot="footer" class="service-common__footer">
        <el-button size="mini" type="primary">确定</el-button>
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
