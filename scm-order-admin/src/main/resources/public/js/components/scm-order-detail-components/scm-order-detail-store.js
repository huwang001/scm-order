lyfdefine([], function () {
    return ({
        name: 'scm-order-detail-ordinary',
        data() {
            return {
                activeStep: 2,
                productTableData: [],
                logTableData: [],
                steps: [{
                    title: '新创建',
                    des: '2020-05-05  15:00:00'
                },
                    {
                        title: '订单审核',
                        des: '2020-05-05  15:00:00'
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
        template: /*html*/`
<div data-scm-order class="scm-order-detail scm-order-detail-store">
    <el-card shadow="never" class="detail-card order-detail-step">
        <el-steps :active="activeStep" align-center finish-status="success">
            <el-step v-for="step in steps" :key="step.title" :title="step.title" :description="step.des"></el-step>
        </el-steps>
    </el-card>
    <el-card shadow="never" class="detail-card order-detail-operation">
        <el-row class="detail-card__body order-detail-operation__row">
            <el-col :span="8" class="detail-card__body__left border-right">
                <div class="item-1">
                    <div class="title">待发货</div>
                </div>
                <div class="item-2">
                    <el-button type="primary" class="detail-order-info__left__btn">发货</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn">取消订单</el-button>
                    <el-button type="primary" class="detail-order-info__left__btn">截停订单</el-button>
                </div>
            </el-col>
            <el-col :span="16" class="detail-card__body__middle">
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">交易号:</span>
                        <span class="item__value">
                            198888881
                            <input class="copy-input--hidden" value="198888881" id="businessNo" />
                        </span>
                        <span class="item__op">
                            <el-button type="text" icon="el-icon-document-copy"></el-button>
                        </span>
                        <span class="item__op"><el-tag>vip</el-tag></span>
                    </div>
                    <div class="item item2">
                        <span class="item__title">订单号:</span>
                        <span class="item__value">
                            202019888002
                            <input class="copy-input--hidden" value="198888881" id="businesssNo" />
                        </span>
                        <span class="item__op">
                            <el-button type="text" icon="el-icon-document-copy"></el-button>
                        </span>
                    </div>
                    <div class="item item3"></div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">订单类型:</span>
                        <span class="item__value">
                            普通
                        </span>
                    </div>
                    <div class="item item2">
                        <span class="item__title">订单来源:</span>
                        <span class="item__value">
                            普通
                        </span>
                    </div>
                    <div class="item item3">
                        <span class="item__title">来源:</span>
                        <span class="item__value">
                            直营门店
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
            </el-row>
        </div>
        <el-row class="detail-card__body">
            <el-col :span="8" class="detail-card__body__left border-right">
                <div class="title"><span>收货信息</span>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">下单账号:</span>
                        <span class="item__value">
                            107U
                        </span>
                    </div>
                </div>
                <div class="row row2">
                    <div class="item item1">
                        <span class="item__title">收货人:</span>
                        <span class="item__value">
                            107U店长
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">收货地址:</span>
                        <span class="item__value">
                            上海市浦东新区XXXXXXXX号楼
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">手机号:</span>
                        <span class="item__value">
                            176****7629
                        </span>
                    </div>
                </div>
                <div class="row row3">
                    <div class="item item1">
                        <span class="item__title">买家留言:</span>
                        <span class="item__value">
                            早点送到哦
                        </span>
                    </div>
                </div>

            </el-col>
            <el-col :span="8" class="detail-card__body__left border-right">
                <div class="title"><span>仓储履约信息</span>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">发货仓:</span>
                        <span class="item__value">
                            上海仓
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">仓库联系人:</span>
                        <span class="item__value">
                            伊仔
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">选择策略:</span>
                        <span class="item__value">
                            成本优先
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">预计发货时间:</span>
                        <span class="item__value">
                            2020.9.30 18:00
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">仓库履约成本:</span>
                        <span class="item__value">
                            105.00
                        </span>
                    </div>
                </div>
            </el-col>
            <el-col :span="8" class="detail-card__body__left border-right">
                <div class="title"><span>配送履约信息</span>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">承运方式:</span>
                        <span class="item__value">
                            快递
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">承运商:</span>
                        <span class="item__value">
                            顺丰
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">预计揽件时间:</span>
                        <span class="item__value">
                            2020.9.30 18:00
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">预计送达时间:</span>
                        <span class="item__value">
                            2020.9.30 18:00
                        </span>
                    </div>
                </div>
                <div class="row row1">
                    <div class="item item1">
                        <span class="item__title">运费:</span>
                        <span class="item__value">
                            5.00
                        </span>
                    </div>
                </div>
            </el-col>
        </el-row>
    </el-card>
    <el-card shadow="never" class="detail-card order-detail-product">
        <div class="detail-product-info__table">
            <el-tabs type="border-card">
                <el-tab-pane label="商品明细">
                    <div>
                        <el-table :data="productTableData" size="mini" border ref="table" style="width: 100%;">
                            <el-table-column type="index" width="85" header-align="center" align="center" label="商品名称">
                            </el-table-column>
                            <el-table-column prop="carrierName" header-align="center" align="center" label="商品编码" min-width="100">
                            </el-table-column>
                            <el-table-column prop="carNumber" header-align="center" align="center" label="商品标记" min-width="100">
                            </el-table-column>
                            <el-table-column prop="driver" header-align="center" align="center" label="商品条码">
                            </el-table-column>
                            <el-table-column prop="deliveryMan" header-align="center" align="center" label="数量（件）">
                            </el-table-column>
                            <el-table-column prop="creator" header-align="center" align="center" label="单价">
                            </el-table-column>
                            <el-table-column prop="modifier" header-align="center" align="center" label="总价">
                            </el-table-column>
                            <el-table-column prop="modifier" header-align="center" align="center" label="优惠金额">
                            </el-table-column>
                            <el-table-column prop="modifier" header-align="center" align="center" label="实付金额">
                            </el-table-column>
                            <el-table-column prop="modifier" header-align="center" align="center" label="库存状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="操作日志">
                    <el-table :data="logTableData" size="mini" border ref="table" style="width: 100%;">
                        <el-table-column type="index" width="85" header-align="center" align="center" label="操作人">
                        </el-table-column>
                        <el-table-column prop="carrierName" header-align="center" align="center" label="操作时间" min-width="100">
                        </el-table-column>
                        <el-table-column prop="carNumber" header-align="center" align="center" label="操作标记" min-width="100">
                        </el-table-column>
                        <el-table-column prop="driver" header-align="center" align="center" label="操作商品">
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
        </div>
    </el-card>
</div>
    `
    });
});
