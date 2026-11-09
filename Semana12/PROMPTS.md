##PROMPTS.md

##Consulta 1

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la estructura general del proyecto de catálogo de hoteles desarrollado en Kotlin, Jetpack Compose y MVVM. Verificar la separación de capas, navegación y funcionamiento de las pantallas de usuario y administrador.

Qué respondió (resumen): Se identificaron mejoras necesarias en la navegación y en la separación de responsabilidades entre las pantallas y los componentes del proyecto.

Qué usamos / qué cambiamos y por qué: Se revisó y mejoró la navegación de MainActivity.kt, diferenciando el acceso del usuario y del administrador. Se buscó que el administrador pudiera acceder al catálogo y regresar a su panel.

Qué aprendimos: La navegación debe considerar los roles y mantener separadas las funcionalidades de cada tipo de usuario.

##Consulta 2

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar el modelo de hoteles y su persistencia local con Room. Implementar los campos necesarios para diferenciar el identificador local de SQLite del identificador remoto de la API.

Qué respondió (resumen): Se explicó que el identificador de Room y el identificador remoto deben manejarse por separado para evitar conflictos al sincronizar los hoteles.

Qué usamos / qué cambiamos y por qué: Se agregó remoteId a HotelEntity y al modelo Hotel. También se adaptó HotelDto para convertir correctamente los datos recibidos desde la API y almacenarlos localmente.

Qué aprendimos: Es importante diferenciar los identificadores de la base de datos local y del servicio remoto para mantener una sincronización correcta.

##Consulta 3

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la configuración de Room y actualizar las migraciones para incorporar nuevos campos sin perder la información existente.

Qué respondió (resumen): Se indicó que cualquier modificación de la estructura de una entidad debe acompañarse de una migración de Room.

Qué usamos / qué cambiamos y por qué: Se actualizó AppDatabase.kt, incrementando la versión de la base de datos a 7 e incorporando la migración MIGRATION_6_7 para agregar el campo remoteId.

Qué aprendimos: Las migraciones permiten modificar una base de datos existente de manera controlada y evitar errores al actualizar la aplicación.

##Consulta 4

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar y mejorar las operaciones de acceso a datos de hoteles en HotelDao.kt, conservando las consultas existentes y agregando las operaciones necesarias para la administración y sincronización.

Qué respondió (resumen): Se propuso mantener las consultas de hoteles, favoritos y estados, además de utilizar operaciones de inserción y actualización mediante Room.

Qué usamos / qué cambiamos y por qué: Se actualizó HotelDao.kt con consultas para obtener hoteles, hoteles activos, favoritos, buscar por identificador, contar registros, guardar, actualizar estados y eliminar registros.

Qué aprendimos: El DAO centraliza el acceso a SQLite y permite que el repositorio trabaje sin ejecutar consultas directamente desde la interfaz.

##Consulta 5

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar los DTO, modelos y mappers del catálogo para asegurar que los datos de la API se conviertan correctamente a entidades Room y modelos utilizados por la interfaz.

Qué respondió (resumen): Se explicó la importancia de mantener separados los objetos de transferencia de datos, las entidades de persistencia y los modelos del dominio.

Qué usamos / qué cambiamos y por qué: Se revisaron HotelDto.kt, Hotel.kt y Mappers.kt, incluyendo el identificador remoto y los campos de los hoteles para mantener la compatibilidad entre las capas.

Qué aprendimos: Los mappers evitan mezclar las estructuras de la API, la base de datos y la interfaz de usuario.

##Consulta 6

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la integración entre Retrofit y Room para que los hoteles descargados desde MockAPI puedan almacenarse localmente y utilizarse desde el catálogo.

Qué respondió (resumen): Se identificó que era necesario separar los identificadores locales y remotos, además de conservar los datos locales durante la sincronización.

Qué usamos / qué cambiamos y por qué: Se revisaron ApiService.kt, HotelRepositoryImpl.kt y AppContainer.kt para mantener la comunicación con la API y la persistencia local.

Qué aprendimos: Retrofit permite obtener datos remotos, mientras que Room puede actuar como fuente de información para la interfaz y facilitar el funcionamiento sin conexión.

##Consulta 7

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la gestión de hoteles del administrador y mejorar la presentación de la información estadística del panel.

