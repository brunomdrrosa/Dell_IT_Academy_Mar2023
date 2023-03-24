import entities.Caminhao;
import enums.CaminhaoEnum;

import java.util.*;

public class Program {

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        System.out.println();
        System.out.println("=== MENU ===");
        System.out.println("1. Consultar os trechos");
        System.out.println("2. Cadastrar um transporte");
        System.out.println("3. Exibir dados estatísticos");
        System.out.println("4. Finalizar o programa");
        try {
            int opcaoMenu = new Scanner(System.in).nextInt();
            switch (opcaoMenu) {
                case 1 -> opcao1();
                case 2 -> opcao2();
                case 3 -> opcao3();
                case 4 -> System.out.println("Fechando o programa...");
                default -> {
                    System.out.println("Digite um número de 1 a 5");
                    menu();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Digite um número de 1 a 5");
            menu();
        }
    }

    private static void opcao1() {
        try {
            String cidadeOrigem = getCidadeInput("Digite a sua cidade de origem: ");
            String cidadeDestino = getCidadeInput("Digite a sua cidade de destino: ");
            String inputTransporte = getModalidadeTransporte();
            Caminhao caminhao = getCaminhao(inputTransporte);

            List<String> listaNomesCapitais = Arrays.asList(CSVReader.readCapitais().get(0)
                    .replaceAll("^\\[|]$", "")
                    .split("\\s*,\\s*"));

            int indexCidadeOrigem = getIndexCidade(cidadeOrigem, listaNomesCapitais);
            int indexCidadeDestino = getIndexCidade(cidadeDestino, listaNomesCapitais);
            String distanciaCidades = CSVReader.readAll().get(indexCidadeOrigem + 1).get(indexCidadeDestino);

            System.out.println("de " + cidadeOrigem + " para " + cidadeDestino + ", utilizando um caminhão de " +
                    caminhao.getPorte() + " porte" + " a distância é de " + distanciaCidades + "km e o custo será de R$" +
                    caminhao.getCaminhaoEnum().getValor() * Integer.parseInt(distanciaCidades));
            menu();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("A cidade informada não existe na lista");
            menu();
        } catch (NumberFormatException e) {
            System.out.println("O número informado para a modalidade do transporte deve ser de 1 a 3");
            menu();
        }
    }

    private static int getIndexCidade(String cidadeOrigem, List<String> listaNomesCapitais) {
        return listaNomesCapitais.indexOf(cidadeOrigem);
    }

    private static Caminhao getCaminhao(String inputTransporte) {
        if (Objects.equals(inputTransporte, "1")) {
            return new Caminhao("pequeno", CaminhaoEnum.PEQUENO_PORTE);
        } else if (Objects.equals(inputTransporte, "2")) {
            return new Caminhao("médio", CaminhaoEnum.MEDIO_PORTE);
        } else if (Objects.equals(inputTransporte, "3")) {
            return new Caminhao("grande", CaminhaoEnum.GRANDE_PORTE);
        }
        throw new NumberFormatException();
    }

    private static String getModalidadeTransporte() {
        System.out.println("Digite a modalidade do transporte que será utilizado: ");
        System.out.println("1. CAMINHÃO DE PEQUENO PORTE");
        System.out.println("2. CAMINHÃO DE MÉDIO PORTE");
        System.out.println("3. CAMINHÃO DE GRANDE PORTE");
        return new Scanner(System.in).nextLine();
    }

    private static String getCidadeInput(String texto) {
        System.out.println(texto);
        return new Scanner(System.in).nextLine().toUpperCase();
    }

    private static void opcao2() {
        try {
            String inputCidades = getInputCidades();
            List<String> listaProdutos = listarProdutosCadastrados();
            String[] listaInputNomesCidades = inputCidades.split("\\s*,\\s*");
            List<String> listaNomesCapitais = Arrays.asList(CSVReader.readCapitais().get(0)
                    .replaceAll("^\\[|]$", "")
                    .split("\\s*,\\s*"));
            List<Integer> listaDistanciaCidades = getListaDistanciaCidades(listaInputNomesCidades, listaNomesCapitais);
            int somaDistancias = listaDistanciaCidades.stream().mapToInt(Integer::intValue).sum();
            String nomesProdutos = getProdutos(listaProdutos);
            System.out.println("de " + inputCidades + ", a distância a ser percorrida é de " + somaDistancias + "km," +
                    " para transportes dos produtos " + nomesProdutos + "será necessário utilizar " + "" + ", de forma a" +
                    " resultar no menor custo de transporte por km rodado. O valor total do transporte dos itens é ");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("A cidade informada não existe na lista");
            menu();
        }
    }

    private static String getProdutos(List<String> listaProdutos) {
        ArrayList<String> listaNomesProdutos = new ArrayList<>();
        for (String listaProduto : listaProdutos) {
            listaNomesProdutos.add(listaProduto.split(",")[0]);
        }
        return listaNomesProdutos.toString().replaceAll("^\\[|]$", "");
    }

    private static List<Integer> getListaDistanciaCidades(String[] listaInputNomesCidades, List<String> listaNomesCapitais) {
        List<Integer> listaIndexsCidades = new ArrayList<>();
        List<Integer> listaDistanciaCidades = new ArrayList<>();

        for (String i : listaInputNomesCidades) {
            listaIndexsCidades.add(listaNomesCapitais.indexOf(i));
        }

        int k = 0;
        for (int i = 1; listaIndexsCidades.size() > i; i++) {
            listaDistanciaCidades.add(Integer.valueOf(CSVReader.readAll()
                    .get(listaIndexsCidades.get(k) + 1).get(listaIndexsCidades.get(i))));
            k = k + 1;
        }
        return listaDistanciaCidades;
    }

    private static List<String> listarProdutosCadastrados() {
        System.out.println("Digite o nome do produto, quantidade e peso (em gramas) separados por vírgula ");
        System.out.println("Exemplo: CELULAR, 20, 500");
        System.out.println("Você pode parar de cadastrar os produtos a qualquer momento digitando PARAR");
        List<String> listaProdutos = new ArrayList<>();
        Scanner inputProdutos = new Scanner(System.in);
        while (true) {
            String input = inputProdutos.nextLine();
            if (input.equalsIgnoreCase("PARAR")) {
                break;
            }
            listaProdutos.add(input);
        }
        listaProdutos.removeAll(List.of(""));
        return listaProdutos;
    }

    private static String getInputCidades() {
        System.out.println("Digite o nome das cidades que você deseja ir separadas por vírgula: ");
        System.out.println("Exemplo: PORTO ALEGRE, CURITIBA, SAO PAULO");
        return new Scanner(System.in).nextLine();
    }

    private static void opcao3() {
        menu();
    }

}