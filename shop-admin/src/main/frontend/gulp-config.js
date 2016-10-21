'use strict';

var distFolder = '../resources/static/';

module.exports = {
    webserver: {
        host: 'localhost',
        port: 3000
    },
    fonts: [
        'lib/cms-icons/icons.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Light.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Regular.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Medium.{woff2,woff,ttf,eot}'
    ],
    libs: {
        vendor: [
            'lib/angular/angular.js',
            'lib/angular-cookies/angular-cookies.js',
            'lib/angular-route/angular-route.js',
            'lib/angular-sanitize/angular-sanitize.js',
            'lib/ngstorage/ngStorage.js',
            'lib/ng-dialog/js/ngDialog.js',
            'lib/angular-drag-and-drop-lists/angular-drag-and-drop-lists.js',
            'lib/ng-sortable/dist/ng-sortable.js'
        ],
        'vendor.min': [
            'lib/angular/angular.min.js',
            'lib/angular-cookies/angular-cookies.min.js',
            'lib/angular-route/angular-route.min.js',
            'lib/angular-sanitize/angular-sanitize.min.js',
            'lib/ngstorage/ngStorage.min.js',
            'lib/ng-dialog/js/ngDialog.min.js',
            'lib/angular-drag-and-drop-lists/angular-drag-and-drop-lists.min.js',
            'lib/ng-sortable/dist/ng-sortable.min.js'
        ]
    },
    src: {
        assets: 'styles/**/*.{jpg,png,gif,svg}',
        css: 'styles/scss/**/*.scss',
        icon: 'icons/**/*.svg',
        img: 'images/**/*.{jpg,png,gif,ico}',
        js: 'scripts/**/*.js',
        tem: 'scripts/**/*.ng.html'
    },
    dist: {
        css: distFolder + 'styles',
        fonts: distFolder + 'fonts',
        img: distFolder + 'images',
        js: distFolder + 'scripts'
    },
    watch: {
        interval: '500'
    }
};