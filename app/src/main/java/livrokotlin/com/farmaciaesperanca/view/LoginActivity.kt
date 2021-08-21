package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.database
import livrokotlin.com.farmaciaesperanca.util.*
import livrokotlin.com.farmaciaesperanca.view.conta.EsqSenhaActivity
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txt_cpfLogin.text = cpfGlobal

        init()

    }

    private fun init(){

        mAuth = FirebaseAuth.getInstance()

        criarConta.setOnClickListener {
            startActivity<CadastroActivity>()
            finishFromChild(this)
        }

        esqueciSenha.setOnClickListener {
            startActivity<EsqSenhaActivity>()
            finishFromChild(this)
        }

        btn_entrar.setOnClickListener {
            loginApp()
        }

    }

    private fun loginApp()
    {
        val cpf = txt_cpfLogin?.text.toString()
        val senha = txt_senhaLogin?.text.toString()

        if(cpf.isNotEmpty() && senha.isNotEmpty())
        {
            llProgressBar.visibility = View.VISIBLE
            Log.d(TAG, "Login do usuario")

            val docRef = FirebaseFirestore.getInstance().collection("Contas").document(cpf)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.data != null)
                    {
                        val nome = document.data!!["nome"].toString()
                        val email = document.data!!["email"].toString()

                        mAuth.signInWithEmailAndPassword(email, senha)
                            .addOnSuccessListener {
                                saveData(nome, cpf, senha)
                                Log.d(TAG, "Logado com sucesso")

                                updateToken(txt_cpfLogin.text.toString())
                                txt_cpfLogin.text.clear()
                                txt_senhaLogin.text.clear()
                                startActivity<SplashActivity>()
                                finishFromChild(this)

                                llProgressBar.visibility = View.GONE

                            }.addOnFailureListener {
                                llProgressBar.visibility = View.GONE
                                val snackbar = Snackbar.make(telaLogin, "O Cadastro falhou", Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }
                    }else
                    {
                        llProgressBar.visibility = View.GONE
                        val snackbar = Snackbar.make(telaLogin, "O Cadastro falhou", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }

        }else{
            llProgressBar.visibility = View.GONE
            val snackbar = Snackbar.make(telaLogin, "Entre com mais detalhes...", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

    private fun saveData(nome: String, cpfLogin: String, senhaLogin: String){

        database.use {
            insert(
                "DadosLogin",
                "nome" to nome,
                "cpf" to cpfLogin,
                "senha" to senhaLogin
            )
        }
        nomeDataBase = nome
        cpfDataBase = cpfLogin
        //senhaDataBase = senhaLogin

    }

    @Suppress("DEPRECATION")
    private fun updateToken(cpf: String){

        val token = FirebaseInstanceId.getInstance().token
        val uid = FirebaseAuth.getInstance().uid

        if( uid != null )
        {
            FirebaseFirestore.getInstance()
                .collection("Contas")
                .document(cpf)
                .update("token", token)
        }

    }

}
