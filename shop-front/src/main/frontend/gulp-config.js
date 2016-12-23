'use strict';

var distFolder = '../resources/static/';

module.exports = {
    fonts: [
        'lib/cms-icons/icons.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Light.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Regular.{woff2,woff,ttf,eot}',
        'lib/roboto-fontface/fonts/Roboto-Medium.{woff2,woff,ttf,eot}'
    ],
    libs: {},
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