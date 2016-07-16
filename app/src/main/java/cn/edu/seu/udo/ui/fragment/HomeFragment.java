package cn.edu.seu.udo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;
import com.yalantis.flipviewpager.view.FlipViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.WeatherBean;
import cn.edu.seu.udo.model.entities.Tool;
import cn.edu.seu.udo.mvp.presenter.WeatherPresenter;
import cn.edu.seu.udo.mvp.view.WeatherIView;
import cn.edu.seu.udo.ui.model.BannerPreviewHolder;
import cn.edu.seu.udo.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cn.edu.seu.udo.ui.fragment.InteractFragment.ActivityInteractionCallback} interface
 * to handle interactWithActivity events.
 */
public class HomeFragment extends ScreenFragment implements AdapterView.OnItemClickListener, OnItemClickListener,
        WeatherIView<WeatherBean> {

    public static final String TAG = "HomeFragment";

    public static final String START = ScreenFragment.START + TAG;

    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.tool_list)
    ListView toolsView;


    private WeatherPresenter weatherPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        weatherPresenter = new WeatherPresenter();
        weatherPresenter.takeView(this);
        setupListView();
        setupBanner();
        return view;
    }

    /**
     * callback for list view
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Home", "ListView item clicked");
        Tool tool = (Tool) toolsView.getAdapter().getItem(position);
        String intent = null;
        switch (tool.getName()) {
            case "study":
                intent = StudyFragment.START;
                break;
            case "rise":
                intent = RiseFragment.START;
                break;
            default:
                ToastUtil.show(getActivity(), tool.getName());
        }
        activityInteraction.doInteract(intent);
    }

    /**
     * banner callback
     *
     * @param position item clicked
     */
    @Override
    public void onItemClick(int position) {
        Log.i("Home", "Banner clicked on " + position);
        weatherPresenter.getWeather("南京");
    }

    private void setupBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.sukura);
        banner.setPages(
                new CBViewHolderCreator<BannerPreviewHolder>() {
                    @Override
                    public BannerPreviewHolder createHolder() {
                        return new BannerPreviewHolder();
                    }
                }, images)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this);
    }

    private void setupListView() {
        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
        ToolAdapter toolAdapter = new ToolAdapter(getActivity(), tools(), settings);
        toolsView.setOnItemClickListener(this);
        toolsView.setAdapter(toolAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    private List<Tool> tools() {
        final List<Tool> tools = new ArrayList<>();

        tools.add(new Tool(R.drawable.study, "study", new ArrayList<String>() {{
            add("study now");
            add("stop study");
        }}));
        tools.add(new Tool(R.drawable.exercise, "exercise", new ArrayList<String>() {{
            add("exercise now");
            add("stop exercise");
        }}));
        tools.add(new Tool(R.drawable.rise, "rise", new ArrayList<String>() {{
            add("rise now");
            add("stop rise");
        }}));
        tools.add(new Tool(R.drawable.rank, "rank", new ArrayList<String>() {{
            add("rank now");
            add("stop rank");
        }}));

        return tools;
    }

    @Override
    public void show(WeatherBean weatherBean) {
        ToastUtil.show(this.getActivity(), weatherBean.getData() + "," + weatherBean.getWendu() + "," + weatherBean.getType());
    }

    @Override
    public String getTitle() {
        return "You Do";
    }

    @Override
    public String getIntent() {
        return START;
    }

    class ToolAdapter extends BaseFlipAdapter<Tool> {

        private final int PAGES = 3;
        private LayoutInflater inflater;

        public ToolAdapter(Context context, List<Tool> tools, FlipSettings settings) {
            super(context, tools, settings);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, Tool item1, Tool item2) {
            final ToolHolder toolHolder;

            if (convertView == null) {
                toolHolder = new ToolHolder();
                convertView = inflater.inflate(R.layout.tool_merge, parent, false);
                toolHolder.leftView = (ImageView) convertView.findViewById(R.id.first);
                toolHolder.rightView = (ImageView) convertView.findViewById(R.id.second);
                toolHolder.infoPage = inflater.inflate(R.layout.tool_info, parent, false);
                toolHolder.toolName = (TextView) toolHolder.infoPage.findViewById(R.id.tool_name);

                convertView.setTag(toolHolder);
            } else {
                toolHolder = (ToolHolder) convertView.getTag();
            }

            switch (position) {
                case 1:
                    toolHolder.leftView.setImageResource(item1.getIconRes());
                    if (item2 != null)
                        toolHolder.rightView.setImageResource(item2.getIconRes());
                    break;
                default:
                    fillHolder(toolHolder, position == 0 ? item1 : item2);
                    toolHolder.infoPage.setTag(toolHolder);
                    return toolHolder.infoPage;
            }
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            FlipViewPager flipViewPager = (FlipViewPager) view.findViewById(R.id.flip_view);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) flipViewPager.getLayoutParams();
            params.height = 500;
            flipViewPager.setLayoutParams(params);
            return view;
        }

        @Override
        public int getPagesCount() {
            return PAGES;
        }

        private void fillHolder(ToolHolder holder, Tool tool) {
            if (tool != null) holder.toolName.setText(tool.getName());
        }

        class ToolHolder {
            ImageView leftView;
            ImageView rightView;
            View infoPage;

            TextView toolName;
        }
    }
}
