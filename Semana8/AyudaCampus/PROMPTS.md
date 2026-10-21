# Prompts utilizados

Fase 2 del Lab 08 (AyudaCampus). Trabajé con Gemini dentro de Android Studio (agente), en la rama `mejora-ia-s8` . Los prompts los redacté con ayuda de  Claude; el código lo generó Gemini y lo revisé yo en cada mejora.

Hice las 6 mejoras de la tabla, un commit por cada una.

---

## Prompt 1 · Mejora A: tiempo promedio de resolución
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega en el panel de soporte el tiempo promedio de resolucion de los tickets cerrados (fechaCierre - fecha). La consulta va en TicketDao, la funcion en AyudaRepository y su implementacion, y el valor se expone como StateFlow en PanelViewModel. La View solo lo muestra. Respeta MVVM: la View no usa DAO ni Repository; el ViewModel solo conoce la interface AyudaRepository; si agregas una funcion al Repository, declarala primero en la interface. No uses XML. Muestrame solo los archivos que cambian.

**Que genero la IA:** Una consulta `AVG(fechaCierre - fecha)` en `TicketDao`, la función `observarTiempoPromedioResolucion()` en `AyudaRepository` y `AyudaRepositoryRoom`, un `StateFlow<String>` con formato en `PanelViewModel` y una tarjeta en `PanelScreen`. Solo mostró el código, no lo aplicó.

**Que tuve que corregir yo:** En `PanelScreen` dejó un comentario `// ... gráficos y alertas ...` que habría borrado los gráficos y la alerta si copiaba el código tal cual.

**Respeta MVVM?:** Si. La consulta está en el DAO, la función se declaró primero en la interface y el ViewModel solo usa `AyudaRepository`.

## Prompt 2 · Mejora A: aplicar los cambios
**Texto enviado a Gemini:** Aplica esos cambios directamente en los archivos. En PanelScreen no borres nada de lo que ya existe (contadores, graficos y alerta), solo agrega la tarjeta del tiempo promedio debajo de los contadores.

**Que genero la IA:** Aplicó los cambios en los 5 archivos y compiló (`assembleDebug`).

**Que tuve que corregir yo:** Nada en el código, pero el resultado tenía un detalle visual (ver prompts 3 y 4).

**Respeta MVVM?:** Si.

## Prompt 3 · Mejora A: el tiempo se partía en dos líneas
**Texto enviado a Gemini:** En PanelScreen el texto del tiempo promedio se parte en dos lineas. Haz que se vea en una sola linea sin cambiar nada mas.

**Que genero la IA:** Dejó el tiempo en una sola línea.

**Que tuve que corregir yo:** Al arreglarlo cortó el título: decía "Tiempo promedio de" sin "resolución".

**Respeta MVVM?:** Si (solo cambió la View).

## Prompt 4 · Mejora A: título cortado
**Texto enviado a Gemini:** Ahora el titulo "Tiempo promedio de resolución" se corta. Haz que la columna del titulo use Modifier.weight(1f) para que el titulo pueda bajar a dos lineas, y el tiempo quede a la derecha en una sola linea. No cambies nada mas.

**Que genero la IA:** Título completo en dos líneas y el tiempo a la derecha.

**Que tuve que corregir yo:** Nada. En absoluto

**Respeta MVVM?:** Si. Commit: `ia: agrega tiempo promedio de resolucion al panel`.

---

## Prompt 5 · Mejora B: contador de SLA por prioridad
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega un contador de SLA: cada ticket muestra el tiempo restante segun su prioridad (Alta 4 h, Media 24 h, Baja 72 h). Solo cambian el ViewModel y la View: el calculo va en el ViewModel y la View solo lo muestra. Respeta MVVM: la View no usa DAO ni Repository; el ViewModel solo conoce la interface AyudaRepository; si agregas una funcion al Repository, declarala primero en la interface. No uses XML. Aplica los cambios directamente en los archivos sin borrar lo que ya existe y dime que archivos cambiaste.

