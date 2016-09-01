(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('editableForm', eidtableFunc)
    .directive('editableInput', editableInputFunc)
    .directive('editableStart', editableStartFunc)
    .directive('editableLabel', editableLabelFunc)
    .directive('editableEnd', editableEndFunc);
  /** @ngInject */
  function eidtableFunc() {
    return {
      restrict: 'A',
      scope: {},
      controller: 'EditableController',
      controllerAs: 'vm',
      bindToController: {
        mainCtrl: '=editableForm',
        name: '=formName'
      },
      link: function (scope, elem, attrs) {
        scope.$parent.editableForm = attrs.name;

        scope.$parent[attrs.name].showEdit = function () {
          scope.$parent.isEditing = true;

          elem.addClass('form-editing')
        }
        scope.$parent[attrs.name].endEdit = function () {
          scope.$parent.isEditing = false;

          elem.removeClass('form-editing')
        }
      }
    };
  }

  function editableInputFunc() {
    return {
      restrict: 'A',
      scope: false,
      require: '?^editableForm',
      link: function (scope, elem, attr, ctrl) {
        if (!ctrl) return;

        scope.$watch('isEditing', function (v) {
          if (v) {
            elem.css('display', 'inline-block');
          } else {
            elem.css('display', 'none');
          }
        })

      }
    };
  }

  function editableStartFunc() {
    return {
      restrict: 'A',
      scope: false,
      require: '?^editableForm',
      link: function (scope, elem, attr, ctrl) {
        if (!ctrl) return;

        scope.$watch('isEditing', function (v) {
          if (v) {
            elem.css('display', 'none');
          } else {
            elem.css('display', 'inline-block');
          }
        })

      }
    }
  }

  function editableLabelFunc() {
    return {
      restrict: 'A',
      scope: false,
      require: '?^editableForm',
      link: function (scope, elem, attr, ctrl) {
        if (!ctrl) return;

        scope.$watch('isEditing', function (v) {
          if (v) {
            elem.css('display', 'none');
          } else {
            elem.css('display', 'inline-block');
          }
        })

      }
    }
  }

  function editableEndFunc() {
    return {
      restrict: 'A',
      scope: false,
      require: '?^editableForm',
      link: function (scope, elem, attr, ctrl) {
        if (!ctrl) return;

        scope.$watch('isEditing', function (v) {
          if (v) {
            elem.css('display', 'inline-block');
          } else {
            elem.css('display', 'none');
          }
        })

      }
    }
  }

})();
