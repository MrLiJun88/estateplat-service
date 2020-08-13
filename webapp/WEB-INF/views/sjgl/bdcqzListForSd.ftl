<style>
    tab-content{
        overflow-y: hidden;
        height: 450px;
    }

</style>
<script type="text/javascript">

</script>

<div class="main-container" id="myModal">
    <div class="tabbable">
        <ul class="nav nav-tabs">
            <li class="active">
                <a data-toggle="tab" id="djsjTab" href="#djsj">
                    不动产权证
                </a>
            </li>
            <li class="">
                <a data-toggle="tab" id="fcsjTab" href="#fcsj">
                    房产证
                </a>
            </li>
            <li class="">
                <a data-toggle="tab" id="tdsjTab" href="#tdsj">
                    土地证
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" style="overflow-y: hidden;height: 450px">
        <div id="djsj" class="tab-pane in active">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="SSinput watermarkText" id="ywsj_search"
                                   data-watermark="请输入不动产权证号/坐落/不动产单元号">
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
            <table id="djsj-grid-table"></table>
            <div id="djsj-grid-pager"></div>
        </div>
        <div id="fcsj" class="tab-pane">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="SSinput watermarkText" id="fcsj_search"
                                   data-watermark="请输入房产证号/坐落">
                        </td>
                        <td class="Search">
                            <a href="#" id="fcsj_search_btn">
                                搜索
                                <i class="ace-icon fa fa-search bigger-130"></i>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="fcsj-grid-table"></table>
            <div id="fcsj-grid-pager"></div>
        </div>
        <div id="tdsj" class="tab-pane">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="SSinput watermarkText" id="tdsj_search"
                                   data-watermark="请输入土地证号/坐落">
                        </td>
                        <td class="Search">
                            <a href="#" id="tdsj_search_btn">
                                搜索
                                <i class="ace-icon fa fa-search bigger-130"></i>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="tdsj-grid-table"></table>
            <div id="tdsj-grid-pager"></div>
        </div>
        <div id="tdsj" class="tab-pane in active">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="SSinput watermarkText" id="tdsj_search"
                                   data-watermark="请输入不动产权证号/坐落/不动产单元号">
                        </td>
                        <td class="Search">
                            <a href="#" id="tdsj_search_btn">
                                搜索
                                <i class="ace-icon fa fa-search bigger-130"></i>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="tdsj-grid-table"></table>
            <div id="tdsj-grid-pager"></div>
        </div>
    </div>

</div>