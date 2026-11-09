package com.tecsup.catalogo.ui.admin

import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.lista.FakeHotelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

private fun hotel(id: Int, activo: Boolean = true) = Hotel(
    id = id,
    nombre = "Hotel $id",
    descripcion = "Descripcion",
    ciudad = "Lima",
    precioNoche = 100.0,
    estrellas = 3,
    imagenUrl = "https://example.com/$id.jpg",
    esFavorito = false,
    activo = activo
)

@OptIn(ExperimentalCoroutinesApi::class)
class AdminHotelesViewModelTest {

    private lateinit var repo: FakeHotelRepository

    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repo = FakeHotelRepository()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel() = AdminHotelesViewModel(repo)

    private fun AdminHotelesViewModel.hoteles(): List<Hotel> =
        (estado.value as UiState.Exito).datos

    @Test
    fun `con internet muestra todos los hoteles incluidos inactivos`() {
        repo.remotos = listOf(
            hotel(1, activo = true),
            hotel(2, activo = false)
        )

        val vm = crearViewModel()

        assertTrue(vm.estado.value is UiState.Exito)
        assertEquals(2, vm.hoteles().size)
    }

    @Test
    fun `sin internet y sin datos guardados muestra el error`() {
        repo.falla = IOException()

        val vm = crearViewModel()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
    }

    @Test
    fun `agregar hotel actualiza la lista`() {
        repo.remotos = listOf(hotel(1))
        val vm = crearViewModel()

        val nuevo = hotel(0).copy(nombre = "Nuevo Hotel")
        vm.guardarHotel(nuevo)

        assertEquals("Hotel agregado exitosamente", vm.mensaje.value)
    }

    @Test
    fun `cambiar estado de hotel actualiza el estado`() {
        repo.remotos = listOf(hotel(1, activo = true))
        val vm = crearViewModel()

        vm.cambiarEstado(1, false)

        assertEquals("Hotel desactivado", vm.mensaje.value)
    }

    @Test
    fun `eliminar hotel remueve el hotel`() {
        repo.remotos = listOf(hotel(1))
        val vm = crearViewModel()

        vm.eliminarHotel(1)

        assertEquals("Hotel eliminado", vm.mensaje.value)
    }
}
