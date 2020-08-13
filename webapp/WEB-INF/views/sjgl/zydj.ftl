<style type="text/css">
   /*重写样式 begin*/
   .tab-content {
       overflow-y: auto;
       height: auto;
   }
   .simpleSearch {
        width: auto;
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
   /*重写样式 end*/
   #zydjSearchPop  .modal-dialog {
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
   /*验证弹出的提示样式修改*/
   .alert {
       font-size: 12px;
       border-radius: 4px;
       padding: 5px;
       margin-bottom: 5px;
   }
   .tableRow,.lableRow {
       margin-bottom: 6px;
       margin-top: 6px
   }

</style>
<script>
    $(function(){
        //初始化表格
        /*高级按钮点击事件 begin*/
        $("#zydjShow").click(function(){
                $("#zydjSearchPop").show();
        });
        /*高级按钮点击事件 end*/

        /*查询点击事件 begin*/
        $("#zydjSarchBtn").click(function(){
            var zl=$("#zydjSarchText").val();
            $("#zydjSearchForm")[0].reset();
            var Url="${bdcdjUrl}/bdcSjgl/getBdcBjxmListByPage?"+$("#zydjSearchForm").serialize();
            tableReload("zydj-grid-table",Url,{zl:zl});
        })
        /*查询点击事件 end*/

        $("#zydjHide").click(function(){
                $("#zydjSearchPop").hide();
                $("#zydjSearchForm")[0].reset();
        });
        $(".zydjSearchPop-modal").draggable({opacity: 0.7,handle: 'div.modal-header'});

        //单元号高级查询的搜索按钮事件
        $("#zydjSearchBtn").click(function(){
            var Url="${bdcdjUrl}/bdcSjgl/getBdcBjxmListByPage?"+$("#zydjSearchForm").serialize();
            tableReload("zydj-grid-table",Url,{zl:""});
        })

        //老数据加载
         $("#oldData").click(function(){
             if(!$("#collapseTwo").hasClass("in")){
             /*
                 $("#collapseTwo").css("height","auto");
                 //初始化了的话不会再初始化
                 $("#zydjTdTab").click();
                 tableReload("zydj-td-grid-table", "${bdcdjUrl}/bdcSjgl/getGdTdJson","");*/
             }
         })
    })
    //表单校验
   // var zydjValidate= $("#zydjXjForm").validate();
    //新建操作
    function zydjXj(){
        if(zydjValidate.form()){
            $("#zydjXjBtn").attr("disabled",true);
            var options = {
                url:'${bdcdjUrl}/bdcSjgl/checkBdcXm',
                type:'post',
                dataType:'json',
                data:$("#zydjXjForm").serialize(),
                success:function (data) {
                    var alertSize=0;
                    var confirmSize=0;
                    if(data.length>0){
                        $.each(data,function(i,item){
                            if (item.checkModel == "confirm") {
                                confirmSize++;
                                $("#confirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')">查看</span>' + item.checkMsg + '</div>');
                            } else if (item.checkModel == "alert") {
                                alertSize++;
                                if(item.checkCode=='199'){
                                    $("#alertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                                }else{
                                    $("#alertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')" >查看</span>' + item.checkMsg + '</div>');
                                }
                            }
                        })
                    }
                    if(alertSize==0 && confirmSize==0){
                        zydjXjXm();
                    }else if(alertSize==0 && confirmSize>0){
                        $("span[name='hlBtn']").click(function(){
                            $(this).parent().remove();
                            if($("#confirmInfo > div").size()==0){
                                zydjXjXm();
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
    }
    //除初始登记外的新建项目的请求
    function zydjXjXm(){
         var options = {
                url:'${bdcdjUrl}/bdcSjgl/saveBdcXm',
                type:'post',
                dataType:'json',
                data:$("#zydjXjForm").serialize(),
                success:function (data) {
                    alert("保存成功");
                    $("#zydjXjBtn").attr("disabled",false);
                    hideModal(data);
                },
                error:function (data) {
                    alert("保存失败");
                    $("#zydjXjBtn").attr("disabled",false);
                }
            };
            $.ajax(options);
        return false;
    }

    function openProjectInfo(proid){
        if (proid && proid != undefined) {
            window.open('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid);
        }
    }

</script>
<div class="row">
    <div class="col-xs-8 leftContent" id="zydjLeft">
        <div id="accordion" class="accordion-style1 panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" id="newData">
                            <i class="bigger-110 ace-icon fa fa-angle-down" data-icon-hide="ace-icon fa fa-angle-down" data-icon-show="ace-icon fa fa-angle-right"></i>
                            &nbsp;<span>新数据</span>
                        </a>
                    </h4>
                </div>

                <div class="panel-collapse collapse in" id="collapseOne" style="height: auto;">
                    <div class="panel-body">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" name="zl" class="Sinput watermarkText" id="zydjSarchText" data-watermark="请输入坐落">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="zydjSarchBtn">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                    <td style="border: 0px">&nbsp;</td>
                                    <td>
                                        <button type="button" class="btn01 AdvancedButton" id="zydjShow">高级搜索</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <#include  "zydjTable.ftl">
                    </div>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" id="oldData">
                            <i class="bigger-110 ace-icon fa fa-angle-right" data-icon-hide="ace-icon fa fa-angle-down" data-icon-show="ace-icon fa fa-angle-right"></i>
                            &nbsp;<span>老数据</span>
                        </a>
                    </h4>
                </div>

                <div class="panel-collapse collapse" id="collapseTwo" style="height: 0px;">
                    <div class="panel-body">
                         <#include  "zydjBdcxm.ftl">
                    </div>
                </div>
            </div>
        </div>
     </div>
    <div class="col-xs-4" id="zydjRight">
    </div>
</div>


