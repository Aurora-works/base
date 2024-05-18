<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_role_user_list" class="easyui-datagrid" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'username', title: '用户名', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysRoleUser(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            }"></th>
        <th data-options="field: 'nickname', title: '姓名', width: 100, sortable: true"></th>
        <th data-options="field: 'sex', title: '性别', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.sexText;
            }"></th>
        <th data-options="field: 'email', title: '电子邮件', width: 100, sortable: true"></th>
        <th data-options="field: 'mobilePhoneNumber', title: '手机号码', width: 100, sortable: true"></th>
        <th data-options="field: 'status', title: '状态', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.statusText;
            }"></th>
        <th data-options="field: 'description', title: '描述', width: 150, sortable: true"></th>
    </tr>
    </thead>
</table>
<%-- 数据网格的头部工具栏 --%>
<shiro:hasPermission name="sys_role:update">
    <div id="sys_role_user_toolbar">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '添加关联'" onclick="addSysRoleUser()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '解除关联'" onclick="removeSysRoleUser()"></a>
    </div>
</shiro:hasPermission>
<%-- 弹出框 --%>
<div id="sys_role_user_dialog"></div>
<script type="text/javascript">
    // 角色关联用户列表
    $('#sys_role_user_list').datagrid({
        toolbar: '#sys_role_user_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_role_user_list(data);
        }
    });

    function readSysRoleUser(index) {
        let row = $('#sys_role_user_list').datagrid('getRows')[index];
        $('#sys_role_user_dialog').css('padding', '10px').dialog({
            width: 1200,
            height: 600,
            href: 'sys/user/read',
            iconCls: 'icon-save',
            modal: true,
            title: '系统用户',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_role_user_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysUserReadForm(row);
            }
        }).dialog('center');
    }

    // 格式化列表
    function convert_sys_role_user_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                if (item.dictCode == 'SEX' && item.dictKey == row.sex) {
                    row.sexText = item.dictValue;
                }
                if (item.dictCode == 'STATUS' && item.dictKey == row.status) {
                    row.statusText = item.dictValue;
                }
            });
        });
        return data;
    }

    // 关联列表的回调函数
    function sysRoleUserCallback(userIds) {
        $.messager.progress({
            title: '提示',
            msg: '保存中, 请稍候...',
            text: '',
            interval: 1000
        });
        let role = $('#sys_role_tree').tree('getSelected');
        $.post('sys/role/user/create', {roleId: role.id, userIds: userIds}, function (result) {
            $.messager.progress('close');
            if (result.code == 0) {
                $('#sys_role_user_dialog').dialog('close');
                loadSysRoleTreeAndSelectByRoleCode(role.roleCode);
            } else {
                $.messager.alert({
                    title: '提示',
                    msg: result.message
                });
            }
        });
    }

    // 添加关联按钮
    function addSysRoleUser() {
        if (!$('#sys_role_tree').tree('getSelected')) {
            return false;
        }
        $('#sys_role_user_dialog').css('padding', '0').dialog({
            width: 1100,
            height: 550,
            href: 'sys/user/win',
            iconCls: 'icon-save',
            modal: true,
            title: '系统用户',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    executeCallback('sysRoleUserCallback');
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_role_user_dialog').dialog('close');
                }
            }]
        }).dialog('center');
    }

    // 解除关联按钮
    function removeSysRoleUser() {
        let rows = $('#sys_role_user_list').datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        $.messager.confirm('操作提示', '确认解除关联用户?', function (result) {
            if (!result) {
                return false;
            }
            $.messager.progress({
                title: '提示',
                msg: '解除中, 请稍后...',
                text: '',
                interval: 1000
            });
            let userIds = [];
            for (let i = 0; i < rows.length; i++) {
                userIds.push(rows[i].id);
            }
            let role = $('#sys_role_tree').tree('getSelected');
            $.post('sys/role/user/delete', {roleId: role.id, userIds: userIds}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    loadSysRoleTreeAndSelectByRoleCode(role.roleCode);
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        });
    }
</script>
</body>
</html>
