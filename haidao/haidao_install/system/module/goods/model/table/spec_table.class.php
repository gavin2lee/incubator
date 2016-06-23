<?php
/**
 *		规格模型数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class spec_table extends table {
	protected $_validate = array(
		array('name','require','{good/goods_spec_name_require}',table::MUST_VALIDATE),
		array('name','','{goods/goods_spec_name_unique}',table::MUST_VALIDATE,'unique'),
        array('status','number','{godos/state_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
        array('sort','number','{goods/sort_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
    );
    protected $_auto = array(
    );
}