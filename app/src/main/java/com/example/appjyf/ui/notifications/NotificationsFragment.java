package com.example.appjyf.ui.notifications;

import android.app.AlertDialog;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Adapter.PedidosAdapter;
import com.example.appjyf.Adapter.ProductosAdapter;
import com.example.appjyf.BorrarFragment;
import com.example.appjyf.DetailsProdFragment;
import com.example.appjyf.Mail.JavaMailAPI;
import com.example.appjyf.Mail.Utils;
import com.example.appjyf.Modelo.Pedidos;
import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.Modelo.Usuarios;
import com.example.appjyf.Modelo.VolleySingleton;
import com.example.appjyf.R;

import com.example.appjyf.SQLite.DBContract;
import com.example.appjyf.SQLite.DBPedidos;
import com.example.appjyf.SQLite.DBProductos;
import com.example.appjyf.SQLite.DBUsuarios;
import com.example.appjyf.SQLite.Database;
import com.example.appjyf.ui.home.HomeFragment;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotificationsFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{



    RecyclerView recyclerPed;
    String para, asunto;
    static ArrayList<Pedidos> listPed;
    //ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest objectRequest;
    PedidosAdapter adapter;
    public Button send;
    String host ="smtp.gmail.com";
    String email = "test.app030295@gmail.com";
    String pass="Uno2Tres4.";
    String from = "test.app030295@gmail.com";
    StringRequest stringRequest;
    String filePath = "/sdcard/download/";
    TextView desc;
    String nombre, apellido, empresa, correo, fono, comuna;
    String idPED, cant,det, total, unidad;
    static int num = 0;
    File file;
    EditText nota, testmail;
//    private ProgressDialog mProgressDialog;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        listPed = new ArrayList<>();
        recyclerPed = root.findViewById(R.id.RviewPedido);
        recyclerPed.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerPed.setHasFixedSize(true);
        send = root.findViewById(R.id.btnSend);
        desc = root.findViewById(R.id.txtDescripcionPedido);
        nota = root.findViewById(R.id.edtNota);
        testmail = root.findViewById(R.id.edtTestMail);


       // para = "riveros.bryan@gmail.com";
        asunto = "Pedido de "+nombre+" "+apellido;

        para = testmail.getText().toString();

        tablaUsuariovacia();
        tablaPedidovacia();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testmail.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Ingrese un Correo de destino", Toast.LENGTH_SHORT).show();
                }else{

                try {
                    CrearPDF2();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR: "+e, Toast.LENGTH_SHORT).show();
                }
                esperar(3);
                SendMail();
                esperar(3);
                DeletePedidoyUsuario();
            }}
        });

        DBPedidos dbPedidos = new DBPedidos(getContext());

        adapter = new PedidosAdapter(dbPedidos.mostrarPedidos(), getContext());
        recyclerPed.setAdapter(adapter);
        readPedidos(getContext());


        request = Volley.newRequestQueue(getContext());
        //MostrarPedidos();


      //  PedidosAdapter adapter= new PedidosAdapter(listPed, getContext());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //aqui se envia el dato hacia BorrarFragment
                Bundle bundle = new Bundle();
                bundle.putString("keyped", listPed.get(recyclerPed.getChildAdapterPosition(v)).getId_prod().toString());

                  /*  bundle.putString("key2", listProd.get(recyclerProd.getChildAdapterPosition(v)).getPrecio_prod().toString());
                    bundle.putString("key3", listProd.get(recyclerProd.getChildAdapterPosition(v)).getDesc_prod());*/
                BorrarFragment fragment = new BorrarFragment();
                fragment.setArguments(bundle);
                fragment.show(getChildFragmentManager(),"MyFragment");
            }
        });
        recyclerPed.setAdapter(adapter);

        return root;
    }


    public static void esperar(int segundos){
        try {
            Thread.sleep(segundos * 1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void tablaUsuariovacia(){
        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getWritableDatabase();
        String count = "SELECT count(*) FROM "+DBContract.TABLE_USER;

        Cursor mcursor = dbLite.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0) {
            mostrarDatoUsuario();
            send.setEnabled(true);
        }else{
            Toast.makeText(getContext(), "¡Antes de enviar tu pedido, completa el formulario con tus datos!", Toast.LENGTH_LONG).show();
        }}

    public void tablaPedidovacia(){
        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getWritableDatabase();
        String count = "SELECT count(*) FROM "+DBContract.TABLE_PEDIDOS;

        Cursor mcursor = dbLite.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0) {
            //mostrarpedidos();
        }else{
           // Toast.makeText(getContext(), "¡Antes de enviar tu pedido, completa tus datos!", Toast.LENGTH_SHORT).show();
        }}



    private void SendMail() {
            //-------------------------------------------------------------------------------
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    //onPreExecute
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Toast.makeText(getContext(), "Enviando...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //doInBackground

                    //Creating properties
                    Properties props = new Properties();

                    //Configuring properties for gmail
                    //If you are not using gmail you may need to change the values
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable","true");
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.socketFactory.port", "587");

                    //Creating a new session
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {

                                //Authenticating the password
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(email, pass);
                                }
                            });
                    session.setDebug(true);

                    try {

                        //llamar a los valores y convertirlos a String

                        //String To = "riveros.bryan@gmail.com";
                        String To = testmail.getText().toString();
                        //String message = sms.getText().toString();
                        String Asunto = "Pedido de "+nombre+" "+apellido;


                        //Creating MimeMessage object
                        MimeMessage mm = new MimeMessage(session);

                        //Setting sender address
                        mm.setFrom(new InternetAddress(from));
                        //Adding receiver
                        mm.addRecipient(Message.RecipientType.TO,new InternetAddress(To));
                        //Adding subject
                        mm.setSubject(Asunto);

                        //mm.setText(message);


                       BodyPart messageBodyPart = new MimeBodyPart();

                       messageBodyPart.setText("Adjunto pedido de Don: "+nombre+" "+apellido);

                       Multipart multipart = new MimeMultipart();

                       multipart.addBodyPart(messageBodyPart);

                       messageBodyPart = new MimeBodyPart();

                       DataSource source = new FileDataSource(filePath+nombre+"_"+apellido+".pdf");

                       messageBodyPart.setDataHandler(new DataHandler(source));

                        messageBodyPart.setFileName(filePath+nombre+"_"+apellido+".pdf");

                        multipart.addBodyPart(messageBodyPart);

                        mm.setContent(multipart);

                        //Sending email
                        Transport.send(mm);

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }


                    //doPostExecute
                 /*   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Dismiss progress dialog when message successfully send
                            //mProgressDialog.dismiss();

                            //Show success toast
                            Toast.makeText(getContext(),"Pedidos Enviados",Toast.LENGTH_SHORT).show();

                        }
                    });*/
                }
            });
            //-------------------------------------------------------------------------------

    }

    private void DeletePedidoyUsuario() {
        DBPedidos pedidos = new DBPedidos(getContext());
        DBUsuarios usu = new DBUsuarios(getContext());
        deletePDF();
        usu.EliminarUsuario();
        pedidos.EliminarTodo();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack();
        navController.navigate(R.id.navigation_notifications);
    }


    /*private void CreatePDF() throws FileNotFoundException {
    //    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),nombre+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument= new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Hola Mundo");

        Text text1 = new Text("Bold").setBold();
        Text text2 = new Text("Italic").setItalic();
        Text text3 = new Text("Underline").setUnderline();

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add(text1).add(text2).add(text3);

        document.add(paragraph);
        document.add(paragraph1);
        List list = new List();
        list.add("Android");
        list.add("Java");
        list.add("C++");
        list.add("Php");

        document.add(list);
        document.close();
            Toast.makeText(getContext(), "PDF Createddd", Toast.LENGTH_SHORT).show();



    }*/


    private void CrearPDF2() throws FileNotFoundException{
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        file = new File(pdfPath,nombre+"_"+apellido+".pdf");

        OutputStream stream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        DeviceRgb verde = new DeviceRgb(51,240,51);
        DeviceRgb gris = new DeviceRgb(220,220,220);

        float columnWidht[] = {140,140,140,140};
        Table table1 = new Table(columnWidht);

        //table1----01
        table1.addCell(new Cell(3,1).add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell(1,5).add(new Paragraph("Pedidos").setFontSize(30f).setFontColor(verde)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        //table1----02
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));


        //table1----04
        table1.addCell(new Cell().add(new Paragraph("Nombre: "+nombre+" "+apellido)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Fono: "+fono)).setBorder(Border.NO_BORDER));

        //table1----05
        table1.addCell(new Cell().add(new Paragraph("Empresa: "+empresa)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Correo: "+correo)).setBorder(Border.NO_BORDER));

        //table1----06
        table1.addCell(new Cell().add(new Paragraph("Comuna: "+comuna)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        float columnWidth2[]={62,162,112,112,112};
        Table table2 = new Table(columnWidth2);

        table2.addCell(new Cell().add(new Paragraph("ID").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("Descripción").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("Cantidad").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("Detalle").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));
        table2.addCell(new Cell().add(new Paragraph("Total").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GREEN));


        float columnWidth3[]={32,200};
        Table table3 = new Table(columnWidth3);

        table3.addCell(new Cell().add(new Paragraph("Nota:").setBold()).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph(nota.getText().toString())).setBorder(Border.NO_BORDER));

        document.add(table1);
        document.add(new Paragraph("\n"));
        document.add(tablePedidos(table2));
        document.add(table3);

        document.close();
        //Toast.makeText(getContext(), "Creando PDF...", Toast.LENGTH_LONG).show();

    }

    private void deletePDF(){
        if (file.exists()){
            file.delete();
            Toast.makeText(getContext(), "pdf "+nombre+" eliminado", Toast.LENGTH_SHORT).show();
        }
    }


    private Table tablePedidos(Table table2) {

        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getReadableDatabase();
        Cursor c = dbLite.rawQuery("SELECT * FROM " + DBContract.TABLE_PEDIDOS, null);

        if (c != null) {
            c.moveToFirst();
            do {

                idPED = c.getString(c.getColumnIndex(DBContract.pedidos.ID_PED));
                cant = c.getString(c.getColumnIndex(DBContract.pedidos.CANT_PED));
                total = c.getString(c.getColumnIndex(DBContract.pedidos.TOTAL_PED));
                unidad = c.getString(c.getColumnIndex(DBContract.pedidos.NOTA_PED));

                det_Producto(Integer.parseInt(c.getString(c.getColumnIndex(DBContract.pedidos.ID_PROD))));
                table2.addCell(new Cell().add(new Paragraph(idPED).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GRAY));
                table2.addCell(new Cell().add(new Paragraph(det).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GRAY));
                table2.addCell(new Cell().add(new Paragraph(cant).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GRAY));
                table2.addCell(new Cell().add(new Paragraph(unidad).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GRAY));
                table2.addCell(new Cell().add(new Paragraph("$"+total).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.GRAY));

            } while (c.moveToNext());
            c.close();
            dbLite.close();
        }
         return table2;
    }
    public void det_Producto(Integer id){
        Database database = new Database(getContext());
        SQLiteDatabase db = database.getReadableDatabase();
        String[] parametro = {id.toString()};
        String[] campo = {DBContract.productos.DESCRIPCION, DBContract.productos.PRECIO};

        Cursor cursor = db.query(DBContract.TABLE_PRODUCTOS,campo,DBContract.productos.ID+"=? ",parametro,null,null,null);
        if (cursor.moveToFirst()) {
            // cursor.moveToPosition(3);
            //desc.setText(cursor.getString(0));
            det = cursor.getString(0);
            //unidad = cursor.getString(1);

        }else{
            Toast.makeText(getContext(), "Error en det_Producto", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }




  /*  public void mostrarpedidos(){
        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getReadableDatabase();

        Cursor c = dbLite.rawQuery("SELECT * FROM "+DBContract.TABLE_PEDIDOS, null);
        if (c != null) {
            c.moveToFirst();
            do {
                idPED = c.getString(c.getColumnIndex(DBContract.pedidos.ID_PED));

            } while (c.moveToNext());
        }

        //Cerramos el cursor y la conexion con la base de datos
        c.close();
        dbLite.close();
    }*/

    private void BorrarTodo() {
        String url = "https://jyfindustrial.cl/php_crud/BorrarTodoPedido.php";

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("Eliminado")){
                  /*etiNombre.setText("");
                    txtDocumento.setText("");*/

                }else{
                    if (response.trim().equalsIgnoreCase("No Existe")){
                       /* Toast.makeText(getContext(),"No se encuentra el pedido",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);*/
                    }else{
                       /* Toast.makeText(getContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);*/

                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Se ha perdido la conexión",Toast.LENGTH_SHORT).show();
                // pDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }


    private void MostrarPedidos() {
       /* progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();*/
        String url = "https://jyfindustrial.cl/php_crud/Lista_Pedidos.php";
        objectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(objectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Error: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Pedidos pedidos = null;

        JSONArray json=response.optJSONArray("pedidos");
        try {
            for (int i=0; i<json.length();i++){
                pedidos = new Pedidos();
                JSONObject object=null;
                object=json.getJSONObject(i);

                pedidos.setId_ped(object.optInt("id_ped"));
                pedidos.setCant_ped(object.optInt("cant_ped"));
                pedidos.setTotal_ped(object.optInt("total_ped"));
                //pedidos.setProd(object.optInt("id_prod"));
                pedidos.setDesc_prod(object.optString("desc_prod"));
              //  pedidos.setDato_ped(object.optString("img_ped"));
                listPed.add(pedidos);
            }
            // progress.hide();
            PedidosAdapter adapter= new PedidosAdapter(listPed, getContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //aqui se envia el dato hacia BorrarFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("keyped", listPed.get(recyclerPed.getChildAdapterPosition(v)).getId_ped().toString());
                  /*  bundle.putString("key2", listProd.get(recyclerProd.getChildAdapterPosition(v)).getPrecio_prod().toString());
                    bundle.putString("key3", listProd.get(recyclerProd.getChildAdapterPosition(v)).getDesc_prod());*/
                    BorrarFragment fragment = new BorrarFragment();
                    fragment.setArguments(bundle);
                    fragment.show(getChildFragmentManager(),"MyFragment");





                   // Toast.makeText(getContext(), "Selección: "+listPed.get(recyclerPed.getChildAdapterPosition(v)).getId_ped(), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerPed.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el Servidor", Toast.LENGTH_SHORT).show();
            //    progress.hide();
        }
    }


    public static void readPedidos(Context context){
        listPed.clear();
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = database.readPedidos(db);
        while (c.moveToNext()){
            listPed.add(new Pedidos(c.getInt(c.getColumnIndex(DBContract.pedidos.ID_PED)),
                    c.getInt(c.getColumnIndex(DBContract.pedidos.CANT_PED)),
                    c.getInt(c.getColumnIndex(DBContract.pedidos.TOTAL_PED)),
                    c.getInt(c.getColumnIndex(DBContract.pedidos.ID_PROD)),
                    c.getString(c.getColumnIndex(DBContract.pedidos.NOTA_PED))));
        }
        PedidosAdapter adapter= new PedidosAdapter(listPed, context);
        adapter.notifyDataSetChanged();
        c.close();
        database.close();
    }

    public static void ConsultarProducto(Integer id, TextView desc, TextView unit, Context context){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] parametro = {id.toString()};
        String[] campo = {DBContract.productos.DESCRIPCION, DBContract.productos.PRECIO};

            Cursor cursor = db.query(DBContract.TABLE_PRODUCTOS,campo,DBContract.productos.ID+"=? ",parametro,null,null,null);
           if (cursor.moveToFirst()) {
              // cursor.moveToPosition(3);
               desc.setText(cursor.getString(0));
               unit.setText("$"+cursor.getString(1));

           }else{
               Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
           }
        cursor.close();
    }


    public void mostrarDatoUsuario(){
        int ID = 1;
        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getReadableDatabase();

        Cursor c = dbLite.rawQuery("SELECT * FROM "+DBContract.TABLE_USER+" WHERE "+DBContract.usuarios.ID_USU+"="+ID, null);
        if (c != null) {
            c.moveToFirst();
            do {
                nombre = c.getString(c.getColumnIndex(DBContract.usuarios.NOM_USU));
                apellido = c.getString(c.getColumnIndex(DBContract.usuarios.APE_USU));
                empresa = c.getString(c.getColumnIndex(DBContract.usuarios.EMP_USU));
                correo = c.getString(c.getColumnIndex(DBContract.usuarios.CORREO_USU));
                fono = c.getString(c.getColumnIndex(DBContract.usuarios.FONO_USU));
                comuna = c.getString(c.getColumnIndex(DBContract.usuarios.COM_USU));
            //    nom.setText(c.getString(1));
            //    ape.setText(c.getString(2));
               // Toast.makeText(getContext(), ""+nombre, Toast.LENGTH_SHORT).show();

            } while (c.moveToNext());
        }

        //Cerramos el cursor y la conexion con la base de datos
        c.close();
        dbLite.close();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    }
