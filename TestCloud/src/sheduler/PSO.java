/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sheduler;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.Swarm;
import util.Constants;

/**
 *
 * @author User
 */
public class PSO {
    
    private static Swarm swarm;
    private static Particle particle[];
    private static SchedulerFitnessFunction ff = new SchedulerFitnessFunction();
    public PSO(){
        initParticles();
    }

    public static double[] run(){
        swarm = new Swarm(Constants.POPULATION_SIZE, new PsoParticles(), ff);
        swarm.setMaxMinVelocity(0.5);
        swarm.setMaxPosition(Constants.vmCount-1);
        swarm.setMinPosition(0);
        swarm.setParticles(particle);
        swarm.setParticleUpdate(new SchedulerParticleUpdate(new PsoParticles()) );
        
        for(int i = 0; i< Constants.iteration; i++ ){
            swarm.evolve();
            if (i % 10 == 0) {
                System.out.printf("Gloabl best at iteration (%d): %f\n", i, swarm.getBestFitness());
            }
        }
        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

        System.out.println("The best solution is: ");
        PsoParticles bestParticle = (PsoParticles) swarm.getBestParticle();
        System.out.println(bestParticle.toString());
        
        return swarm.getBestPosition();
    }
    
    private void initParticles() {
        particle = new PsoParticles[Constants.POPULATION_SIZE];
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            particle[i] = new PsoParticles();
        }
    }
    public Particle printBestFitness() {
        System.out.println("\nBest fitness value: " + swarm.getBestFitness() +
                "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
        System.out.println("Best Cost : " + ff.calcCostPerTime( swarm.getBestParticle().getBestPosition() ) );
        return  swarm.getBestParticle();
    }
    
}
