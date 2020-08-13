<@f.tr>
    <@f.th rowspan="6" colspan="1" >
        <@f.label class="leftLabel" name="不动产登记审批情况"></@f.label>
    </@f.th>
    <@f.th rowspan="2" colspan="2">
        <@f.label name="初审"></@f.label>
    </@f.th>
    <@f.td colspan="10" rowspan="1" height="90px">
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
    <a onclick="sign('${proid!}','csr','${taskid!}','csyj','false')"><i
            class="ace-icon glyphicon glyphicon-edit">签名</i></a>
    </@f.td>
    <@f.td colspan="5">
        <@f.text  id="csyjqm" name="csyjqm" value="${csrq!}" style="text-align:center"></@f.text>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th rowspan="2" colspan="2">
        <@f.label name="复审"></@f.label>
    </@f.th>
    <@f.td colspan="10" rowspan="1" height="90px">
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
    <a onclick="sign('${proid!}','fsr','${taskid!}','fsyj','false')"><i
            class="ace-icon glyphicon glyphicon-edit">签名</i></a>
    </@f.td>
    <@f.td colspan="5">
        <@f.text  id="fsyjqm" name="fsyjqm" value="${fsrq!}" style="text-align:center"></@f.text>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th rowspan="2" colspan="2">
        <@f.label name="核定"></@f.label>
    </@f.th>
    <@f.td colspan="10" rowspan="1" height="90px">
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
    <a onclick="sign('${proid!}','hdr','${taskid!}','hdyj','false')"><i
            class="ace-icon glyphicon glyphicon-edit">签名</i></a>
    </@f.td>
    <@f.td colspan="5">
        <@f.text  id="hdyjqm" name="hdyjqm" value="${hdrq!}" style="text-align:center"></@f.text>
    </@f.td>
</@f.tr>