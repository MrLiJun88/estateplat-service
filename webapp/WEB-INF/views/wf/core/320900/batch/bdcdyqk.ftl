<tbody id="bdcdyqk">
<@f.tr id="tr_ywr">
    <@f.th rowspan="7" colspan="1">
        <@f.label name="                    不<br/>动<br/>产<br/>情<br/>况                   "></@f.label>
    </@f.th>
    <@f.th colspan="3">
        <@f.label name="坐落"></@f.label>
    </@f.th>
    <@f.td colspan="8">
        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>                        </@f.td>
</@f.tr>
                <@f.tr>
    <@f.th colspan="3">
        <@f.label name="不动产单元号"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.textarea  id="bdcdyh" name="bdcdyh" value="${bdcSpxx.bdcdyh!}"  style="overflow-y: hidden;line-height: 15px;height:29px;"></@f.textarea>
    </@f.td>
    <@f.th colspan="2" style="width:60px">
        <@f.label name="不动产类型"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="bdclx" name="bdclx"  showFieldName="MC" valueFieldName="DM" source="bdclxList"defaultValue="${bdcSpxx.bdclx!}" ></@f.select>
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
        <@f.label name="宗地/宗海用途"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdzhytList"defaultValue="${bdcSpxx.zdzhyt!}" ></@f.select>
    </@f.td>
</@f.tr>
                <@f.tr>
    <@f.th colspan="3">
        <@f.label name="定着物面积"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.text  id="djzwmj" name="mj" value="${bdcSpxx.mj!}"></@f.text>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="定着物用途"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="dzwyt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcSpxx.yt!}" ></@f.select>
    </@f.td>
</@f.tr>
                <@f.tr>
    <@f.th colspan="3">
        <@f.label name="宗地/宗海权利性质"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="zdzhqlxz" name="zdzhqlxz"  showFieldName="MC" valueFieldName="DM" source="zdzhqlxzList"defaultValue="${bdcSpxx.zdzhqlxz!}" ></@f.select>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="用海类型"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="yhlx" name="yhlx"  showFieldName="MC" valueFieldName="DM" source="yhlxList"defaultValue="${bdcSpxx.yhlx!}" ></@f.select>
    </@f.td>
</@f.tr>
                <@f.tr>
    <@f.th colspan="3">
        <@f.label name="构筑物类型"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="gzwlx" name="gzwlx"  showFieldName="MC" valueFieldName="DM" source="gzwlxList"defaultValue="${bdcSpxx.gzwlx!}" ></@f.select>
    </@f.td>
    <@f.th colspan="2">
        <@f.label name="林种"></@f.label>
    </@f.th>
    <@f.td colspan="3">
        <@f.select id="lz" name="lz"  showFieldName="MC" valueFieldName="DM" source="lzList"defaultValue="${bdcSpxx.lz!}"></@f.select>
    </@f.td>
</@f.tr>
                <@f.tr>
    <@f.th colspan="3">
        <@f.label name="原不动产权证书号"></@f.label>
    </@f.th>
    <@f.td colspan="8">
        <@f.text  id="ybdcqzh" name="ybdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
    </@f.td>
</@f.tr>
</tbody>