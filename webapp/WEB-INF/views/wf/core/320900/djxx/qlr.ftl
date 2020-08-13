<div>
<#if (bdcQlrList?size > 0)>
    <#list bdcQlrList as qlr>
        <@f.tr id="tr_${qlr.qlrid!}" >
            <@f.hidden id="fbcz" name="fbcz" value="${qlr.qlrid!}"/>
            <@f.hidden id="qlrid" name="qlrid" value="${qlr.qlrid!}"/>
            <@f.hidden id="qlrlx_${qlr.qlrid!}" name="qlrlx" value="${qlr.qlrlx!}"/>
            <@f.th colspan="3"  >
                <@f.label name="权利人姓名（名称）"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text style="display:block" id="qlr_qlr_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" ></@f.text>
                <@f.select style="display:none" id="qlr_qlr_${qlr.qlrid!}" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue="${qlr.qlrmc!}"/>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="共有比例"></@f.label>
            </@f.th>
            <@f.td colspan="2"style="border-right-style:none;">
                <@f.text id="qlr_gybl_${qlr.qlrid!}" name="qlbl" value="${qlr.qlbl!}" ></@f.text>
            </@f.td>
            <@f.td colspan="1" style="border-left-style:none;text-align:center">
                <a id="qlr_del" name="qlr_del"onclick="qlrDel('${qlr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="身份证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlr_qlrsfzjzl_${qlr.qlrid!}" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${qlr.qlrsfzjzl!}" ></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrzjh_${qlr.qlrid!}" name="qlrzjh" value="${qlr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="通讯地址"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrtxdz_${qlr.qlrid!}" name="qlrtxdz" value="${qlr.qlrtxdz!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="邮编"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlryb_${qlr.qlrid!}" name="qlryb" value="${qlr.qlryb!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="法定代表人或负责人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrfddbr_${qlr.qlrid!}" name="qlrfddbr" value="${qlr.qlrfddbr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrfddbrdh_${qlr.qlrid!}" name="qlrfddbrdh" value="${qlr.qlrfddbrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理人姓名"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrdlr_${qlr.qlrid!}" name="qlrdlr" value="${qlr.qlrdlr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlr_qlrdlrdh_${qlr.qlrid!}" name="qlrdlrdh" value="${qlr.qlrdlrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理机构名称"></@f.label>
            </@f.th>
            <@f.td colspan="8">
                <@f.text id="qlr_qlrdljg_${qlr.qlrid!}" name="qlrdljg" value="${qlr.qlrdljg!}" ></@f.text>
            </@f.td>
        </@f.tr>
    </#list>
<#else>
    <@f.tr id="tr_def1" >
        <@f.hidden id="fbcz" name="fbcz" value="def1"/>
        <@f.hidden id="qlrid" name="qlrid" value="def1"/>
        <@f.hidden id="qlrlx_def1" name="qlrlx" value="qlr"/>
        <@f.th colspan="3"  >
            <@f.label name="权利人姓名（名称）"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text style="display:block"id="qlr_qlr_def1" name="qlrmc" value="" ></@f.text>
            <@f.select style="display:none" id="qlr_qlr_def1" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue=""/>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="共有比例"></@f.label>
        </@f.th>
        <@f.td colspan="2"style="border-right-style:none;">
            <@f.text id="qlr_gybl_def1" name="qlbl" value="" ></@f.text>
        </@f.td>
        <@f.td colspan="1" style="border-left-style:none;text-align:center">
            <a id="qlr_del" name="qlr_del"onclick="qlrDel('def1')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="身份证件种类"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.select id="qlr_qlrsfzjzl_def1" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="证件号"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrzjh_def1" name="qlrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="通讯地址"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrtxdz_def1" name="qlrtxdz" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="邮编"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlryb_def1" name="qlryb" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="法定代表人或负责人"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrfddbr_def1" name="qlrfddbr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrfddbrdh_def1" name="qlrfddbrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="代理人姓名"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrdlr_def1" name="qlrdlr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlr_qlrdlrdh_def1" name="qlrdlrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="代理机构名称"></@f.label>
        </@f.th>
        <@f.td colspan="8">
            <@f.text id="qlr_qlrdljg_def1" name="qlrdljg" value="" ></@f.text>
        </@f.td>
    </@f.tr>
</#if>
</div>
