import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client {
    public static void main(String[] args) throws Exception {
        try (Socket clientSocket = new Socket("localhost", 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(System.in))
        ) {
            while (true) {
                System.out.println("Выберите операцию или 'end':" +
                        "\n1. Внести покупку" +
                        "\n2. Показать категории");

                String input = reader.readLine();

                if (input.equals("end")) {
                    break;
                }

                if (input.equals("1")) {
                    System.out.println("Укажите через пробел: " +
                            "покупку дату (ГГГГ.ММ.ДД) сумму или 'end', чтобы выйти");
                    try {
                        String input1 = reader.readLine();
                        String[] parts = input1.split(" ");

                        // Фиксация ошибки ввода: если не 3 части данных через пробел
                        if (parts.length != 3) {
                            System.out.println("Неправильно введены данные");
                            continue;
                        }

                        String title = parts[0];
                        String date = parts[1];

                        // Фиксация ошибки ввода: дата вне формата YYYY.MM.DD
                        try {
                            LocalDate datePattern = LocalDate.parse(date,
                                    DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                        } catch (DateTimeParseException dateTimeParseException) {
                            System.out.println("Ошибка. Формат даты должен быть: ГГГГ.ММ.ДД");
                            continue;
                        }
                        int sum = Integer.parseInt(parts[2]);

                        GsonBuilder builder = new GsonBuilder();
                        builder.excludeFieldsWithoutExposeAnnotation();
                        Gson gson = builder.create();
                        MaxCategory sendChoice = new MaxCategory(title, date, sum);

                        String pkg = gson.toJson(sendChoice);

                        out.println(pkg);
                        // ОТВЕТ СЕРВЕРА - КАТЕГОРИЯ С МАКСИМАЛЬНОЙ СУММОЙ
                        String answer = in.readLine();
                        System.out.println(answer);
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода");
                        continue;
                    }
                }

                // чтение CSV
                if (input.equals("2")) {
                    out.println("2");
                    // Ответ сервера - список всех категорий из файла CSV
                    String answer = in.readLine();
                    System.out.println(answer);
                }
            }
        }
    }
}
