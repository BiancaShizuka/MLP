
package entidade;

import java.util.ArrayList;
import java.util.List;


public class Neuronio {
    //netN
    private double net;
    //iN
    private double saida;
    //erroN
    private double erro;
    //Os pesos para cada neuroneo da camada seguinte
    private List<Double> listaPesos;
    //os erros de cada neuroneo da camada seguinte
    private List<Double> erroPesos;
    
    public Neuronio(int nNosProx) {
        this.listaPesos=new ArrayList<Double>();
        this.erroPesos=new ArrayList<Double>();
        int sorteado=0;
        //Adciona pesos aleatórios
        for (int i = 0; i < nNosProx; i++){
            sorteado=-1 + (int)(2 * Math.random());
         
            listaPesos.add(sorteado*(1.0));
        }
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public double getSaida() {
        return saida;
    }

    public void setSaida(double saida) {
        this.saida = saida;
    }

    public double getErro() {
        return erro;
    }

    public void setErro(double erro) {
        this.erro = erro;
    }

    public List<Double> getListaPesos() {
        return listaPesos;
    }

    public void setListaPesos(List<Double> listaPesos) {
        this.listaPesos = listaPesos;
    }

    public List<Double> getErroPesos() {
        return erroPesos;
    }

    public void setErroPesos(List<Double> erroPesos) {
        this.erroPesos = erroPesos;
    }
}
