package com.flepper.therapeutic.android.presentation.core

import android.util.Log
import androidx.lifecycle.ViewModel
import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.network.ApiResult
import com.flepper.therapeutic.data.usecases.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent




abstract class BaseViewModel : ViewModel(),KoinComponent {
    class OnResultObtained<T>(val result:T?,val isLoaded:Boolean)

    public sealed class  UIResult{
        object InProgress
        class OnError(val readableError:String)
    }

    fun <Output:Any> executeApiCallUseCase(
        viewModelScope:CoroutineScope,
        useCase: BaseUseCase<Unit, FlowResult<Output>>,
        callback: (Output) -> Unit = {},
        onError: (Exception) -> Unit ={}
    ){
        viewModelScope.launch {
            useCase.execute(callback = {
                it.collectLatest {result ->
                    if (result is ApiResult.Success){
                        callback(result.response)
                    }else if(result is ApiResult.GenericError){
                        onError(result.error)
                        Log.e("Error-Result",result.error.toString())
                    }
                }
            }, request = Unit)
        }
    }


    fun <Input,Output:Any> executeApiCallUseCase(
        viewModelScope:CoroutineScope,
        inputValue:Input,
        useCase: BaseUseCase<Input, FlowResult<Output>>,
        callback: (Output) -> Unit = {},
        onError: (Exception) -> Unit ={}
    ){
        viewModelScope.launch {
            useCase.execute(callback = {
                it.collectLatest {result ->
                    if (result is ApiResult.Success){
                        callback(result.response)
                    }else if(result is ApiResult.GenericError){
                        onError(result.error)
                        Log.e("Error-Result",result.error.toString())
                    }
                }
            }, request = inputValue)
        }
    }

    fun <Input,Output:Any> executeLocalUseCase(
        viewModelScope:CoroutineScope,
        inputValue:Input,
        useCase: BaseUseCase<Input, Output>,
        callback: (Output) -> Unit = {},
        onError: (Exception) -> Unit ={}
    ){
        viewModelScope.launch {
            try {
                useCase.execute(callback = {
                    callback(it)
                }, request = inputValue)
            }catch (error:Exception){
                onError(error)
            }

        }
    }

    fun <Input,Output:Any> executeLocalFlowUseCase(
        viewModelScope:CoroutineScope,
        inputValue:Input,
        useCase: BaseUseCase<Input, Flow<Output>>,
        callback: (Output) -> Unit = {},
        onError: (Exception) -> Unit ={}
    ){
        viewModelScope.launch {
            try {
                useCase.execute(callback = {
                    it.collectLatest {result ->
                        callback(result)
                    }
                }, request = inputValue)
            }catch (error:Exception){
                onError(error)
                Log.e("Error-Result",error.toString())
            }

        }
    }

    fun <Output:Any> executeFirebaseUseCase(
        viewModelScope:CoroutineScope,
        useCase: BaseUseCase<Unit, Flow<Output>>,
        state:MutableStateFlow<OnResultObtained<Output>>,
        callback: (Output) -> Unit = {},
        onError: (Exception /* = java.lang.Exception */) -> Unit
    ){
        viewModelScope.launch {
            try {
                useCase.execute(callback = {
                    it.collectLatest {result ->
                        callback(result)
                    }
                }, request = Unit)
            }catch (error:Exception){
                // Set this state here so we prevent setting error result to other params
                state.value = OnResultObtained(null,true)
                Log.e("Error-Result",error.toString())
                onError(error)
            }

        }
    }

    fun <Input,Output:Any> executeFirebaseUseCase(
        viewModelScope:CoroutineScope,
        inputValue:Input,
        useCase: BaseUseCase<Input, Flow<Output>>,
        state:MutableStateFlow<OnResultObtained<Output>>,
        callback: (Output) -> Unit = {},
        onError: (Exception) -> Unit ={}
    ){
        viewModelScope.launch {
            try {
                useCase.execute(callback = {
                    it.collectLatest {result ->
                        callback(result)
                    }
                }, request = inputValue)
            }catch (error:Exception){
                onError(error)
                state.value = OnResultObtained(null,true)
                Log.e("Error-Result",error.toString())
            }

        }
    }


}