package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_alterar_senha.*
import kotlinx.android.synthetic.main.activity_esq_senha.*
import livrokotlin.com.farmaciaesperanca.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class AlterarSenhaActivity : AppCompatActivity() {

    var TAG ="ForgotPasswordActivity"

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        btn_salvar_novaSenha.setOnClickListener {
            sendPasswordEmail()
        }

    }

    private fun sendPasswordEmail(){

        val email = txt_recuperarEmail.text.toString()

        if(txt_emailRecuperar_senha.text.isNotEmpty()){

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    task ->

                if (task.isSuccessful){

                    Log.d(TAG, "Email Enviado")
                    alert("Verifique a caixa de entrada do seu E-mail para alterar a senha.", "E-mail Enviado"){
                        yesButton {
                            finishFromChild(this@AlterarSenhaActivity)
                        }
                    }.show()

                }else{

                    Log.w(TAG, task.exception!!.message)
                    val snackbar = Snackbar.make(alterarSenha_Activity, "Nenhum usuario encontrado com esse E-mail", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        }else{
            val snackbar = Snackbar.make(alterarSenha_Activity, "Entre com um E-mail válido", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

}
