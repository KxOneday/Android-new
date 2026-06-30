package com.kxoneday.jinianri

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings as AndroidSettings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val notificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent { JinianriApp() }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("jinianri", "纪念日提醒", NotificationManager.IMPORTANCE_DEFAULT)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}

private object AppColors {
    val Primary = Color(0xFF8B9DC3)
    val PrimaryLight = Color(0xFFB8C5D6)
    val Accent = Color(0xFFF0A8A8)
    val BackgroundLight = Color(0xFFFAF8F5)
    val BackgroundDark = Color(0xFF1C1C1E)
    val CardLight = Color.White
    val CardDark = Color(0xFF2C2C2E)
    val SecondaryBgLight = Color(0xFFF2EFEC)
    val SecondaryBgDark = Color(0xFF3A3A3C)
    val TextPrimaryLight = Color(0xFF2C2C2C)
    val TextPrimaryDark = Color(0xFFF5F5F5)
    val TextSecondary = Color(0xFF8E8E93)
    val TextTertiary = Color(0xFFC7C7CC)
    val Success = Color(0xFF7BC8A4)
    val Warning = Color(0xFFF0C27A)
    val Error = Color(0xFFE88D8D)
    val Info = Color(0xFF8BB8E8)
}

enum class DayType(val label: String) { Countdown("倒数日"), Countup("正数日") }

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val icon: String,
    val colorHex: String,
    val sortOrder: Int,
    val isDefault: Boolean = true
)

data class ReminderSettings(
    val enabled: Boolean = false,
    val advanceDays: Int = 1,
    val hour: Int = 9,
    val minute: Int = 0,
    val note: String = "",
    val yearlyRepeat: Boolean = false
) {
    fun description(): String {
        if (!enabled) return "未设置提醒"
        val dayText = if (advanceDays == 0) "当天" else "提前${advanceDays}天"
        return "$dayText ${"%02d:%02d".format(hour, minute)} 提醒"
    }
}

data class MemorialDay(
    val id: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    val title: String = "",
    val notes: String = "",
    val targetDate: String = LocalDate.now().toString(),
    val dayType: DayType = DayType.Countdown,
    val categoryId: String? = null,
    val tags: List<String> = emptyList(),
    val pinned: Boolean = false,
    val useLunarCalendar: Boolean = false,
    val lunarMonth: Int? = null,
    val lunarDay: Int? = null,
    val backgroundColorHex: String = "#F5F0EB",
    val backgroundEndColorHex: String? = "#FFFFFF",
    val textColorHex: String = "#2C2C2C",
    val fontSize: Float = 28f,
    val icon: String = "heart",
    val cornerRadius: Float = 16f,
    val shadowRadius: Float = 8f,
    val showGradient: Boolean = true,
    val yearlyRepeat: Boolean = false,
    val reminder: ReminderSettings = ReminderSettings()
) {
    val date: LocalDate get() = LocalDate.parse(targetDate)

    fun daysFromNow(today: LocalDate = LocalDate.now()): Long = ChronoUnit.DAYS.between(today, date)

    fun effectiveDate(today: LocalDate = LocalDate.now()): LocalDate {
        if (dayType != DayType.Countdown) return date
        if (!yearlyRepeat && daysFromNow(today) > 0) return date
        var next = runCatching { LocalDate.of(today.year, date.month, date.dayOfMonth) }.getOrDefault(date)
        if (next.isBefore(today)) next = next.plusYears(1)
        return next
    }

    fun displayDayCount(today: LocalDate = LocalDate.now()): Long {
        return when (dayType) {
            DayType.Countdown -> ChronoUnit.DAYS.between(today, effectiveDate(today)).coerceAtLeast(0)
            DayType.Countup -> abs(ChronoUnit.DAYS.between(today, date)).coerceAtLeast(1)
        }
    }

    fun subtitle(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日")
        val dateText = date.format(formatter)
        return if (useLunarCalendar && lunarMonth != null && lunarDay != null) "农历 ${lunarMonth}月${lunarDay}日 · $dateText" else dateText
    }

    fun countdownDescription(today: LocalDate = LocalDate.now()): String {
        return if (dayType == DayType.Countdown) {
            if (yearlyRepeat || daysFromNow(today) <= 0) "距离今年$title 还有" else "距离$title 还有"
        } else {
            "已经"
        }
    }
}

data class MemorialTemplate(
    val name: String,
    val categoryName: String,
    val icon: String,
    val defaultTitle: String,
    val type: DayType = DayType.Countdown
)

data class AppSettings(
    val requirePassword: Boolean = false,
    val appPassword: String = "",
    val notificationsEnabled: Boolean = true,
    val darkMode: Boolean = false
)

private val defaultCategories = listOf(
    Category(name = "生日", icon = "gift", colorHex = "#FF6B6B", sortOrder = 0),
    Category(name = "恋爱", icon = "heart", colorHex = "#FF69B4", sortOrder = 1),
    Category(name = "纪念日", icon = "star", colorHex = "#FFD700", sortOrder = 2),
    Category(name = "工作", icon = "work", colorHex = "#4A90D9", sortOrder = 3),
    Category(name = "考试", icon = "book", colorHex = "#9B59B6", sortOrder = 4),
    Category(name = "还款", icon = "card", colorHex = "#E74C3C", sortOrder = 5),
    Category(name = "节日", icon = "sparkles", colorHex = "#FFA500", sortOrder = 6),
    Category(name = "旅行", icon = "airplane", colorHex = "#1ABC9C", sortOrder = 7),
    Category(name = "健康", icon = "health", colorHex = "#2ECC71", sortOrder = 8),
    Category(name = "其他", icon = "more", colorHex = "#95A5A6", sortOrder = 9)
)

