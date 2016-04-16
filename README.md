# ArcLayout
[![Maven Central][maven_central_badge_svg]][maven_central_badge_app] [![Android Arsenal][android_arsenal_badge_svg]][android_arsenal_badge_link]

![icon][demo_icon]

A very simple arc layout library for Android.

![Arc Layout Demo1][demo1_gif] ![Arc Layout Demo2][demo2_gif]

Try out the sample application on the Play Store.

[![Get it on Google Play][googleplay_store_badge]][demo_app]

# Usage

_(For a working implementation of this project see the demo/ folder.)_

Add the dependency to your build.gradle.

```
dependencies {
    compile 'com.ogaclejapan.arclayout:library:1.1.0@aar'
}
```

Include the ArcLayout widget in your layout.  

```xml

<com.ogaclejapan.arclayout.ArcLayout
        android:id="@id/arc_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:arc_origin="bottom"
        app:arc_color="#4D000000"
        app:arc_radius="168dp"
        app:arc_axisRadius="120dp"
        app:arc_freeAngle="false"
        app:arc_reverseAngle="false"
        >

    <Button
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:gravity="center"
        android:text="A"
        android:textColor="#FFFFFF"
        android:background="#03A9F4"
        app:arc_origin="center"
        />

    <Button
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:gravity="center"
        android:text="B"
        android:textColor="#FFFFFF"
        android:background="#00BCD4"
        app:arc_origin="center"
        />

    <Button
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:gravity="center"
        android:text="C"
        android:textColor="#FFFFFF"
        android:background="#009688"
        app:arc_origin="center"
        />

</com.ogaclejapan.arclayout.ArcLayout>

```

# Attributes

There are several attributes you can set:

## ArcLayout

![attrs][attrs_overview]

| attr | description |
|:---|:---|
| arc_origin | Center of the arc on layout. All of patterns that can be specified, see the demo app.  |
| arc_color | Arc Shaped color |
| arc_radius | Radius of the layout |
| arc_axisRadius | Radius the axis of the child views |
| arc_freeAngle | If set to true, each child view can set the free angle, default false |
| arc_reverseAngle | If set to true, reverse the order of the child, default false. Note: If arc_freeAngle set to true does not work |


## Child views in ArcLayout

| attr | description |
|:---|:---|
| arc_origin | Set the origin point of arc_axisRadius as well as layout_gravity, default center |
| arc_angle | If arc_freeAngle set to true, layout the specified angle |


# Apps Using ArcLayout

* [Qiitanium][qiitanium]
* [Call Control Free][callcontrolfree_app]

![GIF][callcontrolfree_gif]


# LICENSE

```
Copyright (C) 2015 ogaclejapan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[demo1_gif]: https://raw.githubusercontent.com/ogaclejapan/ArcLayout/master/art/demo1.gif
[demo2_gif]: https://raw.githubusercontent.com/ogaclejapan/ArcLayout/master/art/demo2.gif
[demo_app]: https://play.google.com/store/apps/details?id=com.ogaclejapan.arclayout.demo
[demo_icon]: https://raw.githubusercontent.com/ogaclejapan/ArcLayout/master/art/icon.png
[googleplay_store_badge]: http://www.android.com/images/brand/get_it_on_play_logo_large.png
[maven_central_badge_svg]: https://maven-badges.herokuapp.com/maven-central/com.ogaclejapan.arclayout/library/badge.svg?style=flat
[maven_central_badge_app]: https://maven-badges.herokuapp.com/maven-central/com.ogaclejapan.arclayout/library
[android_arsenal_badge_svg]: https://img.shields.io/badge/Android%20Arsenal-ArcLayout-brightgreen.svg?style=flat
[android_arsenal_badge_link]: http://android-arsenal.com/details/1/1689
[attrs_overview]: https://raw.githubusercontent.com/ogaclejapan/ArcLayout/master/art/attrs.png
[qiitanium]: https://github.com/ogaclejapan/Qiitanium
[callcontrolfree_app]: https://play.google.com/store/apps/details?id=club.androidy.callcontrolfree
[callcontrolfree_gif]: https://raw.githubusercontent.com/ogaclejapan/ArcLayout/master/art/callcontrolfree.gif


