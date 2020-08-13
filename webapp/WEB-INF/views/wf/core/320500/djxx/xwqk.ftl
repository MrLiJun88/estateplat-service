<@f.tr>
    <@f.th rowspan="5">
        <@f.label class="leftLabel" name="询问事项"></@f.label>
    </@f.th>
    <@f.th colspan="3">
        <@f.label name="1．申请登记的不动产是否共有？"></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.radio id="bdcsfgy" name="bdcsfgy" valueFieldName="1"  saveValue="${bdcsfgy!}" defaultValue="1">
        是&nbsp;&nbsp;</@f.radio>
        <@f.radio name="bdcsfgy" valueFieldName="0" saveValue="${bdcsfgy!}">
        否</@f.radio>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th colspan="3">
        <@f.label name="2．共有类型："></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.select id="gylx" name="gylx"  showFieldName="MC" valueFieldName="DM"  source="gyfsList" defaultValue="${gyfs!}"></@f.select>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th colspan="3">
        <@f.label name="3、共有人是否申请分别持证？"></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.radio id="gyrsffbcz" name="gyrsffbcz" valueFieldName="1"  saveValue="${gyrsffbcz!}" defaultValue="1">
        是&nbsp;&nbsp;</@f.radio>
        <@f.radio name="gyrsffbcz" valueFieldName="0" saveValue="${gyrsffbcz!}">
        否</@f.radio>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th colspan="3">
        <@f.label name="4、其他说明"></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.text  id="" name="" value=""></@f.text>
    </@f.td>
</@f.tr>
<@f.tr>
    <@f.th colspan="3">
        <@f.label name="权利人（代理人）签章："></@f.label>
    </@f.th>
    <@f.td colspan="9">
        <@f.text  id="" name="" value=""></@f.text>
    </@f.td>
</@f.tr>