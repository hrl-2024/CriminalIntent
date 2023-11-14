package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import com.bignerdranch.android.criminalintent.database.migration_1_2
import com.bignerdranch.android.criminalintent.database.migration_2_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "crime-database"

// This class is a singleton (there will only ever be 1 instance of it in the app process.
// Data exists when app is in memory, but not when app is closed (onDestroy).
// Useful to pass data between components
class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
        ).addMigrations(migration_1_2, migration_2_3).build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()

    fun getCrime(id: UUID) : Flow<Crime> = database.crimeDao().getCrime(id)

    fun updateCrime(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
                throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}