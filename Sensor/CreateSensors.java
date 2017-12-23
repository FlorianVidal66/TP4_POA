package Sensor;

import Filter.Filter1;
import Filter.Filter2;

public class CreateSensors {

    /**
     * SCENARIO :
     *      Sensor.Sensor 1 -------------------------------> Server.Server
     *      Sensor.Sensor 2 ---> Filter 1 -----------------> Server.Server
     *      Sensor.Sensor 3 ---> Filter 1 ---> Filter 2 ---> Server.Server
     */

    public static void main(String[] args){
        Sensor s1 = new Sensor(1,100,2000,"lol", "localhost",6666);
        Sensor s2 = new Sensor(2,200,5000,"lol", "localhost",6666);
        Sensor s3 = new Sensor(3,180,10000,"lol", "localhost",6666);

        Filter1 f1_2 = new Filter1(1234, "localhost",6666);
        s2.connectionFromFilter("localhost", 1234);

        Filter1 f1_3 = new Filter1(1235, "localhost",6666);
        Filter2 f2_3 = new Filter2(5321, "localhost",6666);
        s3.connectionFromFilter("localhost",1235);
        f1_3.connectionFromFilter("localhost", 5321);

        s1.start();

        s2.start();
        f1_2.start();

        s3.start();
        f1_3.start();
        f2_3.start();
    }
}
