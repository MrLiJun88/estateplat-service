<style>
    #myModal .modal-content {
        overflow-y: hidden;
        overflow-x: hidden;
        width: 860px;
        height:450px;
    }

    .tabbable {
        margin-left: 5px;
        height:400px;
    }
    .tab-content {
        height:400px;
        overflow-x: hidden;
    }
</style>
<div class="Pop-upBox  modal bootbox fade bootbox-prompt in newPro" style="display: none" id="myModal">
    <div class="modal-dialog newPro-modal sdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>选择权利人</h4>
                <button type="button" onclick="closeModal()"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="tabbable">
                <div class="tab-content">
                    <div id="djsj" class="tab-pane in active">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="ryxx_search"
                                               data-watermark="请输入权利人">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="ryxx_search_btn">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <table id="ryxx-grid-table"></table>
                        <div id="ryxx-grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


