package com.novara.levelup.domain
import org.junit.Assert.assertEquals
import org.junit.Test
class ProgressionTest {
 @Test fun easyQuestAwardsTenXp(){assertEquals(10,Progression.award(PlayerProgress(totalXp=0),Quest(1,"Test","",QuestDifficulty.EASY)).totalXp)}
 @Test fun completedQuestCannotAwardAgain(){val p=PlayerProgress(totalXp=40);assertEquals(p,Progression.award(p,Quest(1,"Test","",QuestDifficulty.EPIC,true)))}
 @Test fun levelProgression(){val p=PlayerProgress(totalXp=245);assertEquals(3,p.level);assertEquals(45,p.xpIntoLevel);assertEquals(55,p.xpToNextLevel)}
}
