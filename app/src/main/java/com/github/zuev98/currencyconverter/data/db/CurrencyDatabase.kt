package com.github.zuev98.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.zuev98.currencyconverter.data.dao.CurrencyDao
import com.github.zuev98.currencyconverter.data.entity.Currency

@Database(entities = [Currency::class], version = 3, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

    companion object {
        private const val DB_NAME = "currency-database"

        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): CurrencyDatabase {
            return Room.databaseBuilder(
                context,
                CurrencyDatabase::class.java,
                DB_NAME
            )
                .addMigrations(migration_1_2, migration_2_3)
                .build()
        }
    }
}

private val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Currency ADD COLUMN lastUpdate TEXT NOT NULL DEFAULT ''"
        )
    }
}

private val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS temp_currency (" +
                    "id TEXT NOT NULL," +
                    "charCode TEXT NOT NULL," +
                    "nominal INTEGER NOTNULL," +
                    "name TEXT NOT NULL," +
                    "value REAL NOT NULL," +
                    "lastUpdate TEXT NOT NULL," +
                    "PRIMARY KEY(id))"
        )

        database.execSQL("DROP TABLE Currency")

        database.execSQL("ALTER TABLE temp_currency RENAME TO Currency")
    }
}

