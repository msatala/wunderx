# WundeRx 0.1
Multi-platform todo app created built with ionic, scala.js, scala.rx and akka actors. Server is Play. 

Built for [Slovak Scala Users' Group](http://www.meetup.com/slovak-scala/).

## Lego bricks used
- [Ionic framework](http://ionicframework.com/) - Prebuilt angular components for mobile/tablet/web app development
- [Scala.js](http://www.scala-js.org/) - Scala to javascript compiler
- [Angulate](https://github.com/jokade/scalajs-angulate) - Set of macros that make scala.js development in angular more natural and type safe.
- [Scala.Rx](https://github.com/lihaoyi/scala.rx) - Reactive variables that solve most mutability problems that come up in modern client applications
- [jsactor](https://github.com/CodeMettle/jsactor) - Experimental (not production ready) implementation of Akka Actors for scala.js 

## Notes
Package `boilerplate` contains code that is necessary for all "lego bricks" to work together. This code is static and doesn't grow with the app.

In html you will notice ```<ion-checkbox ng-model-options="{updateOn: ''}" ...>```. This disables angular two-way binding. 
Otherwise angular would try to update immutable ```completed``` value in Task, which would result in console error.  

This app was created for purposes of live coding in a presentation. Thus some parts are overly simplified and not-considered a good practice. 
This is especially true for:

- The way client actor handles server/client communication. While `jsactor` implementation supports reconnecting after connection loss, it doesn't work here.
- The way compiled scala.js code is copied to ionic app.

    

## Adding project to IntelliJ IDEA
Select *'Import Project'* and choose **build.sbt** from the main directory

## Running WundeRx
```
sbt run
```

## Building/running as a native mobile app
Install node.js dependencies.
```
npm install -g cordova ionic gulp
```

Install [Android](http://cordova.apache.org/docs/en/3.3.0/guide_platforms_android_index.md.html#Android%20Platform%20Guide)/[iOS](http://cordova.apache.org/docs/en/3.3.0/guide_platforms_ios_index.md.html#iOS%20Platform%20Guide)
dependencies.

Run ```gulp``` to copy required files to ionic project.

```
wunderx-ionic/gulp
```

`Note: It's much better to handle this by having a separate sbt project. This is just a simplification for purposes of live coding demonstration.`

Build and run native app. Replace `android` for `ios` to change platform. 

```
ionic platform add android
ionic build android
ionic emulate android
```