
package entidade;

import java.util.List;


public final class RetornoTabela {
    public static RetornoTabela instancia=null;
    private List<String> colunas;
    private List<List<Double>> entrada;
    private List<Camada> camadas;
    private Double [][] listaErros;
    private int[][] matConfusao;
    private int nentrada;
    private int nsaida;
    private List<String> atributos;
    
    public static RetornoTabela getInstance(){
        if(instancia==null)
            instancia=new RetornoTabela();
        return instancia;
    }

    
    
    public List<String> getColunas() {
        return colunas;
    }

    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    public List<List<Double>> getEntrada() {
        return entrada;
    }

    public void setEntrada(List<List<Double>> entrada) {
        this.entrada = entrada;
    }

    public int getNentrada() {
        return nentrada;
    }

    public void setNentrada(int nentrada) {
        this.nentrada = nentrada;
    }

    public int getNsaida() {
        return nsaida;
    }

    public void setNsaida(int nsaida) {
        this.nsaida = nsaida;
    }

    public List<Camada> getCamadas() {
        return camadas;
    }

    public void setCamadas(List<Camada> camadas) {
        this.camadas = camadas;
    }

    public int[][] getMatConfusao() {
        return matConfusao;
    }

    public void setMatConfusao(int[][] matConfusao) {
        this.matConfusao = matConfusao;
    }

    public Double[][] getListaErros() {
        return listaErros;
    }

    public void setListaErros(Double[][] listaErros) {
        this.listaErros = listaErros;
    }

    public List<String> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<String> atributos) {
        this.atributos = atributos;
    }
    
    

    
    
    @Override
    public String toString() {
        String ent="\""+"entrada"+"\""+":";
        String coluna="\""+"colunas"+"\""+":";
        coluna+="[";
        for(int i=0;i<colunas.size()-1;i++){
            coluna+="\""+colunas.get(i)+"\",";
        }
        coluna+="\""+colunas.get(colunas.size()-1)+"\""; 
        coluna+="]";
        ent+="[";
        for(int i=0;i<entrada.size();i++){
            ent+="[";
            for(int j=0;j<entrada.get(i).size();j++){
                ent+=""+entrada.get(i).get(j);
                if(j!=entrada.get(i).size()-1)
                    ent+=",";
            }
            ent+="]";
            if(i!=entrada.size()-1)
                ent+=",";
        }
        ent+="]";
        String respentrada="\""+"nentrada"+"\""+":"+nentrada;
        String respsaida="\""+"nsaida"+"\""+":"+nsaida;
        return "{" +coluna+","+ent+","+respentrada+","+respsaida+"}";
    }
    
    public String getResultMatrizConfusao(){
        String retorno="\""+"matConfusao"+"\""+":";
        retorno+="[";
        for(int i=0;i<matConfusao.length;i++){
            retorno+="[";
            for(int j=0;j<matConfusao[i].length;j++){
                retorno+=""+matConfusao[i][j];
                
                if(j!=matConfusao[i].length-1)
                    retorno+=",";
            }
            retorno+="]";
            if(i!=matConfusao.length-1)
                retorno+=",";
        }
        retorno+="]";
        String resposta="{"+retorno+"}";
        return resposta;
    }
    public String getErros(){
        String retorno="\""+"matErro"+"\""+":";
        retorno+="[";
        for(int i=0;i<listaErros.length;i++){
            retorno+="[";
            for(int j=0;j<listaErros[i].length;j++){
                retorno+=""+listaErros[i][j];
                
                if(j!=listaErros[i].length-1)
                    retorno+=",";
            }
            retorno+="]";
            if(i!=listaErros.length-1)
                retorno+=",";
        }
        retorno+="]";
        String resposta="{"+retorno+"}";
        return resposta;
    }
}
