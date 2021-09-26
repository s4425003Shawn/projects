



<div id="searching"class="d-flex align-items-center justify-content-center">
	<form  action="<?php echo base_url(); ?>Searching" method="POST">
	<div class="input-group mb-3">
		
		<input type="text" name = 'searching' id = 'title' placeholder="I'm looking for..." value = "<?php 
		if(isset($keyword)){
			
			echo $keyword;} ?>"class="form-control" aria-label="Text input with dropdown button">

		<div class="input-group-append">
			<button class="btn btn-success" type="submit" id="button-addon2">Search</button>
		</div>
	</div>
	</form>
</div>





	<div id = 'postList' class="container mx-auto" style="width: 90%;" >

		<div class="row">
			<div class="col">
				<h3>Top view</h2>
			</div>

		</div>

		<?php if(!empty($posts)){ foreach($posts as $post){ ?>
			<div class='card m-3 mx-auto' style='width: 28%;'>
				<a id = 'ppp' href='<?php echo base_url() ?>/detail/good/<?php echo $post['id']?> '>
				<img src='<?php echo $post['image_path']?>' style='height: 12rem' class='card-img-top'></a>
				<div class='card-body'>
					<h5 class='card-title'><?php echo $post['title']?></h5>
					<p class='card-text'><?php echo $post['description']?></p>
				</div>
        	</div>
    	<?php } ?>
        	<div class="load-more" lastID="<?php echo $post['id']; ?>" style="display: none;">
            	Loading more posts...
        	</div>
    	<?php }else{ ?>
        	<p>Post(s) not available.</p>
    	<?php } ?>
		



		
		
		
	</div>

		
		
		






