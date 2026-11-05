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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.novara.levelup.data.LevelUpDatabase
import com.novara.levelup.data.LevelUpRepository
import com.novara.levelup.data.QuestEntity
import com.novara.levelup.data.HabitEntity
import com.novara.levelup.data.PlayerEntity

private val Bg=Color(0xFF0B1020)
private val Panel=Color(0xFF151C30)
private val Muted=Color(0xFF9AA5BD)
private val Purple=Color(0xFF9B7BFF)
private val Gold=Color(0xFFFFC857)
private val Cyan=Color(0xFF55D6D2)

@Composable
fun LevelUpApp() {
    val context = LocalContext.current
    val factory = remember(context) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = LevelUpDatabase.get(context).levelUpDao()
                return LevelUpViewModel(LevelUpRepository(dao)) as T
            }
        }
    }
    val vm: LevelUpViewModel = viewModel(factory = factory)
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Bg,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                val icons = listOf(Icons.Default.Home, Icons.Default.Bolt, Icons.Default.Person, Icons.Default.CheckCircle)
                val labels = listOf("Home", "Quests", "Hero", "Habits")
                icons.forEachIndexed { index, icon ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(icon, contentDescription = labels[index]) },
                        label = { Text(labels[index]) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Purple,
                            indicatorColor = Color(0xFF30264D)
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (tab) {
            0 -> HomeScreen(state.player, state.quests, padding, vm::claimQuest)
            1 -> QuestScreen(state.quests, padding, vm::claimQuest)
            2 -> CharacterScreen(state.player, padding)
            else -> HabitScreen(state.habits, padding, vm::setHabit)
        }
    }
}

@Composable
private fun HomeScreen(player: PlayerEntity, quests: List<QuestEntity>, padding: PaddingValues, claim: (Int) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("LEVELUP", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            Text("Good day, Player.", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text("Every small win moves you forward.", color = Muted, fontSize = 14.sp)
        }
        item { LevelCard(player) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Today's quests", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${quests.count { it.completed }}/${quests.size} DONE", color = Cyan, fontSize = 11.sp)
            }
        }
        items(quests, key = { it.id }) { QuestCard(it, claim) }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun QuestScreen(quests: List<QuestEntity>, padding: PaddingValues, claim: (Int) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Your quests", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text("Choose a mission. Earn your XP.", color = Muted)
        }
        items(quests, key = { it.id }) { QuestCard(it, claim) }
    }
}

@Composable
private fun LevelCard(player: PlayerEntity) {
    val level = player.totalXp / 100 + 1
    val xp = player.totalXp % 100
    Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(22.dp)).padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("CURRENT RANK", color = Muted, fontSize = 11.sp, letterSpacing = 1.5.sp)
                Text("LEVEL $level", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
            }
            Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("$xp / 100 XP", color = Color.White, fontWeight = FontWeight.SemiBold)
            Text("${100-xp} XP TO LEVEL UP", color = Muted, fontSize = 10.sp)
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { xp / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = Gold,
            trackColor = Color(0xFF303A52)
        )
        Text("${player.totalXp} total XP earned", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun QuestCard(quest: QuestEntity, claim: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(17.dp)).padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(quest.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(quest.detail, color = Muted, fontSize = 12.sp)
            Text("+${quest.xpReward} XP · ${quest.difficulty}", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(8.dp))
        if (quest.completed) {
            Icon(Icons.Default.CheckCircle, "Completed", tint = Cyan)
        } else {
            Button(onClick = { claim(quest.id) }, colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
                Text("Claim")
            }
        }
    }
}

@Composable
private fun CharacterScreen(player: PlayerEntity, padding: PaddingValues) {
    val level = player.totalXp / 100 + 1
    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Text("Your character", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Your real-world skills become your stats.", color = Muted)
        LevelCard(player)
        StatRow("Strength", level.coerceAtMost(10), Purple)
        StatRow("Intelligence", level.coerceAtMost(10), Cyan)
        StatRow("Wellness", level.coerceAtMost(10), Gold)
        StatRow("Discipline", level.coerceAtMost(10), Purple)
        StatRow("Creativity", level.coerceAtMost(10), Cyan)
        Text("Attribute-specific quest rewards are planned for a future update.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun StatRow(name: String, value: Int, color: Color) {
    Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(15.dp)).padding(14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text("LV. $value", color = color, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = { (value / 10f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(6.dp),
            color = color,
            trackColor = Color(0xFF303A52)
        )
    }
}

@Composable
private fun HabitScreen(habits: List<HabitEntity>, padding: PaddingValues, setHabit: (Int, Boolean) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Daily habits", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text("Build a streak one day at a time.", color = Muted)
        }
        items(habits, key = { it.id }) { habit ->
            Row(
                Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = habit.completedToday,
                    onCheckedChange = { setHabit(habit.id, it) },
                    colors = CheckboxDefaults.colors(checkedColor = Cyan)
                )
                Text(habit.title, color = Color.White)
            }
        }
        item { Text("Habit check-ins are saved on this device. Date-based streak history is next.", color = Muted, fontSize = 12.sp) }
    }
}
