package com.tecsup.catalogo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [HotelEntity::class, ReservaEntity::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hotelDao(): HotelDao
    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE hoteles ADD COLUMN servicios TEXT NOT NULL DEFAULT 'Wi-Fi gratis,Piscina,Aire acondicionado,Restaurante,Recepción 24/7'")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS reservas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        usuarioId TEXT NOT NULL,
                        hotelId INTEGER NOT NULL,
                        nombreHotel TEXT NOT NULL,
                        fechaEntrada TEXT NOT NULL,
                        fechaSalida TEXT NOT NULL,
                        cantidadHuespedes INTEGER NOT NULL,
                        precioPorNoche REAL NOT NULL,
                        cantidadNoches INTEGER NOT NULL,
                        precioTotal REAL NOT NULL,
                        estado TEXT NOT NULL,
                        fechaCreacion INTEGER NOT NULL
                    )
                """)
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservas ADD COLUMN imagenUrl TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE reservas ADD COLUMN ciudad TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservas ADD COLUMN motivoRechazo TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE hoteles ADD COLUMN activo INTEGER NOT NULL DEFAULT 1")
            }
        }

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hoteles_db"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
