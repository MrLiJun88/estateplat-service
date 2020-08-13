<#--jqGrid的列表-->
<#macro list tableId="" pageId="" keyField="" dataUrl="" rowdbclick="" rowNum="" height=""multiboxonly="" multiselect="">
<style>
    .form{
        text-align: right;
    }
    .form .row {
        margin: 10px 40px 10px 0px;
    }
    .form .row .date{
        width: 100%;
    }
    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
    }

    label {
        font-weight: bold;
    }
    /*重写下拉列表高度*/
    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;
    }
    .button{
        margin-bottom: 10px;
    }
    .center{
        text-align: center !important;
    }
    .left{
        text-align: left !important;
    }
    .right{
        text-align: right !important;
    }
</style>
<script type="text/javascript">
    function rendererEvent(fn, args) {
        return fn.apply(this, args);
    }
    function clickEvent(fn, args) {
        fn.apply(this, args);
    }
</script>
<script type="text/javascript">
    //多选数据
    $mulData = new Array();
    $mulRowid = new Array();
    _$(function () {
        var colmodels = new Array();
        var colNames = new Array();
        <#nested/>
        var grid_selector = "#${tableId!}";
        var pager_selector = "#${pageId!}";
        _$(window).on('resize.jqGrid', function () {
            _$(grid_selector).jqGrid('setGridWidth', _$(".page-content").width());
        });
        var parent_column = _$(grid_selector).closest('[class*="col-"]');
        _$(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                _$(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var rowNum;
        if("${rowNum}"==null || "${rowNum}"=="")
            rowNum=5;
        else
            rowNum=parseInt("${rowNum}");
        var height;
        if("${height}"==null || "${height}"=="")
            height='auto';
        else
            height="${height}px";
        var lastselId;
        _$(grid_selector).jqGrid({
            url: "${dataUrl!}",
            datatype: "json",
            height: height,
            jsonReader: {id: '${keyField!}'},
            colNames: colNames,
            colModel: colmodels,
            viewrecords: true,
            rowNum: rowNum,
            rowList: [rowNum, rowNum*2, rowNum*3],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: ${multiboxonly!},
            multiselect: ${multiselect!},
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    _$(grid_selector).jqGrid('setGridWidth',  _$(".page-content").width());
                }, 0);
                for (var i = 0; i <= $mulRowid.length; i++) {
                    _$(grid_selector).jqGrid('setSelection', $mulRowid[i]);
                }
            },
            onSelectAll: function (aRowids, status) {
                var $myGrid = _$(this);
                //aRowids.forEach(function(e){
                $.each(aRowids, function (i, e) {
                    var cm = $myGrid.jqGrid('getRowData', e);
                    //判断是已选择界面还是原界面
                    if (cm.${keyField!} == e) {
                        var index = $.inArray(e, $mulRowid);
                        if (status && index < 0) {
                            $mulData.push(cm);
                            $mulRowid.push(e);
                        } else if (!status && index >= 0) {
                            $mulData.remove(index);
                            $mulRowid.remove(index);
                        }
                    }
                })
            },
            onSelectRow: function (rowid, status) {
                var $myGrid = _$(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
                //判断是已选择界面还是原界面
                if (cm.${keyField!} == rowid) {
                    var index = $.inArray(rowid, $mulRowid);
                    if (status && index < 0) {
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            },
            ondblClickRow:function (rowid) {
                if("${rowdbclick!}"!=null && "${rowdbclick!}"!="")
                    ${rowdbclick!}(rowid);
                var $myGrid = $(this);
                var editable = "${editable!}";
                if (editable == "")
                    editable = false;
                if (editable!=true&&rowid) {
                    $myGrid.jqGrid('restoreRow', lastselId);
                    $myGrid.jqGrid('editRow', rowid, {
                        keys : true,      //这里按[enter]保存\
                        url:'clientArray',
                    });
                    lastselId = rowid;
                }
            },
            editurl: "",
            caption: "",
            autowidth: true
        });

    });

    function enableTooltips(table) {
        _$('.navtable .ui-pg-button').tooltip({container: 'body'});
        _$(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        _$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon =  _$(this);
            var $class =  _$.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }   //时间控件
    $('.date-picker').datepicker({
        autoclose:true,
        todayHighlight:true,
        language:'zh-CN'
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
    //下拉框
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
    $(window).on('resize.chosen',function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
            $(obj).next().css("text-align", "left");
        })
    }).trigger('resize.chosen');
</script>

</#macro>
<#macro listDiv title="" width="">
<div class="listBody" style="<#if width?? && width!="">width:${width!}px</#if> ">
    <div class="firstTitle">${title!}</div>
    <div class="page-content">
        <#nested/>
    </div>
</div>
</#macro>
<#--jqGrid的查询表单-->
<#macro queryForm id="" method="" url="">
<form class="form advancedSearchTable" id="${id!}" name="${id!}" method="${method!}" url="${url!}">
    <#nested/>
</form>
</#macro>
<#--jqGrid的查询条件行-->
<#macro queryRow>
<div class="row">
    <#nested/>
</div>
</#macro>
<#--jqGrid的查询条件-->
<#--查询条件每一行，目前支持六个条件一行，默认支持一行2个条件，支持类型text，datefield，combo，combo必须要有showFieldName显示的名称valueFieldName实际值，defaultValue默认值，source支持json和后台传list，
通过labelClass、fieldClass定义宽度,一行只能是col-xs-12-->
<#macro queryItem label="" name="" itemType="" showFieldName="" valueFieldName="" source="" defaultValue="" labelClass="" fieldClass="">
<div class=" <#if labelClass!="">${labelClass!} <#else>col-xs-2 </#if>">
    <label>${label!}：</label>
</div>
<div class="<#if fieldClass!="">${fieldClass!} <#else>col-xs-4 </#if>">
    <#if itemType=="datefield">
        <span class="input-icon date">
                     <input type="text" class="date-picker form-control" name="${name!}"
                            data-date-format="yyyy-mm-dd">
                    <i class="ace-icon fa fa-calendar"></i>
                </span>
    <#elseif  itemType=="combo">
        <#if source?index_of("[") gt -1>
            <#assign text="{'source':${source}}" />
            <#assign json=text?eval />
            <select name="${name!}" class="form-control chosen-select" data-placeholder=" ">
                <option value=""></option>
                <#list json.source as item>
                    <option value="${item["${valueFieldName?if_exists}"]}" <#if item["${valueFieldName?if_exists}"]==defaultValue> selected="selected"</#if>>${item["${showFieldName?if_exists}"]}</option>
                </#list>
            </select>
        <#else>
            <select name="${name!}" class="form-control chosen-select" data-placeholder=" ">
                <option value=""></option>
                <#list source as item>
                    <option value="${item["${valueFieldName?if_exists}"]}" <#if item["${valueFieldName?if_exists}"]==defaultValue> selected="selected"</#if>>${item["${showFieldName?if_exists}"]}</option>
                </#list>
            </select>
        </#if>
    <#else>
        <input type="text" name="${name!}" class="form-control">
    </#if>
</div>
</#macro>
<#--jqGrid的查询按钮-->
<#macro queryBars class="" style="">
<div class=" <#if class!="">${class!} <#else>col-xs-2 center</#if>" <#if style!="">style="${style!}" </#if>>
    <#nested/>
</div>
</#macro>
<#macro queryBar text="" handler="" >
<button type="button" class="btn btn-sm btn-primary button" onclick="clickEvent(eval('${handler!}'),[])">${text!}</button>
</#macro>
<#--jqGrid的列表按钮-->
<#macro toolBars >
<div class="tableHeader">
    <ul>
        <#nested/>
    </ul>
</div>
</#macro>
<#--jqGrid的列表按钮-->
<#macro toolBar handler="" text="" iClass="">
<li>
    <button type="button"  onclick="clickEvent(${handler!},[])">
        <i class="${iClass!}"></i>
        <span>${text!}</span>
    </button>
</li>
</#macro>
<#--jqGrid的列表字段-->
<#macro field fieldName="" header="" width="" renderer="" hidden="" editable="" edittype="" editoptions ="" selectData="" sorttype="">
var fieldName = "${fieldName!}";
var header = "${header!}";
var width = "${width!}";
var renderer = "${renderer!}";
var hidden = "${hidden!}";
var editable = "${editable!}";
var edittype = "${edittype!}";
var editoptions = "${editoptions!}";
var selectData="${selectData!}";
var sorttype="${sorttype!}";
colNames.push(header);
if (hidden == "")
hidden = false;
else if (hidden == "true")
hidden = true;

if (editable == "")
editable = false;
else if (editable == "true")
editable = true;

if(edittype != ""&&edittype != null){
if(edittype=="select"){
editoptions={value:selectData};
}else if(edittype=="date"){
edittype="text";
editoptions={size:10,maxlengh:10,dataInit:function(element){$(element).datepicker({dateFormat: 'yy-mm-dd'})}};
}
}
var colmodel;
if (renderer != null && renderer != "") {
colmodel = {
name: fieldName,
index: fieldName,
width: width,
sortable: false,
hidden: hidden,
editable: editable,
edittype: edittype,
formatter: function (cellvalue, options, rowObject) {
return rendererEvent(eval("${renderer!}"), [cellvalue, options, rowObject]);
},
sorttype:sorttype,
editoptions:editoptions
};
} else
colmodel = {name: fieldName, index: fieldName, width: width, sortable: false, hidden: hidden, editable: editable,edittype: edittype,sorttype:sorttype,editoptions:editoptions};
colmodels.push(colmodel);
</#macro>