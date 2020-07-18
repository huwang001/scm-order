lyfdefine(['style/scm-order-style'], function (style) {
    return ({
        name: "scm-order-create-ordinary",
        data() {
            return {
                fileList: [],
                submitForm: {
                    qd: "",
                    ddly: "",
                    ddlx: "",
                    hy: "",
                    xdzh: "",
                    shr: "",
                    hhdz: "",
                    sjh: "",
                    fklx: "",
                    psfs: "",
                    yjfhrq: "",
                    fjlb: [],
                    xdsp: [{
                        num: 100
                    },
                        {
                            num: 1001
                        }
                    ],
                    yf: ""
                },
                submitFormOption: {
                    channelTypes: [],
                    orderResources: [],
                    orderTypes: [],
                    payTypes: [],
                    deliveryTypes: []
                }
            };
        },
        methods: {
            validatorNum(item) {
                const innerFunc = function (rule, value, callback) {
                    if (value > 100) {
                        callback()
                    } else {
                        callback(new Error('请输入正整数'))
                    }
                }
                return innerFunc
            },
        },
        template: /*html*/`
<div data-scm-order class="scm-order-create-ordinary">
    <el-form ref="submitForm" inline-message :model="submitForm" label-width="120px">
        <el-card shadow="never" class="order-card order-info">
            <div slot="header" class="order-card__header">
                <h3>下单信息</h3>
            </div>
            <div class="order-info__content">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="渠道" prop="qd" :rules="[{ required: true, message: '请选择渠道', trigger: 'blur' }]">
                            <el-select v-model="submitForm.qd" placeholder="请选择渠道" style="width:200px">
                                <el-option :key="item.value" :label="item.label" :value="item.value" v-for="item in submitFormOption.channelTypes"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="订单来源" prop="ddly" :rules="[{ required: true, message: '请选择订单来源', trigger: 'blur' }]">
                            <el-select v-model="submitForm.ddly" placeholder="请选择订单来源" style="width:200px">
                                <el-option :key="item.value" :label="item.label" :value="item.value" v-for="item in submitFormOption.orderResources"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="订单类型" prop="ddlx" :rules="[{ required: true, message: '请选择订单类型', trigger: 'blur' }]">
                            <el-select v-model="submitForm.ddlx" placeholder="请选择订单类型" style="width:200px">
                                <el-option :key="item.value" :label="item.label" :value="item.value" v-for="item in submitFormOption.orderTypes"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="会员" class="order-vip-form-item" :rules="[{ required: true, message: '请选择会员', trigger: 'blur' }]">
                            <el-input placeholder="请选择会员" v-model="submitForm.hy" disabled style="width:200px">
                                <template slot="append">
                                    <el-button>选择</el-button>
                                </template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
        </el-card>
        <el-card shadow="never" class="order-card order-about">
            <div slot="header" class="order-card__header">
                <h3>订单相关信息</h3>
            </div>
            <el-row class="order-about__content">
                <el-col class="order-about__content__left" :span="12">
                    <el-form-item label="下单账号：" prop="xdzh">
                        <el-col :span="12">sasasa</el-col>
                        <el-col :span="12">
                            <el-button type="text">编辑地址<i class="el-icon-edit"></i></el-button>
                            <el-button type="text">更多地址<i class="el-icon-arrow-right"></i></el-button>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="收货人：" prop="shr">
                        庄晨
                    </el-form-item>
                    <el-form-item label="收货地址：" prop="shdz">
                        上海市浦东新区****路100号101室
                    </el-form-item>
                    <el-form-item label="手机号：" prop="sjh">
                        176*****629
                    </el-form-item>
                    <el-form-item label="买家备注：" prop="mjbz">
                        <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="submitForm.mjbz">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col class="order-about__content__right" :span="12">
                    <el-form-item label="付款类型：" prop="fklx" :rules="[{ required: true, message: '请选择付款类型', trigger: 'blur' }]">
                        <el-select v-model="submitForm.fklx" style="width:200px" placeholder="请选择付款类型">
                            <el-option :key="item.value" :label="item.label" :value="item.value" v-for="item in submitFormOption.payTypes"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="配送方式：" prop="psfs" :rules="[{ required: true, message: '请选择配送方式', trigger: 'blur' }]">
                        <el-select v-model="submitForm.psfs" style="width:200px" placeholder="请选择配送方式">
                            <el-option :key="item.value" :label="item.label" :value="item.value" v-for="item in submitFormOption.deliveryTypes"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="预计发货日期：" prop="yjfhrq" :rules="[{ required: true, message: '请选择预计发货日期', trigger: 'blur' }]">
                        <el-date-picker v-model="submitForm.yjfhrq" type="date" style="width:200px" placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="附件：" prop="yjfhrq">
                        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :file-list="fileList">
                            <el-button size="small" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                        </el-upload>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-card>
        <el-card class="order-card order-product" shadow="never">
            <div slot="header" class="order-card__header">
                <h3>下单商品</h3>
            </div>
            <div class="order-about__content">
                <div class="order-about__content__btns">
                    <el-button type="primary">添加商品</el-button>
                    <el-button type="primary">批量添加</el-button>
                    <el-button type="primary">导出模版</el-button>
                    <el-button type="primary">批量删除</el-button>
                </div>
                <div class="order-about__content__table">
                    <el-table :data="submitForm.xdsp" size="mini" border ref="table" style="width: 100%;">
                        <el-table-column type="selection" width="55">
                        </el-table-column>
                        <el-table-column type="index" width="85" header-align="center" align="center" label="商品名称">
                        </el-table-column>
                        <el-table-column prop="carrierName" header-align="center" align="center" label="商品编码" min-width="100">
                        </el-table-column>
                        <el-table-column prop="carNumber" header-align="center" align="center" label="规格属性" min-width="100">
                        </el-table-column>
                        <el-table-column prop="driver" header-align="center" align="center" label="计量单位">
                        </el-table-column>
                        <el-table-column prop="deliveryMan" header-align="center" align="center" label="可售库存">
                        </el-table-column>
                        <el-table-column prop="num" header-align="center" align="center" label="购买数量">
                            <template slot-scope="scope">
                                <el-form-item label-width="0px" style="marginBottom:0px;" :prop="'xdsp.' + scope.$index + '.num'" :rules="[{
                                    required: true, validator: validatorNum(scope.row), trigger: 'blur'}]">
                                    <el-input v-model="scope.row.num"></el-input>
                                </el-form-item>
                            </template>
                        </el-table-column>
                        <el-table-column prop="phone" header-align="center" align="center" label="销售单价">
                        </el-table-column>
                        <el-table-column prop="carTypeName" header-align="center" align="center" label="购买单价">
                            <template slot-scope="scope">
                                <el-form-item label-width="0px" style="marginBottom:0px;" :prop="'xdsp.' + scope.$index + '.num'" :rules="[{
                                    required: true, validator: validatorNum(scope.row), trigger: 'blur'}]">
                                    <el-input v-model="scope.row.num">
                                        <span slot="prefix">
                                            ¥
                                        </span>
                                    </el-input>
                                </el-form-item>
                            </template>
                        </el-table-column>
                        <el-table-column prop="creator" header-align="center" align="center" label="购买金额">
                        </el-table-column>
                        <el-table-column prop="modifier" header-align="center" align="center" label="备注">
                        </el-table-column>
                        <el-table-column prop="modifier" header-align="center" align="center" label="操作">
                        </el-table-column>
                    </el-table>
                </div>
                <div class="order-about__content__sum">
                    <el-row>
                        <el-col :span="12">
                            <h3>合计</h3>
                        </el-col>
                        <el-col :span="6">
                            数量：90
                        </el-col>
                        <el-col :span="6">
                            金额：¥80:00
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                        </el-col>
                        <el-col :span="6">
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="运费：" prop="yf" label-width="auto">
                                <el-input placeholder="请输入运费" v-model="submitForm.yf" style="width:180px">
                                    <span slot="prefix">
                                        ¥
                                    </span>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                        </el-col>
                        <el-col :span="6">
                            <el-button>取消</el-button>
                            <el-button type="primary">提交订单</el-button>
                        </el-col>
                        <el-col :span="6">
                            总计：¥80:00
                        </el-col>
                    </el-row>

                </div>
            </div>
        </el-card>
    </el-form>
</div>
    `
    });
});
