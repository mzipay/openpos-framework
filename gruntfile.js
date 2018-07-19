module.exports = function(grunt) {

    grunt.initConfig({
        watch: {
            files: {
              files: ['**/*', '!**/node_modules/**'],
              tasks: ['sync-files'],
              options: {
                spawn: false,
              },
            },
          },
        sync: {
            main: {
              files: [
                {
                    cwd: './',
                    src: [
                        '**/*',
                        '!**/node_modules/**',
                        '!gruntfile.js'
                    ], 
                    dest: 
                    '.dist/'
                }, 
              ],
              verbose: true, // Default: false
              pretend: false, // Don't do any disk operations - just write log. Default: false
              failOnError: true, // Fail the task when copying is not possible. Default: false
              updateAndDelete: true, // Remove all files from dest that are not found in src. Default: false
              compareUsing: "md5" // compares via md5 hash of file contents, instead of file modification time. Default: "mtime"
           
            }
          },
          exec: {
            link: {
                cwd: '.dist',
                cmd: 'npm link'
            },
            prune: {
                cwd: '.dist',
                cmd: 'rm -rf node_modules'
            }
        },
        clean: {
            node_modules: ['.dist/node_modules']
          }
      });
       
      grunt.loadNpmTasks('grunt-sync');
      grunt.loadNpmTasks('grunt-contrib-watch');
      grunt.loadNpmTasks('grunt-exec');
      grunt.loadNpmTasks('grunt-contrib-clean');

      grunt.registerTask('default', ['sync', 'exec:link', 'clean:node_modules', 'watch']);
      grunt.registerTask('sync-files', ['sync']);
  
  };