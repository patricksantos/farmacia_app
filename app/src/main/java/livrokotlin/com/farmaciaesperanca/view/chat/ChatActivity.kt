package livrokotlin.com.farmaciaesperanca.view.chat

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_from_button_photo.view.btn_abrirFoto
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.*
import livrokotlin.com.farmaciaesperanca.util.*
import java.io.IOException
import java.util.*

class ChatActivity : AppCompatActivity() {

    lateinit var adapter: GroupAdapter<ViewHolder>
    lateinit var me: User
    lateinit var mSelectedUri: Uri
    lateinit var token: String
    lateinit var user: User
    lateinit var admCodigoCpf: String

    var onlineState: Boolean? = null
    var validador: String = ""
    var bitmap: Bitmap? = null

    var valor = 0

    //val adm = admCpf.toString() //toid

    companion object{
        var usuario: User? = null
        var chatAdm: Boolean? = null
    }

    override fun onPause() {
        super.onPause()
        this.setOnline(false)
    }

    override fun onResume() {
        super.onResume()
        this.setOnline(true)

        admCodigoCpf = getString(R.string.adm_codigo) //toid

        //updateToken()
        tokenFromUser()

        if( usuario != null )
            txt_name_to_user.text = usuario!!.nome
        else
            txt_name_to_user.text = "Atendente"

        if( usuario != null && usuario!!.cpf.toString() == admCodigoCpf )
            finishFromChild(this)
        /*
        user = User(
            nome = intent.getStringExtra("nome"),
            token = intent.getStringExtra("token"),
            cpf = intent.getStringExtra("cpf"),
            email = intent.getStringExtra("email"),
            endereco = intent.getStringExtra("endereco"),
            password = intent.getStringExtra("senha"),
            rg = intent.getStringExtra("rg"),
            telefone = intent.getStringExtra("telefone")
        )*/

        if( validador == "sim" )
            usuario = user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        adapter = GroupAdapter()
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        chat.layoutManager = layoutManager
        chat.adapter = adapter

        if( chatAdm == true ){
            FirebaseFirestore.getInstance().collection("Contas").document(cpfDataBase!!).get()
                .addOnSuccessListener {
                    me = it.toObject<User>(User().javaClass)!!
                    fethMessages()
                }
        }else{
            FirebaseFirestore.getInstance().collection("Contas").document(cpfDataBase!!).get()
                .addOnSuccessListener {
                    me = it.toObject<User>(User().javaClass)!!
                    fethUsuarioMessages()
                }
        }

        btn_sendChat.setOnClickListener {

            //onlineState()
            if( chatAdm == true ){
                sendMessage()
            }else{
                sendUsuarioMessage()
            }
        }

        btn_sendPhoto.setOnClickListener {

            if( !checkPermissionFromDevice() ){
                requestPermission()
            }else{
                sendPhoto()
            }

        }

        btn_info.setOnClickListener {

        }

        btn_voltarChat.setOnClickListener {
            finishFromChild(this)
        }

    }

    fun boolNotification( status: Boolean )
    {
        FirebaseFirestore.getInstance()
            .collection("Chat")
            .document("Notification")
            .update("status", status)
    }

