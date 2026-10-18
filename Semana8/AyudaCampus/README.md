# AyudaCampus — Mesa de ayuda TI del campus

**Laboratorio 08 — MVVM + Room** · Programación en Móviles · Tecsup
**Alumno:** Mijael Lino

## Descripción

AyudaCampus es la mesa de ayuda de TI de un campus con dos perfiles en el mismo dispositivo.

El estudiante reporta incidencias, conversa con soporte, califica la atención y cierra o reabre sus tickets.
Soporte TI ve un panel con gráficos, atiende los tickets  y administra las categorías.
Ambos perfiles comparten la misma base de datos Room: lo que hace Soporte se ve al instante en el detalle del Estudiante.


**Usuarios de prueba:**
- Estudiante: `mijael@campus.test` / `1234`
- Soporte TI: `soporte@campus.test` / `admin`

## Árbol de paquetes
```
com.tecsup.ayudacampus
├── MainActivity.kt
├── AyudaApp.kt
├── data
│   ├── local
│   │   ├── entity      Categoria, Usuario, Ticket, Comentario
│   │   ├── dao         CategoriaDao, UsuarioDao, TicketDao
│   │   ├── relation    TicketConDetalle
│   │   ├── AyudaDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository      AyudaRepository (interface), AyudaRepositoryRoom
│   └── prefs           PreferenciasStore
├── model               Rol, EstadoTicket, Prioridad, ConteoCategoria
├── viewmodel           AyudaViewModelFactory, CategoriasViewModel, SesionViewModel,
│                       MisTicketsViewModel, NuevoTicketViewModel, TicketDetalleViewModel,
│                       PanelViewModel, BandejaViewModel, AtenderViewModel
└── ui
    ├── theme           Color, Type, Theme
    ├── components      AppTopBar, AyudaButton, AyudaTextField, EstadoChip, PrioridadChip,
    │                   TicketCard, BurbujaComentario, StepperTicket, Estrellas
    │   └── graficos    GraficoDona, GraficoBarras
    ├── navigation      Rutas, NavAyuda
    ├── cliente         SplashScreen, LoginScreen, MisTicketsScreen, NuevoTicketScreen,
    │                   DetalleTicketScreen, PerfilScreen
    └── soporte         PanelScreen, BandejaScreen, AtenderTicketScreen, CategoriasScreen,
                        FormularioCategoriaScreen, AjustesScreen
```

## Diagrama MVVM
```
VIEW (Composable)
   | eventos (clics)        ▲ collectAsState()
   ▼                        |
VIEWMODEL (StateFlow + reglas)
   | llama                  ▲ Flow / resultados
   ▼                        |
AyudaRepository (interface)
   | la implementa
   ▼
AyudaRepositoryRoom ──► DAO ──► SQLite (ayudacampus.db)

DataStore (solo preferencias) ──► ViewModel
```

## Capturas

### Estudiante


## | Nuevo ticket |

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/823616ee-6ea5-406d-a76e-3f1bb9786d43" />



## | Detalle del ticket |

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/0210d13c-41c1-4210-936a-be1862f483cc" />

## | Mis tickets | 

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/9f4a7d1d-8122-4324-a8ae-2aaf42d6176e" />

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/fe7dffa2-914f-448f-9a3f-115eeeec2eac" />



### Soporte TI

### | Panel |

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/268ff39a-89a0-4a65-acf7-693a88771314" />


### | Bandeja |

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/99380cf3-8ccb-4500-a9fe-8ff29ed24fdd" />


### | Atender ticket |

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/2825e171-3c81-4169-b394-ab7e997aeba5" />

<img width="340" height="716" alt="image" src="https://github.com/user-attachments/assets/2b8574a5-c10c-4b7c-9afc-4372ee439572" />


## Para pensar

## ¿Por qué las reglas de estado están en el ViewModel y no en el DAO ni en el Composable? | Son lógica de negocio: el DAO solo guarda datos y el Composable solo dibuja. 

Porque son lógica de negocio: el DAO solo guarda datos y el Composable solo muestra.

## ¿Por qué `crearTicket` es suspend y `observarTicket` es Flow? | `crearTicket` escribe una vez; `observarTicket` avisa cada vez que el ticket cambia.

Crear se hace una sola vez; observar avisa cada vez que el ticket cambia.

## ¿Qué pasaría si un ticket pudiera cerrarse sin calificación? | No se sabría si el problema se resolvió ni cómo fue la atención. 

No se sabría si el problema se resolvió ni cómo fue la atención.

## ¿Qué debería cambiar si mañana los tickets viven en un servidor? | Solo la implementación de `AyudaRepository`; ViewModels y pantallas quedan igual. 

Solo la implementación del Repository;  ya que las  pantallas en   ViewModels quedan igual.
