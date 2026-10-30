package com.salpakan.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF020D1C)
private val Navy = Color(0xFF061A32)
private val Panel = Color(0xFF081B32)
private val PanelHi = Color(0xFF102A49)
private val Gold = Color(0xFFE7B64B)
private val GoldBright = Color(0xFFFFD979)
private val GoldDim = Color(0xFF8D6B31)
private val BlueArmy = Color(0xFF174D80)
private val BlueTile = Color(0xFF12365A)
private val RedArmy = Color(0xFF8C272B)
private val RedTile = Color(0xFF4A2028)
private val Muted = Color(0xFFA9B8C9)
private val White = Color(0xFFF4F0E6)

private enum class Page { SPLASH, HOME, ONLINE, CREATE, LOCAL, BOARD, RANKS, RULES, VICTORY, PROFILE, LEADERBOARD, SETTINGS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(2, 13, 28)
        window.navigationBarColor = android.graphics.Color.rgb(2, 13, 28)
        setContent { SalpakanApp() }
    }
}

@Composable private fun SalpakanApp() {
    var page by remember { mutableStateOf(Page.SPLASH) }
    var room by remember { mutableStateOf("My Room") }
    var mode by remember { mutableStateOf("Classic") }
    var players by remember { mutableIntStateOf(2) }
    var selected by remember { mutableIntStateOf(-1) }
    var reveal by remember { mutableStateOf(false) }
    var sound by remember { mutableStateOf(true) }
    var music by remember { mutableStateOf(true) }
    var vibration by remember { mutableStateOf(true) }
    MaterialTheme(colorScheme = darkColorScheme(primary = Gold, onPrimary = Ink, secondary = GoldBright, background = Ink, surface = Panel)) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0A2341), Ink, Color(0xFF010812))))) {
            when (page) {
                Page.SPLASH -> Splash { page = Page.HOME }
                Page.HOME -> Home({ page = Page.LOCAL }, { page = Page.ONLINE }, { page = Page.LOCAL }, { page = Page.RANKS }, { page = Page.RULES }, { page = Page.SETTINGS }, { page = Page.PROFILE }, { page = Page.LEADERBOARD })
                Page.ONLINE -> Online({ page = Page.HOME }, { page = Page.CREATE }, { page = Page.BOARD })
                Page.CREATE -> CreateRoom(room, { room = it }, mode, { mode = it }, players, { players = it }, { page = Page.ONLINE }, { page = Page.BOARD })
                Page.LOCAL -> Local({ page = Page.HOME }, { page = Page.BOARD })
                Page.BOARD -> Board(selected, { selected = if (selected == it) -1 else it }, reveal, { reveal = !reveal }, { page = Page.HOME }, { page = Page.RANKS }, { page = Page.VICTORY })
                Page.RANKS -> Ranks { page = Page.HOME }
                Page.RULES -> Rules { page = Page.HOME }
                Page.VICTORY -> Victory({ selected = -1; page = Page.BOARD }, { page = Page.HOME })
                Page.PROFILE -> Profile { page = Page.HOME }
                Page.LEADERBOARD -> Leaderboard { page = Page.HOME }
                Page.SETTINGS -> Settings(sound, { sound = it }, music, { music = it }, vibration, { vibration = it }, { page = Page.HOME })
            }
        }
    }
}

@Composable private fun GoldButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick, modifier.heightIn(min = 48.dp), shape = RoundedCornerShape(9.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink), elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)) {
        Text(label, fontWeight = FontWeight.Black, letterSpacing = .4.sp, fontSize = 13.sp)
    }
}
@Composable private fun OutlineButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick, modifier.heightIn(min = 46.dp), shape = RoundedCornerShape(9.dp), border = androidx.compose.foundation.BorderStroke(1.dp, GoldDim), colors = ButtonDefaults.outlinedButtonColors(contentColor = White)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}
@Composable private fun Header(title: String, back: (() -> Unit)? = null, trailing: (@Composable () -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
        if (back != null) Text("‹", Modifier.clickable { back() }.padding(end = 14.dp), color = GoldBright, fontSize = 34.sp)
        Text(title, Modifier.weight(1f), color = GoldBright, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        if (trailing != null) trailing()
    }
}
@Composable private fun Frame(title: String, back: () -> Unit, body: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Header(title, back)
        HorizontalDivider(color = GoldDim.copy(.65f))
        Spacer(Modifier.height(12.dp))
        body()
    }
}
@Composable private fun GoldPanel(modifier: Modifier = Modifier, body: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), color = Panel.copy(.97f), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, GoldDim.copy(.85f))) {
        Column(Modifier.padding(13.dp), content = body)
    }
}
@Composable private fun Label(text: String) { Text(text, color = GoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }

@Composable private fun Splash(go: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(Modifier.size(168.dp).clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFF9B702D), Color(0xFF18243A), Ink))), contentAlignment = Alignment.Center) { Text("⚔", color = GoldBright, fontSize = 76.sp) }
        Spacer(Modifier.height(22.dp))
        Text("SALPAKAN", color = GoldBright, fontSize = 36.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Text("GAMES OF THE GENERALS", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(12.dp))
        Text("STRATEGY  •  BLUFF  •  VICTORY", color = GoldBright, fontSize = 10.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(44.dp))
        GoldButton("ENTER THE BATTLEFIELD", Modifier.fillMaxWidth(), go)
    }
}

