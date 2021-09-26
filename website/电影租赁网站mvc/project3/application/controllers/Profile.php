<?php
class Profile extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        
        $this->load->model('user_model');
       
    }
    public function index()
    {   
        if (isset($_SESSION["email"])) {
            $email = $_SESSION["email"];
            $data['profile'] = $this->user_model->get_profile($email);
            $data ['user'] = $this->user_model->get_user($email);
            $this->load->view('header');
            $this->load->view('nav', $data);
            $this->load->view('profile' , $data);
            $this->load->view('footer');
        } else {
            if (isset($_COOKIE['email'])) {
                $email = $_COOKIE["email"];
                $data['profile'] = $this->user_model->get_profile($email);
                $data ['user'] = $this->user_model->get_user($email);
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('profile' , $data);
                $this->load->view('footer');
            } else {
                redirect(base_url(). "User");
                
            }
        }
       
    }
    public function update()
    {   
        $phone = $this->input->post('phone');
        if(isset($_SESSION['email'])){
            $email = $_SESSION["email"];
        }elseif(isset($_COOKIE['email'])){
            $email = $_COOKIE["email"];
        }
        
        $this->user_model->update_phone($email, $phone);
        
       
    }

    public function update_portrait()
    {   
        if(isset($_SESSION['email'])){
            $email = $_SESSION["email"];
        }elseif(isset($_COOKIE['email'])){
            $email = $_COOKIE["email"];
        }
        $config['upload_path']="./assets/images";
        $config['allowed_types']='gif|jpg|png';
        $config['encrypt_name'] = TRUE;
         
        $this->load->library('upload',$config);
        if($this->upload->do_upload("file")){
            $data = $this->upload->data();
 
            //Resize and Compress Image
            $config['image_library']='gd2';
            $config['source_image']='./assets/images/'.$data['file_name'];
            $config['create_thumb']= FALSE;
            $config['maintain_ratio']= FALSE;
            $config['quality']= '60%';
            $config['width']= 600;
            $config['height']= 400;
            $config['new_image']= './assets/images/'.$data['file_name'];
            $this->load->library('image_lib', $config);
            $this->image_lib->resize();
 
            
            $data['path']= './assets/images/'.$data['file_name']; 
            
            $this->user_model->update_portrait($email,$data['path']);
            echo $data['path']; 

           
        }
        
       
        
       
    }
    
}