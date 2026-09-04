# Laboratorio 03: Registro de Producto

**Estudiante:** Mijael Lino
**Curso:** Programación Móvil

## PARTE A - LAB SEM 3

## Descripción
Creamos una aplicación móvil desarrollada en Android Studio con Jetpack Compose para el registro de productos.Tambien valida entradas numéricas y calcula el importe total en una tarjeta de resumen.

## Capturas de Pantalla
<img width="736" height="415" alt="image" src="https://github.com/user-attachments/assets/03dba4fa-8152-4df4-9e95-e601cae19e83" />
<img width="1344" height="701" alt="image" src="https://github.com/user-attachments/assets/c0c6101d-54f0-4e03-9857-3940cff3c034" />


## PARTE B - LAB SEM 3

## Mejora con IA (Parte B)

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
| :--- | :--- | :--- |
| "Por favor modifica la función PantallaRegistro... Agrega validación de campos vacíos... y un botón Limpiar..." | Generó el estado `mensajeError`, la validación básica con `isBlank()`, el mensaje en rojo y un `Button` básico para limpiar. | Acepté el reseteo de campos y el mensaje de error. Cambié el botón Limpiar por `OutlinedButton` para reducir su jerarquía visual respecto al botón principal y agregué la comprobación `toDoubleOrNull()` para manejar el caso de letras ingresadas en campos numéricos. |


## Pregunta de Reflexión
**¿Qué pasaría si declaras las variables de los campos SIN `remember`?**

Si se omite `remember` y se declara únicamente `val nombre = mutableStateOf("")`, el estado se reiniciará a su valor por defecto (`""`) en cada recomposición. Cuando el usuario escriba una letra, Compose volverá a ejecutar la función `PantallaRegistro`, reiniciando la variable y borrando de inmediato el texto ingresado.
