lyfdefine(['scm-order-common/scm-order-common-tag','scm-order-common/scm-order-common-split'], function (BoardTag,SplitDialog) {
    return ({
        components: {
            BoardTag,
            SplitDialog
        },
        props: {
            baseFilterForm: {
                type: Object,
                default () {
                    return null
                }
            },
            currentState: {
                type: [String, Number],
                default: ''
            },
            state: {
                type: [String, Number],
                default: ''
            }
        },
        watch: {
            currentState: {
                handler() {
                    if (this.currentState === this.state && this.tableData.length === 0) {
                        this.loading = true
                        this.$http({
                            method: 'get',
                            url: this.POOL_NAME + '/scm-order-board/list11/' + this.state,
                        }).then(res => {
                            this.loading = false
                            this.tableData = res.data.data.list
                            this.paginationOption.total = res.data.data.total
                        }, () => {
                            const problemOrders = Mock.mock({
                                'list|0-20': [{
                                    // 属性 id 是一个自增数，起始值为 1，每次增 1
                                    'id|+1': 1,
                                    'orderTypeCode': '2',
                                    'transactionNumber': Mock.Random.natural(10000),
                                    'orderNumber': Mock.Random.natural(80),
                                    'orderStates': ['问题订单', Mock.Random.pick(['价格异常', '地址异常'])],
                                    'sourceChannelName': 'APP',
                                    'orderTypeName': '普通订单',
                                    'orderMarks': ['001', '002', '003', '004'],
                                    'orderAmount': Mock.Random.natural(20, 200),
                                    'userName': Mock.Random.cname(),
                                    'userCode': Mock.Random.natural(9090, 1000),
                                    'newLogisticsInfo': Mock.Random.datetime() + '仓库拣货完成',
                                    'orderDatetime': Mock.Random.datetime(),
                                    'logisticsNumber': Mock.Random.natural(0, 20),
                                    'actualAmount': Mock.Random.natural(20, 200),
                                    'expressAmount': Mock.Random.natural(20, 200),
                                    'receiver': '',
                                    'provinceName': Mock.Random.province(),
                                    'cityName': Mock.Random.city(),
                                    'areaName': '',
                                    'address': '',
                                    'postCode': Mock.Random.zip(),
                                    'discountPrice': Mock.Random.natural(20, 200),
                                    'sellerRemark': Mock.Random.cparagraph(2),
                                    'buyerRemark': Mock.Random.cparagraph(2),
                                    'show': false,
                                    'products|1-10': [{
                                        skuCode: Mock.Random.pick(['10056', '10057', '10058', '10059', '10060']),
                                        productMark: '',
                                        skuName: Mock.Random.pick(['百年好核 好核桃仁 120g', '百年好核 好核桃仁 130g', '百年好核 好核桃仁 150g', '百年好核 好核桃仁 160g', '百年好核 好核桃仁 170g']),
                                        skuBarCode: Mock.Random.natural(80, 200),
                                        unitPrice: Mock.Random.natural(20, 200),
                                        amount: Mock.Random.natural(20, 200),
                                        salePrcie: Mock.Random.natural(20, 200),
                                        actualPrice: Mock.Random.natural(20, 200),
                                        discountPrice: Mock.Random.natural(20, 200),
                                        stockState: Mock.Random.pick(['库存锁定', '缺货']),
                                    }, ],
                                }],
                            });
                            const stopOrders = Mock.mock({
                                'list|0-20': [{
                                    // 属性 id 是一个自增数，起始值为 1，每次增 1
                                    'id|+1': 600,
                                    'transactionNumber': Mock.Random.natural(10000),
                                    'orderNumber': Mock.Random.natural(80),
                                    'orderStates': ['截停订单'],
                                    'sourceChannelName': 'APP',
                                    'orderTypeName': '普通订单',
                                    'orderMarks': ['002', '003', '004'],
                                    'orderAmount': Mock.Random.natural(20, 200),
                                    'userName': Mock.Random.cname(),
                                    'userCode': '',
                                    'newLogisticsInfo': '',
                                    'orderDatetime': Mock.Random.datetime(),
                                    'logisticsNumber': Mock.Random.natural(0, 20),
                                    'actualAmount': Mock.Random.natural(20, 200),
                                    'expressAmount': Mock.Random.natural(20, 200),
                                    'receiver': '',
                                    'provinceName': Mock.Random.province(),
                                    'cityName': Mock.Random.city(),
                                    'areaName': '',
                                    'address': '',
                                    'show': false,
                                    'postCode': Mock.Random.zip(),
                                    'discountPrice': Mock.Random.natural(20, 200),
                                    'sellerRemark': Mock.Random.cparagraph(2),
                                    'buyerRemark': Mock.Random.cparagraph(2),
                                    'products|1-10': [{
                                        skuCode: Mock.Random.pick(['10056', '10057', '10058', '10059', '10060']),
                                        productMark: '',
                                        skuName: Mock.Random.pick(['百年好核 好核桃仁 120g', '百年好核 好核桃仁 130g', '百年好核 好核桃仁 150g', '百年好核 好核桃仁 160g', '百年好核 好核桃仁 170g']),
                                        skuBarCode: Mock.Random.natural(80, 200),
                                        unitPrice: Mock.Random.natural(20, 200),
                                        amount: Mock.Random.natural(20, 200),
                                        salePrcie: Mock.Random.natural(20, 200),
                                        actualPrice: Mock.Random.natural(20, 200),
                                        discountPrice: Mock.Random.natural(20, 200),
                                        stockState: Mock.Random.pick(['库存锁定', '缺货']),
                                    }, ],
                                }],
                            });
                            const advanceOrders = Mock.mock({
                                'list|0-30': [{
                                    // 属性 id 是一个自增数，起始值为 1，每次增 1
                                    'id|+1': 300,
                                    'orderTypeCode': '0',
                                    'transactionNumber': Mock.Random.natural(10000),
                                    'orderNumber': Mock.Random.natural(80),
                                    'orderStates': ['预售订单'],
                                    'show': false,
                                    'sourceChannelName': 'APP',
                                    'orderTypeName': '普通订单',
                                    'orderMarks': ['001', '003', '004'],
                                    'orderAmount': Mock.Random.natural(20, 200),
                                    'userName': Mock.Random.cname(),
                                    'userCode': '',
                                    'newLogisticsInfo': '',
                                    'orderDatetime': Mock.Random.datetime(),
                                    'logisticsNumber': Mock.Random.natural(0, 20),
                                    'actualAmount': Mock.Random.natural(20, 200),
                                    'expressAmount': Mock.Random.natural(20, 200),
                                    'receiver': '',
                                    'provinceName': Mock.Random.province(),
                                    'cityName': Mock.Random.city(),
                                    'areaName': '',
                                    'address': '',
                                    'postCode': Mock.Random.zip(),
                                    'discountPrice': Mock.Random.natural(20, 200),
                                    'sellerRemark': Mock.Random.cparagraph(2),
                                    'buyerRemark': Mock.Random.cparagraph(2),
                                    'products|1-10': [{
                                        skuCode: Mock.Random.pick(['10056', '10057', '10058', '10059', '10060']),
                                        productMark: '',
                                        skuName: Mock.Random.pick(['百年好核 好核桃仁 120g', '百年好核 好核桃仁 130g', '百年好核 好核桃仁 150g', '百年好核 好核桃仁 160g', '百年好核 好核桃仁 170g']),
                                        skuBarCode: Mock.Random.natural(80, 200),
                                        unitPrice: Mock.Random.natural(20, 200),
                                        amount: Mock.Random.natural(20, 200),
                                        salePrcie: Mock.Random.natural(20, 200),
                                        actualPrice: Mock.Random.natural(20, 200),
                                        discountPrice: Mock.Random.natural(20, 200),
                                        stockState: Mock.Random.pick(['库存锁定', '缺货']),
                                    }, ],
                                }],
                            });
                            const outOfStockOrders = Mock.mock({
                                'list|0-20': [{
                                    // 属性 id 是一个自增数，起始值为 1，每次增 1
                                    'id|+1': 200,
                                    'transactionNumber': Mock.Random.natural(10000),
                                    'orderNumber': Mock.Random.natural(80),
                                    'orderStates': ['缺货订单'],
                                    'show': false,
                                    'sourceChannelName': 'APP',
                                    'orderTypeName': '普通订单',
                                    'orderMarks': ['001', '002', '003', '004'],
                                    'orderAmount': Mock.Random.natural(20, 200),
                                    'userName': Mock.Random.cname(),
                                    'userCode': '',
                                    'newLogisticsInfo': '',
                                    'orderDatetime': Mock.Random.datetime(),
                                    'logisticsNumber': Mock.Random.natural(0, 20),
                                    'actualAmount': Mock.Random.natural(20, 200),
                                    'expressAmount': Mock.Random.natural(20, 200),
                                    'receiver': '',
                                    'provinceName': Mock.Random.province(),
                                    'cityName': Mock.Random.city(),
                                    'areaName': '',
                                    'address': '',
                                    'postCode': Mock.Random.zip(),
                                    'discountPrice': Mock.Random.natural(20, 200),
                                    'sellerRemark': Mock.Random.cparagraph(2),
                                    'buyerRemark': Mock.Random.cparagraph(2),
                                    'products|1-10': [{
                                        skuCode: Mock.Random.pick(['10056', '10057', '10058', '10059', '10060']),
                                        productMark: '',
                                        skuName: Mock.Random.pick(['百年好核 好核桃仁 120g', '百年好核 好核桃仁 130g', '百年好核 好核桃仁 150g', '百年好核 好核桃仁 160g', '百年好核 好核桃仁 170g']),
                                        skuBarCode: Mock.Random.natural(80, 200),
                                        unitPrice: Mock.Random.natural(20, 200),
                                        amount: Mock.Random.natural(20, 200),
                                        salePrcie: Mock.Random.natural(20, 200),
                                        actualPrice: Mock.Random.natural(20, 200),
                                        discountPrice: Mock.Random.natural(20, 200),
                                        stockState: Mock.Random.pick(['库存锁定', '缺货']),
                                    }, ],
                                }],
                            });
                            const pendingorders = Mock.mock({
                                'list|0-20': [{
                                    // 属性 id 是一个自增数，起始值为 1，每次增 1
                                    'id|+1': 100,
                                    'orderTypeCode': '1',
                                    'transactionNumber': Mock.Random.natural(10000),
                                    'orderNumber': Mock.Random.natural(80),
                                    'orderStates': ['待客服处理'],
                                    'sourceChannelName': 'APP',
                                    'orderTypeName': '门店订单',
                                    'show': false,
                                    'orderMarks': ['001', '002', '004'],
                                    'orderAmount': Mock.Random.natural(20, 200),
                                    'userName': Mock.Random.cname(),
                                    'userCode': '',
                                    'newLogisticsInfo': '',
                                    'orderDatetime': Mock.Random.datetime(),
                                    'logisticsNumber': Mock.Random.natural(0, 20),
                                    'actualAmount': Mock.Random.natural(20, 200),
                                    'expressAmount': Mock.Random.natural(20, 200),
                                    'receiver': '',
                                    'provinceName': Mock.Random.province(),
                                    'cityName': Mock.Random.city(),
                                    'areaName': '',
                                    'address': '',
                                    'postCode': Mock.Random.zip(),
                                    'discountPrice': Mock.Random.natural(20, 200),
                                    'sellerRemark': Mock.Random.cparagraph(2),
                                    'buyerRemark': Mock.Random.cparagraph(2),
                                    'products|1-10': [{
                                        skuCode: Mock.Random.pick(['10056', '10057', '10058', '10059', '10060']),
                                        productMark: '',
                                        skuName: Mock.Random.pick(['百年好核 好核桃仁 120g', '百年好核 好核桃仁 130g', '百年好核 好核桃仁 150g', '百年好核 好核桃仁 160g', '百年好核 好核桃仁 170g']),
                                        skuBarCode: Mock.Random.natural(80, 200),
                                        unitPrice: Mock.Random.natural(20, 200),
                                        amount: Mock.Random.natural(20, 200),
                                        salePrcie: Mock.Random.natural(20, 200),
                                        actualPrice: Mock.Random.natural(20, 200),
                                        discountPrice: Mock.Random.natural(20, 200),
                                        stockState: Mock.Random.pick(['库存锁定', '缺货']),
                                    }, ],
                                }],
                            });
                            const data = {
                                a: problemOrders,
                                b: stopOrders,
                                c: advanceOrders,
                                d: outOfStockOrders,
                                e: pendingorders,
                                f: problemOrders,
                                g: problemOrders,
                                h: problemOrders,
                                i: problemOrders
                            }
                            if (data[this.currentState]) {
                                this.tableData = data[this.currentState].list
                                this.paginationOption.total = data[this.currentState].list.length
                            } else {
                                this.tableData =  []
                                this.paginationOption.total = 0
                            }
                            this.loading = false
                        })
                    }
                },
                immediate: true
            }
        },
        data() {
            return {
                checkedRows: [],
                isIndeterminate: true,
                checkAll: false,
                loading: false,
                splitDialogVisible: false,
                POOL_NAME: '/scm-order-admin',
                tableData: [],
                paginationOption: {
                    total: 3,
                    page: 1,
                    pageSize: 10
                },
            }
        },
        methods: {
            onClickEditBtn(row) {
                this.$router.push({
                    path: 'scm-order-detail',
                    query: {
                        orderTypeCode: row.orderTypeCode,
                        orderNum: row.orderNumber
                    }
                })
            },
            onClickSplitBtn() {
                this.splitDialogVisible = true
            },
            onClickCancelBtn() {
                this.$confirm(`确定要取消这条订单?`, '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                })
                    .then(() => {
                        this.$message({
                            type: 'success',
                            message: '取消成功',
                            showClose: true
                        })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作',
                            showClose: true
                        })
                    });
            },
            onClickDeliverBtn() {
                this.$confirm(`确定要对此条订单执行发货操作?`, '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                })
                    .then(() => {
                        this.$message({
                            type: 'success',
                            message: '发货请求已提交',
                            showClose: true
                        })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作',
                            showClose: true
                        })
                    });
            },
            onClickLockBtn() {
                this.$confirm(`确定要对此条订单执行锁单操作?`, '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                })
                    .then(() => {
                        this.$message({
                            type: 'success',
                            message: '锁单请求已提交',
                            showClose: true
                        })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消锁单操作',
                            showClose: true
                        })
                    });
            },
            onClickBatchCancelBtn() {
                const checkedLen = this.checkedRows.length
                if (checkedLen === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择需要取消的订单!',
                        showClose: true
                    })
                } else {
                    this.$confirm(`确定要取消${checkedLen}条订单?`, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    })
                        .then(() => {
                            this.$message({
                                type: 'success',
                                message: '取消成功',
                                showClose: true
                            })
                        })
                        .catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消操作',
                                showClose: true
                            })
                        });
                }
            },
            onClickMergeBtn() {
                const checkedLen = this.checkedRows.length
                if (checkedLen === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择需要合并的订单!',
                        showClose: true
                    })
                } else {
                    this.$confirm(`确定要合并${checkedLen}条订单?`, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    })
                        .then(() => {
                            this.$message({
                                type: 'success',
                                message: '合并成功',
                                showClose: true
                            })
                        })
                        .catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消操作',
                                showClose: true
                            })
                        });
                }
            },
            onClickBacthDeliverBtn() {
                const checkedLen = this.checkedRows.length
                if (checkedLen === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择需要发货的订单!',
                        showClose: true
                    })
                } else {
                    this.$confirm(`确定要对${checkedLen}条订单执行发货操作?`, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    })
                        .then(() => {
                            this.$message({
                                type: 'success',
                                message: '发货请求已提交',
                                showClose: true
                            })
                        })
                        .catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消操作',
                                showClose: true
                            })
                        });
                }
            },
            onClickFlagBtn() {
                this.$message({
                    type: 'info',
                    message: '功能暂未开通...',
                    showClose: true
                })
            },
            onClickBatchExportBtn() {
                this.$message({
                    type: 'info',
                    message: '功能暂未开通...',
                    showClose: true
                })
            },
            onClickAllExportBtn() {
                this.$message({
                    type: 'info',
                    message: '功能暂未开通...',
                    showClose: true
                })
            },
            handleCheckAllChange(val) {
                this.checkedRows = val ? this.tableData.map(n => n.id) : [];
                this.isIndeterminate = false;
            },
            handleCheckedRowsChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.tableData.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.tableData.length;
            },
            onClickDetailBtn(row) {
                this.$router.push({
                    path: 'scm-order-detail',
                    query: {
                        orderTypeCode: row.orderTypeCode,
                        orderNum: row.orderNumber
                    }
                })
            }
        },
        template: /*html*/`
<div data-scm-order class="scm-order-table-conatiner">
    <div class="scm-order-table-conatiner__header">
        <el-button plain type="danger" @click="onClickBatchCancelBtn">取消订单</el-button>
        <el-button type="primary" plain @click="onClickMergeBtn">合单</el-button>
        <el-button type="primary" plain @click="onClickBacthDeliverBtn">批量发货</el-button>
        <el-button type="primary" plain @click="onClickFlagBtn">插旗备注</el-button>
        <el-button type="primary" plain @click="onClickBatchExportBtn">批量导出</el-button>
        <el-button type="primary" plain @click="onClickAllExportBtn">全部导出</el-button>
    </div>
    <div class="scm-order-table-conatiner__body">
        <div class="scm-flex-table">
            <div class="scm-flex-table__header">
                <el-row class="table-row">
                    <el-col :span="1" class="table-col">
                        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"></el-checkbox>
                    </el-col>
                    <el-col :span="4" class="table-col">单号</el-col>
                    <el-col :span="2" class="table-col">来源渠道</el-col>
                    <el-col :span="2" class="table-col">订单类型</el-col>
                    <el-col :span="3" class="table-col">订单标示</el-col>
                    <el-col :span="1" class="table-col">金额</el-col>
                    <el-col :span="2" class="table-col">客户信息</el-col>
                    <el-col :span="4" class="table-col">最新物流信息</el-col>
                    <el-col :span="2" class="table-col">下单时间</el-col>
                    <el-col :span="3" class="table-col">操 作</el-col>
                </el-row>
            </div>
            <div class="scm-flex-table__body" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading">
                <div class="scm-flex-table__enpty" v-show="tableData.length === 0">无数据</div>
                <el-checkbox-group v-model="checkedRows" @change="handleCheckedRowsChange">
                    <div :class="!item.show ? 'scm-flex-table-item' : 'scm-flex-table-item open'" v-for="(item,index) in tableData" :key="index">
                        <el-row class="scm-flex-table-item__row">
                            <el-col :span="1" class="scm-flex-table-item__col scm-flex-table-item__col0">
                                <el-checkbox :label="item.id"></el-checkbox>
                            </el-col>
                            <el-col :span="4" class="scm-flex-table-item__col scm-flex-table-item__col1">
                                <div class="item item1">
                                    <span class="item__title">交易号:</span>
                                    <span class="item__value">{{item.transactionNumber}}</span>
                                </div>
                                <div class="item item2">
                                    <span class="item__title">订单号:</span>
                                    <span class="item__value">
                                        <el-button type="text" @click="onClickDetailBtn(item)">{{item.orderNumber}}</el-button>
                                    </span>
                                </div>
                                <div class="item item3">
                                    <span class="item__title">订单状态:</span>
                                    <span class="item__value">
                                        <span  class="item__value__state" v-for="state in item.orderStates" :key="state">{{state}}</span>
                                    </span>
                                </div>
                            </el-col>
                            <el-col :span="2" class="scm-flex-table-item__col scm-flex-table-item__col2">
                                {{item.sourceChannelName}}
                            </el-col>
                            <el-col :span="2" class="scm-flex-table-item__col scm-flex-table-item__col3">
                                {{item.orderTypeName}}
                            </el-col>
                            <el-col :span="3" class="scm-flex-table-item__col scm-flex-table-item__col4">
                                <div class="scm-flex-table-tag__col">
                                    <board-tag style="margin:0 3px" v-for="mark in item.orderMarks" :key="mark" :tagValue="mark" />
                                </div>
                            </el-col>
                            <el-col :span="1" class="scm-flex-table-item__col scm-flex-table-item__col5">{{item.orderAmount}}</el-col>
                            <el-col :span="2" class="scm-flex-table-item__col scm-flex-table-item__col6">{{item.userName}}&nbsp;{{item.userCode}}</el-col>
                            <el-col :span="4" class="scm-flex-table-item__col scm-flex-table-item__col7">{{item.newLogisticsInfo}}</el-col>
                            <el-col :span="2" class="scm-flex-table-item__col scm-flex-table-item__col8">{{item.orderDatetime}}</el-col>
                            <el-col :span="3" class="scm-flex-table-item__col scm-flex-table-item__col9">
                                <div class="scm-flex-table-btn__col">
                                    <el-button type="text" @click="onClickEditBtn(item)">编辑</el-button>
                                    <el-button type="text" @click="onClickSplitBtn(item)">拆单</el-button>
                                    <el-button type="text" @click="onClickCancelBtn(item)">取消订单</el-button>
                                    <el-button type="text" @click="onClickDeliverBtn(item)">发货</el-button>
                                    <el-button type="text" @click="onClickLockBtn(item)">锁单</el-button>
                                    <el-button type="text" @click="item.show = !item.show">{{item.show ? '收起' : '展开'}}</el-button>
                                </div>
                            </el-col>
                        </el-row>
                        <el-collapse-transition>
                            <div v-show="item.show" class="scm-flex-table-collapse">
                                <div class="scm-flex-table-collapse__header">
                                    <el-row class="scm-flex-table-collapse__header__row">
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">发货单号：</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">订单金额：1833.22 元</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">收货人：张先生</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">邮编：8383883</el-col>
                                    </el-row>
                                    <el-row class="scm-flex-table-collapse__header__row">
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">系统单号:</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">实收金额：1833.22 元</el-col>
                                        <el-col :span="12" class="scm-flex-table-collapse__header__col">收货地址：上海市 长宁区 湖走路111号123弄 花溪花园 4栋302室</el-col>
                                    </el-row>
                                    <el-row class="scm-flex-table-collapse__header__row">
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">来源单号：</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">快递费用：0.00 元</el-col>
                                        <el-col :span="6" class="scm-flex-table-collapse__header__col">配送服务：</el-col>
                                    </el-row>
                                </div>
                                <div class="scm-flex-table-collapse__body">
                                    <el-table :data="item.products" style="width:100%;">
                                        <el-table-column type="index" width="55" header-align="center" align="center" label="序号">
                                        </el-table-column>
                                        <el-table-column prop="skuName" min-width="55" header-align="center" align="center" label="商品名称">
                                        </el-table-column>
                                        <el-table-column prop="skuCode" min-width="55" header-align="center" align="center" label="商品标记">
                                            <template slot-scope="scope">
                                                <board-tag :tagValue="scope.row.mark" />
                                            </template>
                                        </el-table-column>
                                        <el-table-column prop="skuCode" min-width="55" header-align="center" align="center" label="商品编码">
                                        </el-table-column>
                                        <el-table-column prop="skuCode" min-width="55" header-align="center" align="center" label="序商品条码">
                                        </el-table-column>
                                        <el-table-column prop="unitPrice" min-width="55" header-align="center" align="center" label="单价(元)">
                                        </el-table-column>
                                        <el-table-column prop="salePrcie" min-width="55" header-align="center" align="center" label="售价(元)">
                                        </el-table-column>
                                        <el-table-column prop="amount" min-width="55" header-align="center" align="center" label="数量(件)">
                                        </el-table-column>
                                        <el-table-column prop="actualPrice" min-width="55" header-align="center" align="center" label="实收金额(元)">
                                        </el-table-column>
                                        <el-table-column prop="discountPrice" min-width="55" header-align="center" align="center" label="优惠金额(元)">
                                        </el-table-column>
                                        <el-table-column prop="stockState" min-width="55" header-align="center" align="center" label="库存状态">
                                        </el-table-column>
                                    </el-table>
                                </div>
                                <div class="scm-flex-table-collapse__footer">
                                    <el-row class="scm-flex-table-collapse__footer__row row1">
                                        <el-col :span="2" :push="3" class="scm-flex-table-collapse__footer__col">总计:<span class="text-num">2</span>件</el-col>
                                        <el-col :span="4" :push="2" class="scm-flex-table-collapse__footer__col">实收金额:<span class="text-num">12:00</span>元</el-col>
                                        <el-col :span="5" class="scm-flex-table-collapse__footer__col">优惠:<span class="text-num">200:00</span>元</el-col>
                                    </el-row>
                                    <el-row class="scm-flex-table-collapse__footer__row">
                                        <el-col :span="24" class="scm-flex-table-collapse__footer__col">买家留言：{{item.buyerRemark}}</el-col>
                                    </el-row>
                                    <el-row class="scm-flex-table-collapse__footer__row">
                                        <el-col :span="24" class="scm-flex-table-collapse__footer__col">卖家留言：{{item.sellerRemark}}</el-col>
                                    </el-row>
                                </div>
                            </div>
                        </el-collapse-transition>
                    </div>
                </el-checkbox-group>
            </div>
        </div>
    </div>
    <div class="scm-order-table-conatiner__footer">
        <el-pagination :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
    </div>
    <split-dialog :visible="splitDialogVisible" @dialog-close="splitDialogVisible=false" />
</div>
    `
    });
});
