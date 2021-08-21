package livrokotlin.com.farmaciaesperanca.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pedidos.*
import kotlinx.android.synthetic.main.my_dialog.*
import kotlinx.android.synthetic.main.my_dialog.view.*
import livrokotlin.com.farmaciaesperanca.R

class MyFragment : DialogFragment() {

    var edit1: EditText? = null
    var button: Button? = null

    companion object{
        var codPedido = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = activity!!.layoutInflater.inflate(R.layout.my_dialog, null)

        this.edit1 = view.edit1 as EditText
        this.button = view.alterar_endereco_alert as Button

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        this.button!!.setOnClickListener {

            if( edit1 != null ){
                FirebaseFirestore.getInstance()
                    .collection("Pedidos")
                    .document(codPedido)
                    .update("endereco", edit1!!.text.toString())
                dismiss()
            }

        }

        return alert.create()
    }

}