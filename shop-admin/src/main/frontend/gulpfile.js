'use strict';

var require = require || {};

var config = require('./gulp-config');

var concat = require('gulp-concat');
var es = require('event-stream');
var file = require('gulp-file');
var glyphsMap = require('iconfont-glyphs-map');
var gulp = require('gulp');
var htmlmin = require('gulp-htmlmin');
var iconfont = require('gulp-iconfont');
var jsonSassObj = require('json-sass-obj');
var ngTemplates = require('gulp-ng-templates');
var runSequence = require('run-sequence');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var uglify = require('gulp-uglify');

var helpers = {
    /**
     *  Catch errors
     */
    swallowError: function (error) {
        console.log(error.message);
        this.emit('end');
    }
};

/////////////////////////////////////
// Global / dev tasks              //
/////////////////////////////////////

/**
 *    Watch all assets and dev dependencies for changes
 */
gulp.task('watch', ['scripts:watch', 'style:watch', 'font:watch']);

/**
 * Copy images across
 */
gulp.task('images', function () {
    return gulp.src(config.src.img)
        .pipe(gulp.dest(config.dist.img));
});

/**
 * Default build task
 */
gulp.task('build', function (cb) {
    runSequence('font', ['images', 'style', 'scripts'], cb);
});

/**
 * Default build task
 */
gulp.task('default', ['build']);

/////////////////////////////////////
// Style                           //
/////////////////////////////////////

/**
 *  Compile CSS
 */
gulp.task('style:build', function () {
    return gulp.src(config.src.css)
        .pipe(sourcemaps.init())
        .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
        .pipe(sourcemaps.write('./maps'))
        .pipe(gulp.dest(config.dist.css));
});

/**
 * Copy style assets (e.g. images)
 */
gulp.task('style:assets', function () {
    return gulp.src([config.src.assets])
        .pipe(gulp.dest(config.dist.css));
});

/**
 * Watch for style changes
 */
gulp.task('style:watch', function () {
    gulp.watch(config.src.assets, config.watch, ['style:assets']);
    gulp.watch(config.src.css, config.watch, ['style:build']);
});

/**
 * Run all style tasks
 */
gulp.task('style', ['style:build', 'style:assets']);

/////////////////////////////////////
// Fonts                           //
/////////////////////////////////////

/**
 * Copy fonts across
 */
gulp.task('font', ['font:icons'], function () {
    return gulp.src(config.fonts)
        .pipe(gulp.dest(config.dist.fonts));
});

/**
 * Construct an icon font
 */
gulp.task('font:icons', function () {
    return gulp.src(config.src.icon)
        .pipe(iconfont({
            normalize: true,
            fontWidth: 24,
            fontName: 'icons',
            formats: ['ttf', 'eot', 'woff', 'woff2']
        }).on('glyphs', function (glyphs) {
            file('_cms-icons-map.scss', JSON.stringify({
                name: 'icons',
                glyphs: glyphsMap(glyphs, '', true)
            }))
                .pipe(jsonSassObj({
                    prefix: '$font: ',
                    suffix: ' !default;'
                }))
                .pipe(gulp.dest('lib/cms-icons'));
        }))
        .pipe(gulp.dest('lib/cms-icons'));
});

/**
 * Build fonts then update styles
 */
gulp.task('font:style', function (cb) {
    runSequence('font', 'style:build', cb);
});

/**
 * Watch for style changes
 */
gulp.task('font:watch', function () {
    gulp.watch(config.src.icon, config.watch, ['font:style']);
});

/////////////////////////////////////
// Script                          //
/////////////////////////////////////

/**
 * Merge all vendor JS files
 */
gulp.task('scripts:lib', function () {
    // Loop over each lib
    var tasks = Object.keys(config.libs).map(function (lib) {
        return gulp.src(config.libs[lib])
            .pipe(concat(lib + '.js'))
            .pipe(gulp.dest(config.dist.js));
    });
    return es.concat.apply(null, tasks);
});

/**
 * Merge and compile JS source files
 */
gulp.task('scripts:templates', function () {
    return gulp.src(config.src.tem)
        .pipe(htmlmin({
            collapseWhitespace: true, removeComments: true
        }).on('error', helpers.swallowError))
        .pipe(ngTemplates({
            module: 'cms.templates',
            filename: 'templates.js',
            path: function (path, base) {
                return '/' + path.replace(base, '');
            }
        }))
        .pipe(gulp.dest(config.dist.js));
});

/**
 * Merge and compile JS source files
 */
gulp.task('scripts:js', function () {
    return gulp.src(config.src.js)
        .pipe(sourcemaps.init())
        .pipe(concat('scripts.js'))
        .pipe(uglify({mangle: false}).on('error', helpers.swallowError))
        .pipe(sourcemaps.write('./maps'))
        .pipe(gulp.dest(config.dist.js));
});

/**
 * Merge scripts and templates
 */
gulp.task('scripts:build', ['scripts:js', 'scripts:templates'], function () {
    return gulp.src([config.dist.js + '/scripts.js', config.dist.js + '/templates.js'])
        .pipe(concat('app.min.js'))
        .pipe(gulp.dest(config.dist.js));
});

/**
 * Watches for script changes
 */
gulp.task('scripts:watch', function () {
    gulp.watch([config.src.js, config.src.tem], config.watch, ['scripts:build']);
});

/**
 * Run all script build processes
 */
gulp.task('scripts', ['scripts:lib', 'scripts:build']);