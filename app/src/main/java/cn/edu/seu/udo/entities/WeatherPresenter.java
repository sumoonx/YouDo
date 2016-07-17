package cn.edu.seu.udo.entities;

import cn.edu.seu.udo.entities.WeatherBean;
import cn.edu.seu.udo.mvp.presenter.Presenter;
import cn.edu.seu.udo.mvp.view.WeatherIView;
import cn.edu.seu.udo.mvp.Model.WeatherModel;
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
    WeatherModel weatherModel;

    public WeatherPresenter() {
        weatherModel = new WeatherModel();
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
        weatherIView = null;
    }
}
