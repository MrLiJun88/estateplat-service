<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@script name="static/js/sign.js"></@script>
<script type="text/javascript">
    function saveShxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/sign/updateSignOpinon",
            type: 'POST',
            dataType: 'json',
            data: $("#shxxForm").serialize(),
            success: function () {
                        window.location.reload();
            },
            error: function () {
                window.location.reload();
            }
        });
    }
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="bs-docs-example toolTop">
        <div class="leftToolTop">
            <button type="button" class="btn btn-primary save" onclick="saveShxx()">保存</button>
        </div>
    </div>
    <@f.contentDiv title="不动产登记审核信息">
        <@f.form id="shxxForm" name="csxxForm">
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="proid"  name="proid" value="${proid!}"/>
            <@f.hidden id="csSignId"  name="csSignId" value="${csrSign.signId!}"/>
            <@f.hidden id="fsSignId"  name="fsSignId" value="${fsrSign.signId!}"/>
            <@f.hidden id="hdSignId"  name="hdSignId" value="${hdrSign.signId!}"/>
            <@f.hidden id="csSignKey"  name="csSignKey" value="csr"/>
            <@f.hidden id="fsSignKey"  name="fsSignKey" value="fsr"/>
            <@f.hidden id="hdSignKey"  name="hdSignKey" value="hdr"/>
           <@f.table>
               <@f.tr style="border:none;height:0px;">
                   <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                   <@f.td style="border:none;height:0px;width:70px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                   <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.th rowspan="6" colspan="1" >
                       <@f.label name="不<br>动<br>产<br>登<br>记<br>审<br>批<br>情<br>况"></@f.label>
                   </@f.th>
                   <@f.th rowspan="2" colspan="2">
                       <@f.label name="初审"></@f.label>
                   </@f.th>
                   <@f.td colspan="9" rowspan="1" height="90px">
                       <@f.textarea  id="csyj" name="csyj" value="${csrSign.signOpinion!}"></@f.textarea>
                   </@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.td colspan="1" >
                       <@f.label name="初审人"></@f.label>
                   </@f.td>
                   <@f.td colspan="3" >
                       <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${csrSign.signId!}"width="145" height="50" signId="${csrSign.signId!}" />
                   </@f.td>
                   <@f.td colspan="1" >
                   <a onclick="sign('${proid!}','csr','${taskid!}','csyj','false')"><i class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                   </@f.td>
                   <@f.td colspan="4">${csrq!}</@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.th rowspan="2" colspan="2">
                       <@f.label name="复审"></@f.label>
                   </@f.th>
                   <@f.td colspan="9" rowspan="1" height="90px">
                       <@f.textarea  id="fsyj" name="fsyj" value="${fsrSign.signOpinion!}"></@f.textarea>
                   </@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.td colspan="1" >
                       <@f.label name="复审人"></@f.label>
                   </@f.td>
                       <@f.td colspan="3" >
                           <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${fsrSign.signId!}"width="145" height="50" signId="${fsrSign.signId!}" />
                       </@f.td>
                   <@f.td colspan="1" >
                       <a onclick="sign('${proid!}','fsr','${taskid!}','fsyj','false')"><i class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                   </@f.td>
                   <@f.td colspan="4">${fsrq!}</@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.th rowspan="2" colspan="2">
                       <@f.label name="核定"></@f.label>
                   </@f.th>
                   <@f.td colspan="9" rowspan="1" height="90px">
                       <@f.textarea  id="hdyj" name="hdyj" value="${hdrSign.signOpinion!}"></@f.textarea>
                   </@f.td>
               </@f.tr>
               <@f.tr>
                   <@f.td colspan="1" >
                       <@f.label name="核定人"></@f.label>
                   </@f.td>
                   <@f.td colspan="3" >
                       <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${hdrSign.signId!}"width="145" height="50" signId="${hdrSign.signId!}" />
                   </@f.td>
                   <@f.td colspan="1" >
                       <a onclick="sign('${proid!}','hdr','${taskid!}','hdyj','false')"><i class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                   </@f.td>
                   <@f.td colspan="4">${hdrq!}</@f.td>
               </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
    <#include "defaultOpinion.ftl">
</@com.html>