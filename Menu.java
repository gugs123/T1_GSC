import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu{
    public static void main (String[] args){
        //Chama função limpa terminal
        LimpaTela();
        //Classe que contem todos os dados (usuarios, departamentos e pedidos)
        ListaDepartUsua auxLista = new ListaDepartUsua();
        boolean sair = false;
        Usuario usuarioLogado = null;
        usuarioLogado = Login(auxLista);
        ArrayList<PedidoAquisicao> pedidos;

        do{
            usuarioLogado = Menu(usuarioLogado, auxLista, sair);
        }while(true);
    }
    
    public static void LimpaTela(){
        try
        {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")){
                Runtime.getRuntime().exec("cls");
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else{
                System.out.print("\033[H\033[2J");  
                System.out.flush();
            }
        }
        catch (final Exception e )
        {}
    }  

    public static Usuario Menu(Usuario usuarioLogado, ListaDepartUsua auxLista, boolean sair){
        Scanner in = new Scanner(System.in);
        String opcao = "";
        LimpaTela();
        System.out.println("Operador do sistema: "+ usuarioLogado.getNome());
        System.out.println("Iniciais: "+ usuarioLogado.getInicialNome() + ". " + usuarioLogado.getInicialSobrenome() + ".");
        System.out.println();
        System.out.println();
        System.out.println("*STANLEY* - Sistema de Controle de Aquisição");
        System.out.println("1 - Alterar usuário");
        System.out.println("2 - Criar um pedido");
        System.out.println("3 - Buscar pedido");
        System.out.println("4 - Painel Admistrador");
        System.out.println("9 - Sair"); 
        System.out.print("Opção: ");
        opcao = in.nextLine();

        switch(opcao)
        {
            case "1":
            {
                return usuarioLogado = Login(auxLista);
            }
            case "2":
            {
                //Pega um departamento a partir de uma String(nome do departamento) de um usuário
                Departamento departamentoUsu = auxLista.buscaPorDepartamento(usuarioLogado.getDepartamento());
                //if(criarPedido(usuarioLogado, in, departamentoUsu, auxLista) == usuarioLogado) System.out.println("Pedido adicionado com sucesso.");
                return criarPedido(usuarioLogado, in, departamentoUsu, auxLista);
            }
            case "3":
            {
                busca(usuarioLogado, auxLista);
            }
            case "4":
            {
                Usuario aux = usuarioLogado;
                usuarioLogado = ADM(auxLista, usuarioLogado);
                if(usuarioLogado==null)
                {
                    System.out.println("Usuário logado nao tem permissão para acessar este menu");
                    return aux;
                }
                else
                {
                    return usuarioLogado;
                }
            }
            case "9":
            {
                System.exit(0);
            }

            case "5":
            {
                
            }
            default:
            {
                System.out.println("Opção inválida\n");
                break;
            }
        }
        return usuarioLogado;
    }

    public static Usuario Login(ListaDepartUsua users)
    {
        Usuario logado = null;
        do{   
            LimpaTela();
            Scanner in = new Scanner(System.in);
            System.out.println("Insira sua matrícula para entrar no sistema: ");
            System.out.print("Matrícula: ");
            String matricula = in.nextLine();
            logado = users.buscaPorMatricula(matricula);
        }while(logado == null);
        return logado;
        
        
    }

    public static Usuario criarPedido(Usuario logado, Scanner in, Departamento departamentoUsu, ListaDepartUsua listaAux){

        ArrayList<Item> listaItens = new ArrayList<>();
        boolean op = false;
        boolean subLoop1 = false;
        LimpaTela();
        String dataString = "";
        System.out.println();

        do {
            System.out.print("Informe a data do pedido, com formato dd/MM/yyyy: ");
            dataString = in.nextLine();

            if (dataString.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
                int dias = Integer.parseInt(dataString.substring(0,2));
                int mes = Integer.parseInt(dataString.substring(3,5));

                if ((dias >= 1 && dias <= 31) && (mes >= 1 && mes <= 12)) {
                    subLoop1 = false;
                }
                else {
                    System.out.println("Valor inválido para data, coloque numeros relativos ao calendário.\n");
                    subLoop1 = true;
                }

            } else {
                System.out.println("Valor inválido para data, siga o modelo de formato.\n");
                subLoop1 = true;
            }
        } while (subLoop1);

        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataPedido = LocalDate.parse(dataString,formatoData);

        do{
            double valor = 0;
            int quantidade = 0;
            LimpaTela();
            System.out.print("Informe a descrição do item: ");
            String descItem = in.nextLine();

            //Validacao do valor do item
            do {
                try {

                    System.out.print("Informe o valor do item: ");
                    valor = in.nextDouble();
                    subLoop1 = false;
                } catch (InputMismatchException e1) {
                    System.out.println("Erro: Coloque um numero válido.\n");
                    subLoop1 = true;
                    in.nextLine();

                } catch (Exception e2) {
                    System.out.println("Erro: " + e2+"\n");
                    subLoop1 = true;
                    in.nextLine();
                }
            } while(subLoop1);


            //Validacao da quantidade desejada
            do {
                try {

                    System.out.print("Informe a quantidade desejada: ");
                    quantidade = in.nextInt();
                    subLoop1 = false;
                } catch (InputMismatchException e1) {
                    System.out.println("Erro: Coloque um número inteiro.\n");
                    subLoop1 = true;
                    in.nextLine();

                } catch (Exception e2) {
                    System.out.println("Erro: " + e2+"\n");
                    subLoop1 = true;
                    in.nextLine();
                }
            } while(subLoop1);

            Item item = new Item(descItem, valor, quantidade);
            listaItens.add(item);
            System.out.println();

            //Validacao de continuacao
            int resposta = -1;
            do {
                try {
                    System.out.println("Deseja adicionar outro item? \n[1] sim \n[0] não");
                    System.out.print("Sua resposta: ");
                    resposta = in.nextInt();
                    subLoop1 = false;
                } catch (InputMismatchException e1) {
                    System.out.println("Erro: Coloque um número inteiro.\n");
                    subLoop1 = true;
                    in.nextLine();

                } catch (Exception e2) {
                    System.out.println("Erro: " + e2+"\n");
                    subLoop1 = true;
                    in.nextLine();
                }

                if (resposta != 0 && resposta != 1) {
                    if (!subLoop1) { //Visando o buffer que vai ser limpo do in.nextLine()
                        System.out.println("Erro: Selecione um índice válido.\n");
                        subLoop1 = true;
                        in.nextLine();
                    }
                }
            } while(subLoop1);

            if (resposta == 0) {
                op = true;
            }
            //Limpa o buffer
            in.nextLine();
            System.out.println();

        }while(!op);

        PedidoAquisicao pedido = new PedidoAquisicao(logado, departamentoUsu, dataPedido, listaItens);

        //Faz a regra de negocio do limite do pedido
        if(pedido.getValorTotalPedido() > departamentoUsu.getValorLimitePedido()){
            System.out.println("\nPedido de aquisição inválido: não respeitou o valor limite.");
        } else{
            listaAux.adicionaPedidoAquisicao(pedido);
            System.out.println("\nPedido de aquisição adicionado.");
        }
        return logado;
    }

    public static Usuario ADM(ListaDepartUsua user, Usuario usuarioLogado)
    {
        if(usuarioLogado.isAdm())
        {
            menuADM();
            return usuarioLogado;
        }
        return null;

        
    }


    public static void menuADM() {

            LimpaTela();
            boolean sair = false;
            while(sair==false)
            {
                System.out.println("Menu Admimistrador");
                System.out.println("1- Avaliar pedido em aberto");
                System.out.println("2- Estatística de pedidos totais");
                System.out.println("3- Número de pedidos dos últimos 30 dias e seu valor médio");            
                System.out.println("4- Valor total de cada categoria dos últimos 30 dias");            
                System.out.println("5- Detalhes do pedido de maior valor ainda em aberto");            
                System.out.println("6- Sair do menu ADM");            
                System.out.print("Opção: ");
                Scanner in = new Scanner(System.in);
                String opcao = in.nextLine();
                switch(opcao)
                {
                    case "1":
                    //TODO
                    break;
                    case "2":
                    //TODO
                    break;
                    case "3":
                    //TODO
                    break;
                    case "4":
                    //TODO
                    break;
                    case "5":
                    //TODO
                    break;
                    case "6":
                    sair = true;
                    break;
                    default:
                    System.out.println("Opção inválida");
                }
            }
    }

    public static void buscaPorID(Usuario usuarioLogado, ListaDepartUsua auxLista){
        Scanner in = new Scanner(System.in);
        boolean subLoop = true;
            if(auxLista.getListaPedidoAquisicaoSize() == 0) {
                System.out.println("\nNão há pedidos salvos na sua lista.\nDigite qualquer coisa para voltar ao menu principal.");
                String input = in.next();
                if(input != null) subLoop = false;
            }
            else {do{
                System.out.println("\nDigite o número de identificação do pedido que gostaria de ver. Digite -1 para sair.");
                int idPedido = in.nextInt();
                    if(idPedido == -1) {subLoop = false; break;}

                if(idPedido < auxLista.getListaPedidoAquisicaoSize()) {
                    System.out.println("\n---------------------------------------------------------");
                    PedidoAquisicao retorno = auxLista.getPedidoAquisicao(idPedido);
                    System.out.println(retorno.pedidoToString(idPedido));
                    System.out.println("---------------------------------------------------------\n");
                }
                else {System.out.println("\nNão há pedido com este número de identificação."); break;}

                menuAlteracao(usuarioLogado, in, idPedido, auxLista);

        }while(subLoop == true);}
    }

    public static void menuAlteracao(Usuario usuarioLogado, Scanner in, int idPedido, ListaDepartUsua auxLista){
        System.out.println("\nDeseja fazer algo com este pedido?");
        System.out.println("0 - Não.");
        System.out.println("1 - Deletar pedido");
        System.out.println("2 - Editar pedido");
        int opcaoCase = in.nextInt();
        switch(opcaoCase){
            case 0: {
                break;
            }
            case 1: {
                deletar(usuarioLogado, in, idPedido, auxLista);
                break;
            }
            case 2: {
                editar(usuarioLogado, in, idPedido, auxLista);
                break;
            }
        }
    }

    private static void busca(Usuario usuarioLogado, ListaDepartUsua auxLista){
        Scanner in = new Scanner(System.in);
        System.out.println("\nMenu de busca");
        System.out.println("[0] Cancelar\n[1] Ver todos os meus pedidos\n[2] Procurar pedidos por funcionário\n[3] Procurar pedidos por número de identificação\n[4] Procurar pedidos por itens inclusos");
        int opcao = in.nextInt();
        switch(opcao){
            case 0:{
                System.out.println("\nDigite qualquer coisa para sair.");
                String sair = in.next();
                if(sair != null) break;
                break;
            }
            case 1:{
                buscaPorFuncionario(usuarioLogado, auxLista, true);
                System.out.println("\nDigite qualquer coisa para sair.");
                String sair = in.next();
                if(sair != null) break;
                break;
            }
            case 2:{
                buscaPorFuncionario(usuarioLogado, auxLista, false);
                System.out.println("\nDigite qualquer coisa para sair.");
                String sair = in.next();
                if(sair != null) break;
                break;
            }
            case 3:{
                buscaPorID(usuarioLogado, auxLista);
                System.out.println("\nDigite qualquer coisa para sair.");
                String sair = in.next();
                if(sair != null) break;
                break;
            }
        }
    }

    // MÉTODO OBSOLETO, MAS DEIXADO AQUI CASO O buscaPorFuncionario QUEBRE
    /*private static void buscaMeusPedidos(Usuario usuarioLogado, ListaDepartUsua auxLista){
        Scanner in = new Scanner(System.in);
        int qtd = 0;
        for(int i = 0; i < auxLista.getListaPedidoAquisicaoSize(); i++){
            PedidoAquisicao teste = auxLista.getPedidoAquisicao(i);
            if(teste.getUsuarioSolicitante() == usuarioLogado) { 
                qtd++;
            }
        }
        if(qtd == 0) System.out.println("\nVocê ainda não salvou pedido algum.");
        else {
            System.out.println("\nVocê salvou " + qtd + " pedidos.");
            System.out.println(auxLista.getListaPedidosFuncToString(usuarioLogado));
        }
        
        System.out.println("Selecionar algum pedido? Inserir o número para selecionar, ou -1 para sair.");
        int opcao = in.nextInt();
        if(opcao >= 0 && opcao < auxLista.getListaPedidosFunc(usuarioLogado).size()){
            menuAlteracao(usuarioLogado, in, opcao, auxLista);
        } else System.out.println("Saindo.");

    }*/

    private static void buscaPorFuncionario(Usuario usuarioLogado, ListaDepartUsua auxLista, boolean self){
        Scanner in = new Scanner(System.in);
        int qtd = 0;
        String matricula = "";
        if(self == false) {
            System.out.println("\nInforme a matrícula do funcionário cujos pedidos você quer ver.");
            matricula = in.next();
        } else matricula = usuarioLogado.getMatricula();
        Usuario pesquisa = auxLista.buscaPorMatricula(matricula);
        for(int i = 0; i < auxLista.getListaPedidoAquisicaoSize(); i++){
            PedidoAquisicao teste = auxLista.getPedidoAquisicao(i);
            Usuario solicitante = teste.getUsuarioSolicitante();
            if(solicitante == pesquisa) { 
                qtd++;
            }
        }
        if(qtd == 0 && usuarioLogado != pesquisa) System.out.println("\nNão há pedidos salvos por este funcionário. Digite qualquer coisa para sair.");
        else if (qtd != 0 && usuarioLogado != pesquisa){
            System.out.println("\nEste usuário salvou " + qtd + " pedidos.");
            System.out.println(auxLista.getListaPedidosFuncToString(auxLista.buscaPorMatricula(matricula)));
            if(usuarioLogado.isAdm() == false)System.out.println("\nVocê não pode modificar os pedidos de outros usuários, apenas pode vê-los. Digite qualquer coisa para sair.");
            else {
                System.out.println("Selecionar algum pedido? Inserir o NÚMERO IDENTIFICADOR para selecionar, ou -1 para sair.");
                int opcao = in.nextInt();
                if(opcao >= 0 && opcao <= auxLista.getListaPedidoAquisicaoSize()){
                menuAlteracao(usuarioLogado, in, opcao, auxLista);
            } else System.out.println("Digite algo de novo.");
            };
        }
        else if (qtd == 0 && usuarioLogado == pesquisa){ System.out.println("\nVocê ainda não salvou pedido algum. Digite qualquer coisa para sair."); }
        else if (qtd != 0 && usuarioLogado == pesquisa){
            System.out.println("\nVocê salvou " + qtd + " pedidos.");
            System.out.println(auxLista.getListaPedidosFuncToString(usuarioLogado));
            System.out.println("Selecionar algum pedido? Inserir o NÚMERO IDENTIFICADOR para selecionar, ou -1 para sair.");
            int opcao = in.nextInt();
            if(opcao >= 0 && opcao < auxLista.getListaPedidoAquisicaoSize()){
                menuAlteracao(usuarioLogado, in, opcao, auxLista);
            } else System.out.println("Digite algo de novo.");
        }
        String opcao = in.next();
        if(opcao != null) System.out.println("Saindo.");
    }

    private static void deletar(Usuario usuarioLogado, Scanner in, int idPedido, ListaDepartUsua auxLista){
        PedidoAquisicao retorno = auxLista.getPedidoAquisicao(idPedido);
        if(usuarioLogado == retorno.getUsuarioSolicitante()){
            System.out.println("Você tem certeza que quer deletar este pedido? Esta ação é irreversível.");
            System.out.println("Não se preocupe. Os números de identificação dos outros pedidos continuarão os mesmos.");
            System.out.println("[0] para CANCELAR\n[1] para CONFIRMAR");
            int opcao = in.nextInt();
            if(opcao == 1){
                // TO-DO
            }
            else System.out.println("Operação cancelada.");
        }
        else System.out.println("Desculpe. Você não pode executar esta função.");
    }

    private static void editar(Usuario usuarioLogado, Scanner in, int idPedido, ListaDepartUsua auxLista){
        PedidoAquisicao retorno = auxLista.getPedidoAquisicao(idPedido);
        if(retorno.getStatusDoPedido() != 1) System.out.println("Desculpe, mas este pedido não pode ser editado.");
        else {
            System.out.println("O que gostaria de modificar?\nDigite:");
            System.out.println("[0] Cancelar\n[1] Itens do pedido");
            int opcao = in.nextInt();
            switch(opcao){
                case 1: {
                    retorno.getItensString();
                    System.out.println("Selecione um item.");
                    // TO-DO
                }
                default: {
                    break;
                }
            }
        }

    }
}