lyfdefine(
    function () {
        var style = document.createElement("style");
        style.type = "text/css";
        try{
            style.appendChild(document.createTextNode(".scm-order-package-request-list[data-scm-order-package] .request-list-card2{margin-top:15px;}.scm-order-package-request-add[data-scm-order-package] .el-dialog__body{padding:0px;}.scm-order-package-request-add[data-scm-order-package] .el-card .el-card__header{background-color:#f2f2f2;padding:15px;}.scm-order-package-request-add[data-scm-order-package] .el-card .el-card__body{padding:10px;}.scm-order-package-request-add[data-scm-order-package] .request-add__product__header{margin-bottom:10px;}"));

        }catch(ex){
            style.styleSheet.cssText = ".scm-order-package-request-list[data-scm-order-package] .request-list-card2{margin-top:15px;}.scm-order-package-request-add[data-scm-order-package] .el-dialog__body{padding:0px;}.scm-order-package-request-add[data-scm-order-package] .el-card .el-card__header{background-color:#f2f2f2;padding:15px;}.scm-order-package-request-add[data-scm-order-package] .el-card .el-card__body{padding:10px;}.scm-order-package-request-add[data-scm-order-package] .request-add__product__header{margin-bottom:10px;}";//针对IE

        }
        var head = document.getElementsByTagName("head")[0];
        head.appendChild(style);
        return null
    });