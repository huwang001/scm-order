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
                invoiceInfo: {
                    fplx: '增值税普通发票',
                    fpnr: '商品明细',
                    kpxs: '电子发票',
                    fptt: '上海来伊份信息科技有限公司',
                    nsrsbh: '31011500259999937',
                    zcdz: '上海市闵行区青年大厦100楼',
                    zcdh: '0216199999',
                    khyh: '招商银行上海分行张江支行',
                    yhzh: '6299889999747893'
                }
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
 <el-dialog title="发票明细" data-scm-order-common class="scm-order-common-dialog scm-order-fapiao-detail" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="430px">
    <div class="info">
        <div class="item">
            <span class="title">发票类型:</span>
            <span class="name">{{invoiceInfo.fplx}}</span>
        </div>
        <div class="item">
            <span class="title">发票内容:</span>
            <span class="name">{{invoiceInfo.fpnr}}</span>
        </div>
        <div class="item">
            <span class="title">开票形式:</span>
            <span class="name">{{invoiceInfo.kpxs}}</span>
        </div>
        <div class="item">
            <span class="title">发票抬头:</span>
            <span class="name">{{invoiceInfo.fptt}}</span>
        </div>
        <div class="item">
            <span class="title">纳税人识别码:</span>
            <span class="name">{{invoiceInfo.nsrsbh}}</span>
        </div>
        <div class="item">
            <span class="title">注册地址:</span>
            <span class="name">{{invoiceInfo.zcdz}}</span>
        </div>
        <div class="item">
            <span class="title">注册电话:</span>
            <span class="name">{{invoiceInfo.zcdh}}</span>
        </div>
        <div class="item">
            <span class="title">开户银行:</span>
            <span class="name">{{invoiceInfo.khyh}}</span>
        </div>
        <div class="item">
            <span class="title">银行账户:</span>
            <span class="name">{{invoiceInfo.yhzh}}</span>
        </div>
    </div>
    <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
