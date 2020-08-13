<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->
<#--<script src="../js/sjd.js"></script>-->
<#--<#assign path="${request.getContextPath()}">-->
<script src="static/js/wf.js" type="text/javascript"></script>
<script type="text/javascript" >
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl="${path_platform!}";
    var analysisUrl ="${analysisUrl!}";
    var taskid ="${taskid!}";
    var from="${from!}";
    var rid="${rid!}";
    e = 1;

    function  saveSjclList() {
        $.blockUI({message: "请稍等......"});
        var arry=[];
        $("input[name='sjclid']").each(function(i,n){
            var id=$(n).val();
            if($("#checkbox_"+id)[0].checked){
                var o=new Object();
                o['sjclid']=$(n).val();
                o['sjxxid']=$("#sjxxid").val();
                o['xh']=$("#xh_"+id).val();
                o['clmc']=$("#clmc_"+id).val();
                o['cllx']=$("#cllx_"+id).val();
                o['fs']=$("#fs_"+id).val();
                o['ys']=$("#ys_"+id).val();
                arry.push(o);
            }
        });
        var s=JSON.stringify(arry);
        $.ajax({
            url: bdcdjUrl+"/bdcdjSjdxx/saveSjclList",
            type: 'POST',
            dataType: 'json',
            data:$.param({s:s}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "true") {
                        window.location.reload();
                        alert("保存成功!");
                    }
                }
            },
            error: function (data) {
                alert("保存收件材料失败!");
            }
        });
    }

    function printBzcl() {
        var proid="${proid!}";
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_bzcltzs.cpt&proid=" + proid;
        openWin(url);
    }
</script>
<style type="text/css">
    span{
        border-bottom:1px solid black;
    }
    .toparea{
        width:650px;
        position:relative;
        text-align:left;
        left:100px;
    }
    .toparea .qlrname{
        position:relative;
        text-align:left;
        margin-top: 20px;
    }
    .toparea .info{
        margin-top: 5px;
        margin-bottom: 10px;
        text-align:left;
    }
    .toparea .bh{
        position:relative;
        margin-top: 40px;
        text-align:right;
    }
    .bottomarea{
        width:650px;
        text-align:left;
        position: relative;
        left:100px;
        margin-top: 20px;
    }
    .bottomarea p{
        position: relative;
        text-align:right;
    }
    .bottomarea .bzclInfo{
        position: relative;
        text-align:left;
        margin-top: 15px;
        margin-bottom: 15px;
    }

</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveSjclList()">保存</button>
        <button type="button" class="btn btn-info save" onclick="printBzcl()">打印</button>
    </div>
</div>
<div class="main-container" >
    <@f.contentDiv  title="不动产登记补正材料通知单" >
        <@f.form id="bzclForm" name="bzclForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="sjxxid" name="sjxxid"  value="${bdcSjxx.sjxxid!}"/>
            <@f.hidden id="spxxid" name="spxxid"  value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="sjxxNum" name="sjxxNum"  value="${sjxxNum!}"/>
            <@f.hidden id="bh" name="bh"  value="${bdcXm.bh!}"/>
            <@f.hidden id="zl" name="zl"  value="${bdcSpxx.zl!}"/>
            <@f.hidden id="tzdx" name="tzdx"  value="${qlrs!}"/>
            <div class="toparea">
                <div class="bh"><p>编号：${bdcXm.bh!}</p></div>
                <div class="qlrname"><p><span style="margin-right:100px;">${qlrs!}:</span></p></div>
                <div class="info">
                    <p><span >${sjrq!}</span>，收到你（单位）<span >${bdcdjlx!}</span>的登记申请，坐落于<span>${bdcSpxx.zl!}</span>的申请，受理编号为<span>${bdcXm.bh}</span>。因所提交的申请材料尚不足以证明申请登记相关事项，按照《不动产登记暂行条例》第十七条的规定，请补正以下申请材料：</p>
                </div>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th colspan="4" style="border-right:none;text-align:left">
                        <a onclick="addSjcl()"><i class="ace-icon glyphicon glyphicon-plus-sign">插入行</i></a>&nbsp;&nbsp;
                        <a onclick="sjclDel()"><i class="ace-icon glyphicon glyphicon-remove-sign">删除</i></a>&nbsp;&nbsp;
                        <a onclick="upFile()"><i class="ace-icon glyphicon glyphicon-arrow-up">批量上传</i></a>
                    </@f.th>
                    <@f.th colspan="8" style="border-left:none;text-align:left">
                        <@f.label name="  材  料  清  单  "></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" >
                        <input role="checkbox" id="allcheckbox" class="cbox" type="checkbox" onclick="checkboxOnclick(this)" />
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="序号"></@f.label>
                    </@f.td>
                    <@f.td colspan="4" >
                        <@f.label name="材料名称"></@f.label>
                    </@f.td>
                    <@f.td colspan="2" >
                        <@f.label name="材料类型"></@f.label>
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="份数"></@f.label>
                    </@f.td>
                    <@f.td colspan="1" >
                        <@f.label name="页数"></@f.label>
                    </@f.td>
                    <@f.td colspan="2" style="text-align:left">
                        <@f.label name="编辑|上传"></@f.label>
                    </@f.td>
                </@f.tr>
                <tbody id="sjcl">
                    <#list sjclList as sjcl>
                        <@f.tr>
                            <@f.hidden id="sjclid" name="sjclid"  value="${sjcl.SJCLID!}"/>
                            <@f.td colspan="1" >
                            <input role="checkbox" id="checkbox_${sjcl.SJCLID!}" name="checkbox" class="cbox" type="checkbox"
                                   value="${sjcl.SJCLID!}">
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="xh_${sjcl.SJCLID!}" name="xh" value="${sjcl.ROWNUM!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="4" >
                                <@f.text  id="clmc_${sjcl.SJCLID!}" name="clmc" value="${sjcl.SJCLMC!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="2" >
                                <@f.select  id="cllx_${sjcl.SJCLID!}" name="cllx"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'原件','dm':'1'},{'mc':'复印件','dm':'2'}]" defaultValue="${sjcl.CLLX!}" noEmptyValue="true" ></@f.select>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="fs_${sjcl.SJCLID!}" name="fs" value="${sjcl.FS!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="ys_${sjcl.SJCLID!}" name="ys" value="${sjcl.YS!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.th colspan="2" >
                            <!--<a class="detail" href="javascript:editSjcl('${sjcl.SJCLID!}')">详细</a>-->
                            <a onclick="fileUp('${sjcl.SJCLMC!}')"><i class="ace-icon glyphicon glyphicon-arrow-up"></i></a>
                            </@f.th>
                        </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
            <div class="bottomarea">
                <p>苏州市不动产登记中心（印章)</p>
                <p>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</p>
                <div  class="bzclInfo">
               补正材料结果确认：已于${sjrq!}收到上述补正材料。
                    注：登记时限自补正申请材料之日起重新计算。
                </div>
                <p></p>
                <p>苏州市不动产登记中心（印章)</p>
                <p>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</p>
            </div>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>