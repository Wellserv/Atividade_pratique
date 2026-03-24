import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaEventos {
    private static List<Evento> todosEventos = new ArrayList<>();
    private static List<Evento> presencasConfirmadas = new ArrayList<>();
    private static Usuario usuarioLogado;
    private static final String ARQUIVO_DADOS = "events.data";

    public static void main(String[] args) {
        carregarDados();
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Bem-vindo ao Sistema de Eventos ---");
        System.out.print("Nome: "); String nome = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Telefone: "); String tel = sc.nextLine();
        System.out.print("Cidade: "); String cidade = sc.nextLine();
        System.out.print("Data de Nascimento: "); String nasc = sc.nextLine();
        
        usuarioLogado = new Usuario(nome, email, tel, cidade, nasc);

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n1. Cadastrar Evento Esportivo\n2. Listar Próximos Eventos\n3. Confirmar Presença\n4. Meus Eventos (Cancelar)\n5. Eventos Encerrados\n0. Sair");
            opcao = sc.nextInt(); sc.nextLine();

            switch (opcao) {
                case 1 -> cadastrarEvento(sc);
                case 2 -> listarEventos(true);
                case 3 -> confirmarPresenca(sc);
                case 4 -> gerenciarPresencas(sc);
                case 5 -> listarEventos(false);
                case 0 -> salvarDados();
            }
        }
    }

    private static void cadastrarEvento(Scanner sc) {
        System.out.print("Nome do Evento: "); String n = sc.nextLine();
        System.out.print("Endereço: "); String e = sc.nextLine();
        System.out.print("Data/Hora (dd/MM/yyyy HH:mm): "); 
        String dataStr = sc.nextLine();
        LocalDateTime dt = LocalDateTime.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        System.out.print("Descrição: "); String d = sc.nextLine();

        todosEventos.add(new Evento(n, e, "Esportivo", dt, d));
        todosEventos.sort(Comparator.comparing(Evento::getHorario)); // Ordenação automática [cite: 20]
        System.out.println("Evento cadastrado com sucesso!");
    }

    private static void listarEventos(boolean futuros) {
        LocalDateTime agora = LocalDateTime.now();
        for (Evento ev : todosEventos) {
            if (futuros && ev.getHorario().isAfter(agora)) System.out.println(ev);
            else if (!futuros && ev.getHorario().isBefore(agora)) System.out.println(ev);
        }
    }

    private static void confirmarPresenca(Scanner sc) {
        listarEventos(true);
        System.out.print("Digite o nome exato do evento para participar: ");
        String nome = sc.nextLine();
        todosEventos.stream().filter(e -> e.getNome().equalsIgnoreCase(nome)).findFirst()
                    .ifPresent(presencasConfirmadas::add);
    }

    private static void gerenciarPresencas(Scanner sc) {
        presencasConfirmadas.forEach(System.out::println);
        System.out.print("Deseja cancelar algum? Digite o nome ou 'n': ");
        String nome = sc.nextLine();
        presencasConfirmadas.removeIf(e -> e.getNome().equalsIgnoreCase(nome));
    }

    private static void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(todosEventos);
            System.out.println("Dados salvos em events.data!");
        } catch (IOException e) { System.out.println("Erro ao salvar."); }
    }

    private static void carregarDados() {
        File file = new File(ARQUIVO_DADOS);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                todosEventos = (List<Evento>) ois.readObject();
            } catch (Exception e) { todosEventos = new ArrayList<>(); }
        }
    }
}