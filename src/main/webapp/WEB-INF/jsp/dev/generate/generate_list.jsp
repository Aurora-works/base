<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_generate_list"
        <shiro:hasPermission name="sys_generate:update">
            data-options="onClickRow:editSysGenerate"
        </shiro:hasPermission>
       style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'menuId', title: '菜单', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysGenerate(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + row.menuIdText + '</span>';
            },
            editor: {
                type: 'combotree',
                options: {
                    required: true,
                    editable: false,
                    panelHeight: 'auto',
                    url: 'sys/menu/tree/combo'
                }
            }"></th>
        <th data-options="field: 'tableId', title: '表单', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.tableIdText;
            },
            editor: {
                type: 'combogrid',
                options: {
                    required: true,
                    panelWidth: 450,
                    editable: false,
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
        <th data-options="field: 'description', title: '描述', width: 100, sortable: true,
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
<div id="sys_generate_toolbar">
    <shiro:hasPermission name="sys_generate:create or sys_generate:update or sys_generate:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-save', plain: true, text: '保存'" onclick="saveSysGenerate()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-undo', plain: true, text: '撤销'" onclick="undoSysGenerate()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_generate:create or sys_generate:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_generate:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增一行'" onclick="addSysGenerate()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_generate:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysGenerate()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_generate:create or sys_generate:update or sys_generate:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-lock', plain: true, text: '生成代码'" onclick="generateCode()"></a>
    <span class="datagrid-btn-separator" style="float: none"></span>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOutSysGenerate()"></a>
    <shiro:hasPermission name="sys_generate:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-in', plain: true, text: '导入'" onclick="excelInSysGenerate()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="sys_generate_dialog"></div>
<script type="text/javascript">
    var editSysGenerateId = null;

    function sysGenerateCallback() {
        $('#sys_generate_dialog').dialog('close');
        $('#sys_generate_list').datagrid('reload');
    }

    // 数据列表
    $('#sys_generate_list').datagrid({
        url: 'sys/generate/list',
        toolbar: '#sys_generate_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_generate_list(data);
        }
    });

    function convert_sys_generate_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                if (item.dictCode == 'SysTable' && item.dictKey == row.tableId) {
                    row.tableIdText = item.dictValue;
                }
                if (item.dictCode == 'SysMenu' && item.dictKey == row.menuId) {
                    row.menuIdText = item.dictValue;
                }
            });
        });
        return data;
    }

    function endEditSysGenerate() {
        if (editSysGenerateId == null) {
            return true
        }
        let dataGrid = $('#sys_generate_list');
        if (dataGrid.datagrid('validateRow', editSysGenerateId)) {
            let tableId = dataGrid.datagrid('getEditor', {index: editSysGenerateId, field: 'tableId'});
            dataGrid.datagrid('getRows')[editSysGenerateId]['tableIdText'] = $(tableId.target).combogrid('getText');
            let menuId = dataGrid.datagrid('getEditor', {index: editSysGenerateId, field: 'menuId'});
            dataGrid.datagrid('getRows')[editSysGenerateId]['menuIdText'] = $(menuId.target).combotree('getText');
            dataGrid.datagrid('endEdit', editSysGenerateId);
            editSysGenerateId = null;
            return true;
        } else {
            return false;
        }
    }

    function editSysGenerate(index) {
        if (editSysGenerateId == index) {
            return false;
        }
        if (endEditSysGenerate()) {
            editSysGenerateId = index;
            $('#sys_generate_list').datagrid('beginEdit', index);
        }
    }

    function addSysGenerate() {
        if (endEditSysGenerate()) {
            let dataGrid = $('#sys_generate_list');
            dataGrid.datagrid('insertRow', {
                index: 0,
                row: {}
            });
            editSysGenerateId = 0;
            dataGrid.datagrid('selectRow', editSysGenerateId).datagrid('beginEdit', editSysGenerateId);
        }
    }

    function removeSysGenerate() {
        let dataGrid = $('#sys_generate_list');
        let rows = dataGrid.datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        for (let i = 0, len = rows.length; i < len; i++) {
            let index = dataGrid.datagrid('getRowIndex', rows[i]);
            dataGrid.datagrid('deleteRow', index);
            if (editSysGenerateId == index) {
                editSysGenerateId = null;
            }
            if (editSysGenerateId > index) {
                editSysGenerateId -= 1;
            }
        }
    }

    function undoSysGenerate() {
        $('#sys_generate_list').datagrid('rejectChanges');
        editSysGenerateId = null;
    }

    function saveSysGenerate() {
        if (endEditSysGenerate()) {
            let obj = {};
            obj['inserted'] = $('#sys_generate_list').datagrid('getChanges', 'inserted');
            obj['updated'] = $('#sys_generate_list').datagrid('getChanges', 'updated');
            obj['deleted'] = $('#sys_generate_list').datagrid('getChanges', 'deleted');
            $.messager.progress({
                title: '提示',
                msg: '保存中, 请稍候...',
                text: '',
                interval: 1000
            });
            $.ajax({
                type: 'post',
                url: 'sys/generate/save',
                contentType: 'application/json',
                data: JSON.stringify(obj),
                success: function (result) {
                    $.messager.progress('close');
                    if (result.code == 0) {
                        $('#sys_generate_list').datagrid('acceptChanges').datagrid('reload');
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

    function readSysGenerate(index) {
        let row = $('#sys_generate_list').datagrid('getRows')[index];
        $('#sys_generate_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'dev/generate/read',
            iconCls: 'icon-save',
            modal: true,
            title: '生成代码',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_generate_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysGenerateReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_generate_list').datagrid('enableFilter', [{
        field: 'menuId',
        type: 'combotree',
        options: {
            editable: false,
            icons: [{
                iconCls: 'icon-clear',
                handler: function (e) {
                    $(e.data.target).combotree('clear');
                }
            }],
            url: 'sys/menu/tree/combo',
            panelHeight: 'auto',
            onChange: function (value) {
                if (value == '') {
                    $('#sys_generate_list').datagrid('removeFilterRule', 'menuId');
                } else {
                    $('#sys_generate_list').datagrid('addFilterRule', {
                        field: 'menuId',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_generate_list').datagrid('doFilter');
            }
        }
    }, {
        field: 'tableId',
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
            fitColumns: true,
            onChange: function (value) {
                if (value == '') {
                    $('#sys_generate_list').datagrid('removeFilterRule', 'tableId');
                } else {
                    $('#sys_generate_list').datagrid('addFilterRule', {
                        field: 'tableId',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_generate_list').datagrid('doFilter');
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
                    $('#sys_generate_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_generate_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_generate_list').datagrid('doFilter');
            }
        }
    }]);

    // Excel导出
    function excelOutSysGenerate() {
        let filterRules = $('#sys_generate_list').datagrid('options').filterRules;
        let url = 'sys/generate/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sortName = $('#sys_generate_list').datagrid('options').sortName;
        if (sortName != null) {
            let sortOrder = $('#sys_generate_list').datagrid('options').sortOrder;
            url += '&sort=' + sortName + '&order=' + sortOrder;
        }
        window.location.href = url;
    }

    // Excel导入
    function excelInSysGenerate() {
        $('#sys_generate_dialog').css('padding', '10px').dialog({
            width: 400,
            height: 165,
            href: 'sys/generate/import_excel',
            iconCls: 'icon-excel-in',
            modal: true,
            title: 'Excel批量导入',
            buttons: null
        }).dialog('center');
    }

    function generateCode() {
        let row = $('#sys_generate_list').datagrid('getSelected');
        if (row && row.id) {
            $.messager.progress({
                title: '提示',
                msg: '正在生成代码, 请稍后...',
                text: '',
                interval: 1000
            });
            $.post('sys/generate/code', {id: row.id}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $.messager.alert({
                        title: '提示',
                        msg: "生成代码成功"
                    });
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        }
    }
</script>
</body>
</html>
