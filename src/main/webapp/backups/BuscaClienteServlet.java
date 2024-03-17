import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@WebServlet("/buscarCliente")
public class BuscaClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Map<String, Cliente> clientes = new HashMap<>();

    public void init() throws ServletException {
        // Carrega os clientes do arquivo XML
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Cliente.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cliente");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String cpf = eElement.getElementsByTagName("cpf").item(0).getTextContent();
                    String nome = eElement.getElementsByTagName("nome").item(0).getTextContent();
                    clientes.put(cpf, new Cliente(cpf, nome));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cpf = request.getParameter("cpf");

        // Busca o cliente no HashMap
        Cliente cliente = clientes.get(cpf);

        // Configura a resposta para HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Resultado da Busca</title>");
        out.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">");
        out.println("<style>");
        out.println("body {");
        out.println("    padding: 20px;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<h1 class=\"mt-5\">Resultado da Busca</h1>");
        if (cliente != null) {
            out.println("<div class=\"alert alert-success mt-4\" role=\"alert\">");
            out.println("<h2>Cliente Encontrado:</h2>");
            out.println("<p>Nome: " + cliente.getNome() + "</p>");
            out.println("</div>");
        } else {
            out.println("<div class=\"alert alert-danger mt-4\" role=\"alert\">");
            out.println("<h2>Cliente não encontrado.</h2>");
            out.println("</div>");
        }
        out.println("<a href=\"/BuscaClienteProject/\" class=\"btn btn-primary mt-4\">Voltar</a>");
        out.println("<br>");
        out.println("<hr>");
        out.println("<h6 class=\"text-center\">Desenvolvido por Grupo 40 - ADS - Unifor</h6>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
