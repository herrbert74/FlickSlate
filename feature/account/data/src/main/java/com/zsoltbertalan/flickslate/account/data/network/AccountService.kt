package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.account.data.network.model.AccountDetailsReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.CreateRequestTokenReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.CreateSessionReplyDto
import com.zsoltbertalan.flickslate.account.data.network.model.DeleteSessionReplyDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

private const val URL_CREATE_REQUEST_TOKEN = "authentication/token/new"
private const val URL_VALIDATE_REQUEST_TOKEN_WITH_LOGIN = "authentication/token/validate_with_login"
private const val URL_CREATE_SESSION = "authentication/session/new"
private const val URL_DELETE_SESSION = "authentication/session"
private const val URL_GET_ACCOUNT_DETAILS = "account"

/**
 * As of December 2024, the API reference for login with password is wrong here:
 * https://developer.themoviedb.org/reference/authentication-how-do-i-generate-a-session-id
 *
 * But inspecting this example gives you the correct login flow:
 * http://dev.travisbell.com/play/v3_auth_password.html
 *
 * The login replaces not the third, but the second step in the process, and you still have to create the session,
 * because the second step is only validation, not the session creation.
 */
internal interface AccountService {

	@GET(URL_CREATE_REQUEST_TOKEN)
	suspend fun createRequestToken(): CreateRequestTokenReplyDto

	@POST(URL_VALIDATE_REQUEST_TOKEN_WITH_LOGIN)
	suspend fun validateRequestTokenWithLogin(
		@Body requestBody: RequestBody
	): CreateRequestTokenReplyDto

	@GET(URL_CREATE_SESSION)
	suspend fun createSession(
		@Query("request_token") requestToken: String,
	): CreateSessionReplyDto

	@HTTP(method = "DELETE", path = URL_DELETE_SESSION, hasBody = true)
	suspend fun deleteSession(
		@Body requestBody: RequestBody
	): DeleteSessionReplyDto

	@GET(URL_GET_ACCOUNT_DETAILS)
	suspend fun getAccountDetails(
		@Query("session_id") sessionToken: String,
	): AccountDetailsReplyDto

}
