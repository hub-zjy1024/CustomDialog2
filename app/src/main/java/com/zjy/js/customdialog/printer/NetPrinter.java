package com.zjy.js.customdialog.printer;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

@SuppressLint("NewApi")
public class NetPrinter {
    public Socket socket;

    public int POS_OPEN_NETPORT = 9100;// 0x238c
    public boolean isOpened = false;
    public int Net_SendTimeout = 1000;
    public int Net_ReceiveTimeout = 5 * 1000;
    public String command = ""; //打印命令字符串
    public byte[] outbytes; //传输的命令集
    private String address;
    private int port;
    PrinterCMD pcmd = new PrinterCMD();

    public NetPrinter(String address, int port) {
        this.address = address;
        this.port = port;
        socket = new Socket();
    }

    /// <summary>
    /// 网络打印机 打开网络打印机
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public boolean Open() {
        try {
            SocketAddress ipe = new InetSocketAddress(address, port);
            socket.connect(ipe);
            socket.setSoTimeout(Net_ReceiveTimeout);
            isOpened = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }

    /// <summary>
    /// 网络打印机 关闭
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public void Close() {
        try {
            if (socket != null) {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机 初始化打印机
    /// </summary>
    public void Set() {
        try {
            command = pcmd.send_SetPos();
            OutputStream stream = socket.getOutputStream();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pszString
     * @param nFontAlign 0:居左 1:居中 2:居右
     * @param nFontSize  字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽
     *                   9:四倍大小
     *                   10:五倍高 11:五倍宽 12:五倍大小
     * @param ifzhenda   0:非针打  1:针打
     */
    public void PrintText(String pszString, int nFontAlign, int nFontSize, int ifzhenda) {
        try {
            OutputStream stream = socket.getOutputStream();
            byte[] sendBytes = pszString.getBytes("GBK");
            stream.write(sendBytes);
            byte[] sendBytes1 = pszString.getBytes("gb2312");
            stream.write(sendBytes1);
            byte[] sendBytes2 = pszString.getBytes("utf-8");
            stream.write(sendBytes2);
            byte[] sendBytes3 = pszString.getBytes("Ascii");
            stream.write(sendBytes3);
            stream.flush();
            InputStream inputStream = socket.getInputStream();
            stream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printEnter() {
        try {

            command = pcmd.send_Enter();
            OutputStream stream = socket.getOutputStream();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /// <summary>
    /// 网络打印机 切割
    /// </summary>
    /// <param name="pagenum">切割时，走纸行数</param>
    public void CutPage(int pagenum) {
        try {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.send_PageGO(pagenum) + pcmd.send_Enter();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.send_CutPage() + pcmd.send_Enter();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机换行
    /// </summary>
    /// <param name="lines"></param>
    public void PrintNewLines(int lines) {
        try {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.send_FontSize(0);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
            for (int i = 0; i < lines; i++) {
                command = "" + pcmd.send_Enter();
                outbytes = command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 打开钱箱
    /// </summary>
    public void OpenQianXiang() {
        try {
            OutputStream stream = socket.getOutputStream();
            command = "" + pcmd.open_QianXiang();
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印条码
     *
     * @param numstr
     * @param height
     * @param width
     * @param numweizi  1:上方  2:下方  0:不打印数字
     * @param fontalign 0:居左 1:居中 2:居右
     * @param fontsize  字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽
     *                  9:四倍大小
     *                  10:五倍高 11:五倍宽 12:五倍大小
     */
    public void printTiaoMa(String numstr, int height, int width, int numweizi, int
            fontalign,
                            int fontsize) {
        try {
            OutputStream stream = socket.getOutputStream();
            command = pcmd.set_TiaoMaHeight(height);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.set_TiaoMaWidth(width);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.set_TiaoMaWeiZi(numweizi);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.send_TextAlign(fontalign);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.send_FontSize(fontsize);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.print_TiaoMa(numstr);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
