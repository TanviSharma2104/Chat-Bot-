package CN.chatbot;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.IOException;
import java.io.PrintWriter;
// import java.net.Socket;
import java.net.ServerSocket;

public class ChatSocketServer {
    public static Set<String> userAlias = new HashSet<String>();
    public static Set<PrintWriter> userBroadCasters = new HashSet<>();

    public static boolean addUniqueUser(final String newUserAlias){
        if(newUserAlias.isEmpty() || userAlias.contains(newUserAlias)){
            return false;
        }
        userAlias.add(newUserAlias);
        return true;
    }

    public static void addBroadCasters(final PrintWriter printWriter){
        userBroadCasters.add(printWriter);
    }

    public static void removeBroadCasters(final PrintWriter printWriter){
        userBroadCasters.remove(printWriter);
    }


    public static void broadcastMessage(final String newMessage){
        userBroadCasters.forEach(user-> user.println(newMessage));
    }

    public static void main(String[] args) {
        System.out.println("The chat server about to start.");
        ThreadPoolExecutor chatWindows=(ThreadPoolExecutor)Executors.newFixedThreadPool(10);

        try(ServerSocket chatServerSocket=new ServerSocket(59001)){
            System.out.println("\nThe chat server has started.");
            
            while(true){
                chatWindows.execute(new ChatSocketHandler(chatServerSocket.accept()));
            }

        }
        catch(IOException e){
            throw new RuntimeException(e);

        }
    }
    
}
