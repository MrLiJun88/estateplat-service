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
        overflow: hidden;
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
            leftMenuHieght1 = winHeight - 40;
        }
        $(".content").css({height:leftMenuHieght1 + "px"});
    }
    <!--end   自适应高度    -->
</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row" style="margin-top: 50px">
            <div class="tabbable">
                <ul class="nav nav-tabs headTab">
                    <#if fwopen??><#if fwopen=="1">
                        <li class="active">
                            <a data-toggle="tab" id="fwTab" href="#" onclick="fw();">
                                房产
                            </a>
                        </li>
                    </#if></#if>
                    <#if lqopen??><#if lqopen=="1">
                        <li class="active">
                            <a data-toggle="tab" id="lqTab" href="#" onclick="lq();">
                                林权
                            </a>
                        </li>
                    </#if></#if>
                    <#if cbopen??><#if cbopen=="1">
                        <li class="active">
                            <a data-toggle="tab" id="cbTab" href="#" onclick="cb();">
                                承包
                            </a>
                        </li>
                    </#if></#if>
                    <#if zdopen??><#if zdopen=="1">
                        <li>
                            <a data-toggle="tab" id="zdTab" href="#" onclick="td();">
                                宗地
                            </a>
                        </li>
                    </#if></#if>
                </ul>

            </div>
            <div>
                <iframe id="fwModalFrame" name="fwModalFrame"
                        width=100% height="$(document).height()"
                        class="iframeStyle" frameborder="0"></iframe>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var height=  screen.availHeight-182;
        $("#fwModalFrame").height(height);
        var bdclx = '${bdclx!}';
        if (bdclx != null && bdclx != '') {
            var url = '${bdcdjUrl}/dcxx/queryfw?proid=${proid}&wiid=${wiid!}&bdcdyh=${bdcdyh!}&bdcdyid=${bdcdyid!}';
            if (bdclx == 'TD')
                url = "${bdcdjUrl}/dcxx/querytd?proid=${proid}&bdcdyh=${bdcdyh!}";
            else if (bdclx == 'TDSL')
                url = "${bdcdjUrl}/dcxx/queryLq?proid=${proid}&bdcdyh=${bdcdyh!}";
            else if (bdclx == 'TDQT')
                url = "${bdcdjUrl}/dcxx/queryCb?proid=${proid}&bdcdyh=${bdcdyh!}";
            $("#fwModalFrame").attr("src", url);
        }
    });
    function fw() {
        var url = "${bdcdjUrl}/dcxx/queryfw?proid=${proid}&wiid=${wiid!}&bdcdyh=${bdcdyh!}&bdcdyid=${bdcdyid!}" ;
        $("#fwModalFrame").attr("src", url);
    }
    function td() {
        var url = "${bdcdjUrl}/dcxx/querytd?proid=${proid}&bdcdyh=${bdcdyh!}";
        $("#fwModalFrame").attr("src", url);
    }
    function lq() {
        var url = "${bdcdjUrl}/dcxx/queryLq?proid=${proid}&bdcdyh=${bdcdyh!}";
        $("#fwModalFrame").attr("src", url);
    }
    function cb() {
        var url = "${bdcdjUrl}/dcxx/queryCb?proid=${proid}&bdcdyh=${bdcdyh!}" ;
        $("#fwModalFrame").attr("src", url);
    }
</script>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--end  面板操作  -->
</@com.html>
