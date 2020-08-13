<@com.html title="审批表列表" import="fr,ace,public,init,jqueryVersion">
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
    .page-content{
        padding: 25px 20px 24px !important;
    }
    pre {
        padding: 0px !important;
        background: white!important;
        line-height: 1.5!important;
    }
</style>
<script type="text/javascript">
    //多选数据
    $mulData = new Array();
    $mulRowid = new Array();
    _$(function () {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        _$(window).on('resize.jqGrid', function () {
            _$(grid_selector).jqGrid('setGridWidth', _$(".page-content").width());
        });
        var parent_column = _$(grid_selector).closest('[class*="col-"]');
        _$(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                _$(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        _$(grid_selector).jqGrid({
            url:"${serverUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'QLID '},
            colNames:['序号','不动产单元号','登记类型','权利类型','坐落','操作','权利类型代码','项目ID','QLID','QLLXDM'],
            colModel:[
                {name:'XH', index:'XH', width:'1%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'5%', sortable:false},
                {name:'DJLX', index:'DJLX', width:'5%', sortable:false},
                {name:'QLLX', index:'QLLX', width:'5%', sortable:false},
                {name:'ZL', index:'ZL', width:'5%', sortable:false},
                {name:'CZ', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<div >' +
                            '<div><a class="detail" href="javascript:detailSpb(\'' + rowObject.PROID + '\')">预览</a><button type="button"  class="btn btn-primary" onclick="printOne(\'' + rowObject.PROID + '\')">打印</button></div>'+
                            '</div>'
                }
                },
                {name:'QLLXDM', index:'QLLXDM',sortable:false,hidden: true},
                {name:'PROID', index:'PROID', sortable:false,hidden: true},
                {name:'QLID', index:'QLID', sortable:false,hidden: true},
                {name:'QLLXDM', index:'QLLXDM', sortable:false,hidden: true}
            ],
            viewrecords:true,
            rowNum:5,
            rowList:[5, 10, 15],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:true,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    _$(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
                for (var i = 0; i <= $mulRowid.length; i++) {
                    _$(grid_selector).jqGrid('setSelection', $mulRowid[i]);
                }
            },
            onSelectAll: function (aRowids, status) {
                var $myGrid = $(this);
                //aRowids.forEach(function(e){
                $.each(aRowids, function (i, e) {
                    var cm = $myGrid.jqGrid('getRowData', e);
                    //判断是已选择界面还是原界面
                    if (cm.QLID == e) {
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
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
                //判断是已选择界面还是原界面
                if (cm.QLID == rowid) {
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
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });

    });

    function enableTooltips(table) {
        _$('.navtable .ui-pg-button').tooltip({container:'body'});
        _$(table).find('.ui-pg-div').tooltip({container:'body'});
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
            var icon = _$(this);
            var _$class = _$.trim(icon.attr('class').replace('ui-icon', ''));

            if (_$class in replacement) icon.attr('class', 'ui-icon ' + replacement[_$class]);
        })
    }
    function openWin(url, name) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    function detailSpb(proid) {
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_sqs.cpt&op=write&proid=" + proid;
        openWin(url);
    }
    function printOne(proid) {
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        p.push("{reportlet: '/print/bdc_spb.cpt', proid : '" + proid + "'}");
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
    }
    function print() {
        if ($mulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var proidArr =new Array();
        for (var i = 0; i < $mulData.length; i++) {
            proidArr.push($mulData[i].PROID)
        }
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        $.each(proidArr,function(index,item){
            //打印所有页
            p.push("{reportlet: '/print/bdc_sqs.cpt', proid : '" + item + "'}");
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
    }
    function printAll() {
        _$.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/fraJax/getProidListByQllxAndWiid?wiid=${wiid!}",
                    success: function (result) {
                        var p = [];
                        var printurl = "${reportUrl}/ReportServer";
                        $.each(result,function(index,item){
                            //打印所有页
                            p.push("{reportlet: '/print/bdc_sqs.cpt', proid : '" + item + "'}");
                        })
                        //将参数值组成的数组转化为字符串
                        var rp = p.join(",");
                        //使用FineReport自带的方法cjkEncode进行转码
                        var reportlets = FR.cjkEncode("[" + rp + "]");
                        var config = {
                            url: printurl,
                            isPopUp: true,
                            data: {
                                reportlets: reportlets
                            }
                        };
                        FR.doURLFlashPrint(config);
                    }
                }
        );
    }
    function clickEvent(fn, args) {
        fn.apply(this, args);
    }
</script>
<div class="container">
    <div class="row">
        <div class="span9">
            <@f.contentDiv title="申请书列表">
                <@p.listDiv>
                    <@p.toolBars>
                        <@p.toolBar text="打印" handler="print" iClass="ace-icon fa fa-columns bigger-120 blue"/>
                        <@p.toolBar text="打印全部" handler="printAll" iClass="ace-icon fa fa-columns bigger-120 blue"/>
                    </@p.toolBars>
                    <table id="grid-table"></table>
                    <div id="grid-pager"></div>
                </@p.listDiv>
            </@f.contentDiv>
        </div>
    </div>
</div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
