<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    .page-header {
        margin: 0px;
        padding: 0px;
        text-align: center;
        border-bottom: 0px;
    }

    .page-content {
        padding: 0px 12px 24px;
    }

    body {
        background-color: #FFFFFF;
    }

    .headTab {
        position: fixed;
        top: 0px;
    }

    li.ui-menu-item.active  a, .headTab li.active a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    li.ui-menu-item.active  a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    .imgClass {
        max-width: 1000px;
        myimg: expression(onload=function(){
            this.style.width=(this.offsetWidth > 500)?"500px":"auto"}
        );
    }
</style>
<script type="text/javascript">
    <!--   自适应高度    -->
    $(document).ready(function () {
        loadLeftMenu();
        $("body").attr("data-spy", "scroll").attr("data-target", ".bs-docs-sidebar");

        $("#zhxx,#zhwzt,#zhjzt").click(function () {
            if (this.id == "zhxx") {
                $("#zhxx").show();
                $("#zhwzt").hide();
                $("#zhjzt").hide();
            } else if (this.id == "zhwzt"){
                $("#zhjzt").hide();
                $("#zhxx").hide();
                $("#zhwzt").show();
            }else {
                $("#zhwzt").hide();
                $("#zhxx").hide();
                $("#zhjzt").show();
            }
        })
    });

    $(window).resize(function () {
        encodeURI()
        loadLeftMenu();
    });

    function loadLeftMenu() {
        var winHeight = $(window).height();
        var leftMenuHieght = 0;
        if (winHeight > 0) {
            leftMenuHieght = winHeight - 40;
        }
        $(".content").css({height:leftMenuHieght + "px"});
    }
    <!--end   自适应高度    -->
</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">


            <div id="zd">
                <div class="bs-docs-sidebar" style="position: fixed;top: 15px;right:20px">
                    <ul id="menu"
                        class=" nav  bs-docs-sidenav affix-top ui-menu ui-widget ui-widget-content ui-corner-all">
                        <li class="ui-menu-item">
                            <a href="#zhxx" class="ui-corner-all" tabindex="-1" role="menuitem">宗海信息</a>
                        </li>

                    <#--<li class="ui-menu-item">-->
                    <#--<a href="#zdbsb" class="ui-corner-all" tabindex="-1" role="menuitem"> 宗地标示表</a>-->
                    <#--</li>-->
                        <li class="ui-menu-item">
                            <a href="#zhwzt" class="ui-corner-all" tabindex="-1" role="menuitem"> 宗海位置图</a>
                        </li>
                        <li class="ui-menu-item">
                            <a href="#zhjzt" class="ui-corner-all" tabindex="-1" role="menuitem"> 宗海界址图</a>
                        </li>
                    </ul>
                </div>
                <div>
                    <section id="zhxx">
                        <div class="row-fluid" align="center">
                            <iframe name="myModalFrame"
                                    src="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_hjdcb.cpt&op=write&proid=${proid}&bdcdyh=${bdcdyh!}"
                                    width=670px height="1500px"
                                    class="iframeStyle" frameborder="0"></iframe>
                        </div>
                    </section>
                <#--<section id="zdbsb">-->
                <#--<div class="page-header">-->
                <#--<h1>界址标示表</h1>-->
                <#--</div>-->
                <#--<div class="row-fluid" align="center">-->
                <#--<iframe id="bsbModalFrame" name="bsbModalFrame"-->
                <#--src="${bdcdjUrl!}/dcxx/jzbsb?proid=${proid}&bdcdyh=${bdcdyh!}" width=637px height="550px"-->
                <#--class="iframeStyle" frameborder="0"></iframe>-->
                <#--</div>-->
                <#--</section>-->
                    <section id="zhwzt">
                        <div class="page-header">
                            <h1>宗海位置图</h1>
                        </div>
                        <div class="row-fluid" align="center">
                            <img src="${seaUrl!}/dcxx/selectZht?proid=${proid}&bdcdyh=${bdcdyh!}&id=zhwzt"
                                 class="imgClass" alt="无宗海位置图"/>
                        </div>
                    </section>

                    <section id="zhjzt">
                        <div class="page-header">
                            <h1>宗海界址图</h1>
                        </div>
                        <div class="row-fluid" align="center">
                            <img src="${seaUrl!}/dcxx/selectZht?proid=${proid}&bdcdyh=${bdcdyh!}&id=zhjzt"
                                 class="imgClass" alt="无宗海界址图"/>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--end  面板操作  -->
