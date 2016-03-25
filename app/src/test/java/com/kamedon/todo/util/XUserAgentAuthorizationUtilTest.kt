package com.kamedon.todo.util


import com.kamedon.todo.BuildConfig
import junit.framework.Assert
import okhttp3.internal.Util
import org.junit.Test

/**
 * Created by kamedon on 3/18/16.
 */
class XUserAgentAuthorizationUtilTest {

    @Test
    fun testToken1() {
        Assert.assertEquals(Util.md5Hex(BuildConfig.X_USER_AGENT_AUTHORIZATION_TOKEN), XUserAgentAuthorizationUtil.token())
    }

    @Test
    fun testToken() {
        Assert.assertEquals(Util.md5Hex("test"), XUserAgentAuthorizationUtil.token("test") );
    }

}