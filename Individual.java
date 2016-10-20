import org.vu.contest.ContestEvaluation;

/**
 * Created by Wolf on 17-Oct-16.
 */
public class Individual {
    public double[] genotype;
    public double fitness;
    private ContestEvaluation evaluation_;
    public int evals;
    public int stall;
    //public double F_;
    ///public double CR_;

    //constructor
    public Individual(double[] genotype, ContestEvaluation evaluation, int evals) {
        this.genotype = genotype;
        this.evaluation_ = evaluation;
        this.evals = evals;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.stall = 0;
        this.calculate_fitness();
    }
    public Individual(double[] genotype, double fitness, ContestEvaluation evaluation, int evals) {
        this.genotype = genotype;
        this.evaluation_ = evaluation;
        this.evals = evals;
        this.stall = 0;
        this.fitness = fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    public void setGenotype(double[] genotype) {
        this.genotype = genotype;
    }
    public double getFitness() {
        return this.fitness;
    }
    public double[] getGenotype() {
        return this.genotype;
    }
    public double getStall(){return this.stall; }
    public void increaseStall(){this.stall++;}

    public void calculate_fitness(){
        this.fitness = (double) this.evaluation_.evaluate(this.genotype);
        evals++;
    }

}
