# 编程指南

整体架构采用MVP模式，UI显示采用单Activity多Fragment结构。

## Fragment

所有UIFragment都应该继承BaseFragment，它处理了ButterKnife，其子类可以直接绑定控件，不需要调用findViewById，只需要实现它的抽象方法getLayout来指定布局文件即可。

InteractFragment继承自BaseFragment，中间添加了ActivityInteractionCallback以及onBackPressed方法，其中ActivityInteractionCallback是用来和Activity进行交互的回调接口，其中有一个doInteract方法，接受一个String类型作为交互的指令（类似Intent）。

ScreenFragment继承自InteractFragment，这里用来代替原先的Activity，它将占据整个screen。
每个Screen都要继承ScreenFragment，同时更新其START。代码示例如下：

    public static final String TAG = "StudyFragment";
    public static final String START = ScreenFragment.START + TAG;
然后实现抽象方法getIntent：

    @Override
    public String getIntent() {
        return START;
    }
这里的START是用来和Activity进行交互的指令（上面提到，类似Intent），如果要在一个Screen中启动另一个Screen需要调用

	activityInteraction.doInteract(StudyFragment.START);
相应的Activity收到回调，并检测传递的String变量是否以ScreenFragment.START开头，如果是则交给ScreenManager进行窗口管理的操作（这个类在后面介绍）。

还需要注意的一点是，所有可以使用依赖注入初始化的变量全都放在一个单独的函数initInjector()中，它将在ScreenFragment的onCreate中自动调用，因此只需要重新实现该函数对变量进行初始化，如：

	@Override
    protected void initInjector() {
        super.initInjector();
        presenter =  new StudyDetailPresenter();
    }
这是为了方便后面使用Dagger2进行以来注入。

## ScreenManager
这是Activity中管理ScreenFragment的类。
如果需要修改App启动的主界面（如用来调试自己的Fragment），可以修改如下：

	private static final String HOME = StudyFragment.START;
其中，startScreen用来启动一个ScreenFragment，并且采用类似SingTask的模式，即如果Screen已经存在则将该Screen之上的所有Screen弹出销毁，否则创建一个新的Screen。

stopCurrentScreen即是将当前Screen移除，回退到上一个Screen，需要注意的是，如果已经到了之前设置的HomeScreen，则stop操作会直接退出当前Activity。

比较重要的还有newScreen方法，他根据给定的String生成指定的Screen，显然，如果你在代码中新添加了一个ScreenFragment，则需要在该处添加构造代码,否则Activity将不能启动该Screen。

## MVP模式
在mvp的package下分别继承Presenter和IView来实现MVP模式。一般的，一个ScreenFragment需要实现对应的IView接口，拥有一个对应的Presenter引用。

test ljj
## TODO