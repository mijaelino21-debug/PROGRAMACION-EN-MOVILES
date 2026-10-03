# MiBodega App - Semana 06

Aplicación móvil desarrollada en Android para la gestión de compras en bodegas locales. Incluye catálogo interactivo, filtrado por categorías, carrito de compras dinámico y seguimiento de pedidos en tiempo real.

---

## Capturas 
![img.png](img.png)

![img_1.png](img_1.png)

![img_2.png](img_2.png)

![img_3.png](img_3.png)

![img_4.png](img_4.png)

![img_5.png](img_5.png)

![img_6.png](img_6.png)

## Capturas de las ejecuciones con ia 

![img_7.png](img_7.png)

![img_10.png](img_10.png)

![img_11.png](img_11.png)


##  Preguntas de Reflexión

### 1. ¿Por qué `Producto.kt` y `MainActivity.kt` se entregaron completos, y las pantallas no?
`Producto.kt` define el modelo base y `MainActivity.kt` es el punto de entrada de la app; ambos forman la infraestructura del proyecto. Las pantallas se entregaron como esquemas para trabajar la "maquetación" , el manejo de eventos y la lógica reactiva de la interfaz.

### 2. ¿Cómo lograste que el filtro de categoría y el carrito reaccionen automáticamente?
Se utilizó el sistema de estado de Jetpack Compose (`remember` y `mutableStateOf`). Al modificar una variable de estado, Compose gatilla la *recomposición* automática de la UI, actualizando los elementos visibles de forma inmediata sin refrescos manuales.

### 3. ¿Diferencia entre `navigate()` normal y `popUpTo`?
El `navigate()` estándar apila la nueva pantalla sobre la anterior permitiendo regresar con el botón "Atrás". En cambio, `popUpTo` remueve pantallas previas del historial (como el flujo de pago tras la confirmación), evitando que el usuario vuelva a completar un pedido ya procesado.

### 4. ¿Qué corregiste del código generado por la IA para el buscador en tiempo real?
Se ajustó la sincronización entre el texto ingresado y la lista filtrada, controlando el manejo de mayúsculas/minúsculas (`lowercase()`) y corrigiendo desacoples en los nombres de callbacks dentro de `ClienteApp.kt`.

### 5. ¿`NavigationDrawer` vs `NavigationBar`: cuándo usar cada uno?
- **`NavigationBar` (barra inferior):** Ideal para pocas secciones principales (3 a 5) de acceso rápido y constante con una sola mano.
- **`NavigationDrawer` (menú lateral):** Recomendado para sistemas con múltiples opciones secundarias, configuraciones de perfil o herramientas que no requieren estar fijas en pantalla.



##  Observaciones y Conclusiones

### Observaciones
1. Al integrar `ClienteApp.kt` se presentaron errores de compilación por inconsistencias entre los tipos de datos requeridos por las pantallas hijas (`Producto` e `ItemCarrito`) y los eventos declarados.

2. Fue fundamental elevar el estado de `pedidos` al `NavHost` para evitar la pérdida de información al cambiar entre pestañas.

### Conclusiones
1. Trabajar sobre un esqueleto:Aceleró el desarrollo al contar con una arquitectura base, permitiendo enfocar el esfuerzo en la lógica de navegación y reactividad de estados.

2. La integración de la barra de navegación y el manejo de estado global convirtieron vistas estáticas aisladas en un flujo de compra funcional y coherente.