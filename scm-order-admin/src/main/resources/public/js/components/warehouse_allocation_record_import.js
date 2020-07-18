lyfdefine([], function () {
    return ({
        data: function () {
            return {
                visible: false,
                files: [],
                visibleError: false,
                message: null,
                // ufile: ""
            }
        },
        components: {},
        methods: {
            closeVisible: function () {
                this.visible = false;
                this.$refs['dataForm'].resetFields();
            },
            init: function init() {
                this.visible = true;
            },
            handleSuccess(res) {
                if (res.code === '0') {
                    var _this = this;
                    _this.$emit('refreshDataList');
                    this.$message({
                        message: '上传成功！',
                        type: 'success',
                        duration: 2000,
                    });
                } else {
                    if (typeof (res.msg) == "object") {
                        var _this = this;
                        this.open(res.msg);
                        this.visibleError = true;
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error',
                            duration: 2000,
                        });
                    }
                }
            },
            open(msg) {
                var allMsg = '';
                for (var i = 0; i < msg.length; i++) {
                    if (i == 0) {
                        allMsg = msg[0];
                    } else {
                        allMsg = allMsg + "<br/>" + msg[i];
                    }
                }
                this.message = allMsg
            },
            beforeAvatarUpload(file) {
                if (!file) {
                    return;
                }
                var index = file.name.lastIndexOf('.');
                var Xls = file.name.substr(index + 1);
                var isLt1M = file.size / 1024 / 2048 < 1;
                var isExcel = Xls === 'xls' || Xls === 'xlsx';
                if (!isExcel) {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false;
                }
                if (!isLt1M) {
                    this.$message.error('上传文件大小不能超过 2MB!');
                    return false;
                }
                return isExcel && isLt1M;
            },
            closeDialog(done) {
                this.visible = false;
                // this.ufile = '';
                this.files = [];
            },
            closeDialogError(done) {
                this.visibleError = false;
            },
        },
        template: `
          <el-dialog
                 title="导入"
                 :close-on-click-modal="false"
                 width="40%"
                 :visible.sync="visible"
                 :before-close="closeDialog" append-to-body>
          <el-upload 
     action="/scm-order-admin/order/v1/whAllocation/importExcel"
     accept=".xls,.xlsx"
     :before-upload="beforeAvatarUpload"
     :on-success = 'handleSuccess'
     :file-list="files">
    
    <el-button  type="primary">点击上传</el-button>
    <div slot="tip" class="el-upload__tip"> <a href="/scm-order-admin/static/doc/whallocation.xlsx">模板下载</a></div>
    </el-upload>
<el-dialog
                 title="错误提示"
                 :close-on-click-modal="false"
                 width="40%"
                 :visible.sync="visibleError"
                 :before-close="closeDialogError"
                 append-to-body>
        <div style="height:200px;overflow:scroll;color: red"  >
       <div v-html="message">
       
</div>
</div>
          </el-dialog>
          </el-dialog>
        `
    });
});
