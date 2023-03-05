package com.damian.hibernate.repositories;

import com.damian.hibernate.entity.Customer;
import com.damian.hibernate.util.FactoryConfiguration;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerRepo {
    private  Session session;
    public CustomerRepo(){
        session = FactoryConfiguration.getInstance().getSession();
    }
    public boolean add(Customer customer){
        session.beginTransaction();
        try {
            session.save(customer);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in CustomerREPO! "+e.getLocalizedMessage());
            a.show();
        }
        return false;
    }
    public boolean update(Customer customer){
        session.beginTransaction();
        try {
            session.update(customer);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in CustomerREPO!");
            a.show();
        }
        return false;
    }
    public boolean delete(String id){
        session.beginTransaction();
        try {
            Customer customer = session.get(Customer.class, id);
            session.delete(customer);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in CustomerREPO!"+e.getLocalizedMessage());
            a.show();
        }
        return false;
    }
    public Customer findById(String id){
        session.beginTransaction();
        try {
            Customer customer = session.get(Customer.class,id);
            session.close();
            return customer;
        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in CustomerREPO!");
            a.show();
        }
        return null;
    }
    public  List<Customer> getAll (){
        session.beginTransaction();
        try {
            //select all from customer table
           return session.createQuery("from Customer").list();

        } catch (Exception e) {
            session.getTransaction().rollback();
            Alert a = new Alert(Alert.AlertType.ERROR,"An error occurred in CustomerREPO!");
            a.show();
        }
        return null;
    }

}
