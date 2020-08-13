<@com.html title="申请类型和权利类型关系表配置" import="sqlxQllx">
<style>
    .btn-group > .dropdown-menu {
        overflow: auto;
        max-height: 150px;
    }
</style>

<script>
     $(function(){
         //登记类型变换事件
         $("#djlxSelect").change(function(){
             dyfsIsZgedy();
             getSqlxByDjlxAndBdclx();
         });
         //申请类型变换事件
         $("#sqlxSelect").change(function(){
             getOthersBySqlx();
         });
         //默认值
         $("#djlxSelect").val('${djlx}');
         $("#djlxSelect").change();

         //下拉框变更事件
         $("#qllxdm li").click(function(){
            $("#qllxUl").html("");
            var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
            $("#qllxUl").append(htmlStr);
         });

         $("#yqllxdm li").click(function(){
             var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
             var val = $(this).val();
             var bol = false;
             $("#yqllxdmUl li").each(function(index){
                 var liValue = $(this).val();
                 if(val == liValue){
                     bol = true;
                 }
             });
             if(!bol) {
                 $("#yqllxdmUl").append(htmlStr);
             }

         });

         $("ul.content li button").live("click",function(){
              $(this).parent().remove();
         });



         $("#ysqlxdm li").live("click",function(){
             var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';

             var val = $(this).val();
             var bol = false;
             $("#ysqlxdmUl li").each(function(index){
                 var liValue = $(this).val();
                 if(val == liValue){
                     bol = true;
                 }
             });
             if(!bol) {
                 $("#ysqlxdmUl").append(htmlStr);
             }

         });

         $("#bdcdyly li").click(function(){
             $("#bdcdylyUl").html("");
             var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
             $("#bdcdylyUl").append(htmlStr);
         })

         $("#dzwtzm li").click(function(){
             $("#dzwtzmUl").html("");
             var htmlStr = '<li value="'+$(this).get(0).innerText+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
             $("#dzwtzmUl").append(htmlStr);
         })

         $("#zdtzm li").click(function(){
             $("#zdtzmUl").html("");
             var htmlStr = '<li value="'+$(this).get(0).innerText+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
             $("#zdtzmUl").append(htmlStr);
         })

         $("#dyfs li").click(function(){
             $("#dyfsUl").html("");
             var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
             $("#dyfsUl").append(htmlStr);

         })

         //保存事件
         $("#save").click(function(){
             var paramStr = getParamStr();
             $.ajax({
                 type: "post",
                 url: "${bdcdjUrl}/bdcConfig/saveSqlxQllxRel?"+paramStr,
                // data:{sqlxdm:$("#sqlxSelect  option:selected").val()},
                 dataType: "json",
                 success: function (data) {
                     alert(data.result);
                 },
                 error: function (data) {
                 }
             });
         })
     })

     function getParamStr(){
         var paramStr = "";
         paramStr += $("#sqQlRelForm").serialize();
         var qllxdm = getDm("#qllxUl li");
         paramStr += "&qllxdm=" + qllxdm;
         var yqllxdm  = getDm("#yqllxdmUl li");
         paramStr += "&yqllxdm=" + yqllxdm;
         var dzwtzm =getDmByprimaryJs("dzwtzmUl",0)
         paramStr += "&bdclx=" + dzwtzm;
         var bdcdyly = getDm("#bdcdylyUl li");
         paramStr += "&bdcdyly=" + bdcdyly;
         var zdtzm = getDmByprimaryJs("zdtzmUl",0);
         paramStr += "&zdtzm=" + zdtzm;
         var ysqlxdm = getDm("#ysqlxdmUl li");
         paramStr += "&ysqlxdm=" + ysqlxdm;
         var dyfs = getDm("#dyfsUl li");
         paramStr += "&dyfs=" + dyfs;
         return paramStr;
     }
     //原生js获取li的值
     function getDmByprimaryJs(idName,index){
         if(arguments.length>1) {
             var eles = document.getElementById(idName).getElementsByTagName("li");
             return eles.length > 0 ? eles[index].getAttribute("value"): "";
         }else{
             return document.getElementById(idName).getElementsByTagName("li");
         }
     }

     function getDm(liStr) {
         var dm = "";
         $(liStr).each(function(){
            dm += $(this).val() + ",";
         }) ;
         dm = dm.substring(0,dm.length-1);
         return dm;
     }


     function  dyfsIsZgedy(){
         //抵押权登记类型代码：1100
         if($("#djlxSelect  option:selected").val() == "1100") {
             $("#dyfsIsZgedy").css("display","block");
         }
         else{
             $("#dyfsIsZgedy").css("display","none");
         }
     }
     function getSqlxByDjlxAndBdclx(){
         $.ajax({
             type: "GET",
             url: "${bdcdjUrl}/bdcConfig/getSqlxByDjlx",
             data:{djlx:$("#djlxSelect  option:selected").val()},
             dataType: "json",
             success: function (result) {
                 //清空
                 $("#sqlxSelect").html("");
                 $("#ysqlxdm").html("");
                 if(result !=null && result!=''){
                     $.each(result,function(index,data){
                         $("#sqlxSelect").append('<option value="'+data.dm+'" >'+data.mc+'</option>');
                         $("#ysqlxdm").append('<li value="'+data.dm+'"><a href="#">'+data.mc+'</a></li>');
                     })
                 }
                 //$("#sqlxSelect").trigger("chosen:updated");
                 $("#sqlxSelect").change();

                 //$("#ysqlxdm").trigger("chosen:updated");
                 //$("#ysqlxdm").change();

             },
             error: function (data) {
             }
         });
     }

     function getOthersBySqlx() {
         $.ajax({
             type: "GET",
             url: "${bdcdjUrl}/bdcConfig/getOthersBySqlx",
             data: {sqlx: $("#sqlxSelect  option:selected").val()},
             dataType: "json",
             success: function (result) {
                 //清空
                 if (result != null && result != '') {
                     //$("#relId").val(result.id);
                     if (result.hasOwnProperty("qllxdm")) {
                         var val = result.qllxdm.split(",");
                         $("#qllxUl").html("");
                         $("#qllxdm li").each(function () {
                             if ($(this).val() == result.qllxdm) {
                                 var htmlStr = '<li value="' + $(this).val() + '"><i class="icon_01"></i><font>' +$(this).get(0).innerText + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                 $("#qllxUl").append(htmlStr);
                             }
                         });
                     }else{
                         $("#qllxUl").html("");
                     }
                     if (result.hasOwnProperty("yqllxdm")) {
                         var val = result.yqllxdm.split(",");
                         $("#yqllxdmUl").html("");
                         $.each(val, function (index, data) {
                             $("#qllxdm li").each(function () {
                                 if ($(this).val() == data) {
                                     var htmlStr = '<li value="' + $(this).val() + '"><i class="icon_01"></i><font>' + $(this).get(0).innerText + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                     $("#yqllxdmUl").append(htmlStr);
                                 }
                             });
                         })
                     }else{
                         $("#yqllxdmUl").html("");
                     }
                     if (result.hasOwnProperty("ysqlxdm")) {
                         var val = result.ysqlxdm.split(",");
                         $("#ysqlxdmUl").html("");
                         $.each(val, function (index, data) {
                             $("#ysqlxdm li").each(function () {
                                 if ($(this).val() == data) {
                                     var htmlStr = '<li value="' + $(this).val() + '"><i class="icon_01"></i><font>' + $(this).get(0).innerText + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                     $("#ysqlxdmUl").append(htmlStr);
                                 }
                             });
                         })
                     }else{
                         $("#ysqlxdmUl").html("");
                     }
                     if (result.hasOwnProperty("bdcdyly")) {
                         //$("#bdcdyly").selectpicker('val', result.bdcdyly);
                         $("#bdcdylyUl").html("");
                         $("#bdcdyly li").each(function () {
                             if ($(this).val() == result.bdcdyly) {
                                 var htmlStr = '<li value="' + $(this).val() + '"><i class="icon_01"></i><font>' + $(this).get(0).innerText + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                 $("#bdcdylyUl").append(htmlStr);
                             }
                         });

                     }else{
                         $("#bdcdylyUl").html("");
                     }
                     if (result.hasOwnProperty("bdclx")) {
                         // $("#bdclx").selectpicker('val', result.bdclx);
                         /* $("#dzwtzm li").each(function(){
                              if($(this).val() == result.bdclx){
                                  var htmlStr = '<li value="'+$(this).val()+'"><i class="icon_01"></i><font>'+$(this).get(0).innerText+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                  $("#dzwtzmUl").append(htmlStr);
                              }
                          });*/
                         var dzwtzms = getDmByprimaryJs("dzwtzm");
                         $("#dzwtzmUl").html("");
                         for (var i = 0; i < dzwtzms.length; i++) {
                             var dzwtzm=dzwtzms[i].getAttribute("value");
                             if (dzwtzm== result.bdclx) {
                                 var htmlStr = '<li value="' + dzwtzm + '"><i class="icon_01"></i><font>' + dzwtzms[i].innerHTML + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                 $("#dzwtzmUl").append(htmlStr);
                             }
                         }
                     }else{
                         $("#dzwtzmUl").html("");
                     }
                     if (result.hasOwnProperty("dyfs")) {
                         //$("#dyfs").selectpicker('val',result.dyfs);
                         $("#dyfsUl").html("");
                         $("#dyfs li").each(function () {
                             if ($(this).val() == result.dyfs) {
                                 //$("#dyfsUl").append($(this));
                                 var htmlStr = '<li value="' + $(this).val() + '"><i class="icon_01"></i><font>' + $(this).get(0).innerText + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                 $("#dyfsUl").append(htmlStr);
                             }
                         });
                     }else{
                         $("#dyfsUl").html("");
                     }
                     if (result.hasOwnProperty("zdtzm")) {
                         var zdtzms = getDmByprimaryJs("zdtzm");
                         $("#zdtzmUl").html("");
                         for (var i = 0; i < zdtzms.length; i++) {
                             var zdtzm = zdtzms[i].getAttribute("value");
                             if (zdtzm == result.zdtzm) {
                                 var htmlStr = '<li value="' + zdtzm + '"><i class="icon_01"></i><font>' + zdtzms[i].innerHTML + '</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
                                 $("#zdtzmUl").append(htmlStr);
                             }
                         }
                     }else{
                         $("#zdtzmUl").html("");
                     }
                 }
             },
             error: function (data) {
             }
         });
     }

