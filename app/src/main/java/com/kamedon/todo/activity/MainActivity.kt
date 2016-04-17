package com.kamedon.todo.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.kamedon.todo.BuildConfig
import com.kamedon.todo.KamedonApplication
import com.kamedon.todo.R
import com.kamedon.todo.api.TodoApi
import com.kamedon.todo.di.ActivityModule
import com.kamedon.todo.dialog.SignUpDialog
import com.kamedon.todo.entity.api.Errors
import com.kamedon.todo.entity.api.LoginUserQuery
import com.kamedon.todo.entity.api.NewUserResponse
import com.kamedon.todo.extension.buildIntent
import com.kamedon.todo.extension.observable
import com.kamedon.todo.service.UserService
import com.kamedon.todo.util.logd
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by kamedon on 2/29/16.
 */
class MainActivity : BaseActivity() {

    lateinit var perf: SharedPreferences
    @Inject lateinit var userApi: TodoApi.UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)

        perf = UserService.createSharedPreferences(applicationContext);
        if (UserService.hasApiKey(perf)) {
            startActivity(Intent(applicationContext, TaskActivity::class.java));
            finish();
            return
        }
        setContentView(R.layout.activity_main);
        supportActionBar?.title = "${getString(R.string.app_name)}_${BuildConfig.VERSION_NAME}"
        //        val client = ApiClientBuilder.create(object : ApiClientBuilder.OnRequestListener {
        //            override fun onTimeoutListener(e: IOException) {
        //                Snackbar.make(login_form, R.string.error_timeout, Snackbar.LENGTH_LONG).show();
        //            }
        //
        //            override fun onInvalidApiKeyOrNotFoundUser(response: Response) {
        //            }
        //        });
        //        val api = TodoApiBuilder.buildUserApi(client);
        btn_login.setOnClickListener {
            val query = LoginUserQuery(edit_username.text.toString(), edit_password.text.toString());
            val errors = query.valid(resources)
            if (errors.isEmpty()) {
                observable(userApi.login(query), object : Subscriber<NewUserResponse>() {
                    override fun onCompleted() {
                        if (UserService.isLogin(perf)) {
                            val intent = buildIntent(TaskActivity::class.java)
                            intent.putExtra("user", "login");
                            startActivity(intent);
                            finish()
                        }

                    }

                    override fun onNext(response: NewUserResponse) {
                        response.message.logd("api")

                        when (response.code) {
                            400 -> Snackbar.make(login_form, R.string.error_not_found_user, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            200 -> UserService.update(perf.edit(), response)
                        }
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
            } else {
                edit_password.error = errors["password"]
                edit_username.error = errors["user"]
            }
        }

        btn_signIn.setOnClickListener {
            SignUpDialog(userApi).show(this@MainActivity, object : SignUpDialog.OnSignUpListener {
                override fun onInvalidQuery(errors: Errors) {
                }

                override fun onComplete() {

                    val intent = buildIntent(TaskActivity::class.java)
                    intent.putExtra("user", "new");
                    startActivity(intent);
                    finish();
                }

                override fun onError(e: Throwable?) {
                }

            })
        }
    }

}
