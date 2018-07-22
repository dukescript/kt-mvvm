### Browser

With [kt-mvvm](README.md) [your application](https://github.com/dukescript/kt-mvvm-demo)
can be transpiled to JavaScript and executed in
the browser. The `bck2brwsrShow` task generates necessary `.js` files and
launches a browser with the application
```bash
$ ./gradlew bck2brwsrShow
...
Showing http://localhost:53241/index.html
...
$ ls -l web/build/web/
bck2brwsr.js
index.css
index.html
lib
main.js
```
The files in `web/build/web` directory
contain everything needed to execute your application and as such they can be
deployed to any HTTP server as a static content.
Read [more](https://github.com/jtulach/bck2brwsr/blob/master/docs/GRADLE.md).

![Kt-Mvvm-Demo in the browser](firefox.png)
