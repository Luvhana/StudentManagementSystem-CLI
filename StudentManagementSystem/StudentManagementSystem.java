import java.util.*;

abstract class User {
    protected String username;
    public User(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public abstract void showMenu(Scanner scanner, StudentManagementSystem system);
}

class Admin extends User {
    public Admin(String username) {
        super(username);
    }

    @Override
    public void showMenu(Scanner scanner, StudentManagementSystem system) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Course");
            System.out.println("2. Delete Course");
            System.out.println("3. View All Students");
            System.out.println("4. View All Results");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addCourse(scanner, system);
                    break;
                case "2":
                    deleteCourse(scanner, system);
                    break;
                case "3":
                    system.viewAllStudents();
                    break;
                case "4":
                    system.viewAllResults();
                    break;
                case "5":
                    System.out.println("Admin logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void addCourse(Scanner scanner, StudentManagementSystem system) {
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        Question[] questions = new Question[5];
        for (int i = 0; i < 5; i++) {
            System.out.println("\nAdding Question " + (i + 1));
            System.out.print("Enter question text: ");
            String questionText = scanner.nextLine().trim();

            String[] options = new String[4];
            for (int j = 0; j < 4; j++) {
                System.out.print("Enter option " + (char)('a' + j) + ": ");
                options[j] = scanner.nextLine().trim();
            }

            System.out.print("Enter correct option (a-d): ");
            String correctOption = scanner.nextLine().trim().toLowerCase();

            questions[i] = new Question(questionText, options, correctOption);
        }

        system.addCourse(new Course(courseName, questions));
        System.out.println("Course added successfully!");
    }

    private void deleteCourse(Scanner scanner, StudentManagementSystem system) {
        ArrayList<Course> courses = system.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available to delete.");
            return;
        }

