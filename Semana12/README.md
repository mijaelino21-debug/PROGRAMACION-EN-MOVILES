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

| Lista con búsqueda y filtros | }

<img width="191" height="399" alt="image" src="https://github.com/user-attachments/assets/d721f6f5-0011-4694-a368-fc8910368b0a" />

<img width="188" height="397" alt="image" src="https://github.com/user-attachments/assets/52d793aa-2410-4c9d-8396-523c3e71d4a4" />


|Detalle y reserva | 

<img width="188" height="398" alt="image" src="https://github.com/user-attachments/assets/91080655-1a57-467a-be74-6956b0cafe29" />

<img width="196" height="398" alt="image" src="https://github.com/user-attachments/assets/74294f3f-4cfe-4930-aa79-2099b92e7e89" />


|Mis reservas |

<img width="195" height="398" alt="image" src="https://github.com/user-attachments/assets/56d71d15-91d9-4099-a1ef-3834873743b8" />

| Perfil de administrador | 

<img width="197" height="399" alt="image" src="https://github.com/user-attachments/assets/21c796f8-4c1f-4672-985d-e9a4dbdb488d" />

<img width="188" height="401" alt="image" src="https://github.com/user-attachments/assets/fd9654f4-bf7d-44a2-bd67-aee45aeb4794" />


|Gestión de hoteles | 

<img width="188" height="398" alt="image" src="https://github.com/user-attachments/assets/87f96f79-77d8-4df7-9c1c-bb2e728251c2" />


|Gestión de reservas |

<img width="183" height="402" alt="image" src="https://github.com/user-attachments/assets/1a361f7b-fca5-4cb5-aa67-129705b877ad" />



### Pruebas unitarias
<img width="926" height="303" alt="image" src="https://github.com/user-attachments/assets/e8325cb8-a4ca-4915-abea-5b84e2f2e4e4" />


