import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new GameHandler());
        server.start();
        System.out.println("Server is listening on port 8000");
    }

    static class GameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                String playerChoice = exchange.getRequestBody().toString();
                String computerChoice = generateComputerChoice();

                String result = determineWinner(playerChoice, computerChoice);

                exchange.sendResponseHeaders(200, result.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(result.getBytes());
                outputStream.close();
            }
        }

        private String generateComputerChoice() {
            String[] choices = {"rock", "paper", "scissors"};
            Random random = new Random();
            int index = random.nextInt(choices.length);
            return choices[index];
        }

        private String determineWinner(String playerChoice, String computerChoice) {
            if (playerChoice.equals(computerChoice)) {
                return "It's a tie!";
            } else if ((playerChoice.equals("rock") && computerChoice.equals("scissors")) ||
                    (playerChoice.equals("paper") && computerChoice.equals("rock")) ||
                    (playerChoice.equals("scissors") && computerChoice.equals("paper"))) {
                return "You win!";
            } else {
                return "Computer wins!";
            }
        }
    }
}
