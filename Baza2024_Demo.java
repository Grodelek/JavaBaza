import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baza2024 {
    private static Smartfon[] smartfon;
    public static void main(String[] args) throws IOException {
        smartfon = new Smartfon[4];
        for (int i = 0; i < smartfon.length; i++) {
            smartfon[i] = new Smartfon();
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
                System.out.println("1. Firma:");
                System.out.println("2. Modelu");
                System.out.println("3. Rok premiery");
                wyborSort = sc.nextInt();
            }
        } while (wybor);
        switch (opcja) {
            case 1:
                System.out.println("Wybrano opcje 1");
                otworzBaze(smartfon);
                break;
            case 2:
                System.out.println("Wybrano opcje 2");
                utworzBaze();
                break;
            case 3:
                System.out.println("Wybrano opcje 3");
                sortujBaze(smartfon,wyborSort);
                break;
            case 4:
                System.out.println("Wybrano opcje 4");
                usunBaze();
                break;
            case 5:
                System.out.println("Wybrano opcje 5");
                System.exit(0);
        }
    }
    static void otworzBaze(Smartfon[] smartfon) throws java.io.IOException {
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
        System.out.println("M - Modyfikacja H-Powrot do menu");
        char choice = sc.next().charAt(0);
        switch(choice) {
            case 'M':
                try (BufferedWriter buffwriter = new BufferedWriter(new FileWriter(nazwaBazy, false))) {
                    buffwriter.newLine();
                    System.out.println("Podaj numer ktore pole chcesz zmienic: (0-3)");
                    int numR = sc.nextInt();
                    if (numR >= 0 && numR <= 3) {
                        Smartfon modifiedSmartfon = new Smartfon();
                        modifiedSmartfon.wczytajDane();
                        smartfon[numR] = modifiedSmartfon;
                        for (Smartfon s : smartfon) {
                            buffwriter.write("Firma: " + s.getFirma() + "\n");
                            buffwriter.write("Nazwa modelu: " + s.getNazwaModelu() + "\n");
                            buffwriter.write("Rok premiery: " + s.getRokPremiery() + "\n");
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
    static void sortujBaze(Smartfon[] smartfon, int wyborSort) throws IOException {
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
                Arrays.sort(smartfon, Comparator.comparing(Smartfon::getFirma));
                break;
            case 2:
                Arrays.sort(smartfon, Comparator.comparing(Smartfon::getNazwaModelu));
                break;
            case 3:
                Arrays.sort(smartfon, Comparator.comparingInt(Smartfon::getRokPremiery));
                break;
            default:
                System.out.println("Niepoprawny wybor sortowania");

        }
        for (Smartfon s : smartfon) {
            System.out.println("Firma: " + s.getFirma() + ", Nazwa modelu: " + s.getNazwaModelu()+ ", Rok premiery: " + s.getRokPremiery());
        }
        otworzBaze(smartfon);
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
        Smartfon nowysmartfon = new Smartfon();
        stworzPlik(nazwaBazy);
        drukujMenu();
        wybierzOpcje();
    }

    static void stworzPlik(String nazwaBazy) throws java.io.IOException
    {
        Scanner sc = new Scanner(System.in);
        FileWriter writer = new FileWriter(nazwaBazy,true);

        try {
            for(int i=0;i<smartfon.length;i++) {
                smartfon[i] = new Smartfon();
                smartfon[i].wczytajDane();
                writer.write("Nazwa firmy: " + smartfon[i].getFirma());
                writer.write("\n");
                writer.write("Nazwa modelu: " + smartfon[i].getNazwaModelu());
                writer.write("\n");
                writer.write("Rok Premiery " + smartfon[i].getRokPremiery());
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
class Smartfon{
    private String nazwaModelu;
    private String firma;
    private int rokPremiery;
    private int wyborSort;
    Smartfon(){};
    Smartfon(String nazwaModelu,String firma, int rokPremiery)
    {
        this.nazwaModelu = nazwaModelu;
        this.firma = firma;
        this.rokPremiery = rokPremiery;
    }
    void wczytajDane()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj nazwe firmy");
        this.firma =sc.nextLine();
        System.out.println("Podaj model");
        this.nazwaModelu = sc.nextLine();
        System.out.println("Podaj rok Premiery:");
        while(!sc.hasNextInt()){
            System.out.println("Blad, podaj ponownie rok premiery:");
            sc.next();
        }this.rokPremiery = sc.nextInt();
    }
    String getFirma(){
        return firma;
    }
    String getNazwaModelu(){
        return nazwaModelu;
    }
    int getRokPremiery(){
        return rokPremiery;
    }
}
