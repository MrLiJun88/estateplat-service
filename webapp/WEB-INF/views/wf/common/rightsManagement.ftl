<script type="text/javascript">
    var _refresh = false;
    $(function(){
        var authorUrl='${path_platform!}/author.action?from=${from!}&taskid=${taskid!}&rid=${rid!}';
        authorPage(authorUrl,"${from!}","formBody")
    });
    function  reloadrights() {
        var authorUrl='${path_platform!}/author.action?from=${from!}&taskid=${taskid!}&rid=${rid!}';
        authorPage(authorUrl,"${from!}","formBody")
    }
    function authorPage(authorUrl,from,form) {
        if (from == null || from == "" || from == undefined) {
            readOnlyAllControl(form);
        } else if (from == 'task') {
            $.getJSON(
                    authorUrl,
                    null,
                    function (data) {
                        if (data) {
                            //按照名称控制权限
                            distableControlsName(form, data);
                            //按照id控制权限
                            distableControlsId(form, data);
                            //控制butoon和超链接a的权限
                            distableControlsType(form, data);
                        }
                    }
            );
        } else {
            $.getJSON(
                    authorUrl,
                    null,
                    function (data) {
                        if (data){
                            //按照名称控制权限
                            enableControlsName(form,data);
                            //按照id控制权限
                            enableControlsTd(form,data);
                            //控制butoon和超链接a的权限
                            enableControlsType(form, data);
                        }

                    }
            );
        }
    }
    function author(frameObj) {
        if (_refresh) {
            authorPage();
        }
        _refresh = true;
    }
    function disableAllControl(form){
        $("#" + form).contents().find("input").attr('readonly','true');
        $("#" + form).contents().find("select").attr('disabled','true');
        $("#" + form).contents().find("textarea").attr('readonly','true');
    }
    function readOnlyAllControl(form){
        $("#" + form).contents().find("input").each(function(){
            $(this).removeAttr("onfocus");
            var type=$(this)[0].type;
            $(this).attr('readonly','true');
        });
        $("#" + form).contents().find("select").attr('disabled','true');
        $("#" + form).contents().find("button").attr('disabled','true');
        $("#" + form).contents().find("a").removeAttr('onclick');
        $("#" + form).contents().find("textarea").attr('readonly','true');
        $("#" + form).contents().find("img[dojoAttachEvent*='onclick']").each(function(){
            $(this).attr('disabled','true');
        });
        $("#" + form).contents().find("input[type='button']").attr('disabled','true');
        $("#" + form).contents().find("input[type='radio']").attr('disabled','true');
        //$("#frameMain").contents().find("div").attr('disabled','true');
        $("#" + form).contents().find("div").removeAttr("onmouseover");
        $("#" + form).contents().find("div").removeAttr("onmouseout");
        $("#" + form).contents().find("div").removeAttr("onclick");
    }
    function distableControlsName(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($("[name='"+elementId+"']")).each(function(){
                var type=$(this)[0].type;
                var tagName=$(this)[0].tagName;
                var className=$(this)[0].className;
                //struts datecontrol
                if (jQuery.nodeName(this,"span"))
                {
                    $(this).children().each(function(){
                        if($(this).attr('dojoAttachEvent') == 'onclick:onIconClick'){
                            $(this).attr('disabled','true');
                        }else {
                            $(this).attr('readonly','true');
                            $(this).removeAttr("onfocus");
                        }
                    });
                }else{
                    if (tagName=="INPUT" && (type=="text" || type =="password") || type=="textarea"){
                        $(this).attr('readonly','true');
                        $(this).removeAttr("onfocus");
                        if(className!=null&&className!=''&&className.indexOf("date-picker")>-1){
                            $(this).attr('type','');
                            $(this).attr('disabled','true');
                        }
                    }else if(tagName == "SELECT"){
                        $(this).parent().append('<input type="hidden" name="' + $(this).attr("name") + '" id="' + $(this).attr("id") + '" value="' + $(this).attr("value") + '"/>');
                        $(this).attr('disabled','true');
                    }else{
                        $(this).attr('disabled','true');
                    }
                }
            });
        }
    }

    function enableControlsName(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($("[name='"+elementId+"']")).removeAttr('disabled');
            $("#" + form).contents().find($("[name='"+elementId+"']")).removeAttr("readonly");
        }
    }
    function enableControlsTd(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($("[id^='"+elementId+"']")).removeAttr('disabled');
            $("#" + form).contents().find($("[id^='"+elementId+"']")).removeAttr("readonly");
        }
    }

    function distableControlsId(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($("[id^='"+elementId+"']")).each(function(){
                var type=$(this)[0].type;
                var tagName=$(this)[0].tagName;
                var className=$(this)[0].className;
                //struts datecontrol
                if (jQuery.nodeName(this,"span"))
                {
                    $(this).children().each(function(){
                        if($(this).attr('dojoAttachEvent') == 'onclick:onIconClick'){
                            $(this).attr('disabled','true');
                        }else {
                            $(this).attr('readonly','true');
                            $(this).removeAttr("onfocus");
                        }
                    });
                }else{
                    if (tagName=="INPUT" && (type=="text" || type =="password") || type=="textarea"){
                        $(this).attr('readonly','true');
                        $(this).removeAttr("onfocus");
                        if(className!=null&&className!=''&&className.indexOf("date-picker")>-1){
                            $(this).attr('type','');
                            $(this).attr('disabled','true');
                        }
                    }else if(tagName == "SELECT"){
                        $(this).parent().append('<input type="hidden" name="' + $(this).attr("name") + '" id="' + $(this).attr("id") + '" value="' + $(this).attr("value") + '"/>');
                        $(this).attr('disabled','true');
                    }else{
                        $(this).attr('disabled','true');
                    }
                }
            });
        }
    }

    function enableControlsType(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($(":button")).each(function () {
                var textvalue=$(this).text();
                if(textvalue!=null&&textvalue!=""&&textvalue!=undefined&&elementId==textvalue){
                    $(this).attr('disabled','false');
                }
            });
            $("#" + form).contents().find($("a")).each(function () {
                var textvalue=$(this).text();
                console.log(textvalue);
                if(textvalue!=null&&textvalue!=""&&textvalue!=undefined&&elementId==textvalue){
                    $(this).attr("onclick");
                }
            });
        }
    }
    function distableControlsType(form,data){
        for(var i=0;i<data.length;i++){
            var elementId=data[i].elementId;
            $("#" + form).contents().find($(":button")).each(function () {
                var textvalue=$(this).text();
                if(textvalue!=null&&textvalue!=""&&textvalue!=undefined&&elementId==textvalue){
                    $(this).attr('disabled','true');
                }
            });
            $("#" + form).contents().find($("a")).each(function () {
                var textvalue=$(this).attr("id");
                if(textvalue!=null&&textvalue!=""&&textvalue!=undefined&&elementId==textvalue){
                    $(this).removeAttr("onclick");
                }
            });
        }
    }
</script>