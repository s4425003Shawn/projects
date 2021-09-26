<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Post extends CI_Controller
{

    public function __construct()
    {

        parent::__construct();
        
        $this->load->model('user_model');
        
        
    }

    public function index()
    {

        // load view
        if (isset($_SESSION["email"])) {
            $email = $_SESSION["email"];
            $data['profile'] = $this->user_model->get_profile($email);
            
            $this->load->view('post', $data);
            $this->load->view('footer');
        } else {
            if (isset($_COOKIE['email'])) {
                $email = $_COOKIE["email"];
            $data['profile'] = $this->user_model->get_profile($email);
            
            $this->load->view('post', $data);
            $this->load->view('footer');
            } else {
                redirect(base_url(). "User");
            }
        }
    }

    // File upload
    public function fileUpload()
    {
        if(isset($_SESSION['email'])){
            $email = $_SESSION["email"];
        }elseif(isset($_COOKIE['email'])){
            $email = $_COOKIE["email"];
        }
        if (!empty($_FILES['file']['name'])) {

            // Set preference
            $config['upload_path'] = 'uploads/';
            $config['allowed_types'] = 'jpg|jpeg|png|gif';
            $config['max_size'] = '1024'; // max_size in kb
            $config['file_name'] = $_FILES['file']['name'];

            //Load upload library
            $this->load->library('upload', $config);

            // File upload
            if ($this->upload->do_upload('file')) {
                // Get data about the file
                $uploadData = $this->upload->data();
                $data['path']= 'uploads/'.$uploadData['file_name'];
                
                
                $title = $this->input->post('title');
                $price = $this->input->post('price');
                $description = $this->input->post('description');
                $this->user_model->update_good_detail($email, $title, $price, $description ,$data['path']);
            }
        }
    }
    public function upload_detail()
    {
        if(isset($_SESSION['email'])){
            $email = $_SESSION["email"];
        }elseif(isset($_COOKIE['email'])){
            $email = $_COOKIE["email"];
        }
        $title = $this->input->post('title');
        $price = $this->input->post('price');
        $description = $this->input->post('description');
        $this->user_model->update_good_detail($email, $title, $price, $description );
    }

}
