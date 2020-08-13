<@com.html title="签名页面" import="jquery">

    <@com.style name="static/css/vscontext.css"></@com.style>
    <@com.script name="static/js/vscontext.jquery.js"></@com.script>

<style>
    body {
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;

    }
    .Button_Save {
        background-image:url(${platformUrl!}/pf/images/signsave.gif);
        BORDER-BOTTOM:1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT:1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY:宋体;
        FONT-SIZE:9pt;
        HEIGHT: 28px;
        width: 59px;
        cursor: hand;
    }
    .Button_Del {
        background-image: url(${platformUrl!}/pf/images/signdel.gif);
        BORDER-BOTTOM:1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT:1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY:宋体;
        FONT-SIZE:9pt;
        HEIGHT: 28px;
        width: 59px;
        cursor: hand;
    }
    .Button_Auto {
        background-image:url(${platformUrl!}/pf/images/signauto.gif);
        BORDER-BOTTOM:1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT:1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY:宋体;
        FONT-SIZE:9pt;
        HEIGHT: 28px;
        width: 85px;
        cursor: hand;
    }
    .TextArea{
        BACKGROUND-COLOR:#FFFFFF;
        COLOR:#000000;
        font-size:20px;
        TEXT-DECORATION:None;
        font-family:'楷体';
        TEXT-ALIGN:Left;
        BORDER-TOP:1 Groove #FFFFFF;
        BORDER-LEFT:1 Groove #FFFFFF;
        BORDER-RIGHT:1 Groove #BBBBBB;
        BORDER-BOTTOM:1 Groove #BBBBBB;
        overflow : auto;

        /*border: 1pt solid #6699CC;
        text-indent: 1em;*/
        white-space: normal;
        display: table-cell;
        borderstyle: solid;
        width:100%;
        height:110px;
    }
    .signdiv{
        border:#CCC;
        border-width:thin;
        border-style:solid;
        -moz-user-select:none;
        hutia:expression(this.onselectstart=function(){return(false)});
    }

