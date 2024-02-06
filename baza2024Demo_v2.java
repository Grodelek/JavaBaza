import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baza2024 {
    private static Student[] student;
    public static void main(String[] args) throws IOException {
        student = new Student[4];
        for (int i = 0; i < student.length; i++) {
            student[i] = new Student();
        }
        drukujMenu();
        centerCursorOn();
        wybierzOpcje();

    }
    static void drukujMenu() {
        System.out.println("           MENU");
        System.out.println("+=============================+");
        System.out.println("1.Otworz baze danych");
        System.out.println("2.Utworz nowa baze");
        System.out.println("3.Sortowanie bazy");
        System.out.println("4.Usun baze");
        System.out.println("5.Zakoncz program");
        System.out.println("+=============================+");
    }

    static void wybierzOpcje() throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean wybor = false;
        int input;
        int opcja = 0;
        int wyborSort = 0;
        do {
            System.out.println("Wybierz opcje:");
            input = sc.nextInt();
            if (input < 1 || input > 5) {
                wybor = true;
                System.out.println("Niepoprawna opcja");
            } else {
                wybor = false;
                opcja = input;
            }
            if(opcja == 3){
                System.out.println("Wybierz pole po ktorym chcesz posortowac:");
                System.out.println("1. Imie");
                System.out.println("2. Nazwisko");
                System.out.println("3. Rok studiow");
                wyborSort = sc.nextInt();
            }
        } while (wybor);
        switch (opcja) {
            case 1:
                System.out.println("Wybrano opcje 1");
                otworzBaze(student);
                break;
            case 2:
                System.out.println("Wybrano opcje 2");
                utworzBaze();
                break;
            case 3:
                System.out.println("Wybrano opcje 3");
                sortujBaze(student,wyborSort);
                break;
            case 4:
                System.out.println("Wybrano opcje 4");
                usunBaze();

                break;
            case 5:
                System.out.println("Wybrano opcje 5");
                //zapiszBaze(nazwaOtwartegoPliku);
                System.exit(0);
        }
    }
    static void otworzBaze(Student[] student) throws java.io.IOException {
        Scanner sc = new Scanner(System.in);
        String nazwaBazy;
        System.out.println("+=============================+");
        System.out.println("              Przeglad");
        System.out.println("+=============================+");
        System.out.println("Podaj nazwe bazy lub wroc do menu wpisujac litere G:");
        nazwaBazy = sc.nextLine();
        File file = new File(nazwaBazy);

        if (!file.exists()) {
            System.out.println("Plik nie istnieje");
            drukujMenu();
            wybierzOpcje();

        }
        if (nazwaBazy.equalsIgnoreCase("G")) {
            drukujMenu();
            wybierzOpcje();
            return;
        }
        System.out.println("Otwarto plik: "+nazwaBazy);
        try {
            String line;
            FileReader fileReader = new FileReader(nazwaBazy);
            BufferedReader buffedReader = new BufferedReader(fileReader);
            while ((line = buffedReader.readLine()) != null) {
                System.out.println(line);
            }
            buffedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("+=============================+");
        System.out.println("M - Modyfikacja   S-Sortowanie H-Powrot do menu");
        char choice = sc.next().charAt(0);
        switch(choice) {
            case 'M':
                try (BufferedWriter buffwriter = new BufferedWriter(new FileWriter(nazwaBazy, false))) {
                    buffwriter.newLine();
                    System.out.println("Podaj numer ktore pole chcesz zmienic: (0-3)");
                    int numR = sc.nextInt();
                    if (numR >= 1 && numR <= 3) {
                        Student modifiedStudent = new Student();
                        modifiedStudent.wczytajDane();
                        student[numR - 1] = modifiedStudent;
                        for (Student s : student) {
                            buffwriter.write("Imie: " + s.getImie() + "\n");
                            buffwriter.write("Nazwisko: " + s.getNazwisko() + "\n");
                            buffwriter.write("Rok studiow: " + s.getRokStudiow() + "\n");
                            buffwriter.newLine();
                        }
                    } else{
                        System.out.println("Niepoprawny numer pola");
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 'S':
                System.out.println("Sortowanie");
                break;
            case 'H':
                drukujMenu();
                wybierzOpcje();

        }
        System.out.println("Dane zostaly zmodyfikowane");
        System.out.println("Kliknij enter aby kontynuuowac");
        System.in.read();
        drukujMenu();
        wybierzOpcje();

    }
    static void sortujBaze(Student[] student, int wyborSort) throws IOException {
        Scanner sc = new Scanner(System.in);
        String nazwaBazy;
        System.out.println("Podaj nazwe bazy ktora chcesz posortowac");
        nazwaBazy = sc.next();

        File file = new File(nazwaBazy);
        if (!file.exists()) {
            System.out.println("Plik nie istnieje");
            drukujMenu();
            wybierzOpcje();

        }
        switch (wyborSort) {
            case 1:
                Arrays.sort(student, Comparator.comparing(Student::getImie));
                break;
            case 2:
                Arrays.sort(student, Comparator.comparing(Student::getNazwisko));
                break;
            case 3:
                Arrays.sort(student, Comparator.comparingInt(Student::getRokStudiow));
                break;
            default:
                System.out.println("Niepoprawny wybor sortowania");

        }
        for (Student s : student) {
            System.out.println("Imie: " + s.getImie() + ", Nazwisko: " + s.getNazwisko() + ", Rok studiow: " + s.getRokStudiow());
        }
        otworzBaze(student);
    }
    static void utworzBaze() throws java.io.IOException
    {
        Scanner sc = new Scanner(System.in);
        String nazwaBazy = new String();
        boolean poprawna = false;
        boolean w1=true;
        do{
            System.out.println("Podaj nazwe bazy w formacie bazaXX.dat, gdzie cyfry XX z [0,9]:");
            nazwaBazy=sc.next();
            poprawna = false;
            w1  = true;
            poprawna=sprawdzPoprawnoscNazwyBazy(nazwaBazy);
            File file = new File(nazwaBazy);
            w1=file.exists()||file.isDirectory()||!poprawna;
            if(w1)
                System.out.println("Nazwa bazy niepoprawna lub plik istnieje");
        }while(w1);
        Student nowyStudent = new Student();
        stworzPlik(nazwaBazy);
        drukujMenu();
        wybierzOpcje();
    }

    static void stworzPlik(String nazwaBazy) throws java.io.IOException
    {
        Scanner sc = new Scanner(System.in);
        FileWriter writer = new FileWriter(nazwaBazy,true);

        try {
            for(int i=0;i<student.length;i++) {
                student[i] = new Student();
                student[i].wczytajDane();
                writer.write("Imie studenta: " + student[i].getImie());
                writer.write("\n");
                writer.write("Nazwisko studenta: " + student[i].getNazwisko());
                writer.write("\n");
                writer.write("Rok studiow " + student[i].getRokStudiow());
                writer.write("\n");
                writer.write("\n");

            }
        }catch(IOException e ){
            e.printStackTrace();
        }
        finally{
            if(writer != null) {
                writer.close();
                System.out.println("Dane zostaly zapisane do pliku.");

            }
        }
    }
    static void usunBaze() throws java.io.IOException
    {
        String nazwaBazy;
        Scanner sc = new Scanner(System.in);
        System.out.println("Ktora baze chcesz usunac?");
        nazwaBazy = sc.next();
        try {
            File fileDelete = new File(nazwaBazy);
            if(fileDelete.delete()){
                System.out.println("Plik zostal usuniety prawidlowo.");
            }else{
                System.out.println("Nie udalo sie usunac pliku.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("+=============================+");
        System.out.println("Kliknij enter aby kontynuuowac");
        System.in.read();
        drukujMenu();
        wybierzOpcje();
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
    private static boolean sprawdzPoprawnoscNazwyBazy(String nazwaBazy) {
        Pattern wzorzec = Pattern.compile("^baza\\d{2}\\.dat$");

        Matcher matcher = wzorzec.matcher(nazwaBazy);
        return matcher.matches();
    }
}
class Student{
    private String nazwisko;
    private String imie;
    private int rokStudiow;
    private int wyborSort;
    Student(){};
    Student(String nazwisko,String imie, int rokStudiow)
    {
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.rokStudiow = rokStudiow;
    }
    void wczytajDane()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj imie studenta:");
        this.imie=sc.nextLine();
        System.out.println("Podaj nazwisko");
        this.nazwisko = sc.nextLine();
        System.out.println("Podaj rok studiow:");
        while(!sc.hasNextInt()){
            System.out.println("Blad, podaj ponownie rok studiow:");
            sc.next();
        }this.rokStudiow = sc.nextInt();
    }
    String getImie(){
        return imie;
    }
    String getNazwisko(){
        return nazwisko;
    }
    int getRokStudiow(){
        return rokStudiow;
    }
}
