# CentroTareas

## Descripción

CentroTareas es una aplicación Android desarrollada con Kotlin, Jetpack Compose y corrutinas. Su objetivo es demostrar cómo ejecutar tareas largas y cálculos en segundo plano sin bloquear la interfaz de usuario.

La aplicación permite:
- Ejecutar una tarea con barra de progreso y cancelarla.
- Contar números primos en segundo plano.
- Comparar dos cargas de datos realizadas de manera secuencial y en paralelo.
- Simular errores y volver a intentar una carga.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Coroutines
- StateFlow
- ViewModel
- Repository
- Git y GitHub

## Estructura de paquetes

```text
com.lino.centrotareas/
├── MainActivity.kt
├── model/
│   └── Estados.kt
├── data/
│   ├── TareasRepository.kt
│   └── TareasRepositoryLocal.kt
├── viewmodel/
│   ├── TareasViewModel.kt
│   └── TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt
    └── theme/
```

## Arquitectura MVVM + Repository

La aplicación utiliza la arquitectura MVVM junto con el patrón Repository para separar la interfaz, la lógica y el acceso a los datos.

```text
          VIEW
    PantallaTareas.kt
           |
      Eventos de UI
           |
           v
        VIEWMODEL
    TareasViewModel.kt
           |
      viewModelScope
           |
           v
       REPOSITORY
   TareasRepository.kt
           |
           v
  TareasRepositoryLocal.kt
```

- **View:** muestra la interfaz y comunica las acciones del usuario.
- **ViewModel:** administra los estados y ejecuta las corrutinas.
- **Repository:** contiene las operaciones de carga de datos y cálculo de números primos.

## Observación del Paso 1

En el primer paso, al ejecutar la tarea larga utilizando `Thread.sleep(4000)` en el hilo principal, la pantalla dejaba de responder durante aproximadamente cuatro segundos.

El mensaje «Trabajando...» no llegaba a mostrarse porque el hilo principal estaba ocupado y no podía actualizar la interfaz hasta que terminara la espera.

Si la espera fuera de diez segundos, Android podría mostrar un mensaje indicando que la aplicación no responde (ANR), ya que el hilo principal permanecería bloqueado demasiado tiempo.

## Capturas de pantalla

### 1. Tarea con progreso

<img width="196" height="404" alt="image" src="https://github.com/user-attachments/assets/3d2344da-da62-4e0e-9913-bc86d214f4b1" />


### 2. Tarea cancelada

<img width="197" height="411" alt="image" src="https://github.com/user-attachments/assets/6c4ee596-870f-4884-a967-661cc1ecd356" />


### 3. Conteo de números primos

<img width="200" height="413" alt="image" src="https://github.com/user-attachments/assets/baa347ab-25f5-4008-91d0-d3b4089185c1" />


### 4. Carga secuencial y en paralelo

<img width="197" height="407" alt="image" src="https://github.com/user-attachments/assets/551cec75-eb1b-46d4-990c-6932abcb9677" />

<img width="209" height="419" alt="image" src="https://github.com/user-attachments/assets/ed4d7a7e-a6de-48ef-b8ff-685d2bfe9d96" />


### 5. Error simulado y reintento

<img width="197" height="412" alt="image" src="https://github.com/user-attachments/assets/ec85525d-b40c-4c5c-a825-36597aa7e1b1" />

<img width="211" height="416" alt="image" src="https://github.com/user-attachments/assets/d7b166b3-80a9-491c-a984-d458e633c366" />


## Para pensar

### 1. ¿Por qué no se veía Trabajando... en el Paso 1?

Porque `Thread.sleep()` bloqueaba el hilo principal. Aunque el mensaje se cambiaba a «Trabajando...», la interfaz no podía actualizarse hasta que terminara la espera.

### 2. ¿Qué diferencia hay entre Thread.sleep y delay?

`Thread.sleep()` bloquea el hilo durante el tiempo indicado. En cambio, `delay()` suspende la corrutina sin bloquear el hilo, permitiendo que otras tareas continúen ejecutándose.

### 3. ¿Por qué Dispatchers.Default para los primos y Dispatchers.IO para la red?

`Dispatchers.Default` está pensado para operaciones que requieren procesamiento de CPU, como calcular números primos. `Dispatchers.IO` está pensado para operaciones de entrada y salida, como acceder a archivos o realizar operaciones de red.

### 4. ¿Qué pasaría con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple viewModelScope?

Cuando el `ViewModel` se elimina porque su ciclo de vida termina, las corrutinas iniciadas con `viewModelScope` se cancelan automáticamente. Esto evita que las tareas continúen innecesariamente.

### 5. ¿Por qué async tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?

Con `async`, las dos cargas se ejecutan al mismo tiempo. Como una tarda aproximadamente dos segundos y la otra tres, el tiempo total es cercano a tres segundos, en lugar de cinco segundos que tarda la ejecución secuencial.

Si una de las cargas falla, la excepción se propaga y se debe manejar con `try/catch` para mostrar el error y permitir reintentar la operación.

## Conclusión

En este laboratorio se aplicaron corrutinas, `ViewModel`, `StateFlow`, `withContext`, `async` y cancelación de tareas para evitar bloquear el hilo principal. También se implementó el manejo de errores y el reintento de cargas.
