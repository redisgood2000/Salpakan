package com.salpakan.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF17211D)
private val Panel = Color(0xFF202D27)
private val Green = Color(0xFF81B64C)
private val BoardLight = Color(0xFFE8E4D5)
private val BoardDark = Color(0xFF78936C)
private val Gold = Color(0xFFE5B85C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SalpakanApp() }
    }
}

@Composable
fun SalpakanApp() {
    val board = remember { mutableStateListOf< String? >().apply { repeat(72) { add(null) } } }
    var selected by remember { mutableStateOf<Int?>(null) }
    var turn by remember { mutableIntStateOf(1) }
    var showRanks by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf("Local pass-and-play • Set up both armies to begin") }

    fun resetBoard() {
        for (i in board.indices) board[i] = null
        selected = null
        turn = 1
        status = "Empty board • Arrange your pieces before starting"
    }

    MaterialTheme(colorScheme = darkColorScheme(primary = Green, background = Ink, surface = Panel, onBackground = Color.White, onSurface = Color.White)) {
        Column(Modifier.fillMaxSize().background(Ink).padding(horizontal = 16.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("SALPAKAN", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Text("GAMES OF THE GENERALS", color = Green, fontSize = 10.sp, letterSpacing = 2.sp)
                }
                Surface(color = Panel, shape = RoundedCornerShape(12.dp)) {
                    Text("LOCAL GAME", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(if (turn == 1) "GREEN SIDE TO MOVE" else "GOLD SIDE TO MOVE", color = Green, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(status, color = Color.LightGray, fontSize = 10.sp)
                }
                TextButton(onClick = { showRanks = !showRanks }) { Text(if (showRanks) "Hide ranks" else "Show ranks", color = Gold) }
            }
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(8), modifier = Modifier.fillMaxWidth().weight(1f), userScrollEnabled = false) {
                itemsIndexed(board) { index, piece ->
                    val row = index / 8
                    val col = index % 8
                    val isSelected = selected == index
                    val squareColor = if ((row + col) % 2 == 0) BoardLight else BoardDark
                    Box(Modifier.aspectRatio(1f).background(if (isSelected) Gold else squareColor).border(0.5.dp, Color(0x55000000)).clickable {
                        if (selected == null) {
                            if (piece != null) selected = index
                        } else {
                            val from = selected!!
                            if (from == index) selected = null
                            else if (board[index] == null) {
                                val fr = from / 8; val fc = from % 8
                                val dr = kotlin.math.abs(row - fr); val dc = kotlin.math.abs(col - fc)
                                if (dr + dc == 1) {
                                    board[index] = board[from]
                                    board[from] = null
                                    turn = if (turn == 1) 2 else 1
                                    status = "Piece moved • Full combat rules are not enabled yet"
                                }
                                selected = null
                            } else selected = index
                        }
                    }, contentAlignment = Alignment.Center) {
                        if (piece != null) {
                            Surface(color = if (piece.startsWith("G")) Color(0xFF315B3C) else Color(0xFF9A762E), shape = RoundedCornerShape(7.dp), modifier = Modifier.fillMaxSize().padding(2.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (showRanks) piece.drop(1) else "●", color = Color.White, fontSize = if (showRanks) 9.sp else 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("8 × 9 BOARD", color = Color.Gray, fontSize = 10.sp, letterSpacing = 2.sp)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { resetBoard() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Panel)) { Text("Reset board", color = Color.White) }
                Button(onClick = { status = "Setup mode • Army placement tools are the next build step" }, modifier = Modifier.weight(1f)) { Text("Game setup") }
            }
            Spacer(Modifier.height(8.dp))
            Text("Native Android • Chess-inspired dark + green interface", color = Color.Gray, fontSize = 10.sp)
        }
    }
}