        System.out.println("\nAvailable Courses to Delete:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }

        System.out.print("Select course by number to delete (0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;

            if (choice > 0 && choice <= courses.size()) {
                system.deleteCourse(courses.get(choice - 1));
                System.out.println("Course deleted successfully!");
            } else {
                System.out.println("Invalid course number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}

class Student extends User {
    private String password;
    public int age;
    public String email;
    private Course enrolledCourse;
    private ArrayList<ExamResult> results = new ArrayList<>();
    private HashMap<Course, Integer> trainingProgress = new HashMap<>();

    public Student(String username, String password, int age, String email) {
        super(username);
        this.password = password;
        this.age = age;
        this.email = email;
    }

    public boolean checkPassword(String pass) {
        return password.equals(pass);
    }

    @Override
    public void showMenu(Scanner scanner, StudentManagementSystem system) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Enroll in a Course");
            System.out.println("2. Take Exam");
            System.out.println("3. View Results");
            System.out.println("4. Training Mode");
            System.out.println("5. View Training Progress");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    enrollInCourse(scanner, system);
                    break;
                case "2":
                    takeExam(scanner);
                    break;
                case "3":
                    viewResults();
                    break;
                case "4":
                    trainingMode(scanner);
                    break;
                case "5":
                    viewTrainingProgress();
                    break;
                case "6":
                    System.out.println("Student logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void enrollInCourse(Scanner scanner, StudentManagementSystem system) {
        if (enrolledCourse != null) {
            System.out.println("You are already enrolled in: " + enrolledCourse.getName());
            return;
        }

        ArrayList<Course> courses = system.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available yet.");
            return;
        }

        System.out.println("\nAvailable Courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }

        System.out.print("Select course by number (0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;

            if (choice > 0 && choice <= courses.size()) {
                enrolledCourse = courses.get(choice - 1);
                trainingProgress.put(enrolledCourse, 0); // Initialize training progress
                System.out.println("Successfully enrolled in: " + enrolledCourse.getName());
            } else {
                System.out.println("Invalid course number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void takeExam(Scanner scanner) {
        if (enrolledCourse == null) {
            System.out.println("You need to enroll in a course first!");
            return;
        }

        Question[] questions = enrolledCourse.getQuestions();
        int score = 0;

        System.out.println("\nStarting Exam for " + enrolledCourse.getName());
        System.out.println("There are " + questions.length + " questions. Good luck!\n");

        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            System.out.println("Question " + (i + 1) + ": " + q.getQuestionText());

            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((char)('a' + j) + ") " + options[j]);
            }

            System.out.print("Your answer: ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals(q.getCorrectOption())) {
                score++;
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect! The correct answer is " + q.getCorrectOption().toUpperCase());
            }
            System.out.println();
        }

        double percentage = (score * 100.0) / questions.length;
        results.add(new ExamResult(enrolledCourse, percentage));

        System.out.println("\nExam completed!");
        System.out.println("Your score: " + percentage + "% (" + (percentage >= 50 ? "Passed" : "Failed") + ")");
    }

    private void trainingMode(Scanner scanner) {
        if (enrolledCourse == null) {
            System.out.println("You need to enroll in a course first!");
            return;
        }

        Question[] questions = enrolledCourse.getQuestions();
        int currentProgress = trainingProgress.getOrDefault(enrolledCourse, 0);

        System.out.println("\n--- Training Mode: " + enrolledCourse.getName() + " ---");
        System.out.println("Current Progress: " + currentProgress + "/" + questions.length + " questions");
        System.out.println("Type 'exit' at any time to return to menu\n");

        for (int i = currentProgress; i < questions.length; i++) {
            Question q = questions[i];
            System.out.println("Question " + (i + 1) + ": " + q.getQuestionText());

            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((char)('a' + j) + ") " + options[j]);
            }

            System.out.print("Your answer (or 'hint' for help): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("exit")) {
                System.out.println("Saving progress...");
                trainingProgress.put(enrolledCourse, i);
                return;
            } else if (answer.equals("hint")) {
                System.out.println("Hint: The correct answer starts with '" +
                        q.getCorrectOption().toUpperCase() + "'");
                i--; // Repeat the same question
            } else if (answer.equals(q.getCorrectOption())) {
                System.out.println("Correct! Well done!\n");
                trainingProgress.put(enrolledCourse, i + 1);
            } else {
                System.out.println("Incorrect. The correct answer is " +
                        q.getCorrectOption().toUpperCase() + "\n");
                i--; // Repeat the same question
            }
        }

        System.out.println("Congratulations! You've completed all questions in this course!");
        trainingProgress.put(enrolledCourse, questions.length);
    }

    private void viewTrainingProgress() {
        if (enrolledCourse == null) {
            System.out.println("You need to enroll in a course first!");
            return;
        }

        Question[] questions = enrolledCourse.getQuestions();
        int progress = trainingProgress.getOrDefault(enrolledCourse, 0);

        System.out.println("\nTraining Progress for " + enrolledCourse.getName());
        System.out.println("Completed: " + progress + "/" + questions.length + " questions");
        System.out.println("Percentage: " + (progress * 100 / questions.length) + "% complete");

        if (progress == questions.length) {
            System.out.println("You've completed all training for this course!");
        }
    }

    public void viewResults() {
        if (results.isEmpty()) {
            System.out.println("No exam results yet.");
            return;
        }

        System.out.println("\nYour Exam Results:");
        for (ExamResult result : results) {
            System.out.println(result);
        }
    }
}

class Course {
    private String name;
    private Question[] questions;

    public Course(String name, Question[] questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public Question[] getQuestions() {
        return questions;
    }
}

class Question {
    private String questionText;
    private String[] options;
    private String correctOption;

    public Question(String questionText, String[] options, String correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption.toLowerCase();
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }
}

class ExamResult {
    private Course course;
    private double score;

    public ExamResult(Course course, double score) {
        this.course = course;
        this.score = score;
    }

    @Override
    public String toString() {
        return course.getName() + ": " + String.format("%.1f", score) + "% (" +
                (score >= 50 ? "Passed" : "Failed") + ")";
    }
}

public class StudentManagementSystem {
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void deleteCourse(Course course) {
        courses.remove(course);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println("\nRegistered Students:");
        for (Student student : students) {
            System.out.println("- " + student.getUsername() +
                    " (Age: " + student.age +
                    ", Email: " + student.email + ")");
        }
    }

    public void viewAllResults() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println("\nAll Student Results:");
        for (Student student : students) {
            System.out.println("\nStudent: " + student.getUsername());
            student.viewResults();
        }
    }

    public void run() {
        System.out.println("Welcome to Student Management System");
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Register");
            System.out.println("3. Student Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    adminLogin();
                    break;
                case "2":
                    studentRegister();
                    break;
                case "3":
                    studentLogin();
                    break;
                case "4":
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private void adminLogin() {
        System.out.print("\nAdmin Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Admin Password: ");
        String password = scanner.nextLine().trim();

        if (username.equals("admin") && password.equals("admin123")) {
            new Admin(username).showMenu(scanner, this);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private void studentRegister() {
        System.out.println("\nStudent Registration");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        for (Student s : students) {
            if (s.getUsername().equals(username)) {
                System.out.println("Username already exists. Please choose another.");
                return;
            }
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        students.add(new Student(username, password, age, email));
        System.out.println("Registration successful! You can now login.");
    }

    private void studentLogin() {
        System.out.println("\nStudent Login");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        for (Student student : students) {
            if (student.getUsername().equals(username) && student.checkPassword(password)) {
                System.out.println("Login successful! Welcome, " + username + ".");
                student.showMenu(scanner, this);
                return;
            }
        }

        System.out.println("Invalid username or password.");
    }

    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();

        // Pre-load initial courses
        Question[] dsaQuestions = {
                new Question("What does DSA stand for?",
                        new String[]{"Data Structures and Algorithms", "Digital Signal Analysis",
                                "Dynamic System Analysis", "Data Security Algorithm"}, "a"),
                new Question("Which is a linear data structure?",
                        new String[]{"Array", "Tree", "Graph", "Heap"}, "a"),
                new Question("What is the time complexity of binary search?",
                        new String[]{"O(1)", "O(log n)", "O(n)", "O(n log n)"}, "b"),
                new Question("Which sorting algorithm has worst-case O(n²) complexity?",
                        new String[]{"Merge Sort", "Quick Sort", "Bubble Sort", "Heap Sort"}, "c"),
                new Question("Which data structure uses FIFO principle?",
                        new String[]{"Stack", "Queue", "Tree", "Graph"}, "b")
        };
        system.addCourse(new Course("Data Structures", dsaQuestions));

        Question[] osQuestions = {
                new Question("What does OS stand for?",
                        new String[]{"Operating System", "Online Service",
                                "Optimized Software", "Order System"}, "a"),
                new Question("Which is not an OS?",
                        new String[]{"Windows", "Linux", "MySQL", "MacOS"}, "c"),
                new Question("What is the main function of an OS?",
                        new String[]{"Manage hardware resources", "Provide user interface",
                                "Run applications", "All of the above"}, "d"),
                new Question("Which scheduling algorithm allocates CPU to the process that requests it first?",
                        new String[]{"Round Robin", "Shortest Job First", "First Come First Serve", "Priority"}, "c"),
                new Question("What is virtual memory?",
                        new String[]{"Memory on the hard disk used as RAM", "Extra RAM installed",
                                "Memory used by virtual machines", "Cache memory"}, "a")
        };
        system.addCourse(new Course("Operating Systems", osQuestions));

        system.run();
    }
}
