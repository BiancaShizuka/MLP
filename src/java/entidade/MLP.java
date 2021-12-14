package entidade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MLP {
    private int nosInicio;
    private int nosOculto;
    private int nosSaida;
    private String file="";
    private List<Camada> camadas;
    private List<List<Double>> entrada;
    private double n; // taxa de aprendizagem
    private int qtdeIteracao;
    private int tipoFuncao;
    private double erroMinimo;
    private List<String> listnomecol;
    private List<String> listatributos;
    private int qtdeDados=0;
    
    private int [][] matConfusao;
    
    private Double[][] errosLista;
    public List<String> pegarAtributos(String filename){
        String linha;
        List<String> atributos=null;
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(filename));
            if ((linha = csvReader.readLine()) != null) {
                String[] data = linha.split(",");
                atributos=new ArrayList<>(data.length-1);
                for(int i=0;i<data.length-1;i++){
                    atributos.add(data[i]);
                }
            }
            csvReader.close();
        }catch(Exception e){}
        
        return atributos;
    }
    public void criarListaEntrada(int qtdedados,int qtdecol){
    
        entrada=new ArrayList<>(qtdecol);
        for(int i=0;i<qtdecol;i++){
            entrada.add(new ArrayList<>(qtdedados));
        }
    }
    public RetornoTabela executar(String filename,int qtden,List<String> listaAtrib){
        file=filename;
        
        RetornoTabela ret=RetornoTabela.getInstance();
        lerArquivo(listaAtrib);
        ret.setColunas(listnomecol);
        ret.setEntrada(entrada);
        ret.setNentrada(nosInicio);
        ret.setNsaida(nosSaida);
        ret.setAtributos(listatributos);
      
        return ret;
        
    }
    public void lerArquivo(List<String> listaAtrib){
        String linha,nomescol="";
        //lista para ver as colunas
        List<List<String>> lista=new ArrayList<>(100);
        for(int i=0;i<100;i++){
            lista.add(new ArrayList<>(100));
        }
        System.out.println("entrei no ler arquivo");
        listatributos=listaAtrib;
        for(int v=0;v<listaAtrib.size();v++)
        {
            System.out.println(""+listatributos.get(v));
        }
        System.out.println("passei no if");
        int i=0,j;
        List<String> listasaida=new ArrayList<>(10);
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            while ((linha = csvReader.readLine()) != null) {
             
                String[] data = linha.split(",");
                //é a primeira linha onde está os nomes dos atributos
                if(i==0){
                    nosInicio=listaAtrib.size(); 
                    //guardar os nomes das colunas
                    nomescol=linha;
                    
                }
                else{
                    qtdeDados++;
            
                    
                
                    //é para contar quantas classes de saida existe
                    j=0;
                    while(j<listasaida.size() && !listasaida.get(j).equals(data[data.length-1]))
                        j++;
                    if(j==listasaida.size()){
                        listasaida.add(data[data.length-1]);
                 
                    }
                }
                i++;
           
            
            }
            //vou criar uma lista String com os nomes das colunas
            listnomecol=new ArrayList<>(nosInicio+listasaida.size());
            for(int t=0;t<nosInicio;t++){
                listnomecol.add(listaAtrib.get(t));
                
            }
            for(int t=0;t<listasaida.size();t++){
                listnomecol.add("saida"+(t+1));
            }
            
            criarListaEntrada(qtdeDados,nosInicio+listasaida.size());
            listasaida.sort(Comparator.comparing( String::toString ));
            normalizar(listasaida);
            System.out.println("Nome das colunas: "+listnomecol);
            nosSaida=listasaida.size();
            System.out.println("NosInicio = "+nosInicio+" nosOculto = "+nosOculto+" nosSaida = "+nosSaida);
            
            csvReader.close();
        }catch(Exception e){}
    }
    public int procurarPosListaAtributos(String nome){
        int pos=0;
        while(pos<listatributos.size() && !nome.equals(listatributos.get(pos)))
            pos++;
        
        return pos;
    }
    public void normalizar(List<String> listasaida){
        double min,max,intervalo;
        String linha,nomescol[];
        int i=0,indice;
    
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            linha = csvReader.readLine();
            nomescol=linha.split(",");
            while ((linha = csvReader.readLine()) != null) {
             
                String[] data = linha.split(",");
                for(int j=0;j<data.length-1;j++){
                    int pos=procurarPosListaAtributos(nomescol[j]);
          
                    if(pos<listatributos.size()){
                        entrada.get(pos).add(Double.parseDouble(data[j]));
                    }
                }
                //colocar as saidas
                //pego o indice onde está a saida
                
                
                indice=listasaida.indexOf(data[data.length-1]);
                //preencho as colunas da saida com 0
         
                for(int j=nosInicio;j<nosInicio+listasaida.size();j++)
                    entrada.get(j).add(0.0);
                entrada.get(indice+nosInicio).set(i, 1.0);
             
      
                i++;
            }
            
            
         
         
            csvReader.close();
        }catch(Exception e){
            System.out.println(""+e.getMessage());}
      
        for(i=0;i<nosInicio;i++){
         
            //normalizando as entrdas
            min=Collections.min(entrada.get(i));
            max=Collections.max(entrada.get(i));
            intervalo=max-min;
            System.out.println(""+entrada.get(i).size());
            for(int j=0;j<entrada.get(i).size();j++){
                entrada.get(i).set(j, (entrada.get(i).get(j)-min)/intervalo);
            }
            
        }
        System.out.println("terminei normalizar");
    }
    public RetornoTabela iniciarTreino(int qtde_oculto,int iteracao,double erroMin,double n,int tipo){
        RetornoTabela r=RetornoTabela.getInstance();
        this.qtdeIteracao=iteracao;
        this.nosInicio=r.getNentrada();
        this.nosSaida=r.getNsaida();
        this.nosOculto=qtde_oculto;
        this.erroMinimo=erroMin;
        this.tipoFuncao=tipo;
        this.n=n;
        System.out.println("entrei no iniciar"+nosInicio+" "+nosOculto+" "+nosSaida);
        iniciar(nosInicio,nosOculto,nosSaida);
        treinar(r.getEntrada());
        r.setCamadas(camadas);
        return r;
    }
    public void iniciar(int nosInicio, int nosOculto, int nosSaida) {
        this.nosInicio = nosInicio;
        this.nosOculto = nosOculto;
        this.nosSaida = nosSaida;
        
        this.camadas=new ArrayList<Camada>();
        
        //Camada de inicio
        /*
        nosInicio: quantidade de nós da camada de inicio
        nosOcultos: quantidade de nós da camada oculta
        */
        this.camadas.add(new Camada(nosInicio,nosOculto));
        
        //Camada oculta
        /*
        nosOcultos: quantidade de nós da camada oculta
        nosSaida: quantidade de nós da camada de saida
        */
        this.camadas.add(new Camada(nosOculto,nosSaida));
        
        //camada de saida
        /*
        nosSaida: quantidade de neuroneos da camada de saida
        0: depois não há mais camada
        */
        this.camadas.add(new Camada(nosSaida,0));
    }
    
    public void treinar(List<List<Double>> dados){
        
        RetornoTabela r=RetornoTabela.getInstance();
        int i=0,j,noculto=0,linha=0,cont;
        
        double erroQuadratico=0;
        int epocas=0;
        int colunasSaida=0;
        List<Integer> index=new ArrayList<>(dados.get(0).size());
        for(i=0;i<dados.get(0).size();i++){
            index.add(i);
        }
        Collections.shuffle(index);
        
        
        //Pegar os erros e colocar na matriz
        int qtdeErros=qtdeIteracao/10;
        errosLista=new Double[qtdeErros][2];
        
        i=0;
        try{
            do{
               cont=0;

                
                erroQuadratico=0;
    
                while (cont<dados.get(1).size()) {

                    linha=index.get(cont);

                    //Calcular o net e saida de cada neuronio
                    calcularNet(dados,linha);

                    colunasSaida=nosSaida;
                    double[] respostaEsperada=new double[colunasSaida];

                    //Quantidade de atributos(colunas) de entrada
                    int qtdeColunas=nosInicio;
                    int aux=0;
                    
                    for(int k=nosInicio;k<nosInicio+nosSaida;k++){
                        respostaEsperada[aux]=dados.get(k).get(linha);
                        aux++;
                    }
                    
                    
//                    System.out.print("Lista Reposta Esperada: ");
//                    for(int b=0;b<respostaEsperada.length;b++){
//                        System.out.print(" "+respostaEsperada[b]+" , ");
//                    }
//                    System.out.println("");
//                    
                    
                    erroQuadratico+=erroSaida(respostaEsperada);
                    erroOculta();
                    atualizaPesos();
                    
                    cont++;
                }
            
                
                erroQuadratico=(erroQuadratico/linha);
                System.out.println("EPOCA "+epocas);
                System.out.println("Erro Quadratico = "+erroQuadratico);
                if(epocas%10==0){
                    errosLista[epocas/10][0]=(double)epocas;
                    errosLista[epocas/10][1]=erroQuadratico;
                }
                epocas++;  
                    
            }while(erroQuadratico>erroMinimo && epocas<qtdeIteracao);

                
        }catch(Exception e){
            System.out.println("erro: "+e.getMessage());}
        
        //exibir como ficou os pesos
        
        Camada camada;
        List<Neuronio> neuronios;
        Neuronio n;
        System.out.println("Erro quadratico="+erroQuadratico);
        for(i=0;i<camadas.size();i++){
            System.out.println("Camada "+i);
            camada=camadas.get(i);
            
            neuronios=camada.getNeuroneos();
            for(j=0;j<neuronios.size();j++){
                System.out.println("    ->Neuronio "+j);
                n=neuronios.get(j);
                
                for(int k=0;k<n.getListaPesos().size();k++){
                    System.out.println("        =>peso "+k+"  = "+n.getListaPesos().get(k));
                }
            }
        }
        r.setListaErros(errosLista);
    }
    public void calcularNet(List<List<Double>> data,int linha){
        int posoculto=0;
        int j=0;
        double net=0;
        Camada cInicio=camadas.get(0);
        Camada cOculto=camadas.get(1);
        Camada cSaida=camadas.get(2);
        List<Neuronio> nInicio=cInicio.getNeuroneos();
        List<Neuronio> nOculto=cOculto.getNeuroneos();
        List<Neuronio> nSaida=cSaida.getNeuroneos();
        
        Neuronio nAux=null;
        

        for(int k=0;k<nInicio.size();k++){
            nAux=nInicio.get(k);
            nAux.setNet(data.get(k).get(linha));

            nAux.setSaida(cInicio.funcaoProp(nAux.getNet(),this.tipoFuncao));
        }

        for(posoculto=0;posoculto<nosOculto;posoculto++){
            net=0;
            for(j=0;j<nInicio.size();j++){
                nAux=nInicio.get(j);

                net+=nAux.getListaPesos().get(posoculto)*nAux.getNet();
            }
           
            nOculto.get(posoculto).setNet(net);
//            System.out.println("nOculto.get("+posoculto+").getNet()="+nOculto.get(posoculto).getNet());
            nOculto.get(posoculto).setSaida(cOculto.funcaoProp(nOculto.get(posoculto).getNet(),this.tipoFuncao));
//            System.out.println("nOculto.get("+posoculto+").getSaida()="+nOculto.get(posoculto).getSaida());
        }
        
        //definindo o net e a saida da camada de saida
        for(int i=0;i<nSaida.size();i++){
            net=0;
            for(j=0;j<nOculto.size();j++){
               nAux=nOculto.get(j);
//                System.out.println("nAux.getSaida().get("+j+")="+nAux.getSaida());
               net+=nAux.getSaida()*nAux.getListaPesos().get(i);
                
            }

//            System.out.println("nSaida.get("+i+").getNet()="+net);
            nSaida.get(i).setNet(net);
//            System.out.println("nSaida("+i+")="+cSaida.funcaoProp(nSaida.get(i).getNet()));
            nSaida.get(i).setSaida(cSaida.funcaoProp(nSaida.get(i).getNet(),this.tipoFuncao));
//            System.out.println("nSaida("+i+")="+nSaida.get(i).getSaida());
        }
       
    }
    
    public double erroSaida(double[] respostaEsperada){
        //camada de saida
        Camada cSaida=camadas.get(camadas.size()-1);
        //Pego os neuroneos da camada de saida
        List<Neuronio> neuroneos=cSaida.getNeuroneos();
        
        Neuronio neuroneo;
        Neuronio n;
        double erroTot=0.0;
        double erro;
        for(int i=0;i<neuroneos.size();i++){
            neuroneo=neuroneos.get(i);
//             System.out.println("neuronio.get("+i+")="+neuroneo.getNet());
//            System.out.println("Neuronio saida="+neuroneo.getSaida());
//            System.out.println("Resposta Esperada="+respostaEsperada[i]);
//            System.out.println("Subtração: (respostaEsperada[i]-neuroneo.getSaida()=="+(respostaEsperada[i]-neuroneo.getSaida()));
//            System.out.println("Multiplicação="+(respostaEsperada[i]-neuroneo.getSaida())*cSaida.derivadaProp(neuroneo.getNet()));
            erro=(respostaEsperada[i]-neuroneo.getSaida())*cSaida.derivadaProp(neuroneo.getNet(),this.tipoFuncao);
//            System.out.println("Erro de saida: "+erro);
//            System.out.println("");
      
            neuroneo.setErro(erro);
//            System.out.println("neuroneo.getErro() = "+neuroneo.getErro());
            //Pego a lista de neuroneos da camada oculta
            Camada cOculta=camadas.get(1);                                     
            List<Neuronio> nSaida=cOculta.getNeuroneos();
            
            //Adiciono o erro para a listaErros de cada um dos neuroneos da camada oculta
            for(int j=0;j<nSaida.size();j++){
                n=nSaida.get(j);
                n.getErroPesos().add(erro);
            }
            erroTot+=Math.pow(respostaEsperada[i]-neuroneo.getSaida(), 2)/2.0; // ou divido por 2
//            System.out.println("Erro Tot em "+i+"== "+erroTot);
     
        }
//        System.out.println("ErroTot="+erroTot);
        return erroTot;
    }
    
    public void erroOculta(){
        Camada cOculta=camadas.get(1);
        Camada cInicio=camadas.get(0);
        List<Neuronio> nOculta=cOculta.getNeuroneos();
        List<Neuronio> nInicio=cInicio.getNeuroneos();
        Neuronio n;
        for(int i=0;i<nOculta.size();i++){
            n=nOculta.get(i);
            double erro=0;    
         
            for(int j=0;j<n.getListaPesos().size();j++){
                erro+=n.getListaPesos().get(j)*n.getErroPesos().get(j)*cOculta.derivadaProp(n.getNet(),this.tipoFuncao);
               
            }
          
            n.setErro(erro);
            for(int k=0;k<nInicio.size();k++){
                Neuronio n2=nInicio.get(k);
                n2.getErroPesos().add(erro);
            }
        }
    }
    public void atualizaPesos(){
        double novoPeso;
//        System.out.println("****");
//        System.out.println("ATUALIZANDO OS PESOS");
//        System.out.println("*****");
        //Atualiza da camada Oculta
        Camada camada = camadas.get(1);
        List<Neuronio> neuronios = camada.getNeuroneos();
        Neuronio n;
        for(int j=0;j<neuronios.size();j++){
 
            n=neuronios.get(j);
            for(int k=0;k<n.getListaPesos().size();k++){
                
                novoPeso=n.getListaPesos().get(k)+(n.getErroPesos().get(k)*n.getSaida()*this.n);
                
                    n.getListaPesos().set(k, novoPeso);
            }
        }
        
        //Atualiza da camada de inicio
        camada = camadas.get(0);
        neuronios = camada.getNeuroneos();
        for(int j=0;j<neuronios.size();j++){
            n=neuronios.get(j);
            for(int k=0;k<n.getListaPesos().size();k++){     
              
                novoPeso=n.getListaPesos().get(k)+n.getErroPesos().get(k)*n.getNet()*this.n;

                n.getListaPesos().set(k, novoPeso);
            }
        }
        apagarInfo();
    }
    public void apagarInfo(){
        for (int i=0; i<camadas.size();i++) {
            Camada c = camadas.get(i);
            List<Neuronio> neuronios = c.getNeuroneos();
            Neuronio n;
            for(int j=0;j<neuronios.size();j++){
                n=neuronios.get(j);
                n.setSaida(0);
                n.setNet(0);
               
                n.getErroPesos().clear();
            }
        }
    }
    public RetornoTabela teste(String filename, int tipo){
        System.out.println("Entrei no MLP funcao teste");
        
        this.tipoFuncao=tipo;
        RetornoTabela r=RetornoTabela.getInstance();
        camadas=r.getCamadas();
        listatributos=r.getAtributos();
        Camada cSaida=camadas.get(camadas.size()-1);
        List<Neuronio> nSaida=cSaida.getNeuroneos();
        
        int indexMaior=0;
        double max=0.0;
        
        file=filename;
        lerArquivo(listatributos);
        nosInicio=r.getNentrada();
        nosSaida=r.getNsaida();
        nosOculto=r.getCamadas().get(1).getNeuroneos().size();
        System.out.println("Termnei de ler o arquivo");
        System.out.println("NoInicio= "+nosInicio);
        System.out.println("NosOculto= "+nosOculto);
        System.out.println("NosSaida="+nosSaida);
        
        iniciarMatriz(nosSaida);
        
        List<List<Double>> dados=entrada;
                
        int linha=0;
        while (linha<dados.get(1).size()) {
            calcularNet(dados,linha);
            double[] respostaEsperada=new double[nosSaida];

            //Quantidade de atributos(colunas) de entrada
            int qtdeColunas=nosInicio;
            int aux=0;
            int indexReal=0;
            
            for(int k=nosInicio;k<nosInicio+nosSaida;k++){
                respostaEsperada[aux]=dados.get(k).get(linha);
                if(respostaEsperada[aux]==1)
                        indexReal=aux;
                aux++;
            }
            indexMaior=0;
            max=nSaida.get(indexMaior).getSaida();
            for(int i=0;i<nSaida.size();i++){
                System.out.print("  "+i+" = "+nSaida.get(i).getSaida());
                if(nSaida.get(i).getSaida()>max){
                    indexMaior=i;
                    max=nSaida.get(i).getSaida();
                }
            }
            System.out.println("");
            //System.out.println("indexReal = "+indexReal+"  indexMaior = "+indexMaior);
            matConfusao[indexReal][indexMaior]++;
            
            linha++;
        }
            
        System.out.println("");
        System.out.println("MATRIZ DE CONFUSÃO");
        for(int i=0;i<matConfusao.length;i++){
            for(int j=0;j<matConfusao[i].length;j++){
                System.out.print(matConfusao[i][j]+" ");
            }
            System.out.println("");
        }
        r.setMatConfusao(matConfusao);
        
        System.out.println("Qtde = "+listatributos.size());
        return r;
    }
    
    public void iniciarMatriz(int qtdeClasse){
        matConfusao=new int [qtdeClasse][qtdeClasse];
    }
}
