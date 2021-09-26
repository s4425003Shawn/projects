<?php
class Searching extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        
        $this->load->model('user_model');
       
        
    }
    public function index()
    {
        $keyword = $this->input->post('searching');
        if (isset($_SESSION["email"])) {
            $email = $_SESSION["email"];

            $data['keyword'] = $keyword;
            $data['profile'] = $this->user_model->get_profile($email);
            $data['post'] = $this->user_model->like_post($keyword);
            $this->load->view('header');
            $this->load->view('nav', $data);
            $this->load->view('home', $data);
            $this->load->view('footer');
        } else {
            if (isset($_COOKIE['email'])) {
                $email = $_COOKIE["email"];

                $data['keyword'] = $keyword;
                $data['profile'] = $this->user_model->get_profile($email);
                $data['post'] = $this->user_model->like_post($keyword);
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('home', $data);
                $this->load->view('footer');
            } else {

                $data['post'] = $this->user_model->like_post($keyword);
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('home', $data);
                $this->load->view('footer');
            }
        }
    }
}
