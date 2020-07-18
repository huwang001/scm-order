lyfdefine(function(){
    return ({
        data: function data() {
            return {
                visible: false,
                dataForm: {
                    id: 0,
                    username: '',
                    operation: '',
                    method: '',
                    params: '',
                    time: '',
                    ip: '',
                    createDate: ''
                },
                dataRule: {
                    username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
                    operation: [{ required: true, message: '用户操作不能为空', trigger: 'blur' }],
                    method: [{ required: true, message: '请求方法不能为空', trigger: 'blur' }],
                    params: [{ required: true, message: '请求参数不能为空', trigger: 'blur' }],
                    time: [{ required: true, message: '执行时长(毫秒)不能为空', trigger: 'blur' }],
                    ip: [{ required: true, message: 'IP地址不能为空', trigger: 'blur' }],
                    createDate: [{ required: true, message: '创建时间不能为空', trigger: 'blur' }]
                }
            };
        },

        methods: {
            load:function () {
                console.log('load succ')
            },
            init: function init(id) {
                var _this = this;

                this.dataForm.id = id || 0;
                this.visible = true;
                this.$nextTick(function () {
                    _this.$refs['dataForm'].resetFields();
                    if (_this.dataForm.id) {
                        _this.$http({
                            url: _this.$http.baseUrl('/admin-dome/syslog/info/' + _this.dataForm.id),
                            method: 'get',
                            params: _this.$http.adornParams()
                        }).then(function (_ref) {
                            var data = _ref.data;

                            if (data && data.code === '000000') {
                                _this.dataForm.username = data.sysLog.username;
                                _this.dataForm.operation = data.sysLog.operation;
                                _this.dataForm.method = data.sysLog.method;
                                _this.dataForm.params = data.sysLog.params;
                                _this.dataForm.time = data.sysLog.time;
                                _this.dataForm.ip = data.sysLog.ip;
                                _this.dataForm.createDate = data.sysLog.createDate;
                            }
                        });
                    }
                });
            },

            // 表单提交
            dataFormSubmit: function dataFormSubmit() {
                var _this2 = this;

                this.$refs['dataForm'].validate(function (valid) {
                    if (valid) {
                        _this2.$http({
                            url: _this2.$http.baseUrl('/admin-dome/syslog/' + (!_this2.dataForm.id ? 'save' : 'update')),
                            method: 'post',
                            data: _this2.$http.adornData({
                                'id': _this2.dataForm.id || undefined,
                                'username': _this2.dataForm.username,
                                'operation': _this2.dataForm.operation,
                                'method': _this2.dataForm.method,
                                'params': _this2.dataForm.params,
                                'time': _this2.dataForm.time,
                                'ip': _this2.dataForm.ip,
                                'createDate': _this2.dataForm.createDate
                            })
                        }).then(function (_ref2) {
                            var data = _ref2.data;

                            if (data && data.code === '000000') {
                                _this2.$message({
                                    message: '操作成功',
                                    type: 'success',
                                    duration: 1500,
                                    onClose: function onClose() {
                                        _this2.visible = false;
                                        _this2.$emit('refreshDataList');
                                    }
                                });
                            }
                        });
                    }
                });
            }
        },
        template: `
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="dataForm.username" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item label="用户操作" prop="operation">
      <el-input v-model="dataForm.operation" placeholder="用户操作"></el-input>
    </el-form-item>
    <el-form-item label="请求方法" prop="method">
      <el-input v-model="dataForm.method" placeholder="请求方法"></el-input>
    </el-form-item>
    <el-form-item label="请求参数" prop="params">
      <el-input v-model="dataForm.params" placeholder="请求参数"></el-input>
    </el-form-item>
    <el-form-item label="执行时长(毫秒)" prop="time">
      <el-input v-model="dataForm.time" placeholder="执行时长(毫秒)"></el-input>
    </el-form-item>
    <el-form-item label="IP地址" prop="ip">
      <el-input v-model="dataForm.ip" placeholder="IP地址"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createDate">
      <el-input v-model="dataForm.createDate" placeholder="创建时间"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    <addOrUpdate></addOrUpdate>
  </el-dialog>

`
    });
});