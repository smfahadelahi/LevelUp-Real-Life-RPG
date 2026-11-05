package com.novara.levelup.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LevelUpDaoTest {
    private lateinit var database: LevelUpDatabase
    private lateinit var dao: LevelUpDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LevelUpDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.levelUpDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun questRewardCanOnlyBeClaimedOncePerDay() = runBlocking {
        dao.insertPlayer(PlayerEntity())
        dao.insertQuests(listOf(QuestEntity(1, "Move", "Walk", "NORMAL", "STRENGTH", 25)))

        assertTrue(dao.claimQuest(1, "2026-11-05"))
        assertFalse(dao.claimQuest(1, "2026-11-05"))
        assertTrue(dao.claimQuest(1, "2026-11-06"))

        val player = dao.observePlayer().first()
        assertEquals(50, player?.totalXp)
        assertEquals(50, player?.strengthXp)
    }

    @Test
    fun habitCheckInsAreDateSpecificAndCountedForTheWeek() = runBlocking {
        dao.insertHabits(listOf(HabitEntity(1, "Read")))
        dao.setHabitCompleted(1, true, "2026-11-05")

        val today = dao.observeHabits("2026-11-05").first().single()
        val tomorrow = dao.observeHabits("2026-11-06").first().single()

        assertTrue(today.completedToday)
        assertEquals(1, today.checkInsLast7Days)
        assertFalse(tomorrow.completedToday)
        assertEquals(1, tomorrow.checkInsLast7Days)
    }
}
