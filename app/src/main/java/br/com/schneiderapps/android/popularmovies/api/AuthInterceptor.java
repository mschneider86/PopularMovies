package br.com.schneiderapps.android.popularmovies.api;

import java.io.IOException;

import br.com.schneiderapps.android.popularmovies.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("api_key", BuildConfig.ApiKey)
                .build();

        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

