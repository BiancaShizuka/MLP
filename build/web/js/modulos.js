var chart;

function EnviarArquivo(){
    event.preventDefault(); // evita refresh da tela
   
   const URL_TO_FETCH = 'RecebeDados';
   var formData = new FormData(document.getElementById("fdados"));
   formData.append('acao', 'pegaratributos'); 
   
   fetch(URL_TO_FETCH, { method: 'post',body: formData }).then(function (response) {
       response.text().then(function(result)  //response é um promisse
        {
           
            var div=document.getElementById("div-checkbox");
            var res="";
            obj=JSON.parse(result);
        
            for(let i=0;i<obj.length;i++){
                res+="<div><input type='checkbox' id='"+obj[i]+"' name='check' value='"+obj[i]+"' checked>";
                res+="<label for='"+obj[i]+"'> "+obj[i]+"</label></div>"
  
            }
           
            div.innerHTML=res;
            
        });
      
        
   }).catch(function (error) {
        console.error(error);
   });

}
function GravarFormulario(){
    event.preventDefault(); // evita refresh da tela
    var divExibir=document.getElementById("divExButtons");
    var divTabela=document.getElementById("divArquivo");
    var divGrafico=document.getElementById("myChart");
    var divMatriz=document.getElementById("matriz");
    
    divTabela.style.display="block";
    divGrafico.style.display="none";
    divMatriz.style.display="none";
    divExibir.style.display="block";
    
    var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");
    
    btnDados.style.background="#66ccff";
    btnGrafico.style.background="#ffffff";
    btnMatriz.style.background="#ffffff";
    
    const URL_TO_FETCH = 'RecebeDados';
    var msg=document.getElementById("div-msg");
    var formData = new FormData(document.getElementById("fdados"));
    var qtde_entrada=document.getElementById("qtde-entrada");
    var qtde_saida=document.getElementById("qtde-saida");
    formData.append('acao', 'montarmodelo');
   
    //pegar os atributos do checkbox e colocar no form
    var listatributos=[]
    var atributos=document.getElementsByName("check");
    for(let i=0;i<atributos.length;i++){
        if(atributos[i].checked){
            listatributos.push(atributos[i].value);
        } 
    }
    formData.append("listatributos",listatributos);
   
   
   var obj_tabelas;
   fetch(URL_TO_FETCH, { method: 'post',body: formData }).then(function (response) {
       response.text().then(function(result)  //response é um promisse
        {
           
            if (result.length>0 && result.startsWith('<p>')){
                msg.innerHTML=result;
            }
            else{
                msg.innerHTML="";
               
                obj=JSON.parse(result);
                console.log(obj);
                qtde_entrada.value=obj.nentrada;
                qtde_saida.value=obj.nsaida;
          
                var table=document.getElementById("table-dados");
                let conteudo="";
                conteudo+="<thead>";
                conteudo+="<tr>";
                for(let i=0;i<obj.colunas.length;i++){
                    conteudo+="<th>"+obj.colunas[i]+"</th>"
                }
                conteudo+="</tr>";
                conteudo+="</thead>";
                conteudo+="<tbody>";
                for(let i=0;i<obj.entrada[0].length;i++){
                   conteudo+="<tr>"; 
                   for(let j=0;j<obj.entrada.length;j++){
                  
                        if(obj.colunas[j].startsWith("saida"))
                            conteudo+="<td>"+parseFloat(obj.entrada[j][i]).toFixed(0)+"</td>";
                        else
                            conteudo+="<td>"+parseFloat(obj.entrada[j][i]).toFixed(4)+"</td>"
                   }
                   conteudo+="</tr>";
                }
                conteudo+="</tbody>";
              
                table.innerHTML=conteudo;
            }

            
        });
      
        
   }).catch(function (error) {
        console.error(error);
   });
}

function addData(label, data) {
    chart.data.labels.push(label);
    chart.data.datasets.forEach((dataset) => {
        dataset.data.push(data);
    });
    chart.update();
}

function removeData() {
    chart.data.labels.pop();
    chart.data.datasets.forEach((dataset) => {
        dataset.data.pop();
    });
    chart.update();
}

function Treinar(){
    event.preventDefault(); // evita refresh da tela
   
   var divMatriz=document.getElementById("matriz");
   var divTabela=document.getElementById("divArquivo");
   var divGrafico=document.getElementById("myChart");
   
   var btnDados=document.getElementById("btn-dados");
   var btnGrafico=document.getElementById("btn-grafico");
   var btnMatriz=document.getElementById("btn-matriz");
   
   btnDados.style.background="#66ccff";
   
   divTabela.style.display="none";
   divMatriz.style.display="none";
   divGrafico.style.display="block";
   
    var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");

    btnDados.style.background="#ffffff";
    btnGrafico.style.background="#66ccff";
    btnMatriz.style.background="#ffffff";
   
   const URL_TO_FETCH = 'RecebeDados';
   var formData = new FormData(document.getElementById("fdados"));
   formData.append('acao', 'treinar'); 
   var obj_tabelas;
   fetch(URL_TO_FETCH, { method: 'post',body: formData }).then(function (response) {
       response.text().then(function(result)  //response é um promisse
        {  
            
            console.log(result);
            obj=JSON.parse(result);
            console.log(obj);
            var iteracao=[]
            var erros=[]
            
            for(var i=0;i<obj.matErro.length;i++){
                if(obj.matErro[i][0]!=0 || obj.matErro[i][0]!=null){
                    iteracao.push(obj.matErro[i][0]);
                    erros.push(obj.matErro[i][1]);
                }
            }
            let ctx = document.getElementById('myChart');
            if(chart===undefined){
                console.log("não tem grafico");
                chart=new Chart(ctx, {
                    type: 'line',
                    data: {
                        // Legendas das Linhas
                        labels: iteracao,
                        datasets: [{
                            // Legenda
                            label: 'Erro',
                            // Define-se a cor da linha.
                            borderColor: 'rgb(245,222,179)',
                            // Dados a serem inseridos nas barras
                            data: erros
                        }]
                    },
                    options: {
                        elements: {
                            line: {
                                tension: 0
                            }
                        }
                    }
                });
            }
            else{
                console.log("Grafo ja existia");
                chart.data.datasets[0].data = erros;
                chart.data.labels = iteracao;
                chart.update();

            }
        });
      
        
   }).catch(function (error) {
        console.error(error);
   });
}

