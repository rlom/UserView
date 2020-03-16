import java.math.BigInteger;
import java.security.MessageDigest;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import entidades.Usuario;
import net.bootsfaces.utils.FacesMessages;

@ManagedBean
@ViewScoped
public class UsuarioController {

	Usuario usuario;

	public UsuarioController() {
		super();
		this.usuario = new Usuario();
		this.usuario.setNome("TESTE");
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
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha um nome v�lido!"));
		}

		if (!StringUtils.isNotEmpty(this.usuario.getCpf()) || this.usuario.getCpf().length() < 11) {
			ok = false;
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha um CPF v�lido!"));
		}

		if (this.usuario.getNascimento() == null) {
			ok = false;
			context.addMessage(null, new FacesMessage("Info", "Por Favor, preencha uma data de nascimento v�lido!"));
		}

		if (ok) {
			// SALVAR
			String senha = this.usuario.getCpf().substring(0, 2) + this.usuario.getNascimento().getMonth();

			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-1");
				digest.reset();
				digest.update(senha.getBytes("utf8"));
				senha = String.format("%040x", new BigInteger(1, digest.digest()));
				this.usuario.setSenha(senha);
				System.out.println(senha);
				
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage("Info",
						"N�o foi poss�vel definir uma senha para o usu�rio! " + "Por favor, tente mais tarde."));
				return "usuario";
			}

			context.addMessage(null, new FacesMessage("Successful", "Usu�rio Salvo com Sucesso!"));
			return "usuario?faces-redirect=true";
		}

		return "usuario";
	}

}
