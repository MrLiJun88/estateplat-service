<@com.html title="统计查询" import="ace,public">
<style>
    .col-xs-4{
        padding-left: 6px;
        padding-bottom: 10px;
    }
    .ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }
    .widget-title{
        color: #fff;
        font-size: 24px;
    }
    .widget-main {
        padding: 0px;
    }
    .widget-box{
        border: 0px;
    }
    .step-content .step-pane{
        min-height: 50px;
        padding: 0px 10px 0px 5px;
    }
    .profile-user-info-striped .profile-info-name{
        color:#fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }
    .wizard-actions {
        padding: 0px 20px 0px 0px;
    }
    .hideTr{
        display: none;
    }
</style>
<script>

    var colNames;
    var colModel;
    if ('${displayControl!}' == 'on') {
        colNames = ['cjid', '序号', '批次', '项目编号', '坐落', '申请人', '持有人', '持有时间', '备注', '操作'];
        colModel = [
            {name: 'CJID', index: 'CJID', width: '0%', sortable: false, hidden: true},
            {
                name: 'XH', index: 'XH', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.ROWNUM_ + '.'
                }
            },
            {name: 'PC', index: 'PC', width: '10%', sortable: false},
            {name: 'BH', index: 'BH', width: '10%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
            {name: 'SQR', index: 'SQR', width: '12%', sortable: false},
            {name: 'CYR', index: 'CYR', width: '10%', sortable: false},
            {name: 'CYSJ', index: 'CYSJ', width: '15%', sortable: false},
            {name: 'BZ', index: 'BZ', width: '18%', sortable: false},
            {
                name: 'CZ',
                index: '',
                width: '8%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (isNotBlank(rowObject.BH)) {
                        return '<div style="margin-left:20px;"><div title="发送短信" style="float:left;cursor:pointer;margin-left: 10px" class="ui-pg-div ui-inline-edit" id="" onclick="sendSMS(\'' + rowObject.BH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-mail-forward fa-lg blue"></span></div></div>'
                    }
                }
            }
        ];
    } else {
        colNames = ['cjid', '序号', '批次', '项目编号', '坐落', '申请人', '持有人', '持有时间', '备注'];
        colModel = [
            {name: 'CJID', index: 'CJID', width: '0%', sortable: false, hidden: true},
            {
                name: 'XH', index: 'XH', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.ROWNUM_ + '.'
                }
            },
            {name: 'PC', index: 'PC', width: '10%', sortable: false},
            {name: 'BH', index: 'BH', width: '10%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
            {name: 'SQR', index: 'SQR', width: '12%', sortable: false},
            {name: 'CYR', index: 'CYR', width: '10%', sortable: false},
            {name: 'CYSJ', index: 'CYSJ', width: '15%', sortable: false},
            {name: 'BZ', index: 'BZ', width: '18%', sortable: false}
        ];
    }

    $(function(){
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect:true,no_results_text: "无匹配数据",width:"100%"});
        //resize the chosen on window resize
        $(window).on('resize.chosen', function() {
            $.each($('.chosen-select'),function(index,obj){
                $(obj).next().css("width",0);
                var w = $(obj).parent().width();
                $(obj).next().css("width",w);
            });
            $.each($('.chosen-single-select'),function(index,obj){
                $(obj).next().css("width",0);
                var e = $(obj).parent().width();
                $(obj).next().css("width",e);
            })
        }).trigger('resize.chosen');
        //表单添加校验
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
        Grid();

        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#yzContent").width()>0){
                contentWidth=$("#yzContent").width();
            }
            $("#grid-table").jqGrid('setGridWidth',contentWidth);
        });
    });
    
    //初始化表格数据
    function Grid(){
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcSecZl/queryBdcSelzlList",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'CJID'},
            colNames: colNames,
            colModel: colModel,
            viewrecords: true,
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
                    $(grid_selector).jqGrid('setGridWidth',$("#yzContent").width());
                }, 0);
            },
            ondblClickRow:function (rowid) {
            },
            caption:"",
            autowidth:true
        });
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    }
    <#--暂时不用发送短信-->
    <#--function sendSMS(bh) {-->
        <#--$.ajax({-->
            <#--url: '${bdcdjUrl}/wfProject/sendSMS',-->
            <#--type: 'GET',-->
            <#--data: {bh: bh},-->
            <#--success: function (data) {-->
                <#--alert("发送成功!");-->
            <#--},-->
            <#--error: function (data) {-->
                <#--alert("发送失败!");-->
            <#--}-->
        <#--});-->
    <#--}-->

    //根据查询条件进行查询
    function selCheckInfo(){
        //获取查询信息
        var bh=$("#selBh").val();
        var zl=$("#selZl").val();
        var sqr=$("#selSqr").val();
        var cyr=$("#selCyr").val();
        var pc=$("#selPc").val();
        var Url = "${bdcdjUrl}/bdcSecZl/queryBdcSelzlList";
        tableReload("grid-table", Url, {bh:bh,zl:zl,sqr:sqr,cyr:cyr,pc:pc});
    }

    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    var t1 = null;
    var sendMessage = true;
    //新增查询统计信息
    function addCheckInfo(){
        var num = new Date().Format("yyyyMMddhhmmss").toString();
        $("#bh").attr("disabled",false);
        $("#bz").attr("disabled",false);
        $("#sqr").attr("disabled",false);
        $("#cjid").val("");
        $("#bh").val("");
        $("#zl").val("");
        $("#sqr").val("");
        $("#cyr").val("");
        $("#cysj").val("");
        $("#bz").val("");
        $("#pc").val(num);
        $(".hideTr").css("display","none");
        $("#limitTablePop,#modal-backdrop-pop").show();
        $(window).trigger('resize.chosen');
        $("#titleDlr").html("新增统计信息");
        t1 = window.setInterval(saveDate,3000);
        sendMessage = true;
    }
    function saveDate(){
        var slbh=$("#bh").val();
        var pc=$("#pc").val();
        var isSend= $("input[type='checkbox']").is(':checked');
            if(null != slbh && slbh.length >= 17){
                //获取新增信息
                var bz=$("#bz").val();
                var sqr=$("#sqr").val();
                    $.ajax({
                        url:'${bdcdjUrl}/bdcSecZl/addSelZl?bh='+ slbh + '&bz='+bz+'&pc='+pc+ '&isSend=' +isSend+ '&sqr='+sqr,
                        type:'post',
                        dataType:'json',
                        async:false,
                        success:function (data) {
                            if (data != null && data.result != "" && data.result != "undefined" && data.result != undefined && data.result != "保存成功") {
                                alert(data.result);
                            }
                            $("#bh").val('');
                            sendMessage = false;
                        },
                        error:function (data) {
                            alert("保存失败");
                        }
                    });

            }else{
                return;
            }
    }
    //点击保存事件
    function SaveBtn(){
        var logValidate= $("#bankTableForm").validate();
        var cjid=$("#cjid").val();
        var pc=$("#pc").val();
        //获取新增信息
        if(logValidate.form()){
            $.ajax({
                url:'${bdcdjUrl}/bdcSecZl/saveSelZl?'+$("#bankTableForm").serialize()+ '&cjid=' +cjid+ '&pc=' +pc,
                type:'post',
                dataType:'json',
                success:function (data) {
                    alert(data.result);
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    //此处可以添加对查询数据的合法验证
                    $("#grid-table").jqGrid('setGridParam',{
                        datatype:'json',
                        page:1
                    }).trigger("reloadGrid"); //重新载入
                    $('#bankTableForm')[0].reset();
                },
                error:function (data) {
                    alert("保存失败");
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    $('#bankTableForm')[0].reset();
                }
            });
        }
    }
    //修改信息事件
    function updateCheckInfo(){
        if(null != t1){
            window.clearInterval(t1);
        }
        $(".hideTr").css("display","block");
        $("#bh").attr("disabled",true);
        $("#bz").attr("disabled",false);
        var ids = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
        if(ids.length == 0){
            tipInfo("请选择一条数据!");
            return;
        }
        if(ids.length > 1){
            tipInfo("只能同时修改一条数据!");
            return;
        }
        var data = $('#grid-table').getRowData(ids);
        $("#titleDlr").html("修改持件信息");
        $("#cjid").val("");
        $("#bh").val("");
        $("#zl").val("");
        $("#sqr").val("");
        $("#cyr").val("");
        $("#cysj").val("");
        $("#bz").val("");
        $("#cjid").val(data.CJID);
        $("#bh").val(data.BH);
        $("#zl").val(data.ZL);
        $("#sqr").val(data.SQR);
        $("#cyr").val(data.CYR);
        $("#cysj").val(data.CYSJ);
        $("#bz").val(data.BZ);
        $("#pc").val(data.PC);
        $("#limitTablePop,#modal-backdrop-pop").show();
    }
    //导出数据
    function exports(){
        var records = $("#grid-table").jqGrid('getGridParam','records');
        if(records == 0){
            tipInfo("没有可供导出的记录");
        }else if(records > 6000){
            tipInfo("导出的记录数过大！");
        }else{
            var selBh = $("#selBh").val();
            var selZl = $("#selZl").val();
            var selPc = $("#selPc").val();
            var selSqr = $("#selSqr").val();
            var selCyr = $("#selCyr").val();
            var date = new Date();
            var yjrq = date.getFullYear() +'-'+(date.getMonth() +1) +'-'+(date.getDate());
            var url = "${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/print/bdc_cjlbPrint&op=write&cptName=bdc_cjlbPrint&selBh="+selBh+"&selZl="+selZl+"&selPc="+selPc+"&selSqr="+selSqr+"&selCyr="+selCyr+"&yjrq="+yjrq;
            url = cjkEncode(url);
            showIndexModel(url, "导出持件信息列表", "1000", "500", false);
            //window.open(url);
        }
    }

    function cjkEncode(text) {
        if (text == null) {
            return "";
        }
        var newText = "";
        for (var i = 0; i < text.length; i++) {
            var code = text.charCodeAt (i);
            if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".
                newText += "[" + code.toString(16) + "]";
            } else {
                newText += text.charAt(i);
            }
        }
        return newText;
    }
    //提示
    function tipInfo(msg){
        // bootbox.dialog({
        //     message: "<h3><b>" + msg + "</b></h3>",
        //     title: "",
        //     buttons: {
        //         main: {
        //             label: "关闭",
        //             className: "btn-primary"
        //         }
        //     }
        // });
        $.Prompt(msg,1500);
    }

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
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="yzContent">
        <form id="yzForm">
            <div class="row paddingRow">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 项目编号 </div>
                        <div class="profile-info-value">
                            <input type="text" name="selBh" id="selBh"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 坐落 </div>
                        <div class="profile-info-value">
                            <input type="text" name="selZl" id="selZl"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 批次 </div>
                        <div class="profile-info-value">
                            <input type="text" name="selPc" id="selPc"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row paddingRow">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 申请人姓名 </div>
                        <div class="profile-info-value">
                            <input type="text" name="selSqr" id="selSqr"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 持有人姓名 </div>
                        <div class="profile-info-value">
                            <input type="text" name="selCyr" id="selCyr"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4" style="padding-left: 13px;">
                    <button class="btn btn-sm btn-success" type="button" onclick="selCheckInfo()">
                        <i class="ace-icon fa fa-download"></i>
                        查询
                    </button>
                    <button class="btn btn-sm btn-success" type="button" onclick="addCheckInfo()">
                        <i class="ace-icon fa fa-download"></i>
                        新增
                    </button>
                    <button class="btn btn-sm btn-success" type="button" onclick="exports()">
                        <i class="ace-icon fa fa-download"></i>
                        导出
                    </button>
                </div>
            </div>
        </form>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" onclick="updateCheckInfo()">
                        <i class="ace-icon glyphicon glyphicon-edit"></i>
                        <span>修改</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>

