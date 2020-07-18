/**
 * 仓库调拨修改
 * @autor: sunyj
 * @date: 2019-6-1
 */
lyfdefine(function(addOrUpdate){
    return ({
        data: function () {
            const validateInInfo = (rule, value, callback) => {
                if (this.dataForm.inWarehouseName == '' ||  this.dataForm.inWarehouseName == undefined) {
                    callback(new Error('入库联系人不能为空!'));
                }else if (this.dataForm.inWarehouseMobile == '' ||  this.dataForm.inWarehouseMobile == undefined) {
                    callback(new Error('入库联系人手机号不能为空!'));
                } else {
                    callback();
                }
            };
            const validateOutInfo = (rule, value, callback) => {
                if (this.dataForm.outWarehouseName == '' ||  this.dataForm.outWarehouseName == undefined) {
                    callback(new Error('出库联系人不能为空!'));
                }else if (this.dataForm.outWarehouseMobile == '' ||  this.dataForm.outWarehouseMobile == undefined) {
                    callback(new Error('出库联系人手机号不能为空!'));
                }
                else {
                    callback();
                }
            };
            return {
                dataList: [],
                prodList: [],
                reasonList:[],
                filterCodeIn:[],
                filterCodeOut:[],
                dataForm: {
                    isReturnAllotcate: '',
                    isQualityAllotcate: '',
                    businessType:'',
                    recordCode:'',
                    inWarehouseId:'',
                    inRealWarehouseCode: '',
                    inWarehouseAddr:'',
                    outWarehouseId:'',
                    outRealWarehouseCode: '',
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
                    skuCodes: '',
                    realWarehouseId: '',
                    realWarehouseOutCode: '',
                    factoryCode: '',
                    pageIndex: 1,
                    pageSize: 10,
                    totalPage: 0
                },
                dataListLoading:false,
                prodListLoading:false,
                prodDialogVisible:false,
                visible: false,
                oldFactory:'',
                oldWarehouse:'',
                gridData: [],
                checkedPordArr: [],
                checkedItemArr: [],
                factoryCode:[],
                inRealWareCode:[],
                outRealWareCode:[],
                orderTypeList: [
                    {value: 1, label: '内部调拨'},
                    {value: 2, label: 'RDC调拨'}
                ],
                labelPosition: 'top',
                unitCodes: [],
                unitInfos: new Map(),
                stockMap: new Map(),
                dataRule: {
                    isReturnAllotcate: [{ required: true, message: '请选择是否退货', trigger: 'blur' }],
                    isQualityAllotcate: [{ required: true, message: '请选择是否质量问题', trigger: 'blur' }],
                    businessType: [{ required: true, message: '调拨类型不能为空', trigger: 'blur' }],
                    inWarehouseId: [{ required: true, message: '出库仓不能为空', trigger: 'blur' }],
                    outWarehouseId: [{ required: true, message: '入库仓不能为空', trigger: 'blur' }],
                    allotTime: [{ required: true, message: '调拨日期不能为空', trigger: 'blur' }],
                    expeAogTime: [{ required: true, message: '预计到货日期不能为空', trigger: 'blur' }]

                }
            }
        },
        components: {
            'addOrUpdate': addOrUpdate
        },
        filters: {
            rounding (value) {
                return value.toFixed(3);
            }
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
                        _this.prodForm.realWarehouseOutCode = res.data.data.outRealWarehouse.realWarehouseOutCode;
                    	_this.prodForm.factoryCode = res.data.data.outRealWarehouse.factoryCode;
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
                                    _this.unitCodes[details[i].lineNo] = details[i].skuUnitList[j];
                                    _this.unitInfos.set(details[i].lineNo, details[i].skuUnitList[j]);
                                    _this.changeUnit(details[i].skuUnitList, i);
                                }
                            }
                            lineNo++;
                        }
                        _this.$set(_this.dataForm, 'isReturnAllotcate', res.data.data.isReturnAllotcate.toString());
                        _this.$set(_this.dataForm, 'isQualityAllotcate', res.data.data.isQualityAllotcate.toString());
                        _this.checkedPordArr = details;
                        //_this.checkedItemArr = details;
                        _this.reasonList = res.data.data.reasonList;
                        console.log(_this.unitCodes);
                        console.log(_this.unitInfos);
                    }
                }).catch(function(error){
                    console.log(error);
                });
            },
            initSelectPage: function initSelectPage(){
                let _this = this;
                _this.checkedPordArr=[];
                if(!(_this.prodForm.realWarehouseId > 0)){
                    _this.$message({message:'请选择一个出库仓库！',type: 'error'});
                    return;
                }
                _this.prodDialogVisible = true;
                _this.prodListLoading = true;
                this.$http.post('/scm-order-admin/order/v1/whAllocation/queryStockByWhId',
                    this.prodForm).then(function (res) {
                    if(res.status=='200'){
                        _this.prodList = res.data.data.list;
                        _this.prodForm.totalPage = res.data.data.total;
                    }else{
                        _this.prodList = [];
                        _this.prodForm.totalPage = 0;
                        _this.$message('查询失败！');
                    }
                    _this.prodListLoading = false;
                }).catch(function(error){
                    console.log(error);
                    _this.prodListLoading = false;
                });

            },
            //选中checkbox时触发事件
            handleSelectionChange(val){
                this.checkedPordArr = val;
            },
            handleSelectionChangeS(val){
                this.checkedItemArr = val;
            },
            onQueryProd() {
                this.initSelectPage();
            },
            confrimTable(){
                let _this = this;
                _this.$refs.subTable.clearSort();
                _this.prodDialogVisible = false;
                //this.dataList = this.checkedPordArr
                _this.dataList = this.mergeArray(_this.dataList,_this.checkedPordArr)
                let dataList = _this.dataList;
                for(let i=0;i< dataList.length;i++){
                    if(dataList[i].skuUnitList != null) {
                        for (let j = 0; j < dataList[i].skuUnitList.length; j++) {
                            if (dataList[i].skuUnitList[j].unitName == "箱" && typeof(dataList[i].unit) == "undefined") {
                                _this.unitCodes[dataList[i].lineNo] = dataList[i].skuUnitList[j];
                                _this.unitInfos.set(dataList[i].lineNo, dataList[i].skuUnitList[j]);
                                _this.changeUnit(dataList[i].skuUnitList, i);
                            }
                        }
                    }
                }
            },
            closeVisible: function () {
                this.$refs.mainTable.clearSort();
                this.visible = false;
                this.$refs['dataForm'].resetFields();
            },
            //合并数组并去重
            mergeArray(arr1,arr2){
                let _arr = new Array();
                let lineNo = 1;
                //首次添加
                if(arr1.length == 0){
                    for(let i=0;i<arr2.length;i++){
                        arr2[i].lineNo = lineNo;
                        for (let j = 0; j < arr2[i].skuUnitList.length; j++) {
                            arr2[i].skuUnitList[j].lineNo = arr2[i].lineNo;
                        }
                        _arr.push(arr2[i]);
                        lineNo++;
                    }
                }else{
                    _arr =  arr1;
                for(let i=0;i<arr1.length;i++){
                        lineNo = arr1[i].lineNo;
                        for (let j = 0; j < arr1[i].skuUnitList.length; j++) {
                            arr1[i].skuUnitList[j].lineNo = arr1[i].lineNo;
                }
                    }
                    lineNo++;
                    //把选择的列表加入
                for(let i=0;i<arr2.length;i++){
                        arr2[i].lineNo = lineNo;
                        for (let j = 0; j < arr2[i].skuUnitList.length; j++) {
                            arr2[i].skuUnitList[j].lineNo = arr2[i].lineNo;
                        }
                        _arr.push(arr2[i]);
                        lineNo++;
                    }
                }

                return _arr;
            },
            //删除选中项
            deleteC(){
                for(let i=0;i<this.checkedItemArr.length;i++){
                    for(let x=0;x<this.dataList.length;x++){
                        if(this.checkedItemArr[i].lineNo==this.dataList[x].lineNo){
                            this.dataList.splice(x,1);
                            x--;
                        }
                    }
                }
            },
            //根据工厂编号获取实仓编号和名字
            getRealWareByFCode: async function(row, type){
                let _this = this;
                if(type == 1) {
                    let isSuccc = await _this.clearDataList();
                    if(!isSuccc){
                        _this.dataForm.outfactoryCode = _this.oldFactory;
                        return false;
                    }
                    _this.oldFactory = _this.dataForm.outfactoryCode;
                    _this.dataForm.outWarehouseId = null;
                    _this.dataForm.outWarehouseAddr = row.realWarehouseAddress;
                    this.prodForm.realWarehouseId='';
                }else{
                    _this.dataForm.inWarehouseId = null;
                    _this.dataForm.inWarehouseAddr = row.realWarehouseAddress;
                }
                _this.initWarehouse(row, type);
                if(_this.dataForm.businessType == 1){
                    let inWarehouseId = _this.dataForm.inWarehouseId;
                    if(inWarehouseId == '' || inWarehouseId == ''){
                        _this.inRealWareCode = [];
                        _this.inRealWareCode=_this.outRealWareCode;
                        _this.dataForm.infactoryCode =  _this.dataForm.outfactoryCode
                    }else{
                        _this.inRealWareCode = [];
                        _this.inRealWareCode=_this.outRealWareCode;
                        _this.dataForm.infactoryCode = _this.dataForm.outfactoryCode
                        _this.dataForm.inWarehouseId = '';
                        _this.dataForm.inWarehouseAddr = '';
                        _this.dataForm.inWarehouseName = '';
                        _this.dataForm.inWarehouseMobile = '';
                    }

                }

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
            getFactoryList:function(){
                let _this = this;
                //后台查询所有实仓仓库信息
                this.$http.get('/scm-order-admin/order/v1/shop/queryNotShopFactory').then(function (_ref) {
                    let data = _ref.data.data;

                    if (data) {
                        let arr = [];
                        data.forEach(function (value) {
                            let obj = {};
                            obj.value = value.code;
                            obj.label = value.name + "（" + value.code + "）";
                            if(arr .indexOf(obj) === -1){
                                _this.factoryCode.push(obj);
                            }
                        });

                    }
                });

            },
            setAddress: async function(id, type){
                let _this = this;
                if(type == 1){
                    let isSuccc = await _this.clearDataList();
                    if(!isSuccc){
                        _this.dataForm.outWarehouseId = _this.oldWarehouse;
                        return false;
                    }
                    _this.oldWarehouse = _this.dataForm.outWarehouseId;
                }
                let data = [];
                let row = null;
                if(type == 1){
                    data = _this.outRealWareCode;
                }else{
                    data = _this.inRealWareCode;
                }
                data.forEach(function (e) {
                    if(e.value.id == id){
                        row = e.value;
                    }
                });
                if(type == 1){
                	_this.prodForm.realWarehouseOutCode = row.realWarehouseOutCode;
                	_this.prodForm.factoryCode = row.factoryCode;
                    _this.dataForm.outWarehouseId = row.id;
                    _this.prodForm.realWarehouseId = row.id;
                    _this.dataForm.outWarehouseAddr = row.realWarehouseAddress;
                    _this.dataForm.outRealWarehouseCode = row.realWarehouseOutCode;
                    _this.dataForm.outFactoryCode = row.factoryCode;
                }else{
                    _this.dataForm.inWarehouseId = row.id;
                    _this.dataForm.inWarehouseAddr = row.realWarehouseAddress;
                    _this.dataForm.inRealWarehouseCode = row.realWarehouseOutCode;
                    _this.dataForm.inFactoryCode = row.factoryCode;
                }
            },
            //切换调拨类型
            changeBusinessType: function (businessType) {
                let _this = this;
                let infactoryCode = _this.dataForm.infactoryCode;
                let outfactoryCode = _this.dataForm.outfactoryCode;
                //内部调拨工厂必须一致
                if(outfactoryCode!='' && infactoryCode != '') {
                    if (businessType == 1) {
                        if (infactoryCode != outfactoryCode) {
                            _this.$message({message:'内部调拨必须发生在同一工厂内！',type: 'error'});
                            _this.dataForm.inWarehouseId = '';
                            _this.dataForm.inWarehouseAddr = '';
                            _this.dataForm.inWarehouseName = '';
                            _this.dataForm.inWarehouseMobile = '';
                            _this.dataForm.inRealWareCode = [];
                            _this.inRealWareCode=_this.outRealWareCode;
                            _this.dataForm.infactoryCode =  _this.dataForm.outfactoryCode
                        }
                    }
                    //RDC调拨工厂必须有不一致
                    if (businessType == 2) {
                        if (infactoryCode == outfactoryCode) {
                            _this.$message({message:'RDC调拨必须发生在不同工厂内！',type: 'error'});
                            _this.inRealWareCode = [];
                            _this.dataForm.infactoryCode = '';
                            _this.dataForm.inWarehouseId = '';
                            _this.dataForm.inWarehouseAddr = '';
                            _this.dataForm.inWarehouseName = '';
                            _this.dataForm.inWarehouseMobile = '';
                        }
                    }
                }else{
                    if(outfactoryCode!='' && infactoryCode == ''){
                        if (businessType == 1) {
                            _this.inRealWareCode = [];
                            _this.inRealWareCode=_this.outRealWareCode;
                            _this.dataForm.infactoryCode =  _this.dataForm.outfactoryCode
                        }
                    }
                }
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
                let useQty =parseFloat((row.realQty - row.lockQty).toString()).toFixed(3);
                //质量调拨
                if(_this.dataForm.isQualityAllotcate == 1){
                    useQty = row.realQty;
                }
                let inptQty = row.allotQty;
                if(inptQty != '' && inptQty != undefined){
                    let total  = parseFloat((inptQty * scale).toFixed(3));
                    if(total > useQty){
                        _this.$message({message:'输入的调拨数量大于仓库数量！',type: 'error'});
                        row.allotQty = '';
                        row.baseNum = '';
                        return false;
                    }else{
                        row.spec = "1 x" + scale;
                        row.baseNum = total;
                    }
                }
                row.unit = _this.unitInfos.get(row.lineNo).unitName;
                row.unitCodeInfo = _this.unitInfos.get(row.lineNo);
                this.$set(_this.dataList, index, row)
            },
            changeNum: function(row,index){
                let _this = this;
                //换算
                if(_this.unitInfos.get(row.lineNo) != undefined) {
                    let scale = _this.unitInfos.get(row.lineNo).scale;
                    let useQty = parseFloat((row.realQty - row.lockQty).toString()).toFixed(3);
                    //质量调拨
                    if(_this.dataForm.isQualityAllotcate == 1){
                        useQty = row.realQty;
                    }
                    let inptQty = row.allotQty;
                    row.orginQty = inptQty;
                    let total  = parseFloat((inptQty * scale).toFixed(3));
                    if (total > useQty) {
                        _this.$message({message:'输入的调拨数量大于仓库数量！',type: 'error'});
                        row.allotQty = '';
                        row.orginQty = '';
                        row.baseNum = '';
                        return false;
                    } else {
                        row.spec = "1 x" + scale;
                        row.baseNum = total;
                        this.$set(_this.dataList, index, row)
                    }
                    row.unit = _this.unitInfos.get(row.lineNo).unitName;
                    row.unitCodeInfo = _this.unitInfos.get(row.lineNo);
                }
            },
            itemFormSubmit:function () {
                let _this = this;
                this.$refs['dataForm'].validate(function (valid) {
                    if (valid) {
                        if(_this.dataForm.inWarehouseId ==  _this.dataForm.outWarehouseId){
                            _this.$message({message:'调拨出入库仓库不能相同！',type: 'error'});
                            return;
                        }
                        if(_this.dataList.length == 0 ){
                            _this.$message({message:'请选择明细后再提交！',type: 'error'});
                            return;
                        }
                        _this.dataForm.frontRecordDetails = _this.dataList;
                        let isSucc = true;
                        _this.stockMap = new Map();
                        _this.dataList.forEach(function (e) {
                            let useQty = e.realQty - e.lockQty;
                            if(useQty <= 0){
                                _this.stockMap.set(e.skuId, 0);
                            }else{
                                _this.stockMap.set(e.skuId, useQty);
                            }

                        });
                        _this.dataList.forEach(function (e) {
                            if((e.allotQty != 0 && e.allotQty == '') || e.allotQty == undefined){
                                _this.$message({message:'部分商品明细未选择调拨数量！',type: 'error'});
                                isSucc = false;
                                return;
                            }
                            if(e.unit == '' || e.unit == undefined){
                                _this.$message({message:'部分商品明细未选择调拨单位！',type: 'error'});
                                isSucc = false;
                                return ;
                            }
                            if(e.baseNum >= 0){
                                let useQty =parseFloat((e.realQty - e.lockQty).toString()).toFixed(3);
                                //质量调拨
                                if(_this.dataForm.isQualityAllotcate == 1){
                                    useQty = e.realQty;
                                }
                                if(useQty < e.baseNum) {
                                    _this.$message({message: '部分商品调拨数量小于库存数量！', type: 'error'});
                                    isSucc = false;
                                    return;
                                }
                            }
                            e.unitCode = e.unitCodeInfo.unitCode;
                            if(_this.dataForm.isReturnAllotcate == 1){
                                if(e.reasonCode == '' || e.reasonCode == undefined){
                                    _this.$message({message:'部分商品明细未选择退货原因！',type: 'error'});
                                    isSucc = false;
                                    return;
                                }
                            }else {
                                e.reasonCode = '';
                            }
                        });

                        if(!isSucc){
                            return;
                        }
                        _this.$http.post('/scm-order-admin/order/v1/whAllocation/updateWhAllocation', _this.dataForm
                        ).then(function (_ref2) {
                            let data = _ref2.data;
                            if (data && data.code === '0') {
                                _this.$message({message:'修改成功！',type: 'success'});
                                _this.closeVisible();
                                _this.$emit('refreshDataList');
                            }else{
                                _this.$message({message:'修改失败！',type: 'error'});
                            }

                        })
                    }
                });
            },
            clearForm: function(){
                let _this = this;
                _this.dataList=[];
                _this.checkedPordArr=[];
                _this.checkedItemArr=[];
                _this.unitCodes =[];
                _this.unitInfos = new Map();
                _this.dataForm.isReturnAllotcate='';
                _this.dataForm.isQualityAllotcate='';
                this.dataForm.inWarehouseId='';
                this.dataForm.outWarehouseId='';
                this.prodForm.realWarehouseId='';
                _this.prodForm.realWarehouseOutCode='';
                _this.prodForm.factoryCode='';
                _this.prodForm.skuCodes = '';
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
            },
            clearDataList: async function () {
                let isSuccess = false;
                let _this = this;
                if(_this.dataList.length > 0) {
                    await _this.$confirm('此操作将清空已选择的商品, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        for (let x = 0; x < this.dataList.length; x++) {
                            this.dataList.splice(x, 1);
                            x--;
                        }
                        _this.checkedPordArr=[];
                        _this.checkedItemArr=[];
                        isSuccess = true;
                    }).catch(() => {
                        isSuccess = false;
                    });
                }else{
                    isSuccess = true;
                }
                return isSuccess;
            },
            dataFilterOut(val) {
                this.value = val;
                if (val) { //val存在
                    this.filterCodeOut = this.factoryCode.filter((item) => {
                        if (!!~item.label.indexOf(val) || !!~item.label.toUpperCase().indexOf(val.toUpperCase())) {
                            return true
                        }
                        if (!!~item.value.indexOf(val) || !!~item.value.toUpperCase().indexOf(val.toUpperCase())) {
                            return true
                        }
                    })
                } else { //val为空时，还原数组
                    this.filterCodeOut = this.factoryCode;
                }
            },
            dataFilterIn(val) {
                this.value = val;
                if (val) { //val存在
                    this.filterCodeIn = this.factoryCode.filter((item) => {
                        if (!!~item.label.indexOf(val) || !!~item.label.toUpperCase().indexOf(val.toUpperCase())) {
                            return true
                        }
                        if (!!~item.value.indexOf(val) || !!~item.value.toUpperCase().indexOf(val.toUpperCase())) {
                            return true
                        }
                    })
                } else { //val为空时，还原数组
                    this.filterCodeIn = this.factoryCode;
                }
            },
            clearFilter(){
                this.filterCodeIn = this.factoryCode;
                this.filterCodeOut = this.factoryCode;
            },
            // 每页数
            sizeChangeHandle: function sizeChangeHandle(val) {
                this.prodForm.pageSize = val;
                this.prodForm.pageIndex = 1;
                this.initSelectPage();
            },

            // 当前页
            currentChangeHandle: function currentChangeHandle(val) {
                this.prodForm.pageIndex = val;
                this.initSelectPage();
            },
            resetForm:function (form) {
                this.prodForm.skuCodes = '';
            },
            closeDialog(done){
                this.prodDialogVisible = false
                // this.$refs['itemForm'].resetFields();
                this.prodForm.skuCodes = ''
                this.$refs.subTable.clearSort();
            }
        },
        mounted() {
            this.getFactoryList();
        },
        template: `
                <el-dialog
                 title="编辑"
                 :close-on-click-modal="false"
                 :before-close="closeVisible"
                  v-if="visible"
                 width="80%"
                 :visible.sync="visible">
                    <el-form :inline="true" :label-position="labelPosition"  :model="dataForm" class="demo-form-inline" ref="dataForm" :rules="dataRule">
                        <el-col :span="8">
                            <el-form-item label="调拨类型" prop="businessType">
                                <el-select v-model="dataForm.businessType" placeholder="类型" @change="changeBusinessType(dataForm.businessType)">
                                    <el-option v-for="item in orderTypeList" :key="item.value" :label="item.label" :value="item.value" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="是否退货："  prop="isReturnAllotcate">
                               <el-input   v-model="dataForm.recordCode" :disabled="true" style="display:none;"></el-input>
                                <el-radio v-model="dataForm.isReturnAllotcate" label="1">是</el-radio>
                                <el-radio v-model="dataForm.isReturnAllotcate" label="0">否</el-radio>
                            </el-form-item>
                      
                            <el-form-item label="是否质量问题：" prop="isQualityAllotcate">
                               <el-input   v-model="dataForm.recordCode" :disabled="true" style="display:none;"></el-input>
                                <el-radio v-model="dataForm.isQualityAllotcate" label="1">是</el-radio>
                                <el-radio v-model="dataForm.isQualityAllotcate" label="0">否</el-radio>
                            </el-form-item>
                        </el-col>
                        
                      <el-col :span="8">
                            <el-form-item label="调拨日期："  prop="allotTime">
                                <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.allotTime" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                         <el-col :span="8">
                         <el-form-item label="选择出库工厂："   prop="outfactoryCode">
                            <template slot-scope="scope">
                             <el-select filterable :filter-method="dataFilterOut" @visible-change="clearFilter" v-model="dataForm.outfactoryCode" placeholder="工厂编号" @change="getRealWareByFCode(dataForm.outfactoryCode, 1)">
                                <el-option v-for="item in filterCodeOut"  :key="item.value" :label="item.label" :value="item.value"  :disabled="(dataForm.businessType==2&&dataForm.infactoryCode==item.value)?true:false">
                                </el-option>
                             </el-select>
                             </template>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择出库仓："  prop="outWarehouseId">
                            <el-select filterable v-model="dataForm.outWarehouseId" placeholder="仓库编号/名称" @change="setAddress(dataForm.outWarehouseId, 1)">
                                <el-option v-for="item in outRealWareCode" :key="item.value" :label="item.label" :value="item.value.id" >
                                </el-option>
                            </el-select>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                            <el-form-item label="调出联系人/电话：" prop="outWarehouseName">
                            <div style="width: 190px">
                                <el-col :span="10" >
                                    <el-input  v-model="dataForm.outWarehouseName" placeholder="联系人"></el-input>
                                </el-col>
                                 <el-col :span="14" >
                                    <el-input   v-model="dataForm.outWarehouseMobile" placeholder="电话"></el-input>
                                 </el-col>
                            </div>
                                
                            </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择入库工厂：" prop="infactoryCode">
                           <template slot-scope="scope">
                            <el-select filterable :filter-method="dataFilterIn"  @visible-change="clearFilter"  v-model="dataForm.infactoryCode" placeholder="工厂编号" @change="getRealWareByFCode(dataForm.infactoryCode, 2)"   :disabled="dataForm.businessType==1?true:false">
                                <el-option  v-for="item in filterCodeIn" :key="item.value" :label="item.label" :value="item.value" :disabled="(dataForm.businessType==2&&dataForm.outfactoryCode==item.value)?true:false">
                                </el-option>
                             </el-select>
                            </template>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="选择入库仓："  prop="inWarehouseId">
                            <el-select filterable v-model="dataForm.inWarehouseId" placeholder="仓库编号/名称" @change="setAddress(dataForm.inWarehouseId, 2)"> 
                                <el-option v-for="item in inRealWareCode" :key="item.value" :label="item.label" :value="item.value.id" >
                                </el-option>
                            </el-select>
                        </el-form-item>
                         </el-col>
                         <el-col :span="8">
                        <el-form-item label="调入联系人/电话：" prop="inWarehouseName">
                            <div style="width: 190px">
                                <el-col :span="10" >
                                    <el-input  v-model="dataForm.inWarehouseName" placeholder="联系人"></el-input>
                                </el-col>
                                 <el-col :span="14" >
                                    <el-input  v-model="dataForm.inWarehouseMobile" placeholder="电话"></el-input>
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
                                <el-date-picker type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.expeAogTime" style="width: 100%;"></el-date-picker>                    
                        </el-form-item>
                         </el-col>
                         <el-col :span="16">
                         <el-form-item label="备注：" prop="remark">
                             <el-input v-model="dataForm.remark" placeholder="请输入备注" style="width:500px;"></el-input>
                        </el-form-item>
                        </el-col>
                    </el-form>  
                    
                   <div style="    width: 100%;margin-bottom: 15px;overflow: hidden;">
                        <el-col :span="3"><div style="font-weight: bold">商品明细</div></el-col>
                    </div>
                    
                     <div style="margin: 15px 0">
                        <el-button  type="primary" @click="initSelectPage">新增选择</el-button>
                        <!--
                        <el-button  type="primary" >导入</el-button>
                         <el-button  type="primary" >模板下载</el-button>
                         -->
                          <el-button  type="primary" @click="deleteC">删除</el-button>
                    </div>
                     
                    <el-table size="small" border :data="dataList"  ref="mainTable" 
                    @selection-change="handleSelectionChangeS"
                    v-loading="dataListLoading" element-loading-text="拼命加载中...">
                     <el-table-column
                      type="selection"
                      width="55">
                    </el-table-column>
                           <el-table-column prop="lineNo" width="50" label="序号"></el-table-column>
                        
                        
                        <el-table-column prop="skuCode" label="商品编号" width="140" sortable>
                           
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称" width="140">
                        </el-table-column>
                           <el-table-column prop="orginQty" label="原始数量" width="140" style="display:none">
                        </el-table-column>
                        <el-table-column prop="allotQty" label="调拨数量" width="140">
                        <template slot-scope="scope">
                            <el-input  v-model="scope.row.allotQty" placeholder="" size="small" @change="changeNum(scope.row, scope.$index)"></el-input>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="unitCodeInfo"  label="调拨单位" width="140">
                            <template slot-scope="scope">
                                <el-select  value-key="unitCode" v-model="unitCodes[scope.row.lineNo]" size="small" placeholder="调拨单位" @change="changeUnit(scope.row.skuUnitList, scope.$index)">
                                    <el-option v-for="(item,index) in scope.row.skuUnitList" :key="item.unitName" 
                                    :label="item.unitName" :key="item.unitCode" :value="item">
                                    </el-option>
                                </el-select>
                            </template>
                        </el-table-column>
                        <el-table-column prop="reasonCode" v-if="dataForm.isReturnAllotcate == 1"  label="退货原因" width="140">
                            <template slot-scope="scope">
                                <el-select v-model="scope.row.reasonCode" v-if="dataForm.isReturnAllotcate == 1" size="small" placeholder="退货原因">
                                    <el-option v-for="(item,index) in reasonList" :key="item.reasonName" 
                                    :label="item.reasonName" :value="item.reasonCode" >
                                    </el-option>
                                </el-select>
                            </template>
                        </el-table-column>
                        <el-table-column prop="batchRemark" label="批次备注" width="140">
                        <template slot-scope="scope">
                            <el-input  v-model="scope.row.batchRemark" placeholder="" size="small"></el-input>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="spec" label="规格" width="140">
                        
                        </el-table-column>
                        <el-table-column prop="useQty" label="库存数量" width="140">
                         <template slot-scope="scope">
                            <span v-if="dataForm.isQualityAllotcate == 0" v-model="scope.row.useQty">{{(scope.row.realQty-scope.row.lockQty) | rounding }}</span>
                            <span v-if="dataForm.isQualityAllotcate == 1" v-model="scope.row.useQty">{{scope.row.realQty | rounding }}</span>
                          </template> 
                        </el-table-column>
                        <el-table-column prop="baseNum" label="基本数量" width="140">
                        </el-table-column>
                        <el-table-column prop="baseUnit" label="基本单位" width="140">
                        </el-table-column>
                    </el-table>
                    
                   <el-dialog title="" :before-close="closeDialog" :visible.sync="prodDialogVisible"  v-if="prodDialogVisible" append-to-body>
                       <el-form :inline="true" size="small" :model="dataForm" class="demo-form-inline" style="overflow: hidden">
                            <el-col :span="8">
                            <el-input   v-model="prodForm.realWarehouseId" :disabled="true" style="display:none;"></el-input>
                                <el-form-item label="商品编码：" >
                                    <el-input v-model="prodForm.skuCodes" placeholder="" size="small" type="textarea"
  :autosize="{ minRows: 1, maxRows: 4}"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" style="margin-top: 33px">
                                <el-button type="primary" size="small" @click="onQueryProd">查询</el-button>
                               <el-button  type="primary" size="small" @click="resetForm('prodForm')">重置</el-button>
                            </el-col>
                        </el-form>
                        
                        <el-button  type="primary" style="margin-bottom: 15px" @click="confrimTable">确认</el-button>
                       <el-table size="small" border :data="prodList"  ref="subTable"
                       v-loading="prodListLoading" element-loading-text="拼命加载中..."
                       @selection-change="handleSelectionChange"
                       >
                         <el-table-column type="selection" width="55">
                         </el-table-column>
                        <el-table-column type="index" width="50" label="序号"></el-table-column>
                        <el-table-column prop="skuCode" label="商品编号" width="140" sortable>
                        </el-table-column>
                        <el-table-column prop="skuName" label="商品名称" width="140">
                        </el-table-column>
                        <el-table-column  prop="baseUnit"  label="商品单位" width="140">
                        </el-table-column>
                        <el-table-column prop="useQty" label="可用数量" width="140">
                            <template scope="scope">
                                <span v-if="dataForm.isQualityAllotcate == 0">{{(scope.row.realQty - scope.row.lockQty) | rounding  }}</span>
                                <span v-if="dataForm.isQualityAllotcate == 1">{{scope.row.realQty | rounding }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="qualityQty" label="质检数量" width="140">
                            
                        </el-table-column>
                    
                        <el-table-column prop="lockQty" label="锁定数量" width="140">
                            
                        </el-table-column>
                        
                      </el-table>
                      <el-pagination
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        :current-page="prodForm.pageIndex"
                        :page-sizes="[2,10, 20, 50, 100]"
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
                 </el-dialog>
        `
    });
});
