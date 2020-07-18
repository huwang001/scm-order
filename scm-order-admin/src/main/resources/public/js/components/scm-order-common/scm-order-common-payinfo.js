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
                payInfo: {
                    yfje: 233.00,
                    yfje: 0.00,
                    fklx: '网上支付',
                    zfzt: '已支付',
                    detail: [{
                        type: '支付宝-网页支付',
                        sum: 0.1,
                        payTime: '2018-12-19 14:37:59',
                        payWarterCode: '12192076087100000050'
                    },
                        {
                            type: '微信',
                            sum: 0.1,
                            payTime: '2018-12-19 14:37:59',
                            payWarterCode: '12192076087100000051'
                        }
                    ]
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
 <el-dialog data-scm-order-common class="scm-order-common-dialog scm-order-pay-detail" title="支付信息" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="630px">
    <el-card class="pay-card pay-card-result" shadow="never">
        <div slot="header" class="pay-card__header">
            <span>支付结果</span>
        </div>
        <div class="pay-card-result__body">
            <div class="item">
                <span class="title">应付金额:</span>
                <span class="name">¥{{payInfo.yfje}}</span>
            </div>
            <div class="item">
                <span class="title">已付金额:</span>
                <span class="name">¥{{payInfo.yfje}}</span>
            </div>
            <div class="item">
                <span class="title">付款类型:</span>
                <span class="name">{{payInfo.fklx}}</span>
            </div>
            <div class="item">
                <span class="title">支付状态:</span>
                <span class="name">{{payInfo.zfzt}}</span>
            </div>
        </div>
    </el-card>
    <el-card class="pay-card pay-card-detail" shadow="never">
        <div slot="header" class="pay-card__header">
            <span>支付明细</span>
        </div>
        <div class="pay-card-detail__body">
            <el-table :data="payInfo.detail" style="width: 100%" border>
                <el-table-column prop="type" label="支付方式" width="100">
                </el-table-column>
                <el-table-column prop="sum" label="支付金额" min-width="80">
                    <template slot-scope="scope">¥{{scope.row.sum}}</template>
                </el-table-column>
                <el-table-column prop="payTime" label="支付时间" min-width="80">
                </el-table-column>
                <el-table-column prop="payWarterCode" label="支付流水号" min-width="80">
                </el-table-column>
            </el-table>
        </div>
    </el-card>
    <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
