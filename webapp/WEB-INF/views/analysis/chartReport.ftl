<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    body {
        overflow-y: hidden;
    }

    .no-padding-right {
        text-align: right;
    }

        /*高级搜索的样式修改*/
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }

    .btn-group-vertical{
        width: 150px;
    }

    .col-xs-11{
        width: 85%;
        height: 85%;
    }

    /*.tab-content{*/
        /*border: 0px;*/
        /*box-shadow:0px;*/
        /*overflow-y:hidden;*/
    /*}*/

        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;

        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width: 100% !important;
    }

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: left;
    }

    label {
        font-weight: bold;
    }
</style>
<script>
    $(function () {
        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject) {

            if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
                $(window).resize(function () {
                    $.each($(".moveModel > .modal-dialog"), function () {
                        $(this).css("top", "-600px");
                        $(this).css("left", ($(window).width() - $(this).width()) / 2);

                    })
                })
            }
        }

        $(".tab-content").css("height",window.screen.height*0.6+"px")
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
                    $(this).prev().focus();
                });

        mainContainerHeight();

        //绑定回车键
        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                $("#search").click();
            }
        });

        $("#search").click(function () {
            if($("#Tab1").attr("class")=="active"){
                $("#Tab1").click();
            }else{
                $("#Tab2").click();
            }
            /*$("#iframe").attr("src", $(".btn-group > button.btn-primary").attr("url") + "&" + $("#form").serialize());*/
        });


        //高级搜索
        $("#gjSearchBtn").click(function () {
            if($("#Tab1").attr("class")=="active"){
                $("#Tab1").click();
            }else{
                $("#Tab2").click();
            }
           // $("#iframe").attr("src", $(".btn-group > button.btn-primary").attr("url") + "&" + $("#gjSearchForm").serialize());
            $("#proHide").click();
        });

        $(".btn-group > button").click(function () {
            //清空高级查询表单数据
            $("#gjSearchForm")[0].reset();
            //去掉选中效果
            $(this).siblings().removeClass("btn-primary");
            //添加选中效果
            $(this).addClass("btn-primary");
            /*//将对应的下拉选择框禁掉
            var selectId = $(this).attr("id") + "Sel";
            $("select").attr("disabled", false);
            $("#" + selectId).attr("disabled", true);*/
        });

        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        });

        //将统计数据导出excel
        $("#export").click(function () {
            (function(win, doc){
                var ifr = doc.getElementById('iframe').contentWindow;
                var cb = function(json){
                    eval(json);
                };
                var sendMessage = function(){
                    if(win.postMessage){
                        if (win.addEventListener) {
                            win.addEventListener("message",function(e){
                                cb.call(win,e.data);
                            },false);
                        }else if(win.attachEvent) {
                            win.attachEvent("onmessage",function(e){
                                cb.call(win,e.data);
                            });
                        }
                        return function(data){
                            ifr.postMessage(data,'*');
                        };
                    }else{
                        var hash = '';

                        setInterval(function(){

                            if (win.name !== hash) {
                                hash = win.name;
                                cb.call(win, hash);
                            }
                        }, 200);
                        return function(data){
                            ifr.name = data;
                        };
                    }
                };
                win.sendMessage = sendMessage();
            })(window, document);

            send("_g().exportReportToExcel('simple')");
        });

        //时间重置
        function dateReset(){
            $("#form")[0].reset();
        }

        //导航栏点击事件
        $("#bjtj").click(function(){
            dateReset();
            $("#daohang").val("bjtj");
            $("#Tab1").html("登记类型");
            $("#Tab1").click();
            $("#Tab2").html("权利类型");
            $("#Tab2").show();

        })

        $("#djtj").click(function(){
            dateReset();
            $("#daohang").val("djtj");
            $("#Tab1").html("不动产类型统计");
            $("#Tab1").click();
            $("#Tab2").hide();

        })

        $("#zstj").click(function(){
            dateReset();
            $("#daohang").val("zstj");
            $("#Tab1").html("登记类型");
            $("#Tab1").click();
            $("#Tab2").html("权利类型");
            $("#Tab2").show();

        })
        $("#zmstj").click(function(){
            dateReset();
            $("#daohang").val("zmstj");
            $("#Tab1").html("登记类型");
            $("#Tab1").click();
            $("#Tab2").html("权利类型");
            $("#Tab2").show();

        })

        $("#zsbh").click(function(){
            dateReset();
            $("#daohang").val("zsbh");
            $("#Tab1").html("证书编号统计");
            $("#Tab1").click();
			$("#Tab2").html("证书编号明细");
            $("#Tab2").show();

        })

        $("#zmsbh").click(function(){
            dateReset();
            $("#daohang").val("zmsbh");
            $("#Tab1").html("证明书编号统计");
            $("#Tab1").click();
			$("#Tab2").html("证明书编号明细");
            $("#Tab2").show();

        })
        $("#zsyzhz").click(function(){
            dateReset();
            $("#daohang").val("zsyzhz");
            $("#Tab1").html("证书印制汇总");
            $("#Tab1").click();
            $("#Tab2").hide();
        })
        //标签页点击事件
        $("#Tab1").click(function(){

            $("#Tab1").addClass("active");
            if($("#daohang").val()=="bjtj"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_xm.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }else if($("#daohang").val()=="zstj"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&zstype=zs&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }else if($("#daohang").val()=="zmstj"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&zstype=zms&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }else if($("#daohang").val()=="zsbh"){
				report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbh.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
			}else if($("#daohang").val()=="zmsbh"){
				report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zmsbh.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
			}else if($("#daohang").val()=="zsyzhz"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbhhzb.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }else if($("#daohang").val()=="djtj"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_bdclx_dj.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }

        })
        $("#Tab2").click(function(){
            $("#Tab1").removeClass("active");
            if($("#daohang").val()=="bjtj"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_qllx_xm.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }else if($("#daohang").val()=="zstj"){
                /*report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&zstype=zms&&dwdm=${userDwdm!}')*/
               /*report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_qllx_xm.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize());*/
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_qllx_zs.cpt&op=write&zstype=zs&&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize())
            }else if($("#daohang").val()=="zmstj"){
                /*report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&zstype=zms&&dwdm=${userDwdm!}');*/
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_qllx_zs.cpt&op=write&zstype=zms&&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize())
            }else if($("#daohang").val()=="zsbh"){
				report('${reportUrl!}/ReportServer?reportlet=analysis%2Fbdc_zsyzmxb.cpt&op=form&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
			}else if($("#daohang").val()=="zmsbh"){
				report('${reportUrl!}/ReportServer?reportlet=analysis%2Fbdc_zmyzmxb.cpt&op=form&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
			}else if($("#daohang").val()=="zsyzhz"){
                report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbhhzb.cpt&op=write&dwdm=${userDwdm!}'+ "&" + $("#form").serialize() + "&" + $("#gjSearchForm").serialize());
            }
        })

        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
    })
    $(window).resize(function () {
        mainContainerHeight();
    })
    function mainContainerHeight() {
        var winHeight = $(window).height();
        var containerHieght = 0;
        if (winHeight > 0) {
            containerHieght = winHeight - 110;
        }
        $("#iframe").css({height:containerHieght + "px"});
    }
    function report(url) {
        //$("#form")[0].reset();
        $("#iframe").attr("src", url);
    }

    function send(msg) {
        sendMessage(msg);

    }

   $(document).ready(function(){
        $("#Tab1").addClass("active");
      }
   );


