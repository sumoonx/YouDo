package cn.edu.seu.udo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
public class RiseFragment extends BaseFragment implements RiseIView, View.OnClickListener {

    public static final String TAG = "rise";

    public static final String START = "start_rise";


    @BindView(R.id.greeting_card_ptr_frame)
    PtrFrameLayout frameLayout;
    @BindView(R.id.morning_contents)
    CardRecyclerView greetingCards;
    CardArrayRecyclerViewAdapter cardAdapter;
    List<Greeting> greetings;
    @BindView(R.id.sleep_btn)
    CircularProgressButton sleepBtn;
    @BindView(R.id.my_greeting)
    MaterialEditText myGreeting;
    @BindView(R.id.rise_btn)
    CircularProgressButton riseBtn;
    private GridLayoutManager manager;
    private RisePresenter presenter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupGreetingCards();
        setupPtrHeader();
        setupRiseMenu();

        return view;
    }

    private void setupRiseMenu() {
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


    private String randomGreeting;
    @Override
    public void notifyRandomGreeting(String greeting) {
        randomGreetingSuccess = true;
        randomGreeting = greeting;
    }

    @Override
    public void notifyGreetingSend() {
        greetingSend = true;
    }

    private boolean greetingSend;

    private boolean randomGreetingSuccess;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sleep_btn:
                trySleep();
                break;
            case R.id.rise_btn:
                tryRise();
                break;
        }
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

    private void setupPtrHeader() {
        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setUp(frameLayout);

        frameLayout.setLoadingMinTime(1000);
        frameLayout.setDurationToCloseHeader(1500);
        frameLayout.setHeaderView(header);
        frameLayout.addPtrUIHandler(header);
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.autoRefresh(true);
            }
        }, 100);

        frameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !isLoading;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getGreetingAfter();
                frameLayout.refreshComplete();
            }
        });
    }

    boolean isLoading = false;

    private void getGreetingAfter() {
        isLoading = true;
        if (greetings == null) {
            greetings = presenter.getAfter(null);
        } else {
            greetings.addAll(0, presenter.getAfter(greetings.get(0)));
        }
        cardAdapter.setCards(getGreetingCards(greetings));
        cardAdapter.notifyDataSetChanged();
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
        cardAdapter.setCards(getGreetingCards(greetings));
        cardAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showGreeting(List<Greeting> greetings) {
        cardAdapter.setCards(getGreetingCards(greetings));
        greetingCards.getAdapter().notifyDataSetChanged();
    }

    private boolean noMoreGreeting = false;

    private void setupGreetingCards() {
        cardAdapter = new CardArrayRecyclerViewAdapter(getActivity(), null);
        greetingCards.setHasFixedSize(false);
        greetingCards.setLayoutManager(new LinearLayoutManager(getActivity()));
        greetingCards.setAdapter(cardAdapter);
        manager = new GridLayoutManager(getActivity(), 1);
        greetingCards.setLayoutManager(manager);
        greetingCards.setItemAnimator(new DefaultItemAnimator());
        greetingCards.addOnScrollListener(new CardRecyclerView.OnScrollListener() {
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