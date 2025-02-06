package pl.javastart.library.io.file;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataInputException;
import pl.javastart.library.exception.InvalidDataException;
import pl.javastart.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class CsvFileMenager implements FileMenager{
    private static final String FILE_NAME="Library.csv";
    private static final String USERS_FILE_NAME= "Library_users.csv";
    @Override
    public Library importData() {
       Library library =new Library();
       importPUblications(library);
       importUsers(library);

       return library;
    }

    private void importUsers(Library library) {
        try(BufferedReader bufferedReader=new  BufferedReader(new FileReader(USERS_FILE_NAME))) {
            bufferedReader.lines()
                    .map(this::createUserFromString)
                    .forEach(library::addUser);
        } catch (FileNotFoundException e) {
            throw new DataInputException("Brak pliku"+FILE_NAME);
        } catch (IOException e) {
            throw new DataInputException("Błąd odczytu pliku "+FILE_NAME);
        }

    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split=csvText.split(";");
        String firstName=split[0];
        String lastName=split[1];
        String pesel=split[2];
       return new LibraryUser(firstName,lastName,pesel);

    }

    private void importPUblications(Library library) {
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(FILE_NAME))) {

            bufferedReader.lines()
            .map(this::createObjectFromString)
                    .forEach(library::addPublication);
        } catch (FileNotFoundException e) {
            throw new DataInputException("Brak pliku"+FILE_NAME);
        } catch (IOException e) {
            throw new DataInputException("Błąd odczytu pliku"+FILE_NAME);
        }
    }


    private Publication createObjectFromString(String line) {
        String[] split=line.split(";");
        String type=split[0];
        if(Book.TYPE.equals(type)){
            return createBook(split);
        } else if (Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Nieznany typ publikacji"+type);
    }

    private Magazine createMagazine(String[] data) {
        String title=data[1];
        String publisher=data[2];
        int year=Integer.valueOf(data[3]);
        int month=Integer.valueOf(data[4]);
        int day=Integer.valueOf(data[5]);
        String language=data[6];
        return  new Magazine(title,publisher,language,year,month,day);
    }

    private Book createBook(String[] data) {
        String title=data[1];
        String publisher=data[2];
        int year=Integer.valueOf(data[3]);
        String author=data[4];
        int pages=Integer.valueOf(data[5]);
        String isbn=data[6];

        return new Book(title,author,year,pages,publisher,isbn);
    }

    @Override
    public void exportData(Library library) {
        expportPublications(library);
        exportUsers(library);

    }

    private void exportUsers(Library library) {
        Collection<LibraryUser>users=library.getUsers().values();
       exportToCsv(users,USERS_FILE_NAME);


    }

    private void expportPublications(Library library) {
        Collection<Publication> publications= library.getPublcations().values();
       exportToCsv(publications,FILE_NAME);
    }


    private<T extends CsvConvertible> void exportToCsv(Collection<T> collection,String fileName) {
        try (
                var fileWriter=new FileWriter(fileName);
                var buferredWriter=new BufferedWriter(fileWriter)){
            for (T element : collection) {
                buferredWriter.write(element.toCsv());
                buferredWriter.newLine();
            }

        }catch (IOException e){
            throw new DataExportException("Błąd exportu użytkowników do pliku");
        }


    }


}
