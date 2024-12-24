package com.zsoltbertalan.flickslate.account.data.local

import android.content.Context
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.shared.model.Account
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
class AccountLocalDataSource @Inject constructor(
	@ApplicationContext val context: Context
) : AccountDataSource.Local {

	override fun saveAccessToken(accessToken: String) {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		sharedPreferences.edit().putString("access_token", accessToken).apply()
	}

	override fun getAccessToken(): String? {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		return sharedPreferences.getString("access_token", null)
	}

	override fun saveAccount(account: Account) {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		sharedPreferences.edit().putString("account_name", account.name).apply()
	}

	override fun getAccount(): Account? {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		return sharedPreferences.getString("account_name", null)?.let { Account(it) }
	}

}
