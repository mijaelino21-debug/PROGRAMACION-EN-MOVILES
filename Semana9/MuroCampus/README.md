 # MuroCampus

App Android (Kotlin, Jetpack Compose) que consume la API REST de [JSONPlaceholder](https://jsonplaceholder.typicode.com/): lista publicaciones, muestra el detalle con sus comentarios, filtra por usuario y crea una publicación nueva. Si falla la red, muestra el error y permite reintentar.

## Estructura de paquetes

```
com.lino.murocampus/
├── MainActivity.kt
├── model/
│   └── Modelos.kt
├── data/
│   ├── remote/
│   │   ├── PostApi.kt
│   │   └── RetrofitClient.kt
│   └── PostRepository.kt
├── viewmodel/
│   ├── UiState.kt
│   ├── PostsViewModel.kt
│   └── PostsViewModelFactory.kt
└── ui/
    ├── Componentes.kt
    ├── PantallaLista.kt
    ├── PantallaDetalle.kt
    ├── PantallaCrear.kt
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaLista, PantallaDetalle, PantallaCrear)
   | eventos: cargar, filtrar, abrir, publicar     ^ collectAsStateWithLifecycle()
   v                                               |
ViewModel (PostsViewModel) --- StateFlow<UiState> (Cargando / Exito / Error)
   | viewModelScope.launch
   v
PostRepository (interface)  -> devuelve Result<...>
   -> PostRepositoryRemote (captura las excepciones)
   |
   v
PostApi (Retrofit) --- OkHttp ---> https://jsonplaceholder.typicode.com/
```

## Capturas

| Lista | Detalle | Filtro |
|---|---|---|
| ![Lista](<img width="199" height="416" alt="image" src="https://github.com/user-attachments/assets/708c6fcd-d679-4676-aaf1-834d0e7f8eca" />
) | ![Detalle](<img width="196" height="411" alt="image" src="https://github.com/user-attachments/assets/f00c1f01-5be4-487a-a13d-56bf6b8a7a34" />
) | ![Filtro](<img width="188" height="407" alt="image" src="https://github.com/user-attachments/assets/630f9df0-cbf6-45f1-8e79-16e3ce0161dc" />
) |

| Crear | Error de conexión |
|---|---|
| ![Crear](<img width="191" height="413" alt="image" src="https://github.com/user-attachments/assets/29585a36-eacd-449d-817c-9f2f9c277d56" />
) | ![Error](<img width="193" height="411" alt="image" src="https://github.com/user-attachments/assets/72dc0bd6-b991-4b72-b41e-37351c339b5e" />
) |

## Para pensar

**1. ¿Qué diferencia hay entre GET y POST? ¿Por qué `crear` lleva `@Body` y `porUsuario` lleva `@Query`?**
GET lee datos y POST envía datos para crear un recurso. `crear` manda el objeto `NuevoPost` como JSON en el cuerpo de la petición (`@Body`). `porUsuario` solo filtra una lectura, así que el dato va en la URL como parámetro (`posts?userId=2`).

**2. ¿Por qué la ruta es `"posts"` y no `"/posts"`?**
Una ruta relativa se agrega a la URL base. Con `/` al inicio se resuelve desde la raíz del dominio y reemplaza el camino que tenga la URL base (por ejemplo, `/api/`).

**3. ¿Por qué el Repository devuelve `Result` en vez de dejar que la excepción llegue al ViewModel?**
Para que el fallo sea parte del contrato: el ViewModel trata éxito y error con `onSuccess` y `onFailure`, sin conocer Retrofit y sin que una excepción de red cierre la app. `CancellationException` sí se relanza para no romper la cancelación de corrutinas.

**4. ¿Qué pasa si olvidas el permiso INTERNET? ¿Y si olvidas `ignoreUnknownKeys` y el servidor agrega un campo nuevo?**
Sin el permiso la app compila, pero ninguna llamada puede conectarse y se muestra un error. Sin `ignoreUnknownKeys`, un campo nuevo provoca `SerializationException` y aparece "Respuesta inesperada".

**5. ¿Por qué la lista usa un `when` sobre `UiState` y no un `if` con tres variables?**
`UiState` es una interfaz sellada: solo hay un estado a la vez y el `when` obliga a cubrir los tres. Con tres variables podrían darse combinaciones imposibles, como cargando y error juntos.

**6. JSONPlaceholder no guarda los POST. ¿Cómo cambiaría la app con un servicio real?**
Después de publicar habría que recargar la lista o insertar la publicación devuelta. También se usaría el usuario autenticado en lugar de `userId = 1` y se mostrarían los errores de validación del servidor.
