<?php
class User_model extends CI_Model
{
    public function __construct()
    {
        $this->load->database();
        $this->tblName = 'post1';
    }

    public function get_profile($email)
    {
        
        $query = $this->db->get_where(
            'profile',
            array('email' => $email)
        );
        return $query->row_array();
    }

    public function get_user($email)
    {
        if ($email === FALSE) {
            $query = $this->db->get('users');
            return $query->result_array();
        }
        $query = $this->db->get_where(
            'users',
            array('email' => $email)
        );
        return $query->row_array();
    }
    public function set_user()
    {
        $password = $this->input->post('password');
        $data = array(
            'username' => $this->input->post('username'),
            'password' => $this->bcrypt->hash_password($password),
            'email' => $this->input->post('email')
        );
        $this->db->insert(
            'users',
            $data
        );

        $data = array(
            
            'email' => $this->input->post('email')
        );
        $this->db->insert(
            'profile',
            $data
        );
       


    }

    public function authenticate($email, $password) {
		// $query = $this->db->get_where("users", array('email' => $email));
		
		$query = $this->db->select('*')->from('users')->where('email', $email)->get();
		$row = $query->row_array();

		if (isset($row)) {
            if($this->bcrypt->check_password($password, $row['password'])){
                return FALSE;
            }else{
                return TRUE;
            }
			
		} else {
			return FALSE;
		}

    }

    public function update_phone($email, $phone) {
		
		$data = array(
            'phone' => $phone,
            
        );
        $this->db->where('email', $email);
        $this->db->update('profile', $data);

    }
    public function update_portrait($email, $image) {
		
		$data = array(
            
            'img_path'=>$image
        );
        $this->db->where('email', $email);
        $this->db->update('profile', $data);

    }

    public function update_good_image($email, $image){
        $data = array(
            
            'image_path'=>$image
        );
        $this->db->where('email', $email);
        $this->db->update('post1', $data);
    }
     
    public function update_good_detail($email, $title, $price, $description, $image){
        $data = array(
            
            'title'=>$title,
            'price'=>$price,
            'description'=>$description,
            'email'=>$email,
            'image_path'=> $image
        );
        $this->db->replace('post1', $data);
    }
    public function get_post($params = array()){
        $this->db->select('*');
        $this->db->from($this->tblName);
        
        //fetch data by conditions
        if(array_key_exists("where",$params)){
            foreach ($params['where'] as $key => $value){
                $this->db->where($key,$value);
            }
        }
        
        if(array_key_exists("order_by",$params)){
            $this->db->order_by($params['order_by']);
        }
        
        if(array_key_exists("id",$params)){
            $this->db->where('id',$params['id']);
            $query = $this->db->get();
            $result = $query->row_array();
        }else{
            //set start and limit
            if(array_key_exists("start",$params) && array_key_exists("limit",$params)){
                $this->db->limit($params['limit'],$params['start']);
            }elseif(!array_key_exists("start",$params) && array_key_exists("limit",$params)){
                $this->db->limit($params['limit']);
            }
            
            if(array_key_exists("returnType",$params) && $params['returnType'] == 'count'){
                $result = $this->db->count_all_results();
            }else{
                $query = $this->db->get();
                $result = ($query->num_rows() > 0)?$query->result_array():FALSE;
            }
        }
        return $result;
        //return fetched data
    }
    public function like_post($keyword){
        $query = $this->db->query("SELECT * FROM post1 where title Like '%{$keyword}%'");

        return $query->result();
    }


    public function specific_good($id){
        $query = $this->db->query("SELECT * FROM post1 where id = ".$id."");
        return $query->row();
    }

    function search_post($title){
        $this->db->like('title', $title , 'both');
        $this->db->order_by('title', 'ASC');
        $this->db->limit(10);
        return $this->db->get('post1')->result();
    }
    
    function remove_post($id){
        $this->db->query("DELETE FROM post1 WHERE id = ".$id."");
    }
}
