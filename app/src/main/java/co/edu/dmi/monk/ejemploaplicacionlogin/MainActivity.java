package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private static final int REGISTRO_REQ_ACT = 0;
    private static final String TAG = "MainActivity";
    private String mensajePantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Comunicacion.getInstance().addObserver(this);
        mensajePantalla = "...";
        initBotonLogin();
        initBotonIrARegistrarse();
    }

    private void initBotonIrARegistrarse() {
        TextView txtLogin = (TextView) findViewById(R.id.tv_txt_registrarse);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActividadRegistro.class);
                startActivityForResult(i, REGISTRO_REQ_ACT);
            }
        });
    }

    private void initBotonLogin() {
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = (EditText) findViewById(R.id.ed_txt_user);
                EditText contrasena = (EditText) findViewById(R.id.ed_txt_pass);
                String user = usuario.getText().toString();
                String pass = contrasena.getText().toString();
                if (!user.equals("") && !pass.equals("")) {
                    Comunicacion.getInstance().enviar("login_req:" + user + ":" + pass);
                    Log.d(TAG, "Se envió: login_req:" + user + ":" + pass);
                    usuario.setHintTextColor(Color.BLACK);
                    contrasena.setHintTextColor(Color.BLACK);
                } else {
                    usuario.setHintTextColor(Color.RED);
                    contrasena.setHintTextColor(Color.RED);
                    usuario.setHighlightColor(Color.RED);
                    contrasena.setHighlightColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        String mensaje = (String) data;
        Log.d(TAG, "principal: " + observable.getClass() + " // " + mensaje);
        switch (mensaje) {
            case "usuario_no_existe":
                mostrarMensajeToast("El usuario no está registrado");
                break;
            case "login_ok":
                mostrarMensajeToast("Bienvenido");
                Intent i = new Intent(getApplicationContext(), ActividadMenuPrincipal.class);
                EditText usuario = (EditText) findViewById(R.id.ed_txt_user);
                String user = usuario.getText().toString();
                i.putExtra("usuario_log", user);
                startActivity(i);
                break;
            case "login_no_ok":
                mostrarMensajeToast("Contraseña incorrecta");
                break;
            default:
                break;
        }
    }

    private void mostrarMensajeToast(String mensajeAMostrar) {
        mensajePantalla = mensajeAMostrar;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), mensajePantalla, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == 1) {
                Bundle bodega = data.getExtras();
                String usuarioReg = bodega.getString("usuario_reg");
                String contrasenaReg = bodega.getString("contrasena_reg");
                EditText usuario = (EditText) findViewById(R.id.ed_txt_user);
                EditText contrasena = (EditText) findViewById(R.id.ed_txt_pass);
                usuario.setText(usuarioReg);
                contrasena.setText(contrasenaReg);
                Log.d("" + this, "Resultado OK");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
