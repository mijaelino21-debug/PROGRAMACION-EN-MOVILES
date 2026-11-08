package com.tecsup.catalogo.ui.lista

import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

private fun hotel(id: Int, favorito: Boolean = false) = Hotel(
    id = id,
    nombre = "Hotel $id",
    descripcion = "Descripcion",
    ciudad = "Lima",
    precioNoche = 100.0,
    estrellas = 3,
    imagenUrl = "https://example.com/$id.jpg",
    esFavorito = favorito
)

/** Repositorio falso: todo en memoria, sin Retrofit ni Room. */
class FakeHotelRepository : HotelRepository {
    private val guardados = MutableStateFlow<List<Hotel>>(emptyList())

    var remotos: List<Hotel> = emptyList()   // lo que "devuelve la API"
    var falla: Throwable? = null             // si no es null, refrescar() falla
    var vecesRefrescado = 0

    fun precargar(lista: List<Hotel>) {
        guardados.value = lista
    }

    override val items: Flow<List<Hotel>> = guardados
    override val favoritos: Flow<List<Hotel>> =
        guardados.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> {
        vecesRefrescado++
        falla?.let { return Result.failure(it) }
        guardados.value = remotos
        return Result.success(remotos.size)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Hotel> =
        Result.success(guardados.value.first { it.id == id })

    override suspend fun alternarFavorito(id: Int) {
        guardados.value = guardados.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada(): Int = guardados.value.size
    override suspend fun cantidadFavoritos(): Int = guardados.value.count { it.esFavorito }
    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }
}

/** Notificador falso: guarda los avisos en una lista para poder revisarlos. */
class FakeNotificador : Notificador {
    val avisos = mutableListOf<Pair<String, String>>()
    override fun mostrar(titulo: String, texto: String) {
        avisos.add(titulo to texto)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private lateinit var repo: FakeHotelRepository
    private lateinit var notificador: FakeNotificador

    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repo = FakeHotelRepository()
        notificador = FakeNotificador()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel() = ListaViewModel(repo, notificador)

    private fun ListaViewModel.hoteles(): List<Hotel> =
        (estado.value as UiState.Exito).datos

    @Test
    fun `con internet muestra la lista y avisa con una notificacion`() {
        repo.remotos = listOf(hotel(1), hotel(2))

        val vm = crearViewModel()

        assertTrue(vm.estado.value is UiState.Exito)
        assertEquals(2, vm.hoteles().size)
        assertEquals("Se guardaron 2 elementos", notificador.avisos.last().second)
        assertNull(vm.aviso.value)
    }

    @Test
    fun `sin internet y sin datos guardados muestra el error`() {
        repo.falla = IOException()

        val vm = crearViewModel()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
        assertTrue(notificador.avisos.isEmpty())
    }

    @Test
    fun `sin internet pero con datos guardados muestra la lista y el aviso`() {
        repo.precargar(listOf(hotel(1), hotel(2)))
        repo.falla = IOException()

        val vm = crearViewModel()

        assertTrue(vm.estado.value is UiState.Exito)
        assertEquals(2, vm.hoteles().size)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
    }

    @Test
    fun `reintentar despues de un error vuelve a pedir los datos`() {
        repo.falla = IOException()
        val vm = crearViewModel()
        assertTrue(vm.estado.value is UiState.Error)

        repo.falla = null
        repo.remotos = listOf(hotel(1))
        vm.reintentar()

        assertTrue(vm.estado.value is UiState.Exito)
        assertEquals(1, vm.hoteles().size)
        assertEquals(2, repo.vecesRefrescado)
    }

    @Test
    fun `marcar un favorito lo muestra en el filtro de favoritos`() {
        repo.remotos = listOf(hotel(1), hotel(2))
        val vm = crearViewModel()

        vm.toggleFiltroFavoritos()
        assertTrue(vm.hoteles().isEmpty())

        vm.onFavoritoClicked(1)

        assertEquals(listOf(1), vm.hoteles().map { it.id })
    }
}