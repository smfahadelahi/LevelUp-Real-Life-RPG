package com.novara.levelup.domain
enum class QuestDifficulty(val xp: Int) { EASY(10), NORMAL(25), HARD(50), EPIC(100) }
data class Quest(val id: Int, val title: String, val detail: String, val difficulty: QuestDifficulty, val completed: Boolean = false)
data class PlayerProgress(val totalXp: Int = 120, val strength: Int = 2, val intelligence: Int = 3, val wellness: Int = 2, val discipline: Int = 4, val creativity: Int = 1) {
    val level get() = totalXp / 100 + 1
    val xpIntoLevel get() = totalXp % 100
    val xpToNextLevel get() = 100 - xpIntoLevel
}
object Progression {
    fun award(progress: PlayerProgress, quest: Quest): PlayerProgress =
        if (quest.completed) progress else progress.copy(totalXp = progress.totalXp + quest.difficulty.xp)
}
