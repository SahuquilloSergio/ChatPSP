package chatservidor;

import static chatservidor.ChatServidor.bye;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ChatServidor {

    static ArrayList<Cliente> clientes = new ArrayList();

    public static void main(String[] args) {

        int port = Integer.parseInt(JOptionPane.showInputDialog("Puerto?"));

        try {
            int numeroClientes = 0;
            // Se crea el socket del servidor:
//            System.out.println("Creando socket servidor");
            ServerSocket serverSocket = new ServerSocket(port);

            // El servidor está operativo hasta que cambie el valor del boolean:
            while (true) {
                //El socket del servidor se queda escuchando en la direccion deseada.
                //En cuanto reciba una conexion se crea el objeto Socket
                
                System.out.println("Aceptando conexiones");
                Socket newSocket = serverSocket.accept();
                InputStream is = newSocket.getInputStream();
                OutputStream os = newSocket.getOutputStream();

                byte[] recibido = new byte[250];
                is.read(recibido);
                String nickname = new String(recibido);

                // Se crea un Thread:
                Cliente cliente = new Cliente(os, is, nickname);
                cliente.start();
                clientes.add(cliente);
                System.out.println("Nuevo cliente conectado (nickname:" + nickname + ",ip y puerto:" + newSocket.getRemoteSocketAddress() + ").");
                System.out.println("Hay " + clientes.size() + " clientes conectados.");

                clientes.forEach((elemento) -> {
                    elemento.enviarMensaje("Servidor: " + nickname + " se ha unido.");
                });

            }
        } catch (IOException ex) {
            System.out.println("Error al recibir conexiones");
        }
    }

    /**
     * Operación que suma los dos números recibidos.
     *
     */
    public static void bye() {
        // Cliente se va a tomar por saco
    }

}

/**
 * Hilo para cada cliente del servidor.
 */
class Cliente extends Thread {

    String nickname;
    InputStream is;
    OutputStream os;

    /**
     * Recibimos el socket de conexión con el cliente y abrimos las conexiones
     * de entrada y salida.
     *
     * @param socket socket de conexión con el cliente
     * @throws IOException
     */
    public Cliente(OutputStream os, InputStream is, String nickname){
       this.os=os;
       this.is=is;
       this.nickname=nickname;
    }

    @Override
    public void run() {
        try {
            // Se lee el mensaje recibido:
            byte[] mensajeRecibido = new byte[120];
            is.read(mensajeRecibido);
            System.out.println(new String(mensajeRecibido));
            // Se forma un array de strings para manejar los datos individualmente:
            String mensaje = new String(mensajeRecibido);
            // Se trata el resultado y se envía al cliente:
            ChatServidor.clientes.forEach((elemento) ->{
               elemento.enviarMensaje(nickname+": "+mensaje);
            });
            System.out.println(nickname+": "+mensaje);
            
//            String mensajeEnviado = cadena[0] + ": " + cadena[1];
//            os.write(mensajeEnviado.getBytes());

//            System.out.println(mensajeEnviado);
            System.out.println("Mensaje enviado");
        } catch (IOException ex) {
            System.out.println("Error de conexión");
        }

    }

    public void enviarMensaje(String mensaje) {
        try {
            os.write(mensaje.getBytes());
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje.");
            try {
                os.close();
            } catch (IOException ex1) {
                System.out.println("Error. Envío de mensajes deshabilitado.");
            }
        }
    }
}
