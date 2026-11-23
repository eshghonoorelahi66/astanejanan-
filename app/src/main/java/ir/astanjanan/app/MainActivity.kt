
    
  
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

