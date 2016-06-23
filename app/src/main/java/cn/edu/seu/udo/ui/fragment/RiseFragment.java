package cn.edu.seu.udo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.Greeting;
import cn.edu.seu.udo.mvp.presenter.RisePresenter;
import cn.edu.seu.udo.mvp.view.RiseIView;
import cn.edu.seu.udo.ui.RentalsSunHeaderView;
import cn.edu.seu.udo.ui.view.GreetingCard;
import cn.edu.seu.udo.ui.view.GreetingExpand;
import cn.edu.seu.udo.ui.view.GreetingThumbnail;
import cn.edu.seu.udo.utils.KeyBoardUtil;
import cn.edu.seu.udo.utils.LogUtil;
import cn.edu.seu.udo.utils.ToastUtil;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Author: Jeremy Xu on 2016/6/21 11:20
 * E-mail: jeremy_xm@163.com
 */

//TODO:RecyclerView和PullToRefresh之间的动作冲突
//TODO:软键盘关闭操作（点击任何其他控件都将关闭）
//TODO:底部留空白，否则被遮住
public class RiseFragment extends BaseFragment implements RiseIView, OnClickListener {

    public static final String TAG = "rise";

    public static final String START = "start_rise";


    @BindView(R.id.greeting_card_ptr_frame)
    PtrFrameLayout ptrFrameLayout;

    private GridLayoutManager manager;
    @BindView(R.id.morning_contents)
    CardRecyclerView greetingView;
    CardArrayRecyclerViewAdapter greetingAdapter;
    List<Greeting> greetings;

    @BindView(R.id.sleep_btn)
    CircularProgressButton sleepBtn;
    @BindView(R.id.my_greeting)
    MaterialEditText myGreeting;
    @BindView(R.id.rise_btn)
    CircularProgressButton riseBtn;

    private RisePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupGreetingCards();
        setupPtrHeader();
        setupRiseMenu();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initInjector();
        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        presenter.dropView();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sleep_btn:
                trySleep();
                break;
            case R.id.rise_btn:
                tryRise();
                break;
            case R.id.greeting_card_ptr_frame:
                KeyBoardUtil.closeKeybord(myGreeting, getActivity());
                break;
            case R.id.rise_content:
                myGreeting.clearFocus();
                break;
        }
    }

    boolean isLoading;
    private void setupPtrHeader() {
        //set header for ptrFrame.
        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setUp(ptrFrameLayout);

        //configure ptr
        ptrFrameLayout.setLoadingMinTime(1000);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(true);
            }
        }, 100);

        //ptr handler
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !isLoading;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getGreetingAfter();
                ptrFrameLayout.refreshComplete();
            }
        });
    }

    private void setupGreetingCards() {
        greetingAdapter = new CardArrayRecyclerViewAdapter(getActivity(), null);
        greetingView.setHasFixedSize(false);
        greetingView.setLayoutManager(new LinearLayoutManager(getActivity()));
        greetingView.setAdapter(greetingAdapter);
        manager = new GridLayoutManager(getActivity(), 1);
        greetingView.setLayoutManager(manager);
        greetingView.setItemAnimator(new DefaultItemAnimator());
        greetingView.addOnScrollListener(new CardRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == CardRecyclerView.SCROLL_STATE_IDLE) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (noMoreGreeting) ToastUtil.show(getActivity(), "Bottom hitted!");
                    else if (lastPosition >= manager.getItemCount() - 4) {
                        getGreetingBefore();
                    }
                }
            }
        });
    }

    private void setupRiseMenu() {
        RelativeLayout layout = (RelativeLayout) ptrFrameLayout.findViewById(R.id.rise_content);
        layout.setOnClickListener(this);
        sleepBtn.setOnClickListener(this);
        riseBtn.setOnClickListener(this);
        myGreeting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyBoardUtil.openKeybord(myGreeting, getActivity());
                    LogUtil.i("has focus");
                } else {
                    KeyBoardUtil.closeKeybord(myGreeting, getActivity());
                    LogUtil.i("lose focus");
                }
            }
        });
    }

    private boolean randomGreetingSuccess;
    private String randomGreeting;
    @Override
    public void notifyRandomGreeting(String greeting) {
        randomGreetingSuccess = true;
        randomGreeting = greeting;
    }

    private boolean greetingSend;
    @Override
    public void notifyGreetingSend() {
        greetingSend = true;
    }

    private void tryRise() {
        greetingSend = false;
        sleepBtn.setClickable(false);
        riseBtn.setIndeterminateProgressMode(true);
        riseBtn.setProgress(50);
        presenter.sendGreeting(myGreeting.getText().toString());
        getGreetingAfter();
        riseBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (greetingSend) {
                    riseBtn.setProgress(0);
                    myGreeting.setText("");
                } else {
                    riseBtn.setProgress(-1);
                }
                sleepBtn.setClickable(true);
            }
        }, 500);
    }

    private void trySleep() {
        randomGreetingSuccess = false;
        riseBtn.setClickable(false);
        presenter.getRandomGreeting();
        sleepBtn.setIndeterminateProgressMode(true);
        sleepBtn.setProgress(50);
        sleepBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (randomGreetingSuccess) {
                    sleepBtn.setProgress(0);
                    myGreeting.setText(randomGreeting);
                } else {
                    sleepBtn.setProgress(-1);
                }
                riseBtn.setClickable(true);
            }
        }, 1000);
    }
    private void getGreetingAfter() {
        isLoading = true;
        if (greetings == null) {
            greetings = presenter.getAfter(null);
        } else {
            greetings.addAll(0, presenter.getAfter(greetings.get(0)));
        }
        greetingAdapter.setCards(getGreetingCards(greetings));
        greetingAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void getGreetingBefore() {
        if (noMoreGreeting) return;
        isLoading = true;
        if (greetings == null) {
            greetings = presenter.getAfter(null);
        } else {
            greetings.addAll(presenter.getBefore(greetings.get(greetings.size() - 1)));
            if (greetings.get(greetings.size() - 1).isLast()) {
                noMoreGreeting = true;
                greetings.remove(greetings.size() - 1);
            }
        }
        greetingAdapter.setCards(getGreetingCards(greetings));
        greetingAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showGreeting(List<Greeting> greetings) {
        greetingAdapter.setCards(getGreetingCards(greetings));
        greetingView.getAdapter().notifyDataSetChanged();
    }

    private boolean noMoreGreeting = false;

    private List<Card> getGreetingCards(List<Greeting> greetings) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < greetings.size(); ++i) {
            Greeting greeting = greetings.get(i);
            Card card = new GreetingCard(getActivity(), greeting);
            CardThumbnail thumb = new GreetingThumbnail(getActivity());
            thumb.setDrawableResource(greeting.getThumbnail());
            card.addCardThumbnail(thumb);
            CardExpand expand = new GreetingExpand(getActivity(), greeting);
            expand.setTitle("This is expandable");
            card.addCardExpand(expand);
            card.setBackgroundColorResourceId(greeting.getColor());
            cards.add(card);
        }
        return cards;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_rise;
    }

    private void initInjector() {
        presenter = new RisePresenter();
    }

}