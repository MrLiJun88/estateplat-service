<@com.html title="不动产登记业务管理系统" import="bui,init">
<style type="text/css">
    body {
        background-color: #ffffff;
        overflow-x: hidden;
        overflow-y: hidden;
    }

    .label1 {
        color: #fff;
        font-weight: bold;
        background: #428bca url("../img/flow-steps.png") no-repeat 100% 0;
        height: 23px;
        line-height: 23px;
        text-align: center;
        overflow: hidden;
        padding: 0 40px 0 20px;
    }
</style>
<script type="text/javascript">
    $(function () {
        var winHeight = $(window).height();
        var leftMenuHieght = 0;
        if (winHeight > 0) {
            leftMenuHieght1 = winHeight - 40;
        }
        $("#showQl").height($(window).height());
    });
    function showFrame(tab) {
        $(".active").removeClass();
        $(tab).parent().addClass("active");
        $("#showQl").attr("src", $(tab).next().val());
    }
</script>
<div class="demo-content">
    <#if showQL??>-
        <div class="row ">
            <div class="span8 SelectedConditions " style="border: 0px;">
                <label class="label1" style="padding: 0 40px 0 20px;">已选戶室</label>
            </div>
            <div class="span16" style="border: 0px;">
                <li>${fjh!}室</li>
            </div>
        </div>
    </#if>
</div>
<div class="demo-content" style="padding: 0px">
    <div class="doc-content">
        <ul class="nav-tabs" style="margin-left: 20px;">
            <#if qllist?size gt 0>
                <#list qllist as ql>
                    <li <#if ql_index ==0>class="active" </#if>>
                        <a href="#" onclick="showFrame(this)"> ${ql.mc!}</a>
                        <input value="${ql.tableName!}" type="hidden"/>
                    </li>
                </#list>
            <#else>
                <li><h4>没有相应的权利信息！</h4></li>
            </#if>
        </ul>
        <#if qllist?size gt 0>
            <div class="span-width span16"
                 style="width:100%;border-left: 1px solid #ddd;border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;">
                <iframe id='showQl' name="showQl" src="${qllist[0].tableName!}" frameborder="no" border="0"
                        marginwidth="0" marginheight="0"
                        scrolling="no" allowtransparency="yes" width="99.1%"></iframe>
            </div>
        </#if>
    </div>
    <!-- script end -->
</div>
</@com.html>