</script>
<!--  简单搜索页3  -->
<div class="SimpleSearchPage ConditionScreening">
<div class="TaobaoSearch">
<form id="sqQlRelForm" class="selectCondition">

<div class="flow-steps-style1">
    <ul class="unstyled">
        <li class="first current-prev" >
            <label><font>1.</font>登记类型</label>
            <select  name="djlxdm" id="djlxSelect"  class="control-text " data-placeholder=" ">
                <#list djList as djlx>
                    <option value="${djlx.dm!}" >${djlx.mc!}</option>
                </#list>
            </select>
        </li>
        <li class="last current">
            <label><font>2.</font>申请类型</label>
            <select  name="sqlxdm" id="sqlxSelect"  class="control-text " data-placeholder=" ">
                <#list sqList as sqlx>
                    <option value="${sqlx.DM!}" >${sqlx.MC!}</option>
                </#list>
            </select>
        </li>
    </ul>

</div>

</form>



<div class="clearboth"></div>

<hr/>

<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div class="span2">

            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">权利类型 <span class="caret"></span></button>
                <ul class="dropdown-menu" id="qllxdm"  name="qllxdm">
                    <#list qlList as qllx>
                        <li value="${qllx.dm!}"><a href="#">${qllx.mc!}</a></li>
                    </#list>
                    <!--<li><a href="#">农用地的其他使用权/森林或林木所有权</a></li>
                  <li><a href="#">集体土地所有权</a></li>
                  <li><a href="#">国有土地所有权</a></li>
                  <li><a href="#">国有建设用地使用权</a></li> -->
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>
        <div class="span10">
            <ul id="qllxUl" class="content">
                <!--<li>
                    <i class="icon_01"></i><font>农用地的其他使用权/森林或林木所有权</font><button type="button" class="button"><i class="x-icon">×</i></button>
                </li>-->
            </ul>
        </div>
    </div>
