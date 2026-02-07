package com.zsoltbertalan.flickslate.account.data.network

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.account.data.network.model.toAccount
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class AccountRemoteDataSource @Inject internal constructor(
	private val accountService: AccountService
) : AccountDataSource.Remote {

	override suspend fun createSessionId(username: String, password: String): Outcome<String> {
		return runCatchingApi {
			accountService.createRequestToken()
		}.andThen { createRequestTokenReplyDto ->

			runCatchingApi {
				accountService.validateRequestTokenWithLogin(
					createValidateRequestTokenWithLoginRequestBody(
						username,
						password,
						createRequestTokenReplyDto.request_token
					)
				)
			}
		}.andThen { createRequestTokenReplyDto ->
			runCatchingApi {
				accountService.createSession(createRequestTokenReplyDto.request_token).session_id
			}
		}
	}

	override suspend fun deleteSessionId(sessionId: String): Outcome<Boolean> {
		return runCatchingApi {
			accountService.deleteSession(createDeleteSessionRequestBody(sessionId))
		}.map { it.success }
	}

	override suspend fun getAccountDetails(sessionToken: String): Outcome<Account> {
		return runCatchingApi {
			accountService.getAccountDetails(sessionToken)
		}.map { accountDetailsReplyDto ->
			accountDetailsReplyDto.toAccount()
		}
	}

}

private fun createValidateRequestTokenWithLoginRequestBody(
	username: String,
	password: String,
	requestToken: String,
): RequestBody {
	val body = buildJsonObject {
		put("username", username)
		put("password", password)
		put("request_token", requestToken)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}

private fun createDeleteSessionRequestBody(
	sessionId: String,
): RequestBody {
	val body = buildJsonObject {
		put("session_id", sessionId)
	}
	return body.toString().toRequestBody("application/json; charset=UTF-8".toMediaType())
}
