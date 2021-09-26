<nav class="navbar navbar-expand-lg navbar-light bg-light">
<?php echo '<a href="'.base_url().'home"><img src="'.base_url().'images/Original.png" width="50" height="50" alt=""></a>'?>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarNav">
		<ul class="navbar-nav">
			<li class="nav-item active">
				<a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#">Furniture</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#">Electronic</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#">Motor</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#">Other</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<?php echo base_url() ?>post">Post</a>
			</li>

		</ul>
	</div>

	<div class="btn-group" role="group" aria-label="Basic example">
		<?php
		if (isset($profile)) {

			if (strlen($profile['img_path']) > 1) {
				echo '<a href="' . base_url() . 'profile"><img id ="head" src="' .base_url(). $profile['img_path'] . '"/></a>';
				echo '<a class="btn btn-outline-primary" href="' . base_url() . 'user/logout">Sign out</a>';
			} else {
				echo '<a href="' . base_url() . 'profile"><img id = "head" src="images/head.png" class="img-circle" alt="..."></a>';
				echo '<a class="btn btn-outline-primary" href="' . base_url() . 'user/logout">Sign out</a>';
			}
		} else {

			echo '<a class="btn btn-outline-primary" href="' . base_url() . 'user">Sign in</a>';
			echo '<a class="btn btn-outline-primary" href="' . base_url() . 'register">Sign up</a>';
		}


		?>



	</div>


</nav>