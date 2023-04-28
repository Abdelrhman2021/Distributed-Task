package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;




public class ClientHandler implements Runnable{
    final Socket socket;
    final Scanner scan;
    String name;
    Boolean isLoggedIn;
   
    private DataInputStream input;
    private DataOutputStream output;

  public ClientHandler (Socket socket,String name){
         this.socket=socket;
         scan= new Scanner (System.in);
         this.name = name;
         isLoggedIn = true;
    try {
        input = new DataInputStream(socket.getInputStream());
        output= new DataOutputStream(socket.getOutputStream());
    }catch (IOException ex) {
        System.out.println("ClientHandler :"+ ex.getMessage());
    } 
     
  }

    
  private void closesocket (){
    try {
      socket.close();
    }catch (IOException ex) {
        System.out.println("Closesocket :"+ ex.getMessage());
    }
  }
  
  private void closestreams () {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            System.out.println("closestreams : "+ex.getMessage());
        }
     
  }
  
  private void write(DataOutputStream output ,String message){
      try {
            output.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("write :"+ ex.getMessage());
        }
      
  }   
  
  private String read(){
        String line ="";
      try {
            line = input.readUTF();
        } catch (IOException ex) {
            System.out.println("read :"+ex.getMessage());
        }
      return line;
  }
  
  private void forwardclient(String recieved) {
      StringTokenizer tokenizer = new StringTokenizer(recieved, "#");
      String recipent = tokenizer.nextToken().trim();
      String message = tokenizer.nextToken().trim();
      
      for(ClientHandler c : Server.getclients()){
          if(c.isLoggedIn && c.name.equals(recipent)){
              write(c.output,recipent + " : " +message);
              System.out.println(name + "--->"+recipent +" : "+message);
              break;
          }
      }
  }
          
          @Override
    public void run() {
        
        String recieved;
        write (output, "Your name : "+name);
        
        while(true) {
            recieved = read ();
            if (recieved.equalsIgnoreCase(Constants.LOGOUT)){
                this.isLoggedIn= false;
                closesocket();
                closestreams();
                break;
            }
            forwardclient(recieved);
        }
        closestreams();
    }

    
}
   

 
    

