package com.example.weatherwise.data.repo


import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwizard.alert.model.AlertModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepositoryImpl
    private val remoteDataSource: WeatherRemoteDataSourceImpl = mockk(relaxed = true)
    private val localDataSource: WeatherLocalDataSourceImpl = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl.getInstance(
            remoteDataSource,
            localDataSource
        ) as WeatherRepositoryImpl
    }

    @Test
    fun insertAlarm() = runTest {

        //Given
        val alert = AlertModel(date = "2025-08-01", time = "02:30")

        //When
        repository.insertAlert(alert)

        //That
        coVerify { localDataSource.insertAlert(alert) }
    }

    @Test
    fun deleteAlarm() = runTest {

        //Given
        val alert = AlertModel(date = "2025-08-01", time = "02:30")

        //When
        repository.deleteAlert(alert)

        //That
        coVerify { localDataSource.deleteAlert(alert) }
    }


}
