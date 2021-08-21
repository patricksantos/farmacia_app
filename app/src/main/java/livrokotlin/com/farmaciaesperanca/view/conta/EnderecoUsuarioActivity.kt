package livrokotlin.com.farmaciaesperanca.view.conta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_endereco_usuario.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.util.cpfDataBase

class EnderecoUsuarioActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onResume() {
        super.onResume()
        endereco()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endereco_usuario)

        btn_alterar_endereco.setOnClickListener {

            if( txt_novo_endereco.text.isNotEmpty() ){
                val user = FirebaseFirestore.getInstance().collection("Contas").document(cpfDataBase.toString())
                user.update("endereco", txt_novo_endereco.text.toString())
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                        finishFromChild(this)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                        val snackbar = Snackbar.make(enderecoUser, "Tente novamente", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
            }else{
                val snackbar = Snackbar.make(enderecoUser, "Insira um valor", Snackbar.LENGTH_LONG)
                snackbar.show()
            }

        }

    }

    private fun endereco(){

        val docRef = FirebaseFirestore.getInstance().collection("Contas").document(cpfDataBase.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    meu_endereco.text = document.data!!["endereco"].toString()

                }else {
                    val snackbar = Snackbar.make(enderecoUser, cpfDataBase.toString(), Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failed to read value.", exception)
            }

    }

}
