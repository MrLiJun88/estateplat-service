<@com.html title="不动产登记多选页面" import="ace,public">
<link href="static/css/djsjBdcdyMulQlShow.css" rel="stylesheet" type="text/css"/>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">

    <div class="page-content" id="mainContent">
        <div class="row">
            <div class="tabbable">
                <#include "djsjBdcdyMulTab.ftl">
                <div class="tab-content">
                    <#include "djsjBdcdyMulSearch.ftl">
                    <div id="hhcfsj" class="tab-pane">
                        <iframe id="hhcf" width="100%" style="border-width: 0px;height:458px"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "djsjBdcdyMulGjSearch.ftl">
<#include "djsjBdcdyMulSelected.ftl">
<#include "yzdfSelect.ftl">
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo"></div>
                <div id="csdjConfirmInfo"></div>
            </div>
            <div id="tipFooter" class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>

<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="bdcdyTipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->不动产单元提示信息</h4>
                <button type="button" id="bdcdyTipHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <!--批量选择弹出提示条数过多，下拉可看-->
            <div class="bootbox-body" style="background: #fafafa;height: 150px;overflow-y: auto">
                <div id="bdcdyInfo"></div>
                <div id="bdcdyAlertInfo"></div>
                <div id="bdcdyConfirmInfo"></div>
            </div>
            <div id="footer" class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="bdcdyTipCloseBtn">关闭</button>
                <button type="button" class="btn btn-sm btn-primary" id="bdcdyTipBackBtn" style="display: none;">返回
                </button>
            </div>
        </div>
    </div>
</div>


<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-mul"></div>
<form id="form" hidden="hidden">
    <input type="hidden" id="gdproids" name="gdproids"/>
    <input type="hidden" id="qlids" name="qlids"/>
    <input type="hidden" id="djlx" name="djlx" value="${djlx!}"/>
    <input type="hidden" id="sqlxMc" name="sqlxMc" value="${sqlxmc!}"/>
    <input type="hidden" id="qllx" name="qllx" value="${qllx!}"/>
    <input type="hidden" id="proid" name="proid" value="${proid!}"/>
    <input type="hidden" id="wiid" name="wiid" value="${wiid!}"/>
</form>
<input type="hidden" id="bdcXmRelList" name="bdcXmRelList"/>
<input type="hidden" id="djIds" name="djIds"/>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<script type="text/javascript">
    var sqlxdm = "${sqlxdm!}";
    var bdcdjUrl = "${bdcdjUrl!}";
    var portalUrl = "${portalUrl!}";
    var reportUrl = "${reportUrl!}";
    var platformUrl ="${platformUrl!}";
    var sqlxdm = "${sqlxdm!}";
    var sflw = "${sflw!}";
    var zdtzm = "${zdtzm!}";
    var dyfs = "${dyfs!}"
    var yqllxdm = "${yqllxdm!}";
    var bdclxdm = "${bdclxdm!}";
    var qlxzdm = "${qlxzdm!}";
    var proid = "${proid!}";
    var ysqlxdm = "${ysqlxdm!}";
    var bdcdyly ="${bdcdyly!}";
    var djlx = "${djlx!}";
    var sqlxmc = "${sqlxmc!}";
    var qllx = "${qllx!}";
    var workFlowDefId = "${workFlowDefId}";
    var bdcXmRelListAll = "";
    var djidsAll = "";
    //多选数据
    $mulData = new Array();
    $mulRowid = new Array();
    $bdclx = 'TDFW';
    var valite = [];
    var showOptimize = "${showOptimize!}";
    var notShowCk = "${notShowCk!}";
</script>
<script src="static/js/${dwdm!}/djsjBdcdyMulQlShowList.js" type="text/javascript"></script>
</@com.html>
