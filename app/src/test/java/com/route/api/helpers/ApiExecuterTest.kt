package com.route.api.helpers

import com.google.gson.Gson
import com.route.domain.data.Resource
import com.route.domain.data.ServerException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

// Error bodies below have the shapes the Route API returns
class ApiExecuterTest {

    private val gson = Gson()

    @Test
    fun success_emitsLoadingThenConvertedData() = runBlocking {
        val results = safeApiCallWithoutEnvelope(
            gson = gson,
            api = { "token" },
            converter = { body -> body.length },
        ).toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertEquals(Resource.Success(5), results[1])
    }

    @Test
    fun httpError_withMessage_becomesServerExceptionWithThatMessage() = runBlocking {
        val error = callFailingWith(401, """{"statusMsg":"fail","message":"Incorrect email or password"}""")

        assertEquals("Incorrect email or password", error.serverMessage)
    }

    @Test
    fun httpError_validationErrors_useTheirMsgInsteadOfFail() = runBlocking {
        val error = callFailingWith(
            400,
            """{"message":"fail","errors":{"value":"ahmed@","msg":"Invalid email ","param":"email","location":"body"}}""",
        )

        assertEquals("Invalid email", error.serverMessage)
    }

    @Test
    fun httpError_onlyFail_hasNoMessage() = runBlocking {
        val error = callFailingWith(400, """{"message":"fail"}""")

        assertNull(error.serverMessage)
    }

    @Test
    fun httpError_bodyIsNotJson_hasNoMessage() = runBlocking {
        val error = callFailingWith(502, "<html>Bad Gateway</html>")

        assertNull(error.serverMessage)
        assertTrue(error.cause is HttpException)
    }

    @Test
    fun networkError_isPassedOnUnchanged() = runBlocking {
        val failure = IOException("no network")

        val result = safeApiCallWithoutEnvelope<String, String>(
            gson = gson,
            api = { throw failure },
            converter = { body -> body },
        ).toList().last()

        assertSame(failure, (result as Resource.Error).throwable)
    }

    private suspend fun callFailingWith(code: Int, errorBody: String): ServerException {
        val httpError = HttpException(
            Response.error<String>(code, errorBody.toResponseBody("application/json".toMediaType()))
        )
        val result = safeApiCallWithoutEnvelope<String, String>(
            gson = gson,
            api = { throw httpError },
            converter = { body -> body },
        ).toList().last()
        return (result as Resource.Error).throwable as ServerException
    }
}
