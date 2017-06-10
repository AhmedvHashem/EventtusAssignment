package com.ahmednts.eventtusassignment.data;

/**
 * Created by AhmedNTS on 6/5/2017.
 */

import android.content.Context;

import com.ahmednts.eventtusassignment.utils.Utils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTwitterApiClient extends TwitterApiClient {

//    private TwitterSession session;

    public MyTwitterApiClient(Context context, TwitterSession session) {
        super(session, MyTwitterApiClient.createCachedClient(context));

//        this.session = session;
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public MyTwitterService getTwitterService() {
        return getService(MyTwitterService.class);
    }
//
//    public TwitterSession getSession() {
//        return session;
//    }

    private static OkHttpClient createCachedClient(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "cache_file");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);//20mb
        return new OkHttpClient.Builder().cache(cache)
                .addInterceptor(chain -> getCacheInterceptor(context, chain))
                .addNetworkInterceptor(chain -> getCacheInterceptor(context, chain))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    private static Response getCacheInterceptor(Context context, Interceptor.Chain chain) {
        Request originalRequest = chain.request();
        String cacheHeaderValue = Utils.isOnline(context)
                ? "public, max-age=2419200"
                : "public, only-if-cached, max-stale=2419200";
        Request request = originalRequest.newBuilder().build();
        Response response;
        try {
            response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheHeaderValue)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