</div>


<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">原权利类型 <span class="caret"></span></button>
                <ul id="yqllxdm" name="yqllxdm" class="dropdown-menu">
                    <#list qlList as yqllx>
                        <li value="${yqllx.dm!}"><a href="#">${yqllx.mc!}</a></li>
                    </#list>
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>

        <div class="span10">
            <ul id="yqllxdmUl" class="content">
                <!--<li>
                    <i class="icon_01"></i><font>农用地的其他使用权/森林或林木所有权</font><button type="button" class="button"><i class="x-icon">×</i></button>
                </li>
                <li>
                    <i class="icon_01"></i><font>集体土地所有权</font><button type="button" class="button"><i class="x-icon">×</i></button>
                </li>
                <li>
                    <i class="icon_01"></i><font>宅基地使用权</font><button type="button" class="button"><i class="x-icon">×</i></button>
                </li>-->
            </ul>
        </div>


    </div>
</div>



<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">定着物特征码<span class="caret"></span></button>
                <ul id="dzwtzm" name="dzwtzm" class="dropdown-menu">
                    <#list dzwtzmList as dzwtzm>
                        <li value="${dzwtzm.MC!?trim}"><a href="#">${dzwtzm.MC!?trim}</a></li>
                    </#list>
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>

        <div class="span10">
            <ul id="dzwtzmUl" class="content">

            </ul>
        </div>


    </div>
