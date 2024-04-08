import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleDatabase{
    private static Student[] students;
    public static void main(String[] args) throws IOException {
        students = new Student[4];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student();
        }
        printMenu();
        centerCursorOn();
        chooseOption();
    }

    static void printMenu() {
        System.out.println("           MENU");
        System.out.println("+=============================+");
        System.out.println("1. Open database");
        System.out.println("2. Create new database");
        System.out.println("3. Sort database");
        System.out.println("4. Delete database");
        System.out.println("5. Exit program");
        System.out.println("+=============================+");
    }

    static void chooseOption() throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean choice = false;
        int input;
        int option = 0;
        int sortChoice = 0;
        do {
            System.out.println("Choose an option:");
            input = sc.nextInt();
            if (input < 1 || input > 5) {
                choice = true;
                System.out.println("Invalid option");
            } else {
                choice = false;
                option = input;
            }
            if(option == 3){
                System.out.println("Choose field to sort by:");
                System.out.println("1. First name");
                System.out.println("2. Last name");
                System.out.println("3. Year of study");
                sortChoice = sc.nextInt();
            }
        } while (choice);
        switch (option) {
            case 1:
                System.out.println("Selected option 1");
                openDatabase(students);
                break;
            case 2:
                System.out.println("Selected option 2");
                createDatabase();
                break;
            case 3:
                System.out.println("Selected option 3");
                sortDatabase(students,sortChoice);
                break;
            case 4:
                System.out.println("Selected option 4");
                deleteDatabase();
                break;
            case 5:
                System.out.println("Selected option 5");
                System.exit(0);
        }
    }

    static void openDatabase(Student[] students) throws java.io.IOException {
        Scanner sc = new Scanner(System.in);
        String dbName;
        System.out.println("+=============================+");
        System.out.println("             Browse");
        System.out.println("+=============================+");
        System.out.println("Enter database name or go back to menu by entering letter G:");
        dbName = sc.nextLine();
        File file = new File(dbName);

        if (!file.exists()) {
            System.out.println("File does not exist");
            printMenu();
            chooseOption();
        }
        if (dbName.equalsIgnoreCase("G")) {
            printMenu();
            chooseOption();
            return;
        }
        System.out.println("Opened file: "+dbName);
        try {
            String line;
            FileReader fileReader = new FileReader(dbName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("+=============================+");
        System.out.println("M - Modify   S - Sort H - Back to menu");
        char choice = sc.next().charAt(0);
        switch(choice) {
            case 'M':
                try (BufferedWriter buffwriter = new BufferedWriter(new FileWriter(dbName, false))) {
                    buffwriter.newLine();
                    System.out.println("Enter the number of the field you want to modify: (1-3)");
                    int numR = sc.nextInt();
                    if (numR >= 1 && numR <= 3) {
                        Student modifiedStudent = new Student();
                        modifiedStudent.inputData();
                        students[numR - 1] = modifiedStudent;
                        for (Student s : students) {
                            buffwriter.write("First Name: " + s.getFirstName() + "\n");
                            buffwriter.write("Last Name: " + s.getLastName() + "\n");
                            buffwriter.write("Year of Study: " + s.getYearOfStudy() + "\n");
                            buffwriter.newLine();
                        }
                    } else{
                        System.out.println("Invalid field number");
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 'S':
                System.out.println("Sorting");
                break;
            case 'H':
                printMenu();
                chooseOption();
        }
        System.out.println("Data modified");
        System.out.println("Press enter to continue");
        System.in.read();
        printMenu();
        chooseOption();
    }

    static void sortDatabase(Student[] students, int sortChoice) throws IOException {
        Scanner sc = new Scanner(System.in);
        String dbName;
        System.out.println("Enter the name of the database you want to sort");
        dbName = sc.next();

        File file = new File(dbName);
        if (!file.exists()) {
            System.out.println("File does not exist");
            printMenu();
            chooseOption();
        }
        switch (sortChoice) {
            case 1:
                Arrays.sort(students, Comparator.comparing(Student::getFirstName));
                break;
            case 2:
                Arrays.sort(students, Comparator.comparing(Student::getLastName));
                break;
            case 3:
                Arrays.sort(students, Comparator.comparingInt(Student::getYearOfStudy));
                break;
            default:
                System.out.println("Invalid sort choice");
        }
        for (Student s : students) {
            System.out.println("First Name: " + s.getFirstName() + ", Last Name: " + s.getLastName() + ", Year of Study: " + s.getYearOfStudy());
        }
        openDatabase(students);
    }

    static void createDatabase() throws java.io.IOException
    {
        Scanner sc = new Scanner(System.in);
        String dbName = new String();
        boolean valid = false;
        boolean w1=true;
        do{
            System.out.println("Enter database name in the format databaseXX.dat, where XX is in [0,9]:");
            dbName=sc.next();
            valid = false;
            w1  = true;
            valid=checkDatabaseNameValidity(dbName);
            File file = new File(dbName);
            w1=file.exists()||file.isDirectory()||!valid;
            if(w1)
                System.out.println("Invalid database name or file exists");
        }while(w1);
        Student newStudent = new Student();
        createFile(dbName);
        printMenu();
        chooseOption();
    }

    static void createFile(String dbName) throws java.io.IOException
    {
        Scanner sc = new Scanner(System.in);
        FileWriter writer = new FileWriter(dbName,true);

        try {
            for(int i=0;i<students.length;i++) {
                students[i] = new Student();
                students[i].inputData();
                writer.write("First Name: " + students[i].getFirstName());
                writer.write("\n");
                writer.write("Last Name: " + students[i].getLastName());
                writer.write("\n");
                writer.write("Year of Study " + students[i].getYearOfStudy());
                writer.write("\n");
                writer.write("\n");

            }
        }catch(IOException e ){
            e.printStackTrace();
        }
        finally{
            if(writer != null) {
                writer.close();
                System.out.println("Data has been saved to file.");
            }
        }
    }

    static void deleteDatabase() throws java.io.IOException
    {
        String dbName;
        Scanner sc = new Scanner(System.in);
        System.out.println("Which database do you want to delete?");
        dbName = sc.next();
        try {
            File fileDelete = new File(dbName);
            if(fileDelete.delete()){
                System.out.println("File deleted successfully.");
            }else{
                System.out.println("Failed to delete file.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("+=============================+");
        System.out.println("Press enter to continue");
        System.in.read();
        printMenu();
        chooseOption();
    }

    static void centerCursor(Robot robot){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width/2;
        int centerY = screenSize.height/2;
        robot.mouseMove(centerX,centerY);
    }

    static void centerCursorOn(){
        EventQueue.invokeLater(()->{
            try{
                Robot robot = new Robot();
                centerCursor(robot);
            }catch(AWTException e){
                e.printStackTrace();
            }
        });
    }
    private static boolean checkDatabaseNameValidity(String dbName) {
        Pattern pattern = Pattern.compile("^database\\d{2}\\.dat$");

        Matcher matcher = pattern.matcher(dbName);
        return matcher.matches();
    }
}
class Student{
    private String lastName;
    private String firstName;
    private int yearOfStudy;
    Student(){};
    Student(String lastName, String firstName, int yearOfStudy)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.yearOfStudy = yearOfStudy;
    }
    void inputData()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter student's first name:");
        this.firstName=sc.nextLine();
        System.out.println("Enter last name");
        this.lastName = sc.nextLine();
        System.out.println("Enter year of study:");
        while(!sc.hasNextInt()){
            System.out.println("Error, enter year of study again:");
            sc.next();
        }this.yearOfStudy = sc.nextInt();
    }

    String getFirstName(){
        return firstName;
    }

    String getLastName(){
        return lastName;
    }

    int getYearOfStudy(){
        return yearOfStudy;
    }
}
