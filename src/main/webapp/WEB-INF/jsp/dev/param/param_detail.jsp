<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_param_detail_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="text-align: center; display: none">
            <input id="sys_param_detail_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_param_detail_paramCode" name="paramCode" class="easyui-textbox"
                   data-options="validateOnCreate: false, label: '参数编码', required: true, labelPosition: 'top', validType: 'remote[\'sys/param/validate/paramCode\', \'value\']', invalidMessage: '参数编码已存在, 请修改'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="'sys_param_detail_paramDesc'" name="paramDesc" class="easyui-textbox" data-options="validateOnCreate: false, label: '参数名称', required: true, labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="width:50%; float: left; padding: 0 20px">
            <input id="sys_param_detail_paramValue" name="paramValue" class="easyui-textbox" data-options="validateOnCreate: false, label: '参数值', required: true, labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="width:100%; float: left; padding: 0 20px">
            <input id="sys_param_detail_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', multiline: true" style="width: 100%; height: 188px"/>
        </div>
        <div style="text-align: center; display: none">
            <input id="sys_user_detail_version" name="version" class="easyui-textbox" data-options="label: '版本'"/>
        </div>
    </fieldset>
</form>
<form id="sys_param_detail_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_param_detail_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_param_detail_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_param_detail_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_param_detail_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 初始化表单
    function clearSysParamDetailForm() {
        $('#sys_param_detail_form').form('reset');
        $('#sys_param_detail_form_2').form('reset');
    }

    // 加载数据(数据)
    function loadSysParamDetailForm(row) {
        $('#sys_param_detail_form').form('load', row);
        $('#sys_param_detail_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
        $('#sys_param_detail_paramCode').textbox({
            readonly: true,
            validType: null
        });
    }

    // 提交表单('create'/'update', 回调函数)
    function commitSysParamDetailForm(op, callback) {
        $('#sys_param_detail_form').form('submit', {
            url: 'sys/param/' + op,
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
                    eval(callback + '();');
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            }
        });
    }
</script>
</body>
</html>
