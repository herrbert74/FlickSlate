package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginComponent(
	colorScheme: ColorScheme,
	modifier: Modifier = Modifier,
	login: (String, String) -> Unit
) {
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.surface)
			.padding(16.dp)
	) {
		Text("Username", style = TextStyle(fontSize = 18.sp))

		BasicTextField(
			value = username,
			onValueChange = { username = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.background(Color.LightGray, RoundedCornerShape(4.dp))
				.padding(horizontal = 8.dp, vertical = 12.dp),
			textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
		)

		Spacer(modifier = Modifier.height(16.dp))

		Text("Password", style = TextStyle(fontSize = 18.sp))

		BasicTextField(
			value = password,
			onValueChange = { password = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.background(Color.LightGray, RoundedCornerShape(4.dp))
				.padding(horizontal = 8.dp, vertical = 12.dp),
			textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
			visualTransformation = PasswordVisualTransformation()
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(onClick = {
			login(username, password)
		}) {
			Text("Login")
		}
	}
}
