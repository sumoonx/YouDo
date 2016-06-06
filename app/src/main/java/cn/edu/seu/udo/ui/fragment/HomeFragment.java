package cn.edu.seu.udo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.bean.WeatherBean;
import cn.edu.seu.udo.model.Tool;
import cn.edu.seu.udo.mvp.presenter.WeatherPresenter;
import cn.edu.seu.udo.mvp.view.WeatherIView;
import cn.edu.seu.udo.ui.view.BannerPreviewHolder;
import cn.edu.seu.udo.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnItemClickListener,
        WeatherIView<WeatherBean> {

    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.tool_list)
    ListView toolsView;


    private WeatherPresenter weatherPresenter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        if (tool.getName().equals("study")) {
            fragmentInteractionListener.onFragmentInteraction(StudyFragment.START);
        } else {
            ToastUtil.show(getActivity(), tool.getName());
        }
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
        ToastUtil.show(getActivity(), "Banner");
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
