<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>后台管理系统</title>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/jquery.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/extension/datagrid-filter.js"></script>
    <script type="text/javascript" src="static/echarts-5.5.1/echarts.min.js"></script>
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.10.19/themes/${defaultThemes}/easyui.css">
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.10.19/themes/icon.css">
</head>
<%-- 布局 --%>
<body class="easyui-layout">
<%-- 顶部 --%>
<div data-options="region: 'north', title: '顶部'" style="height: 100px; overflow-y: hidden; display: flex; align-items: center; justify-content: space-between">
    <div style="height: 100%; flex: 1">
        <img src="static/images/base.png" alt="logo" style="height: 100%; max-height: 67px">
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
        <div data-options="title: '主页'" style="padding: 0 20px">
            <div>
                <p>${osStr}</p>
                <p>Booted: ${systemBootTime}</p>
                <p>Uptime: <span id="system_uptime">${systemUptime}</span></p>
                <p>${cpuStr}</p>
                <hr/>
            </div>
            <div style="display: flex; flex-wrap: wrap">
                <div id="echarts_1" style=" width: 515px; height: 325px"></div>
                <div class="echarts_separator"></div>
                <div id="echarts_2" style=" width: 515px; height: 325px"></div>
                <div class="echarts_separator"></div>
                <div id="echarts_3" style=" width: 515px; height: 325px"></div>
            </div>
        </div>
    </div>
</div>
</div>
<%-- 底部 --%>
<div data-options="region: 'south', title: '底部'" style="height: 65px; overflow-y: hidden; display: flex; align-items: center; justify-content: center">
    <p>后台管理系统</p>
</div>
</body>
<style>
    /** 不显示上边框 **/
    .no_border_top {
        border-top: none;
    }

    /** 不显示下边框 **/
    .no_border_bottom {
        border-bottom: none;
    }

    .echarts_separator {
        height: 285px;
        width: 1px;
        background-color: #eee;
        margin: 10px;
    }
</style>
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
        $.post('logout', function (result) {
            $.messager.progress('close');
            if (result.code == 0) {
                $.messager.progress({
                    title: '提示',
                    msg: '退出成功, 正在返回登录页面...',
                    text: '',
                    interval: 1000
                });
                window.location = '';
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

    // 扩展验证规则
    $.extend($.fn.validatebox.defaults.rules, {
        mobilePhoneNumber: { // 手机号验证
            validator: function (value, param) {
                return /^1[0-9]{10}$/.test(value);
            },
            message: '请输入正确的手机号码'
        }
    });

    // echarts
    let echarts_1 = echarts.init(document.getElementById('echarts_1'));

    let option_1 = {
        title: {
            text: 'CPU使用率',
            subtext: '周期 : 1分钟'
        },
        grid: {
            bottom: '10%'
        },
        tooltip: {
            trigger: 'axis',
            valueFormatter: (value) => value.toFixed(2) + ' %'
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: "none"
                },
                magicType: {
                    type: ['bar']
                },
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'time'
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} %'
            }
        },
        series: [
            {
                name: '总体使用率',
                data: [],
                type: 'line',
                showSymbol: false
            }
        ]
    };

    echarts_1.setOption(option_1);

    let echarts_2 = echarts.init(document.getElementById('echarts_2'));

    let option_2 = {
        title: {
            text: '内存使用率',
            subtext: '周期 : 1分钟'
        },
        grid: {
            bottom: '10%'
        },
        tooltip: {
            trigger: 'axis',
            valueFormatter: (value) => value.toFixed(2) + ' %'
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: "none"
                },
                magicType: {
                    type: ['bar']
                },
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'time'
        },
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: '{value} %'
            },
            max: 100
        }],
        series: [
            {
                name: '正在使用的内存',
                data: [],
                type: 'line',
                smooth: true,
                showSymbol: false,
                areaStyle: {}
            }
        ]
    };

    echarts_2.setOption(option_2);

    let echarts_3 = echarts.init(document.getElementById('echarts_3'));

    let option_3 = {
        title: {
            text: '磁盘使用率',
            subtext: '周期 : 1分钟'
        },
        grid: {
            bottom: '10%'
        },
        tooltip: {
            trigger: 'axis',
            valueFormatter: (value) => value.toFixed(2) + ' %'
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: "none"
                },
                magicType: {
                    type: ['bar']
                },
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'time'
        },
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: '{value} %'
            },
            max: 100
        }],
        series: []
    };

    echarts_3.setOption(option_3);

    echarts_1.showLoading();
    echarts_2.showLoading();
    echarts_3.showLoading();

    function echarts_set_data() {
        $.get('systemMonitor', function (data) {

            echarts_1.hideLoading();
            echarts_2.hideLoading();
            echarts_3.hideLoading();

            option_1.series[0].data = data.cpuLoadArr;
            echarts_1.setOption(option_1, true);

            option_2.series[0].data = data.memoryInUseArr;
            echarts_2.setOption(option_2, true);

            option_3.series = function () {
                let nodes = [];
                for (let i = 0; i < data.fileStoreNameArr.length && i < data.spaceInUseArr.length; i++) {
                    nodes.push({
                        name: data.fileStoreNameArr[i],
                        data: data.spaceInUseArr[i],
                        type: 'line',
                        showSymbol: false,
                        smooth: true
                    });
                }
                return nodes;
            }();
            echarts_3.setOption(option_3, true);
        });
    }

    echarts_set_data();

    setInterval(echarts_set_data, 60000);
</script>
</html>
