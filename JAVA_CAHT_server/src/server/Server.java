package server;
import java.rmi.*;
import java.rmi.registry.*;

public class Server{

	public static void main(String[] args)
	{
		new Server();
	}

	public Server()
	{	
		try{
			ChatServerImpl obj=new ChatServerImpl();
			Registry reg =LocateRegistry.createRegistry(11000);
			reg.rebind("ChatService",obj);
		}catch(RemoteException ex){
			ex.printStackTrace();
		}

			
	}
}