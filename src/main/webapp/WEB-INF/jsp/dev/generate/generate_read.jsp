<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_generate_read_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="display: none">
            <input id="sys_generate_read_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_menuId" name="menuIdText" class="easyui-textbox" data-options="label: '菜单', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_tableId" name="tableIdText" class="easyui-textbox" data-options="label: '表单', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<form id="sys_generate_read_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_generate_read_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 加载数据(数据)
    function loadSysGenerateReadForm(row) {
        $('#sys_generate_read_form').form('load', row);
        $('#sys_generate_read_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
    }
</script>
</body>
</html>
