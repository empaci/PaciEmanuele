package it.unibo.cautiousExplorerActors;

import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousExplorerActor extends ActorBasicJava {
    final String forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 150}";
    final String backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 150}";
    final String turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, exploring, obstacle, end };
    private  IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );
    private State curState       =  State.start ;
    private int stepNum          = 1;
    private int sideLength = 1;
    private int sidelengthCounter    = 0;
    private RobotMovesInfo moves = new RobotMovesInfo(true);
    

    public CautiousExplorerActor(String name) {
        super(name);
    }

    @Override
    protected void handleInput(String msg) {
        if( msg.equals("startApp"))  explore("","");
        else msgDriven( new JSONObject(msg) );
    }

    private void explore(String move, String endmove)  {
        System.out.println("move " + move + " endmove " + endmove);
        switch( curState ) {
            case start: {
                stepNum=0;
                turnRight();
                doStep();
                sidelengthCounter++;
                curState = State.exploring;
                break;
            }
            case exploring: {
                if (move.equals("moveForward") && endmove.equals("true")) {
                    if (sidelengthCounter == sideLength) {
                        turnRight();
                        stepNum++;
                        sidelengthCounter=0;
                    }
                    if (stepNum>=4) {
                        curState = State.start;
                        sideLength++;
                        sidelengthCounter = 0;
                        turnLeft();
                    } else {
                        doStep();
                        sidelengthCounter++;
                    }
                }
                else if (move.equals("moveForward") && endmove.equals("false")) {
                    curState = State.obstacle;
                    doStep();
                } else { }//System.out.println("IGNORE answer of turnLeft"); }
                break;
            }
            case obstacle :
                //go back
                System.out.println("Obstacle! stepNum: " + stepNum + " counter: " + sidelengthCounter + " radius: " + sideLength);
                if (stepNum>0 && sidelengthCounter==0) {
                    turnLeft();
                    sidelengthCounter = sideLength;
                    stepNum--;
                }
                if (sidelengthCounter > 0 ) {
                    goBack();
                    sidelengthCounter--;
                }
                if (stepNum==0 && sidelengthCounter==0) {
                    curState = State.end;
                    turnLeft();
                }
                //moves.showRobotMovesRepresentation();
                break;
            case end : {
                System.out.println("END");
            }
            default: System.out.println("error");
        }
    }

    protected void msgDriven( JSONObject infoJson){
        if( infoJson.has("endmove") )        explore(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        //System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        String move   = (String) collisioninfo.get("move");
        System.out.println("RobotApplication | handleCollision move=" + move  );
    }

    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        System.out.println("===================================================="    );
        System.out.println("RobotApplication | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
    }

    protected void doStep(){
        support.forward( forwardMsg);
        delay(1000); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( turnLeftMsg );
        delay(1000); //to avoid too-rapid movement
    }
    protected void turnRight(){
        support.forward( turnRightMsg );
        delay(1000); //to avoid too-rapid movement
    }
    protected void goBack(){
        support.forward( backwardMsg );
        delay(1000); //to avoid too-rapid movement
    }
}
