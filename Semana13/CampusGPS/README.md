# CampusGPS Laboratorio 13 · Localización Geográfica · Programación en Móviles (Tecsup).

CampusGPS es una app Android que obtiene la ubicación del celular, la muestra en un mapa y te dice qué lugar del campus tienes más cerca. Muestra cinco lugares ficticios (biblioteca, cafetería, laboratorio, auditorio y estacionamiento) con su distancia en metros, ordenados del más cercano al más lejano. La app sigue funcionando si el usuario niega el permiso, apaga el GPS o no tiene Google Play Services.


## Tecnologías

- Kotlin + Jetpack Compose + Material 3 (sin XML)
- Google Play Services Location (FusedLocationProviderClient)
- Maps Compose
- Patrón MVVM + Repository (con interfaces)

## Estructura de paquetes

```
com.lino.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                    Posicion, Lugar, LugarConDistancia, TipoError, UbicacionUi
├── data/
│   ├── UbicacionRepository.kt        interface
│   ├── UbicacionRepositoryGoogle.kt  implementación con FusedLocationProviderClient
│   ├── LugaresRepository.kt          interface
│   └── LugaresRepositoryLocal.kt     los 5 lugares fijos
├── util/
│   ├── Distancia.kt                  distanciaMetros
│   └── Formato.kt                    formatearDistancia
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt
    ├── MapaUbicacion.kt
    ├── ListaLugares.kt
    ├── MensajeError.kt
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaMapa)
   | evento: pulsar el botón                ^ collectAsState()
   v                                        |
ViewModel (MapaViewModel) ----- StateFlow --+
   | llama a las interfaces
   v
UbicacionRepository ---> UbicacionRepositoryGoogle (FusedLocationProviderClient)
LugaresRepository   ---> LugaresRepositoryLocal (los 5 lugares fijos)
```

- **View (ui/):** solo dibuja según el estado y habla con su ViewModel.
- **ViewModel:** solo conoce las interfaces de Repository, calcula las distancias y no recibe `Context`.
- **Repository:** aquí vive Google Play Services (en la implementación).
- **util/:** funciones puras (distancia y formato).

## Capturas

**Permiso de ubicación**

<img width="197" height="418" alt="image" src="https://github.com/user-attachments/assets/2d4ee12c-d6cf-45df-9750-eaf9e7dee3a7" />

**Mapa con mi ubicación**

<img width="197" height="410" alt="image" src="https://github.com/user-attachments/assets/21a3241d-3be6-4093-8af1-d72fd095d08c" />

**Marcadores de los lugares **

<img width="187" height="409" alt="image" src="https://github.com/user-attachments/assets/ad4c9af0-c7c7-4556-b757-bef4e1414852" />


**Lista ordenada por distancia**

<img width="191" height="407" alt="image" src="https://github.com/user-attachments/assets/067b6870-7d36-4885-875d-838f6a41e2cf" />


**Estado de error**

<img width="197" height="409" alt="image" src="https://github.com/user-attachments/assets/d14546e0-56a4-4f4d-b964-f97cbe6b2230" />


## Cómo ejecutarlo

1. Abre el proyecto en Android Studio y sincroniza Gradle.
2. Crea una clave de API de Google Maps (Maps SDK for Android) y guárdala en `local.properties`, en la raíz del proyecto:
   ```
   MAPS_API_KEY=tu_clave
   ```
3. `local.properties` no se sube a git. La clave no debe aparecer en el código ni en el repositorio.
4. Usa un emulador con Google Play. Para ver los lugares cerca, pon la ubicación del emulador en `-12.0464, -77.0428` (Extended controls → Location).

## Para pensar

**1. ¿Por qué FusedLocationProviderClient está dentro del Repository y no en el ViewModel?**
Porque FusedLocationProviderClient necesita un `Context` y depende de Google Play Services. El ViewModel no debe conocer Android ni Google, solo la interfaz `UbicacionRepository`. Así, si cambio la forma de obtener la ubicación, el ViewModel no se toca, y es más fácil probarlo.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo da el usuario y sirve para saber dónde está el celular. La clave de API identifica mi app ante Google para poder usar el mapa. Si falta el permiso, no hay ubicación y la app muestra el error con el botón Abrir ajustes. Si falta la clave, el mapa sale gris o en blanco, pero la ubicación y la lista siguen funcionando.

**3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?**
Porque lo que está en el código se sube a GitHub y cualquiera podría usar mi clave y gastar mi cuota. `local.properties` está en el `.gitignore`, así que se queda solo en mi computadora. Si se subiera por error, borraría esa clave en Google Cloud, crearía una nueva con sus restricciones (paquete y SHA-1) y revisaría el `.gitignore`.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
La calculo en el ViewModel (`actualizarDistancias`), usando la función pura `distanciaMetros` de `util/`. El Composable solo dibuja el estado que recibe. Si calculara dentro del Composable, se mezclaría la lógica con la pantalla (rompe MVVM), se repetiría el cálculo en cada redibujado y sería más difícil de probar.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?**
Android deja de mostrar el cuadro de permiso y devuelve "negado" directamente. La app no se cierra: el ViewModel pasa al estado `Error` con tipo `SIN_PERMISO`, y la pantalla muestra el mensaje con el botón **Abrir ajustes**, que lleva a los ajustes de la app para que el usuario active el permiso a mano.
