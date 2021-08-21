package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_minha_conta.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.view.LoginActivity
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.startActivity

class MinhaContaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minha_conta)

        alterar_senha.setOnClickListener {
            startActivity<AlterarSenhaActivity>()
        }

        seu_endereco.setOnClickListener {
            startActivity<EnderecoUsuarioActivity>()
        }

        sair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            if( FirebaseAuth.getInstance().uid == null ){

                database.use {
                    delete("DadosLogin")
                }

                startActivity<LoginActivity>()
                finishFromChild(this)
            }
        }

    }
}