</div>


<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div id="bdcdyly" class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">来源 <span class="caret"></span></button>
                <ul  name="bdcdyly" class="dropdown-menu">
                    <li value="2"><a href="#">全部</a></li>
                    <li value="1"><a href="#">不动产业务库</a></li>
                    <li value="0"><a href="#">地籍库</a></li>
                    <li value="3"><a href="#">查封信息</a></li>
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>

        <div class="span10">
            <ul id="bdcdylyUl" class="content">

            </ul>
        </div>


    </div>
</div>



<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">宗地特征码 <span class="caret"></span></button>
                <ul id="zdtzm" class="dropdown-menu">
                    <#list zdtzmList as zdtzm><li value="${zdtzm.MC!?trim}"><a href="#">${zdtzm.MC!?trim}</a></li>
                    </#list>
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>

        <div class="span10">
            <ul id="zdtzmUl" class="content">

            </ul>
        </div>


    </div>
</div>




<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">原申请类型 <span class="caret"></span></button>
                <ul id="ysqlxdm" name="ysqlxdm" class="dropdown-menu">
                    <#list sqList as ysqlx>
                        <li value="${ysqlx.DM!}"><a href="#" value="${ysqlx.DM!}">${ysqlx.MC!}</a></li>
                    </#list>
                    </ul>
                    </div>
        </div>

        <div class="span10">
            <ul id="ysqlxdmUl" class="content">

            </ul>
        </div>


    </div>
</div>



<div id="dyfsIsZgedy" class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">
            <!-- 选择部分建议使用原有下拉框 -->
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">是否最高额抵押 <span class="caret"></span></button>
                <ul id="dyfs" name="dyfs" class="dropdown-menu">
                    <li value="2"><a href="#">是</a></li>
                    <li value="1"><a href="#">否</a></li>
                </ul>
            </div>
            <!--end 选择部分建议使用原有下拉框 -->
        </div>

        <div class="span10">
            <ul id="dyfsUl" class="content">


            </ul>
        </div>


    </div>
</div>


<hr/>

<div class="container-fluid">
    <div class="row-fluid positionVerification">
        <div  class="span2">

        </div>

        <div class="span10">
            <button id="save" class="btn01 btn01-success" type="submit">保存</button>
        </div>


    </div>
</div>

</div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>