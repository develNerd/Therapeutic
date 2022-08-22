package com.flepper.therapeutic.data.network

import co.touchlab.kermit.Logger
import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.appBaseUrl
import com.flepper.therapeutic.data.apppreference.AppPreference
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import kotlinx.serialization.json.Json

class SquareHttpClient(appPreference: AppPreference) {
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

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(appPreference.accessToken, appPreference.refreshToken)
                }
            }
        }

        followRedirects = false

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
            acceptContentTypes = listOf(ContentType.Application.Json)
        }

        install(Logging) {
            logger = object : io.ktor.client.features.logging.Logger {
                override fun log(message: String) {
                    Logger.v("DNG-Networking") { message }
                }
            }
            level = LogLevel.ALL
        }

    }

    suspend inline fun <reified T : Any> GET(
        route: String,
        queryPair:List<Pair<String,String>>? = null,
    ): T = httpClient.get {
        url {
            protocol = URLProtocol.HTTPS
            path(route)
            queryPair?.forEach { pair ->
                parameters.append(pair.first,pair.second)
            }
        }
    }

    suspend inline fun <reified T : Any, reified Output:Any> POST(
        route: String,
        request: T? = null,
    ): Output = httpClient.post {
        url {
            protocol = URLProtocol.HTTPS
            path(route)
            if (request!= null){
                body = request
            }
        }
    }


}