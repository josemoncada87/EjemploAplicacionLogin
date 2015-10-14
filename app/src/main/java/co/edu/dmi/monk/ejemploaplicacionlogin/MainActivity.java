package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Comunicacion.getInstance().addObserver(this);
        initIU();
    }

    private void initIU() {
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = (EditText) findViewById(R.id.ed_txt_user);
                EditText contrasena = (EditText) findViewById(R.id.ed_txt_pass);
                Comunicacion.getInstance().enviar("Login:"+usuario+":"+contrasena);
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println("principal: " + observable.getClass() + " // "+((String)data));
    }
}
