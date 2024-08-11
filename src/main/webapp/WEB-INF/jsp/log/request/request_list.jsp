<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_request_log_list" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'requestController', title: '请求方法', width: 300, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysRequestLog(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            }"></th>
        <th data-options="field: 'requestMethod', title: '请求方式', width: 50, sortable: true"></th>
        <th data-options="field: 'requestUrl', title: 'URL', width: 200, sortable: true"></th>
        <th data-options="field: 'requestIp', title: 'IP', width: 100, sortable: true"></th>
        <th data-options="field: 'requestParameters', title: '参数列表', width: 100, sortable: true"></th>
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
<div id="sys_request_log_toolbar">
    <shiro:hasPermission name="sys_request:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysRequestLog()"></a>
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOutSysRequestLog()"></a>
</div>
<%-- 弹出框 --%>
<div id="sys_request_log_dialog"></div>
<script type="text/javascript">
    // 删除按钮
    function removeSysRequestLog() {
        let rows = $('#sys_request_log_list').datagrid('getSelections');
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
            $.post('sys/request/delete', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_request_log_list').datagrid('reload');
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
    $('#sys_request_log_list').datagrid({
        url: 'sys/request/list',
        toolbar: '#sys_request_log_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        sortName: 'createTime',
        sortOrder: 'desc'
    });

    function readSysRequestLog(index) {
        let row = $('#sys_request_log_list').datagrid('getRows')[index];
        $('#sys_request_log_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'log/request/read',
            iconCls: 'icon-save',
            modal: true,
            title: '操作日志',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_request_log_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysRequestLogReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_request_log_list').datagrid('enableFilter', [{
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
                    $('#sys_request_log_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_request_log_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_request_log_list').datagrid('doFilter');
            }
        }
    }]);

    // Excel导出
    function excelOutSysRequestLog() {
        let filterRules = $('#sys_request_log_list').datagrid('options').filterRules;
        let url = 'sys/request/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sort = $('#sys_request_log_list').datagrid('options').sortName;
        if (sort != null) {
            let order = $('#sys_request_log_list').datagrid('options').sortOrder;
            url += '&sort=' + sort + '&order=' + order;
        }
        window.location.href = url;
    }
</script>
</body>
</html>
