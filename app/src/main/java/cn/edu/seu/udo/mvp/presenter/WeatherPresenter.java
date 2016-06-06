package cn.edu.seu.udo.mvp.presenter;

import android.util.Log;

import cn.edu.seu.udo.bean.WeatherBean;
import cn.edu.seu.udo.mvp.model.WeatherMode;
import cn.edu.seu.udo.mvp.view.WeatherIView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 *desc weather presenter层实现类
 *author rhg
 *time 2016/6/6 13:46
 *email 1013773046@qq.com
 */
public class WeatherPresenter implements Presenter<WeatherIView> {
    WeatherIView weatherIView;
    WeatherMode weatherModel;

    public WeatherPresenter() {
        weatherModel = new WeatherMode();
    }

    public void getWeather(String city) {
        weatherModel.getWeather(city).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<WeatherBean>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(WeatherBean weather) {
                        weatherIView.show(weather);

                    }
                });
    }

    @Override
    public void takeView(WeatherIView weatherIView) {
        this.weatherIView = weatherIView;
    }

    @Override
    public void dropView() {

    }
}
