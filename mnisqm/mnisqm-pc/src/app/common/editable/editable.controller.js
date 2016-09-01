(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('EditableController', editableController);

  /** @ngInject */
  function editableController($rootScope, $scope) {
    var vm = this;

    vm.isEditing = false;
    // debugger;
    $scope.$parent.editableForm = vm.name;
  }
})();
