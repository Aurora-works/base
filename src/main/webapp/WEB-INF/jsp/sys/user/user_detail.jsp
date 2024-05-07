<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_user_detail_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="text-align: center; display: none">
            <input id="sys_user_detail_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_username" name="username" class="easyui-textbox"
                   data-options="label: '用户名', labelPosition: 'top', validateOnCreate: false, required: true, validType: 'remote[\'sys/user/validate/username\', \'value\']', invalidMessage: '用户名已存在, 请修改'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="'sys_user_detail_nickname'" name="nickname" class="easyui-textbox"
                   data-options="label: '姓名', labelPosition: 'top', validateOnCreate: false, required: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_sex" name="sex" class="easyui-combobox"
                   data-options="label: '性别', labelPosition: 'top', panelHeight: 'auto', editable: false, valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/sex'" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_email" name="email" class="easyui-textbox"
                   data-options="label: '电子邮件', labelPosition: 'top', validType: 'email'" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_mobilePhoneNumber" name="mobilePhoneNumber" class="easyui-textbox"
                   data-options="label: '手机号码', labelPosition: 'top', validateOnCreate: false, required: true, validType: 'mobilePhoneNumber'" style="width: 100%"/></div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_status" name="status" class="easyui-combobox"
                   data-options="label: '状态', labelPosition: 'top', validateOnCreate: false, required: true, panelHeight: 'auto', editable: false, valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/status'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="text-align: center; display: none">
            <input id="sys_user_detail_version" name="version" class="easyui-textbox" data-options="label: '版本'"/>
        </div>
    </fieldset>
</form>
<form id="sys_user_detail_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_user_detail_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 初始化表单
    function clearSysUserDetailForm() {
        $('#sys_user_detail_form').form('reset');
        $('#sys_user_detail_form_2').form('reset');
    }

    // 加载数据(数据)
    function loadSysUserDetailForm(row) {
        $('#sys_user_detail_form').form('load', row);
        $('#sys_user_detail_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
        $('#sys_user_detail_username').textbox({
            readonly: true,
            validType: null
        });
    }

    // 提交表单('create'/'update', 回调函数)
    function commitSysUserDetailForm(op, callback) {
        $('#sys_user_detail_form').form('submit', {
            url: 'sys/user/' + op,
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
                if (result.code == '0') {
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
