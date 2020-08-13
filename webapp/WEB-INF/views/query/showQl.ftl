<@com.html title="不动产登记业务管理系统" import="ace,init">
<style type="text/css">
    body {
        background-color: #ffffff;
        overflow-x: hidden;
    }
    iframe{
        position:relative;
    }
    html{overflow:hidden;}
</style>
<script type="text/javascript">
    $(function () {
        $("#houseInfo").click();
    });

    function showFrame(tableName) {
        var showQl = document.getElementById("showQl");

        showQl.src = tableName;
    }

    function showJbxx() {
        var showQl = document.getElementById("showQl");
        showQl.src = "${reportUrl}/ReportServer?reportlet=edit%2Fbdc_fwxx.cpt&op=write&bdcdyh=${bdcdyh!}";
    }
</script>
<div class="main-container">
    <#if showQL??>
        <div class="TaobaoSearch">
            <div class="container-fluid">
                <div class="row-fluid SelectedConditions">
                    <div class="col-xs-2" style="margin-top: 6px;">
                        <label class="label1" style="padding: 0 40px 0 20px;">已选戶室</label>
                    </div>
                    <div class="col-xs-10">
                        <ul class=" breadcrumb">
                            <li>${fjh!}室</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </#if>
    <div class="row" style="margin-left: 7px;">
        <div class="col-xs-12 leftContent" id="left">
            <div class="tabbable">
                <ul class="nav nav-tabs" id="myTab">

                <#--<li class="active">-->
                <#--<a data-toggle="tab" id="tdTab" href="#" onclick="showJbxx()">-->
                <#--不动产单元信息-->
                <#--</a>-->
                <#--</li>-->
                   <#-- <#if fwxxMap??>-->
                   <#--     <li>-->
                   <#--         <a data-toggle="tab" id="tdTab" href="#" onclick="showFrame('${fwxxMap["url"]}')">-->
                   <#--         ${fwxxMap["mc"]}-->
                   <#--         </a>-->
                   <#--     </li>-->
                   <#-- </#if>-->


                    <li>
                        <a data-toggle="tab" id="houseInfo" herf="#" onclick="showFrame('${reportUrl}/ReportServer?reportlet=edit%2Fdjsj_fw.cpt&op=write&djid=${djid!}')">
                                户室信息
                        </a>
                    </li>


                    <#list qllist as ql>
                        <li>
                            <a data-toggle="tab" id="tdTab" href="#" onclick="showFrame('${ql.tableName!}')">
                            ${ql.mc!}
                            </a>
                        </li>
                    </#list>
                <#--<#if qllist?size gt 0>
                    <#list qllist as ql>
                        <li <#if ql_index ==0>class="active" </#if>>
                            <a data-toggle="tab" id="tdTab" href="#" onclick="showFrame('${ql.tableName!}')">
                            ${ql.mc!}
                            </a>
                        </li>
                    </#list>
                <#else>
                    <li><h4>没有相应的权利信息！</h4></li>
                </#if>-->
                </ul>
                <div class="tab-content" style="height:1200px;overflow-y: hidden;">
                    <div id="file" class="tab-pane in active">
                        <iframe id="showQl" name="showQl"
                                src=""
                                width="99.8%"
                                style="border:none;overflow-y: hidden;" frameborder="0" scrolling="no"></iframe>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<script type="text/javascript">
    $(function () {
        var winHeight = $(window).height();
        var leftMenuHieght = 0;
        if (winHeight > 0) {
            leftMenuHieght1 = winHeight - 40;
        }
        $("#showQl").height($(window).height());
    });
</script>
</@com.html>
