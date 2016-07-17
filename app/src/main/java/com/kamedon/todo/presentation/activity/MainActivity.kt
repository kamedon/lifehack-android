package com.kamedon.todo.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.kamedon.todo.BuildConfig
import com.kamedon.todo.R
import com.kamedon.todo.domain.entity.api.Errors
import com.kamedon.todo.domain.entity.api.LoginUserQuery
import com.kamedon.todo.domain.entity.api.NewUserResponse
import com.kamedon.todo.domain.usecase.user.LoginUseCase
import com.kamedon.todo.domain.usecase.user.UserRegisterUserCase
import com.kamedon.todo.domain.value.login.LoginType
import com.kamedon.todo.domain.value.page.Page
import com.kamedon.todo.presentation.dialog.SignUpDialog
import com.kamedon.todo.util.WidgetUtil
import com.kamedon.todo.util.extension.go
import com.kamedon.todo.util.extension.observable
import com.kamedon.todo.util.logd
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by kamedon on 2/29/16.
 */
class MainActivity : BaseActivity() {

    @Inject lateinit var loginUseCame: LoginUseCase
    @Inject lateinit var userRegisterUserCase: UserRegisterUserCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)

        if (loginUseCame.isLogined()) {
            startActivity(Intent(applicationContext, TaskActivity::class.java));
            finish();
            return
        }
        setContentView(R.layout.activity_main);
        supportActionBar?.title = "${getString(R.string.app_name)}_${BuildConfig.VERSION_NAME}"
        btn_login.setOnClickListener {
            val query = LoginUserQuery(edit_username.text.toString(), edit_password.text.toString());
            val errors = query.valid(resources)
            if (errors.isEmpty()) {

                observable(loginUseCame.login(query), object : Subscriber<NewUserResponse>() {
                    override fun onCompleted() {
                        if (loginUseCame.isLogined()) {
                            go(Page.TASK_ALL) {
                                it.putExtra(LoginType.key(), LoginType.LOGIN);
                            }
                            finish()
                        }

                    }

                    override fun onNext(response: NewUserResponse) {
                        response.message.logd("api")

                        when (response.code) {
                            400 -> Snackbar.make(login_form, R.string.error_not_found_user, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            200 -> {
                                loginUseCame.login(response)
                                WidgetUtil.update(applicationContext)
                            }

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
            SignUpDialog(userRegisterUserCase).show(this@MainActivity, object : SignUpDialog.OnSignUpListener {
                override fun onInvalidQuery(errors: Errors) {
                }

                override fun onComplete() {
                    go(Page.TASK_ALL) {
                        it.putExtra(LoginType.key(), LoginType.NEW);
                    }
                    finish()
                }

                override fun onError(e: Throwable?) {
                }

            })
        }
    }

}
