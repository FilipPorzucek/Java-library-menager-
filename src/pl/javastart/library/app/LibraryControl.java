package pl.javastart.library.app;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataInputException;
import pl.javastart.library.exception.InvalidDataException;
import pl.javastart.library.exception.NoSuchOptionException;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileMenager;
import pl.javastart.library.io.file.FileMenagerBuilder;
import pl.javastart.library.model.*;

import java.util.Arrays;
import java.util.InputMismatchException;

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
                case EXIT:
                    exit();
                    break;
                case DELETE_BOOKS:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji");
                    break;


            }
        }while (option!= Option.EXIT);
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
        Publication[] publications = getSortedPublications();
        printer.printMagazine(publications);
    }

    private Publication[] getSortedPublications() {
        Publication[] publications= library.getPublications();
        Arrays.sort(publications,new AlphabeticalComparator());
        return publications;
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
        Publication[] publications=getSortedPublications();
        printer.printBooks(publications);
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
        DELETE_MAGAZINE(6,"usuń magazyn");


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
