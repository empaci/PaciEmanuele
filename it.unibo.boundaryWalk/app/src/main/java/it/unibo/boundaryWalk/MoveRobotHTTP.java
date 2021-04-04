package it.unibo.boundaryWalk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MoveRobotHTTP {

    public final static String url = "http://localhost:8090/api/move";

    public boolean move(String json_string) {
        boolean result = false;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(json_string, "application/json","UTF-8");
            httppost.setEntity(requestEntity);
            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println(responseString);
                result = responseString.contains("true");
            }
        } catch (Exception e) {
        }
        return result;
    }

    public Boolean left(String time) {
        String json = "{\"robotmove\":\"turnLeft\", \"time\":" + time + "}";
        return this.move(json);
    }

    public Boolean right(String time) {
        String json = "{\"robotmove\":\"turnRight\", \"time\":" + time + "}";
        return this.move(json);
    }

    public Boolean forward(String time) {
        String json = "{\"robotmove\":\"moveForward\", \"time\":" + time + "}";
        return this.move(json);
    }

    public Boolean backward(String time) {
        String json = "{\"robotmove\":\"moveBackward\", \"time\":" + time + "}";
        return this.move(json);
    }

    public static void main(String[] args)  {
        MoveRobotHTTP robot = new MoveRobotHTTP();
        robot.backward("300");
        boolean res = robot.forward("600");
        System.out.println(res);
    }

}
