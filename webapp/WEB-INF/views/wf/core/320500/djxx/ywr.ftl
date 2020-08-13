
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
            <@f.td colspan="3" style="border-right-style:none;">
                <@f.text id="qlbl_${ywr.qlrid!}" name="qlbl" value="${ywr.qlbl!}" ></@f.text>
            </@f.td>
            <@f.td colspan="1" style="border-left-style:none;">
            <a onclick="qlrDel('${ywr.qlrid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
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
            <@f.td colspan="4">
                <@f.text id="qlrzjh_${ywr.qlrid!}" name="qlrzjh" value="${ywr.qlrzjh!}" ></@f.text>
            </@f.td>
        </@f.tr>
      <#if showRows=="true">
          <@f.tr  id="tr_${ywr.qlrid!}" >
              <@f.th colspan="3"  >
                  <@f.label name="代理人"></@f.label>
              </@f.th>
              <@f.td colspan="3">
                  <@f.text id="qlrdlr_${ywr.qlrid!}" name="qlrdlr" value="${ywr.qlrdlr!}" ></@f.text>
              </@f.td>
              <@f.th colspan="2"  >
                  <@f.label name="联系地址或电话"></@f.label>
              </@f.th>
              <@f.td colspan="4">
                  <@f.text id="qlrdlrdh_${ywr.qlrid!}" name="qlrdlrdh" value="${ywr.qlrdlrdh!}" ></@f.text>
              </@f.td>
          </@f.tr>
          <@f.tr  id="tr_${ywr.qlrid!}" >
              <@f.th colspan="3"  >
                  <@f.label name="证件类型"></@f.label>
              </@f.th>
              <@f.td colspan="3">
                  <@f.select id="qlrdlrzjzl_${ywr.qlrid!}" name="qlrdlrzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="${ywr.qlrdlrzjzl!}" noEmptyValue="true"></@f.select>
              </@f.td>
              <@f.th colspan="2"  >
                  <@f.label name="证件号"></@f.label>
              </@f.th>
              <@f.td colspan="4">
                  <@f.text id="qlrdlrzjh_${ywr.qlrid!}" name="qlrdlrzjh" value="${ywr.qlrdlrzjh!}" ></@f.text>
              </@f.td>
          </@f.tr>
      </#if>
    </#list>
<#else>
    <@f.tr id="tr_def2" >
        <@f.hidden id="qlrid" name="qlrid" value="def2"/>
        <@f.hidden id="qlrlx_def2" name="qlrlx" value="ywr"/>
        <@f.th colspan="3"  >
            <@f.label name="义务人姓名（名称）"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.text id="qlrmc_def2" name="qlrmc" value="" ></@f.text>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="共有比例"></@f.label>
        </@f.th>
        <@f.td colspan="3" style="border-right-style:none;">
            <@f.text id="qlbl_def2" name="qlbl" value="" ></@f.text>
        </@f.td>
        <@f.td colspan="1" style="border-left-style:none;">
        <a onclick="qlrDel('def2')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
        </@f.td>
    </@f.tr>
    <@f.tr id="tr_def2" >
        <@f.th colspan="3"  >
            <@f.label name="身份证件种类"></@f.label>
        </@f.th>
        <@f.td colspan="3">
            <@f.select id="qlrsfzjzl_def2" name="qlrsfzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
        </@f.td>
        <@f.th colspan="2"  >
            <@f.label name="证件号"></@f.label>
        </@f.th>
        <@f.td colspan="4">
            <@f.text id="qlrzjh_def2" name="qlrzjh" value="" ></@f.text>
        </@f.td>
    </@f.tr>
    <#if showRows=="true">
        <@f.tr id="tr_def2" >
            <@f.th colspan="3"  >
                <@f.label name="代理人"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.text id="qlrdlr_def2" name="qlrdlr" value="" ></@f.text>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="联系地址或电话"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlrdlrdh_def2" name="qlrdlrdh" value="" ></@f.text>
            </@f.td>
        </@f.tr>
        <@f.tr id="tr_def2" >
            <@f.th colspan="3"  >
                <@f.label name="证件种类"></@f.label>
            </@f.th>
            <@f.td colspan="3">
                <@f.select id="qlrdlrzjzl_def2" name="qlrdlrzjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList"defaultValue="" noEmptyValue="true"></@f.select>
            </@f.td>
            <@f.th colspan="2"  >
                <@f.label name="证件号"></@f.label>
            </@f.th>
            <@f.td colspan="4">
                <@f.text id="qlrdlrzjh_def2" name="qlrdlrzjh" value="" ></@f.text>
            </@f.td>
        </@f.tr>
    </#if>

</#if>
