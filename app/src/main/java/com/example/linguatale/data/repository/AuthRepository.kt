package com.example.linguatale.data.repository

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.example.linguatale.data.local.datastore.TokenDataStore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.linguatale.BuildConfig

@Singleton
class AuthRepository @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val context: Context
) {
    private val userPool by lazy {
        CognitoUserPool(
            context,
            BuildConfig.COGNITO_USER_POOL_ID,
            BuildConfig.COGNITO_CLIENT_ID,
            null,
            Regions.EU_CENTRAL_1
        )
    }

    suspend fun login(email: String, password: String): Result<Unit> =
        suspendCoroutine { continuation ->
            val user = userPool.getUser(email)
            user.getSessionInBackground(object : AuthenticationHandler {

                override fun onSuccess(session: CognitoUserSession, device: CognitoDevice?) {
                    // Use a CoroutineScope instead of blocking the thread if needed,
                    // or save async outside the continuation before returning.
                    kotlinx.coroutines.MainScope().run {
                        // Alternatively: resume first or suspend saving before returning result
                    }

                    // Direct token save via DataStore:
                    // Note: runBlocking works, but saving tokens asynchronously after
                    // returning or switching to suspendCancellableCoroutine is cleaner.
                    runBlocking {
                        tokenDataStore.saveTokens(
                            accessToken  = session.idToken.jwtToken,
                            refreshToken = session.refreshToken.token
                        )
                    }
                    continuation.resume(Result.success(Unit))
                }

                override fun getAuthenticationDetails(
                    authenticationContinuation: AuthenticationContinuation,
                    userId: String
                ) {
                    val details = AuthenticationDetails(email, password, null)
                    authenticationContinuation.setAuthenticationDetails(details)
                    authenticationContinuation.continueTask()
                }

                override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
                    // Fail the flow by passing an exception through the main continuation failure
                    continuation.continueTask()
                }

                override fun authenticationChallenge(continuation: ChallengeContinuation) {
                    continuation.continueTask()
                }

                override fun onFailure(exception: Exception) {
                    continuation.resume(Result.failure(exception))
                }
            })
        }

    suspend fun register(email: String, password: String): Result<Unit> =
        suspendCoroutine { continuation ->
            val attributes = CognitoUserAttributes().apply {
                addAttribute("email", email)
            }

            userPool.signUpInBackground(
                email,
                password,
                attributes,
                null,
                object : SignUpHandler {
                    override fun onSuccess(user: CognitoUser, signUpResult: SignUpResult) {
                        continuation.resume(Result.success(Unit))
                    }
                    override fun onFailure(exception: Exception) {
                        continuation.resume(Result.failure(exception))
                    }
                }
            )
        }

    suspend fun confirmRegistration(email: String, code: String): Result<Unit> =
        suspendCoroutine { continuation ->
            userPool.getUser(email).confirmSignUpInBackground(
                code, true,
                object : GenericHandler {
                    override fun onSuccess() {
                        continuation.resume(Result.success(Unit))
                    }
                    override fun onFailure(exception: Exception) {
                        continuation.resume(Result.failure(exception))
                    }
                }
            )
        }

    suspend fun logout() {
        userPool.currentUser?.signOut()
        tokenDataStore.clear()
    }

    fun isLoggedIn(): Flow<Boolean> =
        tokenDataStore.accessToken.map { it != null }
}