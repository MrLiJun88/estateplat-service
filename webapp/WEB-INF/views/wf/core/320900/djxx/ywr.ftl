
<#if (bdcYwrList?size > 0)>
    <#list bdcYwrList as ywr>
        <@f.tr id="tr_${ywr.qlrid!}" >
            <@f.hidden id="ywrid" name="ywrid" value="${ywr.qlrid!}"/>
            <@f.hidden id="ywrlx_${ywr.qlrid!}" name="ywrlx" value="${ywr.qlrlx!}"/>
            <@f.th colspan="3"  >
                <@f.label name="义务人姓名（名称）"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlr_${ywr.qlrid!}" name="ywrmc" value="${ywr.qlrmc!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="共有比例"></@f.label>
            </@f.th>
            <@f.td colspan="2" style="border-right-style:none;">
                <@f.text id="ywr_gybl_${ywr.qlrid!}" name="ywrqlbl" value="${ywr.qlbl!}" ></@f.text>
            </@f.td>
            <@f.td colspan="1" style="border-left-style:none;text-align:center">
            <a  id="ywr_del"name="ywr_del"onclick="qlrDel('${ywr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
            </@f.td>
        </@f.tr>
        <@f.tr id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="身份证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="ywr_qlrsfzjzl_${ywr.qlrid!}" name="ywrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${ywr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrzjh_${ywr.qlrid!}" name="ywrzjh" value="${ywr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="通讯地址"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrtxdz_${ywr.qlrid!}" name="ywrtxdz" value="${ywr.qlrtxdz!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="邮编"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlryb_${ywr.qlrid!}" name="ywryb" value="${ywr.qlryb!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="法定代表人或负责人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrfddbr_${ywr.qlrid!}" name="ywrfddbr" value="${ywr.qlrfddbr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrfddbrdh_${ywr.qlrid!}" name="ywrfddbrdh" value="${ywr.qlrfddbrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理人姓名"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrdlr_${ywr.qlrid!}" name="ywrdlr" value="${ywr.qlrdlr!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系电话"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="ywr_qlrdlrdh_${ywr.qlrid!}" name="ywrdlrdh" value="${ywr.qlrdlrdh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${ywr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="代理机构名称"></@f.label>
            </@f.th>
            <@f.td colspan="8">
                <@f.text id="ywr_qlrdljg_${ywr.qlrid!}" name="ywrdljg" value="${ywr.qlrdljg!}" ></@f.text>
            </@f.td>
        </@f.tr>
    </#list>
<#else>
    <@f.tr id="tr_def2" >
        <@f.hidden id="ywrid" name="ywrid" value="def2"/>
        <@f.hidden id="ywrlx_def2" name="ywrlx" value="ywr"/>
        <@f.th colspan="3"  >
            <@f.label name="义务人姓名（名称）"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlr_def2" name="ywrmc" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="共有比例"></@f.label>
        </@f.th>
        <@f.td colspan="2" style="border-right-style:none;">
            <@f.text id="ywr_gybl_def2" name="ywrqlbl" value="" ></@f.text>
        </@f.td>
        <@f.td colspan="1" style="border-left-style:none;text-align:center">
        <a id="ywr_del"name="ywr_del"onclick="qlrDel('def2')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
        </@f.td>
    </@f.tr>
    <@f.tr id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="身份证件种类"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.select id="ywr_qlrsfzjzl_def2" name="ywrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="证件号"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrzjh_def2" name="ywrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="通讯地址"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrtxdz_def2" name="ywrtxdz" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="邮编"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlryb_def2" name="ywryb" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="法定代表人或负责人"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrfddbr_def2" name="ywrfddbr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrfddbrdh_def2" name="ywrfddbrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="代理人姓名"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrdlr_def2" name="ywrdlr" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="联系电话"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="ywr_qlrdlrdh_def2" name="ywrdlrdh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <@f.tr  id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="代理机构名称"></@f.label>
        </@f.th>
        <@f.td colspan="8">
            <@f.text id="ywr_qlrdljg_def2" name="ywrdljg" value="" ></@f.text>
        </@f.td>
    </@f.tr>
</#if>
