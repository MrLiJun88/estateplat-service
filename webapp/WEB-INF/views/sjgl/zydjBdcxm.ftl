<style type="text/css">
   /*重写样式 begin*/
   ul, ol {
        margin-left: 0px;
   }
   .SearchList li {
       float: left;
       padding: 0px 10px 10px 0px;
   }
   .tab-content {
       overflow-y: auto;
       height: auto;
   }
   .simpleSearch {
        width: auto;
   }
   .form-base select{
       width: 90%!important;
   }
   .form-base input[type="text"],textarea{
       width: 90%!important;
   }
   .simpleSearch .Sinput{
       border:0px;
       height:34px;
       min-width:330px;
       font-size:12px;
       text-indent:10px;
   }
   .form-base .row>.col-xs-4 {
       text-align: right;
       padding: 0px;
   }
    s{
        color: red;
        padding-right: 5px;
        text-decoration: none;
    }
    label{
        font-size: 12px
    }
   textarea, input[type="text"]{
       margin: 0px;
   }
   textarea{
       resize:none;
   }
   /*重写样式 end*/
   /*高级查询弹出移动框*/
   #zydjDyhSearchPop .modal-dialog,#zydjFcSearchPop .modal-dialog,#zydjTdSearchPop  .modal-dialog {
       width: 600px;
       position: fixed;
       top: 20px;
       right: 0;
       bottom: 0;
       left: 0;
       z-index: 1050;
       -webkit-overflow-scrolling: touch;
       outline: 0;
   }
   /*表单样式*/
   .tableRow,.labelRow {
       margin-bottom: 6px;
       margin-top: 6px
   }

</style>
<script>
    $(function(){
        /*高级按钮点击事件 begin*/
        $("#zydjDyhShow,#zydjTdShow,#zydjFcShow").click(function(){
            if(this.id=="zydjDyhShow"){
                $("#zydjDyhSearchPop").show();
            }else if(this.id=="zydjTdShow"){
                $("#zydjTdSearchPop").show();
            }else if(this.id=="zydjFcShow"){
                $("#zydjFcSearchPop").show();
            }
        });
        /*高级按钮点击事件 end*/

        /*tab点击事件 begin*/
        $("#zydjDyhTab,#zydjTdTab,#zydjFcTab").click(function(){
            var url;
            if(this.id=="zydjDyhTab"){
                $("#zydjHome").addClass("active");
                zydjDyhTableInit();
            }else if(this.id=="zydjTdTab"){
                $("#zydjFile").addClass("active");
                zydjTdTableInit();
            }else if(this.id=="zydjFcTab"){
                $("#zydjProfile").addClass("active");
                zydjFwTableInit();
            }
        })
        /*tab点击事件 end*/

        /*查询点击事件 begin*/
        $("#zydjDyhSearchBtn").click(function(){
            var qlr=$("#zydjDyhSearchText").val();
            $("#zydjDyhSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson?"+$("#zydjDyhSearchForm").serialize();
            tableReload("zydj-dyh-grid-table",Url,{qlr:qlr});
        })
        $("#zydjFcSearchBtn").click(function(){
            var qlr=$("#zydjFcSearchText").val();
            $("#zydjFcSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getGdFwJson?"+$("#zydjFcSearchForm").serialize();
            tableReload("zydj-fw-grid-table",Url,{rf1Dwmc:qlr});
        })
        $("#zydjTdSearchBtn").click(function(){
            var qlr= $("#zydjTdSearchText").val();
            $("#zydjTdSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getGdTdJson?"+$("#zydjTdSearchForm").serialize();
            tableReload("zydj-td-grid-table",Url,{rf1Dwmc:qlr});
        })
        /*查询点击事件 end*/

        $("#zydjDyhHide,#zydjFcHide,#zydjTdHide").click(function(){
            if(this.id=="zydjDyhHide"){
                $("#zydjDyhSearchPop").hide();
                $("#zydjDyhSearchForm")[0].reset();
            }else if(this.id=="zydjFcHide"){
                $("#zydjFcSearchPop").hide();
                $("#zydjFcSearchForm")[0].reset();
            }else if(this.id=="zydjTdHide"){
                $("#zydjTdSearchPop").hide();
                $("#zydjTdSearchForm")[0].reset();
            }
        });
        $(".zydjDyhSearchPop-modal,.zydjTdSearchPop-modal,.zydjFcSearchPop-modal").draggable({opacity: 0.7,handle: 'div.modal-header'});

        //单元号高级查询的搜索按钮事件
        $("#zydjDyhGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson?"+$("#zydjDyhSearchForm").serialize();
            tableReload("zydj-dyh-grid-table",Url,{qlr:""});
        })

        //土地高级查询的搜索按钮事件
        $("#zydjTdGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getGdTdJson?"+$("#zydjTdSearchForm").serialize();
            tableReload("zydj-td-grid-table",Url,{rf1Dwmc:""});
        })

        //房产高级查询的搜索按钮事件
        $("#zydjFcGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getGdFwJson?"+$("#zydjFcSearchForm").serialize();
            tableReload("zydj-fw-grid-table",Url,{rf1Dwmc:""});
        })
    })
</script>
<div class="row">
    <div class="tabbable">
            <ul class="nav nav-tabs" id="zydjMyTab">
                <li class="active">
                    <a data-toggle="tab" id="zydjTdTab" href="#zydjFile">
                        选择土地证
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" id="zydjFcTab" href="#zydjProfile">
                        选择房产证
                    </a>
                </li>
                <li >
                    <a data-toggle="tab" id="zydjDyhTab" href="#zydjHome">
                        选择不动产单元
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="zydjFile" class="tab-pane in active">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="zydjTdSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="zydjTdSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="zydjTdShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "zydjTdgl.ftl">
                </div>
                <div id="zydjProfile" class="tab-pane">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="zydjFcSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="zydjFcSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="zydjFcShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "zydjFwgl.ftl">
                </div>
                <div id="zydjHome" class="tab-pane ">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="zydjDyhSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="zydjDyhSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="zydjDyhShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "zydjDyhgl.ftl">
                </div>
            </div>
        </div>
</div>