<!--新增/修改页面-->
<div class="Pop-upBox bootbox modal fade bootbox-prompt in Pop" style="display:none" id="limitTablePop">
    <input type="hidden" id="cjid" name="cjid">
    <div class="modal-dialog new-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" ><i class="ace-icon fa fa-pencil-square-o icon-only bigger-110" id="titleDlr"></i></h4>
                <button type="button"  class="hideButton"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="PBTools">
                <ul>
                    <li>
                        <a href="#" onclick="SaveBtn()">
                            <i class="ace-icon fa fa-download"></i>
                            <span>保存</span>
                        </a>
                        <label style="margin-left: 40px">批次:</label>
                        <input type="text" id="pc" name="pc" style="margin: 0px;height: 25px">
                        <div style="display: none">
                            <label style="margin-left: 40px">是否发送短信:</label>
                            <input type="checkbox" id="isSend" style="margin: -8px 20px;height: 25px">
                        </div>
                    </li>
                </ul>
            </div>
            <div class="bootbox-body">
                <form  id="bankTableForm"  novalidate="novalidate">
                    <div class="UItable">
                        <table class="tableA">
                            <tbody>
                            <tr >
                                <td >
                                    <label>项目编号:</label>
                                </td>
                                <td >
                                    <input type="text" class="form-control"  id="bh" name="bh" required="false" disabled="disabled">
                                </td>
                            </tr>
                            <tr >
                                <td>
                                    <label>备注:</label>
                                </td>
                                <td>
                                    <input type="tel" class="form-control" id="bz" name="bz" required="false" disabled="disabled">
                                </td>
                            </tr>
                            <tr >
                                <td>
                                    <label>申请人名称:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="sqr" name="sqr" required="false" disabled="disabled">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>持有人名称:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="cyr" name="cyr" required="false" disabled="disabled">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>持有时间:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="cysj" name="cysj" required="false" disabled="disabled">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>坐落:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="zl" name="zl" required="false" disabled="disabled">
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
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>