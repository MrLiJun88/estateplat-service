<#if (bdcdyly==7 && sqlxdm!="4009903" )|| (bdcdyly==8 && sqlxdm != "4009901" && sqlxdm != "4009902") || bdcdyly==1>
    <div id="ywsj" class="tab-pane in active">
<#else>
    <#if bdcdyly==1>
        <div id="ywsj" class="tab-pane in active">
    <#else>
        <div id="ywsj" class="tab-pane">
    </#if>
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="ywsj_search"
                           data-watermark="请输入不动产权证号/权利人/坐落/不动产单元号">
                </td>
                <td class="Search">
                    <a href="#" id="ywsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="ywsjShow">高级搜索</button>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton"onclick=show("zslxShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
    <div class="simpleSearch" id="zslxShow" style="display:none">
    <#--<input type="text" id="checkboxSelect" style="width: 200px;height: 34px;"/>-->
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="border: 0px">
                    <select name="zslx" id="zslxSelect" class="form-control" style="width: 150px;">
                        <option value="">请选择...</option>
                        <option value="zs">不动产权证书</option>
                        <option value="zms">不动产权证明书</option>
                        <option value="zmd">不动产权证明单</option>
                    </select>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td style="border: 0px">发证日期(起)</td>
                <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzqssj" id="fzqssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
                <td style="border: 0px">发证日期(至)</td>
                <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzjssj" id="fzjssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
            </tr>
        </table>
    </div>
    <table id="ywsj-grid-table"></table>
    <div id="ywsj-grid-pager"></div>
</div>