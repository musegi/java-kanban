package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import executor.Managers;
import executor.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case ("GET"):
                getSubtasks(exchange);
                break;
            case ("POST"):
                postSubtasks(exchange);
                break;
            case ("DELETE"):
                removeSubtasks(exchange);
                break;
            default:
                sendNotFound(exchange, "Получен некорректный запрос - " + method
                        + ", Ожидалось GET, POST или DELETE.");
        }
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;
        if (Pattern.matches("^/subtasks$", path)) {
            List<Task> subtasks = manager.getSubtasksList();
            response = gson.toJson(subtasks);
            sendText(exchange, response);
        } else if (Pattern.matches("/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                response = gson.toJson(manager.getSubtaskById(id));
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        }
    }

    private void postSubtasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("/subtasks$", path)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask taskFromJson = gson.fromJson(body, Subtask.class);
            Subtask task = manager.create(taskFromJson);
            if (task != null) {
                sendText(exchange, "Подзадача успешно создана");
            } else {
                sendHasInteractions(exchange, "Не удалось создать подзадачу, имеются" +
                        " пересечения с уже существующими задачами");
            }
        } else if (Pattern.matches("/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask taskFromJson = gson.fromJson(body, Subtask.class);
                Subtask task = manager.update(taskFromJson);
                if (task != null) {
                    sendText(exchange, "Подзадача успешно обновлена");
                } else {
                    sendHasInteractions(exchange, "Не удалось обновить подзадачу, имеются" +
                            " пересечения с уже существующими задачами");
                }
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        }
    }

    private void removeSubtasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/subtasks$", path)) {
            manager.deleteSubtasks();
            sendText(exchange, "Все подзадачи были удалены");
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                manager.deleteSubtaskById(id);
                sendText(exchange, "Подзадача " + id + " успешно удалена.");
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        }
    }
}