private val builtInTemplates = listOf(
    MemorialTemplate("生日", "生日", "gift", "我的生日"),
    MemorialTemplate("恋爱纪念日", "恋爱", "heart", "恋爱纪念日", DayType.Countup),
    MemorialTemplate("结婚纪念日", "恋爱", "sparkles", "结婚纪念日", DayType.Countup),
    MemorialTemplate("考试倒数日", "考试", "book", "考试倒数日"),
    MemorialTemplate("还款提醒", "还款", "card", "信用卡还款"),
    MemorialTemplate("法定节日", "节日", "sparkles", "元旦"),
    MemorialTemplate("工作DDL", "工作", "work", "项目截止日"),
    MemorialTemplate("旅行倒数日", "旅行", "airplane", "旅行出发日"),
    MemorialTemplate("纪念日（自定义）", "其他", "star", "重要日子"),
    MemorialTemplate("健康管理", "健康", "health", "体检日"),
    MemorialTemplate("发薪日", "工作", "money", "发薪日")
)

class JinianriStore(private val context: Context) {
    private val prefs = context.getSharedPreferences("jinianri", Context.MODE_PRIVATE)
    private val gson = Gson()
    var days = mutableStateListOf<MemorialDay>()
        private set
    var categories = mutableStateListOf<Category>()
        private set
    var settings by mutableStateOf(AppSettings())
        private set

    init { load() }

    private fun load() {
        val dayType = object : TypeToken<List<MemorialDay>>() {}.type
        val catType = object : TypeToken<List<Category>>() {}.type
        val savedDays = prefs.getString("days", null)?.let { gson.fromJson<List<MemorialDay>>(it, dayType) } ?: emptyList()
        val savedCategories = prefs.getString("categories", null)?.let { gson.fromJson<List<Category>>(it, catType) } ?: defaultCategories
        days.clear()
        days.addAll(savedDays.sortedWith(compareByDescending<MemorialDay> { it.pinned }.thenBy { it.targetDate }))
        categories.clear()
        categories.addAll(savedCategories.sortedBy { it.sortOrder })
        settings = prefs.getString("settings", null)?.let { gson.fromJson(it, AppSettings::class.java) } ?: AppSettings()
    }

    private fun saveDays() {
        prefs.edit().putString("days", gson.toJson(days)).apply()
    }

    fun saveSettings(newSettings: AppSettings) {
        settings = newSettings
        prefs.edit().putString("settings", gson.toJson(settings)).apply()
    }

    fun upsert(day: MemorialDay) {
        val index = days.indexOfFirst { it.id == day.id }
        if (index >= 0) days[index] = day else days.add(day)
        days.sortWith(compareByDescending<MemorialDay> { it.pinned }.thenBy { it.targetDate })
        saveDays()
    }

    fun delete(id: String) {
        days.removeAll { it.id == id }
        saveDays()
    }

    fun deleteAll(ids: Set<String>) {
        days.removeAll { ids.contains(it.id) }
        saveDays()
    }

    fun togglePin(id: String) {
        val index = days.indexOfFirst { it.id == id }
        if (index >= 0) {
            days[index] = days[index].copy(pinned = !days[index].pinned)
            days.sortWith(compareByDescending<MemorialDay> { it.pinned }.thenBy { it.targetDate })
            saveDays()
        }
    }
}

@Composable
fun JinianriApp() {
    val context = LocalContext.current
    val store = remember { JinianriStore(context) }
    var unlocked by remember { mutableStateOf(!store.settings.requirePassword) }

    MaterialTheme {
        Surface(color = if (store.settings.darkMode) AppColors.BackgroundDark else AppColors.BackgroundLight) {
            if (store.settings.requirePassword && !unlocked) {
                LockScreen(store) { unlocked = true }
            } else {
                MainShell(store)
            }
        }
    }
}

@Composable
private fun MainShell(store: JinianriStore) {
    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = bg(store),
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = card(store),
                contentColor = AppColors.Primary
            ) {
                BottomTab(0, selectedTab, "首页", Icons.Default.Home) { selectedTab = it }
                BottomTab(1, selectedTab, "时间计算器", Icons.Default.CalendarMonth) { selectedTab = it }
                BottomTab(2, selectedTab, "设置", Icons.Default.Settings) { selectedTab = it }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> HomeScreen(store)
                1 -> DateCalculatorScreen(store)
                2 -> SettingsScreen(store)
            }
        }
    }
}

