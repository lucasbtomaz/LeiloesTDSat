
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void cadastrarProduto(ProdutosDTO produto) throws SQLException {

        // Estabelece a conexão com o banco
        conn = new conectaDAO().connectDB();

        try {
            String sql = "INSERT INTO produtos (nome, valor) VALUES (?, ?)";
            prep = conn.prepareStatement(sql);

            prep.setString(1, produto.getNome());
            prep.setDouble(2, produto.getValor());

            prep.executeUpdate();

            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            conn.close();

        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() throws SQLException {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = new conectaDAO().connectDB(); PreparedStatement prep = conn.prepareStatement(sql); ResultSet resultset = prep.executeQuery()) {

            // Itera pelos resultados da consulta e adiciona na lista
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                listagem.add(produto);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }

        return listagem;
    }

    public void venderProduto(ProdutosDTO produto) {
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";

        try (Connection conn = new conectaDAO().connectDB(); PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, "Vendido");
            prep.setInt(2, produto.getId());

            int rowsAffected = prep.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vender o produto: " + e.getMessage());
        }

    }

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> listaVendidos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE status = ?";

        try (Connection conn = new conectaDAO().connectDB(); PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, "Vendido"); 
            ResultSet resultSet = prep.executeQuery();


            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));

                listaVendidos.add(produto);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos: " + e.getMessage());
        }

        return listaVendidos;
    }
}
