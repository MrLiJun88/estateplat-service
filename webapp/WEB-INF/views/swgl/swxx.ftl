<@com.html title="税务信息" import="ace,colorbox">
<style>
    #cboxContent {
        overflow: inherit;
    }
</style>
<script type="text/javascript">
    $(function(){
        //colorbox
        var $overflow = '';
        var colorbox_params = {
            rel: 'colorbox',
            reposition:true,
            photo:true,
            scalePhotos:true,
            scrolling:false,
            transition:"none",
            previous:'<i class="ace-icon fa fa-arrow-left"></i>',
            next:'<i class="ace-icon fa fa-arrow-right"></i>',
            close:'&times;',
            current:'{current} of {total}',
            maxWidth:'100%',
            maxHeight:'100%',
            onOpen:function(){
                $overflow = document.body.style.overflow;
                document.body.style.overflow = 'hidden';
            },
            onClosed:function(){
                document.body.style.overflow = $overflow;
            },
            onComplete:function(){
                $.colorbox.resize();
            }
        };

        $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
        $("#cboxLoadingGraphic").append("<i class='ace-icon fa fa-spinner orange'></i>");

        $(".tooltipBtn").tooltip({
            placement:"bottom"
        });

        //获取完税状态
        $("#getSwzt").click(function(){
            location.reload();
        })
        //共享数据 插入或修改
        $("#shareData").click(function(){
            //遮罩
            $.blockUI({ message:"共享数据中……" });
            $.ajax({
                url:'${bdcdjUrl}/dataShare/saveData?proid=${proid!}',
                type:'post',
                dataType:'json',
                success:function (data) {
                    alert(data);
                    $("#shareData").attr("disabled",false);
                    setTimeout($.unblockUI, 10);
                    location.reload();
                },
                error:function (data) {
                    setTimeout($.unblockUI, 10);
                    alert("操作失败")
                }
            });
        })

        //获取中间库附件到非工作流文件中心
        $("#saveToGlobleFc").click(function(){
            $(this).attr("disabled",true);
            //遮罩
            $.blockUI({ message:"获取附件中……" });
            $.ajax({
                url:'${bdcdjUrl}/dataShare/saveToGlobleFc?proid=${proid!}',
                type:'post',
                dataType:'json',
                success:function (data) {
                    alert(data);
                    $("#saveToGlobleFc").attr("disabled",false);
                    setTimeout($.unblockUI, 10);
                    location.reload();
                },
                error:function (data) {
                    setTimeout($.unblockUI, 10);
                    alert("操作失败")
                }
            });
        })

        //将附件复制到目标节点
        $("#copyFileToNode").click(function(){
            var ids=new Array();
            $("input[name='file']:checked").each(function(index,data){
                /*if(index!=0){
                    ids += ","
                }*/
                ids.push($(this).val());
            })
            if(ids.length==0){
               alert("请选择上传附件");
            }else{
                $("#copyFileToNode").attr("disabled",true);
                //遮罩
                $.blockUI({ message:"上传附件中……" });
                $.ajax({
                    url:'${bdcdjUrl}/dataShare/copyFileToNode?proid=${proid!}&fieldIds='+ids,
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        alert(data);
                        $("#copyFileToNode").attr("disabled",false);
                        setTimeout($.unblockUI, 10);
                    },
                    error:function (data) {
                        setTimeout($.unblockUI, 10);
                        alert("操作失败")
                    }
                });
            }
        })

    })
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="row">
            <div class="widget-box transparent">
                <div class="widget-header widget-header-large">
                    <h3 class="widget-title grey lighter">
                        <i class="fa fa-skype green"></i>
                        税务信息
                    </h3>

                    <div class="widget-toolbar no-border invoice-info">
                        <button class="btn btn-sm btn-default tooltipBtn" title="共享数据到中间库(包括附件)" type="button" id="shareData">
                            <i class="ace-icon fa fa-upload"></i>
                            共享数据
                        </button>
                        <#if tbInfo??>
                            <#if tbInfo.customsstate! ==0>
                                <button class="btn btn-sm btn-primary tooltipBtn" title="获取完税状态" type="button" id="getSwzt" >
                                    <i class="ace-icon fa fa-refresh"></i>
                                    获取完税状态
                                </button>
                            </#if>
                        </#if>
                        <button class="btn btn-sm btn-info tooltipBtn" type="button" title="获取中间库中的税务附件" id="saveToGlobleFc" >
                            <i class="ace-icon fa fa-file"></i>
                            获取附件
                        </button>
                        <button class="btn btn-sm btn-success tooltipBtn" type="button" title="将选中的税务附件上传到文件中心" id="copyFileToNode" >
                            <i class="ace-icon fa fa-upload"></i>
                            提交附件
                        </button>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="space-6"></div>
                   <#if tbInfo??>
                        <#if tbInfo.customsstate! ==0>
                            <div><h4>完税状态:未完税</h4></div>
                        <#else>
                            <div><h4>完税状态:完税</h4></div>
                            <div><h4>完税时间:${tbInfo.customstime?date!} ${tbInfo.customstime?time!}</h4></div>
                        </#if>
                   <#else>
                       <div><h4>完税状态:未完税</h4></div>
                   </#if>
                </div>
            </div>
            <div class="space-6"></div>
            <div>
                <ul class="ace-thumbnails clearfix">
                <!-- #section:pages/gallery -->
                    <#if fileIdList??>
                      <#list fileIdList as filed>
                          <li>
                              <a href="${fileCenterUrl}/file/get.do?fid=${filed}" title="" data-rel="colorbox">
                                  <img width="250px" height="250px" src="${fileCenterUrl}/file/get.do?fid=${filed}" />
                              </a>
                              <div class="tags">
                                <span class="label-holder">
                                    <input type="checkbox" style="zoom:150%;margin-right: 5px" name="file" value="${filed}">
                                </span>
                              </div>
                          <#--<div class="tools">
                              <a href="#">
                                  <i class="ace-icon fa fa-link"></i>
                              </a>

                              <a href="#">
                                  <i class="ace-icon fa fa-paperclip"></i>
                              </a>

                              <a href="#">
                                  <i class="ace-icon fa fa-pencil"></i>
                              </a>

                              <a href="#">
                                  <i class="ace-icon fa fa-times red"></i>
                              </a>
                          </div>-->
                          </li>
                      </#list>
                    </#if>
                </ul>
            </div>
            <!-- PAGE CONTENT ENDS -->
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
