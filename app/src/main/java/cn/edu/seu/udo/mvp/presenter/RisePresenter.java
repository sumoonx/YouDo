package cn.edu.seu.udo.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.Greeting;
import cn.edu.seu.udo.model.entities.Time;
import cn.edu.seu.udo.mvp.view.RiseIView;

/**
 * Author: Jeremy Xu on 2016/6/22 14:50
 * E-mail: jeremy_xm@163.com
 */
public class RisePresenter implements Presenter<RiseIView> {

    private RiseIView view;

    @Override
    public void takeView(RiseIView riseIView) {
        view = riseIView;
    }

    @Override
    public void dropView() {
        view = null;
    }

    public List<Greeting> getAfter(Greeting greeting) {
        return fakeGreetings();
    }

    public List<Greeting> getBefore(Greeting greeting) {
        return fakeGreetings();
    }

    private int i = 0;
    public void getRandomGreeting() {
        view.notifyRandomGreeting("帅的人已经醒来+" + (i++) + "!");
    }

    public void sendGreeting(String greeting) {
        view.notifyGreetingSend();
    }

    private List<Greeting> fakeGreetings() {
        List<Greeting> greetings = new ArrayList<>();
        Greeting greeting = new Greeting(1, R.drawable.invoker, "Invoker", "Whatever is worth doing is worth doing well.", new Time(8, 26));
        greetings.add(greeting);
        greeting = new Greeting(1, R.drawable.invoker, "卡尔", "真理惟一可靠的标准就是永远自相符合。", new Time(7, 48));
        greetings.add(greeting);
        greeting = new Greeting(0, R.drawable.invoker, "卡尔", "真理惟一可靠的标准就是永远自相符合。", new Time(7, 48));
        greetings.add(greeting);
        return greetings;
    }
}
