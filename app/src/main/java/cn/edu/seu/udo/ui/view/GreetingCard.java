package cn.edu.seu.udo.ui.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.Greeting;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Author: Jeremy Xu on 2016/6/21 16:42
 * E-mail: jeremy_xm@163.com
 */
public class GreetingCard extends Card {

    private Greeting greeting;

    protected TextView nameText;
    protected TextView greetingText;
    protected TextView timeText;

    public GreetingCard(Context context, Greeting greeting) {
        super(context, R.layout.greeting_card_content);
        this.greeting = greeting;
        setBackgroundColorResourceId(R.color.card_base_cardwithlist_background_list_color);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        nameText = (TextView) parent.findViewById(R.id.greeting_card_name);
        greetingText = (TextView) parent.findViewById(R.id.greeting_card_content);
        timeText = (TextView) parent.findViewById(R.id.greeting_card_time);
        nameText.setText(greeting.getNickName());
        greetingText.setText(greeting.getContent());
        timeText.setText(greeting.getTimeStr());
    }


}
