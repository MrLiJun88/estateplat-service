<@f.tr>
    <@f.th  colspan="3">
        <@f.label name="申请登记事由"></@f.label>
    </@f.th>
    <@f.td colspan="6">
        <@f.select id="djsy" name="djsy"  showFieldName="MC" valueFieldName="DM" source="djsyMcList" defaultValue="${bdcXm.djsy}" noEmptyValue="true" ></@f.select>
    </@f.td>
    <@f.td colspan="3">
        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList" defaultValue="${bdcXm.djlx!}" noEmptyValue="true" ></@f.select>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th  colspan="3">
        <@f.label name="共有方式"></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.radio id="ddsy" name="gyfs" valueFieldName="0"  saveValue="${gyfs!}" defaultValue="0">单独所有&nbsp;&nbsp;</@f.radio>
        <@f.radio id="gtgy" name="gyfs" valueFieldName="1" saveValue="${gyfs!}"  >
        共同共有&nbsp;&nbsp;</@f.radio>
        <@f.radio id="afgy" name="gyfs" valueFieldName="2"  saveValue="${gyfs!}" >
        按份共有&nbsp;&nbsp;</@f.radio>
        <@f.radio id="qtgy" name="gyfs" valueFieldName="3" saveValue="${gyfs!}"  >其他共有
        &nbsp;&nbsp;</@f.radio>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th  colspan="3">
        <@f.label name="申请证书版式"></@f.label>
    </@f.th>
    <@f.td colspan="4">
        <@f.radio id="sqzsbs"name="sqzsbs" valueFieldName="单一版"  saveValue="${bdcXm.sqzsbs!}" defaultValue="单一版">
        单一版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</@f.radio>
        <@f.radio id="sqzsbs"name="sqzsbs" valueFieldName="集成板" saveValue="${bdcXm.sqzsbs!}"  >
        集成板</@f.radio>
    </@f.td>
    <@f.th  colspan="2">
        <@f.label name="申请分别持证"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.radio id="sqfbcz" name="sqfbcz" valueFieldName="是"  saveValue="${bdcXm.sqfbcz!}" defaultValue="是">
        是&nbsp;&nbsp;</@f.radio>
        <@f.radio id="sqfbcz"name="sqfbcz" valueFieldName="否" saveValue="${bdcXm.sqfbcz!}">
        否</@f.radio>
    </@f.td>
</@f.tr>