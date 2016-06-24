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
import android.widget.LinearLayout;
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

//TODO:软键盘关闭操作（点击任何其他控件都将关闭）
public class RiseFragment extends BaseFragment implements RiseIView, OnClickListener {

    public static final String TAG = "rise";

    public static final String START = "start_rise";


    @BindView(R.id.greeting_card_ptr_frame)
    PtrFrameLayout ptrFrameLayout;

    private GridLayoutManager manager;

    @BindView(R.id.morning_contents)
    CardRecyclerView greetingView;

    List<Greeting> mGreetings;

    @BindView(R.id.sleep_btn)
    CircularProgressButton sleepBtn;
    @BindView(R.id.my_greeting)
    MaterialEditText myGreeting;
    @BindView(R.id.rise_btn)
    CircularProgressButton riseBtn;

    private RisePresenter presenter;

    //state
    boolean isLoading;
    private boolean randomGreetingSuccess;
    private String randomGreeting;
    private boolean greetingSend;
    private boolean noMoreGreeting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupGreetingView();
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
                myGreeting.clearFocus();
                break;
            case R.id.rise_content:
                myGreeting.clearFocus();
                break;
        }
    }

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
                int position = manager.findFirstVisibleItemPosition();
                View item = manager.findViewByPosition(position);
                int top = item.getTop();
                boolean rst = position == 0 && top < 15;
                return rst;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getGreetingAfter();
            }
        });
//        ptrFrameLayout.disableWhenHorizontalMove(true);
    }

    private void setupGreetingView() {
        mGreetings = new ArrayList<>();
        mGreetings.add(Greeting.getEmpty());
        CardArrayRecyclerViewAdapter greetingAdapter = new CardArrayRecyclerViewAdapter(getActivity(), getGreetingCards(mGreetings));
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
                    else if (lastPosition >= manager.getItemCount() - 2) {
                        getGreetingBefore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.i("dy is: " + dy);
            }
        });
        greetingView.requestDisallowInterceptTouchEvent(true);
    }

    private void setupRiseMenu() {
        LinearLayout layout = (LinearLayout) ptrFrameLayout.findViewById(R.id.rise_content);
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

    @Override
    public void notifyRandomGreeting(String greeting) {
        randomGreetingSuccess = true;
        randomGreeting = greeting;
    }

    @Override
    public void notifyGreetingSend() {
        greetingSend = true;
    }

    @Override
    public void notifyAfterLoaded(List<Greeting> greetings) {
        mGreetings.addAll(0, greetings);
        CardArrayRecyclerViewAdapter greetingAdapter = (CardArrayRecyclerViewAdapter) greetingView.getAdapter();
        greetingAdapter.setCards(getGreetingCards(mGreetings));
        greetingAdapter.notifyDataSetChanged();
        ptrFrameLayout.refreshComplete();
        isLoading = false;
    }

    @Override
    public void notifyBeforeLoaded(List<Greeting> greetings) {
        checkEmptyGreeting(greetings);
        if (!noMoreGreeting) {
            mGreetings.remove(mGreetings.size() - 1);
            mGreetings.addAll(greetings);
            CardArrayRecyclerViewAdapter greetingAdapter = (CardArrayRecyclerViewAdapter) greetingView.getAdapter();
            greetingAdapter.setCards(getGreetingCards(mGreetings));
            greetingAdapter.notifyDataSetChanged();
        }
        isLoading = false;
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
        presenter.getAfter(mGreetings.get(0));
    }

    private void getGreetingBefore() {
        if (noMoreGreeting) return;
        isLoading = true;
        int lastPosition = mGreetings.size() > 1 ? mGreetings.size() - 1 : 0;
        presenter.getBefore(mGreetings.get(lastPosition));
    }

    private Card getGreetingCard(Greeting greeting) {
        if (greeting.isEmpty()) return null;
        Card card = new GreetingCard(getActivity(), greeting);
        CardThumbnail thumb = new GreetingThumbnail(getActivity());
        thumb.setDrawableResource(greeting.getThumbnail());
        card.addCardThumbnail(thumb);
        CardExpand expand = new GreetingExpand(getActivity(), greeting);
        card.addCardExpand(expand);
        card.setBackgroundColorResourceId(greeting.getColor());
        return card;
    }

    private List<Card> getGreetingCards(List<Greeting> greetings) {
        List<Card> cards = new ArrayList<>();
        for (Greeting greeting : greetings) {
            Card card = getGreetingCard(greeting);
            if (card != null) cards.add(getGreetingCard(greeting));
        }
        return cards;
    }

    private void checkEmptyGreeting(List<Greeting> greetings) {
        if (noMoreGreeting) return;
        if (greetings.get(greetings.size() - 1).isEmpty()) {
            noMoreGreeting = true;
            greetings.remove(greetings.size() - 1);
        }
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