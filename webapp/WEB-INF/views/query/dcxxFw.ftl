<@com.html title="不动产登记业务管理系统" import="ace,public">
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
    h1{
        margin-top: 0px;
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
        loadLeftMenu();
    });

    function loadLeftMenu() {
        var winHeight = $(window).height();
        var leftMenuHieght = 0;
        if (winHeight > 0) {
            leftMenuHieght = winHeight;
        }
        $(".content").css({height:leftMenuHieght + "px"});
    }
    <!--end   自适应高度    -->
</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">
            <div id="fw">
                <div class="bs-docs-sidebar" style="position: fixed;top: 15px;right:20px">
                    <ul id="menu1"
                        class=" nav  bs-docs-sidenav affix-top ui-menu ui-widget ui-widget-content ui-corner-all">
                        <li class="ui-menu-item ">
                            <a href="#fwxx" class="ui-corner-all" tabindex="-1" role="menuitem">房屋信息</a>
                        </li>
                        <li class="ui-menu-item">
                            <a href="#fcfht" class="ui-corner-all" tabindex="-1" role="menuitem">分层分户图</a>
                        </li>
                    </ul>
                </div>
                <div>
                    <section id="fwxx">
                        <div class="page-header">
                            <h1>房屋信息</h1>
                        </div>
                        <div class="row-fluid" align="center">
                            <iframe id="fwModalFrame" name="fwModalFrame" <#--onload="Javascript:onloadFr(this)"-->
                                    src="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_fwxx.cpt&op=write&proid=${proid}&bdcdyh=${bdcdyh!}"
                                    width="730px" height="400px"
                                    class="iframeStyle" frameborder="0" ></iframe>
                        </div>
                    </section>
                    <section id="fcfht">
                        <div class="page-header">
                            <h1>分层分户图</h1>
                        </div>
                        <div class="row-fluid" align="center">
                            <img src="${bdcdjUrl!}/dcxx/selectHst?proid=${proid}&bdcdyh=${bdcdyh!}"
                                 style="width:50%;height:50%"  class="imgClass" alt="无分层分户图"/>
                        </div>
                    </section>
                </div>
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
