package com.jf.skinchange;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void httptest() {
        try {
            URL url = new URL("https://restapi.amap.com/v3/weather/weatherInfo?city="+URLEncoder.encode("成都","utf-8")
                    +"&key=6d29a580912e6f4b7ffb3b057d1f9ab2");
            System.out.println("protocol:"+url.getProtocol());
            System.out.println("host:"+url.getHost());
            int port = url.getPort()>0?url.getPort():80;
            System.out.println("port:"+port);
            System.out.println("path:"+url.getPath());
            System.out.println("query:"+url.getQuery());
            System.out.println("---------------------------------------------------------------------------\r\n");

            StringBuffer protocol = new StringBuffer();
            //请求行
            protocol.append("POST ");
            protocol.append(url.getPath()).append("?").append(url.getQuery()).append(" ");
            protocol.append("HTTP/1.1");
            protocol.append("\r\n");
            //请求头
            protocol.append("Host: ");
            protocol.append(url.getHost());
            protocol.append("\r\n");
            protocol.append("Content-Length: 60").append("\r\n");
            protocol.append("Content-Type: application/x-www-form-urlencoded").append("\r\n");

            //空行
            protocol.append("\r\n");

            //POST请求数据
            protocol.append(url.getQuery()).append("\r\n");

            System.out.println("发送的HTTP报文:\r\n"+protocol);

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(url.getHost(),port));

            OutputStream os = socket.getOutputStream();
            //os.write(new Byte(protocol.toString()));
            PrintWriter pw = new PrintWriter(os);
            pw.print(protocol);
            pw.flush();

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuffer msgBuffer = new StringBuffer();
            String readData = "";
            String readMsg = "";
            boolean nextIsData = false;
            while ((readMsg = br.readLine()) != null){
                if(nextIsData){
                    readData = readMsg;
                }else{
                    msgBuffer.append("\r\n").append(readMsg);
                }
                if(!nextIsData && "".equals(readMsg)){
                    nextIsData = true;
                }else{
                    nextIsData = false;
                }
            }
            System.out.println("接收消息:"+msgBuffer);
            System.out.println("响应数据:"+readData);

            pw.close();
            os.close();
            br.close();
            isr.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRouter(){
    }
}