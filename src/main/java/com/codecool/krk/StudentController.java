package com.codecool.krk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class StudentController implements HttpHandler {

    private StudentsDataBase dataBase;

    public StudentController() {
        this.dataBase = new StudentsDataBase();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("faaaaaaaaaaaaaaaaaaaaa" + dataBase.getStudentsList());
        String method = httpExchange.getRequestMethod();
        String response = "";
        System.out.println(dataBase.getStudentsList().size());

        if (method.equals("POST") && parsePath(httpExchange).length <=3) {
            add(httpExchange);
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);
            //httpExchange.getResponseHeaders().add("Location", "http://localhost:8000/");
            httpRedirectTo("/students", httpExchange);
        }

        if (method.equals("POST") && parsePath(httpExchange).length > 3 ) {

            switch (parsePath(httpExchange)[2]) {

                case "edit":
                    edit(parsePath(httpExchange)[3],httpExchange);
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
                    JtwigModel model = JtwigModel.newModel();
                    response = template.render(model);
                    break;

                case "delete":
                    delete(parsePath(httpExchange)[3]);
                    template = JtwigTemplate.classpathTemplate("templates/index.twig");
                    model = JtwigModel.newModel();
                    response = template.render(model);
                    break;
            }
            httpRedirectTo("/students", httpExchange);


        }

        if (method.equals("GET") && parsePath(httpExchange).length <=2) {
            System.out.println("weszlo w get");
            System.out.println("parsePath(httpExchange)[1] = " + parsePath(httpExchange)[1]);

            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
            JtwigModel model = JtwigModel.newModel();
            model.with("students", dataBase.getStudentsList());
            response = template.render(model);
        }

        if (method.equals("GET") && parsePath(httpExchange).length > 2) {
            switch (parsePath(httpExchange)[2]) {

                case "edit": {
                    System.out.println(parsePath(httpExchange)[2]);
                    System.out.println(parsePath(httpExchange)[3]);
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/edit.twig");
                    JtwigModel model = JtwigModel.newModel();
                    model.with("student", this.dataBase.getStudentById(parsePath(httpExchange)[3]));
                    response = template.render(model);
                    break;
                }
                case "add": {
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add.twig");
                    JtwigModel model = JtwigModel.newModel();
                    response = template.render(model);
                    break;
                }
                case "delete": {
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/delete.twig");
                    JtwigModel model = JtwigModel.newModel();
                    model.with("student", this.dataBase.getStudentById(parsePath(httpExchange)[3]));
                    response = template.render(model);

                    break;
                }

                default:
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
                    JtwigModel model = JtwigModel.newModel();
                    response = template.render(model);
                    break;
            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String[] parsePath(HttpExchange httpExchange) {
        String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
        return pathArray;
    }

    private void edit(String id, HttpExchange httpExchange) throws IOException {
        Student student = dataBase.getStudentById(id);
        System.out.println("student.getId() = " + student.getId());

        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "UTF8");
        BufferedReader br = new BufferedReader(isr);
        String inputs = br.readLine();

        System.out.println("br.readLine() = " + inputs);

        Map<String, String> map = parseInputs(inputs);
        student.setFirstName(map.get("firstName"));
        student.setLastName(map.get("lastName"));
        student.setAge(map.get("age"));
    }

    private static Map<String, String> parseInputs(String inputs) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String [] pairs = inputs.split("&");
        for (String element : pairs) {
            String [] keyValue = element.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF8");
            map.put(keyValue[0], value);
        }
        return map;
    }


    private void add(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "UTF8");
        BufferedReader br = new BufferedReader(isr);
        String inputs = br.readLine();
        Map<String, String> map = parseInputs(inputs);

        Student student = new Student(map.get("firstName"), map.get("lastName"), map.get("age"));


        dataBase.getStudentsList().add(student);
    }

    private void delete(String id) {

        System.out.println("DB PRZED = "+ dataBase.getStudentsList());
        dataBase.deleteStudentById(id);
        System.out.println(id);
        System.out.println("DB PO = "+ dataBase.getStudentsList());
    }

    private void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(301, -1);
    }
}
