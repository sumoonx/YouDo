package cn.edu.seu.udo.ui.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.Greeting;
import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Author: Jeremy Xu on 2016/6/22 16:08
 * E-mail: jeremy_xm@163.com
 */
public class GreetingExpand extends CardExpand {

    private Greeting greeting;

    public GreetingExpand(Context context, Greeting greeting) {
        super(context, R.layout.greeting_card_expand);
        this.greeting = greeting;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null)   return;
        TextView struggle = (TextView) view.findViewById(R.id.struggle);
        TextView success = (TextView) view.findViewById(R.id.success);

        struggle.setText(greeting.getStruggle());
        success.setText(greeting.getSuccessRate());
    }
}
