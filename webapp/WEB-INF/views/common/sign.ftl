<#macro sign signdivId="" disabled="" signKey=""  proId="" userId="" signId="" signName="">
<input id="${signdivId!}" name="${signdivId!}"  value="${signName!}" type="hidden"/>
<input id="${signdivId!}Sign" name="${signdivId!}Sign" type="hidden" value="${signId!}"/>
<div class="signatureTd " >
     <#if signId=="">
         <img src=""  id="${signdivId!}SignImg" alt="" class="signImg"  onclick="showImgDiv('${signdivId!}')" />
         <div  id="${signdivId!}Div" style="width:20px;float: right;" class="signatureTd unSign" onclick="showImgDiv('${signdivId!}')"></div>
     <#else >
         <img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${signId!}"  id="${signdivId!}SignImg" alt="" class="signImg"  onclick="showImgDiv('${signdivId!}')" />
         <div  id="${signdivId!}Div" style="width:20px;float: right;" class="signatureTd sign" onclick="removeSign('${signdivId!}')"></div>
     </#if>


<div id="${signdivId!}Div1" class="signatureContent" style="width: 160px;">

    <div class="signaturePopUp">
        <div class="popover fade bottom in" id="${signdivId!}SignDiv" style="display: none">
            <div class="arrow"></div>
            <div>

                <img  id="${signdivId!}SignDivImg"  src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${userId!}" alt="" style="width: 252px;height: 139px;" />

                <div class="signaturePrimary">
                    <button class="btn btn-mini btn-primary" type="button" onclick="sign('${signdivId!}')">确定</button>
                    <button class="btn btn-mini" type="button"  onclick="hideImgDiv('${signdivId!}')">取消</button>
                </div>
            </div>

        </div>
    </div>

</div>

</div>
<script type="text/javascript">
    function showImgDiv(id){
        $("#"+id+"SignDiv").show();
    }
    function hideImgDiv(id){
        $("#"+id+"SignDiv").hide();
    }
    function sign(id){
        var signId= $("#"+id+"Sign").val();
        $.ajax({
            type:'get',
            url:'${bdcdjUrl}/bdcSpb/sign',
            data:{proid:"${proId!}",signId:signId,userId:"${userId!}",signdivId:id},
            success:function (data) {
                if(data!=null && data.indexOf("#")>-1){
                    var signName=data.substring(0,data.indexOf("#"));
                    var signId1=data.substring(data.indexOf("#")+1,data.length);
                    $("#"+id).val(signName);
                    $("#"+id+"Sign").val(signId1);
                    if(signId1=="")
                        signId1="${userId!}";
                    $("#"+id+"SignImg").attr("src","${platformUrl!}/tag/signtag!image.action?signVo.signId="+signId1);

                    hideImgDiv(id);
                    $("#"+id+"Div").removeClass().addClass("signatureTd sign");
                }
            },
            error: function (_ex) {
            }
        });

    }
    function removeSign(id){
        var signId= $("#"+id+"Sign").val();
        $.ajax({
            type:'get',
            url:'${bdcdjUrl}/bdcSpb/removeSign',
            data:{proid:"${proId!}",signId:signId,userId:"${userId!}",signdivId:id},
            success:function (data) {
                $("#"+id).val("");
                $("#"+id+"Sign").val("");
                $("#"+id+"SignImg").attr("src","");
                hideImgDiv(id);
                $("#"+id+"Div").removeClass().addClass("signatureTd unSign");
            },
            error: function (_ex) {
            }
        });
    }
</script>
</#macro>
