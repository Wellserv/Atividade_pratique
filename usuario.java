import java.io.Serializable;

public class Usuario implements Serializable {
    private String nome;
    private String email;
    private String telefone;
    private String cidade;
    private String dataNascimento;

    public Usuario(String nome, String email, String telefone, String cidade, String dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public String getNome() { return nome; }
}