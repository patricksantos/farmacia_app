package livrokotlin.com.farmaciaesperanca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cadastro.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.User
import livrokotlin.com.farmaciaesperanca.util.cpfGlobal
import org.jetbrains.anko.startActivity

class CadastroActivity : AppCompatActivity() {

    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        txt_cpfCadastro.text = cpfGlobal

        addContaFirebase()
        //init()

    }

    private fun addContaFirebase()
    {
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        btn_cadastrar.setOnClickListener {

            if(txt_senhaCadastro.text.isNotEmpty() && txt_cpfCadastro.text.isNotEmpty() && txt_emailCadastro.text.isNotEmpty() && txt_enderecoCadastro.text.isNotEmpty() && txt_telefoneCadastro.text.isNotEmpty() && txt_nomeCadastro.text.isNotEmpty()){

                llProgressBar.visibility = View.VISIBLE

                val usuario = User(
                    password = txt_senhaCadastro.text.toString(),
                    email = txt_emailCadastro.text.toString(),
                    cpf = txt_cpfCadastro.text.toString(),
                    telefone = txt_telefoneCadastro.text.toString(),
                    endereco = txt_enderecoCadastro.text.toString(),
                    nome = txt_nomeCadastro.text.toString()
                )

                mAuth.createUserWithEmailAndPassword(usuario.email.toString(), usuario.password.toString()).addOnCompleteListener(this){
                        task ->

                    llProgressBar.visibility = View.GONE

                    if(task.isSuccessful){

                        Log.d(TAG, "CreateUserWithEmail:Sucess")
                        verificarEmail()

                        mFirebaseFirestore.collection("Contas").document(usuario.cpf.toString()).set(usuario)
                            .addOnSuccessListener {
                                val snackbar = Snackbar.make(cadastro, "Cadastrado com sucesso", Snackbar.LENGTH_LONG)
                                snackbar.show()
                                startActivity<LoginActivity>()
                                FirebaseAuth.getInstance().signOut()
                                finishFromChild(this)

                            }.addOnFailureListener {
                                val snackbar = Snackbar.make(cadastro, "O Cadastro falhou", Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }

                    }else{
                        Log.w(TAG, "CreateUserWithEmail:Faillure", task.exception)
                        val snackbar = Snackbar.make(cadastro, "A Autenticação falhou", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                }
            }else {
                if (txt_senhaCadastro.text.isEmpty())
                    txt_senhaCadastro.error = "Insira um valor"
            }
        }

    }

    private fun init(){

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("Contas").child("Clientes")
        mAuth = FirebaseAuth.getInstance()

        btn_cadastrar.setOnClickListener {

            val usuario = User(
                password = txt_senhaCadastro.text.toString(),
                email = txt_emailCadastro.text.toString(),
                cpf = txt_cpfCadastro.text.toString(),
                telefone = txt_telefoneCadastro.text.toString(),
                endereco = txt_enderecoCadastro.text.toString(),
                nome = txt_nomeCadastro.text.toString()
            )

            if(txt_senhaCadastro.text.isNotEmpty() && txt_cpfCadastro.text.isNotEmpty() && txt_emailCadastro.text.isNotEmpty() && txt_enderecoCadastro.text.isNotEmpty() && txt_telefoneCadastro.text.isNotEmpty() && txt_nomeCadastro.text.isNotEmpty()){

                llProgressBar.visibility = View.VISIBLE

                mAuth.createUserWithEmailAndPassword(usuario.email.toString(), usuario.password.toString()).addOnCompleteListener(this){
                    task ->

                    llProgressBar.visibility = View.GONE

                    if(task.isSuccessful){

                        Log.d(TAG, "CreateUserWithEmail:Sucess")
                        //val userId = mAuth.currentUser!!.uid // MUDAR CPF

                        verificarEmail()

                        val currentUserDb = mDatabaseReference.child(usuario.cpf.toString())
                        currentUserDb.setValue(usuario)

                        finishFromChild(this)

                        //updateUserInfoAndUi()

                    }else{

                        Log.w(TAG, "CreateUserWithEmail:Faillure", task.exception)
                        val snackbar = Snackbar.make(cadastro, "A Autenticação falhou", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }

                }
            }else {
                if (txt_senhaCadastro.text.isEmpty())
                    txt_senhaCadastro.error = "Insira um valor"
            }
        }
    }

    private fun updateUserInfoAndUi(){

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    private fun verificarEmail(){

        val mUser = mAuth.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this){
            task ->

            if(task.isSuccessful){
                val snackbar = Snackbar.make(cadastro, "Email verificado ${mUser.email}", Snackbar.LENGTH_LONG)
                snackbar.show()
            }else{
                Log.e(TAG, "SendEmailVerification", task.exception)
                val snackbar = Snackbar.make(cadastro, "Email não verificado", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }

}
