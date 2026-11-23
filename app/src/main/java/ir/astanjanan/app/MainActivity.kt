package ir.astanjanan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AstanApp() }
    }
}

private val NightBackground = Color(0xFF101B19)
private val NightSurface = Color(0xFF192824)
private val IslamicGreen = Color(0xFF173C35)
private val WarmGold = Color(0xFFD4B36A)

@Composable
private fun AstanApp() {
    val navController = rememberNavController()
    var dhikrCount by remember { mutableIntStateOf(0) }
    var darkTheme by remember { mutableStateOf(true) }

    val scheme = if (darkTheme) {
        darkColorScheme(
            primary = WarmGold,
            secondary = Color(0xFF9DB7A5),
            background = NightBackground,
            surface = NightSurface
        )
    } else {
        lightColorScheme(primary = IslamicGreen, secondary = WarmGold)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(colorScheme = scheme) {
            val entry by navController.currentBackStackEntryAsState()
            val currentRoute = entry?.destination?.route

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val tabs = listOf(
                            Triple("home", "خانه", Icons.Default.Home),
                            Triple("quran", "قرآن", Icons.Default.MenuBook),
                            Triple("dhikr", "ذکر", Icons.Default.Favorite),
                            Triple("library", "کتابخانه", Icons.Default.LocalLibrary),
                            Triple("more", "بیشتر", Icons.Default.MoreHoriz)
                        )
                        tabs.forEach { (route, label, icon) ->
                            NavigationBarItem(
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") { HomeScreen { navController.navigate(it) } }
                    composable("quran") { QuranScreen() }
                    composable("dhikr") {
                        DhikrScreen(dhikrCount, { dhikrCount++ }, { dhikrCount = 0 })
                    }
                    composable("library") {
                        InformationScreen(
                            "کتابخانه",
                            "کتاب‌ها و منابع معتبر پس از افزودن بسته‌های محتوایی در این بخش نمایش داده می‌شوند."
                        )
                    }
                    composable("more") {
                        MoreScreen(darkTheme, { darkTheme = it }) {
                            navController.navigate("settings")
                        }
                    }
                    composable("settings") {
                        MoreScreen(darkTheme, { darkTheme = it }, settingsOnly = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun Page(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 25.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(20.dp))
        content()
    }
}

@Composable
private fun HomeScreen(onOpen: (String) -> Unit) {
    Page("آستان جانان") {
        Text(
            "همراهی آرام برای قرآن، نیایش و شناخت",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        val items = listOf(
            Triple("قرآن کریم", "quran", Icons.Default.MenuBook),
            Triple("ذکرشمار", "dhikr", Icons.Default.Favorite),
            Triple("کتابخانه", "library", Icons.Default.LocalLibrary),
            Triple("بیشتر", "more", Icons.Default.Apps)
        )
        items.forEach { item ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                onClick = { onOpen(item.second) }
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(item.third, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(14.dp))
                    Text(item.first, fontSize = 18.sp)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.ChevronLeft, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun QuranScreen() {
    Page("قرآن کریم") {
        Text("بخش قرآن آماده‌ی اتصال به بسته‌ی متن معتبر است.", fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            "برای حفظ امانت، متن آیات در این نسخه به‌صورت نمونه یا ساختگی درج نشده است. متن قرآن و ترجمه باید از منبع معتبر، همراه با نسخه و مجوز، به برنامه افزوده شود.",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        OutlinedCard(Modifier.fillMaxWidth()) {
            Text(
                "فهرست سوره‌ها پس از نصب بسته‌ی آفلاین قرآن نمایش داده می‌شود.",
                modifier = Modifier.padding(18.dp)
            )
        }
    }
}

@Composable
private fun DhikrScreen(count: Int, onIncrement: () -> Unit, onReset: () -> Unit) {
    Page("ذکرشمار") {
        Text("شمارش ذکر", fontSize = 18.sp)
        Spacer(Modifier.height(25.dp))
        Text(count.toString(), fontSize = 64.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onIncrement,
            modifier = Modifier.fillMaxWidth().height(58.dp)
        ) {
            Text("＋  ثبت ذکر", fontSize = 20.sp)
        }
        TextButton(onClick = onReset) { Text("شروع شمارش تازه") }
    }
}

@Composable
private fun InformationScreen(title: String, message: String) {
    Page(title) { Text(message, textAlign = TextAlign.Center) }
}

@Composable
private fun MoreScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    settingsOnly: Boolean = false,
    onOpenSettings: () -> Unit = {}
) {
    Page(if (settingsOnly) "تنظیمات" else "بیشتر") {
        if (!settingsOnly) {
            Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
                Text("تنظیمات برنامه")
            }
            Text("آستان جانان — نسخه‌ی پایه", modifier = Modifier.padding(16.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("حالت تاریک", modifier = Modifier.weight(1f))
            Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
        }
        Text("زبان رابط: فارسی", modifier = Modifier.padding(8.dp))
        Text("نسخه 0.1.0", modifier = Modifier.padding(8.dp))
    }
}
