<#if  (bdcdyly==4 && sqlxdm != "8009901" && sqlxdm != "8009903") || bdcdyly==0>
    <div id="djsj" class="tab-pane in active">
<#else>
    <div id="djsj" class="tab-pane">
</#if>
  <form class="form advancedSearchTable" id="djsjSearchForm">
      <div class="row">
          <div class="col-xs-1" style="width: 100px;text-align: right;">
              <label>权利人：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <input type="text" id="qlrdjsj" style="width: 200px;height: 30px;">
          </div>
          <div class="col-xs-1" style="width: 80px;text-align: right;">
              <label>坐落：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <input type="text" id="tdzldjsj" style="width: 200px;height: 30px;">
          </div>
          <div class="col-xs-1" style="width: 130px;text-align: right;">
              <label>不动产单元号：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <input type="text" id="bdcdyhdjsj" style="width: 200px;height: 30px;">
          </div>
      </div>
      <div class="row">
          <div class="col-xs-1" style="width: 100px;text-align: right;">
              <label>房屋代码：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <input type="text" id="fwbhdjsj" style="width: 200px;height: 30px;">
          </div>
          <div class="col-xs-1" style="width: 80px;text-align: right;">
              <label>地籍号：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <input type="text" id="djhdjsj" style="width: 200px;height: 30px;">
          </div>
          <div class="col-xs-1" style="width: 130px;text-align: right;">
              <label>不动产类型：</label>
          </div>
          <div class="col-xs-2" style="width: 200px;height: 30px;">
              <select id="bdclxSelect" name="bdclx" class="form-control" style="margin-left: 5px;width: 200px;height: 30px;">
                    <#list bdclxList! as bdclx>
                        <option value="${bdclx.DM!}">${bdclx.MC}</option>
                    </#list>
              </select>
          </div>
      </div>
<#--      <div class="row">-->
<#--          <div class="col-xs-1" style="width: 100px;text-align: right;">-->
<#--              <label>合同编号：</label>-->
<#--          </div>-->
<#--          <div class="col-xs-2" style="width: 200px;height: 30px;">-->
<#--              <input type="text" id="htbhdjsj" style="width: 200px;height: 30px;">-->
<#--          </div>-->
<#--      </div>-->
      <div class="row">
          <div class="col-xs-1" style="width: 270px;">
              <button type="button" class="btn btn-sm btn-primary"
                      id="djsj_search_btn">搜&nbsp;&nbsp;索
                  <i class="ace-icon fa fa-search bigger-130"></i>
              </button>
              <#--<label><input type="checkbox" id="exactQuerydjsj"/>精确查询</label>-->
          </div>
      </div>
  </form>
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