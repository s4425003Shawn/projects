
<div id="myDiv" class="container mx-auto my-5 p-4  bg-light">
		<h2>Profile</h2>
		<?php
		// Check in DB

	
			
			echo "Email address: \r {$profile["email"]}";
			echo "</br>";
			echo "Username :  {$user["username"]}";
			
	
		
		



		?>

		<form class="form-inline" id = 'up'  method="POST">

			<div class="form-group mr-3 mb-2">
				<label for="inputPassword2" class="sr-only">Password</label>
                <?php 
                
					echo '<input type="phone" class="form-control" name = "phone" placeholder="Phone number" value=' . $profile['phone'] . '>';
				
					
				
				?>
			</div>
			<button type="submit" class="btn btn-primary mb-2">Update</button>

		</form>





		<!-- upload file here            -->
		<form id="uploadimage"  method="post" enctype="multipart/form-data">

			<div id="selectImage">
				<label>Select Your head portrait</label><br />
				<input type="file" name="file" id="file" required />
				<input type="submit" value="Upload" class="submit" />
			</div>


		</form>
		

	</div>