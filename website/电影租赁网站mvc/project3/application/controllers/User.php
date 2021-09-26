<?php class User extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
    
        $this->load->model('user_model');
        
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
                $this->load->view('header');
                $this->load->view('login', $this->data);
                
                $this->load->view('footer');
            }
        }


       
    }
    public function sign_in()
    {
        $email = $this->input->post('email');
        $password = $this->input->post('password');
        $remember = $this->input->post('remember');
        


        if ($this->user_model->authenticate($email, $password)) {
            if ($remember=='on') {
                setcookie("email", $_POST["email"], time() + 60*60*24, "/");       
                setcookie("keep", $_POST["email"], time() + 60*60*24, "/");                 
            } else {
                delete_cookie('email');
                delete_cookie('keep');
            }
            $_SESSION['email'] = $email;
            $_SESSION['password'] = $password;

            redirect(base_url(). "home");
        } else {
            $this->data['message'] = "Your email or password is incorrect!";
            $this->index();
        }
    }
    public function logout() {
        session_destroy();
        delete_cookie('email');
        redirect(base_url() . "home");
    }
}
