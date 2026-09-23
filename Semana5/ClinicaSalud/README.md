# Actividad — Clinica 


**Autor:** Mijael Lino


## Requerimientos Funcionales

**RF01 — Búsqueda y selección de médico**  
El sistema permite al paciente filtrar médicos por especialidad mediante chips (`LazyRow`, categorías: Todas, Cardiología, Pediatría, Dermatología)[cite: 1], mostrando los médicos cuya categoría coincide con la seleccionada[cite: 1], y visualizar la lista completa de médicos disponibles (`LazyColumn`)[cite: 1], cada uno con nombre, especialidad y calificación[cite: 1]. Al seleccionar un médico, se muestra su perfil completo —con años de experiencia y descripción—[cite: 1] recibiendo los datos por parámetro de navegación.

**RF02 — Agendamiento de citas**  
El sistema permite al paciente seleccionar una fecha (3 opciones)[cite: 1] y una hora (3 opciones)[cite: 1] de forma única para su cita con el médico elegido, y muestra una pantalla de confirmación con el resumen de la cita agendada (médico, fecha, hora) y un botón para dirigirse a "Mis citas"[cite: 1].

**RF03 — Navegación mediante menú lateral**  
El sistema ofrece un menú lateral (*drawer*), accesible mediante un ícono ☰ en la barra superior[cite: 1], para la navegación entre las pantallas de la aplicación[cite: 1].

**RF04 — Gestión de citas del paciente (Resultados sin IA)**  
El sistema permite al paciente visualizar el listado de sus citas agendadas (`LazyColumn`)[cite: 1], diferenciando visualmente su estado (`Confirmada` / `Completada`)[cite: 1] y permitiendo marcar una cita confirmada como completada[cite: 1].

**RF05 — Cancelación de Citas mediante Diálogo (Resultados con IA)**  
El sistema incorpora mediante IA la funcionalidad de cancelación de citas en tiempo real. Al presionar "Cancelar", se despliega un diálogo de confirmación con la consulta *"¿Estás seguro de que deseas cancelar tu cita con Dra. Ana Torres?"* y las opciones *"No, mantener"* y *"Si, cancelar"*[cite: 1]. Al confirmar, el estado cambia de forma inmediata a `Cancelada` en un chip rojo[cite: 1] mediante el uso de estado reactivo local.


## Capturas

RESULTADOS SIN IA CLINICA

<img width="544" height="929" alt="image" src="https://github.com/user-attachments/assets/04f8d272-8f4f-4e46-95f5-a97875c19b9e" />

<img width="531" height="1133" alt="image" src="https://github.com/user-attachments/assets/392e75d3-f25b-4d2c-a1d2-f0fa8ed4f3bc" />

<img width="548" height="1167" alt="image" src="https://github.com/user-attachments/assets/244ac7fc-e07d-4fa2-95be-1cf6d880cbd9" />

<img width="517" height="1091" alt="image" src="https://github.com/user-attachments/assets/6aad53c0-318c-4424-a79a-c235422f236b" />

<img width="539" height="1152" alt="image" src="https://github.com/user-attachments/assets/fb0ad0b6-97cd-4b1f-8cf0-12c907f26c07" />


RESULTADOS CON IA 

<img width="644" height="1159" alt="image" src="https://github.com/user-attachments/assets/ec0fe7cf-f2d0-43d4-a57c-d8936326d600" />

<img width="484" height="700" alt="image" src="https://github.com/user-attachments/assets/c395bcad-0585-4110-823a-dc7b5fb4182d" />




