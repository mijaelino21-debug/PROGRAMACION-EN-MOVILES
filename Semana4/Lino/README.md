# Mi Carrito TECSUP

Nombre: Mijael Lino Barja

## Descripción

La app es un carrito de compras donde puedes 
agregar productos con nombre/precio/cantidad, ver la lista, eliminar 
productos y ver el total con IGV calculado automáticamente


## Capturas

### Carrito vacío
<img width="583" height="476" alt="image" src="https://github.com/user-attachments/assets/0d2db2d4-3afb-4e25-a589-24b23936b0bb" />


### Carrito con productos
<img width="583" height="490" alt="image" src="https://github.com/user-attachments/assets/01566f04-75c9-4166-860b-1c3014cc63dd" />

### Carrito con un producto eliminado
<img width="450" height="367" alt="image" src="https://github.com/user-attachments/assets/feb6f3bb-e541-48c7-9607-cb44a43d7a0b" />


## Respuestas conceptuales

**a) ¿Por qué usar `mutableStateListOf` y no una `MutableList` normal?**

Porque mutableStateListOf avisa a Compose cuando cambia, entonces la pantalla se actualiza sola. Una MutableList normal no avisa nada, así que la pantalla no se redibujaría al agregar/quitar productos.

**b) ¿Por qué la lista de productos se declara con `val`?**

Es val porque la variable que no cambia es la lista misma , lo que cambia es el contenido de adentro (los productos), y eso se puede modificar sin necesidad de reasignar con var.

**c) ¿Que hace weight(1f) en Lazy column ?**

hace que la lista ocupe todo el espacio que sobra en la pantalla, así el panel de totales queda siempre pegado abajo, sin importar cuántos productos haya.

