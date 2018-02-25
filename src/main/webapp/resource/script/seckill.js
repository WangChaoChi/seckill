//存放主要交互逻辑js代码
//javascript模块化
var seckill = {
    //封装秒杀相关ajax 的url
    URL: {
        now: function () {
            return "/seckill/time/now";
        },
        exposer:function (seckillId) {
            return "/seckill/" + seckillId + "/exposer";
        },
        execution:function (seckillId,md5) {
            return "/seckill/"+seckillId+"/"+md5+"/execution"
        }
    },
    //处理秒杀逻辑
    handlerSeckill:function (seckillId,seckillBox) {
        //获取秒杀地址，控制实现逻辑，控制秒杀
        seckillBox.hide().html('<betton class="btn btn-primary btn-lg" id="killBtn">开始秒杀</betton>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回掉函数中执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);
                    //绑定一次点击事件
                    $("#killBtn").one("click",function () {
                        //执行秒杀请求
                        //1：先禁用按钮
                        $(this).addClass("disabled");
                        //2:发送秒杀请求执行秒杀
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                seckillBox.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    seckillBox.show();

                }else{
                    //未开启秒杀（每个人的机器时间可能和服务器的时间不一样）
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.myCountDown(seckillId, now, start, end);
                }
            }else {
                console.log("result:"+result);
            }
        });

    },
    //验证手机号
    validatePhone: function (phone) {
        return phone && phone.length == 11 && !isNaN(phone);
    },
    //时间判断,记时交互
    myCountDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $("#seckill-box");
        if (nowTime > endTime) {
            seckillBox.html('秒杀结束！');
        }else if(nowTime < startTime){
            //秒杀未开始，记时事件绑定
            var killTime = new Date(startTime+1000);//加1秒为了防止计时过程中的时间偏移
            console.log(killTime);
            seckillBox.countdown(killTime,function (event) {
                //时间格式
                var format =event.strftime("秒杀倒计时:%D天 %H时 %M分 %S秒");
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                //获取秒杀地址，控制实现逻辑，控制秒杀
                seckill.handlerSeckill(seckillId,seckillBox);
            })
        }else{
            //秒杀开始
            seckill.handlerSeckill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登陆，记时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //没有通过验证则绑定phone
                //控制modal输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    //显示弹出层
                    show: true,
                    backdrop: 'static',//禁止位置关闭（禁止点击modal外的区域隐藏modal）
                    keyboard: false //关闭键盘事件（禁止按esc推出modal）
                });

                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    console.log("inputPhone=" + inputPhone);//TODO
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie("killPhone", inputPhone, {expires: 7, path: "/seckill"});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

            //已经登陆
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            //记时交互
            $.get(seckill.URL.now(),function (result) {//获取系统当前时间
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断,记时交互
                    seckill.myCountDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log(result);
                }
            })


        }
    }
}