<@com.html title="必填字段表格" import="ace">
<style>
    .new-modal{
        width: 600px;
    }
    .ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }
    .modal-dialog{
        width:650px;
        margin:30px auto;
    }
    .bootbox{
        overflow: auto;
    }
</style>
<script>
    $(function(){
        $("#limitTableSaveBtn").click(function(){
            var options = {
                url:'${bdcdjUrl}/bdcConfig/saveLimitTable',
                type:'post',
                dataType:'json',
                data:$("#limitTableForm").serialize(),
                success:function (data) {
                    alert(data.result);
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    //此处可以添加对查询数据的合法验证
                    $("#limit-table-grid-table").jqGrid('setGridParam',{
                        datatype:'json',
                        page:1
                    }).trigger("reloadGrid"); //重新载入
                    $('#limitTableForm')[0].reset();
                },
                error:function (data) {
                    alert("保存失败");
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    $('#limitTableForm')[0].reset();
                }
            };
            var logValidate= $("#limitTableForm").validate();
            if(logValidate.form()){
                $.ajax({
                    type: "post",
                    url: "${bdcdjUrl}/bdcConfig/validateSql?sqls="+$("#tableXmrelSql").val(),
                    dataType: "json",
                    success: function (data) {
                        if(data.result){
                            $.ajax(options);
                            return false;
                        }else{
                           alert("SQL错误");
                        }
                    },
                    error: function (data) {
                    }
                });
            }
        })
        /*表单校验 begin*/
        $.validator.setDefaults({
            showErrors: function(map, list) {
                // there's probably a way to simplify this
                this.currentElements.removeAttr("title").removeAttr("style");
                $(this.currentElements).tooltip("destroy");
                $.each(list, function(index, error) {
                    $(error.element).attr("title", error.message).attr("style", "border:1px dotted red;");;
                    $(error.element).tooltip({placement:"bottom"});
                });
            }
        });
        jQuery.extend(jQuery.validator.messages, {
            required: "必填字段"
        });
        /*表单校验 end*/
        $(".hideButton").click(function () {
            $(".Pop").hide();
        });
        //生成表格
        limitTableConfigGrid();


        //limitTableConfig新增
        $("#addLimitTableConfig").click(function () {
            $("#limitTablePop,#modal-backdrop-pop").show();
        });
        //logConfig删除
        $("#delLimitTableConfig").click(function () {
            var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
            delRule(ids,"${bdcdjUrl}/bdcConfig/delLimitTableConfig","limit-table-grid-table");
        })
        $("#selTable").click(function(){
            var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
            if(ids.length==0){
                bootbox.dialog({
                    message: "<h3><b>请选择一条数据!</b></h3>",
                    title: "",
                    buttons: {
                        main: {
                            label: "关闭",
                            className: "btn-primary",
                        }
                    }
                });
                return;
            }
            if(ids.length>1){
                bootbox.dialog({
                    message: "<h3><b>只能同时选择一条数据!</b></h3>",
                    title: "",
                    buttons: {
                        main: {
                            label: "关闭",
                            className: "btn-primary",
                        }
                    }
                });
                return;
            }
            window.parent.selTable(ids);
        })
        $("#closeTable").click(function(){
            window.parent.selTable("");
        })
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#limitTableContent").width()>0){
                contentWidth=$("#limitTableContent").width();
            }
            $("#limit-table-grid-table").jqGrid('setGridWidth',contentWidth);
        });
    })
    //删除判断是否没有选择数据
    function delRule(ids,url,gridId){
        if(ids.length==0){
            bootbox.dialog({
                message: "<h3><b>请选择一条数据!</b></h3>",
                title: "",
                buttons: {
                    main: {
                        label: "关闭",
                        className: "btn-primary",
                    }
                }
            });
            return;
        }
        if(ids.length>1){
            bootbox.dialog({
                message: "<h3><b>只能同时删除一条数据!</b></h3>",
                title: "",
                buttons: {
                    main: {
                        label: "关闭",
                        className: "btn-primary",
                    }
                }
            });
            return;
        }
        $.blockUI({ message:"请稍等……" });
        bootbox.dialog({
            message: "是否删除？",
            title: "",
            closeButton:false,
            buttons: {
                success: {
                    label: "确定",
                    className: "btn-success",
                    callback: function () {
                        $.getJSON(url+"?ids=" + ids, {}, function (jsonData) {
                            setTimeout($.unblockUI, 10);
                            alert(jsonData.result);
                            $('#'+gridId).trigger("reloadGrid");
                        })
                    }
                },
                main: {
                    label: "取消",
                    className: "btn-primary",
                    callback: function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    }

    //auth表格初始化
    function limitTableConfigGrid(){
        var grid_selector = "#limit-table-grid-table";
        var pager_selector = "#limit-table-grid-pager";
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcConfig/getBdcLimitTableConfigPagesJson",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'id'},
            colNames:[ '数据表描述', '数据表名', '查询SQL'],
            colModel:[
                {name:'tableName', index:'tableName', width:'20%',sortable:false},
                {name:'tableId', index:'tableId', width:'10%', sortable:false},
                {name:'tableXmrelSql', index:'tableXmrelSql', width:'80%', sortable:false}
            ],
            viewrecords:true,
            rowNum:5,
            rowList:[5, 10, 15],
            pagerpos:"left",
            pager:pager_selector,
            altRows:false,
            multiboxonly:true,
            multiselect:true,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth',$("#limitTableContent").width());
                }, 0);
            },
            ondblClickRow:function (rowid) {
            },
            caption:"",
            autowidth:true
        });
    }
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    //var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
</script>
<div class="space-6"></div>
<div class="main-container">
<div class="page-content" id="limitTableContent">
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="addLimitTableConfig">
                    <i class="ace-icon fa fa-download"></i>
                    <span>新建</span>
                </button>
            </li>
            <li>
                <button type="button" id="delLimitTableConfig">
                    <i class="ace-icon glyphicon glyphicon-remove"></i>
                    <span>删除</span>
                </button>
            </li>
            <#if isSelect==true>
                <li>
                    <button type="button" id="selTable">
                        <i class="ace-icon fa fa-pencil"></i>
                        <span>选择</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="closeTable">
                        <i class="ace-icon fa fa-pencil"></i>
                        <span>返回</span>
                    </button>
                </li>
            </#if>
        </ul>
    </div>
    <table id="limit-table-grid-table"></table>

    <div id="limit-table-grid-pager"></div>

