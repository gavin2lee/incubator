(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('gridCheckbox', gridCheckboxFunc);

  /** @ngInject */
  /**
   * 表格多选框
   * @return {object}
   */
  function gridCheckboxFunc() {
    return {
      require: '^stTable',
      template: '<input type="checkbox"/>',
      scope: {
        row: '=gridCheckbox'
      },
      link: function(scope, element, attr, ctrl) {
        element.addClass('print-remove');

        element.bind('change', function() {
          scope.$apply(function() {
            ctrl.select(scope.row, 'multiple');
          });
        });

        scope.$watch('row.isSelected', function(newValue) {
          /*
          选择框与打印结合，勾选了某项后，打印不显示。
           */
          var printWindowElement = document.querySelector('#printWindow');
          var printWindow;
          if (printWindowElement) {
            printWindow = printWindowElement.contentWindow;
          }
          var rowClass = '.' + Array.prototype.find.call(element.parent()[0].classList, function(classItem) {
            return classItem.indexOf('row') >= 0;
          });
          var printWindowRow;

          if (rowClass !== '' && rowClass && printWindow) {
            printWindowRow = printWindow.document.querySelector(rowClass);
          }
          
          if (newValue === true) {
            element.parent().addClass('active print-remove');
            if (printWindowRow) {
              angular.element(printWindowRow).addClass('print-remove')
            }
          } else {
            element.parent().removeClass('active print-remove');
            if (printWindowRow) {
              angular.element(printWindowRow).removeClass('print-remove')
            }
          }

          element[0].querySelector('input').checked = typeof newValue !== 'undefined' || newValue ? newValue : false;
        });
      }
    };
  }
})();
