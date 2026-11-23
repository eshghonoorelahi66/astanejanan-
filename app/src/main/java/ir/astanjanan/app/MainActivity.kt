package ir.astanjanan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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

private val Ink = Color(0xFF101B19)
private val Gold = Color(0xFFD8B875)
private val Muted = Color(0xFF9EADA5)

@Composable
private fun AstanApp() {
    var dark by remember { mutableStateOf(true) }
    var count by remember { mutableIntStateOf(0) }
    var dhikr by remember { mutableStateOf("سُبْحَانَ اللَّه") }
    val nav = rememberNavController()
    val scheme = if (dark) darkColorScheme(primary = Gold, secondary = Color(0xFF9BC2AC),
        background = Ink, surface = Color(0xFF1A2925), onBackground = Color(0xFFF4F0E6),
        onSurface = Color(0xFFF4F0E6), surfaceVariant = Color(0xFF263832))
    else lightColorScheme(primary = Color(0xFF173C35), secondary = Color(0xFF9A783B),
        background = Color(0xFFF7F5EF), surface = Color.White,
        onBackground = Color(0xFF1D2924), onSurface = Color(0xFF1D2924),
        surfaceVariant = Color(0xFFECEFE8))
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(colorScheme = scheme) {
            val route = nav.currentBackStackEntryAsState().value?.destination?.route ?: "home"
            val tabs = listOf(Triple("home","خانه",Icons.Default.Home),
                Triple("quran","قرآن",Icons.Default.MenuBook), Triple("dhikr","ذکر",Icons.Default.Favorite),
                Triple("library","کتابخانه",Icons.Default.LocalLibrary), Triple("more","بیشتر",Icons.Default.GridView))
            Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { (key,label,icon) ->
                        NavigationBarItem(selected = route == key, onClick = { nav.navigate(key) {
                            popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true
                        } }, icon = { Icon(icon, label) }, label = { Text(label, fontSize = 11.sp) })
                    }
                }
            }) { padding ->
                NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(padding)) {
                    composable("home") { HomeScreen { nav.navigate(it) } }
                    composable("quran") { QuranScreen() }
                    composable("dhikr") { DhikrScreen(count,dhikr,{count++},{count=0},{dhikr=it;count=0}) }
                    composable("library") { LibraryScreen { nav.navigate(it) } }
                    composable("more") { MoreScreen { nav.navigate(it) } }
                    composable("settings") { SettingsScreen(dark) { dark = it } }
                    composable("prayer") { DetailScreen("اوقات شرعی",Icons.Default.Schedule,
                        "برای محاسبه دقیق، شهر و روش محاسبه باید مشخص شود.",listOf("انتخاب شهر","روش محاسبه","اعلان اذان")) }
                    composable("qibla") { DetailScreen("قبله‌نما",Icons.Default.Explore,
                        "جهت قبله پس از اتصال به موقعیت مکانی و قطب‌نمای دستگاه محاسبه می‌شود.",
                        listOf("دسترسی موقعیت مکانی","کالیبره کردن قطب‌نما")) }
                    composable("duas") { DetailScreen("دعا و مناجات",Icons.Default.AutoAwesome,
                        "مجموعه دعاها با ذکر منبع معتبر در این بخش قرار می‌گیرد.",
                        listOf("دعای روزانه","مناجات","زیارات","تعقیبات نماز")) }
                    composable("about") { DetailScreen("درباره آستان جانان",Icons.Default.Info,
                        "همراهی آرام برای قرآن، نیایش و زندگی معنوی.",listOf("نسخه ۰٫۲٫۰","حریم خصوصی","منابع")) }
                }
            }
        }
    }
}

