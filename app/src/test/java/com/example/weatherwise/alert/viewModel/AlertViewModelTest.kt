package com.example.weatherwise.alert.viewModel




import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
import com.example.weatherwizard.alert.model.AlertModel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {

    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModel: AlertViewModel


    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = AlertViewModel(repository)

    }


    @Test
    @Config(manifest = Config.NONE)
    fun insertAlarm_validAlarm() = runTest {

        //Given
        val alert = AlertModel(date = "2025-05-01", time = "06:30")

        coEvery { repository.insertAlert(alert) } returns Unit

        //When
        viewModel.insertAlert(alert)
        advanceUntilIdle()

        //Then
        coVerify { repository.insertAlert(alert) }

    }

    @Test
    @Config(manifest = Config.NONE)
    fun deleteAlarm_validAlarm() = runTest {
        //Given
        val alert = AlertModel(date = "2025-05-01", time = "06:30")

        coEvery { repository.deleteAlert(alert) } returns Unit

        //When
        viewModel.deleteAlert(alert)
        advanceUntilIdle()

        //Then
        coVerify { repository.deleteAlert(alert) }

    }



}