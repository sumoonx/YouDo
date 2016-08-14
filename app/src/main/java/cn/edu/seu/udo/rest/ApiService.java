package cn.edu.seu.udo.rest;


import cn.edu.seu.udo.entities.UploadResult;
import cn.edu.seu.udo.entities.WeatherUrlBean;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
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


    /*获取天气*/
    @FormUrlEncoded
    @POST("/upload")
    Observable<UploadResult> uploadRecord(
            @Field("user_id") String user_id,
            @Field("score") String score,
            @Field("duration") String duration,
            @Field("details") String details,
            @Field("timestamp") String timestamp);
}
