package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Created by 1130613425 on 14/10/2015.
 */
public class Comunicacion extends Observable implements Runnable {

    private static final String TAG = "COM-ANDROID";
    private static Comunicacion ref;
    private Socket s;
    private boolean corriendo;

    public Comunicacion() {
        s = null;
        corriendo = true;
        Log.d(TAG, "Comunicacion con el servidor instanciada");



        System.out.println("---------------------------------------------------------------------");
    }

    public static Comunicacion getInstance() {
        if(ref == null){
            ref =  new Comunicacion();
            Thread t =  new Thread(ref);
            t.start();
        }
        return ref;
    }


    @Override
    public void run() {
        Log.d(TAG, "Hilo de comunicacion iniciado");
        while (corriendo) {
            try {
                if (s == null) {
                   conectar();
                } else {
                    recibir();
                }
                Thread.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
                s = null;
                corriendo = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void conectar() throws IOException {
        InetAddress dirServidor = InetAddress.getByName("10.0.2.2");
        s = new Socket(dirServidor, 5000);
        Log.d(TAG, "Conectado con: " + s.toString());
    }

    private void recibir() throws IOException {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        String recibido = dis.readUTF();
        if(recibido.equals("Login OK")){
            setChanged();
            notifyObservers("Login OK");
            clearChanged();
        }
    }


    public void enviar(String msn) {
        if(s != null) {
            try {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(msn);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            setChanged();
            notifyObservers("No conectado");
            clearChanged();
        }
    }
}
