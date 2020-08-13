<@f.tr>
    <@f.th  colspan="3">
        <@f.label name="申请登记事由"></@f.label>
    </@f.th>
    <@f.td colspan="6">
        <@f.select id="djsy" name="djsy"  showFieldName="MC" valueFieldName="DM" source="djsyMcList" defaultValue="${bdcXm.djsy}" noEmptyValue="true" ></@f.select>
    </@f.td>
    <@f.td colspan="4">
        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList" defaultValue="${bdcXm.djlx!}" noEmptyValue="true" ></@f.select>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th  colspan="3">
        <@f.label name="申请证书版式"></@f.label>
    </@f.th>
    <@f.td colspan="4">
        <@f.radio name="sqzsbs" valueFieldName="单一版"  saveValue="${bdcXm.sqzsbs!}" defaultValue="单一版">
        单一版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</@f.radio>
        <@f.radio name="sqzsbs" valueFieldName="集成板" saveValue="${bdcXm.sqzsbs!}"  >
        集成板</@f.radio>
    </@f.td>
    <@f.th  colspan="2">
        <@f.label name="申请分别持证"></@f.label>
    </@f.th>
    <@f.td colspan="4">
        <@f.radio id="sqfbcz" name="sqfbcz" valueFieldName="是"  saveValue="${bdcXm.sqfbcz!}" defaultValue="是">
        是&nbsp;&nbsp;</@f.radio>
        <@f.radio name="sqfbcz" valueFieldName="否" saveValue="${bdcXm.sqfbcz!}">
        否</@f.radio>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th   colspan="1" id="sqrqk">
        <@f.label class="leftLabel" name="                    申请人情况                   "></@f.label>
    </@f.th>
    <@f.th   style="text-align:center; background: #f9f9f9;border-right-style:none;"  colspan="11" >
        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
    </@f.th>
    <@f.th colspan="1" style="border-left-style:none;background: #f9f9f9;">
    <a onclick="qlrAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
    </@f.th>
</@f.tr>