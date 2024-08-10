<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="sys_table_column_read_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="display: none">
            <input id="sys_table_column_read_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_columnName" name="columnName" class="easyui-textbox" data-options="label: '字段名称', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_displayName" name="displayName" class="easyui-textbox" data-options="label: '显示名称', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_entityName" name="entityName" class="easyui-textbox" data-options="label: '实体类字段名', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_columnType" name="columnTypeText" class="easyui-textbox" data-options="label: '字段类型', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_columnLength" name="columnLength" class="easyui-textbox" data-options="label: '字段长度', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_isPrimary" name="isPrimaryText" class="easyui-textbox" data-options="label: '是否主键', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_isNullable" name="isNullableText" class="easyui-textbox" data-options="label: '可否空值', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_isUnique" name="isUniqueText" class="easyui-textbox" data-options="label: '是否唯一', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_isInsertable" name="isInsertableText" class="easyui-textbox" data-options="label: '可插入', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_isUpdatable" name="isUpdatableText" class="easyui-textbox" data-options="label:'可修改', labelPosition:'top', readonly:true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_orderBy" name="orderBy" class="easyui-textbox" data-options="label: '排序', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_foreignTableId" name="foreignTableIdText" class="easyui-textbox" data-options="label: '外键表', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_dictCode" name="dictCodeText" class="easyui-textbox" data-options="label: '数据字典', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<form id="sys_table_column_read_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="sys_table_column_read_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 加载数据(数据)
    function loadSysTableColumnReadForm(row) {
        $('#sys_table_column_read_form').form('load', row);
        $('#sys_table_column_read_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
    }
</script>
</body>
</html>
