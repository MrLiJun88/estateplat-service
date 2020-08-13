<@com.html title="不动产登记业务管理系统" import="bui,ace,init">
<style>
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
    .AdvancedSearchForm {
        position: absolute;
        top: 10px;
        left: 48px;
        z-index: 9999;
        display: none;
    }

    .Advanced .modal-backdrop {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1;
        background-color: #000;
        opacity: 0.5;
        filter: alpha(opacity = 50);
        display: none;
    }

    .Advanced .AdvancedLab {
        display: block;
        margin: 0;
        background: #f5f5f5;
        font-size: 12px;
        border-top: 1px solid #ddd;
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        padding: 0px 20px 10px 20px;
        position: absolute;
        top: -57px;
        left: 486px;
        z-index: 3;
        width: 90px;
        line-height: 25px;
    }

    .Advanced {
        position: relative;
        margin: 0px 0px 10px 0px;
    }

    .AdvancedSearchForm .form-base {
        padding: 20px 20px 20px 20px;
        border: 1px solid #ddd;
        background: #f5f5f5;
        width: 623px;
        position: absolute;
        top: -22px;
        left: -47px;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }

        /*高级搜索样式添加 end*/
    body{
        background-color: #ffffff;
    }
    a{
        color: #428bca;
    }
</style>
<script type="text/javascript">
    /*   文字水印  */
    $(function () {


        $("#hide").click(function () {
            $(".SearchFloat").hide();
        });
        $("#show").click(function () {
            $(".SearchFloat").show();
        });
    });
    /* 调用子页面方法  */
    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }
    function hideModal() {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
        $("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
    }
    var onmessage = function (e) {
        showModal();
    };

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    $(function () {
        var proidTemp = '';
        if ($("#proid").val() != '') {
            proidTemp = $("#proid").val();
        }
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var index = 0;
        var proid = $("#proid").val();
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/dcxx/getJzbsbPagesJson?proid="+proid,
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'proid'},
            colNames: ["界址点号", "X坐标", 'Y坐标'],
            colModel: [
                {name: 'JZDH', index: 'JZDH', width: '200px', sortable: false},
                {name: 'XZBZ', index: 'XZBZ', width: '221.5px', sortable: false},
                {name: 'YZBZ', index: 'YZBZ', width: '221.5px', sortable: false}
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:false,
            /*rownumbers:true,*/
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
            },
            ondblClickRow:function (rowid) {
//                var rowData = $(grid_selector).getRowData(rowid);
//                if(rowData!=null)
//                    EditXm(rowid,rowData.BDCLX,rowData.BDCDYH);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
//        $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+":this.getMonth() + 1, //月份
                "d+":this.getDate(), //日
                "h+":this.getHours(), //小时
                "m+":this.getMinutes(), //分
                "s+":this.getSeconds(), //秒
                "q+":Math.floor((this.getMonth() + 3) / 3), //季度
                "S":this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    });

    //修改项目信息的函数
    function EditXm(id,bdclx,bdcdyh) {
        var proid='';
        if($("#proid").val()!=''){
            proid=$("#proid").val();
        }

        $.ajax({
            type:'get',
            url:'${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid+'&djId='+id+"&bdclx="+bdclx+"&bdcdyh="+bdcdyh,
            success:function (data) {

                if (data == '成功') {
                    window.parent.hideModel();
                    window.parent.resourceRefresh();

                }else{
                    alert(data);
                }
            },
            error:function (_ex) {
                alertError("保存失败!失败原因:" + _ex);
            }
        });
    }

</script>
<div align="center" class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="space-10"></div>
    <div class="Advanced">
    </div>

    <table id="grid-table"></table>
    <div id="grid-pager"></div>
</div>
</div>
<div align="center" class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
<#--<div class="modal-dialog newPro-modal">-->
        <#--<iframe id="myModalFrame" name="myModalFrame" src="${bdcdjUrl!}/bdcSjgl/toAddBdcxm" width=100% height="560px"-->
                <#--class="iframeStyle" frameborder="0"></iframe>-->
    <#--</div>-->
</div>
<div align="center" class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div align="center" id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
