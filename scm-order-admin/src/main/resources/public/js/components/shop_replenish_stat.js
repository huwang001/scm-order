//门店零售详情
lyfdefine(function () {
    return ({
        data: function () {
            return {
                dataForm: {
                    sapPoNo: null,
                    sapOrderCode: null,
                    factoryCode: null,
                    realWarehouseCode: null,
                    skuCode: null,
                    shopCode: null,
                    startTime: null,
                    endTime: null,
                    pageSize: 10,
                    pageIndex: 1
                },
                dataList: [],
                visible: false,
                dataListLoading: false
            }
        },

        methods: {
            init: function init(dataForm) {
                var _this = this;
                _this.dataForm.sapPoNo = dataForm.sapPoNo;
                _this.dataForm.sapOrderCode = dataForm.sapOrderCode;
                _this.dataForm.factoryCode = dataForm.factoryCode;
                _this.dataForm.realWarehouseCode = dataForm.realWarehouseCode;
                _this.dataForm.skuCode = dataForm.skuCode;
                _this.dataForm.shopCode = dataForm.shopCode;
                _this.dataForm.startTime = dataForm.startTime;
                _this.dataForm.endTime = dataForm.endTime;
                this.visible = true;

                _this.getDataList();
            },
            // 获取数据列表
            getDataList: function getDataList() {
                var _this = this;
                _this.dataListLoading = true;
                this.$http.post('/scm-order-admin/order/v1/shopReplenish/statReplenishReport',
                    _this.dataForm
                ).then(function (_ref) {
                    _this.dataListLoading = false;
                    var data = _ref.data.data;
                    if (data && data.list) {
                        _this.dataList = data.list;
                        _this.dataForm.totalPage = data.total;
                    } else {
                        _this.dataList = [];
                        _this.dataForm.totalPage = 0;
                    }
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
        },
        template: `
       <div>
          <el-dialog :title="'汇总交单情况'" :close-on-click-modal="false" width="70%" style="font-size: 14px" :visible.sync="visible">
              <el-table max-height="550" :data="dataList" border v-loading="dataListLoading" ref="multipleTable" element-loading-text="拼命加载中..." style="width: 100%;">
                  <el-table-column prop="factoryCode" header-align="center" align="center" label="工厂编号">
                  </el-table-column>
                  
                  <el-table-column prop="factoryName" header-align="center" align="center" label="工厂名称">
                  </el-table-column>
                  
                  <el-table-column prop="outRealWarehoseCode" header-align="center" align="center" label="仓库编号">
                  </el-table-column>
            
                  <el-table-column prop="outRealWarehouseName" header-align="center" align="center" label="仓库名称">
                  </el-table-column>
                  
                  <el-table-column prop="recordTypeName" header-align="center" align="center" label="采购单据类型">
                  </el-table-column>
                  
                  <el-table-column prop="requireTypeName" header-align="center" align="center" label="配货类型">
                  </el-table-column>
                  
                  <el-table-column prop="poCount" header-align="center" align="center" label="PO单数">
                  </el-table-column>
                  
                  <el-table-column prop="outRecordCount" header-align="center" align="center" label="中台出库单数">
                  </el-table-column>
                  
                  <el-table-column prop="sapOrderCount" header-align="center" align="center" label="SAP交货单数">
                  </el-table-column>
              </el-table>
              
              <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="dataForm.pageIndex" 
                  :page-sizes="[2,10, 20, 50, 100]" :page-size="dataForm.pageSize" :total="dataForm.totalPage" layout="total, sizes, prev, pager, next, jumper">
              </el-pagination>
          </el-dialog>
       </div>
        `
    });
});