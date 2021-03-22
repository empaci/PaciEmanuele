package it.unibo.resumableBoundaryWalker.wenv;

import it.unibo.resumableBoundaryWalker.interaction.MsgRobotUtil;
import it.unibo.resumableBoundaryWalker.supports.IssCommSupport;

public class ResumableLogic extends Thread{

    private IssCommSupport rs ;

    private int stepNum              = 1;
    private boolean boundaryWalkDone = false ;
    private boolean usearil;
    private int moveInterval         = 1000;
    private RobotMovesInfo robotInfo;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public ResumableLogic(IssCommSupport support, boolean usearil, boolean doMap){
        rs           = support;
        this.usearil = usearil;
        robotInfo    = new RobotMovesInfo(doMap);
        robotInfo.showRobotMovesRepresentation();
    }

    public ResumableLogic() {

    }

    public void doBoundaryGoon(){
        rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg  );
        delay(moveInterval ); //to reduce the robot move rate
    }

    public void updateMovesRep (String move ) {
        robotInfo.updateRobotMovesRepresentation(move);
    }

    protected void delay( int dt ){
        try { Thread.sleep(dt); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) { // may have changed while waiting to
                    // synchronize on pauseLock
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait(); // will cause this Thread to block until
                            // another thread calls pauseLock.notifyAll()
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) { // running might have changed since we paused
                        break;
                    }
                }
            }
            if (stepNum!=5) {
                String r = rs.requestSynch(MsgRobotUtil.wMsg);
                updateMovesRep("w");
                if (r.equals("false")) {
                    rs.request(MsgRobotUtil.lMsg);
                    delay(moveInterval);
                    updateMovesRep("l");
                    stepNum++;
                }
                robotInfo.showRobotMovesRepresentation();
            } else {
                doStop();
            }
        }
    }

    public void doStop() {
        running = false;
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void doResume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

}
