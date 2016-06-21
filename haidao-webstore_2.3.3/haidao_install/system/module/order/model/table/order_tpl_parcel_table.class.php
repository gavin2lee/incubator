<?php
/**
 * 		订单发货单 模型
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class order_tpl_parcel_table extends table {

	protected $_validate = array(
        /* array(验证字段1,验证规则,错误提示,[验证条件,附加规则,验证时间]), */
        // 内容
        array('content', 'require', '{order/content_require}', self::MUST_VALIDATE, 'regex', self::MODEL_INSERT),
    );

    //自动完成
    protected $_auto = array(
    	// array(完成字段1,完成规则,[完成条件,附加规则]),
        array('update_time','time',3,'function'),       //更新数据时更新时间
    );
}