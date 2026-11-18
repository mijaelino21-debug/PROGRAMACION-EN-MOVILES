# PROMPTS - Fase 2 (mejora con IA) - CampusGPS

- Herramienta usada: Gemini , ejecutado dentro de la carpeta `Semana13/CampusGPS`.
- Los prompts los redacté con ayuda de Claude y se los pasé a Gemini; Gemini  escribió el código y compiló y despues tuve que revisar los cambios realizados y ver si estaba de acuerdo o no.
- Rama de trabajo: `mejora-ia-13` (la rama `mejora-ia` ya existía de otros laboratorios).
- Regla que puse en todos los prompts:  no tocar el manifest, no poner la API key en ningún archivo y no cambiar nombres ni colores existentes. Los commits  y la revision de codigo los hice yo.

## Tabla de prompts

| # | Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|---|
| 1 | Activar el punto azul nativo de Google Maps (`isMyLocationEnabled`) y el botón de mi ubicación solo si el permiso está concedido. Tocar solo `PantallaMapa.kt` y `MapaUbicacion.kt`. Pasar un parámetro `tienePermiso` al mapa. Sin Context ni lógica en el ViewModel. Sin API key ni commits. | Agregó `hayPermisoUbicacion()` y un estado `tienePermiso` en `PantallaMapa`, que se actualiza al aceptar el permiso y al pulsar el botón. En `MapaUbicacion` agregó `MapProperties(isMyLocationEnabled = tienePermiso)` y `MapUiSettings(myLocationButtonEnabled = tienePermiso)`. Compiló bien. | Nada: acepté el diff tal cual pero haciendo una revision previa al codigo . | Activar la capa de ubicación sin permiso lanza `SecurityException`, por eso depende del permiso. El permiso se lee en la vista porque el ViewModel no debe tener Context. Limitación: si el permiso se da desde Ajustes, el punto azul aparece al pulsar el botón. |
| 2 | Aplicar un estilo oscuro al mapa cuando el tema del sistema sea oscuro. Solo capa de interfaz: `MapaUbicacion.kt` y un nuevo `res/raw/mapa_oscuro.json`. Usar `isSystemInDarkTheme()` y `MapStyleOptions.loadRawResourceStyle`, con `remember`. Mantener `isMyLocationEnabled`. Sin dependencias nuevas ni commits. | Creó `mapa_oscuro.json` con el estilo oscuro y en `MapaUbicacion` agregó `remember(temaOscuro)` que carga el estilo si el tema es oscuro y deja `null` si es claro, pasado en `MapProperties(mapStyleOptions = estilo)` junto a `isMyLocationEnabled`. Compiló bien. | Nada: acepté el diff tal cual. | Es un cambio solo de vista, no toca el ViewModel ni los repositorios. `remember(temaOscuro)` evita recargar el estilo en cada recomposición. Verifiqué en el emulador con tema claro y oscuro. |
| 3 | Recordar el último lugar seleccionado con DataStore Preferences. Dependencia `datastore-preferences:1.1.1`; interfaz `PreferenciasRepository` (`ultimoLugarId: Flow<Int?>` y `guardarUltimoLugar(id)`); implementación con DataStore; el Context solo en la implementación y en `MainActivity`; `MapaViewModel` recibe la interfaz y expone `ultimoLugar: StateFlow<Lugar?>`; `seleccionar(id)` guarda cuando `id != null`; actualizar Factory y MainActivity; solo un `Text` nuevo en `PantallaMapa`. | Creó `PreferenciasRepository.kt` y `PreferenciasRepositoryDataStore.kt` (clave `ultimo_lugar_id`), modificó `MapaViewModel`, `CampusViewModelFactory`, `MainActivity` (DataStore único a nivel de archivo con `preferencesDataStore`), `PantallaMapa` (Text "Último lugar visitado") y `build.gradle.kts`. Compiló bien. | Nada: acepté el diff tal cual. Lo revisé línea por línea antes de commitear. | Es la mejora que cambia Repository y ViewModel, como pide la guía. Seguí el mismo patrón de interfaz + implementación que ya usaba el proyecto, así el ViewModel sigue sin Context y se puede probar. Verifiqué en el emulador: selecciono un lugar, cierro la app del todo, la abro de nuevo y el texto sigue ahí. |

## Análisis propio

**Mejora 1 (punto azul).** El punto nativo depende del permiso, por eso el estado `tienePermiso` se calcula en la vista con `ContextCompat.checkSelfPermission`. Si estuviera en el ViewModel necesitaría Context, y rompería la arquitectura MVVM.

**Mejora 2 (mapa oscuro).** Es solo vista. `isSystemInDarkTheme()` detecta el tema del sistema y el JSON de `res/raw` define los colores del mapa. No requirió tocar datos ni ViewModel.

**Mejora 3 (último lugar visitado).** El ViewModel solo conoce la interfaz `PreferenciasRepository`; DataStore queda escondido en la implementación. Así el dato se guarda en disco, sobrevive al cierre de la app y se expone como `StateFlow` para que la pantalla lo muestre. `seleccionar` solo guarda cuando se elige un lugar (no al deseleccionar).

## Qué revisé yo en cada diff

- Que solo se tocaran los archivos pedidos (`git status` antes de cada commit).
- Que el ViewModel no tuviera Context.
- Que no apareciera la API key ni se editara el manifest.
- Que compilara y que la app funcionara en el emulador antes de commitear.
