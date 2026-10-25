package com.marcelapaz.mpazpr3

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/*
  Regla de JUnit que reemplaza el dispatcher principal de Android (Dispatchers.Main) por uno de prueba.
  Los ViewModel usan viewModelScope, que necesita Dispatchers.Main, y ese no existe en pruebas de JVM.
*/
@OptIn(ExperimentalCoroutinesApi::class)
class ReglaDispatcherPrincipal(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}
