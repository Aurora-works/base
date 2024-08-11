<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="${menuCode}_list" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'ckb', checkbox: true"></th>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <#list columns as column>
        <#if column?index == 0>
        <th data-options="field: '${column.entityName}', title: '${column.displayName}', width: 100, sortable: true,
            formatter: function (value, row, index) {
                <#if column.dictCode??>
                return '<span onclick=\'read${entityName}(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + row.${column.entityName}Text + '</span>';
                <#else>
                return '<span onclick=\'read${entityName}(' + index + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
                </#if>
            }"></th>
        <#else>
        <#if column.dictCode??>
        <th data-options="field: '${column.entityName}', title: '${column.displayName}', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.${column.entityName}Text;
            }"></th>
        <#elseif column.columnType == "LocalDateTime">
        <th data-options="field: '${column.entityName}', title: '${column.displayName}', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return formatDateTime(value);
            }"></th>
        <#else>
        <th data-options="field: '${column.entityName}', title: '${column.displayName}', width: 100, sortable: true"></th>
        </#if>
        </#if>
        </#list>
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
<div id="${menuCode}_toolbar">
    <shiro:hasPermission name="${menuCode}:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增'" onclick="add${entityName}()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${menuCode}:update">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-edit', plain: true, text: '修改'" onclick="edit${entityName}()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${menuCode}:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="remove${entityName}()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${menuCode}:create or ${menuCode}:update or ${menuCode}:delete">
        <span class="datagrid-btn-separator" style="float: none"></span>
    </shiro:hasPermission>
    <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-out', plain: true, text: '导出'" onclick="excelOut${entityName}()"></a>
    <shiro:hasPermission name="${menuCode}:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-excel-in', plain: true, text: '导入'" onclick="excelIn${entityName}()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="${menuCode}_dialog"></div>
<script type="text/javascript">
    // 表单提交后执行回调函数
    function ${entityName_}Callback() {
        $('#${menuCode}_dialog').dialog('close');
        $('#${menuCode}_list').datagrid('reload');
    }

    // 新增按钮
    function add${entityName}() {
        $('#${menuCode}_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: '${package1}/${package2}/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '新增系统参数',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commit${entityName}DetailForm('create', ${entityName_}Callback);
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#${menuCode}_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                clear${entityName}DetailForm();
            }
        }).dialog('center');
    }

    // 修改按钮
    function edit${entityName}() {
        let row = $('#${menuCode}_list').datagrid('getSelected');
        if (row) {
            $('#${menuCode}_dialog').css('height', '600px').css('width', '1200px').css('padding', '10px').dialog({
                href: '${package1}/${package2}/detail',
                iconCls: 'icon-save',
                modal: true,
                title: '修改系统参数',
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '保存',
                    width: 120,
                    handler: function () {
                        commit${entityName}DetailForm('update', ${entityName_}Callback);
                    }
                }, {
                    iconCls: 'icon-cancel',
                    text: '取消',
                    width: 120,
                    handler: function () {
                        $('#${menuCode}_dialog').dialog('close');
                    }
                }],
                onLoad: function () {
                    load${entityName}DetailForm(row);
                }
            }).dialog('center');
        }
    }

    // 删除按钮
    function remove${entityName}() {
        let rows = $('#${menuCode}_list').datagrid('getSelections');
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
            $.post('${package1}/${package2}/delete', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#${menuCode}_list').datagrid('reload');
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
    $('#${menuCode}_list').datagrid({
        url: '${package1}/${package2}/list',
        toolbar: '#${menuCode}_toolbar',
        pageSize: 50,
        border: false,
        fitColumns: true,
        pagination: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_${menuCode}_list(data);
        }
    });

    function convert_${menuCode}_list(data) {
        data.rows.forEach(row => {
            data.formatter.forEach(item => {
                <#list columns as column>
                <#if column.dictCode??>
                if (item.dictCode == '${column.dictCode}' && item.dictKey == row.${column.entityName}) {
                    row.${column.entityName}Text = item.dictValue;
                }
                </#if>
                </#list>
            });
        });
        return data;
    }

    function read${entityName}(index) {
        let row = $('#${menuCode}_list').datagrid('getRows')[index];
        $('#${menuCode}_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: '${package1}/${package2}/read',
            iconCls: 'icon-save',
            modal: true,
            title: '系统参数',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#${menuCode}_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                load${entityName}ReadForm(row);
            }
        }).dialog('center');
    }

    // 启用数据网格行过滤
    $('#${menuCode}_list').datagrid('enableFilter', [{
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
                    $('#${menuCode}_list').datagrid('removeFilterRule', 'createTime');
                } else {
                    $('#${menuCode}_list').datagrid('addFilterRule', {
                        field: 'createTime',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#${menuCode}_list').datagrid('doFilter');
            }
        }
    }<#list columns as column><#if column.dictCode??>, {
        field: '${column.entityName}',
        type: 'combobox',
        options: {
            valueField: 'dictKey',
            textField: 'dictValue',
            url: 'sys/dict/combo/${column.dictCode}',
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
                    $('#${menuCode}_list').datagrid('removeFilterRule', '${column.entityName}');
                } else {
                    $('#${menuCode}_list').datagrid('addFilterRule', {
                        field: '${column.entityName}',
                        op: 'equal',
                        value: value,
                        type: 'combobox'
                    });
                }
                $('#${menuCode}_list').datagrid('doFilter')
            }
        }
    }<#elseif column.columnType == "LocalDate" || column.columnType == "LocalDateTime">, {
        field: '${column.entityName}',
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
                    $('#${menuCode}list').datagrid('removeFilterRule', '${column.entityName}');
                } else {
                    $('#${menuCode}_list').datagrid('addFilterRule', {
                        field: '${column.entityName}',
                        op: 'equal',
                        value: value,
                        type: 'datebox'
                    });
                }
                $('#${menuCode}_list').datagrid('doFilter');
            }
        }
    }<#elseif column.columnType == "BigDecimal">, {
        field: '${column.entityName}',
        type: 'numberbox',
        op: ['equal', 'less', 'greater'],
        options: {
            precision: ${column.columnLength?split(",")?last}
        }
    }<#elseif column.columnType == "Long" || column.columnType == "Integer">, {
        field: '${column.entityName}',
        type: 'numberbox',
        op: ['equal', 'less', 'greater'],
        options: {
            precision: 0
        }
    }</#if>
    </#list>]);

    // Excel导出
    function excelOut${entityName}() {
        let filterRules = $('#${menuCode}_list').datagrid('options').filterRules;
        let url = '${package1}/${package2}/excel/out?filterRules=' + encodeURIComponent(JSON.stringify(filterRules));
        let sort = $('#${menuCode}_list').datagrid('options').sortName;
        if (sort != null) {
            let order = $('#${menuCode}_list').datagrid('options').sortOrder;
            url += '&sort=' + sort + '&order=' + order;
        }
        window.location.href = url;
    }

    // Excel导入
    function excelIn${entityName}() {
        $('#${menuCode}_dialog').css('padding', '10px').dialog({
            width: 400,
            height: 165,
            href: '${package1}/${package2}/import_excel',
            iconCls: 'icon-excel-in',
            modal: true,
            title: 'Excel批量导入',
            buttons: null
        }).dialog('center');
    }
</script>
</body>
</html>
