<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_param_list" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'paramCode', title: '参数编码', width: 125, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysParam(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            }"></th>
        <th data-options="field: 'paramDesc', title: '参数名称', width: 100, sortable: true"></th>
        <th data-options="field: 'paramValue', title: '参数值', width: 200, sortable: true"></th>
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
<%-- 数据网格的头部工具栏 --%>
<div id="sys_param_toolbar">
    <shiro:hasPermission name="sys_param:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增'" onclick="addSysParam()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_param:update">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-edit', plain: true, text: '修改'" onclick="editSysParam()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_param:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysParam()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_param:create or sys_param:update or sys_param:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOutSysParam()"></a>
    <shiro:hasPermission name="sys_param:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-in', plain: true, text: '导入'" onclick="excelInSysParam()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="sys_param_dialog"></div>
<script type="text/javascript">
    // 表单提交后执行回调函数
    function sysParamCallback() {
        $('#sys_param_dialog').dialog('close');
        $('#sys_param_list').datagrid('reload');
    }

    // 新增按钮
    function addSysParam() {
        $('#sys_param_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'dev/param/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '新增系统参数',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commitSysParamDetailForm('create', sysParamCallback);
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_param_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                clearSysParamDetailForm();
            }
        }).dialog('center');
    }

    // 修改按钮
    function editSysParam() {
        let row = $('#sys_param_list').datagrid('getSelected');
        if (row) {
            $('#sys_param_dialog').css('height', '600px').css('width', '1200px').css('padding', '10px').dialog({
                href: 'dev/param/detail',
                iconCls: 'icon-save',
                modal: true,
                title: '修改系统参数',
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '保存',
                    width: 120,
                    handler: function () {
                        commitSysParamDetailForm('update', sysParamCallback);
                    }
                }, {
                    iconCls: 'icon-cancel',
                    text: '取消',
                    width: 120,
                    handler: function () {
                        $('#sys_param_dialog').dialog('close');
                    }
                }],
                onLoad: function () {
                    loadSysParamDetailForm(row);
                }
            }).dialog('center');
        }
    }

    // 删除按钮
    function removeSysParam() {
        let rows = $('#sys_param_list').datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        $.messager.confirm('操作提示', '确认删除?', function (result) {
            if (!result) {
                return false;
            }
            $.messager.progress({
                title: '提示',
                msg: '删除中, 请稍后...',
                text: '',
                interval: 1000
            });
            let ids = [];
            for (let i = 0, len = rows.length; i < len; i++) {
                ids.push(rows[i].id)
            }
            $.post('sys/param/delete', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_param_list').datagrid('reload');
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
    $('#sys_param_list').datagrid({
        url: 'sys/param/list',
        toolbar: '#sys_param_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true
    });

    function readSysParam(index) {
        let row = $('#sys_param_list').datagrid('getRows')[index];
        $('#sys_param_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'dev/param/read',
            iconCls: 'icon-save',
            modal: true,
            title: '系统参数',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_param_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysParamReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_param_list').datagrid('enableFilter', [{
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
                    $('#sys_param_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_param_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_param_list').datagrid('doFilter');
            }
        }
    }]);

    // Excel导出
    function excelOutSysParam() {
        let filterRules = $('#sys_param_list').datagrid('options').filterRules;
        let url = 'sys/param/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sort = $('#sys_param_list').datagrid('options').sortName;
        if (sort != null) {
            let order = $('#sys_param_list').datagrid('options').sortOrder;
            url += '&sort=' + sort + '&order=' + order;
        }
        window.location.href = url;
    }

    // Excel导入
    function excelInSysParam() {
        $('#sys_param_dialog').css('padding', '10px').dialog({
            width: 400,
            height: 165,
            href: 'dev/param/import_excel',
            iconCls: 'icon-excel-in',
            modal: true,
            title: 'Excel批量导入',
            buttons: null
        }).dialog('center');
    }
</script>
</body>
</html>
