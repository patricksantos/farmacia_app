package livrokotlin.com.farmaciaesperanca.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable

class SalvarDados(context: Context):
    ManagedSQLiteOpenHelper(ctx = context, name = "LoginApp.db", version = 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            "DadosLogin",
            true,
            "nome" to TEXT,
            "cpf" to TEXT,
            "senha" to TEXT
        )

        db.createTable(
            "produtosMinhaCesta",
            true,
            "nome" to TEXT,
            "categoria" to TEXT,
            "valor" to TEXT,
            "qtd" to TEXT,
            "fotoUrl" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object{

        private var instance: SalvarDados? = null
        @Synchronized
        fun getInstance(ctx: Context): SalvarDados {

            if(instance == null){
                instance =
                    SalvarDados(ctx.applicationContext)
            }

            return instance!!
        }

    }

}

val Context.database: SalvarDados
    get() = SalvarDados.getInstance(
        applicationContext
    )
