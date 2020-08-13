<@com.html title="" import="ace,public">
    <style type="text/css">
        .leftLabel {
            margin: 0 auto;
            width: 20px;
            line-height: 24px;
            border: 0px solid #333;
        }
        .tableA>tbody>tr>th {
            text-align: right;
            padding-right: 15px;
            background-color: #FFFFFF;
            border: 1px solid #aaa;
        }
        .UItable table>tbody>tr>td{
            border: 1px solid #aaa;
        }
        /*
       *以下是可搜索select样式优化符合我们页面风格
       */
        .combo-select {
            max-width: 100%;
            margin-bottom: 0px;
            border: 0px;
        }

        .combo-dropdown {
            box-shadow: none;
            border-color: #35a9e0;
        }

        .option-item {
            border: 0px;
        }
        .option-item:hover,.option-hover{
            background-color: #46abe2;
        }
        .src-mc{
            text-align: right;
            padding-right: 0;
            padding-top: 16px;
        }
        .src-code{
            text-align: left;
            padding-top: 16px;
        }
    </style>
    <div class="bs-docs-example toolTop">
        <div class="leftToolTop">
            <button type="button" id="保存" class="btn btn-primary save" onclick="saveMlhXx()">保存</button>
        </div>
    </div>
    <div class="main-container">
        <@f.contentDiv  title="">
            <div class="space-10"></div>
            <@f.form id="gdlxForm" name="gdlxForm">
                <@f.table style="width:650px">
                    <@f.tr id="tr_label" >
                        <@f.th colspan="1"  style="width:350px;">
                            <@f.label name="归档类型"></@f.label>
                        </@f.th>
                        <@f.td colspan="1" style="width:300px;">
                            <@f.label name="当前目录号"></@f.label>
                        </@f.td>
                    </@f.tr>
                    <#if (gdlxlist?size > 0)>
                        <#list gdlxlist as bdcZdGdLx>
                            <input type="hidden" name ="mc" value="${bdcZdGdLx.mc!}">
                            <@f.tr  id="tr_${bdcZdGdLx.mc!}" >
                                <@f.th colspan="1">
                                    <@f.label name="${bdcZdGdLx.mc!}"></@f.label>
                                </@f.th>
                                <@f.td colspan="1">
                                    <@f.text id="zdlxdm_${bdcZdGdLx.mc!}" name="dm" value="${bdcZdGdLx.dm!}" ></@f.text>
                                </@f.td>
                            </@f.tr>
                        </#list>
                    </#if>
                </@f.table>
            </@f.form>
        </@f.contentDiv>
    </div>
    <script type="text/javascript">
        window.onload = function() {
        }
        var serverUrl = "${serverUrl!}";
        var slbh = "${slbh!}";
        $(function () {

        });
        function  saveMlhXx() {
            $.blockUI({message: "请稍等......"});
            var arry = [];
            $("input[name='mc']").each(function (i, n) {
                var mc = $(n).val();
                var dm = $("#zdlxdm_" + mc).val();;
                var o = new Object();
                o['mc'] = mc;
                o['dm'] = dm;
                o['bzf'] = '';
                arry.push(o);
            });
            var s = JSON.stringify(arry);
            $.ajax({
                url: serverUrl + "/bdcarchives/saveMlhXx",
                type: 'POST',
                data: $.param({s: s}),
                success: function (data) {
                    setTimeout($.unblockUI, 10);
                    if (data != null && data.msg != null && data.msg != '' && data.msg =='success') {
                        tipInfo("保存成功!");
                    }else{
                        tipInfo("保存失败!");
                    }
                },
                error: function (data) {
                    setTimeout($.unblockUI, 10);
                    tipInfo("保存失败!");
                }
            });
        }

        window.onbeforeunload = function () {
            window.opener.afterCloseModifyMlh(slbh);
        }
    </script>
</@com.html>