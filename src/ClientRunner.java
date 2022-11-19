import java.io.DataOutputStream;
import java.net.Socket;

public class ClientRunner extends Thread{
    public client client;
    public ClientRunner(client client) {
        this.client= client;
    }
    public void run() {
        while(true) {
            var dos=client.dos;
            var dis= client.dis;
            try {
                var newText=dis.readUTF();
                client.processTable(newText);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
