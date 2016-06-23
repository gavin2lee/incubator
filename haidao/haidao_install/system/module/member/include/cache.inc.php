<?php
/* 更新用户组缓存 */
$groups = model('member/member_group')->select();
cache('groups', $groups, 'member');