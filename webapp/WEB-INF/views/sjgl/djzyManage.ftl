<@com.html title="登记资源配置" import="ace">
<style>
.tabs-left>.nav-tabs>li>a{
    height: 70px;
}
.tab-content {
    padding:0px!important;
    overflow: hidden!important;
}
</style>
<script>
    $(function(){
        $(window).resize(function(){
            $(".tab-content").css("height",$(window).height()-50);
        })
        var height=$(window).height()-50;
        $(".tab-content").css("height",height);

        //li标签添加点击事件
        $("#relationLi").click(function(){
            $("#iframe").attr("src","${bdcdjUrl}/bdcConfig/toRelationConfig");
        })
        $("#resourceLi").click(function(){
            $("#iframe").attr("src","${bdcdjUrl}/bdcConfig/toResourceConfig");
        })
        $("#yzLi").click(function(){
            $("#iframe").attr("src","${bdcdjUrl}/bdcConfig/toCheckConfig");
        })
    })
</script>
<div id="navbar" class="navbar navbar-default">
    <div class="navbar-container" id="navbar-container">
        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <small>
                    <i class="fa fa-pencil-square-o"></i>
                    <span>配置中心</span>
                </small>
            </a>
        </div>
    </div>
    <!-- /.navbar-container -->
</div>
<div class="main-container">
    <div class="tabbable tabs-left">
        <ul class="nav nav-tabs" id="myTab">
            <li class="active" id="resourceLi">
                <a data-toggle="tab" href="#zyTab">
                    <center style="margin-top: 10px;">
                        <i class=" ace-icon fa fa-user bigger-140"></i>
                        <div class="">资源数据</div>
                    </center>
                </a>
            </li>

            <li id="relationLi">
                <a data-toggle="tab" href="#djzygxTab">
                    <center style="margin-top: 10px;">
                        <i class="ace-icon fa fa-rocket bigger-140"></i>
                        <div class="">登记资源配置</div>
                    </center>
                </a>
            </li>

            <li id="yzLi">
                <a data-toggle="tab" href="#yzTab">
                    <center style="margin-top: 10px;">
                        <i class="ace-icon fa fa-rocket bigger-140"></i>
                        <div class="">验证配置</div>
                    </center>
                </a>
            </li>
        </ul>

        <div class="tab-content">
            <iframe id="iframe"  name="mainFrame" src="${bdcdjUrl}/bdcConfig/toResourceConfig" width=100% height='100%' style="border:0;" frameborder="0"  scrolling="auto"  marginwidth="0" marginheight="0"></iframe>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>