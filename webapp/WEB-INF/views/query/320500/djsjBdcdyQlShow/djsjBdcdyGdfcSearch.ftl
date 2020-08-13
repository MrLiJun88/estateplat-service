<#if (bdcdyly==5 || (bdcdyly==8 && (sqlxdm == "4009901" || sqlxdm == "4009902")) || (bdcdyly=7 && sqlxdm=="4009903"))>
    <div id="gdfcsj" class="tab-pane in active">
<#else>
    <div id="gdfcsj" class="tab-pane">
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="gdfcsj_search"
                           data-watermark="请输入权利人/坐落/房产证号">
                </td>
                <td class="Search">
                    <a href="#" id="gdfcsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="gdfcsjShow">高级搜索</button>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton"onclick=show("gdfcSearchShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
  <div class="simpleSearch" id="gdfcSearchShow" style="display:none">
      <table cellpadding="0" cellspacing="0" border="0">
          <tr>
              <td style="border: 0px">不动产单元号</td>
              <td style="border: 0px"><input type="text" name="gdfcBdcdyh" id="gdfcBdcdyh"  style="width: 260px;"></td>
          </tr>
      </table>
  </div>
    <table id="gdfcsj-grid-table"></table>
    <div id="gdfcsj-grid-pager"></div>
</div>