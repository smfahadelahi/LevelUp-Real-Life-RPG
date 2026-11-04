package com.novara.levelup.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novara.levelup.domain.*
private val Bg=Color(0xFF0B1020); private val Panel=Color(0xFF151C30); private val Muted=Color(0xFF9AA5BD); private val Purple=Color(0xFF9B7BFF); private val Gold=Color(0xFFFFC857); private val Cyan=Color(0xFF55D6D2)
@Composable fun LevelUpApp() {
 var player by remember { mutableStateOf(PlayerProgress()) }; var tab by remember { mutableIntStateOf(0) }
 var quests by remember { mutableStateOf(listOf(Quest(1,"Morning movement","Move your body for 20 minutes",QuestDifficulty.NORMAL),Quest(2,"Read 10 pages","Invest in your intelligence",QuestDifficulty.EASY),Quest(3,"Deep work session","Focus for 45 minutes",QuestDifficulty.HARD),Quest(4,"Plan tomorrow","Write your top three priorities",QuestDifficulty.EASY))) }
 fun claim(id:Int) { val q=quests.firstOrNull{it.id==id && !it.completed} ?: return; player=Progression.award(player,q); quests=quests.map{if(it.id==id) it.copy(completed=true) else it} }
 Scaffold(containerColor=Bg,bottomBar={ NavigationBar(containerColor=Panel) { listOf(Icons.Default.Home,Icons.Default.Bolt,Icons.Default.Person,Icons.Default.CheckCircle).forEachIndexed{i,ic-> NavigationBarItem(selected=tab==i,onClick={tab=i},icon={Icon(ic,null)},label={Text(listOf("Home","Quests","Hero","Habits")[i])})} } }) { pad ->
 when(tab) {
  0 -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
   item { Text("LEVELUP",color=Purple,fontWeight=FontWeight.Black,letterSpacing=3.sp); Text("Good day, Player.",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold); Text("Every small win moves you forward.",color=Muted) }
   item { LevelCard(player) }
   item { Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("Today's quests",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold);Text("${quests.count{it.completed}}/${quests.size} DONE",color=Cyan)} }
   items(quests){QuestItem(it,::claim)}
  }
  1 -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){item{Text("Your quests",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold);Text("Choose a mission. Earn XP.",color=Muted)};items(quests){QuestItem(it,::claim)}}
  2 -> Column(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){Text("Your character",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold);LevelCard(player);Stat("Strength",player.strength);Stat("Intelligence",player.intelligence);Stat("Wellness",player.wellness);Stat("Discipline",player.discipline);Stat("Creativity",player.creativity)}
  else -> Column(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){Text("Daily habits",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold);Text("Build a streak one day at a time.",color=Muted);listOf("Drink enough water","Read every day","Move for 20 minutes","Sleep on time").forEach{var done by remember{mutableStateOf(false)};Row(Modifier.fillMaxWidth().background(Panel,RoundedCornerShape(16.dp)).padding(12.dp),verticalAlignment=Alignment.CenterVertically){Checkbox(done,{done=it});Text(it,color=Color.White)}};Text("Persistent habit history is planned for the next milestone.",color=Muted,fontSize=12.sp)}
 }
 } }
}
@Composable private fun LevelCard(p:PlayerProgress){Column(Modifier.fillMaxWidth().background(Panel,RoundedCornerShape(22.dp)).padding(20.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Column{Text("CURRENT RANK",color=Muted,fontSize=11.sp);Text("LEVEL ${p.level}",color=Color.White,fontSize=30.sp,fontWeight=FontWeight.Black)};Icon(Icons.Default.EmojiEvents,null,tint=Gold,modifier=Modifier.size(34.dp))};Spacer(Modifier.height(16.dp));Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("${p.xpIntoLevel} / 100 XP",color=Color.White);Text("${p.xpToNextLevel} XP TO LEVEL UP",color=Muted,fontSize=10.sp)};Spacer(Modifier.height(8.dp));LinearProgressIndicator(progress={p.xpIntoLevel/100f},modifier=Modifier.fillMaxWidth().height(8.dp),color=Gold,trackColor=Color(0xFF303A52));Text("${p.totalXp} total XP",color=Muted,fontSize=12.sp,modifier=Modifier.padding(top=10.dp))}}
@Composable private fun QuestItem(q:Quest,claim:(Int)->Unit){Row(Modifier.fillMaxWidth().background(Panel,RoundedCornerShape(16.dp)).padding(15.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(q.title,color=Color.White,fontWeight=FontWeight.Bold);Text(q.detail,color=Muted,fontSize=12.sp);Text("+${q.difficulty.xp} XP · ${q.difficulty.name}",color=Gold,fontSize=11.sp)};if(q.completed) Icon(Icons.Default.CheckCircle,"Done",tint=Cyan) else Button(onClick={ {claim(q.id)} },colors=ButtonDefaults.buttonColors(containerColor=Purple)){Text("Claim")}}}
@Composable private fun Stat(name:String,value:Int){Column(Modifier.fillMaxWidth().background(Panel,RoundedCornerShape(14.dp)).padding(14.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(name,color=Color.White);Text("LV. $value",color=Cyan)};LinearProgressIndicator(progress={(value/10f).coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth().padding(top=7.dp),color=Purple)}}
