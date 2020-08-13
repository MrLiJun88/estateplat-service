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
        background-image: url(${platformUrl!}/pf/images/signsave.gif);
        BORDER-BOTTOM: 1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT: 1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY: 宋体;
        FONT-SIZE: 9pt;
        HEIGHT: 28px;
        width: 59px;
        cursor: hand;
    }

    .Button_Del {
        background-image: url(${platformUrl!}/pf/images/signdel.gif);
        BORDER-BOTTOM: 1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT: 1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY: 宋体;
        FONT-SIZE: 9pt;
        HEIGHT: 28px;
        width: 59px;
        cursor: hand;
    }

    .Button_Auto {
        background-image: url(${platformUrl!}/pf/images/signauto.gif);
        BORDER-BOTTOM: 1px solid #ffffff;
        BORDER-LEFT: 1px solid #ffffff;
        BORDER-RIGHT: 1px solid #ffffff;
        BORDER-TOP: 1px solid #ffffff;
        FONT-FAMILY: 宋体;
        FONT-SIZE: 9pt;
        HEIGHT: 28px;
        width: 85px;
        cursor: hand;
    }

    .TextArea {
        BACKGROUND-COLOR: #FFFFFF;
        COLOR: #000000;
        font-size: 20px;
        TEXT-DECORATION: None;
        font-family: '楷体';
        TEXT-ALIGN: Left;
        BORDER-TOP: 1 Groove #FFFFFF;
        BORDER-LEFT: 1 Groove #FFFFFF;
        BORDER-RIGHT: 1 Groove #BBBBBB;
        BORDER-BOTTOM: 1 Groove #BBBBBB;
        overflow: auto;

        /*border: 1pt solid #6699CC;
        text-indent: 1em;*/
        white-space: normal;
        display: table-cell;
        borderstyle: solid;
        width: 100%;
        height: 110px;
    }

    .signdiv {
        border: #CCC;
        border-width: thin;
        border-style: solid;
        -moz-user-select: none;
        hutia: expression(this.onselectstart=function(){return(false)});
    }

    .bg {
        background: #FFF url(${platformUrl!}/pf/images/signback.gif) no-repeat fixed 0 -160px;
    }

</style>
<script type="text/javascript">
    var _image = false;
    var _signoption = false;
    var _mousedown = false;
    var _lines = "";
    var _fx = 0;
    var _fy = 0;
    var _postline = "";
    var objPad;
    $(document).ready(function () {
        var signId = $("#signVo_signId").val();
        if ($("#hwPenSign") != null) {
            objPad = $("#hwPenSign")[0];
            if (objPad != null) {
                if (signId != '' && signId != 'null') {
                    $('#signImg').removeAttr("style");
                    $('#hwPenSign').attr("style", "display:none");
                }
                try {
                    objPad.HWSetBkColor(0xE0F8E0);
                    objPad.HWSetCtlFrame(2, 0x000000);
                    objPad.HWInitialize();
                } catch (error) {
                    alert('要使用手写板签名，请先安装手写板控件！');
                }

            }
        }
        $("#signOpinion").bind("propertychange", function () {
            _signoption = true;
        });

        $("#btndel").click(function () {
            var params = {
                'signId': $("#signVo_signId").val(),
                'signVo.proId':$("#signVo_proId").val(),
                'signVo.signKey':$("#signVo_signKey").val()
            };
            $.post(
                    "${bdcdjUrl!}/sign/delSignEsp",
                    params,
                    function (data) {
                        if (data.msg!="error"){
                            window.returnValue = "error";
                            window.close();
                        }
                    },
                    "json"
            );
        });
        $("#btnsave").click(function () {
            var image = objPad.HWGetBase64Stream(1);
            var params = {
                'signVo.signId': $("#signVo_signId").val(),
                'signVo.proId': $("#signVo_proId").val(),
                'signVo.signKey': $("#signVo_signKey").val(),
                'signVo.signType': '1',
                'signVo.userId': $("#userId").val(),
                'base64Image': image
            };
            $.ajax({
                type: "POST",
                url: "${bdcdjUrl!}/sign/saveSignEsp",
                dataType: 'json',
                data: params,
                async: false,
                success: function (data) {
                    if (data.msg!="error"){
                        saveStubPdf();
                        window.returnValue = "error";
                        opener.location="javascript:reload()";
                        window.close();
                    }
                },
                error:function(msg){
                    alert(msg);
                }
            });
        });

        if ($("#lyrsign img").length > 0) _image = true;
        $("#lyrsign").mousedown(function (event) {
            if (!_image) {
                var xo = event.clientX;
                var yo = event.clientY;
                _mousedown = true;
                _lines = "";
            }
        }).mousemove(function () {
            if (!_image) {
                if (_mousedown) {
                    var xo = event.clientX;
                    var yo = event.clientY;
                    xo = xo - $(this).position().left;
                    yo = yo - $(this).position().top;
                    if (Math.abs(_fx - xo) > 1 || Math.abs(_fy - yo) > 1) {
                        _lines = _lines + " " + xo + "," + yo;
                        $("#lyrsigntemp").html("<v:PolyLine Points='" + _lines + "'  strokeweight=5pt StrokeColor=black ><v:fill opacity='0' /></v:polyline>");
                        _fx = xo;
                        _fy = yo;
                    }
                }
            }
        }).mouseup(function () {
            if (!_image) {
                _mousedown = false;
                _postline = _postline + _lines + "|";
                _lines = "";
                $(this).append($("#lyrsigntemp").html());
                $("#lyrsigntemp").html("");
            }
        });
    });

    //生成pdf
    function saveStubPdf() {
        var proid = $("#signVo_proId").val();
        $.ajax({
            url: "${etlUrl!}/stubPdf/uploadStubPdf",
            type: 'POST',
            dataType: 'json',
            data: {proid:proid},
            success: function (data) {
            },
            error: function (data) {
            }
        });
    }
</script>
<div id="lyrsign" class="signdiv" style="position:absolute;left:40px;top:50px;width:400px;height:160px;z-index:0">
    <img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${signVo.signId!}" width="400" height="160"
         style="display: none" id="signImg"/>
    <object id="hwPenSign"
            name="hwPenSign"
            classid="clsid:E8F5278C-0C72-4561-8F7E-CCBC3E48C2E3"
            width="400"
            height="160">
    </object>
</div>

<div style="position:absolute; left:40px; top:250px; width:400px; height:80px;">
    <table width="298" align="center">
        <tr>
            <td width="93" align="center">
                <input type="button" id="btndel" class="Button_Del"/>
            </td>
            <td width="92" align="center">
                <input type="button" id="btnsave" class="Button_Save"/>
            </td>
        </tr>
    </table>
</div>
<input type="hidden" name="signVo.proId" id="signVo_proId" value="${signVo.proId!}"/>
<input type="hidden" name="signVo.signKey" id="signVo_signKey" value="${signVo.signKey!}"/>
<input type="hidden" name="signVo.signId" id="signVo_signId" value="${signVo.signId!}"/>
<input type="hidden" name="signVo.userId" id="signVo_userId" value="${signVo.userId!}"/>
<input type="hidden" name="opinionType" id="opinionType" value="${opinionType!}"/>
<input type="hidden" name="taskid" id="taskid" value="${taskid!}"/>
<input type="hidden" name="userId" id="userId" value="${userId!}"/>
<input type="hidden" name="proids" id="proids" value="${proids!}"/>
<input type="hidden" name="splitStr" id="splitStr" value="${splitStr!}"/>
<div class="vs-context-menu">
    <ul>

    </ul>
</div>
</@com.html>