package com.novara.levelup.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PlayerEntity::class, QuestEntity::class, HabitEntity::class, HabitCheckInEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun levelUpDao(): LevelUpDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE quests ADD COLUMN lastCompletedDate TEXT")
                db.execSQL("CREATE TABLE IF NOT EXISTS habit_checkins (habitId INTEGER NOT NULL, date TEXT NOT NULL, PRIMARY KEY(habitId, date))")
            }
        }

        @Volatile private var instance: LevelUpDatabase? = null

        fun get(context: Context): LevelUpDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                LevelUpDatabase::class.java,
                "levelup.db"
            ).addMigrations(MIGRATION_1_2).build().also { instance = it }
        }
    }
}
