<#if bdcdyly==6 >
    <div id="gdtdsj" class="tab-pane in active">
<#else>
    <div id="gdtdsj" class="tab-pane">
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="gdtdsj_search"
                           data-watermark="请输入权利人/坐落/土地证号">
                </td>
                <td class="Search">
                    <a href="#" id="gdtdsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="gdtdsjShow">高级搜索</button>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton"onclick=show("gdtdSearchShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
  <div class="simpleSearch" id="gdtdSearchShow" style="display:none">
      <table cellpadding="0" cellspacing="0" border="0">
          <tr>
              <td style="border: 0px">不动产单元号</td>
              <td style="border: 0px"><input type="text" name="gdtdBdcdyh" id="gdtdBdcdyh"  style="width: 260px;"></td>
          </tr>
      </table>
  </div>
    <table id="gdtdsj-grid-table"></table>
    <div id="gdtdsj-grid-pager"></div>
</div>