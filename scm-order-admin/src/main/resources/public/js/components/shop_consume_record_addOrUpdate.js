lyfdefine(function(){
   return ({
       data: function () {
           return {
               visible: false,
               recordType:39,//单据类型-门店报废
               itemForm: {
                   factoryCode:'',
                   recordCode: '',
                   realWarehouseId:'',
                   reasonCode:'',
                   approveOACode:'',
                   remark:'',
                   organizationCode: '',
                   organizationName:'',
               },
               factoryForm:{
                   isRequest:true,
                   codeOrName:null,
                   factoryLoading:false,
                   pageIndex:1,
                   pageSize:15,
               },
               realWareCode: [],
               factoryCode1: [],
               factoryLoading: false,
               organizationCode:[],
               rules: {
                   factoryCode: [
                       { required: true, message: '请选择工厂', trigger: 'change' }
                   ],
                   realWarehouseId: [
                       { required: true, message: '请选择仓库', trigger: 'change' }
                   ],
                   reasonCode:[
                       { required: true, message: '请选择业务原因', trigger: 'change' }
                   ],
               },
               reasonCodeList:[],
               detailList:[],
               adjustList:[],
               dataListLoading: false,
               skuList:[],
               tempList:{},
               basicUnitList:{},
               remainStockQtyList:{},
               prodListLoading:false,
               prodDialogVisible:false,
               prodForm:{
                   pageIndex: 1,
                   pageSize: 10,
                   totalPage: 0,
                   realWarehouseId:'',
                   skuCode:'',
               },
               prodList:[],
               checkedArr: [],
           }

       },

       //方法区
       methods: {
           init: function init(id,code) {
               let _this = this;
               this.$nextTick(function () {
                   _this.$refs['itemForm'].resetFields();
               });
               this.recordType=code;
               this.detailList=[];
               this.itemForm.id = id || 0;
               this.visible = true;
               this.skuList = [];
               this.tempList = {};
               this.realWareCode = [];
               this.factoryCode1 = [];
               this.adjustList =[];
               this.prodList = [];
               this.checkedArr = [];
               this.organizationCode = [];

               //若id不为空，调用后台接口查询
               if(0 != _this.itemForm.id){
                   _this.dataListLoading = true;
                   _this.$http.get('/stock-admin/admin/v1/shop_consume/selectShopConsumeRecordDetail?id='+id).then(function (_ref) {
                       let data = _ref.data.data;
                       if (data) {
                           _this.itemForm.realWarehouseId = data.realWarehouseId;
                           _this.itemForm.factoryCode = data.factoryCode;
                           _this.itemForm.recordCode = data.recordCode;
                           _this.itemForm.reasonCode = data.reasonCode;
                           _this.itemForm.organizationCode = data.organizationCode;
                           _this.itemForm.approveOACode = data.approveOACode;
                           _this.itemForm.remark = data.remark;
                           _this.itemForm.recordStatus = data.recordStatus;
                           _this.getOrgCode(data.organizationCode);

                           let el = {};
                           el.value = data.factoryCode;
                           el.label = data.factoryCode + "/" + data.factoryName;
                           _this.factoryCode1.push(el);

                           _this.prodForm.realWarehouseId = data.realWarehouseId;
                           let arr1 = {};
                           arr1.value = data.realWarehouseId;
                           arr1.label = data.realWarehouseCode +"/" + data.realWarehouseName;
                           _this.realWareCode.push(arr1);

                           _this.detailList = data.detailDTOS;
                           data.detailDTOS.forEach(function (val) {
                               val.skuStandard = '1X' + val.scale;
                               let el = {};
                               el.skuId = val.skuId;
                               el.skuCode = val.skuCode;
                               el.skuName = val.skuName;
                               _this.skuList.push(el);
                           });
                           _this.dataListLoading = false;
                       } else {
                           _this.detailList = [];
                           _this.dataListLoading = false;
                       }
                   }).catch(function (error) {
                       console.log(error);
                   });
               }else {
                   this.remoteMethod('');
               }
               this.getReasonList();

           },

           //根据工厂编号获取实仓编号和名字
           getRealWareByFCode: function(row){
               this.realWareCode = [];
               this.itemForm.realWarehouseId = '';
               this.skuList = [];
               this.detailList = [];
               this.itemForm.organizationCode = '';

               if('' == row || undefined == row){
                   return;
               }
               let _this = this;
               //根据工厂code查询仓库列表
               this.$http.get('/stock-admin/admin/v1/queryRealWarehouseByFactoryCode/' + row + '/' + _this.recordType).then(function (_ref2) {
                   let data = _ref2.data.data;
                   _this.realWareCode = [];
                   data.forEach(function (e) {
                       let arr1 = {};
                       arr1.value = e.id;
                       arr1.label = e.realWarehouseCode +"/" + e.realWarehouseName;
                       _this.realWareCode.push(arr1);
                   });
               }).catch(function (error) {
                   console.log(error);
               });

               this.factoryCode1.forEach(function (val) {
                   if(row == val.value){
                       _this.itemForm.organizationCode = val.companyCode;
                       _this.getOrgCode(val.companyCode);
                   }
               })
           },

           //根据公司code查询公司名称
           getOrgCode:function(companyCode){
               let _this = this;
               //根据公司code查询公司信息
               this.$http.get('/stock-admin/admin/v1/shop_consume/getOrgByOrgCode/' + companyCode).then(function (_ref2) {
                   let data = _ref2.data.data;
                   if(data){
                       _this.organizationCode = [];
                       let el = {};
                       el.value = data.orgCode;
                       el.label = data.orgName;
                       _this.organizationCode.push(el);
                       _this.itemForm.organizationName = data.orgName;
                   }
               }).catch(function (error) {
                   console.log(error);
               });
           },

           //根据实仓id查询实仓拥有的所有sku
           getSkuList:function () {
               let _this = this;
               //根据实仓id查询实仓拥有的所有sku
               this.$http.post('/stock-admin/admin/v1/querySkuIdByWhId',_this.prodForm).then(function (_ref2) {
                   if(_ref2.status=='200'){
                       let data = _ref2.data.data;
                       _this.prodList = data.list;
                       _this.prodForm.totalPage = data.total;
                   }else{
                       _this.prodList = [];
                       _this.prodForm.totalPage = 0;
                       _this.$message('查询失败！');
                   }
                   _this.prodListLoading = false;
               }).catch(function (error) {
                   console.log(error);
                   _this.prodListLoading = false;
               });
           },
           deleteDetailRow:function(index, rows) {
               let _this = this;
               let ss = rows.splice(index, 1);
               for (let i = 0;i < _this.skuList.length;i ++ ){
                   if(_this.skuList[i].skuId == ss[0].skuId){
                       _this.tempList[_this.skuList[i].skuId] = null;
                       _this.skuList.splice(i,1);
                   }
               }
           },
           addDetailRow:function () {
               let _this = this;
               if('' == this.itemForm.realWarehouseId){
                   _this.$message({
                       message: '请先选择仓库'
                   });
                   return;
               }else {
                   // _this.$ref['prodForm'].resetField();
                   _this.prodForm.skuCode = '';
                   _this.prodForm.pageIndex = 1;
                   _this.prodForm.pageSize = 10;
                   _this.prodForm.totalPage = 0;
                   _this.prodList = [];
                   //打开商品选择窗口
                   _this.prodDialogVisible = true;
                   _this.prodListLoading = true;
                   //调用接口查询该仓库的所有sku
                   _this.getSkuList();
               }

           },
           //获取业务原因列表
           getReasonList:function () {
               //调用接口查询损益单的业务原因字典
               let _this = this;
               this.$http.post('/stock-admin/admin/v1/queryBusinessReason',
                   this.recordType
               ).then(function (_ref) {
                   let data = _ref.data.data;
                   if (data) {
                       _this.reasonCodeList = data;
                   } else {
                       _this.reasonCodeList = [];
                   }
               }).catch(function (error) {
                   console.log(error);
               });
           },
           //表单提交
           itemFormSubmit: function () {
               let isSuccess = false;
               let _this2 = this;
               this.$refs['itemForm'].validate(function (valid) {
                   if (valid){
                       _this2.adjustList = [];
                       if (_this2.detailList.length == 0) {
                           _this2.$message({
                               message: '请至少添加一项明细'
                           });
                           return;
                       } else {
                           _this2.adjustList = [];
                           _this2.detailList.forEach(function (value) {
                               let arr = {};
                               arr.id = value.id||undefined;
                               arr.recordCode = value.recordCode||undefined;
                               arr.skuId = value.skuId;
                               arr.unitCode = value.unitCode;
                               arr.skuQty = value.skuQty;
                               arr.remark = value.remark;
                               if (arr.unitCode == null || arr.unitCode == undefined) {
                                   _this2.$message({
                                       message: '请选择调整单位'
                                   });
                                   isSuccess = true;
                                   return;
                               }
                               if (arr.skuQty == null || arr.skuQty == undefined) {
                                   _this2.$message({
                                       message: '请输入调整数量'
                                   });
                                   isSuccess = true;
                                   return;
                               }
                               if (arr.skuId == null || arr.skuId == undefined) {
                                   _this2.$message({
                                       message: '请选择商品'
                                   });
                                   isSuccess = true;
                                   return;
                               }
                               if(isSuccess == false){
                                   _this2.adjustList.push(arr);
                               }
                           })
                       }
                       if(isSuccess == true){
                           _this2.adjustList = [];
                           return;
                       }

                       //itemForm.id不存在，调用新增接口
                       let isId = _this2.itemForm.id ? true : false;
                       if (!isId) {
                           _this2.$http({
                               url: '/stock-admin/admin/v1/shop_consume/addShopConsumeAdjustRecord',
                               method: 'post',
                               data: {
                                   'id': _this2.itemForm.id || undefined,
                                   'realWarehouseId': _this2.itemForm.realWarehouseId,
                                   'approveOACode': _this2.itemForm.approveOACode,
                                   'recordType': _this2.recordType,
                                   'reasonCode': _this2.itemForm.reasonCode,
                                   'remark': _this2.itemForm.remark,
                                   'organizationCode':_this2.itemForm.organizationCode,
                                   'organizationName':_this2.itemForm.organizationName,
                                   'detailDTOS': _this2.adjustList,
                               }
                           }).then(function (_ref2) {
                               let data = _ref2.data;
                               if (data && data.code === '0') {
                                   _this2.adjustList = [];
                                   _this2.detailList = [];
                                   _this2.$refs['itemForm'].resetFields();
                                   _this2.$message({
                                       message: '操作成功',
                                       type: 'success',
                                       duration: 1500,
                                       onClose: function onClose() {
                                           _this2.visible = false;
                                           _this2.$emit('refreshDataList');
                                       }
                                   });

                               } else if (data && data != 0) {
                                   _this2.$message({
                                       message: data.msg,
                                       type: 'error',
                                       duration: 1500,
                                       onClose: function onClose() {
                                           _this2.visible = false;
                                       }
                                   });
                               }
                           }).catch(function (error) {
                               console.log(error);
                           });
                       }else {
                           //itemForm.id存在，调用修改接口
                           _this2.$http({
                               url: '/stock-admin/admin/v1/shop_consume/modifyShopConsumeRecord',
                               method: 'post',
                               data: {
                                   'id': _this2.itemForm.id,
                                   'recordCode':_this2.itemForm.recordCode,
                                   'realWarehouseId': _this2.itemForm.realWarehouseId,
                                   'approveOACode': _this2.itemForm.approveOACode,
                                   'recordType': _this2.recordType,
                                   'reasonCode': _this2.itemForm.reasonCode,
                                   'remark': _this2.itemForm.remark,
                                   'organizationCode':_this2.itemForm.organizationCode,
                                   'organizationName':_this2.itemForm.organizationName,
                                   'detailDTOS': _this2.adjustList,
                               }
                           }).then(function (_ref2) {
                               let data = _ref2.data;

                               if (data && data.code === '0') {
                                   _this2.adjustList = [];
                                   _this2.detailList = [];
                                   _this2.$refs['itemForm'].resetFields();
                                   _this2.$message({
                                       message: '操作成功',
                                       type: 'success',
                                       duration: 1500,
                                       onClose: function onClose() {
                                           _this2.visible = false;
                                           _this2.$emit('refreshDataList');
                                       }
                                   });

                               } else if (data && data != 0) {
                                   _this2.$message({
                                       message: data.msg,
                                       type: 'error',
                                       duration: 1500,
                                       onClose: function onClose() {
                                           _this2.visible = false;
                                       }
                                   });
                               }
                           }).catch(function (error) {
                               console.log(error);
                           });
                       }
                   }
               })

           },

           //计算实际调整数量
           computeSkuqty: function (row,val) {
               if(row.unitCode == '' || row.unitCode == null){
                   row.basicQty = val;
               }else{
                   row.skuUnitList.forEach(function (ss) {
                       if(row.unitCode == ss.unitCode){
                           row.basicQty = ss.scale * val;
                           row.skuStandard = '1X' + ss.scale;
                       }
                   })
               }
           },
           //选单位时计算基本单位为单位的调整数量
           getBasicQty:function(row,val){
               if(row.skuQty != null && row.skuQty != ''){
                   row.skuUnitList.forEach(function (ss) {
                       if(ss.unitCode == val){
                           row.skuStandard = '1X' + ss.scale;
                           row.basicQty = ss.scale * row.skuQty;
                       }
                   })
               }else {
                   row.skuUnitList.forEach(function (ss) {
                       if(ss.unitCode == val){
                           row.skuStandard = '1X' + ss.scale;
                       }
                   })
               }
           },

           closeVisible:function(){
               this.visible = false;
               this.realWareCode = [];
               this.factoryCode1 = [];
               this.$refs['itemForm'].resetFields();
           },
           onQueryProd:function () {
               this.prodForm.pageIndex = 1;
               this.prodForm.pageSize = 10;
               this.prodForm.totalPage = 0;
               this.getSkuList();
           },
           resetProd:function(){
               this.prodForm.skuCode = '';
           },
           setProdFormRWCode:function(val){
               //设置商品选择弹框的实仓id
               this.prodForm.realWarehouseId = val;
               this.detailList = [];
               this.skuList = [];
           },
           confrimTable:function () {
               let _this = this;
               let isAdd = true;
               _this.checkedArr.forEach(function (val) {
                   if(undefined != _this.tempList[val.skuId] && null != _this.tempList[val.skuId] && '' != _this.tempList[val.skuId]){
                       isAdd = false;
                   }else{
                       //将商品信息封装在页面商品集合中
                       let el = {};
                       el.skuId = val.skuId;
                       el.skuCode = val.skuCode;
                       el.skuName = val.skuName;
                       _this.skuList.push(el);
                       let list = [];
                       list.skuId = val.skuId;
                       list.skuUnitList = val.skuUnitList;
                       list.remainStockQty = val.realQty - val.lockQty;
                       list.basicUnit = val.baseUnit;
                       _this.detailList.push(list);
                       _this.tempList[val.skuId] = val.skuId;
                   }
               });
               if(!isAdd){
                   _this.$message('已过滤重复商品!');
               }
               this.prodDialogVisible = false;
           },
           handleSelectionChange:function (val) {
               this.checkedArr = val;
           },
           //每页数量改变
           sizeChangeHandle: function sizeChangeHandle(val) {
               this.prodForm.pageSize = val;
               this.prodForm.pageIndex = 1;
               this.getSkuList();
           },

           //当前页设置
           currentChangeHandle: function currentChangeHandle(val) {
               this.prodForm.pageIndex = val;
               this.getSkuList();
           },
           remoteMethod(query) {
               let _this = this;
               _this.factoryForm.factoryLoading = true;

               if(query != _this.factoryForm.codeOrName){
                   _this.factoryForm.isRequest = true;
                   _this.factoryForm.codeOrName = query;
                   _this.factoryForm.pageIndex = 1;
                   _this.factoryCode1 = [];
               }

               if(_this.factoryForm.isRequest == true){
                   _this.$http.post('/stock-admin/admin/v1/shop_consume/queryShopFactory',_this.factoryForm).then(function (_ref) {
                       let data = _ref.data.data;
                       if (data) {
                           if(_this.factoryCode1.length == data.total){
                               _this.factoryForm.isRequest = false;
                           }else {
                               data.list.forEach(function (e) {
                                   let arr = {};
                                   arr.value = e.code;
                                   arr.label = e.code + '/' + e.name;
                                   arr.companyCode = e.companyCode;
                                   _this.factoryCode1.push(arr);
                               });
                           }
                           _this.factoryForm.factoryLoading = false;
                       }
                   }).catch(function () {
                       _this.factoryForm.factoryLoading = false;
                       console.log(error);
                   });
               }
           },
           //点击下拉框时触发的事件
           factoryFocus:function () {
               let _this = this;
               let remoteMethodDom = document.getElementsByName('remoteMethod3');
               if(remoteMethodDom && remoteMethodDom.length){
                   remoteMethodDom[0].parentNode.parentNode.addEventListener('scroll', _this.scrollEvent,true);
               }
           },
           scrollEvent(e){
               //是否请求为true时
               if(this.factoryForm.isRequest){
                   /**
                      * 判断滚动条是否到达底部 如果到最底部调用 loadMore方法
                      * scrollTop 滚动条距顶部的长度
                      * clientHeight 当前显示的高度
                      * scrollHeight 滚动条总高度
                      */
                   if(e.currentTarget.scrollTop != 0){
                       if(e.currentTarget.scrollTop + e.currentTarget.clientHeight >= e.currentTarget.scrollHeight){
                           this.loadMore();
                       }
                   }
               }
           },
           loadMore(){
               this.factoryForm.pageIndex = this.factoryForm.pageIndex + 1;
               this.remoteMethod(this.factoryForm.codeOrName);
           }
       },

       //html
       template:`
          <el-dialog
            :title="!itemForm.id ? '新增' : '修改'"
            :close-on-click-modal="false"
            width="80%"
            :visible.sync="visible">
            <el-form label-position="top" :model="itemForm" :rules="rules" ref="itemForm">
            
                <el-row>
                    <el-col span="4">
                        <el-form-item label="门店报废单号" prop="recordCode">
                           <el-input  :disabled="true" v-model="itemForm.recordCode" placeholder="门店报废单号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col span="4">
                        <el-form-item label="工厂编号/名称" prop="factoryCode">
                           <el-select 
                           :disabled="itemForm.id?true:false" 
                           clearable 
                           filterable 
                           remote
                           :remote-method="remoteMethod" 
                           @change="getRealWareByFCode" 
                           @focus="factoryFocus"
                           v-model="itemForm.factoryCode" 
                           placeholder="工厂编号/名称">
                               <el-option name="remoteMethod3" v-for="item in factoryCode1" :key="item.value" :label="item.label" :value="item.value">
                           </el-option>
                           </el-select>
                           <span v-if="factoryForm.factoryLoading" class="el-icon-loading"></span>
                        </el-form-item>
                    </el-col>
                    <el-col span="4">
                        <el-form-item label="门店仓编号/名称" prop="realWarehouseId">
                           <el-select :disabled="itemForm.id?true:false" v-model="itemForm.realWarehouseId" placeholder="门店仓编号/名称" @change="setProdFormRWCode">
                               <el-option v-for="item in realWareCode" :key="item.value" :label="item.label" :value="item.value">
                           </el-option>
                           </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col span="6">
                        <el-form-item label="组织归属" prop="organizationCode">
                           <el-select disabled="true" style="width: 400px" ref="organizationName" v-model="itemForm.organizationCode" placeholder="组织归属">
                               <el-option v-for="item in organizationCode" :key="item.value" :label="item.label" :value="item.value">
                           </el-option>
                           </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col span="4">
                        <el-form-item label="业务原因" prop="reasonCode">
                           <el-select v-model="itemForm.reasonCode" placeholder="业务原因">
                               <el-option v-for="item in reasonCodeList" :key="item.reasonCode" :label="item.reasonName" :value="item.reasonCode">
                           </el-option>
                           </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                    
                <el-row>
                    <el-col span="12">
                        <el-form-item label="OA审批号" prop="approveOACode">
                           <el-input v-model="itemForm.approveOACode" placeholder="OA审批号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col span="12">
                        <el-form-item label="备注" prop="remark">
                           <el-input v-model="itemForm.remark" placeholder="请填写备注"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-form-item>
                    <el-button @click="addDetailRow()"  type="primary">新增行</el-button>
                </el-form-item>
                
                <el-table
                  :data="detailList"
                  border
                  max-height="500"
                  v-loading="dataListLoading"
                  element-loading-text="拼命加载中...">
                     <el-table-column fixed prop="skuId" width="200px" header-align="center" align="center" label="商品编号">
                        <template slot-scope="scope">
                            <el-select disabled="true" style="width: 180px;" v-model="scope.row.skuId" placeholder="商品编号">
                                <el-option v-for="item in skuList" :key="item.skuId" :label="item.skuCode" :value="item.skuId">
                                </el-option>
                            </el-select>
                        </template>
                     </el-table-column>
                     
                     <el-table-column fixed prop="skuName" width="200px" header-align="center" align="center" label="商品名称">
                        <template slot-scope="scope">
                            <el-select disabled="true" style="width: 180px;" v-model="scope.row.skuId" placeholder="商品名称">
                                <el-option v-for="item in skuList" :key="item.skuId" :label="item.skuName" :value="item.skuId">
                                </el-option>
                            </el-select>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="skuQty" width="160px" header-align="center" align="center" label="调整数量">
                        <template slot-scope="scope">
                            <el-input-number v-model="scope.row.skuQty" precision="3" size="small" controls-position="right" :min="0.001" @change="computeSkuqty(scope.row,scope.row.skuQty)"></el-input-number>
                        </template>
                     </el-table-column>
                     
                     <el-table-column  prop="unitCode" width="140px" header-align="center" align="center" label="调整单位">
                        <template slot-scope="scope">
                            <el-select v-model="scope.row.unitCode" placeholder="单位" @change="getBasicQty(scope.row,scope.row.unitCode)">
                                <el-option v-for="item in scope.row.skuUnitList" :key="item.unitCode" :label="item.unitName" :value="item.unitCode">
                                </el-option>
                            </el-select>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="skuStandard" width="140px" header-align="center" align="center" label="规格">
                        <template slot-scope="scope">
                            <el-input :disabled="true" v-model="scope.row.skuStandard"></el-input>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="remainStockQty" width="140px" header-align="center" align="center" label="库存数量">
                        <template slot-scope="scope">
                            <el-input :disabled="true" v-model="scope.row.remainStockQty"></el-input>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="basicQty" width="140px" header-align="center" align="center" label="基本数量">
                        <template slot-scope="scope">
                            <el-input :disabled="true" v-model="scope.row.basicQty"></el-input>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="basicUnit" width="140px" header-align="center" align="center" label="基本单位">
                        <template slot-scope="scope">
                            <el-input :disabled="true" v-model="scope.row.basicUnit"></el-input>
                        </template>
                     </el-table-column>
                     
                     <el-table-column prop="remark" width="140px" header-align="center" align="center" label="批次备注">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.remark"></el-input>
                        </template>
                     </el-table-column>
                     
                     <el-table-column fixed="right" width="140px" prop="operate" header-align="center" align="center" label="操作">
                        <template slot-scope="scope">
                            <el-button
                              @click.native.prevent="deleteDetailRow(scope.$index, detailList)"
                              type="text"
                              size="small">
                              移除
                            </el-button>
                        </template>
                     </el-table-column>
                     
                </el-table>
            </el-form>
            
            <el-dialog title="" :visible.sync="prodDialogVisible" append-to-body>
                <el-form :inline="true" size="small" :model="prodForm" class="demo-form-inline" style="overflow: hidden">
                    <el-col :span="6">
                    <el-input   v-model="prodForm.realWarehouseId" :disabled="true" style="display:none;"></el-input>
                        <el-form-item label="商品编码：" >
                            <el-input v-model="prodForm.skuCode" placeholder="" size="small"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" style="margin-top: 33px">
                        <el-button type="primary" size="small" @click="onQueryProd">查询</el-button>
                        <el-button type="primary" size="small" @click="resetProd">重置</el-button>
                    </el-col>
                </el-form>
                
                <el-button  type="primary" style="margin-bottom: 15px" @click="confrimTable">确认</el-button>
                   <el-table size="small" border :data="prodList"
                   v-loading="prodListLoading" element-loading-text="拼命加载中..."
                   @selection-change="handleSelectionChange">
                     <el-table-column type="selection" width="55">
                     </el-table-column>
                     <el-table-column prop="skuCode" label="商品编号">
                     </el-table-column>
                     <el-table-column prop="skuName" label="商品名称">
                     </el-table-column>
                     <el-table-column  prop="baseUnit" label="商品基本单位">
                     </el-table-column>
                     <el-table-column prop="useQty" label="可用数量">
                        <template scope="scope">
                            <span >{{(scope.row.realQty - scope.row.lockQty) }}</span>
                        </template>
                     </el-table-column>
                     <el-table-column prop="lockQty" label="锁定数量">
                     </el-table-column>
                  </el-table>
                <el-pagination
                    @size-change="sizeChangeHandle"
                    @current-change="currentChangeHandle"
                    :current-page="prodForm.pageIndex"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="prodForm.pageSize"
                    :total="prodForm.totalPage"
                    layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </el-dialog>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="itemFormSubmit()">提交</el-button>
              <el-button type="primary" @click="closeVisible">返回</el-button>
            </span>
            <addOrUpdate></addOrUpdate>
          </el-dialog>
        
       `
   })
});