@Composable
private fun BottomTab(index: Int, selected: Int, text: String, icon: ImageVector, onClick: (Int) -> Unit) {
    Tab(
        selected = selected == index,
        onClick = { onClick(index) },
        text = { Text(text, fontSize = 12.sp) },
        icon = { Icon(icon, null, Modifier.size(20.dp)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(store: JinianriStore) {
    var showEditor by remember { mutableStateOf<MemorialDay?>(null) }
    var showDetail by remember { mutableStateOf<MemorialDay?>(null) }
    var showTemplates by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var selectMode by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val selected = remember { mutableStateListOf<String>() }

    val filtered = store.days.filter { day ->
        val categoryOk = selectedCategory == null || day.categoryId == selectedCategory
        val queryOk = search.isBlank() || day.title.contains(search, true) || day.notes.contains(search, true) || day.tags.any { it.contains(search, true) }
        categoryOk && queryOk
    }
    val upcoming = store.days.filter { it.dayType == DayType.Countdown && it.displayDayCount() in 0..30 }.take(8)

    Box(Modifier.fillMaxSize().background(bg(store))) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectMode) {
                    TextButton(onClick = {
                        if (selected.size == filtered.size) selected.clear() else {
                            selected.clear()
                            selected.addAll(filtered.map { it.id })
                        }
                    }) {
                        Icon(if (selected.size == filtered.size && filtered.isNotEmpty()) Icons.Default.CheckCircle else Icons.Outlined.Circle, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (selected.size == filtered.size && filtered.isNotEmpty()) "取消全选" else "全选")
                    }
                    Spacer(Modifier.weight(1f))
                    Text("已选 ${selected.size} 项", color = AppColors.TextSecondary, fontSize = 13.sp)
                    Spacer(Modifier.weight(1f))
                    CircleButton(Icons.Default.Close, store) {
                        selectMode = false
                        selected.clear()
                    }
                    if (selected.isNotEmpty()) {
                        CircleButton(Icons.Default.Delete, store, AppColors.Error) {
                            store.deleteAll(selected.toSet())
                            selected.clear()
                            selectMode = false
                        }
                    }
                } else {
                    Text("纪念日", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textPrimary(store))
                    Spacer(Modifier.weight(1f))
                    CircleButton(Icons.Default.Search, store) { showSearch = !showSearch }
                    CircleButton(Icons.Default.FilterVintage, store) { showTemplates = true }
                    CircleButton(Icons.Default.CheckCircle, store) { selectMode = true }
                }
            }

            AnimatedVisibility(showSearch) {
                SearchBox(search) { search = it }
            }

            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip("全部", Icons.Default.Home, selectedCategory == null) { selectedCategory = null } }
                items(store.categories) { cat ->
                    FilterChip(cat.name, iconFor(cat.icon), selectedCategory == cat.id) { selectedCategory = cat.id }
                }
            }

            if (filtered.isEmpty()) {
                EmptyState { showTemplates = true }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 104.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item { StatsBar(filtered) }
                    if (upcoming.isNotEmpty()) item { UpcomingSection(upcoming, store) }
                    items(filtered, key = { it.id }) { day ->
                        MemorialCard(day, store, selectMode, selected.contains(day.id),
                            onSelect = {
                                if (selected.contains(day.id)) selected.remove(day.id) else selected.add(day.id)
                            },
                            onDetail = { showDetail = day },
                            onDelete = { store.delete(day.id) },
                            onPin = { store.togglePin(day.id) }
                        )
                    }
                    item {
                        Button(
                            onClick = { showEditor = MemorialDay() },
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Accent),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(0.58f).height(52.dp)
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(8.dp))
                            Text("新建纪念日")
                        }
                    }
                }
            }
        }

        Row(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingCircle(Icons.Default.FilterVintage, 48.dp, AppColors.Primary) { showTemplates = true }
            Spacer(Modifier.weight(1f))
            FloatingCircle(Icons.Default.Add, 56.dp, AppColors.Accent) { showEditor = MemorialDay() }
        }
    }

    showEditor?.let { day ->
        ModalBottomSheet(
            onDismissRequest = { showEditor = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = bg(store)
        ) {
            EditorScreen(store, day) { showEditor = null }
        }
    }

    showDetail?.let { day ->
        ModalBottomSheet(
            onDismissRequest = { showDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = bg(store)
        ) {
            DetailScreen(
                day = store.days.firstOrNull { it.id == day.id } ?: day,
                store = store,
                onEdit = {
                    showEditor = store.days.firstOrNull { it.id == day.id } ?: day
                    showDetail = null
                },
                onDelete = {
                    store.delete(day.id)
                    showDetail = null
                }
            )
        }
    }

    if (showTemplates) {
        ModalBottomSheet(
            onDismissRequest = { showTemplates = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = bg(store)
        ) {
            TemplatePicker(store, onClose = { showTemplates = false }, onEdit = { showEditor = it })
        }
    }
}

@Composable
private fun MemorialCard(
    day: MemorialDay,
    store: JinianriStore,
    selectMode: Boolean,
    selected: Boolean,
    onSelect: () -> Unit,
    onDetail: () -> Unit,
    onDelete: () -> Unit,
    onPin: () -> Unit
) {
    var actionOpen by remember { mutableStateOf(false) }
    var drag by remember { mutableStateOf(0f) }
    val offset by animateFloatAsState(if (actionOpen) -160f else 0f, label = "cardOffset")
    Box(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        actionOpen = drag < -80
                        drag = 0f
                    },
                    onHorizontalDrag = { _, amount -> drag += amount }
                )
            }
    ) {
        Row(
            Modifier.align(Alignment.CenterEnd).height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ActionTile(if (day.pinned) "取消置顶" else "置顶", Icons.Default.PinDrop, AppColors.Primary, onPin)
            ActionTile("删除", Icons.Default.Delete, AppColors.Error, onDelete)
        }
        Row(
            Modifier.offset { IntOffset(offset.roundToInt(), 0) }
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(day.cornerRadius.dp))
                .clip(RoundedCornerShape(day.cornerRadius.dp))
                .background(cardBrush(day))
                .clickable { if (selectMode) onSelect() else onDetail() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectMode) {
                Icon(if (selected) Icons.Default.CheckCircle else Icons.Outlined.Circle, null, tint = if (selected) AppColors.Accent else AppColors.TextTertiary)
                Spacer(Modifier.width(12.dp))
            }
            Box(
                Modifier.size(72.dp).clip(CircleShape)
                    .background(hexColor(day.backgroundColorHex).copy(alpha = 0.32f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(iconFor(day.icon), null, tint = hexColor(day.textColorHex).copy(alpha = 0.72f), modifier = Modifier.size(16.dp))
                    Text("${day.displayDayCount()}", color = if (day.dayType == DayType.Countdown) AppColors.Info else AppColors.Success, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(if (day.dayType == DayType.Countdown) "天后" else "天前", color = hexColor(day.textColorHex).copy(alpha = 0.62f), fontSize = 10.sp)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(day.title.ifBlank { "未命名纪念日" }, maxLines = 1, overflow = TextOverflow.Ellipsis, color = hexColor(day.textColorHex), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    if (day.pinned) Icon(Icons.Default.PinDrop, null, tint = AppColors.Warning, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                }
                Text(day.subtitle(), color = hexColor(day.textColorHex).copy(alpha = 0.6f), fontSize = 12.sp)
                if (day.notes.isNotBlank()) Text(day.notes, maxLines = 1, overflow = TextOverflow.Ellipsis, color = hexColor(day.textColorHex).copy(alpha = 0.5f), fontSize = 11.sp)
                if (day.tags.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        day.tags.take(3).forEach { tag ->
                            Text("#$tag", color = hexColor(day.textColorHex).copy(alpha = 0.62f), fontSize = 10.sp, modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(hexColor(day.textColorHex).copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
            Icon(Icons.Default.KeyboardArrowRight, null, tint = hexColor(day.textColorHex).copy(alpha = 0.42f), modifier = Modifier.clickable { onDetail() })
        }
    }
}

@Composable
private fun ActionTile(label: String, icon: ImageVector, color: Color, action: () -> Unit) {
    Column(
        Modifier.width(72.dp).fillMaxHeight().clip(RoundedCornerShape(14.dp)).background(color).clickable(onClick = action),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = Color.White)
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EditorScreen(store: JinianriStore, initial: MemorialDay, onClose: () -> Unit) {
    var day by remember(initial.id) { mutableStateOf(initial) }
    var tagText by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = day.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Column(Modifier.fillMaxWidth().fillMaxHeight(0.94f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onClose) { Text("取消", color = AppColors.TextSecondary) }
            Spacer(Modifier.weight(1f))
            Text(if (initial.title.isBlank()) "新建纪念日" else "编辑纪念日", fontWeight = FontWeight.SemiBold, color = textPrimary(store))
            Spacer(Modifier.weight(1f))
            TextButton(enabled = day.title.isNotBlank(), onClick = {
                store.upsert(day)
                onClose()
            }) { Text("保存", color = if (day.title.isBlank()) AppColors.TextTertiary else AppColors.Accent, fontWeight = FontWeight.Bold) }
        }
        PreviewCard(day)
        SectionCard("基本信息", store) {
            OutlinedTextField(day.title, { day = day.copy(title = it) }, label = { Text("名称 *") }, modifier = Modifier.fillMaxWidth())
            Segmented(DayType.entries.map { it.label }, DayType.entries.indexOf(day.dayType)) { day = day.copy(dayType = DayType.entries[it]) }
            OutlinedTextField(day.notes, { day = day.copy(notes = it) }, label = { Text("备注") }, minLines = 3, modifier = Modifier.fillMaxWidth())
        }
        SectionCard("日期设置", store) {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AppColors.SecondaryBgLight).clickable { showDatePicker = true }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, null, tint = AppColors.Accent)
                Spacer(Modifier.width(10.dp))
                Text("目标日期", color = textPrimary(store))
                Spacer(Modifier.weight(1f))
                Text(day.subtitle(), color = AppColors.TextSecondary)
            }
            SwitchRow("每年重复", "生日、节日等会自动计算下一次", day.yearlyRepeat, store) { day = day.copy(yearlyRepeat = it, reminder = day.reminder.copy(yearlyRepeat = it)) }
            SwitchRow("使用农历", "当前版本保留字段与展示，日期仍按公历存储", day.useLunarCalendar, store) { day = day.copy(useLunarCalendar = it) }
        }
        SectionCard("分类与标签", store) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Pill("未分类", day.categoryId == null) { day = day.copy(categoryId = null) } }
                items(store.categories) { cat -> Pill(cat.name, day.categoryId == cat.id) { day = day.copy(categoryId = cat.id) } }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(tagText, { tagText = it }, label = { Text("输入标签后点击 +") }, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    val tag = tagText.trim()
                    if (tag.isNotBlank() && !day.tags.contains(tag)) day = day.copy(tags = day.tags + tag)
                    tagText = ""
                }) { Icon(Icons.Default.Add, null, tint = AppColors.Accent) }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                day.tags.forEach { tag -> Pill("#$tag  ×", false) { day = day.copy(tags = day.tags - tag) } }
            }
        }
        SectionCard("卡片样式", store) {
            ColorTextField("背景颜色", day.backgroundColorHex) { day = day.copy(backgroundColorHex = it) }
            SwitchRow("渐变底色", "", day.showGradient, store) { day = day.copy(showGradient = it) }
            if (day.showGradient) ColorTextField("渐变结束色", day.backgroundEndColorHex ?: "#FFFFFF") { day = day.copy(backgroundEndColorHex = it) }
            ColorTextField("文字颜色", day.textColorHex) { day = day.copy(textColorHex = it) }
            Text("图标", color = AppColors.TextSecondary, fontSize = 13.sp)
            LazyVerticalGrid(columns = GridCells.Fixed(8), modifier = Modifier.height(96.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("heart", "star", "gift", "sparkles", "fire", "moon", "sun", "balloon", "crown", "book", "work", "airplane", "bell", "health", "card", "money")) { icon ->
                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(if (day.icon == icon) AppColors.Accent.copy(alpha = 0.12f) else Color.Transparent).clickable { day = day.copy(icon = icon) }, contentAlignment = Alignment.Center) {
                        Icon(iconFor(icon), null, tint = if (day.icon == icon) AppColors.Accent else AppColors.TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }
            SliderRow("标题字号", day.fontSize, 14f, 48f) { day = day.copy(fontSize = it) }
            SliderRow("卡片圆角", day.cornerRadius, 4f, 32f) { day = day.copy(cornerRadius = it) }
            SliderRow("阴影强度", day.shadowRadius, 0f, 24f) { day = day.copy(shadowRadius = it) }
        }
        SectionCard("提醒设置", store) {
            SwitchRow("开启提醒", "关闭后将收不到任何推送", day.reminder.enabled, store) { day = day.copy(reminder = day.reminder.copy(enabled = it)) }
            if (day.reminder.enabled) {
                Text("提前天数", color = AppColors.TextSecondary, fontSize = 13.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf(0, 1, 2, 3, 7, 15, 30)) { d ->
                        Pill(if (d == 0) "当天" else "${d}天", day.reminder.advanceDays == d) { day = day.copy(reminder = day.reminder.copy(advanceDays = d)) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(day.reminder.hour.toString(), { day = day.copy(reminder = day.reminder.copy(hour = it.toIntOrNull()?.coerceIn(0, 23) ?: 9)) }, label = { Text("时") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                    OutlinedTextField(day.reminder.minute.toString(), { day = day.copy(reminder = day.reminder.copy(minute = it.toIntOrNull()?.coerceIn(0, 59) ?: 0)) }, label = { Text("分") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                }
                OutlinedTextField(day.reminder.note, { day = day.copy(reminder = day.reminder.copy(note = it)) }, label = { Text("推送备注（可选）") }, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(Modifier.height(32.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        day = day.copy(targetDate = date.toString())
                    }
                    showDatePicker = false
                }) { Text("确认") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("取消") } }
        ) { DatePicker(datePickerState) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailScreen(day: MemorialDay, store: JinianriStore, onEdit: () -> Unit, onDelete: () -> Unit) {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("详情", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = textPrimary(store))
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onEdit) { Text("编辑", color = AppColors.Accent) }
        }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(cardBrush(day)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(day.title, color = hexColor(day.textColorHex), fontSize = day.fontSize.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Text("${day.displayDayCount()}", color = hexColor(day.textColorHex), fontSize = 72.sp, fontWeight = FontWeight.Bold)
            Text(day.countdownDescription(), color = hexColor(day.textColorHex).copy(alpha = 0.65f), fontSize = 16.sp)
        }
        if (day.dayType == DayType.Countdown) {
            val target = day.effectiveDate().atStartOfDay()
            val now = java.time.LocalDateTime.now()
            val seconds = abs(ChronoUnit.SECONDS.between(now, target))
            SectionCard("精确倒计时", store) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TimeBlock((seconds / 86400).toInt(), "天", AppColors.Info, Modifier.weight(1f))
                    TimeBlock(((seconds % 86400) / 3600).toInt(), "时", AppColors.Success, Modifier.weight(1f))
                    TimeBlock(((seconds % 3600) / 60).toInt(), "分", AppColors.Warning, Modifier.weight(1f))
                    TimeBlock((seconds % 60).toInt(), "秒", AppColors.Accent, Modifier.weight(1f))
                }
            }
        }
        if (day.notes.isNotBlank()) SectionCard("备注", store) { Text(day.notes, color = textPrimary(store), lineHeight = 20.sp) }
        if (day.tags.isNotEmpty()) SectionCard("标签", store) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) { day.tags.forEach { Pill("#$it", false) {} } }
        }
        SectionCard("提醒", store) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if (day.reminder.enabled) Icons.Default.Notifications else Icons.Default.NotificationsOff, null, tint = if (day.reminder.enabled) AppColors.Warning else AppColors.TextTertiary)
                Spacer(Modifier.width(8.dp))
                Text(day.reminder.description(), color = AppColors.TextSecondary)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val text = "${day.title}\n${day.displayDayCount()} ${if (day.dayType == DayType.Countdown) "天后" else "天前"}\n${day.subtitle()}"
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), "分享海报"))
            }, colors = ButtonDefaults.buttonColors(containerColor = AppColors.Info), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Share, null)
                Spacer(Modifier.width(6.dp))
                Text("分享海报")
            }
            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = AppColors.Error), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Delete, null)
                Spacer(Modifier.width(6.dp))
                Text("删除")
            }
        }
    }
}

