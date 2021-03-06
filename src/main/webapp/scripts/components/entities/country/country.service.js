'use strict';

angular.module('mycellarApp')
    .factory('Country', function ($resource, DateUtils) {
        return $resource('api/countrys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'withDependencies': { method: 'GET', params : {withDependencies: true}, isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
