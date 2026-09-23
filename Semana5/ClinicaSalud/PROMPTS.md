# Prompts del Proyecto (Rama mejora-ia)

## 1. Agregar Botón Cancelar en Reservas
> "Agrega un botón 'Cancelar' en `ReservasScreen.kt` visible únicamente en las reservas en estado 'Confirmada'. Al hacer clic, debe actualizar de inmediato el estado a 'Cancelada', cambiar el color del chip a rojo y ocultar el botón."

## 2. Actualización de Estado en Tiempo Real
> "Separa la tarjeta de reservas en un componente `ReservaCardItem` utilizando un estado local reactivo (`mutableStateOf`) para que al presionar 'Cancelar' la UI se redibuje al instante sin necesidad de salir o recargar la pantalla."
