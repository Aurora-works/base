<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_menu_read_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="text-align: center; display: none;">
            <input id="sys_menu_read_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_menuCode" name="menuCode" class="easyui-textbox" data-options="label: '菜单编码', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_menuName" name="menuName" class="easyui-textbox" data-options="label: '菜单名称', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_href" name="href" class="easyui-textbox" data-options="label: '菜单链接', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_parentId" name="parentId" class="easyui-combotree" data-options="url: 'sys/menu/tree/combo', label: '父菜单', labelPosition: 'top', hasDownArrow: false, readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_orderBy" name="orderBy" class="easyui-textbox" data-options="label: '排序', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_css" name="css" class="easyui-textbox" data-options="label: '菜单图标', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_isOpen" name="isOpen" class="easyui-combobox"
                   data-options="label: '默认展开', labelPosition: 'top', valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/yes_or_no', hasDownArrow: false, readonly: true"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_status" name="status" class="easyui-combobox"
                   data-options="label: '状态', labelPosition: 'top', valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/status', hasDownArrow: false, readonly: true"
                   style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<form id="sys_menu_read_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_menu_read_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 加载数据(数据)
    function loadSysMenuReadForm(row) {
        $('#sys_menu_read_form').form('load', row);
        $('#sys_menu_read_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
    }

    // 加载数据(id)
    function loadSysMenuReadFormById(id) {
        $('#sys_menu_read_form').form({
            onLoadSuccess: function (row) {
                $('#sys_menu_read_form_2').form('load', {
                    'createTime': row.createTime ? formatDateTime(row.createTime) : '',
                    'createUser': row.createUser ? formatSysUser(row.createUser) : '',
                    'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
                    'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
                });
            }
        });
        $('#sys_menu_read_form').form('load', 'sys/menu/find?id=' + id);
    }
</script>
</body>
</html>
