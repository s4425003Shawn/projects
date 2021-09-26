<?php class Register extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        
        $this->load->model('user_model');
        $this->data['status'] = "";
        $this->data['message'] = "";
     
        
    }
    public function index()
    {   
        if(isset($_SESSION['email'])){
            redirect(base_url() . "home");
        }else{
            if(isset($_COOKIE['email'])){
                redirect(base_url() . "home");
            }else{
                $config = array(
                    'img_path'      => 'captcha_images/',
                    'img_url'       => base_url().'captcha_images/',
                    
                    'img_width'     => '160',
                    'img_height'    => 50,
                    'word_length'   => 4,
                    'font_size'     => 18
                    
                );
                $captcha = create_captcha($config);
                
                // Unset previous captcha and set new captcha word
                
                $this->session->unset_userdata('captchaCode');
                $this->session->set_userdata('captchaCode', $captcha['word']);
                // Pass captcha image to view
                $this->data['captchaImg'] = $captcha['image'];
                
                // Load the view
                
                $this->load->view('header');
                $this->load->view('register', $this->data);
                
                $this->load->view('footer');
            }
        }
       
    }

    
    public function sign_up()
    {

        $email = $this->input->post('email');
        $inputCaptcha = $this->input->post('captcha');
        $sessCaptcha = $this->session->userdata('captchaCode');
        if(empty($this->user_model->get_user($email))){
            if($inputCaptcha === $sessCaptcha){
                $this->data['message'] = "Register successful";
            $this->data['status'] = "bg-success";
            $this->user_model->set_user();
            $this->index();
            }else{
                $this->data['message'] = "captcha";
            $this->data['status'] = "bg-danger";
            
            $this->index();
            }
            
        }else{
            $this->data['message'] = "This eamil has already signed up";
            $this->data['status'] = "bg-danger";
            $this->index();
        }
        
        
    }
    
}