//门店零售详情
lyfdefine(function () {
    return ({
        data: function () {
            return {
                data: [],
                visible: false,
                showType: ""
            }
        },

        methods: {
            init: function init(recordId, showType) {
                console.log(recordId + ", " + showType)
                var _this = this;
                this.visible = true;
                this.showType = showType;

                var url = null;
                if(showType == "wms") {
                    url = "/stock-admin/stock/v1/warehouse_sale_tob/getSyncWmsLog/";
                } else if(showType == "dispatch") {
                    url = "/stock-admin/stock/v1/warehouse_sale_tob/getSyncDispatchLog/";
                } else if(showType == "transfer") {
                    url = "/stock-admin/stock/v1/warehouse_sale_tob/getSyncTransferLog/";
                }
                this.$http({
                    url: url,
                    method: 'post',
                    params: _this.$http.adornParams({
                        "warehouseRecordId": recordId,
                    })
                }).then(function (_ref) {
                    var data = _ref.data.data;
                    if (data) {
                        console.log(data);
                        _this.data = data;
                    } else {
                        _this.data = {};
                    }
                });
            },
            formatOrderCode: function formatOrderCode(value) {
                if (value) {
                    return value.join(",");
                }
                return "";
            }
        },
        template: `
       <div>
          <el-dialog
            :title="'详情'"
            :close-on-click-modal="false"
            width="70%"
           style="font-size: 14px"
            :visible.sync="visible">
                <template v-if="showType === 'wms'">
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="5"><div style="font-weight: bold">同步WMS详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="8">
                            <el-form-item label="同步WMS状态：">
                                {{data.syncWmsStatusName}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="同步时间：">
                                {{data.syncWmsLogTime}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="同步WMS详情：">
                                {{data.syncWmsLog}}
                            </el-form-item>
                        </el-col>
                    </el-form>
                </template>
                <template v-else-if="showType === 'dispatch'">
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="5"><div style="font-weight: bold">sap派车详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="8">
                            <el-form-item label="sap派车状态：">
                                {{data.syncDispatchStatusName}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="同步时间：">
                                {{data.syncDispatchLogTime}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="sap派车详情：">
                                {{data.syncDispatchLog}}
                            </el-form-item>
                        </el-col>
                    </el-form>
                </template>
                <template v-else-if="showType === 'transfer'">
                    <el-row type="flex" justify="start" style="line-height:30px;height: 40px;font-size: 14px">
                        <el-col :span="5"><div style="font-weight: bold">sap过账详情</div></el-col>
                    </el-row>
                    <el-form :inline="true"   class="demo-form-inline">
                        <el-col  :span="8">
                            <el-form-item label="sap过账状态：">
                                {{data.syncTransferStatusName}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="同步时间：">
                                {{data.syncTransferLogTime}}
                            </el-form-item>
                        </el-col>
                        <el-col  :span="8">
                            <el-form-item label="sap过账详情：">
                                {{data.syncTransferLog}}
                            </el-form-item>
                        </el-col>
                    </el-form>
                </template>
                
                <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                
                </div>  
            </el-dialog>
       </div>
        `
    });
});