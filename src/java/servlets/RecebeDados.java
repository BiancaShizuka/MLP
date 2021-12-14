/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import entidade.MLP;
import entidade.RetornoTabela;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(
    location="/", 
    fileSizeThreshold=1024*1024,    // 1MB *      
    maxFileSize=1024*1024*100,      // 100MB **
    maxRequestSize=1024*1024*10*10  // 100MB ***
)
@WebServlet(name = "RecebeDados", urlPatterns = {"/RecebeDados"})
public class RecebeDados extends HttpServlet {

    public String gravarArquivo(Part arquivo){
        try{
            if(arquivo.getSize()>0){
                byte[] arq=new byte[(int)arquivo.getSize()];
                arquivo.getInputStream().read(arq);
                // cria um arquivo com o mesmo nome da foto e grava o vetor como seu conteúdo                 + "/" +
                File file=new File(getServletContext().getRealPath("/")+"arquivoscsv");
                System.out.println(getServletContext().getRealPath("/")+"arquivoscsv");
                if(!file.exists()){
                    if(file.mkdir())
                    System.out.println("nao existe uma pasta para esse anuncio e criei");
                    else
                        System.out.println("nao consegui criar");
                }
                
                //Ver a imagem com esse nome já existe
                File fileImg=new File(getServletContext().getRealPath("/") + "\\arquivoscsv\\" + 
                        arquivo.getSubmittedFileName()+".csv");
                if(fileImg.exists()){//se existe exclui
                    fileImg.delete();
                    System.out.println("file existe");
                }
                
                FileOutputStream novoarquivo = new FileOutputStream(new File(getServletContext().getRealPath("/") + "arquivoscsv\\" + 
                        arquivo.getSubmittedFileName()));
                novoarquivo.write(arq);
                novoarquivo.close();
                return getServletContext().getRealPath("/") + "arquivoscsv\\" + 
                        arquivo.getSubmittedFileName();
                
            }
        }catch(Exception e){
            System.out.println("Erro: "+e.getMessage());
        }
        return null;
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        MLP mlp=new MLP();
        RetornoTabela r=null;
        String erro="";
        List<String> listAtrib=null;
        Gson gson=new Gson();
        
        System.out.println("Ver se está atualizando os códigos");
        String qtdeOculto=request.getParameter("qtde-oculto");
        String qtdeSaida=request.getParameter("qtde-saida");
        String qtdeEntrada=request.getParameter("qtde-entrada");
        String valorErro=request.getParameter("valor-erro");
        String valorTxap=request.getParameter("valor-txap");
        String valorIteracao=request.getParameter("valor-iteracao");
        String tipofuncao=request.getParameter("tipo-funcao");
        String acao=request.getParameter("acao");
        System.out.println("acao = "+acao);
        System.out.println("tipofuncao = "+tipofuncao);
        if(request.getParameter("listatributos")!=null){
     
            String atributo=request.getParameter("listatributos");
            String[] patrib=atributo.split(",");
        
            listAtrib=new ArrayList<>(patrib.length);
            for(int i=0;i<patrib.length;i++){
             
                listAtrib.add(patrib[i]);
            }
        }
        System.out.println("testando servlet");
        switch(acao){
           
            case "montarmodelo":
                        System.out.println("passei");
                        Part arquivo = request.getPart("arquivo");
                        if(qtdeOculto.isEmpty()){
                            erro+="<p>Quantidade está vazio</p>";
                        }
                        else{
                            if(Integer.parseInt(qtdeOculto)<=0){
                                erro+="<p>Quantidade é um valor inválido</p>";
                            }

                        }
                        String nome=gravarArquivo(arquivo);
                        if(nome==null){
                            erro+="<p>Arquivo não foi selecionado</p>";
                        }
                        else
                            r=mlp.executar(nome, Integer.parseInt(qtdeOculto),listAtrib);
                        
                        if(!erro.isEmpty())
                            response.getWriter().print(erro);
                        else
                            response.getWriter().print(r.toString());
                        break;
            case "treinar":r=mlp.iniciarTreino(Integer.parseInt(qtdeOculto),
                                            Integer.parseInt(valorIteracao),
                                            Double.parseDouble(valorErro),
                                            Double.parseDouble(valorTxap),
                                            Integer.parseInt(tipofuncao));
                            response.getWriter().print(r.getErros());
                           break;
                       
            case "validar":
                    System.out.println("Entrei para testar");
                    Part arquivoteste = request.getPart("arquivoteste");
                    String nometeste=gravarArquivo(arquivoteste);

                    if(nometeste==null){
                        erro+="<p>Arquivo de teste não foi selecionado</p>";
                    }
                    r=mlp.teste(nometeste,Integer.parseInt(tipofuncao));
                    System.out.println("Retorno");
                    System.out.println(r.getResultMatrizConfusao());
                    response.getWriter().print(r.getResultMatrizConfusao());
                    break;
                    
            case"pegaratributos":
                        arquivo = request.getPart("arquivo");
                        
                        nome=gravarArquivo(arquivo);
                        if(nome==null){
                            erro+="<p>Arquivo não foi selecionado</p>";
                        }
                        else{
                            List<String> atributos=mlp.pegarAtributos(nome);
                            response.getWriter().print(gson.toJson(atributos));
                        }

                        
                
                        break;
        }
       
       
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
