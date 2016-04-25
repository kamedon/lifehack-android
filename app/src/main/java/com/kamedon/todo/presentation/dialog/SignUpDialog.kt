package com.kamedon.todo.presentation.dialog

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.kamedon.todo.R
import com.kamedon.todo.domain.entity.api.Errors
import com.kamedon.todo.domain.entity.api.NewUserQuery
import com.kamedon.todo.domain.entity.api.NewUserResponse
import com.kamedon.todo.domain.usecase.user.UserRegisterUserCase
import com.kamedon.todo.util.extension.observable
import com.kamedon.todo.util.setupCrashlytics
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import rx.Subscriber

/**
 * Created by h_kamei on 2016/03/02.
 */
class SignUpDialog(val userRegisterUserCase: UserRegisterUserCase) {
    fun show(activity: RxAppCompatActivity, onSignUpListener: OnSignUpListener?) {
        var view = activity.layoutInflater.inflate(R.layout.dialog_sign_up, null)
        val edit_username = view.findViewById(R.id.edit_username) as EditText
        val edit_email = view.findViewById(R.id.edit_email) as EditText
        val edit_password = view.findViewById(R.id.edit_password) as EditText
        val edit_password_confirm = view.findViewById(R.id.edit_password_confirm) as EditText

        edit_password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                edit_password_confirm.error = if (equalPassword(text.toString(), edit_password_confirm.text.toString())) {
                    null
                } else {
                    activity.getString(R.string.error_confirm_password)
                }
            }
        })
        edit_password_confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                edit_password_confirm.error = if (equalPassword(text.toString(), edit_password.text.toString())) {
                    null
                } else {
                    activity.getString(R.string.error_confirm_password)
                }
            }

        })


        val dialog = AlertDialog.Builder(activity)
                .setTitle(R.string.title_sign_up)
                .setView(view)
                .setPositiveButton(R.string.action_sign_up, null)
                .setNegativeButton(android.R.string.cancel, null)
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            if (equalPassword(edit_password.text.toString(), edit_password_confirm.text.toString())) {
                val query = NewUserQuery(edit_username.text.toString(), edit_email.text.toString(), edit_password.text.toString());
                val error = query.valid(activity.resources)
                if (error.isEmpty()) {
                    activity.observable(userRegisterUserCase.signUp(query), object : Subscriber<NewUserResponse>() {
                        override fun onCompleted() {
                            if (userRegisterUserCase.isLogined()) {
                                onSignUpListener?.onComplete()
                            }
                        }

                        override fun onNext(response: NewUserResponse) {
                            when (response.code) {
                                400 -> {
                                    val errors = response.errors;
                                    edit_email.error = errors.email?.let { it.errors[0].toString() }
                                    edit_username.error = errors.username?.let { it.errors[0].toString() }
                                    edit_password.error = errors.plainPassword?.let { it.errors[0].toString() }
                                }
                                201 -> {
                                    userRegisterUserCase.complate(response)
                                    response.user.setupCrashlytics()
                                }

                            }
                        }


                        override fun onError(e: Throwable?) {
                            onSignUpListener?.onError(e)
                        }
                    }) ;
                } else {
                    edit_username.error = error["username"]
                    edit_password.error = error["plainPassword"]
                    edit_email.error = error["email"]
                }
            } else {
                edit_password_confirm.error = activity.getString(R.string.error_confirm_password)
            }

        }
    }

    interface OnSignUpListener {
        fun onError(e: Throwable?)
        fun onComplete()
        fun onInvalidQuery(errors: Errors)
    }

    fun equalPassword(p0: String, p1: String) = p0.equals(p1)

}