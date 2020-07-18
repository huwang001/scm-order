lyfdefine(function () {
    return ({
        data: function data() {
            return {
                dataForm: {

                },
                loading:false,
                visible: false,
                width:"30%",
            };

        },

        methods: {

            handle: function () {
                let _this = this;
                if(_this.dataForm.sapDeliveryCode) {
                    //去掉空行
                    _this.dataForm.sapDeliveryCode = _this.dataForm.sapDeliveryCode.replace(/(\n|\r){2,}/g,"\n");
                    //去掉首尾换行
                    _this.dataForm.sapDeliveryCode = _this.dataForm.sapDeliveryCode.replace(/^(\n|\r)+|(\n|\r)+$/g,'');
                    let temp = Array.from(new Set(_this.dataForm.sapDeliveryCode.split(/\n|\r/g)));
                    if (temp.length > 100) {
                        _this.$message({
                            message: "传入的交货单号不能超过100个",
                            type: 'error',
                            duration: 2000,
                        });
                        return;
                    }
                    _this.$confirm('此操作是门店拒收落差异单操作,后续将无法再确认收货, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        _this.loading = true;
                        this.$http({
                            url: '/order/v1/disparity/record/rejectConfirmDisparityBySapCode' ,
                            data: _this.$http.adornData({
                                "sapDeliveryCode": _this.dataForm.sapDeliveryCode
                            }),
                            method: 'post'
                        }).then(function (_ref2) {
                            var data = _ref2.data;
                            if(data && data.code == 0) {
                                let duration = 3000;
                                if(data.data.indexOf('未创建不符合条件')>0){
                                    duration =6000;
                                }
                                _this.$message({
                                    message: data.data,
                                    type: 'success',
                                    showClose: true,
                                    duration: duration,
                                    dangerouslyUseHTMLString: true
                                });
                            } else {
                                _this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                            _this.loading = false;
                            _this.visible = false;
                            _this.dataForm.sapDeliveryCode ='';
                            // _this.$emit('refreshDataList');

                        }).catch(function (error) {

                            _this.$message({
                                type: 'error',
                                message: '系统异常'
                            });
                            console.log(error);
                            _this.loading = false;
                            _this.visible = false;
                        });
                    }).catch(() => {
                        _this.loading = false;
                        _this.visible = false;
                    });
                } else {
                    _this.$message({
                        type: 'error',
                        message: '请输入交货单号'
                    });
                }
            },
            closeVisible:function(){
                this.visible = false;
            },
            init: function () {

                this.visible = true;
                this.loading = false;
                console.log("======")
            }
        },
        template: /*html*/`
  <el-dialog 
            :width="width"
            :title="'整单拒收落差异单批处理'"
            :visible.sync="visible">
             <div v-loading="loading" :element-loading-text="'批量处理中...'" >
            <el-form label-position="left" :inline="true" >
                <el-form-item label="交货单号：">
        <el-input v-model="dataForm.sapDeliveryCode" placeholder="多个换行分隔,空行将被忽略,最多100" type="textarea" :autosize="{ minRows: 1, maxRows: 10}"  clearable></el-input>
      </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" style="margin-left:60%">
              <el-button type="primary" @click="handle">确认</el-button>
               <el-button type="primary" @click="closeVisible">返回</el-button>
            </span>
            </div>
          </el-dialog>
    `
    });
});
