package com.kamedon.todo.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.TextView
import com.kamedon.todo.BuildConfig
import com.kamedon.todo.R
import com.kamedon.todo.domain.entity.Task
import com.kamedon.todo.domain.entity.User
import com.kamedon.todo.domain.entity.api.NewTaskQuery
import com.kamedon.todo.domain.entity.api.NewTaskResponse
import com.kamedon.todo.domain.usecase.task.TaskUserCase
import com.kamedon.todo.domain.usecase.user.LogoutUseCase
import com.kamedon.todo.domain.value.event.okhttp.OkHttp3ErrorEvent
import com.kamedon.todo.domain.value.login.LoginType
import com.kamedon.todo.infra.repository.UserRepository
import com.kamedon.todo.presentation.adapter.TaskListAdapter
import com.kamedon.todo.presentation.anim.TaskFormAnimation
import com.kamedon.todo.presentation.dialog.EditTaskDialog
import com.kamedon.todo.util.Debug
import com.kamedon.todo.util.extension.observable
import com.kamedon.todo.util.logd
import com.kamedon.todo.util.setupCrashlytics
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Subscriber
import rx.Subscription
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * Created by kamedon on 2/29/16.
 */
class TaskActivity : BaseActivity() {
    /*
     *UseCase
     */
    @Inject lateinit var logoutUseCase: LogoutUseCase
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var taskUserCase: TaskUserCase

    lateinit var taskFormAnimation: TaskFormAnimation

    var user: User? = null

    lateinit var inputMethodManager: InputMethodManager
    lateinit var taskListAdapter: TaskListAdapter

    var subscription: Subscription? = null
    /*
     * list表示で使用する
     */
    private var next: AtomicBoolean = AtomicBoolean(true)

