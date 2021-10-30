import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class TServer extends Thread {
    Socket socket;
    ServerSocket serverSocket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    public static void main(String[] args) throws Exception {
        TServer tServer = new TServer();
    }

    public TServer() throws Exception {
        serverSocket = new ServerSocket(8888);
        socket = serverSocket.accept();
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(
                new FileReader("D:\\test\\file1.txt"));
        String msg;
        while (true) {
            msg = bufferedReader.readLine();
            if (msg != null) {
                printWriter.println(msg);
            } else {
                break;
            }
        }
    }
}

class TClient {
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public static void main(String[] args) throws Exception, SocketException {
        TClient tClient = new TClient();
    }

    public TClient() throws Exception, SocketException {
        //定义域
        socket = new Socket("127.0.0.1", 3838);
        bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));//从网络流中读取信息
        bufferedWriter = new BufferedWriter(
                new FileWriter("D:\\testfile\\file2.txt"));//写入新文件中
        String msg;
        try {
            while (true) {
                msg = bufferedReader.readLine();//从网络流中读取一行
                if (msg != null) {
                    //写入新文件中
                    bufferedWriter.write(msg);
                    bufferedWriter.flush();
                    bufferedWriter.newLine();
                } else {
                    break;
                }
            }
        } catch (SocketException e) {
        }
    }
}