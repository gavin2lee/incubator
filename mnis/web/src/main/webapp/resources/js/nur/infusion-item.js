/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);
$(function () {
    $('#patCrisValueLayout').on('click', 'li.clickAble', function (e) {
        var $this = $(this),
            zyId = $this.data('inhospno');
        log(zyId);
        if (zyId) {
            window.location.href = ay.contextPath + '/nur/infusionmanager/infusionDetail.do?id=' + zyId;
        }
    });
});