</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">
        <form class="form" id="form">
            <div class="row">

                <div class="form-group">
                    <label class="col-xs-1 control-label " style="width:96px">起始日期：</label>

                    <div class="col-xs-2">
                    <span class="input-icon">
                         <input type="text" class="date-picker form-control" name="beginDate"
                                data-date-format="yyyy-mm-dd">
                        <i class="ace-icon fa fa-calendar"></i>
                    </span>
                    </div>
                    <label class="col-xs-1 control-label"  style="width:96px">结束日期：</label>

                    <div class="col-xs-2">
                     <span class="input-icon">
                         <input type="text" class="date-picker form-control" name="endDate"
                                data-date-format="yyyy-mm-dd">
                        <i class="ace-icon fa fa-calendar"></i>
                    </span>
                    </div>
                    <button type="button" class="btn btn-sm btn-primary" id="search"><i
                            class="ace-icon fa fa-search bigger-130"></i>统计
                    </button>
                    <button type="button" class="btn01 AdvancedButton" id="show">高级统计</button>
                    <button type="button" class="btn01 AdvancedButton" id="export">导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出</button>
                </div>
            </div>
        </form>
        <hr>
        <div class="row">
            <div class="btn-group btn-group-vertical col-xs-1">
                <button id="bjtj" type="button" class="btn btn-sm-1 btn-primary"
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_xm.cpt&op=write&dwdm=${userDwdm!}"
                        <#--onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_xm.cpt&op=write&dwdm=${userDwdm!}')"-->>办件数量统计
                </button>
                <div class="space-4"></div>
                <button id="djtj" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_bdclx_dj.cpt&op=write&dwdm=${userDwdm!}"
                        onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_bdclx_dj.cpt&op=write&dwdm=${userDwdm!}')">登记情况统计
                </button>
                <#--<div class="space-4"></div>
                <button id="bdclx" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_bdclx_xm.cpt&op=write"
                        onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_bdclx_xm.cpt&op=write&dwdm=${userDwdm!}')">
                    不动产类型
                </button>-->
                <div class="space-4"></div>
                <button id="zstj" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&dwdm=${userDwdm!}&zstype=zs"
                        <#--onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&dwdm=${userDwdm!}')"-->>证书统计
                </button>
                <div class="space-4"></div>
                <button id="zmstj" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&dwdm=${userDwdm!}&zstype=zms"
                        <#--onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_zs.cpt&op=write&dwdm=${userDwdm!}')"-->>证明统计
                </button>
                <div class="space-4"></div>
                <button id="zsbh" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbh.cpt&op=write&dwdm=${userDwdm!}"
                        <#--onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbh.cpt&op=write&dwdm=${userDwdm!}')"-->>证书编号统计
                </button>
                <div class="space-4"></div>
                <button id="zmsbh" type="button" class="btn btn-sm-1 "
                        url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zmsbh.cpt&op=write&dwdm=${userDwdm!}"
                        <#--onclick="report('${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zmsbh.cpt&op=write&dwdm=${userDwdm!}')"-->>
                    证明书编号统计
                </button>
                <div class="space-4"></div>
                <button id="zsyzhz" type="button" class="btn btn-sm-1 " url="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_zsbhhzb.cpt&op=write&dwdm=${userDwdm!}">
                   证书印制汇总
                </button>
            </div>
            <div class="col-xs-11">
                <#--<iframe src="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_xm.cpt&op=write&dwdm=${userDwdm!}" id="iframe"
                        style="border:0;width: 100%;position: fixed; " frameborder="0" scrolling="auto" marginwidth="0"
                        marginheight="0"></iframe>-->
                    <div class="tabbable">
                        <ul class="nav nav-tabs" id="myTab">
                            <li class="active">
                                <a data-toggle="tab" id="Tab1" href="#">
                                    登记类型
                                </a>
                            </li>
                            <li>
                                <a data-toggle="tab" id="Tab2" href="#">
                                    权利类型
                                </a>
                            </li>

                        </ul>
                        <div class="tab-content">
                            <iframe src="${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_djlx_xm.cpt&op=write&dwdm=${userDwdm!}" id="iframe"
                                                         style="border:0;width: 100%;position: fixed; " frameborder="0" scrolling="auto" marginwidth="0"
                                                         marginheight="0">

                             </iframe>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
<div>
    <input type="hidden" id="daohang" value="bjtj">
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">

                    <#list bdcdjtjGjssOrderList as bdcdjtjGjss>
                        <#if bdcdjtjGjss == 'djlx'>
                            <#if (bdcdjtjGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登记类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="djlx" id="djlxSel" class="form-control">
                                    <option></option>
                                    <#list djList as djlx>
                                        <option value="${djlx.dm}">${djlx.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcdjtjGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjtjGjss=='qllx'>
                            <#if (bdcdjtjGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权利类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="qllx" id="qllxSel" class="form-control">
                                    <option></option>
                                    <#list qlList as qllx>
                                        <option value="${qllx.dm}">${qllx.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcdjtjGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjtjGjss=='bdclx'>
                            <#if (bdcdjtjGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="bdclx" class="form-control" id="bdclxSel">
                                    <option></option>
                                    <#list bdcList as bdclx>
                                        <option value="${bdclx.DM}">${bdclx.MC}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcdjtjGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        </#if>
                    </#list>

                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
</@com.html>


