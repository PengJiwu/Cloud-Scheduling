/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sheduler;

import java.util.Random;
import net.sourceforge.jswarm_pso.Particle;
import util.Constants;

/**
 *
 * @author User
 */
public class PsoParticles extends Particle{
    public static int[] map = new int[Constants.cloudletSize+Constants.vmCount]; 
    public PsoParticles(){
        super(Constants.cloudletSize);
        double[] position = new double[Constants.cloudletSize];
        double[] velocity = new double[Constants.cloudletSize];

        for (int i = 0; i < Constants.cloudletSize; i++) {
            Random randObj = new Random();
            position[i] = randObj.nextInt(Constants.vmCount);
            velocity[i] = Math.random();
        }
        setPosition(position);
        setVelocity(velocity);
    }
    
    
    @Override
    public String toString() {
        String output = "";
        int k =0;
        for (int i = 0; i < Constants.vmCount; i++) {
            String tasks = "";
            int no_of_tasks = 0;
            for (int j = 0; j < Constants.cloudletSize; j++) {
                if (i == (int) getPosition()[j]) {
                    tasks += (tasks.isEmpty() ? "" : " ") + j;
                    map[k] = j;
                    k++;
                    ++no_of_tasks;
                }
                
            }
            map [k] = -1;
            k++;
            if (tasks.isEmpty()) output += "There is no tasks associated to Virtual mechine " + i + "\n";
            else
                output += "There are " + no_of_tasks + " tasks associated to Virtual Mechine " + i + " and they are " + tasks + "\n";
        }
        return output;
    }
}
