<script>
    var zydjValidate= $("#zydjXjForm").validate();
    //选择不动产单元号的赋值操作
    function zydjDyhSel(dyh,id,bdclx,xmmc){
        var zdzhh;
        if(dyh && dyh!='undefined'){
            zdzhh=dyh.substr(0,19);
            $("#zydj_bdcdyh").val(dyh);
            $("#zydj_zdzhh").val(zdzhh);
        }else{
            $("#zydj_bdcdyh").val("");
            $("#zydj_zdzhh").val("");
        }
        if(id && id!='undefined'){
            if(id!=$("#zydj_djId").val()){
                clearCheckInfo();
            }
            $("#zydj_djId").val(id);
        }else{
            if($("#zydj_djId").val()!=""){
                clearCheckInfo();
            }
            $("#zydj_djId").val("");
        }
        if(xmmc && xmmc!='undefined'){
            $("#zydj_xmmc").val(xmmc);
        }else{
            $("#zydj_xmmc").val("");
        }
        if(bdclx && bdclx!='undefined'){
            $("#zydj_bdclx").val(bdclx);
            getQllx(bdclx,$("#zydj_djlx_text").val(),"zydj_qllx");
        }else{
            $("#zydj_bdclx").val("");
        }
    }
    //选择房产证号
    function zydjFwSel(fczh,id){
        if(id && id!='undefined'){
            $("#zydj_fwid").val(id);
        }else{
            $("#zydj_fwid").val("");
        }
    }
    //选择土地证号
    function zydjTdSel(tdzh,id){
        if(id && id!='undefined'){
            $("#zydj_tdid").val(id);
        }else{
            $("#zydj_tdid").val("");
        }
    }
    //选择土地证号
    function zydjSel(cqzh,dyh,qllx,xmmc,proid,bdcdyid){
        var zdzhh;
        if(dyh && dyh!='undefined'){
            zdzhh=dyh.substr(0,19);
            $("#zydj_bdcdyh").val(dyh);
            $("#zydj_zdzhh").val(zdzhh);
        }else{
            $("#zydj_bdcdyh").val("");
            $("#zydj_zdzhh").val("");
        }
        if(bdcdyid && bdcdyid!='undefined'){
            $("#zydj_bdcdyid").val(bdcdyid);
        }else{
            $("#zydj_bdcdyid").val("");
        }
        if(cqzh && cqzh!='undefined'){
            $("#zydj_cqzh").val(cqzh);
        }else{
            $("#zydj_cqzh").val("");
        }
        if(qllx && qllx!='undefined'){
            $("#zydj_qllx option").each(function(){ //遍历全部option
                var val = $(this).val(); //获取option的内容
                if(val == qllx){
                    $("#zydj_qllx").val(qllx);
                }
            });
        }
        if (proid && proid!='undefined'){
            if(proid!=$("#zydj_yxmid").val()){
                clearCheckInfo();
            }
            $("#zydj_yxmid").val(proid);
        }else{
            if($("#zydj_yxmid").val()!=""){
                clearCheckInfo();
            }
            $("#zydj_yxmid").val("");
        }
    }
</script>
<form class="form-base "  id="zydjXjForm">
    <div class="row tableRow">
        <div class="col-xs-4">
            <label class="labelRow"><s>*</s>登记类型：</label>
        </div>
        <div class="col-xs-8">
            <select  id="zydj_djlx" class="form-control" disabled>
            <#--<#list djList as djlx>
                <option value="${djlx.dm!}" >${djlx.mc!}</option>
            </#list>-->
            </select>
            <input name="djlx" type="hidden" id="zydj_djlx_text">
        </div>
    </div>
    <div class="row tableRow">
        <div class="col-xs-4">
            <label class="labelRow"><s>*</s>权利类型：</label>
        </div>
        <div class="col-xs-8">
            <select name="qllx" id="zydj_qllx" class="form-control">
           <#-- <#list qlList as qllx>
                <option value="${qllx.dm!}" >${qllx.mc!}</option>
            </#list>-->
            </select>
        </div>
    </div>
    <div class="row tableRow">
        <div class="col-xs-4">
            <label>产权证号：</label>
        </div>
        <div class="col-xs-8">
            <textarea id="zydj_cqzh" class="form-control"></textarea>
        </div>
    </div>
    <div class="row tableRow">
        <div class="col-xs-4">
            <label><s>*</s>不动产单元号：</label>
        </div>
        <div class="col-xs-8">
            <textarea id="zydj_bdcdyh" name="bdcdyh" class="form-control" required="true"></textarea>
            <input type="hidden" id="zydj_yxmid" name="yxmid" class="form-control">
            <input type="hidden" id="zydj_bdcdyid" name="bdcdyid" class="form-control">
        <#-- <a href="" class="btn btn-success beProducedBtn">生成</a>-->
        </div>
    </div>
    <div class="tableRow row">
        <div class="col-xs-4">
            <label class="labelRow"><s>*</s>宗地/宗海号：</label>
        </div>
        <div class="col-xs-8 ">
            <input type="text" id="zydj_zdzhh" name="zdzhh" class="form-control" required="true">
        </div>
    </div>
    <div class="tableRow row">
        <div class="col-xs-4">
            <label class="labelRow"><s>*</s>项目名称：</label>
        </div>
        <div class="col-xs-8 ">
            <input type="text" id="zydj_xmmc" name="xmmc" class="form-control" required="true">
        </div>
    </div>
    <div class="row">
        <div class="col-xs-4">
        <#--隐藏字段-->
            <input type="hidden" id="zydj_fwid" name="fwid" class="form-control">
            <input type="hidden" id="zydj_tdid" name="tdid" class="form-control">
            <input type="hidden" id="zydj_djId" name="djId" class="form-control">
            <input type="hidden" id="zydj_bdclx" name="bdclx" class="form-control">
        </div>
        <div class="col-xs-8">
            <button type="button" class="btn btn-sm btn-primary" id="zydjXjBtn" onclick="zydjXj()">确定</button>
        </div>
    </div>
<#--验证信息显示div-->
    <div class="row tableRow">
        <div id="alertInfo" style="padding-right: 10px"></div>
        <div id="confirmInfo" style="padding-right: 10px"></div>
    </div>
</form>



