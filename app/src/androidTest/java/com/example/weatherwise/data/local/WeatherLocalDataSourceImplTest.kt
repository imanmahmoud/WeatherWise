package com.example.weatherwise.data.local



import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceImplTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao
    private lateinit var localDataSource: WeatherLocalDataSourceImpl

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.weatherDao()
        localDataSource = WeatherLocalDataSourceImpl(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }



    @Test
    fun insertAlerts() = runTest {
        //Given
        val alarm = AlertModel(date = "2025-04-01", time = "08:30")
        localDataSource.insertAlert(alarm)

        //When
        val alarms = localDataSource.getAllAlerts().first()
        val retrievedAlarm = alarms.first()

        //Then
        assertTrue(alarms.isNotEmpty())
        assertEquals("2025-04-01", retrievedAlarm.date)
        assertEquals("08:30", retrievedAlarm.time)
    }

    @Test
    fun deleteAlerts() = runTest {
        //Given
        val alert = AlertModel(date = "2025-04-01", time = "08:30")
        localDataSource.insertAlert(alert)

        //When
        val alertsBeforeDelete = localDataSource.getAllAlerts().first()
        val retrievedAlarm = alertsBeforeDelete.first()
        localDataSource.deleteAlert(retrievedAlarm)
        val alertsAfterDelete = localDataSource.getAllAlerts().first()

        //Then
        assertTrue(alertsAfterDelete.isEmpty())
    }
}