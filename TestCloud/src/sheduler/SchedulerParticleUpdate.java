package sheduler;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
import util.Constants;

public class SchedulerParticleUpdate extends ParticleUpdate {
//    private static final double W = 0.9;
    private static final double C2 = 2.0;
    private static final double C1 = 2.0;

    SchedulerParticleUpdate(Particle particle) {
        super(particle);
    }

    @Override
    public void update(Swarm swarm, Particle particle) {
        double[] v = particle.getVelocity();
        double[] x = particle.getPosition();
        double[] pbest = particle.getBestPosition();
        double[] gbest = swarm.getBestPosition();

        for (int i = 0; i < Constants.cloudletSize; ++i) {
            
            // v(t + 1) = v(t) + c1*r1(LB - x(t)) + c2*r2(GB - x(t))
            v[i] = v[i] + C1 * Math.random() * (pbest[i] - x[i]) + C2 * Math.random() * (gbest[i] - x[i]);
            x[i] = (int) (x[i] + v[i]);
        }
    }
}