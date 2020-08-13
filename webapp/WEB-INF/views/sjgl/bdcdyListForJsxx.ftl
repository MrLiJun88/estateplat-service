<style>
    #myModal .modal-content{
        overflow-y: hidden;
        overflow-x:hidden;
        width: 930px;
        height:435px;
        margin-top: -10px;
    }
    .tabbable{
        margin-left: 5px;
    }
    #myModal .modal-content .tab-content{
        height:450px;
    }
</style>
<div class="Pop-upBox  modal bootbox fade bootbox-prompt in newPro" style="display: none" id="myModal">
    <div class="modal-dialog newPro-modal sdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>选择不动产单元</h4>
                <button type="button" onclick="closeModal()"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a data-toggle="tab" id="djsjTab" href="#djsj">
                            不动产单元
                        </a>
                    </li>
                    <li class="">
                        <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                            不动产权证
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="djsj" class="tab-pane in active">
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
                                </tr>
                            </table>
                        </div>
                        <table id="djsj-grid-table"></table>
                        <div id="djsj-grid-pager"></div>
                    </div>
                    <div id="ywsj" class="tab-pane">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="ywsj_search"
                                               data-watermark="请输入权利人/坐落/不动产单元号/房屋编号">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="ywsj_search_btn">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <table id="ywsj-grid-table"></table>
                        <div id="ywsj-grid-pager"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>


