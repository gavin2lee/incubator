<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class member_group_service extends service {
    protected $count;
    protected $pages;

    public function __construct() {
        $this->table = $this->load->table('member/member_group');
	}

    public function add($params = array()) {
        $data = $this->table->create($params);
        $result = $this->table->add($data);
        if(!$result) {
            $this->error = $this->table->getError();
            return FALSE;
        }
        return $this->table->getLastInsID();
    }

    public function edit($params = array()) {
        $result = $this->table->update($params);
        if($result === false) {
            $this->error = $this->table->getError();
            return FALSE;
        }
        return TRUE;
    }

    /**
     * 查询单个会员信息
     * @param int $id
     * @return mixed
     */
    public function fetch_by_id($id) {
        $r = $this->table->find($id);
        if(!$r) {
            $this->error = '_select_not_exist_';
            return FALSE;
        }
        return $r;
    }

    /**
     * 删除指定分组
     * @param type $id
     */
    public function delete_by_id($id) {
        $ids = (array) $id;
        foreach($ids AS $id) {
            $this->table->where(array('id' => $id))->delete();
        }
        return TRUE;
    }
}