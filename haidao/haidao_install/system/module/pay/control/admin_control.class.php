<?php
/**
 *      后台支付设置控制器
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class admin_control extends init_control {

    public function _initialize() {
        parent::_initialize();
        $this->service = $this->load->service('payment');
        $this->payments = $this->service->fetch_all();
    }

    /**
     * 获取支付方式列表
     */
    public function setting() {
        $payments = $this->payments;
        $this->load->librarys('View')->assign('payments',$payments);
        $this->load->librarys('View')->display('setting');
    }

    /**
     * 配置支付方式
     */
    public function config() {

        $pay_code = $_GET['pay_code'];
        $payment = $this->payments[$pay_code];
        if (checksubmit('dosubmit')) {
            $_POST['config'] = serialize($_POST['config']);
            if ($this->service->save($_POST)) {
            	$this->service->build_cache();
                showmessage(lang('pay/_enabled_success_'), url('setting'), 1);
            } else {
                showmessage(lang('pay/_enabled_error_'), url('setting'), 0);
            }
        } else {
            $this->load->librarys('View')->assign('pay_code',$pay_code);
            $this->load->librarys('View')->assign('payment',$payment);
            $this->load->librarys('View')->display('config');
        }
    }

    /**
     * 启用禁用支付方式
     */
    public function ajax_enabled() {
        $paycode = $_POST['paycode'];
        if ($this->service->change_enabled($paycode)) {
            showmessage(lang('pay/_enabled_success_'), '', 1);
        } else {
            showmessage(lang('pay/_enabled_error_'), '', 0);
        }
    }

    /**
     * 卸载支付方式
     */
    public function uninstall() {
        $pay_code = $_GET['pay_code'];
        $data = array();
        $data['pay_code'] = $pay_code;
        $this->load->table('payment')->where(array('pay_code'=>$pay_code))->delete();
		$this->service->build_cache();
        showmessage(lang('pay/_uninstall_success_'), url('setting'), 1);

    }

}
