import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("START!");

            // Установка начальных значений категорий и суммы из файла data.bin
            MaxCategoryMaker maxCategoryMaker = new MaxCategoryMaker();

            File json = new File("data.bin");
//            startWork.startWork(json);
            maxCategoryMaker.startWork(json);
//            System.out.println("SERVER = maxCategory.getTitleDateSumList(): " + startWork.getTitleDateSumList());
//            List<MaxCategory> categorySumList = startWork.getCategorySumList();
//            System.out.println("SERVER = categorySumList: " + categorySumList);

            while (true) {
                Socket fromClientSocket = serverSocket.accept();
                try (
                        Socket socket = fromClientSocket;
                        // Отправка данных
                        PrintWriter out = new PrintWriter(
                                socket.getOutputStream(), true);
                        // Прием входящих данных
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                ) {
                    String inputWord;
                    while ((inputWord = in.readLine()) != null) {
                        System.out.println("Получено: " + inputWord);
                        if (inputWord.equals("2")) {
                            List<Category> categories =
                                    Category.singleLine("categories.csv");
                            out.println(categories);
                            System.out.println("Отправлен ответ");
                        } else {
                            String maxCategories = maxCategoryMaker.writeCategorySum(inputWord);
                            System.out.println("Внесено!");
                            out.println(maxCategories);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}



