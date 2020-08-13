<tbody id="bdcdyqk">
  <@f.tr>
      <@f.th rowspan="6" colspan="1">
          <@f.label name="                    不<br/>动<br/>产<br/>单<br/>元<br/>情<br/>况                   "></@f.label>
      </@f.th>
    <@f.th colspan="3">
        <@f.label name="产权来源"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.text  id="cqly" name="cqly" value="${bdcFdcq.cqly!}"></@f.text>
    </@f.td>
    <@f.th colspan="2" style="width:60px">
        <@f.label name="房屋性质"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="fwxz" name="fwxz"  showFieldName="MC" valueFieldName="DM" source="fwxzList" defaultValue="${bdcFdcq.fwxz!}" ></@f.select>
    </@f.td>
</@f.tr>
  <@f.tr>
    <@f.th colspan="3">
        <@f.label name="房屋结构"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="fwjg" name="fwjg"  showFieldName="MC" valueFieldName="DM" source="fwjgList" defaultValue="${bdcFdcq.fwjg!}" ></@f.select>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="竣工时间"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.date  id="jgsj" name="jgsj" value="${bdcFdcq.jgsj?date}"></@f.date>
    </@f.td>
</@f.tr>
  <@f.tr>
    <@f.th colspan="3">
        <@f.label name="规划用途"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="dzwyt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcSpxx.yt!}" ></@f.select>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="总层数"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.text  id="zcs" name="zcs" value="${bdcFdcq.zcs!}"></@f.text>
    </@f.td>
</@f.tr>
  <@f.tr>
    <@f.th colspan="3">
        <@f.label name="宗地/宗海面积"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.text  id="zdzhmj" name="zdzhmj" value="${bdcSpxx.zdzhmj!}"></@f.text>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="定着物建筑总面积"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.text  id="zmj" name="zmj" value="${zmj!}"></@f.text>
    </@f.td>
</@f.tr>
  <@f.tr>
    <@f.th colspan="1">
        <@f.label name="土地用途"></@f.label>
    </@f.th>
    <@f.td colspan="2">
        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdzhytList"defaultValue="${bdcSpxx.zdzhyt!}" ></@f.select>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="土地使用开始期限"></@f.label>
    </@f.th>
    <@f.td colspan="2">
        <@f.date  id="tdsyksqx" name="tdsyksqx" value="${tdsyksqx!}"></@f.date>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="土地使用结束期限"></@f.label>
    </@f.th>
    <@f.td colspan="2">
        <@f.date  id="tdsyjsqx" name="tdsyjsqx" value="${tdsyjsqx!}"></@f.date>
    </@f.td>
</@f.tr>
</tbody>