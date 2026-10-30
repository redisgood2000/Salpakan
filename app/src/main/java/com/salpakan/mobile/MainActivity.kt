package com.salpakan.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF06172B)
private val Deep = Color(0xFF031020)
private val Panel = Color(0xFF0B213A)
private val Panel2 = Color(0xFF102B49)
private val Gold = Color(0xFFE8B94F)
private val GoldLight = Color(0xFFFFD978)
private val Blue = Color(0xFF245B91)
private val Red = Color(0xFF8F292E)
private val Muted = Color(0xFF9FB0C3)

private enum class Page { HOME, ONLINE, CREATE, LOCAL, BOARD, RANKS, RULES, VICTORY, PROFILE, LEADERBOARD, SETTINGS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SalpakanApp() }
    }
}

@Composable
private fun SalpakanApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var room by remember { mutableStateOf("My Room") }
    var mode by remember { mutableStateOf("Classic") }
    var players by remember { mutableIntStateOf(2) }
    var selected by remember { mutableIntStateOf(-1) }
    var hidden by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(true) }
    var music by remember { mutableStateOf(true) }
    var vibration by remember { mutableStateOf(true) }
    MaterialTheme(colorScheme = darkColorScheme(primary = Gold, onPrimary = Deep, secondary = GoldLight, background = Deep, surface = Panel)) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Navy, Deep)))) {
            when (page) {
                Page.HOME -> Home({ page = Page.LOCAL }, { page = Page.ONLINE }, { page = Page.LOCAL }, { page = Page.RANKS }, { page = Page.RULES }, { page = Page.SETTINGS }, { page = Page.PROFILE }, { page = Page.LEADERBOARD })
                Page.ONLINE -> Online({ page = Page.HOME }, { page = Page.CREATE }, { page = Page.BOARD })
                Page.CREATE -> CreateRoom(room, { room = it }, mode, { mode = it }, players, { players = it }, { page = Page.ONLINE }, { page = Page.BOARD })
                Page.LOCAL -> Local({ page = Page.HOME }, { page = Page.BOARD })
                Page.BOARD -> Board(selected, { selected = if (selected == it) -1 else it }, hidden, { hidden = !hidden }, { page = Page.HOME }, { page = Page.RANKS }, { page = Page.VICTORY })
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
    Button(onClick, modifier.heightIn(min = 48.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Deep)) {
        Text(label, fontWeight = FontWeight.Bold, letterSpacing = .3.sp)
    }
}
@Composable private fun DarkButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick, modifier.heightIn(min = 48.dp), shape = RoundedCornerShape(10.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Gold.copy(.6f)), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) {
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}
@Composable private fun Frame(title: String, back: (() -> Unit)? = null, body: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth().height(46.dp), verticalAlignment = Alignment.CenterVertically) {
            if (back != null) Text("‹", Modifier.clickable { back() }.padding(end = 14.dp), color = Gold, fontSize = 34.sp)
            Text(title, color = GoldLight, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        HorizontalDivider(color = Gold.copy(.25f))
        Spacer(Modifier.height(12.dp))
        body()
    }
}
@Composable private fun CardBox(modifier: Modifier = Modifier, body: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), color = Panel, shape = RoundedCornerShape(13.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Gold.copy(.3f))) {
        Column(Modifier.padding(13.dp), content = body)
    }
}
@Composable private fun Tile(icon: String, label: String, modifier: Modifier, click: () -> Unit) {
    Surface(modifier.clickable { click() }, color = Panel, shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Gold.copy(.3f))) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, color = Gold, fontSize = 23.sp)
            Text(label, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

@Composable private fun Home(play: () -> Unit, online: () -> Unit, local: () -> Unit, ranks: () -> Unit, rules: () -> Unit, settings: () -> Unit, profile: () -> Unit, leaderboard: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(vertical = 20.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("SALPAKAN", color = GoldLight, fontSize = 29.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Text("GAMES OF THE GENERALS", color = Gold, fontSize = 10.sp, letterSpacing = 2.sp)
                }
                Surface(color = Panel2, shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Gold)) { Text("PG", Modifier.padding(12.dp), color = GoldLight, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(18.dp))
            CardBox {
                Text("WELCOME, PLAYER", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                Text("Ready for your next battle?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("RANK: RECRUIT", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(14.dp))
            GoldButton("⚔   PLAY GAME", Modifier.fillMaxWidth(), play)
            Text("Choose your battlefield", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 7.dp, bottom = 10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                DarkButton("◎ ONLINE MULTIPLAYER", Modifier.weight(1f), online)
                DarkButton("♟ LOCAL MULTIPLAYER", Modifier.weight(1f), local)
            }
            Spacer(Modifier.height(11.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Tile("▤", "HOW TO PLAY", Modifier.weight(1f), rules)
                Tile("★", "RANKS & RULES", Modifier.weight(1f), ranks)
                Tile("⚙", "SETTINGS", Modifier.weight(1f), settings)
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Tile("🏆", "LEADERBOARD", Modifier.weight(1f), leaderboard)
                Tile("◉", "MY PROFILE", Modifier.weight(1f), profile)
            }
            Spacer(Modifier.height(15.dp))
            CardBox {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Stat("PLAYERS ONLINE", "—"); Stat("TOTAL MATCHES", "—"); Stat("YOUR WINS", "0")
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("STRATEGY  •  BLUFF  •  VICTORY", Modifier.fillMaxWidth(), color = Gold, fontSize = 11.sp, letterSpacing = 2.sp, textAlign = TextAlign.Center)
        }
    }
}
@Composable private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = GoldLight, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Muted, fontSize = 8.sp)
    }
}

@Composable private fun Online(back: () -> Unit, create: () -> Unit, join: () -> Unit) = Frame("ONLINE MULTIPLAYER", back) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GoldButton("JOIN ROOM", Modifier.weight(1f), join)
        DarkButton("CREATE ROOM", Modifier.weight(1f), create)
    }
    Spacer(Modifier.height(14.dp))
    Text("AVAILABLE ROOMS", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    listOf("General's Room" to "2/2", "Pinoy Warriors" to "1/2", "Bayanihan" to "1/2", "Salpakan Pro" to "2/2", "Tropa Lang" to "1/2", "Laban Pilipinas" to "2/2").forEach { (name, count) ->
        CardBox(Modifier.padding(top = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("♟", color = Gold, fontSize = 22.sp)
                Column(Modifier.weight(1f).padding(start = 10.dp)) {
                    Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("CLASSIC • PUBLIC ROOM", color = Muted, fontSize = 9.sp)
                }
                Text(count + "  ›", color = GoldLight)
            }
        }
    }
    Spacer(Modifier.weight(1f))
    Text("Preview rooms only • Firebase multiplayer is not connected yet.", color = Muted, fontSize = 10.sp)
}

@Composable private fun CreateRoom(name: String, setName: (String) -> Unit, mode: String, setMode: (String) -> Unit, players: Int, setPlayers: (Int) -> Unit, back: () -> Unit, create: () -> Unit) = Frame("CREATE ROOM", back) {
    Text("ROOM NAME", color = Gold, fontSize = 11.sp)
    OutlinedTextField(name, setName, Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(15.dp))
    Text("GAME MODE", color = Gold, fontSize = 11.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Classic", "Custom").forEach { v -> if (mode == v) GoldButton(v, Modifier.weight(1f)) { setMode(v) } else DarkButton(v, Modifier.weight(1f)) { setMode(v) } }
    }
    Spacer(Modifier.height(15.dp))
    Text("MAX PLAYERS", color = Gold, fontSize = 11.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(2, 4).forEach { v -> if (players == v) GoldButton("$v PLAYERS", Modifier.weight(1f)) { setPlayers(v) } else DarkButton("$v PLAYERS", Modifier.weight(1f)) { setPlayers(v) } }
    }
    Spacer(Modifier.height(15.dp))
    Text("ROOM TYPE", color = Gold, fontSize = 11.sp)
    Text("Public room • Anyone can join", color = Muted, fontSize = 12.sp)
    Spacer(Modifier.weight(1f))
    GoldButton("＋  CREATE ROOM", Modifier.fillMaxWidth(), create)
    Text("Firebase room creation is the next online integration step.", color = Muted, fontSize = 10.sp)
}

@Composable private fun Local(back: () -> Unit, start: () -> Unit) = Frame("LOCAL MULTIPLAYER", back) {
    CardBox {
        Text("2 PLAYERS • SAME DEVICE", color = GoldLight, fontWeight = FontWeight.Bold)
        Text("Take turns using one Android device.", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        GoldButton("START LOCAL MATCH", Modifier.fillMaxWidth(), start)
    }
    Spacer(Modifier.height(12.dp))
    CardBox {
        Text("PASS AND PLAY", color = GoldLight, fontWeight = FontWeight.Bold)
        Text("Keep both armies hidden and pass the device after each turn.", color = Muted, fontSize = 12.sp)
    }
}

@Composable private fun Board(selected: Int, onSelect: (Int) -> Unit, hidden: Boolean, toggle: () -> Unit, back: () -> Unit, info: () -> Unit, win: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("‹", Modifier.clickable { back() }.padding(end = 10.dp), color = Gold, fontSize = 32.sp)
            Column(Modifier.weight(1f)) { Text("LOCAL MATCH", color = GoldLight, fontWeight = FontWeight.Bold); Text("YOUR TURN • TURN 1", color = Muted, fontSize = 10.sp) }
            TextButton(onClick = toggle) { Text(if (hidden) "Reveal" else "Hide", color = Gold) }
            TextButton(onClick = info) { Text("INFO", color = Gold) }
        }
        CardBox {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🔵 BLUE ARMY", color = Color(0xFF8BC7FF), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Text("VS", color = Gold, fontWeight = FontWeight.Black)
                Text("RED ARMY 🔴", color = Color(0xFFFF9B9B), fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(GridCells.Fixed(8), Modifier.fillMaxWidth().weight(1f), userScrollEnabled = false) {
            items(72) { i ->
                val r = i / 8; val c = i % 8
                val blue = r < 3; val red = r > 5
                val piece = (blue || red) && (r + c) % 3 != 1
                Box(Modifier.aspectRatio(1f).padding(.5.dp).background(if (selected == i) Gold.copy(.7f) else if ((r + c) % 2 == 0) Color(0xFF153454) else Color(0xFF0D2744)).border(.5.dp, Gold.copy(.18f)).clickable { onSelect(i) }, contentAlignment = Alignment.Center) {
                    if (piece) Surface(color = if (blue) Blue else Red, shape = RoundedCornerShape(5.dp), modifier = Modifier.fillMaxSize().padding(2.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text(if (hidden) "?" else listOf("★", "✦", "♜", "♟")[(r + c) % 4], color = if (hidden) Color.White else GoldLight, fontSize = 17.sp, fontWeight = FontWeight.Bold) }
                    } else if (r == 0 && c == 4) Text("⚑", color = GoldLight, fontSize = 18.sp)
                }
            }
        }
        Text("A  B  C  D  E  F  G  H", Modifier.fillMaxWidth(), color = Gold, fontSize = 10.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        CardBox {
            Text("SELECT A PIECE, THEN A DESTINATION", color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("Board UI preview. Legal movement, combat and win validation still need implementation.", color = Muted, fontSize = 10.sp)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DarkButton("♜ RANK GUIDE", Modifier.weight(1f), info)
            GoldButton("END MATCH", Modifier.weight(1f), win)
        }
    }
}

private val ranks = listOf(
    "Five-Star General" to "Highest rank • can capture any piece except the Flag.",
    "Four-Star General" to "Second-highest command rank.",
    "Three-Star General" to "High-ranking field commander.",
    "Colonel" to "Senior officer.",
    "Lieutenant Colonel" to "Officer rank.",
    "Major" to "Field officer.",
    "Captain" to "Company officer.",
    "First Lieutenant" to "Junior officer.",
    "Second Lieutenant" to "Junior officer.",
    "Sergeant" to "Non-commissioned officer.",
    "Private" to "Lowest regular rank • special rule against Spy.",
    "Spy" to "Defeats the Five-Star General only when attacking; loses when attacked.",
    "Flag" to "Cannot move • protect it from capture."
)
@Composable private fun Ranks(back: () -> Unit) = Frame("PIECE INFO • RANK GUIDE", back) {
    LazyColumn {
        items(ranks.size) { i ->
            CardBox(Modifier.padding(bottom = 7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (ranks[i].first == "Flag") "⚑" else "★", color = Gold, fontSize = 22.sp)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(ranks[i].first, color = GoldLight, fontWeight = FontWeight.Bold)
                        Text(ranks[i].second, color = Muted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
@Composable private fun Rules(back: () -> Unit) = Frame("HOW TO PLAY", back) {
    val list = listOf(
        "1. OBJECTIVE" to "Capture the enemy Flag by eliminating the opposing army or reaching the Flag according to the selected rules.",
        "2. MOVEMENT" to "Move one square at a time in an orthogonal direction. Pieces cannot jump. The Flag cannot move.",
        "3. COMBAT" to "Compare ranks when attacking an occupied square. Higher rank wins; equal ranks are removed together.",
        "4. SPY RULE" to "The Spy defeats the Five-Star General only when the Spy attacks. The Spy loses when attacked, including by a Private.",
        "5. HIDDEN RANKS" to "Your opponent must not see your piece ranks. Use bluffing and memory."
    )
    list.forEach { pair ->
        CardBox(Modifier.padding(bottom = 9.dp)) {
            Text(pair.first, color = Gold, fontWeight = FontWeight.Bold)
            Text(pair.second, color = Color.White, fontSize = 12.sp)
        }
    }
}
@Composable private fun Victory(again: () -> Unit, home: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("♛", color = Gold, fontSize = 76.sp)
        Text("VICTORY!", color = GoldLight, fontSize = 32.sp, fontWeight = FontWeight.Black)
        Text("You captured the enemy Flag!", color = Color.White)
        Spacer(Modifier.height(26.dp))
        GoldButton("PLAY AGAIN", Modifier.fillMaxWidth(), again)
        Spacer(Modifier.height(10.dp))
        DarkButton("BACK TO MENU", Modifier.fillMaxWidth(), home)
    }
}
@Composable private fun Profile(back: () -> Unit) = Frame("MY PROFILE", back) {
    CardBox {
        Text("PLAYER", color = Gold, fontSize = 11.sp)
        Text("Guest Commander", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Rank: Recruit", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Stat("WINS", "0"); Stat("LOSSES", "0"); Stat("WIN RATE", "—") }
    }
    Spacer(Modifier.height(10.dp))
    Text("Match history will appear here after matches are recorded.", color = Muted, fontSize = 11.sp)
}
@Composable private fun Leaderboard(back: () -> Unit) = Frame("LEADERBOARD", back) {
    CardBox {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("RANK", color = Gold); Text("PLAYER", color = Gold); Text("WINS", color = Gold) }
        listOf("General2025", "PinoyStrategy", "SalpakanKing", "TropaLang", "LazadaBoy").forEachIndexed { i, name ->
            HorizontalDivider(color = Gold.copy(.15f))
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text((i + 1).toString(), color = GoldLight); Text(name, color = Color.White); Text("—", color = Muted) }
        }
    }
    Text("Sample leaderboard layout • live rankings need online accounts and match records.", color = Muted, fontSize = 10.sp)
}
@Composable private fun Settings(sound: Boolean, setSound: (Boolean) -> Unit, music: Boolean, setMusic: (Boolean) -> Unit, vibration: Boolean, setVibration: (Boolean) -> Unit, back: () -> Unit) = Frame("SETTINGS", back) {
    CardBox {
        Setting("Sound effects", sound, setSound)
        Setting("Music", music, setMusic)
        Setting("Vibration", vibration, setVibration)
    }
    Spacer(Modifier.height(12.dp))
    CardBox {
        Text("ACCOUNT & ONLINE PLAY", color = Gold, fontWeight = FontWeight.Bold)
        Text("Firebase sign-in and multiplayer connection will be added in the online-play phase.", color = Muted, fontSize = 11.sp)
    }
}
@Composable private fun Setting(label: String, value: Boolean, change: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), color = Color.White)
        Switch(value, change, colors = SwitchDefaults.colors(checkedThumbColor = Gold, checkedTrackColor = Gold.copy(.35f)))
    }
}
