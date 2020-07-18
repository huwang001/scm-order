lyfdefine(function(){
    return ({
        data: function data() {
            return {
                POOL_NAME: '/pmz-admin-web',
                visible: false,
                showResultMsg:false,
                resultMsg:"",
                showErrorMsg:false,
                errorMsg:"",
                //文件上传相关
                fileLists: [],
                "fileList": {
                    "url": ''
                },
                dialogImageUrl: '',
                dialogVisible: false,
                fileUploadHeaders:{
                    "Content-Type": "multipart/form-data"
                },
            };
        },
        methods: {
            init() {
                let _this=this;
                _this.visible = true;
                _this.showResultMsg=false;
                _this.showErrorMsg=false;
                _this.$refs.upload.clearFiles();
            },
            closeDialog() {
                this.fileLists = [];
            },
            submitUpload() {
                this.$refs.upload.submit();
            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },
            beforeAvatarUpload(file) {
                const isExcel = (file.name.indexOf(".xlsx")>0&&file.type.indexOf("vnd")>0);
                const isLt2M = file.size / 1024 / 1024 <= 1;

                if (!isExcel) {
                    this.$message.error('上传文件只能是 xlsx 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 1MB!');
                }
                return isExcel && isLt2M;
            },
            handleExceed(files, fileList) {
                this.$message.warning(`只能上传一个文件`);
            },
            fileUpload: function(response, file){
                let _this=this;
                _this.errorMsg = '';
                _this.$refs.upload.clearFiles();
                if(response.code=='0'){
                    _this.showResultMsg=true;
                    _this.showErrorMsg=false;
                }else{
                    _this.showResultMsg=false;
                    _this.showErrorMsg=true;
                    _this.errorMsg=response.msg;
                }
            },
            frameWorkDownload(){
                let downloadUrl = '';
                if (window.sessionStorage.getItem("menuList") === null) {
                    downloadUrl = "/pmz/clientConfig/download";
                } else {
                    downloadUrl = this.POOL_NAME + "/pmz/clientConfig/download";
                }
                location.href=downloadUrl;
            },
            setUploadUrl(){
                let uploadUrl = '';
                if (window.sessionStorage.getItem("menuList") === null) {
                    uploadUrl = "/pmz/clientConfig/clientConfigUpload";
                } else {
                    uploadUrl = this.POOL_NAME + "/pmz/clientConfig/clientConfigUpload";
                }
                return uploadUrl;
            }
        },
        template: `
  <el-dialog
    :title="'批量更新'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @close='closeDialog'
    width="50%">
    <el-form  @submit.native.prevent label-width="100px">
    <el-form-item label="" >
      <el-button @click="frameWorkDownload" size="small" type="primary">下载模板</el-button>
    </el-form-item>
    <el-form-item label="选择文件">
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="setUploadUrl()"
          accept="xlsx"
          multiple
          :limit="1"
          :before-upload="beforeAvatarUpload"
          :on-success="fileUpload"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-exceed="handleExceed"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">选取数据文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">确定上传</el-button>
          <div slot="tip" style="color:red" class="el-upload__tip">1、只能上传xlsx文件，且不超过1MB</div>
        </el-upload>
    </el-form-item>
    <!--<el-form-item v-if="showResultMsg" label="导入结果" >-->
       <!--<span>{{ resultMsg }}</span> <a v-if="showErrorUrl" :href="errorFileUrl">查看失败原因</a>-->
    <!--</el-form-item>-->
    
    <el-form-item v-if="showResultMsg">
       <span>导入成功</span>
    </el-form-item>
    <el-form-item v-if="showErrorMsg" >
       <span>导入失败，失败原因：{{errorMsg}}</span>
    </el-form-item>
    </el-form>
  </el-dialog>

`
    });
});