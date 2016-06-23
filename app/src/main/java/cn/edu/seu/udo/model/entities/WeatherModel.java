package cn.edu.seu.udo.model.entities;


import cn.edu.seu.udo.model.repository.ApiManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/*
 *desc weather model层实现类
 *author rhg
 *time 2016/6/6 13:49
 *email 1013773046@qq.com
 */
public class WeatherModel {
    public WeatherModel() {
    }

    public Observable<WeatherBean> getWeather(String city) {
        return ApiManager.getApiManager().getApiService().getWeather(city).flatMap(new Func1<WeatherUrlBean, Observable<WeatherBean>>() {
            @Override
            public Observable<WeatherBean> call(final WeatherUrlBean weatherUrlBean) {
                return Observable.create(new Observable.OnSubscribe<WeatherBean>() {
                    @Override
                    public void call(Subscriber<? super WeatherBean> subscriber) {
                        WeatherBean weatherBean=new WeatherBean(weatherUrlBean.getData().getWendu(), weatherUrlBean.getData().getForecast().get(0).getDate()
                                ,weatherUrlBean.getData().getForecast().get(0).getType());
                        subscriber.onNext(weatherBean);
                    }
                });
            }
        });
    }
}