@Composable
private fun TemplatePicker(store: JinianriStore, onClose: () -> Unit, onEdit: (MemorialDay) -> Unit) {
    Column(Modifier.fillMaxHeight(0.92f).verticalScroll(rememberScrollState()).padding(vertical = 16.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("选择模板快速创建", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary(store))
                Text("选择一个模板，快速创建纪念日后再调整细节", color = AppColors.TextSecondary, fontSize = 14.sp)
            }
            TextButton(onClick = onClose) { Text("关闭", color = AppColors.TextSecondary) }
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), modifier = Modifier.height(390.dp).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(builtInTemplates) { template -> TemplateCard(template, store) { onEdit(template.toDay(store)); onClose() } }
        }
        builtInTemplates.groupBy { it.categoryName }.toSortedMap().forEach { (category, templates) ->
            Text(category, Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = textPrimary(store))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(templates) { template -> TemplateCard(template, store) { onEdit(template.toDay(store)); onClose() } }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

private fun MemorialTemplate.toDay(store: JinianriStore): MemorialDay {
    val category = store.categories.firstOrNull { it.name == categoryName }
    return MemorialDay(title = defaultTitle, dayType = type, categoryId = category?.id, icon = icon)
}

@Composable
private fun TemplateCard(template: MemorialTemplate, store: JinianriStore, onClick: () -> Unit) {
    val color = hexColor(store.categories.firstOrNull { it.name == template.categoryName }?.colorHex ?: "#8B9DC3")
    Column(
        Modifier.width(150.dp).height(100.dp).clip(RoundedCornerShape(16.dp)).background(AppColors.SecondaryBgLight).border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(16.dp)).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(iconFor(template.icon), null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(8.dp))
        Text(template.name, color = textPrimary(store), fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text("立即创建", color = color, fontSize = 11.sp)
    }
}

@Composable
private fun DateCalculatorScreen(store: JinianriStore) {
    var mode by remember { mutableIntStateOf(0) }
    val modes = listOf("日期间隔", "日期推算", "时间换算", "工作日")
    val icons = listOf(Icons.Default.CalendarMonth, Icons.Default.ArrowForward, Icons.Default.AccessTime, Icons.Default.Work)
    Column(Modifier.fillMaxSize().background(bg(store)).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            modes.forEachIndexed { i, label ->
                Column(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(if (mode == i) AppColors.Primary else AppColors.SecondaryBgLight).clickable { mode = i }.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(icons[i], null, tint = if (mode == i) Color.White else AppColors.TextSecondary, modifier = Modifier.size(18.dp))
                    Text(label, color = if (mode == i) Color.White else AppColors.TextSecondary, fontSize = 11.sp)
                }
            }
        }
        when (mode) {
            0 -> IntervalCalculator(store)
            1 -> DateOffsetCalculator(store)
            2 -> TimeConvertCalculator(store)
            3 -> WorkdayCalculator(store)
        }
    }
}

