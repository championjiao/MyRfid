package com.jxb.myrfid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jxb on 2017-12-27.
 */

public class USBService extends Service {

    public static final String TAG = "TAG";
    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    ServerSocket serverSocket = null;
    final int SERVER_PORT = 10086;

    public static ArrayList<Socket> mList = new ArrayList<Socket>();
    //线程池
    private ExecutorService mExecutorService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, Thread.currentThread().getName() + "---->" + "  onCreate");
        mExecutorService = Executors.newFixedThreadPool(3);

        //启动监听
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mainThreadFlag = true;
                ioThreadFlag = true;
                doListen();
            }
        });
    }

    private void doListen() {
        Log.d(TAG, Thread.currentThread().getName() + "---->" + " doListen() START");
        serverSocket = null;
        try {
            Log.d(TAG, Thread.currentThread().getName() + "---->" + " doListen() new serverSocket");
            serverSocket = new ServerSocket(SERVER_PORT);

            while (mainThreadFlag) {
                Log.d(TAG, Thread.currentThread().getName() + "---->" + " doListen() listen");
                if(serverSocket != null){
                    Socket client = serverSocket.accept();
                    mList.add(client);
                    mExecutorService.execute(new IOSocket(client));
                }
            }
        } catch (IOException e1) {
            Log.v(USBService.TAG, Thread.currentThread().getName() + "---->" + "new serverSocket error");
            e1.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 关闭线程
        mainThreadFlag = false;  //虽然这样 但是accept还在等待
        ioThreadFlag = false;
        // 关闭服务器
        try {
            Log.v(TAG, Thread.currentThread().getName() + "---->"
                    + "serverSocket.close()");
            serverSocket.close();
            //强制关闭所有线程
            mExecutorService.shutdownNow();
            mList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(TAG, Thread.currentThread().getName() + "---->"
                + "**************** onDestroy****************");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, Thread.currentThread().getName() + "---->" + " onStart()");
        super.onStart(intent, startId);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "  onBind");
        return null;
    }

    public class IOSocket implements Runnable {

        private Socket client;
        private BufferedOutputStream out;
        private BufferedInputStream in;
        private Boolean ioThisFlag = true;

        IOSocket(Socket client) {
            this.client = client;
            try {
                out = new BufferedOutputStream(this.client.getOutputStream());
                in = new BufferedInputStream(this.client.getInputStream());
                Log.d(USBService.TAG, Thread.currentThread().getName() + "---->" + "a client has connected to server!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
            /* PC端发来的数据msg */
                String currCMD = "";
                while (USBService.ioThreadFlag) {
                    try {
                        if (!ioThisFlag) {
                            Log.d(USBService.TAG, Thread.currentThread().getName() + "---->" + "a client has break!");
                            break;
                        }

                        /* 接收PC发来的数据 */
                        Log.v(USBService.TAG, Thread.currentThread().getName() + "---->" + "will read......");
                        /* 读操作命令 */
                        currCMD = readCMDFromSocket(in);
                        Log.v(USBService.TAG, Thread.currentThread().getName() + "---->" + "**currCMD ==== " + currCMD);

                    /* 根据命令分别处理数据 */
                        if (currCMD.equals("1")) {
                            out.write("OK".getBytes());
                            out.flush();
                        }else if (currCMD.equals("exit")) {

                        }
                    } catch (Exception e) {
                        Log.e(USBService.TAG, Thread.currentThread().getName()
                                + "---->" + "read write error111111");
                    }
                }
                out.close();
                in.close();
            } catch (Exception e) {
                Log.e(USBService.TAG, Thread.currentThread().getName() + "---->" + "read write error222222");
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) {
                        Log.v(USBService.TAG, Thread.currentThread().getName() + "---->" + "client.close()");
                        client.close();
                    }
                    mList.remove(client);
                } catch (IOException e) {
                    Log.e(USBService.TAG, Thread.currentThread().getName() + "---->" + "read write error333333");
                    e.printStackTrace();
                }
            }
        }

        /**
         * 功能：从socket流中读取完整文件数据
         * <p>
         * InputStream in：socket输入流
         * <p>
         * byte[] filelength: 流的前4个字节存储要转送的文件的字节数
         * <p>
         * byte[] fileformat：流的前5-8字节存储要转送的文件的格式（如.apk）
         */
        public byte[] receiveFileFromSocket(InputStream in,
                                            OutputStream out, byte[] filelength, byte[] fileformat) {
            byte[] filebytes = null;// 文件数据
            try {
                in.read(filelength);// 读文件长度
                int filelen = MyUtil.bytesToInt(filelength);// 文件长度从4字节byte[]转成Int
                String strtmp = "read file length ok:" + filelen;
                out.write(strtmp.getBytes("utf-8"));
                out.flush();

                filebytes = new byte[filelen];
                int pos = 0;
                int rcvLen = 0;
                while ((rcvLen = in.read(filebytes, pos, filelen - pos)) > 0) {
                    pos += rcvLen;
                }
                Log.v(USBService.TAG, Thread.currentThread().getName()
                        + "---->" + "read file OK:file size=" + filebytes.length);
                out.write("read file ok".getBytes("utf-8"));
                out.flush();
            } catch (Exception e) {
                Log.v(USBService.TAG, Thread.currentThread().getName()
                        + "---->" + "receiveFileFromSocket error");
                e.printStackTrace();
            }
            return filebytes;
        }

        /* 读取命令 */
        public String readCMDFromSocket(InputStream in) {
            int MAX_BUFFER_BYTES = 2048;
            String msg = "";
            byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
            try {
                int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
                msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");
                tempbuffer = null;
            } catch (Exception e) {
                Log.v(USBService.TAG, Thread.currentThread().getName()
                        + "---->" + "readFromSocket error");
                ioThisFlag = false;
                e.printStackTrace();
            }

            return msg;
        }
    }
}
