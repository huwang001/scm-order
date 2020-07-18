lyfdefine(['scm-order-package-select-product','scm-order-package-select-order'],function(ProductDialog,OrderDialog){
    return ({    components: {
            ProductDialog,
            OrderDialog
        },
        props: {
            visible: {
                type: Boolean,
                default: false
            },
            callback: {
                type: Function,
                default: () => {},
            }
        },
        watch: {
            visible(n) {
                if (n) {
                    this.init()
                }
            }
        },
        data: function () {

            const validateProiroty = (rule, value, callback) => {
                // 包含0的正整数
                const reg = /(^[1-9]\d*$)/
                if (value && !reg.test(value)) {
                    callback(new Error('只能输入1-100的正整数'));
                }
                if (!value || value === 0) {
                    callback(new Error('只能输入1-100的正整数'));
                }
                if (value > 100) {
                    callback(new Error('只能输入1-100的正整数'));
                }
                callback()
            }
            const validateSaleCode = (rule, value, callback) => {
                if (/[\u4E00-\u9FA5]/g.test(value)) {
                    callback(new Error('不能输入中文'));
                } else {
                    callback();
                }
            }
            return {
                isCheckProduct: false,
                loading: false,
                productDialogVisible: false,
                orderDialogVisible: false,
                POOL_NAME: '/scm-order-admin',
                timer: null,
                validateSpmc() {
                    const validate = (rule, value, callback) => {
                        if (value && value.length > 100) {
                            callback(new Error('请输入1-100个字符'));
                        }
                        callback()
                    }
                    return validate
                },
                validateZdyzhm() {
                    const validate = (rule, value, callback) => {
                        console.log(value)
                        if (value === '' || value === null) {
                            callback(new Error('请输入自定义组合码'));
                        }
                        if (value && value.length > 100) {
                            callback(new Error('请输入1-100个字符'));
                        }
                        callback()
                    }
                    return validate
                },
                validateBz() {
                    const validate = (rule, value, callback) => {
                        if (value && value.length > 200) {
                            callback(new Error('请输入0-200个字符'));
                        }
                        callback()
                    }
                    return validate
                },
                validateXqsl(row) {
                    const validate = (rule, value, callback) => {
                        // 包含0的正数
                        const reg = /^\d+(?=\.{0,1}\d+$|$)/
                        if (!reg.test(value)) {
                            callback(new Error('只能输入正数'));
                        }
                        if (Number(value) === 0) {
                            callback(new Error('只能输入正数'));
                        }
                        if (value.toString().length > 32) {
                            callback(new Error('需求数量超过阈值'));
                        }
                        callback()
                    }
                    return validate
                },
                validateZhfs(row) {
                    const validate = (rule, value, callback) => {
                        // 包含0的正整数
                        const reg = /^([1-9]\d*|[0]{1,1})$/
                        if (!reg.test(value)) {
                            callback(new Error('只能输入正整数'));
                        }
                        if (Number(value) === 0) {
                            callback(new Error('只能输入正整数'));
                        }
                        if (value > 9999999999) {
                            callback(new Error('组合份数超过阈值'));
                        }
                        callback()
                    }
                    return validate
                },
                submitForm: {
                    channelName: '',
                    packType: 1, // 包装类型
                    saleCode: '', // 销售订单号
                    department: '', // 需求部门
                    channelCode: '', // 渠道编码
                    demandDate: '', // 需求日期
                    inFactoryCode: '', //入向工厂编码
                    inRealWarehouseCode: '', // 入向实仓编码
                    inRealWarehouseId: '', // 包装仓ID
                    outFactoryCode: '', // 出向工厂编码
                    outRealWarehouseCode: '', // 出向实仓编码
                    outRealWarehouseId: '', // 领料仓ID
                    isOut: '', // 是否外采购
                    introducer: '', // 需求提出人
                    priority: '', // 优先级
                    remark: '', // 备注
                    packDemandComponentDTOList: [],
                    packDemandDetailDTOList: []
                },
                formatRequireBoxQty: (row) => {
                    if (row.requireBoxQty > 0) {
                        return row.requireBoxQty + '' + row.transportUnitName
                    }
                    return ''
                },
                submitFormOption: {
                    packageTypes: [{
                        value: 1,
                        label: '组装'
                    },
                        {
                            value: 2,
                            label: '反拆'
                        },
                        {
                            value: 3,
                            label: '自定义组合'
                        },
                        {
                            value: 4,
                            label: '自定义反拆'
                        },
                        {
                            value: 5,
                            label: '拆箱'
                        }
                    ],
                    pickingWarehouses: [],
                    packageWarehouses: [],
                    isGetMaterial: [{
                        value: true,
                        label: '是'
                    },
                        {
                            value: false,
                            label: '否'
                        }
                    ],
                    isDelegate: [{
                        value: 1,
                        label: '是'
                    },
                        {
                            value: 0,
                            label: '否'
                        }
                    ]
                },
                validateRules: {
                    saleCode: [{
                        validator: validateSaleCode,
                        required: false,
                        trigger: 'blur'
                    }],
                    packType: [{
                        required: true,
                        message: '请选择包装类型',
                        trigger: 'blur'
                    }, ],
                    channelName: [{
                        required: true,
                        message: '请选择渠道',
                        trigger: 'blur'
                    }],
                    demandDate: [{
                        required: true,
                        message: '请选择需求日期',
                        trigger: 'blur'
                    }],
                    inRealWarehouseId: [{
                        required: true,
                        message: '请选择包装仓库',
                        trigger: 'blur'
                    }],
                    outRealWarehouseId: [{
                        required: true,
                        message: '请选择领料仓库',
                        trigger: 'blur'
                    }],
                    isOut: [{
                        required: true,
                        message: '请选择是否委外',
                        trigger: 'blur'
                    }],
                    introducer: [{
                        required: false,
                        trigger: 'blur'
                    },
                        {
                            min: 0,
                            max: 32,
                            message: '长度在 0 到 32 个字符',
                            trigger: 'blur'
                        }
                    ],
                    priority: [{
                        validator: validateProiroty,
                        required: true,
                        trigger: 'blur'
                    }],
                    remark: [{
                        required: false,
                        trigger: 'blur'
                    },
                        {
                            min: 0,
                            max: 128,
                            message: '长度在 0 到 128 个字符',
                            trigger: 'blur'
                        }
                    ],
                    department: [{
                        required: false,
                        trigger: 'blur'
                    },
                        {
                            min: 0,
                            max: 32,
                            message: '长度在 0 到 32 个字符',
                            trigger: 'blur'
                        }
                    ],
                }
            }
        },
        methods: {
            init() {
                // 请求领料仓库
                this.$http({
                    method: 'get',
                    url: this.POOL_NAME + '/order/v1/real_warehouse/queryNotShopWarehouse',
                }).then(res => {
                    if (res.data.code === '0' && Array.isArray(res.data.data)) {
                        this.submitFormOption.pickingWarehouses = res.data.data
                    }
                }, () => {
                    this.submitFormOption.pickingWarehouses = []
                })
            },
            onClickSubmitBtn() {
                clearTimeout(this.timer)
                this.timer = setTimeout(() => {
                    this.$refs.submitForm.validate((valid) => {
                        if (valid) {
                            const _submitForm = {
                                packType: this.submitForm.packType,
                                saleCode: this.submitForm.saleCode,
                                department: this.submitForm.department,
                                channelCode: this.submitForm.channelCode,
                                demandDate: this.submitForm.demandDate,
                                inFactoryCode: this.submitForm.inFactoryCode,
                                inRealWarehouseCode: this.submitForm.inRealWarehouseCode,
                                inRealWarehouseId: this.submitForm.inRealWarehouseId,
                                outFactoryCode: this.submitForm.outFactoryCode,
                                outRealWarehouseCode: this.submitForm.outRealWarehouseCode,
                                outRealWarehouseId: this.submitForm.outRealWarehouseId,
                                isOut: this.submitForm.isOut,
                                introducer: this.submitForm.introducer,
                                priority: this.submitForm.priority,
                                remark: this.submitForm.remark,
                                packDemandComponentDTOList: this.submitForm.packDemandComponentDTOList.map(n => {
                                    return {
                                        parentSkuCode: n.parentSkuCode,
                                        skuCode: n.skuCode,
                                        skuName: n.skuName,
                                        requireQty: n.requireQty,
                                        bomQty: n.bomQty,
                                        actualMoveQty: n.actualMoveQty,
                                        requireBoxQty: n.requireBoxQty,
                                        unit: n.unit,
                                        unitCode: n.unitCode,
                                        remark: n.remark,
                                        moveType: n.moveType,
                                        isPick: n.isPick,
                                    }
                                }),
                                packDemandDetailDTOList: this.submitForm.packDemandDetailDTOList.map(n => {
                                    return {
                                        skuUnitList: [],
                                        remark: n.remark,
                                        unit: n.unit,
                                        unitCode: n.unitCode,
                                        actualMoveQty: n.actualMoveQty,
                                        compositeQty: n.compositeQty,
                                        requireQty: n.requireQty,
                                        skuName: n.skuName,
                                        skuCode: n.skuCode,
                                        customGroupCode: n.customGroupCode,
                                        compositeQty: n.compositeQty,
                                        moveType: n.moveType
                                    }
                                }),
                            }
                            this.loading = true
                            this.$http({
                                method: 'post',
                                url: this.POOL_NAME + '/order/v1/demand/createDemand',
                                data: _submitForm
                            }).then(
                                (res) => {
                                    this.loading = false
                                    if (res.data.code === '0') {
                                        this.$message({
                                            type: 'success',
                                            message: '创建成功',
                                            showClose: true
                                        })
                                        this.handleBeforeClose();
                                        this.callback();
                                    } else {
                                        this.$message({
                                            type: 'error',
                                            message: res.data.msg,
                                            showClose: true
                                        })
                                    }
                                },
                                (error) => {
                                    this.loading = false
                                    this.$message({
                                        type: 'error',
                                        message: error.toString(),
                                        showClose: true
                                    })
                                },
                            );
                        }
                    });

                }, 300)
            },
            getMoveType(isproduct) {
                //组装成品入库原材料出库 反拆成品出库原材料入库 自定义组合成品入库原材料出库 拆箱成品出库原材料入库
                if (this.submitForm.packType === 1 || this.submitForm.packType === 3) {
                    return !isproduct ? 2 : 1
                } else {
                    return !isproduct ? 1 : 2
                }
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            // 选择销售订单
            onClickSelectOrderBtn(order) {
                this.orderDialogVisible = true
            },
            // 新增行
            onClickAddRowBtn() {
                this.isCheckProduct = false
                this.productDialogVisible = true
            },
            // 删除行
            onClickDeleteProductBtn(row, index) {
                this.submitForm.packDemandDetailDTOList.splice(index, 1)
                if (this.submitForm.packType === 3 || this.submitForm.packType === 4 || this.submitForm.packType === 5) {
                    this.submitForm.packDemandComponentDTOList = this.submitForm.packDemandComponentDTOList.filter(n => n.skuCode !== row.skuCode)
                } else {
                    this.submitForm.packDemandComponentDTOList = this.submitForm.packDemandComponentDTOList.filter(n => n.parentSkuCode !== row.skuCode)
                }
            },
            handleChangePackageType(type) {
                this.submitForm.packDemandComponentDTOList = []
                this.submitForm.packDemandDetailDTOList = []
                this.submitForm.saleCode = ''
            },
            handleChangeChannel(channel) {
                if (channel.length > 0) {
                    this.submitForm.channelName = channel[0].channelName
                    this.submitForm.channelCode = channel[0].channelCode
                } else {
                    this.submitForm.channelName = ''
                    this.submitForm.channelCode = ''
                }
            },
            handleSelectProduct(products) {
                if (this.isCheckProduct) {
                    this.$message({
                        type: 'warning',
                        message: '正在加载组件信息，请等待上次选择商品被加载后，再尝试操作',
                        showClose: true
                    })
                    return
                }
                this.isCheckProduct = true
                // 反拆组装只允许选择单sku商品
                if (this.submitForm.packType === 1 || this.submitForm.packType === 2) {
                    if (products.findIndex(n => n.combineType === 0) > -1) {
                        this.$message({
                            type: 'warning',
                            message: '请选择组装商品'
                        })
                        this.isCheckProduct = false
                        return
                    }
                }
                // // 自定义组合不可选组合商品
                // if (this.submitForm.packType === 3) {
                //     if (products.findIndex(n => n.combineType !== 0) > -1) {
                //         this.$message({
                //             type: 'warning',
                //             message: '请选择单sku商品'
                //         })
                //         this.isCheckProduct = false
                //         return
                //     }
                // }
                // 过滤重复数据
                const filterProducts = products.filter(n => !this.submitForm.packDemandDetailDTOList.some(c => c.skuCode === n.skuCode));
                if (filterProducts.length === 0) {
                    this.$message({
                        type: 'warning',
                        message: '请勿选择重复数据'
                    })
                    this.isCheckProduct = false
                    return
                }

                /***组装获取成品组件dto开始 */
                const productDto = filterProducts.map(n => {
                    // 获取默认单位并且复制
                    let defaultUnit = n.skuUnitList.find(n => n.type === 5)
                    // 没有拿到默认单位试着获取第一个
                    if (!defaultUnit) {
                        defaultUnit = n.skuUnitList.length > 0 ? n.skuUnitList[0] : {
                            unitName: '',
                            unitCode: ''
                        }
                    }
                    return {
                        skuCode: n.skuCode,
                        skuName: n.skuName,
                        unitCode: defaultUnit.unitCode
                    }
                })
                /*** 组装获取成品组件dto结束*/

                // 请求组件商品
                this.$http({
                    method: 'post',
                    url: this.POOL_NAME + '/order/v1/demand/queryCombinesBySkuCodeAndUnitCode',
                    data: productDto,
                    params: {
                        packType: this.submitForm.packType
                    }
                }).then(res => {
                    if (res.data.code === '0') {
                        const _data = res.data.data
                        // 请求成功开始重新组合数据
                        this.productDialogVisible = false
                        if (Array.isArray(_data.packDemandComponentDTOList)) {
                            _data.packDemandComponentDTOList.forEach(n => {
                                n.moveType = this.getMoveType()
                                this.submitForm.packDemandComponentDTOList.push(n)
                            })
                        }
                        if (Array.isArray(_data.packDemandDetailDTOList)) {
                            _data.packDemandDetailDTOList.forEach(n => {
                                n.moveType = this.getMoveType(true)
                                const p = filterProducts.find(c => c.skuCode === n.skuCode)
                                if (p) {
                                    // 获取默认单位并且复制
                                    let defaultUnit = p.skuUnitList.find(u => u.type === 5)
                                    // 没有拿到默认单位试着获取第一个
                                    if (!defaultUnit) {
                                        defaultUnit = p.skuUnitList.length > 0 ? p.skuUnitList[0] : {
                                            unitName: '',
                                            unitCode: ''
                                        }
                                    }
                                    n.unitCode = defaultUnit.unitCode
                                    n.unit = defaultUnit.unitName
                                    n.skuUnitList = p.skuUnitList
                                }
                                this.submitForm.packDemandDetailDTOList.push(n)
                            })
                        }
                        this.isCheckProduct = false
                    } else {
                        this.isCheckProduct = false
                        // 请求失败
                        this.$message({
                            type: 'error',
                            message: res.data.msg,
                            showClose: true
                        })
                    }
                }, () => {
                    this.isCheckProduct = false
                })
            },
            updateComponentsData(row, requireQty, currentUnitCode) {
                const component = this.submitForm.packDemandComponentDTOList.find(n => n.skuCode === row.skuCode)
                // 获取当前单位
                const currentUnit = row.skuUnitList.find(n => n.unitCode === currentUnitCode);
                row.unit = currentUnit.unitName // 同步单位名称
                // 获取箱单位
                const karUnit = row.skuUnitList.find(n => n.type === 3)
                let boxRate = 1
                if (karUnit) {
                    boxRate = karUnit.scale
                }
                // 计算组件的需求数量
                if (this.submitForm.packType === 3 || this.submitForm.packType === 4) {
                    if (!component) {
                        return
                    }
                    // 成品需求数量
                    const finishProductRequireQty = requireQty
                    // 成品组合份数
                    const finishProductCompositeQty = Number(row.compositeQty)
                    if (currentUnit) {
                        // 计算组件需求数量
                        component.requireQty = isNaN(finishProductRequireQty) ? 0 : Math.floor(finishProductRequireQty * currentUnit.scale * 1000) / 1000
                    }
                    if (karUnit) {
                        // 计算组件相单位数量
                        component.requireBoxQty = Math.ceil(component.requireQty / boxRate)
                    }
                    if (component.boxUnitRate > 0) {
                        // 计算组件相单位数量
                        component.requireBoxQty = Math.ceil(component.requireQty / component.boxUnitRate)
                    }
                    if (component.requireQty && !isNaN(component.requireQty) && finishProductCompositeQty > 0) {
                        // 计算bom数量
                        component.bomQty = Math.floor(component.requireQty / finishProductCompositeQty * 1000) / 1000
                    }
                    return
                }
                // 拆箱
                if (this.submitForm.packType === 5) {
                    this.submitForm.packDemandComponentDTOList.forEach(n => {
                        if (n.parentSkuCode === row.skuCode) {
                            n.bomQty = currentUnit.scale
                            n.requireQty = currentUnit.scale * requireQty
                            if (karUnit) {
                                // 计算组件相单位数量
                                n.requireBoxQty = Math.ceil(n.requireQty / boxRate)
                            }
                            if (n.boxUnitRate > 0) {
                                // 计算组件相单位数量
                                n.requireBoxQty = Math.ceil(n.requireQty / n.boxUnitRate)
                            }

                        }
                    })
                    return
                }
                // 其他
                this.submitForm.packDemandComponentDTOList.forEach(c => {
                    if (c.parentSkuCode === row.skuCode) {
                        c.requireQty = Math.floor(c.bomQty * requireQty * currentUnit.scale * 1000) / 1000
                        if (karUnit) {
                            // 计算组件相单位数量
                            c.requireBoxQty = Math.ceil(c.requireQty / boxRate)
                        }
                        if (c.boxUnitRate > 0) {
                            // 计算组件相单位数量
                            c.requireBoxQty = Math.ceil(c.requireQty / c.boxUnitRate)
                        }
                    }
                })
            },
            handleChangeProductUnit(value, row) {
                const curValue = !isNaN(Number(row.requireQty)) ? Number(row.requireQty) : 0
                this.updateComponentsData(row, curValue, value)
            },
            handleInputProductQty(value, row) {
                const curValue = !isNaN(Number(value)) ? Number(value) : 0
                this.updateComponentsData(row, curValue, row.unitCode)
            },
            handleChangeDiyCode(row) {
                const component = this.submitForm.packDemandComponentDTOList.find(n => n.skuCode === row.skuCode)
                component.parentSkuCode = row.customGroupCode
            },
            computedBomByGroupAmount(value, product) {
                const component = this.submitForm.packDemandComponentDTOList.find(n => n.skuCode === product.skuCode)
                if (!component) {
                    return
                }
                const curValue = Number(value)
                if (!isNaN(Number(component.requireQty)) && !isNaN(curValue) && curValue > 0) {
                    component.bomQty = Math.floor(Number(component.requireQty) / curValue * 1000) / 1000
                } else {
                    component.bomQty = 0
                }
            },
            handleChangeGroupAmount(value, row) {
                this.submitForm.packDemandDetailDTOList.forEach(n => {
                    if (n.customGroupCode === row.customGroupCode && n.skuCode !== row.skuCode) {
                        n.compositeQty = value
                        this.computedBomByGroupAmount(value, n)
                    }
                })
                this.computedBomByGroupAmount(value, row)
            },
            handleChangeOutRealWarehouseId(value) {
                this.submitForm.outFactoryCode = ''
                this.submitForm.outRealWarehouseCode = ''
                this.submitForm.inFactoryCode = ''
                this.submitForm.inRealWarehouseId = ''
                this.submitForm.inRealWarehouseCode = ''
                this.submitFormOption.packageWarehouses = []
                const warehouse = this.submitFormOption.pickingWarehouses.find(n => n.id === value)
                if (warehouse) {
                    // 请求包装仓库
                    this.$http({
                        method: 'get',
                        url: this.POOL_NAME + '/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeAndRWType',
                        params: {
                            factoryCode: warehouse.factoryCode,
                            realWarehouseType: 2
                        }
                    }).then(res => {
                        if (res.data.code === '0' && Array.isArray(res.data.data)) {
                            this.submitFormOption.packageWarehouses = res.data.data
                        }
                    }, () => {
                        this.submitFormOption.packageWarehouses = []
                    })
                    this.submitForm.outFactoryCode = warehouse.factoryCode
                    this.submitForm.outRealWarehouseCode = warehouse.realWarehouseOutCode
                }
            },
            handleChangeInRealWarehouseId(value) {
                const warehouse = this.submitFormOption.packageWarehouses.find(n => n.id === value)
                if (warehouse) {
                    this.submitForm.inFactoryCode = warehouse.factoryCode
                    this.submitForm.inRealWarehouseCode = warehouse.realWarehouseOutCode
                }
            },
            handleSelectOrder(order) {
                this.$http({
                    method: 'get',
                    url: this.POOL_NAME + '/order/v1/demand/queryDemandDetailAndComponent',
                    params: {
                        recordCode: order.recordCode
                    }
                }).then(res => {
                    if (res.data.code === '0') {
                        const data = res.data.data
                        if (Array.isArray(data.packDemandDetailDTOList)) {
                            this.submitForm.packDemandDetailDTOList = data.packDemandDetailDTOList
                        }
                        if (Array.isArray(data.packDemandComponentDTOList)) {
                            this.submitForm.packDemandComponentDTOList = data.packDemandComponentDTOList
                        }
                    } else {
                        this.$message({
                            type: 'error',
                            showClose: true,
                            message: res.data.msg
                        })
                    }
                }, (err) => {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: err.toString()
                    })
                })
                if (order) {
                    this.submitForm.saleCode = order.recordCode
                }
            },
            handleBeforeClose() {
                this.submitForm.channelCode = ''
                this.submitForm.outFactoryCode = ''
                this.submitForm.outRealWarehouseCode = ''
                this.submitForm.inFactoryCode = ''
                this.submitForm.inRealWarehouseCode = ''
                if (this.$refs.submitForm) {
                    this.$refs.submitForm.resetFields();
                    this.submitForm.packDemandComponentDTOList = []
                    this.submitForm.packDemandDetailDTOList = []
                }
                this.$emit('dialog-close');
            }

        },
        template: `
<el-dialog title="新增包装需求单" data-scm-order-package class="scm-order-package-request-add" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="1200px">
    <div v-loading="loading">
        <el-form :model="submitForm" inline-message :rules="validateRules" ref="submitForm" size="mini" label-width="100px">
            <el-card shadow="never">
                <div slot="header">
                    基础信息
                </div>
                <div class="request-add__base">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="包装类型:" prop="packType" label-width="100px">
                                <el-select v-model="submitForm.packType" style="width:100%" @change="handleChangePackageType">
                                    <el-option v-for="item in submitFormOption.packageTypes" :key="item.value" :value="item.value" :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item :label="submitForm.packType === 4 ? '需求单号:' : '销售订单号:'" prop="saleCode" label-width="100px">
                                <el-row :gutter="5">
                                    <el-col :span="13">
                                        <el-input style="width:100%" v-model="submitForm.saleCode" :disabled="submitForm.packType === 4" placeholder="销售订单号"></el-input>
                                    </el-col>
                                    <el-col :span="10">
                                        <el-button style="width:100%;" type="primary" @click="onClickSelectOrderBtn" :disabled="submitForm.packType !==4">选择</el-button>
                                    </el-col>
                                </el-row>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="渠道:" prop="channelName" label-width="100px">
                                <el-row :gutter="5">
                                    <el-col :span="11">
                                        <el-input style="width:100%;" v-model="submitForm.channelName" disabled placeholder="请选择渠道" />
                                    </el-col>
                                    <el-col :span="11">
                                        <basedata-el-channel style="width:100%;" @change="handleChangeChannel" :clear="true"></basedata-el-channel>
                                    </el-col>
                                </el-row>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="需求日期:" prop="demandDate" label-width="100px">
                                <el-date-picker style="width:100%;" v-model="submitForm.demandDate" type="date" placeholder="选择日期" clearable></el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="领料仓库:" prop="outRealWarehouseId">
                                <el-select style="width:100%" @change="handleChangeOutRealWarehouseId" v-model="submitForm.outRealWarehouseId" filterable clearable placeholder="请选择领料仓库">
                                    <el-option v-for="item in submitFormOption.pickingWarehouses" :key="item.id" :label="item.realWarehouseName+ '('+item.realWarehouseCode+')'" :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="包装仓库:" prop="inRealWarehouseId">
                                <el-select style="width:100%" @change="handleChangeInRealWarehouseId" v-model="submitForm.inRealWarehouseId" filterable clearable placeholder="请选择包装仓库">
                                    <el-option v-for="item in submitFormOption.packageWarehouses" :key="item.id" :label="item.realWarehouseName+ '('+item.realWarehouseCode+')'" :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <!-- <el-col :span="6">
                        <el-form-item label="是否委外:" prop="isOut" label-width="100px">
                            <el-select v-model="submitForm.isOut" style="width:100%">
                                <el-option v-for="item in submitFormOption.isDelegate" :key="item.value" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col> -->
                        <el-col :span="6">
                            <el-form-item label="需求提出人:" prop="introducer" label-width="100px">
                                <el-input style="width:100%" v-model="submitForm.introducer"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="需求优先级:" prop="priority" label-width="100px">
                                <el-input style="width:100%" v-model="submitForm.priority"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="指令部门:" prop="department" label-width="100px">
                                <el-input style="width:100%" v-model="submitForm.department"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="备注:" prop="remark" label-width="100px">
                                <el-input style="width:100%" v-model="submitForm.remark"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
            </el-card>
            <el-card shadow="never">
                <div slot="header">
                    成品商品明细
                </div>
                <div class="request-add__product">
                    <div class="request-add__product__header">
                        <el-button @click="onClickAddRowBtn" type="primary" v-if="submitForm.packType !== 4">新增行</el-button>
                    </div>
                    <div class="request-add__product__body">
                        <el-table :data="submitForm.packDemandDetailDTOList" size="mini" border ref="table" style="width: 100%;">
                            <el-table-column label="序号" align="center" type="index" width="55">
                            </el-table-column>
                            <el-table-column prop="skuCode" header-align="center" align="center" label="成品商品编码" min-width="120">
                            </el-table-column>
                            <el-table-column prop="skuName" header-align="center" align="center" label="商品名称" min-width="100">
                                <template slot-scope="scope">
                                    <span>{{scope.row.skuName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="requireQty" header-align="center" align="center" label="需求数量">
                                <template slot-scope="scope">
                                    <el-form-item label-width="0px" style="marginBottom:0px;" :prop="'packDemandDetailDTOList.'+scope.$index + '.requireQty'" :rules="{
                                    required: true, validator: validateXqsl(scope.row), trigger: 'blur'}">
                                        <el-input v-model="scope.row.requireQty" @input="handleInputProductQty($event, scope.row)">
                                        </el-input>
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column prop="unitCode" header-align="center" align="center" label="单位">
                                <template slot-scope="scope">
                                    <el-form-item v-if="submitForm.packType !== 4" label-width="0px" style="marginBottom:0px;">
                                        <el-select v-model="scope.row.unitCode" @change="handleChangeProductUnit($event, scope.row)">
                                            <el-option v-for="unit in scope.row.skuUnitList" :key="unit.unitCode" :value="unit.unitCode" :label="unit.unitName" />
                                        </el-select>
                                    </el-form-item>
                                    <span v-else>{{scope.row.unit}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="remark" header-align="center" align="center" width="150px" label="明细备注">
                                <template slot-scope="scope">
                                    <el-form-item label-width="0px" style="marginBottom:0px;" :prop="'packDemandDetailDTOList.'+scope.$index + '.remark'" :rules="{
                                    required: true, validator: validateBz(scope.row), trigger: 'blur'}">
                                        <el-input v-model="scope.row.remark">
                                        </el-input>
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column prop="customGroupCode" header-align="center" align="center" label="自定义组合码">
                                <template slot-scope="scope">
                                    <el-form-item v-if="submitForm.packType === 3" label-width="0px" style="marginBottom:0px;" :prop="'packDemandDetailDTOList.'+scope.$index + '.customGroupCode'" :rules="{
                                    required: true, validator: validateZdyzhm(scope.row), trigger: 'blur'}">
                                        <el-input v-model="scope.row.customGroupCode" @input="handleChangeDiyCode(scope.row)">
                                        </el-input>
                                    </el-form-item>
                                    <span v-else>{{scope.row.customGroupCode}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="compositeQty" header-align="center" align="center" label="组合份数">
                                <template slot-scope="scope">
                                    <el-form-item v-if="submitForm.packType === 3 || submitForm.packType === 4" label-width="0px" style="marginBottom:0px;" :prop="'packDemandDetailDTOList.'+scope.$index + '.compositeQty'" :rules="{
                                    required: true, validator: validateZhfs(scope.row), trigger: 'blur'}">
                                        <el-input v-model="scope.row.compositeQty" @input="handleChangeGroupAmount($event, scope.row)">
                                        </el-input>
                                    </el-form-item>
                                    <span v-else>{{scope.row.compositeQty}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column v-if="submitForm.packType !== 4" header-align="center" align="center" label="操作" width="120">
                                <template slot-scope="scope">
                                    <el-button type="text" @click="onClickDeleteProductBtn(scope.row, scope.$index)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-card>
            <el-card shadow="never">
                <div slot="header">
                    组件商品明细
                </div>
                <div class="request-add__component">
                    <div class="request-add__component__body">
                        <el-table :data="submitForm.packDemandComponentDTOList" size="mini" border ref="table" style="width: 100%;">
                            <el-table-column label="序号" align="center" type="index" width="55">
                            </el-table-column>
                            <el-table-column prop="parentSkuCode" header-align="center" align="center" label="成品商品编码" min-width="100">
                                <template slot-scope="scope">
                                    <span>{{scope.row.parentSkuCode}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="skuCode" header-align="center" align="center" label="组件商品编码" min-width="100">
                                <template slot-scope="scope">
                                    <span>{{scope.row.skuCode}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="skuName" header-align="center" width="180" align="center" label="商品名称">
                                <template slot-scope="scope">
                                    <span>{{scope.row.skuName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="bomQty" header-align="center" align="center" label="BOM数量">
                            </el-table-column>
                            <el-table-column prop="requireQty" header-align="center" align="center" label="需求数量">
                                <template slot-scope="scope">
                                    <span>{{scope.row.requireQty}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="unit" header-align="center" align="center" label="基本单位">
                            </el-table-column>
                            <el-table-column prop="requireBoxQty" header-align="center" align="center" label="运输单位数量">
                            </el-table-column>
                            <el-table-column prop="isPick" header-align="center" align="center" label="是否需领料" width="120px">
                                <template slot-scope="scope">
                                    <el-form-item label-width="0px" style="marginBottom:0px;">
                                        <el-select v-model="scope.row.isPick">
                                            <el-option v-for="item in submitFormOption.isGetMaterial" :key="item.value" :value="item.value" :label="item.label"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column prop="actualMoveQty" header-align="center" align="center" label="实际移库数量">
                            </el-table-column>
                            <el-table-column prop="moveType" header-align="center" align="center" label="移库类型">
                                <template slot-scope="scope">
                                    {{scope.row.moveType === 2 ? '出库' : '入库'}}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-card>
        </el-form>
        <div slot="footer" class="dialog-footer" style="textAlign: center;padding:10px 0px;">
            <el-button type="primary" :disabled="loading" @click.native="onClickSubmitBtn" size="mini">保存</el-button>
            <el-button @click.native="onClickCancelBtn" size="mini">返回</el-button>
        </div>
    </div>
    <product-dialog :visible="productDialogVisible" @dialog-close="productDialogVisible=false" @select-product="handleSelectProduct" />
    <order-dialog :visible="orderDialogVisible" @dialog-close="orderDialogVisible=false" @select-order="handleSelectOrder" />
</el-dialog>
        `
    });
});
