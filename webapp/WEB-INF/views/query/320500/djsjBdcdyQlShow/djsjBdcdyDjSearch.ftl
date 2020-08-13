<#if bdcdyly==2 || (bdcdyly==4 && sqlxdm != "8009901" && sqlxdm != "8009903") || bdcdyly==0>
    <div id="djsj" class="tab-pane in active">
<#else>
    <div id="djsj" class="tab-pane">
</#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="djsj_search"
                           data-watermark="请输入权利人/坐落/不动产单元号/房屋编号">
                </td>
                <td class="Search">
                    <a href="#" id="djsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="djsjShow">高级搜索</button>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton"onclick=show("bdclxZxShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
    <div class="simpleSearch" id="bdclxZxShow" style="display:none">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="border: 0px">
                    <select name="bdclx" id="bdclxSelect" class="form-control" style="width: 150px;" onchange="changeBdclx()">
                                            <#list bdclxList! as bdclx>
                                                <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                            </#list>
                    </select>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td style="border: 0px">
                    <select name="bdclxZx" id="bdclxZxSelect" class="form-control" style="width: 150px;">
                        <option value="">请选择...</option>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>