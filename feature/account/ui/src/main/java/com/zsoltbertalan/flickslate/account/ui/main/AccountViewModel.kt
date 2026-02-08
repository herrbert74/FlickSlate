package com.zsoltbertalan.flickslate.account.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@ViewModelKey(AccountViewModel::class)
@ContributesIntoMap(AppScope::class)
class AccountViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel() {

	private val _loggedInEvent = MutableStateFlow<Account?>(null)
	val loggedInEvent = _loggedInEvent.asSharedFlow()

	init {
		viewModelScope.launch {
			val account = accountRepository.getAccount()
			_loggedInEvent.emit(account)
		}
	}

	fun login(username: String, password: String) {
		viewModelScope.launch {
			accountRepository.login(username, password)
				.onSuccess { account ->
					_loggedInEvent.emit(account)
				}
				.onFailure {
					_loggedInEvent.emit(null)
				}
		}
	}

	fun logout() {
		viewModelScope.launch {
			accountRepository.logout()
			_loggedInEvent.emit(null)
		}
	}

}
