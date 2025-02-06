package pl.javastart.library.io.file;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataInputException;
import pl.javastart.library.model.Library;

import java.io.*;

public class SerialazableFileMenager implements FileMenager{
private static final String FILE_NAME="Library.o";

    @Override
    public Library importData() {

        try (
                var fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois=new ObjectInputStream(fis);
                ){
            return (Library) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new DataInputException("Brak pliku"+FILE_NAME);
        } catch (IOException e) {
            throw new DataInputException("Błąd odczytu pliku"+FILE_NAME);
        } catch (ClassNotFoundException e) {
            throw new DataInputException("Niezgodny typ danych w pliku"+FILE_NAME);
        }


    }

    @Override
    public void exportData(Library library) {

        try (
                FileOutputStream fos = new FileOutputStream(FILE_NAME);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                ){

            oos.writeObject(library);
        } catch (FileNotFoundException e) {
            throw new DataExportException("Brak pliku"+FILE_NAME);
        } catch (IOException e) {
            throw new DataExportException("Blad zapisu danych do pliku "+FILE_NAME);

        }

    }
}
