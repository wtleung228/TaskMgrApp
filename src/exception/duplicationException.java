package exception;

import java.util.List;

import task.Task;

public class duplicationException extends Exception {
    public duplicationException(String message) {
        super(message);
    }

    public static void checkDuplicatedTaskName(String taskName, List<Task> tasks) throws duplicationException {
        if (tasks.isEmpty()) {
            return;
        }
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(taskName)) {
                throw new duplicationException("Task name already exists.");
            }
        }
    }
}