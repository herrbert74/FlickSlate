package com.zsoltbertalan.flickslate.account.data.local

import android.content.Context
import androidx.core.content.edit
import com.zsoltbertalan.flickslate.account.data.api.AccountDataSource
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class AccountLocalDataSource @Inject constructor(
	@param:ApplicationContext val context: Context
) : AccountDataSource.Local {

	override fun saveAccessToken(accessToken: String) {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		sharedPreferences.edit { putString("access_token", accessToken) }
	}

	override fun getAccessToken(): String? {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		return sharedPreferences.getString("access_token", null)
	}

	override fun deleteAccessToken() {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		sharedPreferences.edit { remove("access_token") }
	}

	override fun saveAccount(account: Account) {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		sharedPreferences.edit { putString("account_display_name", account.displayName) }
		sharedPreferences.edit { putString("account_name", account.username) }
		sharedPreferences.edit { putString("account_id", account.id.toString()) }
		sharedPreferences.edit { putString("account_language", account.language) }
		sharedPreferences.edit { putBoolean("account_include_adult", account.includeAdult) }
	}

	override fun getAccount(): Account? {
		val sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
		val accountName = sharedPreferences.getString("account_name", null) ?: return null
		val displayName = sharedPreferences.getString("account_display_name", null) ?: accountName
		val language = sharedPreferences.getString("account_language", null) ?: "en-US"
		val id = sharedPreferences.getString("account_id", null)?.toIntOrNull() ?: 0
		val includeAdult = sharedPreferences.getBoolean("account_include_adult", false)
		return Account(displayName, accountName, language, id, includeAdult)
	}

}
