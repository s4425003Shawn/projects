<div class="container-fluid bg-light">
    <div class="row">
        <div class="col-sm-1">

        </div>
        <div class="col">
            <h4>Tips&help</h4>
            <p>FAQs</p>
            <p>Technical support</p>
            <p>About us</p>
            <p>Contact us</p>
        </div>
        <div class="col">

            <h4>Our business </h4>
            <p>History</p>
            <p>Concept</p>
            <p>Blogs</p>


        </div>
        <div class="col-sm">

        </div>
    </div>
</div>




<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->


<script src="<?php echo base_url(); ?>js/jquery-3.3.1.min.js"></script>
<script src="<?php echo base_url(); ?>js/bootstrap.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="<?php echo base_url(); ?>js/script.js"></script>
<script src="<?php echo base_url(); ?>js/jquery-ui.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $(window).scroll(function(){
        var lastID = $('.load-more').attr('lastID');
        if(($(window).scrollTop() == $(document).height() - $(window).height()) && (lastID != 0)){
            $.ajax({
                type:'POST',
                url:'<?php echo base_url('home/loadMoreData'); ?>',
                data:'id='+lastID,
                beforeSend:function(){
                    $('.load-more').show();
                },
                success:function(html){
                    $('.load-more').remove();
                    $('#postList').append(html);
                }
            });
        }
    });

        $('#up').submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: '<?php echo base_url(); ?>profile/update',
                type: "post",
                data: new FormData(this),
                processData: false,
                contentType: false,
                cache: false,
                async: false,
                success: function(data) {

                }
            });
        });
        $('#uploadimage').submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: '<?php echo base_url(); ?>profile/update_portrait',
                type: "post",
                data: new FormData(this),
                processData: false,
                contentType: false,
                cache: false,
                async: false,
                success: function(data) {
                    document.getElementById("head").src = data;
                }
            });
        });
        $('#uploadgood').submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: '<?php echo base_url(); ?>post/upload_detail',
                type: "post",
                data: new FormData(this),
                processData: false,
                contentType: false,
                cache: false,
                async: false,
                success: function(data) {
                    alert("Successful upload good")
                }
            });
        });
        $("#title").autocomplete({
            source: "<?php echo base_url(); ?>Home/get_autocomplete"
        });

    });
</script>
</body>

</html>