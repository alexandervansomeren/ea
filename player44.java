import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Arrays;
import java.util.Random;
import java.util.Properties;

public class player44 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private int total_population_;
    private double min_value_;
    private double max_value_;
    private int total_genes_;
    private double F_; //differential weight [0,2]
    private double CR_; //crossover probability [0,1]
    public int evals;
    private int stall_restart_divider_;
    private int stall_minimum_;
    private boolean random_restarts_;

    public player44()
    {
        rnd_ = new Random();
        total_population_ = 100;
        max_value_ = 5.0;
        min_value_ = - max_value_;
        total_genes_ = 10;
        F_ = 0.5;
        CR_ = 0.1;
        evals = 0;
        stall_restart_divider_ = 500;
        stall_minimum_ = 10;
        random_restarts_ = true;
    }

    public void setSeed(long seed)
    {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation)
    {
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        //System.out.println("isMultimodal: " + isMultimodal);
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        //System.out.println("hasStructure: " + hasStructure);
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
        //System.out.println("isSeparable: " + isSeparable);

        // Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }

    public void run()
    {
        // init population
        Individual population[] = init_population();
        evals += total_population_;
        //print_population(population);

        System.out.println(evaluation_.toString());
        System.out.println("Total evaluations: " + evaluations_limit_);
        while(evals < evaluations_limit_){
            population = differential_evolution(population);
            evals++;
            if(evals % (evaluations_limit_/10) == 0){
                System.out.println(evals + "/" + evaluations_limit_ + " Current best fitness: " + evaluation_.getFinalResult());
            }
        }
        System.out.println("best fitness: " + evaluation_.getFinalResult());
        System.out.println(evaluation_.getFinalResult());
        //System.out.println("best individual: " + Arrays.toString(best_individual.getGenotype()));
    }

    public void print_population(Individual[] population){
        for(int i = 0; i < total_population_; i++){
            System.out.println(i + " " + Arrays.toString(population[i].getGenotype()) + ": " +population[i].getFitness());
        }
    }


    public Individual[] init_population(){
        Individual population[] = new Individual[total_population_];
        for(int j = 0; j<total_population_; j++) {
            population[j] = random_individual();
            //System.out.println("Initial population: " + Arrays.toString(parents[j]));
        }
        return population;
    }

    public Individual random_individual(){
        double[] genotype = new double[total_genes_];

        for (int i = 0; i < total_genes_; i++) {
            genotype[i] = min_value_ + (max_value_ - min_value_) * this.rnd_.nextDouble();
        }
        Individual individual = new Individual(genotype, this.evaluation_, this.evals);
        return individual;
    }


    public Individual[] differential_evolution(Individual[] population){
        int max = total_population_;

        // The individual to be mutated
        int individual_x = rnd_.nextInt(max); // return int between 0 and total_population_

        if(this.random_restarts_ == true && population[individual_x].getStall() >= evals/this.stall_restart_divider_ && population[individual_x].getStall() >= this.stall_minimum_){
            population[individual_x] = random_individual();
            System.out.println("New random individual created!" + evals/this.stall_restart_divider_);
            return population;

        }

        // The different parent/mutation individuals
        int individual_a = -1;
        int individual_b = -1;
        int individual_c = -1;

        do {
            individual_a = rnd_.nextInt(max); // return int between 0 and total_population_
        }while(individual_x == individual_a);
        do {
            individual_b = rnd_.nextInt(max); // return int between 0 and total_population_
        }while(individual_x == individual_b || individual_a == individual_b);
        do {
            individual_c = rnd_.nextInt(max); // return int between 0 and total_population_
        }while(individual_x == individual_c || individual_a == individual_c || individual_b == individual_c);
        //System.out.println(String.format("selected individuals: %d %d %d %d", individual_x, individual_a, individual_b, + individual_c));

        int gene_index = rnd_.nextInt(total_genes_);

        //System.out.println(gene_index);

        Individual parent = population[individual_x];
        Individual child = new Individual(parent.getGenotype().clone(), parent.getFitness(), this.evaluation_, this.evals);

        double[] child_genotype = child.getGenotype();
        for(int i = 0; i < total_genes_; i++){
            if(i == gene_index || rnd_.nextDouble() < CR_ ){
                child_genotype[i] = population[individual_a].getGenotype()[i] + F_ *(population[individual_b].getGenotype()[i] - population[individual_c].getGenotype()[i]);
            }
        }

        child.calculate_fitness();

        if(child.getFitness() > parent.getFitness()){
            //System.out.println(evals + "+++++ Child is better than parent, child: " + child.getFitness() + " Parent: " + parent.getFitness());
            population[individual_x] = child;
        }else{
            parent.increaseStall();
            //System.out.println("----- Child is worse than parent, child: " + child.getFitness() + " Parent: " + parent.getFitness() + " Stall: " + parent.getStall());
        }

//        if(child.getFitness() > best_individual.getFitness()){
//            best_individual = new Individual(child.getGenotype(), child.getFitness());
//        }

        return population;
    }

//    public static void main(String[] vars){
//    	System.out.println("Running Evolutionary Algorithms!");
//        player44 player = new player44();
//        player.setEvaluation(new FletcherPowellEvaluation());
//    	player.run();
//    }
}




