<script type="text/javascript">
    $(function () {
        var url ="";
        if("${bdcdjUrl!}"==""){
            url = "${serverUrl!}/bdcLimit/getLimitfieldColor?taskid=${taskid!}&wiid=${wiid!}";
        }else{
            url = "${bdcdjUrl!}/bdcLimit/getLimitfieldColor?taskid=${taskid!}&wiid=${wiid!}";
        }
        getLimitfieldColor(url,"formBody");
    });
    function  reloadcolor() {
        var url ="";
        if("${bdcdjUrl!}"==""){
            url = "${serverUrl!}/bdcLimit/getLimitfieldColor?taskid=${taskid!}&wiid=${wiid!}";
        }else{
            url = "${bdcdjUrl!}/bdcLimit/getLimitfieldColor?taskid=${taskid!}&wiid=${wiid!}";
        }
        getLimitfieldColor(url,"formBody");
    }
    function getLimitfieldColor(url, form) {
        $.getJSON(
                url,
                null,
                function (result) {
                    if (result)
                        setLimitfieldColor(form, result);
                }
        );
    }
    function setLimitfieldColor(form, result) {
        var data = result.data;
        if (data != null && data!=undefined) {
            for (var i = 0; i < data.length; i++) {
                var elementId =(data[i].cptFieldName).toLowerCase();
                $("#" + form).contents().find($("[id^='"+elementId+"']")).each(function () {
                    $(this).css("background-color", result.color);
                });
            }
        }
    }

</script>