@Composable
private fun IntervalCalculator(store: JinianriStore) {
    var start by remember { mutableStateOf(LocalDate.now()) }
    var end by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var exclude by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    CalculatorBody(store) {
        DateInput("开始日期", start) { start = it }
        DateInput("结束日期", end) { end = it }
        SwitchRow("排除周末（仅计算工作日）", "", exclude, store) { exclude = it }
        CalculateButton {
            val days = abs(ChronoUnit.DAYS.between(start, end)).toInt()
            val workdays = countWorkdays(start, end)
            result = "间隔 $days 天\n折合 ${days / 7}周${days % 7}天\n小时 ${days * 24}\n分钟 ${days * 24 * 60}" + if (exclude) "\n工作日 $workdays 天" else ""
        }
        result?.let { ResultCard(store, it) }
    }
}

@Composable
private fun DateOffsetCalculator(store: JinianriStore) {
    var base by remember { mutableStateOf(LocalDate.now()) }
    var future by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("7") }
    var unit by remember { mutableStateOf("天") }
    var result by remember { mutableStateOf<String?>(null) }
    CalculatorBody(store) {
        DateInput("基准日期", base) { base = it }
        Segmented(listOf("未来", "过去"), if (future) 0 else 1) { future = it == 0 }
        OutlinedTextField(value, { value = it }, label = { Text("数量") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Segmented(listOf("天", "周", "月", "年"), listOf("天", "周", "月", "年").indexOf(unit)) { unit = listOf("天", "周", "月", "年")[it] }
        CalculateButton {
            val n = value.toLongOrNull() ?: 0
            val signed = if (future) n else -n
            val date = when (unit) {
                "周" -> base.plusWeeks(signed)
                "月" -> base.plusMonths(signed)
                "年" -> base.plusYears(signed)
                else -> base.plusDays(signed)
            }
            result = date.format(DateTimeFormatter.ofPattern("yyyy年M月d日"))
        }
        result?.let { ResultCard(store, it) }
    }
}

@Composable
private fun TimeConvertCalculator(store: JinianriStore) {
    var input by remember { mutableStateOf("1") }
    var type by remember { mutableStateOf("小时→分钟") }
    var result by remember { mutableStateOf("") }
    CalculatorBody(store) {
        Segmented(listOf("小时→分钟", "小时→秒", "分钟→小时", "天→小时"), listOf("小时→分钟", "小时→秒", "分钟→小时", "天→小时").indexOf(type)) {
            type = listOf("小时→分钟", "小时→秒", "分钟→小时", "天→小时")[it]
        }
        OutlinedTextField(input, { input = it }, label = { Text("请输入数字") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
        CalculateButton {
            val n = input.toDoubleOrNull() ?: 0.0
            result = when (type) {
                "小时→秒" -> "${(n * 3600).format()} 秒"
                "分钟→小时" -> "${(n / 60).format()} 小时"
                "天→小时" -> "${(n * 24).format()} 小时"
                else -> "${(n * 60).format()} 分钟"
            }
        }
        if (result.isNotBlank()) ResultCard(store, result)
    }
}

@Composable
private fun WorkdayCalculator(store: JinianriStore) {
    var start by remember { mutableStateOf(LocalDate.now()) }
    var end by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var result by remember { mutableStateOf<String?>(null) }
    CalculatorBody(store) {
        DateInput("开始日期", start) { start = it }
        DateInput("结束日期", end) { end = it }
        Text("自动排除周六、周日，仅计算工作日", color = AppColors.TextSecondary, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(AppColors.Info.copy(alpha = 0.08f)).padding(12.dp))
        CalculateButton {
            val total = abs(ChronoUnit.DAYS.between(start, end)).toInt()
            val work = countWorkdays(start, end)
            result = "总天数 $total\n工作日 $work\n周末 ${total - work}"
        }
        result?.let { ResultCard(store, it) }
    }
}

@Composable
private fun SettingsScreen(store: JinianriStore) {
    val context = LocalContext.current
    var passwordDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    LazyColumn(Modifier.fillMaxSize().background(bg(store)).statusBarsPadding(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            SectionCard("隐私安全", store) {
                SwitchRow("应用锁", "打开应用时需验证身份", store.settings.requirePassword, store) {
                    if (it && store.settings.appPassword.isBlank()) passwordDialog = true
                    store.saveSettings(store.settings.copy(requirePassword = it))
                }
                if (store.settings.requirePassword) LinkRow("修改密码", Icons.Default.KeyboardArrowRight) { passwordDialog = true }
            }
        }
        item {
            SectionCard("通知设置", store) {
                SwitchRow("允许通知", "关闭后将收不到任何提醒", store.settings.notificationsEnabled, store) { store.saveSettings(store.settings.copy(notificationsEnabled = it)) }
                LinkRow("系统通知设置", Icons.Default.ArrowForward) { context.startActivity(Intent(AndroidSettings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(AndroidSettings.EXTRA_APP_PACKAGE, context.packageName)) }
                LinkRow("发送测试通知", Icons.Default.Notifications, AppColors.Warning) { sendTestNotification(context) }
            }
        }
        item {
            SectionCard("关于", store) {
                InfoRow("应用名称", "纪念日", store)
                InfoRow("版本", "v1.0.0 (Build 1)", store)
                InfoRow("数据安全", "AES-256 本地加密", store, AppColors.Success)
                InfoRow("作者", "Felix.", store, AppColors.Primary)
            }
        }
    }
    if (passwordDialog) {
        AlertDialog(
            onDismissRequest = { passwordDialog = false },
            title = { Text("设置密码") },
            text = { OutlinedTextField(password, { password = it.take(4) }, label = { Text("输入4位数密码") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) },
            confirmButton = {
                TextButton(onClick = {
                    if (password.length >= 4) store.saveSettings(store.settings.copy(requirePassword = true, appPassword = password))
                    else store.saveSettings(store.settings.copy(requirePassword = false))
                    password = ""
                    passwordDialog = false
                }) { Text("确认") }
            },
            dismissButton = {
                TextButton(onClick = {
                    password = ""
                    passwordDialog = false
                }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun LockScreen(store: JinianriStore, onUnlock: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(bg(store)).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Lock, null, tint = AppColors.PrimaryLight, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(24.dp))
        Text("纪念日已锁定", color = textPrimary(store), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text("输入密码解锁", color = AppColors.TextSecondary, modifier = Modifier.padding(top = 8.dp))
        Row(Modifier.padding(24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { index ->
                Box(Modifier.size(20.dp).clip(CircleShape).border(2.dp, if (error) AppColors.Error else if (index < pin.length) AppColors.Accent else AppColors.TextTertiary, CircleShape).background(if (index < pin.length) AppColors.Accent else Color.Transparent))
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(300.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items((1..9).map { it.toString() } + listOf("", "0", "⌫")) { key ->
                if (key.isBlank()) Spacer(Modifier.size(64.dp)) else NumberButton(key, store) {
                    if (key == "⌫") pin = pin.dropLast(1) else if (pin.length < 4) pin += key
                    if (pin.length == 4) {
                        if (pin == store.settings.appPassword) onUnlock() else {
                            error = true
                            pin = ""
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleButton(icon: ImageVector, store: JinianriStore, tint: Color = textPrimary(store), action: () -> Unit) {
    IconButton(onClick = action, modifier = Modifier.size(38.dp).clip(CircleShape).background(if (store.settings.darkMode) AppColors.SecondaryBgDark else AppColors.SecondaryBgLight)) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SearchBox(value: String, onChange: (String) -> Unit) {
    Row(Modifier.padding(horizontal = 20.dp, vertical = 6.dp).clip(RoundedCornerShape(12.dp)).background(AppColors.SecondaryBgLight).padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Search, null, tint = AppColors.TextSecondary, modifier = Modifier.size(18.dp))
        OutlinedTextField(value, onChange, placeholder = { Text("搜索名称、备注或标签...") }, singleLine = true, modifier = Modifier.weight(1f))
        if (value.isNotEmpty()) Icon(Icons.Default.Close, null, tint = AppColors.TextTertiary, modifier = Modifier.clickable { onChange("") })
    }
}

@Composable
private fun FilterChip(label: String, icon: ImageVector, selected: Boolean, action: () -> Unit) {
    Row(Modifier.clip(RoundedCornerShape(50)).background(if (selected) AppColors.Primary else Color.Transparent).border(1.dp, if (selected) Color.Transparent else AppColors.TextTertiary.copy(alpha = 0.3f), RoundedCornerShape(50)).clickable(onClick = action).padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = if (selected) Color.White else AppColors.TextSecondary, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(5.dp))
        Text(label, color = if (selected) Color.White else AppColors.TextSecondary, fontSize = 13.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
private fun EmptyState(onTemplate: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.FavoriteBorder, null, tint = AppColors.PrimaryLight, modifier = Modifier.size(64.dp))
        Text("还没有纪念日", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 18.dp))
        Text("点击下方按钮或使用模板\n记录你生命中的重要日子", color = AppColors.TextSecondary, textAlign = TextAlign.Center, lineHeight = 22.sp, modifier = Modifier.padding(12.dp))
        Button(onClick = onTemplate, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Primary), border = BorderStroke(1.5.dp, AppColors.Primary)) {
            Icon(Icons.Default.FilterVintage, null)
            Spacer(Modifier.width(8.dp))
            Text("从模板创建")
        }
    }
}

@Composable
private fun StatsBar(days: List<MemorialDay>) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatBadge("全部", days.size, AppColors.Primary, Modifier.weight(1f))
        StatBadge("倒数", days.count { it.dayType == DayType.Countdown }, AppColors.Info, Modifier.weight(1f))
        StatBadge("正数", days.count { it.dayType == DayType.Countup }, AppColors.Success, Modifier.weight(1f))
    }
}

@Composable
private fun StatBadge(title: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.08f)).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$count", color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(title, color = AppColors.TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun UpcomingSection(days: List<MemorialDay>, store: JinianriStore) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, null, tint = AppColors.Warning, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("即将到期", color = textPrimary(store), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            Text("共 ${days.size} 项", color = AppColors.TextSecondary, fontSize = 13.sp)
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(days) { day ->
                Column(Modifier.width(140.dp).clip(RoundedCornerShape(14.dp)).background(AppColors.SecondaryBgLight).padding(12.dp)) {
                    Text(day.title, maxLines = 1, overflow = TextOverflow.Ellipsis, color = textPrimary(store), fontWeight = FontWeight.SemiBold)
                    Text("剩余 ${day.displayDayCount()} 天", color = AppColors.Accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(day.subtitle(), color = AppColors.TextSecondary, fontSize = 11.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun FloatingCircle(icon: ImageVector, size: androidx.compose.ui.unit.Dp, color: Color, action: () -> Unit) {
    Box(Modifier.size(size).shadow(6.dp, CircleShape).clip(CircleShape).background(color).clickable(onClick = action), contentAlignment = Alignment.Center) {
        Icon(icon, null, tint = Color.White)
    }
}

@Composable
private fun PreviewCard(day: MemorialDay) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.fillMaxWidth().height(200.dp).padding(top = 8.dp).shadow(day.shadowRadius.dp, RoundedCornerShape(day.cornerRadius.dp)).clip(RoundedCornerShape(day.cornerRadius.dp)).background(cardBrush(day)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(iconFor(day.icon), null, tint = hexColor(day.textColorHex).copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
            Text(day.title.ifBlank { "预览" }, color = hexColor(day.textColorHex), fontSize = day.fontSize.sp, fontWeight = FontWeight.SemiBold)
            Text("99", color = hexColor(day.textColorHex), fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text(if (day.dayType == DayType.Countdown) "天后" else "天前", color = hexColor(day.textColorHex).copy(alpha = 0.7f), fontSize = 14.sp)
        }
        Text("实时预览", color = AppColors.TextTertiary, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
    }
}

@Composable
private fun SectionCard(title: String, store: JinianriStore, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(14.dp)).background(if (store.settings.darkMode) AppColors.CardDark else AppColors.CardLight).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(title, color = textPrimary(store), fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun SwitchRow(title: String, subtitle: String, checked: Boolean, store: JinianriStore, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = textPrimary(store), fontSize = 15.sp)
            if (subtitle.isNotBlank()) Text(subtitle, color = AppColors.TextSecondary, fontSize = 11.sp)
        }
        Switch(checked, onChange)
    }
}

@Composable
private fun Pill(text: String, selected: Boolean, action: () -> Unit) {
    Text(text, color = if (selected) Color.White else AppColors.TextSecondary, fontSize = 13.sp, modifier = Modifier.clip(RoundedCornerShape(50)).background(if (selected) AppColors.Primary else Color.Transparent).border(1.dp, AppColors.TextTertiary.copy(alpha = 0.3f), RoundedCornerShape(50)).clickable(onClick = action).padding(horizontal = 14.dp, vertical = 7.dp))
}

@Composable
private fun ColorTextField(label: String, value: String, onChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(28.dp).clip(CircleShape).background(hexColor(value)).border(1.dp, AppColors.TextTertiary, CircleShape))
        Spacer(Modifier.width(12.dp))
        OutlinedTextField(value, onChange, label = { Text(label) }, singleLine = true, modifier = Modifier.width(150.dp))
    }
}

@Composable
private fun SliderRow(label: String, value: Float, min: Float, max: Float, onChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.roundToInt()}", color = AppColors.TextSecondary, fontSize = 12.sp)
        Slider(value, onValueChange = onChange, valueRange = min..max)
    }
}

@Composable
private fun Segmented(items: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppColors.SecondaryBgLight).padding(3.dp)) {
        items.forEachIndexed { i, item ->
            Text(item, color = if (selected == i) Color.White else AppColors.TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(if (selected == i) AppColors.Primary else Color.Transparent).clickable { onSelect(i) }.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun TimeBlock(value: Int, unit: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(color).padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(unit, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
    }
}

@Composable
private fun CalculatorBody(store: JinianriStore, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateInput(label: String, date: LocalDate, onChange: (LocalDate) -> Unit) {
    var show by remember { mutableStateOf(false) }
    val picker = rememberDatePickerState(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppColors.CardLight).clickable { show = true }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarMonth, null, tint = AppColors.Accent)
        Spacer(Modifier.width(12.dp))
        Text(label)
        Spacer(Modifier.weight(1f))
        Text(date.format(DateTimeFormatter.ofPattern("yyyy年M月d日")), color = AppColors.TextSecondary)
    }
    if (show) {
        DatePickerDialog(onDismissRequest = { show = false }, confirmButton = {
            TextButton(onClick = {
                picker.selectedDateMillis?.let { onChange(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()) }
                show = false
            }) { Text("确认") }
        }, dismissButton = { TextButton(onClick = { show = false }) { Text("取消") } }) { DatePicker(picker) }
    }
}

@Composable
private fun CalculateButton(action: () -> Unit) {
    Button(onClick = action, colors = ButtonDefaults.buttonColors(containerColor = AppColors.Accent), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
        Icon(Icons.Default.PlayArrow, null, Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text("计算", fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ResultCard(store: JinianriStore, text: String) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(AppColors.Accent.copy(alpha = 0.12f), AppColors.PrimaryLight.copy(alpha = 0.08f)))).border(1.dp, AppColors.Accent.copy(alpha = 0.2f), RoundedCornerShape(14.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.CheckCircle, null, tint = AppColors.Success)
        Text(text, color = textPrimary(store), fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 30.sp)
    }
}

@Composable
private fun LinkRow(label: String, icon: ImageVector, color: Color = AppColors.Primary, action: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = action).padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = color, fontSize = 14.sp)
        Spacer(Modifier.weight(1f))
        Icon(icon, null, tint = AppColors.TextTertiary, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String, store: JinianriStore, valueColor: Color = textPrimary(store)) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, color = AppColors.TextSecondary, fontSize = 14.sp)
        Spacer(Modifier.weight(1f))
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun NumberButton(key: String, store: JinianriStore, action: () -> Unit) {
    Box(Modifier.aspectRatio(1f).clip(CircleShape).background(if (store.settings.darkMode) AppColors.SecondaryBgDark else AppColors.SecondaryBgLight).clickable(onClick = action), contentAlignment = Alignment.Center) {
        Text(key, color = textPrimary(store), fontSize = 28.sp, fontWeight = FontWeight.Medium)
    }
}

private fun sendTestNotification(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
    val notification = NotificationCompat.Builder(context, "jinianri")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("📅 纪念日提醒测试")
        .setContentText("这是一个测试通知！如果看到这条消息，说明通知功能正常工作。")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    NotificationManagerCompat.from(context).notify(1001, notification)
}

private fun countWorkdays(a: LocalDate, b: LocalDate): Int {
    val start = minOf(a, b)
    val end = maxOf(a, b)
    var date = start
    var count = 0
    while (date.isBefore(end)) {
        if (date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY) count++
        date = date.plusDays(1)
    }
    return count
}

private fun Double.format(): String = if (this % 1.0 == 0.0) this.toInt().toString() else "%.2f".format(this)

private fun cardBrush(day: MemorialDay): Brush {
    val start = hexColor(day.backgroundColorHex)
    val end = hexColor(day.backgroundEndColorHex ?: day.backgroundColorHex)
    return if (day.showGradient) Brush.linearGradient(listOf(start, end), start = Offset.Zero, end = Offset(900f, 900f)) else Brush.linearGradient(listOf(start, start))
}

private fun hexColor(hex: String): Color {
    return runCatching {
        val clean = hex.trim().removePrefix("#")
        Color(android.graphics.Color.parseColor("#$clean"))
    }.getOrDefault(AppColors.Primary)
}

@Composable private fun bg(store: JinianriStore) = if (store.settings.darkMode) AppColors.BackgroundDark else AppColors.BackgroundLight
@Composable private fun card(store: JinianriStore) = if (store.settings.darkMode) AppColors.CardDark else AppColors.CardLight
@Composable private fun textPrimary(store: JinianriStore) = if (store.settings.darkMode) AppColors.TextPrimaryDark else AppColors.TextPrimaryLight

private fun iconFor(name: String): ImageVector = when (name) {
    "heart", "health" -> Icons.Default.Favorite
    "star" -> Icons.Default.Star
    "gift" -> Icons.Default.FavoriteBorder
    "sparkles", "fire", "sun", "moon", "balloon", "crown" -> Icons.Default.FilterVintage
    "book" -> Icons.Default.School
    "work" -> Icons.Default.BusinessCenter
    "airplane" -> Icons.Default.AirplanemodeActive
    "bell" -> Icons.Default.Notifications
    "card" -> Icons.Default.CreditCard
    "money" -> Icons.Default.Work
    else -> Icons.Default.Star
}
