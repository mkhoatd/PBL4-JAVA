import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper {
    private InputStream is;
    public InputStreamWrapper(InputStream is) {
        this.is=is;
    }
    public String readUTF() throws IOException {
        byte[] lenBytes = new byte[4];
        is.read(lenBytes, 0, 4);
        int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
        byte[] receivedBytes = new byte[len];
        is.read(receivedBytes, 0, len);

        return new String(receivedBytes, 0, len);
    }
}
