lyfdefine(
    function () {
        return ({
                data: function () {
                    return {
                        visible: false,
                        show: false,
                        POOL_NAME: '/scm-order-admin',
                        signTypeList: {},
                        dataForm: {
                            id: '', //id
                            recordCode:"",   // 所属单据编号
                            frontRecordId:"",   // 前置单ID
                            skuId:"", // 商品skuId
                            skuCode:"", // sku编码
                            skuQty:"", // 商品数量
                            skuQty:"", // 商品数量
                            unit: '',      //单位
                            unitCode: '', //单位编码
                            skuName: '', //sku名称
                            deliveryQty: '', //实际发货数量
                            actualQty: '' //实际发货数量

                        },
                        dataList: [
                        ],
                        pagedataList: [{
                            key: '',
                            dataList: []
                        }],
                        returnDTO: {
                            realWarehouseName:"",   // 退入仓库：
                            realWarehouseAddress: '', //退入仓库地址：
                            reason: '', //退货原因：
                            customName: '', //所属客户：
                            customMobile: '', //客户联系电话
                            recordStatusDesc:"", // 团购入库单状态
                            address:"", // 客户地址：
                            createTime:"", // 团购入库单状态
                            recordStatusDesc:"" // 创建日期
                        },
                        pageIndex: 1,
                        pageSize: 10,
                        totalPage: 0,
                        dataListLoading: false,
                        recordCode: '',
                        dataListSelections: []



                    }
                },

                activated: function () {
                    this.init(this.recordCode)
                },
                mounted: function () {
                    this.init(this.recordCode)
                },
                methods: {

                    // 获取数据列表
                    init(recordCode) {
                        if(recordCode==null||recordCode==''){
                            return;
                        }
                        this.visible = true
                        this.recordCode = recordCode;
                        this.pagedataList = [{
                            key: '',
                            dataList: []
                        }];
                        this.$http({
                            url: this.POOL_NAME + '/order/v1/orderReturn/queryOrderReturnDetailPageByAfterSaleCode?afterSaleCode=' + afterSaleCode + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize,
                            method: 'get',
                            data: {
                            },
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(result => {
                            let data = result.data;
                        if (data && data.code == '0') {
                            var check = false;
                            this.pagedataList.forEach(item =>{
                                if(item.key == data.data.returnDetailPageInfo.pageNum){
                                check = true;
                                this.dataList = item.dataList;
                            }
                        });
                            if(!check){
                                this.dataList = data.data.returnDetailPageInfo.list;
                                let m ={};
                                m.key = data.data.returnDetailPageInfo.pageNum;
                                m.dataList = data.data.returnDetailPageInfo.list;
                                this.pagedataList.push(m);
                            }

                            this.returnDTO = data.data;
                            this.totalPage = data.data.returnDetailPageInfo.total;
                            this.pageIndex = data.data.returnDetailPageInfo.pageNum;
                            this.pageSize = data.data.returnDetailPageInfo.pageSize;
                            if (data.data.returnDetailPageInfo.list == null || data.data.returnDetailPageInfo.list.length == 0){
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



                        //     //获取预约单信息
                        //     this.$http({
                        //         url: this.POOL_NAME + '/order/v1/order/findOrderByOrderCode/'+orderCode,
                        //         method: 'get',
                        //         data: {
                        //         },
                        //         headers: {
                        //             'Content-Type': 'application/json'
                        //         }
                        //     }).then(result => {
                        //         let data = result.data;
                        //     if (data && data.code === '0') {
                        //         this.orderDTO = data.data;
                        //     } else {
                        //         this.orderDTO = {};
                        //
                        //     }
                        // }).catch(result=> {
                        //         let _data = result.response.data;
                        //     this.$message({
                        //         message: "操作失败，原因："+_data.msg,
                        //         type: 'fail',
                        //         duration: 1500,
                        //     });
                        // }).catch(() => {
                        //         this.$message({
                        //             type: 'fail',
                        //             message: '服务异常，请稍后再试',
                        //             duration: 1500,
                        //         });
                        // });

                    },


                    // 每页数
                    sizeChangeHandle(val) {
                        this.pageSize = val
                        this.pageIndex = 1
                        this.init(this.recordCode)
                    },

                    // 当前页
                    currentChangeHandle(val) {
                        this.pageIndex = val
                        this.init(this.recordCode)
                    },
                    // 多选
                    selectionChangeHandle(val) {
                        this.dataListSelections = val
                    },


                    getreason(){
                        return   this.returnDTO.reason;
                    },

                    updateConfirmAmount(actualQty,row){
                        var  skuQty = row.skuQty;

                        if(isNaN(actualQty.target.value)){
                            this.$message({
                                message: "实际入库数量只能为数字!",
                                type: 'fail',
                                duration: 1500,
                            })
                            row.actualQty = actualQty.target.value;
                            practicalActualQty= actualQty.target.value;
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
                            row.actualQty = actualQty.target.value;
                            practicalActualQty= actualQty.target.value;

                        }else{
                            practicalActualQty = actualQty.target.value;
                        }

                        var  practicalActualQty = "";
                        if(actualQty.target.value>skuQty){
                            this.$message({
                                message: "实际入库数量不能大于退货数量!",
                                type: 'fail',
                                duration: 1500,
                            })
                            // actualQty.target.value = skuQty;
                            // row.actualQty = skuQty;
                            // practicalActualQty= skuQty;

                            row.actualQty = actualQty.target.value;
                            practicalActualQty= actualQty.target.value;

                        }else{
                            practicalActualQty = actualQty.target.value;
                        }


                        this.pagedataList.forEach(item =>{
                            if(item.key == this.pageIndex){
                            var pageActualList = item.dataList;
                            pageActualList.forEach(actual =>{
                                if(actual.id === row.id){
                                actual.actualQty = practicalActualQty  ;
                            }
                        });
                        }
                    });
                        return ;
                    },

                    //收货
                    dataFormSubmit : function () {
                        var check = false;
                        this.pagedataList.forEach(item =>{

                            var pageActualList = item.dataList;
                            pageActualList.forEach(actual =>{
                               if(actual.actualQty>actual.skuQty){
                            this.$message({
                                message: "实际入库数量不能大于退货数量!",
                                type: 'fail',
                                duration: 1500,
                            })
                            check = true;
                            }else if(actual.actualQty<0) {
                            this.$message({
                                message: "实际入库数量不能为负数!",
                                type: 'fail',
                                duration: 1500,
                            })
                            check = true;
                            }else if(isNaN(actual.actualQty)){
                            this.$message({
                                message: "实际入库数量只能为数字!",
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
                            url: this.POOL_NAME + `/stock/v1/return_group/receipt`,
                            method: 'post',
                            data :{
                                'recordCode': this.recordCode,
                                'returnDetails': paramList,
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





                    signTypeFormat (row, column, cellValue){
                        let obj = {};
                        obj = this.signTypeList.find((item)=>{
                            return item.dicKey === cellValue;
                    });
                        if (obj == undefined){
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

                },
                template: `
<el-dialog
    width="65%"
    height="65%"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
        <el-card class="box-card search-input" shadow="never">
            <div slot="header">
                <span >团购入库单详情</span>
            </div>
            <el-form  label-width="120px" size="mini" :model="returnDTO">

                  <el-col :span="7" >
                    <el-form-item label="退入仓库：">
                        <el-input v-model="returnDTO.realWarehouseName" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                 <el-col :span="8">
                    <el-form-item label="退入仓库地址：">
                      <el-input v-model="returnDTO.realWarehouseAddress" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                 <el-col :span="8">
                    <el-form-item label="退货原因：">
                     <el-input v-model="returnDTO.reason" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                 
                <el-col :span="6">
                    <el-form-item label="所属客户：">
                     <el-input v-model="returnDTO.customName" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                 <el-col :span="7">
                    <el-form-item label="客户联系电话：">
                     <el-input v-model="returnDTO.customMobile" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                <el-col :span="8">
                    <el-form-item label="团购入库单状态:">
                     <el-input  v-model="returnDTO.recordStatus == 10? '待入库' : '已入库'" disabled="disabled" size="mini" max-width="80px"  clearable></el-input>
                    </el-form-item>
                </el-col>
                
                <el-col :span="6">
                    <el-form-item label="创建日期：">
                     <el-input v-model="returnDTO.createTime" disabled="disabled" size="mini" max-width="100px" clearable></el-input>
                    </el-form-item>
                </el-col>
                
                <el-col :span="13">
                    <el-form-item label="客户地址：">
                     <el-input v-model="returnDTO.address" disabled="disabled" size="mini" max-width="80px" clearable></el-input>
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
                            prop="skuCode"
                            header-align="center"
                            align="center"
                            label="商品编码">
                    </el-table-column>
                    <el-table-column
                            prop="skuName"
                            header-align="center"
                            width="190"
                            align="center"
                            label="商品名称">
                    </el-table-column>
                    <el-table-column
                        prop="skuQty"
                        header-align="center"
                        align="center"
                        label="退货数量">
                    </el-table-column>
                    <el-table-column
                            prop="deliveryQty"
                            header-align="center"
                            align="center"

                            label="实际发货数量">
                    </el-table-column>
                    

                    
                    <el-table-column prop="actualQty"  header-align="center" align="center" label="实际入库数量">
        <template slot-scope="scope">
            <input :value="scope.row.actualQty==null||scope.row.actualQty=='' ? scope.row.skuQty : scope.row.actualQty" @change="updateConfirmAmount($event, scope.row)" placeholder="实际入库数量" style="text-align:center;width:69px;" />
        </template>
    </el-table-column>

                    
                       <el-table-column
                            prop="unit"
                            header-align="center"
                            align="center"
                            width="70"
                            label="单位">
                    </el-table-column>
                       <el-table-column
                            prop="reason"
                            header-align="center"
                            align="center"
                            :formatter="getreason"
                            label="退货原因">
                    </el-table-column>
                       <el-table-column
                            prop="createTime"
                            header-align="center"
                            width="140"
                            align="center"
                            label="创建时间">
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
    
     <span slot="footer" class="dialog-footer">
             <el-button @click="visible = false">返回</el-button>
             <el-button type="primary"   @click="dataFormSubmit()">提交</el-button>
            </span>

        </el-dialog>
    `
            }
        );
    });