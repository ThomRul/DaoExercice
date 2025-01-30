package com.example.jdbci;

import com.example.jdbci.dao.DAOFactory;
import com.example.jdbci.models.Address;
import com.example.jdbci.models.Book;
import com.example.jdbci.models.Borrowing;
import com.example.jdbci.models.User;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        ConnectionFactory.createDropTable();
        doCrudOnUser();
        doCrudOnAddress();
        doCrudOnBook();
        doCrudOnBorrowing();
    }
    public static void doCrudOnUser(){
        int id = 1;
        // region INSERT
        DAOFactory.user.insert(new User("james","jamesBond007.msi6.com"));
        DAOFactory.user.insert(new User("tom","tomJedusor@mechants.com"));
        System.out.println(DAOFactory.user.getAll());
        // endregion

        // region UPDATE
        User userToUpdate = new User("bond","bond007@msi.en");
        boolean updated = DAOFactory.user.update(id,userToUpdate);
        System.out.println(updated ? "User updated" : "User not updated");
        System.out.println(DAOFactory.user.getAll());
        // endregion

        // region GETONE
        User u = DAOFactory.user.getOne(id);
        if(u != null){
            System.out.println("user : " + u.getName());
        }
        // endregion

        //region DELETE
        boolean deleted = DAOFactory.user.delete(id);
        System.out.println(deleted ? "User deleted" : "User not deleted");
        System.out.println(DAOFactory.user.getAll());
        // endregion

    }

    public static void doCrudOnAddress(){
        // region INSERT
        int id = 2;
        if(DAOFactory.user.getOne(id) != null){
            DAOFactory.address.insert(new Address("rue a", "ville b",2));
        }
        System.out.println(DAOFactory.address.getAll());
        // endregion

        // region GETONE
        Address address = DAOFactory.address.getOne(1);
        if(address != null){
            System.out.println("Address : " + address.getStreet() + " " + address.getCity());
        }
        // endregion

        // region UPDATE
        if(DAOFactory.user.getOne(id) != null){
            boolean updated = DAOFactory.address.update(1, new Address("rue du bonheur", "ville du paradis",2));
            System.out.println(updated ? "Address updated" : "Address not updated");
        }
        System.out.println(DAOFactory.address.getAll());
        // endregion

        // region DELETE
        boolean deleted = DAOFactory.address.delete(1);
        System.out.println(deleted ? "Address deleted" : "Address not deleted");
        //endregion

        // region GETALL
        System.out.println(DAOFactory.address.getAll());
        // endregion


    }

    public static void doCrudOnBook() {
        // region INSERT
        System.out.println("Insertion de livres");
        DAOFactory.book.insert(new Book("Le Petit Prince de lu", "Antoine de Saint-Exupéry", "9783140464079"));
        DAOFactory.book.insert(new Book("1984", "George Orwell", "9780451524935"));
        DAOFactory.book.insert(new Book("Le silence des agneaux", "Frank Herbert", "9780441172719"));
        System.out.println("Liste des livres");
        System.out.println(DAOFactory.book.getAll());
        // endregion

        // region FIND_BY_AUTHOR
        System.out.println("Recherche par auteur");
        String authorToFind = "Orwell";
        System.out.println("Livres de " + authorToFind + " :");
        System.out.println(DAOFactory.book.findByAuthor(authorToFind));
        // endregion


        // region UPDATE
        System.out.println("Mise à jour");
        Book bookToUpdate = new Book("1984 le retour", "George Orwell", "9780451524935");
        bookToUpdate.setAvailable(false);
        boolean updated = DAOFactory.book.update(2, bookToUpdate);
        System.out.println(updated ? "Livre mis à jour" : "Livre non mis à jour");
        System.out.println(DAOFactory.book.getAll());
        // endregion

        // region FIND_AVAILABLE
        System.out.println("Livres disponibles");
        System.out.println(DAOFactory.book.findAvailableBooks());
        // endregion
    }

    public static void doCrudOnBorrowing() {
        // region INSERT
        System.out.println("Création d'un emprunt");
        Borrowing newBorrowing = new Borrowing(2, 1);
        DAOFactory.borrowing.insert(newBorrowing);
        System.out.println("liste des livres empruntés");
        System.out.println(DAOFactory.borrowing.getAll());
        // endregion

        // region FIND_BY_USER
        System.out.println("Emprunts de l'utilisateur 2 :");
        System.out.println(DAOFactory.borrowing.findByUser(2));
        // endregion

        // region UPDATE (retour d'un livre)
        System.out.println("Retour d'un livre");
        Borrowing borrowingToReturn = DAOFactory.borrowing.getOne(1);
        if (borrowingToReturn != null) {
            borrowingToReturn.setReturnDate(LocalDateTime.now());
            borrowingToReturn.setReturned(true);
            boolean returned = DAOFactory.borrowing.update(1, borrowingToReturn);
            System.out.println(returned ? "Livre retourné avec succès" : "Échec du retour");
        }
        // endregion

        // region FIND_OVERDUE
        System.out.println("Livres en retard");
        System.out.println(DAOFactory.borrowing.findOverdueBooks());
        // endregion

        // region VERIF
        System.out.println("Etat des emprunts");
        System.out.println(DAOFactory.borrowing.getAll());
        System.out.println("Etat des livres");
        System.out.println(DAOFactory.book.getAll());
        // endregion
    }
}