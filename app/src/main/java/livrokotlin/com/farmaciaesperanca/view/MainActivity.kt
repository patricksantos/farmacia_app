package livrokotlin.com.farmaciaesperanca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.util.*
import org.jetbrains.anko.startActivity

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    private fun init(){
        btn_proximo.setOnClickListener {

            cpfGlobal = txt_cpfTelaInicial.text
            verCpf()

        }

        btn_entrarVisitante.setOnClickListener {

            startActivity<VisitanteActivity>()

        }

    }

    private fun verCpf(){

        llProgressBar.visibility = View.VISIBLE

        val docRef = FirebaseFirestore.getInstance().collection("Contas").document(txt_cpfTelaInicial.text.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    llProgressBar.visibility = View.GONE
                    startActivity<LoginActivity>()
                    finishFromChild(this)
                }else {
                    llProgressBar.visibility = View.GONE
                    startActivity<CadastroActivity>()
                    finishFromChild(this)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

}