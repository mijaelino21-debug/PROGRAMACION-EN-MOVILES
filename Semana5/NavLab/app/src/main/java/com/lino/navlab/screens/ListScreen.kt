package com.lino.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lino.navlab.navigation.Screen
import com.lino.navlab.ui.theme.AccentPurple

data class Student(val id: Int, val name: String, val career: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val students = listOf(
        Student(1, "Mijael Lino", "Diseño y Desarrollo de Software", "https://i.pravatar.cc/300?img=1"),
        Student(2, "Juan León", "Ingeniería de Sistemas", "https://i.pravatar.cc/300?img=5"),
        Student(3, "María García", "Arquitectura", "https://i.pravatar.cc/300?img=3"),
        Student(4, "Carlos Pérez", "Medicina", "https://i.pravatar.cc/300?img=9"),
        Student(5, "Ana López", "Derecho", "https://i.pravatar.cc/300?img=7"),
        Student(6, "Luis Ramírez", "Administración", "https://i.pravatar.cc/300?img=10")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "Directorio de Alumnos", 
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentPurple
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDE7F6)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(students) { student ->
                StudentItem(student) {
                    navController.navigate(Screen.Detail.createRoute(student.id))
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StudentItem(student: Student, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = student.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF673AB7).copy(alpha = 0.2f), CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.career,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF673AB7),
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF673AB7),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
