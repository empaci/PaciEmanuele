/*
===============================================================
RobotControllerMapper.java
implements the business logic by handling messages received on the cmdsocket-8091

===============================================================
*/
package it.unibo.resumableBoundaryWalker.wenv;

import it.unibo.resumableBoundaryWalker.interaction.IssObserver;
import it.unibo.resumableBoundaryWalker.supports.IssCommSupport;
import org.json.JSONObject;

public class RobotInputController implements IssObserver {
    private IssCommSupport     commSupport;  //IssArilRobotSupport
    private ResumableLogic     resLogic;
    private boolean firstTime = true;

    //public enum robotLang {cril, aril}    //todo

    public RobotInputController(IssCommSupport support, boolean usearil, boolean doMap){
        commSupport = support;
        resLogic = new ResumableLogic(support,usearil, doMap);
     }

    @Override
    public void handleInfo(String infoJson) {
        handleInfo( new JSONObject(infoJson) );
    }

    /*
    ENTRY
    Hhandler of the messages sent by WENv over the cmdsocket-8091 to notify:
    - the answer to a robot-command move {"endmove":"RESULT", "move":MOVE}
    - the information emitted by a sonar { "sonarName": "sonarName", "distance": 1, "axis": "x" }
    - a collision between the robot and an obstacle { "collision" : "false", "move": "moveForward"}
     */
    @Override
    public synchronized void  handleInfo(JSONObject infoJson) {
        System.out.println("RobotInputController | handleInfo:" + infoJson  );
        if( infoJson.has("endmove") )        handleEndMove(infoJson);
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if (infoJson.has("robotcmd") )  handleGUIcommand(infoJson);
    }

    private void handleGUIcommand(JSONObject infoJson) {
        String op = (String) infoJson.get("robotcmd");
        if (op.equals("RESUME")) {
            System.out.println("RobotInputController | RESUME");
            if (firstTime) {
                resLogic.start();
                firstTime = false;
            } else {
                resLogic.doResume();
            }
        } else if (op.equals("STOP")) {
            System.out.println("RobotInputController | STOP");
            resLogic.pause();
        }
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        //System.out.println("RobotInputController | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotInputController | handleCollision move=" + move  );
    }
    protected void handleEndMove(JSONObject endmove ){
        String answer = (String) endmove.get("endmove");
        String move   = (String) endmove.get("move");   //moveForward, ...
        System.out.println("RobotInputController | handleEndMove:" + move + " answer=" + answer);
        switch( answer ){
            //case "true"       : robotBehaviorLogic.boundaryStep( move, false );break;
            //case "false"      : robotBehaviorLogic.boundaryStep( move, true  );break;
            case "halted"     : System.out.println("RobotInputController | handleEndMove to do halt" );break;
            case "notallowed" : System.out.println("RobotInputController | handleEndMove to do notallowed" );break;
            default           : System.out.println("RobotInputController | handleEndMove IMPOSSIBLE answer for move=" + move);
        }
    }

}
