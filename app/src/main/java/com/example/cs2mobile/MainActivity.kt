package com.example.cs2mobile

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MatchViewModel : ViewModel() {
    // États pour stocker les sélections du joueur
    var gameMode by mutableStateOf("Compétitif")
        private set

    var teamSize by mutableStateOf(5)
        private set

    var selectedMap by mutableStateOf("Dust II")
        private set

    // Fonction pour mettre à jour tous les paramètres à la fois
    fun updateMatchSettings(gameMode: String, teamSize: Int, selectedMap: String) {
        this.gameMode = gameMode
        this.teamSize = teamSize
        this.selectedMap = selectedMap
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            val navController = rememberNavController()
            // Créer une instance partagée du ViewModel
            val viewModel: MatchViewModel = viewModel()
            // Passer le ViewModel au NavigationGraph
            NavigationGraph(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MatchViewModel) {
    NavHost(navController = navController, startDestination = "page1") {
        composable("page1") {
            Page1(navController = navController, viewModel = viewModel)
        }
        composable("page2") {
            Page2(
                navController = navController,
                text = "En attente d'autres joueurs...",
                nextPage = "page1",
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun Page1(navController: NavHostController, viewModel: MatchViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Image de fond
        Image(
            painter = painterResource(id = R.drawable.cs2background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Bouton en haut à droite - Réglages et Profil
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Button(
                onClick = { /* Action pour Réglages et Profil */ }
            ) {
                Text(text = "Réglages et Profil")
            }
        }

        // Contenu principal avec texte et boutons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween, // Espace entre les éléments
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texte en haut et centré horizontalement
            Text(
                text = "GrailSimon",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.White, // Pour être lisible sur l'image
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f)) // Pousse les boutons en bas

            // Boutons en bas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Bouton Inventaire et Shop
                Button(
                    onClick = { /* Action pour Inventaire et Shop */ },
                ) {
                    Text(text = "Inventaire et Shop")
                }

                // Bouton Match
                Button(
                    onClick = { showDialog.value = true }
                ) {
                    Text(text = "Match")
                }
            }
        }

        // Pop-up Match
        if (showDialog.value) {
            EnhancedMatchDialog(
                viewModel = viewModel,
                onDismiss = { showDialog.value = false },
                onSearchGame = {
                    showDialog.value = false
                    navController.navigate("page2")
                }
            )
        }
    }
}

@Composable
fun EnhancedMatchDialog(
    viewModel: MatchViewModel,
    onDismiss: () -> Unit,
    onSearchGame: () -> Unit
) {
    // États pour les différentes options - initialiser avec les valeurs du ViewModel
    var selectedGameMode by remember { mutableStateOf(viewModel.gameMode) }
    var teamSize by remember { mutableIntStateOf(viewModel.teamSize) }
    var selectedMap by remember { mutableStateOf(viewModel.selectedMap) }

    // Couleurs des boutons d'équipe
    val oneVsOneColor = Color(0xFF2196F3)  // Bleu
    val twoVsTwoColor = Color(0xFFFF9800)  // Orange
    val fiveVsFiveColor = Color(0xFF4CAF50)  // Vert

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)  // Augmentation de la largeur
                .fillMaxHeight(0.95f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // En-tête avec titre
                Text(
                    text = "Menu Match",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    textAlign = TextAlign.Center
                )

                // Contenu principal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, bottom = 60.dp)
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Section Mode de jeu
                    Text(
                        text = "Mode de jeu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val gameModes = listOf("Compétitif", "Casual", "Personnalisé")
                        gameModes.forEach { mode ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedGameMode == mode) Color(0xFFE0E0FF)
                                        else Color(0xFFEEEEEE)
                                    )
                                    .selectable(
                                        selected = (selectedGameMode == mode),
                                        onClick = { selectedGameMode = mode }
                                    )
                                    .padding(16.dp)
                                    .width(110.dp)
                            ) {
                                val icon = when (mode) {
                                    "Compétitif" -> Icons.Default.Star
                                    "Casual" -> Icons.Default.Person
                                    else -> Icons.Default.Settings
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = mode,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = mode, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Section Taille de l'équipe
                    Text(
                        text = "Taille de l'équipe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { teamSize = 1 },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (teamSize == 1) oneVsOneColor else Color.Gray
                            )
                        ) {
                            Text(text = "1 vs 1", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { teamSize = 2 },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (teamSize == 2) twoVsTwoColor else Color.Gray
                            )
                        ) {
                            Text(text = "2 vs 2", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { teamSize = 5 },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (teamSize == 5) fiveVsFiveColor else Color.Gray
                            )
                        ) {
                            Text(text = "5 vs 5", fontWeight = FontWeight.Bold)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Section Choix de la carte
                    Text(
                        text = "Choix de la carte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val maps = listOf("Dust II", "Inferno", "Mirage")
                        maps.forEach { map ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedMap == map) Color(0xFFE0E0FF)
                                        else Color(0xFFEEEEEE)
                                    )
                                    .selectable(
                                        selected = (selectedMap == map),
                                        onClick = { selectedMap = map }
                                    )
                                    .padding(16.dp)
                                    .width(110.dp)
                            ) {
                                val icon = when (map) {
                                    "Dust II" -> Icons.Default.Home
                                    "Inferno" -> Icons.Default.Home
                                    else -> Icons.Default.Home
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = map,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = map, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Boutons d'action en bas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        )
                    ) {
                        Text(text = "Annuler")
                    }

                    Button(
                        onClick = {
                            // Mettre à jour le ViewModel avant de naviguer
                            viewModel.updateMatchSettings(selectedGameMode, teamSize, selectedMap)
                            onSearchGame()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(text = "Rechercher partie")
                    }
                }
            }
        }
    }
}

@Composable
fun Page2(navController: NavHostController, text: String, nextPage: String, viewModel: MatchViewModel) {
    // État pour l'animation des points de chargement
    var dots by remember { mutableStateOf("") }

    // Récupération des données du ViewModel
    val gameMode = viewModel.gameMode
    val teamSize = viewModel.teamSize
    val selectedMap = viewModel.selectedMap

    // Déterminer l'image à afficher en fonction de la carte sélectionnée
    val mapImageResource = when (selectedMap) {
        "Dust II" -> R.drawable.dust2cs2
        "Inferno" -> R.drawable.infernocs2
        "Mirage" -> R.drawable.miragecs2
        else -> R.drawable.dust2cs2 // Image par défaut
    }

    // Effet pour animer les points de chargement
    LaunchedEffect(key1 = true) {
        while (true) {
            dots = ""
            delay(500)
            dots = "."
            delay(500)
            dots = ".."
            delay(500)
            dots = "..."
            delay(500)
        }
    }

    // Interface principale
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cs2background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Colonne supérieure pour le titre, l'image et le chargement
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                // Titre dynamique en haut à gauche
                Text(
                    text = "$gameMode ${teamSize}v${teamSize} sur $selectedMap",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Image de la map sélectionnée
                Image(
                    painter = painterResource(id = mapImageResource),
                    contentDescription = "Image de $selectedMap",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // Texte de chargement avec animation des points
                Text(
                    text = "Chargement$dots",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            // Zone centrale avec le texte existant
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            // Bouton en bas à gauche
            Button(
                onClick = { navController.navigate(nextPage) },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = "Quitter la recherche de partie")
            }
        }
    }
}