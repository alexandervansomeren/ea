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
    private double[] best_individual;
    private double best_fitness;
    private int evals;
	
	public player44()
	{
		rnd_ = new Random();
        total_population_ = 20;
        max_value_ = Math.pow(5, 10);
        min_value_ = - max_value_;
        total_genes_ = 10;
        F_ = 1.0;
        CR_ = 0.5;
        best_fitness = Double.NEGATIVE_INFINITY;
        evals = 0;

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
        System.out.println("isMultimodal: " + isMultimodal);
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        System.out.println("hasStructure: " + hasStructure);
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
        System.out.println("isSeparable: " + isSeparable);

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
        double population[][] = init_population();
        // calculate fitness
        double fitness[] = calculate_fitness(population);
        //System.out.println("Fitness: " + Arrays.toString(fitness));
        System.out.println(evaluation_.toString());
        System.out.println("Total evaluations: " + evaluations_limit_);
        while(evals< evaluations_limit_){
            population = differential_evolution(population);
            if(evals % (evaluations_limit_/10) == 0){
                System.out.println(evals + "/" + evaluations_limit_ + " Current best fitness: " + best_fitness);
            }
        }
        System.out.println("best fitness: " + best_fitness);
        System.out.println(evaluation_.getFinalResult());
        System.out.println("best individual: " + Arrays.toString(best_individual));
        //System.out.println(Arrays.toString(calculate_fitness(population)));


	}

	public double[] calculate_fitness(double[][] population){
        double[] fitness = new double[total_population_];
        for(int i = 0; i < total_population_; i++){
            fitness[i] = (double) evaluation_.evaluate(population[i]);
            evals++;
        }
        return fitness;
    }


	public double[][] init_population(){
        double parents[][] = new double[total_population_][total_genes_];
        for(int j = 0; j<total_population_; j++) {
            for (int i = 0; i < total_genes_; i++) {
                parents[j][i] = min_value_ + (max_value_ - min_value_) * this.rnd_.nextDouble();
            }
            //System.out.println("Initial population: " + Arrays.toString(parents[j]));
        }
        return parents;
    }

    public double[][] differential_evolution(double[][] population){
        int min = 0;
        int max = total_population_;

        // The individual to be mutated
        int individual_x = rnd_.nextInt(max); // return int between 0 and total_population_

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

        double[] parent = population[individual_x];
        double[] child = parent.clone();

        for(int i = 0; i < total_genes_; i++){
            if(i == gene_index || rnd_.nextDouble() < CR_ ){
                child[i] = population[individual_a][i] + F_ *(population[individual_b][i] - population[individual_c][i]);
            }
        }

        double parent_fitness = (double) evaluation_.evaluate(parent);
        evals++;
        //System.out.println("Evals: " + evals);
        double child_fitness = (double) evaluation_.evaluate(child);
        evals++;


        if(child_fitness > parent_fitness){
            //System.out.println("+++++ Child is better than parent, child: " + child_fitness + " Parent: " + parent_fitness);
            population[individual_x] = child;
        }else{
            //System.out.println("----- Child is worse than parent, child: " + child_fitness + " Parent: " + parent_fitness);
        }

        if(child_fitness > best_fitness){
            best_fitness = child_fitness;
            best_individual = child;
        }



        return population;
    }

	//public static void main(String[] vars){
	//	System.out.println("Running Evolutionary Algorithms!");
	//	player44 player = new player44();
    //    player.setEvaluation(new RastriginEvaluation());
	//	player.run();
	//}
}




