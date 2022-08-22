package com.flepper.therapeutic.android.presentation.home.euti.authentication

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.IntentSender.SendIntentException
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.flepper.therapeutic.android.BuildConfig
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.home.euti.EutiChatType
import com.flepper.therapeutic.android.presentation.home.euti.EutiScreens
import com.flepper.therapeutic.android.presentation.home.euti.EutiViewModel
import com.flepper.therapeutic.android.presentation.home.euti.MAIN_SHEET
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.OnFailureListener
import kotlinx.coroutines.delay


/** @RegistrationScreen*/
@Composable
fun RegistrationScreen(eutiViewModel: EutiViewModel, navController: NavController) {
    var isSignIn by remember {
        mutableStateOf(eutiViewModel.signInMethod ==  EutiViewModel.SignInMethod.SIGN_IN)
    }

    val signInResponse by eutiViewModel.signInResponse.collectAsState()
    val signUpResponse by eutiViewModel.signUpResponse.collectAsState()
    val signInError by eutiViewModel.eutiGenericError.collectAsState()

    var userName by remember {
        mutableStateOf("")
    }

    var passWord by remember {
        mutableStateOf("")
    }

    var repeatPassword by remember {
        mutableStateOf("")
    }

    var showButtonLoading by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = smallPadding, vertical = smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                mediumPadding
            )
        ) {
            MediumTextBold(
                text = stringResource(id = if (isSignIn) R.string.sign_In else R.string.sign_up),
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(smallPadding)) {
                /** @UserName Text Field*/
                OutLineEdittextLogin(
                    hint = stringResource(id = R.string.email),
                    stroke = BorderStroke(
                        size05dp, gray
                    ),
                    text = userName,
                    modifier = Modifier
                ) { name ->
                    userName = name
                }

                /** @Password Text Field*/
                OutLineEdittextPassword(
                    hint = stringResource(id = R.string.password),
                    stroke = BorderStroke(
                        size05dp, gray
                    ),
                    text = passWord,
                    modifier = Modifier
                ) { name ->
                    passWord = name
                }


                /** @RepeatPassword Text Field*/
                if (!isSignIn) {
                    OutLineEdittextPassword(
                        hint = stringResource(id = R.string.repeat_password),
                        stroke = BorderStroke(
                            size05dp, gray
                        ),
                        text = repeatPassword,
                        modifier = Modifier
                    ) { name ->
                        repeatPassword = name
                    }
                }

                /** @Buttons*/

                Column(
                    modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                        smallPadding
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var isSignUpEnabled by remember {
                        mutableStateOf(false)
                    }

                    isSignUpEnabled =
                        userName.isNotEmpty() && passWord.isNotEmpty() && repeatPassword.isNotEmpty()

                    var isSignInEnabled by remember {
                        mutableStateOf(false)
                    }

                    isSignInEnabled = userName.isNotEmpty() && passWord.isNotEmpty()

                    /** Stop showing progress bar*/
                    LaunchedEffect(key1 = signInResponse.isLoaded || signUpResponse.isLoaded, block = {
                        showButtonLoading = false
                    })


                    RoundedCornerButton(
                        isEnabled = if (!isSignIn) isSignUpEnabled else isSignInEnabled,
                        text = stringResource(id = R.string.continue_button),
                        isLoading = showButtonLoading,
                        modifier = Modifier
                            .padding(bottom = largePadding)
                            .fillMaxWidth()
                    ) {
                        if (isSignIn){
                            eutiViewModel.signInUser(email = userName.trim(),passWord.trim())
                        }else{
                            eutiViewModel.signUpUser(email = userName.trim(),passWord.trim())
                        }
                        showButtonLoading = true
                    }
                    TextButton(onClick = { isSignIn = !isSignIn }) {
                        MediumTextBold(
                            text = stringResource(id = if (!isSignIn) R.string.already_sign_in else R.string.already_sign_up),
                            color = MaterialTheme.colors.primaryVariant, fontWeight = FontWeight.Bold
                        )
                    }
                    MediumTextBold(text = stringResource(id = R.string.or))
                    SignInWithGoogleButton(eutiViewModel,showButtonLoading){
                        showButtonLoading = true
                    }
                }


            }


        }

        fun navigate(){
            navController.popBackStack(MAIN_SHEET,false)
            navController.navigate(EutiScreens.ScheduleSessionDateScreen(eutiViewModel).screenName)
        }

        LaunchedEffect(key1 = signInResponse.isLoaded || signUpResponse.isLoaded, block = {
            if (signInResponse.isLoaded && signInResponse.result != null){
                navigate()
            }
            if (signUpResponse.isLoaded && signUpResponse.result != null){
                navigate()
            }
        })



        /** @ErrorSnackBar*/
        if (signInError.isNotEmpty()){
            showButtonLoading = false
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(smallPadding))
            {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing3dp))
                {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(spacing3dp), horizontalArrangement = Arrangement.spacedBy(
                            smallPadding), verticalAlignment = Alignment.CenterVertically) {
                        MediumTextBold(text = signInError, modifier = Modifier.weight(1f))

                        TextButton(onClick = {
                            eutiViewModel.setSignInError("")
                        }) {
                            MediumTextBold(text = stringResource(id = R.string.ok), color = MaterialTheme.colors.primary)
                        }

                    }
                }
            }
            LaunchedEffect(key1 = true, block = {
                delay(3000)
                eutiViewModel.setSignInError("")
            })
        }

    }



}


