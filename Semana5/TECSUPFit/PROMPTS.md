# Documentación del Proyecto: TECSUPFit (Rama mejora-ia)

## Descripción General
Este proyecto integra funcionalidades impulsadas por Inteligencia Artificial (IA) y mejoras en la gestión de reservas dentro de la aplicación móvil **TECSUPFit**, desarrollada en Android con Jetpack Compose.

---

## Requerimientos Funcionales de IA Implementados

### RF-IA-01: Modelo de Datos para Recomendaciones (`IARecomendacionModel.kt`)
- **Estructura**: Define la entidad de datos para estructurar la información sugerida por el motor de IA.
- **Atributos**: `id`, `titulo`, `descripcion` y `nivel`.

### RF-IA-02: Servicio de Inteligencia Artificial (`IAService.kt`)
- **Lógica de negocio**: Repositorio encargado de procesar y proveer las recomendaciones personalizadas.
- **Funcionalidades**:
  - `obtenerConsejoDiario()`: Retorna un consejo diario dinámico sobre salud y rendimiento físico.
  - `obtenerRecomendaciones(nivel: String)`: Filtra y retorna listas de clases recomendadas según el nivel del usuario.

### RF-IA-03: Interfaz de Usuario IA (`IAScreen.kt`)
- **UI Compose**: Pantalla dedicada a la visualización de los consejos y sugerencias de IA.
- **Componentes**:
  - Tarjeta superior destacada para el **Consejo del día**.
  - Listado de tarjetas interactivas para las **Clases Recomendadas**.
  - Paleta de colores optimizada para contraste y legibilidad.

### RF-IA-04: Integración en la Navegación (`AppNavigation.kt`)
- **Rutero**: Integración del módulo de IA dentro del flujo principal de navegación de la app.
- **Pestaña**: Conectado directamente a la pestaña **Rutinas** en el menú de navegación inferior (`NavigationBar`).

---

## Requerimiento Adicional: Gestión de Reservas

### RF-IA-05: Cancelación de Reservas en Tiempo Real (`ReservasScreen.kt`)
- **Descripción**: Permite a los usuarios cancelar sus reservas confirmadas de manera rápida e intuitiva.
- **Comportamiento**:
  - Inclusión del botón **"Cancelar"** en tarjetas con estado `"Confirmada"`.
  - Manejo de estado local reactivo (`mutableStateOf`) dentro del componente `ReservaCardItem`.
  - Actualización instantánea en la interfaz de usuario: cambio del estado a `"Cancelada"`, actualización de color del chip informativo a rojo y ocultamiento automático del botón.

---

## Instrucciones de Ejecución
1. Abrir el proyecto en **Android Studio**.
2. Sincronizar las dependencias de Gradle.
3. Ejecutar la aplicación en un emulador o dispositivo físico (**Shift + F10**).
4. Navegar a la pestaña **Rutinas** para probar el módulo de IA y a **Reservas** para validar la cancelación de clases.
