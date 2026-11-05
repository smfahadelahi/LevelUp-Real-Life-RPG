package com.novara.levelup.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
@Database(entities=[PlayerEntity::class,QuestEntity::class,HabitEntity::class,HabitCheckInEntity::class],version=3,exportSchema=false)
abstract class LevelUpDatabase:RoomDatabase(){
 abstract fun levelUpDao():LevelUpDao
 companion object {
  private val MIGRATION_1_2=object:Migration(1,2){
   override fun migrate(db:SupportSQLiteDatabase){
    db.execSQL("CREATE TABLE quests_new (id INTEGER NOT NULL,title TEXT NOT NULL,detail TEXT NOT NULL,difficulty TEXT NOT NULL,xpReward INTEGER NOT NULL,lastCompletedDate TEXT,PRIMARY KEY(id))")
    db.execSQL("INSERT INTO quests_new (id,title,detail,difficulty,xpReward,lastCompletedDate) SELECT id,title,detail,difficulty,xpReward,CASE WHEN completed=1 THEN date('now','localtime') ELSE NULL END FROM quests")
    db.execSQL("DROP TABLE quests"); db.execSQL("ALTER TABLE quests_new RENAME TO quests")
    db.execSQL("CREATE TABLE habits_new (id INTEGER NOT NULL,title TEXT NOT NULL,PRIMARY KEY(id))")
    db.execSQL("INSERT INTO habits_new (id,title) SELECT id,title FROM habits")
    db.execSQL("DROP TABLE habits"); db.execSQL("ALTER TABLE habits_new RENAME TO habits")
    db.execSQL("CREATE TABLE IF NOT EXISTS habit_checkins (habitId INTEGER NOT NULL,date TEXT NOT NULL,PRIMARY KEY(habitId,date))")
   }
  }
  private val MIGRATION_2_3=object:Migration(2,3){
   override fun migrate(db:SupportSQLiteDatabase){
    db.execSQL("ALTER TABLE player ADD COLUMN strengthXp INTEGER NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE player ADD COLUMN intelligenceXp INTEGER NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE player ADD COLUMN wellnessXp INTEGER NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE player ADD COLUMN disciplineXp INTEGER NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE player ADD COLUMN creativityXp INTEGER NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE quests ADD COLUMN category TEXT NOT NULL DEFAULT 'DISCIPLINE'")
   }
  }
  @Volatile private var instance:LevelUpDatabase?=null
  fun get(context:Context):LevelUpDatabase=instance ?: synchronized(this){
   instance ?: Room.databaseBuilder(context.applicationContext,LevelUpDatabase::class.java,"levelup.db")
    .addMigrations(MIGRATION_1_2,MIGRATION_2_3).build().also{instance=it}
  }
 }
}