function Testar(){
    event.preventDefault(); // evita refresh da tela
   var mat=document.getElementById("matriz");
   var divTabela=document.getElementById("divArquivo");
   var divGrafico=document.getElementById("myChart");
   
   divTabela.style.display="none";
   divGrafico.style.display="none"
   
   var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");

    btnDados.style.background="#ffffff";
    btnGrafico.style.background="#ffffff";
    btnMatriz.style.background="#66ccff";
   
   const URL_TO_FETCH = 'RecebeDados';
   var formData = new FormData(document.getElementById("fdados"));
   formData.append('acao', 'validar'); 
   var obj_tabelas;
   
   
    var divMatriz=document.getElementById("matriz");
    divMatriz.style.display="block";
   fetch(URL_TO_FETCH, { method: 'post',body: formData }).then(function (response) {
       response.text().then(function(result)  //response é um promisse
        {  
            console.log(result);
            obj=JSON.parse(result);
            console.log(obj);
            
            var exibir="<h2>Matriz de Confusão</h2>";
            exibir+="<div class='div_matresp'>";
            
            exibir+="<div class='div_linharesp'>";
            exibir+="<div class='div_colunanome'><p></p></div>";
            for(var j=0;j<obj.matConfusao.length;j++){
                exibir+="<div class='div_colunanome'><p>"+"Classe "+j+"</p></div>";
            }
            exibir+="</div>";
            
            var soma=0;
            var totCorretos=0;
            var tot=0;
            var correto=0;
            for(var i=0;i<obj.matConfusao.length;i++){
                exibir+="<div class='div_linharesp'>";
                exibir+="<div class='div_colunanome'><p>"+"Classe "+i+"</p></div>";
                soma=0;
                for(var j=0;j<obj.matConfusao.length;j++){
                    tot+=obj.matConfusao[i][j];
                    soma+=obj.matConfusao[i][j];
                    exibir+="<div class='div_colunaresp'><p>"+obj.matConfusao[i][j]+"</p></div>";
                    if(i==j){
                        totCorretos+=obj.matConfusao[i][j];
                        correto=obj.matConfusao[i][j];
                    }
                }
                exibir+="<div class='div_acuracia'>"+"Acurácia da Classe "+i+": "+(correto/soma*100)+"%</div>";
                exibir+="</div>";
            }
            exibir+="</div>";
            
            mat.innerHTML=""+exibir+"<p>Acurácia Geral="+((totCorretos/tot)*100)+"%</p>";
            
        });
      
        
   }).catch(function (error) {
        console.error(error);
   });
}

function mostrarDivDados(){
    var divTabela=document.getElementById("divArquivo");
    var divGrafico=document.getElementById("myChart");
    var divMatriz=document.getElementById("matriz");
    console.log("entrei na div para abrir dados");
    divTabela.style.display="block";
    divGrafico.style.display="none";
    divMatriz.style.display="none";
    
    var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");

    btnDados.style.background="#66ccff";
    btnGrafico.style.background="#ffffff";
    btnMatriz.style.background="#ffffff";
    /*
    divTabela.style.visibility="visible";
    divGrafico.style.visibility="hidden";
    divMatriz.style.visibility="hidden";*/
}

function mostrarDivGrafico(){
    var divTabela=document.getElementById("divArquivo");
    var divGrafico=document.getElementById("myChart");
    var divMatriz=document.getElementById("matriz");
    divTabela.style.display="none";
    divGrafico.style.display="block";
    divMatriz.style.display="none";
    
    var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");

    btnDados.style.background="#ffffff";
    btnGrafico.style.background="#66ccff";
    btnMatriz.style.background="#ffffff";
    /*
    console.log("entrei na div para abrir grafico");
    divTabela.style.visibility="hidden";
    divGrafico.style.visibility="visible";
    divMatriz.style.visibility="hidden";*/
}

function mostrarDivMatriz(){
    var divTabela=document.getElementById("divArquivo");
    var divGrafico=document.getElementById("myChart");
    var divMatriz=document.getElementById("matriz");
    divTabela.style.display="none";
    divGrafico.style.display="none";
    divMatriz.style.display="block";
    
    var btnDados=document.getElementById("btn-dados");
    var btnGrafico=document.getElementById("btn-grafico");
    var btnMatriz=document.getElementById("btn-matriz");

    btnDados.style.background="#ffffff";
    btnGrafico.style.background="#ffffff";
    btnMatriz.style.background="#66ccff";
    
}