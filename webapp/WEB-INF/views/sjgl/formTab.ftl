<@com.html title="表单" import="bui">
<style>
    body{
        overflow: hidden;
    }
    /*弹出框*/
    .icon-right{
        float: right;

    }
</style>
<script id="tab" src="${bdcdjUrl}/static/js/tab.js" url="${bdcdjUrl}/bdcConfig/getResourceByDjlx?proid=${bdcXm.proid!}&readOnly=${readOnly?c!}"></script>
<script>
    jQuery(function ($) {
       /* //保存帆软表单
        $("#saveForm").click(function(){
            document.getElementById('mainFrame').contentWindow.contentPane.writeReport();
        })*/
        //生成证书点击事件
        $("#sczs").click(function(){
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSjgl/sczs?proid=${bdcXm.proid!}",
                dataType: "json",
                success: function (data) {
                    alert(data);
                },
                error: function (data) {
                 alert("生成证书失败!");
                }
            });
        })
        //打印证书点击事件
        $("#dyzs").click(function(){

        })
    });
    function showModel(url,title){
        $('#handleModel').modal('show');
        var handleModelDiv=document.getElementById("handleModelDiv");
        handleModelDiv.src=url;
        $("#handleModelTitle").html(title);
    }
    function hideModel(){
        $('#handleModel').modal('hide');
        var handleModelDiv=document.getElementById("handleModelDiv");
        handleModelDiv.src="";
        $("#handleModelTitle").html("");
    }
    function resourceRefresh(){
        var contentFrame=document.getElementById("mainFrame");
        contentFrame.src=contentFrame.src;
    }
</script>
<div class="PopPanel">
    <div class="panel-header panelBg ">
        <h3 class="panelLabel pull-left "><i class="icon icon-white icon-edit iconPosition"></i>${bdcXm.xmmc!}
            <span class="label label-success"><#if bdcXm.xmzt?? && bdcXm.xmzt = "1">办结<#else>办理中</#if></span>
        </h3>
        <div class="pull-right">
            <button type="button" class="panelButton" id="sczs"><i class="icon-ok icon-white"></i>生成证书</button>
            <#--<button class="panelButton" id="dyzs"><i class="icon-share-alt icon-white"></i>打印证书</button>-->
        </div>
    </div>
    <div class="panel-body">
        <div class="tabbable tabs-left businessContent">
            <ul class="nav nav-tabs" id="myTab">
            </ul>
            <div class="tab-content">
                <div class="Pop-upBox bootbox modal fade bootbox-prompt in newPro" style="display: none;" id="handleModel">
                    <div class="modal-dialog newPro-modal">
                        <div class="modal-content">
                            <div class="modal-header panelBg ">
                            <#--<button type="button" data-dismiss="modal" aria-label="Close">  <i class="icon-remove icon-white"></i></button>-->
                                <i class="icon-remove icon-white icon-right" onclick="hideModel()"></i>
                                <h4 class="modal-title" style="margin: 0; " id="handleModelTitle"></h4>
                            </div>
                            <div class="bootbox-body">
                                <iframe id="handleModelDiv" name="handleModelDiv" src="" width="99.8%" height="550" style="border:none;"></iframe>
                            </div>
                        </div>
                    </div>
                </div>
                <iframe id="mainFrame"  name="mainFrame" src="" width=100% height=100% style="border:0" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0">
            </div>
        </div>
    </div>
</div>
</@com.html>