</style>
<script type="text/javascript">
    var _image=false;
    var _signoption=false;
    var _mousedown=false;
    var _lines="";
    var _fx=0;
    var _fy=0;
    var _postline="";
    $(document).ready(function() {
        $("#signOpinion").bind("propertychange", function() {
            _signoption=true;
        });

        $("#btndel").click(function(){
            $("#lyrsign").html('');
            $("#lyrsigntemp").html("");
            _lines="";
            _postline="";
            _image=false;
            $("#signOpinion").val("");
            var params1 = {
                'proid':$("#signVo_proId").val(),
                'signKey':$("#signVo_signKey").val()

            };
            $("#signVo_signId").val('');
            $.get(
                    "${bdcdjUrl!}/sign/delSignIds",
                    params1,
                    function(data){
                        if ( data.msg!=null && data.msg!="" ){
                            window.returnValue="del";
                            window.close();

                        }
                    },
                    "json"
            );
        });
        $("#btnauto").click(function(){
            var params = {
                'signVo.signId':$("#signVo_signId").val(),
                'signVo.proId':$("#signVo_proId").val(),
                'signVo.signKey':$("#signVo_signKey").val(),
                'signVo.signOpinion':$("#signOpinion").val(),
//                'signVo.userId':$("#signVo_userId").val(),
                'signVo.signType':'1',
                'signVo.userId':$("#userId").val()
            };
            $.post(
                    "${bdcdjUrl!}/sign/autosign",
                    params,
                    function(data){
                        if (data.msg!=""){

                            window.returnValue=$("#signOpinion").val();
                            window.close();

                        }
                    },
                    "json"
            );
//            var proids=$("#proids").val();

        <#--if(proids!=null && proids!=""){-->
        <#--var proidArray= new Array();-->
        <#--proidArray=proids.split($("#splitStr").val());-->
        <#--for(var i=0;i<proidArray.length; i++){-->

        <#--var params = {-->
        <#--'signVo.signId':$("#signVo_signId").val(),-->
        <#--'signVo.proId':proidArray[i],-->
        <#--'signVo.signKey':$("#signVo_signKey").val(),-->
        <#--'signVo.signOpinion':$("#signOpinion").val(),-->
        <#--//                'signVo.userId':$("#signVo_userId").val(),-->
        <#--'signVo.signType':'1',-->
        <#--'signVo.userId':$("#userId").val()-->
        <#--};-->

        <#--$.post(-->
        <#--"${bdcdjUrl!}/sign/autosign",-->
        <#--params,-->
        <#--function(msg){-->

        <#--if (msg!="" ){-->

        <#--window.returnValue=$("#signOpinion").val();-->
        <#--window.close();-->

        <#--}-->
        <#--},-->
        <#--"json"-->
        <#--);-->
        <#--}-->

        <#--}else{-->
        <#--var params = {-->
        <#--'signVo.signId':$("#signVo_signId").val(),-->
        <#--'signVo.proId':$("#signVo_proId").val(),-->
        <#--'signVo.signKey':$("#signVo_signKey").val(),-->
        <#--'signVo.signOpinion':$("#signOpinion").val(),-->
        <#--//                'signVo.userId':$("#signVo_userId").val(),-->
        <#--'signVo.signType':'1',-->
        <#--'signVo.userId':$("#userId").val()-->
        <#--};-->
        <#--$.post(-->
        <#--"${platformUrl!}/tag/signtag!autosign.action",-->
        <#--params,-->
        <#--function(msg){-->
        <#--if (msg!=""){-->

        <#--window.returnValue=$("#signOpinion").val();-->
        <#--window.close();-->

        <#--}-->
        <#--},-->
        <#--"json"-->
        <#--);-->
        <#--}-->


        });
        $("#btnsave").click(function(){
            if(_image == false){
                if (_postline=="" && $("#signOpinion").val()=="" && $("#signVo_signId").val()!=""){
                    _image = false;
                    //删除
                    var params = {
                        'signVo.signId':$("#signVo_signId").val()
                    };
                    $.post(
                            "${platformUrl!}/tag/signtag!deleteSign.action",
                            params,
                            function(msg){
                                if (!msg){
                                    msg = {};
                                    msg.signKey = $("#signVo_signKey").val();
                                    msg.proId = $("#signVo_proId").val();
                                }
                                window.returnValue=msg;
                                window.close();
                            },
                            "json"
                    );
                }else{
                    var params = {
                        'signVo.signId':$("#signVo_signId").val(),
                        'signVo.proId':$("#signVo_proId").val(),
                        'signVo.signKey':$("#signVo_signKey").val(),
                        'signVo.signOpinion':$("#signOpinion").val(),
                        'signPoints':_postline
                    };
                    $.post(
                            "${platformUrl!}/tag/signtag!savesign.action" + "?t=" + (new Date()).valueOf(),
                            params,
                            function(msg){
                                if (msg!=""){

                                }
                                window.returnValue=msg;
                                window.close();
                            },
                            "json"
                    );
                }
            }else if(_signoption){
                //只保存意见
                var params = {
                    'signVo.signId':$("#signVo_signId").val(),
                    'signVo.proId':$("#signVo_proId").val(),
                    'signVo.signKey':$("#signVo_signKey").val(),
                    'signVo.signOpinion':$("#signOpinion").val()
                };
                $.post(
                        "${platformUrl!}/tag/signtag!saveSignOpinion.action" + "?t=" + (new Date()).valueOf(),
                        params,
                        function(msg){
                            if (msg!=""){

                            }
                            window.returnValue=msg;
                            window.close();
                        },
                        "json"
                );
            }else {
                window.close();
            }
        });
        if ($("#lyrsign img").length>0) _image=true;
        $("#lyrsign").mousedown(function(event){
            if (!_image){
                var xo = event.clientX;
                var yo = event.clientY;
                _mousedown=true;
                _lines="";
            }
        }).mousemove(function(){
            if (!_image){
                if(_mousedown){
                    var xo = event.clientX;
                    var yo = event.clientY;
                    xo=xo-$(this).position().left;
                    yo=yo-$(this).position().top;
                    if (Math.abs(_fx-xo)>1 || Math.abs(_fy-yo)>1){
                        _lines=_lines + " " + xo + "," +yo;
                        $("#lyrsigntemp").html("<v:PolyLine Points='" +_lines + "'  strokeweight=5pt StrokeColor=black ><v:fill opacity='0' /></v:polyline>");
                        _fx= xo;
                        _fy= yo;
                    }
                }
            }
        }).mouseup(function(){
            if (!_image){
                _mousedown=false;
                _postline=_postline+_lines+"|";
                _lines="";
                $(this).append($("#lyrsigntemp").html());
                $("#lyrsigntemp").html("");
            }
        });
        $.getJSON(
                "${bdcdjUrl!}/sign/menu",
                {'opinionType':$("#opinionType").val(),'taskid':$("#taskid").val(),'userId':$("#userId").val()},
                function(msg){
                    if (msg){
                        for(var i=0;i<msg.length;i++){
                            $(".vs-context-menu ul").append("<li class=''><a href='#' onclick='javascript:menuclick();' id=''>"+msg[i].content+"</a></li>");
                        }
                        $('#signOpinion').vscontext({menuBlock: 'vs-context-menu'});
                    }
                }
        );

    });
    function menuclick(){
        var obj = event.srcElement ? event.srcElement : event.target;
        $("#signOpinion").val($(obj).html());
    }
