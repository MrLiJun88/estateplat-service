<@com.html title="权利房屋土地信息" import="ace">
<style>
    iframe {
        position: relative;
    }

    .col-xs-11 {
        width: 100%;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    #qllx {
        width: 150px;
        height: 40px;
        font-size: 18pt;
        font-family: "微软雅黑", serif;
        font-weight: 500;
        color: #000000;
        border-left: 0;
        border-top: 0;
        border-right: 0;
        border-bottom: 1px;
    }

    #infoIframe {
        width: 700px;
        height: 450px
    }

    #infoTab {
        width: 800px;
    }

</style>
<script type="text/javascript">
    $(function () {
        var infoId = $("#infoId").val();
        var info = $("#info").val();
        var proid = $("#proid").val();
        var cptName = $("#cptName").val();
        var url = "${reportUrl!}/ReportServer?reportlet=edit%2F" + cptName + ".cpt&op=write";
        if (info == "FW") {
            url += "&editFlag=false&fwid=" + infoId;
        } else if (info == "TD") {
            url += "&tdid=" + infoId;
        } else if (info == "QL") {
            url += "&qlid=" + infoId;
        }
        $("#infoIframe").attr("src", url);
        $("#infoTab").addClass("active");
    })
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-11">
                <div class="tab">
                    <div class="tab-content" align="center">
                        <#if "${info!}"=="QL">
                            <span id="qllx">
                            ${qllx!}
                            </span>
                        </#if>
                        <hr/>
                        <div id="infoTab" class="tab-pane" align="center">
                            <iframe id='infoIframe' src="" frameborder="no" allowtransparency="yes" marginwidth="0"
                                    marginheight="0" scrolling="no">
                            </iframe>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="infoId" name="infoId" value="${infoId!}">
    <input type="hidden" id="info" name="info" value="${info!}">
    <input type="hidden" id="proid" name="proid" value="${proid!}">
    <input type="hidden" id="cptName" name="cptName" value="${cptName!}">
</form>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
