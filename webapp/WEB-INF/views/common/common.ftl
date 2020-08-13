<#macro html title="" import="" css="" js="">
    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
        <title>${title}</title>
        <#if import??>
            <#list import?split(",") as lib>
                <#switch lib>
                    <#case "bui">
                        <@script name="static/thirdControl/jquery/jquery.min.js"></@script>
                        <@script name="static/js/leftMenu.js"></@script>
                        <@style name="static/thirdControl/bui/css/bs3/dpl.css"></@style>
                        <@style name="static/thirdControl/bui/css/bs3/bui.css"></@style>
                        <@style name="static/thirdControl/bui/css/other.css"></@style>
                        <@script name="static/thirdControl/bui/bui.js"></@script>
                        <@script name="static/thirdControl/bui/sea.js"></@script>
                        <@script name="static/thirdControl/bui/config.js"></@script>
                        <@style name="static/thirdControl/bootstrap/css/bootstrap.min.css"></@style>
                        <@script name="static/thirdControl/bootstrap/js/bootstrap.min.js"></@script>
                        <@script name="static/thirdControl/jquery/plugins/jquery.form.min.js"></@script>
                        <@script name="static/js/public.js"></@script>
                        <@script name="static/js/jquery.watermark.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/jquery.blockUI.js"></@script>
                        <#break />
                    <#case "bootstrap">
                        <@script name="static/thirdControl/jquery/jquery.min.js"></@script>
                        <@style name="static/thirdControl/bootstrap/css/bootstrap.css"></@style>
                        <@script name="static/thirdControl/bootstrap/js/bootstrap.min.js"></@script>
                        <@style name="static/thirdControl/jqgrid/css/ui.jqgrid.css"></@style>
                        <@style name="static/thirdControl/bootstrap/css/ace.min.css"></@style>
                        <@script name="static/thirdControl/jqgrid/js/jquery.jqGrid.src.js"></@script>
                        <@script name="static/thirdControl/jqgrid/js/i18n/grid.locale-cn.js"></@script>
                        <@style name="static/thirdControl/ace/font-awesome/css/font-awesome.min.css"></@style>
                        <@script name="static/thirdControl/bootstrap/js/bootbox.min.js"></@script>
                        <#break />
                    <#case "ace">
                        <@script name="static/thirdControl/jquery/jquery.min.js"></@script>
                        <@style name="static/thirdControl/ace/css/jquery-ui.min.css"></@style>
                        <@script name="static/thirdControl/jquery/jquery.cookie.js"></@script>
                        <@script name="static/js/jquery.watermark.min.js"></@script>
                        <#--<@style name="static/thirdControl/bootstrap/css/bootstrap.css"></@style>-->
                        <#--<@script name="static/thirdControl/bootstrap/js/bootstrap.min.js"></@script>-->
                        <@style name="static/thirdControl/ace/bootstrap/css/bootstrap.min.css"></@style>
                        <@style name="static/thirdControl/ace/bootstrap/css/bootstrap-combined.min.css"></@style>
                        <@style name="static/thirdControl/jqgrid/css/ui.jqgrid.css"></@style>
                        <@script name="static/thirdControl/jqgrid/js/jquery.jqGrid.src.js"></@script>
                        <@script name="static/thirdControl/jqgrid/js/i18n/grid.locale-cn.js"></@script>
                        <@script name="static/thirdControl/ace/bootstrap/js/html5shiv.min.js"></@script>
                        <@style name="static/thirdControl/ace/font-awesome/css/font-awesome.min.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace-fonts.css"></@style>
                        <@style name="static/thirdControl/ace/css/chosen.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace.min.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace-rtl.min.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace-skins.min.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace-part2.min.css"></@style>
                        <@style name="static/thirdControl/ace/css/ace-ie.min.css"></@style>
                        <@script name="static/thirdControl/ace/js/ace-extra.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/jquery.easy-pie-chart.min.js"></@script>
                        <@style name="static/thirdControl/ace/css/index.css"></@style>
                        <@script name="static/thirdControl/ace/js/jquery-ui.min.js"></@script>
                        <@style name="static/thirdControl/ace/css/ace.onpage-help.css"></@style>
                        <@script name="static/thirdControl/ace/js/ace/ace.onpage-help.js"></@script>
                        <@script name="static/thirdControl/ace/js/ace/fuelux.wizard.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/jquery.blockUI.js"></@script>
                        <@script name="static/thirdControl/ace/js/bootbox.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/chosen.jquery.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/jquery.validate.min.js"></@script>
                        <@style name="static/thirdControl/ace/js/date-time/datepicker.css"></@style>
                        <@script name="static/thirdControl/ace/js/date-time/bootstrap-datepicker.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/date-time/locales/bootstrap-datepicker.zh-CN.js"></@script>
                        <@script name="static/thirdControl/ace/bootstrap/js/bootstrap.min.js"></@script>
                        <@script name="static/thirdControl/ace/js/fuelux/data/fuelux.tree-sample-demo-data.js"></@script>
                        <@script name="static/thirdControl/ace/js/fuelux/fuelux.tree.min.js"></@script>
                        <@script name="static/thirdControl/jquery/jquery.prompt.js"></@script>
                        <@script name="static/js/browserMsie.js"></@script>
                        <@style name="static/css/jquery.prompt.css"></@style>
                        <#break />
                    <#case "sqlxQllx">
                        <@style name="static/css/sqlxQllx.css"></@style>
                        <@style name="static/css/docs.css"></@style>
                        <@style name="static/thirdControl/bootstrap/css/bootstrap-datetimepicker.min.css"></@style>
                        <@style name="static/thirdControl/bootstrap/css/bootstrap.css"></@style>
                        <@script name="static/thirdControl/jquery/jquery-1.8.1.min.js"></@script>
                        <@script name="static/thirdControl/bootstrap/js/bootstrap.js"></@script>
                        <#break />
                    <#case "colorbox">
                        <@style name="static/thirdControl/ace/css/colorbox.css"></@style>
                        <@script name="static/thirdControl/ace/js/jquery.colorbox-min.js"></@script>
                        <#break />
                    <#case "select">
                        <@style name="static/css/other.css"></@style>
                        <@script name="static/thirdControl/ace/bootstrap/js/bootstrap-select.min.js"></@script>
                        <#break />
                    <#case "init">
                        <@style name="static/css/other.css"></@style>
                        <#break />
                    <#case "public">
                        <@script name="static/js/public.js"></@script>
                        <@script name="static/js/cjfun.js"></@script>
                        <@script name="static/js/examine.js"></@script>
                        <#break />
                    <#case "jquery">
                        <@script name="static/thirdControl/jquery/jquery.min.js"></@script>
                        <#break />
                    <#case "highcharts">
                        <@script name="static/thirdControl/highcharts/adapters/highcharts.js"></@script>
                        <@script name="static/thirdControl/highcharts/adapters/highcharts-3d.js"></@script>
                        <@script name="static/thirdControl/highcharts/adapters/exporting.js"></@script>
                        <#break />
                    <#case "jqueryVersion">
                        <script type="text/javascript">
                            var _$ = jQuery.noConflict(true);
                        </script>
                        <#break />
                    <#case "fr">
                        <@frscript name="/ReportServer?op=resource&resource=/com/fr/web/jquery.js"></@frscript>
                        <@frscript name="/ReportServer?op=emb&resource=finereport.js"></@frscript>
                        <@frstyle  name="/ReportServer?op=emb&resource=finereport.css"></@frstyle>
                        <#break />
                    <#case "combo">
                    <#--可搜索下拉框-->
                        <@script name="static/thirdControl/combo/js/jquery.combo.select.js"></@script>
                        <@style name="static/thirdControl/combo/css/combo.select.css"></@style>
                        <#break/>
                    <#case "multiselect">
                    <#--<@script name="static/js/checkboxSelect.js"></@script>-->
                        <#break />
                    <#default>
                </#switch>
            </#list>
        </#if>
        ${css!}
    </head>
    <body>
    <#if import??>
        <#list import?split(",") as lib>
            <#switch lib>
                <#case "ace">
                    <@script name="static/thirdControl/ace/js/ace-elements.min.js"></@script>
                    <@script name="static/thirdControl/ace/js/ace.min.js"></@script>
                    <@script name="static/thirdControl/jquery/jquery.prompt.js"></@script>
                    <@script name="static/js/browserMsie.js"></@script>
                    <@style name="static/css/jquery.prompt.css"></@style>
                    <#break />
                <#case "authorize">
                    <@include name="../wf/common/rightsManagement.ftl"/>
                    <@include name="../wf/common//fieldColorManagement.ftl"/>
                    <@include name="../wf/common//xtYhManagement.ftl"/>
                    <#break />
                <#default>
            </#switch>
        </#list>
    </#if>
    <#nested />
    <@script name="static/bootstrap/js/bootstrap-dropdown.js"></@script>
    ${js!}
    </body>
    </html>
