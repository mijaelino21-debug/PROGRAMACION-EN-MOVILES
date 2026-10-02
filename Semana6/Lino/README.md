# TECSUP Store — Aplicación Móvil

Nombre: Mijael Lino Barja

## Descripción

Realizamos una Aplicación móvil desarrollada en Kotlin con Jetpack Compose para la gestión de productos y carrito de compras. Incluye:

* Navegación contextual mediante DropdownMenu en cada tarjeta de producto (Favoritos, Compartir, Reportar).

* Navegación global mediante NavigationDrawer entre las vistas de Inicio, Mis pedidos, Favoritos y Perfil.

* Elevación de estado (State Hoisting) para alimentar dinámicamente un contador con BadgedBox en la opción Favoritos del menú lateral.

## Resultados y Funcionalidades

Formulario para agregar productos al carrito dinámicamente.

Menú desplegable contextual en cada producto con el ícono de tres puntos (MoreVert).

Drawer lateral personalizado con encabezado de usuario, indicación visual de la sección activa e ícono de cierre de sesión.

Contador reactivo en tiempo real (BadgedBox) en el menú lateral que refleja el número de elementos en favoritos.

## Capturas de los resultados sin IA

### Menú lateral de navegación con perfil de usuario
<img width="336" height="588" alt="image" src="https://github.com/user-attachments/assets/4f936779-9930-425c-86ba-a0a2914da86a" />


### Pantalla Carrito con DropdownMenu
<img width="338" height="675" alt="image" src="https://github.com/user-attachments/assets/1fd25f3a-d816-46c2-b8af-191acb9479e2" />


### Pantalla Favoritos
<img width="327" height="617" alt="image" src="https://github.com/user-attachments/assets/91401bee-81f9-4c75-8bac-5d5fe76e387d" />

## Capturas de los resultados con ia 

<img width="336" height="713" alt="image" src="https://github.com/user-attachments/assets/810bb303-62c5-47ad-b17c-06373d5b2793" />

<img width="337" height="724" alt="image" src="https://github.com/user-attachments/assets/f06ea550-c407-4309-aa58-c86ee4e22eb2" />

<img width="330" height="725" alt="image" src="https://github.com/user-attachments/assets/a41c1528-55ec-43d5-bf92-ffa233a7f26c" />


## Respuestas conceptuales

## ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa, y no en cualquier parte de la pantalla?}

Porque Jetpack Compose requiere el contenedor Box para definir la posición relativa de anclaje  del menú respecto al botón. De declararse fuera, perdería su referencia posicional y se renderizaría en la esquina superior izquierda de la pantalla.

## ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu (afectan solo a un producto) y las del NavigationDrawer (afectan a toda la app)?

El DropdownMenu tiene un alcance local y contextual, operando de forma exclusiva sobre la entidad Producto donde fue activado. El NavigationDrawer tiene un alcance global y estructural, modificando el estado principal de navegación de toda la aplicación .

## ¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el DropdownMenu de cada producto?

tuve que aplicar State Hoisting definiendo listaFavoritos en la composable de nivel superior AppNavegacion.kt. Esta lista se pasó como parámetro a PantallaCarrito y AppDrawer. Al presionar Favoritos en el DropdownMenu, la lista observable se actualiza y genera la recomposición automática del badge en el AppDrawer.

## ¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos?

Se corrigieron descalces en la firma del callback de TarjetaProducto (onAgregarFavorito), se estandarizaron las rutas de los paquetes (com.tecsup.lino y com.tecsup.lino.Screens) y se pasó obligatoriamente la función onMenuClick a todas las pantallas para evitar errores de compilación.

## Observaciones y conclusiones

Observaciones

Las inconsistencias de capitalización en el nombre de los paquetes (screens vs Screens) generaron errores de referencia no resuelta  en las importaciones.

Al modificar los parámetros del componente TarjetaProducto, fue necesario actualizar en cascada sus invocaciones en PantallaCarrito y PantallaFavoritos.

Conclusiones

La Fase 1 permitió asimilar la sintaxis y maquetación de Compose, mientras que la Fase 2 aceleró la implementación de lógica reactiva como State Hoisting mediante el soporte de la IA.

La asistencia con IA no sustituye el criterio técnico; fue indispensable comprender la arquitectura para resolver los fallos de firmas de parámetros e importaciones entre archivos.

