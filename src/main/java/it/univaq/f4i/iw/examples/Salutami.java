package it.univaq.f4i.iw.examples;

import it.univaq.f4i.iw.framework.utils.ServletHelpers;
import it.univaq.f4i.iw.framework.result.HTMLResult;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 */
public class Salutami extends HttpServlet {

    private LocalDateTime startup;

    private void action_saluta_noto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("n");
        //qualche controllo di sicurezza (ridondante)
        //some (redundant) security check
        if (nome == null || nome.isEmpty()) {
            nome = "sconosciuto";
        } else {
            //qui dovremmo "sanitizzare" il parametro            
            //ad esempio con https://github.com/OWASP/java-html-sanitizer
            //ma usiamo il nostro sanitizzatore "di base" direttamente in fase di output
            //here we should "sanitize" the parameter
            //for example using https://github.com/OWASP/java-html-sanitizer
            //but we use our "basic" sanitizer directly in the output statement     
        }

        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Salutami!");
        result.setBody("<p>Hello, " + HTMLResult.sanitizeHTMLOutput(nome) + "!</p>");
        result.activate(request, response);
    }

    private void action_saluta_anonimo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Salutami!");
        result.appendToBody("<p>Hello!</p>");
        result.appendToBody("<form method=\"get\" action=\"salutami\">");
        result.appendToBody("<p>What is your name?");
        result.appendToBody("<input type=\"text\" name=\"n\"/>");
        result.appendToBody("<input type=\"submit\" name=\"s\" value=\"Hello!\"/>");
        result.appendToBody("</p>");
        result.appendToBody("</form>");
        result.appendToBody("<p><small>Current timestamp is " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</small></p>");
        result.appendToBody("<p><small>I'm greeting all users since " + startup.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</small></p>");
        result.activate(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String n = request.getParameter("n");
            if (n == null || n.isBlank()) {
                action_saluta_anonimo(request, response);
            } else {
                action_saluta_noto(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            ServletHelpers.handleError(request, response, getServletContext());
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        startup = LocalDateTime.now();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "A kind servlet";

    }// </editor-fold>
}
