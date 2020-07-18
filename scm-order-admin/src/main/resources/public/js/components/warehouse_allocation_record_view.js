/**
 * 仓库调拨查看
 * @autor: sunyj
 * @date: 2019-6-1
 */
lyfdefine(function(addOrUpdate){
    return ({
        data: function () {
            return {
                dataList: [],
                prodList: [],
                reasonList:[],
                dataForm: {
                    isReturnAllotcate: '',
                    isQualityAllotcate: '',
                    businessType:'',
                    recordCode:'',
                    inWarehouseId:'',
                    inWarehouseAddr:'',
                    outWarehouseId:'',
                    outWarehouseAddr:'',
                    infactoryCode:'',
                    outfactoryCode:'',
                    expeAogTime:'',
                    allotTime:'',
                    inWarehouseName:'',
                    inWarehouseMobile:'',
                    outWarehouseName:'',
                    outWarehouseMobile:'',
                    remark:'',
                    itemList: [],
                },
                prodForm:{
                    realWarehouseId: '',
                    pageIndex: 1,
                    pageSize: 10,
                    totalPage: 0
                },
                dataListLoading:false,
                prodListLoading:false,
                prodDialogVisible:false,
                visible: false,
                gridData: [],
                checkedArr: [],
                factoryCode:[],
                inRealWareCode:[],
                outRealWareCode:[],
                orderTypeList: [
                    {value: 1, label: '内部调拨'},
                    {value: 2, label: 'RDC调拨'},
                    {value: 3, label: '退货调拨'},
                    {value: 4, label: '电商仓调拨'},
                    {value: 5, label: '质量问题调拨'}
                ],
                labelPosition: 'top',
                unitCodes: [],
                unitInfos: new Map()
            }
        },
        components: {
            'addOrUpdate': addOrUpdate
        },
        methods: {
            init: function init(id) {
                let _this = this;
                this.clearForm();
                this.visible = true;
                _this.$http.get('/scm-order-admin/order/v1/whAllocation/initEditPage?id='+id
                ).then(function (res) {
                    if(res.status=='200'){
                        _this.dataForm = res.data.data;
                        _this.dataForm.outfactoryCode = res.data.data.outRealWarehouse.factoryCode;
                        _this.dataForm.infactoryCode = res.data.data.inRealWarehouse.factoryCode;
                        _this.initWarehouse(_this.dataForm.outfactoryCode, 1);
                        _this.initWarehouse(_this.dataForm.infactoryCode, 2);
                        _this.prodForm.realWarehouseId = res.data.data.outWarehouseId;
                        _this.dataForm.outWarehouseId= res.data.data.outWarehouseId;
                        _this.dataForm.inWarehouseId= res.data.data.inWarehouseId;
                        _this.dataForm.outWarehouseAddr =  res.data.data.outRealWarehouse.realWarehouseAddress;
                        _this.dataForm.inWarehouseAddr =  res.data.data.inRealWarehouse.realWarehouseAddress;
                        _this.dataList = _this.mergeArray(_this.dataList,res.data.data.frontRecordDetails);
                        let details = res.data.data.frontRecordDetails;
                        let lineNo = 1;
                        for(let i=0;i<details.length;i++){
                            details[i].lineNo = lineNo;
                            for(let j = 0;j<details[i].skuUnitList.length;j++){
                                details[i].skuUnitList[j].lineNo = details[i].lineNo;
                                if(details[i].unitCode == details[i].skuUnitList[j].unitCode){
                                    _this.unitCodes[i] = details[i].skuUnitList[j];
                                    _this.unitInfos.set(details[i].lineNo, details[i].skuUnitList[j]);
                                    _this.changeUnit(details[i].skuUnitList, i);
                                }
                            }
                            lineNo++;
                        }
                        _this.$set(_this.dataForm, 'isReturnAllotcate', res.data.data.isReturnAllotcate.toString());
                        _this.$set(_this.dataForm, 'isQualityAllotcate', res.data.data.isQualityAllotcate.toString());
                        _this.checkedPordArr = details;
                        _this.checkedItemArr = details;
                        _this.reasonList = res.data.data.reasonList;
                        console.log(_this.unitCodes);
                        console.log(_this.unitInfos);
                    }
                }).catch(function(error){
                    console.log(error);
                });
            },
            closeVisible: function () {
                this.visible = false;
                this.$refs['dataForm'].resetFields();
                this.$refs.mainTable.clearSort();
            },
            //合并数组并去重
            mergeArray(arr1,arr2){
                let _arr = new Array();
                for(let i=0;i<arr1.length;i++){
                    _arr.push(arr1[i]);
                }
                for(let i=0;i<arr2.length;i++){
                    let flag = true;
                    for(let j=0;j<arr1.length;j++){
                        if(arr2[i].skuId==arr1[j].skuId){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        _arr.push(arr2[i]);
                    }
                }
                return _arr;
            },
            //初始化仓库
            initWarehouse:function (row, type) {
                let _this = this;
                if(type == 1) {
                    _this.outRealWareCode = [];
                }else{
                    _this.inRealWareCode = [];
                }
                this.$http.post('/scm-order-admin/order/v1/real_warehouse/queryRealWarehouseByFactoryCode', row
                ).then(function (_ref2) {
                    let data = _ref2.data.data;
                    data.forEach(function (e) {
                        let arr1 = {};
                        arr1.value = e;
                        arr1.label = e.realWarehouseOutCode +"/" + e.realWarehouseName;
                        if(type == 1){
                            _this.outRealWareCode.push(arr1);
                        }else{
                            _this.inRealWareCode.push(arr1);
                        }

                    });

                })

            },
            changeUnit: function(data, index){
                let _this = this;
                let row = _this.dataList[index];
                data.forEach(function (e) {
                    if(_this.unitCodes != null) {
                        _this.unitCodes.forEach(function (obj) {
                            if (obj.lineNo == e.lineNo && obj.unitCode == e.unitCode) {
                                _this.unitInfos.set(row.lineNo, e);
                            }
                        });
                    }
                });
                //换算
                let scale = _this.unitInfos.get(row.lineNo).scale;
                let useQty = parseFloat((row.realQty - row.lockQty).toString()).toFixed(3);
                //质量调拨
                if(_this.dataForm.isQualityAllotcate == 1){
                    useQty = row.realQty;
                }
                let inptQty = row.allotQty;
                if (inptQty != '' && inptQty != undefined) {
                    let total = parseFloat((inptQty * scale).toFixed(3));
                    row.spec = "1 x" + scale;
                    row.baseNum = total;
                }
                row.unit = _this.unitInfos.get(row.lineNo).unitName;
                row.unitCodeInfo = _this.unitInfos.get(row.lineNo);
                this.$set(_this.dataList, index, row)
            },
            isRowRed:function ({ row }) {
                if(row.orginQty != null){
                    if (row.orginQty - row.allotQty > 0) {
                        return {
                            backgroundColor: '#C4723C',
                        }
                    }
                }
            },
            getFactoryList:function(){
                let _this = this;
                //后台查询所有实仓仓库信息
                this.$http.get('/scm-order-admin/order/v1/shop/getRealWarehouseFactory').then(function (_ref) {
                    let data = _ref.data.data;

                    if (data) {
                        let arr = [];
                        data.forEach(function (e) {
                            let obj = {};
                            obj.value = e.code;
                            obj.label = e.name;
                            if(arr .indexOf(obj) === -1){
                                _this.factoryCode.push(obj);
                            }
                        });

                    }
                });

            },
            clearForm: function(){
                let _this = this;
                _this.dataList=[];
                _this.unitCodes =[];
                _this.unitInfos =new Map();
                _this.dataForm.isReturnAllotcate='';
                _this.dataForm.isQualityAllotcate='';
                this.dataForm.inWarehouseId='';
                this.dataForm.outWarehouseId='';
                this.prodForm.realWarehouseId='';
                this.inRealWareCode=[];
                this.outRealWareCode=[];
                this.dataForm.remark='';
                this.dataForm.inWarehouseName='';
                this.dataForm.inWarehouseMobile='';
                this.dataForm.outWarehouseName='';
                this.dataForm.outWarehouseMobile='';
                this.$nextTick(function () {
                    _this.$refs['dataForm'].resetFields();
                });
            }
        },
        mounted() {
            this.getFactoryList();
        },
        template: `
                <el-dialog
                 title="查看"
                 :before-close="closeVisible"
                 :close-on-click-modal="false"
                 width="80%"
                 v-if="visible"
                 :visible.sync="visible">
                    <el-form :inline="true" :label-position="labelPosition"  :model="dataForm" class="demo-form-inline" ref="dataForm" >
                    
                        <el-col :span="8">
                            <el-form-item label="调拨类型" prop="businessType">
                                <el-select v-model="dataForm.businessType" placeholder="类型" :disabled="true">
                                    <el-option v-for="item in orderTypeList" :key="item.value" :label="item.label" :value="item.value" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="是否退货："  prop="isReturnAllotcate">
                               <el-input   v-model="dataForm.isReturnAllotcate" :disabled="true" style="display:none;"></el-input>
                                <el-radio v-model="dataForm.isReturnAllotcate" :disabled="true" label="1">是</el-radio>
                                <el-radio v-model="dataForm.isReturnAllotcate" :disabled="true" label="0">否</el-radio>
                            </el-form-item>
                        
                            <el-form-item label="是否质量问题：" prop="isQualityAllotcate">
                               <el-input   v-model="dataForm.isQualityAllotcate" :disabled="true" style="display:none;"></el-input>
                                <el-radio v-model="dataForm.isQualityAllotcate" :disabled="true" label="1">是</el-radio>
                                <el-radio v-model="dataForm.isQualityAllotcate" :disabled="true" label="0">否</el-radio>
                            </el-form-item>
                        </el-col>
                        
                        <el-col :span="8">
                            <el-form-item label="调拨日期："  prop="allotTime">
                                <el-date-picker  :disabled="true" type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.allotTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                    
                         <el-col :span="8">
                         <el-form-item label="选择出库工厂："   prop="outfactoryCode">
                            <template slot-scope="scope">
                             <el-select v-model="dataForm.outfactoryCode" placeholder="工厂编号" :disabled="true">
                                <el-option v-for="item in factoryCode"  :key="item.value" :label="item.label" :value="item.value">
                                </el-option>
                             </el-select>
                             </template>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择出库仓："  prop="outWarehouseId">
                            <el-select v-model="dataForm.outWarehouseId" placeholder="仓库编号/名称" :disabled="true">
                                <el-option v-for="item in outRealWareCode" :key="item.value" :label="item.label" :value="item.value.id" >
                                </el-option>
                            </el-select>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                            <el-form-item label="调出联系人/电话：" prop="outWarehouseName">
                            <div style="width: 190px">
                                <el-col :span="10" >
                                    <el-input  v-model="dataForm.outWarehouseName" placeholder="联系人" :disabled="true"></el-input>
                                </el-col>
                                 <el-col :span="14" >
                                    <el-input   v-model="dataForm.outWarehouseMobile" placeholder="电话" :disabled="true"></el-input>
                                 </el-col>
                            </div>
                                
                            </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择入库工厂：" prop="infactoryCode">
                           <template slot-scope="scope">
                            <el-select  v-model="dataForm.infactoryCode" placeholder="工厂编号" :disabled="true" >
                                <el-option v-for="item in factoryCode" :key="item.value" :label="item.label" :value="item.value">
                                </el-option>
                             </el-select>
                            </template>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择入库仓："  prop="inWarehouseId">
                            <el-select v-model="dataForm.inWarehouseId" placeholder="仓库编号/名称"  :disabled="true"> 
                                <el-option v-for="item in inRealWareCode" :key="item.value" :label="item.label" :value="item.value.id" >
                                </el-option>
                            </el-select>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="调入联系人/电话：" prop="inWarehouseName">
                            <div style="width: 190px">
                                <el-col :span="10" >
                                    <el-input  v-model="dataForm.inWarehouseName" placeholder="联系人" :disabled="true"></el-input>
                                </el-col>
                                 <el-col :span="14" >
                                    <el-input  v-model="dataForm.inWarehouseMobile" placeholder="电话" :disabled="true"></el-input>
                                 </el-col>
                            </div>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="调入仓库地点："   prop="inWarehouseAddr">
                             <el-input v-model="dataForm.inWarehouseAddr" :disabled="true" placeholder="请输入仓库地点"></el-input>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="调出仓库地点："   prop="outWarehouseAddr">
                             <el-input v-model="dataForm.outWarehouseAddr":disabled="true" placeholder="请输入仓库地点"></el-input>
                        </el-form-item>
                         </el-col>
                        
                         <el-col :span="8">
                        <el-form-item label="预计到货日期：" prop="expeAogTime">
                                <el-date-picker type="datetime" placeholder="选择日期" :disabled="true" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.expeAogTime" style="width: 100%;"></el-date-picker>                    
                        </el-form-item>
                         </el-col>
                         <el-col :span="16">
                         <el-form-item label="备注：" prop="remark">
                             <el-input v-model="dataForm.remark" placeholder="请输入备注" style="width:500px;" :disabled="true"></el-input>
                        </el-form-item>
                        </el-col>
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        <el-col :span="3"><div style="font-weight: bold">商品明细</div></el-col>
                    </div>
                    
                    <el-table  :row-style="isRowRed"  ref="mainTable" size="small"  border :data="dataList"  v-loading="dataListLoading" element-loading-text="拼命加载中...">
                        <el-table-column type="index" width="50" label="序号"></el-table-column>
                         
                        <el-table-column prop="lineNo" label="po行号" width="140"></el-table-column>
                         
                        <el-table-column prop="skuCode" label="商品编号" width="140" sortable>
                           
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称" width="140">
                        </el-table-column>
                         <el-table-column prop="orginQty" label="原始数量" width="140">
                        </el-table-column>
                        <el-table-column prop="allotQty" label="调拨数量" width="140">
                        <template slot-scope="scope">
                            <el-input  v-model="scope.row.allotQty" placeholder="" size="small" :disabled="true"></el-input>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="unitCodeInfo"  label="调拨单位" width="140">
                            <template slot-scope="scope">
                                <el-select  value-key="unitCode" v-model="unitCodes[scope.$index]" size="small" placeholder="调拨单位" @change="changeUnit(scope.row.skuUnitList, scope.$index)" :disabled="true">
                                    <el-option v-for="(item,index) in scope.row.skuUnitList" :key="item.unitName" 
                                    :label="item.unitName" :key="item.unitCode" :value="item">
                                    </el-option>
                                </el-select>
                            </template>
                        </el-table-column>
                        <el-table-column prop="reasonCode" v-if="dataForm.isReturnAllotcate == 1"  label="退货原因" width="140">
                            <template slot-scope="scope">
                                <el-select v-model="scope.row.reasonCode" :disabled="true" v-if="dataForm.isReturnAllotcate == 1" size="small" placeholder="退货原因">
                                    <el-option v-for="(item,index) in reasonList" :key="item.reasonName" 
                                    :label="item.reasonName" :value="item.reasonCode" >
                                    </el-option>
                                </el-select>
                            </template>
                        </el-table-column>
                        <el-table-column prop="batchRemark" label="批次备注" width="140">
                        <template slot-scope="scope">
                            <el-input  v-model="scope.row.batchRemark" placeholder="" size="small" :disabled="true"></el-input>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="spec" label="规格" width="140">
                        
                        </el-table-column>
                        <el-table-column prop="outQty" label="实出数量" width="140">
                        </el-table-column>
                         <el-table-column prop="inQty" label="实入数量" width="140">
                        </el-table-column>
                        <el-table-column prop="baseNum" label="基本数量" width="140">
                        </el-table-column>
                        <el-table-column prop="baseUnit" label="基本单位" width="140">
                        </el-table-column>
                    </el-table>
                    <span slot="footer" class="dialog-footer">
                        <el-button type="primary" @click="closeVisible">返回</el-button>
                    </span>
                    <addOrUpdate></addOrUpdate>
                   </el-dialog>
                 </el-dialog>
        `
    });
});
