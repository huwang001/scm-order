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
                packages: [{
                    name: '包裹1',
                    baseInfo: {
                        wlgs: '申通快递',
                        wldh: 'XX111111111111',
                        lxdh: '17602187629',
                        fhck: '昆山物流中心',
                        fhsj: '2020.03.31   18:00:00',
                        zl: '1.00kg'
                    },
                    moveDetail: [{
                        date: '2020-04-29',
                        nodes: [{
                            time: '21:00:00',
                            des: '快件离开【上海徐汇田林营业部】,正发往 【上海虹桥集散中心2】'
                        },
                            {
                                time: '21:52:00',
                                des: '快件离开【上海虹桥集散中心2】,正发往下一站'
                            }
                        ]
                    },
                        {
                            date: '2020-04-28',
                            nodes: [{
                                time: '01:40:00',
                                des: '快件离开【苏州昆山集散中心】,正发往下一站'
                            },
                                {
                                    time: '04:56:00',
                                    des: '快件离开【苏州吴中集散中心】,正发往 【苏州工业园苏利达服饰营业点】'
                                },
                                {
                                    time: '09:32:00',
                                    des: '快件到达顺丰合作点【苏州大学独墅湖校区一期炳麟图书馆咖啡吧'
                                }
                            ]
                        },
                        {
                            date: '2020-04-27',
                            nodes: [{
                                time: '15:00:00',
                                des: '正在派送途中,请您准备签收(派件人:摇摇,电话:1896999940)'
                            },
                                {
                                    time: '19:05:00',
                                    des: '已签收,感谢使用顺丰,期待再次为您服务'
                                }
                            ]
                        }
                    ],
                    products: [{
                        name: '小核桃仁',
                        code: '0284234898',
                        barcode: '0284234898',
                        specs: '10包/袋',
                        num: 15
                    },
                        {
                            name: '牛肉味花生',
                            code: '0284234898',
                            barcode: '0284234898',
                            specs: '10包/袋',
                            num: 15
                        }
                    ]
                },
                    {
                        name: '包裹2',
                        baseInfo: {
                            wlgs: 'x通快递',
                            wldh: 'XX111111111111',
                            lxdh: '17602187629',
                            fhck: '昆山物流中心',
                            fhsj: '2020.03.31   18:00:00',
                            zl: '1.00kg'
                        },
                        moveDetail: [{
                            date: '2020-04-29',
                            nodes: [{
                                time: '21:00:00',
                                des: '快件离开【上海徐汇田林营业部】,正发往 【上海虹桥集散中心2】'
                            },
                                {
                                    time: '21:52:00',
                                    des: '快件离开【上海虹桥集散中心2】,正发往下一站'
                                }
                            ]
                        },
                            {
                                date: '2020-04-28',
                                nodes: [{
                                    time: '01:40:00',
                                    des: '快件离开【苏州昆山集散中心】,正发往下一站'
                                },
                                    {
                                        time: '04:56:00',
                                        des: '快件离开【苏州吴中集散中心】,正发往 【苏州工业园苏利达服饰营业点】'
                                    },
                                    {
                                        time: '09:32:00',
                                        des: '快件到达顺丰合作点【苏州大学独墅湖校区一期炳麟图书馆咖啡吧'
                                    }
                                ]
                            },
                            {
                                date: '2020-04-27',
                                nodes: [{
                                    time: '15:00:00',
                                    des: '正在派送途中,请您准备签收(派件人:摇摇,电话:1896999940)'
                                },
                                    {
                                        time: '19:05:00',
                                        des: '已签收,感谢使用顺丰,期待再次为您服务'
                                    }
                                ]
                            }
                        ],
                        products: [{
                            name: '小核桃仁',
                            code: '0284234898',
                            barcode: '0284234898',
                            specs: '10包/袋',
                            num: 15
                        },
                            {
                                name: '牛肉味花生',
                                code: '0284234898',
                                barcode: '0284234898',
                                specs: '10包/袋',
                                num: 15
                            }
                        ]
                    },
                ]
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
 <el-dialog data-scm-order-common class="scm-order-common-dialog scm-order-package-detail" title="订单包裹明细" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="700px">
    <el-tabs type="border-card">
        <el-tab-pane :label="item.name" :key="item.name" v-for="item in packages">
            <el-card class="package-card package-card-base" shadow="never">
                <div slot="header" class="package-card__header">
                    <span>包裹信息</span>
                </div>
                <div class="package-card-base__body">
                    <div class="item">
                        <span class="title">物流公司:</span>
                        <span class="name">{{item.baseInfo.wlgs}}</span>
                    </div>
                    <div class="item">
                        <span class="title">物流单号:</span>
                        <span class="name">{{item.baseInfo.wldh}}</span>
                    </div>
                    <div class="item">
                        <span class="title">联系电话:</span>
                        <span class="name">{{item.baseInfo.lxdh}}</span>
                    </div>
                    <div class="item">
                        <span class="title">发货仓库:</span>
                        <span class="name">{{item.baseInfo.fhck}}</span>
                    </div>
                    <div class="item">
                        <span class="title">发货时间:</span>
                        <span class="name">{{item.baseInfo.fhsj}}</span>
                    </div>
                    <div class="item">
                        <span class="title">重量:</span>
                        <span class="name">{{item.baseInfo.zl}}</span>
                    </div>
                </div>
            </el-card>
            <el-card class="package-card package-card-move" shadow="never">
                <div slot="header" class="package-card__header">
                    <span>包裹物流详情</span>
                </div>
                <div class="package-card-move__body">
                    <el-row class="item" :key="move.date" v-for="move in item.moveDetail">
                        <el-col :span="3">{{move.date}}</el-col>
                        <el-col :span="21">
                            <div v-for="node in move.nodes" :key="node.time" class="node">
                                <span class="time">
                                    {{node.time}}
                                </span>
                                <span class="des">
                                    {{node.des}}
                                </span>
                            </div>
                        </el-col>
                    </el-row>
                </div>
            </el-card>
            <el-card class="package-card package-card-product" shadow="never">
                <div slot="header" class="package-card__header">
                    <span>包裹商品明细</span>
                </div>
                <div class="package-card-product__body">
                    <el-table :data="item.products" style="width: 100%" border>
                        <el-table-column prop="name" label="商品名称" width="100">
                        </el-table-column>
                        <el-table-column prop="code" label="商品编码" min-width="80">
                        </el-table-column>
                        <el-table-column prop="barcode" label="条形码" min-width="80">
                        </el-table-column>
                        <el-table-column prop="specs" label="规格属性" min-width="80">
                        </el-table-column>
                        <el-table-column prop="num" label="发货数量" min-width="80">
                        </el-table-column>
                    </el-table>
                </div>
            </el-card>
        </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
