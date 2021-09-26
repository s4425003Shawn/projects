<!DOCTYPE html>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<?php echo base_url(); ?>css/bootstrap.css">

    <link rel="stylesheet" href="<?php echo base_url(); ?>css/style.css">
    <link href='<?= base_url() ?>resources/dropzone.css' type='text/css' rel='stylesheet'>
    <script src='<?= base_url() ?>resources/dropzone.js' type='text/javascript'></script>
    <title>GLB</title>
    <style>
    .content{
      width: 50%;
      padding: 5px;
      margin: 0 auto;
    }
    .content span{
      width: 250px;
    }
    .dz-message{
      text-align: center;
      font-size: 28px;
    }
    </style>
    <script>
    // Add restrictions
    Dropzone.options.fileupload = {
      acceptedFiles: 'image/*',
      maxFilesize: 1 // MB
    };
    </script>
</head>

<body>


<nav class="navbar navbar-expand-lg navbar-light bg-light">


    <?php echo '<a href="' . base_url() . 'user"><img src="images/Original.png" width="50" height="50" alt=""></a>' ?>

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
                <a class="nav-link" href="<?php echo base_url()?>post">Post</a>
            </li>

        </ul>
    </div>

    <div id="head_portrait" class="btn-group" role="group" aria-label="Basic example">

        <?php


        if (strlen($profile['img_path']) > 1) {
            echo '<a href="'.base_url().'profile"><img id ="head" src="' . $profile['img_path'] . '"/></a>';
        } else {
            echo '<a href="'.base_url().'profile"><img id = "head" src="images/head.png" class="img-circle" alt="..."></a>';
        }





        echo '<a class="btn btn-outline-primary" href="' . base_url() . 'user/logout">Sign out</a>';
        ?>







    </div>


</nav>
<div id="signup_form" class="container bg-light w-50 mt-5 mb-5">
    <h3 class="mx-auto" style="width: 100px;">Post</h3>
    <form action="<?= base_url('Post/fileupload') ?>" class="dropzone" id="fileupload">
    <div class="form-group">
            <label for="exampleInputEmail1">Title</label>
            <input type="text" name = "title" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Title">
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">Price</label>
            <input type="text" name = "price" class="form-control" id="exampleInputPassword1" placeholder="$">
        </div>
        <div class="form-group">
            <label for="exampleFormControlTextarea1">Description</label>
            <textarea class="form-control" name = "description"id="exampleFormControlTextarea1" rows="3"></textarea>
        </div>
        
        


      </form> 
    
    

</div>
