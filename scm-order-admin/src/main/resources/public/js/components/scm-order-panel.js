lyfdefine(['style/scm-order-style'],function(style){
    return ({
        name: 'scm-order-panel',
        data() {
            return {
                getStyle(index) {
                    const style = {
                        left: 100 / (5) * index + '%',
                    }
                    return style
                },
                warnningFilterForm: {
                    date: this.getCurrentDate(),
                    channel: ''
                },
                stepFilterForm: {
                    date: this.getCurrentDate()
                },
                chartFilterForm: {
                    date: [this.getCurrentDate(-30),this.getCurrentDate()],
                },
                filterFormOption: {
                    channels: [{
                        value: 1,
                        label: '直营'
                    },
                        {
                            value: 2,
                            label: '加盟'
                        },
                        {
                            value: 3,
                            label: '销售中心'
                        },
                        {
                            value: 4,
                            label: '云商'
                        },
                        {
                            value: 5,
                            label: 'APP'
                        }
                    ]
                },
                steps: [{
                    name: '发货单',
                    num: 100,
                    bgc: '#4150d8'
                },
                    {
                        name: '待下发仓库',
                        num: 100,
                        bgc: '#28bf7e'
                    },
                    {
                        name: '仓库作业中',
                        num: 1010,
                        bgc: '#ed7c2f'
                    },
                    {
                        name: '仓库已发货',
                        num: 100,
                        bgc: '#f2a93b'
                    },
                    {
                        name: '已揽件',
                        num: 100,
                        bgc: '#f9cf36'
                    },
                    {
                        name: '客户已签收',
                        num: 100,
                        bgc: '#4a5bdc'
                    }
                ]
            }
        },
        methods: {
            getCurrentDate(day) {
                const date = new Date().getTime() + ((day ? day : 0) * 24 * 60 * 60 * 1000);
                const nowDate = new Date(date)
                const y = nowDate.getFullYear()
                const m = nowDate.getMonth() + 1
                const d = nowDate.getDate()
                const yy = y
                const MM = m < 10 ? '0' + m : m
                const dd = d < 10 ? '0' + d : d
                return yy + '-' + MM + '-' + dd
            },
            handleChangeChannel(e) {}
        },
        mounted() {
            const echartEl = document.getElementById('scm-order-pannel-echart')
            const echartInstance = window.echarts.init(echartEl)
            let xdata = []
            for(let i=0;i<30;i++){
                xdata.push(this.getCurrentDate(-i))
            }
            const option = {
                title: {
                    text: '全渠道发货订单数',
                    textAlign: 'center',
                    left: '50%'
                },
                grid: {},
                legend: {
                    top: '30px',
                    data: ['直营店', '加盟店', '销售中心', '云商', 'APP']
                },
                xAxis: {
                    data: xdata
                },
                yAxis: {},
                series: [{
                    name: '直营店',
                    type: 'line',
                    data: [5, 20, 36, 10, 10, 20,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,11,1,1,1,1,]
                },
                    {
                        name: '加盟店',
                        type: 'line',
                        data: [5, 201, 36, 10, 10, 20]
                    },
                    {
                        name: '销售中心',
                        type: 'line',
                        data: [5, 20, 36, 109, 10, 20]
                    },
                    {
                        name: '云商',
                        type: 'line',
                        data: [5, 20, 36, 100, 10, 20]
                    },
                    {
                        name: 'APP',
                        type: 'line',
                        data: [5, 20, 36, 10, 10, 250]
                    }
                ]
            };
            echartInstance.setOption(option);
        },
        template: `
<div data-scm-order class="scm-order-panel">
    <el-card class="panel-card panel-card-warnning">
        <div slot="header" class="panel-card__header">
            <span>全渠道订单预警信息</span>
        </div>
        <div class="panel-card-warnning__body panel-card__body">
            <div class="header">
                <el-row type="flex" align="middle">
                    <el-col :span="8">
                        <el-row type="flex" align="middle" :gutter="5">
                            <el-col :span="2">
                                <span>日期</span>
                            </el-col>
                            <el-col :span="10">
                                <el-date-picker :clearable="false" value-format="yyyy-MM-dd" v-model="warnningFilterForm.date" type="date" placeholder="选择日期" style="width:100%">
                                </el-date-picker>
                            </el-col>
                            <el-col :span="2">
                                <span>渠道</span>
                            </el-col>
                            <el-col :span="8">
                                <el-select v-model="warnningFilterForm.channel">
                                    <el-option v-for="item in filterFormOption.channels" :key="item.value" :value="item.value" :label="item.label"></el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="8" class="sub-title">{{warnningFilterForm.date}}全渠道发货单预警信息</el-col>
                </el-row>
            </div>
            <div class="content">
                <el-row type="flex" justify="cneter" align="middle">
                    <el-col :span="12">
                        <div class="item">
                            <span class="item__title">订单预警:</span>
                            <span class="item__num">100</span>
                        </div>
                        <div class="item">
                            <span class="item__title">缺货订单:</span>
                            <span class="item__num">100</span>
                        </div>
                        <div class="item">
                            <span class="item__title">待人工处理订单:</span>
                            <span class="item__num">100</span>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="item">
                            <span class="item__title">滞留/未及时发货订单:</span>
                            <span class="item__num">100</span>
                        </div>
                        <div class="item">
                            <span class="item__title">24小时未发货订单:</span>
                            <span class="item__num">100</span>
                        </div>
                        <div class="item">
                            <span class="item__title">48小时未发货订单:</span>
                            <span class="item__num">100</span>
                        </div>
                        <div class="item">
                            <span class="item__title">72小时以上未发货:</span>
                            <span class="item__num">100</span>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>
    </el-card>
    <el-card class="panel-card panel-card-steps">
        <div slot="header" class="panel-card__header">
            <span>发货订单全链路图</span>
        </div>
        <div class="panel-card-steps__body panel-card__body">
            <div class="header">
                <el-row type="flex" align="middle">
                    <el-col :span="8">
                        <el-row type="flex" align="middle" :gutter="5">
                            <el-col :span="2">
                                <span>日期</span>
                            </el-col>
                            <el-col :span="10">
                                <el-date-picker :clearable="false" value-format="yyyy-MM-dd" v-model="stepFilterForm.date" type="date" placeholder="选择日期" style="width:100%">
                                </el-date-picker>
                            </el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="8" class="sub-title">{{stepFilterForm.date}}链路各环节订单分布</el-col>
                </el-row>
            </div>
            <div class="content">
                <div class="line">
                    <div class="line-bar"></div>
                    <div class="mark" v-for="(item, index) in steps" :key="item.name" :style="getStyle(index)">
                        <div class="circle" :style="{backgroundColor: item.bgc}">{{item.num}}</div>
                        <div class="des">{{item.name}}</div>
                    </div>
                </div>
            </div>
        </div>
    </el-card>
    <el-card class="panel-card panel-card-chart">
        <div slot="header" class="panel-card__header">
            <span>全渠道发货单数量</span>
        </div>
        <div class="panel-card-steps__body panel-card__body">
            <div class="header">
                <el-row type="flex" align="middle">
                    <el-col :span="8">
                        <el-row type="flex" align="middle" :gutter="5">
                            <el-col :span="2">
                                <span>日期</span>
                            </el-col>
                            <el-col :span="16">
                                <el-date-picker style="width:100%" :clearable="false" value-format="yyyy-MM-dd" v-model="chartFilterForm.date" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                                </el-date-picker>
                            </el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="8" class="sub-title">{{chartFilterForm.date[0]}}-{{chartFilterForm.date[1]}}全渠道发货单预警信息</el-col>
                </el-row>
            </div>
            <div class="content" id="scm-order-pannel-echart" style="height:500px">
            </div>
        </div>
    </el-card>
</div>
        `
    });
});
