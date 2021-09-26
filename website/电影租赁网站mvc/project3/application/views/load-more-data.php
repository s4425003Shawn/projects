








	<div id = 'postList' class="container mx-auto" style="width: 90%;" >

		

        <?php if(!empty($posts)){ foreach($posts as $post){ ?>
        <div class="list-item">
            <div class='card m-3 mx-auto' style='width: 28%;'>
				<a id = 'ppp' href='<?php echo base_url() ?>/detail/good/<?php echo $post['id']?> '>
				<img src='<?php echo $post['image_path']?>' style='height: 12rem' class='card-img-top'></a>
				<div class='card-body'>
					<h5 class='card-title'><?php echo $post['title']?></h5>
					<p class='card-text'><?php echo $post['description']?></p>
				</div>
        	</div>
        </div>
    <?php } ?>
    <?php if($postNum > $postLimit){ ?>
        <div class="load-more" lastID="<?php echo $post['id']; ?>" style="display: none;">
           Loading more posts...
        </div>
    <?php }else{ ?>
        <div class="load-more" lastID="0">
            That's All!
        </div>
    <?php } ?>    
<?php }else{ ?>    
    <div class="load-more" lastID="0">
            That's All!
    </div>    
<?php } ?>
		



		
		
		
	</div>

		
		
		

