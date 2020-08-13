<div id="ywsj" class="tab-pane">
    <form class="form advancedSearchTable" id="ywsjSearchForm">
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <#if (qllx == "18" && sqlxdm != "1001" && sqlxdm != "1019" && sqlxdm != "9999905" && sqlxdm != "9979902" && sqlxdm != "9999910") || sqlxdm == "9940002">
                    <label>不动产登记证明号：</label>
                <#else>
                    <label>不动产权证号：</label>
                </#if>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcqzh" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <#if (qllx == "18" && sqlxdm != "1001" && sqlxdm != "1019" && sqlxdm != "9999905" && sqlxdm != "9979902" && sqlxdm != "9999910") || sqlxdm == "9940002">
                    <label>登记证明号简称：</label>
                <#else>
                    <label>产权证号简称：</label>
                </#if>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="cqzhjcywsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 80px;text-align: right;">
                <label>坐落：</label>
            </div>
            <div class="col-xs-2" style="width: 190px;height: 30px;">
                <input type="text" id="zlywsj" style="width: 190px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>房屋代码：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="fwbmywsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产单元号：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <input type="text" id="bdcdyhywsj" style="width: 200px;height: 30px;">
            </div>
            <div class="col-xs-1" style="width: 80px;text-align: right;">
                <label>权利人：</label>
            </div>
            <div class="col-xs-2" style="width: 190px;height: 30px;">
                <input type="text" id="qlrywsj" style="width: 190px;height: 30px;">
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 130px;text-align: right;">
                <label>不动产类型：</label>
            </div>
            <div class="col-xs-2" style="width: 200px;height: 30px;">
                <select id="bdclx" name="bdclx" class="form-control"
                        style="margin-left: 5px;width: 200px;height: 30px;">
                    <#list bdclxList! as bdclx>
                        <option value="${bdclx.DM!}">${bdclx.MC}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-1" style="width: 270px;">
                <button type="button" class="btn btn-sm btn-primary"
                        id="ywsj_search_btn">搜&nbsp;&nbsp;索
                    <i class="ace-icon fa fa-search bigger-130"></i>
                </button>
                <#--<label><input type="checkbox" id="exactQueryywsj"/>精确查询</label>-->
            </div>
        </div>
    </form>
    <div class="simpleSearch" id="zslxShow" style="display:none">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td style="border: 0px">&nbsp;&nbsp;</td>
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
                <td style="border: 0px"><input type="text" class="date-picker form-control"
                                               name="fzqssj" id="fzqssj"
                                               data-date-format="yyyy-mm-dd" style="width: 150px;">
                </td>
                <td style="border: 0px">发证日期(至)</td>
                <td style="border: 0px"><input type="text" class="date-picker form-control"
                                               name="fzjssj" id="fzjssj"
                                               data-date-format="yyyy-mm-dd" style="width: 150px;">
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="ywsjSure">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>确定</span>
                </button>
            </li>
            <li>
                <button type="button" id="ywsjMulXx">
                    <span>已选择</span>
                </button>
            </li>
            <#if bdcdyly==4||bdcdyly==7>
                <li>
                    <button type="button" name="addHhcf" id="addBdcqz">
                        <span name="shoppingCartButton"></span>
                    </button>
                </li>
            </#if>
            <li>
                <button type="button" id="ywsjSureMul">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>批量创建</span>
                </button>
            </li>

        </ul>
    </div>
    <table id="ywsj-grid-table"></table>
    <div id="ywsj-grid-pager"></div>
</div>