<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    body {
        overflow-y: hidden;
    }

    .btn-group-vertical{
        width: 150px;
    }

    .col-xs-11{
        width: 100%;
        height: 100%;
    }

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width: 100% !important;
    }


    .form .row {
        margin: 0px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: left;
    }
     label {
        font-weight: bold;
        font-size: 15px;
        font-family: 宋体;
    }
    ul li{
       width: 80px;
    }
    select{
        font: 15px 宋体;
        height: 26px;
    }
    .form-group>div[class*="col-"] {
        padding-top: 4px;
        margin-bottom: 4px;
    }
    .tab-content{
        overflow-y: auto;
        box-shadow: rgba(0, 0, 0, 0) 0px 0px 0px 0px;
        border-right: hidden;
        border-bottom: hidden;
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

    $("#selGgmc").bind("change",function(){
        var selectGgmc = $('#selGgmc option:selected') .text();
        var fileName=$(this).val();//eg:bdcdjScdjgg,bdcdjDjbgzgg...
        $.ajax({
            type: "GET",
            url:  "${bdcdjUrl!}/bdcgg/getGglxdmByGgmc?ggFtl=" + fileName ,
            success: function (result) {
                if(result != '' && result != null){
                    report('${bdcdjUrl!}/bdcdjGg?wiid=${wiid}&proid${workflowProid}&fileName='+fileName+"&ggzdDm="+ result+'&ggmc='+selectGgmc+$("#form").serialize());
                }
            }
        });
    });

    $(".btn-group > button").click(function () {
        //去掉选中效果
        $(this).siblings().removeClass("btn-primary");
        //添加选中效果
        $(this).addClass("btn-primary");

    });

    //时间重置
    function dateReset(){
        $("#form")[0].reset();
    }


    //标签页点击事件
    $("#Tab").click(function(){
        report('${reportUrl!}/ReportServer?reportlet=edit%2F'+$("#gglx").val()+'.cpt&op=write'+"&wiid="+"${wiid}"+"&proid="+"${workflowProid}"+ "&" + $("#form").serialize() );
    })

    //发布公告(待完善)
    $("#announcement").click(function(){
        var postInfo = "公告期间流程挂起";
        $.ajax({
            url:"${platformUrl!}/task!post.action?a=1&taskid=" + "${taskid}",
            type:'post',
            data:{remark:postInfo},
            success:function(msg){
                tipInfo("流程挂起成功");
            }
        });

    })


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
            $("#Tab").addClass("active");
        }
);


</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">

        <div class="row">
            <div class="form-group">
                <label class="col-xs-1 control-label " style="width:96px">公告类型</label>
                <div class="col-xs-3">
                    <select id="selGgmc" style="">
                        <#list gglist as gg>
                            <option value="${gg.ftl!}">${gg.gglx!}</option>
                        </#list>
                    </select>
                </div>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-primary" id="announcement">发布公告</button>
            </div>
            <div class="col-xs-11">
                <div class="tabbable">
                    <#--<ul class="nav nav-tabs" id="myTab">-->
                        <#--<li class="active">-->
                            <#--<a data-toggle="tab" id="Tab" href="#">-->

                            <#--</a>-->
                        <#--</li>-->
                    <#--</ul>-->
                    <div class="tab-content">
                        <iframe src="${src!}" id="iframe"
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
    <input type="hidden" id="gglx" name="gglx">
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>

</@com.html>


