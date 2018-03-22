package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class PacienteController {

	private Paciente pac = new Paciente();
	private List<Paciente> todosPacs;
	
	
	@Autowired 
	private PacienteRepository pac_rep;	
		
	private Lesao lesaoSelecionada;	
	
	private List<Lesao> pacLesoes = new ArrayList<Lesao>();
	private List<Imagem> pacImagens = new ArrayList<Imagem>();	
	private Lesao lesao = new Lesao ();	
	private Imagem img = new Imagem ();
	private List<String> imgsPath; 
	
	
	
	public List<String> completarEstados (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] estados = new String[] {"ACRE","ALAGOAS","AMAPÁ","AMAZONAS","BAHIA","CEARÁ","DISTRITO FEDERAL","ESPÍRITO SANTO","GOIÁS","MARANHÃO",
				"MATO GROSSO","MATO GROSSO DO SUL","MINAS GERAIS","PARÁ","PARAÍBA","PARANÁ","PERNAMBUCO","PIAUÍ","RIO DE JANEIRO",
				"RIO GRANDE DO NORTE","RIO GRANDE DO SUL","RONDÔNIA","RORAIMA","SANTA CATARINA","SÃO PAULO","SERGIPE","TOCANTINS"};
		
		for (String s : estados){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}  
	
	public List<String> completarOrigemFamiliar (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] estados = new String[] {"ALEMANHA", "POMERANO", "PORTUGAL", "ITÁLIA", "BRASIL", "ESTADOS UNIDOS", "ESPANHA", "MÉXICO", "NORUEGA", "FRANÇA",
				"OUTRO", "NÃO SABE", "NAO SABE", "INGLATERRA", "POLÔNIA", "CANADÁ", "ARGENTINA", "URUGUAI", "CHILE", "EQUADOR", "ÁRABE", "EUROPA", "ASIA",
				"CHINA", "JAPÃO", "CORÉIA", "HUNGRIA", "DINAMARCA", "COLÔMBIA", "LATINA", "AFRICA", "PARAGUAI", "VENEZUELA"};
		
		for (String s : estados){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}
	
	public List<String> completarDiagnosticoLesao (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] diag = new String[] {"CARCINOMA BASOCELULAR", "CARCINOMA ESPINOCELULAR", "DOENÇA DE BOWN", "CEROTOSE ACTÍNICA"};
		
		for (String s : diag){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}
	
	public void inserirLesao () {		
		System.out.println(lesao.getDiagnostico_clinico() +" "+lesao.getRegiao()+" "+lesao.getDiametro_maior()+" "+lesao.getDiametro_menor());
		FacesContext context = FacesContext.getCurrentInstance();		
		
		if (lesao.getRegiao() != null && lesao.getDiagnostico_clinico() != null && lesao.getProcedimento() != null) {
			lesao.setImagens(pacImagens);						
			pacLesoes.add(lesao);			
			lesao = new Lesao ();		
			pacImagens = new ArrayList<Imagem>();			
			context.addMessage(null, new FacesMessage("Lesão adicionada com sucesso."));			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você não preencheu todos os campos obrigatorios de uma lesão. Verifique se esqueceu algum campo e tente novamente!", "  "));
		}
		lesao = null;
		lesao = new Lesao();
	}
	
	private void printImgs (List<Imagem> imgs) {
		System.out.println("Imprimindo imagens");
		for (Imagem img: imgs) {
			System.out.println(img.getPath());
		}
	}
	
	private void printLesoes (List<Lesao> lesoes) {
		System.out.println("Imprimindo lesoes");
		for (Lesao les: lesoes) {
			System.out.println(les.getRegiao() + " - " + les.getDiagnostico_clinico());
			printImgs(les.getImagens());
		}
	}
	
	public void inserirImagemLista (FileUploadEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			UploadedFile arq = event.getFile();		
			InputStream in = new BufferedInputStream(arq.getInputstream());
			String pathImg = (new Date().getTime())+ "_" + arq.getFileName();
			File file = new File("src/main/webapp/dashboard/imgLesoes/" + pathImg);
    		FileOutputStream fout = new FileOutputStream(file);

		while (in.available() != 0) {
			fout.write(in.read());
		}
			fout.close();			
			context.addMessage(null, new FacesMessage("Imagem adicionada com sucesso."));
			
			img.setPath(pathImg);
			pacImagens.add(img);
			img = new Imagem ();
			System.out.println("Adicionando " + pathImg);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}		
	}	
	
	/*public List<String> pegaImagensCadastro (){
		List<String> imgsPath = new ArrayList<String>();		
		
		System.out.println("FUNCAO DE PEGAR IMAGENS. Lesão: " + lesao.getRegiao() );
		
		for (Imagem img : lesaoSelecionada.getImagens()) {
			System.out.println("Adicionando o path: " + img.getPath());
			imgsPath.add(img.getPath());
		}	
		
		return imgsPath;
	}	*/
	
	
	// VERIFICAR SE A IMAGEM ESTA NA LISTA, SE NAO INSERIR. DEPOIS TRATAR A EXCLUSÃO
	public void copiaLesaoSelecionada () {		
		imgsPath = new ArrayList<String>();		
		System.out.println("Funcao de copia: " + lesaoSelecionada.getRegiao());		
		for (Imagem img : lesaoSelecionada.getImagens()) {
			System.out.println("Adicionando o path: " + img.getPath());
			imgsPath.add(img.getPath());
		}		
	}	
	
	public void excluirLesao () {
		System.out.println("Excluindo lesão "+lesaoSelecionada.getRegiao());
		this.pacLesoes.remove(this.lesaoSelecionada);
	}
	
	public String salvar () {		
		String ret = "/dashboard/cadastar_pacientes.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Paciente p1 = pac_rep.buscaPorCartaoSus(this.pac.getCartao_sus());	
			
			if (pacLesoes.isEmpty()) {
				ret =  null;
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você está cadastrando um paciente sem lesões adicionadas! Essa operação não é permitida.", "  "));
			} else if (p1 == null) {		
				printLesoes(pacLesoes);
				this.pac.setLesoes(pacLesoes);
				pac_rep.save(this.pac);
				context.addMessage(null, new FacesMessage("Paciente cadastrado com sucesso. \nID do Paciente: " + pac.getId()));
				pacLesoes.clear();
				pac = new Paciente();
				lesao = new Lesao();
			} else {
				ret = null;  
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Este cartão do SUS já está cadastrado! Visualize o paciente na página de visualização.", "  "));

			}			
						
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema na comunicação com banco de dados. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
			ret = null;
		}		
		return ret;
	}	
	
	
	
/* ###########################################  Getters and Setters ###################################################*/
	public Paciente getPac() {
		return pac;
	}

	public void setPac(Paciente pac) {
		this.pac = pac;
	}

	public PacienteRepository getPac_rep() {
		return this.pac_rep;
	}

	public void setUserRep(PacienteRepository pac_rep) {
		this.pac_rep = pac_rep;
	}

	public List<Paciente> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<Paciente> todosPacs) {
		this.todosPacs = todosPacs;
	}

	public Lesao getLesao() {
		return lesao;
	}

	public void setLesao(Lesao lesao) {
		this.lesao = lesao;
	}

	public List<Lesao> getPacLesoes() {
		return pacLesoes;
	}

	public void setPacLesoes(List<Lesao> pacLesoes) {
		this.pacLesoes = pacLesoes;
	}

	public Lesao getlesaoSelecionada() {
		return lesaoSelecionada;
	}

	public void setlesaoSelecionada(Lesao lesaoSelecionada) {
		this.lesaoSelecionada = lesaoSelecionada;
	}

	public List<String> getImgsPath() {
		return imgsPath;
	}

	public void setImgsPath(List<String> imgsPath) {
		this.imgsPath = imgsPath;
	}


}
