package CN.chatbot;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;

public class ChatSocketHandler implements Runnable{
    private Scanner getMessage;
    private PrintWriter sendMessage;
    private Socket socket;
    private String userAlias;

    public ChatSocketHandler(final Socket socket)throws IOException{
        this.socket=socket;
        this.getMessage=new Scanner(this.socket.getInputStream());
        this.sendMessage=new PrintWriter(this.socket.getOutputStream(),true);
    }

    @Override
    public void run() {
        try{

            while (true) {
                this.sendMessage.println("Welcomr to chatbot\n");
                this.sendMessage.println("Please enter a unique Alias: \n");
                this.userAlias=getMessage.nextLine();
                if(this.userAlias==null){
                    return;
                }

                if(!ChatSocketServer.addUniqueUser(this.userAlias)){
                    this.sendMessage.println("\n that name is already taken or its empty");
                    continue;
                }
                break;
            }
            ChatSocketServer.broadcastMessage("\nThis user "+this.userAlias+" has entered the chat");
            ChatSocketServer.addBroadCasters(this.sendMessage);


            this.sendMessage.println("\n Please enter a new message");
            while (true) {
                String newMessage=getMessage.nextLine();
                if(newMessage!=null && !newMessage.isEmpty()){
                    if(newMessage.toLowerCase().startsWith("quit")){
                        break;
                    }
                    //broadcast message to everyone
                    ChatSocketServer.broadcastMessage(newMessage);
                    
                }
                
            }
            socket.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            ChatSocketServer.broadcastMessage("\nThis user "+this.userAlias+" has left the chat");
            ChatSocketServer.removeBroadCasters(this.sendMessage);
        }

        
    }
    
}
