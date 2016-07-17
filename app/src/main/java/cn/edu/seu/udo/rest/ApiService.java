package cn.edu.seu.udo.rest;


import cn.edu.seu.udo.entities.WeatherUrlBean;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/*
 *desc 服务器请求接口
 *author rhg
 *time 2016/6/6 13:41
 *email 1013773046@qq.com
 */
public interface ApiService {
    /*获取天气*/
    @GET("/weather_mini")
    Observable<WeatherUrlBean> getWeather(
            @Query("city") String city);

}
