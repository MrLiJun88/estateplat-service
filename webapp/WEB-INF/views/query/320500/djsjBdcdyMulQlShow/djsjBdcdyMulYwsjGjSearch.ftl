<!--业务高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="ywsjSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>业务高级查询</h4>
                <button type="button" id="ywsjHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="ywsjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label> 土地坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="ywsjGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>