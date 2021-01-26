import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WakeOnLan {

    //main方法 發送UDP廣播，實現遠程開機，需要目標電腦的MAC地址

    public static void main(String[] args){

        //廣播ip位址
        String ip = "60.249.22.239";
        String macAddress = "4CEDFB691545";
        //port號
        int port = 9;

        //魔術封包數據
        String magicPackage = "0xFFFFFFFFFFFF";
        for(int i = 0; i < 12 ;i++ ){
            magicPackage += macAddress;
        }

//        System.out.println(magicPackage);
        //轉為二進位
        byte[] command = hexToBinary(magicPackage);
        //廣播魔術封包
        try{
            //1.獲取ip地址
            InetAddress address = InetAddress.getByName(ip);
            //2.獲取廣播socket
            MulticastSocket socket = new MulticastSocket(port);
            //3.封裝數據包
            /*public DatagramPacket(byte[] buf,int length
            * ,InetAddress address
            * ,int port)
            * buf：緩存的命令
            * length：每次發送的數據字節數，該值必須小於等於buf的大小
            * address：廣播地址
            * port：廣播埠 */
            DatagramPacket packet = new DatagramPacket(command, command.length, address, port);
            //4.發送數據
            socket.send(packet);
            //5.關閉socket
            socket.close();
        }catch (UnknownHostException e){
            //ip地址錯誤時拋出
            System.out.println("host error");
            e.printStackTrace();
        }catch (IOException e) {
            //獲取socket失敗時拋出
            System.out.println("socket error");
            e.printStackTrace();
        }
    }

    //將 16進位字串轉為byte陣列表示的二進位
    // param hexString : 16進位字串
    // return byte表示的16進位數
    private static byte[] hexToBinary(String hexString){
        //1. 定義變量 : 用於存儲轉換結果的陣列
        byte[] result = new byte[hexString.length()];
        //2. 去除字串中的16進位標籤"0x" 並將所以字母轉換為大寫
        hexString = hexString.toUpperCase().replace("0X", "");
        //3. 開始轉換
        //3.1 定義兩個臨時的變數
        char temp1 = '0';
        char temp2 = '0';
        //3.2 開始轉換 將每個16進位的數放進一個byte變數中
        for(int i = 0; i < hexString.length(); i += 2){
            result[i / 2] = (byte)((hexToDec(temp1) << 4) | (hexToDec(temp2)));
            System.out.println((byte)((hexToDec(temp1) << 4) | (hexToDec(temp2))));
        }
        System.out.println(Arrays.toString(result));
        return result;
    }

    //用於將16進位的單個字符映射到10進位的方法
    private static byte hexToDec(char c ){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

}



