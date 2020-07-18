lyfdefine(['scm-order-common/scm-order-common-edit-address',
    'scm-order-common/scm-order-common-log',
    'scm-order-common/scm-order-common-package',
    'scm-order-common/scm-order-common-payinfo',
    'scm-order-common/scm-order-common-fapiao',
    'scm-order-common/scm-order-common-tag',
    'scm-order-common/scm-order-common-split',
    'scm-order-common/scm-order-common-service'
], function (EditAddressDialog,LogDialog,PackageDialog,PayDialog,InvoiceDialog,BoardTag,SplitDialog,ServiceDialog) {
    return ({
        name: 'scm-order-detail-ordinary',
        components: {
            EditAddressDialog,
            ServiceDialog,
            BoardTag,
            LogDialog,
            PackageDialog,
            PayDialog,
            InvoiceDialog,
            SplitDialog
        },
        data() {
            return {
                showEditAddressDialog:false,
                showLogDialog: false,
                showPackageDialog: false,
                showPayDialog: false,
                showInvoiceDialog: false,
                showSplitDialog: false,
                showServiceDialog: false,
                activePackage: 'first',
                activeStep: 1,
                productTableData: [{
                    f1: '小核桃',
                    f2: 1201210,
                    f3: '002',
                    f4: 1212192109218,
                    f5: 10,
                    f6: '10.00元',
                    f7: '100.00元',
                    f8: '20.00元',
                    f9: '80.00元',
                    f10: '充足'
                },
                    {
                        f1: '猪肉脯',
                        f2: 12012101,
                        f3: '003',
                        f4: 12121921092128,
                        f5: 100,
                        f6: '10.00元',
                        f7: '1000.00元',
                        f8: '100.00元',
                        f9: '900.00元',
                        f10: '充足'
                    }
                ],
                activities: [{
                    content: '你的订单在京东【苏州接货仓】发货完成，准备送往京东【上海分拨转运中心】',
                    timestamp: '2020-7-18/周日 20:46:09',
                }, {
                    content: '你的订单在京东【苏州接货仓】分拣完成',
                    timestamp: '2018-04-03 20:46',
                    type: 'primary',
                }, {
                    highlightContent: '仓库处理中',
                    content: '打包完成',
                    timestamp: '2018-04-03 20:46',
                    size: 'large',
                    type: 'primary',
                    icon: 'el-icon-s-home'
                }, {
                    content: '扫描员已经扫描',
                    timestamp: '2018-04-03 20:46',
                    type: 'primary',
                },
                    {
                        content: '您的订单已经捡货完成',
                        timestamp: '2018-04-12 20:46',
                        type: 'primary',
                    }, {
                        content: '你的等待确认',
                        timestamp: '2018-04-03 20:46',
                        type: 'primary',
                    }
                ],
                steps: [{
                    title: '新创建',
                    des: '2020-05-05  15:00:00'
                },
                    {
                        title: '订单审核',
                    },
                    {
                        title: '订单池等待',
                    },
                    {
                        title: '下发仓库',
                    },
                    {
                        title: '仓库处理',
                    },
                    {
                        title: '仓库已发货',
                    },
                    {
                        title: '已揽件',
                    },
                    {
                        title: '已签收',
                    },
                ]
            }
        },
        methods: {
            // 发货
            onClickShipBtn() {
                this.$confirm(`是否确定立即下发订单，下发后将进行订单发货作业。下发后无法驳回!`, '立即下发', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                })
                    .then(() => {
                        console.log(1)
                    })
                    .catch(() => {
                        // console.log('取消') for debug
                    });
            },
            // 日志
            onClickLogBtn(){
                this.showLogDialog = true
            },
            // 补发货
            onClickExtralShipBtn() {
                this.showShipDialog = true
            },
            // 拆单
            onClickSplitOrderBtn(){
                this.showSplitDialog = true
            },
            // 协助售后
            onClickServiceBtn(){
                this.showServiceDialog = true
            },
            // 取消订单
            onClickCancelOrderBtn() {
                this.$confirm(`是否确定取消发货？`, '取消发货', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                })
                    .then(() => {
                        console.log(1)
                    })
                    .catch(() => {
                        // console.log('取消') for debug
                    });
            },
            // 订单转换
            onClickConvertOrderBtn() {
                this.$confirm(`当前订单是：一件代发，<br/>是需要转换成 仓库发货？`, '转换订单', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    dangerouslyUseHTMLString: true,
                    type: 'warning',
                })
                    .then(() => {
                        console.log(1)
                    })
                    .catch(() => {
                        // console.log('取消') for debug
                    });
            },
            // 修改地址
            onClickEditAddressBtn(){
                this.showEditAddressDialog= true
            },
            // 包裹明细按钮
            onClickPackageDetailBtn() {
                this.showPackageDialog = true
            },
            // 支付明细
            onClickPayDetailBtn() {
                this.showPayDialog = true
            },
            // 发票明细
            onClickInvoiceDetailBtn() {
                this.showInvoiceDialog = true
            },
            // 复制
            onClickCopy(id) {
                const el = document.getElementById(id);
                el.select();
                document.execCommand('copy');
                this.$message({
                    type: 'success',
                    message: '复制成功',
                    showClose: true
                })
            }
        },
        template: /*html*/`
<div data-scm-order class="scm-order-detail scm-order-detail-ordinary">
    <el-card shadow="never" class="detail-card order-detail-step">
        <el-steps :active="activeStep" align-center finish-status="success">
            <el-step v-for="step in steps" :key="step.title" :title="step.title" :description="step.des"></el-step>
        </el-steps>
    </el-card>
    <el-card shadow="never" class="detail-card order-detail-operation">
        <el-row class="detail-card__body order-detail-operation__row">
            <el-col :span="6" class="detail-card__body__left border-right">
                <div class="item-1">
                    <div class="title">待发货</div>
                    <div class="log-btn">
                        <el-button type="text" @click="onClickLogBtn">日志</el-button>
                    </div>
                </div>
                <div class="item-2">
                    <el-button type="primary" class="detail-order-info__left__btn" @click="onClickShipBtn">发货</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn" @click="onClickConvertOrderBtn">订单转换</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn" @click="onClickCancelOrderBtn">取消订单</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn" @click="onClickSplitOrderBtn">拆单</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn" @click="onClickServiceBtn">协助售后</el-button>
                    <el-button type="primary" style="visibility:hidden;" class="detail-order-info__left__btn" @click="onClickCancelOrderBtn">协助售后</el-button>
                </div>
            </el-col>
            <el-col :span="6" class="detail-card__body__middle border-right">
                <div class="row row1">
                    <div class="item item-1">
                        <span class="item__title">黑金会员</span>
                        <span class="item__value">
                            <el-tag>vip</el-tag>
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item-1">
                        <span class="item__title">会员ID:</span>
                        <span class="item__value">
                            8888888888888
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item-1">
                        <span class="item__title">订单标示:</span>
                        <span class="item__value">
                            <board-tag style="margin:0 3px" v-for="mark in ['001','002', '003']" :key="mark" :tagValue="mark" />
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="12" class="detail-card__body__right">
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">交易号:</span>
                        <span class="item__value">
                            19888882121221212121212181
                            <input class="copy-input--hidden" value="198888881" id="businessNo" />

                        </span>
                        <span class="item__op">
                            <el-button type="text" icon="el-icon-document-copy" @click="onClickCopy('businessNo')"></el-button>
                        </span>
                    </div>
                    <div class="item item2">
                        <span class="item__title">订单号:</span>
                        <span class="item__value">
                            202019888002
                            <input class="copy-input--hidden" value="202019888002" id="orderNum" />

                        </span>
                        <span class="item__op">
                            <el-button type="text" icon="el-icon-document-copy" @click="onClickCopy('orderNum')"></el-button>
                        </span>
                    </div>
                    <div class="item item3">
                        <span class="item__title">外部单号:</span>
                        <span class="item__value">
                            202028888888
                            <input class="copy-input--hidden" value="202028888888" id="outOrderNum" />

                        </span>
                        <span class="item__op">
                            <el-button type="text" icon="el-icon-document-copy" @click="onClickCopy('outOrderNum')"></el-button>
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">来源渠道:</span>
                        <span class="item__value">
                            APP
                        </span>
                    </div>
                    <div class="item item2">
                        <span class="item__title">订单类型:</span>
                        <span class="item__value">
                            普通订单
                        </span>
                    </div>
                    <div class="item item3"></div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">同步库存:</span>
                        <span class="item__value">
                            已同步
                        </span>
                    </div>
                    <div class="item item2">
                        <span class="item__title">同步派车:</span>
                        <span class="item__value">
                            未同步
                        </span>
                    </div>
                    <div class="item item3">
                        <span class="item__title">派车信息:</span>
                        <span class="item__value">
                            未知
                        </span>
                    </div>
                </div>
            </el-col>

        </el-row>
    </el-card>
    <el-card shadow="never" class="detail-card order-detail-logistics">
        <div slot="header" class="detail-card__header">
            <el-row class="order-detail-logistics__header">
                <el-col :span="6" class="title border-right">
                    <div class="title__inner">物流信息</div>
                </el-col>
                <el-col :span="18" class="tabs">
                    <el-tabs v-model="activePackage" type="card">
                        <el-tab-pane label="包裹一" name="first"></el-tab-pane>
                        <el-tab-pane label="包裹二" name="second"></el-tab-pane>
                    </el-tabs>
                </el-col>
            </el-row>
        </div>
        <el-row class="detail-card__body">
            <el-col :span="6" class="detail-card__body__left border-right">
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">物流公司:</span>
                        <span class="item__value">
                            特特快递
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">物流单号:</span>
                        <span class="item__value">
                            XX111111111111
                        </span>
                        <span class="item__op">
                            <input class="copy-input--hidden" value="XX111111111111" id="moveNo" />
                            <el-button type="text" icon="el-icon-document-copy" @click="onClickCopy('moveNo')"></el-button>
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">联系电话:</span>
                        <span class="item__value">
                            17602187629
                        </span>
                    </div>
                </div>
                <div class="row row4">
                    <div class="item item1">
                        <span class="item__title">发货仓库:</span>
                        <span class="item__value">
                            上海仓
                        </span>
                    </div>
                </div>
                <div class="row row5">
                    <div class="item item1">
                        <span class="item__title">重量:</span>
                        <span class="item__value">
                            1.00kg
                        </span>
                        <span class="item__op">
                            <el-button type="text" @click="onClickPackageDetailBtn">包裹明细</el-button>
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="18" class="detail-card__body__middle">
                <el-timeline class="order-detail-logistics__timeline">
                    <el-timeline-item v-for="(activity, index) in activities" :key="index" :icon="activity.icon" :type="activity.type" :color="activity.color" :size="activity.size">
                        <div class="timeline-high-light">{{activity.highlightContent}}</div>
                        <div class="timeline-content">{{activity.content}}</div>
                        <div class="timeline-left">{{activity.timestamp}}</div>
                    </el-timeline-item>
                </el-timeline>
            </el-col>
        </el-row>
    </el-card>
    <el-card shadow="never" class="detail-card order-detail-info">
        <el-row>
            <el-col :span="6" class="border-right">
                <div class="title"><span>收货信息</span>
                    <el-button type="text" @click="onClickEditAddressBtn">修改地址</el-button>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">下单账号:</span>
                        <span class="item__value">
                            嘿嘿
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">收货人:</span>
                        <span class="item__value">
                            呵呵呵
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">手机号:</span>
                        <span class="item__value">
                            17602187629
                        </span>
                    </div>
                </div>
                <div class="row row4">
                    <div class="item item1">
                        <span class="item__title">收货地址:</span>
                        <span class="item__value">
                            上海市浦东新区嘿嘿软件园3号楼
                        </span>
                    </div>
                </div>
                <div class="row row5">
                    <div class="item item1">
                        <span class="item__title">买家留言:</span>
                        <span class="item__value">
                            请发顺丰
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="6" class="border-right">
                <div class="title">配送信息</div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">配送方式:</span>
                        <span class="item__value">
                            快递
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">运费:</span>
                        <span class="item__value">
                            ¥10.00
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">期望配送时间:</span>
                        <span class="item__value">
                            2019.03.30 18:00
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="6" class="border-right">
                <div class="title">订单金额信息</div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">付款类型:</span>
                        <span class="item__value">
                            线上支付
                        </span>
                        <span class="item__op">
                            <el-button type="text" @click="onClickPayDetailBtn">明细</el-button>
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">支付方式:</span>
                        <span class="item__value">
                            支付宝
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">支付时间:</span>
                        <span class="item__value">
                            2019.03.30 18:00
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="6">
                <div class="title">发票信息</div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">发票类型:</span>
                        <span class="item__value">
                            电子普通发票
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">开票状态:</span>
                        <span class="item__value">
                            已开票
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">发票抬头:</span>
                        <span class="item__value">
                            奥特曼
                        </span>
                    </div>
                </div>
                <div class="row row4">
                    <div class="item item1">
                        <span class="item__title">发票内容:</span>
                        <span class="item__value">
                            商品明细
                        </span>
                        <span class="item__op">
                            <el-button type="text" @click="onClickInvoiceDetailBtn">明细</el-button>
                        </span>
                    </div>
                </div>
            </el-col>
        </el-row>
    </el-card>
    <el-card shadow="never" class="detail-card detail-product-info">
        <div class="order-datail-products__table">
            <el-table :data="productTableData" size="mini" border ref="table" style="width: 100%;">
                <el-table-column prop="f1" width="85" header-align="center" align="center" label="商品名称">
                </el-table-column>
                <el-table-column prop="f2" header-align="center" align="center" label="商品编码" min-width="100">
                </el-table-column>
                <el-table-column prop="f3" header-align="center" align="center" label="商品标记" min-width="100">
                    <template slot-scope="scope">
                        <board-tag :tagValue="scope.row.f3" />
                    </template>
                </el-table-column>
                <el-table-column prop="f4" header-align="center" align="center" label="商品条码">
                </el-table-column>
                <el-table-column prop="f5" header-align="center" align="center" label="数量（件）">
                </el-table-column>
                <el-table-column prop="f6" header-align="center" align="center" label="单价">
                </el-table-column>
                <el-table-column prop="f7" header-align="center" align="center" label="总价">
                </el-table-column>
                <el-table-column prop="f8" header-align="center" align="center" label="优惠金额">
                </el-table-column>
                <el-table-column prop="f9" header-align="center" align="center" label="实付金额">
                </el-table-column>
                <el-table-column prop="f10" header-align="center" align="center" label="库存状态">
                </el-table-column>
            </el-table>
        </div>
        <div class="order-datail-products__sum">
            <div class="row row1">
                <div class="item item1">
                    <span class="sum">合计</span>
                    <span class="item__title">
                        商品总额:
                    </span>
                    <span class="item__value">
                        ¥ 1100.00
                    </span>
                </div>
            </div>
            <div class="row row2">
                <div class="item item1">
                    <span class="item__title">
                        券卡/优惠:
                    </span>
                    <span class="item__value">
                        ¥ 120.00
                    </span>
                </div>
            </div>
            <div class="row row3">
                <div class="item item1">
                    <span class="item__title">
                        运费金额:
                    </span>
                    <span class="item__value">
                        ¥ 10.00
                    </span>
                </div>
            </div>
            <div class="row row4">
                <div class="item item1 total">
                    <span class="item__title">
                        实付金额:
                    </span>
                    <span class="item__value">
                        ¥ 990.00
                    </span>
                </div>
            </div>
        </div>
    </el-card>
    <log-dialog :visible="showLogDialog" @dialog-close="showLogDialog=false" />
    <package-dialog :visible="showPackageDialog" @dialog-close="showPackageDialog=false" />
    <pay-dialog :visible="showPayDialog" @dialog-close="showPayDialog=false" />
    <invoice-dialog :visible="showInvoiceDialog" @dialog-close="showInvoiceDialog=false" />
    <split-dialog :visible="showSplitDialog" @dialog-close="showSplitDialog=false" />
    <service-dialog :visible="showServiceDialog" @dialog-close="showServiceDialog=false" />
    <edit-address-dialog :visible="showEditAddressDialog" @dialog-close="showEditAddressDialog=false" />
</div>
    `
    });
});
