package servletcontroller.clientservice;

import dao.DaoImplementation;
import dao.IDaoInterface;
import dao.MysqlDatabaseOperation;
import inputvalidation.InputValidation;
import inputvalidation.InvalidException;
import pojo.Client;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DeleteClientServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        IDaoInterface<Client, MysqlDatabaseOperation> daoInterface = new DaoImplementation<>();
        MysqlDatabaseOperation<Client> mysqlDatabaseOperation = MysqlDatabaseOperation.getInstance();
        DeleteClientServlet deleteClientServlet = new DeleteClientServlet();
        final String ID = "clientId";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Client client = new Client();

        String id = request.getParameter(ID);
        client.setId(id);

        boolean valid = deleteClientServlet.inputValidation(client, request, response);
        if (valid) {

            Map<String, String> checkData = new HashMap<>();
            checkData.put(ID, id);

            try {
                boolean checkId = daoInterface.isIdPresent(client, mysqlDatabaseOperation, checkData);

                if (checkId) {

                    daoInterface.delete(client, mysqlDatabaseOperation, checkData);
                    out.print("<script>alert('Record deleted successfully!'); location ='retrieveAll';</script>");

                } else {
                    out.print("<script>alert('Id is not present.'); location ='retrieveAll';</script>");
                }
            } catch (Exception e) {
                out.print("<script>alert('" + e + "');location ='retrieveAll';</script>");
            }
        }
    }

    private boolean inputValidation(Client client, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        InputValidation inputValidation = new InputValidation();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            inputValidation.userIdValidator(client.getId());
            return true;
        } catch (InvalidException e) {
            out.print("<script>alert('" + e + "');location ='retrieveAll';</script>");
            return false;
        }
    }
}
