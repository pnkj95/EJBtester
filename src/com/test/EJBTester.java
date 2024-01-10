package com.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.demo.entitybean.Book;
import com.demo.entitybean.LibraryPersistentBeanRemote;
import com.demo.stateful.LibraryStatefulBeanRemote;
import com.demo.stateless.LibrarySessionBeanRemote;
 
 
public class EJBTester {
   BufferedReader brConsoleReader = null;
   Properties props;
   InitialContext ctx;
   {
      props = new Properties();
      try {
         props.load(new FileInputStream("jndi.properties"));
         System.out.println("file is loaded");
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      try {
    	  System.out.println(props.getProperty("java.naming.provider.url"));
         ctx = new InitialContext(props);   
      } catch (NamingException ex) {
         ex.printStackTrace();
      }
      brConsoleReader = 
      new BufferedReader(new InputStreamReader(System.in));
      
   }
   
   public static void main(String[] args) {
 
      EJBTester ejbTester = new EJBTester();
//      System.out.println("********************Calling Stateless EJB******************");
//      ejbTester.testStatelessEjb();
//      System.out.println("********************END Stateless EJB******************");
//      System.out.println("********************Calling Stateful EJB******************");
//      ejbTester.testStatefulEjb();
//      System.out.println("********************Stateful EJB - END******************");
      System.out.println("********************Calling Entity EJB******************");
      ejbTester.testMessageBeanEjb();
      System.out.println("********************Entity EJB - END******************");
   }
   private void showGUI() {
      System.out.println("**********************");
      System.out.println("Welcome to Book Store");
      System.out.println("**********************");
      System.out.print("Options \n1. Add Book\n2. Exit \nEnter Choice: ");
   }
   private void testStatelessEjb() {
      try {
         int choice = 1; 
         
       //lookup for jar file- test.jar is deployed on jboss
         LibrarySessionBeanRemote libraryBeanWithJar = 
                 (LibrarySessionBeanRemote)ctx.lookup("ejb:/test/LibrarySessionBean!com.demo.stateless.LibrarySessionBeanRemote");
         //lookup for ear file
         LibrarySessionBeanRemote libraryBean = 
         (LibrarySessionBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibrarySessionBean!com.demo.stateless.LibrarySessionBeanRemote");
         while (choice != 2) {
            String bookName;
            showGUI();
            String strChoice = brConsoleReader.readLine();
            choice = Integer.parseInt(strChoice);
            if (choice == 1) {
               System.out.print("Enter book name: ");
               bookName = brConsoleReader.readLine();                    
               libraryBean.addBook(bookName);          
            }else if (choice == 2) {
               break;
            }
         }
         List<String> booksList = libraryBean.getBooks();
         System.out.println("Book(s) entered so far: " + booksList.size());
         for (int i = 0; i < booksList.size(); ++i) {
            System.out.println((i+1)+". " + booksList.get(i));
         }
         LibrarySessionBeanRemote libraryBean1 = 
         (LibrarySessionBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibrarySessionBean!com.demo.stateless.LibrarySessionBeanRemote");
         List<String> booksList1 = libraryBean1.getBooks();
         System.out.println(
            "***Using second lookup to get library stateless object***");
         System.out.println(
            "Book(s) entered so far: " + booksList1.size());
         for (int i = 0; i < booksList1.size(); ++i) {
            System.out.println((i+1)+". " + booksList1.get(i));
         }
      } catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
      } finally {
         try {
            if(brConsoleReader !=null) {
               brConsoleReader.close();
            }
         } catch (IOException ex) {
            System.out.println(ex.getMessage());
         }
      }
   }  
   
   
   private void testStatefulEjb() {
	      try {
	         int choice = 1; 
	         
	         //lookup for ear file
	         LibraryStatefulBeanRemote libraryBean = 
	         (LibraryStatefulBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibraryStatefulBean!com.demo.stateful.LibraryStatefulBeanRemote?stateful");
	         while (choice != 2) {
	            String bookName;
	            showGUI();
	            String strChoice = brConsoleReader.readLine();
	            choice = Integer.parseInt(strChoice);
	            if (choice == 1) {
	               System.out.print("Enter book name: ");
	               bookName = brConsoleReader.readLine();                    
	               libraryBean.addBook(bookName);          
	            }else if (choice == 2) {
	               break;
	            }
	         }
	         List<String> booksList = libraryBean.getBooks();
	         System.out.println("Book(s) entered so far: " + booksList.size());
	         for (int i = 0; i < booksList.size(); ++i) {
	            System.out.println((i+1)+". " + booksList.get(i));
	         }
	         LibraryStatefulBeanRemote libraryBean1 = 
	         (LibraryStatefulBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibraryStatefulBean!com.demo.stateful.LibraryStatefulBeanRemote?stateful");
	         List<String> booksList1 = libraryBean1.getBooks();
	         System.out.println(
	            "***Using second lookup to get library stateless object***");
	         System.out.println(
	            "Book(s) entered so far: " + booksList1.size());
	         for (int i = 0; i < booksList1.size(); ++i) {
	            System.out.println((i+1)+". " + booksList1.get(i));
	         }
	      } catch (Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      } finally {
	         try {
	            if(brConsoleReader !=null) {
	               brConsoleReader.close();
	            }
	         } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	         }
	      }
	   }  
   
   private void testEntityEjb() {
	      try {
	         int choice = 1; 
	         
	         //lookup for ear file
	         LibraryPersistentBeanRemote libraryBean = 
	         (LibraryPersistentBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibraryPersistentBean!com.demo.entitybean.LibraryPersistentBeanRemote");
	         while (choice != 2) {
	            String bookName;
	            showGUI();
	            String strChoice = brConsoleReader.readLine();
	            choice = Integer.parseInt(strChoice);
	            if (choice == 1) {
	               System.out.print("Enter book name: ");
	               bookName = brConsoleReader.readLine();                    
	               Book book = new Book();
	               book.setName(bookName);
	               libraryBean.addBook(book);         
	            }else if (choice == 2) {
	               break;
	            }
	         }
	         List<Book> booksList = libraryBean.getBooks();
	         System.out.println("Book(s) entered so far: " + booksList.size());
	         for (int i = 0; i < booksList.size(); ++i) {
	            System.out.println((i+1)+". " + booksList.get(i).getName());
	         }
	         LibraryPersistentBeanRemote libraryBean1 = 
	         (LibraryPersistentBeanRemote)ctx.lookup("ejb:ejb-moduleEAR/ejb-module/LibraryPersistentBean!com.demo.entitybean.LibraryPersistentBeanRemote");
	         List<Book> booksList1 = libraryBean1.getBooks();
	         System.out.println(
	            "***Using second lookup to get library entity bean object***");
	         System.out.println(
	            "Book(s) entered so far: " + booksList1.size());
	         for (int i = 0; i < booksList1.size(); ++i) {
	            System.out.println((i+1)+". " + booksList1.get(i).getName());
	         }
	      } catch (Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      } finally {
	         try {
	            if(brConsoleReader !=null) {
	               brConsoleReader.close();
	            }
	         } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	         }
	      }
	   }  
   
   private void testMessageBeanEjb() {
	   
	      try {
	         int choice = 1; 
	         Queue queue = (Queue) ctx.lookup("jms/queue/TestQueue");
	         QueueConnectionFactory factory =
	         (QueueConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
	         QueueConnection connection =  factory.createQueueConnection("jmsuser","jmsuser");
	         System.out.println("Creating JMS Session...");
	         QueueSession session = 
	         connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
	         QueueSender sender = session.createSender(queue);
	         System.out.println("Got sender from session...");
	         QueueReceiver receiver = session.createReceiver(queue);
	         System.out.println("Creating Text Message..");
	         
	         TextMessage message = session.createTextMessage("Test message with TextMessage class");
	 
	         while (choice != 2) {
	            String bookName;
	            showGUI();
	            String strChoice = brConsoleReader.readLine();
	            choice = Integer.parseInt(strChoice);
	            if (choice == 1) {
	               System.out.print("Enter book name: ");
	               bookName = brConsoleReader.readLine();
	               Book book = new Book();
	               book.setName(bookName);
	               ObjectMessage objectMessage = 
	                  session.createObjectMessage(book);
	               //sender.send(objectMessage); 
	               sender.send(message);
	            } else if (choice == 2) {
	               break;
	            }
	         }
	         
	         System.out.println("The queue size is : "+getQueueSize(session, queue));
	               
	      } catch (Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }finally {
	         try {
	            if(brConsoleReader !=null) {
	               brConsoleReader.close();
	            }
	         } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	         }
	      }
	   }  
   
   private int getQueueSize(Session session, Queue queue) {
	    int count = 0;
	    try {
	        QueueBrowser browser = session.createBrowser(queue);
	        Enumeration elems = browser.getEnumeration();
	        while (elems.hasMoreElements()) {
	            elems.nextElement();
	            count++;
	        }
	    } catch (JMSException ex) {
	        ex.printStackTrace();
	    }
	    return count;
	}
}
