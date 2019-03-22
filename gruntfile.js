module.exports = function(grunt) {

    grunt.initConfig({
        concurrent: {
            target: {
                tasks: [['wait', 'link', 'watch-bundle'], 'watch-build'],
                options: {
                    logConcurrentOutput: true
                }
            }
        },
        exec: {
            build: {
                cmd: 'npm run watch-openpos-lib'
            },
            bundle: {
                cmd: 'npm run watch-scss'
            },
            link: {
                cwd: 'dist/openpos-client-core-lib',
                cmd: 'npm link'
            }
        }
    });
     
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-exec');

    grunt.registerTask('default', ['concurrent:target']);
    grunt.registerTask('watch-build', ['exec:build']);
    grunt.registerTask('watch-bundle', ['exec:bundle']);
    grunt.registerTask('link', ['exec:link']);
    grunt.registerTask('wait', 'wait:pause', function() {
        var done = this.async();
        setTimeout(function() {
            done();
          }, 30000);
    });
  };