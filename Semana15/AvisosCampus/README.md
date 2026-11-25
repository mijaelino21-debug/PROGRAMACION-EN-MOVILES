# AvisosCampus

App Android hecha con Kotlin, Jetpack Compose y Material 3 que muestra notificaciones locales, programa un recordatorio a los 10 segundos y avisa cuando se activa o desactiva el modo avión. Todos los avisos quedan en un historial.

## Árbol de paquetes

```
com.lino.avisoscampus/
├── AvisosApp.kt
├── MainActivity.kt
├── model/
│   └── Modelos.kt
├── data/
│   └── AvisosRepository.kt
├── notificacion/
│   └── Notificador.kt
├── receiver/
│   └── ModoAvionReceiver.kt
├── viewmodel/
│   ├── AvisosViewModel.kt
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt
    ├── HistorialAvisos.kt
    └── theme/
```

## Diagrama MVVM

```
View (PantallaAvisos)
   | eventos: botones, switch          ^ collectAsState()
   v                                   |
ViewModel (AvisosViewModel) --- StateFlow (avisos, permiso, faltan...)
   | registra el aviso                 | emite eventos (SharedFlow<Aviso>)
   v                                   v
AvisosRepository (interface)      La View recibe el evento y llama a
 -> AvisosRepositoryMemoria       Notificador.mostrar(context, aviso)
```

## Capturas

### Notificación en el panel

<img width="182" height="394" alt="image" src="https://github.com/user-attachments/assets/773dc86c-2cf9-471c-95ab-fa9c04951952" />


### Recordatorio

<img width="188" height="391" alt="image" src="https://github.com/user-attachments/assets/50559f13-2a58-4138-90a3-500b1d3e943d" />

<img width="180" height="375" alt="image" src="https://github.com/user-attachments/assets/69ea3b00-10ae-43bc-b844-9f9ceb4661c3" />


### Modo avión

<img width="180" height="375" alt="image" src="https://github.com/user-attachments/assets/d5204bce-cbc7-4c95-8366-d96abb6e0517" />

<img width="180" height="374" alt="image" src="https://github.com/user-attachments/assets/3177fa02-bd26-474d-93d0-fb329cd94519" />


### Historial

<img width="180" height="374" alt="image" src="https://github.com/user-attachments/assets/379fd8ec-9bf4-43f9-a9a7-c592b803062c" />


### Permiso denegado

<img width="182" height="373" alt="image" src="https://github.com/user-attachments/assets/470bbc93-9e20-45eb-9471-99ae44313b3c" />


## Para pensar

**1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?**
El canal (Android 8+) agrupa las notificaciones y permite al usuario configurarlas; sin canal, la notificación no se muestra. El permiso POST_NOTIFICATIONS (Android 13+) deja que el usuario decida si la app puede avisarle; sin permiso, la notificación no aparece. En esta app el aviso igual se guarda en el historial y se muestra un mensaje.

**2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?**
Porque mostrarla requiere un Context y el ViewModel no debe conocerlo. Así se respeta MVVM, se evitan fugas de memoria y el ViewModel se puede probar sin Android. La pantalla recibe el evento y llama al Notificador.

**3. ¿Qué pasa si usas el mismo id en dos notificaciones?**
La segunda reemplaza a la primera. Por eso se usa el id de cada aviso.

**4. ¿Por qué onReceive debe ser corto y por qué hay que quitar el receptor al salir?**
Porque onReceive se ejecuta en el hilo principal y un trabajo largo puede congelar la app o causar un ANR. Si el receptor no se quita, sigue registrado, puede generar fugas de memoria y avisos repetidos.

**5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?**
Una local cuando el aviso nace en el propio celular, como un recordatorio o un cambio de modo avión; no necesita internet. Una push de Firebase cuando el aviso viene de un servidor, como notas publicadas, anuncios del campus o mensajes de otros usuarios.
