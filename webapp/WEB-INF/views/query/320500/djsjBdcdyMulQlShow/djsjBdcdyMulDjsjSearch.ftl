<div id="djsj" class="tab-pane">
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="djsj_search"
                           data-watermark="请输入权利人/坐落/不动产单元号">
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
                    <button type="button" class="btn01 AdvancedButton" onclick=show("bdclxZxShow")>
                        <i class="ace-icon fa fa-plus bigger-130"></i></button>
                </td>
            </tr>
        </table>
    </div>
    <div class="simpleSearch" id="bdclxZxShow" style="display:none">
    <#--<input type="text" id="checkboxSelect" style="width: 200px;height: 34px;"/>-->
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="border: 0px">
                    <select name="bdclx" id="bdclxSelect" class="form-control" style="width: 150px;"
                            onchange="changeBdclx()">
                                            <#list bdclxList! as bdclx>
                                                <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                            </#list>
                    </select>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td style="border: 0px">
                    <select name="bdclxZx" id="bdclxZxSelect" class="form-control"
                            style="width: 150px;">
                        <option value="">请选择...</option>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="djsjSure">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>确定</span>
                </button>
            </li>
            <li>
                <button type="button" id="djsjMulXx">
                    <span>已选择</span>
                </button>
            </li>
                                <#if bdcdyly==4>
                                    <li>
                                        <button type="button" name="addHhcf" id="addBdcdy">
                                            <span name="shoppingCartButton"></span>
                                        </button>
                                    </li>
                                </#if>
        </ul>
    </div>
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>