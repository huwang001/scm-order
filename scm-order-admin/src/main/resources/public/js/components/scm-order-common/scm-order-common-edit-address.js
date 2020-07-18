lyfdefine([], function () {
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
                submitForm: {
                    province: 1,
                    city: 1,
                    area: 1,
                    address: '嘿嘿软件园3号楼'
                },
                submitFormOption: {
                    provinces: [{
                        label: '上海',
                        value: 1
                    }],
                    cities: [{
                        label: '上海',
                        value: 1
                    }],
                    areas: [{
                        label: '浦东新区',
                        value: 1
                    }]
                }
            };
        },
        methods: {
            init() {},
            onClickConfirmBtn() {

            },
            onClickCancelBtn() {
                this.handleBeforeClose();
            },
            handleBeforeClose() {
                this.$emit("dialog-close");
            },
        },
        watch: {
            visible(n) {
                if (n) {
                    this.init()
                }
            }
        },
        template: /*html*/`
 <el-dialog title="修改地址" data-scm-order-common class="scm-order-common-dialog scm-order-common-edit-address" :visible.sync="visible" :close-on-click-modal="false" :beforeClose="handleBeforeClose" width="500px">
    <div class="common-edit-address__body">
        <el-form :model="submitForm" inline-message ref="submitForm" size="mini" label-width="80px">
            <el-form-item label="省:" prop="province">
                <el-select v-model="submitForm.province">
                    <el-option v-for="item in submitFormOption.provinces" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="市:" prop="city">
                <el-select v-model="submitForm.city">
                    <el-option v-for="item in submitFormOption.cities" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="区:" prop="area">
                <el-select v-model="submitForm.area">
                    <el-option v-for="item in submitFormOption.areas" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="详细地址:" prop="saleCode">
                <el-input style="width:100%" v-model="submitForm.address"></el-input>
            </el-form-item>
        </el-form>
    </div>
    <div slot="footer" class="common-edit-address__footer">
        <el-button size="mini" type="primary" @click.native="onClickConfirmBtn">确定</el-button>
        <el-button size="mini" @click.native="onClickCancelBtn">取消</el-button>
    </div>
</el-dialog>
    `
    });
});
