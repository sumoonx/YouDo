package cn.edu.seu.udo.mvp.view;

import java.util.List;

import cn.edu.seu.udo.model.entities.Greeting;

/**
 * Author: Jeremy Xu on 2016/6/21 11:20
 * E-mail: jeremy_xm@163.com
 */
public interface RiseIView extends IView {
    void showGreeting(List<Greeting> greetings);

    void notifyRandomGreeting(String greeting);

    void notifyGreetingSend();
}
