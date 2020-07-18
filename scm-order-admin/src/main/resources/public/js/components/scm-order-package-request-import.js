lyfdefine([],function(){
    return ({
        props: {
            importType: {
                type: String,
                default: 'create'
            },
            visible: {
                type: Boolean,
                default: false
            },
            callback: {
                type: Function,
                default: () => {},
            }
        },
        data() {
            return {
                msgTip: '',
                showTip: false,
                show: false,
                loading: false,
                POOL_NAME: '/scm-order-admin',
                timer: null,
                domeDownloadHref: '/scm-order-admin/static/doc/batchCreateDemand.xlsx',
                createdBlur: false,
                cancelBtnText: '取消',
                titleText: '批量创建',
                importErrInfo: '', //导入校验失败信息
                importBlur: false,
                importCode: '', //导入批次号
                activeStep: 1,
                fileList: [],
                dataSize: 0,
                fileName: '',
                filePath: '',
            }
        },
        filters: {
            formatFileSize(fileSize) {
                if (!fileSize)
                    return "";
                var num = 1024.00; //byte
                if (fileSize < num)
                    return size + "B";
                if (fileSize < Math.pow(num, 2))
                    return (fileSize / num).toFixed(2) + "KB"; //kb
                if (fileSize < Math.pow(num, 3))
                    return (fileSize / Math.pow(num, 2)).toFixed(2) + "M"; //M
                if (fileSize < Math.pow(num, 4))
                    return (fileSize / Math.pow(num, 3)).toFixed(2) + "G"; //G
                return (fileSize / Math.pow(num, 4)).toFixed(2) + "T"; //T

            }
        },
        methods: {
            onClickRemoveBtn(item) {
                const index = this.fileList.findIndex(n => n.uid === item.uid)
                this.fileList.splice(index, 1)
                if (this.fileList.length === 0) {
                    this.activeStep = 1
                }
            },
            onClickSubmitBtn() {
                let filebject = new FormData();
                filebject.append('file', this.fileList[0].raw);
                this.loading = true
                this.$http({
                    headers: {
                        contentType: 'multipart/form-data'
                    },
                    timeout: 60000,
                    method: "post",
                    url: this.POOL_NAME + '/order/v1/demand/importExcel',
                    data: filebject
                })
                    .then(
                        res => {
                            this.loading = false
                            if (res.data.code === "0") {
                                this.$message({
                                    message: '创建成功',
                                    type: 'success',
                                    duration: 1000,
                                });
                                this.fileList = [];
                                this.activeStep = 3;
                                this.cancelBtnText = "关闭";
                                this.createdBlur = true;
                                this.importErrInfo = '导入成功'
                            } else {
                                this.msgTip = ''
                                let msg = ''
                                if (Array.isArray(res.data.msg) && res.data.msg[0]) {
                                    msg = res.data.msg[0].replace('[', '').replace(']', '').split(',').join('</br>')
                                    this.msgTip = msg
                                    this.showTip = true
                                } else {
                                    msg = res.data.msg
                                    this.$alert(msg, '提示', {
                                        confirmButtonText: '确定',
                                        dangerouslyUseHTMLString: true,
                                        callback: action => {}
                                    });
                                }
                            }
                        })
                    .catch(result => {
                        this.loading = false
                        this.$alert(result.toString(), '提示', {
                            confirmButtonText: '确定',
                            dangerouslyUseHTMLString: true,
                            callback: action => {}
                        });
                    })
            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                this.fileList = [];
                this.dataSize = 0;
                this.activeStep = 1;
                this.cancelBtnText = "取消";
                this.createdBlur = false;
                this.importBlur = false;
                this.$emit('dialog-close');
            },
            handleChangeUpload(file) {
                this.fileList.push(file);
                this.activeStep = 2;
            }
        },
        template: `
<el-dialog :title="titleText" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="700px">
    <div v-loading="loading">
        <el-steps :active="activeStep" align-center>
            <el-step title="上传文档"></el-step>
            <el-step title="云端导入"></el-step>
            <el-step title="导入完成"></el-step>
        </el-steps>
        <div v-show="activeStep !== 3">1.
            <el-button type="text">点击<a :href="domeDownloadHref" style="color:#17b3a3;">下载导入数据模板</a></el-button>将要导入的数据填充到数据导入模板文件中。
            <div>
                <div style="color:red;">注意事项:</div>
                <p>（1）模板中的表头不可更改，表头行不可删除；</p>
                <p>（2）除必填的列以外，不需要的列可以删除；</p>
                <p>（3）单次导入的数据不超过1000条；</p>
            </div>
        </div>
        <div v-show="fileList.length === 0 && activeStep !== 3">
            2. 请选择要导入的数据文件。
            <el-upload :on-change="handleChangeUpload" :show-file-list="false" :auto-upload="false" class="upload-demo" style="padding:5px 18px;" action="https://jsonplaceholder.typicode.com/posts/" :limit="1" :file-list="fileList">
                <el-button size="small" type="primary"><i class="el-icon-plus"></i>&nbsp;选择文件</el-button>
                <div slot="tip" class="el-upload__tip" style="color:#ccc;">请选择excel文件，允许上传附件类型（xls,xlsx,csv），文件大小不得大于100MB</div>
            </el-upload>
        </div>
        <div class="file-list" v-show="fileList.length > 0">
            <div class="file-list__item" @mouseover="show = true" @mouseleave="show = false" style="marginTop:20px;padding:10px 20px;backgroundColor: #eaeaea;display:flex;justifyContent: space-between;" v-for="item in fileList" :key="item.name">
                <div>{{item.name}}</div>
                <div>{{item.size | formatFileSize}}</div>
                <div>完成<i class="el-icon-delete" @click="onClickRemoveBtn(item)" v-show="show" style="padding: 0 5px;cursor: pointer;"></i></div>
            </div>
        </div>
         <p style="text-align: center;color:red;margin-top:10px;" v-if="activeStep === 3">{{importErrInfo}}</p>
        <div slot="footer" class="dialog-footer" style="text-align:center;marginTop:10px;">
            <el-button type="primary" @click.native="onClickSubmitBtn" :disabled="fileList.length===0" v-show="activeStep !==3">开始导入</el-button>
            <el-button @click.native="onClickCancelBtn">{{activeStep ===3 ? '完成' : '取消'}}</el-button>
        </div>
    </div>
    <el-dialog title="提示" :visible.sync="showTip" append-to-body>
        <div v-html="msgTip" style="height:400px;overflow:scroll;"></div>
    </el-dialog>
</el-dialog>
        `
    });
});
