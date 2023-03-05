package com.damian.hibernate.repositories;

import com.damian.hibernate.entity.Customer;
import com.damian.hibernate.entity.Item;
import com.damian.hibernate.util.FactoryConfiguration;
import javafx.scene.control.Alert;
import org.hibernate.Session;

import java.util.List;

public class ItemRepo {
    private Session session;
    public ItemRepo(){
        session = FactoryConfiguration.getInstance().getSession();
    }
    public boolean add(Item item){
        session.beginTransaction();
        try {
            session.save(item);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in ItemREPO! "+e.getLocalizedMessage());
            a.show();
        }
        return false;
    }
    public boolean update(Item item){
        session.beginTransaction();
        try {
            session.update(item);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in ItemREPO!");
            a.show();
        }
        return false;
    }
    public boolean delete(String id){
        session.beginTransaction();
        try {
            Item item = session.get(Item.class, id);
            session.delete(item);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in ItemREPO!"+e.getLocalizedMessage());
            a.show();
        }
        return false;
    }
    public Item findById(String id){
        session.beginTransaction();
        try {
            Item item = session.get(Item.class, id);
            session.close();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in ItemREPO!");
            a.show();
        }
        return null;
    }
    public List<Item> getAll (){
        session.beginTransaction();
        try {
            //select all from customer table
           return session.createQuery("from Item").list();

        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in ItemREPO!");
            a.show();
        }
        return null;
    }
}
