package com.zsoltbertalan.flickslate.shared.data.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import retrofit2.Response

inline fun <reified T> successCall(server: MockWebServer, dto: T, apiCall: () -> T): T {
	val json = Json { ignoreUnknownKeys = true }
	val mockResponse = MockResponse(body = json.encodeToJsonElement(dto).toString())
	server.enqueue(mockResponse)
	val data = apiCall()
	server.takeRequest()
	return data
}

inline fun <reified T> failureCall(server: MockWebServer, apiCall: () -> T): T {
	val mockResponse = MockResponse(code = 404, body = "message = \"Client error\"")
	server.enqueue(mockResponse)
	val data = apiCall()
	server.takeRequest()
	return data
}

inline fun <reified T> successResponseCall(server: MockWebServer, dto: T, apiCall: () -> Response<T>): Response<T> {
	val json = Json { ignoreUnknownKeys = true }
	val mockResponse = MockResponse(body = json.encodeToJsonElement(dto).toString())
	server.enqueue(mockResponse)
	val data = apiCall()
	server.takeRequest()
	return data
}

inline fun <reified T> failureResponseCall(server: MockWebServer, apiCall: () -> Response<T>): Response<T> {
	val mockResponse = MockResponse(code = 404, body = "message = \"Client error\"")
	server.enqueue(mockResponse)
	val data = apiCall()
	server.takeRequest()
	return data
}
