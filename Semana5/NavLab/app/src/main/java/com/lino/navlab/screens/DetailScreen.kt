package com.lino.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lino.navlab.ui.theme.AccentPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val studentDetails = mapOf(
        1 to Triple("Mijael Lino", "Diseño y Desarrollo de Software", "mijael.lino@tecsup.edu.pe"),
        2 to Triple("Juan León", "Ingeniería de Sistemas", "juan.leon@tecsup.edu.pe"),
        3 to Triple("María García", "Arquitectura", "maria.garcia@tecsup.edu.pe"),
        4 to Triple("Carlos Pérez", "Medicina", "carlos.perez@tecsup.edu.pe"),
        5 to Triple("Ana López", "Derecho", "ana.lopez@tecsup.edu.pe"),
        6 to Triple("Luis Ramírez", "Administración", "luis.ramirez@tecsup.edu.pe")
    )

    val detail = studentDetails[itemId] ?: Triple("Estudiante", "Carrera", "correo@tecsup.edu.pe")
    val imageUrls = mapOf(
        1 to "https://i.pravatar.cc/300?img=1",
        2 to "https://i.pravatar.cc/300?img=5",
        3 to "https://i.pravatar.cc/300?img=3",
        4 to "https://i.pravatar.cc/300?img=9",
        5 to "https://i.pravatar.cc/300?img=7",
        6 to "https://i.pravatar.cc/300?img=10"
    )
    val imageUrl = imageUrls[itemId] ?: "https://i.pravatar.cc/300?img=1"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .background(Color(0xFFEDE7F6))
                .verticalScroll(rememberScrollState())
        ) {
            // Header superior morado con curva
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(AccentPurple, AccentPurple.copy(alpha = 0.85f))
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(130.dp))

                // Avatar flotante circular de 110.dp con borde blanco
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = detail.first,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
                Text(
                    text = detail.second,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF673AB7),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Card de Material 3 con información estructurada
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Expediente Académico",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        DetailIconRow(icon = Icons.Default.Badge, label = "ID Estudiante", value = "2026100$itemId")
                        HorizontalDivider(color = Color(0xFFF1F4F9), modifier = Modifier.padding(vertical = 4.dp))

                        DetailIconRow(icon = Icons.Default.Email, label = "Correo", value = detail.third)
                        HorizontalDivider(color = Color(0xFFF1F4F9), modifier = Modifier.padding(vertical = 4.dp))

                        DetailIconRow(icon = Icons.Default.School, label = "Carrera", value = detail.second)
                        HorizontalDivider(color = Color(0xFFF1F4F9), modifier = Modifier.padding(vertical = 4.dp))

                        DetailIconRow(icon = Icons.Default.School, label = "Facultad", value = "Tecnología e Ingeniería")
                        HorizontalDivider(color = Color(0xFFF1F4F9), modifier = Modifier.padding(vertical = 4.dp))

                        DetailIconRow(
                            icon = Icons.Default.Info,
                            label = "Biografía",
                            value = "Estudiante destacado en la carrera de ${detail.second}. Proactivo, disciplinado y enfocado en el desarrollo continuo."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailIconRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentPurple,
            modifier = Modifier
                .size(24.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF2D3142)
            )
        }
    }
}
