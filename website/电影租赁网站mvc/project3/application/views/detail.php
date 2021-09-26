<div class='card m-3 mx-auto' style='width: 50%; '>
    <img src='<?php echo base_url(); echo $good->image_path;?> ' >
    <div class='card-body'>
        <h5 class='card-title'><?php echo $good->title?></h5>
        <p class='card-text'><?php echo $good->description?></p>
        <p class='card-text'>$<?php echo $good->price?></p>
        <a href="<?php 
        if(isset($_SESSION['email']) || isset($_COOKIE['email'])){
            echo base_url('detail/buy/'.$good->id); 
        }else{
            echo base_url('user'); 
        }
        
        
        
        ?>">
                    <img width = 100 src="<?php echo base_url('assets/images/x-click-but01.png'); ?>" />
                </a>
    </div>
</div>