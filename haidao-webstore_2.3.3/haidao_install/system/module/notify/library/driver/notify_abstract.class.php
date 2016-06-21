<?php
abstract class notify_abstract
{   
	protected $config = array();
	public function _notify($send){
    	$queue = new queue();
		if(!$send){
			$queue->send_fail($this->config['id']);
		}else{
			$queue->update($this->config['id']);
		}
		return true;
    }
}