/** @ChooseToSingUpOrSignIn*/

@Composable
fun LoginOrSignUpButtonScreen(navController: NavController,eutiViewModel: EutiViewModel){

    val context = LocalContext.current

    Row(modifier = Modifier.padding(smallPadding)) {

        RoundedOutlineButton(
            text = stringResource(R.string.sign_In),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
        ) {
            val chat = EutiChatType.User(context.getString(R.string.sign_In), false).apply {
                this.isHead = eutiViewModel.checkHead(this)
            }
            eutiViewModel.addToReplies(chat)
            eutiViewModel.signInMethod = EutiViewModel.SignInMethod.SIGN_IN
            navController.navigate(EutiScreens.LoginScreenView(eutiViewModel).screenName)
        }
        Spacer(modifier = Modifier.weight(0.04f))
        RoundedCornerButton(
            text = stringResource(R.string.sign_up),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
        ) {
            eutiViewModel.signInMethod = EutiViewModel.SignInMethod.SIGN_UP
            val chat = EutiChatType.User(context.getString(R.string.sign_up), false).apply {
                this.isHead = eutiViewModel.checkHead(this)
            }
            eutiViewModel.addToReplies(chat)
            navController.navigate(EutiScreens.LoginScreenView(eutiViewModel).screenName)
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignInWithGoogleButton(eutiViewModel: EutiViewModel,isLoading:Boolean = false,setShowLoading:(Boolean) -> Unit) {
    val context = LocalContext.current
    val oneTapClient =  Identity.getSignInClient(context)
    val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode != RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(it.data)
        val idToken = googleCredential.googleIdToken
        eutiViewModel.signInWithGoogle(idToken ?: "")

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = smallPadding, vertical = mediumPadding),
        backgroundColor = MaterialTheme.colors.background,
        onClick = {
            setShowLoading(true)
            oneTapClient.beginSignIn(signInWithGoogle())
                .addOnSuccessListener((context as Activity)
                ) { result ->
                    try {
                        Log.d("TAG", "Started")
                        val intent = result.pendingIntent.intentSender
                        signInLauncher.launch(IntentSenderRequest.Builder(intent).build())
                    } catch (e: SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: " + e.message)
                    }
                }
                .addOnFailureListener(
                    context,
                    OnFailureListener { e -> // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        setShowLoading(false)
                        e.printStackTrace()
                        e.message?.let { Log.e("TAG", it) }
                    })
        }
    ) {
        if (true){
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(smallPadding)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(largePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "",
                        modifier = Modifier.size(
                            size24dp, size24dp
                        )
                    )
                    MediumTextBold(text = stringResource(id = R.string.continue_with_google))
                }
            }
        }else{
            Box(modifier = Modifier
                .padding(smallPadding)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

fun signInWithGoogle():BeginSignInRequest{
    return BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(BuildConfig.WEB_CLIENT_API_KEY)
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

}

