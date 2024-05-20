<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="easyui-layout" style="height: 100%">
    <%-- 系统角色树 --%>
    <div data-options="region: 'west', title: '系统角色', border: false, collapsible: false" style="width: 325px">
        <ul id="sys_auth_role_tree"></ul>
    </div>
    <%-- 功能菜单列表 --%>
    <div data-options="region: 'center', title: '功能菜单', headerCls: 'no_border_top', bodyCls: 'no_border_bottom', href: 'sys/auth/menu_list'"></div>
</div>
<%-- 弹出框 --%>
<div id="sys_auth_dialog"></div>
<script type="text/javascript">
    // 系统角色树
    $('#sys_auth_role_tree').tree({
        url: 'sys/role/tree',
        lines: true,
        loadFilter: function (data) {
            return convert_sys_auth_role_tree(data);
        },
        onSelect: function (node) {
            $('#sys_auth_menu_list').treegrid({
                url: 'sys/auth/menu/list?rid=' + node.id
            });
        }
    });

    // 格式化系统角色树
    function convert_sys_auth_role_tree(rows) {
        let iconCls = 'icon-man';
        let nodes = [];
        rows.forEach(row => {
            if (row.parentId == null) {
                row.text = row.roleName;
                row.iconCls = iconCls;
                nodes.push(row);
            }
        });
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
