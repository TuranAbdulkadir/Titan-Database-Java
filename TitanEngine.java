import java.io.*;
import java.net.*; // AG KUTUPHANESI EKLENDI
import java.util.*;

public class TitanEngine {
    private static Map<String, String> memoryStore = new HashMap<>();
    private static final String DB_FILE = "titan_core.db";
    private static final int PORT = 9090; // Titan'in Hizmet Portu

    public static void main(String[] args) {
        printBanner();
        loadDataFromDisk();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SISTEM] TITAN DB Ag Modunda Baslatildi.");
            System.out.println("[SISTEM] Dinlenen Port: " + PORT);

            // SUNUCU ASLA KAPANMAZ
            while (true) {
                System.out.println("[TITAN] Baglanti bekleniyor...");
                Socket clientSocket = serverSocket.accept(); // Baglanti gelene kadar burada durur
                System.out.println("[AG] Yeni baglanti kabul edildi: " + clientSocket.getInetAddress());

                // Gelen baglantiyi ayri bir islemde ele al (Handling)
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("[KRITIK HATA] Sunucu baslatilamadi: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            // Istemci bagli oldugu surece komutlari dinle
            while ((inputLine = in.readLine()) != null) {
                String response = processCommand(inputLine);
                out.println(response); // Cevabi istemciye gonder
            }
        } catch (IOException e) {
            System.out.println("[AG] Baglanti koptu.");
        }
    }

    // KOMUT ISLEME MERKEZI
    private static String processCommand(String input) {
        if (input == null || input.trim().isEmpty()) return "ERROR: Bos komut";

        String[] parts = input.trim().split("\\s+", 3);
        String command = parts[0].toUpperCase();

        try {
            switch (command) {
                case "SET":
                    if (parts.length < 3) return "ERROR: Kullanim SET <key> <value>";
                    memoryStore.put(parts[1], parts[2]);
                    saveDatabase();
                    return "OK"; // Kisa ve net cevap

                case "GET":
                    if (parts.length < 2) return "ERROR: Kullanim GET <key>";
                    if (memoryStore.containsKey(parts[1])) {
                        return "FOUND " + memoryStore.get(parts[1]);
                    } else {
                        return "NOT_FOUND";
                    }

                case "LIST": // Tum verileri tek satirda don (protokol icin)
                    return "COUNT " + memoryStore.size();

                default:
                    return "ERROR: Bilinmeyen Komut";
            }
        } catch (Exception e) {
            return "ERROR: Sunucu Hatasi";
        }
    }

    // --- DISK ISLEMLERI (AYNI KALDI) ---
    private static void saveDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (Map.Entry<String, String> entry : memoryStore.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) { System.err.println("Disk Hatasi"); }
    }

    private static void loadDataFromDisk() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) memoryStore.put(parts[0], parts[1]);
            }
        } catch (IOException e) { }
    }

    private static void printBanner() {
        System.out.println("--- TITAN DB (NETWORK EDITION) ---");
    }
}