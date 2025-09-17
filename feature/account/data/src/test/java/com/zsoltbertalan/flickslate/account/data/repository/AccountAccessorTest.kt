package com.zsoltbertalan.flickslate.account.data.repository

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.movies.domain.model.AccountMother
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AccountAccessorTest {

	private val accountLocalDataSource: AccountDataSource.Local = mockk()
	private val accountRemoteDataSource: AccountDataSource.Remote = mockk()

	private lateinit var accountAccessor: AccountAccessor

	@Before
	fun setup() {
		coEvery { accountLocalDataSource.getAccount() } returns null
		coEvery { accountLocalDataSource.getAccessToken() } returns ""
		coEvery { accountLocalDataSource.saveAccount(any()) } returns Unit
		coEvery { accountLocalDataSource.saveAccessToken(any()) } returns Unit
		coEvery { accountRemoteDataSource.createSessionId(any(), any()) } returns Ok(
			"session123abc"
		)
		coEvery { accountRemoteDataSource.getAccountDetails(any()) } returns Ok(
			AccountMother.createAccount()
		)
		coEvery { accountRemoteDataSource.deleteSessionId(any()) } returns Ok(
			true
		)
		accountAccessor = AccountAccessor(
			accountRemoteDataSource,
			accountLocalDataSource,
		)
	}

	@Test
	fun `when login called then returns correct account`() = runTest {
		val accountOutcome = accountAccessor.login("john.doe", "password123")
		val account = AccountMother.createAccount()
		accountOutcome.value shouldBeEqual account
	}

	@Test
	fun `when getAccount called then returns correct account`() = runTest {
		val accountOutcome = accountAccessor.getAccount()
		val account = AccountMother.createAccount()
		if (accountOutcome != null) {
			accountOutcome shouldBeEqual account
		}
	}

	@Test
	fun `when logout called then session is deleted`() = runTest {
		accountAccessor.logout()
		coVerify { accountRemoteDataSource.deleteSessionId(any()) }
	}

}
