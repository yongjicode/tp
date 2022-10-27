package task;

import appointment.Appointment;
import appointment.AppointmentList;
import employee.Employee;
import employee.EmployeeList;
import exception.DukeException;

import java.util.ArrayList;

public class TaskList {
    static ArrayList<Task> tasks = new ArrayList<>();

    //view every single task in the clinic
    public static void viewTasks() {
        System.out.println("Here are all the tasks for the clinic:");
        for (Task task : tasks) {
            System.out.println("_______________________________________");
            task.printTask();
        }
    }

    public static void addTask(Task task) throws DukeException {

        // appointment aggregate task
        Appointment appointment = AppointmentList.findAppointment(task.getAppointmentId());
        if (appointment == null) {
            throw new DukeException();
        }

        // employee aggregate task
        Employee employee = EmployeeList.findEmployee(task.getEmployeeId());
        if (employee == null) {
            throw new DukeException();
        }

        // only after passing the check, add this task
        tasks.add(task);
        appointment.addTaskToAppointment(task);
        appointment.updateAppointmentStatus();
        employee.addTaskToEmployee(task);

        System.out.print("Got it. I've added this task: ");
        System.out.println(task.getTaskDescription());
        System.out.println("Performed by: " + task.getEmployeeId());
        System.out.println("Appointment: " + task.getAppointmentId());
        System.out.println("Now you have " + tasks.size() + " task in the list.");
    }

    // assign task to be done by another person
    public static void reassignTask(int taskId, int employeeId) {
        Task taskToReassign = TaskList.findTask(taskId);
        // Remove from original Employee's task list
        if (taskToReassign != null) {
            EmployeeList.findEmployee(taskToReassign.getEmployeeId()).removeTaskFromEmployee(taskId);
            // Add to new Employee's task list
            EmployeeList.findEmployee(employeeId).addTaskToEmployee(taskToReassign);
            System.out.println("Got it. Task " + taskId + " has been reassigned from " + EmployeeList.findEmployee(taskToReassign.getEmployeeId()).getEmployeeName() + " to " + EmployeeList.findEmployee(employeeId).getEmployeeName() + "!");
            // Change employeeId in taskToReassign
            taskToReassign.setEmployeeId(employeeId);
        }
    }

    public static void removeTask(int taskId) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                // remove from overall task list
                tasks.remove(task);
                // remove from appointment
                AppointmentList.findAppointment(task.getAppointmentId()).removeTaskFromAppointment(taskId);
                // remove from employee
                EmployeeList.findEmployee(task.getEmployeeId()).removeTaskFromEmployee(taskId);
                System.out.print("Got it. I've removed this task: ");
                task.printTask();
                System.out.println("Now you have " + tasks.size() + " task in the list.");
                break;
            }
        }
    }

    public static Task findTask(int taskId) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    public static void finishTask(int taskId) throws DukeException {
        findTask(taskId).setDone();
        System.out.print("Got it. I've finished this task: ");
        System.out.println(findTask(taskId).getTaskDescription());
        Appointment appointment = AppointmentList.findAppointment(findTask(taskId).appointmentId);
        if (appointment == null) {
            throw new DukeException();
        }
        appointment.updateAppointmentStatus();
    }

}
