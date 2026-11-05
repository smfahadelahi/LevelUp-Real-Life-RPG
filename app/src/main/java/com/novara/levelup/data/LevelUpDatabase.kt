package com.novara.levelup.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities=[PlayerEntity::class,QuestEntity::class,HabitEntity::class],version=1,exportSchema=false)
abstract class LevelUpDatabase:RoomDatabase(){
 abstract fun levelUpDao():LevelUpDao
 companion object {
  @Volatile private var instance:LevelUpDatabase?=null
  fun get(context:Context):LevelUpDatabase = instance ?: synchronized(this) {
   instance ?: Room.databaseBuilder(context.applicationContext,LevelUpDatabase::class.java,"levelup.db").build().also{instance=it}
  }
 }
}
