<@com.html title="系统日志" import="ace">
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
</style>
<script>
    $(function(){
        $("#logConfigSaveBtn").click(function(){
            var options = {
                url:'${bdcdjUrl}/bdcConfig/saveLogConfig',
                type:'post',
                dataType:'json',
                data:$("#logform").serialize(),
                success:function (data) {
                    alert(data.result);
                    $("#logPop,#modal-backdrop-pop").hide();
                    //此处可以添加对查询数据的合法验证
                    $("#log-grid-table").jqGrid('setGridParam',{
                        datatype:'json',
                        page:1
                    }).trigger("reloadGrid"); //重新载入
                    $('#logform')[0].reset();
                },
                error:function (data) {
                    alert("保存失败");
                    $("#logPop,#modal-backdrop-pop").hide();
                    $('#logform')[0].reset();
                }
            };
            var logValidate= $("#logform").validate();
            if(logValidate.form()){
                $.ajax(options);
                return false;
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
        logConfigGrid();


        //logConfig新增
        $("#addLogConfig").click(function () {
            $("#logPop,#modal-backdrop-pop").show();
        });
        //logConfig删除
        $("#delLogConfig").click(function () {
            var ids = $('#log-grid-table').jqGrid('getGridParam', 'selarrrow');
            delRule(ids,"${bdcdjUrl}/bdcConfig/delLogConfig","log-grid-table");
        })
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#logContent").width()>0){
                contentWidth=$("#logContent").width();
            }
            $("#log-grid-table").jqGrid('setGridWidth',contentWidth);
        });
    })
    //删除判断是否没有选择数据
    function delRule(ids,url,gridId){
        if(ids.length==0){
            bootbox.dialog({
                message: "<h3><b>请至少选择一条数据!</b></h3>",
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
    function logConfigGrid(){
        var grid_selector = "#log-grid-table";
        var pager_selector = "#log-grid-pager";
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcConfig/getBdcXtLogConfigPagesJson",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'controllerPath'},
            colNames:[ '方法描述', '方法路径'],
            colModel:[
                {name:'controllerMsg', index:'controllerMsg', width:'50%',sortable:false},
                {name:'controllerPath', index:'controllerPath', width:'50%', sortable:false}
            ],
            viewrecords:true,
            pagerpos:"left",
            pager:pager_selector,
            altRows:false,
            rowNum:5,
            rowList:[5, 10, 15],
            multiboxonly:true,
            multiselect:true,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth',$("#logContent").width());
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
<div class="main-container">
    <div class="space-10"></div>
<div class="page-content" id="logContent">
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="addLogConfig">
                    <i class="ace-icon fa fa-download"></i>
                    <span>新建</span>
                </button>
            </li>
            <li>
                <button type="button" id="delLogConfig">
                    <i class="ace-icon glyphicon glyphicon-remove"></i>
                    <span>删除</span>
                </button>
            </li>
        </ul>
    </div>
    <table id="log-grid-table"></table>

    <div id="log-grid-pager"></div>

</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in Pop" style="display:none" id="logPop">
    <div class="modal-dialog new-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-pencil-square-o icon-only bigger-110"></i>添加日志记录路径</h4>
                <button type="button"  class="hideButton"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="PBTools">
                <ul>
                    <li>
                        <a href="#" id="logConfigSaveBtn">
                            <i class="ace-icon fa fa-download"></i>
                            <span>保存</span>
                        </a>
                    </li>
                    <li>
                        <a href="#" onclick="$('#logform')[0].reset();">
                            <i class="ace-icon fa fa-pencil-square-o"></i>
                            <span>重置</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="bootbox-body">
                <form  id="logform"  novalidate="novalidate">
                    <div class="UItable">
                        <table cellpadding="0" cellspacing="0" border="0" class="tableA">
                            <tbody>
                            <tr>
                                <td>
                                    <label>方法路径:</label>
                                </td>
                                <td>
                                  <span class="block input-icon input-icon-right">
                                        <input type="text" class="form-control" id="controllerPath"
                                               name="controllerPath" required="true">
                                  </span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>方法描述:</label>
                                </td>
                                <td>
                                   <span class="block input-icon input-icon-right">
                                         <input type="text" class="form-control" id="controllerMsg"
                                                name="controllerMsg" required="true">
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
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-pop"></div>
</@com.html>