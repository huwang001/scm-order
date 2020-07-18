lyfdefine(
    function () {
        return ({
                data: function () {
                    return {
                        visible: false,
                        show: false,
                        POOL_NAME: '/scm-order-admin',
                        orderStatusList: {},
                        dataForm: {
                            id: '', //id
                            orderCode:"",   // 预约单号
                            skuCode:"",   // 商品编码
                            skuName:"", // 商品名称
                            unit:"", // 基本单位
                            requireQty:"", // 下单数量
                            wareHouseQty:"", // 仓库数量
                            requireQty: '',      //需求锁定数量
                            hasLockQty: '', //实际锁定数量
                            deliverQty: '', //实际发货数量
                            allotQty: '', //调拨数量
                            lockStatus: '', //锁定状态
                            deliverWareName: '', //发货仓库

                        },
                        dataList: [
                        ],
                        orderDTO: {
                            orderCode:"",   // 预约单号
                            orderType: '', //单据类型 1:DIY订单 2:常规订单
                            orderStatus: '', //单据状态
                            customName: '', //所属客户
                            expectDate: '', //预计交货日期
                            expectDateStr: '', //预计交货日期
                            customAddress:"", // 送货地址
                            realWarehouseName:"", // 发货仓库
                        },
                        pageIndex: 1,
                        pageSize: 10,
                        totalPage: 0,
                        dataListLoading: false,
                        orderCode: '',
                        dataListSelections: []



                    }
                },

                activated: function () {
                    // this.init(this.orderCode)
                    this.getSignType()
                },
                mounted: function () {
                    // this.init(this.orderCode)
                    this.getSignType()
                },
                methods: {
                    // 获取当前时间，day为number，getDay(-1):昨天的日期;getDay(0):今天的日期;getDay(1):明天的日期;【以此类推】
                    getDayDate(day) {
                        var today = new Date();
                        var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
                        today.setTime(targetday_milliseconds); //注意，这行是关键代码

                        var tYear = today.getFullYear();
                        var tMonth = today.getMonth();
                        var tDate = today.getDate();
                        tMonth = this.doHandleMonth(tMonth + 1);
                        tDate = this.doHandleMonth(tDate);
                        return tYear + "-" + tMonth + "-" + tDate +" 00:00:00";
                    },
                    doHandleMonth(month) {
                        var m = month;
                        if (month.toString().length == 1) {
                            m = "0" + month;
                        }
                        return m;
                    },
                    // 获取数据列表
                    init(orderCode) {
                        if(orderCode==null||orderCode==''){
                            return;
                        }
                        this.visible = true
                        this.dataListLoading = false;
                        this.orderCode = orderCode;
                        if(this.dataForm.createTimeSt==null&&this.dataForm.createTimeEnd==null){
                            this.dataForm.createTimeSt=  new Date(this.getDayDate(-30));
                            this.dataForm.createTimeEnd= new Date();
                        }

                        this.$http({
                            url: this.POOL_NAME + '/order/v1/order/queryOrderDetail/'+orderCode+'?pageNum='+this.pageIndex+'&pageSize='+this.pageSize,
                            method: 'get',
                            data: {
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                        if (data && data.code === '0') {
                            this.dataList = data.data.list;
                            this.totalPage = data.data.total;
                            this.pageIndex = data.data.pageNum;
                            this.pageSize = data.data.pageSize;
                            if (data.data.list == null || data.data.list.length == 0){
                                this.pageIndex = 1;
                                this.pageSize = 10;
                            }
                        } else {
                            this.dataList = [];
                            this.totalPage = 0;
                            this.pageIndex = 1;
                            this.pageSize = 10;
                        }
                        this.dataListLoading = false
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                        this.dataListLoading = false
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                        this.dataListLoading = false
                    });



                        //获取预约单信息
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/order/findOrderByOrderCode/'+orderCode,
                            method: 'get',
                            data: {
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                        if (data && data.code === '0') {
                            this.orderDTO = data.data;
                        } else {
                            this.orderDTO = {};

                        }
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                    });

                    },


                    // 每页数
                    sizeChangeHandle(val) {
                        this.pageSize = val
                        this.pageIndex = 1
                        this.init(this.orderCode)
                    },

                    // 当前页
                    currentChangeHandle(val) {
                        this.pageIndex = val
                        this.init(this.orderCode)
                    },
                    // 多选
                    selectionChangeHandle(val) {
                        this.dataListSelections = val
                    },






                    // 获取字典信息
                    getDic(dicName) {
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/order/queryOrderStatusList',
                            method: 'post',
                            data: {
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let _data = result.data;
                        if (_data && _data.code == '0') {
                            this.orderStatusList = _data.data;
                        } else {
                            this.orderStatusList = []
                        }
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                    });
                    },

                    // 查询商户属性
                    getSignType(){
                        this.getDic('signType');
                    },


                    getDoRealWarehouseName (row, column, cellValue){
                        return this.orderDTO.realWarehouseName;
                    },

                    getOrderType (row, column, cellValue){
                        if(cellValue =='1'){
                            return "DIY订单";
                        }else if(cellValue =='2'){
                            return "常规订单";
                        }

                    },

                    //时间转换
                    formatDate(row, column, cellValue) {
                        let realDate;
                        if (cellValue) {
                            let date = new Date(cellValue);
                            let y = date.getFullYear();
                            let MM = date.getMonth() + 1;
                            MM = MM < 10 ? ('0' + MM) : MM;
                            let d = date.getDate();
                            d = d < 10 ? ('0' + d) : d;
                            let h = date.getHours();
                            h = h < 10 ? ('0' + h) : h;
                            let m = date.getMinutes();
                            m = m < 10 ? ('0' + m) : m;
                            let s = date.getSeconds();
                            s = s < 10 ? ('0' + s) : s;
                            return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
                        } else {
                            realDate = "";
                        }
                        return realDate;
                    },

                },
                template: `
<el-dialog
    width="85%"
    height="90%"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
        <el-card class="box-card search-input" shadow="never">
            <div slot="header">
                <span >预约单详情</span>
            </div>
            <el-form  label-width="120px" size="mini" :model="orderDTO">

                <el-col :span="7" >
                    <el-form-item label="预约单号：">
                        <el-input v-model="orderDTO.orderCode" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                 <el-col :span="6">
                    <el-form-item label="订单类型："disabled="disabled" size="mini" max-width="80px" clearable>
                    <el-input  v-model="orderDTO.orderType == 1? '普通订单' : '卡卷订单'" disabled="disabled" size="mini" max-width="80px"  clearable></el-input>
                    </el-form-item>
                     </el-col>

                 <el-col :span="6">
                    <el-form-item label="预约单状态：">
                    <el-select v-model="orderDTO.orderStatus" disabled="disabled" size="mini" max-width="80px" clearable>
                            <el-option
                              v-for="item in orderStatusList"
                              :key="item.orderStatusCode"
                              :label="item.orderStatusName"
                              :value="item.orderStatusCode">
                            </el-option>
                          </el-select>
                    </el-form-item>
                </el-col>
                
                 
                <el-col :span="5">
                    <el-form-item label="所属客户：">
                     <el-input v-model="orderDTO.customName" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
    
                 <el-col :span="7">
                    <el-form-item label="预计交货日期：">
                     <el-input v-model="orderDTO.expectDateStr"  disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
              
               <el-col :span="17">
                    <el-form-item label="送货地址：">
                     <el-input v-model="orderDTO.customAddress" disabled="disabled" size="mini" max-width="80px" clearable></el-input>

                    </el-form-item>
                </el-col>
            </el-form>
        </el-card>
        <el-card >
            <el-table
                    :data="dataList"
                    size="mini"
                    border
                    v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle"
                    style="width: 100%;">
                     <el-table-column
                            prop="orderCode"
                            header-align="center"
                            width="120"
                            align="center"
                            label="预约单号">
                    </el-table-column>
                    <el-table-column
                            prop="skuCode"
                            header-align="center"
                            align="center"
                            label="商品编码">
                    </el-table-column>
                    <el-table-column
                            prop="skuName"
                            header-align="center"
                            width="160"
                            align="center"
                            label="商品名称">
                    </el-table-column>
                    <el-table-column
                        prop="unit"
                        header-align="center"
                        width="65"
                        align="center"
                        label="基本单位">
                    </el-table-column>
                    <el-table-column
                            prop="orderQty"
                            header-align="center"
                            width="70"
                            align="center"
                            label="下单数量">
                    </el-table-column>
                    
<!--                       <el-table-column-->
<!--                            prop="wareHouseQty"-->
<!--                            header-align="center"-->
<!--                            align="center"-->

<!--                            label="仓库数量">-->
<!--                    </el-table-column>-->
                       <el-table-column
                            prop="requireQty"
                            header-align="center"
                            width="70"
                            align="center"

                            label="需求锁定数量">
                    </el-table-column>
                       <el-table-column
                            prop="hasLockQty"
                            header-align="center"
                            width="70"
                            align="center"
                            label="实际锁定数量">
                    </el-table-column>
                       <el-table-column
                            prop="requireQty"
                            header-align="center"
                            width="70"
                            align="center"
                            label="实际发货数量">
                    </el-table-column>  
<!--                     <el-table-column-->
<!--                            prop="allotQty"-->
<!--                            header-align="center"-->
<!--                            align="center"-->
<!--                            label="调拨数量">-->
<!--                    </el-table-column> -->
                    <el-table-column
                            prop="lockStatus"
                            header-align="center"
                            width="100"
                            align="center"
                            label="锁定状态">
                            <template slot-scope="scope">
                            <span v-if ="scope.row.lockStatus == 1">部分锁定</span>
                            <span v-if ="scope.row.lockStatus == 2">全部锁定</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="deliverWareName"
                            header-align="center"
                            align="center"
                             :formatter="getDoRealWarehouseName"
                            label="发货仓库">
                    </el-table-column>
                  
                    
            </el-table>
        </el-card>
        <el-pagination
                @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle"
                :current-page="pageIndex"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                :total="totalPage"
                layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

       
    </div>
        </el-dialog>
    `
            }
        );
    });