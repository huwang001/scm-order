lyfdefine(['disparity_duty','disparity_rejectHandle'], function (dutyMode,disparityMode) {
    return ({
        data: function data() {
            return {
                dataForm: {
                    recordType: 48,
                    pageIndex: 1,
                    totalPage: 0
                },
                loading: false,
                factoryCode:null,
                disparityDuty: false,
                disparityHandle: false,
                dataListLoading: false,
                loadingStr:"拼命加载中...",
                dataListSelections: [],
                dataList: [],
                multipleSelection: [],
                recordStatusList: [
                    {value: 0, label: '初始'},
                    {value: 1, label: '已定责'},
                    {value: 2, label: '待过账'},
                    {value: 3, label: '处理成功'},
                    {value: 4, label: '处理失败'}

                ],
                reasonsList: [
                    {value: 1, label: '实物在收货仓'},
                    {value: 2, label: '实物在发货仓'},
                    {value: 3, label: '实物丢失'},
                    {value: 4, label: '实物质量问题'},
                    {value: 5, label: '仓库漏发'}
                ],
                responsibleTypeList: [
                    {value: 1, label: '门店责任'},
                    {value: 2, label: '仓库责任'},
                    {value: 3, label: '物流责任'}

                ]
            };
        },
        mounted() {
            this.getDataList();
        },
        components: {
            'dutyMode': dutyMode,
            'disparityMode':disparityMode
        },
        methods: {
            // 获取数据列表
            getDataList: function getDataList() {
                let _this = this;
                if(_this.dataForm.sapDeliveryCode) {
                    //去掉空行
                    _this.dataForm.sapDeliveryCode = _this.dataForm.sapDeliveryCode.replace(/(\n|\r){2,}/g,"\n");
                    //去掉首尾换行
                    _this.dataForm.sapDeliveryCode = _this.dataForm.sapDeliveryCode.replace(/^(\n|\r)+|(\n|\r)+$/g,'');
                    let temp = Array.from(new Set(_this.dataForm.sapDeliveryCode.split(/\n|\r/g)));
                    if (temp.length > 50) {
                        _this.$message({
                            message: "传入的交货单号不能超过50个",
                            type: 'error',
                            duration: 2000,
                        });
                        return;
                    }
                }

                _this.loadingStr="拼命加载中...";
                this.dataListLoading = true;
                this.$http.post('/scm-order-admin/order/v1/disparity/record/query_by_condition', this.dataForm,
                ).then(function (_ref) {
                    let data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                        _this.buttonController();
                        if(data.list.length>0){
                            _this.factoryCode = data.list[0].outFactoryCode;
                        }
                    } else {
                        _this.disparityDuty = false;
                        _this.disparityHandle = false;
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                        _this.$message.error(_ref.data.msg);
                    }
                    _this.dataListLoading = false;
                });
            },
            // 每页数
            sizeChangeHandle: function sizeChangeHandle(val) {
                this.dataForm.pageSize = val;
                this.dataForm.pageIndex = 1;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle: function currentChangeHandle(val) {
                this.dataForm.pageIndex = val;
                this.getDataList();
            },
            resetForm: function () {
                var _this = this;
                Object.keys(_this.dataForm).forEach(function (key) {
                    if (key !== 'totalPage' && key !== 'pageIndex' && key !== "pageSize") {
                        _this.dataForm[key] = null;
                    }
                });

                _this.dataForm.recordType = 48;
            },

            formatRecordStatus(row, column, cellValue) {
                var _this = this;
                var columnValue = "";
                Object.keys(_this.recordStatusList).forEach(function (key) {
                    if (_this.recordStatusList[key].value == row.recordStatus) {
                        columnValue = _this.recordStatusList[key].label
                    }
                });
                return columnValue
            },
            formatReasons(row, column, cellValue) {
                var _this = this;
                var columnValue = "";
                Object.keys(_this.reasonsList).forEach(function (key) {
                    if (_this.reasonsList[key].value == row.reasons) {
                        columnValue = _this.reasonsList[key].label
                    }
                });
                return columnValue
            },
            formatResponsibleType(row, column, cellValue) {
                var _this = this;
                var columnValue = "";
                Object.keys(_this.responsibleTypeList).forEach(function (key) {
                    if (_this.responsibleTypeList[key].value == row.responsibleType) {
                        columnValue = _this.responsibleTypeList[key].label
                    }
                });
                return columnValue
            },
            handleSelectionChange: function (val) {
                this.multipleSelection = val;

            },
            duty: function () {
                let _this = this;
                if (0 == this.multipleSelection.length) {
                    this.$message({
                        message: '请至少选择一个明细',
                        type: 'warn'
                    });
                    return;
                }
                let factoryCode = this.multipleSelection[0].outFactoryCode;
                for (let i = 1; i < this.multipleSelection.length; i++) {
                    if (factoryCode != this.multipleSelection[i].outFactoryCode) {
                        _this.$message({
                            message: '只能选择相同工厂下的明细进行定责',
                            type: 'warn'
                        });
                        return;
                    }
                }


                this.$http.post('/scm-order-admin/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeNoShop', factoryCode).then(function (_ref2) {
                    let data = _ref2.data;
                    if (data && data.code === '0') {
                        let realWarehouseList = data.data;
                        if (!realWarehouseList || realWarehouseList.length == 0) {
                            _this.$message({
                                message: '根据工厂查询仓库出现异常',
                                type: 'warn'
                            });
                            return;
                        }
                        _this.$refs.dutyMode.init(1, _this.multipleSelection,realWarehouseList);
                    }
                }).catch(function (error) {
                    console.log(error);
                });

            },
            //模糊查询
            querySearch: function (queryString, cb) {
                let _this = this;
                let list = [];
                this.$http.post('/scm-order-admin/order/v1/whAllocation/queryForAdmin?name=' + queryString).then(function (res) {
                    if (res.status == '200') {
                        for (let i of res.data.data) {
                            i.value = i.realWarehouseName;   //将想要展示的数据作为value
                        }
                    }
                    list = res.data.data;
                    cb(list);
                }).catch(function (error) {
                    _this.$message('查询失败！');
                    cb();
                });
            },
            //出库选中
            handleSelectOut(item) {
                this.dataForm.outWarehouseId = item.id;
            },
            //入库选中
            handleSelectIn(item) {
                this.dataForm.inWarehouseId = item.id;
            },
            handle: function () {
                let _this2 = this;
                if (0 == this.multipleSelection.length) {
                    this.$message({
                        message: '请至少选择一个明细',
                        type: 'warn'
                    });
                    return;
                }
                let ids=[];
                this.multipleSelection.forEach(function (value) {
                    ids.push(value.id);
                });
                _this2.$confirm('确定执行过账操作吗, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this2.loadingStr="差异处理中，耗时操作，请耐心等待...";
                    _this2.dataListLoading = true;
                    _this2.$http.post('/scm-order-admin/order/v1/disparity/record/handlerDisparity',ids)
                        .then(function (_ref2) {
                            _this2.dataListLoading = false;
                            let data = _ref2.data;
                            if (data && data.code === '0') {
                                _this2.$message({
                                    message: '操作成功',
                                    type: 'success',
                                    duration: 800

                                });
                                _this2.getDataList();

                            } else if (data && data.code != 0) {
                                _this2.$message({
                                    message: data.msg,
                                    type: 'error',
                                    duration: 2500,
                                });
                                _this2.getDataList();
                            }
                        }).catch(function (error) {
                        _this2.dataListLoading = false;
                        console.log(error);
                    });
                })
            },
            //定责按钮和过账按钮显示控制
            buttonController: function () {
                console.log("====" + this.dataForm);
                //只有当用户选种了交货单号 ，并且该单子处于已定责状态【可能修改责任原因】或初始状态时，才能定责
                //只有当用户选中了交货单号，并且该单子处于已定责状态或处理失败时，才能确认过账
                this.disparityDuty = true;
                this.disparityHandle = true;
                // this.disparityDuty = false;
                // this.disparityHandle = false;
                // if (this.dataForm.sapDeliveryCode && this.dataList && this.dataList.length>0) {
                //     if (this.dataList[0].recordStatus == 0 || this.dataList[0].recordStatus == 1) {
                //         this.disparityDuty = true;
                //     }
                //     if (this.dataList[0].recordStatus == 1 || this.dataList[0].recordStatus == 4) {
                //         this.disparityHandle = true;
                //     }
                // }
            },



            exportExcel: function () {
                var _this = this;
                if (!this.dataForm.createStartDate || !this.dataForm.createEndDate) {
                    this.$message({
                        message: '导出数据时必须限定创建时间段且跨度在30天以内!',
                        type: 'warn'
                    });
                    return;
                }
                let start = new Date(this.dataForm.createStartDate);
                let end = new Date(this.dataForm.createEndDate);
                let max = 3600 * 24 * 30 *1000;
                if(end.getTime() - start.getTime() > max){
                    this.$message({
                        message: '时间跨度必须在30天以内!',
                        type: 'warn'
                    });
                    return;
                }


                _this.exportButtonDisabled = 1;
                _this.loading = true;
                this.$http({
                    method: "post",
                    url: "/scm-order-admin/order/v1/disparity/record/exportDisparityRecords",
                    data: _this.dataForm,
                    responseType: 'blob'
                })
                    .then(function (res) {
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
                            _this.loading = false;
                        }
                    }).catch(function (error) {
                    _this.loading = false;
                    _this.exportButtonDisabled = 0;
                    console.log(error);
                });
            },



            rejectConfirmDisparity: function rejectConfirmDisparity() {
                this.$refs.disparityMode.init();

            },
            checkboxT(row, rowIndex) {
                //let b = row.sapOrderCode == '' || row.sapOrderCode == null;
                if ( row.recordStatus == 4 ||row.recordStatus == 3) {
                    return false;//禁用
                } else {
                    return true;//不禁用
                }
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
            }
        },
        template: /*html*/`
 <div class="mod-config">
   <el-form :inline="true" :model="dataForm" ref="dataForm" > <!--@keyup.enter.native="getDataList()">-->
    
       <el-form-item label="交货单号：">
        <el-input v-model="dataForm.sapDeliveryCode" placeholder="多个换行分隔,空行将被忽略,最多50" type="textarea" :autosize="{ minRows: 1, maxRows: 10}"  clearable></el-input>
      </el-form-item>
       <el-form-item label="责任方：" prop="responsibleType">
            <el-select v-model="dataForm.responsibleType" placeholder="责任方" clearable>
                <el-option v-for="item in responsibleTypeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
        </el-select>
      </el-form-item>
     
       <el-form-item label="出库仓库" >
               <el-input v-model="dataForm.outRealWarehouseCode" placeholder="出库仓库" clearable></el-input>
                       </el-form-item>
        <el-form-item label="入库仓库" >
                       <el-input v-model="dataForm.inRealWarehouseCode" placeholder="入库仓库" clearable></el-input>
                       </el-form-item>
      <!--<el-form-item label="前置单据号：">-->
        <!--<el-input v-model="dataForm.frontRecordCode" placeholder="前置单据号" clearable></el-input>-->
      <!--</el-form-item>-->
      
      <el-form-item label="物料编码：">
        <el-input v-model="dataForm.skuCode" placeholder="物料编码" clearable></el-input>
      </el-form-item>
      
      
      <!--- 1010新增查询条件 start -->
        <el-form-item label="更新人" >
                            <el-input v-model="dataForm.empNum" placeholder="请输入工号" clearable/>
                        </el-form-item>
                        
                        <el-form-item label="单状态" prop="recordStatus">
                            <el-select v-model="dataForm.recordStatus" placeholder="单状态" clearable>
                            <el-option key="0" label="初始" value="0"/>
                                <el-option key="1" label="已定责" value="1"/>
                                <el-option key="3" label="过账成功" value="3"/>
                            </el-select>
                        </el-form-item>
                        
                           <el-form-item label="发货工厂" >
                            <el-input v-model="dataForm.factoryCode" placeholder="发货工厂" clearable/>
                        </el-form-item>
      
      
      <el-form-item label="创建日期">
                            <el-col :span="11">
                                <el-form-item prop="createStartDate">
                                    <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.createStartDate" style="width: 100%;"></el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col class="line" :span="1">-</el-col>
                            <el-col :span="11">
                                 <el-form-item prop="createEndDate">
                                     <el-date-picker type="datetime" default-time="23:59:59" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.createEndDate" style="width: 100%;"></el-date-picker>
                                 </el-form-item>
                            </el-col>
                        </el-form-item>
                        
         <el-form-item label="更新日期">
                            <el-col :span="11">
                                <el-form-item prop="updateStartDate">
                                    <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.updateStartDate" style="width: 100%;"></el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col class="line" :span="1">-</el-col>
                            <el-col :span="11">
                                 <el-form-item prop="updateEndDate">
                                     <el-date-picker type="datetime" default-time="23:59:59" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.updateEndDate" style="width: 100%;"></el-date-picker>
                                 </el-form-item>
                            </el-col>
                        </el-form-item>
        <!--- 1010新增查询条件 end-->
      
      <el-form-item>
        <el-button @click="getDataList()"  type="primary">查询</el-button>
        <el-button  @click="resetForm()">重置</el-button>
        <el-button   @click="exportExcel">导出</el-button><i v-show="loading" class="el-icon-loading"></i>
        <el-button   @click="rejectConfirmDisparity"  type="primary">整单拒收落差异单批处理</el-button>
      </el-form-item>
    </el-form>
    
    <el-form>
     <el-form-item>
          <el-button :disabled=!disparityDuty @click="duty()"  type="primary">责任判断</el-button>
          <el-button :disabled=!disparityHandle @click="handle()" type="primary">确认过账</el-button>
     </el-form-item>
    
    <el-table max-height="550" :data="dataList" border v-loading="dataListLoading"
    ref="multipleTable"
    @selection-change="handleSelectionChange"
     :element-loading-text="loadingStr" style="width: 100%;">
         <el-table-column
                      type="selection" :selectable='checkboxT'
                      width="55">
          </el-table-column>
          <el-table-column prop="id" header-align="center" align="center" label="差异明细id"/>
        <el-table-column prop="recordCode" header-align="center" align="center" label="差异单号"/>
        <el-table-column prop="frontRecordCode" header-align="center" align="center" label="前置单号"/>
        <el-table-column prop="sapPoNo" header-align="center" align="center" label="SAP采购单号"/>
        <el-table-column prop="sapDeliveryCode" header-align="center" align="center" label="SAP交货单号"/>
        <el-table-column prop="inWarehouseRecordCode" header-align="center" align="center" label="入库单号"/>
        <el-table-column prop="outWarehouseRecordCode" header-align="center" align="center" label="出库单号"/>
        <el-table-column prop="shopCode" header-align="center" align="center" label="门店编码"/>
        <el-table-column prop="outRealWarehouseName" header-align="center" align="center" label="出库仓库"/>
        <el-table-column prop="inRealWarehouseName" header-align="center" align="center" label="入库仓库"/>
        <el-table-column prop="recordStatus" header-align="center" align="center" label="单据状态" :formatter="formatRecordStatus"/>
        <el-table-column prop="lineNo" header-align="center" align="center" label="po行号"/>
        <el-table-column prop="deliveryLineNo" header-align="center" align="center" label="交货行号"/>
        <el-table-column prop="skuCode" header-align="center" align="center" label="商品编码"/>
        <el-table-column prop="skuName" header-align="center" align="center" label="商品名称"/>
        <el-table-column prop="outSkuQty" header-align="center" align="center" label="出库数量[基本单位]"/>
        <el-table-column prop="inSkuQty" header-align="center" align="center" label="入库数量[基本单位]"/>
        <el-table-column prop="skuQty" header-align="center" align="center" label="差异数量[基本单位]"/>
        <el-table-column prop="unit" header-align="center" align="center" label="单位[基本单位]"/>
        
        <el-table-column prop="realOutSkuQty" header-align="center" align="center" label="出库数量"/>
        <el-table-column prop="realInSkuQty" header-align="center" align="center" label="入库数量"/>
        <el-table-column prop="realSkuQty" header-align="center" align="center" label="差异数量"/>
        <el-table-column prop="realUnit" header-align="center" align="center" label="单位"/>
        
        <el-table-column prop="responsibleType" header-align="center" align="center" label="责任方" :formatter="formatResponsibleType"/>
        <el-table-column prop="reasons" header-align="center" align="center" label="责任原因" :formatter="formatReasons"/>
        
        <el-table-column prop="remark" header-align="center" align="center" label="备注" />
        <el-table-column prop="costCenter" header-align="center" align="center" label="成本中心" />
        <el-table-column prop="handlerInRealWarehouseName" header-align="center" align="center" label="差异处理入库仓" />
        
        
        <el-table-column prop="createTime" header-align="center" align="center" label="创建时间"/>
        <el-table-column prop="updateTime" header-align="center" align="center" label="更新时间"/>
        <el-table-column prop="createEmployeeNumber" header-align="center" align="center" label="创建人"/>
        <el-table-column prop="employeeNumber" header-align="center" align="center" label="更新人"/>

        <!--<el-table-column fixed="right"  prop="操作" header-align="center" align="center" width="150" label="操作">-->
        </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="dataForm.pageIndex"
      :page-sizes="[10, 20, 50, 100 , 500]" :page-size="dataForm.pageSize" :total="dataForm.totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
     </el-form>
    <dutyMode v-if="true" ref="dutyMode" @refreshDataList="getDataList"></dutyMode>
    <disparityMode v-if="true" ref="disparityMode" @refreshDataList="getDataList"></disparityMode>

    </div>
    `
    });
});
