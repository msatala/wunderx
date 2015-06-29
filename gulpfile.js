var gulp = require('gulp');
var watch = require('gulp-watch');

gulp.task('default', function () {
    watch('../projectluna-client-common/web/**/*.*')
        .pipe(gulp.dest('public'));
});
