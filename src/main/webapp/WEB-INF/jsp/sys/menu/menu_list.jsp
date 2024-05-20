<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_menu_list" style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'id', title: '编号', width: 100, sortable: true, hidden: true"></th>
        <th data-options="field: 'menuName', title: '菜单名称', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readSysMenu(' + row.id + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            }"></th>
        <th data-options="field: 'href', title: '菜单链接', width: 150, sortable: true"></th>
        <th data-options="field: 'parentId', title: '父菜单', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.parentName;
            }"></th>
        <th data-options="field: 'orderBy', title: '排序', width: 100, sortable: true"></th>
        <th data-options="field: 'css', title: '菜单图标', width: 100, sortable: true"></th>
        <th data-options="field: 'isOpen', title: '默认展开', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.isOpenText;
            }"></th>
        <th data-options="field: 'status', title: '状态', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return row.statusText;
            }"></th>
        <th data-options="field: 'description', title: '描述', width: 100, sortable: true"></th>
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
<div id="sys_menu_toolbar">
    <shiro:hasPermission name="sys_menu:create">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-add', plain: true, text: '新增'" onclick="addSysMenu()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_menu:update">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-edit', plain: true, text: '修改'" onclick="editSysMenu()"></a>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys_menu:delete">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-remove', plain: true, text: '删除'" onclick="removeSysMenu()"></a>
    </shiro:hasPermission>
</div>
<%-- 弹出框 --%>
<div id="sys_menu_dialog"></div>
<script type="text/javascript">
    $('#sys_menu_list').treegrid({
        url: 'sys/menu/tree',
        idField: 'id',
        treeField: 'menuName',
        toolbar: '#sys_menu_toolbar',
        border: false,
        fitColumns: true,
        rownumbers: true,
        loadFilter: function (data) {
            return convert_menu_list_tree(data);
        }
    });

    // 格式化列表
    function convert_menu_list_tree(data) {
        let nodes = [];
        data.rows.forEach(row => {
            if (row.parentId == null) {
                data.formatter.forEach(item => {
                    if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isOpen) {
                        row.isOpenText = item.dictValue;
                    }
                    if (item.dictCode == 'STATUS' && item.dictKey == row.status) {
                        row.statusText = item.dictValue;
                    }
                });
                row.iconCls = row.css;
                nodes.push(row);
            }
        });
        let tempArr = [...nodes];
        while (tempArr.length) {
            let node = tempArr.shift();
            data.rows.forEach(row => {
                if (row.parentId == node.id) {
                    data.formatter.forEach(item => {
                        if (item.dictCode == 'SysMenu' && item.dictKey == row.parentId) {
                            row.parentName = item.dictValue;
                        }
                        if (item.dictCode == 'YES_OR_NO' && item.dictKey == row.isOpen) {
                            row.isOpenText = item.dictValue;
                        }
                        if (item.dictCode == 'STATUS' && item.dictKey == row.status) {
                            row.statusText = item.dictValue;
                        }
                    });
                    row.iconCls = row.css;
                    let child = row;
                    if (node.children) {
                        node.children.push(child);
                    } else {
                        node.children = [child];
                    }
                    tempArr.push(child);
                }
            });
        }
        return nodes;
    }

    // 表单提交后执行回调函数
    function sysMenuCallback() {
        $('#sys_menu_dialog').dialog('close');
        $('#sys_menu_list').treegrid('reload');
    }

    // 新增按钮
    function addSysMenu() {
        $('#sys_menu_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'sys/menu/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '新增功能菜单',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commitSysMenuDetailForm('create', 'sysMenuCallback');
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_menu_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                clearSysMenuDetailForm();
            }
        }).dialog('center');
    }

    // 修改按钮
    function editSysMenu() {
        let row = $('#sys_menu_list').treegrid('getSelected');
        if (row) {
            $('#sys_menu_dialog').css('padding', '10px').dialog({
                height: 600,
                width: 1200,
                href: 'sys/menu/detail',
                iconCls: 'icon-save',
                modal: true,
                title: '修改功能菜单',
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '保存',
                    width: 120,
                    handler: function () {
                        commitSysMenuDetailForm('update', 'sysMenuCallback');
                    }
                }, {
                    iconCls: 'icon-cancel',
                    text: '取消',
                    width: 120,
                    handler: function () {
                        $('#sys_menu_dialog').dialog('close');
                    }
                }],
                onLoad: function () {
                    loadSysMenuDetailForm(row);
                }
            }).dialog('center');
        }
    }

    // 删除按钮
    function removeSysMenu() {
        let rows = $('#sys_menu_list').treegrid('getSelections');
        if (!rows.length > 0) {
            return false;
        }
        $.messager.confirm('操作提示', '确认删除?', function (result) {
            if (!result) {
                return false;
            }
            $.messager.progress({
                title: '提示',
                msg: '删除中, 请稍后...',
                text: '',
                interval: 1000
            });
            let ids = [];
            for (let i = 0, len = rows.length; i < len; i++) {
                ids.push(rows[i].id)
            }
            $.post('sys/menu/delete', {ids: ids}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_menu_list').treegrid('reload');
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        });
    }

    // 详情
    function readSysMenu(id) {
        let row = $('#sys_menu_list').treegrid('find', id);
        $('#sys_menu_dialog').css('padding', '10px').dialog({
            height: 600,
            width: 1200,
            href: 'sys/menu/read',
            iconCls: 'icon-save',
            modal: true,
            title: '功能菜单',
            buttons: [{
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_menu_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysMenuReadForm(row);
            }
        }).dialog('center');
    }
</script>
</body>
</html>
