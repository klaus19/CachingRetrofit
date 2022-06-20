package com.example.cachingretrofit;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cachingretrofit.api.ApiService;
import com.example.cachingretrofit.databinding.SecondBinding;
import com.example.cachingretrofit.utils.Constants;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    private SecondBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecondBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setUpRetrofit();

        binding.button.setOnClickListener(view -> getRandomJokefromAPI());


    }

    private void getRandomJokefromAPI() {
        Observable<JokeResponse> observable = apiService.getRandomJokes("random");
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(jokeResponse -> jokeResponse.getValue()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        binding.textView.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "An error occurred in the Retrofit request. Perhaps no response/cache", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void setUpRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(getCacheDir(), "offlineCache");

        //10 MB
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(provideCacheInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient)
                .baseUrl(Constants.BASE_URL)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private Interceptor provideCacheInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
                String cacheControl = originalResponse.header("Cache-Control");

                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {


                    CacheControl cc = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();


                    request = request.newBuilder()
                            .cacheControl(cc)
                            .build();

                    return chain.proceed(request);

                } else {
                    return originalResponse;
                }
            }
        };

    }


    private Interceptor provideOfflineCacheInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Exception e) {


                    CacheControl cacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();

                    Request offlineRequest = chain.request().newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                    return chain.proceed(offlineRequest);
                }
            }
        };



    }
}
