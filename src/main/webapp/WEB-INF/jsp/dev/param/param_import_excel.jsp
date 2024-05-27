<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_param_import_form" method="post" enctype="multipart/form-data">
    <div style="width:100%; float: left; padding: 10px 20px">
        <input class="easyui-filebox" name="excelFile"
               data-options="validateOnCreate: false, required: true, prompt: '选择一个.xlsx格式文件', buttonText: '本地上传', accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'" style="width:100%"/>
    </div>
    <div style="width:100%; float: left; padding: 10px 20px">
        <a id="sys_param_import_btn" href="javascript:" class="easyui-linkbutton" style="width:100%">上传</a>
    </div>
</form>
<script type="text/javascript">
    $('#sys_param_import_btn').click(function () {
        $('#sys_param_import_form').form('submit', {
            url: 'sys/param/excel/in',
            onSubmit: function () {
                let isValid = $(this).form('validate');
                if (isValid) {
                    $.messager.progress({
                        title: '提示',
                        msg: '保存中, 请稍候...',
                        text: '',
                        interval: 1000
                    });
                }
                return isValid;
            },
            success: function (result) {
                result = JSON.parse(result);
                $.messager.progress('close');
                if (result.code == 0) {
                    sysParamCallback();
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            }
        });
    });
</script>
</body>
</html>
