<@com.html title="" import="ace,public">
<link href="../static/css/jgDataPic.css" rel="stylesheet" type="text/css"/>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <#include "top.ftl">
        <div class="space-4"></div>
        <div class="row">
            <#include "left.ftl">
            <#include "right.ftl">
        </div>
    </div>
</div>
<#--文件选择框-->
    <#include "file.ftl">
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gdXzyyPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    <i class="ace-icon fa fa-search bigger-110"></i>
                    请输入限制原因
                </h4>
                <button type="button" id="proHide" class="proHide">
                    <i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gdXzyyForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>限制原因：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" name="xzyy" id="xzyy" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gdLockSureBtn">确定</button>
                <button type="button" class="btn btn-sm btn-primary" id="gdLockCancelBtn">取消</button>
            </div>
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="djlx" name="djlx">
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
    <input type="hidden" id="tdqlzt" name="tdqlzt"/>
    <input type="hidden" id="selPpzt" name="selPpzt"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<script>
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var portalUrl = "${portalUrl!}";
    var wfids = "${wfids!}";
    var matchTdzh = "${matchTdzh!}";
    var filterFwPpzt = "${filterFwPpzt!}";
    var gdTabOrder = "${gdTabOrder!}";
    var gdTabLoadData = "${gdTabLoadData!}";
    var bppwfids = "${bppwfids!}";
    var mulFczTdzPp = "${mulFczTdzPp!}";
    var etlUrl = "${etlUrl!}";
    var hjhtdzSearch = "${hjhtdzSearch!}";
    var showOptimize  = "${showOptimize}";
    var isShowPphzd = "${isShowPphzd!}";
</script>
<script src="../static/js/${dwdm!}/jgDataPic.js" type="text/javascript"></script>
</@com.html>