@Composable private fun Home(play: () -> Unit, online: () -> Unit, local: () -> Unit, ranks: () -> Unit, rules: () -> Unit, settings: () -> Unit, profile: () -> Unit, leaderboard: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 22.dp)) {
        item {
            GoldPanel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(43.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Gold, GoldDim))), contentAlignment = Alignment.Center) { Text("♟", color = Ink, fontSize = 25.sp) }
                    Column(Modifier.weight(1f).padding(start = 10.dp)) { Text("PLAYER", color = White, fontSize = 13.sp, fontWeight = FontWeight.Bold); Text("Guest", color = Muted, fontSize = 10.sp) }
                    Text("⚙   ♟   ◉", color = GoldBright, fontSize = 19.sp)
                }
            }
            Spacer(Modifier.height(18.dp))
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("☼", color = GoldBright, fontSize = 43.sp)
                Text("SALPAKAN", color = GoldBright, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("GAMES OF THE GENERALS", color = Gold, fontSize = 10.sp, letterSpacing = 2.sp)
                Text("STRATEGY  •  BLUFF  •  VICTORY", color = GoldBright, fontSize = 9.sp, letterSpacing = 1.5.sp)
            }
            Spacer(Modifier.height(19.dp))
            GoldPanel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚔", color = GoldBright, fontSize = 35.sp)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) { Text("PLAY GAME", color = White, fontSize = 19.sp, fontWeight = FontWeight.Bold); Text("Local or Online Match", color = Muted, fontSize = 11.sp) }
                    Text("›", color = GoldBright, fontSize = 28.sp)
                }
                Spacer(Modifier.height(10.dp))
                GoldButton("PLAY NOW", Modifier.fillMaxWidth(), play)
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                MenuCard("◎", "ONLINE MULTIPLAYER", "Join or Create Room", Modifier.weight(1f), online)
                MenuCard("♟", "LOCAL MULTIPLAYER", "Play on One Device", Modifier.weight(1f), local)
            }
            Spacer(Modifier.height(9.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MenuCard("▤", "HOW TO PLAY", "", Modifier.weight(1f), rules)
                MenuCard("★", "RANKS & RULES", "", Modifier.weight(1f), ranks)
                MenuCard("⚙", "SETTINGS", "", Modifier.weight(1f), settings)
            }
            Spacer(Modifier.height(9.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MenuCard("♙", "MY PROFILE", "", Modifier.weight(1f), profile)
                MenuCard("🏆", "LEADERBOARD", "", Modifier.weight(1f), leaderboard)
            }
            Spacer(Modifier.height(12.dp))
            GoldPanel {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Stat("● ONLINE", "—"); Stat("TOTAL MATCHES", "—"); Stat("YOUR WINS", "0")
                }
            }
        }
    }
}
@Composable private fun MenuCard(icon: String, title: String, subtitle: String, modifier: Modifier, click: () -> Unit) {
    GoldPanel(modifier.clickable { click() }) {
        Text(icon, color = GoldBright, fontSize = 24.sp)
        Text(title, color = White, fontSize = 9.sp, fontWeight = FontWeight.Bold, lineHeight = 12.sp)
        if (subtitle.isNotEmpty()) Text(subtitle, color = Muted, fontSize = 9.sp)
    }
}
@Composable private fun Stat(label: String, value: String) { Column { Text(label, color = Muted, fontSize = 8.sp); Text(value, color = GoldBright, fontSize = 18.sp, fontWeight = FontWeight.Bold) } }

