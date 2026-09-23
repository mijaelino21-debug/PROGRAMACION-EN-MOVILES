package com.lino.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lino.navlab.navigation.Screen
import com.lino.navlab.ui.theme.GradientEnd
import com.lino.navlab.ui.theme.GradientStart
import com.lino.navlab.ui.theme.LightLavender

@Composable
fun HomeScreen(navController: NavController, userName: String = "Mijael Lino") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Bienvenido, $userName",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "¿Qué deseas gestionar hoy?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            HomeElevatedCard(
                title = "Directorio de Alumnos",
                subtitle = "Ver lista de compañeros",
                icon = Icons.AutoMirrored.Filled.List,
                onClick = { navController.navigate(Screen.List.route) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeElevatedCard(
                title = "Mi Perfil Académico",
                subtitle = "Configuración y datos",
                icon = Icons.Default.AccountCircle,
                onClick = { navController.navigate(Screen.Profile.route) }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión Segura",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun HomeElevatedCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(LightLavender),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = GradientStart
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF2D3142),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
