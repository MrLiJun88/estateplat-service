<#if (bdcYwrList?size > 0)>
    <#list bdcYwrList as ywr>
        <@f.tr id="tr_${ywr.qlrid!}" >
            <@f.hidden id="qlrid" name="qlrid" value="${ywr.qlrid!}"/>
            <@f.hidden id="qlrlx_${ywr.qlrid!}" name="qlrlx" value="${ywr.qlrlx!}"/>
            <@f.th colspan="3"  >
                <@f.label name="义务人姓名（名称）"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrmc_${ywr.qlrid!}" name="qlrmc" value="${ywr.qlrmc!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="共有比例"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlbl_${ywr.qlrid!}" name="qlbl" value="${ywr.qlbl!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="身份证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlrsfzjzl_${ywr.qlrid!}" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${ywr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrzjh_${ywr.qlrid!}" name="qlrzjh" value="${ywr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="通讯地址"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrtxdz_${ywr.qlrid!}" name="qlrtxdz" value="${ywr.qlrtxdz!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="邮编"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlryb_${ywr.qlrid!}" name="qlryb" value="${ywr.qlryb!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="法定代表人或负责人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrfddbr_${ywr.qlrid!}" name="qlrfddbr" value="${ywr.qlrfddbr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrfddbrdh_${ywr.qlrid!}" name="qlrfddbrdh" value="${ywr.qlrfddbrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理人姓名"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlr_${ywr.qlrid!}" name="qlrdlr" value="${ywr.qlrdlr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlrdh_${ywr.qlrid!}" name="qlrdlrdh" value="${ywr.qlrdlrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理机构名称"></@f.label>
            </@f.th>
            <@f.td colspan="8">
                <@f.text id="qlrdljg_${ywr.qlrid!}" name="qlrdljg" value="${ywr.qlrdljg!}" ></@f.text>
            </@f.td>
        </@f.tr>
    </#list>
<#else>
    <@f.tr id="tr_def2" >
        <@f.hidden id="qlrid" name="qlrid" value="newYwr"/>
        <@f.hidden id="qlrlx_newYwr" name="qlrlx" value="ywr"/>
        <@f.th colspan="3"  >
            <@f.label name="义务人姓名（名称）"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrmc_newYwr" name="qlrmc" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="共有比例"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlbl_newYwr" name="qlbl" value="" ></@f.text>
        </@f.td>

    </@f.tr>
    <@f.tr id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="身份证件种类"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.select id="qlrsfzjzl_newYwr" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="证件号"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrzjh_newYwr" name="qlrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="通讯地址"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrtxdz_newYwr" name="qlrtxdz" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="邮编"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlryb_newYwr" name="qlryb" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="法定代表人或负责人"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrfddbr_newYwr" name="qlrfddbr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrfddbrdh_newYwr" name="qlrfddbrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="代理人姓名"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrdlr_newYwr" name="qlrdlr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrdlrdh_newYwr" name="qlrdlrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="代理机构名称"></@f.label>
        </@f.th>
        <@f.td colspan="8">
            <@f.text id="qlrdljg_newYwr" name="qlrdljg" value="" ></@f.text>
        </@f.td>
    </@f.tr>
</#if>