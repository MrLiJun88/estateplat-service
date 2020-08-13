<#if (bdcdyly==3 && sqlxdm != "8009902" && sqlxdm != "8009904")>
    <div id="qlxx" class="tab-pane in active">
<#else>
    <div id="qlxx" class="tab-pane">
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                <#--<input type="text" class="SSinput watermarkText" id="qlxx_search" data-watermark="权利人/坐落/不动产单元号">-->
                    <input type="text" class="SSinput watermarkText" id="qlxx_search"
                           data-watermark="请输入被查封权利人/坐落/不动产单元号/查封文号/产权证号">
                </td>
                <td class="Search">
                    <a href="#" id="qlxx_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
            </tr>
        </table>
    </div>
    <table id="qlxx-grid-table"></table>
    <div id="qlxx-grid-pager"></div>
</div>