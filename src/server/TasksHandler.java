package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import executor.TaskManager;
import executor.Managers;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson = Managers.getGson();

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case ("GET"):
                getTasks(exchange);
                break;
            case ("POST"):
                postTasks(exchange);
                break;
            case ("DELETE"):
                removeTasks(exchange);
                break;
            default:
                sendNotFound(exchange, "Получен некорректный запрос - " + method
                        + ", Ожидалось GET, POST или DELETE.");
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;
        if (Pattern.matches("^/tasks$", path)) {
            List<Task> tasks = manager.getTasksList();
            response = gson.toJson(tasks);
            sendText(exchange, response);
        } else if (Pattern.matches("/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                response = gson.toJson(manager.getTaskById(id));
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }

    private void postTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("/tasks$", path)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task taskFromJson = gson.fromJson(body, Task.class);
            Task task = manager.create(taskFromJson);
            if (task != null) {
                sendText(exchange, "Задача успешно создана");
            } else {
                sendHasInteractions(exchange, "Не удалось создать задачу, имеются" +
                        " пересечения с уже существующими задачами");
            }
        } else if (Pattern.matches("/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task taskFromJson = gson.fromJson(body, Task.class);
                Task task = manager.update(taskFromJson);
                if (task != null) {
                    sendText(exchange, "Задача успешно обновлена");
                } else {
                    sendHasInteractions(exchange, "Не удалось обновить задачу, имеются" +
                            " пересечения с уже существующими задачами");
                }
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }

    private void removeTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/tasks$", path)) {
            manager.deleteTasks();
            sendText(exchange, "Все задачи были удалены");
        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                manager.deleteTaskById(id);
                sendText(exchange, "Задача " + id + " успешно удалена.");
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }
}
