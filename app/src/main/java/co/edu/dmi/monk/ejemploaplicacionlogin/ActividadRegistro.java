package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class ActividadRegistro extends AppCompatActivity implements Observer {

    private static final String TAG = "ActividadRegistro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_registro);
        Comunicacion.getInstance().addObserver(this);
        initBotonRegistrarse();
        initBotonVolverAlLogin();
    }

    private void initBotonVolverAlLogin() {
        Button btnVolverAlLogin = (Button) findViewById(R.id.btn_regresar_reg_log);
        btnVolverAlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regreso = new Intent();
                setResult(0, regreso);
                finish();
            }
        });
    }

    private void initBotonRegistrarse() {
        Button btnRegistrarse = (Button) findViewById(R.id.btn_registro);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = (EditText) findViewById(R.id.ed_txt_user_registro);
                EditText contrasena = (EditText) findViewById(R.id.ed_txt_pass_registro);
                String user = usuario.getText().toString();
                String pass = contrasena.getText().toString();
                if (!user.equals("") && !pass.equals("")) {
                    Comunicacion.getInstance().enviar("signup_req:" + user + ":" + pass);
                    usuario.setHintTextColor(Color.BLACK);
                    contrasena.setHintTextColor(Color.BLACK);
                    Intent regreso = new Intent();
                    regreso.putExtra("usuario_reg", user);
                    regreso.putExtra("contrasena_reg", pass);
                    setResult(1, regreso);
                } else {
                    usuario.setHintTextColor(Color.RED);
                    contrasena.setHintTextColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        String mensaje = (String) data;
        Log.d(TAG, "registrarse: " + observable.getClass() + " // " + mensaje);
        switch (mensaje) {
            case "usuario_existe":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case "usuario_registrado":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
                break;
            default:
                break;
        }
    }
}