</@com.html>

<#--
<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    .page-header {
        margin: 0px;
        padding: 0px;
        text-align: center;
        border-bottom: 0px;
    }

    .page-content {
        padding: 0px 12px 24px;
    }

    body {
        background-color: #FFFFFF;
    }

    .headTab {
        position: fixed;
        top: 0px;
    }

    li.ui-menu-item.active  a, .headTab li.active a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    li.ui-menu-item.active  a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    .imgClass {
        max-width: 1000px;
        myimg: expression(onload=function(){
            this.style.width=(this.offsetWidth > 500)?"500px":"auto"}
        );
    }
</style>
<script type="text/javascript">
    <!--   自适应高度    &ndash;&gt;
    $(document).ready(function () {
        loadLeftMenu();
        $("body").attr("data-spy", "scroll").attr("data-target", ".bs-docs-sidebar");

        $("#fwTab,#zdTab").click(function () {
            if (this.id == "fwTab") {
                $("#fw").show();
                $("#zd").hide();
            } else {
                $("#fw").hide();
                $("#zd").show();
            }
        })
    });

    $(window).resize(function () {
        encodeURI()
        loadLeftMenu();
    });

    function loadLeftMenu() {
        var winHeight = $(window).height();
        var leftMenuHieght = 0;
        if (winHeight > 0) {
            leftMenuHieght = winHeight - 40;
        }
        $(".content").css({height:leftMenuHieght + "px"});
    }
    <!--end   自适应高度    &ndash;&gt;
</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">


            <div id="zd">
                <div class="bs-docs-sidebar" style="position: fixed;top: 15px;right:20px">
                    <ul id="menu"
                        class=" nav  bs-docs-sidenav affix-top ui-menu ui-widget ui-widget-content ui-corner-all">
                        <li class="ui-menu-item">
                            <a href="#zhxx" class="ui-corner-all" tabindex="-1" role="menuitem">宗海信息</a>
                        </li>

                        &lt;#&ndash;<li class="ui-menu-item">&ndash;&gt;
                            &lt;#&ndash;<a href="#zdbsb" class="ui-corner-all" tabindex="-1" role="menuitem"> 宗地标示表</a>&ndash;&gt;
                        &lt;#&ndash;</li>&ndash;&gt;
                        <li class="ui-menu-item">
                            <a href="#zdt" class="ui-corner-all" tabindex="-1" role="menuitem"> 宗海图</a>
                        </li>
                    </ul>
                </div>
                <div>
                    <section id="zhxx">
                        <div class="row-fluid" align="center">
                            <iframe name="myModalFrame"
                                    src="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_hjdcb.cpt&op=write&proid=${proid}&bdcdyh=${bdcdyh!}"
                                    width=670px height="1500px"
                                    class="iframeStyle" frameborder="0"></iframe>
                        </div>
                    </section>
                    &lt;#&ndash;<section id="zdbsb">&ndash;&gt;
                        &lt;#&ndash;<div class="page-header">&ndash;&gt;
                            &lt;#&ndash;<h1>界址标示表</h1>&ndash;&gt;
                        &lt;#&ndash;</div>&ndash;&gt;
                        &lt;#&ndash;<div class="row-fluid" align="center">&ndash;&gt;
                            &lt;#&ndash;<iframe id="bsbModalFrame" name="bsbModalFrame"&ndash;&gt;
                                    &lt;#&ndash;src="${bdcdjUrl!}/dcxx/jzbsb?proid=${proid}&bdcdyh=${bdcdyh!}" width=637px height="550px"&ndash;&gt;
                                    &lt;#&ndash;class="iframeStyle" frameborder="0"></iframe>&ndash;&gt;
                        &lt;#&ndash;</div>&ndash;&gt;
                    &lt;#&ndash;</section>&ndash;&gt;
                    <section id="zdt">
                        <div class="page-header">
                            <h1>宗海图</h1>
                        </div>
                        <div class="row-fluid" align="center">
                            <img src="${bdcdjUrl!}/dcxx/selectZdt?proid=${proid}&bdcdyh=${bdcdyh!}"
                                 class="imgClass" alt="无宗海图"/>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>

&lt;#&ndash;无用div 防止ace报错&ndash;&gt;
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--end  面板操作  &ndash;&gt;
</@com.html>
-->
