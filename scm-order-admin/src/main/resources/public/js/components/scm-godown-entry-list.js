lyfdefine(['scm-godown-entry-detail', 'validate'],
    function (returnDetailList, scmGodownEntryActual) {
        return ({
                data: function () {
                    return {
                        show: false,
                        orderTransInfoVisible: false,
                        POOL_NAME: '/scm-order-admin',
                        tranSts: [],
                        dataForm: {
                            id: '', //id
                            recordType: "",   // 单据类型
                            recordCode: "", // 单据编号
                            orderStatus: '', //单据状态 1待入库 2已入库
                            realWarehouseId: '',      //实仓id
                            afterSaleCode: '', //售后单号
                            saleCode: '', //销售单号
                            customName: '', //客户名称
                            customMobile: '', //客户手机号
                            reason: '', //退货原因
                            expressNo: "",   // 快递单号
                            createTime: "", // 创建时间
                            updateTime: '', //更新时间
                            startTime: '', //开始时间
                            endTime: '', //结束时间
                            pageSize: 10,
                            pageIndex: 1

                        },
                        dataList: [],
                        dateTimeRange: [new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000), new Date()],
                        pageIndex: 1,
                        pageSize: 10,
                        totalPage: 0,
                        dataListLoading: false,
                        dataListSelections: [],
                        returnDetailListVisible: false,
                        scmGodownEntryActualVisible: false,
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

                    }
                },
                components: {
                    'returnDetailList': returnDetailList,
                    'scmGodownEntryActual': scmGodownEntryActual

                },
                activated: function () {
                    this.getDataList()
                },
                mounted: function () {
                    this.getDataList()
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
                        var hour = today.getHours();
                        var minutes = today.getMinutes();
                        var second = today.getSeconds();
                        return tYear + "-" + tMonth + "-" + tDate + " " + hour + ":" + minutes + ":" + second;
                    },
                    doHandleMonth(month) {
                        var m = month;
                        if (month.toString().length == 1) {
                            m = "0" + month;
                        }
                        return m;
                    },

                    // 获取数据列表
                    getDataList() {
                        var _this = this;
                        if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                            //构建时间查询参数
                            _this.dataForm.startTime = _this.dateTimeRange[0];
                            _this.dataForm.endTime = _this.dateTimeRange[1];
                        } else {
                            _this.dataForm.startTime = null;
                            _this.dataForm.endTime = null;
                        }
                        if (_this.datePayTimeRange && _this.datePayTimeRange.length > 1) {
                            _this.dataForm.startPayTime = _this.datePayTimeRange[0];
                            _this.dataForm.endPayTime = _this.datePayTimeRange[1];
                        } else {
                            _this.dataForm.startPayTime = null;
                            _this.dataForm.endPayTime = null;
                        }
                        if (this.dataForm.startTime == '' && this.dataForm.endTime == '') {
                            this.dataForm.startTime = new Date(this.getDayDate(-30));
                            this.dataForm.endTime = new Date();
                        }
                        this.dataListLoading = true
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/orderReturn/queryOrderReturnPageByCondition',
                            method: 'post',
                            data: {
                                'pageIndex': this.pageIndex,
                                'pageSize': this.pageSize,
                                'afterSaleCode': this.dataForm.afterSaleCode,
                                'recordCode': this.dataForm.recordCode,
                                'customName': this.dataForm.customName,
                                'expressNo': this.dataForm.expressNo,
                                'orderStatus': this.dataForm.orderStatus,
                                'startTime': this.dataForm.startTime,
                                'endTime': this.dataForm.endTime
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
                                if (data.data.list == null || data.data.list.length == 0) {
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
                        }).catch(result => {
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因：" + _data.msg,
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

                    },
                    refreshDataList() {
                        this.getDataList()
                    },

                    // 每页数
                    sizeChangeHandle(val) {
                        this.pageSize = val
                        this.pageIndex = 1
                        this.getDataList()
                    },
                    //重置
                    reset(dataForm) {
                        this.dataForm = {
                            recordType: "",   // 单据类型
                            recordCode: "", // 单据编号
                            orderStatus: '', //单据状态
                            realWarehouseId: '',      //实仓id
                            afterSaleCode: '', //售后单号
                            saleCode: '', //销售单号
                            customName: '', //客户名称
                            customMobile: '', //客户手机号
                            reason: '', //退货原因
                            expressNo: "",   // 快递单号
                            createTime: "", // 创建时间
                            updateTime: '', //更新时间
                            startTime: new Date(this.getDayDate(-30)), //开始时间
                            endTime: new Date(), //结束时间
                            pageSize: 10,
                            pageIndex: 1,

                        };
                        this.dateTimeRange = [new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000), new Date()];
                    },
                    // 当前页
                    currentChangeHandle(val) {
                        this.pageIndex = val
                        this.getDataList()
                    },
                    // 多选
                    selectionChangeHandle(val) {
                        this.dataListSelections = val
                    },

                    getIndex: function getIndex(index) {

                        return this.dataForm.pageSize * (this.dataForm.pageIndex - 1) + index + 1;
                    },


                    returnDetail(afterSaleCode) {
                        console.log("---->" + afterSaleCode);
                        this.returnDetailListVisible = true
                        this.$nextTick(() => {
                            this.$refs.returnDetailList.init(afterSaleCode)
                        })
                    },

                    scmGodownEntryActual(recordCode) {
                        this.scmGodownEntryActualVisible = true
                        this.$nextTick(() => {
                            this.$refs.scmGodownEntryActual.init(recordCode)
                        })
                    },


                    //导出渠道列表
                    exportData() {
                        let exportUrl = '';
                        if (window.sessionStorage.getItem("menuList") === null) {
                            exportUrl = "/order/v1/orderReturn/queryOrderReturnDetailPageByAfterSaleCode";
                        } else {
                            exportUrl = this.POOL_NAME + "/order/v1/orderReturn/queryOrderReturnDetailPageByAfterSaleCode";
                        }
                        exportUrl = exportUrl + '?mercCd=' + this.dataForm.mercCd
                            + '&mercName=' + this.dataForm.mercName + '&orderNo=' + this.dataForm.orderNo
                            + '&outOrderNo=' + this.dataForm.outOrderNo + '&agentCd=' + this.dataForm.agentCd
                            + '&agentName=' + this.dataForm.agentName + '&tranSts=' + this.dataForm.tranSts
                            + '&startDate=' + this.dataForm.startDate + '&endDate=' + this.dataForm.endDate

                        window.open(exportUrl)
                    },


                    // 以下为导出excel
                    exportExcelList: function exportExcelList() {
                        let _this = this;
                        if (_this.dateTimeRange && _this.dateTimeRange.length > 1) {
                            _this.dataForm.startTime = _this.dateTimeRange[0];
                            _this.dataForm.endTime = _this.dateTimeRange[1];
                        } else {
                            _this.dataForm.startTime = null;
                            _this.dataForm.endTime = null;
                        }
                        let star = _this.dataForm.startTime;
                        if (star == null || null == _this.dataForm.endTime) {
                            _this.$message({
                                message: '请选择开始时间'
                            });
                            return;
                        }
                        // if(this.dataForm.startTime==''&&this.dataForm.endTime==''){
                        //     this.dataForm.startTime=  new Date(this.getDayDate(-30));
                        //     this.dataForm.endTime= new Date();
                        // }

                        _this.loading = true;
                        this.$http({
                            method: "post",
                            url: "/scm-order-admin/order/v1/orderReturn/exportOrderReturn",
                            data: _this.dataForm,
                            responseType: 'blob'
                        }).then(function (res) {
                            if (res.headers && (res.headers['content-type'] === 'application/vnd.ms-excel;charset=utf8' || res.headers['content-type'] === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                                const elementA = document.createElement('a');
                                const disposition = res.headers['content-disposition'];
                                const contentMsg = res.headers['content-msg'];
                                const contentOtherMsg = res.headers['content-other-msg'];
                                const fileName = decodeURIComponent(disposition.substring(disposition.indexOf('filename=') + 9, disposition.length));
                                elementA.download = `${fileName}`;
                                const blob = new Blob([res.data])
                                elementA.href = window.URL.createObjectURL(blob);
                                elementA.dispatchEvent(new MouseEvent('click'));
                                _this.loading = false
                            }
                        }).catch(function (error) {
                            _this.loading = false
                            console.log(error);
                        });
                    },


                    downloadFile(fileName, content) {
                        let aLink = document.createElement('a');
                        let blob = this.base64ToBlob(content); //new Blob([content]);
                        let evt = document.createEvent("HTMLEvents");
                        evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
                        aLink.download = fileName;
                        aLink.href = URL.createObjectURL(blob);
                        aLink.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));//兼容火狐
                    }, base64ToBlob(code) {
                        let parts = code.split(';base64,');
                        let contentType = parts[0].split(':')[1];
                        let raw = window.atob(parts[1]);
                        let rawLength = raw.length;
                        let uInt8Array = new Uint8Array(rawLength);
                        for (let i = 0; i < rawLength; ++i) {
                            uInt8Array[i] = raw.charCodeAt(i);
                        }
                        return new Blob([uInt8Array], {type: contentType});
                    },


                    tranStsFormat(row, column, cellValue) {
                        let obj = {};
                        obj = this.tranSts.find((item) => {
                            return item.dicKey == cellValue;
                        });
                        if (obj == undefined) {
                            return cellValue;
                        }
                        return obj.dicValue;
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

                    //时间转换
                    formatStrDate(row, column, cellValue) {
                        let realDate;
                        if (cellValue) {
                            if (cellValue === undefined) {
                                return "";
                            }
                            // yyyy-MM-dd hh:mm:ss
                            if (cellValue.length >= 4) {
                                let Y = cellValue.substring(0, 4);
                                realDate = Y;
                            }
                            if (cellValue.length >= 6) {
                                let M = cellValue.substring(4, 6);
                                realDate = realDate + "-" + M;
                            }
                            if (cellValue.length >= 8) {
                                let d = cellValue.substring(6, 8);
                                realDate = realDate + "-" + d;
                            }
                            if (cellValue.length >= 10) {
                                let h = cellValue.substring(8, 10);
                                realDate = realDate + "  " + h;
                            }
                            if (cellValue.length >= 12) {
                                let m = cellValue.substring(10, 12);
                                realDate = realDate + ":" + m;
                            }
                            if (cellValue.length >= 14) {
                                let s = cellValue.substring(12, 14);
                                realDate = realDate + ":" + s;
                            }

                            return realDate;
                        } else {
                            realDate = "";
                        }
                        return realDate;
                    },

                    // 分转元
                    regFenToYuan(row, column, cellValue) {
                        let num = cellValue;
                        num = cellValue * 0.01;
                        num += '';
                        let reg = num.indexOf('.') > -1 ? /(\d{1,3})(?=(?:\d{3})+\.)/g : /(\d{1,3})(?=(?:\d{3})+$)/g;
                        num = num.replace(reg, '$1');
                        num = this.toDecimal2(num)
                        return num
                    },

                    toDecimal2(x) {
                        let f = parseFloat(x);
                        if (isNaN(f)) {
                            return false;
                        }
                        f = Math.round(x * 100) / 100;
                        let s = f.toString();
                        let rs = s.indexOf('.');
                        if (rs < 0) {
                            rs = s.length;
                            s += '.';
                        }
                        while (s.length <= rs + 2) {
                            s += '0';
                        }
                        return s;
                    },

                },
                template: `

    <div class="mod-config">
        <el-card class="box-card search-input" shadow="never">
            
            <el-form  label-width="120px" size="mini" :model="dataForm" @keyup.enter.native="getDataList()">
                
              
                <el-col :span="6">
                    <el-form-item label="售后单号：">
                        <el-input v-model="dataForm.afterSaleCode" clearable></el-input>
                    </el-form-item>
                </el-col>
               
                <el-col :span="6">
                    <el-form-item label="团购入库单号：">
                        <el-input v-model="dataForm.returnEntryCode" clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="所属客户：">
                        <el-input v-model="dataForm.customName" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                 <el-col :span="6">
                    <el-form-item label="快递单号：">
                        <el-input v-model="dataForm.expressNo" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                <el-col :span="7">
                   <el-form-item label="团购入库单状态:">
                     <el-select v-model="dataForm.orderStatus" clearable>
                          <el-option label="全部" value=""></el-option>
                          <el-option label="待入库" value="1"></el-option>
                           <el-option label="已入库" value="2"></el-option>
                          </el-select>
                    </el-form-item>
               </el-col>
                <el-col :span="5">
                <el-form-item label="创建时间：" prop="dateTimeRange">
            <el-date-picker
              value-format='yyyy-MM-dd HH:mm:ss'
              :default-time="['00:00:00', '23:59:59']"
              v-model="dataForm.dateTimeRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right">
            </el-date-picker>
         </el-form-item>
<!--                       <el-form-item label="创建时间：">-->
<!--                           <el-date-picker value-format='yyyy-MM-dd HH:mm:ss' :default-time="['00:00:00', '23:59:59']"  v-model="dataForm.startTime" type="datetime" placeholder="选择日期" clearable></el-date-picker>-->
<!--                       </el-form-item>-->
<!--                   </el-col>-->
<!--                  <el-col :span="5">-->
<!--                       <el-form-item label="至：">-->
<!--                            <el-date-picker value-format='yyyy-MM-dd HH:mm:ss' :default-time="['00:00:00', '23:59:59']" v-model="dataForm.endTime" type="datetime" placeholder="选择日期" clearable></el-date-picker>-->
<!--                       </el-form-item>-->
                  </el-col>
               <el-col :span="8" :offset="6">
                   <el-form-item>
                        <el-button type="primary" @click="getDataList()">查询</el-button>
                        <el-button @click="reset('dataForm')">重置</el-button>
                        <el-button @click="exportExcelList()">导出</el-button>
                    </el-form-item>
               </el-col>
            </el-form>
        </el-card>
        <el-card class="box-card" shadow="never">
            <el-table
                    :data="dataList"
                    size="mini"
                    border
                    v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle"
                    style="width: 100%;">
                    <el-table-column
                        label="序号"
                        type="index"
                        align="center">
                        <template scope="scope">
                            <span>{{getIndex(scope.$index)}}</span>
                        </template>
                    </el-table-column>
                   
                   
                   <el-table-column
                            prop="afterSaleCode"
                            header-align="center"
                            align="center"
                            width="150"
                            label="售后单号">
                    </el-table-column>
                   
                    <el-table-column
                            prop="orderCode"
                            header-align="center"
                            width="150"
                            align="center"
                            label="预约单号">
                    </el-table-column>
                    <el-table-column
                            prop="returnEntryCode"
                            header-align="center"
                            align="center"
                            label="团购入库单号">
                    </el-table-column>
                    <!--<el-table-column-->
                            <!--prop="tranOrderNo"-->
                            <!--header-align="center"-->
                            <!--align="center"-->
                            <!--label="支付订单号">-->
                    <!--</el-table-column>-->
                    <el-table-column
                            prop="customName"
                            header-align="center"
                            align="center"
                            label="所属客户">
                    </el-table-column>
                    <el-table-column
                        prop="orderStatusDesc"
                        header-align="center"
                        align="center"
                        width="70"
                        :formatter="tranStsFormat"
                        label="订单状态">
                    </el-table-column>
                    <el-table-column
                        prop="realWarehouseName"
                        header-align="center"
                        width="80"
                        align="center"
                        label="收货仓库">
                    </el-table-column>
                    <!--<el-table-column-->
                        <!--prop="refundAmt"-->
                        <!--header-align="center"-->
                        <!--align="center"-->
                        <!--:formatter="regFenToYuan"-->
                        <!--label="已退款金额">-->
                    <!--</el-table-column>-->
                    <!--<el-table-column-->
                        <!--prop="mercName"-->
                        <!--header-align="center"-->
                        <!--align="center"-->
                        <!--label="商户名称">-->
                    <!--</el-table-column>-->
                    <el-table-column
                        prop="reason"
                        header-align="center"
                        align="center"
                        label="退货原因">
                    </el-table-column>
                    
                    <el-table-column
                        prop="expressNo"
                        header-align="center"
                        align="center"
                        label="快递单号">
                    </el-table-column>
                   
                    <el-table-column
                        prop="createTime"
                        header-align="center"
                        align="center"
                        width="140"
                        :formatter="formatDate"
                        label="创建时间">
                    </el-table-column>
                     <el-table-column
                        prop="updateTime"
                        header-align="center"
                        align="center"
                        width="140"
                        :formatter="formatDate"
                        label="更新时间">
                    </el-table-column>

                    <el-table-column
                        fixed="right"
                        header-align="center"
                        align="center"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="returnDetail(scope.row.afterSaleCode)">查看详情</el-button>
                    </template>
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

        <!-- 弹窗, 详情 -->
             <returnDetailList v-if="returnDetailListVisible" ref="returnDetailList" @refreshDataList="refreshDataList"></returnDetailList>
             <scmGodownEntryActual v-if="scmGodownEntryActualVisible" ref="scmGodownEntryActual" @refreshDataList="refreshDataList"></scmGodownEntryActual>
             
             
    </div>
    `
            }
        );
    });