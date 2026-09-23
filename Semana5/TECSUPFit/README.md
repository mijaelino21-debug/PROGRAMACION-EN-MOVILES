# Actividad — TECSUPFit

**Autor:** MIJAEL LINO


## Requerimientos Funcionales

**RF01 — Navegación e Interfaz Principal**  
El sistema permite al usuario navegar entre las 4 secciones principales de la aplicación (*Inicio*, *Reservas*, *Rutinas* y *Perfil*) mediante una barra de navegación inferior (`AppNavigation` / `NavigationBar`), manteniendo un diseño visual coherente, adaptativo y de alto contraste.

**RF02 — Gestión e Historial de Reservas**  
El sistema permite al usuario visualizar sus clases agendadas (`ReservasScreen` / `LazyColumn`), diferenciando el estado de cada reserva (`Confirmada` en verde mint, `Completada` en gris y `Cancelada` en rojo pastel) junto con la barra de énfasis y sus horarios correspondientes.

**RF03 — Cancelación de Reservas en Tiempo Real**  
El sistema habilita un botón **"Cancelar"** en las tarjetas de reservas en estado "Confirmada". Al hacer clic, el estado cambia a "Cancelada" de forma instantánea mediante un estado local reactivo (`mutableStateOf` en `ReservaCardItem`), ocultando el botón y actualizando el chip de estado sin necesidad de recargar ni cambiar de pantalla.

**RF04 — Módulo de Inteligencia Artificial (IA)**  
El sistema muestra en la sección "Rutinas" un consejo diario dinámico de salud e hidratación generado por el motor de IA (`IAService`), además de un listado de recomendaciones personalizadas de clases y entrenamientos (`IARecomendacionModel`) adaptadas al nivel de condición física del usuario.



## Capturas

RESULTADOS OBTENIDOS DE TECSUP FIT 

<img width="601" height="981" alt="image" src="https://github.com/user-attachments/assets/36015359-2a03-4a46-bd26-469a9ea3323a" />

<img width="581" height="1242" alt="image" src="https://github.com/user-attachments/assets/58df945f-4b80-432d-9f3c-edc1b9965b5a" />

<img width="627" height="1252" alt="image" src="https://github.com/user-attachments/assets/192b348f-6395-4e12-8731-0fb114684787" />

<img width="616" height="1255" alt="image" src="https://github.com/user-attachments/assets/67e6a6b9-f8c4-4f8c-8a8f-e51ee3819652" />

<img width="583" height="1250" alt="image" src="https://github.com/user-attachments/assets/1c8af517-456f-4fe8-9951-0f9422fa77b0" />

MEJORA CON IA 

<img width="545" height="1128" alt="image" src="https://github.com/user-attachments/assets/9223bc36-4560-466c-bdc6-8f89786b5509" />

<img width="825" height="1169" alt="image" src="https://github.com/user-attachments/assets/16b1187a-9f1a-4e51-b042-b36b5b715a69" />







