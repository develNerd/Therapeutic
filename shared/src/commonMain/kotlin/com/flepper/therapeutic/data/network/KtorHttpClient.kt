package com.flepper.therapeutic.data.network

import com.flepper.therapeutic.data.appBaseUrl
import com.flepper.therapeutic.data.apppreference.AppPreference
import io.ktor.client.HttpClient
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

class KtorHttpClient(appPreference: AppPreference) {
    private val TIMEOUT_DEFAULT_CONFIGURATION = 30_000L

    val httpClient
         = HttpClient {
            defaultRequest {
                install(HttpTimeout)
                host = appBaseUrl
                followRedirects = false
                expectSuccess = true

                header("Content-Type", "application/json")
                header("Accept", "application/json")
                timeout { requestTimeoutMillis = TIMEOUT_DEFAULT_CONFIGURATION }
            }

            followRedirects = false

            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
                acceptContentTypes = listOf(ContentType.Any)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.v("DNG-Networking") { message }
                    }
                }
                level = LogLevel.ALL
            }

        }

    suspend inline fun <reified T : Any> GET(
        route: String,
        queryPair:List<Pair<String,String>>? = null,
        isAuthorizationRequired:Boolean = true,
    ): T = httpClient.get {

        addAuthenticationIfRequired(isAuthorizationRequired)
        url {
            protocol = URLProtocol.HTTPS
            path(route)
            queryPair?.forEach { pair ->
                parameters.append(pair.first,pair.second)
            }

        }
    }

    fun HttpRequestBuilder.addAuthenticationIfRequired(isAuthorizationRequired: Boolean) {
        if (isAuthorizationRequired) {
            header("AUTHORIZATION_TOKEN_HEADER_KEY", "")
        }
    }
}