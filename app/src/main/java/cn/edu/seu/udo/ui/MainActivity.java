package cn.edu.seu.udo.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.List;
import java.util.Stack;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.ui.fragment.BaseFragment;
import cn.edu.seu.udo.ui.fragment.DrawerFragment;
import cn.edu.seu.udo.ui.fragment.HomeFragment;
import cn.edu.seu.udo.ui.fragment.RiseCoolFragment;
import cn.edu.seu.udo.ui.fragment.RiseFragment;
import cn.edu.seu.udo.ui.fragment.StudyDetailFragment;
import cn.edu.seu.udo.ui.fragment.StudyFragment;
import cn.edu.seu.udo.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    private static final String START = HomeFragment.START;

    private DrawerLayout drawer;
    private TextView title;

    private Stack<BaseFragment> fragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new Stack<>();
        setupDrawer();
        setupToolBar();
        setupMain();
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
//        Drawable drawable = new IconicsDrawable(this)
//                .icon(MaterialDesignIconic.Icon.gmi_arrow_back);
        imageView.setImageResource(R.drawable.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onBackPressed() {
        BaseFragment topFragment = fragmentStack.peek();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (topFragment == null || !topFragment.onBackPressed()) {
            LogUtil.i("remained fragment has " + getFragmentManager().getBackStackEntryCount());
            LogUtil.i("current fragment is  " + topFragment.getName());
            if (topFragment.getName().equals(HomeFragment.TAG)) {
                finish();
            } else {
                removeFragment();
            }
        }
    }

    @Override
    public void setTopFragment(BaseFragment fragment) {
        fragmentStack.push(fragment);
        LogUtil.i("fragment " + fragment.getName() + "is now at top");
    }

    @Override
    public void onFragmentInteraction(String uri) {
        if (uri == null)    return;
        switch (uri) {
            case HomeFragment.START:
                backHome();
                title.setText("You Do");
                break;
            case StudyFragment.START:
                addFragment(new StudyFragment());
                title.setText("自习");
                break;
            case StudyDetailFragment.START:
                addFragment(new StudyDetailFragment());
                title.setText("自习详情");
                break;
            case RiseFragment.START:
                RiseFragment fragment = new RiseFragment();
                Slide slide = new Slide(Gravity.TOP);
                slide.setDuration(1000);
                fragment.setEnterTransition(slide);
                addFragment(fragment);
                title.setText("早起");
                break;
            case RiseCoolFragment.START:
                RiseCoolFragment fragment1 = new RiseCoolFragment();
                addFragment(fragment1);
                title.setText("早起历史");
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
        //ragmentStack.push(fragment);
//        transaction.commitAllowingStateLoss();
        transaction.commit();
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
            BaseFragment topFragment = fragmentStack.pop();
            LogUtil.i("fragment " + topFragment.getName() + " is removed");
        }
    }

    private void clearBackStack() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
            fragmentManager.getFragments();
        }
        fragmentStack.clear();
    }
}
