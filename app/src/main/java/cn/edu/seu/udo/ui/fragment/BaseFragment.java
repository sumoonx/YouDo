package cn.edu.seu.udo.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.seu.udo.R;

/**
 * Author: Jeremy Xu on 2016/5/30 16:09
 * E-mail: jeremy_xm@163.com
 */
public abstract class BaseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Unbinder mBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof OnFragmentInteractionListener)) {
            throw new ClassCastException("Hosting Activity must implement OnFragmentInteractionListener");
        } else {
            mListener = (OnFragmentInteractionListener) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayout(), container, false);
        mBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.setTopFragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        mBinder.unbind();
        super.onDestroyView();
    }

    public void interaction(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //return false to let activity to handle it
    public boolean onBackPressed() {
        return false;
    }

    @LayoutRes
    protected abstract int getLayout();

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void setTopFragment(BaseFragment fragment);
        void onFragmentInteraction(Uri uri);
    }
}
