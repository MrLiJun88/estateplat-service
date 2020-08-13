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
                <@f.text id="qlrmc_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="是否持证人"></@f.label>
            </@f.th>
            <@f.td colspan="3"style="border-right-style:none;">
                <@f.radio id="sfczr_${qlr.qlrid!}" name="sfczr" valueFieldName="1"  saveValue="${qlr.sfczr!}" defaultValue="1">
                    是&nbsp;&nbsp;</@f.radio>
                <@f.radio name="sfczr" valueFieldName="0" saveValue="${qlr.sfczr!}">
                    否</@f.radio>
            </@f.td>
            <@f.td colspan="1" style="border-left-style:none;">
            <a onclick="qlrDel('${qlr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_${qlr.qlrid!}" >
            <@f.th colspan="3"  >
                <@f.label name="身份证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlrsfzjzl_${qlr.qlrid!}" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${qlr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlrzjh_${qlr.qlrid!}" name="qlrzjh" value="${qlr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
        <#if showRows=="true">
            <@f.tr  id="tr_${qlr.qlrid!}" >
                <@f.th colspan="3"  >
                    <@f.label name="共有方式"></@f.label>
                </@f.th>
                <@f.td colspan="3">
                    <@f.select id="gyfs_${qlr.qlrid!}" name="qlrGyfs"  showFieldName="MC" valueFieldName="DM"  source="gyfsList" defaultValue="${qlr.gyfs!}"></@f.select>
                </@f.td>
                <@f.th colspan="2"  >
                    <@f.label name="共有比例"></@f.label>
                </@f.th>
                <@f.td colspan="4">
                    <@f.text id="qlbl_${qlr.qlrid!}" name="qlbl" value="${qlr.qlbl!}" ></@f.text>
                </@f.td>
                <@f.td colspan="1">
                    <p style="display: none;" id="error_${qlr.qlrid!}"></p>
                </@f.td>
            </@f.tr>
            <@f.tr  id="tr_${qlr.qlrid!}" >
                <@f.th colspan="3"  >
                    <@f.label name="其余共有人"></@f.label>
                </@f.th>
                <@f.td colspan="9">
                    <@f.text id="qygyr_${qlr.qlrid!}" name="qygyr" value="${qlr.qygyr!}" ></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr  id="tr_${qlr.qlrid!}" >
                <@f.th colspan="3"  >
                    <@f.label name="代理人"></@f.label>
                </@f.th>
                <@f.td colspan="3">
                    <@f.text id="qlrdlr_${qlr.qlrid!}" name="qlrdlr" value="${qlr.qlrdlr!}" ></@f.text>
                </@f.td>
                <@f.th colspan="2"  >
                    <@f.label name="联系地址或电话"></@f.label>
                </@f.th>
                <@f.td colspan="4">
                    <@f.text id="qlrdlrdh_${qlr.qlrid!}" name="qlrdlrdh" value="${qlr.qlrdlrdh!}" ></@f.text>
                </@f.td>
            </@f.tr>
            <@f.tr  id="tr_${qlr.qlrid!}" >
                <@f.th colspan="3"  >
                    <@f.label name="证件类型"></@f.label>
                </@f.th>
                <@f.td colspan="3">
                    <@f.select id="qlrdlrzjzl_${qlr.qlrid!}" name="qlrdlrzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${qlr.qlrdlrzjzl!}" noEmptyValue="true"></@f.select>
                </@f.td>
                <@f.th colspan="2"  >
                    <@f.label name="证件号"></@f.label>
                </@f.th>
                <@f.td colspan="4">
                    <@f.text id="qlrdlrzjh_${qlr.qlrid!}" name="qlrdlrzjh" value="${qlr.qlrdlrzjh!}" ></@f.text>
                </@f.td>
            </@f.tr>
        </#if>

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
            <@f.text id="qlrmc_def1" name="qlrmc" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="是否持证人"></@f.label>
        </@f.th>
        <@f.td colspan="3"style="border-right-style:none;">
            <@f.text id="sfczr_def1" name="sfczr" value="" ></@f.text>
        </@f.td>
        <@f.td colspan="1" style="border-left-style:none;">
        <a onclick="qlrDel('def1')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
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
        <@f.td colspan="4">
            <@f.text id="qlrzjh_def1" name="qlrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <#if showRows=="true">
        <@f.tr  id="tr_def1" >
            <@f.th colspan="3"  >
                <@f.label name="共有方式"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="gyfs_def1" name="qlrGyfs"  showFieldName="MC" valueFieldName="DM"  source="gyfsList" defaultValue=""></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="共有比例"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlbl_def1" name="qlbl" value="" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_def1" >
            <@f.th colspan="3"  >
                <@f.label name="其余共有人"></@f.label>
            </@f.th>
            <@f.td colspan="9">
                <@f.text id="qygyr_${qlr.qlrid!}" name="qygyr" value="" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_def1" >
            <@f.th colspan="3"  >
                <@f.label name="代理人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlr_def1" name="qlrdlr" value="" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系地址或电话"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlrdlrdh_def1" name="qlrdlrdh" value="" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr  id="tr_def1" >
            <@f.th colspan="3"  >
                <@f.label name="证件类型"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlrdlrzjzl_def1" name="qlrdlrzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlrdlrzjh_def1" name="qlrdlrzjh" value="" ></@f.text>
            </@f.td>
        </@f.tr>
    </#if>
</#if>
</div>
