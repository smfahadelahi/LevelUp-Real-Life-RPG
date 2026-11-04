package com.novara.levelup
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.novara.levelup.ui.LevelUpApp
import com.novara.levelup.ui.theme.LevelUpTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LevelUpTheme { LevelUpApp() } }
    }
}
