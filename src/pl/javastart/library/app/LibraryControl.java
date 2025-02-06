package pl.javastart.library.app;

import pl.javastart.library.exception.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileMenager;
import pl.javastart.library.io.file.FileMenagerBuilder;
import pl.javastart.library.model.*;
import pl.javastart.library.model.comparator.AlphabeticalTitleComparator;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.concurrent.ConcurrentMap;

public class LibraryControl {


    private ConsolePrinter printer=new ConsolePrinter();
    private DataReader dataReader=new DataReader(printer);
    private Library library;
    private FileMenager fileMenager;

    public LibraryControl() {
       fileMenager= new FileMenagerBuilder(printer,dataReader).build();
       try{
           library=fileMenager.importData();
           printer.printLine("Zaimportowane dane z pliku");
       }catch (DataInputException | InvalidDataException e){
           printer.printLine(e.getMessage());
           System.out.println("Zainicjowano nową baze");
           library=new Library();
       }

    }

    public void controlLoop(){
        Option option;

        do{
            printOptions();
            option=getOption();
            switch (option){
                case ADD_BOOK:
                    addBook();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                     break;
                case ADD_MAGAZINES:
                    addMagazines();
                    break;
                case PRINT_MAGAZINES:
                    printMagzines();
                    break;
                case DELETE_BOOKS:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USER:
                    printUsers();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji");
                    break;


            }
        }while (option!= Option.EXIT);
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
             //   (p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName()
             //   )
               Comparator.comparing(User:: getLastName,String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.creatwLibearyUser();
       try{ library.addUser(libraryUser);
       }catch (UserAlreadyExistsEsception e){
        printer.printLine(e.getMessage());
       }
    }


    private Option getOption(){
        boolean optionOk=false;
        Option option=null;
        while (!optionOk){
            try {
                option=Option.createFromInt(dataReader.getInt());
                optionOk=true;
            }catch (NoSuchOptionException e){
            printer.printLine(e.getMessage());
            }catch (InputMismatchException e){
                printer.printLine("Wprowadzono wartość która nie jest liczbą podaj ponownie");
            }

        }
        return option;
    }

    private void printMagzines() {
        printer.printMagazine(library.getSortedPublications(
       //         (p1,p2)->p1.getTitle().compareToIgnoreCase(p2.getTitle())
Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER)
        ));
    }



    private void addMagazines() {
        try{
            Magazine magazine=dataReader.readAndCreateMagzine();
            library.addPublication(magazine);
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        }catch (ArrayIndexOutOfBoundsException e){
            printer.printLine("Osiągnięto limi magazynów");
        }

    }
    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagzine();
            if (library.removePublication(magazine))
                printer.printLine("Usunieto magazyn");
            else
                printer.printLine("Brak wskazanego magazynu");
        }catch (InputMismatchException e) {
            printer.printLine("Nie udalo sie utworzyc magazynu, niepoprawne dane");
        }
    }

    private void exit() {
        try{
            fileMenager.exportData(library);
            printer.printLine("Export danych do pliku zakonczony powodzeniem");
        }catch (DataExportException e){
            printer.printLine(e.getMessage());
        }

        printer.printLine("Zapraszamy ponownie");
        dataReader.close();
    }



    private void printBooks() {

        printer.printBooks(library.getSortedPublications(
            //    (p1,p2)->p1.getTitle().compareToIgnoreCase(p2.getTitle())
                Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void addBook() {
        try {
            Book book=dataReader.readAndCreateBook();
            library.addPublication(book);
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        }catch (ArrayIndexOutOfBoundsException e){
            printer.printLine("Osiągnięto limit książek");
        }

    }

    private void deleteBook() {
        try {


            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                printer.printLine("Usunieto książke");
            } else printer.printLine("Nie znaleziono książki");
        }catch (InputMismatchException e){
            printer.printLine("Nie udalo sie utworzyc książki, niepoprawne dane");
        }

    }


    private void printOptions() {

        printer.printLine("Wybierz opcje");
        for (Option option : Option.values()) {
            printer.printLine(option.toString());
        }

    }
    private enum Option {
        EXIT(0,"wyjście z programu"),
        ADD_BOOK(1,"dodanie nowej książki"),
        PRINT_BOOKS(2,"wyświetlenie dostępnych książek"),
        ADD_MAGAZINES(3,"dodanie nowego magazynu"),
        PRINT_MAGAZINES(4,"wyświetlenie dostępnych magazynów"),
        DELETE_BOOKS(5,"usuń książkę"),
        DELETE_MAGAZINE(6,"usuń magazyn"),
        ADD_USER(7,"Dodaj użytkownika"),
        PRINT_USER(8,"Wyświetl użytkownikó");


        private final int value;
        private final String description;
        private FileMenager fileMenager;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value+"-"+description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException{
            try {
                return Option.values()[option];
            }catch (ArrayIndexOutOfBoundsException e){
                throw new NoSuchOptionException("Brak opcji o id"+option);
            }
        }
    }

}
