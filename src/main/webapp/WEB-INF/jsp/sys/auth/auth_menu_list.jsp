<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<%-- 数据网格 --%>
<table id="sys_auth_menu_list" class="easyui-treegrid"
        <shiro:hasPermission name="sys_auth:update">
            data-options="onClickRow: editSysAuth"
        </shiro:hasPermission>
       style="height: 100%">
    <thead>
    <tr>
        <th data-options="field: 'id', title: '编号', width: 100, hidden: true, sortable: true"></th>
        <th data-options="field: 'menuName', title: '菜单名称', width: 100, sortable: true,
            formatter: function (value, row, index) {
                return '<span onclick=\'readAuthSysMenu(' + row.id + ');\' style=\'text-decoration: underline; cursor: pointer\'>' + value + '</span>';
            }"></th>
        <th data-options="field: 'readOp', title: '查看', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.isParent) {
                    return '';
                }
                return row.readOpText;
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
        <th data-options="field: 'createOp', title: '创建', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.isParent) {
                    return '';
                }
                return row.createOpText;
            },
            editor: {
                type: 'combobox',
                options : {
                    required: true,
                    panelHeight: 'auto',
                    editable: false,
                    valueField: 'dictKey',
                    textField: 'dictValue',
                    url: 'sys/dict/combo/status'
                }
            }"></th>
        <th data-options="field: 'updateOp', title: '修改', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.isParent) {
                    return '';
                }
                return row.updateOpText;
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
        <th data-options="field: 'deleteOp', title: '删除', width: 100, sortable: true,
            formatter: function (value, row, index) {
                if (row.isParent) {
                    return '';
                }
                return row.deleteOpText;
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
    </tr>
    </thead>
</table>
<%-- 数据网格的头部工具栏 --%>
<shiro:hasPermission name="sys_auth:update">
    <div id="sys_auth_menu_toolbar">
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-save', plain: true, text: '保存'" onclick="saveSysAuth()"></a>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-undo', plain: true, text: '撤销'" onclick="undoSysAuth()"></a>
    </div>
</shiro:hasPermission>
<%-- 弹出框 --%>
<div id="sys_auth_menu_dialog"></div>
<script type="text/javascript">
    var editSysAuthId = null;
    $('#sys_auth_menu_list').treegrid({
        idField: 'id',
        treeField: 'menuName',
        toolbar: '#sys_auth_menu_toolbar',
        border: false,
        fitColumns: true,
        rownumbers: true,
        onLoadSuccess: function () {
            editSysAuthId = null;
        },
        loadFilter: function (data) {
            return convert_auth_menu_list_tree(data);
        }
    });

    // 格式化列表
    function convert_auth_menu_list_tree(data) {
        let nodes = [];
        data.rows.forEach(row => {
            if (row.parentId == null) {
                data.formatter.forEach(item => {
                    if (item.dictCode == 'STATUS' && item.dictKey == row.readOp) {
                        row.readOpText = item.dictValue;
                    }
                    if (item.dictCode == 'STATUS' && item.dictKey == row.createOp) {
                        row.createOpText = item.dictValue;
                    }
                    if (item.dictCode == 'STATUS' && item.dictKey == row.updateOp) {
                        row.updateOpText = item.dictValue;
                    }
                    if (item.dictCode == 'STATUS' && item.dictKey == row.deleteOp) {
                        row.deleteOpText = item.dictValue;
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
                        if (item.dictCode == 'STATUS' && item.dictKey == row.readOp) {
                            row.readOpText = item.dictValue;
                        }
                        if (item.dictCode == 'STATUS' && item.dictKey == row.createOp) {
                            row.createOpText = item.dictValue;
                        }
                        if (item.dictCode == 'STATUS' && item.dictKey == row.updateOp) {
                            row.updateOpText = item.dictValue;
                        }
                        if (item.dictCode == 'STATUS' && item.dictKey == row.deleteOp) {
                            row.deleteOpText = item.dictValue;
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

    function endEditSysAuth() {
        if (editSysAuthId == null) {
            return true
        }
        let treeGrid = $('#sys_auth_menu_list');
        if (treeGrid.treegrid('validateRow', editSysAuthId)) {
            let readOp = treeGrid.treegrid('getEditor', {index: editSysAuthId, field: 'readOp'});
            let createOp = treeGrid.treegrid('getEditor', {index: editSysAuthId, field: 'createOp'});
            let updateOp = treeGrid.treegrid('getEditor', {index: editSysAuthId, field: 'updateOp'});
            let deleteOp = treeGrid.treegrid('getEditor', {index: editSysAuthId, field: 'deleteOp'});
            treeGrid.treegrid('find', editSysAuthId).readOpText = $(readOp.target).combobox('getText');
            treeGrid.treegrid('find', editSysAuthId).createOpText = $(createOp.target).combobox('getText');
            treeGrid.treegrid('find', editSysAuthId).updateOpText = $(updateOp.target).combobox('getText');
            treeGrid.treegrid('find', editSysAuthId).deleteOpText = $(deleteOp.target).combobox('getText');
            treeGrid.treegrid('endEdit', editSysAuthId);
            editSysAuthId = null;
            return true;
        } else {
            return false;
        }
    }

    function editSysAuth(row) {
        if (editSysAuthId == row.id) {
            return false;
        }
        if (endEditSysAuth()) {
            if (row.isParent) {
                return false;
            }
            editSysAuthId = row.id;
            $('#sys_auth_menu_list').treegrid('beginEdit', editSysAuthId);
        }
    }

    function saveSysAuth() {
        let role = $('#sys_auth_role_tree').tree('getSelected');
        if (!role || !endEditSysAuth()) {
            return false;
        }
        let changes = $('#sys_auth_menu_list').treegrid('getChanges');
        if (changes.length == 0) {
            return false;
        }
        $.messager.progress({
            title: '提示',
            msg: '保存中, 请稍候...',
            text: '',
            interval: 1000
        });
        let rid = role.id;
        $.ajax({
            type: 'post',
            url: 'sys/auth/save?rid=' + rid,
            contentType: 'application/json',
            data: JSON.stringify(changes),
            success: function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    $('#sys_auth_menu_list').treegrid('acceptChanges').treegrid('reload');
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            }
        });
    }

    function undoSysAuth() {
        let treeGrid = $('#sys_auth_menu_list');
        if (treeGrid.treegrid('getChanges').length > 0) {
            treeGrid.treegrid('acceptChanges').treegrid('reload');
        } else {
            if (editSysAuthId == null) {
                return;
            }
            treeGrid.treegrid('cancelEdit', editSysAuthId);
        }
        editSysAuthId = null;
    }

    function readAuthSysMenu(id) {
        $('#sys_auth_menu_dialog').css('padding', '10px').dialog({
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
                    $('#sys_auth_menu_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysMenuReadFormById(id);
            }
        }).dialog('center');
    }
</script>
</body>
</html>
