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
   #dyhSearchPop .modal-dialog,#fcSearchPop .modal-dialog,#tdSearchPop  .modal-dialog {
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
        $("#dyhShow,#tdShow,#fcShow").click(function(){
            if(this.id=="dyhShow"){
                $("#dyhSearchPop").show();
            }else if(this.id=="tdShow"){
                $("#tdSearchPop").show();
            }else if(this.id=="fcShow"){
                $("#fcSearchPop").show();
            }
        });
        /*高级按钮点击事件 end*/

        /*添加水印*/
        /*$(".control-text").watermark("权利人");*/
        /*tab点击事件 begin*/
        $("#dyhTab,#tdTab,#fcTab").click(function(){
            var url;
            if(this.id=="dyhTab"){
                $("#home").addClass("active");
                dyhTableInit();
            }else if(this.id=="tdTab"){
                $("#file").addClass("active");
                tdTableInit();
            }else if(this.id=="fcTab"){
                $("#profile").addClass("active");
                fwTableInit();
            }
        })
        /*tab点击事件 end*/

        /*查询点击事件 begin*/
        $("#dyhSearchBtn").click(function(){
            var qlr=$("#dyhSearchText").val();
            $("#dyhSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson?"+$("#dyhSearchForm").serialize();
            tableReload("dyh-grid-table",Url,{qlr:qlr});
        })
        $("#fcSearchBtn").click(function(){
            var qlr=$("#fcSearchText").val();
            $("#fcSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getGdFwJson?"+$("#fcSearchForm").serialize();
            tableReload("fw-grid-table",Url,{rf1Dwmc:qlr});
        })
        $("#tdSearchBtn").click(function(){
            var qlr= $("#tdSearchText").val();
            $("#tdSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getGdTdJson?"+$("#tdSearchForm").serialize();
            tableReload("td-grid-table",Url,{rf1Dwmc:qlr});
        })
        /*查询点击事件 end*/

//        //限制只能选择海域和土地其中一个
//        $("#tdLi").click(function(){
//            if($("#tdLi").is(':checked')){
//                $("#hyLi").attr("disabled",true);
//            }else{
//                $("#hyLi").attr("disabled",false);
//            }
//        })
//        //限制只能选择海域和土地其中一个
//        $("#hyLi").click(function(){
//            if($("#hyLi").is(':checked')){
//                $("#tdLi").attr("disabled",true);
//            }else{
//                $("#tdLi").attr("disabled",false);
//            }
//        })

        //表单添加校验
        var xjValidate= $("#xjForm").validate();
        //新建操作
        $("#xjBtn").click(function(){
            if(xjValidate.form()){
                $("#xjBtn").attr("disabled",true);
                var options = {
                    url:'${bdcdjUrl}/bdcSjgl/checkBdcXm',
                    type:'post',
                    dataType:'json',
                    data:$("#xjForm").serialize(),
                    success:function (data) {
                        var alertSize=0;
                        var confirmSize=0;
                        if(data.length>0){
                            $.each(data,function(i,item){
                                if (item.checkModel == "confirm") {
                                    confirmSize++;
                                    $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')">查看</span>' + item.checkMsg + '</div>');
                                } else if (item.checkModel == "alert") {
                                    alertSize++;
                                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')" >查看</span>' + item.checkMsg + '</div>');
                                }
                            })
                        }
                        if(alertSize==0 && confirmSize==0){
                            csdjXjXm();
                        }else if(alertSize== 0 && confirmSize>0){
                            $("span[name='hlBtn']").click(function(){
                                $(this).parent().remove();
                                if($("#csdjConfirmInfo > div").size()==0){
                                    csdjXjXm();
                                }
                            })
                        }
                    },
                    error:function (data) {

                    }
                };
                $.ajax(options);
                return false;
            }
        })

        $("#dyhHide,#fcHide,#tdHide").click(function(){
            if(this.id=="dyhHide"){
                $("#dyhSearchPop").hide();
                $("#dyhSearchForm")[0].reset();
            }else if(this.id=="fcHide"){
                $("#fcSearchPop").hide();
                $("#fcSearchForm")[0].reset();
            }else if(this.id=="tdHide"){
                $("#tdSearchPop").hide();
                $("#tdSearchForm")[0].reset();
            }
        });
        $(".dyhSearchPop-modal,.tdSearchPop-modal,.fcSearchPop-modal").draggable({opacity: 0.7,handle: 'div.modal-header'});

        //单元号高级查询的搜索按钮事件
        $("#dyhGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson?"+$("#dyhSearchForm").serialize();
            tableReload("dyh-grid-table",Url,{qlr:""});
        })

        //土地高级查询的搜索按钮事件
        $("#tdGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getGdTdJson?"+$("#tdSearchForm").serialize();
            tableReload("td-grid-table",Url,{rf1Dwmc:""});
        })

        //房产高级查询的搜索按钮事件
        $("#fcGjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getGdFwJson?"+$("#fcSearchForm").serialize();
            tableReload("fw-grid-table",Url,{rf1Dwmc:""});
        })
    })
    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    //初始登记新建项目操作
    function csdjXjXm(){
        var options = {
            url:'${bdcdjUrl}/bdcSjgl/saveBdcXm',
            type:'post',
            dataType:'json',
            data:$("#xjForm").serialize(),
            success:function (data) {
                alert("保存成功");
                $("#xjBtn").attr("disabled",false);
                hideModal(data);
            },
            error:function (data) {
                alert("保存失败")
                $("#xjBtn").attr("disabled",false);
            }
        };
        $.ajax(options);
        return false;
    }
