package server;

import com.sun.net.httpserver.HttpServer;
import executor.FileBackedTaskManager;
import executor.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/epics", new EpicTasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile("src/save.csv");
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
        httpTaskServer.stop();
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        server.start();
    }

    public void stop() {
        System.out.println("Остановка HTTP-сервера на " + PORT + " порту.");
        server.stop(0);
    }
}
