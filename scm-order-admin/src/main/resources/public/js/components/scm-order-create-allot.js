lyfdefine(
    function () {
        return ({
                data: function () {
                    return {
                        visible: false,
                        POOL_NAME: '/scm-order-admin',
                        tranSts: [],
                        terminalType: [],
                        dataForm: {
                            skuCode:"",   // 商品编码
                            skuName:"", // 商品名称
                            orderQty:"",   // 下单数量
                            requireQty:"", // 需求锁定数量
                            hasLockQty:"",   // 已锁定数量
                            vmStockQty:"", // 虚仓库存数量
                            needMoveQty:"",   // 需要转移数量
                            moveQty:"", // 商品名称
                            unit:"", // 基本单位
                            unitCode:"", // 单位编码
                            realWarehouseCode:"", // 实仓Code
                            joinVmWarehouseCode:"", // 调入编码
                        },
                        dataList: [
                        ],
                        orderDetailAndAllotDTO: {
                            realWarehouseCode:"",   // 实仓Code
                            realWarehouseName:"",   // 实仓Code
                            factoryCode:"",   // 工厂编号
                            needPackage:"",   // 是否需要包装 0:不需要 1:需要


                        },
                        warehouseInCode:[
                        ],
                        orderCode:"",
                        allotLoading:false
                    }
                },
                activated: function () {
                },
                mounted: function () {
                },
                methods: {
                    // 获取数据列表
                    init (orderCode) {
                        this.visible = true
                        this.orderCode = orderCode;
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/realWarehouse/allocation/queryCreateAllot?orderCode='+orderCode,
                            method: 'get',
                            data: {
                                'orderCode': orderCode,
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                        if (data && data.code === '0') {
                            this.dataList = data.data.list;
                            this.orderDetailAndAllotDTO = data.data;
                            this.warehouseInCode = data.data.warehouseList;
                            if(this.warehouseInCode && this.warehouseInCode.length>0){
                                this.dataForm.joinVmWarehouseCode=this.warehouseInCode[0].realWarehouseOutCode;
                            }
                        } else {
                            this.dataList = [];
                            this.$message({
                                message: "操作失败，原因："+data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
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

                    //确认调拨
                    dataFormSubmit : function () {
                        let _this=this;
                        if(_this.dataForm.joinVmWarehouseCode =='' ||_this.dataForm.joinVmWarehouseCode ==null){
                            _this.$message({
                                message: '调入仓不能为空'
                            });
                            return;
                        }
                        if(this.orderCode==null || this.orderCode==''){
                            _this.$message({
                                message: '预约单号不能为空'
                            });
                            return;
                        }
                        _this.allotLoading=true;
                        this.$http({
                            url: this.POOL_NAME + `/order/v1/realWarehouse/allocation/realWarehouseAllocation?orderCode=`+this.orderCode+"&realWarehouseCode="+this.dataForm.joinVmWarehouseCode,
                            method: 'get',
                            headers:{
                                'Content-Type':'application/json'
                            }
                        }).then(result =>  {
                            let _data = result.data;
                        if (_data && _data.code == 0) {
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 2000,
                                onClose: () => {
                                this.visible = false,
                                this.$emit('refreshDataList');
                                _this.allotLoading=false;
                            }
                        });
                        }else {
                            _this.allotLoading=false;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                        }
                    }).catch(result=> {
                            _this.allotLoading=false;
                            let _data = result.response.data;
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                            });
                    }).catch(() => {
                            _this.allotLoading=false;
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试'
                            });
                    	});
                    },

                    tranStsFormat (row, column, cellValue){
                        let obj = {};
                        obj = this.tranSts.find((item)=>{
                            return item.dicKey == cellValue;
                    });
                        if (obj == undefined){
                            return cellValue;
                        }
                        return obj.dicValue;
                    },

                    terminalTypeFormat (row, column, cellValue){
                        let obj = {};
                        obj = this.terminalType.find((item)=>{
                            return item.dicKey == cellValue;
                    });
                        if (obj == undefined){
                            return cellValue;
                        }
                        return obj.dicValue;
                    },

                    //时间转换
                    formatDate(row, column, cellValue) {
                        var realDate;
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
                            if (cellValue.length >=4){
                                let Y = cellValue.substring(0,4);
                                realDate = Y;
                            }
                            if (cellValue.length >=6){
                                let M = cellValue.substring(4,6);
                                realDate = realDate+"-"+M;
                            }
                            if (cellValue.length >=8){
                                let d = cellValue.substring(6,8);
                                realDate = realDate+"-"+d;
                            }
                            if (cellValue.length >=10){
                                let h = cellValue.substring(8,10);
                                realDate = realDate+"  "+h;
                            }
                            if (cellValue.length >=12){
                                let m = cellValue.substring(10,12);
                                realDate = realDate+":"+m;
                            }
                            if (cellValue.length >=14){
                                let s = cellValue.substring(12,14);
                                realDate = realDate+":"+s;
                            }

                            return realDate;
                        } else {
                            realDate = "";
                        }
                        return realDate;
                    },

                    // 分转元
                    regFenToYuan (row, column, cellValue) {
                        let num = cellValue;
                        num=cellValue*0.01;
                        num+='';
                        let reg = num.indexOf('.') >-1 ? /(\d{1,3})(?=(?:\d{3})+\.)/g : /(\d{1,3})(?=(?:\d{3})+$)/g;
                        num=num.replace(reg,'$1');
                        num = this.toDecimal2(num)
                        return num
                    },

                    toDecimal2 (x){
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
<el-dialog
    width="85%"
    height="90%"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
        
 
    
        <el-card class="box-card" shadow="never">
        <div slot="header">
                <span >生成调拨单</span>
            </div>
         <el-form  label-width="130px" size="mini" :model="dataForm" @keyup.enter.native="getDataList()">

               
               <el-col :span="12">
                        <el-form-item label="调出仓库：" prop="realWarehouseCode">
                            <span >{{orderDetailAndAllotDTO.factoryCode}}-{{orderDetailAndAllotDTO.realWarehouseCode}} {{orderDetailAndAllotDTO.realWarehouseName}}  </span>
                        </el-form-item>
                    </el-col>
                    
                    
                          <el-col :span="12">
                        <el-form-item label="调入仓库：" >
                            <el-select clearable  filterable v-model="dataForm.joinVmWarehouseCode" placeholder="仓库编号/名称"">
                                        <el-option v-for="e in warehouseInCode" :key="e.realWarehouseCode" :label="e.realWarehouseCode+'-'+e.realWarehouseName" :value="e.realWarehouseOutCode" >
                                        </el-option>
                                    </el-select>
                        </el-form-item>
                    </el-col>
                    
               

            <el-table
                    :data="dataList"
                    size="mini"
                    border
                    style="width: 100%;">
                    <el-table-column
                            prop="skuCode"
                            header-align="center"
                            align="center"
                            label="商品编码">
                    </el-table-column>
                    <el-table-column
                            prop="skuName"
                            header-align="center"
                            align="center"
                            label="商品名称">
                    </el-table-column>
                    <el-table-column
                            prop="unit"
                            header-align="center"
                            align="center"
                            label="基本单位">
                    </el-table-column>
                     
                     <el-table-column
                            prop="orderQty"
                            header-align="center"
                            align="center"
                            label="需求数量">
                    </el-table-column>
                     <el-table-column
                            prop="requireQty"
                            header-align="center"
                            align="center"
                            label="需求锁定数量">
                    </el-table-column>
                    <el-table-column
                            prop="hasLockQty"
                            header-align="center"
                            align="center"
                            label="需要调拨数量">
                    </el-table-column>
                    
            </el-table>
            
             </el-form>
        </el-card>
    </div>
     <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"  :loading="allotLoading"  @click="dataFormSubmit()">确认调拨</el-button>
    </span>
    </el-dialog>
    `
            }
        );
    });