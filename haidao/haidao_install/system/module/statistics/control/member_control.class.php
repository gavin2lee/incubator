<?php
Core::load_class('init', 'admin');
class member_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('member');
		$this->deposit = $this->load->service('member_deposit');
	}

	public function index(){
		$sqlmap['_string']='date_format(from_UNIXTIME(`register_time`),\'%Y-%m-%d\') = date_format(now(),\'%Y-%m-%d\')';
		$member['today'] = $this->service->_count($field,$sqlmap);
		$sqlmap['_string']='date_format(from_UNIXTIME(`register_time`),\'%Y-%m\') = date_format(now(),\'%Y-%m\')';
		$member['tomonth'] = $this->service->_count($field,$sqlmap);
		unset($sqlmap);
		$field = "SUM(money) money,count(id) as num";
		$data= $this->service->_query($field,$sqlmap);
		$member['money'] = $data[0]['money'];
		$member['num'] = $data[0]['num'];
		$this->load->librarys('View')->assign('data',$data);
		$this->load->librarys('View')->assign('member',$member);
		$this->load->librarys('View')->display('member');
	}

	public function ajax_getdata(){
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$params = $_GET;
		$sqlmap = array();
		$xAxis = array();
		/* 时间周期 */
		if(isset($params['days']) && $params['days']>0){
			$params['etime'] = time();
			$params['stime'] = strtotime("-{$params['days']}day",$params['etime']);
			
		}
		if(isset($params['stime']{0}) && isset($params['etime']{0})){
			$params['etime'] = strtotime($params['etime']);
			$params['stime'] = strtotime($params['stime']);
		}
		
		$days=round(($params['etime']-$params['stime'])/86400);
		
		//两个时间戳之间的天数
		
		$sqlmap['register_time'] = array('between',array(
			strtotime(date('Y-m-d',$params['stime']).'00:00:00'),
			strtotime(date('Y-m-d',$params['etime']).'23:59:59')
		));
		$group = 'days';
		$subtext = $params['stime'].'-'.$params['etime'];
		for ($i=0; $i <= $days; $i++) { 
			$xAxis[$i] = date('Y-m-d',strtotime("+{$i}day",$params['stime']));
		}
		//注册数
		$field = "FROM_UNIXTIME(register_time,'%Y-%m-%d') days,count(id) as member_num";
		$_reg = $this->service->_query($field,$sqlmap,$group);
	
		foreach($_reg as $k =>$v){
			$_reg[$v['days']] = $_reg[$k];
		}
		//充值数
		$sqlmap['trade_time'] = array('between',array(
			strtotime(date('Y-m-d',$params['stime']).'00:00:00'),
			strtotime(date('Y-m-d',$params['etime']).'23:59:59')
		));
		$sqlmap['trade_status'] = 1;
		$group = 'days';
		
		$field = "FROM_UNIXTIME(trade_time,'%Y-%m-%d') days,SUM(money) as money";
		$_money = $this->deposit->_query($field,$sqlmap,$group);
		foreach($_money as $k =>$v){
			$_money[$v['days']] = $_money[$k];
		}
		
		//组装数据
		foreach ($xAxis as $key => $value) {
			$_regval[] = isset($_reg[$value]['member_num'])?$_reg[$value]['member_num']:'0';
			$_moneyval[] = isset($_money[$value]['money'])?$_money[$value]['money']:'0.00';
		}

		
		
		$row['member'] ['xAxis']= $xAxis;
		$row['member'] ['reg'][]= $_regval;
		$row['member'] ['money'][]= $_moneyval;
		
		$this->load->librarys('View')->assign('row',$row);
        $row = $this->load->librarys('View')->get('row');
		echo json_encode($row);
	}
}