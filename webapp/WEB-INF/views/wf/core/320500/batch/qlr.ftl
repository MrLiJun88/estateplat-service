
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
                <@f.text style="display:block" id="qlrmc_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" ></@f.text>
                <@f.select style="display:none" id="qlrmc_${qlr.qlrid!}" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue="${qlr.qlrmc!}"/>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="共有比例"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlbl_${qlr.qlrid!}" name="qlbl" value="${qlr.qlbl!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="身份证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlrsfzjzl_${qlr.qlrid!}" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${qlr.qlrsfzjzl!}" ></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrzjh_${qlr.qlrid!}" name="qlrzjh" value="${qlr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="通讯地址"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrtxdz_${qlr.qlrid!}" name="qlrtxdz" value="${qlr.qlrtxdz!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="邮编"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlryb_${qlr.qlrid!}" name="qlryb" value="${qlr.qlryb!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="法定代表人或负责人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrfddbr_${qlr.qlrid!}" name="qlrfddbr" value="${qlr.qlrfddbr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrfddbrdh_${qlr.qlrid!}" name="qlrfddbrdh" value="${qlr.qlrfddbrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理人姓名"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlr_${qlr.qlrid!}" name="qlrdlr" value="${qlr.qlrdlr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlrdh_${qlr.qlrid!}" name="qlrdlrdh" value="${qlr.qlrdlrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理机构名称"></@f.label>
            </@f.th>
            <@f.td colspan="8">
                <@f.text id="qlrdljg_${qlr.qlrid!}" name="qlrdljg" value="${qlr.qlrdljg!}" ></@f.text>
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
            <@f.text style="display:block"id="qlrmc_def1" name="qlrmc" value="" ></@f.text>
            <@f.select style="display:none" id="qlrmc_def1" name="qlrmc"   showFieldName="YHMC" valueFieldName="YHMC" source="yhList" defaultValue=""/>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="共有比例"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlbl_def1" name="qlbl" value="" ></@f.text>
        </@f.td>

    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="身份证件种类"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.select id="qlrsfzjzl_def1" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="证件号"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrzjh_def1" name="qlrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="通讯地址"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrtxdz_def1" name="qlrtxdz" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="邮编"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlryb_def1" name="qlryb" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="法定代表人或负责人"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrfddbr_def1" name="qlrfddbr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrfddbrdh_def1" name="qlrfddbrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="代理人姓名"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrdlr_def1" name="qlrdlr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrdlrdh_def1" name="qlrdlrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def1" >
        <@f.th colspan="3"  >
            <@f.label name="代理机构名称"></@f.label>
        </@f.th>
        <@f.td colspan="8">
            <@f.text id="qlrdljg_def1" name="qlrdljg" value="" ></@f.text>
        </@f.td>
    </@f.tr>
</#if>