@Composable private fun Online(back: () -> Unit, create: () -> Unit, join: () -> Unit) = Frame("ONLINE MULTIPLAYER", back) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { GoldButton("JOIN ROOM", Modifier.weight(1f), join); OutlineButton("CREATE ROOM", Modifier.weight(1f), create) }
    Spacer(Modifier.height(14.dp)); Label("PUBLIC ROOMS")
    listOf("General's Room" to "2/2", "Pinoy Warriors" to "1/2", "Bayanihan" to "1/2", "Salpakan Pro" to "2/2", "Tropa Lang" to "1/2", "Laban Pilipinas" to "2/2").forEach { (name, count) ->
        GoldPanel(Modifier.padding(top = 8.dp).clickable { if (count != "2/2") join() }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("♟", color = Gold, fontSize = 22.sp)
                Column(Modifier.weight(1f).padding(start = 10.dp)) { Text(name, color = White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp); Text("CLASSIC  •  PUBLIC ROOM", color = Muted, fontSize = 9.sp) }
                Text(count, color = if (count == "2/2") Muted else GoldBright, fontSize = 11.sp); Text("  ›", color = Gold, fontSize = 23.sp)
            }
        }
    }
    Spacer(Modifier.weight(1f))
    OutlineButton("⟳   REFRESH ROOMS", Modifier.fillMaxWidth(), {})
    Text("Room list is a visual preview until Firebase is connected.", color = Muted, fontSize = 9.sp, modifier = Modifier.padding(top = 7.dp))
}

@Composable private fun CreateRoom(name: String, setName: (String) -> Unit, mode: String, setMode: (String) -> Unit, players: Int, setPlayers: (Int) -> Unit, back: () -> Unit, create: () -> Unit) = Frame("CREATE ROOM", back) {
    Label("ROOM NAME")
    OutlinedTextField(name, setName, Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gold, unfocusedBorderColor = GoldDim, focusedTextColor = White, unfocusedTextColor = White))
    Spacer(Modifier.height(15.dp)); Label("GAME MODE")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Classic", "Custom").forEach { v -> if (mode == v) GoldButton(v, Modifier.weight(1f)) { setMode(v) } else OutlineButton(v, Modifier.weight(1f)) { setMode(v) } } }
    Spacer(Modifier.height(15.dp)); Label("PASSWORD (OPTIONAL)")
    var password by remember { mutableStateOf("") }
    OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Enter password") }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gold, unfocusedBorderColor = GoldDim, focusedTextColor = White, unfocusedTextColor = White))
    Spacer(Modifier.height(15.dp)); Label("MAX PLAYERS")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf(2, 4).forEach { v -> if (players == v) GoldButton(v.toString(), Modifier.weight(1f)) { setPlayers(v) } else OutlineButton(v.toString(), Modifier.weight(1f)) { setPlayers(v) } } }
    Spacer(Modifier.weight(1f)); GoldButton("＋   CREATE ROOM", Modifier.fillMaxWidth(), create)
    Text("Online room creation needs Firebase configuration.", color = Muted, fontSize = 9.sp, modifier = Modifier.padding(top = 7.dp))
}

@Composable private fun Local(back: () -> Unit, start: () -> Unit) = Frame("LOCAL MULTIPLAYER", back) {
    GoldPanel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("♟", color = GoldBright, fontSize = 31.sp)
            Column(Modifier.weight(1f).padding(start = 12.dp)) { Text("2 PLAYERS (SAME DEVICE)", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp); Text("Take turns on one device", color = Muted, fontSize = 10.sp) }
            Text("›", color = Gold, fontSize = 25.sp)
        }
        Spacer(Modifier.height(10.dp)); GoldButton("PASS AND PLAY", Modifier.fillMaxWidth(), start)
    }
    Spacer(Modifier.height(12.dp))
    GoldPanel { Label("HOW LOCAL PLAY WORKS"); Text("Each player prepares an army, hides their ranks, then takes turns moving pieces. Pass the device after each turn so the opponent cannot see your setup.", color = Muted, fontSize = 11.sp, lineHeight = 17.sp) }
    Spacer(Modifier.weight(1f))
    Text("STRATEGY  •  BLUFF  •  VICTORY", Modifier.fillMaxWidth(), color = Gold, fontSize = 9.sp, textAlign = TextAlign.Center)
}

