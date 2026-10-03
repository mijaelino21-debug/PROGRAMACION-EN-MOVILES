# MiBodega App - Semana 06

Aplicación móvil desarrollada en Android para la gestión de compras en bodegas locales. Incluye catálogo interactivo, filtrado por categorías, carrito de compras dinámico y seguimiento de pedidos en tiempo real.

---

## Capturas 
<img width="500" height="1067" alt="image" src="https://github.com/user-attachments/assets/0414f622-ae40-4812-ac0f-a6a022f80124" />


<img width="576" height="1132" alt="image" src="https://github.com/user-attachments/assets/3398b2d5-dae9-4686-99ee-5b6834566d67" />


<img width="525" height="1119" alt="image" src="https://github.com/user-attachments/assets/1f43f34d-76ca-4f20-bc28-64ee8058c6d1" />


<img width="519" height="1102" alt="image" src="https://github.com/user-attachments/assets/a1b8c749-ac5c-4208-9456-884a14400b10" />


<img width="530" height="1133" alt="image" src="https://github.com/user-attachments/assets/8e786c79-96ea-4523-8712-98eee4b205a0" />


<img width="577" height="1169" alt="image" src="https://github.com/user-attachments/assets/bbf3f222-666e-4837-be13-57cfc335f4c4" />


<img width="553" height="1176" alt="image" src="https://github.com/user-attachments/assets/72fc563c-99ed-4784-acc3-8f20777e78dc" />


## Capturas de las ejecuciones con ia 

<img width="545" height="1174" alt="image" src="https://github.com/user-attachments/assets/7cf6bcf4-6811-4aa0-b093-a1177a6b2648" />

Interfaz de Login mostrando la validación de error en rojo tras ingresar datos incorrectos.


<img width="547" height="1155" alt="image" src="https://github.com/user-attachments/assets/63a40e25-4ee2-4eea-9a38-cae822729f5b" />

Implementación de AlertDialog para confirmar la eliminación de un producto del carrito.


<img width="519" height="1119" alt="image" src="https://github.com/user-attachments/assets/6fd85c5d-18e6-4cbe-b0fa-9ee675ee33b8" />

Validación de campos obligatorios en el formulario de Datos de Entrega con notificación visual de error.


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
