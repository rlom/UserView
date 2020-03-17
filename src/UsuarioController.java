import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import entidades.Usuario;
import net.bootsfaces.utils.FacesMessages;
import repositorios.UsuarioDao;

@ManagedBean
@ViewScoped
public class UsuarioController {

	UsuarioDao dao;
	Usuario usuario;
	List<Usuario> usuarios;

	public UsuarioController() {
		super();
		dao = new UsuarioDao();
		this.usuario = new Usuario();
		this.usuarios = dao.listarTodos();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();

		boolean ok = true;
		if (!StringUtils.isNotEmpty(this.usuario.getNome())) {
			ok = false;
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha um nome válido!"));
		}

		if (!StringUtils.isNotEmpty(this.usuario.getCpf()) || this.usuario.getCpf().length() < 11) {
			ok = false;
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha um CPF válido!"));
		}

		Date dataAtual = Calendar.getInstance().getTime();
		Calendar dataMinima = Calendar.getInstance();
		dataMinima.set(Calendar.DAY_OF_MONTH, 1);
		dataMinima.set(Calendar.MONTH, 1);
		dataMinima.set(Calendar.YEAR, 1900);
		if (this.usuario.getNascimento() == null || this.usuario.getNascimento().after(dataAtual)
				|| this.usuario.getNascimento().before(dataMinima.getTime())) {
			ok = false;
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha uma data de nascimento válido!"));
		}

		if (ok) {
			// SALVAR
			String senha = this.usuario.getCpf().substring(0, 2) + this.usuario.getNascimento().getMonth();

			try {
				if (usuario.getId() == null) {
					MessageDigest digest = MessageDigest.getInstance("SHA-1");
					digest.reset();
					digest.update(senha.getBytes("utf8"));
					senha = String.format("%040x", new BigInteger(1, digest.digest()));
					this.usuario.setSenha(senha);
					System.out.println(senha);
				}
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage("Info",
						"Não foi possível definir uma senha para o usuário! " + "Por favor, tente mais tarde."));
				return "";
			}

			try {
				if (usuario.getId() != null) {
					dao.editar(usuario);
				} else {
					dao.salvar(usuario);
				}

			} catch (Exception e) {
				context.addMessage(null, new FacesMessage("Info", "Não foi possível se comunicar com o banco."));
				return "usuario";
			}

			context.addMessage(null, new FacesMessage("Successful", "Usuário Salvo com Sucesso!"));
			return "usuario?faces-redirect=true";
		}

		return "usuario";
	}

	public String remover() {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			dao.remove(this.usuario);
			this.usuario = new Usuario();
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage("Info", "Não foi possível se comunicar com o banco."));
			return "usuario";
		}

		context.addMessage(null, new FacesMessage("Successful", "Usuário Removido com Sucesso!"));
		return "usuario?faces-redirect=true";
	}
}
