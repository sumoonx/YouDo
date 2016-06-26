package cn.edu.seu.udo.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.ui.fragment.BaseFragment;
import cn.edu.seu.udo.ui.fragment.DrawerFragment;
import cn.edu.seu.udo.ui.fragment.HomeFragment;
import cn.edu.seu.udo.ui.fragment.RiseCoolFragment;
import cn.edu.seu.udo.ui.fragment.RiseFragment;
import cn.edu.seu.udo.ui.fragment.StudyDetailFragment;
import cn.edu.seu.udo.ui.fragment.StudyFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    private static final String START = HomeFragment.START;

    private DrawerLayout drawer;
    private BaseFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        setupMain();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (contentFragment == null || !contentFragment.onBackPressed()) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                removeFragment();
            }
        }
    }

    @Override
    public void setTopFragment(BaseFragment fragment) {
        contentFragment = fragment;
    }

    @Override
    public void onFragmentInteraction(String uri) {
        if (uri == null)    return;
        switch (uri) {
            case HomeFragment.START:
                backHome();
                break;
            case StudyFragment.START:
                addFragment(new StudyFragment());
                break;
            case StudyDetailFragment.START:
                addFragment(new StudyDetailFragment());
                break;
            case RiseFragment.START:
                RiseFragment fragment = new RiseFragment();
                Slide slide = new Slide(Gravity.TOP);
                slide.setDuration(1000);
                fragment.setEnterTransition(slide);
                addFragment(fragment);
                break;
            case RiseCoolFragment.START:
                RiseCoolFragment fragment1 = new RiseCoolFragment();
                addFragment(fragment1);
                break;
        }
    }

    //TODO:
    private void backHome() {
        addFragment(HomeFragment.newInstance("", ""));
    }

    private void setupDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //remove full screen mask while drawer open
        if (drawer != null) drawer.setScrimColor(0x00ffffff);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawer, new DrawerFragment());
        transaction.commit();
    }

    private void setupMain() {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.container, HomeFragment.newInstance("", ""));
//        transaction.commit();
        onFragmentInteraction(START);
    }

    private void addFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
//        transaction.commitAllowingStateLoss();
        transaction.commit();
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        }
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
