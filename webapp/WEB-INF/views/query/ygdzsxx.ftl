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
    <!--end   自适应高度    -->
</script>
<div class="main-container">
    <div class="page-content" id="mainContent" align="center">
        <div class="row">
            <div align="center" style="width: 800px">
                <section>
                    <#if "${bdclx!}"=="TD">
                        <div class="page-header">
                            <h1>纯土地证</h1>
                        </div>
                        <iframe id='xmiframe'
                                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_tdsyq.cpt&op=write&qlid=${qlid!}"
                                frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
                                allowtransparency="yes" style="width: 650px;height:200px"></iframe>
                    </#if>
                    <#if "${bdclx!}"=="TDFW">
                        <div class="page-header">
                            <h1>房产证</h1>
                        </div>
                        <iframe id='xmiframe'
                                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fwsyq.cpt&op=write&qlid=${qlid!}&bdcid=${bdcid!}"
                                frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
                                allowtransparency="yes" style="width: 685px;height:200px"></iframe>
                    </#if>
                    <#if "${bdclx!}"=="TDQT">
                        <iframe id='xmiframe'
                                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&proid=${id!}"
                                frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
                                allowtransparency="yes" style="width: 685px;height:450px"></iframe>
                    </#if>
                    <#if "${bdclx!}"=="TDSL">
                        <iframe id='xmiframe'
                                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&proid=${id!}"
                                frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
                                allowtransparency="yes" style="width: 685px;height:450px"></iframe>
                    </#if>
                </section>
                <#if "${isdy!}"=="true">
                <section>
                    <div class="page-header">
                        <h1>抵押信息</h1>
                    </div>
                    <iframe id='dyiframe'
                            src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_dy.cpt&op=write&qlid=${dyid!}&bdcid=${bdcid!}"
                            frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
                            allowtransparency="yes" style="width: 685px;height:450px"></iframe>
                </section>
                </#if>
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
