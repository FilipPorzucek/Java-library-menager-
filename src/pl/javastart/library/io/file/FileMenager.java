package pl.javastart.library.io.file;

import pl.javastart.library.model.Library;

public interface FileMenager {
    Library importData();
    void exportData(Library library);

}
