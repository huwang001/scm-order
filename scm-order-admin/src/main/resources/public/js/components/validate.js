var isInteger = function isInteger(rule, value, callback) {

    // if (!value) {
    //     return callback(new Error('输入不可以为空'));
    // }
    if(!value){
        callback();
    }

    const re = /^-?\d+$/;
    const rsCheck = re.test(value);
    if (!rsCheck) {
        callback(new Error('请输入整数'));
    } else {
        callback();
    }

}
var syncRateValidate = function syncRateValidate(rule, value, callback) {

    if (!value) {
        return callback(new Error('输入不可以为空'));
    }
    const re = /^-?\d+$/;
    const rsCheck = re.test(value);
    if (!rsCheck) {
        callback(new Error('请输入整数'));
    }else if(value< 0 || value >100){
        callback(new Error('必须在0-100之间的整数'));
    } else {
        callback();
    }
}

var priorityValidate = function syncRateValidate(rule, value, callback) {

    if (!value) {
        return callback(new Error('输入不可以为空'));
    }
    const re = /^-?\d+$/;
    const rsCheck = re.test(value);
    if (!rsCheck) {
        callback(new Error('请输入整数'));
    } else if(value< 1 || value >100){
        callback(new Error('必须在1-100之间的整数'));
    } else {
        callback();
    }
}
var exist = function exist(rule, value, callback) {

    if (value) {
        $http({
            url: '/stock-admin/stock/virtual_warehouse/code/' + value,
            method: 'get'
        }).then(function (_ref) {
            var data = _ref.data;


        });
    }

};


lyfdefine(function () {
    return ({
        data: [],

        methods: {
            isLYFEmail: function isLYFEmail(str) {
                const reg = /^[a-z0-9](?:[-_.+]?[a-z0-9]+)*@laiyifen\.com$/i;
                return reg.test(str.trim());
            },
            isInteger2: function isInteger(rule, value, callback) {

                if (!value) {
                    return callback(new Error('输入不可以为空'));
                }
                if (!Number(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    const re = /^[0-9]*[1-9][0-9]*$/;
                    const rsCheck = re.test(value);
                    if (!rsCheck) {
                        callback(new Error('请输入正整数'));
                    } else {
                        callback();
                    }
                }
            }
        }
    })
})