Qué respondió (resumen): Se recomendó organizar la información administrativa y mostrar indicadores que permitan conocer el estado general del catálogo.

Qué usamos / qué cambiamos y por qué: Se revisaron AdminScreen.kt y AdminViewModel.kt, y se incorporó el componente TarjetaEstadisticaAdmin.kt para presentar estadísticas de manera más organizada.

Qué aprendimos: La separación de componentes reutilizables facilita el mantenimiento y mejora la presentación de la información.

##Consulta 8

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar los mensajes de error de la aplicación y mejorar su claridad para que el usuario pueda comprender los problemas que ocurren durante las operaciones.

Qué respondió (resumen): Se recomendó centralizar y mostrar mensajes comprensibles en lugar de presentar errores técnicos directamente.

Qué usamos / qué cambiamos y por qué: Se revisó MensajesError.kt para organizar los mensajes de error y facilitar su presentación en la aplicación.

Qué aprendimos: Los mensajes claros mejoran la experiencia del usuario y facilitan la identificación de problemas.

##Consulta 9

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la obtención del detalle de los hoteles y la comunicación entre el ViewModel y el repositorio.

Qué respondió (resumen): Se explicó que el ViewModel debe delegar las operaciones de datos al repositorio y controlar los resultados mediante estados.

Qué usamos / qué cambiamos y por qué: Se revisó DetalleViewModel.kt para mantener la comunicación con el repositorio y gestionar la información del detalle del hotel.

Qué aprendimos: El ViewModel coordina la lógica de presentación sin acceder directamente a Retrofit ni a Room.

##Consulta 10

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar la estructura del proyecto, verificar los cambios realizados y comprobar que el código Kotlin compile correctamente.

Qué respondió (resumen): Se identificaron posibles inconsistencias entre paquetes, imports y entidades de Room que debían corregirse para evitar errores de compilación.

Qué usamos / qué cambiamos y por qué: Se revisaron los paquetes de HotelEntity, HotelDao, AppDatabase y HotelDto, manteniendo la entidad en com.tecsup.catalogo.data.local. Se ejecutó .\gradlew.bat :app:compileDebugKotlin y se obtuvo BUILD SUCCESSFUL.

Qué aprendimos: Mantener una estructura coherente de paquetes e imports es fundamental para evitar errores de compilación en proyectos Kotlin.

##Consulta 11

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Revisar el estado de Git y preparar los cambios pendientes del proyecto Semana12 para realizar un commit sin incluir archivos de otros proyectos.

Qué respondió (resumen): Se explicó cómo revisar los archivos modificados, agregar únicamente los cambios del proyecto correspondiente y verificar la sincronización con GitHub.

Qué usamos / qué cambiamos y por qué: Se utilizó git status --short, luego git add app/, se creó un commit con el mensaje feat: integrar gestion de hoteles, reservas y administracion y se subieron los cambios mediante git push origin mejora-ia-12.

Qué aprendimos: Es importante revisar el estado de Git antes de realizar un commit para evitar incluir archivos de otros proyectos o carpetas.

##Consulta 12

Fecha: 08/11/2026

Herramienta: ChatGPT

Prompt: Comparar el avance del proyecto con el documento del Laboratorio 11-12 y verificar los requisitos técnicos, pruebas de aceptación y entregables solicitados.

Qué respondió (resumen): Se organizó una lista de verificación que incluye Firebase Authentication, roles, Retrofit, Room, favoritos, modo sin conexión, notificaciones, panel administrativo, pruebas unitarias y documentación.

Qué usamos / qué cambiamos y por qué: Se utilizó el PDF como referencia para identificar los requisitos que deben comprobarse antes de la entrega, incluyendo README.md, PROMPTS.md, Pull Request, versión v1.0 y APK.

Qué aprendimos: Comparar el proyecto con la rúbrica permite identificar los pendientes reales y preparar una entrega más completa.

