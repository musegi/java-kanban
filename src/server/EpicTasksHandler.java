package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import executor.Managers;
import executor.TaskManager;
import tasks.EpicTask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicTasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicTasksHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case ("GET"):
                getEpicTasks(exchange);
                break;
            case ("POST"):
                postEpicTasks(exchange);
                break;
            case ("DELETE"):
                removeEpicTasks(exchange);
                break;
            default:
                sendNotFound(exchange, "Получен некорректный запрос - " + method
                        + ", Ожидалось GET, POST или DELETE.");
        }
    }

    private void getEpicTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;
        if (Pattern.matches("^/epics$", path)) {
            List<Task> tasks = manager.getEpicTasksList();
            response = gson.toJson(tasks);
            sendText(exchange, response);
        } else if (Pattern.matches("/epics/\\d+$", path)) {
            String pathId = path.replaceFirst("/epics/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                response = gson.toJson(manager.getEpicTaskById(id));
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else if (Pattern.matches("/epics/\\d+/subtasks", path)) {
            String pathId = path.replaceFirst("/epics/", "")
                    .replace("/subtasks", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                response = gson.toJson(manager.getSubtaskListByEpicTaskId(id));
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }

    private void postEpicTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("/epics$", path)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            EpicTask taskFromJson = gson.fromJson(body, EpicTask.class);
            EpicTask task = manager.create(taskFromJson);
            if (task != null) {
                sendText(exchange, "Эпик задача успешно создана");
            } else {
                sendHasInteractions(exchange, "Не удалось создать эпик задачу, имеются" +
                        " пересечения с уже существующими задачами");
            }
        } else if (Pattern.matches("/epics/\\d+$", path)) {
            String pathId = path.replaceFirst("/epics/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                EpicTask taskFromJson = gson.fromJson(body, EpicTask.class);
                EpicTask task = manager.update(taskFromJson);
                if (task != null) {
                    sendText(exchange, "Эпик задача успешно обновлена");
                } else {
                    sendHasInteractions(exchange, "Не удалось обновить эпик задачу, имеются" +
                            " пересечения с уже существующими задачами");
                }
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }

    private void removeEpicTasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/epics$", path)) {
            manager.deleteEpicTasks();
            sendText(exchange, "Все эпик задачи были удалены");
        } else if (Pattern.matches("^/epics/\\d+$", path)) {
            String pathId = path.replaceFirst("/epics/", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                manager.deleteEpicTaskById(id);
                sendText(exchange, "Эпик задача " + id + " успешно удалена.");
            } else {
                sendNotFound(exchange, "Получен некорректный id=" + pathId);
            }
        } else {
            sendNotFound(exchange, "Указан неверный путь: " + path);
        }
    }
}
