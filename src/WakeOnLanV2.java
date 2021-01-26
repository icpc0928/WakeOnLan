import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WakeOnLanV2 {
    public static final int PORT = 9;
    public static void main(String[] args) {
        args = new String[2];
        args[0] = "60.249.22.240";
        args[0] = "182.155.80.38";
        args[1] = "4C:ED:FB:69:15:45";
        args[1] = "04:D4:C4:54:83:0A";
        if (args.length != 2) {
            System.out.println("Usage: java WakeOnLan <broadcast-ip> <mac-address>");
            System.out.println("Example: java WakeOnLan 192.168.0.255 00:0D:61:08:22:4A");
            System.out.println("Example: java WakeOnLan 192.168.0.255 00-0D-61-08-22-4A");
            System.exit(1);
        }

        String ipStr = args[0];
        String macStr = args[1];

        try {
            byte[] macBytes = getMacBytes(macStr);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            System.out.println("Wake-on-LAN packet sent.");
        }
        catch (Exception e) {
            System.out.println("Failed to send Wake-on-LAN packet: + e" + e.toString());
            System.exit(1);
        }

    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
