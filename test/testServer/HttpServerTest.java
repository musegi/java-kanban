package testServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import executor.Managers;
import executor.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.HttpURLConnection.HTTP_OK;

class HttpServerTest {
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private EpicTask epicTask1;
    private Subtask subtask1;
    private Task task1;
    private final URI uri = URI.create("http://localhost:8080");
    private final Gson gson = Managers.getGson();

    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefaultTask();
        taskServer = new HttpTaskServer(taskManager);
        task1 = new Task("Задача 1", "описание задача 1",
                Statuses.NEW, "14:37 12.03.15", 20);
        epicTask1 = new EpicTask("Эпик 1", "описание эпик 1");
        subtask1 = new Subtask("Подзадача 1", "описание подзадача 1",
                Statuses.IN_PROGRESS, epicTask1, "15:53 12.03.15", 15);
        taskServer.start();

    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void getTaskByIdTest () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        taskManager.create(task1);
        URI uri1 = URI.create(uri + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri1)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(task1, actual, "Задачи не совпадают");
    }

    @Test
    void deleteTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(task1);
        URI uri1 = URI.create(uri + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri1)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());
    }

    @Test
    void deleteAllEpicsTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(epicTask1);
        URI uri1 = URI.create(uri + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri1)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());
    }
}
