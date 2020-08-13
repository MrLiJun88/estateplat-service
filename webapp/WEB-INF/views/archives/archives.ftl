<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    body {
        overflow-y: hidden;
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

})

$(window).resize(function () {
    mainContainerHeight();
})

function mainContainerHeight() {
    var winHeight = $(window).height();
    $("#iframe").css({height:winHeight + "px"});
}



</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <iframe src="${archivesUrl!}" id="iframe"
                style="border:0;width: 100%;position: fixed;" frameborder="0" scrolling="auto" marginwidth="0"
                marginheight="0">
        </iframe>
    </div>

</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>

</@com.html>


