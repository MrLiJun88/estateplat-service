<@com.html title="申请人信息" import="fr,ace,public,init,jqueryVersion">
<style type="text/css">
    .page-content {
        padding: 25px 20px 24px !important;
    }
    pre {
        padding: 0px !important;
        background: white!important;
        line-height: 1.5!important;
    }
</style>
<script type="text/javascript">
    function qllxCz(cellvalue, options, rowObject) {
        return '<div >' +
                '<div><a class="detail" href="javascript:detailSjd(\'' + rowObject.PROID + '\')">预览</a><button type="button"  class="btn btn-primary" onclick="printOne(\'' + rowObject.PROID + '\')">打印</button></div>' +
                '</div>'
    }
    function detailSjd(proid) {
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_sjd.cpt&op=write&proid=" + proid;
        openWin(url);
    }
    function printOne(proid) {
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        p.push("{reportlet: '/print/bdc_sjd.cpt', proid : '" + proid + "'}");
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
    function print() {
        if ($mulData.length == 0) {
            alert("请至少选择一条数据！");
            return;
        }
        var proidArr = new Array();
        for (var i = 0; i < $mulData.length; i++) {
            proidArr.push($mulData[i].PROID)
        }
        var p = [];
        var printurl = "${reportUrl}/ReportServer";
        $.each(proidArr, function (index, item) {
            //打印所有页
            p.push("{reportlet: '/print/bdc_sjd.cpt', proid : '" + item + "'}");
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
    function printAll() {
        _$.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/fraJax/getProidListByQllxAndWiid?wiid=${wiid!}",
                    success: function (result) {
                        var p = [];
                        var printurl = "${reportUrl}/ReportServer";
                        $.each(result, function (index, item) {
                            //打印所有页
                            p.push("{reportlet: '/print/bdc_sjd.cpt', proid : '" + item + "'}");
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
</script>
    <@f.contentDiv title="收件单列表">
        <@q.listDiv>
        <#--列表按钮-->
            <@q.toolBars>
                <@p.toolBar text="打印" handler="print" iClass="ace-icon fa fa-columns bigger-120 blue"/>
                <@p.toolBar text="打印全部" handler="printAll" iClass="ace-icon fa fa-columns bigger-120 blue"/>
            </@q.toolBars>
            <@q.list tableId="sjd-grid-table" pageId="sjd-grid-pager" keyField="QLID" dataUrl="${serverUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${wiid!}" multiboxonly="true"multiselect="true">
                <@q.field fieldName="XH" header="序号" width="1%"/>
                <@q.field fieldName="BDCDYH" header="不动产单元号" width="5%"/>
                <@q.field fieldName="QLLX" header="权利类型" width="5%"/>
                <@q.field fieldName="ZL" header="坐落" width="5%"/>
                <@q.field fieldName="PROID" header="操作" width="5%"   renderer="qllxCz"/>
                <@q.field fieldName="QLLXDM" header="权利类型代码" hidden="true"/>
                <@q.field fieldName="PROID" header="项目ID" hidden="true"/>
                <@q.field fieldName="QLID" header="QLID" hidden="true"/>
                <@q.field fieldName="QLLXDM" header="QLLXDM"  hidden="true"/>
            </@q.list>
        <table id="sjd-grid-table"></table>
        <div id="sjd-grid-pager"></div>
        </section>
        </@q.listDiv>
    </@f.contentDiv>

<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
</@com.html>