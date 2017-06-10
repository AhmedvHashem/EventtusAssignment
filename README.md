# EventtusAssignment
a simple twitter client for android using TwitterKit SDK 

# Features
1. Splash Screen that decides the logic of opening Login or Followers screen.
2. Login with your Twitter.
3. Followers screen that shows the current user followers in a recycler view with Pull to Refresh and Infinite Scrolling.
4. Followers Information shows basic user information and his/her last 10 Tweets.

# Extra Bonus
1. Followers/FollowerInfo screens works in offline mode using api response caching not a local database like SQLlist or Realm.
2. Followers Information screen with a sticky header for background image.
3. Localization for Arabic and English.

# Architecture Design Pattern used
- MVP (Model-View-Presenter) from [Google Android Architecture](https://github.com/googlesamples/android-architecture)
- Package by feature not layer refer to [this](https://medium.com/@cesarmcferreira/package-by-features-not-layers-2d076df1964d)
# Third party library used
1. [TwitterSDK](https://dev.twitter.com/twitterkit/android/overview)
2. [Retrofit](http://square.github.io/retrofit/)
3. [Gson](https://github.com/google/gson)
4. [Picasso](http://square.github.io/picasso/)
5. [Butterknife](http://jakewharton.github.io/butterknife/)
6. [Parceler](https://github.com/johncarl81/parceler)
7. [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)
8. [LeakCanary](https://github.com/square/leakcanary)

# Why
1. **Retrofit**: Type-safe HTTP client for Android and Java by Square, Inc. with retrofit we can easly call web apis and with the help of Gson we can parse JSON and capture JSON from the response body, all we need to deal with is just a plain java objects (POJO) files

2. **Picasso**: again from Square, Inc. comes the Image loading library. Many common pitfalls of image loading are handled by Picasso like 
- Memory caching out of the box with the help of OkHttp
- customizable complex image transformation 
- download cancellation in an adapter hassle-free

3. **Parceler**: using Annotations it allows you to convert you POJO to a Parcelable object and using it is much faster than using Serialization as Parcelables does not use reflection.

4. **Butterknife**: view injection can never be easer. it reduce the amount of boilerplate code for just initialize views and hooking view event like OnClick.

5. **LeakCanary**: the nightmare catcher. it catches MemoryLeaks on the fly and alert you with the leak details in a push notification msg, neat hah.













