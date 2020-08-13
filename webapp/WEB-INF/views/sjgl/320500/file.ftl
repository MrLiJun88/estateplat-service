<#--文件选择框-->
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle">选择文件（请确认文件为.xls格式）</h4>
                <button type="button" id="fileHide" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <td><input type="file" id="fileWindow" name="file"
                                                   style="width: 280px;height:34px"/></td>
                                        <input type="hidden" name="sjlx" id="sjlx">
                                        <td><input type="submit" id="fileSub" class="filesub" value="导入"/></td>
                                        <td><a href="" class="filesub" id="fileDownLoad">模板下载</a></td>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <table id="bdcxm-grid-table"></table>
                    <div id="bdcxm-grid-pager"></div>
                </div>
            </div>

        <#--</div>-->
        </div>
    </div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput1">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle1">关联项目</h4>
                <button type="button" id="fileHide1" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw1">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm1" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="text" style="width: 265px" class="SSinput watermarkText"
                                                       id="bdcxm_search_qlr" data-watermark="受理编号"></td>
                                            <td class="Search"><a href="#" id="bdcxm_search"> 搜索<i
                                                    class="ace-icon fa fa-search bigger-130"></i></a></td>
                                        </tr>
                                    </table>
                                </form>
                            </div>
                        </div>
                        <table id="bdcxm-grid-table1"></table>
                        <div id="bdcxm-grid-pager1"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>