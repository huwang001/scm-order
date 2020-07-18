lyfdefine(['validate'],function(){
   return ({
       data: function () {
           return {
               visible: false,
               recordType: 39,//单据类型-门店报废
               itemForm: {
                   realWarehouseCode: '',
                   factoryCode: '',
                   recordCode: '',
                   reasonCode: '',
                   organizationName: '',
                   approveOACode: '',
                   remark: '',
                   sapRecordCode:'',
               },
               reasonCodeList:{},
               detailList:[],
               adjustList:[],
               unitList:{},
               basicUnitList:{},
               remainStockQtyList:{},
               dataListLoading: false,
           }

       },

       //方法区
       methods: {
           init: function init(id,code) {
               let _this = this;
               this.recordType=code;
               this.detailList=[];
               this.itemForm.id = id || 0;
               this.visible = true;

               //若id不为空，调用后台接口查询
               if(0 != _this.itemForm.id){
                   _this.dataListLoading = true;
                   _this.$http.get('/stock-admin/admin/v1/shop_consume/selectShopConsumeRecordDetail?id='+id).then(function (_ref) {
                       let data = _ref.data.data;
                       if (data) {
                           _this.itemForm.realWarehouseCode = data.realWarehouseCode +"/" + data.realWarehouseName;
                           _this.itemForm.factoryCode = data.factoryCode;
                           _this.itemForm.recordCode = data.recordCode;
                           _this.itemForm.sapRecordCode = data.sapRecordCode;
                           _this.itemForm.reasonCode = data.reasonCode;
                           _this.itemForm.organizationName = data.organizationName;
                           _this.itemForm.approveOACode = data.approveOACode;
                           _this.itemForm.remark = data.remark;

                           _this.detailList = data.detailDTOS;
                           _this.detailList.forEach(function (val) {
                               val.skuStandard = '1X' + val.scale;
                           })
                           _this.dataListLoading = false;
                       } else {
                           _this.detailList = [];
                           _this.dataListLoading = false;
                       }
                   }).catch(function (error) {
                       console.log(error);
                       _this.dataListLoading = false;
                   });
               }
               this.getReasonList();
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
                       data.forEach(function (val) {
                           _this.reasonCodeList[val.reasonCode] = val.reasonName;
                       });
                   } else {
                       _this.reasonCodeList = {};
                   }
               }).catch(function (error) {
                   console.log(error);
               });;
           },
           closeVisible:function(){
               this.visible = false;
               this.$refs['itemForm'].resetFields();
           },

       },


       //html
       template:`
          <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="80%"
            :visible.sync="visible">
            <el-form label-position="left" :model="itemForm" ref="itemForm">
            
                <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="3"><div style="font-weight: bold">门店报废单详情</div></el-col>
                </el-row>
                <el-col span="8">
                    <el-form-item label="门店报废单号:" prop="recordCode">{{itemForm.recordCode}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="SAP过账单号:" prop="recordCode">{{itemForm.sapRecordCode}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="工厂编号:" prop="factoryCode">{{itemForm.factoryCode}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="门店仓编号/名称:" prop="realWarehouseCode">{{itemForm.realWarehouseCode}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="业务原因:" prop="reasonCode">{{reasonCodeList[itemForm.reasonCode]}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="组织归属:" prop="organizationName">{{itemForm.organizationName}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="OA审批号:" prop="approveOACode">{{itemForm.approveOACode}}</el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item label="备注:" prop="remark">{{itemForm.remark}}</el-form-item>
                </el-col>
                
                <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                    <el-col :span="3"><div style="font-weight: bold">商品明细</div></el-col>
                </div>
                <el-table
                  :data="detailList"
                  border
                  max-height="500"
                  v-loading="dataListLoading"
                  element-loading-text="拼命加载中...">
                     <el-table-column fixed prop="skuCode" header-align="center" align="center" label="商品编号">
                     </el-table-column>
                     
                     <el-table-column fixed prop="skuName" header-align="center" align="center" label="商品名称">
                     </el-table-column>
                     
                     <el-table-column prop="skuQty" header-align="center" align="center" label="调整数量">
                     </el-table-column>
                     
                     <el-table-column  prop="unit" header-align="center" align="center" label="调整单位">
                     </el-table-column>
                     
                     <el-table-column prop="actualQty" header-align="center" align="center" label="实际出库数量">
                     </el-table-column>
                     
                     <el-table-column prop="skuStandard" header-align="center" align="center" label="规格">
                     </el-table-column>
                     
                     <el-table-column prop="basicQty" header-align="center" align="center" label="基本数量">
                     </el-table-column>
                     
                     <el-table-column prop="basicUnit" header-align="center" align="center" label="基本单位">
                     </el-table-column>
                     
                     <el-table-column prop="remark" header-align="center" align="center" label="批次备注">
                     </el-table-column>
                </el-table>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="closeVisible">返回</el-button>
            </span>
          </el-dialog>
        
       `
   })
});
