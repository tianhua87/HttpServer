package cn.szu.httpserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	private static int countOfSocket=0;
	
	public static void main(String[] args) {
		
		try {
			ServerSocket server=new ServerSocket(80);
			Socket s=null;
			while(true)
			{
				System.out.println("正在监听80窗口");
				s=server.accept();
				countOfSocket++;
				System.out.println("第 "+countOfSocket+" 个Socket");
				
				new SocketThread(s).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

class SocketThread extends Thread
{
	private Socket socket;
	
	private static String HEADER="HTTP/1.1 200 OK\r\n"
			+ "Date:Tue, 29 Mar 2016 10:49:59 GMT\r\n"
			//+ "Content-Type:text/html; charset=utf-8\r\n"
			+ "Connection:Keep-Alive\r\n"
			+ "\r\n";

	
	public SocketThread(Socket socket)
	{
		this.socket=socket;
	}
	
	public void run()
	{
		
		
		
		byte b[]=new byte[1024];
		int len=0;
		try {
			
			InputStream in=socket.getInputStream();
			InputStreamReader isr=new InputStreamReader(in);
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			
			//读取头部信息的第一行
			line=br.readLine();
			String path = null;
			boolean isFirstRequest=true;
			if(line!=null)
			{
				path=line.split(" ")[1];
				if(!path.equals("/"))
					isFirstRequest=false;
			}
			System.out.println(line);
			while(((line=br.readLine())!=null)&&(line.length()!=0))
			{
				System.out.println(line);
			}
			
			OutputStream out=socket.getOutputStream();
			FileInputStream fis=null;
			if(isFirstRequest==true)
				fis=new FileInputStream("f:/HttpServer/wenjibin.html");
			else
			{
				fis=new FileInputStream("F:/HttpServer/images"+path);
			}
			
			out.write(HEADER.getBytes());
			
			while((len=fis.read(b, 0, 1024))!=-1)
			{
				out.write(b, 0, len);
			}
			if(socket!=null)socket.close();
			if(fis!=null)fis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
