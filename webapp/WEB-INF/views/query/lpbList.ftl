<@com.html title="证书列表" import="fr,ace,jqueryVersion,public">
<style xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
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

        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
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
    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }
    label {
        font-weight: bold;
    }
        /*高级搜索样式添加 end*/
</style>
<script type="text/javascript">
    /*   文字水印  */
    _$(function () {
        //搜索事件
    <#--_$(".search").click(function () {-->

    <#--var dcxc = '';-->
    <#--if (_$("#search_xmmc").val() != '') {-->
    <#--dcxc = _$("#search_xmmc").val();-->
    <#--}-->
    <#--var xmjc = '';-->
    <#--if (_$("#xmjc").val() != '') {-->
    <#--xmjc = _$("#xmjc").val();-->
    <#--}-->
    <#--var zl = '';-->
    <#--if (_$("#zl").val() != '') {-->
    <#--zl = _$("#zl").val();-->
    <#--}-->
    <#--var zdh = '';-->
    <#--if (_$("#zdh").val() != '') {-->
    <#--zdh = _$("#zdh").val();-->
    <#--}-->
    <#--var Url = "_${bdcdjUrl}/lpb/getLpbPagesJson"-->
    <#--Url = Url + "?xmjc=" + xmjc +"&zl="+zl+ "&zdh=" + zdh +"&dcxc="+dcxc;-->
    <#--Url= encodeURI(Url);-->
    <#--var jqgrid = _$("#grid-table");-->
    <#--jqgrid.setGridParam({url:Url, datatype:'json', page:1});-->
    <#--jqgrid.trigger("reloadGrid");//重新加载JqGrid-->
    <#--})-->


//        _$("#hide").click(function () {
//            _$(".SearchFloat").hide();
//        });
//        _$("#show").click(function () {
//            _$(".SearchFloat").show();
//        });
        var dcxc =  "${dcxc!}";
        if (dcxc != '' && dcxc != null) {
            _$("#search_xmmc").val(decodeURI(dcxc));
        } else {
            /*   文字水印  */
            _$(".watermarkText").watermark();
        }
        _$(document).keydown(function (event) {
            if (event.keyCode == 13) { //绑定回车
                var xmmc = _$("#search_xmmc").val();
                var Url = "${bdcdjUrl}/lpb/getLpbPagesJson?" + _$("#gjSearchForm").serialize();
                tableReload("grid-table", Url, {dcxc:xmmc});
            }
        });

        //项目表搜索事件
        _$("#search").click(function () {
            var xmmc = _$("#search_xmmc").val();
            var Url = "${bdcdjUrl}/lpb/getLpbPagesJson?" + _$("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:xmmc});
        });

        //项目表高级查询的搜索按钮事件
        _$("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/lpb/getLpbPagesJson?" + _$("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});
        });
        //拖拽功能
        _$(".modal-header").mouseover(function () {
            _$(this).css("cursor", "move");//改变鼠标指针的形状
        })
        _$(".modal-header").mouseout(function () {
            _$(".show").css("cursor", "default");
        })
        _$(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

        //项目高级搜索关闭事件
        _$("#proHide").click(function () {
            _$("#gjSearchPop").hide();
            _$("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        _$("#show").click(function () {
            _$("#gjSearchPop").show();
        });
    });

    /* 调用子页面方法  */
    function showModal() {
        _$('#myModal').show();
        _$('#modal-backdrop').show();
    }
    function hideModal() {
        _$('#myModal').hide();
        _$('#modal-backdrop').hide();
        _$("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
    }
    var onmessage = function (e) {
        showModal();
    };

    function enableTooltips(table) {
        _$('.navtable .ui-pg-button').tooltip({container:'body'});
        _$(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
        };
        _$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = _$(this);
            var _$class = _$.trim(icon.attr('class').replace('ui-icon', ''));

            if (_$class in replacement) icon.attr('class', 'ui-icon ' + replacement[_$class]);
        })
    }

    _$(function () {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        //resize to fit page size
        _$(window).on('resize.jqGrid', function () {
            _$(grid_selector).jqGrid('setGridWidth', _$(".page-content").width());
        });
        //resize on sidebar collapse/expand
        var parent_column = _$(grid_selector).closest('[class*="col-"]');
        _$(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                _$(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var dcxc =  "${dcxc!}";
        _$(grid_selector).jqGrid({
            url:"${bdcdjUrl}/lpb/getLpbPagesJson?dcxc=" + dcxc,
            datatype:"json",
            height:'auto',
            jsonReader:{id:'FW_DCB_INDEX '},
            colNames:['小区名称','坐落','宗地号','查看'],
            colModel:[
                {name:'FWMC', index:'FWMC', width:'10%', sortable:false},
                {name:'ZLDZ', index:'ZLDZ', width:'10%', sortable:false},
                {name:'LSZD', index:'LSZD', width:'10%', sortable:false},
                {name:'mydy', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<div title="点击选择" style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.FW_DCB_INDEX + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>'+
                            '<div title="批量打印分割土地权利证明" style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="pldy(\'' + rowObject.LSZD + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-columns bigger-120 blue"></span></div>'+
                            '<div title="批量打印匹配通知书" style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="pldyPptzs(\'' + rowObject.LSZD + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-file-text bigger-120 blue"></span></div>'
                }
                }
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
            ondblClickRow:function (id) {
//                    EditXm(id);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
        _$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
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
                fmt = fmt.replace(RegExp._$1, (this.getFullYear() + "").substr(4 - RegExp._$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp._$1, (RegExp._$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    });

    //修改项目信息的函数
    function EditXm(id) {
        if(id!=""){
            var proid = encodeURI(encodeURI(id));
            var xmmc = _$("#search_xmmc").val();
            location.href ="${bdcdjUrl}/lpb/lpb?isNotWf=true&showQl=true&dcbId="+id+"&openQlWay=${openQlWay!}"+"&dcxc="+xmmc;
            //location.href ="_${reportUrl}/ReportServer?reportlet=edit%2Fbdc_spb.cpt&op=write&format=pdf&proid="+proid;
        }
    }

    function pldy(lszd) {
        _$.ajax({
            type:"POST",
            url:"${bdcdjUrl}/lpb/getBdcdyhByLszd",
            data:{lszd:lszd},
            dataType:"json",
            success:function (result) {
                var p = [];
                var printurl = "${reportUrl}/ReportServer";
                _$.each(result, function (index,ojb) {
                    p.push("{reportlet: '/edit/gd_fgql.cpt', bdcdyh : '" + ojb + "'}");
                })
                //将参数值组成的数组转化为字符串
                var rp = p.join(",");
                //使用FineReport自带的方法cjkEncode进行转码
                var reportlets = FR.cjkEncode("[" + rp + "]");

                var config = {
                    url:printurl,
                    isPopUp:true,
                    data:{
                        reportlets:reportlets
                    }
                };
                FR.doURLFlashPrint(config);
            },
            error:function (data) {
            }
        });
    }
    function pldyPptzs(lszd) {
        _$.ajax({
            type:"POST",
            url:"${bdcdjUrl}/lpb/getBdcdyhByLszd",
            data:{lszd:lszd},
            dataType:"json",
            success:function (result) {
                var p = [];
                var printurl = "${reportUrl}/ReportServer";
                _$.each(result, function (index,ojb) {
                    p.push("{reportlet: '/print/bdc_pptzs.cpt', bdcdyh : '" + ojb + "'}");
                })
                //将参数值组成的数组转化为字符串
                var rp = p.join(",");
                //使用FineReport自带的方法cjkEncode进行转码
                var reportlets = FR.cjkEncode("[" + rp + "]");

                var config = {
                    url:printurl,
                    isPopUp:true,
                    data:{
                        reportlets:reportlets
                    }
                };
                FR.doURLFlashPrint(config);
            },
            error:function (data) {
            }
        });
    }
    function tableReload(table,Url,data){
        var jqgrid = _$("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入坐落/宗地号">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <#--<div class="Advanced">-->
            <#--<div class="AdvancedSearchForm SearchFloat" style="display: none;">-->
                <#--<h3 class="AdvancedLab">高级搜索</h3>-->
                <#--<form class="form-base">-->
                    <#--<div class="row-fluid">-->
                        <#--<div class="col-xs-12">-->
                            <#--<div class="HasOptional">-->
                                <#--<table cellpadding="0" cellspacing="0" border="0">-->
                                    <#--<tr>-->
                                        <#--<td>-->
                                            <#--名称：-->
                                        <#--</td>-->
                                        <#--<td>-->
                                            <#--<input type="text" class="SSinput" id="xmjc">-->
                                        <#--</td>-->
                                        <#--<td>-->
                                            <#--地址：-->
                                        <#--</td>-->
                                        <#--<td>-->
                                            <#--<input type="text" class="SSinput" id="zl">-->
                                        <#--</td>-->

                                    <#--</tr>-->
                                    <#--<tr>-->
                                        <#--<td>-->
                                            <#--宗地号：-->
                                        <#--</td>-->
                                        <#--<td>-->
                                            <#--<input type="text" class="SSinput" id="zdh">-->
                                        <#--</td>-->
                                    <#--</tr>-->
                                <#--</table>-->
                            <#--</div>-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<div class="row-fluid ">-->
                        <#--<div class="span10 offset2">-->
                            <#--<button type="submit" class="btn01 btn01-primary search"><i-->
                                    <#--class="icon-search icon-white"></i>搜索-->
                            <#--</button>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</form>-->
            <#--</div>-->
            <#--<div class="modal-backdrop SearchFloat" id="hide" style="display: none;"></div>-->
        <#--</div>-->

        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <input id="zdtzm" name="zdtzm" type="hidden" value="${zdtzm!}"/>
                    <#list lpbGjssOrderList as lpbGjss>
                        <#if lpbGjss == 'bdclx'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="bdclx" class="form-control" id="bdclxSel">
                                    <option></option>
                                    <#list bdcList as bdclx>
                                        <option value="${bdclx.DM}">${bdclx.MC}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   lpbGjss=='bdcdyh'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   lpbGjss=='zl'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zl" class="form-control">
                            </div>
                            <div class="col-xs-2">
                            </div>
                            <div class="col-xs-4">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   lpbGjss=='zdzdh'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地宗海号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdzhh" class="form-control">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   lpbGjss=='dbr'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登簿人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="dbr" class="form-control">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   lpbGjss=='zdh'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdh" class="form-control">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif lpbGjss='xqmc'>
                            <#if (lpbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>小区名称：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fwmc" class="form-control">
                            </div>
                            <#if (lpbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>
                        </#if>
                    </#list>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
