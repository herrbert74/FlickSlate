package com.zsoltbertalan.flickslate.account.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.api.AccountRepository
import com.zsoltbertalan.flickslate.shared.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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
			val loginResult = accountRepository.login(username, password)
			if (loginResult.isOk) {
				_loggedInEvent.emit(loginResult.value)
			} else {
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
