$(document).ready(function () {

    Date.prototype.format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };


    window.car = Object();
    window.car.loginDialog = new function () {
        var disabled = false;
        var name = "", pwd = "",role="";

        this.disable = function () {
            disabled = true;
            $("#login-holder").fadeIn(500);
        };

        this.enable = function () {
            disabled = false;
            $("#login-holder").fadeOut(500);
        };

        this.show = function () {
            $("#login-panel").fadeIn(2000);
        };

        this.hide = function () {
            $("#login-panel").fadeOut(1000);
        };

        this.login = function () {
            if (!disabled) {
                window.car.loginDialog.disable();
                window.car.loginDialog.disableLogin();
                Materialize.toast('尝试登录', 3000, 'rounded');
                $.getJSON("api/user/login?uid=" + $("#name").val() + "&pwd=" + $("#pwd").val()+"&role="+$("#role").val(), function (res) {
                    window.car.loginDialog.enable();
                    if (res["code"] != 0) {
                        if(res["code"] == -4) {
                            Materialize.toast('登录失败：没有权限', 3000, 'rounded');
                        }else {
                            Materialize.toast('登录失败：用户名密码错误', 3000, 'rounded');
                        }
                    } else {
                        Materialize.toast('登录成功', 3000, 'rounded');
                        window.car.loginDialog.hide();
                        setTimeout(function () {
                            location.href = "/";
                        },3000);
                    }
                }).fail(function () {
                    window.car.loginDialog.enable();
                    window.car.loginDialog.enableLogin();
                    Materialize.toast('登录失败：服务器异常', 3000, 'rounded');
                });
            }
        };

        this.disableLogin = function () {
            $("#btn-login-submit").addClass("disabled").addClass("waves-effect");
        };

        this.enableLogin = function () {
            $("#btn-login-submit").removeClass("disabled").removeClass("waves-effect");
        };

        this.onchange = function () {
            if (name != "" && pwd != "") {
                this.enableLogin();
            } else {
                this.disableLogin();
            }
        };

        $("#name").keydown(function () {
            name = $(this).val();
            window.car.loginDialog.onchange();
        }).change(function(){
            name = $(this).val();
            window.car.loginDialog.onchange();
        });

        $("#pwd").keydown(function (e) {
            pwd = $(this).val();
            window.car.loginDialog.onchange();

            if (e.keyCode == 13) {
                window.car.loginDialog.login();
            }
        }).change(function(){
            pwd = $(this).val();
            window.car.loginDialog.onchange();
        });

        $("#btn-login-submit").click(function () {
            window.car.loginDialog.login();
        });

        $("#login-form").submit(function () {
            window.car.loginDialog.login();
        }).on("reset",function(){
            name = $(this).val();
            pwd = $(this).val();
            window.car.loginDialog.onchange();
        });

        $("#btn-login-reset").click(function () {
            $("#name").val("");
            $("#pwd").val("");
            name = pwd = "";
            window.car.loginDialog.onchange();
        });

        this.show();
        this.enable();
    };

    $('select').material_select();


    $('.collapsible').collapsible({
        accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });



});