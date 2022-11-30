/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.wsn2.nodes.timers;

import projects.wsn2.nodes.messages.WsnMsg;
import projects.wsn2.nodes.nodeImplementations.SinkNode;
import sinalgo.nodes.timers.Timer;

/**
 *
 * @author pozza
 */
public class WsnMessageTimer extends Timer {

    private WsnMsg message = null;

    public WsnMessageTimer(WsnMsg message) {
        this.message = message;
    }

    @Override
    public void fire() {
        ((SinkNode)node).broadcast(message);
    }

}
