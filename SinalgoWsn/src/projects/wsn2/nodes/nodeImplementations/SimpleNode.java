/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.wsn2.nodes.nodeImplementations;

import java.awt.Color;
import java.util.Random;
import projects.wsn2.nodes.messages.WsnMsg;
import projects.wsn2.nodes.timers.WsnMessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

/**
 *
 * @author pozza/aleciom
 */
public class SimpleNode extends Node {

    //Armazenar o nó que sera usado para alcançar a Estacao-Base
    private Node proximoNoAteEstacaoBase;
    //Armazena o número de sequencia da última mensagem recebida
    private Integer sequenceNumber = 0;
    //Armazena qual o tipo de mensagem tipo 0 rota tipo 1 constroi roteamento
    private Integer mensagem = 0;
    //Armazena a quantidade de vezes que a mensagem foi repassada
    private Integer contador = 0;
    //Valor randomico gerado para envio de pacotes
    private Random randomico = new Random();
    //Armazena intervalo que uma mensagem demora para ser enviada
    private Integer intervalo;
    //Armazena o sinkNode que começa o roteamento
    private Node sinkNode; 
    //Armazena a quantidade do proximo no ate o destino
    private Integer proximoNoAteDestino = 0;
    
    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof WsnMsg) {
                Boolean encaminhar = Boolean.TRUE;
                WsnMsg wsnMessage = (WsnMsg) message;
                wsnMessage.saltosAteDestino ++;
                //System.out.println(this.ID+" recebe dados de "+wsnMessage.origem.ID);
                if (wsnMessage.forwardingHop.equals(this)) { // A mensagem voltou. O no deve descarta-la
                    encaminhar = Boolean.FALSE;
                } else if (wsnMessage.tipoMsg == 0) { // A mensagem é um flood. Devemos atualizar a rota
                    //comentei porque não tive como testar talvez no eclipse funcione, porém como não testei, comentei, no meu netbenas esta descomentado
                   // proximoNoAteDestino = wsnMessage.saltosAteDestino;
                    if (proximoNoAteEstacaoBase == null) {                        
                        proximoNoAteEstacaoBase = inbox.getSender();
                        sequenceNumber = wsnMessage.sequenceID;                         
                        
                        if(wsnMessage.saltosAteDestino < proximoNoAteDestino){ //condição para verificar  se saltosatedestino era menor que proximonoatedestino
                            
                            proximoNoAteEstacaoBase = inbox.getSender();
                        
                        }                        
                        
                    } else if (sequenceNumber < wsnMessage.sequenceID) {
                    //Recurso simples para evitar loop.
                        //Exemplo: Noh A transmite em brodcast. Noh B recebe a
                        //msg e retransmite em broadcast.
                        //Consequentemente, noh A irá receber a msg. Sem esse
                        //condicional, noh A iria retransmitir novamente, gerando um loop
                        sequenceNumber = wsnMessage.sequenceID;
                    } else {
                        encaminhar = Boolean.FALSE;
                    }
                }else{
                    encaminhar = Boolean.FALSE;
                }
                if (encaminhar) {          
                    //proximoNoAteEstacaoBase = inbox.getSender();
                    //Devemos alterar o campo forwardingHop(da mensagem) para armazenar o
                    //noh que vai encaminhar a mensagem.                    
                    System.out.println("Node "+this.ID+" recebe pacote de rota do Sink");
                    wsnMessage.forwardingHop = this;
                    broadcast(wsnMessage);                 
                }
                sinkNode = wsnMessage.origem;
                
                if(wsnMessage.tipoMsg == 1 ){                       
                    send(wsnMessage, proximoNoAteEstacaoBase);
                }
            }
        }
    }

    @Override
    public void preStep() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      
    }
    @Override
    public void init() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.setColor(Color.GREEN);
        intervalo = 50+randomico.nextInt(150);
        System.out.println("Node "+this.ID+" tem envio de mensagem marcado para " +intervalo+" rounds");
    }

    @Override
    public void neighborhoodChange() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStep() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(sinkNode != null){
            if(contador >= intervalo){
                WsnMsg wsnMessage = new WsnMsg(mensagem, this, sinkNode, this, 1);
                //System.out.println("saltos para o proximo");
                send(wsnMessage, proximoNoAteEstacaoBase);
                mensagem++;
                contador = 0;
            }
            contador ++;
            
        }
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
