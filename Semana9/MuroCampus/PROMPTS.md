# PROMPTS.md - MuroCampus

Asistente usado: opencode (modelo Hy4 flash Free).

## Fase 2 (rama de mejoras)

| Prompt que escribí (resumido) | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Ajustar SOLO el diseño visual en ui/: colores, tarjetas, etiqueta por usuario, carga, error, chips, detalle y crear. No tocar viewmodel/, data/ ni model/» | Cambió los composables y el tema | Sin cambios manuales | Limité el prompt a ui/ para no romper la arquitectura |
| «Borrar publicación: @DELETE con Response<Unit>, Result en el Repository, estado en el ViewModel y botón Eliminar con confirmación» | API, Repository, ViewModel, diálogo en el detalle y regreso a la lista | Sin cambios manuales. Probé el error sin internet | El Repository convierte el fallo en Result y el ViewModel no usa Retrofit |
| «Búsqueda por título sobre las publicaciones ya descargadas, combinada con el filtro de usuario» | StateFlow busqueda, campo de texto, botón Limpiar y mensaje Sin resultados | Sin cambios manuales | No hace llamadas nuevas a la API |
| «Interceptor de OkHttp que agregue Accept: application/json, antes del de logging» | Interceptor en RetrofitClient usando header() | Sin cambios manuales | header() evita duplicar el encabezado y el orden permite verlo en el log |

