lyfdefine(
    function () {
        return ({
                data: function () {
                    return {
                        visible: false,
                        // payTransInfoVisible:false,
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
                        },
                        dataList: [
                        ],
                        pagedataList: [{
                            key: '',
                            dataList: []
                        }],
                        orderDTO: {
                            realWarehouseCode:"",   // 实仓Code
                            realWarehouseName:"",   // 实仓名称
                            joinVmWarehouseCode:"", // 调入虚仓编码
                            joinVmWarehouseName:"", // 调入虚仓名称
                            exitVmWarehouseCode:"",   // 调出虚仓编码
                        },
                        exitVmWarehouseCodeList:[
                        ],
                        orderCode:"",
                        pageIndex: 1,
                        pageSize: 10,
                        totalPage: 0,
                        dataListLoading: false,
                        recordCode: '',
                        dataListSelections: []

                    }
                },
                activated: function () {
                    // this.getTranSts()
                    // this.getTerminalType()
                },
                mounted: function () {
                    // this.getTranSts()
                    // this.getTerminalType()
                },
                methods: {
                    // 获取数据列表
                    init (orderCode,vwWarehouseCode) {
                        this.visible = true
                        this.orderCode = orderCode;
                        this.pagedataList = [{
                            key: '',
                            dataList: []
                        }];

                        this.$http({
                            url: this.POOL_NAME + '/order/v1/admin/recordStatusLog/queryNeedOrderVmMoveInfo?orderCode='+orderCode+'&vwWarehouseCode='+vwWarehouseCode+ '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize,
                            method: 'get',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                        if (data && data.code === '0') {
                            this.orderDTO = data.data;
                            this.exitVmWarehouseCodeList = data.data.exitVmWarehouseCodeList;

                            if(this.exitVmWarehouseCodeList && this.exitVmWarehouseCodeList.length>0){
                                this.orderDTO.exitVmWarehouseCode=this.exitVmWarehouseCodeList[0].virtualWarehouseCode;
                            }

                            this.totalPage = data.data.pagelist.total;
                            this.pageIndex = data.data.pagelist.pageNum;
                            this.pageSize = data.data.pagelist.pageSize;
                            if (data.data.pagelist.list == null || data.data.pagelist.list.length == 0){
                                this.pageIndex = 1;
                                this.pageSize = 10;
                            }

                            var check = false;
                            this.pagedataList.forEach(item =>{
                                if(item.key == data.data.pagelist.pageNum){
                                check = true;
                                this.dataList = item.dataList;
                            }
                             });
                            if(!check){
                                this.dataList = data.data.pagelist.list;
                                let m ={};
                                m.key = data.data.pagelist.pageNum;
                                m.dataList = data.data.pagelist.list;
                                this.pagedataList.push(m);
                            }


                        } else {
                            this.dataList = [];
                            this.totalPage = 0;
                            this.pageIndex = 1;
                            this.pageSize = 10;
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

                    refreshDataList() {
                        this.getDataList()
                    },

                    // 每页数
                    sizeChangeHandle(val) {
                        this.pageSize = val
                        this.pageIndex = 1
                        this.getDataList()
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

//
                    updateConfirmAmount(actualQty,row){
                        var  vmStockQty = row.vmStockQty;
                        var  practicalMoveQty = "";

                        if(isNaN(actualQty.target.value)){
                            this.$message({
                                message: "实际入库数量只能为数字!",
                                type: 'fail',
                                duration: 1500,
                            })
                            row.moveQty = actualQty.target.value;
                            practicalMoveQty= actualQty.target.value;
                        }
                        if(actualQty.target.value<0){
                            this.$message({
                                message: "实际入库数量不能为负数!",
                                type: 'fail',
                                duration: 1500,
                            })
                            // actualQty.target.value = skuQty;
                            // row.actualQty = skuQty;
                            // practicalActualQty= skuQty;
                            row.moveQty = actualQty.target.value;
                            practicalMoveQty= actualQty.target.value;

                        }else{
                            practicalMoveQty = actualQty.target.value;
                        }

                        // if(actualQty.target.value>vmStockQty){
                        //     this.$message({
                        //         message: "实际调拨数量不能大于虚仓库存数量",
                        //         type: 'fail',
                        //         duration: 1500,
                        //     })
                        //     row.moveQty = actualQty.target.value;
                        //     practicalMoveQty= actualQty.target.value;
                        // }else{
                        //     practicalMoveQty = actualQty.target.value;
                        // }

                        this.pagedataList.forEach(item =>{
                            if(item.key == this.pageIndex){
                            var pageActualList = item.dataList;
                            pageActualList.forEach(actual =>{
                                if(actual.id === row.id){
                                actual.moveQty = practicalMoveQty;
                            }
                        });
                        }
                    });
                        return ;
                    },

                    selectChanged(vmWarehouseCodeEvent){
                       this.orderDTO.exitVmWarehouseCode =  vmWarehouseCodeEvent;
                        this.pagedataList = [];
                        this.init(this.orderCode,vmWarehouseCodeEvent);
                    },

                    //确认移库
                    dataFormSubmit : function () {

                        var check = false;
                        this.pagedataList.forEach(item =>{

                            var pageActualList = item.dataList;
                        pageActualList.forEach(actual =>{
                        //     if(actual.moveQty>actual.vmStockQty){
                        //     this.$message({
                        //         message: "实际入库数量不能大于退货数量!",
                        //         type: 'fail',
                        //         duration: 1500,
                        //     })
                        //     check = true;
                        // }else
                            if(actual.moveQty<0) {
                            this.$message({
                                message: "实际调拨数量不能为负数!",
                                type: 'fail',
                                duration: 1500,
                            })
                            check = true;
                            alert();
                        }else if(isNaN(actual.moveQty)){
                            this.$message({
                                message: "实际调拨数量只能为数字!",
                                type: 'fail',
                                duration: 1500,
                            })
                            check = true;
                        }



                    });

                    });

                        if(check){
                            return;
                        }


                        var paramList = [];
                        this.pagedataList.forEach(item =>{
                            paramList = paramList.concat(item.dataList);
                    });

                        this.$http({
                            url: this.POOL_NAME + `/order/v1/admin/recordStatusLog/saveNeedOrderVmMoveInfo`,
                            method: 'POST',
                            data: {
                                'orderCode': this.orderCode,
                                'realWarehouseCode': this.orderDTO.joinVmWarehouseCode,
                                'outVwWarehouseCode': this.orderDTO.exitVmWarehouseCode,
                                'detailDTOList': paramList,
                            },
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
                                this.$emit('refreshDataList')
                            }
                        })
                        }else {
                            this.$message({
                                message: "操作失败，原因："+_data.msg,
                                type: 'fail',
                                duration: 1500,
                                // onClose: () => {
                                //     this.visible = false
                                //     this.$emit('refreshDataList')
                                // }
                            })
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
                <span >调拨虚仓建议</span>
            </div>
         <el-form  label-width="130px" size="mini" :model="dataForm" @keyup.enter.native="getDataList()">

               
               <el-col :span="12">
                        <el-form-item label="实仓编码：" prop="orderNo">
                            <span >{{orderDTO.factoryCode}}-{{orderDTO.realWarehouseCode}}-- {{orderDTO.realWarehouseName}}</span>
                        </el-form-item>
                    </el-col>
                    
                    <el-col :span="12">
                        <el-form-item label="调入虚仓编码：" prop="orderNo">
                            <span >{{orderDTO.joinVmWarehouseCode}} {{orderDTO.joinVmWarehouseName}}</span>
                        </el-form-item>
                    </el-col>
                    
                   
                   <el-col :span="12">
                        <el-form-item label="调出虚仓编码：" >
                            <el-select   v-model="orderDTO.exitVmWarehouseCode"  @change="selectChanged($event)" clearable>
                                        <el-option v-for="e in exitVmWarehouseCodeList" :key="e.virtualWarehouseCode" :label="e.virtualWarehouseName" :value="e.virtualWarehouseCode" >
                                        </el-option>
                                    </el-select>
                        </el-form-item>
                    </el-col>
               
               

        
                <div slot="header">
                <span >商品详情:</span>
            </div>
            <el-table
                    :data="dataList"
                    size="mini"
                    border
                    style="width: 101%;">
                    <el-table-column
                            prop="skuCode"
                            header-align="center"
                            width="120"
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
                            width="80"
                            align="center"
                            label="基本单位">
                    </el-table-column>
                     
                     <el-table-column
                            prop="orderQty"
                            header-align="center"
                            width="80"
                            align="center"
                            label="下单数量">
                    </el-table-column>
                    
                    <el-table-column
                            prop="requireQty"
                            header-align="center"
                            width="80"
                            align="center"
                            label="需求锁定数量">
                    </el-table-column>
                    
                    <el-table-column
                            prop="hasLockQty"
                            header-align="center"
                            width="80"
                            align="center"
                            label="实际锁定数量">
                    </el-table-column>
                    
                    
                    
<!--                     <el-table-column-->
<!--                            prop="vmStockQty"-->
<!--                            header-align="center"-->
<!--                            width="80"-->
<!--                            align="center"-->
<!--                            label="虚仓库存数量">-->
<!--                    </el-table-column>-->
                    
                      <el-table-column
                            prop="needMoveQty"
                            header-align="center"
                            width="80"
                            align="center"
                            label="需要调拨数量">
                    </el-table-column>
                    
                    <el-table-column
                            prop="moveQty"
                            header-align="center"
                            width="140"
                            align="center"
                            label="实际调拨数量">
                            <template slot-scope="scope">
            <input :value="scope.row.moveQty==null||scope.row.moveQty=='' ? 0 : scope.row.moveQty" @change="updateConfirmAmount($event, scope.row)" style="text-align:center;width:95px;" placeholder="实际调拨数量" />
        </template>
                    </el-table-column>
                    
                    
                    
            </el-table>
            
             </el-form>
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
    
     <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确认调拨</el-button>
    </span>
    </el-dialog>
    `
            }
        );
    });