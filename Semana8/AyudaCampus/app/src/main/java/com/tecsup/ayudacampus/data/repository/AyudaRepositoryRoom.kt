package com.tecsup.ayudacampus.data.repository

import com.tecsup.ayudacampus.data.local.dao.CategoriaDao
import com.tecsup.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(
    private val categoriaDao: CategoriaDao
    // TODO (Paso 4): agrega usuarioDao y ticketDao
) : AyudaRepository {

    // Categorías
    override fun observarCategorias(): Flow<List<Categoria>> = categoriaDao.observarTodas()
    override fun observarCategoriasActivas(): Flow<List<Categoria>> = categoriaDao.observarActivas()
    override suspend fun guardarCategoria(c: Categoria) { categoriaDao.insertar(c) }
    override suspend fun eliminarCategoria(c: Categoria) { categoriaDao.eliminar(c) }
    override suspend fun cambiarActiva(id: Int, activa: Boolean) { categoriaDao.cambiarActiva(id, activa) }

    // TODO (Paso 4): usuarios, tickets y panel
}