    private var page: AtomicInteger = AtomicInteger(1);
    private var state: String = Task.state_untreated;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_task)
        user = userRepository.getUser();
        user?.setupCrashlytics()


        initToolBar();
        initNavigation();
        taskFormAnimation = TaskFormAnimation(layout_register_form)
        taskFormAnimation.topMargin = resources.getDimension(R.dimen.activity_vertical_margin)
        btn_toggle_task.setOnClickListener {
            taskFormAnimation.toggle();
        }
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;

        /*
         * Task一覧の初期化
         */
        taskListAdapter = TaskListAdapter(layoutInflater, CopyOnWriteArrayList());
        taskListAdapter.onChangedTaskStateComplete = { view, task, complete ->

            observable(taskUserCase.edit(task), object : Subscriber<NewTaskResponse>() {
                override fun onNext(response: NewTaskResponse) {
                    Debug.d("response", response.toString());
                    if (!state.equals(response.task.state) && !state.equals(Task.state_all) ) {
                        taskListAdapter.list.remove(task)
                    }
                    Snackbar.make(layout_register_form, response.task.state(resources), Snackbar.LENGTH_LONG).show();
                }

                override fun onCompleted() {
                    taskListAdapter.notifyDataSetChanged()
                }

                override fun onError(e: Throwable?) {
                    Debug.d("response", e.toString());
                }
            })
        }
        taskListAdapter.onShowEditDialogListener = { position, task ->
            EditTaskDialog(taskUserCase)
                    .setInputMethodManager(inputMethodManager)
                    .setOnDeleteListener(object : EditTaskDialog.OnDeleteListener {
                        override fun onDelete(task: Task) {
                            taskListAdapter.list.remove(task)
                        }

                        override fun onError(e: Throwable?) {
                        }

                        override fun onComplete() {
                            updateEmptyView();
                            taskListAdapter.notifyDataSetChanged()
                            Snackbar.make(layout_register_form, R.string.complete_delete_task, Snackbar.LENGTH_LONG).setAction("Action", null).show()
                        }

                    }).setOnEditListener(object : EditTaskDialog.OnEditListener {
                override fun onEdit(task: Task) {
                }

                override fun onError(e: Throwable?) {
                }

                override fun onComplete() {
                    val view = list.getChildAt(position);
                    taskListAdapter.getView(position, view, list);
                    Snackbar.make(layout_register_form, R.string.complete_edit_task, Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }
            }).show(this@TaskActivity, task)
        }
        list.adapter = taskListAdapter

        btn_register.setOnClickListener {
            view ->
            inputMethodManager.hideSoftInputFromWindow(layout_register_form.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            val query = NewTaskQuery(edit_task.text.toString())
            val errors = query.valid(resources)
            if (errors.isEmpty()) {
                observable(taskUserCase.new(query), object : Subscriber<NewTaskResponse>() {
                    override fun onCompleted() {
                        edit_task.setText("")
                        taskListAdapter.notifyDataSetChanged()
                        updateEmptyView();
                        Snackbar.make(view, R.string.complete_register_task, Snackbar.LENGTH_LONG).setAction("Action", null).show()
                    }

                    override fun onNext(response: NewTaskResponse) {
                        taskListAdapter.list.add(0, response.task)
                    }

                    override fun onError(e: Throwable?) {

                        Debug.d("api", "ng:" + e?.message);
                    }
                }) ;
            } else {
                edit_task.error = errors["task"]
            }
        }
        when (intent?.extras?.getSerializable(LoginType.key()) as? LoginType ?: LoginType.ALREADY) {
            LoginType.NEW -> Snackbar.make(layout_register_form, R.string.welcome, Snackbar.LENGTH_LONG).setAction("Action", null).show()
            LoginType.LOGIN -> Snackbar.make(layout_register_form, R.string.complete_login, Snackbar.LENGTH_LONG).setAction("Action", null).show()
            LoginType.ALREADY -> Snackbar.make(layout_register_form, R.string.hello, Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        ptr_layout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light);
        ptr_layout.setOnRefreshListener {
            page.set(1)
            updateList(state, 1, true);
        }

        list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {

            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                val process = subscription?.isUnsubscribed ?: false
                val isLastItemVisible = totalItemCount == list.firstVisiblePosition + visibleItemCount;
                if (isLastItemVisible && process && next.get()) {
                    updateList(state, page.incrementAndGet(), false)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    //    private fun initApi() {
    //        val client = ApiClientBuilder.create(UserService.getApiKey(perf).token, object : ApiClientBuilder.OnRequestListener {
    //            override fun onTimeoutListener(e: IOException) {
    //                Snackbar.make(layout_register_form, R.string.error_timeout, Snackbar.LENGTH_LONG).show();
    //            }
    //
    //            override fun onInvalidApiKeyOrNotFoundUser(response: Response) {
    //                UserService.deleteApiKey(perf.edit());
    //                val intent = Intent(applicationContext, MainActivity::class.java)
    //                startActivity(intent);
    //                finish();
    //            }
    //        })
    //        api = TodoApiBuilder.buildTaskApi(client)
    //    }

    private fun initNavigation() {
        val navigationView = findViewById(R.id.nav_view) as NavigationView;

        val header = navigationView.getHeaderView(0);
        (header.findViewById(R.id.text_name) as TextView).text = user?.username;
        (header.findViewById(R.id.text_email) as TextView).text = user?.email;
        (header.findViewById(R.id.text_version) as TextView).text = BuildConfig.VERSION_NAME;

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    logoutUseCase.logout()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                R.id.nav_all -> update(Task.state_all, R.string.title_task_all)
                R.id.nav_untreated -> update(Task.state_untreated, R.string.title_task_untreated)
                R.id.nav_complete -> update(Task.state_complete, R.string.title_task_complete)
            }
            false
        }
    }


    private fun initToolBar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout;

        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)

        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Debug.d("menu", "select: ${item.itemId}")
        when (item.itemId) {
            R.id.nav_all -> update(Task.state_all, R.string.title_task_all)
            R.id.nav_untreated -> update(Task.state_untreated, R.string.title_task_untreated)
            R.id.nav_complete -> update(Task.state_complete, R.string.title_task_complete)
        }
        return true;
    }


    override fun onResume() {
        super.onResume()
        ptr_layout.isRefreshing = true;
        page.set(1);
        updateList(state, page.get(), true);
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        taskFormAnimation.init();
    }


    private fun update(state: String, stringId: Int) {
        next.set(true)
        updateHeader(state, stringId)
        updateList(state, 1, true)
    }

    private fun updateHeader(state: String, stringId: Int) {
        this.state = state;
        toolbar.title = getString(stringId)
        drawer_layout.closeDrawers()
    }

    private fun updateList(state: String, page: Int, clean: Boolean) {
        subscription = observable(taskUserCase.list(state, page), object : Subscriber<List<Task>>() {
            override fun onCompleted() {
                taskListAdapter.notifyDataSetChanged()
                ptr_layout.isRefreshing = false;
                updateEmptyView();
                if (clean) {
                    updateForm()
                }
            }


            override fun onNext(response: List<Task>) {
                if (clean) {
                    taskListAdapter.list = CopyOnWriteArrayList(response)
                } else {
                    taskListAdapter.list.addAll(taskListAdapter.list.lastIndex, response)
                }
                taskListAdapter.notifyDataSetChanged()
                next.set(response.size >= BuildConfig.PAGE_LIMIT);
            }

            override fun onError(e: Throwable?) {
                ptr_layout.isRefreshing = false
            }
        }) ;
    }

    private fun updateForm() {
        if (taskListAdapter.isEmpty) {
            taskFormAnimation.show()
        } else {
            taskFormAnimation.hide()
        }

    }

    private fun updateEmptyView() {
        if (taskListAdapter.isEmpty) {
            empty.visibility = View.VISIBLE
            taskFormAnimation.show()
        } else {
            empty.visibility = View.GONE
            layout_register_form.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) fun onEvent(event: OkHttp3ErrorEvent) {
        event.error.msg().logd("http:error:")
    }

}

