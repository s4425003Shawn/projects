<?php
class Home extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        
        $this->load->model('user_model');
        $this->perPage = 4;
        
    }
    public function index()
    {
        if (isset($_SESSION["email"])) {
            $email = $_SESSION["email"];
           
            $data['profile'] = $this->user_model->get_profile($email);
            
        
        // Get posts data from the database
                $conditions['order_by'] = "id DESC";
                $conditions['limit'] = $this->perPage;
                $data['posts'] = $this->user_model->get_post($conditions);
            $this->load->view('header');
            $this->load->view('nav', $data);
            $this->load->view('home', $data);
            $this->load->view('footer');
        } else {
            if (isset($_COOKIE['email'])) {
                $email = $_COOKIE["email"];
                
                $data['profile'] = $this->user_model->get_profile($email);
                $
        
        // Get posts data from the database
                $conditions['order_by'] = "id DESC";
                $conditions['limit'] = $this->perPage;
                $data['posts'] = $this->user_model->get_post($conditions);
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('home', $data);
                $this->load->view('footer');
            } else {
                $data = array();
        
        // Get posts data from the database
                $conditions['order_by'] = "id DESC";
                $conditions['limit'] = $this->perPage;
                $data['posts'] = $this->user_model->get_post($conditions);
                $this->load->view('header');
                $this->load->view('nav');
                $this->load->view('home',$data);
                $this->load->view('footer');
            }
        }
    }
    function get_autocomplete(){
        if (isset($_GET['term'])) {
            $result = $this->user_model->search_post($_GET['term']);
            if (count($result) > 0) {
            foreach ($result as $row)
                $arr_result[] = $row->title;
                echo json_encode($arr_result);
            }
        }
    }

    function loadMoreData(){
        $conditions = array();
        
        // Get last post ID
        $lastID = $this->input->post('id');
        
        // Get post rows num
        $conditions['where'] = array('id <'=>$lastID);
        $conditions['returnType'] = 'count';
        $data['postNum'] = $this->user_model->get_post($conditions);
        
        // Get posts data from the database
        $conditions['returnType'] = '';
        $conditions['order_by'] = "id DESC";
        $conditions['limit'] = $this->perPage;
        $data['posts'] = $this->user_model->get_post($conditions);
        
        $data['postLimit'] = $this->perPage;
        
        // Pass data to view
        
        $this->load->view('load-more-data', $data, false);
        
    }
 
}
