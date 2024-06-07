<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_table_column_list"
        <shiro:hasPermission name="sys_table:update">
            data-options="onClickRow:editSysTableColumn"
        </shiro:hasPermission>
       style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 75, sortable: true, hidden: true"></th>
        <th data-options="field: 'columnName', title: '字段名称', width: 75, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysTableColumn(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            },
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'displayName', title: '显示名称', width: 75, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'entityName', title: '实体类字段名', width: 75, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'columnType', title: '字段类型', width: 75, sortable: true,
            formatter: function (value, row, index) {
                return row.columnTypeText;
            },
            editor: {
                type: 'combobox',
                options: {
                    required: true,
                    panelHeight: 'auto',
                    editable: false,
                    valueField: 'dictKey',
                    textField: 'dictValue',
                    url: 'sys/dict/combo/column_type'
                }
            }"></th>
        <th data-options="field: 'columnLength', title: '字段长度', width: 75, sortable: true,
            editor: {
                type: 'textbox'
            }"></th>
        <th data-options="field: 'isPrimary', title: '是否主键', width: 50, sortable: true,
            formatter: function (value, row, index) {
                return row.isPrimaryText;
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
        <th data-options="field: 'isNullable', title: '可否空值', width: 50, sortable: true,
            formatter: function (value, row, index) {
                return row.isNullableText;
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
        <th data-options="field: 'isUnique', title: '是否唯一', width: 50, sortable: true,
            formatter: function (value, row, index) {
                return row.isUniqueText;
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
        <th data-options="field: 'isInsertable', title: '可插入', width: 50, sortable: true,
            formatter: function (value, row, index) {
                return row.isInsertableText;
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
        <th data-options="field: 'isUpdatable', title: '可修改', width: 50, sortable: true,
            formatter: function (value, row, index) {
                return row.isUpdatableText;
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
        <th data-options="field: 'orderBy', title: '排序', width: 50, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'foreignTableId', title: '外键表', width: 75, sortable: true,
            formatter: function (value, row, index) {
                return row.foreignTableIdText;
            },
            editor: {
                type: 'combogrid',
                options: {
                    panelWidth: 450,
                    editable: false,
                    icons: [{
                        iconCls: 'icon-clear',
                        handler: function (e) {
                            $(e.data.target).combogrid('clear');
                        }
                    }],
                    idField: 'id',
                    textField: 'tableDesc',
                    url: 'sys/table/grid',
                    columns: [[
                        {field: 'tableDesc', title: '表描述', width: 100},
                        {field: 'tableName', title: '表名', width: 100}
                    ]],
                    fitColumns: true
                }
            }"></th>
        <th data-options="field: 'dictCode', title: '数据字典', width: 75, sortable: true,
            formatter: function (value, row, index) {
                return row.dictCodeText;
            },
            editor: {
                type: 'combogrid',
                options: {
                    panelWidth: 450,
                    editable: false,
                    icons: [{
                        iconCls: 'icon-clear',
                        handler: function (e) {
                            $(e.data.target).combogrid('clear');
                        }
                    }],
                    idField: 'dictKey',
                    textField: 'dictValue',
                    url: 'sys/dict/grid',
                    columns: [[
                        {field: 'dictCode', title: '字典编码', width: 100},
                        {field: 'dictValue', title: '字典描述', width: 100}
                    ]],
                    fitColumns: true
                }
            }"></th>
        <th data-options="field: 'description', title: '描述', width: 100, sortable: true,
            editor: {
                type: 'textbox',
            }"></th>
    </tr>
    </thead>
</table>
<%-- 数据网格的头部工具栏 --%>
<shiro:hasPermission name="sys_table:update">
    <div id="sys_table_column_toolbar">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-save', plain: true, text: '保存'" onclick="saveSysTableColumn()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-undo', plain: true, text: '撤销'" onclick="undoSysTableColumn()"></a>
        <span class="datagrid-btn-separator" style="float: none"></span>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增一行'" onclick="addSysTableColumn()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysTableColumn()"></a>
    </div>
</shiro:hasPermission>
<%-- 弹出框 --%>
<div id="sys_table_column_dialog"></div>
<script type="text/javascript">
    var editSysTableColumnId = null;

    // 数据列表
    $('#sys_table_column_list').datagrid({
        url: 'sys/table/column/list?tid=' + ${tableId},
        pageSize: 50,
        toolbar: '#sys_table_column_toolbar',
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        sortName: 'orderBy',
        sortOrder: 'asc',
        loadFilter: function (data) {
            return convert_sys_table_column_list(data);
        }
    });

    function convert_sys_table_column_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isPrimary) {
                    row.isPrimaryText = item.dictValue;
                }
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isNullable) {
                    row.isNullableText = item.dictValue;
                }
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isUnique) {
                    row.isUniqueText = item.dictValue;
                }
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isInsertable) {
                    row.isInsertableText = item.dictValue;
                }
                if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isUpdatable) {
                    row.isUpdatableText = item.dictValue;
                }
                if (item.dictCode == 'COLUMN_TYPE' && item.dictKey == row.columnType) {
                    row.columnTypeText = item.dictValue;
                }
                if (item.dictCode == 'SysTable' && item.dictKey == row.foreignTableId) {
                    row.foreignTableIdText = item.dictValue;
                }
                if (item.dictCode == row.dictCode && item.dictKey == row.dictCode) {
                    row.dictCodeText = item.dictValue;
                }
            });
        });
        return data;
    }

    function readSysTableColumn(index) {
        let row = $('#sys_table_column_list').datagrid('getRows')[index];
        $('#sys_table_column_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'sys/table/column_read',
            iconCls: 'icon-save',
            modal: true,
            title: '表单字段',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_table_column_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysTableColumnReadForm(row);
            }
        }).dialog('center');
    }

    function endEditSysTableColumn() {
        if (editSysTableColumnId == null) {
            return true
        }
        let dataGrid = $('#sys_table_column_list');
        if (dataGrid.datagrid('validateRow', editSysTableColumnId)) {
            let isPrimary = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'isPrimary'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['isPrimaryText'] = $(isPrimary.target).combobox('getText');
            let isNullable = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'isNullable'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['isNullableText'] = $(isNullable.target).combobox('getText');
            let isUnique = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'isUnique'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['isUniqueText'] = $(isUnique.target).combobox('getText');
            let isInsertable = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'isInsertable'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['isInsertableText'] = $(isInsertable.target).combobox('getText');
            let isUpdatable = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'isUpdatable'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['isUpdatableText'] = $(isUpdatable.target).combobox('getText');
            let columnType = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'columnType'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['columnTypeText'] = $(columnType.target).combobox('getText');
            let foreignTableId = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'foreignTableId'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['foreignTableIdText'] = $(foreignTableId.target).combogrid('getText');
            let dictCode = dataGrid.datagrid('getEditor', {index: editSysTableColumnId, field: 'dictCode'});
            dataGrid.datagrid('getRows')[editSysTableColumnId]['dictCodeText'] = $(dictCode.target).combogrid('getText');
            dataGrid.datagrid('endEdit', editSysTableColumnId);
            editSysTableColumnId = null;
            return true;
        } else {
            return false;
        }
    }

    function editSysTableColumn(index) {
        if (editSysTableColumnId == index) {
            return false;
        }
        if (endEditSysTableColumn()) {
            editSysTableColumnId = index;
            $('#sys_table_column_list').datagrid('beginEdit', index);
        }
    }

    function addSysTableColumn() {
        if (endEditSysTableColumn()) {
            let dataGrid = $('#sys_table_column_list');
            let data = dataGrid.datagrid('getData');
            let tableId = ${tableId};
            let orderBy = 1;
            for (let i = 0; i < data.rows.length; i++) {
                let row = data.rows[i];
                if (row.tableId == tableId) {
                    orderBy += 1;
                }
            }
            orderBy = orderBy.toString();
            while (orderBy.length < 3) {
                orderBy = '0' + orderBy;
            }
            dataGrid.datagrid('insertRow', {
                index: 0,
                row: {
                    tableId: tableId,
                    orderBy: orderBy,
                    isPrimary: '0',
                    isNullable: '0',
                    isUnique: '0',
                    isInsertable: '1',
                    isUpdatable: '1'
                }
            });
            editSysTableColumnId = 0;
            dataGrid.datagrid('selectRow', editSysTableColumnId).datagrid('beginEdit', editSysTableColumnId);
        }
    }

    function removeSysTableColumn() {
        let dataGrid = $('#sys_table_column_list');
        let rows = dataGrid.datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        for (let i = 0, len = rows.length; i < len; i++) {
            let index = dataGrid.datagrid('getRowIndex', rows[i]);
            dataGrid.datagrid('deleteRow', index);
            if (editSysTableColumnId == index) {
                editSysTableColumnId = null;
            }
            if (editSysTableColumnId > index) {
                editSysTableColumnId -= 1;
            }
        }
    }

    function undoSysTableColumn() {
        $('#sys_table_column_list').datagrid('rejectChanges');
        editSysTableColumnId = null;
    }

    function saveSysTableColumn() {
        if (endEditSysTableColumn()) {
            let obj = {};
            obj['inserted'] = $('#sys_table_column_list').datagrid('getChanges', 'inserted');
            obj['updated'] = $('#sys_table_column_list').datagrid('getChanges', 'updated');
            obj['deleted'] = $('#sys_table_column_list').datagrid('getChanges', 'deleted');
            $.messager.progress({
                title: '提示',
                msg: '保存中, 请稍候...',
                text: '',
                interval: 1000
            });
            $.ajax({
                type: 'post',
                url: 'sys/table/column/save',
                contentType: 'application/json',
                data: JSON.stringify(obj),
                success: function (result) {
                    $.messager.progress('close');
                    if (result.code == 0) {
                        $('#sys_table_column_list').datagrid('acceptChanges').datagrid('reload');
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
</script>
</body>
</html>
