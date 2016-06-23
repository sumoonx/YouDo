package cn.edu.seu.udo.model.repository;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import cn.edu.seu.udo.UdoApplication;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/*
 *desc 请求管理类
 *author rhg
 *time 2016/6/6 10:58
 *email 1013773046@qq.com
 */
public class ApiManager {

    private static ApiManager apiManager;
    private ApiService apiService;

    public static ApiManager getApiManager() {
        if (apiManager == null)
            apiManager = new ApiManager();
        return apiManager;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public ApiManager() {
        /*Okhttp 缓存 ,对于GET有效*/
        OkHttpClient okHttpClient = new OkHttpClient();
        File cacheFile = new File(UdoApplication.getUdoApplication().getExternalCacheDir(), "UdoCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20);/*20M cache*/
        okHttpClient.setCache(cache);
        /*Okhttp 缓存 ,对于GET有效*/

        Retrofit sRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .build();

        this.apiService = sRetrofit.create(ApiService.class);
    }

}
