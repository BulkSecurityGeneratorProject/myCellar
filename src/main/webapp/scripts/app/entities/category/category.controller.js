'use strict';

angular.module('mycellarApp')
    .controller('CategoryController', function ($scope, Category, Vineward, Bottle, ParseLinks, MycellarOptions) {
        $scope.options = MycellarOptions;
        $scope.categorys = [];
        $scope.vinewards = Vineward.query();
        $scope.bottles = Bottle.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Category.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categorys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
                $('#saveCategoryModal').modal('show');
            });
        };

        $scope.saveCategory = function () {
            if ($scope.category.id != null) {
                Category.update($scope.category,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Category.save($scope.category,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
                $('#deleteCategoryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Category.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCategoryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCategoryModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.category = {name: null, color: null, timeToWait: null, id: null};
//            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
