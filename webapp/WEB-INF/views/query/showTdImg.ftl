<@com.html title="不动产登记业务管理系统" import="bui">

<style>
    /*弹出框*/
    .icon-right{
        float: right;

    }
</style>

<script>
    function showModel(url,title){
        $('#handleModel').modal('show');
        var handleModelDiv=document.getElementById("handleModelDiv");
        $("#pic").attr("src",url);
        //handleModelDiv.src=url;
        $("#handleModelTitle").html(title);
    }
    function hideModel(){
        $('#handleModel').modal('hide');
        var handleModelDiv=document.getElementById("handleModelDiv");
        handleModelDiv.src="";
        $("#handleModelTitle").html("");
    }
</script>

<div class="main-container">
    <div class="page-content" style="height:1200px;position: relative;overflow: auto;">
        <#if maplist??>
            <div style="margin-top: 20px;">
                <#list maplist as li>
                    <div style="width: 250px;height: 250px;float: left;margin-left: 10px;margin-bottom: 10px;" >
                        <img src="${li.pagePath}" alt="" onclick="showModel('${li.clickPath}','')" style="width: 240px;height: 240px;float:left;margin: auto">
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in newPro" style="display: none;" id="handleModel">
    <div class="modal-dialog newPro-modal">
        <div class="modal-content">
            <div class="modal-header panelBg ">
            <#--<button type="button" data-dismiss="modal" aria-label="Close">  <i class="icon-remove icon-white"></i></button>-->
                <i class="icon-remove icon-white icon-right" onclick="hideModel()"></i>
                <h4 class="modal-title" style="margin: 0; " id="handleModelTitle"></h4>
            </div>
            <div class="bootbox-body">
                <div id="handleModelDiv" name="handleModelDiv" style="width:980px; height:500px;position: relative;overflow: auto;">

                    <img id="pic" src="" width="99.8%" alt="" style="display: block;margin:auto;width: 960px;height: 960px;" >

                </div>
            </div>
        </div>
    </div>
</div>
<#--<div id="center" align="center"><h1>页面显示错误</h1></div>-->
</@com.html>