</script>
<div style="position:absolute; width: 400px; height: 100px; z-index: 0; left: 40px; top:50px" id="layer1">
    <textarea  name="signVo.signOpinion" id="signOpinion"  cssClass="TextArea" cols="48" rows="6">${signVo.signOpinion!}</textarea>
</div>
<div align="left" >
    <table width="475" align="center" background="${platformUrl!}/pf/images/signback.gif" border="0" cellpadding="0"
           cellspacing="1" style="border-collapse: collapse">
        <tr>
        </tr>
        <tr>
        </tr>
        <tr>
            <td height="300" align="center">　</td>
        </tr>
    </table>
</div>
<p><br></p>


<div id="lyrsigntemp" class="signdiv" style="position:absolute;left:40px;top:200px;width:400px;height:160px;z-index:0">

</div>
<div id="lyrsign" class="signdiv" style="position:absolute;left:40px;top:200px;width:400px;height:160px;z-index:0">
    <#if signVo.signId!='' >
        <img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${signVo.signId!}" width="200" height="100" />
    </#if>
</div>

<div style="position:absolute; left:40px; top:360px; width:400px; height:80px;">
    <table width="298" align="center">
        <tr>
            <td width="93" align="center">
                <input type="button" id="btndel"  class="Button_Del"/>
            </td>
            <td width="97" align="center">
                <input type="button" id="btnauto"  class="Button_Auto"/>
            </td>
        <#--<td width="92" align="center">-->
        <#--<input type="button" id="btnsave"  class="Button_Save"/>-->
        <#--</td>-->
        </tr>
    </table>
</div>
<input type="hidden" name="signVo.proId" id="signVo_proId" value="${signVo.proId!}"/>
<input type="hidden"name="signVo.signKey" id="signVo_signKey" value="${signVo.signKey!}"/>
<input type="hidden" name="signVo.signId" id="signVo_signId" value="${signVo.signId!}"/>
<input type="hidden" name="signVo.userId" id="signVo_userId" value="${signVo.userId!}"/>
<input type="hidden" name="opinionType"  id="opinionType" value="${opinionType!}"/>
<input type="hidden" name="taskid"  id="taskid" value="${taskid!}"/>
<input type="hidden" name="userId"  id="userId" value="${userId!}"/>
<input type="hidden" name="proids"  id="proids" value="${proids!}"/>
<input type="hidden" name="splitStr"  id="splitStr" value="${splitStr!}"/>
<div class="vs-context-menu">
    <ul>

    </ul>
</div>
</@com.html>