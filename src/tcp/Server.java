package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {

    static List<ClientHandler> clients;
    ServerSocket serversocket;
    static int numofusers=0;
    Socket socket;
    
    public static List<ClientHandler> getclients() {
         return clients;
    }
    
    public Server(){
        
        
        clients = new ArrayList<>();
        
        
        try {
            serversocket = new ServerSocket(Constants.PORT);
        } catch (IOException ex) {
            System.out.println("Server :"+ex.getMessage());
        }
        
    }
    
    private void addClient(ClientHandler client) {
        clients.add(client);
    }
            
    private void waitingconnection (){
        System.out.println("Server running..........");
        
        while(true) {
            try {
                socket = serversocket.accept();
            } catch (IOException ex) {
               System.out.println("Waiting connection "+ex.getMessage());
            }
         System.out.println("Client accepted: "+socket.getInetAddress());
         numofusers++;
         
         ClientHandler handler = new ClientHandler(socket,"user" + numofusers);
         Thread thread = new Thread((Runnable) handler);
         addClient(handler);
         thread.start();
        }
    }
    
    public static void main(String []args){
        Server server = new Server();
        server.waitingconnection();
    } 
    
}