    fun onlineState(){
        FirebaseFirestore.getInstance()
            .collection("Contas")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (doc in document)
                        if( doc.data["cpf"].toString() == admCodigoCpf )
                        {
                            onlineState = doc.data["online"] as Boolean
                            break
                        }
                }
            }
    }

    fun tokenFromUser()
    {
        if( chatAdm == true ){
            FirebaseFirestore.getInstance().collection("Contas").document(usuario!!.cpf.toString()).get()
                .addOnSuccessListener {
                    me = it.toObject<User>(User().javaClass)!!
                    token = me.token.toString()
                }
        }else{
            FirebaseFirestore
                .getInstance()
                .collection("Contas")
                .get().addOnSuccessListener { document ->
                    if (document != null) {
                        for (doc in document)
                        {
                            if( doc.data["cpf"].toString() == getString(R.string.adm_codigo) ){
                                token = doc.data["token"].toString()
                                break
                            }
                        }

                    }

            }
        }
    }

    private fun fethMessages()
    {
        if( me.cpf != null && usuario != null){

            val fromId = admCodigoCpf
            val toId = usuario!!.cpf.toString()

            FirebaseFirestore.getInstance().collection("Chat")
                .document("ADM")
                .collection(fromId)
                .document(toId)
                .collection("Mensagens")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, _ ->

                    val docChanges = querySnapshot!!.documentChanges

                    for( doc in docChanges ){
                        if( doc.type == DocumentChange.Type.ADDED ){
                            val message = doc.document.toObject(Message::class.java)
                            adapter.add(MessageItem(this, message))
                        }else{
                            valor++
                        }
                        chat.layoutManager!!.scrollToPosition(valor++)
                    }
                }
        }
    }

    private fun fethUsuarioMessages()
    {
        if( me.cpf != null ){

            val fromId = me.cpf.toString()
            val toId = admCodigoCpf

            FirebaseFirestore.getInstance().collection("Chat")
                .document(fromId)
                .collection(toId)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, _ ->

                    val docChanges = querySnapshot!!.documentChanges

                    for( doc in docChanges ){
                        if( doc.type == DocumentChange.Type.ADDED ){
                            val message = doc.document.toObject(Message::class.java)
                            adapter.add(MessageItem(this, message))
                        }else{
                            valor++
                        }
                        chat.layoutManager!!.scrollToPosition(valor++)
                    }
                }

        }
    }

    private fun sendMessage() { //Enviar Mensagem ADM

        if( usuario != null ){

            val text = et_chat.text.toString()
            et_chat.text.clear()
            val fromId = admCodigoCpf//ADM
            val toId = usuario!!.cpf.toString()
            val time = System.currentTimeMillis()

            val msg = Message(
                text = text,
                fromId = fromId,
                toId = toId,
                time = time,
                photo = null
            )

            if( msg.text!!.isNotEmpty() ){

                FirebaseFirestore.getInstance().collection("Chat")
                .document("ADM")
                .collection(fromId)
                .document(toId)
                .collection("Mensagens").add(msg)
                .addOnSuccessListener {

                    //Ver Status do usuario
                    FirebaseFirestore.getInstance().collection("Contas").document(usuario!!.cpf.toString()).get()
                        .addOnSuccessListener {
                            onlineState = it.data!!["online"] as Boolean

                            if( !onlineState!! ){
                                val notification = Notification(
                                    fromId = msg.fromId,
                                    toId = msg.toId,
                                    time = msg.time,
                                    text = msg.text,
                                    toName = me.nome,
                                    fromName = nomeDataBase
                                )

                                FirebaseFirestore.getInstance().collection("notification")
                                    .document(token)
                                    .set(notification)
                            }

                        }

                }

                FirebaseFirestore.getInstance().collection("Chat").document(toId).collection(fromId).add(msg)

            }

        }

    }

    private fun sendUsuarioMessage() { //Enviar Mensagem User

        val text = et_chat.text.toString()
        et_chat.text.clear()
        val fromId = cpfDataBase.toString()
        val toId = admCodigoCpf
        val time = System.currentTimeMillis()

        val msg = Message(
            text = text,
            fromId = fromId,
            toId = toId,
            time = time,
            photo = null
        )

        if( msg.text!!.isNotEmpty() ){

            FirebaseFirestore.getInstance().collection("Chat").document(fromId).collection(toId).add(msg)
                .addOnSuccessListener {

                    boolNotification(true)

                    val contato = Contato(
                        nome = nomeDataBase,
                        lastMessage = msg.text,
                        time = msg.time,
                        fromId = msg.fromId
                    )

                    FirebaseFirestore.getInstance()
                        .collection("Chat")
                        .document("UsuariosChat")
                        .collection("Usuarios")
                        .document(fromId)
                        .set(contato)

                    //Ver Status do ADM
                    FirebaseFirestore.getInstance()
                        .collection("Contas")
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                for (doc in document)
                                    if( doc.data["cpf"].toString() == admCodigoCpf )
                                    {
                                        onlineState = doc.data["online"] as Boolean
                                        break
                                    }

                                if( !onlineState!! ){
                                    val notification = Notification(
                                        fromId = msg.fromId,
                                        toId = msg.toId,
                                        time = msg.time,
                                        text = msg.text,
                                        toName = me.nome,
                                        fromName = nomeDataBase
                                    )

                                    FirebaseFirestore.getInstance().collection("notification")
                                        .document(token)
                                        .set(notification)
                                }

                            }
                        }

                }

            FirebaseFirestore.getInstance().collection("Chat")
                .document("ADM")
                .collection(admCodigoCpf)
                .document(fromId)
                .collection("Mensagens")
                .add(msg)

        }


    }

    private class MessageItem( context: Context ,msg: Message): Item<ViewHolder>() {

        val message = msg
        val ctx = context

        override fun getLayout(): Int {

            if( chatAdm == true )
                return if( message.fromId == ctx.getString(R.string.adm_codigo) )
                {
                    if( message.text != null )
                        R.layout.item_from_msg
                    else
                        R.layout.item_from_button_photo
                }
                else
                {
                    if( message.text != null )
                        R.layout.item_to_msg
                    else
                        R.layout.item_to_button_photo
                        //R.layout.item_to_photo
                }
            else
                return if( message.fromId == cpfDataBase )
                {
                    if( message.text != null )
                        R.layout.item_from_msg
                    else
                        R.layout.item_from_button_photo
                }
                else
                {
                    if( message.text != null )
                        R.layout.item_to_msg
                    else
                        R.layout.item_to_button_photo
                        //R.layout.item_to_photo
                }
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {

            if( message.text != null )
            {
                val txtMsg = viewHolder.itemView.findViewById<TextView>(R.id.txt_msgChat)
                txtMsg.text = message.text
            }else {
                val btn = viewHolder.itemView.findViewById<Button>(R.id.btn_abrirFoto)
                btn.btn_abrirFoto.setOnClickListener {
                    val intent = Intent(ctx, ImageActivity::class.java)
                    ImageActivity.url = message.photo.toString()
                    ctx.startActivity(intent)

                }
            }
        }
    }
    //SENDFOTO

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {// transforma a foto em um bitmap
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 0 && data != null ){

            mSelectedUri = data.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mSelectedUri)
                sendPhotoDatabase()
            }catch (e: IOException){
            }
        }

    }

    fun sendPhoto()// transforma a foto em um bitmap
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun sendPhotoDatabase(){

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/" + filename )

        if( usuario != null &&  chatAdm == true ) //ADM
        {
            et_chat.text.clear()
            val fromId = admCodigoCpf
            val toId = usuario!!.cpf.toString()
            val time = System.currentTimeMillis()

            ref.putFile(mSelectedUri).addOnSuccessListener{

                ref.downloadUrl.addOnSuccessListener {

                    val msg = Message(
                        text = null,
                        fromId = fromId,
                        toId = toId,
                        time = time,
                        photo = it.toString()
                    )

                    FirebaseFirestore.getInstance().collection("Chat")
                        .document("ADM")
                        .collection(admCodigoCpf)
                        .document(toId)
                        .collection("Mensagens")
                        .add(msg)
                        .addOnSuccessListener {

                            val contato = Contato(
                                nome = nomeDataBase,
                                lastMessage = "Arquivo de Foto...",
                                time = msg.time,
                                fromId = msg.fromId
                            )

                            FirebaseFirestore.getInstance()
                                .collection("Chat")
                                .document("Notificacao")
                                .collection(fromId)
                                .document(toId).set(contato)
                        }

                    FirebaseFirestore.getInstance().collection("Chat").document(toId).collection(fromId).add(msg)
                }
            }

        }else if( chatAdm == false ) //user
        {
            et_chat.text.clear()
            val fromId = cpfDataBase.toString()
            val toId = admCodigoCpf
            val time = System.currentTimeMillis()

            ref.putFile(mSelectedUri).addOnSuccessListener{
                ref.downloadUrl.addOnSuccessListener {

                    val msg = Message(
                        text = null,
                        fromId = fromId,
                        toId = toId,
                        time = time,
                        photo = it.toString()
                    )

                    FirebaseFirestore.getInstance().collection("Chat").document(fromId).collection(admCodigoCpf).add(msg)
                        .addOnSuccessListener {

                            val contato = Contato(
                                nome = nomeDataBase,
                                lastMessage = "Arquivo de Foto...",
                                time = msg.time,
                                fromId = msg.fromId
                            )

                            FirebaseFirestore.getInstance()
                                .collection("Chat")
                                .document("UsuariosChat")
                                .collection("Usuarios")
                                .document(fromId).set(contato)
                        }

                    FirebaseFirestore.getInstance().collection("Chat")
                        .document("ADM")
                        .collection(admCodigoCpf)
                        .document(fromId)
                        .collection("Mensagens")
                        .add(msg)
                }
            }

        }

    }

    private fun checkPermissionFromDevice(): Boolean
    {
        val writeExternalStore = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return writeExternalStore == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
    }

    @Suppress("DEPRECATION")
    private fun updateToken(){

        val tokenL = FirebaseInstanceId.getInstance().token
        val uid = FirebaseAuth.getInstance().uid

        if( uid != null )
        {
            if( chatAdm == true )
                FirebaseFirestore.getInstance()
                    .collection("Contas")
                    .document(admCpf.toString())
                    .update("token", tokenL)
            else
                FirebaseFirestore.getInstance()
                    .collection("Contas")
                    .document(cpfDataBase.toString())
                    .update("token", tokenL)
        }

    }

    private fun setOnline( bool: Boolean)
    {
        val uid = FirebaseAuth.getInstance().uid
        if( uid != null )
        {
            FirebaseFirestore.getInstance()
                .collection("Contas")
                .document(cpfDataBase.toString())
                .update("online", bool)
        }
    }

}

