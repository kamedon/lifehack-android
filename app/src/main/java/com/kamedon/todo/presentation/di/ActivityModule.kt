package com.kamedon.todo.presentation.di

import android.app.Activity
import com.kamedon.todo.domain.api.TodoApi
import com.kamedon.todo.domain.usecase.user.LoginUseCase
import com.kamedon.todo.domain.usecase.user.LogoutUserCase
import com.kamedon.todo.domain.usecase.user.UserRegisterUserCase
import com.kamedon.todo.infra.repository.UserRepository
import dagger.Module
import dagger.Provides

/**
 * Created by kamedon on 4/3/16.
 */
@Module
class ActivityModule(val activity: Activity) {

    @Provides
    @ActivityScope
    fun provideLoginUseCase(userApi: TodoApi.UserApi, userRepository: UserRepository) = LoginUseCase(userApi, userRepository)

    @Provides
    @ActivityScope
    fun provideLogoutUserCase(userRepository: UserRepository) = LogoutUserCase(userRepository)

    @Provides
    @ActivityScope
    fun provideUserRegisterUseCase(userApi: TodoApi.UserApi, userRepository: UserRepository) = UserRegisterUserCase(userApi, userRepository)


}
