package entidade;

import java.util.ArrayList;
import java.util.List;

public class Camada {
    private List<Neuronio> neuroneos;

    public Camada(List<Neuronio> neuroneos) {
        this.neuroneos = neuroneos;
    }
    
    public Camada(int nNos,int nNosProx){
        this.neuroneos=new ArrayList<Neuronio>();
        
        for(int i=0;i<nNos;i++){
            //Adiciono um neuroneo na camada que possui já as ligações com os neuroneos da camada seguinte
            this.neuroneos.add(new Neuronio(nNosProx));
        }
    }
    /*
        Cálculo da função de propagação
    */
    public double funcaoProp(double valor,int tipo){
        double result=0;
        
        switch(tipo){
            case 0: //linear
                result=valor/10.0;
                break;
            case 1: //logistico
                result=1.0 / (1 + Math.pow(Math.E, valor*(-1)));
                break;
            case 2: //tangente
                result=Math.tanh(valor);
                break;
        }
        return result;
    }
    /*
        Cálculo da derivada
    */
    public double derivadaProp(double valor,int tipo){
        double result=0;
        
        switch(tipo){
            case 0: //linear
                result=1/10.0;
                break;
            case 1: //logistico
                result=funcaoProp(valor,1) * (1 - funcaoProp(valor,1));
                break;
            case 2: //tangente
                result=1 - Math.pow(funcaoProp(valor,2), 2);
                break;
        }
        return result;
    }
    public List<Neuronio> getNeuroneos() {
        return neuroneos;
    }

    public void setNeuroneos(List<Neuronio> neuroneos) {
        this.neuroneos = neuroneos;
    }
}
