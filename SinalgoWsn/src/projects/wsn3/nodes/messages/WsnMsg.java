/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.wsn3.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

/**
 *
 * @author pozza/aleciom
 */
public class WsnMsg extends Message {
  

    // Message Identifier
    public Integer sequenceID;

    // Package's Life Time
    public Integer plt;

    // Destination Node
    public Node destination;

    // Origin Node
    public Node origin;

    // Forwarding Node
    public Node forwardingHop;

    // Number of Hops to Destination
    public Integer hops;

    // Package Type. 0 for Route Establishment and 1 for Data Packets
    public Integer messageType = 0;

    // Class Constructor
    public WsnMsg(Integer seqID, Node origin, Node destination, Node forwardingHop, Integer type) {
        this.sequenceID = seqID;
        this.origin = origin;
        this.destination = destination;
        this.forwardingHop = forwardingHop;
        this.messageType = type;
    }

    // Message Structure Clone
    @Override
    public Message clone() {
        WsnMsg msg = new WsnMsg(
                this.sequenceID, // Sequence ID
                this.origin, // Origin Node
                this.destination, // Destination Node
                this.forwardingHop, // Forwarding Node
                this.messageType // Message Type
        );
        // Sets the Package's Life Time
        msg.plt = this.plt;
        // Sets the Number of Hops to Destination
        msg.hops = hops;
        // Returns the Message Structure
        return msg;
    }


}
