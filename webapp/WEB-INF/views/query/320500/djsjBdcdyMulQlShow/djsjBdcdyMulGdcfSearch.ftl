<div id="gdcfsj" class="tab-pane">
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
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="gdCfSure">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>确定</span>
                </button>
            </li>
            <li>
                <button type="button" id="gdCfMulXx">
                    <span>已选择</span>
                </button>
            </li>
                                <#if bdcdyly==4||bdcdyly==7>
                                    <li>
                                        <button type="button" name="addHhcf" id="addFcz">
                                            <span name="shoppingCartButton"></span>
                                        </button>
                                    </li>
                                </#if>
        </ul>
    </div>

    <table id="gdcfsj-grid-table"></table>
    <div id="gdcfsj-grid-pager"></div>
</div>