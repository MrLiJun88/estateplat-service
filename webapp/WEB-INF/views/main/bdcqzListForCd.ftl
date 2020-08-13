<style>
    tab-content{
        overflow-y: hidden;
        height: 560px;
    }

</style>
<script type="text/javascript">

</script>

<div class="main-container" id="myModal">
    <div class="tabbable">
        <ul class="nav nav-tabs">
            <li class="active">
                <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                    产权证
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" style="overflow-y: hidden;height: 560px">
        <div id="ywsj" class="tab-pane in active">
            <form class="form advancedSearchTable" id="ywsjSearchForm">
                <div class="row">
                    <div class="col-xs-1" style="width: 150px;text-align: right;">
                        <label>房屋代码：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="fwbmywsj" style="width: 180px;height: 30px;">
                    </div>
                    <div class="col-xs-1" style="width: 140px;text-align: right;">
                        <label>产权证号简称：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="cqzhjcywsj" style="width: 180px;height: 30px;">
                    </div>
                    <div class="col-xs-1" style="width: 100px;text-align: right;">
                        <label>权利人：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="qlrywsj" style="width: 180px;height: 30px;">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-1" style="width: 150px;text-align: right;">
                        <label>不动产权证号：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="bdcqzhgrid" style="width: 180px;height: 30px;">
                    </div>
                    <div class="col-xs-1" style="width: 140px;text-align: right;">
                        <label>不动产单元号：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="bdcdyhywsj" style="width: 180px;height: 30px;">
                    </div>
                    <div class="col-xs-1" style="width: 100px;text-align: right;">
                        <label>坐落：</label>
                    </div>
                    <div class="col-xs-2" style="width: 180px;height: 30px;">
                        <input type="text" id="zlywsj" style="width: 180px;height: 30px;">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-1" style="width: 270px;">
                        <button type="button" class="btn btn-sm btn-primary"
                                id="ywsj_search_btn">搜&nbsp;&nbsp;索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </button>
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
                        <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzqssj" id="fzqssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
                        <td style="border: 0px">发证日期(至)</td>
                        <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzjssj" id="fzjssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
                    </tr>
                </table>
            </div>
            <table id="ywsj-grid-table"></table>
            <div id="ywsj-grid-pager"></div>
        </div>
    </div>

</div>