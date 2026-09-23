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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.lino.navlab.navigation.Screen
import com.lino.navlab.ui.theme.AccentPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
        ) {
            // Header superior morado con curva
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                Spacer(modifier = Modifier.height(110.dp))

                // Avatar circular grande de Mijael Lino Barja
                AsyncImage(
                    model = "https://i.pravatar.cc/300?img=1",
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mijael Lino Barja",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
                Text(
                    text = "ID: 20261001 • Alumno",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjeta "INFORMACIÓN PERSONAL" con RoundedCornerShape(24.dp) y ElevatedCard
                ProfileSectionCard(
                    title = "INFORMACIÓN PERSONAL",
                    items = listOf(
                        ProfileRowData(Icons.Default.Person, "Nombre completo", "Mijael Lino Barja"),
                        ProfileRowData(Icons.Default.Email, "Correo electrónico", "mijael.lino@tecsup.edu.pe"),
                        ProfileRowData(Icons.Default.Phone, "Teléfono", "+51 987 654 321")
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta "ACADÉMICO" con RoundedCornerShape(24.dp) y ElevatedCard
                ProfileSectionCard(
                    title = "ACADÉMICO",
                    items = listOf(
                        ProfileRowData(Icons.Default.School, "Carrera", "Diseño y Desarrollo de Software"),
                        ProfileRowData(Icons.Default.CalendarMonth, "Ciclo", "IV Ciclo"),
                        ProfileRowData(Icons.Default.Badge, "Condición", "Regular")
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón rojo de "Cerrar Sesión" limpiando el back stack hasta Login
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFECEB),
                        contentColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFE53935))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

data class ProfileRowData(val icon: ImageVector, val label: String, val value: String)

@Composable
fun ProfileSectionCard(title: String, items: List<ProfileRowData>) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AccentPurple,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2D3142),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (index < items.size - 1) {
                    HorizontalDivider(color = Color(0xFFF1F4F9), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
