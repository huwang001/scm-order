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
                leftTableData: [{
                    skuName: '小核桃仁200g',
                    skuCode: '11001',
                    payAmount: '100.00元',
                    state: '库存缺货'
                },
                    {
                        skuName: '小核桃仁800g',
                        skuCode: '11002',
                        payAmount: '100.00元',
                        state: '库存充足'
                    },
                    {
                        skuName: '小核桃仁400g',
                        skuCode: '11003',
                        payAmount: '100.00元',
                        state: '库存缺货'
                    }
                ],
                rightTableData: [],
            };
        },
        methods: {
            init() {},
            onClickSelectBtn(row) {
                const index = this.leftTableData.findIndex(n => n.skuCode === row.skuCode);
                this.leftTableData.splice(index, 1)
                this.rightTableData.push(row)
            },
            onClickRemoveBtn(row) {
                const index = this.rightTableData.findIndex(n => n.skuCode === row.skuCode);
                this.rightTableData.splice(index, 1)
                this.leftTableData.push(row)
            },
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
 <el-dialog title="拆单列表" data-scm-order-common class="scm-order-common-dialog scm-order-common-split" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="830px">
    <el-row class="split__body" :gutter="10">
        <el-col :span="12">
            <el-card class="split__body__left" shadow="never">
                <div slot="header" class="split__body__left__header">
                    请选择要拆单的商品
                </div>
                <div class="split__body__left__body">
                    <el-table :data="leftTableData" height="400px" size="mini" ref="table" style="width: 100%;">
                        <el-table-column prop="skuName" min-width="85" header-align="center" align="center" label="商品名称">
                        </el-table-column>
                        <el-table-column prop="skuCode" header-align="center" align="center" label="商品编码" width="70">
                        </el-table-column>
                        <el-table-column prop="payAmount" header-align="center" align="center" label="支付金额" width="60">
                        </el-table-column>
                        <el-table-column prop="state" header-align="center" align="center" label="库存状态" width="60">
                        </el-table-column>
                        <el-table-column prop="deliveryMan" header-align="center" align="center" label="操作" width="60">
                            <template slot-scope="scope">
                                <el-button type="text" @click="onClickSelectBtn(scope.row)">选择</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-card>
        </el-col>
        <el-col :span="12">
            <el-card class="split__body__right" shadow="never">
                <div slot="header" class="split__body__right__header">
                    <div>已选的商品列表<span class="selected-text">已选择{{rightTableData.length}}项</span></div>
                </div>
                <div class="split__body__right__body">
                    <el-table :data="rightTableData" height="400px" size="mini" ref="table" style="width: 100%;">
                        <el-table-column prop="skuName" min-width="85" header-align="center" align="center" label="商品名称">
                        </el-table-column>
                        <el-table-column prop="skuCode" header-align="center" align="center" label="商品编码" width="70">
                        </el-table-column>
                        <el-table-column prop="payAmount" header-align="center" align="center" label="支付金额" width="60">
                        </el-table-column>
                        <el-table-column prop="state" header-align="center" align="center" label="库存状态" width="60">
                        </el-table-column>
                        <el-table-column prop="deliveryMan" header-align="center" align="center" label="操作" width="60">
                            <template slot-scope="scope">
                                <el-button type="text" @click="onClickRemoveBtn(scope.row)">移除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-card>
        </el-col>
    </el-row>
    <div slot="footer" class="split__footer">
        <el-button size="mini" type="primary">确定</el-button>
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
