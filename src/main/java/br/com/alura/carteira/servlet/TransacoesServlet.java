package br.com.alura.carteira.servlet;

import br.com.alura.carteira.dao.TransacaoDao;
import br.com.alura.carteira.factory.ConnectionFactory;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/transacoes")
public class TransacoesServlet extends HttpServlet {

    private TransacaoDao dao;

    public TransacoesServlet() {
        this.dao = new TransacaoDao(new ConnectionFactory().getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("transacoes", dao.listar());

        req.getRequestDispatcher("WEB-INF/jsp/transacoes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String ticker = req.getParameter("ticker");
            LocalDate data = LocalDate.parse(req.getParameter("data"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            BigDecimal preco = new BigDecimal(req.getParameter("preco").replace(",", "."));
            int quantidade = Integer.parseInt(req.getParameter("quantidade"));
            TipoTransacao tipo = TipoTransacao.valueOf(req.getParameter("tipo"));

            Transacao transacao = new Transacao(ticker, data, preco, quantidade, tipo);

            dao.cadastrar(transacao);

            resp.sendRedirect("transacoes");
        } catch (NumberFormatException e) {
            // TODO: handle exception
            resp.sendRedirect("transacoes?erro=campo invalido!");
        }
    }
}
