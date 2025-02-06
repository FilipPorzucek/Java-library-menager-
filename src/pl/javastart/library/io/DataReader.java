package pl.javastart.library.io;

import pl.javastart.library.model.Book;
import pl.javastart.library.model.LibraryUser;
import pl.javastart.library.model.Magazine;

import java.util.Scanner;

public class DataReader {

    private Scanner scanner=new Scanner(System.in);
  private ConsolePrinter consolePrinter;

    public DataReader(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    }

    public   Book readAndCreateBook(){

        consolePrinter.printLine("Tytuł");
        String title=scanner.nextLine();
        consolePrinter.printLine("Autor");
        String autor=scanner.nextLine();
        consolePrinter.printLine("Wydawnictwo:");
        String publisher=scanner.nextLine();
        consolePrinter.printLine("ISBN");
        String isbn=scanner.nextLine();
        consolePrinter.printLine("Rok wydania:");
        int releaseDate=getInt();
        consolePrinter.printLine("Liczba stron");
        int pages=getInt();
       return new Book(autor,title,releaseDate,pages,publisher,isbn);
    }
    public Magazine readAndCreateMagzine(){

        consolePrinter.printLine("Tytuł");
        String title=scanner.nextLine();
        consolePrinter.printLine("Wydawnictwo");
        String publisher=scanner.nextLine();
        consolePrinter.printLine("Język:");
        String language=scanner.nextLine();
        consolePrinter.printLine("Rok wydania");
       int year=getInt();
        consolePrinter.printLine("Miesiąc:");
        int month=getInt();
        consolePrinter.printLine("Dzień");
        int day= getInt();
        return new Magazine(title,publisher,language,year,month,day);
    }

    public LibraryUser creatwLibearyUser(){
        consolePrinter.printLine("Podaj imie");
        String firstName=scanner.nextLine();
        consolePrinter.printLine("Podaj Nazwisko");
        String lastName=scanner.nextLine();
        consolePrinter.printLine("Podaj Pesel");
        String pesel=scanner.nextLine();
        return new LibraryUser(firstName,lastName,pesel);
    }


    public int getInt(){
      try {
          return scanner.nextInt();
      }finally {
          scanner.nextLine();
      }

    }

    public String getString(){
        return scanner.nextLine();
    }

    public void close(){
      scanner.close();
    }

}
