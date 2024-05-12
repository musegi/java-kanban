package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import executor.Managers;
import executor.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            String path = exchange.getRequestURI().getPath();
            if (Pattern.matches("/history$", path)) {
                List<Task> history = manager.getHistory();
                String response = gson.toJson(history);
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Указан неверный путь: " + path);
            }
        } else {
            sendNotFound(exchange, "Получен некорректный запрос - " + method
                        + ", Ожидалось GET.");
        }
    }
}
