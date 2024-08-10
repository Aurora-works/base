<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<form id="${menuCode}_detail_form" method="post" style="margin-block-end: 1em">
    <fieldset style="border: 1px solid #ccc">
        <legend>基础信息</legend>
        <div style="display: none">
            <input id="${menuCode}_detail_id" name="id" class="easyui-textbox" data-options="label: '编号'"/>
        </div>
        <#list columns as column>
        <div style="width:25%; float: left; padding: 0 20px">
            <#if column.dictCode??>
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-combobox"
            <#elseif column.columnType == "LocalDate">
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-datebox"
            <#elseif column.columnType == "LocalTime">
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-timespinner"
            <#elseif column.columnType == "LocalDateTime">
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-datetimebox"
            <#elseif column.columnType == "BigDecimal">
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-numberspinner"
            <#elseif column.columnType == "Long" || column.columnType == "Integer">
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-numberspinner"
            <#else>
            <input id="${menuCode}_detail_${column.entityName}" name="${column.entityName}" class="easyui-textbox"
            </#if>
                   <#if column.dictCode??>
                   data-options="label: '${column.displayName}', labelPosition: 'top'<#if column.isNullable == "0">, required: true, validateOnCreate: false</#if>, panelHeight: 'auto', editable: false, valueField: 'dictKey', textField: 'dictValue', url: 'sys/dict/combo/${column.dictCode}'" style="width: 100%"/>
                   <#else>
                   data-options="label: '${column.displayName}', labelPosition: 'top'<#if column.isNullable == "0">, required: true</#if
                   ><#if column.columnType == "BigDecimal">, precision: ${column.columnLength?split(",")?last}</#if
                   ><#if column.isUnique == "1">, validType: 'remote[\'${package1}/${package2}/validate/${column.entityName}\', \'value\']', invalidMessage: '${column.displayName}已存在, 请修改'</#if
                   ><#if column.isNullable == "0" || column.isUnique == "1">, validateOnCreate: false</#if>" style="width: 100%"/>
                   </#if>
        </div>
        </#list>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_detail_description" name="description" class="easyui-textbox" data-options="label: '描述', labelPosition: 'top'" style="width: 100%"/>
        </div>
        <div style="display: none">
            <input id="${menuCode}_detail_version" name="version" class="easyui-textbox" data-options="label: '版本'"/>
        </div>
    </fieldset>
</form>
<form id="${menuCode}_detail_form_2">
    <fieldset style="border: 1px solid #ccc">
        <legend>制单信息</legend>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_detail_createTime" name="createTime" class="easyui-textbox" data-options="label: '创建时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_detail_createUser" name="createUser" class="easyui-textbox" data-options="label: '创建人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_detail_lastTime" name="lastTime" class="easyui-textbox" data-options="label: '最后修改时间', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
        <div style="width:25%; float: left; padding: 0 20px">
            <input id="${menuCode}_detail_lastUser" name="lastUser" class="easyui-textbox" data-options="label: '最后修改人', labelPosition: 'top', readonly: true" style="width: 100%"/>
        </div>
    </fieldset>
</form>
<script type="text/javascript">
    // 初始化表单
    function clear${entityName}DetailForm() {
        $('#${menuCode}_detail_form').form('reset');
        $('#${menuCode}_detail_form_2').form('reset');
    }

    // 加载数据(数据)
    function load${entityName}DetailForm(row) {
        $('#${menuCode}_detail_form').form('load', row);
        $('#${menuCode}_detail_form_2').form('load', {
            'createTime': row.createTime ? formatDateTime(row.createTime) : '',
            'createUser': row.createUser ? formatSysUser(row.createUser) : '',
            'lastTime': row.lastTime ? formatDateTime(row.lastTime) : '',
            'lastUser': row.lastUser ? formatSysUser(row.lastUser) : ''
        });
        <#list columns as column>
        <#if column.isUnique == "1">
        $('#${menuCode}_detail_${column.entityName}').textbox({
            readonly: true,
            validType: null
        });
        </#if>
        </#list>
    }

    // 提交表单('create'/'update', 回调函数)
    function commit${entityName}DetailForm(op, callback) {
        $('#${menuCode}_detail_form').form('submit', {
            url: '${package1}/${package2}/' + op,
            onSubmit: function () {
                let isValid = $(this).form('validate');
                if (isValid) {
                    $.messager.progress({
                        title: '提示',
                        msg: '保存中, 请稍候...',
                        text: '',
                        interval: 1000
                    });
                }
                return isValid;
            },
            success: function (result) {
                result = JSON.parse(result);
                $.messager.progress('close');
                if (result.code == 0) {
                    callback();
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            }
        });
    }
</script>
</body>
</html>
