package cn.edu.seu.udo.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.ui.fragment.BaseFragment;
import cn.edu.seu.udo.ui.fragment.DrawerFragment;
import cn.edu.seu.udo.ui.fragment.HomeFragment;
import cn.edu.seu.udo.ui.fragment.InteractFragment;
import cn.edu.seu.udo.ui.fragment.ScreenFragment;
import cn.edu.seu.udo.utils.LogUtil;
import cn.edu.seu.udo.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements InteractFragment.ActivityInteractionCallback {

    private static final String START = HomeFragment.START;

    private DrawerLayout drawer;
    private TextView title;

    private ScreenManager screenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        setupToolBar();
        setupScreen();
    }

    @Override
    public void onBackPressed() {
        ScreenFragment topFragment = screenManager.topScreen();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!topFragment.onBackPressed()) {
            screenManager.stopCurrentScreen();
        }
    }

    @Override
    public void doInteract(String intent) {
        if (intent == null) return;
        if (intent.startsWith(ScreenFragment.START)) {
            screenManager.startScreen(intent);
            ToastUtil.show(this, intent);
        }
//        if (intent == null)    return;
//        switch (intent) {
//            case HomeFragment.START:
//                backHome();
//                title.setText("You Do");
//                break;
//            case StudyFragment.START:
//                addFragment(new StudyFragment());
//                title.setText("自习");
//                break;
//            case StudyDetailFragment.START:
//                addFragment(new StudyDetailFragment());
//                title.setText("自习详情");
//                break;
//            case RiseFragment.START:
//                RiseFragment fragment = new RiseFragment();
//                Slide slide = new Slide(Gravity.TOP);
//                slide.setDuration(1000);
//                fragment.setEnterTransition(slide);
//                addFragment(fragment);
//                title.setText("早起");
//                break;
//            case RiseCoolFragment.START:
//                RiseCoolFragment fragment1 = new RiseCoolFragment();
//                addFragment(fragment1);
//                title.setText("早起历史");
//                break;
//        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    private void setupDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //remove full screen mask while drawer open
        if (drawer != null) drawer.setScrimColor(0x00ffffff);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.drawer, new DrawerFragment());
        transaction.commit();
    }

    private void setupScreen() {
        screenManager = new ScreenManager(this, R.id.container);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
//        Drawable drawable = new IconicsDrawable(this)
//                .icon(MaterialDesignIconic.Icon.gmi_arrow_back);
        imageView.setImageResource(R.drawable.buildings);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
