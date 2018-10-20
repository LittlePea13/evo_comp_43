# evo_comp_43

#### Run current code with:

javac -cp contest.jar player43.java Individual.java Mutation.java Recombination.java Sampler.java AdaptiveRandomness.java

jar cmf MainClass.txt submission.jar player43.class Individual.class Mutation.class Recombination.class WholeArithRecombination.class SingleNonUniformMutation.class NStepNonUniformMutation.class RouletteSampling.class Sampler.class CauchyMutation.class SimpleArithRecombination.class AdaptiveRandomness.class

 java -Dbulk=false -jar testrun.jar -submission=player43 -evaluation=SphereEvaluation -seed=1
