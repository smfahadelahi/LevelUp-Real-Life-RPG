package com.novara.levelup.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="player") data class PlayerEntity(@PrimaryKey val id:Int=1,val totalXp:Int=0)
@Entity(tableName="quests") data class QuestEntity(@PrimaryKey val id:Int,val title:String,val detail:String,val difficulty:String,val xpReward:Int,val completed:Boolean=false)
@Entity(tableName="habits") data class HabitEntity(@PrimaryKey val id:Int,val title:String,val completedToday:Boolean=false)
