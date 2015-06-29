var gulp = require('gulp');

gulp.task('default', ['copy-js', 'copy-html']);

gulp.task('copy-js', function() {
  return gulp.src('../wunderx-server/target/web/public/main/*.js')
      .pipe(gulp.dest('www/js'));
});

gulp.task('copy-html', function() {
  return gulp.src('..//wunderx-server/public/*.html')
      .pipe(gulp.dest('www'));
});