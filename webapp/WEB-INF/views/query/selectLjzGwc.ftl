<@com.html title="" import="ace,public">
<style>

    .main-content {
        margin-left: 0px;
    }
    .widget-box{
        height: 500px;
    }
    #tree2{
        height: 400px;
        border-left:none;
    }
    .center, .align-center {
        text-align: center !important;
    }
    .tree .tree-item{
        height: 40px;
    }

</style>
<script type="text/javascript"> ace.vars['base'] = '..'; </script>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>

    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-content">

            <div class="row">
                <div class="col-xs-12">

                    <!-- #section:plugins/fuelux.treeview -->
                    <div class="row">
                        <div class="col-xs-3">
                            <div class="widget-box widget-color-blue2">
                                <div class="widget-header">
                                    <h4 class="widget-title lighter smaller">逻辑幢</h4>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main padding-8">
                                        <div id="ljzTree" class="tree"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-9">
                            <div class="widget-box widget-color-green2">
                                <div class="widget-header">
                                    <h4 class="widget-title lighter smaller">楼盘表查看</h4>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main padding-8">
                                        <div id="tree2" >
                                            <iframe id="mainiframe"frameborder="no" border="0" width="100%" height="100%" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- /section:plugins/fuelux.treeview -->
                    <#--<script type="text/javascript">-->
                        <#--var $assets = "../assets";//this will be used in fuelux.tree-sampledata.js-->
                    <#--</script>-->


                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->


    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<#--<!--[if !IE]> &ndash;&gt;-->
<#--<script type="text/javascript">-->
    <#--window.jQuery || document.write("<script src='static/thirdControl/jquery/jquery.min.js'>"+"<"+"/script>");-->
<#--</script>-->

<#--<!-- <![endif]&ndash;&gt;-->

<#--<!--[if IE]>-->
<#--<script type="text/javascript">-->
    <#--window.jQuery || document.write("<script src='static/thirdControl/jquery/jquery1x.min.js'>"+"<"+"/script>");-->
<#--</script>-->
<#--<![endif]&ndash;&gt;-->


<!-- inline scripts related to this page -->
<script type="text/javascript">
//    var mulSelectJson1=window.opener.mulSelectJson;
    var zjgcdyFw="${zjgcdyFw!}";
    var bdcdjUrl="${bdcdjUrl}";
    var portalUrl="${portalUrl!}";
    var proid="${proid}";
    var mulSelectJson={};
    if($.cookie('lpb_cookie')!=null){
        mulSelectJson=JSON.parse($.cookie('lpb_cookie'));
    }
    var isYc="${isYc!}";
//    var tree_data_new={逻辑幢A : {name: '逻辑幢A', type: 'item',value:'2222'},逻辑幢B : {name: '逻辑幢B', type: 'item',value:'11111'},逻辑幢C: {name: '逻辑幢C', type:'item',value:'11111'}};
    var treeDataSourceNew= new DataSourceTree({data: mulSelectJson});


    jQuery(function($){

        $('#ljzTree').ace_tree({
            dataSource: treeDataSourceNew ,
            multiSelect:false,
            loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon' : 'ace-icon tree-minus',
            'close-icon' : 'ace-icon tree-plus',
            'selectable' : true,
            'selected-icon' : 'ace-icon fa fa-check',
            'unselected-icon' : 'ace-icon fa fa-times'
        });






        $('#ljzTree').on('loaded', function (evt, data) { alert("loaded")	;	});

        $('#ljzTree').on('opened', function (evt, data) { alert("opened")	;		});

        $('#ljzTree').on('closed', function (evt, data) {alert("closed")	;		});

        $('#ljzTree').on('selected',
                function (evt, data) {
                    var url="";
//                    alert(data)	;
                    var nameStr=data.info[0].name;
                    var zdzhh= nameStr.substring(nameStr.indexOf("(")+1,nameStr.indexOf(")"));
                    if(isYc!=""&& isYc!=null && isYc=="1"){
                        url=bdcdjUrl+"/lpb/ycLpb";
                    }
                    else{
                        url=bdcdjUrl+"/lpb/lpb";
                    }
                    url=url+"?isLjz="+data.info[0].ljz+"&lszd="+zdzhh+"&dcbId="+data.info[0].value+"&proid=${proid!}"+"&mulSelect=${mulSelect!}&showQl=false&type=${type!}&isNotWf=true&zjgcdyFw=${zjgcdyFw!}&isNotBack=true";
//                  $("#tree2").load(url);
//
//                    $('#mainiframe').attr("src",url) ;
                    var bdcdyhStr=new Array();
                    if($.cookie('lpb_cookie')!=null) {
                        var jsonStr = JSON.parse($.cookie('lpb_cookie'));
                        $.each(jsonStr, function (idx, item) {
                            var info = item.property;
                            for (var i = 0; i < info.length; i++) {
                                bdcdyhStr.push(info[i].substring(info[i].indexOf("$") + 1))
                            }
                        });
                    } else if(data.info[0].property!=null){
                        var info=data.info[0].property;
                        for(var i=0;i<info.length;i++){
                            bdcdyhStr.push(info[i].substring(info[i].indexOf("$")+1))
                        }
                    }
                    openPostPage(url,"bdcdyhStr",bdcdyhStr,"iframe");

                });

    });

//    function createXm(){
//        window.opener.createXmxx();
//    }

    $(function () {
        $("#tipHide,#tipCloseBtn").click(function () {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        });
    });

</script>
<@script name="static/js/plGwc.js"></@script>
<div class="main-container">
    <div class="center">
        <button class="btn btn-primary" type="button" onclick="createXmxx()">确定</button>
    </div>
</div>

<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo" ></div>
                <div id="csdjConfirmInfo" ></div>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
</@com.html>