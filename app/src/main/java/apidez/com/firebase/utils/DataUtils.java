package apidez.com.firebase.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import apidez.com.firebase.model.Priority;
import apidez.com.firebase.model.Todo;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class DataUtils {
    private static String[] LANGUAGES = new String[]{
            "Android", "iOS", "Ruby on Rails", "C#", "Java", "Swift",
            "Python", "C", "C++", "Objective-C", "Scala", "Perl"
    };

    private static Priority[] PRIORITIES = new Priority[]{
            Priority.HIGH, Priority.LOW, Priority.LOW, Priority.MED, Priority.HIGH, Priority.MED,
            Priority.HIGH, Priority.LOW, Priority.LOW, Priority.MED, Priority.HIGH, Priority.MED
    };

    public static List<Todo> provideMockTodoList() {
        List<Todo> todoList = new ArrayList<>();

        // Three special case
        todoList.add(
                new Todo.Builder(LANGUAGES[0], PRIORITIES[0])
                        .dueDate(new Date())
                        .completed(false)
                        .build()
        );
        todoList.add(
                new Todo.Builder(LANGUAGES[1], PRIORITIES[1])
                        .dueDate(DateUtils.getTomorrow())
                        .completed(false)
                        .build()
        );
        todoList.add(
                new Todo.Builder(LANGUAGES[2], PRIORITIES[2])
                        .completed(false)
                        .build()
        );

        // Others
        Random random = new Random();
        for (int i = 3; i < 12; i++) {
            todoList.add(
                    new Todo.Builder(LANGUAGES[i], PRIORITIES[i])
                            .completed(random.nextBoolean())
                            .dueDate(DateUtils.createRandomDate())
                            .build()
            );
        }
        return todoList;
    }
}
