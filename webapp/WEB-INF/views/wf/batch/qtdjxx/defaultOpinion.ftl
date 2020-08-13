<script type="text/javascript">
    $(function () {
        loadDefaultValue();
    });
    //zdd 增加bdcdjUrl参数
    function loadDefaultValue() {
        var defaultConfigUrl = '${bdcdjUrl!}/defaultConfig';
        var urlVars = getUrlVars();
        var param = {
            from: urlVars.from,
            taskid: urlVars.taskid,
            proid: urlVars.proid,
            wiid: urlVars.wiid,
            rid: urlVars.rid
        };
        $.get(defaultConfigUrl, param, function (data) {
            if (data != null && data != '') {
                var opinion = data.opinion;
                var csyj = document.getElementById("csyj");
                if (csyj != undefined && csyj != null && csyj != "") {
                    var csyjEnabled = csyj.readOnly;
                    var csyjValue = csyj.defaultValue;
                    if (!csyjEnabled && csyjValue == '') {
                        $("#csyj").val(opinion);
                    }
                }
                var fsyj = document.getElementById("fsyj");
                if (fsyj != undefined && fsyj != null && fsyj != "") {
                    var fsyjEnabled = fsyj.readOnly;
                    var fsyjValue = fsyj.defaultValue;
                    if (!fsyjEnabled && fsyjValue == '') {
                        $("#fsyj").val(opinion);
                    }
                }
                if (hdyj != undefined && hdyj != null && hdyj != "") {
                    var hdyj = document.getElementById("hdyj");
                    var hdyjEnabled = hdyj.readOnly;
                    var hdyjValue = hdyj.defaultValue;
                    if (!hdyjEnabled && hdyjValue == '') {
                        $("#hdyj").val(opinion);
                    }
                }
            }
        });
    }
    //获取参数
    function getUrlVars() {
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for (var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    }
</script>