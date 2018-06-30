### iOS

You can package your application, for example [our demo](https://github.com/dukescript/kt-mvvm-demo)
as an **iOS** `.ipa` file with the help of Intel's
[Multi OS Engine](https://multi-os-engine.org/) when running on Mac OS X.
First of all list your simulators and then use one of them to launch your 
application:
```bash
$ ./gradlew moeListSimulators
...
- DD9904B6-76CD-4F2D-9153-EC7182878897 - iOS 11.4 - iPhone X
...
$ ./gradlew moeLaunch -Pmoe.launcher.simulators=DD9904B6-76CD-4F2D-9153-EC7182878897
```

![Kt-Mvvm-Demo on iOS](iOS.png)

