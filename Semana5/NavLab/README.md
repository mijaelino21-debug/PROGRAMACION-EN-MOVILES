Lab05— Navegación Portal Académico con Jetpack Compose y Android Studio AI 

Autor: Mijael Lino Barja

Carrera: Diseño y Desarrollo de Software

Capturas de los resultados sin el rediseño con IA de Android Studio
<img width="384" height="722" alt="image" src="https://github.com/user-attachments/assets/cd9d5f39-1d34-4656-a3b6-9350f6711358" />
<img width="420" height="720" alt="image" src="https://github.com/user-attachments/assets/2556eda0-1bd3-47e5-b5db-a388697d3ea8" />
<img width="648" height="1152" alt="image" src="https://github.com/user-attachments/assets/fb1dea34-c954-418c-a2a2-6c92ea65b6bd" />

Capturas de los resultados  con la mejora de diseño con IA de Android Studio
<img width="310" height="632" alt="image" src="https://github.com/user-attachments/assets/bf34d8d7-1f31-47a5-932c-65f6cedd3df8" />
<img width="306" height="651" alt="image" src="https://github.com/user-attachments/assets/b1c67048-4786-4f3c-96bf-4e0101303aa7" />
<img width="314" height="553" alt="image" src="https://github.com/user-attachments/assets/fd2d4bb3-e99b-4cff-a7b1-f99f747f1ba9" />
<img width="300" height="561" alt="image" src="https://github.com/user-attachments/assets/85de308c-3c08-47e6-aaeb-a462ae63cea2" />
<img width="303" height="626" alt="image" src="https://github.com/user-attachments/assets/8894c338-0b6b-4f4d-991c-572c953cf2d9" />

Prompt usado 

Actúa como desarrollador senior de aplicaciones móviles Android en Kotlin y Diseñador UX/UI experto en Jetpack Compose (Material 3).

Necesito rediseñar y refinar la interfaz y lógica de la aplicación "NavLab" (com.lino.navlab) aplicando el sistema de diseño Material 3, degradados/fondos morados y la lógica de inicio de sesión con navegación por argumentos, sin romper la arquitectura existente (Screen.kt, AppNavigation.kt, NavHost):

1. Sistema de Diseño e Identidad (Material 3 y Paleta Exacta a la Referencia):
- Paleta de Colores: Define un fondo de pantalla lila/morado claro pastel suave (Color(0xFFEDE7F6) o Brush.verticalGradient desde #4A2E83 a #C9B8F0) para lograr exactamente el contraste visual de la primera imagen.
- Formas: Aplica RoundedCornerShape(20.dp) a 24.dp en todas las tarjetas (Card / ElevatedCard).
- Iconos: Coloca los iconos en las tarjetas dentro de contenedores circulares (CircleShape) en fondo lavanda claro.
- Tipografía: Estructura la jerarquía con MaterialTheme.typography (headlineSmall, titleMedium, bodyMedium).

2. LoginScreen (Screen.Login como startDestination):
- UI: Card blanca centrada (24.dp), título "Portal Académico" y subtítulo "Accede a tu cuenta". OutlinedTextField para "Correo Institucional" y "Contraseña" con toggle de visibilidad. Botón morado "INICIAR SESIÓN". Enlace con Text + Modifier.clickable() para "¿Olvidaste tu contraseña?" que despliega un Snackbar. Campos limpios al iniciar.
- Lógica de Validación (al presionar "INICIAR SESIÓN", muestra mensaje rojo debajo y bloquea la navegación si falla):
  i. Campo vacío → "Este campo es obligatorio"
  ii. Formato de correo inválido → "Correo inválido"
  iii. Contraseña < 8 caracteres → "La contraseña debe tener al menos 8 caracteres"
  iv. Aceptar cualquier contraseña de >= 8 caracteres si el correo es institucional (@tecsup.edu.pe).
  v. Éxito → Navegar a HomeScreen pasando el nombre del usuario ("Mijael Lino").

3. HomeScreen (Bienvenida Dinámica):
- Firma de función: fun HomeScreen(navController: NavController, userName: String = "Mijael Lino").
- Fondo degradado morado vertical completo.
- Título dinámico: "Bienvenido, [userName]".
- Dos ElevatedCard independientes blancas con iconos circulares para "Directorio de Alumnos" (hacia ListScreen) y "Mi Perfil Académico" (hacia ProfileScreen).
- Botón "Cerrar Sesión Segura" en tono rojo que regresa a Login limpiando la pila (popUpTo(0)).

4. ListScreen (Directorio de Alumnos - Réplica Exacta de la Imagen 1):
- TopAppBar de Material 3 con fondo transparente o lila, título en negrita "Directorio de Alumnos" e IconButton de flecha hacia atrás.
- LazyColumn con espacio vertical de 12.dp entre elementos. Cada elemento es una Card blanca elevada con bordes de 20.dp que contiene:
  * Avatar circular real (AsyncImage de Coil desde URLs "https://i.pravatar.cc/300?img=X") a la izquierda.
  * Nombre del estudiante en negrita (Color(0xFF2D3142)) y su respectiva Carrera en color morado (Color(0xFF673AB7)).
  * Lista variada de estudiantes con distintas carreras (NO repetir la misma carrera en todos):
    1. Mijael Lino — Diseño y Desarrollo de Software
    2. Juan León — Ingeniería de Sistemas
    3. María García — Arquitectura
    4. Carlos Pérez — Medicina
    5. Ana López — Derecho
    6. Luis Ramírez — Administración
  * Icono chevron '>' gris/morado alineado a la derecha. Al presionar, navega a DetailScreen pasando el id.

5. DetailScreen (Expediente Académico):
- Header superior morado con curva inferior.
- Avatar flotante circular de 110.dp con borde blanco de 3.dp superpuesto al centro.
- Card de Material 3 con filas estructuradas (ID, Correo, Carrera, Facultad, Biografía).

6. ProfileScreen (Configuración de Perfil - Datos Reales):
- Header morado con el avatar circular de Mijael Lino Barja.
- Tarjeta "INFORMACIÓN PERSONAL": Nombre completo ("Mijael Lino Barja"), Correo ("mijael.lino@tecsup.edu.pe") y Teléfono ("+51 987 654 321").
- Tarjeta "ACADÉMICO": Carrera ("Diseño y Desarrollo de Software"), Ciclo ("IV Ciclo") y Condición ("Regular").
- Botón rojo suave para "Cerrar Sesión" volviendo a Login.

Asegura que todo compile al 100% en Material 3 sin errores de deprecación ni firmas faltantes.