</#macro>

<#macro script name>
    <script src="<@rootPath/>/${name}" type="text/javascript"></script>
</#macro>

<#macro pfscript name>
    <script src="<@pfRootPath/>/${name}" type="text/javascript"></script>
</#macro>

<#macro fcscript name>
    <script src="<@fcRootPath/>/${name}" type="text/javascript"></script>
</#macro>

<#macro frscript name>
    <script src="<@frRootPath/>/${name}" type="text/javascript"></script>
</#macro>


<#macro style name>
    <link href="<@rootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>

<#macro pfstyle name>
    <link href="<@pfRootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>

<#macro fcstyle name>
    <link href="<@fcRootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>

<#macro frstyle name>
    <link href="<@frRootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>


<#macro rootPath>${springMacroRequestContext.getContextPath()}</#macro>
<#macro pfRootPath>${platformUrl!}</#macro>
<#macro fcRootPath>${fileCenterUrl!}</#macro>
<#macro frRootPath>${reportUrl!}</#macro>

<#macro msg>
    <div id="msgDiv" class="FromsToOk">
    </div>
    <script type="text/javascript">
        var processWidth = 0;
        var processInterval = 0;

        function loadMsgDiv() {
            processWidth = 0;
            processInterval = 0;
            var msgDiv = " <div class=\"progress progress-success  progress-striped active\"  id=\"alertdiv\" ><div id=\"processBar\" class=\"bar\" style=\"width: 5%;\"></div></div><div class=\"FromsToOkWord ToOk\" id=\"errorMsg\"></div>";
            $('#msgDiv').empty();
            $('#msgDiv').html(msgDiv);
            processInterval = setInterval(changeProcessBar, 1000)
        }

        function changeProcessBar() {
            processWidth = processWidth + 5;
            if (processWidth >= 98)
                processWidth = 98;
            $("#processBar")[0].style.width = processWidth + "%";
        }

        function alertProcess() {
            loadMsgDiv();
            $("#alertdiv").removeClass().addClass("progress progress-success  progress-striped active");
        }

        function alertSucess(html) {

            clearInterval(processInterval);
            $("#errorMsg").removeClass().addClass("FromsToOkWord ToOk");
            $("#processBar")[0].style.width = "100%";
            $("#errorMsg").html("<i class=\"icon_ok\"></i>" + html);
            $("#alertdiv").show();
        }

        function alertError(html) {
//        loadMsgDiv();
            $("#alertdiv").removeClass().addClass("progress progress-warning  progress-striped active");
            $("#errorMsg").removeClass().addClass("FromsToOkWord ToWrong");
            clearInterval(processInterval);
            $("#processBar")[0].style.width = "100%";
            $("#errorMsg").html("<i class=\"icon_worng\"></i>" + html);
            $("#alertdiv").show();
        }
    </script>
