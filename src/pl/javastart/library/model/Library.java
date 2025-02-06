package pl.javastart.library.model;

import pl.javastart.library.exception.PublicationAlreadyExistsException;
import pl.javastart.library.exception.UserAlreadyExistsEsception;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {





private Map<String,Publication>publcations=new HashMap<>();
private Map<String,LibraryUser>users=new HashMap<>();

    public Map<String, Publication> getPublcations() {
        return publcations;
    }



    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator){
        List<Publication> list = new ArrayList<>(publcations.values());
        list.sort(comparator);
        return list;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator){
        ArrayList<LibraryUser> list = new ArrayList<>(users.values());
        list.sort(comparator);
        return list;
    }

    public void addPublication(Publication publication){
            if(publcations.containsKey(publication.getTitle())){
                throw new PublicationAlreadyExistsException("Publikcaja o tym tytule już istnieje");
            };

           publcations.put(publication.getTitle(),publication);

    }

    public void addUser(LibraryUser user){
        if(users.containsKey(user.getPesel())){
            throw new UserAlreadyExistsEsception("Użytkownik o tym peselu już istnieje");
        }
        users.put(user.getPesel(),user);
    }

    public boolean removePublication(Publication pub){
        if(publcations.containsValue(pub)){
            publcations.remove(pub.getTitle());
            return true;
        }else {
            return false;
        }



    }

}
