lyfdefine(function () {
    return ({
        data: function data() {
            return {
                visible: false,
                width:"90%",
                type:1,////*type=1 补货差异单  type=2 退货差异单*/
                radio: "",
                handlerInRealWarehouseId:null,
                handlerInRealWarehouseId_logistic:null,
                handlerInRealWarehouseId_house:null,
                remark:null,
                needShowHouse:false,
                needShowLogistic:false,
                costCenter:null,
                multipleSelection: [],
                realWarehouseList:[],
            };

        },

        methods: {

            handle: function () {
                let _this2=this;
                console.log("---------"+this.radio);
                if (!this.radio) {
                    this.$message({
                        message: '请选择差异原因!',
                        type: 'warn'
                    });
                    return;
                }
                let responsibleType = _this2.radio.split("-")[0];
                let reasons = _this2.radio.split("-")[1];
                if (responsibleType == '1') {
                    _this2.handlerInRealWarehouseId = null;
                } else if (responsibleType == '2') {
                    _this2.handlerInRealWarehouseId = _this2.handlerInRealWarehouseId_house;
                    if(!_this2.handlerInRealWarehouseId && _this2.type == 1){
                        this.$message({
                            message: '仓库责任必须选择仓库!',
                            type: 'warn'
                        });
                        return;
                    }
                } else if (responsibleType == '3') {
                    _this2.handlerInRealWarehouseId = _this2.handlerInRealWarehouseId_logistic;
                    if(!_this2.handlerInRealWarehouseId && _this2.type == 1){
                        _this2.$message({
                            message: '物流责任必须选择仓库!',
                            type: 'warn'
                        });
                        return;
                    }
                    if(!_this2.costCenter || !_this2.costCenter){
                        _this2.$message({
                            message: '物流责任时备注、成本中心都必填!',
                            type: 'warn'
                        });
                        return;
                    }
                }

                _this2.$confirm('确定执行操作吗, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log("--------------------:responsible_type="+responsibleType+",reasons="+reasons)
                    _this2.multipleSelection.forEach(function (row) {
                        row.responsibleType = responsibleType;
                        row.reasons=reasons;
                        row.handlerInRealWarehouseId = _this2.handlerInRealWarehouseId;
                        row.remark=_this2.remark;
                        row.costCenter = _this2.costCenter
                    });
                    //调用确认单接口，根据单据id修改单子状态为确认
                    _this2.$http.post('/scm-order-admin/order/v1/disparity/record/disparityDuty',_this2.multipleSelection).then(function (_ref2) {
                        let data = _ref2.data;
                        if (data && data.code === '0') {
                            _this2.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 800,
                                onClose: function onClose() {
                                    _this2.visible = false;
                                    _this2.radio="";
                                    _this2.remark= null;
                                    _this2.costCenter =null;
                                    _this2.needShowHouse = false;
                                    _this2.needShowLogistic =false;
                                    _this2.$emit('refreshDataList');
                                }
                            });
                        } else if (data && data.code != 0) {
                            _this2.$message({
                                message: data.msg,
                                type: 'error',
                                duration: 800,
                                onClose: function onClose() {
                                    _this2.visible = false;
                                    _this2.radio="";
                                    _this2.remark= null;
                                    _this2.costCenter =null;
                                    _this2.needShowHouse = false;
                                    _this2.needShowLogistic =false;
                                    _this2.$emit('refreshDataList');

                                }
                            });
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                })
            },
            changeRadio: function () {
                this.handlerInRealWarehouseId_logistic = null;
                this.handlerInRealWarehouseId_house = null;
                this.needShowHouse = false;
                this.needShowLogistic = false;
                if (this.radio) {
                    if (this.radio == '3-3') {
                        //选中了物流责任按钮
                        this.handlerInRealWarehouseId_logistic = this.handlerInRealWarehouseId;
                        this.needShowLogistic = true;
                    } else {
                        this.remark = null;
                        this.costCenter = null;
                        if (this.radio == '2-4' || this.radio == '2-5' || this.radio == '2-1') {
                            //选中了仓库责任按钮
                            this.handlerInRealWarehouseId_house = this.handlerInRealWarehouseId;
                            this.needShowHouse = true;
                        }
                    }
                }
            },
            init: function (type,multipleSelection,realWarehouseList) {
                console.log(12331333333333);
                this.type = type;
                if (type == 1) {
                    this.width = "90%";
                } else {
                    this.width = "70%";
                }
                this.multipleSelection=multipleSelection;
                this.visible = true;
                //this.realWarehouseList = realWarehouseList;
                if (realWarehouseList) {
                    for (let i = 0; i < realWarehouseList.length; i++) {
                        // 12 退货仓 13退货中转仓 20 其他
                        realWarehouseList[i].realWarehouseRemark = realWarehouseList[i].realWarehouseCode + '/' + realWarehouseList[i].realWarehouseName;
                        if (realWarehouseList[i].realWarehouseType == 12 ||
                            realWarehouseList[i].realWarehouseType == 13 ||
                            realWarehouseList[i].realWarehouseType == 20) {
                            this.realWarehouseList.push(realWarehouseList[i]);
                            if (realWarehouseList[i].realWarehouseType == 12) {
                                this.handlerInRealWarehouseId = realWarehouseList[i].id;
                                this.handlerInRealWarehouseId_logistic = realWarehouseList[i].id;
                                this.handlerInRealWarehouseId_house = realWarehouseList[i].id;
                            }
                        }

                    }
                }

                console.log("======"+realWarehouseList.length)
            }
        },
        template: /*html*/`
  <el-dialog
            :width="width"
            :title="'责任判断'"
            :visible.sync="visible">
            <el-form label-position="left" :inline="true" >
                <div v-if="type==1" style="font-size: 16px;margin-left: 50px">
                
                     <el-row style="height: 50px">
                         <el-col :span="22"><el-form-item label="门店责任：" >
                            <el-radio-group v-model="radio" @change="changeRadio">
                            <el-radio :label="'1-1'" style="margin-right: 30px;line-height: 36px;">实物在收货仓</el-radio>
                            </el-radio-group>
                         </el-col>
                      </el-row>
                      <el-row style="height: 50px">
                           <el-col :span="8">
                                <el-form-item label="仓库责任：" >
                                   <el-radio-group v-model="radio" @change="changeRadio">
                                   <el-radio :label="'2-4'" style="margin-right: 30px;line-height: 36px">实物质量问题</el-radio>
                                   <el-radio :label="'2-5'" style="margin-right: 30px;line-height: 36px">仓库漏发</el-radio>
                                   </el-radio-group>
                               </el-form-item>
                           </el-col>
                            <el-col :span="14">
                                 <el-form-item label="选择仓库" >
                                        <el-select :disabled=!needShowHouse v-model="handlerInRealWarehouseId_house"  placeholder="选择仓库" filterable clearable >
                                            <el-option v-for="item in realWarehouseList" :key="item.id"   :label="item.realWarehouseRemark" :value="item.id">
                                        </el-option>
                                       </el-select>
                                  </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row style="height: 50px">
                            <el-col :span="4">
                                <el-form-item label="物流责任：" >
                                    <el-radio-group v-model="radio" @change="changeRadio">
                                        <el-radio :label="'3-3'" style="margin-right: 30px;line-height: 36px">实物丢失</el-radio>
                                     </el-radio-group>
                                </el-form-item>
                            </el-col>
                             <el-col :span="6">
                                 <el-form-item label="选择仓库" >
                                    <el-select :disabled=!needShowLogistic v-model="handlerInRealWarehouseId_logistic"  placeholder="选择仓库" filterable clearable >
                                        <el-option v-for="item in realWarehouseList" :key="item.id"   :label="item.realWarehouseRemark" :value="item.id">
                                    </el-option>
                                </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="备注" >
                                    <el-input :disabled=!needShowLogistic  v-model="remark" placeholder="备注" clearable></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="成本中心"  >
                                    <el-input :disabled=!needShowLogistic  v-model="costCenter"  placeholder="成本中心" clearable></el-input>
                                </el-form-item>
                             </el-col>
                         </el-row>
                     <!--</div>-->
                 </div>
                 <div v-if="type==2" style="font-size: 16px;margin-left: 50px">
                
                       <el-row style="height: 50px">
                         <el-col :span="22"><el-form-item label="门店责任：" >
                            <el-radio-group v-model="radio" @change="changeRadio">
                            <el-radio :label="'1-2'" style="margin-right: 30px;line-height: 36px">实物在发货仓</el-radio>
                            </el-radio-group>
                         </el-col>
                      </el-row>
                       <el-row style="height: 50px">
                           <el-col :span="22">
                                <el-form-item label="仓库责任：" >
                                   <el-radio-group v-model="radio" @change="changeRadio">
                                   <el-radio :label="'2-1'" style="margin-right: 30px;line-height: 36px">实物在收货仓</el-radio>
                                   </el-radio-group>
                               </el-form-item>
                           </el-col>
                        </el-row>
                     
                     <el-row style="height: 50px">
                            <el-col :span="6">
                                <el-form-item label="物流责任：" >
                                    <el-radio-group v-model="radio" @change="changeRadio">
                                        <el-radio :label="'3-3'" style="margin-right: 30px;line-height: 36px">实物丢失</el-radio>
                                     </el-radio-group>
                                </el-form-item>
                            </el-col>
                          
                            <el-col :span="8">
                                <el-form-item label="备注" >
                                    <el-input :disabled=!needShowLogistic  v-model="remark" placeholder="备注" clearable></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="成本中心"  >
                                    <el-input :disabled=!needShowLogistic  v-model="costCenter"  placeholder="成本中心" clearable></el-input>
                                </el-form-item>
                             </el-col>
                     </el-row>
                 </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="handle">确认</el-button>
            </span>
          </el-dialog>
    `
    });
});
