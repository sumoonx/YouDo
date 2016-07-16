package cn.edu.seu.udo.ui.fragment;

import android.os.Bundle;

/**
 * Author: Jeremy Xu on 2016/7/15 21:03
 * E-mail: jeremy_xm@163.com
 */
public abstract class InteractFragment extends BaseFragment {

    protected ActivityInteractionCallback activityInteraction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof ActivityInteractionCallback)) {
            throw new ClassCastException("Hosting Activity must implement ActivityInteractionCallback");
        } else {
            activityInteraction = (ActivityInteractionCallback) getActivity();
        }
    }

    @Override
    public void onDetach() {
        activityInteraction = null;
        super.onDetach();
    }

    //return false to let activity to handle it
    public boolean onBackPressed() {
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interactWithActivity in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ActivityInteractionCallback {
        void doInteract(String intent);
    }
}
