# PROMPTS.md — Fase 2: mejoras con IA

- **App:** AvisosCampus (Kotlin + Jetpack Compose, MVVM + Repository)
- **Herramienta de IA:** gemini
- **Rama de trabajo:** `mejora-ia-15` (la guía la llama `mejora-ia`; se usó este nombre porque ya existía una rama `mejora-ia` de otra semana)
- **Reglas dadas en cada prompt:** implementar solo lo pedido, compilar y corregir errores, no hacer commit, no poner Context ni NotificationManager en el ViewModel.

## Mejora 1: Dos canales

| Campo | Detalle |
|---|---|
| Qué debe lograr | Un canal **Urgentes** con IMPORTANCE_HIGH (emergente) y otro **Avisos**; el recordatorio usa el urgente |
| Capas que cambian | Notificador + ViewModel |
| Commit | `feat(mejora): agrega canal Urgentes para el recordatorio` |

**Prompt:**

```text
Mejora "Dos canales". Implementa solo esto, sin aumentar ni quitar nada.
- En Notificador.crearCanal agrega un canal "urgentes" con nombre "Urgentes" e IMPORTANCE_HIGH, además del canal "avisos".
- El recordatorio (Origen.RECORDATORIO) usa el canal urgente con PRIORITY_HIGH; los demás siguen en "avisos".
- Respeta MVVM + Repository: nada de Context ni NotificationManager en el ViewModel.
- No cambies la interfaz.
```

**Respuesta de la IA:** compiló correctamente. 

**Qué cambié o descarté:** nada, se dejó como lo entregó la IA porque realizo un buen cambio .

**Verificación:** en Ajustes → Apps → AvisosCampus → Notificaciones aparecen los canales Avisos y Urgentes. El recordatorio ("Recordar en 10 s") sale por el canal Urgentes.

## Mejora 2: Texto largo

| Campo | Detalle |
|---|---|
| Qué debe lograr | Mostrar el texto completo al expandir la notificación con BigTextStyle |
| Capas que cambian | Notificador |
| Commit | `feat(mejora): agrega BigTextStyle y texto largo en el aviso` |

**Prompt:**

```text
Mejora "Texto largo". Implementa solo esto, sin aumentar ni quitar nada.
- En Notificador, agrega NotificationCompat.BigTextStyle a la notificación para mostrar el texto completo al expandirla. Usa el mismo texto del aviso como bigText.
- Solo cambia la clase Notificador. No toques ViewModel, Repository, Receiver ni la interfaz.
- No cambies los canales ni lo que hizo la mejora anterior.
```

**Respuesta de la IA:** agregó `NotificationCompat.BigTextStyle().bigText(aviso.texto)` al Builder en `Notificador.kt`, sin tocar canales ni otros archivos. `gradlew build` exitoso, sin commit.

**Qué cambié o descarté:** el texto del aviso de entrega en `AvisosViewModel.kt` (línea 51) era muy corto y no permitía ver el efecto de BigTextStyle. Lo cambié a mano por un texto de varias frases para poder comprobar que al expandir la notificación se ve completo. Ese cambio quedó en el mismo commit.

**Verificación:** al expandir la notificación "Tarea lista" se ve el mensaje completo en varias líneas.

## Mejora 3: Receptor del cargador

| Campo | Detalle |
|---|---|
| Qué debe lograr | Escuchar ACTION_POWER_CONNECTED y ACTION_POWER_DISCONNECTED y avisar, con su propio switch |
| Capas que cambian | Receiver + ViewModel + View |
| Commit | `feat(mejora): agrega receptor del cargador con switch propio` |

**Prompt:**

```text
Mejora "Receptor del cargador". Implementa solo esto, sin aumentar ni quitar nada.
- Crea un BroadcastReceiver que escuche ACTION_POWER_CONNECTED y ACTION_POWER_DISCONNECTED y avise con una notificación ("Cargador conectado" / "Cargador desconectado").
- Agrega su propio switch "Escuchar el cargador" en la pantalla, con el mismo patrón que el switch del modo avión. El receptor solo debe estar registrado mientras el switch esté activado.
- El estado del switch vive en el ViewModel. Respeta MVVM + Repository: nada de Context ni NotificationManager en el ViewModel.
- No cambies lo que hicieron las mejoras anteriores (canales, BigTextStyle).
```

**Respuesta de la IA:** implementó `CargadorReceiver` para CONNECT/DISCONNECT, con el estado y los avisos en el ViewModel (sin Context) y el switch "Escuchar el cargador" con registro dinámico solo cuando está activo. Los avisos usan `Origen.BOTON` porque no existía otro origen definido. Canales y BigTextStyle quedaron intactos. `gradlew build` exitoso, sin commit.

**Qué cambié o descarté:** nada, se dejó como lo entregó la IA .

**Verificación:** con el switch activo, en el emulador (Extended controls → Battery → Charger connection) al pasar de AC charger a None sale "Cargador desconectado" y al volver sale "Cargador conectado". Con el switch apagado no sale nada.