</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in Pop" style="display:none" id="limitTablePop">
    <div class="modal-dialog new-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-pencil-square-o icon-only bigger-110"></i>添加必填表格</h4>
                <button type="button"  class="hideButton"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="PBTools">
                <ul>
                    <li>
                        <a href="#" id="limitTableSaveBtn">
                            <i class="ace-icon fa fa-download"></i>
                            <span>保存</span>
                        </a>
                    </li>
                    <li>
                        <a href="#" onclick="$('#limitTableForm')[0].reset();">
                            <i class="ace-icon fa fa-pencil-square-o"></i>
                            <span>重置</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="bootbox-body">
                <form  id="limitTableForm"  novalidate="novalidate">
                    <div class="UItable">
                        <table cellpadding="0" cellspacing="0" border="0" class="tableA">
                            <tbody>
                            <tr>
                                <td>
                                    <label>表名:</label>
                                </td>
                                <td>
                                  <span class="block input-icon input-icon-right">
                                        <input type="text" class="form-control" id="tableId"
                                               name="tableId" required="true">
                                  </span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>表描述:</label>
                                </td>
                                <td>
                                   <span class="block input-icon input-icon-right">
                                         <input type="text" class="form-control" id="tableName"
                                                name="tableName" required="true">
                                   </span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>查询SQL:</label>
                                </td>
                                <td>
                                   <span class="block input-icon input-icon-right">
                                         <input type="text" class="form-control" id="tableXmrelSql"
                                                name="tableXmrelSql" required="true">
                                   </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<div class="modal-backdrop fade in Pop" style="display:none;" id="modal-backdrop-pop"></div>
</@com.html>