package repositorios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entidades.Usuario;

public class UsuarioDao {
	public void salvar(Usuario usuario) throws Exception {

		String sql = "insert into usuario " + "(nome,cpf,senha,dataNascimento) " + "values (?,?,?,?)";

		try (Connection con = new ConnectionFactory().getConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getCpf());
			stmt.setString(3, usuario.getSenha());

			Calendar date = Calendar.getInstance();
			date.setTime(usuario.getNascimento());
			java.sql.Date dataParaGravar = new java.sql.Date(date.getTimeInMillis());
			stmt.setDate(4, dataParaGravar);

			stmt.execute();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("Não foi possível realizar a Conexão!");
			throw new Exception("Não foi possível realizar a Conexão!");
		}
	}

	public void editar(Usuario usuario) throws Exception {
		String sql = "update usuario set" + " nome=?, cpf=?, senha=?, dataNascimento=? where id=?";

		try (Connection con = new ConnectionFactory().getConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);

			
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getCpf());
			stmt.setString(3, usuario.getSenha());

			Calendar date = Calendar.getInstance();
			date.setTime(usuario.getNascimento());
			java.sql.Date dataParaGravar = new java.sql.Date(date.getTimeInMillis());
			stmt.setDate(4, dataParaGravar);
			
			stmt.setLong(5, usuario.getId());

			stmt.execute();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("Não foi possível realizar a Conexão!");
			throw new Exception("Não foi possível realizar a Conexão!");
		}
	}

	public List<Usuario> listarTodos() {

		try (Connection con = new ConnectionFactory().getConnection()) {
			PreparedStatement stmt = con.prepareStatement("select * from usuario");

			ResultSet rs = stmt.executeQuery();

			List<Usuario> usuarios = new ArrayList<Usuario>();

			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getLong("id"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setNascimento(rs.getDate("dataNascimento"));
				usuarios.add(usuario);
			}

			stmt.execute();
			stmt.close();

			return usuarios;

		} catch (SQLException e) {
			System.out.println("Não foi possível realizar a Conexão!");
			return new ArrayList<Usuario>();
		}
	}

	public void remove(Usuario usuario) {
		try (Connection con = new ConnectionFactory().getConnection()) {
			PreparedStatement stmt = con.prepareStatement("delete from usuario where id=?");
			stmt.setLong(1, usuario.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
