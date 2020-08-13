<@com.html title="缮证" import="ace">
<style type="text/css">
    .sidebar {
        width: 250px;
    }
    .main-content {
        margin-left: 250px;
    }
    .no-skin .nav-list>li.open:after {
        display: block;
        content: "";
        position: absolute;
        right: -2px;
        top: -1px;
        bottom: 0;
        z-index: 1;
        border: 2px solid;
        border-width: 0 2px 0 0;
        border-color: #2b7dbc;
    }
</style>
<script>
    $(function(){
        $("body").addClass("no-skin");
        mainContainerHeight();

        //菜单处理逻辑
        $("li > a").click(function(){
            //清除所有选中
            $("li").removeClass("active");
            //判定如果没有子菜单的直接赋予选中效果
            if(!$(this).parent().hasClass("hsub")){
                $(this).parent().addClass("active");
            }
            //判定如果有子菜单并关闭的直接赋予选中效果
            if($(this).parent().hasClass("open")){
                $(this).parent().addClass("active");
            }
            //判定如果选择没有子菜单的 关闭其他子菜单并去掉选中效果
            if(!$(this).parent().parent().hasClass("submenu") && !$(this).parent().hasClass("hsub")){
                  $("li.hsub").removeClass("open").removeClass("active");
                  $("ul.submenu").hide();
            }
        })
    })
    $(window).resize(function(){
        mainContainerHeight();
    })
    var djbid = $("#djbid").val();
    function ylzs() {
        $("#rightiframe").attr("src", "${bdcdjUrl!}/bdcZsResource?proid=${proid!}")
    }
    function bdcdjb() {
        $("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}");
    }

    function mainContainerHeight() {
        var winHeight = $(window).height();
        var containerHieght = 0;
        if (winHeight > 0) {
            containerHieght = winHeight - 60;
        }
        $(".main-container,.sidebar,.main-content").css({height:containerHieght + "px"});
    }
    function zdzh() {
        $("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdc_zdxx.cpt&op=write&djbid=${djbid!}")
    }
    function bdcdyml() {
        $("#rightiframe").attr("src", "${bdcdjUrl!}/bdcqldj?djbid=${djbid!}");
    }
    function qlxx() {
        $("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdc_fdcq.cpt&op=write&bdcdyh=${bdcdyh!}")
    }
</script>
<input type="hidden" id="djbid" value="${djbid!}">
<div id="navbar" class="navbar navbar-default">
    <div class="navbar-container" id="navbar-container">
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>
        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <small>
                    <i class="fa fa-pencil-square-o"></i>
                    <span>缮证</span>
                </small>
            </a>
        </div>
    </div>
    <!-- /.navbar-container -->
</div>
<div class="main-container">
    <div id="sidebar" class="sidebar responsive">
            <ul class="nav nav-list" style="top: 0px;">
                <li class="hsub">
                    <a href="#" class="dropdown-toggle" onclick="bdcdjb()">
                        <i class="menu-icon fa fa-book"></i>
                        <span class="menu-text"> 不动产登记簿 </span>
                        <b class="arrow fa fa-angle-down"></b>
                    </a>
                    <b class="arrow"></b>
                    <ul class="submenu active nav-show" style="display: block;">
                        <li class="">
                            <a href="#" onclick="zdzh()">
                                <i class="menu-icon fa fa-file"></i>
                                <span class="menu-text"> 宗地/宗海基本信息 </span>
                            </a>
                            <b class="arrow"></b>
                        </li>
                        <li class="">
                            <a href="#" onclick="bdcdyml()">
                                <i class="menu-icon fa fa-file"></i>
                                <span class="menu-text"> 不动产权利登记目录 </span>
                            </a>
                            <b class="arrow"></b>
                        </li>
                        <li class="">
                            <a href="#" onclick="qlxx()">
                                <i class="menu-icon fa fa-file"></i>
                                <span class="menu-text"> 不动产权利登记信息
                                    </span>
                            </a>
                            <b class="arrow"></b>
                        </li>
                    </ul>
                </li>

                <li class="">
                    <a href="#" onclick="ylzs()">
                        <i class="menu-icon fa fa-file"></i>
                        <span class="menu-text"> 不动产权证书/证明 </span>
                    </a>
                    <b class="arrow"></b>
                </li>

            </ul><!-- /.nav-list -->
            <!-- #section:basics/sidebar.layout.minimize -->
            <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
                <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
            </div>
        </div>
    <div class="main-content">
        <iframe id="rightiframe"  name="mainFrame" src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}" width=100% height=100% style="border:0" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0">
    </div>
</div>
<!--  带属性的面板  -->
</@com.html>
