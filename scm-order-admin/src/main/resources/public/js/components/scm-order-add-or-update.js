lyfdefine(
    function () {
        return ({
            data : function () {
                return {
                    POOL_NAME: '/pmz-admin-web',
                    typeBtn: '',
                    visible: false,
                    disabled: false,
                    mercNature: [],
                    mercType: [],
                    mercStatus: [],
                    dataForm: {
                        id: '', //id
                        clientCode:"",   // 客户端编号
                        verifyInfo:"", // 校验信息
                        terminalNo:"", // 终端编号
                        userCode: '', //用户编号
                        userPwd: '', //用户密码
                        orgCode: '', //机构号
                        depCode: '', //部门号
                        desc: '', //标签描述
                        merchantId:"", // 商户id
                        creator: '', //创建人
                        createTime: '', //创建时间
                        modifier: '', //修改人
                        updateTime: '', //修改时间
                        tenantId: '', //租户id
                        appId: '', //应用id
                        isDeleted: '', //是否删除
                        merchantName: '' //
                    },
                    dataRule: {
                        clientCode: [
                            { required: true, message: '客户端编号不能为空', trigger: 'blur' }
                        ],
                        orgCode: [
                            { required: true, message: '机构号不能为空', trigger: 'blur' }
                        ]
                    },

                    uploadParam:{
                        clientCode:"",   // 商户编号
                        orgCode:"", // 支付方式
                    },
                    dialogImageUrl: '',
                    dialogVisible: false,
                    // fileUploadHeaders:{
                    //     "Content-Type": "multipart/form-data"
                    // },
                }
            },
            methods: {
                init : function (id,type,signType) {
                    this.typeBtn = type
                    this.signType = signType
                    this.dataForm.id = id || 0
                    this.visible = true
                    this.$nextTick(() => {
                        this.$refs['dataForm'].resetFields()
                        if (this.dataForm.id) {
                            this.$http({
                                url: this.POOL_NAME + `/pmz/clientConfig/getClientConfigById`,
                                method: 'post',
                                data :{
                                    'id': this.dataForm.id
                                },
                                headers:{
                                    'Content-Type':'application/json'
                                }
                            }).then(data =>  {
                                let _data = data.data;
                                if (_data && _data.code == 0) {
                                    this.dataForm = _data.data
                                }
                            }).catch(result=> {
                                let _data = result.response.data;
                                this.$message({
                                    message: "操作失败，原因："+_data.msg,
                                    type: 'fail',
                                    duration: 1500,
                                });
                            }).catch(() => {
                                this.$message({
                                    type: 'fail',
                                    message: '服务异常，请稍后再试',
                                    duration: 1500,
                                });
                            });
                        }
                    })
                },

                queryMerc: function () {
                    if (this.dataForm.orgCode == ''){
                        this.dataForm.merchantName = "";
                    }else{
                        this.$http({
                            url: this.POOL_NAME + `/pmz/merc/queryByCd`,
                            method: 'post',
                            data :{
                                'mercCd': this.dataForm.orgCode
                            },
                            headers:{
                                'Content-Type':'application/json'
                            }
                        }).then(data =>  {
                            let _data = data.data;
                        if (_data && _data.code == 0) {
                            if (_data.data){
                                this.dataForm.merchantName = _data.data.mercName
                            }else {
                                this.dataForm.merchantName = '';
                                // this.dataForm.orgCode = '';
                                // this.$message({
                                //     message: "未查询到上级商户信息",
                                //     type: 'fail',
                                //     duration: 1500,
                                // });
                            }
                        }
                    }).catch(result=> {
                            let _data = result.response.data;
                        this.$message({
                            message: "操作失败，原因："+_data.msg,
                            type: 'fail',
                            duration: 1500,
                        });
                    }).catch(() => {
                            this.$message({
                                type: 'fail',
                                message: '服务异常，请稍后再试',
                                duration: 1500,
                            });
                    });
                    }
                },



                // 表单提交
                dataFormSubmit : function () {
                    this.$refs['dataForm'].validate(valid =>  {
                        if (valid) {
                            let path = "";
                            if (this.dataForm.id){
                                path = "update";
                            } else {
                                path = "add";
                            }
                            this.$http({
                                url: this.POOL_NAME + `/pmz/clientConfig/`+ path,
                                method: 'post',
                                data: this.$http.adornData(
                                    this.dataForm
                                ),
                                headers:{
                                    'Content-Type':'application/json'
                                }
                            }).then(result =>  {
                                let _data = result.data;
                                if (_data && _data.code == 0) {
                                    this.$message({
                                        message: '操作成功',
                                        type: 'success',
                                        duration: 500,
                                        onClose: () => {
                                            this.visible = false
                                            this.$emit('refreshDataList')
                                        }
                                    })
                                }else {
                                    this.$message({
                                        message: "操作失败，原因："+_data.msg,
                                        type: 'fail',
                                        duration: 1500,
                                        // onClose: () => {
                                        //     this.visible = false
                                        //     this.$emit('refreshDataList')
                                        // }
                                    })
                                }
                            }).catch(result=> {
                                let _data = result.response.data;
                                this.$message({
                                    message: "操作失败，原因："+_data.msg,
                                    type: 'fail',
                                    duration: 1500,
                                });
                            }).catch(() => {
                                this.$message({
                                    type: 'fail',
                                    message: '服务异常，请稍后再试',
                                    duration: 1500,
                                });
                            });
                        }
                    })
                },

                // 证书上传
                submitUpload() {
                    this.uploadParam.mercCd = this.dataForm.mercCd;
                    this.uploadParam.productCd = this.dataForm.productCd;
                    this.$refs.upload.submit();
                },
                handleRemove(file, fileList) {
                    console.log(file, fileList);
                },
                handlePreview(file) {
                    console.log(file);
                },
                beforeAvatarUpload(file) {
                    console.log(file);
                },
                handleExceed(files, fileList) {
                    this.$message.warning(`只能上传一个文件`);
                },
                fileUpload: function(response, file){
                    let _this=this;
                    _this.$refs.upload.clearFiles();
                    if(response.code=='0'){
                        this.dataForm.certificateUrlPath =response.data.data;
                    }else{
                        this.$message.info(response.msg)
                    }
                },

                setUploadUrl(){
                    let uploadUrl = '';
                    if (window.sessionStorage.getItem("menuList") === null) {
                        uploadUrl = "/pmz/merc/secret/UploadCertificate";
                    } else {
                        uploadUrl = this.POOL_NAME + "/pmz/merc/secret/UploadCertificate";
                    }
                    return uploadUrl;
                },

            }
            ,template: `  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    :model="dataForm">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
    
     <el-form-item label="客户端编号" prop="clientCode" label-width="200px">
      <el-input v-model="dataForm.clientCode" placeholder="客户端编号"   maxlength="32" >
      </el-input>
      
      
      
    </el-form-item>
    <el-form-item label="门店编号" prop="orgCode" label-width="200px">
      <el-input v-model="dataForm.orgCode" placeholder="门店编号"  v-bind:disabled="dataForm.id"  maxlength="32" @change="queryMerc">
      </el-input>
    </el-form-item>
    <el-form-item label="门店名称" prop="merchantName" label-width="200px">
      <el-input v-model="dataForm.merchantName" placeholder="门店名称" disabled="false" maxlength="3000" >
      </el-input>
    </el-form-item>
    
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" v-if="typeBtn == 1" size="mini" @click="dataFormSubmit()">新增</el-button>
      <el-button type="primary" v-if="typeBtn == 2" size="mini" @click="dataFormSubmit()">修改</el-button>
    </span>
  </el-dialog>`
        })})