**Que genero la IA:** Un archivo nuevo `SlaUtils.kt` con el cálculo, la función `calcularSla(ticket)` en los 4 ViewModels de tickets, y el texto de SLA en `TicketCard` y en las pantallas de tickets y de atender (rojo si está vencido). Por su cuenta agregó "SLA Cumplido / Vencido" para los tickets resueltos y cerrados. Además movió el cálculo del "hace X h" de las pantallas al ViewModel.

**Que tuve que corregir yo:** Tocó 10 archivos y reemplazó el "hace X h" que ya mostraba cada tarjeta por el SLA, aunque le pedí no borrar nada (ver prompt 6). La primera vez también falló por cuota de Gemini (`RESOURCE_EXHAUSTED`) y reintenté.

**Respeta MVVM?:** Si. El cálculo está en `SlaUtils` y los ViewModels; las pantallas solo llaman a `vm.calcularSla(...)`. Detalle menor: `TicketCard` decide el color rojo con `sla.contains("Vencido")`, es decir, compara texto en la View; funciona y lo dejé así.

## Prompt 6 · Mejora B: recuperar el "hace X h"
**Texto enviado a Gemini:** En TicketCard reemplazaste el texto "hace X h" que ya existia por el SLA y se perdio. Muestra los dos: el "hace X h" arriba a la derecha como estaba antes y el SLA debajo. No cambies nada mas.

**Que genero la IA:** La tarjeta muestra "Hace 1 h" arriba y el SLA debajo.

**Que tuve que corregir yo:** Nada más. Commit: `ia: agrega contador de SLA por prioridad`.

**Respeta MVVM?:** Si.

---

## Prompt 7 · Mejora C: búsqueda avanzada en la bandeja
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega busqueda avanzada en la bandeja de soporte: soporte filtra por prioridad, categoria y texto al mismo tiempo. Solo cambian BandejaViewModel y BandejaScreen: los filtros van como StateFlow en el ViewModel y la View solo los muestra. Respeta MVVM: la View no usa DAO ni Repository; el ViewModel solo conoce la interface AyudaRepository; si agregas una funcion al Repository, declarala primero en la interface. No uses XML. Aplica los cambios directamente en los archivos sin borrar lo que ya existe y dime que archivos cambiaste.

**Que genero la IA:** En `BandejaViewModel`, `StateFlow` para prioridad, categoría y texto, y un `combine` que los junta con el filtro de estado. En `BandejaScreen`, un campo de búsqueda, chips de prioridad y categoría y un botón "Limpiar filtros". Usó la función de categorías que ya existía en la interface, sin tocar el DAO.

**Que tuve que corregir yo:** Nada en el código. Solo cambió los 2 archivos que pedí. Por su cuenta hizo que el texto también busque en cliente, ubicación e ID, y agregó el botón "Limpiar filtros". Al probar, el emulador no me dejaba escribir, pero era un problema del emulador (se arregló con Wipe Data), no de Gemini.

**Respeta MVVM?:** Si. La pantalla solo usa los `StateFlow` del ViewModel. Prueba: prioridad Alta + categoría Hardware + texto "ser" dejó solo "Servidor del aula 305". Commit: `ia: agrega busqueda avanzada en la bandeja`.

---

## Prompt 8 · Mejora D: registro de estudiante
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega una pantalla de registro de estudiante para crear una cuenta nueva con validaciones (correo unico). La consulta va en UsuarioDao, la funcion en AyudaRepository y su implementacion, las validaciones en un ViewModel y la View solo muestra el formulario. Respeta MVVM: la View no usa DAO ni Repository; el ViewModel solo conoce la interface AyudaRepository; si agregas una funcion al Repository, declarala primero en la interface. No uses XML. Aplica los cambios directamente en los archivos sin borrar lo que ya existe y dime que archivos cambiaste.

**Que genero la IA:** `buscarPorCorreo` e `insertar` en `UsuarioDao`; `buscarPorCorreo` y `registrarUsuario` en `AyudaRepository` y `AyudaRepositoryRoom`; `RegistroViewModel` (campos vacíos, formato de correo, clave mínima de 6, claves iguales y correo único); `RegistroScreen`; un botón en el login para ir al registro; y la ruta y la fábrica de ViewModels.

