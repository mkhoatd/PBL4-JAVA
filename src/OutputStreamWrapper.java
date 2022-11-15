import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapper {
    private OutputStream os;
    public OutputStreamWrapper(OutputStream os) {
        this.os=os;
    }
    public void writeUTF(String message) throws IOException {
        byte[] toSendBytes = message.getBytes();
        int toSendLen = toSendBytes.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte)(toSendLen & 0xff);
        toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
        os.write(toSendLenBytes);
        os.write(toSendBytes);
    }
}
