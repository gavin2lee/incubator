/**
 * Created by gary on 16/6/7.
 */
var fs = require('fs');
var recursive = require('recursive-readdir');
var path = require('path');


function addVersionToFile(err, files) {
    if (!files) {
        console.log(files);
        return;
    }
    files.forEach(function (file_path, index) {
        fs.readFile(file_path, {encoding: 'utf-8'}, function addVersion(err, str) {
            var newStr = str.replace(/((\#url\()*\'.+\.js)[?\d]*/g, '$1?' + new Date().getTime());
            fs.writeFile(file_path, newStr, function (err) {
              if (err) {
                console.log(err);
                return;
              }
              console.log(index + '   success');
            });
        });
    });
}


rootPath = process.argv[2];
recursive(rootPath, ['.vm'], addVersionToFile);
//node revision.js '../web/src/main/webapp/WEB-INF/view/nur/test'
