import entities.Caminhao;
import entities.Produto;
import entities.Viagem;
import enums.CaminhaoEnum;

import java.util.*;

public class Program {

    static List<Viagem> listaViagens = new ArrayList<>();

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
            System.out.println("Você informou um input inválido, retornando ao menu principal");
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
            String[] listaInputNomesCidades = inputCidades.split("\\s*,\\s*");
            List<String> listaNomesCapitais = Arrays.asList(CSVReader.readCapitais().get(0)
                    .replaceAll("^\\[|]$", "")
                    .split("\\s*,\\s*"));
            List<Integer> listaDistanciaCidades = getListaDistanciaCidades(listaInputNomesCidades, listaNomesCapitais);

            List<Produto> listaProdutos = listarProdutosCadastrados();
            String listaNomesProdutos = listarNomesProdutos(listaProdutos);
            double pesoProdutos = calcularPesoTotal(listaProdutos);
            List<Integer> listaPortesCaminhoes = calcularPorteCaminhoes(pesoProdutos);
            Integer quantidadeProdutos = somarQuantidadeProdutos(listaProdutos);
            int somaDistancias = listaDistanciaCidades.stream().mapToInt(Integer::intValue).sum();
            double valorTotalViagem = getValorTotalViagem(listaPortesCaminhoes, somaDistancias);
            double valorUnitarioMedio = valorTotalViagem / quantidadeProdutos;
            Viagem novaViagem = cadastrarViagem(inputCidades, listaNomesProdutos, listaPortesCaminhoes, somaDistancias, valorTotalViagem, valorUnitarioMedio);
            imprimirResultadoViagem(novaViagem);
            listaViagens.add(novaViagem);
            menu();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("A cidade informada não existe na lista");
            menu();
        }
    }

    private static Viagem cadastrarViagem(String inputCidades, String listaNomesProdutos, List<Integer> listaPortesCaminhoes,
                                          int somaDistancias, double valorTotalViagem, double valorUnitarioMedio) {
        return new Viagem(inputCidades, somaDistancias, listaNomesProdutos, listaPortesCaminhoes, valorTotalViagem, valorUnitarioMedio);
    }

    private static void imprimirResultadoViagem(Viagem viagem) {
        System.out.println();
        System.out.println("De " + viagem.getNomesCidades() + ", a distância a ser percorrida é de " + viagem.getDistanciaTotal() + "km");
        System.out.println("Para transportes dos produtos " + viagem.getNomesProdutos() + " será necessário utilizar: ");
        System.out.println(viagem.getQuantidadePortesCaminhoes().get(0) + " " + checarQuantidadePequenoPorte(viagem.getQuantidadePortesCaminhoes()) + " de porte PEQUENO");
        System.out.println(viagem.getQuantidadePortesCaminhoes().get(1) + " de porte MÉDIO");
        System.out.println(viagem.getQuantidadePortesCaminhoes().get(2) + " de porte GRANDE");
        System.out.println("O valor total do transporte dos itens é R$ " + viagem.getValorTotalViagem() +
                ", sendo R$ " + String.format("%.2f", viagem.getValorUnitarioMedio()) + " é o custo unitário médio");
    }

    private static Integer somarQuantidadeProdutos(List<Produto> listaProdutos) {
        List<Integer> listaQuantidadeProdutos = new ArrayList<>();
        for (Produto listaProduto : listaProdutos) {
            listaQuantidadeProdutos.add(listaProduto.getQuantidade());
        }
        return listaQuantidadeProdutos.stream().mapToInt(Integer::intValue).sum();
    }

    private static double getValorTotalViagem(List<Integer> listaPortesCaminhoes, int somaDistancias) {
        double valorTotalPequenoPorte = somaDistancias * listaPortesCaminhoes.get(0) * CaminhaoEnum.PEQUENO_PORTE.getValor();
        double valorTotalMedioPorte = somaDistancias * listaPortesCaminhoes.get(1) * CaminhaoEnum.MEDIO_PORTE.getValor();
        double valorTotalGrandePorte = somaDistancias * listaPortesCaminhoes.get(2) * CaminhaoEnum.GRANDE_PORTE.getValor();
        return valorTotalPequenoPorte + valorTotalMedioPorte + valorTotalGrandePorte;
    }

    private static String checarQuantidadePequenoPorte(List<Integer> listaPortesCaminhoes) {
        if (listaPortesCaminhoes.get(0) == 1) {
            return "caminhão";
        } else {
            return "caminhões";
        }
    }

    private static List<Integer> calcularPorteCaminhoes(double pesoProdutos) {
        List<Integer> listaPortesCaminhoes = new ArrayList<>();
        int quantidadePortePequeno = 0;
        int quantidadePorteMedio = 0;
        int quantidadePorteGrande = 0;

        while (pesoProdutos > 0) {
            if (pesoProdutos > 8000) {
                quantidadePorteGrande += 1;
                pesoProdutos -= 10000;
            } else if (pesoProdutos > 2000) {
                quantidadePorteMedio += 1;
                pesoProdutos -= 4000;
            } else {
                quantidadePortePequeno += 1;
                pesoProdutos -= 1000;
            }
        }
        listaPortesCaminhoes.add(quantidadePortePequeno);
        listaPortesCaminhoes.add(quantidadePorteMedio);
        listaPortesCaminhoes.add(quantidadePorteGrande);
        return listaPortesCaminhoes;
    }

    private static double calcularPesoTotal(List<Produto> listaProdutos) {
        List<Double> listaPesosProdutos = new ArrayList<>();
        for (Produto listaProduto : listaProdutos) {
            listaPesosProdutos.add(listaProduto.getPeso() * listaProduto.getQuantidade());
        }
        return listaPesosProdutos.stream().mapToDouble(Double::doubleValue).sum();
    }

    private static String listarNomesProdutos(List<Produto> listaProdutos) {
        List<String> listaNomesProdutos = new ArrayList<>();
        for (Produto produto : listaProdutos) {
            listaNomesProdutos.add(produto.getNome());
        }
        return Arrays.toString(listaNomesProdutos.toArray()).replaceAll("^\\[|]$", "");
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

    private static List<Produto> listarProdutosCadastrados() {
        ArrayList<Produto> listaProdutos = new ArrayList<>();
        Scanner inputProdutos = new Scanner(System.in).useLocale(Locale.US);
        while (true) {
            String nomeProduto = cadastrarNome(inputProdutos);
            int quantidade = cadastrarQuantidade(inputProdutos);
            double pesoQuilo = cadastrarPeso(inputProdutos);
            Produto novoProduto = new Produto(nomeProduto, quantidade, pesoQuilo);
            listaProdutos.add(novoProduto);
            System.out.println("Produto cadastrado com sucesso");
            System.out.println("Deseja cadastrar outro produto? (S) ou (N)");
            String input = new Scanner(System.in).nextLine().toUpperCase();
            if (input.equalsIgnoreCase("N")) {
                break;
            }
            inputProdutos.nextLine();
        }
        return listaProdutos;
    }

    private static String cadastrarNome(Scanner inputProdutos) {
        System.out.print("Digite o nome do produto que você deseja transportar: ");
        return inputProdutos.nextLine();
    }

    private static double cadastrarPeso(Scanner inputProdutos) {
        System.out.print("Digite o peso do produto em quilos usando o ponto final (.) como casa decimal: ");
        double pesoQuilo = inputProdutos.nextDouble();
        if (pesoQuilo < 0) {
            System.out.println("Você não pode informar um peso com valor negativo");
            menu();
        }
        return pesoQuilo;
    }

    private static int cadastrarQuantidade(Scanner inputProdutos) {
        System.out.print("Digite a quantidade do produto: ");
        int quantidade = inputProdutos.nextInt();
        if (quantidade < 1) {
            System.out.println("Você deve informar um número inteiro e maior que zero para a quantidade do produto");
            menu();
        }
        return quantidade;
    }

    private static String getInputCidades() {
        System.out.println("Digite o nome das cidades que você deseja ir separadas por vírgula: ");
        System.out.println("Exemplo: PORTO ALEGRE, CURITIBA, SAO PAULO");
        return new Scanner(System.in).nextLine().toUpperCase();
    }

    private static void opcao3() {
        if (listaViagens.size() == 0) {
            System.out.println("Nenhuma viagem foi cadastrada ainda");
            menu();
        } else {
            for (Viagem listaViagem : listaViagens) {
                imprimirResultadoViagem(listaViagem);
            }
            menu();
        }

    }

}
