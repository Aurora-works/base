<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_user_win" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'username', title: '用户名', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysUserWin(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
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
<%-- 弹出框 --%>
<div id="sys_user_win_dialog"></div>
<script type="text/javascript">
    // 数据列表
    $('#sys_user_win').datagrid({
        url: 'sys/user/win',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_user_win(data);
        }
    });

    function executeCallback(callback) {
        let rows = $('#sys_user_win').datagrid('getSelections');
        let userIds = [];
        for (let i = 0; i < rows.length; i++) {
            userIds.push(rows[i].id);
        }
        eval(callback + '([' + userIds + ']);');
    }

    function convert_sys_user_win(data) {
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

    // 启用数据网格行过滤
    $('#sys_user_win').datagrid('enableFilter', [{
        field: 'sex',
        type: 'combobox',
        options: {
            valueField: 'dictKey',
            textField: 'dictValue',
            url: 'sys/dict/combo/sex',
            panelHeight: 'auto',
            editable: false,
            onChange: function (value) {
                if (value == '') {
                    $('#sys_user_win').datagrid('removeFilterRule', 'sex');
                } else {
                    $('#sys_user_win').datagrid('addFilterRule', {
                        field: 'sex',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_user_win').datagrid('doFilter');
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
            onChange: function (value) {
                if (value == '') {
                    $('#sys_user_win').datagrid('removeFilterRule', 'status');
                } else {
                    $('#sys_user_win').datagrid('addFilterRule', {
                        field: 'status',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_user_win').datagrid('doFilter');
            }
        }
    }]);

    function readSysUserWin(index) {
        let row = $('#sys_user_win').datagrid('getRows')[index];
        $('#sys_user_win_dialog').css('padding', '10px').dialog({
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
                    $('#sys_user_win_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysUserReadForm(row);
            }
        }).dialog('center');
    }
</script>
</body>
</html>
