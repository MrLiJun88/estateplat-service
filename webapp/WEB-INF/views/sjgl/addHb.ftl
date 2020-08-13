<@com.html title="申请类型和权利类型关系表配置" import="sqlxQllx">
<style>
    .btn-group > .dropdown-menu {
        overflow: auto;
        max-height: 150px;
    }
    #sty{
        margin-top: 20px;
        margin-left: 70px;
        margin-bottom: 20px;
        display:block;
        width:80%;
        text-align: center;
    }
    #sty1{
        margin-top: 40px;
        margin-left: 70px;
        display:block;
        width:80%;
        text-align: center;
    }
</style>

<script>
    $(function(){
        $("#djlxSelect").change(function(){
            getSqlxByDjlxAndBdclx();
        });
        //默认值
        $("#djlxSelect").val('${djlx}');
        $("#djlxSelect").change();

        $("ul.content li button").live("click",function(){
            $(this).parent().remove();
        });

        $("#addXm").click(function(){
            var sqlx = document.getElementById("sqlxSelect");
            var htmlStr = '<li value="'+$("#sqlxSelect").val()+'"><i class="icon_01"></i><font>'+sqlx.options[sqlx.selectedIndex].text+'</font><button type="button" class="button"><i class="x-icon">×</i></button></li>';
            var val = $("#sqlxSelect").val();
            var bol = false;
            $("#sqlxUl li").each(function(index){
                var liValue = $(this).val();
                if(val == liValue){
                    bol = true;
                }
            });
            if(!bol) {
                $("#sqlxUl").append(htmlStr);
            }
        })
        //保存事件

        $("#save").click(function(){
            var sqlxback = "";
            $("#sqlxUl li").each(function(index){
                var liValue = $(this).val();
                alert(liValue);
                sqlxback = sqlxback + liValue + " ";
            });
            $("#sqlxback").val(sqlxback);
            alert($("#sqlxback").val());
        })
    })
    function getParamStr(){
        var paramStr = "";
        paramStr += $("#sqQlRelForm").serialize();
        var yqllxdm  = getDm("#yqllxdmUl li");
        paramStr += "&yqllxdm=" + yqllxdm;
        return paramStr;
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
                if(result !=null && result!=''){
                    $.each(result,function(index,data){
                        $("#sqlxSelect").append('<option value="'+data.dm+'" >'+data.mc+'</option>');
                    })
                }
                $("#sqlxSelect").change();
            },
            error: function (data) {
            }
        });
    }


</script>
<!--选择-->
<div class="SimpleSearchPage ConditionScreening">
    <div class="TaobaoSearch">
        <form id="sqQlRelForm" class="selectCondition">

            <div class="flow-steps-style1">
                <ul class="unstyled">
                    <li class="first current-prev" >
                        <label><font></font>登记类型</label>
                        <select  name="djlxdm" id="djlxSelect"  class="control-text " data-placeholder=" ">
                            <#list djList as djlx>
                                <option value="${djlx.dm!}" >${djlx.mc!}</option>
                            </#list>
                        </select>
                    </li>
                    <li class="last current">
                        <label><font></font>申请类型</label>
                        <select  name="sqlxdm" id="sqlxSelect"  class="control-text " data-placeholder=" ">
                            <#list sqList as sqlx>
                                <option value="${sqlx.dm!}">${sqlx.mc!}</option>
                            </#list>
                        </select>
                    </li>
                </ul>
                <ul>
                    <button type="button"  id="addXm" class="btn btn-sm btn-primary" style="height: 40px">添加</button>
                </ul>
            </div>

        </form>
        
        <div class="clearboth"></div>

        <hr/>

        <div class="container-fluid">
            <div class="row-fluid positionVerification">
                <div class="span10" id="sty">
                    <ul id="sqlxUl" class="content">

                    </ul>
                </div>
            </div>
        </div>
        <hr/>
        <div class="container-fluid">
            <div class="row-fluid positionVerification">

                <div class="span10" id="sty1">
                    <button id="save" class="btn btn-sm btn-primary" style="height: 40px" >保存</button>
                </div>

            </div>
        </div>
        <form id="form" hidden="hidden">
            <input type="hidden" id="sqlxback" name="sqlxback">
        </form>

    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>