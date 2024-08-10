<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_role_read_form" method="post">
    <div style="display: none">
        <input id="sys_role_read_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_roleCode" name="roleCode" class="easyui-textbox" data-options="label: '角色编码', labelPosition: 'top', readonly: true" style="width: 100%"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_roleName" name="roleName" class="easyui-textbox" data-options="label: '角色名称', labelPosition: 'top', readonly: true" style="width: 100%"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_parentId" name="parentId" class="easyui-combotree"
               data-options="url: 'sys/role/tree/combo', label: '父角色', panelHeight: 'auto', labelPosition: 'top', hasDownArrow: false, readonly: true" style="width: 100%"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_orderBy" name="orderBy" class="easyui-textbox" data-options="label: '排序', labelPosition: 'top', readonly: true" style="width: 100%"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_status" name="status" class="easyui-combobox"
               data-options="label: '状态', panelHeight: 'auto', labelPosition: 'top', hasDownArrow: false, readonly: true, valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/status'" style="width: 100%"/>
    </div>
    <div style="width:25%; float: left; padding: 0 20px">
        <input id="sys_role_read_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', readonly: true" style="width: 100%"/>
    </div>
</form>
<script type="text/javascript">
    // 加载数据(数据)
    function loadSysRoleReadForm(row) {
        $('#sys_role_read_form').form('load', row);
    }

    // 初始化表单
    function clearSysRoleReadForm() {
        $('#sys_role_read_form').form('clear');
    }
</script>
</body>
</html>
