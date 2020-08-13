<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    body{
        overflow-y: hidden;
    }
    .no-padding-right{
        text-align: right;
    }


    /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;100% !important;
        height: 35px!important;
    }
    label {
        font-weight: bold;
    }
</style>
<script>
    $(function(){
        mainContainerHeight();
        $("#iframe").attr("src", "${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_spf_xm.cpt&op=write&"+$("#form").serialize());
        $("#search").click(function(){
            $("#iframe").attr("src", "${reportUrl!}/ReportServer?reportlet=analysis%2Ftj_spf_xm.cpt&op=write&"+$("#form").serialize());
        })
    })
    $(window).resize(function(){
        mainContainerHeight();
    })
    function mainContainerHeight() {
        var winHeight = $(window).height();
        var containerHieght = 0;
        if (winHeight > 0) {
            containerHieght = winHeight - 110;
        }
        $("#iframe").css({height:containerHieght + "px"});
    }
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">
        <form class="form" id="form">
            <div class="row">
                <div class="form-group">
                    <label class="col-xs-1 control-label no-padding-right">年份：</label>
                    <div class="col-xs-1">
                    <span class="input-icon">
                       <select name="year">
                           <option value="2015">2015年</option>
                           <option value="2013">2013年</option>
                           <option value="2014">2014年</option>
                           <option value="2016">2016年</option>
                       </select>
                    </span>
                    </div>
                    <button type="button" class="btn btn-sm btn-primary" id="search"><i class="ace-icon fa fa-search bigger-130"></i>查询</button>
                </div>
            </div>
        </form>
        <hr>
        <div class="row">
            <div class="col-xs-12">
                <iframe src=""  id="iframe"  style="border:0;width: 100%;position: fixed; " frameborder="0" scrolling="auto" marginwidth="0" marginheight="0"></iframe>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>


