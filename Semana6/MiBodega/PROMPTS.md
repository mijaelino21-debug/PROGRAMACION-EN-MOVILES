
## Registro de Prompts e Iteración Técnica

**Proyecto:** MiBodega App  
**Rama:** `ia`

---

### 1. Módulo de Autenticación y Validaciones
* **Prompt:**
  > "Diseñar el flujo de autenticación para `PantallaLogin` y `RegistroScreen`, agregando validaciones de campos obligatorios y formato de correo antes de permitir el ingreso."
* **Solución:**
  Se implementaron validaciones previas de estado y mensajes de error en los campos de texto antes de disparar la navegación.

---

### 2. Navegación y Persistencia de Pedidos (*State Hoisting*)
* **Prompt:**
  > "¿Cómo se estructuran los estados de `carrito` y `pedidos` en `ClienteApp.kt` para que las compras de `ConfirmacionScreen` se guarden dinámicamente en `PedidosScreen`?"
* **Solución:**
  Se elevó el estado de `pedidos` en el `NavHost`, registrando un nuevo `PedidoReal` al confirmar la compra y reseteando la lista del carrito sin perder el historial.

---


### 3. Sincronización de Contratos y Callbacks en `ClienteApp.kt`
* **Prompt:**
  > "Ayudame a sincronizar el grafo de navegación con los componentes reales, resolviendo referencias a `PantallaLogin` y alineando los callbacks de `CarritoScreen`."
* **Solución:**
  Se ajustaron las firmas en `ClienteApp.kt` para coincidir exactamente con los tipos de datos expuestos (`Producto` e `ItemCarrito`) y los nombres de composables.
