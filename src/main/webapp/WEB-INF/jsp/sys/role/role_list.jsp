<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="easyui-layout" style="height: 100%">
    <%-- 系统角色树 --%>
    <div data-options="region: 'west', title: '系统角色', border: false, collapsible: false" style="width: 325px">
        <ul id="sys_role_tree"></ul>
    </div>
    <div data-options="region: 'center', border: false">
        <div class="easyui-layout" data-options="fit: true">
            <%-- 角色信息展示 --%>
            <div data-options="region: 'north', title: '角色信息', headerCls: 'no_border_top', href: 'sys/role/read'" style="height: 190px; padding: 10px"></div>
            <%-- 关联用户列表 --%>
            <div data-options="region: 'center', title: '关联用户', bodyCls: 'no_border_bottom', href: 'sys/role/user_list'"></div>
        </div>
    </div>
</div>
<%-- 自定义右键菜单 --%>
<shiro:hasPermission name="sys_role:create or sys_role:update or sys_role:delete">
    <div id="sys_role_tree_menu" class="easyui-menu">
        <shiro:hasPermission name="sys_role:create">
            <div onclick="addSysRole()" data-options="iconCls: 'icon-add'">新增</div>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys_role:update">
            <div onclick="editSysRole()" data-options="iconCls: 'icon-edit'">修改</div>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys_role:delete">
            <shiro:hasPermission name="sys_role:create or sys_role:update">
                <div class="menu-sep"></div>
            </shiro:hasPermission>
            <div onclick="removeSysRole()" data-options="iconCls: 'icon-remove'">删除</div>
        </shiro:hasPermission>
    </div>
</shiro:hasPermission>
<%-- 弹出框 --%>
<div id="sys_role_dialog"></div>
<script type="text/javascript">
    // 表单提交后执行回调函数
    function sysRoleCallback(roleCode) {
        $('#sys_role_dialog').dialog('close');
        $('#sys_role_tree').tree({
            onLoadSuccess: function (node, data) {
                // 选中新增/修改的节点
                loadSysRoleTreeAndSelectByRoleCode(roleCode);
            }
        });
    }

    // 根据角色编码选中对应角色树节点
    function loadSysRoleTreeAndSelectByRoleCode(roleCode) {
        let data = $('#sys_role_tree').tree('getRoots');

        function getTreeNodeByRoleCode(data) {
            for (let i = 0; i < data.length; i++) {
                if (data[i].roleCode == roleCode) {
                    return $('#sys_role_tree').tree('find', data[i].id);
                } else {
                    if (data[i].children != null) {
                        let node = getTreeNodeByRoleCode(data[i].children);
                        if (node != null) {
                            return node;
                        }
                    }
                }
            }
        }

        $('#sys_role_tree').tree('select', getTreeNodeByRoleCode(data).target);
    }

    // 系统角色树
    $('#sys_role_tree').tree({
        url: 'sys/role/tree',
        lines: true,
        loadFilter: function (data) {
            return convert_sys_role_tree(data);
        },
        onSelect: function (node) {
            // 角色信息展示
            loadSysRoleReadForm(node);
            // 关联用户列表
            $('#sys_role_user_list').datagrid({
                url: 'sys/role/user/list?rid=' + node.id
            });
        },
        onContextMenu: function (e, node) {
            // 取消右键点击的默认动作
            e.preventDefault();
            // 选中右键点击的节点
            $('#sys_role_tree').tree('select', node.target);
            // 打开自定义右键菜单
            $('#sys_role_tree_menu').menu('show', {
                hideOnUnhover: false,
                left: e.pageX,
                top: e.pageY
            });
        }
    });

    // 新增
    function addSysRole() {
        $('#sys_role_dialog').css('padding', '10px').dialog({
            width: 1200,
            height: 600,
            href: 'sys/role/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '新增系统角色',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commitSysRoleDetailForm('create', 'sysRoleCallback');
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_role_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                clearSysRoleDetailForm();
            }
        }).dialog('center');
    }

    // 修改
    function editSysRole() {
        let row = $('#sys_role_tree').tree('getSelected');
        $('#sys_role_dialog').css('padding', '10px').dialog({
            width: 1200,
            height: 600,
            href: 'sys/role/detail',
            iconCls: 'icon-save',
            modal: true,
            title: '修改系统角色',
            buttons: [{
                iconCls: 'icon-ok',
                text: '保存',
                width: 120,
                handler: function () {
                    commitSysRoleDetailForm('update', 'sysRoleCallback');
                }
            }, {
                iconCls: 'icon-cancel',
                text: '取消',
                width: 120,
                handler: function () {
                    $('#sys_role_dialog').dialog('close');
                }
            }],
            onLoad: function () {
                loadSysRoleDetailForm(row);
            }
        }).dialog('center');
    }

    // 删除
    function removeSysRole() {
        let row = $('#sys_role_tree').tree('getSelected');
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
            $.post('sys/role/delete', {id: row.id}, function (result) {
                $.messager.progress('close');
                if (result.code == 0) {
                    // 刷新角色树
                    $('#sys_role_tree').tree({
                        // 重置事件
                        onLoadSuccess: function (node, data) {
                        }
                    });
                    // 重置角色信息展示
                    clearSysRoleReadForm();
                    // 重置关联用户列表
                    $('#sys_role_user_list').datagrid('loadData', {total: 0, rows: []});
                } else {
                    $.messager.alert({
                        title: '提示',
                        msg: result.message
                    });
                }
            });
        });
    }

    // 格式化系统角色树
    function convert_sys_role_tree(rows) {
        let iconCls = 'icon-man';
        let nodes = [];
        for (let i = 0; i < rows.length; i++) {
            let row = rows[i];
            if (row.parentId == null) {
                row.text = row.roleName;
                row.iconCls = iconCls;
                nodes.push(row);
            }
        }
        let tempArr = [...nodes];
        while (tempArr.length) {
            let node = tempArr.shift();
            rows.forEach(row => {
                if (row.parentId == node.id) {
                    row.text = row.roleName;
                    row.iconCls = iconCls;
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
</script>
</body>
</html>
