/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.wsn3.nodes.nodeImplementations;

import java.awt.Color;
import projects.wsn3.nodes.messages.WsnMsg;
import projects.wsn3.nodes.timers.WsnMessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class SinkNode extends Node {

    // Stores the node that will be used to reach the Base Station
    private Node nextNodeToBaseStation = null;

    // This method is called only once, when the node is created
    @Override
    public void init() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // This method is called only once, before a step is executed
    @Override
    public void preStep() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // This method is called only once, after a step is executed
    @Override
    public void postStep() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // Builds the Message Routing
    @NodePopupMethod(menuText = "Construir Roteamento")
    public void buildRouting() {
        // Next Node to Base Station Receives Current Node
        this.nextNodeToBaseStation = this;
        // Creates a new message
        WsnMsg msg = new WsnMsg(
                1, // Message Type (Data) | Sequence ID
                this, // Origin Node
                null, // Destination Node
                this, // Router Node
                0 // Message Type (Routing)
        );
        // Sets the timer which will be used to determine the Rounds
        WsnMessageTimer timer = new WsnMessageTimer(msg);
        // Starts the timer with the Round and the broadcast node
        timer.startRelative(1, this);
    }

    // Handles the Messages
    @Override
    public void handleMessages(Inbox inbox) {
        // Walks through each message in the inbox
        while (inbox.hasNext()) {
            // Gets the next message
            Message message = inbox.next();
            // Checks if the message is a WsnMsg
            if (message instanceof WsnMsg) {
                // Gets the WsnMsg
                WsnMsg wsnMessage = (WsnMsg) message;
                // If the message is a data message, it will be forwarded to the next node
                if (wsnMessage.messageType == 1) {
                    if (wsnMessage.destination.equals(this)) {
                        // Simple Node Instance (Gets the Origin Node)
                        SimpleNode simpleNode = (SimpleNode) wsnMessage.origin;
                        System.out.println("Sink "+ wsnMessage.destination.ID+" recebe pacote " +wsnMessage.sequenceID +" de dados do Node " + wsnMessage.origin.ID);
                    }
                }
            }
        }
    }

    // Changes the neighbor nodes, which will be used to send the message back to
    // the sink node
    @Override
    public void neighborhoodChange() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // Requirements to be implemented
    @Override
    public void checkRequirements() throws WrongConfigurationException {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

}