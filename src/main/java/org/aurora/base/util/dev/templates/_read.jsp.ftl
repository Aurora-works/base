<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="${menuCode}_read_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="text-align: center; display: none">
            <input id="${menuCode}_read_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <#list columns as column>
        <div style="width: 25%; float:left; padding: 0 20px">
            <#if column.dictCode??>
            <input id="${menuCode}_read_${column.entityName}" name="${column.entityName}Text" class="easyui-textbox"
            <#else>
            <input id="${menuCode}_read_${column.entityName}" name="${column.entityName}" class="easyui-textbox"
            </#if>
                   data-options="label: '${column.displayName}', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        </#list>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_read_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<form id="${menuCode}_read_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_read_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_read_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_read_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_read_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 加载数据(数据)
    function load${entityName}ReadForm(row) {
        $('#${menuCode}_read_form').form('load', row);
        $('#${menuCode}_read_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
    }
</script>
</body>
</html>