**Que tuve que corregir yo:** Nada. Probé correo repetido, campos vacíos, claves distintas y una cuenta nueva con inicio de sesión, y todo funcionó. Solo noté que pide clave de mínimo 6 caracteres y la del estudiante de ejemplo tiene 4; no afecta porque solo aplica a cuentas nuevas.

**Respeta MVVM?:** Si. Las funciones se declararon primero en la interface y el ViewModel solo usa `AyudaRepository`. Commit: `ia: agrega registro de estudiante`.

---

## Prompt 9 · Mejora E: comentarios internos
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega comentarios internos: soporte escribe notas que el estudiante NO ve. Cambian la entidad Comentario, el DAO, el ViewModel y la View. Respeta MVVM: la View no usa DAO ni Repository; el ViewModel solo conoce la interface AyudaRepository; si agregas una funcion al Repository, declarala primero en la interface. No uses XML. Aplica los cambios directamente en los archivos sin borrar lo que ya existe y dime que archivos cambiaste.

**Que genero la IA:** Campo `esInterno` en `Comentario`, consultas en `TicketDao`, el parámetro `esInterno` en `agregarComentario` (primero en la interface), el filtro de internos en `TicketDetalleViewModel`, `comentarInterno()` en `AtenderViewModel`, una burbuja rosada con la etiqueta "Nota interna (Oculta al estudiante)" y un chip en la pantalla de atender para alternar entre nota interna y respuesta pública.

**Que tuve que corregir yo:** Nada en el código. Como cambia la tabla de Room, tuve que limpiar los datos de la app (`pm clear`). Al enviar la primera nota pareció que no aparecía en la conversación, pero apareció sola poco después.

**Respeta MVVM?:** Si. El filtro está en el ViewModel del estudiante y las pantallas solo muestran. Prueba: soporte ve la nota interna; el estudiante ve solo la respuesta pública. Commit: `ia: agrega comentarios internos de soporte`.

---

## Prompt 10 · Mejora F: animaciones y estados vacíos
**Texto enviado a Gemini:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un AyudaRepository (interface). Agrega animaciones y estados vacios: transiciones, animacion del stepper y mensajes con ilustracion cuando no hay datos. Solo cambia la View. No uses XML. Aplica los cambios directamente en los archivos sin borrar lo que ya existe y dime que archivos cambiaste.

**Que genero la IA:** Un componente nuevo `EmptyState` (ícono, título y subtítulo con animación de aparición), animaciones de color y escala en `StepperTicket`, el estado vacío en Mis tickets, Bandeja y Categorías, y transiciones entre pantallas en `NavAyuda`.

**Que tuve que corregir yo:** Nada. Probé el estado vacío buscando "zzz" y el stepper en un ticket resuelto.

**Respeta MVVM?:** Si. Solo cambió la capa View (componentes, pantallas y navegación); no tocó ViewModels, DAO ni Repository. Commit: `ia: agrega animaciones y estados vacios`.

---

## Reflexion (5 lineas)
- **Qué mejoró la IA y qué no:** Hizo bien la parte repetitiva como las consultas, funciones del Repository, formularios, estados vacíos  y respetó el MVVM en las 6 mejoras. Le costó respetar "no borres nada" y el alcance de los cambios.
- **Qué error cometió:** En la A dejó un `// ...` que habría borrado código, en la B reemplazó el "hace X h" por el SLA, y al arreglar un texto cortó el título de otra tarjeta.
- **Qué aprendí sobre MVVM, Room y reglas de negocio:** Aprendí que en MVVM cada cosa va en su capa: la consulta en el DAO, la función declarada primero en la interface del Repository, las reglas en el ViewModel y la View solo muestra. Por eso, cuando le pedí algo a Gemini, tuve que decirle en qué capa iba cada parte para que no metiera lógica en el Composable.
- **Qué haría distinto:** Revisar siempre el diff antes del commit, porque la IA a veces borra o cambia cosas que no pidió, y pedirle en cada prompt que aplique los cambios en los archivos y no solo los muestre.
