package cn.edu.seu.udo.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.entities.Greeting;
import cn.edu.seu.udo.entities.Time;
import cn.edu.seu.udo.mvp.view.RiseIView;

/**
 * Author: Jeremy Xu on 2016/6/22 14:50
 * E-mail: jeremy_xm@163.com
 */
public class RisePresenter implements Presenter<RiseIView> {

    private RiseIView view;

    @Inject
    public RisePresenter() {}

    @Override
    public void takeView(RiseIView riseIView) {
        view = riseIView;
    }

    @Override
    public void dropView() {
        view = null;
    }

    public void getAfter(Greeting greeting) {
        view.notifyAfterLoaded(fakeGreetings());
    }

    public void getBefore(Greeting greeting) {
        view.notifyBeforeLoaded(fakeGreetings());
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
        greeting = new Greeting(2, R.drawable.invoker, "卡尔", "真理惟一可靠的标准就是永远自相符合。", new Time(7, 48));
        greetings.add(greeting);
        greeting = new Greeting(3, R.drawable.invoker, "萨尔", "放个圈圈框框沉默你。", new Time(6, 52));
        greetings.add(greeting);
        greeting = new Greeting(0, R.drawable.invoker, "卡尔", "真理惟一可靠的标准就是永远自相符合。", new Time(7, 28));
        greetings.add(greeting);
        return greetings;
    }
}
