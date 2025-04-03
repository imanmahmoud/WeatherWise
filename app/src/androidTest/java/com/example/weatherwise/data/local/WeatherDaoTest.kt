package com.example.weatherwise.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.weatherDao()
    }

    @After
    fun tearDown() = database.close()



    @Test
    fun insertAlert() = runTest {
        //Given
        val alert = AlertModel(date = "2025-02-01", time = "06:30")
        dao.insertAlert(alert)

        //When
        val alerts = dao.getAllAlerts().first()
        val retrievedAlarm = alerts.first()

        //Then

        assertEquals("2025-02-01", retrievedAlarm.date)
        assertEquals("06:30", retrievedAlarm.time)
    }

    @Test
    fun deleteAlert() = runTest {
        //Given
        val alert = AlertModel(date = "2025-02-01", time = "06:30")
        dao.insertAlert(alert)

        //When
        val alertsBeforeDelete = dao.getAllAlerts().first()
        assertTrue(alertsBeforeDelete.isNotEmpty())

        //When
        dao.deleteAlert(alert)
        val alertsAfterDelete = dao.getAllAlerts().first()
        assertFalse(alertsAfterDelete.contains(alert))
    }

}