<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_role_detail_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="text-align: center; display: none">
            <input id="sys_role_detail_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_roleCode" name="roleCode" class="easyui-textbox"
                   data-options="validateOnCreate: false, label: '角色编码', required: true, labelPosition: 'top', validType: 'remote[\'sys/role/validate/roleCode\', \'value\']', invalidMessage: '角色编码已存在, 请修改'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_roleName" name="roleName" class="easyui-textbox" data-options="validateOnCreate: false, label: '角色名称', required: true, labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_parentId" name="parentId" class="easyui-combotree"
                   data-options="url: 'sys/role/tree/combo', validateOnCreate: false, label: '父角色', panelHeight: 'auto', editable: false, labelPosition: 'top'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_orderBy" name="orderBy" class="easyui-textbox" data-options="validateOnCreate: false, label: '排序', required: true, labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_status" name="status" class="easyui-combobox"
                   data-options="validateOnCreate: false, label: '状态', required: true, panelHeight: 'auto', editable: false, labelPosition: 'top', valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/status'"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="text-align: center; display: none">
            <input id="sys_role_detail_version" name="version" class="easyui-textbox" data-options="label: '版本'"/>
        </div>
    </fieldset>
</form>
<form id="sys_role_detail_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_role_detail_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 初始化表单
    function clearSysRoleDetailForm() {
        $('#sys_role_detail_form').form('reset');
        $('#sys_role_detail_form_2').form('reset');
    }

    // 加载数据(数据)
    function loadSysRoleDetailForm(row) {
        $('#sys_role_detail_form').form('load', row);
        $('#sys_role_detail_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
        $('#sys_role_detail_roleCode').textbox({
            readonly: true,
            validType: null
        });
    }

    // 提交表单('create'/'update', 回调函数)
    function commitSysRoleDetailForm(op, callback) {
        $('#sys_role_detail_form').form('submit', {
            url: 'sys/role/' + op,
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
                let roleCode = $('#sys_role_detail_form').find('input[name=roleCode]').val();
                result = JSON.parse(result);
                $.messager.progress('close');
                if (result.code == 0) {
                    callback(roleCode);
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
