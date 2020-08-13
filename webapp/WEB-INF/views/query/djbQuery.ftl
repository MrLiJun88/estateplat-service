<@com.html title="不动产登记业务管理系统" import="ace">
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
    function zdzh() {
        $("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdc_zdxx.cpt&op=write&djbid=${djbid!}")
    }
    function bdcdjb() {
        <#--$("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}")-->
        $("#rightiframe").attr("src", "${bdcdjUrl!}/bdcDjb/djbTab?djbid=${djbid!}");
    }
    function bdcdyml() {
        <#--$("#rightiframe").attr("src", "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdyml.cpt&op=write&djbid=${djbid!}")-->
        $("#rightiframe").attr("src", "${bdcdjUrl!}/bdcqldj?djbid=${djbid!}");
    }
    /*
    function bdcdy(bdcdyh) {
        url="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdydjxx.cpt&op=write&djbid=${djbid!}&bdcdyh="+bdcdyh;
        $("#rightiframe").attr("src", url);
    }*/
    function bdcdyOrLpb(url) {
        $("#rightiframe").attr("src", url);
    }
  /*  function quanli(bdcdyh, tableName) {
        tableName=tableName.toLocaleLowerCase();
        tableName=tableName+'_q';
        url = "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2F"+tableName+".cpt&op=write&bdcdyh=" + bdcdyh;
        $("#rightiframe").attr("src", url)
    }*/
    function mainContainerHeight() {
        var winHeight = $(window).height();
        var containerHieght = 0;
        if (winHeight > 0) {
            containerHieght = winHeight - 60;
        }
        $(".main-container,.sidebar,.main-content").css({height:containerHieght + "px"});
    }
    function frdjb(){
        var url = "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}"
        $("#rightiframe").attr("src", url);
    }
    function frzdxx(){
        var url;//
        var zdtzm="${zdtzm!}";
        if(zdtzm.indexOf("GH")>-1||zdtzm.indexOf("GG")>-1){
            url = "${reportUrl!}/ReportServer?reportlet=edit%2Fbdc_zhxx.cpt&op=write&djbid=${djbid!}"
        }else{
             url = "${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdc_zdxx.cpt&op=write&djbid=${djbid!}"
        }

        $("#rightiframe").attr("src", url);
    }
    function frdjml(){
        var url = "${reportUrl}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjml.cpt&op=form&djbid=${djbid!}"
        $("#rightiframe").attr("src", url);
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
                    <span>${title!}</span>
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
                    <a href="#" class="dropdown-toggle">
                        <i class="menu-icon fa fa-list"></i>
                        <span class="menu-text">不动产登记簿</span>

                        <b class="arrow fa fa-angle-down"></b>
                    </a>
                    <b class="arrow"></b>
                    <ul class="submenu">
                        <li >
                            <a id="li_djb" href="#" onclick="frdjb()">
                                <i class="menu-icon fa fa-list"></i>
                                登记簿
                            </a>
                        </li>
                        <b class="arrow"></b>
                        <li >
                            <a id="li_zdxx" href="#" onclick="frzdxx()">
                                <i class="menu-icon fa fa-list"></i>
                                宗地/宗海基本信息
                            </a>
                        </li>
                        <b class="arrow"></b>
                        <li >
                            <a id="li_djml" href="#" onclick="bdcdyml()">
                                <i class="menu-icon fa fa-list"></i>
                                权利登记目录
                            </a>
                        </li>
                    </ul>

                </li>

               <#-- <li class="">
                    <a href="#" onclick="zdzh()">
                        <i class="menu-icon fa fa-file"></i>
                        <span class="menu-text"> 宗地/宗海基本信息 </span>
                    </a>
                    <b class="arrow"></b>
                </li>-->
            <#-- <li class="">
               <a href="#" onclick="bdcdyml()">
                   <i class="menu-icon fa fa-file"></i>
                   <span class="menu-text"> 不动产权利登记目录 </span>
               </a>
               <b class="arrow"></b>
           </li> -->
                <#if list?size!=0 >
                    <#list list as a>
                        <#if a.URL?exists>
                            <li class="">
                                <a href="#" onclick="bdcdyOrLpb('${a.URL!}')">
                                    <i class="menu-icon fa fa-list"></i>
                                    <span class="menu-text"> ${a.MC!} </span>
                                </a>
                                <b class="arrow"></b>
                            </li>
                        <#else>
                            <li class="hsub">
                                <a href="#" class="dropdown-toggle">
                                    <i class="menu-icon fa fa-list"></i>
                                    <span class="menu-text"> ${a.MC!} </span>

                                    <b class="arrow fa fa-angle-down"></b>
                                </a>

                                <b class="arrow"></b>

                                <ul class="submenu">
                                    <#if a.LJZ??>
                                        <#list a.LJZ as b>
                                            <li class="">
                                                <a href="#" onclick="bdcdyOrLpb('${b.URL!}')">
                                                    <i class="menu-icon fa fa-caret-right"></i>
                                                ${b.FWMC!}
                                                </a>
                                                <b class="arrow"></b>
                                            </li>
                                        </#list>
                                    </#if>
                                </ul>
                            </li>
                        </#if>
                    </#list>
                </#if>
            </ul><!-- /.nav-list -->
            <!-- #section:basics/sidebar.layout.minimize -->
            <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
                <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
            </div>
        </div>
    <div class="main-content">
        <iframe id="rightiframe"  name="mainFrame" src="${bdcdjUrl!}/bdcqldj?djbid=${djbid!}" width=100% height=100% style="border:0" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0">
        <#--<iframe id="rightiframe"  name="mainFrame" src="${reportUrl!}/ReportServer?reportlet=bdcdj_djb%2Fbdcdjb.cpt&op=write&djbid=${djbid!}" width=100% height=100% style="border:0" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0">-->
    </div>

</div>
<!--  带属性的面板  -->
</@com.html>
