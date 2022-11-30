/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.wsn3.nodes.nodeImplementations;

import java.awt.Color;
import java.util.Random;

import projects.wsn3.nodes.messages.WsnMsg;
import projects.wsn3.nodes.timers.WsnMessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Node.NodePopupMethod;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;


/*
	@authors: pozza/aleciom
*/

// Simple Node Class - Redirects messages to other nodes
public class SimpleNode extends Node {

    // Stores the sequence number of the last message received
    private Integer sequenceNumber = 0;

    // Stores the node that will be used to reach the Base Station
    private Node nextNodeToBaseStation;

    // Sender Flag
    Boolean senderFlag = Boolean.FALSE;

    // This method is called only once, when the node is created
    @Override
    public void init() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // This method is called only once, before a step is executed
    @Override
    public void preStep() {
        // Checks if the next node to base station exists and is available to send a
        // message
        if (nextNodeToBaseStation != null && !senderFlag) {
            // Increments the sequence number
            this.sequenceNumber++;
            // Creates a new message
            WsnMsg msg = new WsnMsg(
                    this.sequenceNumber, // Message Type (Routing) | Sequence ID
                    this, // Origin Node
                    this.nextNodeToBaseStation, // Destination Node
                    this, // Router Node
                    1 // Message Type (Data)
            );
            // Sets the timer which will be used to determine the Rounds
            WsnMessageTimer timer = new WsnMessageTimer(msg);
            // Starts the timer with the Round and the broadcast node
            timer.startRelative(new Random().nextInt(200) + 200, this);
            // Sets the sender flag
            senderFlag = true;
        }
    }

    // This method is called only once, after a step is executed
    @Override
    public void postStep() {
        // throw new UnsupportedOperationException("Not supported yet.");
        // To change body of generated methods, choose Tools | Templates.
    }

    // Gets the Next Node To Base Station
    public Node getNextNodeToBaseStation() {
        return this.nextNodeToBaseStation;
    }

    // Activated By The Timer (Sets the Sender Flag)
    public void setFlag(Boolean flag) {
        this.senderFlag = flag;
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
                // Determines if the node is sending the message
                Boolean send = Boolean.TRUE;
                // Router Assistant
                Boolean routerAssistant = Boolean.TRUE;
                // Gets the WsnMsg
                WsnMsg wsnMessage = (WsnMsg) message;
                // If the next receiver is the current node, it will ignore the message
                if (wsnMessage.forwardingHop.equals(this)) {
                    // Ignores the message
                    send = Boolean.FALSE;
                } else if (wsnMessage.messageType == 0) { // Flooded Message, the route shall be updated
                    // Checks if the message is the first message
                    if (nextNodeToBaseStation == null) {
                        // Sets the next node to the base station
                        nextNodeToBaseStation = inbox.getSender();
                        // Gets the sequence number of the message
                        sequenceNumber = wsnMessage.sequenceID;
                    } else if (sequenceNumber < wsnMessage.sequenceID) {
                        // Simple feature to avoid looping
                        // For Example: Node A broadcasts a transmission. Node B receives the message
                        // and...
                        // ...broadcasts it back to Node A
                        // Without this feature, the message would be looped back to Node B, generating
                        // a loop
                        sequenceNumber = wsnMessage.sequenceID;
                    } else {
                        // Ignores the message (If the package is neither new nor sequential)
                        send = Boolean.FALSE;
                    }
                    // If the message is a data message, it will be forwarded to the next node
                } else if (wsnMessage.messageType == 1) {
                    if (wsnMessage.destination.equals(this)) {
                        wsnMessage.destination = this.nextNodeToBaseStation;
                        routerAssistant = Boolean.FALSE;
                    } else {
                        send = Boolean.FALSE;
                    }
                }
                // Checks if the current node will be sending the message
                if (send) {
                    // Accepts the Routing Message Packet (Broadcasts)
                    if (routerAssistant) {
                        Tools.appendToOutput(this.ID + " receives data from " + wsnMessage.origin.ID + "\n");
                        System.out.println(this.ID + " receives data from " + wsnMessage.origin.ID + "\n");
                    }
                    // We must change the forwardingHop field (of the message) to store the node
                    // wich will be...
                    // ...sending the message
                    wsnMessage.forwardingHop = this;
                    broadcast(wsnMessage);
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