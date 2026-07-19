package com.titan;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.Socket;

public class TitanDBTest {
    private static Thread serverThread;
    
    @BeforeAll
    public static void startServer() {
        serverThread = new Thread(() -> {
            TitanEngine.main(new String[]{});
        });
        serverThread.start();
        
        // Wait for server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetGetDelCommands() throws IOException {
        try (Socket socket = new Socket("localhost", 9090);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            // Test SET
            out.println("SET user1 abdulkadir");
            assertEquals("OK", in.readLine());
            
            // Test GET
            out.println("GET user1");
            assertEquals("FOUND abdulkadir", in.readLine());
            
            // Test GET not found
            out.println("GET unknown");
            assertEquals("NOT_FOUND", in.readLine());
            
            // Test DEL
            out.println("DEL user1");
            assertEquals("OK", in.readLine());
            
            // Test GET after DEL
            out.println("GET user1");
            assertEquals("NOT_FOUND", in.readLine());
        }
    }
}
