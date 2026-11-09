 # Catálogo de Hoteles: Laboratorio 11-12

Programación en Móviles · Docente: Juan León S. 
Alumno: Mijael Lino 

App Android en Kotlin con **Jetpack Compose + MVVM + Retrofit + Room + Firebase Auth/Firestore** y notificación local.

## Tema y variante

- **Tema:** catálogo de hoteles (API propia en MockAPI).
- **Lista:** `GET /hoteles`
- **Detalle:** `GET /hoteles/{id}`

## Arquitectura

```
UI (Compose) → ViewModel (StateFlow<UiState>) → Repository (interface) → Retrofit / Room / Firebase
```

- La lista siempre sale de **Room** (fuente única de verdad). Retrofit solo la actualiza.
- Los ViewModel dependen de la **interface** `HotelRepository` y de `Notificador`, no reciben `Context`.
- Tres modelos: `HotelDto` (API), `HotelEntity` (Room) y `Hotel` (UI), con mappers.
- Los favoritos se conservan al sincronizar.

## Funcionalidades

| Requisito | Qué hace |
|---|---|
| Login | Firebase Authentication con correo y contraseña, valida campos vacíos |
| Roles | `admin` y `usuario`, leídos de Firestore (`usuarios/{uid}`, campo `rol`) |
| Lista | Estados Cargando, Error (con Reintentar) y Vacío |
| Detalle | Segundo endpoint `hoteles/{id}` |
| Favoritos | Marcar y desmarcar; persisten en Room |
| Sin conexión | Muestra lo guardado con un aviso; si no hay nada, error con Reintentar |
| Notificación | Al sincronizar: «Se guardaron N elementos». Pide el permiso `POST_NOTIFICATIONS`; al tocarla abre la app |
| Panel Admin | Total guardado, total de favoritos, Sincronizar y Limpiar caché |
| Sesión | Se mantiene al reabrir la app; botón Cerrar sesión |

## Capturas

| Login | 

<img width="185" height="407" alt="image" src="https://github.com/user-attachments/assets/b5f3b6b2-994f-4543-b0ea-6f72a7bc86e4" />

|Lista | 

<img width="186" height="416" alt="image" src="https://github.com/user-attachments/assets/0d40f514-2496-49f9-864a-44525c378da2" />

|Detalle |

<img width="190" height="410" alt="image" src="https://github.com/user-attachments/assets/beec457a-122a-4f98-bfa4-169da25f29fe" />


| Favoritos |

<img width="187" height="404" alt="image" src="https://github.com/user-attachments/assets/a3bc226f-76ec-452b-81bc-7b5479e4e72c" />

| Panel Admin | 

<img width="190" height="408" alt="image" src="https://github.com/user-attachments/assets/832b4716-6220-46eb-94cb-42c3fd7cd6c9" />


| Notificación |

<img width="182" height="402" alt="image" src="https://github.com/user-attachments/assets/d4f22a54-a802-467f-8d19-c9e8d3294170" />

## Cómo compilar

1. Abrir la carpeta `Semana12` con Android Studio.
2. Usar **JDK 17** (Settings > Build Tools > Gradle > Gradle JDK).
3. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/), registrar la app con el paquete `com.tecsup.catalogo` y descargar **`google-services.json`**. Colocarlo en `app/`. **No se sube al repositorio** (está en `.gitignore`).
4. En Firebase activar **Authentication > Correo/contraseña** y crear **Firestore**.
5. Sync, y ejecutar en un emulador con Google Play.

## Usuarios de prueba

Crear en Firebase Authentication un usuario `admin` y un usuario normal. Luego en Firestore crear la colección `usuarios` con un documento por usuario:

- **ID del documento:** el UID del usuario (Authentication > Usuarios)
- **Campos (string):** `rol` = `admin` o `usuario`, y `nombre`

Reglas de Firestore:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}

## Mejoras con IA (rama `mejora-ia-12`)

Después de la versión base (Fase 1, sin IA), se hizo una segunda fase con ayuda de IA en la rama `mejora-ia-12`. Cada consulta está documentada en [PROMPTS.md](PROMPTS.md).

### Qué se mejoró

| Mejora | Descripción |
|---|---|
| Contratos del laboratorio | `AuthRepository`, `FirebaseAuthRepository`, `Sesion`, `Rol` y `AppContainer`. Los ViewModels ya no usan Firebase directamente. Los métodos del repositorio siguen los nombres del PDF. |
| Pruebas unitarias | Pruebas de `ListaViewModel` con un repositorio falso (estados de carga, éxito, error y favoritos) y pruebas de `AdminHotelesViewModel`. |
| Búsqueda y filtros | Búsqueda por nombre, filtros por ciudad, calificación y precio, y ordenamiento en la lista de hoteles. |
| Reservas | El usuario reserva un hotel desde el detalle y ve "Mis reservas". Los datos se guardan en Room. |
| Gestión de hoteles (admin) | El administrador agrega, edita, activa o desactiva y elimina hoteles. Un hotel desactivado no aparece al usuario. |
| Gestión de reservas (admin) | El administrador confirma o rechaza reservas, con estadísticas, búsqueda, pestañas y orden. |
| Sincronización sin duplicados | Room guarda el id local y el `remoteId` de MockAPI por separado. Al sincronizar se actualizan los hoteles existentes y se conservan favoritos y estado. |
| Interfaz | Nuevo tema, tarjetas de hotel con imagen y estrellas, perfil con accesos por rol y pantallas separadas en componentes. |

### Capturas de las mejoras

| Lista con búsqueda y filtros |
<img width="185" height="397" alt="image" src="https://github.com/user-attachments/assets/d92dd4f2-e878-4b9c-812b-7b5beb456480" />

|Detalle y reserva |
<img width="188" height="400" alt="image" src="https://github.com/user-attachments/assets/c1b10916-9eae-4aba-8e3f-47eb33a3c8b3" />

<img width="195" height="406" alt="image" src="https://github.com/user-attachments/assets/9c3748aa-5c7d-4d68-9846-a48e5b27b6ac" />

|Mis reservas |

<img width="191" height="399" alt="image" src="https://github.com/user-attachments/assets/37c0d508-b0f6-423d-8b7c-7fb57c946401" />


| Perfil de administrador |

<img width="188" height="392" alt="image" src="https://github.com/user-attachments/assets/0e8b36e7-ae75-4f5f-807e-3cf60e9bf3a5" />

<img width="189" height="398" alt="image" src="https://github.com/user-attachments/assets/f2a6d513-de6d-4328-9391-e9bd4a8d6570" />


| Gestión de hoteles |

<img width="190" height="404" alt="image" src="https://github.com/user-attachments/assets/4c959532-9025-4413-975c-b85fba1b6d65" />


|Gestión de reservas |

<img width="193" height="397" alt="image" src="https://github.com/user-attachments/assets/1cad6302-e705-44c3-b4ba-bae666edf2ab" />


### Pruebas unitarias

<img width="957" height="332" alt="image" src="https://github.com/user-attachments/assets/7f5103b4-823d-4fc6-af62-d6f185268a11" />

Resultado: 11 pruebas de `ListaViewModelTest`, todas aprobadas.

Se ejecutan con:
```
.\gradlew.bat testDebugUnitTest
```
