package livrokotlin.com.farmaciaesperanca.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_visitante.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.view.categorias.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class VisitanteActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitante)

        init()
    }

    private fun init(){

        btn_categoria_saude2.setOnClickListener {
            startActivity<CategoriaSaudeActivity>()
        }

        btn_categoria_infantil2.setOnClickListener {
            startActivity<CategoriaInfantilActivity>()
        }

        btn_categoria_higiene2.setOnClickListener {
            startActivity<CategoriaHigieneActivity>()
        }

        btn_categoria_beleza2.setOnClickListener {
            startActivity<CategoriaBelezaActivity>()
        }

        btn_verMais2.setOnClickListener {
            startActivity<CategoriasActivity>()
        }

    }

    private fun msgCadastrar(){

        alert("Para acessar esse conteúdo você deverá estar conectado em uma conta.", "Fazer Loggin ou Cadastro"){
            yesButton {
                finishFromChild(this@VisitanteActivity)
                startActivity<LoginActivity>()
            }
            noButton {}
        }.show()

    }

}