@Composable
private fun Page(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        .padding(horizontal=18.dp).padding(top=17.dp,bottom=18.dp), content=content)
}
@Composable
private fun Page(horizontalAlignment: Alignment.Horizontal, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        .padding(horizontal=18.dp).padding(top=17.dp,bottom=18.dp),
        horizontalAlignment=horizontalAlignment, content=content)
}
@Composable
private fun Heading(title:String, sub:String?=null) {
    Text(title,fontSize=27.sp,fontWeight=FontWeight.Bold)
    if(sub!=null) Text(sub,color=Muted,fontSize=13.sp)
}
@Composable
private fun Section(title:String) {
    Text(title,fontSize=17.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(vertical=10.dp))
}
@Composable
private fun HomeScreen(open:(String)->Unit) {
    Page {
        Row(verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("السلام علیک",color=MaterialTheme.colorScheme.primary,fontSize=13.sp)
                Text("آستان جانان",fontSize=27.sp,fontWeight=FontWeight.ExtraBold)
            }
            IconButton(onClick={open("settings")}) { Icon(Icons.Default.Settings,"تنظیمات") }
        }
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF28594A),Color(0xFF142D27))))
            .padding(21.dp)) {
            Column {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome,null,tint=Gold)
                    Spacer(Modifier.width(8.dp))
                    Text("خانه‌ای برای آرامش روح",color=Color(0xFFE7D5A6),fontSize=12.sp)
                }
                Spacer(Modifier.height(16.dp))
                Text("با یاد خدا،\nدل آرام می‌گیرد",color=Color.White,fontSize=26.sp,
                    fontWeight=FontWeight.Bold,lineHeight=37.sp)
                Spacer(Modifier.height(8.dp))
                Text("قرآن، نیایش و لحظه‌های ناب بندگی",color=Color(0xFFD0DED5),fontSize=12.sp)
                Spacer(Modifier.height(17.dp))
                Text("امروز را با نیت خیر آغاز کن",color=Color(0xFFE7D5A6),fontSize=11.sp)
            }
        }
        Section("دسترسی سریع")
        val tiles=listOf(Triple("قرآن کریم","quran",Icons.Default.MenuBook),
            Triple("ذکرشمار","dhikr",Icons.Default.Favorite), Triple("اوقات شرعی","prayer",Icons.Default.Schedule),
            Triple("قبله‌نما","qibla",Icons.Default.Explore), Triple("دعا و مناجات","duas",Icons.Default.AutoAwesome),
            Triple("کتابخانه","library",Icons.Default.LocalLibrary))
        tiles.chunked(2).forEach { pair ->
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp),modifier=Modifier.padding(bottom=10.dp)) {
                pair.forEach { (title,route,icon) ->
                    Surface(Modifier.weight(1f).height(100.dp).clickable { open(route) },
                        shape=RoundedCornerShape(19.dp),color=MaterialTheme.colorScheme.surface) {
                        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.SpaceBetween) {
                            Icon(icon,null,tint=MaterialTheme.colorScheme.primary,modifier=Modifier.size(25.dp))
                            Text(title,fontWeight=FontWeight.SemiBold,fontSize=13.sp)
                        }
                    }
                }
            }
        }
        Section("یادآوری امروز")
        Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(18.dp)) {
            Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.WbSunny,null,tint=Gold,modifier=Modifier.size(27.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("لحظه‌ای برای آرامش",fontWeight=FontWeight.Bold)
                    Text("چند دقیقه مکث، ذکر و توجه به دل",color=Muted,fontSize=11.sp)
                }
            }
        }
    }
}
@Composable
private fun QuranScreen() {
    var query by remember { mutableStateOf("") }
    val data=listOf("الفاتحه|7","البقره|286","آل عمران|200","النساء|176","المائده|120",
        "الانعام|165","الاعراف|206","الانفال|75","التوبه|129","یونس|109",
        "هود|123","یوسف|111","الرعد|43","ابراهیم|52","الحجر|99")
    val filtered=data.filter { it.substringBefore("|").contains(query) || it.substringAfter("|")==query }
    Page {
        Heading("قرآن کریم","همنشینی با کلام وحی")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),singleLine=true,
            placeholder={Text("جست‌وجوی سوره")},leadingIcon={Icon(Icons.Default.Search,null)},
            shape=RoundedCornerShape(16.dp))
        Spacer(Modifier.height(12.dp))
        Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(17.dp)) {
            Row(Modifier.fillMaxWidth().padding(15.dp),verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.Bookmark,null,tint=Gold); Spacer(Modifier.width(10.dp))
                Column {
                    Text("ادامه مطالعه",fontWeight=FontWeight.Bold)
                    Text("هنوز سوره‌ای برای ادامه انتخاب نشده",color=Muted,fontSize=11.sp)
                }
            }
        }
        Section("فهرست سوره‌ها")
        LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)) {
            items(filtered) { row ->
                val name=row.substringBefore("|"); val verses=row.substringAfter("|")
                Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(15.dp),
                    modifier=Modifier.fillMaxWidth().clickable { }) {
                    Row(Modifier.padding(13.dp),verticalAlignment=Alignment.CenterVertically) {
                        Box(Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),contentAlignment=Alignment.Center) {
                            Text((data.indexOf(row)+1).toString(),fontWeight=FontWeight.Bold,
                                color=MaterialTheme.colorScheme.primary)
                        }
                        Spacer(Modifier.width(12.dp)); Text(name,Modifier.weight(1f),fontWeight=FontWeight.Bold)
                        Text(verses+" آیه",color=Muted,fontSize=11.sp)
                    }
                }
            }
        }
        Text("نمایش آیات و ترجمه پس از افزودن بسته محتوایی معتبر قرآن فعال می‌شود.",
            color=Muted,fontSize=11.sp,textAlign=TextAlign.Center,modifier=Modifier.padding(top=10.dp))
    }
}
@Composable
private fun DhikrScreen(count:Int,dhikr:String,add:()->Unit,reset:()->Unit,choose:(String)->Unit) {
    Page(Alignment.CenterHorizontally) {
        Heading("ذکرشمار","با حضور قلب، آرام و پیوسته")
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement=Arrangement.spacedBy(5.dp)) {
            listOf("سُبْحَانَ اللَّه","الْحَمْدُ لِلَّه","اللَّهُ أَکْبَر").forEach {
                FilterChip(selected=dhikr==it,onClick={choose(it)},label={Text(it,fontSize=10.sp)})
            }
        }
        Spacer(Modifier.height(25.dp))
        Box(Modifier.size(245.dp).clip(CircleShape)
            .background(Brush.radialGradient(listOf(MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.surface))),contentAlignment=Alignment.Center) {
            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                Text(dhikr,color=MaterialTheme.colorScheme.primary,fontSize=17.sp)
                Text(count.toString(),fontSize=62.sp,fontWeight=FontWeight.Light)
                Text("ذکر ثبت‌شده",color=Muted,fontSize=12.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick=add,Modifier.fillMaxWidth().height(58.dp),shape=RoundedCornerShape(17.dp)) {
            Icon(Icons.Default.Add,null); Spacer(Modifier.width(8.dp)); Text("ثبت ذکر",fontSize=17.sp)
        }
        TextButton(onClick=reset) { Icon(Icons.Default.Refresh,null); Text("شروع شمارش تازه") }
        Text("ذخیره دائمی شمارش در مرحله بعد اضافه می‌شود.",color=Muted,fontSize=11.sp,textAlign=TextAlign.Center)
    }
}
@Composable
private fun LibraryScreen(open:(String)->Unit) {
    Page {
        Heading("کتابخانه معنوی","منابعی برای مطالعه و تأمل")
        Spacer(Modifier.height(17.dp))
        val rows=listOf(Triple("دعا و مناجات","متون دعا و نیایش",Icons.Default.AutoAwesome),
            Triple("احادیث و روایات","گزیده روایات با ذکر منبع",Icons.Default.FormatQuote),
            Triple("آموزش و معارف","موضوعات اعتقادی و اخلاقی",Icons.Default.School),
            Triple("یادداشت‌های معنوی","تأمل‌ها و نوشته‌های روزانه",Icons.Default.Lightbulb))
        rows.forEachIndexed { i,item ->
            Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(17.dp),
                modifier=Modifier.fillMaxWidth().padding(bottom=10.dp).clickable { if(i==0) open("duas") }) {
                Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
                    Icon(item.third,null,tint=MaterialTheme.colorScheme.primary); Spacer(Modifier.width(13.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.first,fontWeight=FontWeight.Bold); Text(item.second,color=Muted,fontSize=11.sp)
                    }
                    Icon(Icons.Default.ChevronLeft,null,tint=Muted)
                }
            }
        }
    }
}
@Composable
private fun MoreScreen(open:(String)->Unit) {
    Page {
        Heading("بخش‌های بیشتر","ابزارها و تنظیمات آستان جانان")
        Spacer(Modifier.height(18.dp))
        val rows=listOf(Triple("اوقات شرعی","محاسبه زمان نمازها",Icons.Default.Schedule) to "prayer",
            Triple("قبله‌نما","یافتن جهت قبله",Icons.Default.Explore) to "qibla",
            Triple("دعا و مناجات","مجموعه نیایش‌ها",Icons.Default.AutoAwesome) to "duas",
            Triple("تنظیمات","شخصی‌سازی برنامه",Icons.Default.Settings) to "settings",
            Triple("درباره برنامه","معرفی و نسخه",Icons.Default.Info) to "about")
        rows.forEach { (item,route) ->
            Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(16.dp),
                modifier=Modifier.fillMaxWidth().padding(bottom=9.dp).clickable { open(route) }) {
                Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
                    Icon(item.third,null,tint=MaterialTheme.colorScheme.primary); Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.first,fontWeight=FontWeight.Bold); Text(item.second,color=Muted,fontSize=11.sp)
                    }
                    Icon(Icons.Default.ChevronLeft,null,tint=Muted)
                }
            }
        }
    }
}
@Composable
private fun SettingsScreen(dark:Boolean,setDark:(Boolean)->Unit) {
    Page {
        Heading("تنظیمات","ظاهر و ترجیحات برنامه")
        Spacer(Modifier.height(18.dp))
        Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Icon(if(dark) Icons.Default.DarkMode else Icons.Default.LightMode,null,tint=MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("حالت تاریک",fontWeight=FontWeight.Bold)
                        Text("نمایش آرام‌تر در محیط کم‌نور",color=Muted,fontSize=11.sp)
                    }
                    Switch(dark,setDark)
                }
                HorizontalDivider(Modifier.padding(vertical=12.dp))
                SettingRow("زبان برنامه","فارسی"); SettingRow("جهت نمایش","راست‌چین"); SettingRow("نسخه برنامه","۰٫۲٫۰")
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("انتخاب شهر، روش محاسبه اوقات شرعی، اعلان‌ها و پشتیبان‌گیری در مراحل بعد فعال می‌شوند.",
            color=Muted,fontSize=12.sp,lineHeight=20.sp)
    }
}
@Composable
private fun SettingRow(title:String,value:String) {
    Row(Modifier.fillMaxWidth().padding(vertical=7.dp)) {
        Text(title,Modifier.weight(1f)); Text(value,color=Muted,fontSize=12.sp)
    }
}
@Composable
private fun DetailScreen(title:String,icon:ImageVector,description:String,items:List<String>) {
    Page(Alignment.CenterHorizontally) {
        Surface(shape=RoundedCornerShape(20.dp),color=MaterialTheme.colorScheme.surfaceVariant) {
            Icon(icon,null,tint=MaterialTheme.colorScheme.primary,modifier=Modifier.padding(18.dp).size(38.dp))
        }
        Spacer(Modifier.height(14.dp)); Text(title,fontSize=26.sp,fontWeight=FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(description,color=Muted,textAlign=TextAlign.Center,lineHeight=22.sp)
        Spacer(Modifier.height(20.dp))
        items.forEach {
            Surface(color=MaterialTheme.colorScheme.surface,shape=RoundedCornerShape(15.dp),
                modifier=Modifier.fillMaxWidth().padding(bottom=9.dp)) {
                Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically) {
                    Icon(Icons.Default.ChevronLeft,null,tint=MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp)); Text(it)
                }
            }
        }
    }
}
