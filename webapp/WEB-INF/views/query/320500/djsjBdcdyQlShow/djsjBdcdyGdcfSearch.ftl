<#if bdcdyly==3 || bdcdyly==4 >
    <#if (sqlxdm == "8009902" || sqlxdm == "8009904")>
        <div id="gdcfsj" class="tab-pane in active">
    <#else>
        <div id="gdcfsj" class="tab-pane">
    </#if>
<#else>
    <div id="qlxx" class="tab-pane">
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="gdcfsj_search"
                           data-watermark="请输入查封文号/坐落/产权证号">
                </td>
                <td class="Search">
                    <a href="#" id="gdcfsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton"onclick=show("gdcfSearchShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
  <div class="simpleSearch" id="gdcfSearchShow" style="display:none">
      <table cellpadding="0" cellspacing="0" border="0">
          <tr>
              <td style="border: 0px">不动产单元号</td>
              <td style="border: 0px"><input type="text" name="gdcfBdcdyh" id="gdcfBdcdyh"  style="width: 260px;"></td>
          </tr>
      </table>
  </div>
    <table id="gdcfsj-grid-table"></table>
    <div id="gdcfsj-grid-pager"></div>
</div>