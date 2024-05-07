<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String basePath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>后台管理系统</title>
    <script type="text/javascript" src="<%=basePath%>/static/jquery-easyui-1.10.19/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/jquery-easyui-1.10.19/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/jquery-easyui-1.10.19/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/jquery-easyui-1.10.19/extension/datagrid-filter.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/jquery-easyui-1.10.19/themes/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/jquery-easyui-1.10.19/themes/icon.css">
</head>
<%-- 布局 --%>
<body class="easyui-layout">
<%-- 顶部 --%>
<div data-options="region: 'north', title: '顶部'" style="height: 100px; overflow-y: hidden; display: flex; align-items: center; justify-content: space-between">
    <div style="height: 100%; flex: 1">
        <img src="<%=basePath%>/static/images/base.png" alt="logo" style="height: 100%; max-height: 67px">
    </div>
    <div>
        <a href="javascript:" class="easyui-linkbutton" data-options="iconCls: 'icon-exit', plain: true, text: '退出系统'" onclick="logout()" style="margin-right: 10px"></a>
    </div>
</div>
<%-- 左侧功能菜单 --%>
<div data-options="region: 'west', title: '功能菜单'" style="width: 275px; overflow-x: hidden">
    <div id="west_menu" style="width: 100%"></div>
</div>
<%-- 操作中心 --%>
<div data-options="region: 'center', title: '操作中心'">
    <div id="center_tabs" class="easyui-tabs" data-options="fit: true, border: false, tabWidth: 120">
        <div data-options="title: '主页'" style="padding: 20px"></div>
    </div>
</div>
<%-- 底部 --%>
<div data-options="region: 'south', title: '底部'" style="height: 65px; overflow-y: hidden; display: flex; align-items: center; justify-content: center">
    <p>后台管理系统</p>
</div>
</body>
<script type="text/javascript">
    // 加载功能菜单
    $('#west_menu').sidemenu({
        data: convert_west_menu(JSON.parse('${westMenuData}')),
        border: false,
        onSelect: function (item) {
            let url = item.url;
            let title = item.text;
            if ($('#center_tabs').tabs('exists', title)) {
                $('#center_tabs').tabs('select', title);
            } else {
                $('#center_tabs').tabs('add', {
                    title: title,
                    href: url,
                    closable: true
                });
            }
        }
    });

    // 格式化功能菜单
    function convert_west_menu(rows) {
        let nodes = [];
        rows.forEach(row => {
            if (row.parentId == null) {
                nodes.push({
                    id: row.id,
                    text: row.menuName,
                    iconCls: row.css,
                    url: row.href,
                    state: row.isOpen == '1' ? 'open' : 'closed'
                });
            }
        });
        let tempArr = [...nodes];
        while (tempArr.length) {
            let node = tempArr.shift();
            rows.forEach(row => {
                if (row.parentId == node.id) {
                    let child = {
                        id: row.id,
                        text: row.menuName,
                        iconCls: row.css,
                        url: row.href,
                        state: row.isOpen == '1' ? 'open' : 'closed'
                    };
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

    // 退出系统
    function logout() {
        $.messager.progress({
            title: '提示',
            msg: '正在退出系统...',
            text: '',
            interval: 1000
        });
        $.post('/logout', function (result) {
            $.messager.progress('close');
            if (result.code == 0) {
                $.messager.progress({
                    title: '提示',
                    msg: '退出成功, 正在返回登录页面...',
                    text: '',
                    interval: 1000
                });
                window.location = '/';
            } else {
                $.messager.alert({
                    title: '提示',
                    msg: result.message
                });
            }
        });
    }

    // 格式化日期时间
    function formatDateTime(date) {
        let d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;
        return [year, month, day].join('-') + ' ' + d.toLocaleTimeString();
    }

    // 格式化创建人/最后修改人
    function formatSysUser(user) {
        return user.nickname + '[' + user.username + ']';
    }

    // 添加验证规则
    $.extend($.fn.validatebox.defaults.rules, {
        mobilePhoneNumber: { // 手机号验证
            validator: function (value, param) {
                return /^1[0-9]{10}$/.test(value);
            },
            message: '请输入正确的手机号码'
        }
    });
</script>
</html>
