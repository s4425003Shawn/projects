<?php class Detail extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        
        $this->load->model('user_model');
        $this->load->library('paypal_lib');
        $this->load->library('paypal_lib');
        $this->load->model('product');
        
        
    }
   
    function buy($id){
        // Set variables for paypal form
        $returnURL = base_url().'paypal/success';
        $cancelURL = base_url().'paypal/cancel';
        $notifyURL = base_url().'paypal/ipn';
        
        // Get product data from the database
        $product = $this->product->getRows($id);
        
        // Get current user ID from the session
        if (isset($_SESSION["email"])) {
            $userID = $_SESSION['email'];
        }elseif(isset($_COOKIE['email'])){
            $userID = $_COOKIE['email'];
        }
        
        
        // Add fields to paypal form
        $this->paypal_lib->add_field('return', $returnURL);
        $this->paypal_lib->add_field('cancel_return', $cancelURL);
        $this->paypal_lib->add_field('notify_url', $notifyURL);
        $this->paypal_lib->add_field('item_name', $product['title']);
        $this->paypal_lib->add_field('custom', $userID);
        $this->paypal_lib->add_field('item_number',  $product['id']);
        $this->paypal_lib->add_field('amount',  $product['price']);
        
        // Render paypal form
        $this->paypal_lib->paypal_auto_form();
    }

    public function good($id)
    {
        $_SESSION['id']=$id;
        if (isset($_SESSION["email"])) {
            $email = $_SESSION["email"];
            $data['good'] = $this->user_model->specific_good($id);
            $data['profile'] = $this->user_model->get_profile($email);
            
            $this->load->view('header');
            $this->load->view('nav', $data);
            $this->load->view('detail', $data);
            $this->load->view('footer');
        } else {
            if (isset($_COOKIE['email'])) {
                $email = $_COOKIE["email"];
                $data['good'] = $this->user_model->specific_good($id);
                $data['profile'] = $this->user_model->get_profile($email);
                
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('detail', $data);
                $this->load->view('footer');
            } else {
                
                $data['good'] = $this->user_model->specific_good($id);
                
                
                $this->load->view('header');
                $this->load->view('nav', $data);
                $this->load->view('detail', $data);
                $this->load->view('footer');
            }
        }
            
    }
           
}
