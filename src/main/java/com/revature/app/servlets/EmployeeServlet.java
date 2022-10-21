package com.revature.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.models.Employee;
import com.revature.app.service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

//annotation-based registration
//@WebServlet("/users",loadOnStartup = 2, initParams = @WebInitParam{name = "firstname", value = "james"}
//@WebServlet("/users")
public class EmployeeServlet extends HttpServlet {
    public final ObjectMapper mapper;

    public final EmployeeService es;
    public EmployeeServlet(ObjectMapper mapper, EmployeeService es){
        this.mapper = mapper;
        this.es = es;
    }

// TODO #1 - fix this endpoint. #2 add simple old functionality #3 add login session stuff
// TODO - add if statements checking for query string params...or something. (for adding in old functionality in both servlets)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //LOGIN/LOGOUT USER
        System.out.println(req.getQueryString());
        if(req.getParameter("action").equals("login")) {

            HttpSession sessionC = req.getSession(false);

            if(sessionC != null){
                resp.setStatus(400);
                resp.getWriter().write("Already logged in");
                return;
            }

            HashMap<String, String> info = mapper.readValue(req.getInputStream(), HashMap.class);
            Employee result = es.login(info.get("username"), info.get("password"));
            if (result != null) {
                System.out.println(result);
                HttpSession session = req.getSession();
                session.setAttribute("current-user", result);
                resp.setStatus(200);
                resp.getWriter().write("Successfully logged in");
                return;
            }
            resp.setStatus(400);
            resp.setContentType("application/json");
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Invalid credentials!");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;

        } else if (req.getParameter("action").equals("logout")){

            HttpSession session = req.getSession(false);

            if(session != null){
                System.out.println(session.getAttribute("current-user"));
                session.invalidate();
            } else {
                //this block is for when someone tries to logout without being logged in
                resp.setStatus(400);
                resp.getWriter().write("Not logged in");
                return;
            }

            resp.setStatus(200);
            resp.getWriter().write("Logged out successfully");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //LOGIN/LOGOUT USER
        System.out.println(req.getQueryString());
        if(req.getParameter("action")!=null){
            if(req.getParameter("action").equals("login")) {

                HttpSession sessionC = req.getSession(false);

                if(sessionC != null){
                    resp.setStatus(400);
                    resp.getWriter().write("Already logged in");
                    return;
                }

                HashMap<String, String> info = mapper.readValue(req.getInputStream(), HashMap.class);
                Employee result = es.login(info.get("username"), info.get("password"));
                if (result != null) {
                    System.out.println(result);
                    HttpSession session = req.getSession();
                    session.setAttribute("current-user", result);
                    resp.setStatus(200);
                    resp.getWriter().write("Successfully logged in");
                    return;
                }
                resp.setContentType("application/json");
                HashMap<String, Object> errorMessage = new HashMap<>();
                errorMessage.put("Status code", 400);
                errorMessage.put("Message", "Invalid credentials!");
                errorMessage.put("Timestamp", LocalDateTime.now().toString());
                resp.getWriter().write(mapper.writeValueAsString(errorMessage));
                resp.setStatus(400);
                return;

            } else if (req.getParameter("action").equals("logout")){

                HttpSession session = req.getSession(false);

                if(session != null){
                    System.out.println(session.getAttribute("current-user"));
                    session.invalidate();
                    resp.setStatus(200);
                    resp.getWriter().write("Logged out successfully");
                } else {
                    //this block is for when someone tries to logout without being logged in
                    resp.setStatus(400);
                    resp.getWriter().write("Not logged in");
                }
                return;


            }
        }
        // REGISTER NEW ACCOUNT.
        HttpSession sessionC = req.getSession(false);
        if(sessionC != null){
            resp.setStatus(400);
            resp.getWriter().write("Already logged in");
            return;
        }
        System.out.println("[LOG] - UserServlet received a request at " + LocalDateTime.now());
        HashMap<String,String> newUser = mapper.readValue(req.getInputStream(), HashMap.class);
        //at this point can user newUser as a normal java object. Send to
        //service layer, then dao layer, then database.
        for(String keys: newUser.keySet()){
            System.out.println(newUser.get(keys));
        }
        Employee newAcc = es.register(newUser.get("first"),newUser.get("last"),newUser.get("username"),newUser.get("password"),Boolean.parseBoolean(newUser.get("manager")));
        System.out.println(newAcc);
        if(newAcc != null){
        resp.setStatus(200);
        resp.setContentType("application/json");
        HashMap<String, Object> message = new HashMap<>();
        message.put("Status code", 200);
        message.put("Message", "Created new user!");
        message.put("Timestamp", LocalDateTime.now().toString());
        message.put("New user", newUser);

        HttpSession session = req.getSession();
        session.setAttribute("current-user", newAcc);

        resp.getWriter().write(mapper.writeValueAsString(message));
        return;
        }
        resp.setStatus(409);
        resp.setContentType("application/json");
        HashMap<String, Object> errorMessage = new HashMap<>();
        errorMessage.put("Status code", 409);
        errorMessage.put("Message", "Username was already taken!");
        errorMessage.put("Timestamp", LocalDateTime.now().toString());

        resp.getWriter().write(mapper.writeValueAsString(errorMessage));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);


        if (session == null){
            resp.setStatus(401);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 401);
            errorMessage.put("Message", "Must be logged in as a manager to promote or demote.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        Employee info = (Employee) session.getAttribute("current-user");
        HashMap<String, String> updateInfo = mapper.readValue(req.getInputStream(), HashMap.class);

        if (!info.isManager()){
            resp.setStatus(403);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 403);
            errorMessage.put("Message", "Must be a manager to promote or demote.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        if(!updateInfo.containsKey("employeeId") || updateInfo.get("employeeId").trim().equals("")){

            resp.setStatus(400);
            resp.setContentType("application/json");
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "role change must have valid employeeId field.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        if(req.getParameter("action").equals("promote")){
            Employee res = es.changeRole(Integer.parseInt(updateInfo.get("employeeId")),true);
            resp.setStatus(200);
            resp.setContentType("application/json");
            HashMap<String, Object> message = new HashMap<>();
            message.put("Status code", 200);
            message.put("Message", "Promoted user "+ res.getUsername() +" to manager.");
            message.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(message));
        } else if(req.getParameter("action").equals("demote")){
            Employee res = es.changeRole(Integer.parseInt(updateInfo.get("employeeId")),false);
            if(Integer.parseInt(updateInfo.get("employeeId"))==(info.getEmployeeId())){
                info.setManager(!info.isManager());
                session.setAttribute("current-user", info);
                resp.setStatus(200);
                resp.setContentType("application/json");
                HashMap<String, Object> message = new HashMap<>();
                message.put("Status code", 200);
                message.put("Message", "Demoted yourself "+ " to employee.");
                message.put("Timestamp", LocalDateTime.now().toString());

                resp.getWriter().write(mapper.writeValueAsString(message));
            } else {
                resp.setStatus(200);
                resp.setContentType("application/json");
                HashMap<String, Object> message = new HashMap<>();
                message.put("Status code", 200);
                message.put("Message", "Demoted user "+ res.getUsername() +" to employee.");
                message.put("Timestamp", LocalDateTime.now().toString());

                resp.getWriter().write(mapper.writeValueAsString(message));
            }
        } else{
            resp.setStatus(400);
            resp.setContentType("application/json");
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Action invalid.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }


}
