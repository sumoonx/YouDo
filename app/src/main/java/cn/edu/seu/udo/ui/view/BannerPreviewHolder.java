package cn.edu.seu.udo.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Author: Jeremy Xu on 2016/6/3 21:06
 * E-mail: jeremy_xm@163.com
 */
public class BannerPreviewHolder implements Holder<Integer> {

    private ImageView view;

    @Override
    public View createView(Context context) {
        view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        view.setImageResource(data);
    }
}