</#macro>
<#macro loading width="" height="">
    <div id="loading" class="loading"
         style="position:absolute;top:0px;left:0px;z-index: 9999;width: ${width!};height:  ${height!};display: block;">
        <img src="../static/img/loading.gif" alt=""/>
    </div>
    <script type="text/javascript">
        function showLoading() {
            $("#loading").show();
//        document.getElementById("loading").style.display="block";
        }

        function hideLoading() {
            $("#loading").hide();
//        document.getElementById("loading").style.display="none";
        }
    </script>
</#macro>

<#macro initTab  title="" url="">
    <style>
        body {
            overflow: hidden;
        }
    </style>
    <script id="tab" src="<@rootPath/>/static/js/tab.js" url="${url}"></script>
    <div class="PopPanel">
        <div class="panel-header panelBg ">
            <h3 class="panelLabel pull-left ">${title}</h3>
            <#--  <#if  buttonList!="">
                  <div class="pull-right">
                  <#list buttonList as button>

                  </#list>
                  </div>
              </#if>-->
        </div>
        <div class="panel-body">
            <div class="tabbable tabs-left businessContent">
                <ul class="nav nav-tabs" id="myTab">
                </ul>
                <div class="tab-content">
                    <iframe id="mainFrame" name="mainFrame" src="" width=100% height=100% style="border:0"
                            frameborder="0" scrolling="auto" marginwidth="0" marginheight="0">
                </div>
            </div>
        </div>
    </div>
</#macro>
<#macro include name>"
    <#include "${name}"/>
</#macro>