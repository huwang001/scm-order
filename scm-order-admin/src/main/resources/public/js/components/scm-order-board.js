lyfdefine(['style/scm-order-style','scm-order-board-components/scm-order-board-table'], function (style,ScmOrderTable) {
    return ({
        name: 'scm-order-board',
        components: {
            ScmOrderTable
        },
        data() {
            return {

                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                tableState: 'a',
                activeTab: 'a',
                state: '',
                showCollapse: false,
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                },
                tabArray: [{
                    name: 'a',
                    label: '全部',
                    num: 100,
                },
                    {
                        name: 'b',
                        label: '问题订单',
                        num: 10,
                    },
                    {
                        name: 'c',
                        label: '截停订单',
                        num: 20,
                    },
                    {
                        name: 'd',
                        label: '预售订单',
                        num: 10,
                    },
                    {
                        name: 'e',
                        label: '缺货订单',
                        num: 10,
                    },
                    {
                        name: 'f',
                        label: '待客服处理',
                        num: 10,
                    },
                ],
                filterForm: {
                    fhdh: '',
                    jydh: '',
                    shmc: '',
                    bhsp: '',
                    lyqd: '',
                    djlx: '',
                    xdkssj: '',
                    xdjssj: '',
                    ddlx: '',
                },
                filterFormOption: {
                    states: [{
                        label: '待下发仓库(0)',
                        value: 'g',
                    },
                        {
                            label: '仓库生产中(0)',
                            value: 'h'
                        },
                        {
                            label: '系统报缺(0)',
                            value: 'i'
                        },
                        {
                            label: '实物报缺(0)',
                            value: 'j'
                        },
                        {
                            label: '已发货(0)',
                            value: 'k'
                        },
                        {
                            label: '物流配送中(0)',
                            value: 'l'
                        },
                        {
                            label: '已签收(0)',
                            value: 'm'
                        },
                        {
                            label: '已拒收(0)',
                            value: 'n'
                        }
                    ],
                    orderTypes: [{

                        label: '配送单',
                        value: 3
                    },
                        {
                            label: '普通订单',
                            value: 4
                        },
                        {
                            label: '天猫订单',
                            value: 5
                        },
                        {
                            label: '销售单',
                            value: 6
                        }
                    ],
                    channelTypes: [{
                        label: '直营门店',
                        value: 1
                    },
                        {
                            label: '加盟门店',
                            value: 2
                        },
                        {
                            label: 'APP',
                            value: 3
                        },
                        {
                            label: '销售中心',
                            value: 4
                        },
                        {
                            label: '云商',
                            value: 5
                        },
                        {
                            label: '其他类型',
                            value: 6
                        }
                    ],
                    documentTypes: [{
                        label: '待定',
                        value: 1
                    }],
                }
            }
        },
        methods: {
            onClickResetBtn() {
                if (this.$refs.filterForm) {
                    this.$refs.filterForm.resetFields()
                }
            },
            onClickCreateOrderBtn() {
                this.$router.push({
                    path: 'scm-order-create-ordinary'
                })
            },
            handleChangeoOtherState(value) {
                this.activeTab = value
                this.tableState = value
            },
            handleTabClick(tab) {
                this.state = ''
                this.tableState = tab.name
            }
        },
        template: /*html*/`
 <div data-scm-order class="scm-order-board">
    <div class="scm-order-board__filter">
        <div class="scm-order-board__filter__header">
            <div></div>
            <div>
                <el-button type="primary" plain @click="onClickCreateOrderBtn">创建订单</el-button>
                <el-button type="primary" plain>批量导入订单</el-button>
                <el-button type="primary" plain>批量发货</el-button>
            </div>
        </div>
        <div class="scm-order-board__filter__form">
            <el-form ref="filterForm" :model="filterForm" label-width="100px">
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="发货单号：" prop="fhdh">
                            <el-input type="textarea" placeholder="支持换行多个查询" autosize v-model="filterForm.fhdh"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="交易单号：" prop="jydh">
                            <el-input v-model="filterForm.jydh" placeholder="交易单号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="收货方名称：" prop="shmc">
                            <el-input v-model="filterForm.shmc" placeholder="门店编号/名称、用户名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="包含商品：" prop="bhsp">
                            <el-input autosize type="textarea" v-model="filterForm.bhsp" placeholder="支持多行换行查询"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row justify="center" align="middle" type="flex">
                    <el-col :span="6">
                        <el-form-item label="来源渠道：" prop="lyqd">
                            <el-select v-model="filterForm.lyqd" placeholder="请选择渠道" style="width:100%">
                                <el-option v-for="item in filterFormOption.channelTypes" :key="item.value" :label="item.label" :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据类型：" prop="djlx">
                            <el-select v-model="filterForm.djlx" placeholder="请选择单据类型" style="width:100%">
                                <el-option v-for="item in filterFormOption.documentTypes" :key="item.value" :label="item.label" :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="下单时间：" prop="xdkssj">
                            <el-date-picker style="width:100%;" v-model="filterForm.xdkssj" type="datetimerange" :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-collapse-transition>
                    <el-row v-show="showCollapse">
                        <el-col :span="6">
                            <el-form-item label="订单类型" prop="ddlx">
                                <el-select v-model="filterForm.ddlx" placeholder="请选择订单类型" style="width:100%">
                                    <el-option v-for="item in filterFormOption.orderTypes" :key="item.value" :label="item.label" :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-collapse-transition>
                <el-row justify="center" align="middle" type="flex">
                    <el-button type="primary">查询</el-button>
                    <el-button @click="onClickResetBtn">重置</el-button>
                    <el-button @click="showCollapse = !showCollapse">更多 <i :class="!showCollapse ? 'el-icon-arrow-down' : 'el-icon-arrow-up'"></i></el-button>
                </el-row>
            </el-form>
        </div>
    </div>
    <div class="scm-order-board__content">
        <div class="scm-order-board__content__header">
            <div class="tab-container">
                <el-tabs v-model="activeTab" @tab-click="handleTabClick">
                    <el-tab-pane :name="tab.name" v-for="(tab, index) in tabArray" :key="index">
                        <span slot="label" class="tab-container-item">
                            <span class="text-title">{{tab.label}}</span>
                            <span class="text-num">({{tab.num}})</span>
                        </span>
                    </el-tab-pane>
                </el-tabs>
            </div>
            <div class="select-container">
                <span class="text-title">更多状态：</span>
                <el-select v-model="state" @change="handleChangeoOtherState" placeholder="请选择其他状态">
                    <el-option v-for="item in filterFormOption.states" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="scm-order-board__content__body">
            <scm-order-table v-show="activeTab === 'a'" state='a' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'b'" state='b' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'c'" state='c' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'd'" state='d' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'e'" state='e' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'f'" state='f' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'g'" state='g' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'h'" state='h' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'i'" state='i' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'j'" state='j' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'k'" state='k' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'l'" state='l' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'm'" state='m' :currentState="tableState" />
            <scm-order-table v-show="activeTab === 'n'" state='m' :currentState="tableState" />
        </div>
    </div>
</div>
    `
    });
});