</script>
<div class="row">
    <div class="col-xs-8 leftContent" id="left">
        <div class="tabbable">
            <ul class="nav nav-tabs" id="myTab">
                <li class="active">
                    <a data-toggle="tab" id="tdTab" href="#file">
                        选择土地证
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" id="fcTab" href="#profile">
                        选择房产证
                    </a>
                </li>
                <li >
                    <a data-toggle="tab" id="dyhTab" href="#home">
                        选择不动产单元
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="file" class="tab-pane in active">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="tdSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="tdSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="tdShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "tdgl.ftl">
                </div>
                <div id="profile" class="tab-pane">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="fcSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="fcSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="fcShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "fwgl.ftl">
                </div>
                <div id="home" class="tab-pane ">
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" name="qlr" class="Sinput watermarkText" id="dyhSearchText" data-watermark="请输入权利人">
                                </td>
                                <td class="Search">
                                    <a href="#" id="dyhSearchBtn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="dyhShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <#include  "dyhgl.ftl">
                </div>
            </div>
        </div>
    </div>
    <div class="col-xs-4" id="right">
        <form class="form-base "  id="xjForm">
            <#--<div class="row">-->
                <#--<div class="col-xs-4">-->
                    <#--<label><s>*</s>不动产类型：</label>-->
                <#--</div>-->
                <#--<div class="col-xs-8">-->
                    <#--<ul class="SearchList affirmance1">-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="tdLi"  value="土地">土地-->
                        <#--</li>-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="fwLi" value="房屋">房屋-->
                        <#--</li>-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="lqLi" value="森林、林木">森林、林木-->
                        <#--</li>-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="cyLi" value="草原">草原-->
                        <#--</li>-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="gzwLi"  value="构筑物">构筑物-->
                        <#--</li>-->
                        <#--<li>-->
                            <#--<input type="checkbox" id="hyLi"  value="海域">海域-->
                        <#--</li>-->
                    <#--</ul>-->
                <#--</div>-->
            <#--</div>-->
            <div class="row tableRow">
                <div class="col-xs-4">
                    <label class="labelRow"><s>*</s>登记类型：</label>
                </div>
                <div class="col-xs-8">
                    <select  id="djlx" class="form-control" disabled>
                        <#list djList as djlx>
                            <option value="${djlx.dm!}" >${djlx.mc!}</option>
                        </#list>
                    </select>
                    <input type="hidden" name="djlx" id="djlx_text">
                </div>
            </div>
            <div class="row tableRow">
                <div class="col-xs-4">
                    <label class="labelRow"><s>*</s>权利类型：</label>
                </div>
                <div class="col-xs-8">
                    <select name="qllx" class="form-control" id="csdjQllx">
                        <#list qlList as qllx>
                            <option value="${qllx.dm!}" >${qllx.mc!}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="row tableRow">
                <div class="col-xs-4">
                    <label><s>*</s>项目名称：</label>
                </div>
                <div class="col-xs-8">
                    <input type="text" id="xmmc" name="xmmc"  class="form-control" required="true">
                    <#--<textarea id="cqzh"></textarea>-->
                </div>
            </div>
            <div class="row tableRow">
                <div class="col-xs-4">
                    <label><s>*</s>不动产单元号：</label>
                </div>
                <div class="col-xs-8">
                    <textarea id="bdcdyh" name="bdcdyh" class="form-control" required="true"></textarea>
                    <input type="hidden" id="djId" name="djId">
                    <input type="hidden" id="bdclx" name="bdclx">

                <#-- <a href="" class="btn btn-success beProducedBtn">生成</a>-->
                </div>
            </div>
            <div class="tableRow row">
                <div class="col-xs-4">
                    <label class="labelRow"><s>*</s>宗地/宗海号：</label>
                </div>
                <div class="col-xs-8 ">
                    <input type="text" id="zdzhh" name="zdzhh" class="form-control" required="true">
                </div>
            </div>
            <div class="tableRow row">
                <div class="col-xs-4">
                    <label class="labelRow">土地证号：</label>
                </div>
                <div class="col-xs-8">
                    <input  type="text"  id="tdzh" name="tdzh" class="form-control" >
                    <input type="hidden" id="tdid" name="tdid">
                </div>
            </div>
            <div class="tableRow row">
                <div class="col-xs-4">
                    <label class="labelRow">房产证号：</label>
                </div>
                <div class="col-xs-8">
                    <input type="text" id="fczh" name="fczh" class="form-control" >
                    <input type="hidden" id="fwid" name="fwid">
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">

                </div>
                <div class="col-xs-8">
                    <button class="btn btn-sm btn-primary" type="button"  id="xjBtn">确定</button>
                </div>
            </div>
            <#--验证信息显示div-->
            <div class="row tableRow">
                <div id="csdjAlertInfo" style="padding-right: 10px"></div>
                <div id="csdjConfirmInfo" style="padding-right: 10px"></div>
            </div>
        </form>
    </div>
</div>



