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

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(Context context, TwitterSession session) {
        super(session, MyTwitterApiClient.createCachedClient(context));
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public MyTwitterCustomService getTwitterCustomService() {
        return getService(MyTwitterCustomService.class);
    }

    private static OkHttpClient createCachedClient(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "cache_file");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        return new OkHttpClient.Builder().cache(cache)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String cacheHeaderValue = Utils.isOnline(context)
                            ? "public, max-age=2419200"
                            : "public, only-if-cached, max-stale=2419200";
                    Request request = originalRequest.newBuilder().build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build();
                })
                .addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String cacheHeaderValue = Utils.isOnline(context)
                            ? "public, max-age=2419200"
                            : "public, only-if-cached, max-stale=2419200";
                    Request request = originalRequest.newBuilder().build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build();
                })
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
