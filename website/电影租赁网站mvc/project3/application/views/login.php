

<nav class="navbar navbar-light bg-light">
        <a class="navbar-brand" href="#">
            <img src="images/Original.png" width="50" height="50" alt="">
        </a>

        
            <a href="<?php echo base_url(); ?>home"<button  class="btn btn-outline-success my-2 my-sm-0">Home Page</button></a>
       

    </nav>

	

    <div id="signin_form" class="container bg-light w-50 mt-5 mb-5">
		<h3 class="mx-auto" style = "width: 100px;">Sign in</h3>
        <?php 
            
            

               
        
                // Check in DB
               
               
                if(strlen($message)>0){
                    echo '<div class="p-1 rounded-lg mb-2 bg-danger text-white">Eamil or Password is not correct</div>';
                }
                
                
                
        
            
            
            ?>
	<form class="needs-validation" action="<?php echo base_url(); ?>user/sign_in" method="POST" novalidate>
		
		<div class="form-group">
			<label for="email">Email address</label>
			<input type="email" class="form-control"  value = "<?php if (isset($_COOKIE["keep"])): echo $_COOKIE["keep"]; endif ?>" name = "email" id="email" placeholder="Email address"  required>
			<div class="invalid-feedback">
			  You need provide a valid email address.
			</div>
		  </div>
		 
		
          <div class="form-group">
			 
            <label for="password">Password</label>
            <input type=password class="form-control"  name = "password" id="validationCustom03" placeholder="Password" required >
            <div class="invalid-feedback">
                    You need provide your password.
            </div>
          </div>
          <div class="form-group form-check">
          <input type="checkbox" id="remember" name="remember" 
                        <?php if (isset($_COOKIE["keep"])): echo "checked"; endif ?>
                    class="mb-4">
            <label class="form-check-label" for="exampleCheck1">Remember me</label>
          </div>
          <p class="check_register">Don't have an account?</p>
          <button type="button" onclick="window.location.href='<?php echo base_url(); ?>register'" class = "register btn btn-link pb-2 pl-1">Register now</button>
		 
		  
	
		<button class="btn btn-primary float-right mt-4" type="submit">Sign in</button>
		
	  </form>
		
	</div>