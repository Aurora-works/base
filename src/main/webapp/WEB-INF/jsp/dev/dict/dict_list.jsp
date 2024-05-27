<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_dict_list"
        <shiro:hasPermission name="sys_dict:update">
            data-options="onClickRow:editSysDict"
        </shiro:hasPermission>
       style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'dictCode', title: '字典编码', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysDict(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            },
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'dictKey', title: '字典键', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'dictValue', title: '字典值', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'orderBy', title: '排序', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
            }"></th>
        <th data-options="field: 'status', title: '状态', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.statusText;
            },
            editor: {
                type: 'combobox',
                options: {
                    required: true,
                    panelHeight: 'auto',
                    editable: false,
                    valueField: 'dictKey',
                    textField: 'dictValue',
                    url: 'sys/dict/combo/status'
                }
            }"></th>
        <th data-options="field: 'description', title: '描述', width: 100, sortable: true,
            editor: {
                type: 'textbox',
                options: {
                    required: true
                }
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
<div id="sys_dict_toolbar">
    <shiro:hasPermission name="sys_dict:create or sys_dict:update or sys_dict:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-save', plain: true, text: '保存'" onclick="saveSysDict()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-undo', plain: true, text: '撤销'" onclick="undoSysDict()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_dict:create or sys_dict:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_dict:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增一行'" onclick="addSysDict()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_dict:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysDict()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_dict:create or sys_dict:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOutSysDict()"></a>
    <shiro:hasPermission name="sys_dict:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-in', plain: true, text: '导入'" onclick="excelInSysDict()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="sys_dict_dialog"></div>
<script type="text/javascript">
    var editSysDictId = null;

    // 表单提交后执行回调函数
    function sysDictCallback() {
        $('#sys_dict_dialog').dialog('close');
        $('#sys_dict_list').datagrid('reload');
    }

    // 数据列表
    $('#sys_dict_list').datagrid({
        url: 'sys/dict/list',
        toolbar: '#sys_dict_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_sys_dict_list(data);
        }
    });

    function convert_sys_dict_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                if (item.dictCode == 'STATUS' && item.dictKey == row.status) {
                    row.statusText = item.dictValue;
                }
            });
        });
        return data;
    }

    function endEditSysDict() {
        if (editSysDictId == null) {
            return true
        }
        let dataGrid = $('#sys_dict_list');
        if (dataGrid.datagrid('validateRow', editSysDictId)) {
            let status = dataGrid.datagrid('getEditor', {index: editSysDictId, field: 'status'});
            dataGrid.datagrid('getRows')[editSysDictId]['statusText'] = $(status.target).combobox('getText');
            dataGrid.datagrid('endEdit', editSysDictId);
            editSysDictId = null;
            return true;
        } else {
            return false;
        }
    }

    function editSysDict(index) {
        if (editSysDictId == index) {
            return false;
        }
        if (endEditSysDict()) {
            editSysDictId = index;
            $('#sys_dict_list').datagrid('beginEdit', index);
        }
    }

    function addSysDict() {
        if (endEditSysDict()) {
            let dataGrid = $('#sys_dict_list');
            dataGrid.datagrid('insertRow', {
                index: 0,
                row: {
                    status: 1
                }
            });
            editSysDictId = 0;
            dataGrid.datagrid('selectRow', editSysDictId).datagrid('beginEdit', editSysDictId);
        }
    }

    function removeSysDict() {
        let dataGrid = $('#sys_dict_list');
        let rows = dataGrid.datagrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        for (let i = 0, len = rows.length; i < len; i++) {
            let index = dataGrid.datagrid('getRowIndex', rows[i]);
            dataGrid.datagrid('deleteRow', index);
            if (editSysDictId == index) {
                editSysDictId = null;
            }
            if (editSysDictId > index) {
                editSysDictId -= 1;
            }
        }
    }

    function undoSysDict() {
        $('#sys_dict_list').datagrid('rejectChanges');
        editSysDictId = null;
    }

    function saveSysDict() {
        if (endEditSysDict()) {
            let obj = {};
            obj['inserted'] = $('#sys_dict_list').datagrid('getChanges', 'inserted');
            obj['updated'] = $('#sys_dict_list').datagrid('getChanges', 'updated');
            obj['deleted'] = $('#sys_dict_list').datagrid('getChanges', 'deleted');
            $.messager.progress({
                title: '提示',
                msg: '保存中, 请稍候...',
                text: '',
                interval: 1000
            });
            $.ajax({
                type: 'post',
                url: 'sys/dict/save',
                contentType: 'application/json',
                data: JSON.stringify(obj),
                success: function (result) {
                    $.messager.progress('close');
                    if (result.code == 0) {
                        $('#sys_dict_list').datagrid('acceptChanges').datagrid('reload');
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

    function readSysDict(index) {
        let row = $('#sys_dict_list').datagrid('getRows')[index];
        $('#sys_dict_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'dev/dict/read',
            iconCls: 'icon-save',
            modal: true,
            title: '数据字典',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_dict_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysDictReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#sys_dict_list').datagrid('enableFilter', [{
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
                    $('#sys_dict_list').datagrid('removeFilterRule', 'status');
                } else {
                    $('#sys_dict_list').datagrid('addFilterRule', {
                        field: 'status',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#sys_dict_list').datagrid('doFilter');
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
                    $('#sys_dict_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#sys_dict_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#sys_dict_list').datagrid('doFilter');
            }
        }
    }]);

    // Excel导出
    function excelOutSysDict() {
        let filterRules = $('#sys_dict_list').datagrid('options').filterRules;
        let url = 'sys/dict/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sort = $('#sys_dict_list').datagrid('options').sortName;
        if (sort != null) {
            let order = $('#sys_dict_list').datagrid('options').sortOrder;
            url += '&sort=' + sort + '&order=' + order;
        }
        window.location.href = url;
    }

    // Excel导入
    function excelInSysDict() {
    }
</script>
</body>
</html>
