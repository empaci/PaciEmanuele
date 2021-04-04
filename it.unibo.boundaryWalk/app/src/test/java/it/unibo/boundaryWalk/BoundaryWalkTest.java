package it.unibo.boundaryWalk;

import org.junit.*;
import static org.junit.Assert.*;

public class BoundaryWalkTest {

    private MoveVirtualRobot appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestMoveVirtualRobot | setUp: robot should be at HOME-DOWN ");
        appl = new MoveVirtualRobot();
    }

    @After
    public void  terminate() {
        System.out.println("%%%  TestMoveVirtualRobot |  terminates ");
    }

    @Test
    public void testMoveBoundary() {
        System.out.println("TestMoveVirtualRobot | testMoveBoundary");
        String path = "";
        for(int i=0;i<4;i++) {
            boolean moveFailed = false;
            while (!moveFailed) {
                moveFailed = appl.moveForward(200);
                path = path.concat("w");
            }
            appl.moveLeft(300);
            path = path.concat("l");
        }
        //test se il percorso è corretto
        System.out.println(path);
        assertTrue(path.matches("w*lw*lw*lw*l"));

        int l = 0,w_1 = 0,w_2 = 0,w_3 = 0,w_4 = 0;

        for(char c : path.toCharArray()) {
            if (c=='l') {
                l++;
            } else if (l==0) {
                w_1++;
            } else if (l==1) {
                w_2++;
            } else if (l==2) {
                w_3++;
            }
            else if (l==3) {
                w_4++;
            }
        }
        assertTrue(l==4);
        assertTrue(w_1==w_3);
        assertTrue(w_2==w_4);
    }
}
