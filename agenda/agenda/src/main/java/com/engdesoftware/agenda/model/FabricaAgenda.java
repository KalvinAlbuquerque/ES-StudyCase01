package com.engdesoftware.agenda.model;

/**
 * Classe responsável pela criação de instâncias de Agenda.
 * Segue os padrões de projeto Singleton e Factory Method.
 */
public class FabricaAgenda {

    // Constantes para definir o tipo de agenda a ser criada 
    public static final int AGENDA_MAP = 0;
    public static final int AGENDA_LIST = 1;

    // Atributo estático para armazenar a única instância da classe (Singleton)
    private static FabricaAgenda instancia;

    /**
     * Construtor privado para impedir a criação de instâncias fora desta classe.
     * Característica principal do padrão Singleton.
     */
    private FabricaAgenda() {}

    /**
     * Método público estático para obter a instância única da fábrica.
     * Se a instância ainda não existir, ela é criada.
     * @return A única instância de FabricaAgenda.
     */
    public static FabricaAgenda getInstancia() {
        if (instancia == null) {
            instancia = new FabricaAgenda();
        }
        return instancia;
    }

    /**
     * Método de fábrica (Factory Method) que cria e retorna uma implementação
     * de IF_Agenda com base no tipo fornecido.
     * @param tipo O tipo de agenda desejado (use as constantes AGENDA_MAP ou AGENDA_LIST).
     * @return Uma instância que implementa IF_Agenda ou null se o tipo for inválido.
     */
    public IF_Agenda criaAgenda(int tipo) 
    {
        switch (tipo) {
            case AGENDA_LIST:
                System.out.println("Criando uma instância de AgendaList...");
                return new AgendaList();
            case AGENDA_MAP:
                System.out.println("Criando uma instância de AgendaMap...");
                return new AgendaMap();
            default:
                // Lança uma exceção se o tipo for desconhecido
                throw new IllegalArgumentException("Tipo de agenda inválido.");
        }
    }
}