<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_user_list" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'username', title: '用户名', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysUser(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
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
        <th data-options="field: 'createTime', title: '创建时间', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return formatDateTime(value);
            }"></th>
        <th data-options="field: 'createUser.nickname', title: '创建人', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return formatSysUser(row.createUser);
            }"></th>
    </tr>
    </thead>
</table>
<%-- 数据网格头部工具栏 --%>
<div id="sys_user_toolbar">
    <shiro:hasPermission name="sys_user:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增'" onclick="addSysUser()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_user:update">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-edit', plain: true,text: '修改'" onclick="editSysUser()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_user:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysUser()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_user:update">
        <span class="datagrid-btn-separator" style="float: none"></span>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-lock', plain: true, text: '重置密码'" onclick="resetSysUserPwd()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_user:create or sys_user:update or sys_user:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOutSysUser()"></a>
    <shiro:hasPermission name="sys_user:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-in', plain: true, text: '导入'" onclick="excelInSysUser()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="sys_user_dialog"></div>
<script type="text/javascript">
    // 表单提交后执行回调函数
    function sysUserCallback() {
        $('#sys_user_dialog').dialog('close');
        $('#sys_user_list').datagrid('reload');
    }

    // 新增
    function addSysUser() {
        $('#sys_user_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'sys/user/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '新增系统用户',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commitSysUserDetailForm('create', sysUserCallback);
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_user_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                clearSysUserDetailForm();
            }
        }).dialog('center');
    }

    // 修改
    function editSysUser() {
        let row = $('#sys_user_list').datagrid('getSelected');
        if (row) {
            $('#sys_user_dialog').css('padding', '10px').dialog({
                width: 1200,
                height: 600,
                href: 'sys/user/detail',
                iconCls: 'icon-save',
                modal: true,
                title: '修改系统用户',
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '保存',
                    width: 120,
                    handler: function () {
                        commitSysUserDetailForm('update', sysUserCallback);
                    }
                }, {
                    iconCls: 'icon-cancel',
                    text: '取消',
                    width: 120,
                    handler: function () {
                        $('#sys_user_dialog').dialog('close');
                    }
                }],
                onLoad: function () {
                    loadSysUserDetailForm(row);
                }
            }).dialog('center');
        }
    }

    // 删除
    function removeSysUser() {
        let rows = $('#sys_user_list').datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        $.messager.confirm('操作提示', '确认删除?', function (result) {
            if (!result) {
                return false;
            }
            $.messager.progress({
                title: '提示',
                msg: '删除中, 请稍候...',
                text: '',
                interval: 1000
            });
            let ids = [];
            for (let i = 0, len = rows.length; i < len; i++) {
                ids.push(rows[i].id)
            }
            $.post('sys/user/delete', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_user_list').datagrid('reload');
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        });
    }

    // 重置密码
    function resetSysUserPwd() {
        let rows = $('#sys_user_list').datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        $.messager.confirm('操作提示', '确认重置密码?', function (result) {
            if (!result) {
                return false;
            }
            $.messager.progress({
                title: '提示',
                msg: '重置密码中, 请稍候...',
                text: '',
                interval: 1000
            });
            let ids = [];
            for (let i = 0, len = rows.length; i < len; i++) {
                ids.push(rows[i].id)
            }
            $.post('sys/user/resetPwd', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_user_list').datagrid('reload');
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        });
    }

    // 数据列表
    $('#sys_user_list').datagrid({
        url: 'sys/user/list',
        toolbar: '#sys_user_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_user_list(data);
        }
    });

    function convert_sys_user_list(data) {
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

    // 详情
    function readSysUser(index) {
        let row = $('#sys_user_list').datagrid('getRows')[index];
        // $('#sys_user_dialog').css('padding', '10px').dialog({
        $('#index_default_dialog').css('padding', '10px').dialog({
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
                    // $('#sys_user_dialog').dialog('close');
                    $('#index_default_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysUserReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_user_list').datagrid('enableFilter', [{
        field: 'sex',
        type: 'combobox',
        options: {
            valueField: 'dictKey',
            textField: 'dictValue',
            url: 'sys/dict/combo/sex',
            panelHeight: 'auto',
            editable: false,
            icons: [{
                iconCls: 'icon-clear',
                handler: function (e) {
                    $(e.data.target).combobox('clear');
                }
            }],
            onChange: function (value) {
                if (value == '') {
                    $('#sys_user_list').datagrid('removeFilterRule', 'sex');
                } else {
                    $('#sys_user_list').datagrid('addFilterRule', {
                        field: 'sex',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_user_list').datagrid('doFilter');
            }
        }
    }, {
        field: 'status',
        type: 'combobox',
        options: {
            valueField: 'dictKey',
            textField: 'dictValue',
            url: 'sys/dict/combo/status',
            panelHeight: 'auto',
            editable: false,
            icons: [{
                iconCls: 'icon-clear',
                handler: function (e) {
                    $(e.data.target).combobox('clear');
                }
            }],
            onChange: function (value) {
                if (value == '') {
                    $('#sys_user_list').datagrid('removeFilterRule', 'status');
                } else {
                    $('#sys_user_list').datagrid('addFilterRule', {
                        field: 'status',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_user_list').datagrid('doFilter')
            }
        }
    }, {
        field: 'createTime',
        type: 'datebox',
        options: {
            editable: false,
            icons: [{
                iconCls: 'icon-clear',
                handler: function (e) {
                    $(e.data.target).datebox('clear');
                }
            }],
            onChange: function (value) {
                if (value == '') {
                    $('#sys_user_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_user_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_user_list').datagrid('doFilter');
            }
        }
    }]);

    // Excel导出
    function excelOutSysUser() {
        let filterRules = $('#sys_user_list').datagrid('options').filterRules;
        let url = 'sys/user/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sort = $('#sys_user_list').datagrid('options').sortName;
        if (sort != null) {
            let order = $('#sys_user_list').datagrid('options').sortOrder;
            url += '&sort=' + sort + '&order=' + order;
        }
        window.location.href = url;
    }

    // Excel导入
    function excelInSysUser() {
        $('#sys_user_dialog').css('padding', '10px').dialog({
            width: 400,
            height: 165,
            href: 'sys/user/import_excel',
            iconCls: 'icon-excel-in',
            modal: true,
            title: 'Excel批量导入',
            buttons: null
        }).dialog('center');
    }
</script>
</body>
</html>
