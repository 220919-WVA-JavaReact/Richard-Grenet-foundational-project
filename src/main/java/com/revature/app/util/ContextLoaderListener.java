package com.revature.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.service.EmployeeService;
import com.revature.app.service.TicketService;
import com.revature.app.servlets.EmployeeServlet;
import com.revature.app.servlets.TicketServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import java.time.LocalDateTime;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[LOG] - The servlet context was initialized at " + LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        EmployeeService es = new EmployeeService();
        EmployeeServlet employeeServlet = new EmployeeServlet(mapper,es);
        TicketService ts = new TicketService();
        TicketServlet ticketServlet = new TicketServlet(mapper,ts);

        ServletContext context = sce.getServletContext();
        ServletRegistration.Dynamic registeredServlet = context.addServlet("EmployeeServlet", employeeServlet);

        registeredServlet.addMapping("/users");
        context.addServlet("TicketServlet", ticketServlet).addMapping("/tickets");
        registeredServlet.setLoadOnStartup(2);
        registeredServlet.setInitParameter("user-servlet-key", "user-servlet-value");
        registeredServlet.setInitParameter("another-param", "another-value");

        //context.addServlet("FlashcardServlet", flashcardServlet).addMapping("/flashcards/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[LOG] - The servlet context was destroyed at " + LocalDateTime.now());
    }
}
