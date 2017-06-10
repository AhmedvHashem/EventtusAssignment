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
MVP (Model-View-Presenter) from [Google Android Architecture](https://github.com/googlesamples/android-architecture)
# Third party library used
1. [TwitterSDK](https://dev.twitter.com/twitterkit/android/overview)
2. [Retrofit](http://square.github.io/retrofit/)
3. [Gson](https://github.com/google/gson)
4. [Picasso](http://square.github.io/picasso/)
5. [Butterknife](http://jakewharton.github.io/butterknife/)
6. [Parceler](https://github.com/johncarl81/parceler)
7. [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)
