<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_table_list"
        <shiro:hasPermission name="sys_table:update">
            data-options="onClickRow:editSysTable"
        </shiro:hasPermission>
       style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'tableDesc', title: '表描述', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysTable(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            },
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'tableName', title: '表名', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'entityName', title: '实体类名', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'isReal', title: '是否实体表', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.isRealText;
            },
            editor: {
                type: 'combobox',
                options: {
                    required: true,
                    panelHeight: 'auto',
                    editable: false,
                    valueField: 'dictKey',
                    textField: 'dictValue',
                    url: 'sys/dict/combo/yes_or_no'
                }
            }"></th>
        <th data-options="field: 'description', title: '描述', width: 150, sortable: true,
            editor: {
                type: 'textbox'
            }"></th>
        <th data-options="field: 'createTime', title: '创建时间', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.id == null) {
                    return '';
                }
                return formatDateTime(value);
            }"></th>
        <th data-options="field: 'createUser.nickname', title: '创建人', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.id == null) {
                    return '';
                }
                return formatSysUser(row.createUser);
            }"></th>
    </tr>
    </thead>
</table>
<%-- 数据网格的头部工具栏 --%>
<div id="sys_table_toolbar">
    <shiro:hasPermission name="sys_table:create or sys_table:update or sys_table:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-save', plain: true, text: '保存'" onclick="saveSysTable()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-undo', plain: true, text: '撤销'" onclick="undoSysTable()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_table:create or sys_table:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_table:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增一行'" onclick="addSysTable()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_table:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysTable()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_table:create or sys_table:update or sys_table:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-lock', plain: true, text: '查看表单字段'" onclick="editSysTableColumns()"></a>
</div>
<%-- 弹出框 --%>
<div id="sys_table_dialog"></div>
<script type="text/javascript">
    var editSysTableId = null;

    // 数据列表
    $('#sys_table_list').datagrid({
        url: 'sys/table/list',
        toolbar: '#sys_table_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_table_list(data);
        }
    });

    function convert_sys_table_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isReal) {
                    row.isRealText = item.dictValue;
                }
            });
        });
        return data;
    }

    function endEditSysTable() {
        if (editSysTableId == null) {
            return true
        }
        let dataGrid = $('#sys_table_list');
        if (dataGrid.datagrid('validateRow', editSysTableId)) {
            let isReal = dataGrid.datagrid('getEditor', {index: editSysTableId, field: 'isReal'});
            dataGrid.datagrid('getRows')[editSysTableId]['isRealText'] = $(isReal.target).combobox('getText');
            dataGrid.datagrid('endEdit', editSysTableId);
            editSysTableId = null;
            return true;
        } else {
            return false;
        }
    }

    function editSysTable(index) {
        if (editSysTableId == index) {
            return false;
        }
        if (endEditSysTable()) {
            editSysTableId = index;
            $('#sys_table_list').datagrid('beginEdit', index);
        }
    }

    function addSysTable() {
        if (endEditSysTable()) {
            let dataGrid = $('#sys_table_list');
            dataGrid.datagrid('insertRow', {
                index: 0,
                row: {
                    isReal: '1'
                }
            });
            editSysTableId = 0;
            dataGrid.datagrid('selectRow', editSysTableId).datagrid('beginEdit', editSysTableId);
        }
    }

    function removeSysTable() {
        let dataGrid = $('#sys_table_list');
        let rows = dataGrid.datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        for (let i = 0, len = rows.length; i < len; i++) {
            let index = dataGrid.datagrid('getRowIndex', rows[i]);
            dataGrid.datagrid('deleteRow', index);
            if (editSysTableId == index) {
                editSysTableId = null;
            }
            if (editSysTableId > index) {
                editSysTableId -= 1;
            }
        }
    }

    function undoSysTable() {
        $('#sys_table_list').datagrid('rejectChanges');
        editSysTableId = null;
    }

    function saveSysTable() {
        if (endEditSysTable()) {
            let obj = {};
            obj['inserted'] = $('#sys_table_list').datagrid('getChanges', 'inserted');
            obj['updated'] = $('#sys_table_list').datagrid('getChanges', 'updated');
            obj['deleted'] = $('#sys_table_list').datagrid('getChanges', 'deleted');
            $.messager.progress({
                title: '提示',
                msg: '保存中, 请稍候...',
                text: '',
                interval: 1000
            });
            $.ajax({
                type: 'post',
                url: 'sys/table/save',
                contentType: 'application/json',
                data: JSON.stringify(obj),
                success: function (result) {
                    $.messager.progress('close');
                    if (result.code == 0) {
                        $('#sys_table_list').datagrid('acceptChanges').datagrid('reload');
                    } else {
                        $.messager.alert({
                            title: '提示',
                            msg: result.message
                        });
                    }
                }
            });
        }
    }

    function readSysTable(index) {
        let row = $('#sys_table_list').datagrid('getRows')[index];
        $('#sys_table_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'dev/table/read',
            iconCls: 'icon-save',
            modal: true,
            title: '系统表单',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_table_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysTableReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_table_list').datagrid('enableFilter', [{
        field: 'isReal',
        type: 'combobox',
        options: {
            valueField: 'dictKey',
            textField: 'dictValue',
            url: 'sys/dict/combo/yes_or_no',
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
                    $('#sys_table_list').datagrid('removeFilterRule', 'isReal');
                } else {
                    $('#sys_table_list').datagrid('addFilterRule', {
                        field: 'isReal',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_table_list').datagrid('doFilter');
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
                    $('#sys_table_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_table_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_table_list').datagrid('doFilter');
            }
        }
    }]);

    // 查看/修改表单字段
    function editSysTableColumns() {
        let row = $('#sys_table_list').datagrid('getSelected');
        if (row && row.id) {
            $('#sys_table_dialog').css('padding', '0').dialog({
                height: 700,
                width: 1400,
                href: 'sys/table/column/list?tid=' + row.id,
                iconCls: 'icon-save',
                modal: true,
                title: row.tableDesc,
                buttons: [{
                    iconCls: 'icon-cancel',
                    text: '取消',
                    width: 120,
                    handler: function () {
                        $('#sys_table_dialog').dialog('close');
                    }
                }]
            }).dialog('center');
        }
    }
</script>
</body>
</html>
