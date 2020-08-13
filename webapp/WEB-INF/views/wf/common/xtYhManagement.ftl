<script type="text/javascript">
    $(function () {
        var proid=$("#proid").val();
        var qllx=$("#qllx").val();
        if(qllx==18){
            $("#formBody").contents().find($("input[name='qlrmc']")).each(function () {
                $(this).css('display','none');
            });
            $("#formBody").contents().find($("select[name='qlrmc']")).each(function () {
                $(this).css('display','block');
            });
        }
        qlrselectchange();
    });
    function  xtyh() {
        var qllx=$("#qllx").val();
        if(qllx==18){
            $("#sjdForm").contents().find($("input[name='qlrmc']")).each(function () {
                $(this).css('display','none');
            });
            $("#sjdForm").contents().find($("select[name='qlrmc']")).each(function () {
                $(this).css('display','block');
            });
        }
        qlrselectchange();
    }
    function  qlrselectchange() {
        $("select[name='qlrmc']").change(function () {
            var index = $(this).attr('id');
            var qlrmc=$(this).val();
            var id = getID(index);
            $.ajax({
                url: "${bdcdjUrl}/bdcdjSjdxx/getxtYh?qlrmc=" + qlrmc + "&proid=" + proid,
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    if (data != null && data != "") {
                        $("#qlr_qlrsfzjzl_" + id).val(data.qlrsfzjzl);
                        $("#qlr_qlrzjh_" + id).val(data.qlrzjh);
                    }
                },
            });
        });
    }
    function getID(name) {
        var y = name.lastIndexOf("_");
        var id = name.substr(y + 1);
        return id;
    }
</script>