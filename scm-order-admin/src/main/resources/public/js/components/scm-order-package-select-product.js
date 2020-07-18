lyfdefine([],function(){
    return ({
        props: {
            visible: {
                type: Boolean,
                default: false
            },
            callback: {
                type: Function,
                default: () => {}
            }
        },
        data() {
            return {
                POOL_NAME: "/scm-order-admin",
                timer: null,
                tableData: [],
                selectedTableData: [],
                paginationOption: {
                    total: 0,
                    page: 1,
                    pageSize: 10
                },
                filterForm: {
                    skuCode: '', // 商品编码
                    skuTypeCode: '', // 商品类型
                },
                formatCombineType: (row) => {
                    const combineType = this.filterFormOption.combineTypes.find(n => n.value === row.combineType)
                    if (combineType) {
                        return combineType.label
                    }
                    return row.combineType
                },
                filterFormOption: {
                    combineTypes: [{
                        value: 0,
                        label: '单sku'
                    },
                        {
                            value: 1,
                            label: '组合'
                        },
                        {
                            value: 2,
                            label: '组装'
                        },
                        {
                            value: 3,
                            label: '现制现售'
                        }
                    ],
                    productTypes: []
                }
            };
        },
        methods: {
            init() {
                // 获取商品类型
                this.$http({
                    method: 'get',
                    url: this.POOL_NAME + '/order/v1/demand/skuType'
                }).then(res => {
                    if (res.data.code === '0' && Array.isArray(res.data.data)) {
                        this.filterFormOption.productTypes = res.data.data
                    }
                }, () => {})
            },
            onClickSearchBtn() {
                clearTimeout(this.timer);
                this.paginationOption.page = 1;
                this.timer = setTimeout(() => {
                    this.fetchTableData();
                }, 300)
            },
            onClickResetBtn() {
                this.$refs.filterForm.resetFields()
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            onClickConfirm() {
                this.timer = setTimeout(() => {
                    const selectedTableData = this.selectedTableData.map(n => {
                        return {
                            combineType: n.combineType,
                            skuCode: n.skuCode,
                            skuName: n.name,
                            skuUnitList: Array.isArray(n.skuUnitExtDTO) ? n.skuUnitExtDTO.map(c => {
                                return {
                                    basicUnitName: c.basicUnitName,
                                    basicUnitCode: c.basicUnitCode,
                                    unit: c.unitName,
                                    unitCode: c.unitCode,
                                    unitName: c.unitName,
                                    scale: c.scale,
                                    type: c.type
                                }
                            }) : []
                        }
                    })
                    this.$emit('select-product', selectedTableData)
                }, 300)
                // this.handleBeforeClose()
            },
            handleBeforeClose() {
                if (this.$refs.filterForm) {
                    this.$refs.filterForm.resetFields();
                }
                this.$emit("dialog-close");
            },
            handleSizeChange(pageSize) {
                this.paginationOption.pageSize = pageSize
                this.paginationOption.page = 1
                this.fetchTableData();
            },
            handleCurrentPageChange(page) {
                this.paginationOption.page = page
                this.fetchTableData();
            },
            handleSelectionChange(rows) {
                this.selectedTableData = rows;
            },
            fetchTableData(showMessage) {
                this.selectReceiverCode = []
                const params = {
                    skuCodes: this.filterForm.skuCode ? this.filterForm.skuCode.split(/[(\r\n)\r\n]+/) : [],
                    skuType: this.filterForm.skuTypeCode,
                    pageSize: this.paginationOption.pageSize,
                    pageNum: this.paginationOption.page
                }
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/demand/querySkuInfoListByChannel',
                    data: params,
                }).then(res => {
                    if (res.data.code === '0' && res.data.data && Array.isArray(res.data.data.list)) {
                        this.tableData = res.data.data.list;
                        this.paginationOption.total = res.data.data.total;
                    } else {
                        this.tableData = [];
                        this.paginationOption.total = 0;
                    }
                }, () => {
                    this.tableData = [];
                    this.paginationOption.total = 0;
                })
            }
        },
        watch: {
            visible(n) {
                if (n) {
                    this.init()
                    this.onClickSearchBtn()
                } else {
                    if (this.$refs.filterForm) {
                        this.$refs.filterForm.resetFields();
                    }
                }
            }
        },
        template: `
<el-dialog title="选择商品" append-to-body :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1100px">
    <el-form size="mini" :model="filterForm" inline ref="filterForm" label-width="90px">
        <el-form-item label="商品编码:" prop="skuCode">
            <el-input autosize type="textarea" clearable v-model="filterForm.skuCode" style="width:150px" />
        </el-form-item>
        <el-form-item label="商品类型:" prop="skuTypeCode">
            <el-select v-model="filterForm.skuTypeCode" style="width:100%">
                <el-option v-for="item in filterFormOption.productTypes" :key="item.code" :value="item.code" :label="item.name"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button size="mini" type="primary" @click="onClickSearchBtn">查询</el-button>
            <el-button size="mini" type="primary" @click="onClickResetBtn">重置</el-button>
            <el-button size="mini" type="primary" @click="onClickConfirm" :disabled="selectedTableData.length === 0">确定</el-button>
        </el-form-item>
    </el-form>
    <el-table border class="el-table__select--nolabel" height="300px" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column header-align="center" width="50" align="center" label="序号" type="index">
        </el-table-column>
        <el-table-column width="85" align="center" label="商品编号" prop="skuCode">
        </el-table-column>
        <el-table-column prop="name" align="center" label="商品名称" min-width="70">
        </el-table-column>
        <el-table-column prop="combineType" :formatter="formatCombineType" align="center" label="商品属性" min-width="100"></el-table-column>
        <el-table-column prop="skuType" align="center" label="商品类型" min-width="100"></el-table-column>
    </el-table>
    <el-pagination style="padding:10px 0; textAlign:right;" @size-change="handleSizeChange" @current-change="handleCurrentPageChange" :current-page="paginationOption.page" :page-sizes="[10, 20, 50, 100]" :page-size="paginationOption.pageSize" :total="paginationOption.total" layout="total, sizes, prev, pager, next, jumper"></el-pagination>
</el-dialog>
        `
    });
});
