package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_esq_senha.*
import livrokotlin.com.farmaciaesperanca.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class EsqSenhaActivity : AppCompatActivity() {

    var TAG ="ForgotPasswordActivity"

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esq_senha)

        init()

    }

    private fun init(){

        mAuth = FirebaseAuth.getInstance()

        btn_recuperar.setOnClickListener {
            sendPasswordEmail()
        }

    }

    private fun sendPasswordEmail(){

        val email = txt_recuperarEmail.text.toString()

        if(txt_recuperarEmail.text.isNotEmpty()){

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task ->

                if (task.isSuccessful){

                    Log.d(TAG, "Email Enviado")
                    alert("Verifique a caixa de entrada do seu E-mail para alterar a senha.", "E-mail Enviado"){
                         yesButton {
                             finishFromChild(this@EsqSenhaActivity)
                         }
                    }.show()

                }else{

                    Log.w(TAG, task.exception!!.message)
                    toast("Nenhum usuario encontrado com esse E-mail")

                }
            }
        }else{

            toast("Entre com um E-mail válido")

        }

    }
}
