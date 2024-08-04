<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>后台管理系统</title>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/jquery.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.10.19/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.10.19/themes/${defaultThemes}/easyui.css">
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.10.19/themes/icon.css">
</head>
<body>
<%-- 登录窗口 --%>
<div id="login_win" style="width: 400px">
    <%-- 选择登录方式 --%>
    <form style="width: 300px; margin: 25px auto">
        <input class="easyui-radiobutton" id="use_login_form" name="change_login_form" data-options="labelPosition: 'after', label: '密码登录', checked: true"/>
        <input class="easyui-radiobutton" id="use_login_by_mobile_form" name="change_login_form" data-options="labelPosition: 'after', label: '短信登录'"/>
    </form>
    <%-- 密码登录表单 --%>
    <form id="login_form" method="post">
        <div style="width: 300px; margin: 25px auto">
            <%-- 用户名 --%>
            <input type="text" id="username" name="username" style="width: 100%" tabindex="1" value="admin"/>
        </div>
        <div style="width: 300px; margin: 25px auto">
            <%-- 密码 --%>
            <input type="text" id="password" name="password" style="width: 100%" tabindex="2" value="123456"/>
        </div>
        <div style="width: 300px; margin: 25px auto">
            <%-- 登录按钮 --%>
            <a href="javascript:" id="login_btn" style="width: 100%" tabindex="3">登录</a>
        </div>
    </form>
    <%-- 短信登录表单 --%>
    <form id="login_by_mobile_form" method="post" style="display: none">
        <div style="width: 300px; margin: 25px auto">
            <%-- 手机号 --%>
            <input type="text" id="mobilePhoneNumber" name="mobilePhoneNumber" style="width: 100%" tabindex="1"/>
        </div>
        <div style="width: 300px; margin: 25px auto">
            <%-- 验证码 --%>
            <input type="text" id="code" name="code" style="width: 100%" tabindex="2"/>
        </div>
        <div style="width: 300px; margin: 25px auto">
            <%-- 登录按钮 --%>
            <a href="javascript:" id="login_by_mobile_btn" style="width: 100%" tabindex="3">登录</a>
        </div>
    </form>
</div>
<script type="text/javascript">
    $('#login_win').window({
        title: '登录',
        collapsible: false,
        minimizable: false,
        maximizable: false,
        closable: false,
        draggable: false,
        resizable: false,
        shadow: false,
        modal: true
    });
    // 密码登录
    $('#username').textbox({
        prompt: '用户名',
        buttonIcon: 'icon-man',
        buttonAlign: 'left',
        onClickButton: function () {
            $(this).textbox('textbox').focus();
        },
        required: true
    });
    $('#password').passwordbox({
        prompt: '密　码',
        buttonIcon: 'icon-lock',
        buttonAlign: 'left',
        onClickButton: function () {
            $(this).textbox('textbox').focus();
        },
        required: true
    });
    $('#login_btn').linkbutton({
        iconCls: 'icon-ok'
    });
    $('#login_form').form({
        novalidate: true,
        url: 'login',
        onSubmit: function () {
            let isValid = $(this).form('enableValidation').form('validate');
            if (isValid) {
                $.messager.progress({
                    title: '提示',
                    msg: '正在验证用户名和密码, 请稍候...',
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
                $.messager.progress({
                    title: '提示',
                    msg: '登录成功, 正在进入管理后台...',
                    text: '',
                    interval: 1000
                });
                window.location = '';
            } else {
                $.messager.alert('提示', result.message ? result.message : '用户名或密码错误!');
            }
        }
    });
    $('#login_btn').click(function () {
        $('#login_form').submit();
    });
    $('#password').textbox('textbox').bind('keydown', function (event) {
        if (event.keyCode == 13) {
            $('#login_form').submit();
        }
    });
    $('#username').textbox('textbox').focus();

    // 短信登录
    $('#mobilePhoneNumber').textbox({
        prompt: '手机号',
        buttonText: '+86',
        buttonAlign: 'left',
        onClickButton: function () {
            $(this).textbox('textbox').focus();
        },
        required: true,
        validType: 'mobilePhoneNumber'
    });
    let isSendCode = false; // 是否在60s内发送过验证码
    $('#code').textbox({
        prompt: '验证码',
        buttonText: '获取验证码',
        onClickButton: function () {
            let isValid = $('#mobilePhoneNumber').textbox('enableValidation').textbox('isValid');
            if (isValid) {
                if (isSendCode) {
                    return;
                }
                isSendCode = true;
                let i = 60;
                let timer = setInterval(function () {
                    if (i < 1) {
                        isSendCode = false;
                        $('#code').textbox('button').html('<span class="l-btn-left" style="margin-top: 0"><span class="l-btn-text">获取验证码</span></span>');
                        clearInterval(timer);
                    } else {
                        $('#code').textbox('button').html('<span class="l-btn-left" style="margin-top: 0"><span class="l-btn-text">' + i + 's后重试</span></span>');
                        i--;
                    }
                }, 1000);
                $(this).textbox('textbox').focus();
                $.post('sendCode', {mobilePhoneNumber: $('#mobilePhoneNumber').textbox('getText')}, function (result) {
                    if (result.code != 0) {
                        $.messager.alert({
                            title: '提示',
                            msg: result.message
                        });
                    }
                });
            } else {
                $('#mobilePhoneNumber').textbox('textbox').focus();
            }
        },
        required: true
    });
    $('#login_by_mobile_btn').linkbutton({
        iconCls: 'icon-ok'
    });
    $('#login_by_mobile_form').form({
        novalidate: true,
        url: 'loginByMobile',
        onSubmit: function () {
            let isValid = $(this).form('enableValidation').form('validate');
            if (isValid) {
                $.messager.progress({
                    title: '提示',
                    msg: '正在验证, 请稍候...',
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
                $.messager.progress({
                    title: '提示',
                    msg: '登录成功, 正在进入管理后台...',
                    text: '',
                    interval: 1000
                });
                window.location = '';
            } else {
                $.messager.alert('提示', result.message ? result.message : '用户未注册或没有授权, 请联系管理员');
            }
        }
    });
    $('#login_by_mobile_btn').click(function () {
        $('#login_by_mobile_form').submit();
    });
    $('#code').textbox('textbox').bind('keydown', function (event) {
        if (event.keyCode == 13) {
            $('#login_by_mobile_form').submit();
        }
    });

    // 切换登录方式
    $('#use_login_form').radiobutton({
        onChange: function (isChecked) {
            if (isChecked) { // 密码登录
                $('#login_by_mobile_form').css('display', 'none');
                $('#login_form').css('display', 'block');
                $('#username').textbox('textbox').focus();
            }
        }
    });
    $('#use_login_by_mobile_form').radiobutton({
        onChange: function (isChecked) {
            if (isChecked) { // 短信登录
                $('#login_by_mobile_form').css('display', 'block');
                $('#login_form').css('display', 'none');
                $('#mobilePhoneNumber').textbox('textbox').focus();
            }
        }
    });

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
</body>
</html>
