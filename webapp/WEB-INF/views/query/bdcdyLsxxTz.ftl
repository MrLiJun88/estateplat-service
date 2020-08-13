<@com.html title="不动产单元历史信息跳转" import="ace,public">
<script type="text/javascript">
    $(function () {
        var proid = $("#proid").val();
        var bdcdyid = $("#bdcdyid").val();
        var bdclx = $("#bdclx").val();
        var analysisUrl = $("#analysisUrl").val();
        var url = analysisUrl + "/cxURI/bdcdjZhQuery?proid=" + proid + "&bdcdyid=" + bdcdyid + "&bdclx=" + bdclx;
        openWin(url);
    });
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <input type="hidden" id="bdcdyid" value="${bdcdyid!}">
    <input type="hidden" id="bdclx" value="${bdclx!}">
    <input type="hidden" id="analysisUrl" value="${analysisUrl!}">
</div>

<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>