@Composable private fun Board(selected: Int, select: (Int) -> Unit, reveal: Boolean, toggle: () -> Unit, back: () -> Unit, info: () -> Unit, win: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 7.dp)) {
        Header("LOCAL MATCH", back) { TextButton(onClick = toggle) { Text(if (reveal) "HIDE" else "REVEAL", color = Gold) } }
        GoldPanel {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text("☼ YOUR TURN", color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 12.sp); Text("BLUE ARMY", color = Muted, fontSize = 9.sp) }
                Text("TURN  3", color = White, fontSize = 11.sp)
                Column(horizontalAlignment = Alignment.End) { Text("ENEMY", color = Color(0xFFFF9B9B), fontWeight = FontWeight.Bold, fontSize = 12.sp); Text("RED ARMY", color = Muted, fontSize = 9.sp) }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("SALPAKAN BATTLEFIELD", Modifier.fillMaxWidth(), color = GoldBright, fontSize = 9.sp, letterSpacing = 1.sp, textAlign = TextAlign.Center)
        LazyVerticalGrid(GridCells.Fixed(8), Modifier.fillMaxWidth().weight(1f).border(1.dp, GoldDim, RoundedCornerShape(4.dp)), userScrollEnabled = false) {
            gridItems(72) { index ->
                val row = index / 8; val col = index % 8
                val blue = row in 0..2; val red = row in 6..8
                val piece = (blue || red) && (row * 3 + col) % 5 != 0
                val tile = when { selected == index -> Gold.copy(.78f); blue -> if ((row + col) % 2 == 0) BlueTile else Color(0xFF0C2B4A); red -> if ((row + col) % 2 == 0) RedTile else Color(0xFF381C25); else -> if ((row + col) % 2 == 0) Color(0xFF182A3C) else Color(0xFF101F30) }
                Box(Modifier.aspectRatio(1f).background(tile).border(.45.dp, Gold.copy(.18f)).clickable { select(index) }, contentAlignment = Alignment.Center) {
                    if (piece) Box(Modifier.fillMaxSize().padding(2.dp).clip(RoundedCornerShape(4.dp)).background(if (blue) BlueArmy else RedArmy).border(1.dp, if (blue) Color(0xFF6EA6D4) else Color(0xFFD36A61), RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                        Text(if (reveal) listOf("✦", "♜", "★", "♟", "⚑")[(row + col) % 5] else "?", color = if (reveal) GoldBright else White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    } else if ((row == 0 && col == 4) || (row == 8 && col == 3)) Text("⚑", color = GoldBright, fontSize = 17.sp)
                }
            }
        }
        Text("A     B     C     D     E     F     G     H", Modifier.fillMaxWidth(), color = GoldBright, textAlign = TextAlign.Center, fontSize = 9.sp)
        Spacer(Modifier.height(7.dp))
        GoldPanel { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("☼  YOU", color = Color(0xFF8CCAFF), fontWeight = FontWeight.Bold, fontSize = 11.sp); Text("VS", color = GoldBright, fontWeight = FontWeight.Black); Text("ENEMY  ☼", color = Color(0xFFFF9B9B), fontWeight = FontWeight.Bold, fontSize = 11.sp) } }
        Spacer(Modifier.height(7.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlineButton("PIECE INFO", Modifier.weight(1f), info); OutlineButton("MENU", Modifier.weight(1f), back) }
    }
}

private val rankRows = listOf(
    "Five-Star General" to "Highest rank. Defeats every ordinary rank.",
    "Four-Star General" to "Second-highest command rank.",
    "Three-Star General" to "Senior general officer.",
    "Colonel" to "Senior field officer.",
    "Lieutenant Colonel" to "Field-grade officer.",
    "Major" to "Field-grade officer.",
    "Captain" to "Company-grade officer.",
    "First Lieutenant" to "Junior officer.",
    "Second Lieutenant" to "Junior officer.",
    "Sergeant" to "Non-commissioned officer.",
    "Private" to "Lowest regular rank. Defeats Spy when attacking.",
    "Spy" to "Defeats Five-Star General only when Spy attacks.",
    "Flag" to "Cannot move. Capture it to win."
)
@Composable private fun Ranks(back: () -> Unit) = Frame("PIECE INFO / RANK GUIDE", back) {
    LazyColumn { items(rankRows) { (name, desc) ->
        GoldPanel(Modifier.padding(bottom = 7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(38.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Gold, GoldDim))), contentAlignment = Alignment.Center) { Text(if (name == "Flag") "⚑" else "★", color = Ink, fontSize = 20.sp) }
                Column(Modifier.padding(start = 12.dp)) { Text(name, color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 13.sp); Text(desc, color = Muted, fontSize = 10.sp) }
            }
        }
    } }
}
@Composable private fun Rules(back: () -> Unit) = Frame("HOW TO PLAY", back) {
    val rules = listOf(
        "1. OBJECTIVE" to "Capture the enemy Flag by eliminating the opposing army or reaching the Flag using the selected game rules.",
        "2. MOVEMENT" to "Move one square at a time in an orthogonal direction. Pieces cannot jump over other pieces. The Flag cannot move.",
        "3. COMBAT RULES" to "When attacking an occupied square, compare ranks. The higher rank wins. Equal ranks are both removed.",
        "4. SPECIAL SPY RULE" to "The Spy defeats the Five-Star General only when the Spy attacks. The Spy loses when attacked, including by a Private.",
        "5. HIDDEN PIECES" to "Your opponent cannot see your piece ranks. Use strategy, memory, and bluffing."
    )
    LazyColumn {
        items(rules) { (heading, body) -> GoldPanel(Modifier.padding(bottom = 9.dp)) { Text(heading, color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 13.sp); Spacer(Modifier.height(5.dp)); Text(body, color = White, fontSize = 11.sp, lineHeight = 17.sp) } }
        item { GoldButton("GOT IT", Modifier.fillMaxWidth(), back) }
    }
}
@Composable private fun Victory(again: () -> Unit, home: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("☼", color = GoldBright, fontSize = 62.sp); Text("VICTORY!", color = GoldBright, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp); Text("You captured the enemy flag!", color = White, fontSize = 13.sp)
        Spacer(Modifier.height(28.dp)); GoldButton("PLAY AGAIN", Modifier.fillMaxWidth(), again); Spacer(Modifier.height(10.dp)); OutlineButton("BACK TO MENU", Modifier.fillMaxWidth(), home)
    }
}
@Composable private fun Profile(back: () -> Unit) = Frame("MY PROFILE", back) {
    GoldPanel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(64.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Gold, Navy))), contentAlignment = Alignment.Center) { Text("♟", color = Ink, fontSize = 35.sp) }
            Column(Modifier.padding(start = 12.dp)) { Text("PLAYER", color = Muted, fontSize = 10.sp); Text("Guest Commander", color = GoldBright, fontSize = 19.sp, fontWeight = FontWeight.Bold); Text("LEVEL 1  •  RECRUIT", color = Muted, fontSize = 10.sp) }
        }
        Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { ProfileStat("WINS", "0"); ProfileStat("LOSSES", "0"); ProfileStat("WIN RATE", "—") }
    }
    Spacer(Modifier.height(12.dp)); GoldPanel { Text("◷   MATCH HISTORY", color = White, fontWeight = FontWeight.Bold); Text("Your recent battles will appear here.", color = Muted, fontSize = 10.sp) }
}
@Composable private fun ProfileStat(label: String, value: String) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, color = GoldBright, fontSize = 21.sp, fontWeight = FontWeight.Bold); Text(label, color = Muted, fontSize = 9.sp) } }
@Composable private fun Leaderboard(back: () -> Unit) = Frame("LEADERBOARD", back) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { GoldButton("GLOBAL", Modifier.weight(1f), {}); OutlineButton("FRIENDS", Modifier.weight(1f), {}) }
    Spacer(Modifier.height(10.dp))
    GoldPanel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("RANK", color = Gold); Text("PLAYER", color = Gold); Text("WINS", color = Gold) }
        listOf("General2025", "PinoyStrategy", "SalpakanKing", "TropaLang", "LazadaBoy", "You").forEachIndexed { index, name ->
            HorizontalDivider(color = GoldDim.copy(.35f))
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text((index + 1).toString(), color = GoldBright, fontWeight = FontWeight.Bold); Text("♟  $name", color = White, fontSize = 12.sp); Text(if (index == 5) "0" else "—", color = GoldBright) }
        }
    }
    Text("Sample leaderboard layout. Live rankings require online accounts and recorded matches.", color = Muted, fontSize = 9.sp)
}
@Composable private fun Settings(sound: Boolean, setSound: (Boolean) -> Unit, music: Boolean, setMusic: (Boolean) -> Unit, vibration: Boolean, setVibration: (Boolean) -> Unit, back: () -> Unit) = Frame("SETTINGS", back) {
    GoldPanel { Label("GAME SETTINGS"); SettingRow("Sound effects", sound, setSound); SettingRow("Music", music, setMusic); SettingRow("Vibration", vibration, setVibration) }
    Spacer(Modifier.height(12.dp))
    GoldPanel { Label("ACCOUNT"); Text("Link Firebase (Online Play)", color = White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)); Text("Connect your account to create rooms, play online, and save match history.", color = Muted, fontSize = 10.sp) }
    Spacer(Modifier.height(12.dp)); OutlineButton("SIGN OUT", Modifier.fillMaxWidth(), back)
}
@Composable private fun SettingRow(label: String, value: Boolean, change: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), color = White, fontSize = 13.sp)
        Switch(value, change, colors = SwitchDefaults.colors(checkedThumbColor = GoldBright, checkedTrackColor = GoldDim, uncheckedThumbColor = Muted))
    }
}