## Consulta 13
- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** Comparar mi repositorio con el PDF del laboratorio y completar la estructura y los contratos de la sección 5 (AuthRepository, Sesion, Rol, AppContainer y nombres de los métodos del repositorio).
- **Qué respondió (resumen):** Indicó los archivos que faltaban y propuso un refactor para que los ViewModels dependan de `AuthRepository` en vez de usar FirebaseAuth y Firestore directamente, con un `AppContainer` que crea las dependencias.
- **Qué usamos / qué cambiamos y por qué:** Se crearon `Sesion.kt`, `AuthRepository.kt`, `FirebaseAuthRepository.kt` y `AppContainer.kt`. Se renombraron los métodos del repositorio al contrato del PDF (`items`, `favoritos`, `refrescar`, `cantidadGuardada`, `cantidadFavoritos`). Se eliminó `HotelApi.kt` sin uso y se usó `rememberSaveable` en `MainActivity`.
- **Qué aprendimos:** Los ViewModels no deben conocer Firebase ni Context; los contratos con nombres fijos permiten cambiar la implementación sin tocar la interfaz.

## Consulta 14
- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** Los botones "Gestión de Hoteles" y "Gestión de Reservas" del perfil de administrador no hacen nada.
- **Qué respondió (resumen):** Detectó que `ListaScreen` no pasaba a `PerfilScreen` los callbacks `onIrAGestionHoteles` y `onIrAGestionReservas`, por lo que se usaba el valor por defecto vacío.
- **Qué usamos / qué cambiamos y por qué:** Se agregaron los dos parámetros en `ListaScreen.kt` y se conectaron en `MainActivity.kt` para abrir las pantallas de gestión.
- **Qué aprendimos:** Un parámetro con valor por defecto `{}` puede ocultar un callback que nunca se conectó; hay que revisar toda la cadena de pantallas.

## Consulta 15
- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** En Gestión de Reservas el buscador y las pestañas no filtran, y los textos "Confirmadas" y "Rechazadas" se cortan en dos líneas.
- **Qué respondió (resumen):** El `StateFlow` del ViewModel solo se recalculaba cuando cambiaba la lista porque leía búsqueda, pestaña y orden con `.value`. Propuso usar `combine` con los cuatro flujos y reducir el texto de las pestañas.
- **Qué usamos / qué cambiamos y por qué:** Se cambió `AdminReservasViewModel.kt` para usar `combine`, y en `AdminReservasScreen.kt` se puso `fontSize = 12.sp`, `maxLines = 1` y `softWrap = false` en las pestañas.
- **Qué aprendimos:** Para que la interfaz reaccione a varios filtros hay que combinar todos los flujos de entrada, no leer sus valores dentro de un `map`.

## Consulta 16
- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** Los hoteles se duplican en la pantalla cada vez que sincronizo y al desactivar un hotel se crea otro igual en MockAPI.
- **Qué respondió (resumen):** Explicó que `@PrimaryKey(autoGenerate = true)` con `id = 0` hace que `@Upsert` inserte una fila nueva en cada sincronización, que las filas viejas no tenían `remoteId`, y que el `POST` por error 404 creaba copias en el servidor.
- **Qué usamos / qué cambiamos y por qué:** Se reescribió `HotelRepositoryImpl.refrescar()` para emparejar por `remoteId` (y por nombre y ciudad las filas viejas), eliminar repetidos y conservar el id local, los favoritos y el estado activo. Se agregaron `listarTodos()` y `eliminarPorIds()` al DAO. Activar o desactivar un hotel ahora se guarda solo en Room y `subirARemoto` ya no crea copias de hoteles existentes. Se borraron a mano los duplicados de MockAPI.

- ## Reflexión final

Durante el desarrollo del proyecto se utilizó inteligencia artificial (ChatGPT y Claude) como apoyo para revisar la arquitectura, corregir errores, completar los contratos del laboratorio y mejorar la persistencia local. Las sugerencias se revisaron y adaptaron al proyecto antes de aplicarlas, y varias tuvieron que corregirse al probarlas en el emulador.

Se aprendió la importancia de separar las responsabilidades mediante MVVM, usar Retrofit para consumir servicios web, Room como fuente de verdad local, mantener identificadores remotos y locales diferenciados, y combinar flujos en los ViewModels para que la interfaz reaccione a los filtros. También se aprendió que los errores de sincronización (como los hoteles duplicados) se detectan probando con datos reales, no solo compilando.

La inteligencia artificial fue una herramienta de apoyo; las modificaciones y verificaciones se hicieron sobre el proyecto, comprobando la compilación y el funcionamiento en el emulador.
