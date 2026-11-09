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
    esFavorito = favorito,
    activo = true
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
    override val hotelesActivos: Flow<List<Hotel>> =
        guardados.map { lista -> lista.filter { it.activo } }
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

    override suspend fun agregarHotel(hotel: Hotel): Result<Unit> {
        guardados.value = guardados.value + hotel
        return Result.success(Unit)
    }

    override suspend fun actualizarHotel(hotel: Hotel): Result<Unit> {
        guardados.value = guardados.value.map { if (it.id == hotel.id) hotel else it }
        return Result.success(Unit)
    }

    override suspend fun eliminarHotel(id: Int): Result<Unit> {
        guardados.value = guardados.value.filter { it.id != id }
        return Result.success(Unit)
    }

    override suspend fun cambiarEstadoHotel(id: Int, activo: Boolean): Result<Unit> {
        guardados.value = guardados.value.map { if (it.id == id) it.copy(activo = activo) else it }
        return Result.success(Unit)
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

    @Test
    fun `buscar por nombre filtra los hoteles`() {
        repo.remotos = listOf(
            hotel(1).copy(nombre = "Hotel Marriot"),
            hotel(2).copy(nombre = "Hotel Sheraton")
        )
        val vm = crearViewModel()
        vm.onSearchQueryChanged("Marriot")
        assertEquals(1, vm.hoteles().size)
        assertEquals("Hotel Marriot", vm.hoteles().first().nombre)
    }

    @Test
    fun `filtrar por ciudad muestra solo los hoteles de esa ciudad`() {
        repo.remotos = listOf(
            hotel(1).copy(ciudad = "Lima"),
            hotel(2).copy(ciudad = "Cusco")
        )
        val vm = crearViewModel()
        vm.onCitySelected("Cusco")
        assertEquals(1, vm.hoteles().size)
        assertEquals("Cusco", vm.hoteles().first().ciudad)
    }

    @Test
    fun `ordenar por precio menor a mayor`() {
        repo.remotos = listOf(
            hotel(1).copy(id = 1, precioNoche = 200.0),
            hotel(2).copy(id = 2, precioNoche = 50.0),
            hotel(3).copy(id = 3, precioNoche = 100.0)
        )
        val vm = crearViewModel()
        vm.onSortOrderChanged(SortOrder.PRICE_LOW_TO_HIGH)
        val precios = vm.hoteles().map { it.precioNoche }
        assertEquals(listOf(50.0, 100.0, 200.0), precios)
    }

    @Test
    fun `ordenar por mejor calificacion`() {
        repo.remotos = listOf(
            hotel(1).copy(id = 1, estrellas = 3),
            hotel(2).copy(id = 2, estrellas = 5),
            hotel(3).copy(id = 3, estrellas = 4)
        )
        val vm = crearViewModel()
        vm.onSortOrderChanged(SortOrder.BEST_RATING)
        val estrellas = vm.hoteles().map { it.estrellas }
        assertEquals(listOf(5, 4, 3), estrellas)
    }

    @Test
    fun `filtros combinados de ciudad y calificacion`() {
        repo.remotos = listOf(
            hotel(1).copy(id = 1, ciudad = "Lima", estrellas = 5),
            hotel(2).copy(id = 2, ciudad = "Lima", estrellas = 3),
            hotel(3).copy(id = 3, ciudad = "Cusco", estrellas = 5)
        )
        val vm = crearViewModel()
        vm.onCitySelected("Lima")
        vm.onRatingSelected(RatingFilter.FIVE)
        val lista = vm.hoteles()
        assertEquals(1, lista.size)
        assertEquals(1, lista.first().id)
    }

    @Test
    fun `limpiar filtros restablece la lista completa`() {
        repo.remotos = listOf(
            hotel(1).copy(id = 1, nombre = "Marriot", ciudad = "Lima"),
            hotel(2).copy(id = 2, nombre = "Sheraton", ciudad = "Cusco")
        )
        val vm = crearViewModel()
        vm.onSearchQueryChanged("Marriot")
        vm.onCitySelected("Lima")
        assertEquals(1, vm.hoteles().size)

        vm.limpiarFiltros()
        assertEquals(2, vm.hoteles().size)
    }
}
