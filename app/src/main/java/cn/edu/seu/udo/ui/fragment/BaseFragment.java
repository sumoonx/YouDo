package cn.edu.seu.udo.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment for all fragments, setup {@link ButterKnife}.
 *
 * Author: Jeremy Xu on 2016/5/30 16:09
 * E-mail: jeremy_xm@163.com
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder mBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayout(), container, false);
        mBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        mBinder.unbind();
        super.onDestroyView();
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    @LayoutRes
    protected abstract int getLayout();
}
