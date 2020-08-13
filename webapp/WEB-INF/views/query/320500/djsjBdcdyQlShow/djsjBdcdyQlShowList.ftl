<@com.html title="不动产登记业务管理系统" import="ace,public,multiselect">
<link href="static/css/djsjBdcdyQlShow.css" rel="stylesheet" type="text/css"/>
<div class="main-container" id="draggable">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="page-content">
        <div class="row">
            <div class="tabbable">
                <#include "djsjBdcdyTab.ftl">
                <div class="tab-content">
                    <#--搜索栏-->
                    <#include "djsjBdcdySearch.ftl">
                </div>
            </div>
        </div>
    </div>
    <#--高级搜索框-->
    <#include "djsjBdcdyGjSearch.ftl">
    <!--错误提示-->
    <div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
        <div class="modal-dialog tipPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">
                    <button type="button" id="tipHide" class="proHide"><i
                            class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <div id="csdjAlertInfo"></div>
                    <div id="csdjConfirmInfo"></div>
                </div>
                <div id="footer" class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <form id="form" hidden="hidden">
        <input type="hidden" id="djlx" name="djlx">
        <input type="hidden" id="bdclxdm" name="bdclxdm">
        <input type="hidden" id="fwid" name="fwid">
        <input type="hidden" id="tdid" name="tdid">
        <input type="hidden" id="dah" name="dah">
        <input type="hidden" id="lqid" name="lqid">
        <input type="hidden" id="cqid" name="cqid">
        <input type="hidden" id="djId" name="djId">
        <input type="hidden" id="bdcdyh" name="bdcdyh">
        <input type="hidden" id="workFlowDefId" name="workFlowDefId">
        <input type="hidden" id="sqlx" name="sqlxMc">
        <input type="hidden" id="xmmc" name="xmmc">
        <input type="hidden" id="tdzh" name="tdzh"/>
        <input type="hidden" id="ppzt" name="ppzt"/>
        <input type="hidden" id="dyid" name="dyid"/>
        <input type="hidden" id="ygid" name="ygid"/>
        <input type="hidden" id="cfid" name="cfid"/>
        <input type="hidden" id="yyid" name="yyid"/>
        <input type="hidden" id="gdproid" name="gdproid"/>
        <input type="hidden" id="mulGdfw" name="mulGdfw"/>
        <input type="hidden" id="djIds" name="djIds"/>
        <input type="hidden" id="bdcdyhs" name="bdcdyhs"/>
        <input type="hidden" id="tdids" name="tdids"/>
        <input type="hidden" id="qlid" name="qlid"/>
        <input type="hidden" id="qlzt" name="qlzt"/>
        <input type="hidden" id="gdproids" name="gdproids"/>
        <input type="hidden" id="qlids" name="qlids"/>
        <input type="hidden" id="qlr"/>
        <input type="hidden" id="ybdcqzh" name="ybdcqzh"/>
        <input type="hidden" id="proids" name="proid"/>
        <input type="hidden" id="bdcid" name="bdcid"/>
    </form>
    <div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
    <#--无用div 防止ace报错-->
    <div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<script>
        var bdcdjUrl = "${bdcdjUrl!}";
        var reportUrl = "${reportUrl!}";
        var portalUrl ="${portalUrl!}";
        var platformUrl = "${platformUrl!}";
        var zdtzm = "${zdtzm!}";
        var dyfs = "${dyfs!}";
        var bdclxdm = "${bdclxdm!}";
        var qlxzdm = "${qlxzdm!}";
        var yqllxdm = "${yqllxdm!}";
        var proid = "${proid!}";
        var wiid = "${wiid!}";
        var ysqlxdm = "${ysqlxdm!}";
        var glzs ="${glzs!}";
        var sflw ="${sflw!}";
        var sqlxmc="${sqlxmc!}";
        var workFlowDefId ="${workFlowDefId!}";
        var djlx ="${djlx!}";
        var cfsqlx ="${sqlx!}";
        var plChoseOne ="${plChoseOne!}";
        var glbdcdy ="${glbdcdy!}";
        var sqlxdm ="${sqlxdm!}";
        var bdcdyly ="${bdcdyly!}";
        var qllx = "${qllx!}";
        var showOptimize = "${showOptimize!}";
        var etlUrl = "${etlUrl!}";
        var notShowCk = "${notShowCk!}";
</script>
<script src="static/js/${dwdm!}/djsjBdcdyQlShowList.js" type="text/javascript"></script>
</@com.html>